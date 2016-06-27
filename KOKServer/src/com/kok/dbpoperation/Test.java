package com.kok.dbpoperation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v24Tag;

import com.kok.model.InfoManager;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
		InfoManager manager = InfoManager.getInstance();
		String[] fields = {"album_id"};
		String[]arg = {"12"};
		ArrayList<Map<String, String>> list = manager.getMulInfos("musicinfo", null, null, null,null,null);
		
		System.out.println(list.get(0).get("music_title"));
		
		
	}

}
