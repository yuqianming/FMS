package com.mvc.controller.dictionary;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.model.sys.TTaxRate;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.dictionary.TaxRateServiceI;
import com.mvc.service.sys.MenuServiceI;

/**
 * 税率  Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/taxRate")
public class TaxRateController extends BaseController{
	@Autowired
	private MenuServiceI menuService;
	@Autowired
	private TaxRateServiceI taxRateService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dictionary/taxRate";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(TTaxRate info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			grid.setRows(taxRateService.dataGrid(info,ph));
			grid.setTotal(taxRateService.count(info,ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(TTaxRate info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setUpdate_time(new Date());
			taxRateService.add(info);
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
	public Json edit(TTaxRate info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUpdate_by(sessionInfo.getId());
			info.setUpdate_time(new Date());
			taxRateService.edit(info);
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
			taxRateService.delete(ids);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/getRateList")
	@ResponseBody
	public List<Map<String,Object>> getRateList(HttpSession session)
	{
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try
		{
			list=taxRateService.getRateList();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
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
			}
			taxRateService.upload(info);
			j.setSuccess(true);
			j.setMsg("上传成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
