// MusicSearchInterface.aidl
package com.example.administrator.kok_music_player;

// Declare any non-default types here with import statements
import com.example.administrator.kok_music_player.Utils.musicutils.MusicInfo;
interface MusicSearchInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            List getAllMusicInfro();

}
