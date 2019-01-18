package com.mvc.controller.report;

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
import com.mvc.service.report.PaymentReportServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 已付款情况统计Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/paymentReport")
public class PaymentReportController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	
	@Autowired
	private MenuServiceI menuService;
	
	@Autowired
	private PaymentReportServiceI paymentReportService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/report/paymentReport";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(String startMonth,String endMonth,String type,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			Map<String,Object> result=paymentReportService.dataGrid(startMonth,endMonth,type,ph);
			List<Map<String,Object>> viewList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(viewList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(String startMonth,String endMonth,String type,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=paymentReportService.dataGrid(startMonth,endMonth,type,null);
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("已付款情况统计.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"group_name","pay_num","pay_amt","sign_num","sign_amt"};
			String[] columnNames={"分公司/供应商","个数","金额","个数","付款金额（不含税）"};
			String[] cellTypes={"String","BigDecimal","BigDecimal","BigDecimal","BigDecimal"};
			String[] rangeNames={"分公司/供应商","已付款项目","已签字未付款项目"};
			String[] rangeCells={"0,1,0,0","0,0,1,2","0,0,3,4"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			info.setRangeNames(rangeNames);
			info.setRangeCells(rangeCells);
			String filePath=ExportUtils.exportExcelHasRang(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("payReport_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("payReport_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
