// MusicPlayerInterface.aidl
package com.example.administrator.kok_music_player;

// Declare any non-default types here with import statements

interface MusicPlayerInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            void start();
            void pause();
            void next();
            void previous();
            void stop();
            void chooseNewSong(int musicId);
            void setPlayModel(int model);
            boolean isplaying();
            int getDuration();
            int getMusicId();
            int getcurrentposition();
            void continueToPlay();
            void choosePositonToPlay(int musicId,int position);
            void changePlayModel();
            String getMusicName();
            void requestState();
}
