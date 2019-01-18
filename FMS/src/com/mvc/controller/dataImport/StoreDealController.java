package com.mvc.controller.dataImport;

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
import com.mvc.model.sys.TStoreDeal;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.service.dataImport.StoreDealServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 仓库交易日志Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/deal")
public class StoreDealController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private StoreDealServiceI storeDealService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataImport/deal";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TStoreDeal info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(storeDealService.dataGrid(info,ph));
			grid.setTotal(storeDealService.count(info,ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(HttpSession session,HttpServletRequest request,TStoreDeal sd) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			sd.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> testList=storeDealService.dataGrid(sd,null);
			ExcelInfo info=new ExcelInfo();
			info.setFileName("仓储交易日志表.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"project_code","project_name","order_code","batch_no","material_code","material_name","busi_type","opration_type","supplier_code","supplier_name","quantity_org","quantity_txn","quantity_left","total_amt_e_tax","total_amt_i_tax","create_time","is_check","remark"};
			String[] columnNames={"项目编号","项目名称","采购订单号","批次号","PMS物料编码","物料名称","业务类型","操作类型","供应商编码","供应商名称","交易前数量","交易数量","交易后数量","总金额（产品净额）","含税总金额（产品净额+税额）","创建时间","是否进行明细核对","备注"};
			String[] cellTypes={"String","String","String","String","String","String","String","String","String","String","String","String","String","BigDecimal","BigDecimal","String","String","String"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,testList);
			CacheUtils.cacheMe("signature_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("signature_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
