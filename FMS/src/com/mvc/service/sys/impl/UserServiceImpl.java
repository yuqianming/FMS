package com.mvc.service.sys.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvc.dao.BaseDaoI;
import com.mvc.model.sys.TUser;
import com.mvc.pageModel.sys.User;
import com.mvc.service.sys.UserServiceI;
import com.mvc.utils.MD5Util;
import com.mvc.utils.StringUtil;

@Service
public class UserServiceImpl implements UserServiceI {
	protected Log log = LogFactory.getLog(UserServiceImpl.class);
	@Autowired
	private BaseDaoI<TUser> userDao;

	@Override
	public int getLogin(User user) {
		
		int loginReturnCode = 0;
		
		String userAccount = user.getUser_id();
		
		/*String userAccountAll = ""; // 如果页面上输入的不带D 前加D 后截取后6位
		
		int len = userAccount.length();
		if(!StringUtil.isNull(userAccount)){
			userAccountAll = "D" + userAccount.substring(len < 6 ? 0 : len - 6, len);
		}*/
		
        String userPassword = user.getPassword();
        
        if(StringUtil.isNull(userAccount) || StringUtil.isNull(userPassword)){
        	return 0;
        }

        try{
            	//废弃用 本地验证
            	Map<String, Object> params = new HashMap<String, Object>();
         		params.put("user_id", userAccount);
         		TUser t = userDao.get("from TUser t where t.user_id = :user_id", params);
         		if(t!=null)
         		{
             		if(t.getPassword().equals(MD5Util.md5(userPassword))){
             			loginReturnCode = 1;
        			}    
         		}
     
       }
       catch(Exception e)
       {
    	   e.printStackTrace();
    	   loginReturnCode = 0;
       }

       return loginReturnCode;
	}

	@Override
	public User getLoginUser(String user_id) {
		TUser t = userDao.get("from TUser t  where t.user_id = '" + user_id + "'");
		User u = new User();
		if (t != null) {
			//BeanUtils.copyProperties(t, u);
			u.setUser_id(t.getUser_id());
			u.setUser_name(t.getUser_name());
			u.setEmail(t.getEmail());
			u.setMobile(t.getMobile());
			if(t.getDepartment()!=null)
			{
				u.setOrg_id(t.getDepartment().getOrg_id());
				u.setOrg_name(t.getDepartment().getOrg_name());
			}
			u.setSex(t.getSex());
			return u;
		} else {
			return null;
		}
	}

	@Override
	public void resetPassword(User user) throws Exception {
		// TODO Auto-generated method stub
		TUser t = userDao.get("from TUser t where t.user_id ='"+user.getUser_id()+"'");
		if(MD5Util.md5(user.getPassword()).equals(t.getPassword()))
		{
			t.setPassword(MD5Util.md5(user.getNewPassword()));
			userDao.update(t);
		}
		else
		{
			throw new Exception("原密码不正确！");
		}
	}

}
