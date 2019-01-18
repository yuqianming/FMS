package com.mvc.controller.content;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.mvc.controller.base.BaseController;
import com.mvc.framework.constant.GlobalConstant;
import com.mvc.pageModel.base.Grid;
import com.mvc.pageModel.base.Json;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.SessionInfo;
import com.mvc.service.content.FileServiceI;
import com.mvc.utils.CacheUtils;
import com.mvc.utils.ExportUtils;

@Controller
@RequestMapping("/files")
public class FileController extends BaseController{
	@Autowired
	private FileServiceI fileServiceI;
	
	@RequestMapping("/index")
	public String index() {
		return "/content/file";
	}
	
	@RequestMapping("/dataGrid")
	@ResponseBody
	public Grid dataGrid(String file_name,String remark,HttpSession session,PageFilter ph) {
		Grid grid = new Grid();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> result=fileServiceI.dataGrid(file_name,remark,sessionInfo.getOrgId(),ph);
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
	public Json add(@RequestParam MultipartFile upFile,String remark,HttpSession session) {
		Json json=new Json();
		try
		{
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			fileServiceI.add(upFile,remark,sessionInfo.getId(),sessionInfo.getOrgId());
			json.setSuccess(true);
			json.setMsg("发布成功！");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("发布失败！");
		}
		return json;
	}
	
	@RequestMapping("/deleteFile")
	@ResponseBody
	public Json deleteFile(String id,HttpSession session) {
		Json json=new Json();
		try
		{
			fileServiceI.delete(id);
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
	
	@RequestMapping(value = "/queryFile")
	@ResponseBody
	public Json queryFile(int id,HttpSession session,HttpServletRequest request) {
		Json j = new Json();
		try {
			SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
			Map<String,Object> map=fileServiceI.getFilePathById(id);
			//System.out.println("######下载路径："+filePath);
			j.setSuccess(true);
			j.setMsg("查询成功！");
			CacheUtils.cacheMe("file_"+sessionInfo.getId(), map.get("file_path"));
			CacheUtils.cacheMe("name_"+sessionInfo.getId(), map.get("file_name"));
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
		}
		return j;
	}
	
	@RequestMapping(value = "/exportFile") 
	@ResponseBody
	public void exportFile(HttpSession session,HttpServletResponse response) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(GlobalConstant.SESSION_INFO);
		String filePath=(String) CacheUtils.getCache("file_"+sessionInfo.getId());
		String fileName=(String) CacheUtils.getCache("name_"+sessionInfo.getId());
		ExportUtils.download_fb(filePath, fileName, response);
	}
}
