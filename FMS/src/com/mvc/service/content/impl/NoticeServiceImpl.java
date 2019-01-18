package com.mvc.service.content.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.service.content.NoticeServiceI;
import com.mvc.utils.PackResultUtils;

@Service
public class NoticeServiceImpl implements NoticeServiceI {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Map<String,Object> dataGrid(String id,String notice_title,String notice_content,String org_id,PageFilter ph) {
		// TODO Auto-generated method stub
		String sql="select t.id,t.org_id,t.user_id,u.user_name,t.notice_title,t.notice_content,t.notice_time from t_notice t,user_info u where t.user_id = u.user_id and t.org_id = '"+org_id+"' ";
		if(StringUtils.hasText(notice_title))
		{
			sql+=" and t.notice_title like '%"+notice_title+"%' ";
		}
		if(StringUtils.hasText(notice_content))
		{
			sql+=" and t.notice_content like '%"+notice_content+"%' ";
		}
		
		if(StringUtils.hasText(id))
		{
			sql+=" and t.id = '"+id+"' ";
		}
		
		if (ph != null) {
			sql+=" order by " + ph.getSort() + " " + ph.getOrder();
		}
		
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		Map<String,Object> result=PackResultUtils.packResult(mapList, ph);
		//System.out.println("###### 公告管理："+sdf.format(new Date()));
		return result;
	}

	@Override
	public void add(String notice_title, String notice_content, String org_id,String user_id)
			throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jdbcTemplate.execute("insert into t_notice(notice_title,notice_content,notice_time,org_id,user_id) values('"+notice_title+"','"+notice_content+"','"+sdf.format(new Date())+"','"+org_id+"','"+user_id+"')");
	}

	@Override
	public void delete(String ids) throws Exception {
		// TODO Auto-generated method stub
		String sql="delete t.* from t_notice t where t.id IN ("+ids+") ";
		jdbcTemplate.execute(sql);
	}
}
