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
 * Servlet implementation class AlbumInfoServlet
 */
@WebServlet("/AlbumInfoServlet")
public class MusicSetInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private InfoManager manager;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MusicSetInfoServlet() {
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
	
		
		String sql3 = "select  * from albuminfo order by hot_index desc LIMIT 0,100 ";

		String type = request.getParameter("type");
		PrinterUtil.log("name", type);
		String sql = "";
		switch (type) {
		case "recommend":
			sql = "SELECT musicset.*,setimage.* from musicsetinfo musicset,musicsetimage setimage "
					+ "where musicset.musicset_id = setimage.musicset_id &&musicset.musicset_id in(SELECT musicset_id from recmusicsets)";
			break;
		case "chart":
			//选出前500的歌曲信息
			sql = "SELECT musicset.*,setimage.* from musicsetinfo musicset,musicsetimage setimage"
					+ " where musicset.musicset_id = setimage.musicset_id order by musicset.hot_index desc LIMIT 0,100";
			break;

		default:
			break;
		}
		ArrayList<Map<String, String>> userdata = manager.getMulInfos(sql);
		OutputStream outputStream = response.getOutputStream();
		ObjectOutputStream oStream = new ObjectOutputStream(outputStream);
		String json = GsonUtil.toJson(userdata);
		oStream.writeObject(json);

	}

}
