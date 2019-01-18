package com.mvc.controller.sys;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.Depart;
import com.mvc.service.sys.DepartServiceI;

@Controller
@RequestMapping("/departAdmin")
public class DepartController extends BaseController{
	@Autowired
	private DepartServiceI departService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		/*SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());*/
		return "/system/depart";
	}
	
	@RequestMapping("/treeGrid")
	@ResponseBody
	public List<Tree> treeGrid(HttpSession session) {
		List<Tree> result = new ArrayList<Tree>();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			result=departService.treeGrid(sessionInfo.getOrgId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	@RequestMapping("/departList")
	@ResponseBody
	public List<Tree> departList(HttpSession session) {
		List<Tree> result = new ArrayList<Tree>();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			result=departService.departList(sessionInfo.getOrgId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(Depart depart)
	{
		Json j=new Json();
		try
		{
			departService.add(depart);
			j.setSuccess(true);
			j.setMsg("保存成功！");
		}
		catch(Exception e)
		{
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/edit")
	@ResponseBody
	public Json edit(Depart depart)
	{
		Json j=new Json();
		try
		{
			departService.edit(depart);
			j.setSuccess(true);
			j.setMsg("修改成功！");
		}
		catch(Exception e)
		{
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(Depart depart)
	{
		Json j=new Json();
		try
		{
			departService.delete(depart);
			j.setSuccess(true);
			j.setMsg("删除成功！");
		}
		catch(Exception e)
		{
			j.setSuccess(false);
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
