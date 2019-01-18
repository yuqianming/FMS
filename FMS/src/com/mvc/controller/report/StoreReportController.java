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
import com.mvc.service.report.StoreReportServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 仓储核对汇总Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/storeReport")
public class StoreReportController  extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	
	@Autowired
	private MenuServiceI menuService;
	
	@Autowired
	private StoreReportServiceI storeReportService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/report/storeReport";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(String startMonth,String endMonth,boolean no_out,boolean no_in,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			Map<String,Object> result=storeReportService.dataGrid(startMonth,endMonth,no_out,no_in,ph);
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
	public Json createExcel(String startMonth,String endMonth,boolean no_out,boolean no_in,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=storeReportService.dataGrid(startMonth,endMonth,no_out,no_in,null);
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("仓储核对汇总.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"org_name","deal_total","deal_rk","deal_ck","deal_ckhg","deal_tk","deal_cz","store_total","store_rk","store_ck","store_ckhg","store_tk","store_cz","dvalue_total","dvalue_rk","dvalue_ck","dvalue_ckhg","dvalue_tk","dvalue_cz"};
			String[] columnNames={"分公司","合计","入库","出库","出库回滚","退库 ","其他","合计","入库","出库","出库回滚","退库 ","其他","合计","入库","出库","出库回滚","退库 ","其他"};
			String[] cellTypes={"String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal"};
			String[] rangeNames={"分公司","核算系统","仓储系统","差异"};
			String[] rangeCells={"0,1,0,0","0,0,1,6","0,0,7,12","0,0,13,18"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			info.setRangeNames(rangeNames);
			info.setRangeCells(rangeCells);
			String filePath=ExportUtils.exportExcelHasRang(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("storeReport_"+sessionInfo.getId(), filePath);
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
		String filePath=(String) CacheUtils.getCache("storeReport_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}

}
