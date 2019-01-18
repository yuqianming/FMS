package com.mvc.controller.content;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import com.mvc.service.content.NoticeServiceI;

@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController{
	@Autowired
	private NoticeServiceI noticeServiceI;
	
	@RequestMapping("/index")
	public String index() {
		return "/content/notice";
	}
	
	@RequestMapping("/getNotices")
	@ResponseBody
	public Grid dataGrid(String notice_title,String notice_content,HttpSession session,PageFilter ph) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=noticeServiceI.dataGrid(null,notice_title,notice_content,sessionInfo.getOrgId(),ph);
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
	@RequestMapping("/add")
	@ResponseBody
	public Json add(String notice_title,String notice_content,HttpSession session) {
		Json json=new Json();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			noticeServiceI.add(notice_title, notice_content, sessionInfo.getOrgId(),sessionInfo.getId());
			json.setSuccess(true);
			json.setMsg("发布成功！");
		}
		catch(Exception e)
		{
			json.setSuccess(false);
			json.setMsg("发布失败！");
		}
		return json;
	}
	
	@RequestMapping("/deleteNotice")
	@ResponseBody
	public Json deleteNotice(String ids,HttpSession session) {
		Json json=new Json();
		try
		{
			noticeServiceI.delete(ids);
			json.setSuccess(true);
			json.setMsg("删除成功！");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("删除失败！");
		}
		return json;
	}
	
}
