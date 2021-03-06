package org.ggs.web;

import org.ggs.comm.GGS;
import org.ggs.web.annotation.Action;
import org.ggs.web.bean.ActionBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


/*******************************************************************************
 * @author GGS Servlet 基类 控制器 
 * 由全局一个servlet类来调用其他业务类 业务类需继承BaseAction
 * 需要在配置里配置默认action包路径
 * 调用方法即BaseServlet的url+/业务类名!方法名
 * 在web.xml中配置
 *   <servlet>
    <servlet-name>BaseServlet</servlet-name>
    <servlet-class>org.ggs.web.BaseServlet</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>BaseServlet</servlet-name>
    <url-pattern>*.action</url-pattern>
  </servlet-mapping>
 * 
 * 如：在com.softfz.shop.action包下有一个类叫做UserAction，它继承了BaseAction
 * BaseAction下面有一个无返回（void）方法名叫做saveUser()的方法，那么它的访问路径就是/UserAction!saveUser.action
 * package com.softfz.shop.action;

import org.ggs.mvc.BaseAction;
@Action
public class UserAction extends BaseAction{

	public void saveUser(){
		//获取参数名
		this.getRequest().getParameter("username");
		//重定向
		this.responseRedirect("/path");
		//请求转发
		this.requestDispatch("/path");
		//输出js
		this.responseJs("alert('hello!');");
		
	}
}
 */
public class BaseServlet extends HttpServlet {	
	

	private static final String SET_REQUEST_METHOD = "setRequest";
	private static final String SET_RESPONSE_METHOD = "setResponse";
	private static final String BEFORE_ACTION_METHOD="beforeAction";
	private static final String AFTER_ACTION_METHOD="afterAction";
	private static final String GET_MODEL_METHOD="getModel";
	private HttpServletRequest request;
	private HttpServletResponse response;
    /**
     * 扫描Action元注释集合
     * */
    private Set<String> actionPkgList=new HashSet<String>();




    public BaseServlet(){
        //扫描Action包
        getPackages(GGS.ACTION_PACKAGE, Action.class);
    }



    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		init(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}	

	/***
	 * 初始化
	 * */
	public void init(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        //设置编码
        request.setCharacterEncoding(GGS.ENCODING);
        response.setCharacterEncoding(GGS.ENCODING);
		// 设置响应类型
		response.setContentType("text/html");
		// 获取响应输出流
		this.request = request;
		this.response = response;		
		// 调用方法
		this.execute();
	}

	/**
	 * 通过参数名选择执行的方法，默认为PARAM_NAME
	 */
	public void execute() {
		String uri = request.getRequestURI();
		ActionBean actionBean = getActionBean(uri);
        String pkg=actionBean.getPkg();
        String method =actionBean.getMethod();
		try {
            System.out.println("Do Action："+pkg+"."+method+"()");
            Object o   = Class.forName(pkg)
                    .newInstance();

			// 设置request
			this.callBackMethod(o, this.SET_REQUEST_METHOD, this.request);
			// 设置response
			this.callBackMethod(o, this.SET_RESPONSE_METHOD, this.response);
			//调用初始化方法 每个方法都会调用
			this.callBackMethod(o, this.BEFORE_ACTION_METHOD, null);
			//调用参数转model方法
			Object model = this.callBackMethod(o, this.GET_MODEL_METHOD, null);
			if(model!=null)
				Request2Bean.setRequest2Bean(request, model);
			// 调用方法
			this.callBackMethod(o, method, null);
			//调用Actin方法之后 每个方法都会调用
			this.callBackMethod(o, this.AFTER_ACTION_METHOD, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 回调实例方法
	 * 
	 * @param o
	 *            实例
	 * @param methodName
	 *            方法名
	 * @param params
	 *            参数列表
	 */
	private Object callBackMethod(Object o, String methodName, Object... params) {
		Object result = null;
		Method[] methods = o.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				try {
					result = 	method.invoke(o, params);
					return result;
				} catch (IllegalArgumentException e) {					
					e.printStackTrace();
				} catch (IllegalAccessException e) {					
					e.printStackTrace();
				} catch (InvocationTargetException e) {					
					e.printStackTrace();
				}
				break;
			}
		}
		return null;
	}


    private  ActionBean getActionBean(String url){
        ActionBean actionBean = new ActionBean();
        url=url.substring(url.lastIndexOf("/") + 1, url.length());
        String[]str=url.split("!");
        String actionName=str[0];
        String methodName=str[1].substring(0,str[1].indexOf("."));
        actionBean.setName(actionName);
        actionBean.setMethod(methodName);
        String pkg="";
        for(String s: actionPkgList){
            if(s.endsWith(actionName)){
                pkg=s;
                break;
            }
        }
        actionBean.setPkg(pkg);
        return actionBean;
    }
    private  void getPackages(String pkgName,Class annotationClass){
        String path  ="/"+pkgName.replaceAll("\\.", "/");
        scanPackage(GGS.class.getResource(path).getPath(),annotationClass);
    }
    private  void scanPackage(String path,Class annotationClass){
        File dir = new File(path);
        File []files  = dir.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                scanPackage(file.getPath(),annotationClass);
            }else{
                String p =file.getPath();
                int binIndex=p.indexOf("\\bin");
                int classesIndex=p.indexOf("\\classes");
                int pkgStartIndex=0;
                if(binIndex>0){
                    pkgStartIndex=binIndex+"\\bin".length()+1;
                }else if(classesIndex>0){
                    pkgStartIndex=classesIndex+"\\classes".length()+1;
                }
                String pkg = p.substring(pkgStartIndex,p.lastIndexOf(".")).replaceAll("\\\\",".");
                if(annotationClass!=null){
                    try {
                        if(Class.forName(pkg).getAnnotation(annotationClass)!=null){
                            actionPkgList.add(pkg);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    actionPkgList.add(pkg);
                }

            }
        }
    }

}

