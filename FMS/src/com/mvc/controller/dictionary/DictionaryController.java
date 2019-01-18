package com.mvc.controller.dictionary;

import java.util.List;

import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.service.sys.MenuServiceI;
/**
 * 字典维护导航Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/dictionary")
public class DictionaryController extends BaseController{
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> childList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		sessionInfo.setChildList(childList);
		return "/dictionary/index";
	}
}
