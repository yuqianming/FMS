package com.mvc.service.sys.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TDepartment;
import com.mvc.model.sys.TMenu;
import com.mvc.model.sys.TRole;
import com.mvc.model.sys.TRoleMenu;
import com.mvc.model.sys.TRoleUser;
import com.mvc.model.sys.TUser;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.Role;
import com.mvc.pageModel.sys.User;
import com.mvc.service.sys.RoleAdminServiceI;
import com.mvc.utils.StringUtil;




@Service
public class RoleAdminServiceImpl implements RoleAdminServiceI {
	protected Log log = LogFactory.getLog(RoleAdminServiceImpl.class);

	@Autowired
	private BaseDaoI<TRole> roleDao;

	@Autowired
	private BaseDaoI<TUser> userAdminDao;
	
	@Autowired
	private BaseDaoI<TMenu> menuDao;
	
	@Autowired
	private BaseDaoI<TRoleMenu> roleMenuDao;
	
	@Autowired
	private BaseDaoI<TRoleUser> roleUserDao;
	
	@Autowired
	private BaseDaoI<TDepartment> departmentDao;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Role get(int id) throws Exception{
		TRole t = roleDao.get(TRole.class, id);
		Role r = new Role();
		r.setRole_id(id);
		r.setRole_name(t.getRole_name());
		r.setRemark(t.getRemark());
		r.setCreate_time(t.getCreate_time());
		r.setOrg_id(t.getDepartment().getOrg_id());
		r.setOrg_name(t.getDepartment().getOrg_name());
		copyMenusToRole(r);
		copyUsersToRole(r);
		return r;
	}
	private void copyMenusToRole(Role r)
	{
		List<TRoleMenu> tList=roleMenuDao.find("from TRoleMenu t where t.role_id = "+r.getRole_id());
		if(tList!=null&&tList.size()>0)
		{
			String resourceIds = "";
			for(TRoleMenu t:tList)
			{
				List<TMenu> mList=menuDao.find("from TMenu t where t.parent.menu_id ='"+t.getMenu_id()+"'");
				if(mList==null||mList.size()<=0)
				{
					resourceIds+=","+t.getMenu_id();
				}
			}
			r.setResourceIds(resourceIds.substring(1));
		}
	}
	
	private void copyUsersToRole(Role r)
	{
		List<TRoleUser> uList=roleUserDao.find("from TRoleUser t where t.role_id = "+r.getRole_id());
		if(uList!=null&&uList.size()>0)
		{
			String userIds="";
			for(TRoleUser u:uList)
			{
				userIds+=","+u.getUser_id();
			}
			r.setUserIds(userIds.substring(1));
		}
	}

	@Override
	public List<Map<String,Object>> dataGrid(Role role, PageFilter ph) throws Exception{
		String sql = "";

		sql = "select t.role_id,t.role_name,t.remark,t.create_time,d.org_name "+whereHql(role)+" order by ";
		if("org_name".equals(ph.getSort()))
		{
			sql+="convert(d.org_name using gbk) ";
		}
		else if("role_name".equals(ph.getSort())||"remark".equals(ph.getSort()))
		{
			sql+="convert(t."+ph.getSort()+" using gbk) ";
		}
		else
		{
			sql+="t."+ph.getSort()+" ";
		}
		sql+=ph.getOrder()+" limit "+(ph.getPage()-1)*ph.getRows()+","+ph.getRows();
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList(sql);
		return mapList;
	}

	@Override
	public Long count(Role role, PageFilter ph) throws Exception{
		Long count=jdbcTemplate.queryForObject("select count(t.role_id) " +  whereHql(role),Long.class);
		return count;
	}

	private String whereHql(Role role) {
		String sql=" from role_info t inner join department_info d on t.org_id = d.org_id where t.org_id in ("+getAllOrg(role.getOrg_id())+")";
		if (role != null) {
			if (!StringUtil.isNull(role.getRole_name())) {
				sql += " and t.role_name like '%"+role.getRole_name()+"%'";
			}
		}
		
		return sql;
	}
	
	private String getAllOrg(String org_id)
	{
		String result="'"+org_id+"'";
		List<TDepartment> list=departmentDao.find("from TDepartment t");
		return connectOrgId(org_id,result,list);
	}
	
