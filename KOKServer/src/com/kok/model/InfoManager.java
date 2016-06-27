package com.kok.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24FieldKey;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import com.kok.dbpoperation.DBOperation;
import com.kok.utils.FileUtil;
import com.kok.utils.PrinterUtil;

import sun.security.util.Length;

public class InfoManager {

	private static volatile InfoManager manager;
	private DBOperation operation;
	private final String TAG = "InfoManager";

	private InfoManager() {
		operation = DBOperation.getInstance();
	}

	public static InfoManager getInstance() {
		if (manager == null) {
			synchronized (InfoManager.class) {
				if(manager==null){
					manager = new InfoManager();
				}
			}
			
		}
		return manager;
	}

	/**
	 * 根据查询条件查询单个数据体
	 * 
	 * @param databaseName
	 *            数据库name
	 * @param argnames
	 *            查询条件字段组
	 * @param args
	 *            字段组相应值
	 * @return
	 */
	public Map<String, String> getSingleInfo(String tableName, String[] fields, String[] argnames, String[] args) {
		String field = "";
		String sql = "";
		// 如果字段组为空则获取所有字段
		if (field.equals("")) {
			sql = "select * from " + tableName + " ";
		} else {
			for (int i = 0; i < fields.length; i++) {
				field += fields[i];
				if (i <= fields.length - 2) {
					field += ",";
				}
			}
			sql = "select " + field + " from " + tableName + " ";
		}

		if (argnames!=null) {
			sql += "where ";
			for (int i = 0; i < argnames.length; i++) {
				if (argnames[i] != null && argnames[i].equals("")) {
					sql += argnames[i] + " = '" + args[i] + "'";
					if (i <= argnames.length - 2) {
						sql += " & ";
					}
				}

			}
		}
		
		Map<String, String> albumInfo = operation.query(sql);
		/********************* 打印信息 **************/
		PrinterUtil.log(TAG,sql);
		/*******************************************/
		System.out.println(albumInfo.get("album_title"));
		return albumInfo;
	}

	/**
	 * 根据sql语句查询单个数据体
	 * @param albumId
	 * @return
	 */
	public Map<String, String> getSingleInfo(String sql) {
		Map<String, String> albumInfo = operation.query(sql);
		/********************* 打印信息 **************/
		PrinterUtil.log(TAG,sql);
		/*******************************************/
		return albumInfo;
	}

	/**
	 * 根据查询条件查询专辑
	 * 
	 * @param argnames
	 *            查询条件的字段组
	 * @param args
	 *            查询条件的相应值
	 * @param tableName
	 *            数据表名
	 * @param fields
	 *            要获取信息的字段组
	 *   @param groupby 
	 *            分组          
	 * @param orderby 
	 *            排列方式
	 * @param 
	 * @return
	 */
	public ArrayList<Map<String, String>> getMulInfos(String tableName, String[] fields, String[] argnames,
			String[] args,String groupby ,String orderby) {
		String field = "";
		String sql = "";
		// 如果字段组为空则获取所有字段
		if (field.equals("")) {
			sql = "select * from " + tableName + " ";
		} else {
			for (int i = 0; i < fields.length; i++) {
				field += fields[i];
				if (i <= fields.length - 2) {
					field += ",";
				}
			}
			sql = "select " + field + " from " + tableName + " ";
		}

		if (argnames!=null) {
			sql += "where ";
			for (int i = 0; i < argnames.length; i++) {
				if (argnames[i] != null && argnames[i].equals("")) {
					sql += argnames[i] + " = '" + args[i] + "'";
					if (i <= argnames.length - 2) {
						sql += " & ";
					}
				}

			}
		}
		//是否分组
		if(groupby!=null&&!groupby.equals("")){
			sql += " group by "+groupby;
		}
		//是否有排列条件
		if(orderby!=null&&!orderby.equals("")){
			sql +=" order by "+orderby;
		}
		
		ArrayList<Map<String, String>> musicInfos = operation.queryList(sql);
		/****************** 打印信息 **************/
		PrinterUtil.log(TAG,sql);
		/*****************************************/
		return musicInfos;
	}

	/**
	 * 根据sql语句查询专辑
	 * 
	 * @param sql
	 *            sql语句
	 * @return
	 */
	public ArrayList<Map<String, String>> getMulInfos(String sql) {

		PrinterUtil.log(TAG, sql);
		ArrayList<Map<String, String>> musicInfos = operation.queryList(sql);
		System.out.println(musicInfos.size());
		System.out.println(sql);
		return musicInfos;
	}


}
