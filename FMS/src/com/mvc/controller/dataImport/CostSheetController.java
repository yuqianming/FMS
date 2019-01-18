package com.mvc.controller.dataImport;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.CostInfo;
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.service.dataImport.CostSheetServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

@Controller
@RequestMapping("/costSheet")
public class CostSheetController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private CostSheetServiceI costSheetService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataImport/cost";
	}
	
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(CostInfo costinfo,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			costinfo.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=costSheetService.dataGrid(costinfo,ph);
			grid.setRows(mapList);
			grid.setTotal(costSheetService.count(costinfo,ph));
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			BigDecimal account_amt=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				account_amt=account_amt.add(new BigDecimal(new DecimalFormat().parse(map.get("account_amt").toString()).doubleValue()));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> temp=new HashMap<String,Object>();
			temp.put("busi_date", "合计");
			temp.put("account_amt", df.format(account_amt));
			footer.add(temp);
			grid.setFooter(footer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(HttpSession session,CostInfo costinfo,HttpServletRequest request) {
		Json j = new Json();
		try {
			System.out.println("导出成本单Excel开始："+new Date());
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			costinfo.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> testList=costSheetService.dataGrid(costinfo,null);
			System.out.println("导出成本单数据量："+testList.size());
			ExcelInfo info=new ExcelInfo();
			info.setFileName("成本单.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={ "list_id","busi_date","order_code","source_type","supplier_name","supplier_code","material_code","manufacturer","spec_code","material_name","service_code","unit","quantity","unit_price_e_tax","total_amt_e_tax","period_cost_amt","account_amt","project_code","project_name","address_code","address_name","accounting_org","doc_type","documentary","assemble_sts","allocation_sts","asset_trans_sts","timestamp","account_code","account_name","order_type","voucher_no","detail_no"};
			String[] columnNames={"清单ID","业务日期","采购订单号","订单来源类型","供应商名称","供应商编号","物料编码","生产厂家","规格编号","物料名称","物资服务编码","计量单位","数量","不含税单价","不含税总金额","当期成本确认金额","入账金额","项目编码","项目名称","站址编码","站址名称","核算组织","单据类型","制单人","是否装配","是否分摊","是否转资","时间戳","科目编码","科目名称","订单类型","凭证编号","交易明细行号"  };
			String[] cellTypes={"String","String","String","String","String","String","String","String","String","String","String","String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","String","String","String","String","String","String","String","String","String","String","Date","String","String","String","String","String"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,testList);
			CacheUtils.cacheMe("cost_"+sessionInfo.getId(), filePath);
			System.out.println("导出成本单Excel结束："+new Date());
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportExcel") 
	@ResponseBody
	public void exportExcel(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("cost_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
}
