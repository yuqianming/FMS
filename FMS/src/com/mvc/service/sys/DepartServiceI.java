package com.mvc.service.sys;

import java.util.List;

import com.mvc.pageModel.base.Tree;
import com.mvc.pageModel.sys.Depart;

public interface DepartServiceI {
	public List<Tree> treeGrid(String org_id) throws Exception;
	public List<Tree> departList(String org_id) throws Exception;
	public void add(Depart depart) throws Exception;
	public void edit(Depart depart) throws Exception;
	public void delete(Depart depart) throws Exception;
}
