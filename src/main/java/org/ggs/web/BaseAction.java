package org.ggs.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.ggs.comm.GGS;
import org.ggs.web.bean.FileBean;
import org.ggs.web.bean.UploadBean;


/*******************************************************************************
 * @author GGS 业务类基类 辅控制器
 */
public class BaseAction {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected ServletContext servletContext;
	protected Object model;

	/**
	 * 执行Action方法前
	 * */
	protected void beforeAction(){
		
	}
	/**
	 * 执行Action方法后
	 * */
	protected void afterAction(){
		
	}
	

	
	

	public void setRequest(HttpServletRequest request) {
		this.request = request;
		this.session = this.request.getSession();
		this.servletContext = this.session.getServletContext();
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;	
	}

	/**
	 * 请求转发
	 */
	public void forward(String path) {
		try {
			this.request.getRequestDispatcher(path).forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 重定向
	 */
	public void redirect(String path) {
		try {
			this.response.sendRedirect(this.request.getContextPath() + path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 输出
	 */
	public void out(String html) {		
		try {
			response.getWriter().print( html);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * 获取上传参数
	 */
	public UploadBean getUploadBean() {
		UploadBean uploadBean = new UploadBean();
		Map<String, String> paramMap =uploadBean.getParams();
		try {
			DiskFileUpload dfu = new DiskFileUpload(); // 获取文件上传处理对象.
			dfu.setSizeMax(1024 * 1024 * 100);// 设置允许上传文件大小上限.
			dfu.setSizeThreshold(4096);// 设置缓冲区大小.
			// dfu.setRepositoryPath(this.getServletContext().getRealPath("/tmp"));//
			// 设置临时文件夹.
			List list = dfu.parseRequest(request);
			// 解析request.
			Iterator it = list.iterator();
			while (it.hasNext()) {
				FileItem fi = (FileItem) it.next();
				if (fi.isFormField()) {// 如果是普通文本.
					String tmp = fi.getString();					
					paramMap.put(fi.getFieldName(), tmp);
				} else {// 否则是文件.
					String tmpFileName = "";
					if (!fi.getName().equals("")) {// 如果有传文件.
						tmpFileName = getRndFileName(fi.getName());
						// 将文件保存到服务器.
						try {
							String realpath = this.servletContext	.getRealPath(GGS.UPLOAD_PATH);
							File dir = new File(realpath);
							if(!dir.exists()){
								//如果目录不存在，直接创建目录
								dir.mkdirs();
							}
							fi.write(new File(dir,tmpFileName));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						paramMap.put(fi.getFieldName(), tmpFileName);
						
						//保存文件对象
						FileBean fileBean = new FileBean();
						fileBean.setNewname(tmpFileName);
						String filename = fi.getName();
						filename = filename.substring(filename.lastIndexOf("\\")+1);
						fileBean.setOldname(filename);
						uploadBean.getFiles().add(fileBean);
						
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uploadBean;
	}

	// 最后返回的的HashMap中存的是表单元素名与值; 如果是上传文件框，值是文件名;

	// 获取随机文件名.如 aa.gif bb.jpg 等.
	private String getRndFileName(String path) {
		String rndFileName = String.valueOf(System.currentTimeMillis())
				+ String.valueOf(new Random().nextInt(100));
		String extFileName = getExName(path);
		if (!extFileName.equals("")) {
			rndFileName = rndFileName + "." + extFileName;
		}
		return rndFileName;
	}

	// 获取文件扩展名.如: gif jpg (不包括".")等.
	private String getExName(String path) {
		int strint = path.lastIndexOf(".");
		return path.substring(strint + 1);
	}





	public Object getModel() {
		return model;
	}

	/**
	 * 下载  默认为配置UPLOAD_PATH参数
	 * @param filename 下载文件名
	 * */
	public void download(String filename){
		download(GGS.UPLOAD_PATH, filename);
	}
	
	/**
	 * 下载 
	 * @param path 下载路径
	 * @param filename 下载文件名
	 * */
	public void download(String path, String filename){
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename="
				+ filename);
		String dir  = this.servletContext.getRealPath(path);		
		File file = new File(dir,filename);
		FileInputStream fis = null;
		OutputStream os = null;
		try {
			os =  response.getOutputStream();
			fis = new FileInputStream(file);			
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead); // 将文件发送到客户端
			}
			os.flush();
			os.close();
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}




}
