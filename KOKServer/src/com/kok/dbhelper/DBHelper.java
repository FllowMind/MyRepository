package com.kok.dbhelper;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBHelper {

	String user ="root";
	String password = "root";
	String driver ="com.mysql.jdbc.Driver" ;
	String url ="jdbc:mysql://127.0.0.1:3306/kok?user="+user+"&password="+password+"";
	
	
	Statement stmt =null;
	PreparedStatement pstmt =null;
	Connection conn  = null;
	ResultSet res =null;
	
	public DBHelper() {
	   getConnection();
	}
	public Connection getConnection(){
		
		
		try {
			Class.forName(driver);//加载驱动
			if(conn==null){				
				conn = DriverManager.getConnection(url);//建立连接		
				System.out.println("连接成功");
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("找不到驱动");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return conn;
	}

	public ResultSet excuteQuery(String sql){
		try {
			System.out.println("in");
			pstmt = conn.prepareStatement(sql);
			res = pstmt.executeQuery();
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
	public boolean excuteUpdate(String sql){
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
 
}
