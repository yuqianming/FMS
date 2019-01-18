package com.mvc.controller.dataCheck;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import com.mvc.service.dataCheck.OverPayCheckServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 超额付款检查Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/overPay")
public class OverPayCheckController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private OverPayCheckServiceI overPayCheckService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataCheck/overPayCheck";
	}
	
	@RequestMapping("/supplierGrid")
	@ResponseBody
	public Grid supplierGrid(boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=overPayCheckService.supplierGrid(cancelStock,cancelAudit,noMaterial,deviation,ph,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(mapList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
			//overPayCheckService.supplierCount(cancelStock,cancelAudit,noMaterial,deviation,ph,sessionInfo.getOrgId())
			//grid.setFooter(overPayCheckService.supplierFooter(cancelStock,cancelAudit,ph));
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
			map.put("dvalue", df.format(account_amt.subtract(pay_amt_e_tax)));
			map.put("supplier_name","合计");
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
	
	@RequestMapping("/payGrid")
	@ResponseBody
	public Grid payGrid(String supplier_name,String project_code,boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=overPayCheckService.payGrid(supplier_name,project_code,is_all,cancelStock,cancelAudit,noMaterial,deviation,ph,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(mapList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
			//overPayCheckService.payCount(supplier_name,project_code,is_all,cancelStock,cancelAudit,noMaterial,deviation,ph,sessionInfo.getOrgId())
			//grid.setFooter(overPayCheckService.payFooter(supplier_name,project_code,is_all,ph));
			BigDecimal invoice_amt=new BigDecimal(0);
			BigDecimal pay_amt=new BigDecimal(0);
			BigDecimal pay_amt_e_tax=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				invoice_amt=invoice_amt.add((BigDecimal) map.get("invoice_amt"));
				pay_amt=pay_amt.add((BigDecimal) map.get("pay_amt"));
				pay_amt_e_tax=pay_amt_e_tax.add((BigDecimal) map.get("pay_amt_e_tax"));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("invoice_amt_txt", df.format(invoice_amt));
			map.put("pay_amt_e_tax_txt",df.format(pay_amt_e_tax));
			map.put("pay_amt_txt", df.format(pay_amt));
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
	public Json createExcel(boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=overPayCheckService.supplierGrid(cancelStock,cancelAudit,noMaterial,deviation,null,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("超额付款检查.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"supplier_name","project_code","account_amt","pay_amt_e_tax","dvalue"};
			String[] columnNames={"供应商名称","项目编号","成本单金额","已付款（不含税）","成本单减付款金额"};
			String[] cellTypes={"String","String","BigDecimal","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("supplierGrid_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("supplierGrid_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
	@RequestMapping(value = "/createExcelDetail") 
	@ResponseBody
	public Json createExcelDetail(String supplier_name,String project_code,boolean is_all,boolean cancelStock,boolean cancelAudit,boolean noMaterial,BigDecimal deviation,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=overPayCheckService.payGrid(supplier_name,project_code,is_all,cancelStock,cancelAudit,noMaterial,deviation,null,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("超额付款明细.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"month","voucher_no","account_name","order_code","invoice_amt","pay_amt","pay_amt_e_tax"};
			String[] columnNames={"付款日期","凭证号","科目名称","订单编号","发票金额","本次付款","本次付款（不含税）"};
			String[] cellTypes={"String","String","String","String","BigDecimal","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("payGrid_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("payGrid_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