	private String connectOrgId(String org_id,String str,List<TDepartment> list)
	{
		for(TDepartment td:list)
		{
			if(td.getDepartment()!=null&&org_id.equals(td.getDepartment().getOrg_id()))
			{
				str+=",'"+td.getOrg_id()+"'";
				connectOrgId(td.getOrg_id(),str,list);
			}
		}
		return str;
	}


	@Override
	public List<Tree> listMenuTree(String userId) throws Exception{
		// TODO Auto-generated method stub
		List<TMenu> l = null;
		List<Tree> lt = new ArrayList<Tree>();
		String hql="";
		if(userId.endsWith("admin"))
		{
			hql="select distinct t from TMenu t order by t.sort asc";
		}
		else
		{
			hql="select distinct t from TMenu t,TRoleMenu r,TRoleUser u where t.menu_id = r.menu_id and r.role_id = u.role_id and u.user_id = '"+userId+"' order by t.sort asc";
		}
		l = menuDao.find(hql);
		
		if ((l != null) && (l.size() > 0)) {
			for (TMenu r : l) {
				
				Tree tree = new Tree();
				
				tree.setId(r.getMenu_id());
				tree.setText(r.getMenu_name());
				
				if (r.getParent()!=null) {
					tree.setPid(r.getParent().getMenu_id());
					if("menu".equals(r.getType()))
					{
						tree.setIconCls("icon-folder");
					}
					else if("button".equals(r.getType()))
					{
						tree.setIconCls("icon-btn");
					}
					
				} else {
					tree.setIconCls("icon-company");
				}
				
				//单独的路径设置处理在这里
				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("url", r.getUrl());
				
				tree.setAttributes(attr);
				
				lt.add(tree);
			}
		}
		
		return lt;
	}

	@Override
	public void add(Role role) throws Exception{

		TRole t = new TRole();
		t.setRole_name(role.getRole_name());
		t.setRemark(role.getRemark());
		TDepartment td=new TDepartment();
		td.setOrg_id(role.getOrg_id());
		t.setDepartment(td);
		t.setCreate_time(new Date());
		roleDao.save(t);
		role.setRole_id(t.getRole_id());
		addRoleMenu(role);
		addRoleUser(role);
	}
	
	private void addRoleMenu(Role role)
	{
		String resourceIds=role.getResourceIds();
		String[] menu_ids=resourceIds.split(",");
		List<String> idsParentList = new ArrayList<String>();
		for(int i=0;i<menu_ids.length;i++)
		{
			saveRoleMenu(role.getRole_id(),menu_ids[i],idsParentList);
		}
	}
	
	private void saveRoleMenu(int role_id,String menu_id,List<String> idsParentList)
	{
		idsParentList.add(menu_id);
		TRoleMenu roleMenu=new TRoleMenu();
		roleMenu.setRole_id(role_id);
		roleMenu.setMenu_id(menu_id);
		roleMenuDao.save(roleMenu);
		String parentId=getParentIdByNode(menu_id);
		if(StringUtils.hasText(parentId))
		{
			if (!idsParentList.contains(parentId)) {
			 saveRoleMenu(role_id,parentId,idsParentList);
			}
		}
	}
	
	private String getParentIdByNode(String menu_id) {
		TMenu t = menuDao.get("from TMenu t  where t.menu_id = '" + menu_id + "'");
		if (t != null) {
			if(t.getParent()!=null)
			{
				return t.getParent().getMenu_id();
			}
			return null;
		} else {
			return "";
		}
	}
	
	private void addRoleUser(Role role)
	{
		String userIds=role.getUserIds();
		if(StringUtils.hasText(userIds))
		{
			String[] ids=userIds.split(",");
			for(String id:ids)
			{
				TRoleUser tru=new TRoleUser();
				tru.setRole_id(role.getRole_id());
				tru.setUser_id(id);
				roleUserDao.save(tru);
			}
		}
	}

	/*private List<String> getAllOrg(String org_id,List<String> result)
	{
		List<Map<String,Object>> mapList=jdbcTemplate.queryForList("select org_id from department_info where pid = '"+org_id+"'");
		if(mapList!=null&&mapList.size()>0)
		{
			for(Map<String,Object> map:mapList)
			{
				result.add(map.get("org_id").toString());
				getAllOrg(map.get("org_id").toString(),result);
			}
		}
		return result;
	}*/
	
