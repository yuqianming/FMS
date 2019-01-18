package com.mvc.service.sys.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TDepartment;
import com.mvc.model.sys.TUser;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.Depart;
import com.mvc.service.sys.DepartServiceI;
import com.mvc.utils.MD5Util;
@Service
public class DepartServiceImpl implements DepartServiceI{
	@Autowired
	private BaseDaoI<TDepartment> departDao;
	@Autowired
	private BaseDaoI<TUser> userDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public List<Tree> treeGrid(String org_id) throws Exception{
		// TODO Auto-generated method stub
		List<Tree> result=new ArrayList<Tree>();
		List<TDepartment> list=departDao.find("from TDepartment t");
		for(TDepartment depart:list)
		{
			Tree t=new Tree();
			t.setId(depart.getOrg_id());
			t.setText(depart.getOrg_name());
			if(depart.getDepartment()!=null)
			{
				t.setPid(depart.getDepartment().getOrg_id());
			}
			result.add(t);
		}
		return getFatherTree(org_id,result);
	}
	
	public List<Tree> getFatherTree(String org_id,List<Tree> treeList)
	{
		List<Tree> result=new ArrayList<Tree>();
		if("all".equals(org_id))
		{
			for(Tree tree:treeList)
			{
				if(!StringUtils.hasText(tree.getPid()))
				{
					List<Tree> childList=getChildTree(tree.getId(),treeList);
					tree.setChildren(childList);
					tree.setIconCls("icon-folder");
					result.add(tree);
				}
			}
		}
		else
		{
			for(Tree tree:treeList)
			{
				if(org_id.equals(tree.getPid()))
				{
					List<Tree> childList=getChildTree(tree.getId(),treeList);
					tree.setChildren(childList);
					tree.setIconCls("icon-folder");
					result.add(tree);
				}
			}
		}
		return result;
	}
	
	public List<Tree> getChildTree(String pid,List<Tree> treeList)
	{
		List<Tree> result=new ArrayList<Tree>();
		for(Tree tree:treeList)
		{
			if(!StringUtils.hasText(tree.getPid()))
			{
				continue;
			}
			if(pid.equals(tree.getPid()))
			{
				List<Tree> childList=getChildTree(tree.getId(),treeList);
				tree.setChildren(childList);
				result.add(tree);
			}
		}
		return result;
	}

	@Override
	public List<Tree> departList(String org_id) throws Exception{
		// TODO Auto-generated method stub
		List<Tree> result=new ArrayList<Tree>();
		List<TDepartment> list=departDao.find("from TDepartment t");
		List<Tree> all=new ArrayList<Tree>();
		for(TDepartment depart:list)
		{
			Tree t=new Tree();
			t.setId(depart.getOrg_id());
			t.setText(depart.getOrg_name());
			if(depart.getDepartment()!=null)
			{
				t.setPid(depart.getDepartment().getOrg_id());
			}
			else
			{
				t.setIconCls("icon-folder");
			}
			all.add(t);
		}
		if("all".equals(org_id))
		{
			return result;
		}
		else
		{
			return getTreeList(org_id,all,result);
		}
	}
	
	private List<Tree> getTreeList(String org_id,List<Tree> list,List<Tree> result)
	{
		for(Tree t:list)
		{
			if(org_id.equals(t.getId()))
			{
				result.add(t);
			}
			if(org_id.equals(t.getPid()))
			{
				getTreeList(t.getId(),list,result);
			}
		}
		return result;
	}

	@Override
	public void add(Depart depart) throws Exception{
		// TODO Auto-generated method stub
		TDepartment t=departDao.get(TDepartment.class,depart.getOrg_id());
		if(t!=null)
		{
			throw new Exception("编码为\""+depart.getOrg_id()+"\"的机构已存在！");
		}
		else
		{
			TDepartment td=new TDepartment();
			td.setOrg_id(depart.getOrg_id());
			td.setOrg_name(depart.getOrg_name());
			TDepartment tt=new TDepartment();
			tt.setOrg_id(depart.getPid());
			td.setDepartment(tt);
			departDao.save(td);
			TUser tu=new TUser();
			tu.setDepartment(td);
			tu.setFlag("0");
			tu.setPassword(MD5Util.md5("admin"));
			tu.setUser_id(td.getOrg_id()+"_admin");
			tu.setUser_name(td.getOrg_name()+"管理员");
			userDao.save(tu);
		}
	}

	@Override
	public void edit(Depart depart) throws Exception {
		// TODO Auto-generated method stub
		TDepartment td=new TDepartment();
		td.setOrg_id(depart.getOrg_id());
		td.setOrg_name(depart.getOrg_name());
		TDepartment tt=new TDepartment();
		tt.setOrg_id(depart.getPid());
		td.setDepartment(tt);
		departDao.update(td);
	}

	@Override
	public void delete(Depart depart) throws Exception {
		// TODO Auto-generated method stub
		List<TDepartment> tList=departDao.find("from TDepartment t where t.department.org_id = '"+depart.getOrg_id()+"'");
		if(tList!=null&&tList.size()>0)
		{
			throw new Exception("机构\""+depart.getOrg_name()+"\"还有下级机构，不能删除！");
		}
		else
		{
			List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select user_id from user_info where org_id = '"+depart.getOrg_id()+"' and flag = '1'");
			if(mapList!=null&&mapList.size()>0)
			{
				throw new Exception("机构\""+depart.getOrg_name()+"\"下还有人员，不能删除！");
			}
			else
			{
				TDepartment td=new TDepartment();
				td.setOrg_id(depart.getOrg_id());
				departDao.delete(td);
				String user_id=depart.getOrg_id()+"_admin";
				userDao.executeSql("delete from user_info where user_id = '"+user_id+"'");
			}
		}
	}

}