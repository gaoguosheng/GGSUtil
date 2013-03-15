package org.ggs.web;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

public class Request2Bean {

	/**
	 * 把request的getParameter参数值，赋值给bean
	 * 
	 * @param request
	 * @param bean
	 */
	public static void setRequest2Bean(HttpServletRequest request, Object bean) {
		Class c = bean.getClass();
		Method[] ms = c.getMethods();
		for (int i = 0; i < ms.length; i++) {
			String name = ms[i].getName();
			if (name.startsWith("set")) {
				Class[] cc = ms[i].getParameterTypes();
				if (cc.length == 1) {
					String type = cc[0].getName(); // parameter type
					try {
						// get property name:
						String prop = Character.toLowerCase(name.charAt(3))
								+ name.substring(4);
						// get parameter value:
						String param = request.getParameter(prop);
						if (param != null && !param.equals("")) {
							// ms[i].setAccessible(true);
							if (type.equals("java.lang.String")) {
								ms[i].invoke(bean, new Object[] { param });
							} else if (type.equals("int")
									|| type.equals("java.lang.Integer")) {
								ms[i].invoke(bean, new Object[] { new Integer(
										param) });
							} else if (type.equals("long")
									|| type.equals("java.lang.Long")) {
								ms[i].invoke(bean, new Object[] { new Long(
										param) });
							} else if (type.equals("short")
									|| type.equals("java.lang.Short")) {
								ms[i].invoke(bean, new Object[] { new Short(
										param) });
							} else if (type.equals("boolean")
									|| type.equals("java.lang.Boolean")) {
								ms[i].invoke(bean, new Object[] { Boolean
										.valueOf(param) });
							} else if (type.equals("java.util.Date")) {
								int length = param.length();
								String formatstr = "yyyy-MM-dd";
								if(length>10){
									formatstr = "yyyy-MM-dd HH:mm:ss";
								}
								SimpleDateFormat sdf = new SimpleDateFormat(
										formatstr);
								
								
								Date date = sdf.parse(param);
								if (date != null)
									ms[i].invoke(bean, new Object[] { date });
							} else {
								Object obj = cc[0].newInstance();
								setRequest2Bean(request, obj);
								ms[i].invoke(bean, new Object[] { obj });
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
