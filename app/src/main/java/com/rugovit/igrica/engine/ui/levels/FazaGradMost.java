
package com.rugovit.igrica.engine.ui.levels;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;















import android.content.Context;
import android.view.View;


import rugovit.igrica.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.Taskbar;
import com.rugovit.igrica.engine.ui.UIManager;

public class FazaGradMost extends Activity {
	
	//DB varijable
	private String IDKoristeneFaze;
	private String idZadnjegSlota="";
	private int tezina=0;
	private HashMap<String,Integer> listaStanjaSlotova;
	private HashMap<String,String> listaImenaSlotova;
	private HashMap<String,String> koristeneFazePoSlotovima;
	private HashMap<String,Integer> koristeneFazeBrZvjezdica;
	private HashMap<String,Integer> koristeneFazeTezina;
	private HashMap<String,Integer> slotoviTezina;
	private HashMap<String,Boolean> koristeneFazeOtkljucana;
	///zvuk
	SoundPool soundPool;
	////////
	////odreðujem velicinu polja igre
	private static float xPozCm=12.5f;
	private static float yPozCm=8.5f;
	///efektivan sirina ekrana dobija se od predhodnih activitiya
	private int efekVis;
	private int efekSir;
	/////////
	
	private FazeIgre faIgr;
	private UIManager uiMan,uiMan2;
	
	private GameLogic gLog;
	private BitmapFactory.Options opts ;
	private Activity activity;
	private float xPiksCm,yPiksCm;
	private Bundle bun;
	private Bitmap pozadina;
	private Taskbar task;
	private boolean restart=false;
	private String idSlota;
	@Override
	protected void onCreate(Bundle bun) {
		super.onCreate(bun);
	
		getWindow().setFormat(PixelFormat.RGBA_4444);
		activity=this;
		 Log.d("game onCreate", "memorije nativ prije lodiranje=" + Debug.getNativeHeapAllocatedSize()/1024);
    	 Log.d("game onCreate", "memorije GlobalAllocSize prije lodiranje=" + Debug.getGlobalAllocSize()/1024);
    	 Log.d("game onCreate", "memorije ThreadAllocSize prije lodiranje=" + Debug.getThreadAllocSize()/1024);
    	  bun= getIntent().getExtras();
    	 this.efekVis=bun.getInt("vis");
         this.efekSir=bun.getInt("sir");
         this.IDKoristeneFaze=bun.getString(IgricaActivity.IDKoristeneFaze);
         idSlota=bun.getString(IgricaActivity.IDSlota);
         tezina=bun.getInt(IgricaActivity.tezina);
         lodirajIzDB();
         ucitajIzListaUVarijable();
    	// Debug.startMethodTracing("wfl14fps.f20fps");
     ////orientacija 
 		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
 		///puni ekran
 		  requestWindowFeature(Window.FEATURE_NO_TITLE);
 	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
 	                                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
 		////////	
	    //////////////    
 	 	 
	    ////PARAMETRI EKRANA
 	  
 	   double NativeHeapAllocatedSize= Debug.getNativeHeapAllocatedSize()/1024;
		double GlobalAllocSize=Debug.getGlobalAllocSize()/1024;
		double ThreadAllocSize= Debug.getThreadAllocSize()/1024;
 		    DisplayMetrics metrics = new DisplayMetrics();
		    getWindowManager().getDefaultDisplay().getMetrics(metrics);
			int ypMet=metrics.heightPixels;
			int xpMet=metrics.widthPixels;
			float xdpi=metrics.xdpi;
			float ydpi=metrics.ydpi;
			float density=metrics.densityDpi; 
			 xPiksCm=xdpi/2.54f;
			
			 yPiksCm=ydpi/2.54f;
			 /////odredivanje smanjenja
			long maxMemory=0;
	        int smanjenje=1;
	        Log.d("igrica onCreate", "memorije nativ prije lodiranje=" + Debug.getNativeHeapAllocatedSize()/1024);
	      
	        
        // odredivanje smanjenja
  	            Runtime rt = Runtime.getRuntime();
                maxMemory = rt.maxMemory()/1000000;
                if(maxMemory<18) smanjenje=4;
                else if(maxMemory>=18&&maxMemory<24) smanjenje=3;
                else  if(maxMemory>=24&&maxMemory<32) smanjenje=2;
                else  if(maxMemory>=32&&maxMemory<38) smanjenje=1;
                else smanjenje=1;
          
			opts = new BitmapFactory.Options();// stavlja da ne mjenja velicinu
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8grad0most, opts);
			int pozVis = opts.outHeight;
			int pozSir = opts.outWidth;
			opts.inDither=false;
			
			opts.inJustDecodeBounds = false;
			
