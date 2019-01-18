package com.mvc.service.sys.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TDepartment;
import com.mvc.model.sys.TRoleUser;
import com.mvc.model.sys.TUser;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.User;
import com.mvc.service.sys.UserAdminServiceI;
import com.mvc.utils.MD5Util;
import com.mvc.utils.StringUtil;


@Service
public class UserAdminServiceImpl implements UserAdminServiceI {

	@Autowired
	private BaseDaoI<TUser> userAdminDao;

	@Autowired
	private BaseDaoI<TDepartment> departmentDao;

	@Autowired
	private BaseDaoI<TRoleUser> roleUserDao;
	@Override
	public void add(User user) throws Exception{
		List<TUser> list = userAdminDao.find("from TUser t where t.user_id ='"+user.getUser_id()+"'");
		if(list!=null&&list.size()>0)
		{
			throw new Exception("该ID用户已存在！");
		}
		else
		{
			TUser t=new TUser();
			t.setUser_id(user.getUser_id().trim());
			t.setUser_name(user.getUser_name());
			t.setMobile(user.getMobile());
			if(StringUtils.hasText(user.getOrg_id()))
			{
				TDepartment department=new TDepartment();
				department.setOrg_id(user.getOrg_id());
				t.setDepartment(department);
			}
			t.setPassword(MD5Util.md5("000000"));
			t.setFlag("1");
			userAdminDao.save(t);
		}
	}
	
	@Override
	public void delete(String ids) {
		String[] userIds=ids.split(",");
		for(String id:userIds)
		{
			TUser u=userAdminDao.get(TUser.class,Integer.parseInt(id));
			roleUserDao.executeSql("delete from role_user where user_id ='"+u.getUser_id()+"'");
		}
		userAdminDao.executeSql("delete from user_info where id in ("+ids+")");
	}

	@Override
	public void edit(User u) {
		TUser t = userAdminDao.get(TUser.class, u.getId());
		t.setUser_name(u.getUser_name());
		t.setMobile(u.getMobile());
		if(StringUtils.hasText(u.getOrg_id()))
		{
			TDepartment department=new TDepartment();
			department.setOrg_id(u.getOrg_id());
			t.setDepartment(department);
		}
		userAdminDao.update(t);

	}

	@Override
	public List<TDepartment> getOrgList()
	{
		return departmentDao.find("from TDepartment");
	}
	
	@Override
	public int editPwd(User u) {
		int rtnType = 0;
		TUser t = userAdminDao.get(TUser.class, u.getUser_id());

		if (!StringUtil.isNull(u.getPassword()) && MD5Util.md5(u.getPassword()).equals(t.getPassword())) {
//			if (!StringUtil.isNull(u.getPasswordNew())) {
//				t.setPassword(MD5Util.md5(u.getPasswordNew()));
//				userAdminDao.update(t);
//			}
		} else {
			rtnType = 1;
		}
		return rtnType;
	}

	@Override
	public User get(String id) {
		TUser t = userAdminDao.get(TUser.class, id);
		User u = new User();
		//copyRolesToUser(t, u);
		u.setUser_id(t.getUser_id());
		u.setUser_name(t.getUser_name());
		u.setPassword(t.getPassword());
		u.setEmail(t.getEmail());
		if (t.getDepartment() != null) {
			u.setOrg_name(t.getDepartment().getOrg_name());
			u.setOrg_id(t.getDepartment().getOrg_id());
		}
		return u;
	}

	@Override
	public List<User> dataGrid(User user, PageFilter ph) {
		List<User> ul = new ArrayList<User>();
		String hql = " from TUser t "+whereHql(user)+" order by t." + ph.getSort() + " " + ph.getOrder();;
	
		List<TUser> l = userAdminDao.find(hql, ph.getPage(), ph.getRows());
		for (TUser t : l) {
			User u = new User();
			BeanUtils.copyProperties(t, u);
			//copyRolesToUser(t, u);
			if (t.getDepartment() != null) {
				u.setOrg_name(t.getDepartment().getOrg_name());
				u.setOrg_id(t.getDepartment().getOrg_id());
			}
			ul.add(u);
		}
		return ul;
	}

	@Override
	public Long count(User user, PageFilter ph) {
		String hql = " from TUser t ";
		return userAdminDao.count("select count(distinct t.user_id) " + hql + whereHql(user));
	}

