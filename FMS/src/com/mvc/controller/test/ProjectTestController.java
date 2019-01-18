package com.mvc.controller.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.mvc.pageModel.sys.ExcelInfo;
import com.mvc.pageModel.sys.ProjectTest;
import com.mvc.pageModel.sys.UploadInfo;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.service.test.ProjectTestServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 项目测试 Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/projectTest")
public class ProjectTestController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private MenuServiceI menuService;
	
	@Autowired
	private ProjectTestServiceI projectTestService;
	
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		try {
			projectTestService.deleteOrderTest(sessionInfo.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/testPayment/projectTest";
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
			projectTestService.upload(info);
			j.setSuccess(true);
			j.setMsg("上传成功！");
		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(projectTestService.dataGrid(sessionInfo.getId(),ph));
			grid.setTotal(projectTestService.count(sessionInfo.getId(),ph));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Json add(ProjectTest info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			projectTestService.add(sessionInfo.getId(),info);
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
	public Json edit(ProjectTest info,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			projectTestService.edit(sessionInfo.getId(),info);
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
	public Json delete(String ids,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			projectTestService.delete(sessionInfo.getId(),ids);
			j.setMsg("删除成功！");
			j.setSuccess(true);
		} catch (Exception e) {
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/startTest")
	@ResponseBody
	public Json startTest(ProjectTest info,HttpSession session,BigDecimal deviation) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			info.setOrg_id(sessionInfo.getOrgId());
			Map<String,Object> map=projectTestService.startTest(info,sessionInfo.getId(),deviation);
			j.setSuccess(true);
			j.setMsg("测试成功！");
			j.setObj(map);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/getNopassCount")
	@ResponseBody
	public Json getNopassCount(HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> map=projectTestService.getNopassCount(sessionInfo.getId());
			j.setSuccess(true);
			j.setObj(JSONObject.fromObject(map));
			//j.setMsg("测试成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping("/confirmSign")
	@ResponseBody
	public Json confirmSign(BigDecimal invoice_amt_e_tax,BigDecimal pay_amt_e_tax,HttpSession session) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			projectTestService.confirmSign(invoice_amt_e_tax,pay_amt_e_tax,sessionInfo.getId(),sessionInfo.getOrgId());
			j.setSuccess(true);
			j.setMsg("签字成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/createExcel") 
	@ResponseBody
	public Json createExcel(HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			List<Map<String,Object>> testList=projectTestService.dataGrid(sessionInfo.getId(),null);
			ExcelInfo info=new ExcelInfo();
			info.setFileName("项目测试.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"project_code","supplier_name","account_name","address_name","c_account_amt","c_invoice_amt","c_pay_amt","n_pay_amt","t_invoice_amt","t_pay_amt","is_pass","build_mode","accept_date","deliver_date","project_status","rent_status","is_cancel","is_old"};
			String[] columnNames={"项目编号","供应商名称","科目名称","站址名称","已入账金额","已开票金额","已交付金额","签字未登记","本次开票金额","本次交付金额","符合性测试","建设方式","内验时间","交付时间","项目状态","起租信息","是否销项","是否旧项目"};
			String[] cellTypes={"String","String","String","String","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","BigDecimal","String","String","String","String","String","String","String","String"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			/*List<Map<String,Object>> mapList=new ArrayList<Map<String,Object>>();
			for(ProjectTest test:testList)
			{
				Map<String,Object> map=BeanToMapUtils.toMap(test);
				mapList.add(map);
			}*/
			String filePath=ExportUtils.exportExcel(info,testList);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
			CacheUtils.cacheMe("project_test_"+sessionInfo.getId(), filePath);
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportExcel") 
	@ResponseBody
	public void exportExcel(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("project_test_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
	
	
	@RequestMapping("/getAccountBySupplier")
	@ResponseBody
	public List<Map<String,Object>> getAccountBySupplier(String supplier_name,HttpSession session)
	{
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		try {
			result=projectTestService.getAccountBySupplier(supplier_name,sessionInfo.getId(),sessionInfo.getOrgId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
