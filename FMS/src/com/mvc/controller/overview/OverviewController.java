package com.mvc.controller.overview;

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
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.pageModel.sys.OverView;
import com.mvc.service.overview.OverviewServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 项目总览Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/overview")
public class OverviewController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private OverviewServiceI overviewService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/project/overview";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(OverView info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			Map<String,Object> result=overviewService.dataGrid(info,ph);
			List<Map<String,Object>> viewList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(viewList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			BigDecimal account_amt=new BigDecimal(0);
			//BigDecimal total_amt_i_tax=new BigDecimal(0);
			BigDecimal t_pay_amt=new BigDecimal(0);
			BigDecimal n_pay_amt=new BigDecimal(0);
			BigDecimal t_invoice_amt=new BigDecimal(0);
			BigDecimal t_invoice_amt_e_tax=new BigDecimal(0);
			BigDecimal s_pay_amt=new BigDecimal(0);
			for(Map<String,Object> view:viewList)
			{
				account_amt=account_amt.add(new BigDecimal(new DecimalFormat().parse(view.get("account_amt").toString()).doubleValue()));
				//total_amt_i_tax=total_amt_i_tax.add(over.getTotal_amt_i_tax());
				t_pay_amt=t_pay_amt.add(new BigDecimal(new DecimalFormat().parse(view.get("t_pay_amt").toString()).doubleValue()));
				s_pay_amt=s_pay_amt.add(new BigDecimal(new DecimalFormat().parse(view.get("s_pay_amt").toString()).doubleValue()));
				n_pay_amt=n_pay_amt.add(new BigDecimal(new DecimalFormat().parse(view.get("n_pay_amt").toString()).doubleValue()));
				t_invoice_amt=t_invoice_amt.add(new BigDecimal(new DecimalFormat().parse(view.get("t_invoice_amt").toString()).doubleValue()));
				t_invoice_amt_e_tax=t_invoice_amt_e_tax.add(new BigDecimal(new DecimalFormat().parse(view.get("t_invoice_amt_e_tax").toString()).doubleValue()));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> temp=new HashMap<String,Object>();
			temp.put("project_code", "合计");
			temp.put("account_amt", df.format(account_amt));
			temp.put("t_pay_amt", df.format(t_pay_amt));
			temp.put("s_pay_amt", df.format(s_pay_amt));
			temp.put("n_pay_amt", df.format(n_pay_amt));
			temp.put("t_invoice_amt", df.format(t_invoice_amt));
			temp.put("t_invoice_amt_e_tax", df.format(t_invoice_amt_e_tax));
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
	public Json createExcel(HttpSession session,HttpServletRequest request,OverView overView) {
		Json j = new Json();
		try {
			System.out.println("###### d导出Excel开始 ："+new Date());
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			overView.setOrg_id(sessionInfo.getOrgId());
			Map<String,Object> result=overviewService.dataGrid(overView,null);
			List<Map<String,Object>> viewList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("整体浏览.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"project_code","address_code","address_name","account_name","supplier_name","account_amt","t_pay_amt","n_pay_amt","t_invoice_amt","t_invoice_amt_e_tax"};
			String[] columnNames={"项目编号","站址编号","站址名称","科目名称","供应商名称","入账金额","已付款不含税金额","未付款金额","已开发票","开票不含税金额"};
			String[] cellTypes={"String","String","String","String","String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			/*List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
			for(OverView test:testList)
			{
				Map<String,Object> map=BeanToMapUtils.toMap(test);
				mapList.add(map);
			}*/
			String filePath=ExportUtils.exportExcel(info,viewList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("overview_"+sessionInfo.getId(), filePath);
			System.out.println("###### d导出Excel结束 ："+new Date());
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
		String filePath=(String) CacheUtils.getCache("overview_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
}
