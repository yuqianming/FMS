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
import com.mvc.service.report.CancelReportServiceI;
import com.mvc.service.report.CostlReportServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 成本单账龄分析Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/costReport")
public class CostReportController extends BaseController{

	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	
	@Autowired
	private MenuServiceI menuService;
	
	@Autowired
	private CostlReportServiceI costlReportService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/report/costReport";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(String startMonth,String endMonth,boolean no_cancel,String asset_trans_sts,String assemble_sts,String doc_type,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			Map<String,Object> result=costlReportService.dataGrid(startMonth,endMonth,no_cancel,asset_trans_sts,assemble_sts,doc_type);
			List<Map<String,Object>> viewList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(viewList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(String startMonth,String endMonth,boolean no_cancel,String asset_trans_sts,String assemble_sts,String doc_type,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=costlReportService.dataGrid(startMonth,endMonth,no_cancel,asset_trans_sts,assemble_sts,doc_type);
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("成本单账龄分析.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"org_name","build_mode","account_amt0","account_amt1","account_amt2","account_amt3","account_amt4","account_amt5"};
			String[] columnNames={"公司名称","建设方式","0~3月","4~6月","7~9月","10~12月","13~24月","25月以上"};
			String[] cellTypes={"String","String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("costReport_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("costReport_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
