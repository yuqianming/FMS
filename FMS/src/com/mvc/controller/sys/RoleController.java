package com.mvc.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.mvc.pageModel.sys.Role;
import com.mvc.pageModel.sys.User;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.service.sys.RoleAdminServiceI;



@Controller
@RequestMapping("/roleAdmin")
public class RoleController extends BaseController{
	protected Log log = LogFactory.getLog(RoleController.class);
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private RoleAdminServiceI roleAdminService;
	
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/system/role";
	}
	
	@RequestMapping("/addPage")
	public String addPage() {
		return "/system/roleAdd";
	}
	
	@RequestMapping("/editPage")
	public String editPage(HttpServletRequest request, int id) {
		try
		{
			Role r = roleAdminService.get(id);
			request.setAttribute("role", r);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "/system/roleEdit";
	}
	
	@RequestMapping("/viewPage")
	public String viewPage(HttpServletRequest request, int id) {
		try
		{
			Role r = roleAdminService.get(id);
			request.setAttribute("role", r);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "/system/roleView";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(Role role, PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			role.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(roleAdminService.dataGrid(role, ph));
			grid.setTotal(roleAdminService.count(role, ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/listMenuTree")
	@ResponseBody
	public List<Tree> tree(HttpSession session) {
		List<Tree> treeList=new ArrayList<Tree>();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			treeList=roleAdminService.listMenuTree(sessionInfo.getId());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return treeList;
	}
	
	@RequestMapping("/listUserTree")
	@ResponseBody
	public List<Tree> listUserTree(String org_id) {
		List<Tree> treeList=new ArrayList<Tree>();
		try
		{
			User u=new User();
			u.setOrg_id(org_id);
			treeList = roleAdminService.listUserTreeBySearch(u);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return treeList;
	}
	
	@RequestMapping("/listUserTreeBySearch")
	@ResponseBody
	public List<Tree> listUserTreeBySearch(User user,HttpSession session) {
		List<Tree> treeList=new ArrayList<Tree>();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			user.setOrg_id(sessionInfo.getOrgId());
			treeList = roleAdminService.listUserTreeBySearch(user);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return treeList;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(Role role) {
		Json j = new Json();
		try {
			roleAdminService.add(role);
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
	public Json edit(Role role,HttpSession session) {
		Json j = new Json();
		try {
			roleAdminService.edit(role);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/listUserTreeByRole")
	@ResponseBody
	public List<Tree> listUserTreeByRole(int role_id) {
		List<Tree> treeList=new ArrayList<Tree>();
		try
		{
			treeList = roleAdminService.listUserTreeByRole(role_id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return treeList;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(int id) {
		Json j = new Json();
		try {
			roleAdminService.delete(id);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
