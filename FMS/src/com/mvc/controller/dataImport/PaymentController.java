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
import com.mvc.model.sys.TPayment;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dataImport.PaymentServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;
/**
 * 已付款Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/payment")
public class PaymentController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private PaymentServiceI paymentService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataImport/pay";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TPayment info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=paymentService.dataGrid(info,ph);
			grid.setRows(mapList);
			grid.setTotal(paymentService.count(info,ph));
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			BigDecimal invoice_amt=new BigDecimal(0);
			BigDecimal invoice_amt_e_tax=new BigDecimal(0);
			BigDecimal pay_amt=new BigDecimal(0);
			BigDecimal pay_amt_e_tax=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				invoice_amt=invoice_amt.add(new BigDecimal(new DecimalFormat().parse(map.get("invoice_amt_txt").toString()).doubleValue()));
				invoice_amt_e_tax=invoice_amt_e_tax.add(new BigDecimal(new DecimalFormat().parse(map.get("invoice_amt_e_tax_txt").toString()).doubleValue()));
				pay_amt=pay_amt.add(new BigDecimal(new DecimalFormat().parse(map.get("pay_amt_txt").toString()).doubleValue()));
				pay_amt_e_tax=pay_amt_e_tax.add(new BigDecimal(new DecimalFormat().parse(map.get("pay_amt_e_tax_txt").toString()).doubleValue()));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> temp=new HashMap<String,Object>();
			temp.put("project_code", "合计");
			temp.put("invoice_amt_txt", df.format(invoice_amt));
			temp.put("invoice_amt_e_tax_txt", df.format(invoice_amt_e_tax));
			temp.put("pay_amt_txt", df.format(pay_amt));
			temp.put("pay_amt_e_tax_txt", df.format(pay_amt_e_tax));
			footer.add(temp);
			grid.setFooter(footer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String ids,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			paymentService.delete(ids,sessionInfo.getOrgId());
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(TPayment info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setOrg_id(sessionInfo.getOrgId());
			info.setUpdate_time(new Date());
			paymentService.edit(info);
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(HttpSession session,TPayment pay,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			pay.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> testList=paymentService.dataGrid(pay,null);
			ExcelInfo info=new ExcelInfo();
			info.setFileName("已付款.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"project_code","supplier_code","supplier_name","order_code","account_name","address_code","address_name","invoice_amt","invoice_amt_e_tax","pay_amt","pay_amt_e_tax","month","voucher_no","online_offline","settle_no","remark","tax_rate"};
			String[] columnNames={"项目编号","供应商编码","供应商名称","采购订单号","科目名称","站址编码","站址名称","本次发票","本次发票净额","本次付款","本次付款净额","日期","凭证号","线上线下","结算单号","备注","税率"};
			String[] cellTypes={"String","String","String","String","String","String","String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","String","String","String","String","String","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,testList);
			CacheUtils.cacheMe("payment_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("payment_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
	@RequestMapping("/upload")
	@ResponseBody
	public Json upload(UploadInfo info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			if(sessionInfo!=null)
			{
				info.setUserId(sessionInfo.getId());
				info.setOrgId(sessionInfo.getOrgId());
			}
			paymentService.upload(info);
			j.setSuccess(true);
			j.setMsg("上传成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
