package com.kok.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class MusicInfoManager {
	private static MusicInfoManager manager;
	private DBOperation operation;

	private MusicInfoManager() {	
		operation = DBOperation.getInstance();
	}

	public static MusicInfoManager getInstance() {	
		if(manager==null){
			manager = new MusicInfoManager();
		}
		return manager;
	}

	// 根据musicId查询单个数据体
	public Map<String, String> getSingleMusicInfo(String musicId) {
		String sql = "select * from musicinfo where music_id = '" + musicId + "'";
		Map<String, String> musicInfo = operation.query(sql);
		System.out.println(musicInfo.get("music_id"));
		return musicInfo;
	}

	public ArrayList<Map<String, String>> getMulMusicInfo(String field,String arg) {
		String sql = "select * from musicinfo ";
		switch (field) {
		case "music_title":
			sql += "where music_title = '" + arg + "'";
			break;
		case "music_year":
			sql += "where music_year = '" + arg + "'";
			break;
		case "music_type":
			sql += "where music_type = '" + arg + "'";
			break;
		case "album_id":
			sql += "where album_id = '" + arg + "'";
			break;
		case "artist_id":
			sql += "where artist_id = '" + arg + "'";
			break;
		default:
			break;
		}
		
		ArrayList<Map<String, String>> musicInfos = operation.queryList(sql);
		System.out.println(musicInfos.size());
		System.out.println(sql);
		return musicInfos;
	}
	
	public boolean insert(String musicpath){
		
		try {
			File file = new File(musicpath);
			MP3File mp3file = (MP3File)AudioFileIO.read(file);
			Tag tag        = mp3file.getTag();
//			ID3v1Tag         v1Tag  = (ID3v1Tag)tag;
			AbstractID3v2Tag v2tag  = mp3file.getID3v2Tag();
			ID3v24Tag        v24tag = (ID3v24Tag)mp3file.getID3v2TagAsv24();
			
			String title,year,album,artist,type,url,size,artistId;
			long filesize = file.length();
			if(v2tag.getFirst(FieldKey.TITLE)!=null&&v2tag.getFirst(FieldKey.TITLE).equals("")){
				title = v2tag.getFirst(FieldKey.TITLE);
				 System.out.println(title);
			}else{
				title = v24tag.getFirst(ID3v24FieldKey.TITLE);
				 System.out.println(title);
			}
			
			if(v2tag.getFirst(FieldKey.YEAR)!=null&&!v2tag.getFirst(FieldKey.YEAR).equals("")){
				year = v2tag.getFirst(FieldKey.YEAR);
			}else{
				year = v24tag.getFirst(FieldKey.YEAR);
			}
			
			if(v2tag.getFirst(FieldKey.ALBUM)!=null&&!v2tag.getFirst(FieldKey.ALBUM).equals("")){
				album = v2tag.getFirst(FieldKey.ALBUM);
			}else{
				artist = v24tag.getFirst(FieldKey.ARTIST);
			}
			size = FileUtil.formetFileSize(filesize);
			type = "伤感";
			url = "/Resources/Musics/xxx.mp3";
			artistId = "122";
			String sql = "insert into musicinfo(music_id,music_title,music_type,music_year,music_size,music_url,artist_id) values('222','"+title+"','"+type+"','"+year+"','"+size+"','"+url+"','"+artistId+"')";
			
			return operation.update(sql);           
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
 
}
