package com.mvc.controller.dataCheck;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

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
import com.mvc.service.dataCheck.StoreCheckServiceI;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

/**
 * 仓储数据核对Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/storeCheck")
public class StoreCheckController extends BaseController{
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	@Autowired
	private StoreCheckServiceI storeCheckService;
	
	private Map<String,Object> map=new HashMap<String,Object>();
	
	@Autowired
	private MenuServiceI menuService;
	@RequestMapping("/index")
	public String index(HttpSession session,String menu_id) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		List<Tree> buttonList=menuService.getChildMenuListByPid(sessionInfo.getId(),menu_id);
		JSONArray array=JSONArray.fromObject(buttonList);
		sessionInfo.setButtonList(array.toString());
		return "/dataCheck/storeCheck";
	}
	@RequestMapping("/addGrid")
	@ResponseBody
	public Grid addGrid(String startMonth,String endMonth,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			List<Map<String,Object>> mapList=storeCheckService.addGrid(startMonth,endMonth,sessionInfo.getOrgId());
			grid.setRows(mapList);
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			BigDecimal store_amt=new BigDecimal(0);
			BigDecimal deal_amt=new BigDecimal(0);
			BigDecimal all_amt=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				store_amt=store_amt.add(new BigDecimal(map.get("store_amt").toString()));
				deal_amt=deal_amt.add(new BigDecimal(map.get("deal_amt").toString()));
				all_amt=all_amt.add(new BigDecimal(map.get("all_amt").toString()));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> temp=new HashMap<String,Object>();
			temp.put("type", "合计");
			temp.put("store_amt_txt", df.format(store_amt));
			temp.put("deal_amt_txt", df.format(deal_amt));
			temp.put("all_amt_txt", df.format(all_amt));
			temp.put("store_amt",store_amt);
			temp.put("deal_amt", deal_amt);
			temp.put("all_amt", all_amt);
			footer.add(temp);
			grid.setFooter(footer);
			map.put("store_amt_add", store_amt);
			map.put("deal_amt_add", deal_amt);
			map.put("all_amt_add", all_amt);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/subGrid")
	@ResponseBody
	public Grid subGrid(String startMonth,String endMonth,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			List<Map<String,Object>> mapList=storeCheckService.subGrid(startMonth,endMonth,sessionInfo.getOrgId());
			grid.setRows(mapList);
			List<Map<String,Object>> footer=new ArrayList<Map<String,Object>>();
			BigDecimal store_amt=new BigDecimal(0);
			BigDecimal deal_amt=new BigDecimal(0);
			BigDecimal all_amt=new BigDecimal(0);
			for(Map<String,Object> map:mapList)
			{
				store_amt=store_amt.add(new BigDecimal(map.get("store_amt").toString()));
				deal_amt=deal_amt.add(new BigDecimal(map.get("deal_amt").toString()));
				all_amt=all_amt.add(new BigDecimal(map.get("all_amt").toString()));
			}
			DecimalFormat df = new DecimalFormat("###,##0.00");
			Map<String,Object> temp=new HashMap<String,Object>();
			temp.put("type", "合计");
			temp.put("store_amt_txt", df.format(store_amt));
			temp.put("deal_amt_txt", df.format(deal_amt));
			temp.put("all_amt_txt", df.format(all_amt));
			temp.put("store_amt",store_amt);
			temp.put("deal_amt", deal_amt);
			temp.put("all_amt", all_amt);
			footer.add(temp);
			grid.setFooter(footer);
			map.put("store_amt_sub", store_amt);
			map.put("deal_amt_sub", deal_amt);
			map.put("all_amt_sub", all_amt);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	@RequestMapping("/poolData")
	@ResponseBody
	public Map<String,Object> poolData(String startMonth,String endMonth,HttpSession session)
	{
		Map<String,Object> result=new HashMap<String,Object>();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			DecimalFormat df = new DecimalFormat("###,##0.00");
			result=storeCheckService.poolData(startMonth, endMonth,sessionInfo.getOrgId());
			BigDecimal store_amt=new BigDecimal(result.get("store_amt").toString());
			BigDecimal deal_amt=new BigDecimal(result.get("deal_amt").toString());
			result.put("store_amt_txt", df.format(store_amt));
			result.put("deal_amt_txt", df.format(deal_amt));
			result.put("dvalue_txt", df.format(store_amt.subtract(deal_amt)));
			BigDecimal deal_amt_adj=deal_amt.add(new BigDecimal(map.get("store_amt_add").toString()))
					.subtract(new BigDecimal(map.get("store_amt_sub").toString()))
					.add(new BigDecimal(map.get("all_amt_add").toString()))
					.subtract(new BigDecimal(map.get("all_amt_sub").toString()));
			BigDecimal store_amt_adj=store_amt.add(new BigDecimal(map.get("deal_amt_add").toString()))
					.subtract(new BigDecimal(map.get("deal_amt_sub").toString()));
					//.add(new BigDecimal(map.get("all_amt_add").toString()));
			result.put("store_amt_adj_txt", df.format(store_amt_adj));
			result.put("deal_amt_adj_txt", df.format(deal_amt_adj));
			result.put("dvalue_adj_txt", df.format(store_amt_adj.subtract(deal_amt_adj)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	
	@RequestMapping("/detailGrid")
	@ResponseBody
	public Grid detailGrid(String startMonth,String endMonth,String type,String mode,boolean isAll,boolean hideZero,PageFilter ph,HttpSession session) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=storeCheckService.detailGrid(startMonth,endMonth,type,mode,isAll,hideZero,ph,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			grid.setRows(mapList);
			grid.setTotal(Long.parseLong(result.get("total").toString()));
			//grid.setRows(storeCheckService.detailGrid(startMonth,endMonth,type,mode,isAll,hideZero,ph,sessionInfo.getOrgId()));
			//grid.setTotal(storeCheckService.detailCount(startMonth,endMonth,type,mode,isAll,hideZero,sessionInfo.getOrgId()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return grid;
	}
	
	
	@RequestMapping(value = "/createDetailExcel") 
	@ResponseBody
	public Json createExcel(HttpSession session,HttpServletRequest request,String startMonth,String endMonth,String type,String mode,boolean isAll,boolean hideZero) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			//List<Map<String,Object>> testList=storeCheckService.detailGrid(startMonth,endMonth,type,mode,isAll,hideZero,null,sessionInfo.getOrgId());
			Map<String,Object> result=storeCheckService.detailGrid(startMonth,endMonth,type,mode,isAll,hideZero,null,sessionInfo.getOrgId());
			List<Map<String,Object>> mapList=(List<Map<String, Object>>) result.get("rows");
			ExcelInfo info=new ExcelInfo();
			info.setFileName("核对明细.xlsx");
			String docsPath = request.getSession().getServletContext()
						.getRealPath("excel")+FILE_SEPARATOR;
			info.setFileUrl(docsPath);
			String[] columnCodes={"month","order_code","supplier_name","deal_amt","store_amt","all_amt"};
			String[] columnNames={"日期","订单号或入库单号","厂家","核对系统金额","仓储系统金额","差异金额（仓储 - 核算）"};
			String[] cellTypes={"String","String","String","BigDecimal","BigDecimal","BigDecimal"};
			info.setColumnCodes(columnCodes);
			info.setColumnNames(columnNames);
			info.setCellTypes(cellTypes);
			String filePath=ExportUtils.exportExcel(info,mapList);
			CacheUtils.cacheMe("storeCheck_"+sessionInfo.getId(), filePath);
			j.setSuccess(true);
			j.setMsg("生成Excel成功！");
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportDetailExcel") 
	@ResponseBody
	public void exportExcel(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("storeCheck_"+sessionInfo.getId());
		ExportUtils.download(filePath,response);
	}
}
