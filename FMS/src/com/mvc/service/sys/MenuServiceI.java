package com.mvc.service.sys;

import java.util.List;

import com.mvc.pageModel.base.Tree;

public interface MenuServiceI {
	public List<Tree> getMenuList(String userId);
	public List<Tree> getChildMenuListByPid(String userId,String pid);
}