		/////povecanje zbog veæeg ekrana
		if(pozSir<efekSir) {
			xPiksCm=efekSir/xPozCm;//povecava tako da ispuni ekran 
		}
		else if(xPozCm*xPiksCm<efekSir){
			xPiksCm=efekSir/xPozCm;//povecava tako da ispuni ekran 
		}
		if(pozVis<efekVis) {
			yPiksCm=efekVis/yPozCm;// povecava tako da ispuni ekran
		}
		else if(yPozCm*yPiksCm<efekVis){
			yPiksCm=efekVis/yPozCm;//povecava tako da ispuni ekran 
		}
		//opts.inScaled =false;
		opts.inSampleSize=smanjenje;
	    NativeHeapAllocatedSize= Debug.getNativeHeapAllocatedSize()/1024;
		 GlobalAllocSize=Debug.getGlobalAllocSize()/1024;
		 ThreadAllocSize= Debug.getThreadAllocSize()/1024;                             

	        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
	    //////////inicijalizacija gamelogica i uimanager pozadine
	    /////////ui manager*/
	        faIgr=new FazeIgre(5,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		    faIgr.postaviNovcePrijeDolaskaNovogVala(2);
		    faIgr.setDelayIzmVal(5);
		    faIgr.setDelayIzmVal(0,15);
		    faIgr.setDelayIzmVal(1,20);
		    faIgr.setDelayIzmVal(2,20);
		    faIgr.setDelayIzmVal(3,25);
		    faIgr.setDelayIzmVal(4,25);
		 
		    //faIgr=new FazeIgre(200,getResources(),60,60);
		    faIgr. dodajPocetneParametreIgre(100,10);
		    faIgr.dodajParametreFazeIzDB(bun.getString(IgricaActivity.IDKoristeneFaze),bun.getInt(IgricaActivity.brojZvijezdica),bun.getInt(IgricaActivity.tezina));
            
		/////TASKBAR
			 LinkedList<Typeface> listaFontova= new LinkedList<Typeface>();
			    Typeface font = Typeface.createFromAsset(
					     getAssets(), 
					    "fonts/basic.ttf");
			    listaFontova.add(font);
			task=new Taskbar(listaFontova);
			faIgr.dodajTaskbar(task);
		    uiMan=new UIManager(activity,xPiksCm,yPiksCm,efekSir,efekVis){
		    	boolean tekPoceo=true;
				RectF recCrt=null, rectLoad=null, rectLoad2=null;
				Paint paint=null,paint2=null;
				float xLoad=0;
				float yLoad=0;
				float vis=0;
				float sir=0;
		    	public void izvanThrednoZaNacrtati(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		    	
		    		faIgr.nacrtajLoadEkran(can, fps, uiMan, PpCmX, PpCmY, pomCanX, pomCanY);
		    	}
		    };
		    soundPool = new SoundPool(IgricaActivity.maxBrojZvukova,AudioManager.STREAM_MUSIC, 0);
		    dodajPozadinu();
	
		gLog=new GameLogic(20,uiMan,faIgr,activity){
			public boolean spremiSlikuNaSD(Bitmap image,String ime) {

				try {
				// Use the compress method on the Bitmap object to write image to
				// the OutputStream
				/*FileOutputStream fos = activity.openFileOutput("desiredFilename.png", Context.MODE_PRIVATE);
                fos.write(image.);
				// Writing the bitmap to the output stream
				image.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
				*/
				//b is the Bitmap
					
					
					
				//File file = new File(Environment.getExternalStorageDirectory()+ "/"+IgricaActivity.imeIgre+ "/" +  "slike/", ime+".raw"); 
					File DIR = new File(Environment.getExternalStorageDirectory().toString()+ "/"+IgricaActivity.imeIgre+ "/" +  "slike/"); 
					DIR.mkdirs();
				File file = new File(Environment.getExternalStorageDirectory().toString()+ "/"+IgricaActivity.imeIgre+ "/" +  "slike/", ime+".raw"); 
				
				//calculate how many bytes our image consists of.
				int bytes =image.getRowBytes() * image.getHeight(); //image.getByteCount();
				//or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
				//int bytes = b.getWidth()*b.getHeight()*4; 
                  
				ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
				image.copyPixelsToBuffer(buffer); //Move the byte data to the buffer

				byte[] array = buffer.array();
				
				FileOutputStream fos = new FileOutputStream(file); 
				// if(!file.exists()) {
					                 fos.write(array); 
				                     fos.close();
				                  //}
				return true;
				} catch (Exception e) {
				Log.e("saveToInternalStorage()","greska"+ e.getMessage());
				return false;
				}
				}
			public Bitmap lodirajSlikuSaSD(String ime) {

				InputStream stream;
				Bitmap bitmap=null ;
				try {
					stream = new FileInputStream(Environment.getExternalStorageDirectory().toString()+ "/"+IgricaActivity.imeIgre+ "/" +  "slike/"+ ime+".raw");
					 bitmap = BitmapFactory.decodeStream(stream, null, opts);
					 try {
						stream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return bitmap;
				}
			@Override
			public void loadSlika(){
				  ///////// ZVUK
				   soundPool = new SoundPool(IgricaActivity.maxBrojZvukova,AudioManager.STREAM_MUSIC, 0);
			        this.faIgr.lodirajResurse(activity, soundPool,opts,gLog,uiMan);
			}
			
			@Override
			public void loadMetoda() {


				// TODO Auto-generated method stub
			    ///////		
				getWindow().setFormat(PixelFormat.RGBA_4444);
				opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
			
				 float omjer=2.983f;
				 float polX=0f;
				 float polY=0.71f;
				 float sir=2f;
				 float vis=1.87f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 2.29f,1.76f,0.46f);
				 
				 polX=4.2f;
				 polY=2.54f;
				 sir=2.79f;
				 vis=2.93f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 502,0,2,polX, polY, sir,vis,10.37f, 7.55f, 2.12f,0.95f);
			
				 polX=9.53f;
				 polY=5.86f;
				 sir=3.5f;
				 vis=2.6f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 503,0,2,polX, polY, sir,vis,10.37f, 7.55f, 2.12f,0.95f);
				 polX=0.04f;
				 polY=5.96f;
				 sir=1.31f;
				 vis=1.2f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 504,0,2,polX, polY, sir,vis,0.18f, 6.6f, 1.09f,0.46f);
		  
		  /////dodavanje èlanova igrice
	 	///gornji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.7f,5.46f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.53f,1.86f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.06f,2.56f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.49f,2.7f);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.37f,3.4f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.74f,3.34f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.38f,6.61f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.44f,5.07f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.31f,5.56f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.94f,5.46f);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.36f,7.28f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.4f,6.48f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.98f,5.0f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.69f,3.66f);
	 
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.2f,3.93f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.29f,2.42f);
		  //pocetak
				int brP=-1;
				int brPGornji=0;
				int brPDonji=0;
				float xPP=0;
				float yPP=0;
				float sirP=0;
				float visP=0;
				float speed=0.0005f;
				///////
				////////////////////////////////////Ravni put 1//////////////////////////////////////////////////////////////
			    float visina=0.85f;
			    float sirina=1.23f;
			    float sirina2=1.7f;
			    ////////////////////posebni pocetni
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=10.9f;
				yPP=5.61f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=10.05f;
				yPP=5.33f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=9.21f;
				yPP=5.12f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=11.75f;
				yPP=4.45f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=10.9f;
				yPP=4.27f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=10.05f;
				yPP=4.02f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.07f;				
				xPP=9.21f;
				yPP=3.81f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    ////////////////////posebni gore -1
			     //-1
			    sirP=0.18f;
				visP=0.85f;				
				xPP=9.03f;
				yPP=2.47f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,180,-1);
			    //
			    //-1
			    sirP=1.02f;
				visP=0.18f;				
				xPP=7.66f;
				yPP=1.27f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    //-1
			    sirP=0.81f;
				visP=0.18f;				
				
				xPP=6.84f;
				yPP=1.02f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    //-1
			    sirP=0.81f;
				visP=0.18f;				
				xPP=5.96f;
				yPP=0.81f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,270, -1);
			    //
			    //-1
			    sirP=0.6f;
				visP=0.11f;				
				xPP=3.6f;
				yPP=0.88f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    ////////////posebni dolje
			    //-1
			    sirP=0.85f;
				visP=0.28f;				
				xPP=6.95f;
				yPP=6.91f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			    //
			    //-1
			    sirP=0.85f;
				visP=0.18f;				
				xPP=6.1f;
				yPP=7.2f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			    //
			     //-1
			    sirP=1.62f;
				visP=0.14f;				
				xPP=4.41f;
				yPP=7.37f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			    //  //-1
			    sirP=1.62f;
				visP=0.14f;				
				xPP=4.45f;
				yPP=6.0f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=4.09f;
				yPP=5.01f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,180,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=3.85f;
				yPP=4.16f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,180,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=3.6f;
				yPP=3.32f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,180,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=3.46f;
				yPP=2.47f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,0,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=2.26f;
				yPP=3.32f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,0,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=2.33f;
				yPP=4.16f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,0,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=2.58f;
				yPP=5.01f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,0,-1);
			    //
			    //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=2.75f;
				yPP=5.86f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,0,-1);
			    //
			  //-1
			    sirP=0.11f;
				visP=0.85f;				
				xPP=2.12f;
				yPP=2.47f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,0,-1);
			    ///////////////posebni most
			    //-1
			    sirP=0.85f;
				visP=0.11f;				
				xPP=1.69f;
				yPP=1.13f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //-1
			    sirP=0.85f;
				visP=0.11f;				
				xPP=0.85f;
				yPP=0.92f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //-1
			    sirP=0.85f;
				visP=0.11f;				
				xPP=0f;
				yPP=0.71f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,270,-1);
			    //-1
			    sirP=0.85f;
				visP=0.11f;				
				xPP=0f;
				yPP=2.01f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			  //-1
			    sirP=0.85f;
				visP=0.11f;				
				xPP=0.85f;
				yPP=2.26f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
			  //-1
			    sirP=0.42f;
				visP=0.11f;				
				xPP=1.69f;
				yPP=2.47f;
			
			    faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP, speed,90,-1);
