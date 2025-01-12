
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
import com.rugovit.igrica.engine.ui.Taskbar;
import com.rugovit.igrica.engine.ui.UIManager;

public class FazaGradRibarski extends Activity {
	
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
 	  
 	    ////////////////
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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8ribarski0grad, opts);
			int pozVis = opts.outHeight;
			int pozSir = opts.outWidth;
			opts.inDither=false;
			
			opts.inJustDecodeBounds = false;
			
			float   omjerSlike=(float)pozSir/(float)pozVis;

			
			 if(pozSir<efekSir) {
				xPiksCm=efekSir/xPozCm;//povecava tako da ispuni ekran 
				float tempVisina=efekSir/omjerSlike;
				if(tempVisina>=efekVis){
					yPiksCm=(tempVisina)/yPozCm;
				}
				else{
					yPiksCm=efekVis/yPozCm;//povecava tako da ispuni ekran 
					float tempSirina=efekVis*omjerSlike;
					xPiksCm=(tempSirina)/xPozCm;
				}
			}
			 else	 if(pozVis>efekVis) {
			
				yPiksCm=efekVis/yPozCm;//povecava tako da ispuni ekran 
				float tempSirina=efekVis*omjerSlike;
		
				if(tempSirina>=efekSir){
					xPiksCm=(tempSirina)/xPozCm;
				}
				else{
					xPiksCm=efekSir/xPozCm;//povecava tako da ispuni ekran 
					float tempVisina=efekSir/omjerSlike;	
				    yPiksCm=(tempVisina)/yPozCm;
				}
			}
	
		//opts.inScaled =false;
		opts.inSampleSize=smanjenje;
	    NativeHeapAllocatedSize= Debug.getNativeHeapAllocatedSize()/1024;
		 GlobalAllocSize=Debug.getGlobalAllocSize()/1024;
		 ThreadAllocSize= Debug.getThreadAllocSize()/1024;                             

	        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;

	   
	    //////////inicijalizacija gamelogica i uimanager pozadine
	    /////////ui manager*/
	        faIgr=new FazeIgre(3,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		    
		    faIgr.dodajParametreFazeIzDB(IDKoristeneFaze,bun.getInt(IgricaActivity.brojZvijezdica),bun.getInt(IgricaActivity.tezina));
            
		/////TASKBAR
			 LinkedList<Typeface> listaFontova= new LinkedList<Typeface>();
			    Typeface font = Typeface.createFromAsset(
					     getAssets(), 
					    "fonts/king.ttf");
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
				
				 
		
		  /////dodavanje èlanova igrice
				    faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				    faIgr.setDelayIzmVal(30);
				    /*faIgr.setDelayIzmVal(0,5);
				    faIgr.setDelayIzmVal(1,10);
				    faIgr.setDelayIzmVal(2,15);
				    faIgr.setDelayIzmVal(3,10);
				    faIgr.setDelayIzmVal(4,10);
				    faIgr.setDelayIzmVal(5,15);*/
				    //faIgr=new FazeIgre(200,getResources(),60,60);
				    faIgr. dodajPocetneParametreIgre(100,10);
	 	///lijevi
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.03f,4.16f);
	    faIgr.dodajBr200ToranjEmbrio(0,0,2.23f,2.72f);
	    //gornji
	   /* faIgr.dodajBr200ToranjEmbrio(0,0,5.47f,0.95f);
		faIgr.dodajBr200ToranjEmbrio(0,0,6.77f,0.95f);
		faIgr.dodajBr200ToranjEmbrio(0,0,8.22f,0.95f);*/
		//desni
		faIgr.dodajBr200ToranjEmbrio(0,0,10.93f,2.57f);
	    faIgr.dodajBr200ToranjEmbrio(0,0,11f,4.16f);
		faIgr.dodajBr200ToranjEmbrio(0,0,11.13f,5.56f);
	//	faIgr.dodajBr200ToranjEmbrio(0,0,11.13f,6.51f);
		//srednji
		faIgr.dodajBr200ToranjEmbrio(0,0,8.05f,3.46f);
		faIgr.dodajBr200ToranjEmbrio(0,0,5.24f,3.46f);
		faIgr.dodajBr200ToranjEmbrio(0,0,7.25f,4.34f);
		faIgr.dodajBr200ToranjEmbrio(0,0,6.0f,4.34f);
		//donji
		faIgr.dodajBr200ToranjEmbrio(0,0,6.65f,6.98f);
		faIgr.dodajBr200ToranjEmbrio(0,0,3.55f,6.98f);
		faIgr.dodajBr200ToranjEmbrio(0,0,5.1f,6.98f);
		//faIgr.dodajBr200ToranjEmbrio(0,0,7.99f,6.98f);
	  //  faIgr.dodajBr50Junak(0,4.46f,2.9f, 0.5f,0.5f);
		  //pocetak
				int brP=-1;
				int brPLijevi=0;
				int brPDesni=0;
				float xPP=0;
				float yPP=0;
				float sirP=0;
				float visP=0;
				float speed=0.0005f;
				///////
				//1.ravni put
			    float visina=0.8625f;
			    float sirina=2f;
				sirP=sirina;
				visP=visina;
				xPP=8.61f;
				yPP=8.50f-visP;
				float yPPDesni,yPPLijevi;
				float xPPDesni,xPPLijevi;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP, speed,90,  ++brP);
			      ////
				//xPP=2.78f;
				
				yPP=yPP-visP;
				//sirP=1.9f;
				//visP=1.41f;
				
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,  speed,90,  ++brP);
				//
				//////////////////////////Lijevi segment///////////////////////////////////////////////////
				//Okuka1 
				brPLijevi=brP;
				/////
				sirP=sirina/2;
				
				visP=sirina;
				//xPP=4.72f;
				yPPLijevi=yPP-visP;
				xPPLijevi=xPP;
				faIgr.dodajBr205OkukaKut(0,0, xPPLijevi, yPPLijevi, sirP, visP,speed,135, ++brPLijevi);
				//1 
				sirP=visina*0.90f;
				visP=sirina;
				xPPLijevi=xPPLijevi-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPLijevi, yPPLijevi, sirP, visP,speed, 180,++brPLijevi);
				//2 
				
				xPPLijevi=xPPLijevi-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPLijevi, yPPLijevi, sirP, visP,speed, 180, ++brPLijevi);
				//3 
				
				xPPLijevi=xPPLijevi-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPLijevi, yPPLijevi, sirP, visP,speed, 180, ++brPLijevi);
				//4 
				
				xPPLijevi=xPPLijevi-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPLijevi, yPPLijevi, sirP, visP,speed, 180, ++brPLijevi);
                //5 
				
				xPPLijevi=xPPLijevi-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPLijevi, yPPLijevi, sirP, visP,speed, 170, ++brPLijevi);
				//2. okuka
				
				xPPLijevi=xPPLijevi-sirina/2;
				sirP=sirina/2;
				visP=sirina;
				
				faIgr.dodajBr205OkukaKut(0,0, xPPLijevi, yPPLijevi, sirP, visP,speed,130, ++brPLijevi);
				
				xPPLijevi=xPPLijevi-sirina/2;
				faIgr.dodajBr205OkukaKut(0,0, xPPLijevi, yPPLijevi, sirP, visP,speed,90, ++brPLijevi);
				///
				
				sirP=sirina;
				visP=visina;
				yPPLijevi=yPPLijevi-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPPLijevi, yPPLijevi, sirP, visP,speed,90, ++brPLijevi);
				yPPLijevi=yPPLijevi-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPPLijevi, yPPLijevi, sirP, visP,speed,80, ++brPLijevi);
				
				////////////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////// Desni segment///////////////////////////////////////////////////
		        //Okuka1
				brPDesni=brP;
		        /////
				sirP=sirina/2;
				visP=sirina;
		        //xPP=4.72f;
		        yPPDesni=yPP-visP;
		        xPPDesni=xPP+sirP;
		        faIgr.dodajBr205OkukaKut(0,0, xPPDesni, yPPDesni, sirP, visP,speed,90, ++brPDesni);
		        //
		        //1 
				sirP=sirina;
				visP=visina;
				xPPDesni=xPPDesni-sirP/2;
				yPPDesni=yPPDesni-visP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 95,++brPDesni);
				//2 
				
				//xPPDesni=xPPDesni-sirP/2;
				yPPDesni=yPPDesni-visP;
				faIgr.dodajBr205OkukaKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 100,++brPDesni);
				// Okuka 2
				sirP=sirina/2;
				visP=sirina*0.75f;
				yPPDesni=yPPDesni-visP;
		        //xPPDesni=xPP+sirP;
		        faIgr.dodajBr205OkukaKut(0,0, xPPDesni, yPPDesni, sirP, visP,speed,160, ++brPDesni);
		       // yPPDesni=yPP-visP;
		        xPPDesni=xPPDesni+sirP;
		        faIgr.dodajBr205OkukaKut(0,0, xPPDesni, yPPDesni, sirP, visP,speed,140, ++brPDesni);
		        //1
		        visP=sirina*0.8f;
		        sirP=visina;
				xPPDesni=xPPDesni-sirina/2-sirP;
			//yPPDesni=yPPDesni-visP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 180,++brPDesni);
				//2
			
						xPPDesni=xPPDesni-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 180,++brPDesni);
				//3
				
				xPPDesni=xPPDesni-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 180,++brPDesni);
				//4
				sirP=(xPPDesni-xPPLijevi-sirina)/2;
				xPPDesni=xPPDesni-sirP;
				faIgr.dodajBr203RavniPutKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 180,++brPDesni);
                 //5
				
				xPPDesni=xPPDesni-sirP;
				faIgr.dodajBr205OkukaKut(0,0,xPPDesni, yPPDesni, sirP, visP,speed, 130,++brPDesni);
				///////////////////////////////////////////////////////////////////////////////////////////////
		       ///////////////////////////spajanje/////////////////////////////////////////////////////////////
				brP=brPDesni;
				
				xPP=xPPLijevi;
				visP=visina;
				sirP=sirina;
				yPP=yPPLijevi-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,80, ++brP);
				yPP=yPP-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,80, ++brP);
				yPP=yPP-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP*1.2f, visP,speed,80, ++brP);
				yPP=yPP-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP*1.2f, visP,speed,80, ++brP);
				yPP=yPP-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP*1.2f, visP,speed,90, ++brP);
				yPP=yPP-visP;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP*1.2f, visP,speed,90, ++brP);
			
											
					yPP=yPP-visP;
				faIgr.dodajBr204Kraj(0,0,xPP, yPP, sirP*1.2f, visP, 0, -speed,++brP);
				//////////////////////////////////////////////////////////////////////////////////////////////////
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 9.8f,7.6f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,6, 9.8f,7.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(0,4, 9.8f,7.6f,0.4f);
		faIgr.dodajBr_5ProtivnikPocetni(0,3, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 9.8f,7.6f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,2, 9.8f,7.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1, 9.8f,7.6f,0.2f);		
		//faIgr.dodajBr_5ProtivnikPocetni(0,1, 9.8f,7.6f,0.4f);
		//faIgr.dodajBr_7ProtivnikCistacica(0,1, 9f,7.6f,0.2f);
		
		faIgr.dodajBr_5ProtivnikPocetni(0,7, 9.8f,7.6f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1, 9.8f,7.6f,0.4f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,5, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1, 9.8f,7.6f,0.4f);
		//faIgr.dodajBr_5ProtivnikPocetni(0,1, 9.8f,7.6f,0.4f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,2, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1, 9.8f,7.6f,0.4f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1, 9.8f,7.6f,0.4f);
	
		///val br 2
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,6, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.2f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,6, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,6, 9.8f,7.6f,0.8f);
		//faIgr.dodajBr_6ProtivnikDebeli(1,1, 9f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);		
		//faIgr.dodajBr_6ProtivnikDebeli(1,1, 9f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);

		faIgr.dodajBr_5ProtivnikPocetni(1,5, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(1,5, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.9f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,8, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1, 9.8f,7.6f,0.9f);
		///3. val
		faIgr.dodajBr_5ProtivnikPocetni(2,5, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,3, 9.8f,7.6f,0.2f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		
		faIgr.dodajBr_5ProtivnikPocetni(2,7, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1, 9.8f,7.6f,0.2f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.2f);
		
		faIgr.dodajBr_5ProtivnikPocetni(2,7, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,5, 9.8f,7.6f,0.8f);
		
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,5, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.9f);
		
		faIgr.dodajBr_6ProtivnikDebeli(2,6, 9.8f,7.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,0, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 9.8f,7.6f,0.8f);
		
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.7f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,0, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 9.8f,7.6f,0.9f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.7f);
		
		faIgr.dodajBr_7ProtivnikCistacica(2,4, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0, 9.8f,7.6f,0.4f);
		faIgr.dodajBr_5ProtivnikPocetni(2,0, 9.8f,7.6f,0.8f);	
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1, 9.8f,7.6f,0.8f);	
		faIgr.dodajBr_7ProtivnikCistacica(2,0, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1, 9.8f,7.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(2,0, 9.8f,7.6f,0.8f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1, 9.8f,7.6f,0.7f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1, 9.8f,7.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 9.8f,7.6f,0.9f);
		
		
		  ////////////poseban metoda za lod slika
		 loadSlika();
												 Log.d("game onCreate", "memorije nativ poslije lodiranje=" + Debug.getNativeHeapAllocatedSize()/1024);
										    	 Log.d("game onCreate", "memorije GlobalAllocSize poslije lodiranje=" + Debug.getGlobalAllocSize()/1024);
										    	 Log.d("game onCreate", "memorije ThreadAllocSize poslije lodiranje=" + Debug.getThreadAllocSize()/1024);												
												   
												    uiMan.daliDaProvjeravamPovlacenje(true);
												}
									      
			
			
			
			
			
			
									        };  
									        /*FrameLayout game = new FrameLayout(this);
									    	IgricaActivity.trenutacniFrame = game;
									        
									        
									    	VanjskiCrtacZaUIMan temView=new VanjskiCrtacZaUIMan(this);
											 
									        temView.setVisibility(View.VISIBLE);
										 
											gLog.pokreniGlavnuPetlju(); // izbacio sam opciju da se GameLogic sam pokreæe jer moglo biti grešaka pri pristupu listi  
											
										    game.addView(uiMan);
											game.addView(temView);
											

                                       
                                          
										    setContentView(game);*/
									        //gLog.pokreniGlavnuPetlju();
									        gLog.pokreniGlavnuPetlju();
									        setContentView(uiMan);
										   /*RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
										              ViewGroup.LayoutParams.MATCH_PARENT);
										    linlay.setLayoutParams(para);*/
										    
											
										
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
		    	   //Bitmap  tempBitmap= Bitmap.createBitmap( (int)uiMan.getSirEkrana(),(int)uiMan.getVisEkrana(), Bitmap.Config.ARGB_4444);
				//  setDrawingCacheEnabled(true);  
				   
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
		    opts.inPreferredConfig = Bitmap.Config.RGB_565;
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8ribarski0grad,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
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
