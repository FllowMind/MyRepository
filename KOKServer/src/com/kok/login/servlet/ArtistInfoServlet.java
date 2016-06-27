package com.kok.login.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kok.model.InfoManager;
import com.kok.utils.GsonUtil;
import com.kok.utils.PrinterUtil;

/**
 * Servlet implementation class ArtistInfoServlet
 */
@WebServlet("/ArtistInfoServlet")
public class ArtistInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private InfoManager manager;
   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArtistInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
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
	
		String sql2 = "select  * from artistinfo order by hot_index desc LIMIT 0,100";
		String sql3 = "select  * from albuminfo order by hot_index desc LIMIT 0,100 ";
		//选出前500的歌曲信息
		String sql = "SELECT artist.*,artistimage.* from artistinfo artist,artistimageurl artistimage "
				+ "where artist.artist_id = artistimage.artist_id && artistimage.artist_image_type='1' "
				+ "order by artist.hot_index desc LIMIT 0,100";
		
		String field = request.getParameter("artist_id");
		if(field!=null){
			
		}
		ArrayList<Map<String, String>> userdata = manager.getMulInfos(sql);
		
//		InputStream inputStream = request.getInputStream();
		String s = request.getParameter("name");
		PrinterUtil.log("name", s);
		OutputStream outputStream = response.getOutputStream();
		ObjectOutputStream oStream = new ObjectOutputStream(outputStream);
		String json = GsonUtil.toJson(userdata);
		oStream.writeObject(json);

	}

}
