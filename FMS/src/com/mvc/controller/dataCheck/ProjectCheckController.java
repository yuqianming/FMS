package com.mvc.controller.dataCheck;

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
import com.mvc.pageModel.sys.SearchInfo;
import com.mvc.service.dataCheck.ProjectCheckServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 项目数据核对Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/projectCheck")
public class ProjectCheckController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private ProjectCheckServiceI projectCheckService;
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataCheck/project";
	}
	
	@RequestMapping("/poolGrid")
	@ResponseBody
	public Grid poolGrid(HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(projectCheckService.poolGrid(sessionInfo.getOrgId()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	@RequestMapping("/monthGrid")
	@ResponseBody
	public Grid monthGrid(SearchInfo info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(projectCheckService.monthGrid(info,ph,sessionInfo.getOrgId()));
			grid.setTotal(projectCheckService.monthCount(info,ph,sessionInfo.getOrgId()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	@RequestMapping("/categoryGrid")
	@ResponseBody
	public Grid categoryGrid(SearchInfo info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(projectCheckService.categoryGrid(info,ph,sessionInfo.getOrgId()));
			grid.setTotal(projectCheckService.categoryCount(info,ph,sessionInfo.getOrgId()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	@RequestMapping("/detailGrid")
	@ResponseBody
	public Grid detailGrid(SearchInfo info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(projectCheckService.detailGrid(info,ph,sessionInfo.getOrgId()));
			grid.setTotal(projectCheckService.detailCount(info,ph,sessionInfo.getOrgId()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping(value = "/createCategoryExcel") 
	@ResponseBody
	public Json createCategoryExcel(SearchInfo search,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			List<Map<String,Object>> mapList=projectCheckService.categoryGrid(search,null,sessionInfo.getOrgId());
			ExcelInfo info=new ExcelInfo();
			info.setFileName("可变分类汇总.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"category","amt","amt_e_tax"};
			String[] columnNames={"分类","金额","不含税金额"};
			String[] cellTypes={"String","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("categoryGrid_"+sessionInfo.getId(), filePath);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportCategoryExcel") 
	@ResponseBody
	public void exportCategoryExcel(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("categoryGrid_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
	@RequestMapping(value = "/createDetailExcel") 
	@ResponseBody
	public Json createDetailExcel(SearchInfo search,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			List<Map<String,Object>> mapList=projectCheckService.detailGrid(search,null,sessionInfo.getOrgId());
			ExcelInfo info=new ExcelInfo();
			info.setFileName("明细.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"project_code","supplier_name","amt"};
			String[] columnNames={"项目编号","供应商","金额"};
			String[] cellTypes={"String","String","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("detailGrid_"+sessionInfo.getId(), filePath);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportDetailExcel") 
	@ResponseBody
	public void exportDetailExcel(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("detailGrid_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
