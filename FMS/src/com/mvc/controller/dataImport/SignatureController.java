package com.mvc.controller.dataImport;

import java.util.Date;
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
import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.service.dataImport.SignatureServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;
/**
 * 签字未付款Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/signature")
public class SignatureController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private SignatureServiceI signatureService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataImport/sign";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TSignature info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(signatureService.dataGrid(info,ph));
			grid.setTotal(signatureService.count(info,ph));
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
			signatureService.delete(ids,sessionInfo.getOrgId());
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(TSignature info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setOrg_id(sessionInfo.getOrgId());
			info.setUpdate_time(new Date());
			signatureService.edit(info);
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
	public Json createExcel(HttpSession session,HttpServletRequest request,TSignature ts) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			ts.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> testList=signatureService.dataGrid(ts,null);
			ExcelInfo info=new ExcelInfo();
			info.setFileName("签字未付款.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"batch_no","project_code","supplier_code","supplier_name","order_code","account_name","address_code","address_name","invoice_amt","invoice_amt_e_tax","pay_amt","pay_amt_e_tax","remark","tax_rate"};
			String[] columnNames={"批次号","项目编号","供应商编码","供应商名称","采购订单号","科目名称","站址编码","站址名称","本次发票","本次发票净额","本次付款","本次付款净额","备注","税率"};
			String[] cellTypes={"String","String","String","String","String","String","String","String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","String","BigDecimal"};
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
