package com.mvc.controller.payment;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.payment.OnlinePaymentServiceI;
import com.mvc.service.sys.MenuServiceI;

/**
 * 线上付款导入 Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/onlinePayment")
public class OnlinePaymentController extends BaseController{
	@Autowired
	private OnlinePaymentServiceI onlinePaymentService;
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/formalPayment/onlinePayment";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid supplierDataGrid(HttpSession session,PageFilter ph) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(onlinePaymentService.dataGrid(sessionInfo.getId(),ph));
			grid.setTotal(onlinePaymentService.count(sessionInfo.getId(),ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
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
			int count=onlinePaymentService.upload(info,session);
			j.setSuccess(true);
			j.setMsg(count+"");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/dataHandle")
	@ResponseBody
	public Json dataHandle(String month,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			onlinePaymentService.dataHandle(month,sessionInfo.getId(),sessionInfo.getOrgId());
			j.setSuccess(true);
			j.setMsg("上传成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/check")
	@ResponseBody
	public Json check(HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> map=onlinePaymentService.check(sessionInfo.getId(),sessionInfo.getOrgId());
			j.setSuccess(true);
			j.setMsg("确认成功！");
			j.setObj(JSONObject.fromObject(map));
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/confirm")
	@ResponseBody
	public Json confirm(HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			onlinePaymentService.confirm(sessionInfo.getId());
			j.setSuccess(true);
			j.setMsg("确认成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
