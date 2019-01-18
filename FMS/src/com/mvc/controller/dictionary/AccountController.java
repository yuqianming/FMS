
package com.mvc.controller.dictionary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.model.sys.TAccount;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.service.dictionary.AccountServiceI;
import com.mvc.service.sys.MenuServiceI;
/**
 * 科目Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController{
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private AccountServiceI accountService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dictionary/account";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TAccount accountInfo,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			accountInfo.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(accountService.dataGrid(accountInfo,ph));
			grid.setTotal(accountService.count(accountInfo,ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(TAccount info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setUpdate_time(new Date());
			info.setOrg_id(sessionInfo.getOrgId());
			accountService.add(info);
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
	public Json edit(TAccount info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setUpdate_time(new Date());
			info.setOrg_id(sessionInfo.getOrgId());
			accountService.edit(info);
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
			accountService.delete(ids);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/getAccountList")
	@ResponseBody
	public List<TAccount> getAccountList()
	{
		List<TAccount> list=new ArrayList<TAccount>();
		try
		{
			list=accountService.getAccountList();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}
}
