package com.mvc.service.sys;

import com.mvc.pageModel.sys.User;

public interface UserServiceI {
	public int getLogin(User user);
	public User getLoginUser(String user_id);
	public void resetPassword(User user) throws Exception;
}