//			    /////////////////////////////////////////////////////////////////////
			  //  NORMALNI DIO
			    /////////////////////////////////////////////////////////////////////////////////////////////////
			    //0
			    sirP=0.85f;
				visP=1.23f;				
				xPP=-0.85f;
				yPP=0.78f;
			
			    faIgr.dodajBr204Kraj(0,0,xPP, yPP, sirP, visP, -speed, 0,23); 
			    //1
				sirP=0.85f;
				visP=1.24f;				
				xPP=0f;
				yPP=0.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 170,-1);
				//2
				sirP=0.85f;
				visP=1.24f;				
				xPP=0.8f;
				yPP=1.01f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 170,-1);
				//3
				sirP=0.85f;
				visP=1.24f;				
				xPP=1.7f;
				yPP=1.22f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,180,20);
				sirP=0.85f;
				visP=0.64f;				
				xPP=2.54f;
				yPP=1.83f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,170,19);
				//4
				sirP=0.85f;
				visP=0.81f;				
				xPP=2.55f;
				yPP=1.02f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 200,18);
				//gornji dio///////////////////////////////////////////////////////////////
				brPGornji=brP;
				//5
				sirP=0.85f;
				visP=1.24f;				
				xPP=3.4f;
				yPP=1.01f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 220,12);
				//6
				sirP=0.85f;
				visP=1.24f;				
				xPP=4.25f;
				yPP=0.82f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 200,11);
				//7
				sirP=0.85f;
				visP=1.24f;				
				xPP=5.1f;
				yPP=0.82f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,10);
				//8
				sirP=0.85f;
				visP=1.24f;				
				xPP=5.94f;
				yPP=1.01f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 160,9);
				//9
				sirP=0.85f;
				visP=1.24f;				
				xPP=6.79f;
				yPP=1.22f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 150,8);
				//10
				sirP=1.24f;
				visP=1.05f;				
				xPP=7.64f;
				yPP=1.45f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 160,7);
				//11
				sirP=1.59f;
				visP=0.85f;				
				xPP=7.44f;
				yPP=2.46f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 110,6);
				//5
				sirP=1.23f;
				visP=1.2f;				
				xPP=7.97f;
				yPP=3.32f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 100,5);
				///donjidio/////////////////////////////////////////////////////////////////////
				brPDonji=brP;
				//5
				sirP=1.24f;
				visP=0.85f;				
				xPP=2.22f;
				yPP=2.46f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 90,17);
				//6
				sirP=1.24f;
				visP=0.85f;				
				xPP=2.37f;
				yPP=3.31f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 110,16);
				//7
				sirP=1.41f;
				visP=0.85f;				
				xPP=2.43f;
				yPP=4.16f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 120,15);
				//8
				sirP=1.41f;
				visP=0.85f;				
				xPP=2.68f;
				yPP=5.01f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 120,14);
				//9
				sirP=1.55f;
				visP=0.85f;				
				xPP=2.86f;
				yPP=5.86f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 120,13);
				//10
				sirP=1.24f;
				visP=0.85f;				
				xPP=3.17f;
				yPP=6.7f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 100,12);
				//11
				sirP=0.85f;
				visP=1.24f;				
				xPP=4.41f;
				yPP=6.15f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 180,11);
				//12
				sirP=0.85f;
				visP=1.24f;				
				xPP=5.26f;
				yPP=6.15f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,10);
				//9
				sirP=0.85f;
				visP=1.24f;				
				xPP=6.11f;
				yPP=5.97f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 190,9);
				//8
				sirP=0.85f;
				visP=1.24f;				
				xPP=6.96f;
				yPP=5.77f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 190,8);
				//7
				sirP=1.24f;
				visP=0.85f;				
				xPP=7.81f;
				yPP=6.05f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 200,7);
				//6
				sirP=1.24f;
				visP=0.85f;				
				xPP=7.81f;
				yPP=5.21f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 220,6);
				//5
				sirP=1.23f;
				visP=0.67f;				
				xPP=7.97f;
				yPP=4.52f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,230,5);
				///2 dio
				//4
				sirP=0.85f;
				visP=1.24f;				
				xPP=9.21f;
				yPP=3.88f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,165,4);
				//3
				sirP=0.85f;
				visP=1.24f;				
				xPP=10.05f;
				yPP=4.09f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 165,3);
				//2
				sirP=0.85f;
				visP=1.24f;				
				xPP=10.9f;
				yPP=4.36f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 165,2);
				//1
				sirP=0.95f;
				visP=1.23f;				
				xPP=11.75f;
				yPP=4.53f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 165,1);
				//0
				sirP=0.85f;
				visP=1.23f;				
				xPP=12.69f;
				yPP=4.69f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 165,0);
				////////////////////////////////////////////////////////////////////////////////
				///donjidio/////////////////////////////////////////////////////////////////////
	    // prvi valž
			
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 12.55f,5f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_7ProtivnikCistacica(0,3,12.55f,5f,0.2f);
		
		faIgr.dodajBr_7ProtivnikCistacica(0,5,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_1Radnik(0,5,12.55f,5f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 12.55f,5f,0.1f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,12.55f,5f,0.2f);
		
		faIgr.dodajBr_1Radnik(0,5,12.55f,5f,0.1f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,12.55f,5f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_5ProtivnikPocetni(0,6, 12.55f,5f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 12.55f,5f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_7ProtivnikCistacica(0,6,12.55f,5f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_6ProtivnikDebeli(0,1, 12.55f,5f,0.2f);
		
		faIgr.dodajBr_6ProtivnikDebeli(0,6,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_6ProtivnikDebeli(0,2, 12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(0,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_7ProtivnikCistacica(0,2,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.2f);
		
		faIgr.dodajBr_6ProtivnikDebeli(0,14,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,4.7f,0.1f);
		faIgr.dodajBr_6ProtivnikDebeli(0,1, 12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_1Radnik(0,5,12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(0,1, 12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(0,1,12.55f,5f,0.2f);
		
		///2. val
		
		faIgr.dodajBr_6ProtivnikDebeli(1,7,12.55f,5f,0.4f);
		faIgr.dodajBr_1Radnik(1,1,12.55f,5f,0.6f);
		faIgr.dodajBr_1Radnik(1,1,12.55f,5f,0.5f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 12.55f,5f,0.6f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,12.55f,5f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,12.55f,5f,0.4f);	
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,1,12.55f,5f,0.1f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.3f);
		
		
		faIgr.dodajBr_6ProtivnikDebeli(1,7,12.55f,5f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,12.55f,5f,0.4f);
		faIgr.dodajBr_1Radnik(1,0,12.55f,5f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 12.55f,5f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,12.55f,5f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,12.55f,5f,0.4f);
		faIgr.dodajBr_1Radnik(1,5,12.55f,5f,0.4f);
		
		faIgr.dodajBr_9ProtivnikPas(1,4,12.55f,5f,0.1f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,1,12.55f,5f,0.1f);
		
		faIgr.dodajBr_6ProtivnikDebeli(1,6,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(1,0,12.55f,5f,0.3f);
		faIgr.dodajBr_1Radnik(1,1,12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(1,0, 12.55f,5f,0.3f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,12.55f,5f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(1,0,12.55f,5f,0.3f);	
		
		faIgr.dodajBr_9ProtivnikPas(1,5,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(1,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.3f);
		
	
		faIgr.dodajBr_6ProtivnikDebeli(1,0,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(1,0,12.55f,5f,0.2f);
		faIgr.dodajBr_1Radnik(1,1,12.55f,5f,0.3f);
		faIgr.dodajBr_6ProtivnikDebeli(1,0, 12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,12.55f,5f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(1,0,12.55f,5f,0.2f);
		
		faIgr.dodajBr_9ProtivnikPas(1,5,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(1,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(1,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,12.55f,5f,0.1f);
		
		//3.val 
		faIgr.dodajBr_10ProtivnikPolicajac(2,0,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,3,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,2,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,2,12.55f,5f,0.7f);	
		
		faIgr.dodajBr_9ProtivnikPas(2,9,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.9f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.8f); 
		faIgr.dodajBr_1Radnik(2,7,12.55f,5f,0.7f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 12.55f,5f,0.8f);
		faIgr.dodajBr_1Radnik(2,1,12.55f,5f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0,12.55f,5f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,12.55f,5f,0.7f);
		
		faIgr.dodajBr_10ProtivnikPolicajac(2,12,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,12.55f,5f,0.9f);
		faIgr.dodajBr_1Radnik(2,1,12.55f,5f,0.7f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 12.55f,5f,0.9f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,5,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.9f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.8f); 
		
		faIgr.dodajBr_10ProtivnikPolicajac(2,12,12.55f,5f,0.1f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,12.55f,5f,0.3f);
		faIgr.dodajBr_1Radnik(2,1,12.55f,5f,0.3f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 12.55f,5f,0.3f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(2,1,12.55f,5f,0.1f);
		faIgr.dodajBr_9ProtivnikPas(2,0,12.55f,5f,0.2f); 

		faIgr.dodajBr_1Radnik(2,7,12.55f,5f,0.1f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,2,12.55f,5f,0.2f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,12.55f,5f,0.3f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,2,12.55f,5f,0.1f);
		faIgr.dodajBr_1Radnik(2,1,12.55f,5f,0.3f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,12.55f,5f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0,12.55f,5f,0.2f);
		//4.val 
		
		//mjesano
		
		faIgr.dodajBr_1Radnik(3,1,12.55f,5f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(3,0, 12.55f,5f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(3,1,12.55f,5f,0.7f);
		faIgr.dodajBr_1Radnik(3,0,12.55f,5f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(3,0,12.55f,5f,0.5f);
		faIgr.dodajBr_1Radnik(3,1,12.55f,5f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(3,0, 12.55f,5f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(3,0,12.55f,5f,0.6f);
		//gore
		faIgr.dodajBr_9ProtivnikPas(3,8,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.1f);
		faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.2f); 
		//dolje
		faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.7f);
	
		
		faIgr.dodajBr_10ProtivnikPolicajac(3,12,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.2f);
		faIgr.dodajBr_10ProtivnikPolicajac(3,1,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.3f);
		faIgr.dodajBr_10ProtivnikPolicajac(3,7,12.55f,5f,0.6f);
		faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.4f);
		faIgr.dodajBr_10ProtivnikPolicajac(3,1,12.55f,5f,0.8f);
	
		
			
		//gore
				faIgr.dodajBr_9ProtivnikPas(3,10,12.55f,5f,0.3f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.2f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.3f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.1f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.2f); 
				
				faIgr.dodajBr_10ProtivnikPolicajac(3,17,12.55f,5f,0.8f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.6f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,1,12.55f,5f,0.7f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.3f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,7,12.55f,5f,0.6f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.7f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,1,12.55f,5f,0.8f);
				
				//dolje
				faIgr.dodajBr_9ProtivnikPas(3,12,12.55f,5f,0.7f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.8f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.7f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.8f); 
				
				
				faIgr.dodajBr_1Radnik(3,12,12.55f,5f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,0, 12.55f,5f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,12.55f,5f,0.7f);
				faIgr.dodajBr_1Radnik(3,0,12.55f,5f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,7,12.55f,5f,0.5f);
				faIgr.dodajBr_1Radnik(3,1,12.55f,5f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(3,0, 12.55f,5f,0.2f);
			
				
				faIgr.dodajBr_10ProtivnikPolicajac(3,17,12.55f,5f,0.8f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.2f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,1,12.55f,5f,0.7f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.3f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,8,12.55f,5f,0.6f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,0,12.55f,5f,0.4f);
				
				//gore
				faIgr.dodajBr_9ProtivnikPas(3,10,12.55f,5f,0.3f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.2f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.3f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.1f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.2f); 
				//dolje
				faIgr.dodajBr_9ProtivnikPas(3,7,12.55f,5f,0.7f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.8f);
				faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(3,1,12.55f,5f,0.7f);
		        faIgr.dodajBr_9ProtivnikPas(3,0,12.55f,5f,0.8f); 
		//5. val
		faIgr.dodajBr_10ProtivnikPolicajac(4,15,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.2f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.3f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,7,12.55f,5f,0.6f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.4f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.3f);		
		//gore
		faIgr.dodajBr_9ProtivnikPas(4,8,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.2f); 
		
	
		//dolje
		faIgr.dodajBr_9ProtivnikPas(4,8,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.8f); 
	
	
		
		
		faIgr.dodajBr_10ProtivnikPolicajac(4,15,12.55f,5f,0.8f);
		faIgr.dodajBr_3MMA(4,0,12.55f,5f,0.2f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_3MMA(4,5,12.55f,5f,0.3f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,6,12.55f,5f,0.6f);
		faIgr.dodajBr_3MMA(4,0,12.55f,5f,0.4f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,1,12.55f,5f,0.8f);

		faIgr.dodajBr_3MMA(4,15,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.2f);
		faIgr.dodajBr_3MMA(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,6,12.55f,5f,0.8f);
		faIgr.dodajBr_3MMA(4,5,12.55f,5f,0.6f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.4f);
		faIgr.dodajBr_3MMA(4,1,12.55f,5f,0.8f);
   		
        //gore
		faIgr.dodajBr_9ProtivnikPas(4,8,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.2f); 
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.3f);
		//dolje
		faIgr.dodajBr_9ProtivnikPas(4,5,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.8f); 
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.6f);


		faIgr.dodajBr_10ProtivnikPolicajac(4,15,12.55f,5f,0.8f);
		faIgr.dodajBr_8ProtivnikKuhar(4,0,12.55f,5f,0.2f);
		faIgr.dodajBr_3MMA(4,0,12.55f,5f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_3MMA(4,0,12.55f,5f,0.3f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,10,12.55f,5f,0.6f);
		
		faIgr.dodajBr_8ProtivnikKuhar(4,0,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_3MMA(4,0,12.55f,5f,0.3f);	

		faIgr.dodajBr_3MMA(4,15,12.55f,5f,0.8f);
		faIgr.dodajBr_8ProtivnikKuhar(4,0,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.2f);
		faIgr.dodajBr_3MMA(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.3f);
		faIgr.dodajBr_8ProtivnikKuhar(4,10,12.55f,5f,0.7f);
	
		faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.8f);
		faIgr.dodajBr_3MMA(4,1,12.55f,5f,0.8f);
        faIgr.dodajBr_10ProtivnikPolicajac(4,0,12.55f,5f,0.3f);	
		
        //gore
		faIgr.dodajBr_9ProtivnikPas(4,8,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.1f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.2f); 
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.3f);
	
		//dolje
		faIgr.dodajBr_9ProtivnikPas(4,5,12.55f,5f,0.1f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(4,0,12.55f,5f,0.8f); 
		faIgr.dodajBr_9ProtivnikPas(4,1,12.55f,5f,0.96f);
		
		
		
         ////////////poseban metoda za lod slika
			 loadSlika();
												 Log.d("game onCreate", "memorije nativ poslije lodiranje=" + Debug.getNativeHeapAllocatedSize()/1024);
										    	 Log.d("game onCreate", "memorije GlobalAllocSize poslije lodiranje=" + Debug.getGlobalAllocSize()/1024);
										    	 Log.d("game onCreate", "memorije ThreadAllocSize poslije lodiranje=" + Debug.getThreadAllocSize()/1024);												
												   
												    uiMan.daliDaProvjeravamPovlacenje(true);
												}
									      
			
			
			
			
			
									        };  
									      
									        gLog.pokreniGlavnuPetlju();
									        setContentView(uiMan);
										
}
	@Override
	protected void onStart() {
		super.onStart();
		
		if(restart){
			  dodajPozadinu();
			gLog.setSystemPauze(false);
		}
		restart=true;
		
		
	}
	
	  public class VanjskiCrtacZaUIMan extends View{
          private boolean zahtjevZaCrtanje=false;
          private UIManager uiMan;
          private Bitmap tempBitmap;
    	  boolean bitmapGotov=false;
		  public VanjskiCrtacZaUIMan(Context context) {
		   super(context);
		   // TODO Auto-generated constructor stub
		   setWillNotDraw(false);
		  }
		  public boolean  vratiBitmapCijelogEkrana(Bitmap bitmapZaVratiti,UIManager uiMan){
			  boolean b=false;
		
			 // this.postInvalidate();
			  if(!zahtjevZaCrtanje) {
		    	
		    	  this.postInvalidate();
		    	  zahtjevZaCrtanje=true;
		   
			  }
			
			  this.uiMan=uiMan;
			  
			  if(bitmapGotov==true){
				  b=true;
				  
				 // buildDrawingCache(true);
		    	 //  tempBitmap=Bitmap.createBitmap(getDrawingCache());
				  bitmapZaVratiti=tempBitmap;
				  tempBitmap=null;
					
				  bitmapGotov=false;
				  zahtjevZaCrtanje=false;
				  //setDrawingCacheEnabled(false);
			  }
			  else  bitmapZaVratiti=null;
			  
			  return b;
		  }
		  Paint p= new Paint();
		  
		  @Override
		  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		      setMeasuredDimension(efekSir,efekVis);
		  }
		  
		  @Override
		  protected void onDraw(Canvas canvas) {
		   // TODO Auto-generated method stub
			 // super.onDraw(canvas);
			  super.onDraw(canvas);
		      if(zahtjevZaCrtanje) {
		    
		    	  Bitmap  tempBmp= Bitmap.createBitmap( efekSir,efekVis, Bitmap.Config.ARGB_8888);
		    	  Canvas tempCan=new Canvas(tempBmp);
		    	
		    	
			   uiMan.glavnoCrtanje(tempCan);
			   bitmapGotov=true;
			   //canvas.drawBitmap(tempBmp, 0,0, null);
			   this.tempBitmap= tempBmp.copy(Bitmap.Config.ARGB_8888, true);
	          
	                 
			 
	           
		      }
		    
		  }
		   }
	
	  public void dodajPozadinu(){

	      
		  //////////////////////
		  
		    opts.inPreferredConfig = Bitmap.Config.RGB_565;
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8grad0most,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			   SpriteHendler dodatak=new SpriteHendler(1);
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8grad0most0most,opts),1, 1,0);//ispaljenje
			
			 faIgr.dodajSprite(501, dodatak);
			  dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8grad0most8kip,opts),1, 1,0);//ispaljenje
			
			 faIgr.dodajSprite(502, dodatak);
			 dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8grad0most8grad,opts),1, 1,0);//ispaljenje
			
			 faIgr.dodajSprite(503, dodatak);
			 dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.faza8grad0most8kola,opts),1, 1,0);//ispaljenje
			
			 faIgr.dodajSprite(504, dodatak);
			opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
		} 
	   public  Bitmap namjestiProzirnost(Bitmap bitmap){
		   int [] allpixels = new int [ bitmap.getHeight()*bitmap.getWidth()];
          
		   bitmap.getPixels(allpixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),bitmap.getHeight());

		   for(int i =0; i<bitmap.getHeight()*bitmap.getWidth();i++){

		    if( allpixels[i] == Color.BLACK)
		                allpixels[i] = Color.TRANSPARENT;
		    }
           Bitmap bm = null;
           bm =Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),bitmap.getConfig());
           bm.setPixels(allpixels, 0,bitmap.getWidth(), 0, 0, bitmap.getWidth(),bitmap.getHeight());
		   bitmap.recycle();
		   return bm;
	   } 
										@Override
										protected void onPause(){
											super.onPause();
											if(gLog!=null){
											
												gLog.setSystemPauze(true);
											}
											/*gLog.pause();
											uiMan.stop();*/
											//faIgr.reciklirajSveSpriteove();
											//uiMan.reciklirajPozadinu();
										}
										
										@Override
										protected void onRestart() {
											// TODO Auto-generated method stub
											super.onRestart();
										}
										@Override
										protected void onResume() {
											// TODO Auto-generated method stub
											/*gLog.resume();
											uiMan.pokreniGlavnuPetlju();*/
											super.onResume();
										}
										@Override
										protected void onDestroy() {
											// TODO Auto-generated method stub
										/*	uiMan.stop();
											gLog.ugasiGlavnuPetlju();
											faIgr.reciklirajSveSpriteove();
											uiMan.reciklirajPozadinu();*/
											super.onDestroy();
										}
										@Override
										protected void onStop() {
											// TODO Auto-generated method stub
											super.onStop();
											/*	uiMan.stop();
											gLog.ugasiGlavnuPetlju();
											faIgr.reciklirajSveSpriteove();
											uiMan.reciklirajPozadinu();*/
											
									     	//Debug.stopMethodTracing(); 
										}
										@Override
										 public void onBackPressed(){// ovo se mora napraviti za svaku fazu
											if(this.gLog!=null)if(gLog.daliDaGasimIvracamNazad()){
												if(gLog!=null){
													this.gLog.ugasiGlavnuPetlju();
													gLog=null;
												}
											    if(uiMan!=null){			
											    	uiMan.reciklirajPozadinu();
														uiMan.reciklirajTeksturu();
														this.faIgr.reciklirajSveSpriteove();
													 uiMan.makniSveObjekteMultyThread();
													 uiMan=null;}
													 
													stvoriMapu();
											}
											else{
												
												
											 uiMan.ukljuciTouchEvente();
											 if(this.uiMan.tempUniverzalniIzbornik()!=null&&!uiMan.onBackPressJeNormalan){
												 uiMan.tempUniverzalniIzbornik().backBotunStisnut();// u slucaju da nije ovradana funkija za back press pozvat ce defoltna u kojoj se poziva ova funkcija ponovno i postavlja se zastavica da je onbackpress normalan i ovvafunkcija reagira po defoltur5r5r5r5r5r5r5r5r5r5r5r5r5r5
												 uiMan.onBackPressJeNormalan=false;// vvraca ovu zastavicu da sljedeci put opet pozove izbbornik ako 
											 }
											 else gLog.faIgr.getTaskbar().kliknutoNaExit();
											}
										/*if(gLog!=null){
											this.gLog.ugasiGlavnuPetlju();
											gLog=null;
										}
									    if(uiMan!=null){	
									    	uiMan.reciklirajPozadinu();
												uiMan.reciklirajTeksturu();
												this.faIgr.reciklirajSveSpriteove();
											 uiMan.makniSveObjekteMultyThread();
											 uiMan=null;}
											 
											stvoriMapu();*/
											/*Intent intent=new Intent("android.intent.action.pokreni_mapu");
											  Bundle bundle= new Bundle();
											  bundle.putInt("sir",uiMan.can.getWidth() );
											 bundle.putInt("vis", uiMan.can.getHeight());
											 bundle.putBoolean("pokriSaMape", true);
											 intent.putExtras(bundle);
											 this.gLog.ugasiGlavnuPetlju();
												uiMan.reciklirajPozadinu();
												uiMan.reciklirajTeksturu();
												this.faIgr.reciklirajSveSpriteove();
											 uiMan.makniSveObjekteMultyThread();
											 uiMan=null;
											 gLog=null;
											 startActivity(intent);
											 finish();*/
										}
									
										public void stvoriMapu(){
											 final Intent intent=new Intent("android.intent.action.pokreni_mapu");
										
											  Bundle bundle= new Bundle();
											  bundle.putInt("sir",efekSir );
											  bundle.putInt("vis", efekVis);
											  bundle.putBoolean("pokriSaMape", true);
											  intent.putExtras(bundle);
											  startActivity(intent);
											 // GameActivity.this.finish(); 
											  finish();
											
										
										
										}
										@Override
										/////////sprijeèava promjenu  orjentacije 
										public void onConfigurationChanged(Configuration newConfig) {
										    super.onConfigurationChanged(newConfig);
										    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
										}
										private void ucitajIzListaUVarijable(){
											tezina=slotoviTezina.get(this.idZadnjegSlota);
										} 
									   private void lodirajIzDB(){
										    	slotoviTezina=new HashMap<String,Integer>();
										    	listaImenaSlotova=new HashMap<String,String>() ;
										    	koristeneFazePoSlotovima=new HashMap<String,String>() ;
										        koristeneFazeBrZvjezdica=new HashMap<String,Integer>();
										        koristeneFazeTezina=new HashMap<String,Integer>();
										    	koristeneFazeOtkljucana=new HashMap<String,Boolean>();
										    	listaStanjaSlotova=new HashMap<String,Integer>();
											 SQLiteDatabase bazaPodataka;
										     bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
											  Cursor cur1=bazaPodataka.query(IgricaActivity.listaSlotova, null,null, null, null, null, null);
											  Cursor cur3=bazaPodataka.query(IgricaActivity.zadnjiKoristenSlot, null,null, null, null, null, null);
											 try{
												  cur3.moveToFirst();
												 this.idZadnjegSlota=cur3.getString(cur3.getColumnIndex(IgricaActivity.IDSlota));
											 }
											 catch(Exception e){
												 
											 }
											
											    cur1.moveToFirst();
											         String IDSlota;
											         String IDFaze;
											         String IDKoristeneFaze;
											        String imeSlota;
											        int stanjeSlota;
											        int brZvjezdica;
											        int tezina;
											       int stanje;
													while(cur1.isAfterLast()==false){// ucitava slot po slot
														IDSlota=cur1.getString(cur1.getColumnIndex(IgricaActivity.IDSlota));
													    imeSlota=cur1.getString(cur1.getColumnIndex(IgricaActivity.imeSlota));
											               Cursor cur2=bazaPodataka.query(IgricaActivity.listaKoristenihFaza, null,null,null, null, null, IgricaActivity.IDKoristeneFaze+ " ASC");
											               tezina=cur1.getInt(cur1.getColumnIndex(IgricaActivity.tezina));
											               stanjeSlota=cur1.getInt(cur1.getColumnIndex(IgricaActivity.stanjeSlota));
											               slotoviTezina.put(IDSlota, tezina);
											               listaStanjaSlotova.put(IDSlota, stanjeSlota);
											               cur2.moveToFirst();
											               listaImenaSlotova.put(IDSlota, imeSlota);
											               while(cur2.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 
											            	   String idSlottemp=cur2.getString(cur2.getColumnIndex(IgricaActivity.IDSlota));
											            	   if(IDSlota.equals(idSlottemp)){
											            	     IDFaze=cur2.getString(cur2.getColumnIndex(IgricaActivity.IDFaze));	
											            	     brZvjezdica=cur2.getInt(cur2.getColumnIndex(IgricaActivity.brojZvijezdica));
											            	     
											            	     stanje=cur2.getInt(cur2.getColumnIndex(IgricaActivity.stanjeFaze));
											            	     IDKoristeneFaze=cur2.getString(cur2.getColumnIndex(IgricaActivity.IDKoristeneFaze));
											            	     this.koristeneFazePoSlotovima.put(IDSlota,IDKoristeneFaze);// mapiram koristene faze u slotove tako da se samo pomocu imena slota mogu naci svi atributi određene faze
											            	     this.koristeneFazeBrZvjezdica.put(IDKoristeneFaze,brZvjezdica);
											            	     this.koristeneFazeTezina.put(IDKoristeneFaze, tezina);
											            	     if(stanje==1){// 1 znaci da je otkljucana
											            		   this.koristeneFazeOtkljucana.put(IDKoristeneFaze,true);
											            	   }
											            	   else if(stanje==0){// 0 znaci da je zakljucana
											            		   this.koristeneFazeOtkljucana.put(IDKoristeneFaze,false);
											            	   }
											            	   }
											     
											         	    cur2.moveToNext();// za pomicanje u bazi podataka   
											               
											                  }
											               cur2.close();
												             cur1.moveToNext();// za pomicanje u bazi podataka
											           
														}
													cur3.close();
														cur1.close();
													  bazaPodataka.close();
										 } 
									}
