package org.ggs.util;

import org.ggs.bean.BaseBean;
import org.ggs.bean.BaseBean2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * bean工具，如两个bean互拷，map与bean互拷等
 * @author高国生
 * @version1.0
 * 
 * */
public class BeanUtil {


    /**
     * 两个对象互拷
     * */
    public static void objCopyObj(Object src,Object dest){
        Method[]srcMethods = src.getClass().getMethods();
        Method[]destMethods = dest.getClass().getMethods();

        for(Method destMethod:destMethods){
            String destMethodName =destMethod.getName();
            if(destMethodName.startsWith("set")){
                String field = destMethodName.substring(3);
                for(Method srcMethod:srcMethods){
                    String srcMethodName=srcMethod.getName();
                    if(srcMethodName.equalsIgnoreCase("get"+field)){
                        try {
                            Object srcValue = srcMethod.invoke(src);
                            destMethod.invoke(dest,srcValue);
                            break;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

        }
    }


	/**
	 * request拷贝到javabean
	 * */
	public static void requestCopyObject(HttpServletRequest request,Object o){
		Method[]methods = o.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			Method method = (Method)methods[i];
			if(method.getName().startsWith("set")){			
				String prop =method.getName().substring(3,4).toLowerCase()+ method.getName().substring(4);
				Object obj = null;
				try {					
					if(method.getParameterTypes()[0].getClass().isArray()){
						method.invoke(o, new Object[]{request.getParameterValues(prop)});
					}else{
						method.invoke(o, new Object[]{request.getParameter(prop)});	
					}										
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
				
			}
			
		}
	}

	
	
	/**
	 * map拷贝到javabean
	 * */
	public static void mapCopyObject(Map<String,Object> map,Object o){
		Method[] methods = o.getClass().getMethods();
		for (String key:map.keySet()){
			String methodstr = "set"+key.substring(0, 1).toUpperCase()+key.substring(1);
			for(Method method:methods){
				if(method.getName().equals(methodstr)){
					try {
						method.invoke(o, new Object[]{map.get(key)});
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}

	/**
	 * javabean拷贝到map
	 * */
	public static void ObjectCopyMap(Object o,Map<String,Object> map){
		Method[]methods = o.getClass().getMethods();
		for(int i=0;i<methods.length;i++){
			Method method = (Method)methods[i];
			if(method.getName().startsWith("get")){			
				String prop =method.getName().substring(3,4).toLowerCase()+ method.getName().substring(4);
				Object obj = null;
				try {
					obj = method.invoke(o, null);					
					map.put(prop, obj);					
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				
				
			}
			
		}
	}

	
	
}
