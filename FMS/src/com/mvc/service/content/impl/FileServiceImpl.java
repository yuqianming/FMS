package com.mvc.service.content.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.content.FileServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class FileServiceImpl implements FileServiceI {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public Map<String, Object> dataGrid(String file_name, String remark,
			String org_id, PageFilter ph) throws Exception {
		// TODO Auto-generated method stub
		String sql="select t.id,t.file_name,t.remark,t.create_time,u.user_name from t_file t,user_info u where t.org_id = '"+org_id+"' and t.user_id = u.user_id ";
		if(StringUtils.hasText(file_name))
		{
			sql+=" and t.file_name like '%"+file_name+"%' ";
		}
		if(StringUtils.hasText(remark))
		{
			sql+=" and t.remark like '%"+remark+"%' ";
		}
		sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		//System.out.println("###### 公告管理："+sdf.format(new Date()));
		return result;
	}

	@Override
	public void add(MultipartFile upFile, String remark, String user_id,
			String org_id) throws Exception {
		// TODO Auto-generated method stub
		//OutputStream os = null;
		//InputStream  in = null;
		try
		{
			String file_name=upFile.getOriginalFilename();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
			String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
			String path="/fms/file/"+sdf.format(new Date())+"/";
			System.out.println("###### 文件路径："+path);
			File f=new File(path);
		    if(!f.exists()){
			        f.mkdirs();
			}
		    String file_path=path+uuid+file_name.substring(file_name.lastIndexOf("."));
		    upFile.transferTo(new File(file_path)); 
            /*byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len=0;
		    os = new FileOutputStream(path+uuid+file_name.substring(file_name.lastIndexOf(".")));
		    in=upFile.getInputStream();
		    // 开始读取
            while ((len = in.read(bs)) != -1) {
                os.write(bs, 0, len);
            }*/
			SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			jdbcTemplate.execute("insert into t_file(file_name,file_path,remark,user_id,org_id,create_time) values ('"+file_name+"','"+file_path+"','"+remark+"','"+user_id+"','"+org_id+"','"+sd.format(new Date())+"')");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception(e);
		}
		/* finally {
	            // 完毕，关闭所有链接
	            try {
	                os.close();
	                in.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	                throw new Exception(e);
	            }
	        }*/
	}

	@Override
	public Map<String,Object> getFilePathById(int id) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map=jdbcTemplate.queryForMap("select t.file_path,t.file_name from t_file t where t.id = "+id);
		return map;
	}

	@Override
	public void delete(String id) throws Exception {
		// TODO Auto-generated method stub
		String sql="delete t.* from t_file t where t.id = '"+id+"' ";
		jdbcTemplate.execute(sql);
	}
	
}
