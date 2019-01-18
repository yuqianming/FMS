package com.mvc.service.sys;

import java.util.List;
import java.util.Map;

import com.mvc.pageModel.base.PageFilter;
import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.Role;
import com.mvc.pageModel.sys.User;



public interface RoleAdminServiceI {

	public List<Map<String,Object>> dataGrid(Role role, PageFilter ph) throws Exception;

	public Long count(Role role, PageFilter ph) throws Exception;
	
	public List<Tree> listMenuTree(String userId) throws Exception;
	
	public List<Tree> listUserTreeBySearch(User user) throws Exception;

	public void delete(int id) throws Exception;

	public void add(Role role) throws Exception;

	public void edit(Role role) throws Exception;

	public Role get(int id) throws Exception;
	
	public List<Tree> listUserTreeByRole(int role_id) throws Exception;

}