	@Override
	public List<Tree> listUserTreeBySearch(User user) throws Exception{
		// TODO Auto-generated method stub
		List<Tree> lt = new ArrayList<Tree>();
		String hql="from TUser t where t.flag = '1' and t.department.org_id = '"+user.getOrg_id()+"'";
		if(user!=null)
		{
			if(StringUtils.hasText(user.getUser_id()))
			{
				hql+=" and t.user_id like '%"+user.getUser_id()+"%'";
			}
			if(StringUtils.hasText(user.getUser_name()))
			{
				hql+=" and t.user_name like '%"+user.getUser_name()+"%'";
			}
		}
		hql+=" order by t.user_id";
		List<TUser> list = userAdminDao.find(hql);
		if(list!=null&&list.size()>0)
		{
			for(TUser u:list)
			{
				Tree tree = new Tree();
				tree.setId(u.getUser_id());
				String text=u.getUser_id()+" - "+u.getUser_name()+" - ";
				if(u.getDepartment()!=null)
				{
					text+=u.getDepartment().getOrg_name();
				}
				tree.setText(text);
				lt.add(tree);
			}
		}
		return lt;
	}
	@Override
	public void edit(Role role) throws Exception{
		// TODO Auto-generated method stub
		TRole t = roleDao.get(TRole.class, role.getRole_id());
		t.setRole_name(role.getRole_name());
		t.setRemark(role.getRemark());
		t.setCreate_time(new Date());
		roleDao.update(t);
		updateRoleMenu(role);
		updateRoleUser(role);
	}
	
	private void updateRoleMenu(Role role)
	{
		List<TRoleMenu> mList=roleMenuDao.find("from TRoleMenu t where t.role_id = "+role.getRole_id());
		if(mList!=null&&mList.size()>0)
		{
			for(TRoleMenu m:mList)
			{
				roleMenuDao.delete(m);
			}
		}
		addRoleMenu(role);
	}
	
	private void updateRoleUser(Role role)
	{
		List<Map<String,Object>> uList=jdbcTemplate.queryForList("select t.id from role_user t left join user_info u on t.user_id = u.user_id where t.role_id = "+role.getRole_id());
		if(uList!=null&&uList.size()>0)
		{
			for(Map<String,Object> u:uList)
			{
				TRoleUser ru=new TRoleUser();
				ru.setId(Integer.parseInt(u.get("id").toString()));
				roleUserDao.delete(ru);
			}
		}
		addRoleUser(role);
	}
	
	@Override
	public List<Tree> listUserTreeByRole(int role_id) throws Exception{

		List<Tree> lt = new ArrayList<Tree>();
		String hql="select t from TUser t,TRoleUser r where t.user_id = r.user_id and r.role_id = "+role_id;
		List<TUser> uList=userAdminDao.find(hql);
		if(uList!=null&&uList.size()>0)
		{
			for(TUser u:uList)
			{
				Tree tree = new Tree();
				tree.setId(u.getUser_id());
				String text=u.getUser_id()+" - "+u.getUser_name()+" - ";
				if(u.getDepartment()!=null)
				{
					text+=u.getDepartment().getOrg_name();
				}
				tree.setText(text);
				lt.add(tree);
			}
		}
		return lt;
	}

	
	@Override
	public void delete(int id) throws Exception{
		
			// 先删除role_menu关联表数据
			List<TRoleMenu> mList=roleMenuDao.find("from TRoleMenu t where t.role_id = "+id);
			if(mList!=null&&mList.size()>0)
			{
				for(TRoleMenu m:mList)
				{
					roleMenuDao.delete(m);
				}
			}

			// 先删除roleUser关联表数据
			List<TRoleUser> rList=roleUserDao.find("from TRoleUser t where t.role_id = "+id);
			if(rList!=null&&rList.size()>0)
			{
				for(TRoleUser u:rList)
				{
					roleUserDao.delete(u);
				}
			}

			TRole r = roleDao.get(TRole.class, id);
			roleDao.delete(r);
		}

}
