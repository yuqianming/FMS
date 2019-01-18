package com.mvc.controller.common;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.common.CommonServiceI;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController{
	@Autowired
	private CommonServiceI commonService;
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
			int count=commonService.upload(info,session);
			j.setSuccess(true);
			j.setMsg(count+"");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		System.out.println("###### result : "+JSONObject.fromObject(j).toString());
		return j;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public Json save(UploadInfo info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			if(sessionInfo!=null)
			{
				info.setUserId(sessionInfo.getId());
				info.setOrgId(sessionInfo.getOrgId());
			}
			commonService.save(info);
			j.setSuccess(true);
			j.setMsg("上传成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/tip")
	@ResponseBody
	public Json tip(String tableName,HttpSession session)
	{
		Json json=new Json();
		String result="";
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			result=commonService.tip(tableName,sessionInfo.getOrgId());
			json.setMsg(result);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return json;
	}
}
