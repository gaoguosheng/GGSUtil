package org.ggs.util;

/* 
 * Version information

 *

 * Date:2008-6-26

 *

 * Copyright (C) 2008 Chris.Tu
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * java文件操作工具类
 * 
 * @author ggs 高国生 
 * 
 * @version 2008-6-26
 * 
 */

public class FileUtil {

	private static void createDirectory(String directory, String subDirectory) {

		String dir[];

		File fl = new File(directory);

		try {

			if (subDirectory == "" && fl.exists() != true) {

				fl.mkdir();

			} else if (subDirectory != "") {

				dir = subDirectory.replace('\\', '/').split("/");

				for (int i = 0; i < dir.length; i++) {

					File subFile = new File(directory + File.separator + dir[i]);

					if (subFile.exists() == false)

						subFile.mkdir();

					directory += File.separator + dir[i];

				}

			}

		} catch (Exception ex) {

			System.out.println(ex.getMessage());

		}

	}

	/**
	 * 
	 * 拷贝文件夹中的所有文件到另外一个文件夹
	 * 
	 * @param srcDirector
	 *            源文件夹
	 * 
	 * @param desDirector
	 *            目标文件夹
	 * 
	 */

	public static void copyFileWithDirector(String srcDirector,
			String desDirector) throws IOException {

		(new File(desDirector)).mkdirs();

		File[] file = (new File(srcDirector)).listFiles();

		for (int i = 0; i < file.length; i++) {

			if (file[i].isFile()) {

				FileInputStream input = new FileInputStream(file[i]);

				FileOutputStream output = new FileOutputStream(desDirector
						+ "/" + file[i].getName());

				byte[] b = new byte[1024 * 5];

				int len;

				while ((len = input.read(b)) != -1) {

					output.write(b, 0, len);

				}

				output.flush();

				output.close();

				input.close();

			}

			if (file[i].isDirectory()) {

				copyFileWithDirector(srcDirector + "/" + file[i].getName(),
						desDirector + "/" + file[i].getName());

			}

		}

	}

	/**
	 * 
	 * 删除文件夹
	 * 
	 * @param folderPath
	 *            folderPath 文件夹完整绝对路径
	 * 
	 */

	public static void delFolder(String folderPath) throws Exception {

		// 删除完里面所有内容

		delAllFile(folderPath);

		String filePath = folderPath;

		filePath = filePath.toString();

		File myFilePath = new File(filePath);

		// 删除空文件夹

		myFilePath.delete();

	}

	/**
	 * 
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹完整绝对路径
	 * 
	 */

	public static boolean delAllFile(String path) throws Exception {

		boolean flag = false;

		File file = new File(path);

		if (!file.exists()) {

			return flag;

		}

		if (!file.isDirectory()) {

			return flag;

		}

		String[] tempList = file.list();

		File temp = null;

		for (int i = 0; i < tempList.length; i++) {

			if (path.endsWith(File.separator)) {

				temp = new File(path + tempList[i]);

			} else {

				temp = new File(path + File.separator + tempList[i]);

			}

			if (temp.isFile()) {

				temp.delete();

			}

			if (temp.isDirectory()) {

				// 先删除文件夹里面的文件

				delAllFile(path + "/" + tempList[i]);

				// 再删除空文件夹

				delFolder(path + "/" + tempList[i]);

				flag = true;

			}

		}

		return flag;

	}
	
	/***
	 * 获取文件扩展名
	 * @param filename
	 * 		文件名
	 * */

	public static String getFileExt(String filename){
		String ext = "";
		int i = filename.lastIndexOf(".");		
		if (i>=0)
			ext = filename.substring(i);
		return ext;
	}
	
	/**
	 * 创建一个文件夹
	 * @param path 路径
	 * @return 返回是否成功
	 * */
	public static boolean createFolder(String path){
		return new File(path).mkdir();
	}
	
	/**
	 * 拷贝文件
	 * @param sourcePath 源文件路径
	 * @param descPath 目标文件路径
	 * @return 返回是否成功
	 * */
	public static boolean copyFile(String sourcePath,String descPath){
		boolean t = false;
		try {
			FileInputStream in = new FileInputStream(sourcePath);
			FileOutputStream out = new FileOutputStream(descPath);
			byte []b = new byte[4096];
			int len = 0;
			while((len =in.read(b))!=-1){
				out.write(b, 0, len);
			}
			out.flush();
			out.close();
			in.close();
			t= true;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return t;
	}
	
	/**
	 * 删除一个文件
	 * */
	public static boolean deleteFile(String path){
		return new File(path).delete();
	}

	/**
	 * 判断文件或者文件夹是否存在
	 * */
	public static boolean isExist(String path){
		return new File(path).exists();
	}
	
	/**
	 * 判断某个路径下面的文件夹列表中，将文件夹为空的列表显示出来
	 * */
	public static Object[] dirListEmpty(String path){
		List list = new ArrayList();
		File dir = new File(path);
		if (dir.exists()){
			File files[] = dir.listFiles();
			if (files==null){
				System.out.println("没有文件夹");
			}else{
				for(File file :files){
					File afiles[]  = file.listFiles();	
					if (afiles==null || (afiles!=null && afiles.length==0)){
						list.add(file.getName());
					}		
					
				}				
			}
			
		}else{
			System.out.println("该目录不存在");
		}
		return list.toArray();
	}
	
	
	public static void main(String[] args) {
		Object []  o = FileUtil.dirListEmpty("D:\\works\\01博洋教育\\02班级\\2009\\JN0905\\作业提交\\01项目需求和WEB基础");
		System.out.println(o.length);
		for(Object oo:o){
			System.out.println(oo);
		}
	}
}
