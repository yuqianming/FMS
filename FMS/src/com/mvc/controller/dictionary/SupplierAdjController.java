package com.mvc.controller.dictionary;

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
import com.mvc.model.sys.TSupplierAdj;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.CostInfo;
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dictionary.SupplierAdjServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 供应商调整 Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/supplierAdj")
public class SupplierAdjController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private SupplierAdjServiceI supplierAdjService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dictionary/supplierAdj";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TSupplierAdj info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(supplierAdjService.dataGrid(info,ph));
			grid.setTotal(supplierAdjService.count(info,ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(TSupplierAdj info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setUpdate_time(new Date());
			info.setOrg_id(sessionInfo.getOrgId());
			supplierAdjService.add(info);
			j.setSuccess(true);
			j.setMsg("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(TSupplierAdj info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setUpdate_time(new Date());
			info.setOrg_id(sessionInfo.getOrgId());
			supplierAdjService.edit(info);
			j.setSuccess(true);
			j.setMsg("修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String ids) {
		Json j = new Json();
		try {
			supplierAdjService.delete(ids);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
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
			supplierAdjService.upload(info,session);
			j.setSuccess(true);
			j.setMsg("上传成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(HttpSession session,TSupplierAdj supplierInfo,HttpServletRequest request) {
		Json j = new Json();
		try {
			System.out.println("导出成本单Excel开始："+new Date());
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			supplierInfo.setOrg_id(sessionInfo.getOrgId());
			List<Map<String,Object>> testList=supplierAdjService.dataGrid(supplierInfo,null);
			ExcelInfo info=new ExcelInfo();
			info.setFileName("供应商调整.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={ "s_supplier_code","s_supplier_name","t_supplier_code","t_supplier_name"};
			String[] columnNames={"待调整供应商代码","待调整供应商名称","目标供应商代码","目标供应商名称"};
			String[] cellTypes={"String","String","String","String"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,testList);
			CacheUtils.cacheMe("supper_adj_"+sessionInfo.getId(), filePath);
			System.out.println("导出供应商调整Excel结束："+new Date());
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
		String filePath=(String) CacheUtils.getCache("supper_adj_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
