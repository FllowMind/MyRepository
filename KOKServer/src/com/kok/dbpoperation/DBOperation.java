package com.kok.dbpoperation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.kok.dbhelper.DBHelper;
import com.mysql.jdbc.ResultSetMetaData;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

/*
 *����user����� 
 *ʹ�õ���ģʽȷ��Ψһ�ԣ�������Դ����
 * */
public class DBOperation {

	String sql = "";
	DBHelper helper;
	private static DBOperation dbOperation = new DBOperation();
	private DBOperation() {
		helper = new DBHelper();
	}
	
	public static DBOperation getInstance(){
		return dbOperation;
	}

	
	// ��ѯ����������
	public Map<String, String> query(String sql) {

		Map<String, String> result = new HashMap<>();
		ResultSet resultSet = helper.excuteQuery(sql);
		ResultSetMetaData rsmdata;

		try {
			rsmdata = (ResultSetMetaData) resultSet.getMetaData();
			System.out.println(rsmdata.getColumnCount());
			resultSet.first();
//			while (resultSet.next()) {
				for (int i = 1; i <= rsmdata.getColumnCount(); i++) {
					result.put(rsmdata.getColumnName(i), resultSet.getString(i));
					System.out.println(rsmdata.getColumnName(i));
				}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * ��ȡ���������
	 * @return
	 */
	public ArrayList<Map<String,String>> queryList(String sql){
		
    ArrayList<Map<String,String>> lists = new ArrayList<>();
    ResultSet resultSet = helper.excuteQuery(sql);
	ResultSetMetaData rsmdata;
	try {
		rsmdata = (ResultSetMetaData) resultSet.getMetaData();
		System.out.println(rsmdata.getColumnCount());
		while (resultSet.next()) {
			Map<String,String>result = new HashMap<>();
			for (int i = 1; i <= rsmdata.getColumnCount(); i++) {
				result.put(rsmdata.getColumnName(i), resultSet.getString(i));
			}
			lists.add(result);
		}
		if(lists.size()>0){
			System.out.println("��ȡ���ݳɹ���");
		}
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
		return lists;
	}

//  ��������
	public boolean update(String sql) {
       return helper.excuteUpdate(sql);
	}

}
