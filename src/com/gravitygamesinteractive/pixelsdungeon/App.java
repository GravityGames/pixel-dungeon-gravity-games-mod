package com.gravitygamesinteractive.pixelsdungeon;

import com.peculiargames.andmodplug.PlayerThread;

import android.content.Context;
import android.os.Bundle;

public class App extends PixelDungeon{

    private static Context mContext;
    private static PlayerThread musicPlayer = null;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mContext = this;
        musicPlayer = new PlayerThread(0);
    }

    public static Context getContext(){
        return mContext;
    }
    
    public static PlayerThread getMusicPlayer(){
    	return musicPlayer;
    }
}