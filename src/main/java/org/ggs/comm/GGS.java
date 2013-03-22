package org.ggs.comm;

import java.util.Properties;


public class GGS {
	/**
	 * 系统配置文件
	 * */
	public final static String CFG_PATH="/ggs.properties";
	/**
	 * WEB
	 * */
	/**
	 * Action包路径，扫描根目录
	 * */
	public static String ACTION_PACKAGE = "com";
	/**
	 * 上传路径
	 * */
	public static String UPLOAD_PATH="/upload";

    /**
     * 编码
     * */
    public static String ENCODING="UTF-8";

	/***
	 * JDBC
	 * */
    /**
     * JDBC驱动
     * */
 	public  static String JDBC_DRIVER;
    /**
     * JDBC连接串
     * */
	public  static String JDBC_URL ;
    /**
     * 用户名
     * */
	public  static String JDBC_USER;
    /**
     * 密码
     * */
	public  static String JDBC_PASSWORD;
    /**
     * 最小连接数
     * */
	public  static int JDBC_MinPoolSize=5;
    /**
     * 最大连接数
     * */
    public  static int JDBC_MaxPoolSize=20;
    /**
     * 用完连接一次性创建的连接数
     * */
	public  static int JDBC_AcquireIncrement=5;

	
	static{
        //读取配置
		Properties pro = new Properties();
		try {
			pro.load(GGS.class.getResourceAsStream(GGS.CFG_PATH));
			ACTION_PACKAGE= pro.getProperty("ACTION_PACKAGE");
			UPLOAD_PATH=pro.getProperty("UPLOAD_PATH");
            ENCODING=pro.getProperty("ENCODING");
			
			JDBC_DRIVER=pro.getProperty("JDBC_DRIVER");
			JDBC_URL=pro.getProperty("JDBC_URL");
			JDBC_USER=pro.getProperty("JDBC_USER");
			JDBC_PASSWORD=pro.getProperty("JDBC_PASSWORD");
            JDBC_MinPoolSize=Integer.parseInt(pro.getProperty("JDBC_MinPoolSize"));
            JDBC_AcquireIncrement=Integer.parseInt(pro.getProperty("JDBC_AcquireIncrement"));
            JDBC_MaxPoolSize=Integer.parseInt(pro.getProperty("JDBC_MaxPoolSize"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


}
