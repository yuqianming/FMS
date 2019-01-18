package com.mvc.controller.dataCheck;

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
import com.mvc.service.dataCheck.CancelProjectServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 已销项检查Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/cancelProjPay")
public class CancelProjectController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private CancelProjectServiceI cancelProjectService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataCheck/cancelProjPay";
	}
	
	@RequestMapping("/cancelProjGrid")
	@ResponseBody
	public Grid cancelProjGrid(boolean hide,boolean cancelStock,boolean noMaterial,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=cancelProjectService.cancelProjGrid(hide,cancelStock,noMaterial,ph,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(mapList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
			//grid.setFooter(cancelProjectService.cancelProjFooter(hide,ph));
			BigDecimal account_amt=new BigDecimal(0);
			BigDecimal pay_amt_e_tax=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				account_amt=account_amt.add((BigDecimal) map.get("account_amt"));
				pay_amt_e_tax=pay_amt_e_tax.add((BigDecimal) map.get("pay_amt_e_tax"));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("account_amt_txt", df.format(account_amt));
			map.put("pay_amt_e_tax_txt",df.format(pay_amt_e_tax));
			map.put("project_code","合计");
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			footer.add(map);
			grid.setFooter(footer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	@RequestMapping("/supplierGrid")
	@ResponseBody
	public Grid supplierGrid(String project_code,String supplier_name,boolean is_all,boolean cancelStock,boolean noMaterial,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=cancelProjectService.supplierGrid(project_code,supplier_name,is_all,cancelStock,noMaterial,ph,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(mapList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
			//grid.setFooter(cancelProjectService.supplierFooter(project_code,is_all,ph));
			BigDecimal pay_amt_e_tax=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				pay_amt_e_tax=pay_amt_e_tax.add((BigDecimal) map.get("pay_amt_e_tax"));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("pay_amt_e_tax_txt",df.format(pay_amt_e_tax));
			map.put("month","合计");
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			footer.add(map);
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
	public Json createExcel(boolean hide,boolean cancelStock,boolean noMaterial,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=cancelProjectService.cancelProjGrid(hide,cancelStock,noMaterial,null,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("已销项付款检查.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"project_code","supplier_name","account_amt","pay_amt_e_tax"};
			String[] columnNames={"已销项目编号","供应商名称","成本单金额","已付款（不含税）"};
			String[] cellTypes={"String","String","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("cacenlProjGrid_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("cacenlProjGrid_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
	@RequestMapping(value = "/createExcelDetail") 
	@ResponseBody
	public Json createExcelDetail(String project_code,String supplier_name,boolean is_all,boolean cancelStock,boolean noMaterial,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=cancelProjectService.supplierGrid(project_code,supplier_name,is_all,cancelStock,noMaterial,null,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("已销项付款明细.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"month","voucher_no","supplier_name","project_code","order_code","account_name","pay_amt_e_tax"};
			String[] columnNames={"月份","凭证号","供应商名称","已销项目编号","订单编号","科目名称","已付款（不含税）"};
			String[] cellTypes={"String","String","String","String","String","String","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("cacenlProjGridDetail_"+sessionInfo.getId(), filePath);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportExcelDetail") 
	@ResponseBody
	public void exportExcelDetail(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("cacenlProjGridDetail_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
