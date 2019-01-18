package com.mvc.controller.payment;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.model.sys.TSignature;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.SignInfo;
import com.mvc.service.payment.SignPaymentServiceI;
import com.mvc.service.sys.MenuServiceI;
/**
 * 签字付款 Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/signPayment")
public class SignPaymentController extends BaseController{
	@Autowired
	private SignPaymentServiceI signPaymentService;
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/formalPayment/signPayment";
	}
	
	@RequestMapping("/supplierDataGrid")
	@ResponseBody
	public Grid supplierDataGrid(TSignature info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(signPaymentService.supplierDataGrid(info,ph));
			grid.setTotal(signPaymentService.supplierCount(info,ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/accountDataGrid")
	@ResponseBody
	public Grid accountDataGrid(TSignature info,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			grid.setRows(signPaymentService.accountDataGrid(info,ph));
			grid.setTotal(signPaymentService.accountCount(info,ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/save")
	public @ResponseBody Json save(@RequestBody SignInfo info,HttpSession session)
	{
		Json json=new Json();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setUser_id(sessionInfo.getId());
			info.setOrg_id(sessionInfo.getOrgId());
			signPaymentService.save(info);
			json.setSuccess(true);
			json.setMsg("付款完成！");
		}
		catch(Exception e)
		{
			json.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public Json delete(String batchs,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			signPaymentService.delete(batchs,sessionInfo.getOrgId());
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
}
