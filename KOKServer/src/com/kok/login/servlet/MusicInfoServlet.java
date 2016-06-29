package com.kok.login.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kok.common.User;
import com.kok.dbpoperation.DBOperation;
import com.kok.model.InfoManager;
import com.kok.model.MusicInfoManager;
import com.kok.utils.PrinterUtil;

import hebernate.ArtistimageurlHome;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class MusicInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InfoManager manager;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MusicInfoServlet() {
    	
        System.out.println("初始化音乐信息管理器");
        // TODO Auto-generated constructor stub
   	
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
           doPost(request,response);
				
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("inpost");
		request.setCharacterEncoding("utf-8");
 
		manager = InfoManager.getInstance();
	    String sql ="";
		String sql2 = "select  * from artistinfo order by hot_index desc LIMIT 0,100";
		String sql3 = "select  * from albuminfo order by hot_index desc LIMIT 0,100 ";
		//选出前500的歌曲信息
		
		
		
//		InputStream inputStream = request.getInputStream();
		String field = request.getParameter("musicset_id");
		String field2 = request.getParameter("artist_id");
		String type = request.getParameter("type");
		if(field!=null){
			sql = "select m.*,a.* from musicinfo m,artistinfo a where a.artist_id = m.artist_id "
					+ "&& m.music_id in (SELECT m.music_id from musicsetcontent m where m.musicset_id = '"+field+"') ";
		}if(field2!=null){
			sql = "select m.*,a.* from musicinfo m,artistinfo a where "
					+ "a.artist_id = m.artist_id && m.artist_id = '"+field2+"' order by m.hot_index ";
		}		
		else{
			sql = "select m.* ,artist.artist_name,album.album_title from musicinfo m ,artistinfo artist ,albuminfo album "
					+ "where m.artist_id = artist.artist_id && album.album_id = m.album_id "
					+ "order by m.hot_index desc LIMIT 0,500 ";
		}
		ArrayList<Map<String, String>> userdata = manager.getMulInfos(sql);
		PrinterUtil.log("field", field);
		OutputStream outputStream = response.getOutputStream();
		ObjectOutputStream oStream = new ObjectOutputStream(outputStream);
		String json = getJson(userdata);
		oStream.writeObject(json);
		
		 ArtistimageurlHome home = new  ArtistimageurlHome();
		 home.findById("11");
		 System.out.println("url"+ home.findById("11").getArtistImageUrl());

	}
	
	
	//将json数据转换成对象
	public static<T> T getObject(String jsonString,Class<T> cls){
		T t  =null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t ;
	}
	
	
	//将对象转换成json
	public String  getJson(Object obj){
		
		Gson gson = new Gson();
		
		return gson.toJson(obj);
	}

}
