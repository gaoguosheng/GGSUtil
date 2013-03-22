package org.ggs.util;

public class NullUtil {

	/**
	 * 判断是否为空或者空串
	 */
	public static boolean isNotNull(Object o) {
		boolean t = false;
		if (o == null) {
			return t;
		}
		if (o.getClass() == String.class) {
			if (o.toString().trim().length() > 0) {
				t = true;
			}
		} else {
			t = true;
		}
		return t;
	}
	
	/**
	 * 判断是否为空/空串
	 * */
	public static boolean isNull(Object o){
		if(o==null){
			return true;
		}else{
			if(o.getClass()==String.class){
				String s = (String)o;
				if(s==null || s.trim().length()==0){
					return true;
				}else{
					return false;
				}
			}		
			return false;
		}
		
		
	}
	

	/**
	 * 判断是否为null，为null则输出空串
	 */
	public static String nullToString(Object o) {
		boolean t = false;
		if (o == null) {
			return "";
		}
		return o.toString().trim();
	}
	
	/**
	 * 判断是否为null，为null则输出0
	 */
	public static Integer nullToInt(Object o) {
		boolean t = false;
		if (o == null) {
			return 0;
		}
		return new Integer(o.toString());
	}

}
