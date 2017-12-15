package com.rugovit.igrica.engine.ui.elements;

import java.util.Timer;
import java.util.TimerTask;

import rugovit.igrica.R;
import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
	public static int brGlazbeZaPustitiMapa=1;
	public static int brGlazbeZaPustitiStaza=1;
	
	public static final float maxJakostZvukaUOdnosuNaPojacanje=35;
	private static int staGlobalno;
	private static int stanLokalno;
	private static int brFaz;
	private static int staGlobalnoStara;
	private static int stanLokalnoStara;
	private static int brFazStara;
	private static MediaPlayer mediaPlayer;
	private static float    iVolume,FLOAT_VOLUME_MIN=0,FLOAT_VOLUME_MAX=25;
	private static int INT_VOLUME_MIN=0,INT_VOLUME_MAX=25;
	private static boolean mute=false;
	private static Context context;
	public static void stanjeIgre(Context
			cont,int stanjeGlobalno, int stanjeLokalno, int brFaze){
		//stanjeGlobal:
		    //0: nista gasi sve
		    //1: nalazi se u pocetnom izborniku
		    //2 nalazi se na mapi
		    //3 nalazi se u jednoj od faza
		staGlobalno=stanjeGlobalno;
		stanLokalno=stanjeLokalno;
		brFaz=brFaze;
		context=cont;
		////
		azurirajGlazbu(cont);
		////
		staGlobalnoStara=stanjeGlobalno;
		stanLokalnoStara=stanjeLokalno;
		brFazStara=brFaze;
	}
	public static void setMute(boolean isMuted){
		mute=isMuted;
		if(mute){
			staGlobalnoStara=0;
			stanLokalnoStara=0;
			brFazStara=0;
			stop(0);
		}
		else {
			azurirajGlazbu( context);
			//play(1000);
		}
	}
	public static void stopAndRelease(int fadeDuration) {
		
		staGlobalnoStara=0;
		stanLokalnoStara=0;
		brFazStara=0;
		
		if(fadeDuration>0){
			
			
	    try {
	        final Timer timer = new Timer(true);
	        TimerTask timerTask = new TimerTask()
	        {
	            @Override
	            public void run()
	            {
	            	iVolume= updateVolume(-1,mediaPlayer,iVolume);
	                if (iVolume == INT_VOLUME_MIN)
	                {
	                    // Stop and Release player after Pause music
	                	try{
	                        if(mediaPlayer!=null) {
	                	           mediaPlayer.stop();
	                 
	                             mediaPlayer.release();
	                            }
	                	}
	                	catch (Exception e){}
	                    timer.cancel();
	                    timer.purge();
	                }
	            }
	        };

	        timer.schedule(timerTask, fadeDuration);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		}
		else {
			try{
			if(mediaPlayer!=null) {
			 mediaPlayer.stop();
             mediaPlayer.release();
			}
			}
			catch (Exception e){}
		}
	}
	 public static void stop(int fadeDuration){
	if(fadeDuration>0) {
	     try {
	         // Set current volume, depending on fade or not
	    	 if(INT_VOLUME_MAX<=0)INT_VOLUME_MAX=1;
	         if (fadeDuration > 0)
	        	
	             iVolume = INT_VOLUME_MAX;
	         else
	             iVolume = INT_VOLUME_MIN;
          

	         // Start increasing volume in increments
	        final MediaPlayer tempMediaPlayer= mediaPlayer;
	         if (fadeDuration > 0)
	         {
	        	 
	        	 
	             final Timer timer = new Timer(true);
	            
	             TimerTask timerTask = new TimerTask()
	             
	             {
	            	 float temVol=updateVolume(0,tempMediaPlayer,iVolume);
	                 @Override
	                 public void run()
	                 {
	                	 
	                     temVol= updateVolume(-1,tempMediaPlayer,temVol);
	                     if (temVol <= INT_VOLUME_MIN)
	                     {
	                         // Pause music
	                    try	{// bug 3.
	                    	if(tempMediaPlayer!=null){ tempMediaPlayer.stop();
	                              timer.cancel();
	                              timer.purge();
	                    	    }
	                     }
	                    catch (Exception e){}
	                      }
	                 }
	             };
	            
	             // calculate delay, cannot be zero, set to 1 if zero
	             if(INT_VOLUME_MAX<=0)INT_VOLUME_MAX=1;
	             int delay = fadeDuration / INT_VOLUME_MAX;
	             if (delay == 0)
	                 delay = 1;

	             timer.schedule(timerTask, delay, delay);
	         }
	     } catch (Exception e) {
	         e.printStackTrace();
	   }
	}
	else {
		try{
		 mediaPlayer.stop();
		}
		catch (Exception e){}
	}
	 }
	 public  static void play(int fadeDuration)
	 {
	     //Set current volume, depending on fade or not
		 if(INT_VOLUME_MAX<=0)INT_VOLUME_MAX=1;
	     if (fadeDuration > 0) 
	         iVolume = INT_VOLUME_MIN;
	     else 
	         iVolume = INT_VOLUME_MAX;

	    // iVolume= updateVolume(0,mediaPlayer,iVolume);
	    // bug 4.
		 try{ mediaPlayer.setVolume(iVolume/100,iVolume/100);
		 }
		 catch(Exception e){
    		 
    	 }
	     //Play music
		 try{   if(!mute)  if(!mediaPlayer.isPlaying()) mediaPlayer.start();
		 }
	        catch(Exception e){
    		 
    	 }
	     //Start increasing volume in increments
	     if(fadeDuration > 0)
	     {
	         final Timer timer = new Timer(true);
	         TimerTask timerTask = new TimerTask() 
	         {
	             @Override
	             public void run() 
	             {
	            	 //iVolume= updateVolume(1,mediaPlayer,iVolume);
	            	 iVolume++;
	            	 try{
	            	  mediaPlayer.setVolume(iVolume/100,iVolume/100);
	            	 }
	            	 catch(Exception e){
	            		 
	            	 }
	                 if (iVolume >= INT_VOLUME_MAX)
	                 {
	                     timer.cancel();
	                     timer.purge();
	                 }
	             }
	         };

	         // calculate delay, cannot be zero, set to 1 if zero
	         int delay = fadeDuration/INT_VOLUME_MAX;
	         if (delay == 0) delay = 1;

	         timer.schedule(timerTask, delay, delay);
	     }
	 }
	 private static float updateVolume(int change, MediaPlayer tempMediaPlayer,float tempIVolume)
	 {
		 if(INT_VOLUME_MAX<=0)INT_VOLUME_MAX=1;
	     //increment or decrement depending on type of fade
		 tempIVolume = tempIVolume + change;

	     //ensure iVolume within boundaries
	     if (tempIVolume < INT_VOLUME_MIN)
	    	 tempIVolume = INT_VOLUME_MIN;
	     else if (tempIVolume > INT_VOLUME_MAX)
	    	 tempIVolume = INT_VOLUME_MAX;

	     //convert to float value
	     float fVolume = 1 - ((float) Math.log(INT_VOLUME_MAX - tempIVolume) / (float) Math.log(INT_VOLUME_MAX));
	     fVolume = ((float) Math.log(tempIVolume) / (float) Math.log(INT_VOLUME_MAX));

	     //ensure fVolume within boundaries
	     if(FLOAT_VOLUME_MAX<=0)FLOAT_VOLUME_MAX=1;
	     if (fVolume < FLOAT_VOLUME_MIN)
	         fVolume = FLOAT_VOLUME_MIN;
	     else if (fVolume > FLOAT_VOLUME_MAX)
	         fVolume = FLOAT_VOLUME_MAX;     
       try{
	    if(tempMediaPlayer!=null) tempMediaPlayer.setVolume(fVolume, fVolume);
       }
       catch(Exception e){
    	   tempIVolume = tempIVolume - change;
       }
	     return tempIVolume;
	 }
	 public static void pause(int fadeDuration)
	 {
	     //Set current volume, depending on fade or not
		 if(INT_VOLUME_MAX<=0)INT_VOLUME_MAX=1;
	     if (fadeDuration > 0) 
	         iVolume = INT_VOLUME_MAX;
	     else 
	         iVolume = INT_VOLUME_MIN;

	     iVolume= updateVolume(0,mediaPlayer,iVolume);

	     //Start increasing volume in increments
	     if(fadeDuration > 0)
	     {
	         final Timer timer = new Timer(true);
	         TimerTask timerTask = new TimerTask() 
	         {
	             @Override
	             public void run() 
	             {   
	            	 iVolume= updateVolume(-1,mediaPlayer,iVolume);
	                 if (iVolume == INT_VOLUME_MIN)
	                 {
	                     //Pause music
	                	 try{
	                         if (mediaPlayer.isPlaying()) mediaPlayer.pause();
	                         timer.cancel();
	                          timer.purge();
	                	 }
	                		catch (Exception e){}
	                 }
	             }
	         };

	         // calculate delay, cannot be zero, set to 1 if zero
	         int delay = fadeDuration/INT_VOLUME_MAX;
	         if (delay == 0) delay = 1;

	         timer.schedule(timerTask, delay, delay);
	     }     
	     else {
	    	try{
	    		if(mediaPlayer!=null) if (mediaPlayer.isPlaying()) mediaPlayer.pause();
	    	}
	    	catch (Exception e){}
	     }
	 }
	public static void namjestiGlasnocu(float posto){
		INT_VOLUME_MAX=(int)posto;
		FLOAT_VOLUME_MAX=posto;
		if(INT_VOLUME_MAX<=0)INT_VOLUME_MAX=1;
		if(FLOAT_VOLUME_MAX<=0)FLOAT_VOLUME_MAX=1;
		
		try{
		mediaPlayer .setVolume(posto/100,posto/100);
		}
		catch (Exception e){}
	}
	public static int getGlobalnoStanje(){
		return staGlobalno;
	}
   /////////////////////////////////////////////////////////////////////////////
 	private static void azurirajGlazbu(Context cont){
 		try{
		if(staGlobalno==1){//pocetni prozor
			if(staGlobalno!=staGlobalnoStara){
				
				if(mediaPlayer!=null) stop(2000);
			      mediaPlayer = MediaPlayer.create(cont, R.raw.fireplace);
			      mediaPlayer.setLooping(true);
			      //mediaPlayer .start();
			      play(0);
			}
		}
		else if (staGlobalno==2){// mapa
			if(staGlobalno!=staGlobalnoStara){
			   stop(2000);
			   if(brGlazbeZaPustitiMapa==1){
				   mediaPlayer = MediaPlayer.create(cont, R.raw.beethoven0symphony070allegretto0mvt02);
			   }
		
				   
			   
			   mediaPlayer.setLooping(true);
			   play(2000);
			}
		}
        else if (staGlobalno==3){// faza
        	if(staGlobalno!=staGlobalnoStara){
        		
        	  stop(2000);
       	   if(brGlazbeZaPustitiMapa==1){	 
       		   mediaPlayer = MediaPlayer.create(cont, R.raw.chopin0fantasie0impromptu0opus066);
       		   }
       	   else if(brGlazbeZaPustitiMapa==2){	 
       		   mediaPlayer = MediaPlayer.create(cont, R.raw.mozart0sonata0for0two0pianos);
       		   }
			  mediaPlayer.setLooping(true);
			  play(2000);
        	}
		}
        else if (staGlobalno==4){// kraj fazee
        	if(staGlobalno!=staGlobalnoStara){
        		 stopAndRelease(500);
        	 if(stanLokalno==1){// kraj pobjeda
        		  stop(500);
        	 }
        	 else if(stanLokalno==2){// kraj gubitak
        		 stop(500);
        	 }
        	}
		}
 		}
 		catch (Exception e){}
	}
	/////////////////////////////////////////////////////////////////////////
 	public static boolean getIsMute(){
 		return mute;
 	}
	/*
private static final String TAG = "MusicManager";
public static final int MUSIC_PREVIOUS = -1;
public static final int MUSIC_MENU = 0;
public static final int MUSIC_GAME = 1;
public static final int MUSIC_END_GAME = 2;

private static HashMap players = new HashMap();
private static int currentMusic = -1;
private static int previousMusic = -1;

public static float getMusicVolume(Context context) {
String[] volumes = context.getResources().getStringArray(R.array.volume_values);
String volumeString = PreferenceManager.getDefaultSharedPreferences(context).getString(
context.getString(R.string.key_pref_music_volume), volumes[PREF_DEFAULT_MUSIC_VOLUME_ITEM]);
return new Float(volumeString).floatValue();
}

public static void start(Context context, int music) {
start(context, music, false);
}

public static void start(Context context, int music, boolean force) {
if (!force && currentMusic > -1) {
// already playing some music and not forced to change
return;
}
if (music == MUSIC_PREVIOUS) {
Log.d(TAG, "Using previous music [" + previousMusic + "]");
music = previousMusic;
}
if (currentMusic == music) {
// already playing this music
return;
}
if (currentMusic != -1) {
previousMusic = currentMusic;
Log.d(TAG, "Previous music was [" + previousMusic + "]");
// playing some other music, pause it and change
pause();
}
currentMusic = music;
Log.d(TAG, "Current music is now [" + currentMusic + "]");
MediaPlayer mp = players.get(music);
if (mp != null) {
if (!mp.isPlaying()) {
mp.start();
}
} else {
if (music == MUSIC_MENU) {
mp = MediaPlayer.create(context, R.raw.menu_music);
} else if (music == MUSIC_GAME) {
mp = MediaPlayer.create(context, R.raw.game_music);
} else if (music == MUSIC_END_GAME) {
mp = MediaPlayer.create(context, R.raw.end_game_music);
} else {
Log.e(TAG, "unsupported music number - " + music);
return;
}
players.put(music, mp);
float volume = getMusicVolume(context);
Log.d(TAG, "Setting music volume to " + volume);
mp.setVolume(volume, volume);
if (mp == null) {
Log.e(TAG, "player was not created successfully");
} else {
try {
mp.setLooping(true);
mp.start();
} catch (Exception e) {
Log.e(TAG, e.getMessage(), e);
}
}
}
}

public static void pause() {
Collection mps = players.values();
for (MediaPlayer p : mps) {
if (p.isPlaying()) {
p.pause();
}
}
// previousMusic should always be something valid
if (currentMusic != -1) {
previousMusic = currentMusic;
Log.d(TAG, "Previous music was [" + previousMusic + "]");
}
currentMusic = -1;
Log.d(TAG, "Current music is now [" + currentMusic + "]");
}

public static void updateVolumeFromPrefs(Context context) {
try {
float volume = getMusicVolume(context);
Log.d(TAG, "Setting music volume to " + volume);
Collection mps = players.values();
for (MediaPlayer p : mps) {
p.setVolume(volume, volume);
}
} catch (Exception e) {
Log.e(TAG, e.getMessage(), e);
}
}

public static void release() {
Log.d(TAG, "Releasing media players");
Collection mps = players.values();
for (MediaPlayer mp : mps) {
try {
if (mp != null) {
if (mp.isPlaying()) {
mp.stop();
}
mp.release();
}
} catch (Exception e) {
Log.e(TAG, e.getMessage(), e);
}
}
mps.clear();
if (currentMusic != -1) {
previousMusic = currentMusic;
Log.d(TAG, "Previous music was [" + previousMusic + "]");
}
currentMusic = -1;
Log.d(TAG, "Current music is now [" + currentMusic + "]");
}*/
}