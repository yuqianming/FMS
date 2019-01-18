package com.mvc.utils;

public class StringUtil {

	/**
	 * 格式化字符串
	 * 
	 * 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String formateString(String str, String... params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}
	
	public static boolean isNull(String str){
		if(str == null || str.length() == 0){
			return true;
		}else{
			return false;
		}
	}
	
	public static String noNullTrim(String str){
		if(str == null){
			return "";
		}else{
			return str.trim();
		}
	}
	
	public static String convertObjectToString(Object o){
		String str = "";
		if(o == null){
			return "";
		}else{
			try{
				if(o instanceof String){
					str = noNullTrim(o.toString());
				}
			}catch(Exception e){
				
			}
		}
		return str;
	}

}
