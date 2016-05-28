package com.example.administrator.kok_music_player.Utils.musicutils;

/**
 * Created by Administrator on 2016/5/27.
 */
        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileReader;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.Iterator;
        import java.util.List;
        import java.util.regex.Matcher;
        import java.util.regex.Pattern;

        import android.util.Log;

/**
 * <p>
 * LRC文件解析类
 * </p>
 */
public class LyricHelper {

    // 一行歌词的正则表达式，如：[00:11.43]XXXXXXX 或者 [01:14.54]...[02:57.12]XXXXXXXX
    private final static String LINE_REGEX = "^(\\[\\d{2,}:\\d\\d\\.\\d\\d\\]\\s*)+(.+)$";

    // 时间的正则表达式，如：[00:11.43]
    private final static String TIME_REGEX = "\\[\\d{2,}:\\d\\d\\.\\d\\d\\]";

    // 将歌词与之对应的毫秒数，保存在Map中
    private List<Lyric> mLyrics = new ArrayList<Lyric>();

    public LyricHelper() {
    }

    public LyricHelper(String path) {
        setLrcPath(path);
    }

    public void setLrcPath(String path) {
        Log.d("SHENJ.ME", "Lrc path :" + path);
        if (path == null || path.length() == 0) {
            return;
        }

        mLyrics.clear();
        List<String> lyricLines = readFile(path);
        for (Iterator<String> iter = lyricLines.iterator(); iter.hasNext(); ) {
            String line = iter.next();
            matchLyric(line);
        }

        // 根据时间排序
        Collections.sort(mLyrics, new Comparator<Lyric>() {
            @Override
            public int compare(Lyric l1, Lyric l2) {
                return l1.getTime().compareTo(l2.getTime());
            }
        });

        /*
        for (Iterator<Lyric> iter = mLyrics.iterator(); iter.hasNext();) {
            Lyric l = iter.next();
            Log.d("SHENJ.ME", l.getTime() + ":" + l.getContent());
        }
        */

    }

    public List<Lyric> getLyrics() {
        return mLyrics;
    }

    /**
     * 读行
     *
     * @param path
     * @return
     */
    private List<String> readFile(String path) {
        List<String> lyricLines = new ArrayList<String>();

        String newpath = path.replace(".mp3", ".lrc");
        newpath = newpath.replace(".ape", ".lrc");
        File testfile = new File(newpath);
        if(testfile.exists()==false){
            newpath = newpath.replace(".lrc", ".trc");
        }
        testfile = new File(newpath);
        if (testfile.exists() == false) {
            newpath = newpath.replace(".trc", ".xlrc");
        }
        testfile = new File(newpath);
        if (testfile.exists() == false) {
            newpath = newpath.replace(".xlrc", ".xtrc");
        }
        Log.i(this.getClass().getName(), newpath);
        File file = new File(newpath);
        if (file.exists() && !file.isDirectory()) {
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    lyricLines.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (fileReader != null) {
                        fileReader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("SHENJ.ME", "file.exists() = " + file.exists() + ", file.isDirectory() = " +
                    file.isDirectory());
        }
        return lyricLines;
    }

    /**
     * 解析某行歌词
     *
     * @param line
     */
    private void matchLyric(String line) {
        Pattern pattern = Pattern.compile(LINE_REGEX);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            return;
        }
        String lrc = matcher.group(2);
        if (lrc == null || lrc.length() == 0) {
            return;
        }

        pattern = Pattern.compile(TIME_REGEX);
        matcher = pattern.matcher(line);
        while (matcher.find()) {
            long time = getMillisTime(matcher.group());
            mLyrics.add(new Lyric(time, lrc));
        }
    }

    // 将[01:14.54]类型字符串转换成毫秒数
    private long getMillisTime(String time) {
        if (time == null || time.length() == 0) {
            return 0;
        }
        time = time.replace("[", "").replace("]", "");
        String[] min_sec = time.split(":");
        if (min_sec == null || min_sec.length != 2) {
            return 0;
        }
        long min = Long.parseLong(min_sec[0]);
        float sec = Float.parseFloat(min_sec[1]);
        return min * 60 * 1000 + (long) (sec * 1000);
    }

}

