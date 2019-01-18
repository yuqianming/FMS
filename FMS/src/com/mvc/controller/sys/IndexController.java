package com.mvc.controller.sys;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.User;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.service.sys.UserServiceI;

@Controller
@RequestMapping("/admin")
public class IndexController extends BaseController {

	@Autowired
	private UserServiceI userService;
	
	@Autowired
	private MenuServiceI menuService;

	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);

	@RequestMapping("/index")
	public String index(HttpSession session) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		if ((sessionInfo != null) && (sessionInfo.getId() != null)) {
			return "/index";
		}
		return "/login";
	}

	@ResponseBody
	@RequestMapping("/login")
	public Json login(User user, HttpSession session) {
		Json j = new Json();
		if(user.getUser_id().equals("admin"))
		{
			if(user.getPassword().equals("admin"))
			{
				j.setSuccess(true);
				j.setMsg("登陆成功！");
				
				List<Tree> treeList=menuService.getMenuList("admin");
				SessionInfo sessionInfo = new SessionInfo();
				sessionInfo.setId("admin");
				sessionInfo.setLoginname("超级管理员");
				sessionInfo.setOrgId("all");
				sessionInfo.setOrgName("系统");
				sessionInfo.setTreeList(treeList);
				session.setAttribute(GlobalConstant.SESSION_INFO, sessionInfo);
			}else {
				j.setMsg("用户名或密码错误！");
			}
		}
		else
		{
           int loginReturnCode = userService.getLogin(user);
			
			if (loginReturnCode != 0) {
				
				String user_id = user.getUser_id();
				
				j.setSuccess(true);
				j.setMsg("登陆成功！");

				user = userService.getLoginUser(user_id);
				List<Tree> treeList=menuService.getMenuList(user_id);
				SessionInfo sessionInfo = new SessionInfo();
				sessionInfo.setId(user_id);
				sessionInfo.setLoginname(user.getUser_name());
				sessionInfo.setOrgId(user.getOrg_id());
				sessionInfo.setOrgName(user.getOrg_name());
				sessionInfo.setTreeList(treeList);
				session.setAttribute(GlobalConstant.SESSION_INFO, sessionInfo);
				
			} else {
				j.setMsg("用户名或密码错误！");
			}
		}
		return j;
	}

	@ResponseBody
	@RequestMapping("/logout")
	public Json logout(HttpSession session) {
		Json j = new Json();
		if (session != null) {
			session.invalidate();
		}
		j.setSuccess(true);
		j.setMsg("注销成功！");
		return j;
	}

	@RequestMapping("/resetPassword")
	@ResponseBody
	public Json resetPassword(User user) {
		Json j = new Json();
		try {
			if(user.getUser_id().contains("admin"))
			{
				j.setSuccess(false);
				j.setMsg("系统生成管理员的密码不允许修改！");
			}
			else
			{
				userService.resetPassword(user);
				j.setSuccess(true);
				j.setMsg("修改密码成功！");
			}

		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
