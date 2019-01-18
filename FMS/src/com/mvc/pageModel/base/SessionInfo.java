package com.mvc.pageModel.base;

import java.util.List;

public class SessionInfo implements java.io.Serializable {

	private static final long serialVersionUID = 7865864937878324050L;

	private String id;// 用户ID
	private String loginname;// 登录名
	private String orgId;
	private String orgName;

	
	private List<Tree> treeList; //横向导航菜单
	private List<Tree> childList;//点击横向菜单后的子菜单
	private String buttonList;//页面上有权限的按钮
	private List<Tree> userTree;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public List<Tree> getTreeList() {
		return treeList;
	}

	public void setTreeList(List<Tree> treeList) {
		this.treeList = treeList;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public List<Tree> getChildList() {
		return childList;
	}

	public void setChildList(List<Tree> childList) {
		this.childList = childList;
	}

	public List<Tree> getUserTree() {
		return userTree;
	}

	public void setUserTree(List<Tree> userTree) {
		this.userTree = userTree;
	}

	public String getButtonList() {
		return buttonList;
	}

	public void setButtonList(String buttonList) {
		this.buttonList = buttonList;
	}
	
}
