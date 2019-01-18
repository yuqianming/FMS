package com.mvc.controller.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.model.sys.TPayment;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.pageModel.base.Tree;
import com.mvc.service.content.FileServiceI;
import com.mvc.service.content.NoticeServiceI;
import com.mvc.service.home.HomeServiceI;
import com.mvc.service.report.CancelReportServiceI;
import com.mvc.service.report.CostlReportServiceI;
import com.mvc.service.report.OverpayReportServiceI;
import com.mvc.service.report.PaymentReportServiceI;
import com.mvc.service.report.StoreReportServiceI;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController{
	@Autowired
	private HomeServiceI homeServiceI;
	
	@Autowired
	private FileServiceI fileServiceI;
	
	@Autowired
	private NoticeServiceI noticeServiceI;
	
	@Autowired
	private OverpayReportServiceI overpayReportService;
	@Autowired
	private CancelReportServiceI cancelReportService;
	@Autowired
	private PaymentReportServiceI paymentReportService;
	@Autowired
	private CostlReportServiceI costlReportService;
	@Autowired
	private StoreReportServiceI storeReportService;
	
	@RequestMapping("/index")
	public String index() {
		return "/homePage/index";
	}
	
	@RequestMapping("/dataImportWin")
	public String dataImport() {
		return "/homePage/dataImportWin";
	}
	
	@RequestMapping("/noticeWin")
	public String noticeWin() {
		return "/homePage/noticeWin";
	}
	
	@RequestMapping("/fileWin")
	public String fileWin() {
		return "/homePage/fileWin";
	}
	
	@RequestMapping("/noticeView")
	public String noticeView(HttpSession session,String id) {
		
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String, Object> result= noticeServiceI.dataGrid(id,null,null,sessionInfo.getOrgId(),null);
			List<Map<String,Object>> noticeList=(List<Map<String, Object>>) result.get("rows");
			
			if (noticeList != null && noticeList.size() > 0) {
				//公告明细
				session.setAttribute("orgName",homeServiceI.getNoticeDept(noticeList.get(0).get("org_id").toString()).get("org_name"));
				session.setAttribute("userName",homeServiceI.getNoticeUser(noticeList.get(0).get("user_id").toString()).get("user_name"));
				session.setAttribute("noticeTitle", noticeList.get(0).get("notice_title"));
				session.setAttribute("noticeTime", noticeList.get(0).get("notice_time"));
				session.setAttribute("noticeContent", noticeList.get(0).get("notice_content"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "/homePage/noticeView";
	}
	
	@RequestMapping("/initEchartInfo")
	@ResponseBody
	public Json initEchartInfo(HttpSession session) {
		Json j = new Json();
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		
//		List<Tree> menuTreeList=menuService.getMenuList(sessionInfo.getId());
		try {
			List<Map<String, Object>> customTreeList = homeServiceI.initCustomTreeList(sessionInfo.getId());
			j.setSuccess(true);
			j.setObj(customTreeList);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		
		return j;
	}
	
	@RequestMapping("/getEchartData")
	@ResponseBody
	public Json getEchartData(String optionId) {
		Json j = new Json();
		
		Map<String, Object> result = null;
		try {
			if ("1".equals(optionId)) {
				result = cancelReportService.dataGrid(null,null,"1",false,false,false,null);
			} else if ("2".equals(optionId)) {
				result = overpayReportService.dataGrid(null,null,"1",false,false,false,null);
			} else if ("3".equals(optionId)) {
				result = paymentReportService.dataGrid(null,null,"1",null);
			} else if ("4".equals(optionId)) {
				result = costlReportService.dataGrid(null,null,false,null,null,null);
			} else if ("5".equals(optionId)) {
				result = storeReportService.dataGrid(null,null,false,false,null);
			}
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> tempList=(List<Map<String, Object>>) result.get("rows");
			
			List<List<String>> echartDataList = new ArrayList<>();
			
			if ("1".equals(optionId) || "2".equals(optionId) || "3".equals(optionId)) {
				//分公司
				List<String> groupNameList = new ArrayList<>();
				
				List<String> numList1 = new ArrayList<>();//个数
				List<String> amtList1 = new ArrayList<>();//金额
				
				List<String> numList2 = new ArrayList<>();//个数
				List<String> amtList2 = new ArrayList<>();//金额
				
				for (int i = 0; i < tempList.size(); i++) {
					//分公司
					groupNameList.add(tempList.get(i).get("group_name").toString());
					
					if ("3".equals(optionId)) {
						numList1.add(tempList.get(i).get("sign_num").toString());
						amtList1.add(String.valueOf(Float.parseFloat(tempList.get(i).get("sign_amt").toString().replaceAll(",", ""))/10000));
					} else {
						numList1.add(tempList.get(i).get("cost_num").toString());
						amtList1.add(String.valueOf(Float.parseFloat(tempList.get(i).get("cost_amt").toString().replaceAll(",", ""))/10000));
					}
					
					numList2.add(tempList.get(i).get("pay_num").toString());
					amtList2.add(String.valueOf(Float.parseFloat(tempList.get(i).get("pay_amt").toString().replaceAll(",", ""))/10000));
				}
				
				//分公司
				echartDataList.add(groupNameList);
				//金额
				echartDataList.add(amtList1);
				echartDataList.add(amtList2);
				//个数
				echartDataList.add(numList1);
				echartDataList.add(numList2);
				
			} else if ("4".equals(optionId)) {
				//分公司
				List<String> groupNameList = new ArrayList<>();
				
				List<String> amtList1 = new ArrayList<>();//金额1
				List<String> amtList2 = new ArrayList<>();//金额2
				List<String> amtList3 = new ArrayList<>();//金额3
				List<String> amtList4 = new ArrayList<>();//金额4
				List<String> amtList5 = new ArrayList<>();//金额5
				
				for (int i = 0; i < tempList.size(); i++) {
					//分公司
					groupNameList.add(tempList.get(i).get("org_name").toString());
					
					amtList1.add(String.valueOf(Float.parseFloat(tempList.get(i).get("account_amt0").toString().replaceAll(",", ""))/10000));
					amtList2.add(String.valueOf(Float.parseFloat(tempList.get(i).get("account_amt1").toString().replaceAll(",", ""))/10000));
					amtList3.add(String.valueOf(Float.parseFloat(tempList.get(i).get("account_amt2").toString().replaceAll(",", ""))/10000));
					amtList4.add(String.valueOf(Float.parseFloat(tempList.get(i).get("account_amt3").toString().replaceAll(",", ""))/10000));
					amtList5.add(String.valueOf(Float.parseFloat(tempList.get(i).get("account_amt4").toString().replaceAll(",", ""))/10000));
				}
				
				//分公司
				echartDataList.add(groupNameList);
				//金额
				echartDataList.add(amtList1);
				echartDataList.add(amtList2);
				echartDataList.add(amtList3);
				echartDataList.add(amtList4);
				echartDataList.add(amtList5);
			}
			
			j.setSuccess(true);
			j.setObj(echartDataList);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		
		return j;
	}
	
	@RequestMapping("/updateCustom")
	@ResponseBody
	public Json updateCustom(HttpSession session,String customIds) {
		Json j = new Json();
		Boolean flag = false;
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			int[] ids = homeServiceI.updateCustom(sessionInfo.getId(),customIds);
			if (ids != null) {
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] == 1) {
						flag = true;
					} else {
						flag = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		j.setSuccess(flag);
		return j;
	}
	
	@RequestMapping("/getNoticeTree")
	@ResponseBody
	public Json getNoticeTree(HttpSession session,PageFilter ph) {
		Json j = new Json();
		List<Tree> listTree = new ArrayList<>();
		
		ph.setPage(1);
		ph.setRows(10);
		ph.setOrder("desc");
		ph.setSort("notice_time");
		
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String, Object> result = noticeServiceI.dataGrid(null,null,null,sessionInfo.getOrgId(),ph);
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> noticeTree=(List<Map<String, Object>>) result.get("rows");
			for (int i = 0; i < noticeTree.size(); i++) {
				Tree tree = new Tree();
				tree.setId(noticeTree.get(i).get("id").toString());
				tree.setText(noticeTree.get(i).get("notice_title").toString() +"-("+noticeTree.get(i).get("notice_time").toString()+")");
				
				//公告内容
				List<String> strs = new ArrayList<String>();
				strs.add(noticeTree.get(i).get("notice_title").toString());
				strs.add(noticeTree.get(i).get("notice_time").toString());
				strs.add(noticeTree.get(i).get("notice_content").toString());
				
				//公告内容
				tree.setAttributes(strs);
				
				tree.setState("open");
				listTree.add(tree);
			}
			
			j.setObj(listTree);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		
		return j;
	}
	
	@RequestMapping("/getNoticeGrid")
	@ResponseBody
	public Grid getNoticeGrid(HttpSession session,PageFilter pf) {
		Grid grid = new Grid();
		pf.setPage(1);
		pf.setRows(10);
		pf.setSort("notice_time");
		pf.setOrder("desc");
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=noticeServiceI.dataGrid(null,null,null,sessionInfo.getOrgId(),pf);
			List<Map<String,Object>> viewList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(viewList);
			//grid.setTotal(Long.parseLong(result.get("total").toString()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/getFileTree")
	@ResponseBody
	public Json getFileTree(HttpSession session,PageFilter ph) {
		Json j = new Json();
		List<Tree> listTree = new ArrayList<>();
		
		ph.setPage(1);
		ph.setRows(10);
		ph.setOrder("desc");
		ph.setSort("create_time");
		
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String, Object> result = fileServiceI.dataGrid(null,null,sessionInfo.getOrgId(),ph);
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> fileTree=(List<Map<String, Object>>) result.get("rows");
			for (int i = 0; i < fileTree.size(); i++) {
				Tree tree = new Tree();
				tree.setId(fileTree.get(i).get("id").toString());
				tree.setText(fileTree.get(i).get("file_name").toString() +"-("+fileTree.get(i).get("create_time").toString()+")");
				tree.setState("open");
				listTree.add(tree);
			}
			
			j.setObj(listTree);
			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
		}
		
		return j;
	}
	
	@RequestMapping("/getFileGrid")
	@ResponseBody
	public Grid getFileGrid(HttpSession session,PageFilter pf) {
		Grid grid = new Grid();
		pf.setPage(1);
		pf.setRows(10);
		pf.setSort("create_time");
		pf.setOrder("desc");
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=fileServiceI.dataGrid(null,null,sessionInfo.getOrgId(),pf);
			List<Map<String,Object>> viewList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(viewList);
			//grid.setTotal(Long.parseLong(result.get("total").toString()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/getPayOffTree")
	@ResponseBody
	public Json getPayOffTree() {
		Json j = new Json();
		List<Tree> listTree = new ArrayList<>();
		List<TPayment> paymentTree = null;
		try {
			paymentTree = homeServiceI.getPayOffList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < paymentTree.size(); i++) {
			Tree tree = new Tree();
			tree.setId(String.valueOf(paymentTree.get(i).getId()));
			tree.setText(paymentTree.get(i).getSupplier_name()+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+paymentTree.get(i).getPay_amt());
			tree.setState("open");
			listTree.add(tree);
		}
		
		j.setObj(listTree);
		return j;
	}
	
	@RequestMapping("/getPayOffGrid")
	@ResponseBody
	public Grid getPayOffGrid(HttpSession session,PageFilter pf) {
		Grid grid = new Grid();
		pf.setPage(1);
		pf.setRows(10);
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			grid.setRows(homeServiceI.payOffDataGrid(sessionInfo.getOrgId(),pf));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/getDataImportTree")
	@ResponseBody
	public Json getDataImportTree(HttpSession session) {
		Json j = new Json();
		
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		
		List<Tree> listTree = new ArrayList<>();
		
		List<Map<String, Object>> listMapTree = null;
		try {
			listMapTree = homeServiceI.getDataImport(sessionInfo.getOrgId(),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < listMapTree.size(); i++) {
			Tree tree = new Tree();
			tree.setId(String.valueOf(listMapTree.get(i).get("id")));
			tree.setText(listMapTree.get(i).get("user_name").toString()+"导入"+listMapTree.get(i).get("file_name").toString() +"-("+listMapTree.get(i).get("log_time").toString()+")");
			tree.setState("open");
			listTree.add(tree);
		}
		
		j.setObj(listTree);
		return j;
	}
	
	@RequestMapping("/getDataImportGrid")
	@ResponseBody
	public Grid getDataImportGrid(HttpSession session,PageFilter pf) {
		Grid grid = new Grid();
		if (pf.getPage() == 0 && pf.getRows() == 0) {
			pf.setPage(1);
			pf.setRows(10);
			pf.setSort("log_time");
			pf.setOrder("desc");
		}
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		try
		{
			grid.setRows(homeServiceI.getDataImport(sessionInfo.getOrgId(),pf));
			if (pf.getPage() != 0 || pf.getRows() != 0) {
				grid.setTotal(homeServiceI.count(sessionInfo.getOrgId()));
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	/*@RequestMapping("/getUpdateContentTree")
	@ResponseBody
	public Json getUpdateContentTree(HttpSession session) {
		Json j = new Json();
		List<Tree> listTree = new ArrayList<>();
		
		Tree t=new Tree();
		t.setId("");
		t.setText("updateContent1");
		t.setUrl("");
		t.setState("open");
		List<Tree> children = new ArrayList<>();
		Tree child1 = new Tree();
		child1.setId("");
		child1.setText("updateContentCheld1");
		child1.setUrl("");
		child1.setState("close");
		children.add(child1);
		Tree child2 = new Tree();
		child2.setId("");
		child2.setText("updateContentCheld1");
		child2.setUrl("");
		child2.setState("close");
		children.add(child2);
		t.setChildren(children);
		
		Tree t2=new Tree();
		t2.setId("");
		t2.setText("updateContent2");
		t2.setUrl("");
		t2.setState("open");
		
		Tree t3=new Tree();
		t3.setId("");
		t3.setText("updateContent3");
		t3.setUrl("");
		t3.setState("open");
		
		listTree.add(t);
		listTree.add(t2);
		listTree.add(t3);
		
		j.setObj(listTree);
		return j;
	}*/
	
}
