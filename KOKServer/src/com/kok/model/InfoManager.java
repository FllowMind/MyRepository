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
	 * ���ݲ�ѯ������ѯ����������
	 * 
	 * @param databaseName
	 *            ���ݿ�name
	 * @param argnames
	 *            ��ѯ�����ֶ���
	 * @param args
	 *            �ֶ�����Ӧֵ
	 * @return
	 */
	public Map<String, String> getSingleInfo(String tableName, String[] fields, String[] argnames, String[] args) {
		String field = "";
		String sql = "";
		// ����ֶ���Ϊ�����ȡ�����ֶ�
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
		/********************* ��ӡ��Ϣ **************/
		PrinterUtil.log(TAG,sql);
		/*******************************************/
		System.out.println(albumInfo.get("album_title"));
		return albumInfo;
	}

	/**
	 * ����sql����ѯ����������
	 * @param albumId
	 * @return
	 */
	public Map<String, String> getSingleInfo(String sql) {
		Map<String, String> albumInfo = operation.query(sql);
		/********************* ��ӡ��Ϣ **************/
		PrinterUtil.log(TAG,sql);
		/*******************************************/
		return albumInfo;
	}

	/**
	 * ���ݲ�ѯ������ѯר��
	 * 
	 * @param argnames
	 *            ��ѯ�������ֶ���
	 * @param args
	 *            ��ѯ��������Ӧֵ
	 * @param tableName
	 *            ���ݱ���
	 * @param fields
	 *            Ҫ��ȡ��Ϣ���ֶ���
	 *   @param groupby 
	 *            ����          
	 * @param orderby 
	 *            ���з�ʽ
	 * @param 
	 * @return
	 */
	public ArrayList<Map<String, String>> getMulInfos(String tableName, String[] fields, String[] argnames,
			String[] args,String groupby ,String orderby) {
		String field = "";
		String sql = "";
		// ����ֶ���Ϊ�����ȡ�����ֶ�
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
		//�Ƿ����
		if(groupby!=null&&!groupby.equals("")){
			sql += " group by "+groupby;
		}
		//�Ƿ�����������
		if(orderby!=null&&!orderby.equals("")){
			sql +=" order by "+orderby;
		}
		
		ArrayList<Map<String, String>> musicInfos = operation.queryList(sql);
		/****************** ��ӡ��Ϣ **************/
		PrinterUtil.log(TAG,sql);
		/*****************************************/
		return musicInfos;
	}

	/**
	 * ����sql����ѯר��
	 * 
	 * @param sql
	 *            sql���
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
