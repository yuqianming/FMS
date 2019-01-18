package com.mvc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.mvc.model.sys.TMenu;
import com.mvc.pageModel.base.Tree;

public class TreeUtils implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1176083973771031619L;
	public static List<Tree> formAsyncAjaxTreeForMenu(List<TMenu> menuList)
	{
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
		return getFatherTree(treeList);
	}
	
	public static List<Tree> getFatherTree(List<Tree> treeList)
	{
		List<Tree> result=new ArrayList<Tree>();
		for(Tree tree:treeList)
		{
			if("#".equals(tree.getUrl())&&!StringUtils.hasText(tree.getPid()))
			{
				List<Tree> childList=getChildTree(tree.getId(),treeList);
				tree.setChildren(childList);
				result.add(tree);
			}
		}
		return result;
	}
	
	public static List<Tree> getChildTree(String pid,List<Tree> treeList)
	{
		List<Tree> result=new ArrayList<Tree>();
		for(Tree tree:treeList)
		{
			if("#".equals(tree.getUrl())&&!StringUtils.hasText(tree.getPid()))
			{
				continue;
			}
			if(pid.equals(tree.getPid()))
			{
				result.add(tree);
			}
		}
		return result;
	}
}
