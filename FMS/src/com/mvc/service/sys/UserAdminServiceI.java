package com.mvc.service.sys;

import java.util.HashMap;
import java.util.List;

import com.mvc.model.sys.TDepartment;
import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.User;


public interface UserAdminServiceI {

	public List<User> dataGrid(User user, PageFilter ph) throws Exception;

	public Long count(User user, PageFilter ph) throws Exception;
	
	public void add(User user) throws Exception;

	public void delete(String id) throws Exception;
	
	public void reset(String id) throws Exception;

	public void edit(User user) throws Exception;
	
	public List<TDepartment> getOrgList() throws Exception;
	
	public int editPwd(User user);

	public User get(String id);
	
	public List<HashMap<String, String>> listOrg();
	
	public List<Tree> listOrgTree();

	List<Tree> listUserByOrg(String department);

}
