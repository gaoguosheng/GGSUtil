package org.ggs.web;

import java.io.File;

import org.ggs.comm.GGS;
import org.ggs.web.bean.ActionBean;

/***
 * 扫描包
 * */
public class ScanPackage {
	
                               /*
	public static  ActionBean getActionBean(String url){
		ActionBean actionBean = new ActionBean();
		url=url.substring(url.lastIndexOf("/") + 1, url.length());
		String[]str=url.split("!");
		String actionName=str[0];
		String methodName=str[1].substring(0,str[1].indexOf("."));		
		actionBean.setName(actionName);
		actionBean.setMethod(methodName);
		String pkg="";
		for(String s: GGS.actionList){
			if(s.endsWith(actionName)){
				pkg=s;
				break;
			}
		}
		actionBean.setPkg(pkg);
		return actionBean;
	}	
	public static  void getPackages(String pkgName,Class annotationClass){				
		String path  ="/"+pkgName.replaceAll("\\.", "/");		
		scanPackage(GGS.class.getResource(path).getPath(),annotationClass);
	}	
	private static  void scanPackage(String path,Class annotationClass){		
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
							GGS.actionList.add(pkg);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}	
				}else{
					GGS.actionList.add(pkg);
				}
							
			}			
		}
	}	
                                           */
}