	private String whereHql(User user) {
		String hql = "";
		hql += " where t.flag = '1' ";
		if (user != null) {
			if (!StringUtil.isNull(user.getUser_id())) {
				hql += " and t.user_id like '%"+user.getUser_id()+"%'";
			}
			if (!StringUtil.isNull(user.getUser_name())) {
				hql += " and t.user_name like '%"+user.getUser_name()+"%'";
			}
			if (!StringUtil.isNull(user.getOrg_name())) {
				hql += " and t.department.org_name like '%"+user.getOrg_name()+"%'";
			}
			
			hql+=" and t.department.org_id in ("+getAllOrg(user.getOrg_id())+")";
		}
		return hql;
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
	public List<Tree> listUserByOrg(String department) {
		List<Tree> lt = new ArrayList<Tree>();

		StringBuffer sql = new StringBuffer(" SELECT a.USER_ID, a.USER_NAME FROM user_info a ");
		if (!StringUtil.isNull(department)) {
			sql.append(" WHERE a.ORG_ID = '" + department + "'");
		}
		List<Object[]> userlist = userAdminDao.findBySql(sql.toString());

		if ((userlist != null) && (userlist.size() > 0)) {
			Tree tree = new Tree();

			tree.setId("0");
			tree.setText("全选");

			lt.add(tree);

			for (Object[] r : userlist) {
				tree = new Tree();
				tree.setPid("0");
				tree.setId((String) r[0]);
				tree.setText((String) r[1]);
				lt.get(0).getChildren().add(tree);
				// lt.add(tree);
			}
		}

		return lt;
	}

	@Override
	public List<HashMap<String, String>> listOrg() {

		List<TDepartment> l = null;
		l = departmentDao.find("select distinct t from TDepartment t order by t.org_id");

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		if ((l != null) && (l.size() > 0)) {
			for (TDepartment r : l) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("org_id", r.getOrg_id());
				map.put("org_name", r.getOrg_name());
				list.add(map);
			}
		}
		return list;
	}

	@Override
	public List<Tree> listOrgTree() {

		List<TDepartment> l = null;
		List<Tree> lt = new ArrayList<Tree>();
		l = departmentDao.find("select distinct t from TDepartment t where t.dr = 0 order by t.org_id");

		if ((l != null) && (l.size() > 0)) {
			for (TDepartment r : l) {
				Tree tree = new Tree();
				tree.setId(r.getOrg_id());
				if (r.getDepartment() != null) {
					tree.setPid(r.getDepartment().getOrg_id());
				}
				tree.setText(r.getOrg_name());
				lt.add(tree);
			}
		}
		return lt;

	}

	@Override
	public void reset(String ids) throws Exception {
		// TODO Auto-generated method stub
		userAdminDao.executeSql("update user_info set password = '"+MD5Util.md5("000000")+"' where id in ("+ids+")");
	}
	
//	private List<TRole> getUserRolesByIdunitId(String idunitId){
//		List<TRole> roles = new ArrayList<TRole>();
//		if(!StringUtil.isNull(idunitId)){
//			List<TIdunitRole> l = null;
//			Map<String, Object> params = new HashMap<String, Object>();
//			params.put("idunit_id", idunitId);// 自查自己岗位有权限的资源
//			l= idunitRoleDao.find("select distinct t from TIdunitRole t where t.idunit_id = :idunit_id order by t.idunit_id", params);
//			
//			if(l != null && !l.isEmpty()){
//				for (TIdunitRole r : l) {
//					TRole t = roleDao.get(TRole.class, r.getRole_id());
//					roles.add(t);
//				}
//				
//			}
//		}
//		return roles;
//	}

//	private void copyRolesToUser(TUser t, User u) {
//
//		List<TRole> selectedRoles = getUserRolesByIdunitId(t.getIdunit_id());
//		
//		List<Role> roleList = listAllRoles();
//
//		if ((selectedRoles != null) && !selectedRoles.isEmpty()) {
//
//			boolean b = false;
//			String names = "";
//			for (TRole role : selectedRoles) {
//				if (b) {
//					names += ",";
//				} else {
//					b = true;
//				}
//				names += role.getRole_name();
//			}
//			u.setRoleNames(names);
//
//			for (Role role : roleList) {
//				for (TRole selected : selectedRoles) {
//					int role_id = selected.getRole_id();
//					if (role.getRole_id() == role_id) {
//						role.setChecked("checked");
//					}
//				}
//			}
//		}
//		u.setRoleList(roleList);
//
//	}
//
//	private List<Role> listAllRoles() {
//		List<Role> allRoleList = new ArrayList<Role>();
//
//		List<TRole> l = null;
//		l = roleDao.find("select distinct t from TRole t order by t.role_id");
//
//		if ((l != null) && !l.isEmpty()) {
//			for (TRole role : l) {
//				allRoleList.add(new Role(role.getRole_id(), role.getRole_name(), "", ""));
//			}
//		}
//
//		return allRoleList;
//	}

}
