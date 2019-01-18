package com.mvc.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.model.sys.TDepartment;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.User;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.service.sys.UserAdminServiceI;


@Controller
@RequestMapping("/userAdmin")
public class UserController extends BaseController{
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private UserAdminServiceI userAdminService;
	
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/system/user";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(User user, PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			user.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(userAdminService.dataGrid(user, ph));
			grid.setTotal(userAdminService.count(user, ph));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return grid;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(User user) {
		Json j = new Json();
		try {
			userAdminService.add(user);
			j.setSuccess(true);
			j.setMsg("新增成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(User user) {
		Json j = new Json();
		try {
			userAdminService.edit(user);
			j.setSuccess(true);
			j.setMsg("编辑成功！");
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String ids) {
		Json j = new Json();
		try {
			userAdminService.delete(ids);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/reset")
	@ResponseBody
	public Json reset(String ids) {
		Json j = new Json();
		try {
			userAdminService.reset(ids);
			j.setMsg("重置成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/getOrgList")
	@ResponseBody
	public List<TDepartment> getOrgList() {
		List<TDepartment> orgList=new ArrayList<TDepartment>();
		try {
			orgList=userAdminService.getOrgList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgList;
	}
}
