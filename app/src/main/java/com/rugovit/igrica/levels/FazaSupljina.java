
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

public class FazaSupljina extends Activity {

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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8planina0jame, opts);
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
	        faIgr=new FazeIgre(6,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		  
		    //faIgr=new FazeIgre(200,getResources(),60,60);
		   
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
				
				 float omjer=1.1f;
				 float polX=-1.16f;
				 float polY=6.74f;
				 float sir=2.5f;
				 float vis=2.12f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 ///POSTAVLJANJE
				 //faIgr. dodajPocetneParametreIgre(767,10);
				faIgr. dodajPocetneParametreIgre(100,10);
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				 faIgr.setDelayIzmVal(30);
				 //drvece
				 polX=-0.78f;
				 polY=5.29f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=-0.88f;
				 polY=2.72f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 //dodatak planina gore	
				 //log
				 float polXLog=0f;
				 float polYLog=0f;
				 float sirLog=2.82f;
				 float visLog=0.85f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0,0,0,polXLog, polYLog,sirLog,visLog);
                 //log
				 polXLog=2.82f;
				 polYLog=0f;
				 sirLog=2.01f;
				 visLog=0.64f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 //log
				 polXLog=4.83f;
				 polYLog=0f;
				 sirLog=0.53f;
				 visLog=0.53f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 //log
				 polXLog=5.36f;
				 polYLog=0f;
				 sirLog=1.2f;
				 visLog=0.25f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				
				 
				 polX=0f;
				 polY=0f;
				 sir=6.67f;
				 vis=1.02f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 //srednji lijevo
				//log
				 polXLog=2.29f;
				 polYLog=4.13f;
				 sirLog=0.53f;
				 visLog=0.42f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=1.66f;
				 polYLog=3.74f;
				 sirLog=0.64f;
				 visLog=0.88f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=0.95f;
				 polYLog=3.46f;
				 sirLog=0.71f;
				 visLog=1.31f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=0f;
				 polYLog=3.07f;
				 sirLog=0.95f;
				 visLog=1.73f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 
				 
				 polX=0f;
				 polY=2.89f;
				 sir=2.93f;
				 vis=2.12f;
				 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 503,0,2,polX, polY, sir,vis,0, 0f, 0,0,0-vis);
				 //srednji desno
				//log
				 polXLog=7.87f;
				 polYLog=3.7f;
				 sirLog=0.56f;
				 visLog=0.95f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=8.43f;
				 polYLog=3.39f;
				 sirLog=0.74f;
				 visLog=1.06f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=9.14f;
				 polYLog=3.03f;
				 sirLog=0.99f;
				 visLog=1.41f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=10.12f;
				 polYLog=2.47f;
				 sirLog=0.64f;
				 visLog=1.92f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=10.76f;
				 polYLog=2.54f;
				 sirLog=0.64f;
				 visLog=1.48f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log
				 polXLog=11.43f;
				 polYLog=2.26f;
				 sirLog=1.09f;
				 visLog=1.76f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,0,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 
				 polX=7.37f;
				 polY=1.94f;
				 sir=5.12f;
				 vis=2.86f;
				 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 504,0,2,polX, polY, sir,vis,0, 0f, 0,0,0-vis);
			
				 ///
				
				 
		
		  /////dodavanje èlanova igrice
				float dodatak=-0.15f; 
	 	///gornji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.08f,5.31f);//
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.02f,7.28f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.89f,5.31f);//**
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.94f,2.5f);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.74f,7.19f);//
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.54f,7.17f);//
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.13f,5.42f);///
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.65f,7.13f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.52f,7.33f);//*
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.58f,5.42f);//**
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.44f,0.62f);//**
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.82f,0.65f);
	 //	faIgr.dodajBr200ToranjEmbrio(0,0,7.2f,0.86f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.86f,1.75f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.61f,4.29f);//*
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.66f,3.58f);//*
		faIgr.dodajBr200ToranjEmbrio(0,0,5.61f,2.91f);//*
		faIgr.dodajBr200ToranjEmbrio(0,0,4.37f,2.08f);
		faIgr.dodajBr200ToranjEmbrio(0,0,3.32f,1.39f);//***
		faIgr.dodajBr200ToranjEmbrio(0,0,1.62f,1.29f);//***
		faIgr.dodajBr200ToranjEmbrio(0,0,5.19f,5.24f);//*
	  //  faIgr.dodajBr50Junak(0,4.46f,2.9f, 0.5f,0.5f);
	 
		  //pocetak
				int brP=0;
				int brPLijevi=-1;
				int brPDesni=0;
				float xPP=0;
				float yPP=0;
				float sirP=0;
				float visP=0;
				float speed=0.0005f;
				///////
				
			    float visina=0.85f;
			    float sirina=1.23f;
			    float sirina2=1.7f;
			    
			    float dodatakX=0f;
			    ///////////////////////////lijevi (glavni) segment///////////////////////////////////////
			    /////0
			    xPP=-0.85f;
				yPP=1.54f;
			    
			    sirP=0.85f;
			    visP=1.13f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
			   /////1
			    xPP=0f;
				yPP=1.54f;
			    
			    sirP=0.85f;
			    visP=1.13f;
				
			    
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
			   /////2
			    xPP=0.85f;
				yPP=1.6f;
			    
			    sirP=0.85f;
			    visP=1.14f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////3
			    xPP=1.7f;
				yPP=1.69f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////4
			    xPP=2.55f;
				yPP=1.69f;
			    
			    sirP=0.83f;
			    visP=1.49f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,340,++brPLijevi);
				///
				 /////5
			    xPP=3.38f;
				yPP=1.69f;
			    
			    sirP=0.83f;
			    visP=1.49f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////6
			    xPP=2.91f;
				yPP=3.18f;
			    
			    sirP=1.3f;
			    visP=0.85f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				///
				 /////7
			    xPP=3.06f;
				yPP=4.03f;
			    
			    sirP=1.3f;
			    visP=0.85f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////8
			    xPP=3.26f;
				yPP=4.85f;
			    
			    sirP=0.56f;
			    visP=1.04f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				///
				 /////9
			    xPP=3.81f;
				yPP=4.85f;
			    
			    sirP=0.53f;
			    visP=1.04f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				///
				 /////-1 posebni
			    xPP=4.34f;
				yPP=5.64f;
			    
			    sirP=0.35f;
			    visP=0.25f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,-brPLijevi);
				///
				 /////10
			    xPP=3.05f;
				yPP=5.89f;
			    
			    sirP=0.83f;
			    visP=1.04f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////11
			    xPP=3.88f;
				yPP=5.89f;
			    
			    sirP=0.83f;
			    visP=1.04f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////12
			    xPP=4.71f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				/*
				 /////13
			    xPP=4.71f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				*/
				 /////13
			    xPP=5.56f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////14
			    xPP=6.41f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////15
			    xPP=7.26f;
				yPP=5.71f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////16
			    xPP=8.11f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////17
			    xPP=8.95f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////18
			    xPP=9.8f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////19
			    xPP=10.65f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////20
			    xPP=11.5f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////21
			    xPP=12.35f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////22 kraj
			    xPP=13.2f;
				yPP=5.7f;
			    
			    sirP=0.85f;
			    visP=1.24f;
				
				
				faIgr.dodajBr204Kraj(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				////dolje lijevo
				
				brPLijevi=0;
				
				  /////1
			    xPP=0f;
				yPP=6.03f;
			    
			    sirP=0.76f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				  /////2
			    xPP=0.76f;
				yPP=5.91f;
			    
			    sirP=0.76f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				  /////3
			    xPP=1.52f;
				yPP=5.81f;
			    
			    sirP=0.76f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				  /////4
			    xPP=2.29f;
				yPP=5.7f;
			    
			    sirP=0.76f;
			    visP=1.24f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				///////////////cesta desno gore//////////////////
				brPLijevi=-1;
				 /////0
			    xPP=10.95f;
				yPP=-0.85f;
			    
			    sirP=1.23f;
			    visP=0.85f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////1
			    xPP=10.91f;
				yPP=0f;
			    
			    sirP=1.24f;
			    visP=0.85f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////2
			    xPP=10.88f;
				yPP=0.85f;
			    
			    sirP=1.29f;
			    visP=0.65f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,210,++brPLijevi);
				///
				 /////3
			    xPP=10.87f;
				yPP=1.5f;
			    
			    sirP=1.29f;
			    visP=0.65f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////4
			    xPP=10.02f;
				yPP=1.06f;
			    
			    sirP=0.85f;
			    visP=1.06f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////-1 posebni
			    xPP=10.02f;
				yPP=0.95f;
			    
			    sirP=0.85f;
			    visP=0.11f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,-brPLijevi);
				///
				 /////5
			    xPP=9.18f;
				yPP=0.98f;
			    
			    sirP=0.85f;
			    visP=1.16f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////6
			    xPP=8.33f;
				yPP=1.03f;
			    
			    sirP=0.85f;
			    visP=1.13f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////-1 posebni
			    xPP=8.29f;
				yPP=2.15f;
			    
			    sirP=2.68f;
			    visP=0.14f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,-brPLijevi);
				///
				 /////7
			    xPP=7.48f;
				yPP=1.11f;
			    
			    sirP=0.85f;
			    visP=1.03f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////-1 posebni
			    xPP=7.48f;
				yPP=2.15f;
			    
			    sirP=0.81f;
			    visP=0.14f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,-brPLijevi);
				///
				 /////8
			    xPP=6.82f;
				yPP=1.2f;
			    
			    sirP=0.66f;
			    visP=1.24f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,230,++brPLijevi);
				///
				 /////9
			    xPP=6.17f;
				yPP=1.2f;
			    
			    sirP=0.66f;
			    visP=1.24f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////10
			    xPP=6.16f;
				yPP=2.44f;
			    
			    sirP=1.24f;
			    visP=0.85f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////11
			    xPP=6.03f;
				yPP=3.29f;
			    
			    sirP=1.24f;
			    visP=0.85f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				
				 /////12
			    xPP=5.97f;
				yPP=4.14f;
			    
			    sirP=1.24f;
			    visP=0.78f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////13
			    xPP=5.97f;
				yPP=4.92f;

			    sirP=1.24f;	
			    visP=0.78f;
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				///////////////////////Z///////////////////////////
				///
		//1. Val
		//lijevo	
		/*		sirP=1.24f;
			    visP=0.85f;				
				xPP=1.2f;
				yPP=1.36f;
		faIgr.dodajBr_12BossIzvanzemaljac(0,0,1.89f+dodatakX,1.86f,3,0.2f);*/
	  /////lijevo gore glavni
	 /*   xPP=-0.85f;
		yPP=1.54f;
	    
	    sirP=0.85f;
	    visP=1.23f;
	    faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f+dodatakX,2.14f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(0,0,-0.85f+dodatakX,2.14f,0.8f);	
		
		faIgr.dodajBr_9ProtivnikPas(0,0,-0.85f+dodatakX,2.14f,0.2f); 
		faIgr.dodajBr_9ProtivnikPas(0,0,-0.85f+dodatakX,2.14f,0.8f); 
		//////////////////////////
	    /////lijevo dolje
	    xPP=-0.85f;
		yPP=6.08f;
	    
	    sirP=0.85f;
	    visP=1.23f;
	    faIgr.dodajBr_5ProtivnikPocetni(0,0,-0.85f+dodatakX,6.61f,0.2f);
	 		faIgr.dodajBr_5ProtivnikPocetni(0,0,-0.85f+dodatakX,6.61f,0.8f);	
	 		
	 		faIgr.dodajBr_9ProtivnikPas(0,0,-0.85f+dodatakX,6.61f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(0,0,-0.85f+dodatakX,6.61f,0.8f); */
	    /////////////////////1 val///////////////////////////////////////////////////////////////
	    ////desno gore
	    faIgr.dodajBr_10ProtivnikPolicajac(0,7,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,11.55f+dodatakX,-0.85f,0.8f);
	    
	    faIgr.dodajBr_10ProtivnikPolicajac(0,20,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,11.55f+dodatakX,-0.85f,0.8f);
	    faIgr.dodajBr_10ProtivnikPolicajac(0,1,11.55f+dodatakX,-0.85f,0.5f);
	    /////lijevo gore glavni
	    faIgr.dodajBr_10ProtivnikPolicajac(0,20,-0.85f+dodatakX,2.14f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,-0.85f+dodatakX,2.14f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,-0.85f+dodatakX,2.14f,0.8f);
	    
	    faIgr.dodajBr_10ProtivnikPolicajac(0,10,-0.85f+dodatakX,2.14f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,-0.85f+dodatakX,2.14f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,-0.85f+dodatakX,2.14f,0.8f);
	    faIgr.dodajBr_10ProtivnikPolicajac(0,1,-0.85f+dodatakX,2.14f,0.5f);
       /////mjesano
	    faIgr.dodajBr_10ProtivnikPolicajac(0,20,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_10ProtivnikPolicajac(0,2,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_3MMA(0,15,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,2,11.55f+dodatakX,-0.85f,0.8f);
	    faIgr.dodajBr_10ProtivnikPolicajac(0,1,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_10ProtivnikPolicajac(0,1,-0.85f+dodatakX,2.14f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,-0.85f+dodatakX,2.14f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,-0.85f+dodatakX,2.14f,0.8f);
	  ///////////////////////////////////////////////2 val///////////////////////////////////////////////////////
	    ////desno gore
	    faIgr.dodajBr_10ProtivnikPolicajac(1,7,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_3MMA(1,1,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(1,1,11.55f+dodatakX,-0.85f,0.8f);
	    
	    faIgr.dodajBr_15ProtivnikStarleta(1,3,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(1,1,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_8ProtivnikKuhar(1,0,11.55f+dodatakX,-0.85f,0.8f);
	    
	    faIgr.dodajBr_10ProtivnikPolicajac(1,12,11.55f+dodatakX,-0.85f,0.5f);
	    faIgr.dodajBr_3MMA(1,2,11.55f+dodatakX,-0.85f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(1,0,11.55f+dodatakX,-0.85f,0.8f);
	    faIgr.dodajBr_10ProtivnikPolicajac(1,1,11.55f+dodatakX,-0.85f,0.5f);
	    
	    /////lijevo gore glavni
	    faIgr.dodajBr_10ProtivnikPolicajac(0,20,-0.85f+dodatakX,2.14f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,-0.85f+dodatakX,2.14f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,-0.85f+dodatakX,2.14f,0.8f);
	    
	    
	    faIgr.dodajBr_15ProtivnikStarleta(1,3,-0.85f+dodatakX,2.14f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(1,2,-0.85f+dodatakX,2.14f,0.5f);
	    faIgr.dodajBr_8ProtivnikKuhar(1,0,-0.85f+dodatakX,2.14f,0.8f);
	    
	    faIgr.dodajBr_10ProtivnikPolicajac(0,5,-0.85f+dodatakX,2.14f,0.5f);
	    faIgr.dodajBr_3MMA(0,2,-0.85f+dodatakX,2.14f,0.2f);
	    faIgr.dodajBr_8ProtivnikKuhar(0,0,-0.85f+dodatakX,2.14f,0.8f);
	    faIgr.dodajBr_10ProtivnikPolicajac(0,1,-0.85f+dodatakX,2.14f,0.5f);
	    
	       /////mjesano
		    faIgr.dodajBr_10ProtivnikPolicajac(1,10,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(1,2,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_10ProtivnikPolicajac(1,2,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(1,15,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(1,2,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(1,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_10ProtivnikPolicajac(1,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_3MMA(1,2,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(1,0,-0.85f+dodatakX,2.14f,0.8f);
		    
		    faIgr.dodajBr_9ProtivnikPas(1,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f+dodatakX,2.14f,0.5f);
		    
		    faIgr.dodajBr_9ProtivnikPas(1,7,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(1,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(1,1,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(1,1,11.55f+dodatakX,-0.85f,0.5f);
		    /////////////////////////////////3  val //////////////////////////////////////////////////////
		    /////0
		    xPP=-0.85f;
			yPP=6.08f;
		    
		    sirP=0.85f;
		    visP=1.23f;
		    
		  
			faIgr.dodajBr203RavniPutKut(1,1,xPP+dodatakX, yPP, sirP, visP,speed,0,0);
			///
			faIgr.dodajBr_12BossIzvanzemaljac(2, 10, 3, 2.2f,3, 0.5f);
			
			
			faIgr.dodajBr_12BossIzvanzemaljac(2, 20, 9, 1.3f,3, 0.5f);
			
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,6,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			
             faIgr.dodajBr_12BossIzvanzemaljac(2, 30, 3, 2.2f,3, 0.5f);
			
			
			faIgr.dodajBr_12BossIzvanzemaljac(2, 20, 9, 1.3f,3, 0.5f);
			
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,25,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,-0.85f+dodatakX,6.61f,0.5f);	
			///////4. VAL
			//lijevo DOLJE
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,10,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			 /////mjesano
		    faIgr.dodajBr_9ProtivnikPas(3,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f+dodatakX,2.14f,0.5f);
		    
		    faIgr.dodajBr_9ProtivnikPas(3,7,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(3,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(3,2,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_10ProtivnikPolicajac(3,2,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(3,15,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(3,2,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(3,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_10ProtivnikPolicajac(3,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_3MMA(3,2,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(3,0,-0.85f+dodatakX,2.14f,0.8f);
		  //lijevo DOLJE
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,6,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,-0.85f+dodatakX,6.61f,0.5f);	
			////MJESANO
		    faIgr.dodajBr_9ProtivnikPas(3,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f+dodatakX,2.14f,0.5f);
		    
		    faIgr.dodajBr_9ProtivnikPas(3,7,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(3,1,11.55f+dodatakX,-0.85f,0.8f);
			/////////////////5 val//////////
		    ////desno gore
		    faIgr.dodajBr_2Sminkerica(4,7,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_4Reper(4,0,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_5ProtivnikPocetni(4,0,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_7ProtivnikCistacica(4,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_6ProtivnikDebeli(4,0,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_1Radnik(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_2Sminkerica(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_4Reper(4,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_5ProtivnikPocetni(4,0,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_7ProtivnikCistacica(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_6ProtivnikDebeli(4,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_1Radnik(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_5ProtivnikPocetni(4,0,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_7ProtivnikCistacica(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_6ProtivnikDebeli(4,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_1Radnik(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    
		    /////lijevo gore glavni
		    faIgr.dodajBr_2Sminkerica(4,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_4Reper(4,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_5ProtivnikPocetni(4,0,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_7ProtivnikCistacica(4,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_6ProtivnikDebeli(4,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_1Radnik(4,0,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_2Sminkerica(4,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_4Reper(4,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_5ProtivnikPocetni(4,0,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_7ProtivnikCistacica(4,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_6ProtivnikDebeli(4,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_1Radnik(4,0,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_5ProtivnikPocetni(4,0,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_7ProtivnikCistacica(4,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_6ProtivnikDebeli(4,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_1Radnik(4,0,-0.85f+dodatakX,2.14f,0.5f);
			
		    ////mjesano
		    faIgr.dodajBr_9ProtivnikPas(4,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(4,0,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(4,0,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f+dodatakX,2.14f,0.5f);	    
		    faIgr.dodajBr_9ProtivnikPas(4,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(4,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(4,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(4,0,11.55f+dodatakX,-0.85f,0.2f);

		  //lijevo DOLJE
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,0,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,-0.85f+dodatakX,6.61f,0.5f);	

			
			////////////6 val////////////////////////////////////////////////////////////////////////////////////
		    /////0
		    xPP=-0.85f;
			yPP=6.08f;
		    
		    sirP=0.85f;
		    visP=1.23f;
		    
		  
			faIgr.dodajBr203RavniPutKut(5,1,xPP+dodatakX, yPP, sirP, visP,speed,0,0);
			///
		
//////////////////////////////////////////////
///////////////////////////////////////////////
			
			   ////desno gore
		    faIgr.dodajBr_16DebeliSerac(5,6,11.55f+dodatakX,-0.85f,0.5f);		
////desno gore
			
faIgr.dodajBr_2Sminkerica(5,7,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_4Reper(5,0,11.55f+dodatakX,-0.85f,0.2f);
faIgr.dodajBr_5ProtivnikPocetni(5,0,11.55f+dodatakX,-0.85f,0.8f);
faIgr.dodajBr_7ProtivnikCistacica(5,1,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_6ProtivnikDebeli(5,0,11.55f+dodatakX,-0.85f,0.2f);
faIgr.dodajBr_1Radnik(5,0,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_2Sminkerica(5,0,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_4Reper(5,1,11.55f+dodatakX,-0.85f,0.2f);
faIgr.dodajBr_5ProtivnikPocetni(5,0,11.55f+dodatakX,-0.85f,0.8f);
faIgr.dodajBr_7ProtivnikCistacica(5,0,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_6ProtivnikDebeli(5,1,11.55f+dodatakX,-0.85f,0.2f);
faIgr.dodajBr_1Radnik(5,0,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_5ProtivnikPocetni(5,0,11.55f+dodatakX,-0.85f,0.8f);
faIgr.dodajBr_7ProtivnikCistacica(5,0,11.55f+dodatakX,-0.85f,0.5f);
faIgr.dodajBr_6ProtivnikDebeli(5,1,11.55f+dodatakX,-0.85f,0.2f);
faIgr.dodajBr_1Radnik(5,0,11.55f+dodatakX,-0.85f,0.5f);
//LIJEVO GORE
faIgr.dodajBr_16DebeliSerac(5,0,-0.85f+dodatakX,2.14f,0.5f);
/////lijevo gore glavni
faIgr.dodajBr_2Sminkerica(5,7,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_4Reper(5,0,-0.85f+dodatakX,2.14f,0.2f);
faIgr.dodajBr_5ProtivnikPocetni(5,0,-0.85f+dodatakX,2.14f,0.8f);
faIgr.dodajBr_7ProtivnikCistacica(5,1,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_6ProtivnikDebeli(5,0,-0.85f+dodatakX,2.14f,0.2f);
faIgr.dodajBr_1Radnik(5,0,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_2Sminkerica(5,1,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_4Reper(5,0,-0.85f+dodatakX,2.14f,0.2f);
faIgr.dodajBr_5ProtivnikPocetni(5,0,-0.85f+dodatakX,2.14f,0.8f);
faIgr.dodajBr_7ProtivnikCistacica(5,1,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_6ProtivnikDebeli(5,0,-0.85f+dodatakX,2.14f,0.2f);
faIgr.dodajBr_1Radnik(5,0,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_5ProtivnikPocetni(5,0,-0.85f+dodatakX,2.14f,0.8f);
faIgr.dodajBr_7ProtivnikCistacica(5,1,-0.85f+dodatakX,2.14f,0.5f);
faIgr.dodajBr_6ProtivnikDebeli(5,0,-0.85f+dodatakX,2.14f,0.2f);
			
			/////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
			
			
			//lijevo DOLJE
			faIgr.dodajBr_16DebeliSerac(5,25,-0.85f+dodatakX,6.61f,0.5f);	
			////MJESANO
		    faIgr.dodajBr_10ProtivnikPolicajac(5,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,1,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_3MMA(5,2,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,0,-0.85f+dodatakX,2.14f,0.8f);
			 /////mjesano
		    faIgr.dodajBr_9ProtivnikPas(5,2,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(5,5,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f);    
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(5,5,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f); 
		
			
			//lijevo DOLJE
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			 /////mjesano
		    faIgr.dodajBr_9ProtivnikPas(5,7,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
		    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_9ProtivnikPas(5,2,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
		    ////desno gore
		    faIgr.dodajBr_16DebeliSerac(5,3,11.55f+dodatakX,-0.85f,0.5f);
		    
		    faIgr.dodajBr_10ProtivnikPolicajac(5,0,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,1,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_3MMA(5,2,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,0,-0.85f+dodatakX,2.14f,0.8f);
		  //lijevo DOLJE
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,6,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			
			
			/////////////////////////////////
			///////////////////////////////
			 ////MJESANO
		    faIgr.dodajBr_10ProtivnikPolicajac(5,30,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,2,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,2,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_3MMA(5,2,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,1,-0.85f+dodatakX,2.14f,0.8f);
			  faIgr.dodajBr_12BossIzvanzemaljac(5, 2, 3, 2.2f,3, 0.5f);
				
				
				faIgr.dodajBr_12BossIzvanzemaljac(5, 2, 9, 1.3f,3, 0.5f);
				
				
				///////////////////////////
				///////////////////////////
			//LIJEVO GORE
			   faIgr.dodajBr_16DebeliSerac(5,30,-0.85f+dodatakX,2.14f,0.5f);

		   ////MJESANO
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,2,11.55f+dodatakX,-0.85f,0.2f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
			 ////desno gore
		    faIgr.dodajBr_16DebeliSerac(5,5,11.55f+dodatakX,-0.85f,0.5f);
		
		    faIgr.dodajBr_8ProtivnikKuhar(5,2,11.55f+dodatakX,-0.85f,0.8f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
		    faIgr.dodajBr_10ProtivnikPolicajac(5,2,-0.85f+dodatakX,2.14f,0.5f);
		    faIgr.dodajBr_3MMA(5,1,-0.85f+dodatakX,2.14f,0.2f);
		    faIgr.dodajBr_8ProtivnikKuhar(5,2,-0.85f+dodatakX,2.14f,0.8f);
		  //lijevo DOLJE
			//lijevo DOLJE
			faIgr.dodajBr_16DebeliSerac(5,4,-0.85f+dodatakX,6.61f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,-0.85f+dodatakX,6.61f,0.5f);	
			
		    faIgr.dodajBr_1Radnik(5,0,-0.85f+dodatakX,2.14f,0.5f);
			
			//////////////////////////////////////////////
			//////////////////////////////////////////////
				  faIgr.dodajBr_12BossIzvanzemaljac(5, 30, 3, 2.2f,3, 0.5f);
					
					
					faIgr.dodajBr_12BossIzvanzemaljac(5, 2, 9, 1.3f,3, 0.5f);
					
					//LIJEVO GORE
					   faIgr.dodajBr_16DebeliSerac(5,7,-0.85f+dodatakX,2.14f,0.5f);
				 /////mjesano
			    faIgr.dodajBr_9ProtivnikPas(5,7,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.2f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
			    faIgr.dodajBr_9ProtivnikPas(5,5,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f);    
			    faIgr.dodajBr_9ProtivnikPas(5,7,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.2f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
			    faIgr.dodajBr_9ProtivnikPas(5,5,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f);    
			  
				 ////desno gore
			    faIgr.dodajBr_16DebeliSerac(5,0,11.55f+dodatakX,-0.85f,0.5f);
				
				
			    faIgr.dodajBr_10ProtivnikPolicajac(5,2,11.55f+dodatakX,-0.85f,0.5f);
			    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
			    faIgr.dodajBr_10ProtivnikPolicajac(5,2,11.55f+dodatakX,-0.85f,0.5f);
			    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
			    faIgr.dodajBr_8ProtivnikKuhar(5,2,11.55f+dodatakX,-0.85f,0.8f);
			    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
			    faIgr.dodajBr_10ProtivnikPolicajac(5,2,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_3MMA(5,2,-0.85f+dodatakX,2.14f,0.2f);
			    faIgr.dodajBr_8ProtivnikKuhar(5,1,-0.85f+dodatakX,2.14f,0.8f);
			    faIgr.dodajBr_10ProtivnikPolicajac(5,2,11.55f+dodatakX,-0.85f,0.5f);
			    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
			    //lijevo DOLJE
				faIgr.dodajBr_16DebeliSerac(5,10,-0.85f+dodatakX,6.61f,0.5f);	
				
			    faIgr.dodajBr_10ProtivnikPolicajac(5,2,11.55f+dodatakX,-0.85f,0.5f);
			    faIgr.dodajBr_3MMA(5,1,11.55f+dodatakX,-0.85f,0.2f);
			    faIgr.dodajBr_8ProtivnikKuhar(5,2,11.55f+dodatakX,-0.85f,0.8f);
			    faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.55f+dodatakX,-0.85f,0.5f);
			    faIgr.dodajBr_10ProtivnikPolicajac(5,2,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_3MMA(5,2,-0.85f+dodatakX,2.14f,0.2f);
			    faIgr.dodajBr_8ProtivnikKuhar(5,1,-0.85f+dodatakX,2.14f,0.8f);
			    
			    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.2f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
			    faIgr.dodajBr_9ProtivnikPas(5,5,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f);    
			    faIgr.dodajBr_9ProtivnikPas(5,7,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f+dodatakX,2.14f,0.2f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.8f);
			    faIgr.dodajBr_9ProtivnikPas(5,5,-0.85f+dodatakX,2.14f,0.5f);
			    faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f+dodatakX,2.14f,0.5f);    
			    
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8planina0jame,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			SpriteHendler dodatak=new SpriteHendler(1);
			dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8borova0suma0mracna,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(501, dodatak);
			SpriteHendler dodatak1=new SpriteHendler(1);  
			dodatak1.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8planina0jame8gore,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(502, dodatak1);
			SpriteHendler dodatak2=new SpriteHendler(1);  
			dodatak2.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8planina0jame8jama0lijeva,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(503, dodatak2);
			SpriteHendler dodatak3=new SpriteHendler(1);  
			dodatak3.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8planina0jame8jama0desna,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(504, dodatak3);
			
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
											  System.exit(0);
										
										
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
