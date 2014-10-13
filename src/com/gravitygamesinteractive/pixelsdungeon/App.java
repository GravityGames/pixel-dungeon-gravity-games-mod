package com.gravitygamesinteractive.pixelsdungeon;

import java.io.IOException;
import java.io.InputStream;

import com.peculiargames.andmodplug.PlayerThread;
import com.watabou.noosa.Game;

import android.content.Context;
import android.os.Bundle;

public class App extends PixelDungeon{

    private static Context mContext;
    private static PlayerThread musicPlayer = null;
    private static byte[] moduleData;
    private static String moduleName;
   // private static int pattern=0;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        mContext = this;
        musicPlayer = new PlayerThread(0);
    }
    
    @Override
	public void onPause(){
    	super.onPause();
    	stopModMusic();
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	if (musicPlayer == null) {
    		musicPlayer = new PlayerThread(0);
    		if(moduleData == null){
    			//pattern=0;
    			try {
    				InputStream moduleFile = Game.instance.getAssets().open(moduleName);
    				moduleData = new byte[moduleFile.available()];
    				//moduleFile.read(moduleData);
    				moduleFile.read(moduleData,0, moduleFile.available());
    				
    			} catch (IOException e) {
    				System.exit(0);
    			}
    		}
    		//musicPlayer.setCurrentPattern(pattern);
    		App.setModuleData(moduleData);
    		startModMusic();
    	}
    	if(musicPlayer != null){
    	//startModMusic();
    		}
    }

    public static Context getContext(){
        return mContext;
    }
    
    public static PlayerThread getMusicPlayer(){
    	return musicPlayer;
    }
    
    public static void startModMusic(){
    	//if(pattern != null){
    		//musicPlayer.setCurrentPattern(pattern);
    	//}
    	musicPlayer.start();
    	musicPlayer.UnPausePlay();
    }
    
    public static void pauseModMusic(){
    	//pattern=musicPlayer.getCurrentPattern();
    	musicPlayer.PausePlay();
    }
    
    public static void switchModMusic(byte[] modData){
    	//pattern=musicPlayer.getCurrentPattern();
    	musicPlayer.PausePlay();
    	musicPlayer.UnLoadMod();
    	moduleData = modData;
    	musicPlayer.LoadMODData(modData);
    	musicPlayer.UnPausePlay();
    }
    
    public static void stopModMusic(){
    	if(musicPlayer != null){
    		//pattern=musicPlayer.getCurrentPattern();
    		musicPlayer.PausePlay();
    	
    	boolean retry = true;

    	// now close and join() the mod player thread
    	musicPlayer.StopThread();
        while (retry) {
        	try {
        		musicPlayer.join();
        		retry = false;
        	} catch (InterruptedException e) {
        		// keep trying to close the player thread
        	}
        }
        PlayerThread.CloseLIBMODPLUG();

        musicPlayer.InvalidatePlayer();

        musicPlayer = null;
    	}
    }
    
    public static void setModuleData(byte[] modData){
    	//pattern=0;
    	moduleData = modData;
    	musicPlayer.LoadMODData(modData);
    }
    
    public static void setVolume(int volume){
    	musicPlayer.setVolume(volume);
    }
    
    public static void setModuleFileName(String filename){
    	moduleName = filename;
    }
}