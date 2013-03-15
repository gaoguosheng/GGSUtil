package org.ggs.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.ggs.jdbc.JDBCUtil;
import org.ggs.orm.annotation.NotColumn;
import org.ggs.orm.annotation.PK;
import org.ggs.orm.annotation.PKType;
import org.ggs.orm.annotation.Table;
import org.ggs.orm.bean.Locate;


public class ORMUtil extends JDBCUtil {

	/**
	 * ORM保存
	 * */
	public  void save(Object o) {
		// 表名
		String table_name = getTableName(o);
		// 获得主键
		Field pkField = getPKField(o);
		// 字段名
		List<String>fields = getFields(o);
		
		PK pk = pkField.getAnnotation(PK.class);
		if (pk.value() == PKType.increment) {
			fields = getFields(o,pkField.getName());
		}	
		
		StringBuilder s2 = new StringBuilder();
		List objects = new ArrayList();
		for (String field : fields) {
			for (Method method : o.getClass().getMethods()) {
				if (method.getName().equalsIgnoreCase("get"+field)) {
					Object value = "";
					try {
						if (field.equalsIgnoreCase(pkField.getName())) {							
							if (pk.value() == PKType.increment) {
								continue;
							} else if (pk.value() == PKType.sequence) {
								value = "seq_" + table_name + ".nextval";
								s2.append(value + ",");
								continue;
							}
						}
						value = method.invoke(o, null);
						if(pk.value()==PKType.sequence){
							//oracle 转换日期型 
							if(value instanceof Date){
								String datestr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
								value = "to_date('" + datestr + "','yyyy-mm-dd hh24:mi:ss')";
								s2.append(value + ",");
								continue;
							}
						}						
						objects.add(value);
						s2.append("?,");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		String fs = "(" + listToString(fields) + ")";
		String fs2 = " values (" + s2.substring(0, s2.length() - 1) + ")";
		String sql = "insert into " + table_name + fs + fs2;
		System.out.println(sql);
		update(sql, objects.toArray());
	}

	/**
	 * ORM更新
	 * */
	public  void update(Object o) {
		// 表名
		String table_name = getTableName(o);
		// 获得主键
		Field pkField = getPKField(o);
		PK pk = pkField.getAnnotation(PK.class);
		// 字段名
		List<String>fields=getFields(o);
		StringBuilder s2 = new StringBuilder();
		List objects = new ArrayList();
		for (String field : fields) {
			if(field.equalsIgnoreCase(pkField.getName())){
				continue;
			}
			
								
			Object value =getFieldValue(o, field);
			if(pk.value()==PKType.sequence){
				//oracle 转换日期型 
				if(value instanceof Date){
					String datestr=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
					value = "to_date('" + datestr + "','yyyy-mm-dd hh24:mi:ss')";
					s2.append(field + "="+value);
					s2.append(",");
					continue;
				}
			}	
			s2.append(field + "=?,");	
			objects.add(value);			
		}
		String sql = "update " + table_name + " set " + s2.substring(0,s2.length()-1)+" where "+pkField.getName()+"=?";
		objects.add(getFieldValue(o, pkField.getName()));
		System.out.println(sql);
		update(sql, objects.toArray());
	}
	
	/***
	 * ORM获取持久对象
	 * */
	public  Object get(Class c,Object pk){
		Object result=null;
		Connection conn=null;
		PreparedStatement stm=null;
		ResultSet rs=null;
		try {
			result = c.newInstance();
			String table_name=getTableName(result);
			Field field =getPKField(result);
			Method[]methods =result.getClass().getMethods();
			String fieldname = field.getName();
			PK p = field.getAnnotation(PK.class);
			String sql ="select * from "+table_name+" where "+fieldname+"="+pk;
			conn = getConn();
			stm =conn.prepareStatement(sql);			
			rs =  stm.executeQuery();			
			ResultSetMetaData meta = stm.getMetaData(); 
			int cols = meta.getColumnCount(); 
			if(rs.next()){
				for (int i = 0; i < cols; i++)  {
					String colName = meta.getColumnName(i + 1);									
					for (int j = 0; j < methods.length; j++) {
						if (methods[j].getName().equalsIgnoreCase("set" + colName)) {					
							if(p.value()==PKType.sequence){
								//oracle
								int coltype=meta.getColumnType(i+1);
								if(coltype==2){
									//数值型 
									int scale=meta.getScale(i+1);								
									if(scale>0){
										methods[j].invoke(result, rs.getDouble(i + 1));	
									}else{
										methods[j].invoke(result, rs.getInt(i + 1));
									}								
								}else if(coltype==91){
									//日期型									
									methods[j].invoke(result, rs.getDate(i + 1));									
								}else{
									//其他型
									methods[j].invoke(result, rs.getObject(i + 1));	
								}
							}else{
								methods[j].invoke(result, rs.getObject(i + 1));	
							}
							
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeConn(conn, stm, rs);
		}
		
		return result;
	}
	/**
	 * ORM删除
	 * */
	public  void del(Object o){
		String tableName = getTableName(o);
		String pkFieldName = getPKField(o).getName();
		Object pkValue = getFieldValue(o, pkFieldName);		
		update("delete from "+tableName+" where "+pkFieldName+"=?",new Object[]{pkValue});
	}	

	/***
	 * 获取表名
	 * */
	protected  String getTableName(Object o) {
		String result = o.getClass().getAnnotation(Table.class).value();		
		return result;
	}

	/**
	 * 获得主键字段
	 * */
	protected  Field getPKField(Object o) {
		// 获得主键
		Field result = null;
		for (Field field : o.getClass().getDeclaredFields()) {
			if (field.getAnnotation(PK.class) != null) {
				result = field;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 获取字段列表
	 * */
	protected  List<String>getFields(Object o){
		List<String>result = new ArrayList<String>();
		for (Field field : o.getClass().getDeclaredFields()) {
			if (field.getAnnotation(NotColumn.class) != null) {
				continue;
			}
			result.add(field.getName());
		}
		return result;
	}
	
	/**
	 * 获取字段列表，忽略主键
	 * */
	protected  List<String>getFields(Object o,String pk){
		List<String>result = getFields(o);
		result.remove(pk);
		return result;
	}
	/**
	 * 获取字段值 
	 * */
	protected  Object getFieldValue(Object o,String fieldname){
		Object result = null;
		for (Method method : o.getClass().getMethods()) {
			if (method.getName().equalsIgnoreCase("get"+fieldname)) {				
				try {						
					result = method.invoke(o, null);		
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 链表转字符串
	 * */
	protected  String listToString(List list){
		String s=Arrays.deepToString(list.toArray());
		String result = s.substring(1,s.length()-1);
		return result;
	}
	
	public static void main(String[] args) {
		ORMUtil ormUtil = new ORMUtil();
		Locate locate = new Locate();
		locate.setDatetime(new Date(System.currentTimeMillis()));
		locate.setLat("26.019341");
		locate.setLon("119.336801");
		locate.setMobile("18959189975");
		locate.setSpeed(141.03);
		locate.setRid(108);		
		ormUtil.save(locate);
		//dbUtil.save(locate);
		//save(locate);
		//update(locate);
		/*
		Locate l = (Locate) get(Locate.class,107);
		for(String field:getFields(l)){
			System.out.println(field+"："+getFieldValue(l, field));
		}
		*/
		//del(locate);
		
	}
}
