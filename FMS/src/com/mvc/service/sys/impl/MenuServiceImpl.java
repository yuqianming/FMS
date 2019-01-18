package com.mvc.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TMenu;
import com.mvc.pageModel.base.Tree;
import com.mvc.service.sys.MenuServiceI;
import com.mvc.utils.TreeUtils;

@Service
public class MenuServiceImpl implements MenuServiceI{
	@Autowired
	private BaseDaoI<TMenu> menuDao;
	@Override
	public List<Tree> getMenuList(String userId) {
		// TODO Auto-generated method stub
		String hql="";
		if(userId.equals("admin"))
		{
			hql="select t from TMenu t where t.menu_id in ('FN0600','FN0603')";
		}
		else if(userId.contains("admin"))
		{
			hql="select t from TMenu t where t.menu_id in ('FN0600','FN0601','FN0602','FN0603')";
		}
		else
		{
			hql="select distinct t from TMenu t,TRoleMenu r,TRoleUser u where u.role_id=r.role_id and r.menu_id=t.menu_id and t.level = '1' and u.user_id = '"+userId+"' order by t.sort asc";
		}
		List<TMenu> menuList=menuDao.find(hql);
		return TreeUtils.formAsyncAjaxTreeForMenu(menuList);
	}
	@Override
	public List<Tree> getChildMenuListByPid(String userId,String pid) {
		// TODO Auto-generated method stub
		List<TMenu> menuList=menuDao.find("select distinct t from TMenu t,TRoleMenu r,TRoleUser u where u.role_id=r.role_id and r.menu_id=t.menu_id and t.parent.menu_id = '"+pid+"' and u.user_id = '"+userId+"' order by t.sort asc");
		List<Tree> treeList=new ArrayList<Tree>();
		for(TMenu menu:menuList)
		{
			Tree t=new Tree();
			t.setId(menu.getMenu_id());
			t.setText(menu.getMenu_name());
			t.setUrl(menu.getUrl());
			if(menu.getParent()!=null)
			{
				t.setPid(menu.getParent().getMenu_id());
			}
			treeList.add(t);
		}
		return treeList;
	}
}
