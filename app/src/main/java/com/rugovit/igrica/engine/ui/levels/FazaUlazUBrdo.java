
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

public class FazaUlazUBrdo extends Activity {
	
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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8ulazak0u0planinu, opts);
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
	    ///////		
					getWindow().setFormat(PixelFormat.RGBA_4444);
					opts.inPreferredConfig = Bitmap.Config.ARGB_4444;

	 
	    NativeHeapAllocatedSize= Debug.getNativeHeapAllocatedSize()/1024;
		 GlobalAllocSize=Debug.getGlobalAllocSize()/1024;
		 ThreadAllocSize= Debug.getThreadAllocSize()/1024;                             

	        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
	      
	   
	    //////////inicijalizacija gamelogica i uimanager pozadine
	    /////////ui manager*/
	        faIgr=new FazeIgre(7,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		  
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
				faIgr.setDelayIzmVal(10);
			    faIgr.setDelayIzmVal(0,15);
			    faIgr.setDelayIzmVal(1,20);
			    faIgr.setDelayIzmVal(2,20);
			    faIgr.setDelayIzmVal(3,25);
			    faIgr.setDelayIzmVal(4,30);
			    faIgr.setDelayIzmVal(5,30);
			    faIgr.setDelayIzmVal(6,35);

				// TODO Auto-generated method stub
			
				 
				 float omjer=1.1f;
				 float polX=-1.16f;
				 float polY=6.74f;
				 float sir=2.5f;
				 float vis=2.12f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 faIgr. dodajPocetneParametreIgre(100,10);
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				 faIgr.setDelayIzmVal(30);
				   //drveca
				 polX=7.4f;
				 polY=-1.87f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=7.4f;
				 polY=-1.87f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);			
				 ///
				 //vrataleteceg tanjura
				 polX=6.84f;
				 polY=4.02f;
				 sir=1.06f;
				 vis=1.34f;
				 faIgr.dodaj505DodatakNaMapuIzvrsiJedanputIZavrsiNa(6, 5,  507, 0, 11,2,polX, polY, sir,vis, 0, 0, 0, 0, vis/4);
				 
				 polX=-1.27f;
				 polY=-0.71f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=10.51f;
				 polY=-2.15f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 ///
				 
				  //502 planina rub gore lijevo
				 polX=0f;
				 polY=0f;
				 sir=1.38f;
				 vis=1.38f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				  //503 planina rub gore desno
				 polX=2.29f;
				 polY=0f;
				 sir=3.81f;
				 vis=2.08f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 503,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				  //509 planina rub gore desno desno
				 polX=5.96f;
				 polY=0f;
				 sir=1.94f;
				 vis=1.38f;
				// faIgr.dodaj503DodatakNaMapuPomakSortiranja(brVala, sec, brSlikeUListiSpriteova, pocmiOdStupca, kodSortiranja, xSlike, ySlike, sirSlike, visSlike, xLog, yLog, sirLog, visLog, yPomSortiranja);
				 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 509,0,2,polX, polY, sir,vis,0, 0f, 0,0,-vis/2);
				  //504 tunel gore
				 polX=0f;
				 polY=1.94f;
				 sir=0.78f;
				 vis=1.16f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 504,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				  //505 tunel srednji
				 polX=0f;
				 polY=2.08f;
				 sir=1.55f;
				 vis=3.7f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 505,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				  //506 tunel donji
				 polX=0f;
				 polY=4.52f;
				 sir=0.67f;
				 vis=2.82f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 506,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				  //508 leteci tanjur
				 polX=7.09f;
				 polY=3.67f;
				 sir=2.93f;
				 vis=1.94f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 508,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
		
		  /////dodavanje èlanova igrice
				float dodatak=0.15f; 
	 	///gornji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.94f,3.92f+dodatak);//
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.08f,4.3f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.65f,4.14f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.04f,2.32f+dodatak);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.18f,1.99f+dodatak);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.39f,3.6f+dodatak);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.46f,3.26f+dodatak);
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.54f,1.55f+dodatak);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.53f,1.34f+dodatak);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.18f,1.53f+dodatak);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,12.02f,2.9f+dodatak);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,12.06f,4.66f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,12.02f,6.53f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.52f,7.1f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.65f,7.13f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.99f,5.05f+dodatak);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.42f,7.09f+dodatak);
		faIgr.dodajBr200ToranjEmbrio(0,0,4.23f,7.08f+dodatak);
		faIgr.dodajBr200ToranjEmbrio(0,0,4.35f,5.05f+dodatak);
		faIgr.dodajBr200ToranjEmbrio(0,0,2.93f,5.04f+dodatak);
		faIgr.dodajBr200ToranjEmbrio(0,0,7.2f,7.1f+dodatak);
		
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
			    ///////////////////////////desni segment///////////////////////////////////////
			    /////0
			    visP=0.85f;
				sirP=1.23f;
				xPP=3.18f;
				yPP=-0.85f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
			    /////1
			    visP=0.85f;
				sirP=1.24f;
				xPP=3.18f;
				yPP=0.00f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
			    /////2
			    visP=0.65f;
				sirP=1.24f;
				xPP=3.18f;
				yPP=0.63f;
				faIgr.dodajBr205OkukaKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////3
			    visP=1.04f;
				sirP=0.7f;
				xPP=2.48f;
				yPP=0.33f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////4
			    visP=0.51f;
				sirP=1.04f;
				xPP=1.45f;
				yPP=0.34f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////5
			    visP=0.5f;
				sirP=1.2f;
				xPP=1.29f;
				yPP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,250,++brPLijevi);
				///
				 /////6
				sirP=1.24f;
			    visP=0.85f;				
				xPP=1.2f;
				yPP=1.36f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,260,++brPLijevi);
				///
				 /////7
				sirP=1.24f;
			    visP=0.85f;				
				xPP=1.11f;
				yPP=2.2f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,260,++brPLijevi);
				///
				 /////8
				sirP=0.61f;
			    visP=0.85f;				
				xPP=1.12f;
				yPP=3.04f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,230,++brPLijevi);
				///
				 /////-8
				sirP=0.6f;
			    visP=0.35f;				
				xPP=1.13f;
				yPP=3.88f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,-brPLijevi);
				///
				 /////9
				sirP=0.61f;
			    visP=0.85f;				
				xPP=1.73f;
				yPP=3.04f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,310,++brPLijevi);
				///
				 /////-9
				sirP=0.6f;
			    visP=0.35f;				
				xPP=1.73f;
				yPP=3.88f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,-brPLijevi);
				///
				 /////10
				sirP=0.85f;
			    visP=1.24f;				
				xPP=2.35f;
				yPP=2.94f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////11
				sirP=0.85f;
			    visP=1.24f;				
				xPP=3.2f;
				yPP=2.83f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////12
				sirP=0.85f;
			    visP=1.24f;				
				xPP=4.05f;
				yPP=2.69f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////13
				sirP=0.85f;
			    visP=1.24f;				
				xPP=4.9f;
				yPP=2.53f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////14
				sirP=0.85f;
			    visP=1.24f;				
				xPP=5.74f;
				yPP=2.36f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////15
				sirP=0.85f;
			    visP=1.24f;				
				xPP=6.59f;
				yPP=2.2f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////16
				sirP=0.85f;
			    visP=1.24f;				
				xPP=7.44f;
				yPP=2.06f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////17
				sirP=0.85f;
			    visP=1.24f;				
				xPP=8.29f;
				yPP=1.91f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////18
				sirP=0.85f;
			    visP=1.24f;				
				xPP=9.14f;
				yPP=1.79f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
				///
				 /////19
				sirP=1.24f;
			    visP=0.69f;				
				xPP=9.99f;
				yPP=1.77f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////20
				sirP=1.24f;
			    visP=0.69f;				
				xPP=9.99f;
				yPP=2.46f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,300,++brPLijevi);
				///
				 /////21
				sirP=1.24f;
			    visP=0.85f;				
				xPP=10.15f;
				yPP=3.15f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////22
				sirP=1.24f;
			    visP=0.85f;				
				xPP=10.27f;
				yPP=3.99f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////23
				sirP=1.24f;
			    visP=0.85f;				
				xPP=10.18f;
				yPP=4.84f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////24
				sirP=1.45f;
			    visP=0.7f;				
				xPP=9.98f;
				yPP=5.68f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,210,++brPLijevi);
				///
				 /////25
				sirP=1.41f;
			    visP=0.6f;				
				xPP=9.98f;
				yPP=6.39f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////26
				sirP=0.85f;
			    visP=1.24f;				
				xPP=9.14f;
				yPP=5.69f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////27
				sirP=0.85f;
			    visP=1.24f;				
				xPP=8.29f;
				yPP=5.74f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				
				 /////28
				sirP=0.85f;
			    visP=1.24f;				
				xPP=7.44f;
				yPP=5.74f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////29
				sirP=0.85f;
			    visP=1.24f;				
				xPP=6.59f;
				yPP=5.73f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				
				 /////30
				sirP=0.51f;
			    visP=1.24f;				
				xPP=6.08f;
				yPP=5.73f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,200,++brPLijevi);
				///
				 /////31
				sirP=0.51f;
			    visP=1.24f;				
				xPP=5.57f;
				yPP=5.73f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////32
				sirP=0.51f;
			    visP=1.24f;				
				xPP=5.06f;
				yPP=5.74f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////33
				sirP=1.24f;
			    visP=0.85f;				
				xPP=5.1f;
				yPP=6.95f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
			    /////34
				sirP=1.24f;
			    visP=0.85f;				
				xPP=5.1f;
				yPP=7.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				///kraj
				
				sirP=1.24f;
			    visP=0.85f;		
				 xPP=5.1f;
			     yPP=8.65f;
			  
				faIgr.dodajBr204Kraj(0,0,xPP+dodatakX, yPP, sirP, visP,0,speed,++brPLijevi);
				////
				////////////////////////////tuel dio////////////////////////////////////////////////
				brPLijevi=8; 
				/////9
				sirP=0.85f;
			    visP=1.24f;				
				xPP=0.26f;
				yPP=3.01f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////10
				sirP=0.85f;
				visP=1.23f;				
				xPP=-0.59f;
				yPP=3.01f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////11
				sirP=0.85f;
				visP=1.23f;				
				xPP=-1.44f;
				yPP=3.04f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////12
				sirP=0.85f;
				visP=1.23f;				
				xPP=-2.29f;
				yPP=3.03f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////13
				sirP=0.85f;
				visP=1.23f;				
				xPP=-3.14f;
				yPP=3.03f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////14
				sirP=1.23f;
				visP=0.85f;				
				xPP=-4.37f;
				yPP=3.01f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////15
				sirP=1.23f;
				visP=0.85f;				
				xPP=-4.37f;
				yPP=3.86f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////16
				sirP=1.23f;
				visP=0.95f;				
				xPP=-4.37f;
				yPP=4.73f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////17
				sirP=0.85f;
				visP=1.23f;				
				xPP=-4.37f;
				yPP=5.65f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////18
				sirP=0.85f;
				visP=1.23f;				
				xPP=-3.52f;
				yPP=5.68f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////19
				sirP=0.85f;
				visP=1.23f;				
				xPP=-2.67f;
				yPP=5.66f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////20
				sirP=0.85f;
				visP=1.23f;				
				xPP=-1.82f;
				yPP=5.71f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////21
				sirP=0.85f;
				visP=1.23f;				
				xPP=-0.97f;
				yPP=5.71f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////22
				sirP=0.85f;
				visP=1.23f;				
				xPP=-0.14f;
				yPP=5.7f;
				faIgr.dodajBr203RavniPutKutInertni(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////23
				sirP=0.85f;
				visP=1.23f;				
				xPP=0.72f;
				yPP=5.71f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////24
				sirP=0.85f;
				visP=1.23f;				
				xPP=1.57f;
				yPP=5.71f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////25
				sirP=0.85f;
				visP=1.23f;				
				xPP=2.42f;
				yPP=5.73f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////26
				sirP=0.85f;
				visP=1.23f;				
				xPP=3.27f;
				yPP=5.71f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////27
				sirP=0.95f;
				visP=1.23f;				
				xPP=4.12f;
				yPP=5.71f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				/////////////////////////////////////////////////////////////////////////////////////
				///
		//1. Val
		//lijevo	
				sirP=1.24f;
			    visP=0.85f;				
				xPP=1.2f;
				yPP=1.36f;
		/*faIgr.dodajBr_12BossIzvanzemaljac(0,1,1.89f+dodatakX,1.86f,0.2f);
		
		faIgr.dodajBr_5ProtivnikPocetni(0,0,3.7f+dodatakX,-0.85f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,0,3.7f+dodatakX,-0.85f,0.8f);	
		
		
		faIgr.dodajBr_9ProtivnikPas(0,0,3.7f+dodatakX,-0.85f,0.8f); */
				
				//faIgr.dodajBr_12BossIzvanzemaljacProgramiranZaIzlazIzTanjura(0,1,1.89f+dodatakX,1.86f,0.7f);
				/*faIgr.dodajBr_15ProtivnikStarleta(0,1,3.7f+dodatakX,-0.85f,0.2f);		
				faIgr.dodajBr_15ProtivnikStarleta(0,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_15ProtivnikStarleta(0,1,3.7f+dodatakX,-0.85f,0.2f);		
				faIgr.dodajBr_15ProtivnikStarleta(0,0,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_15ProtivnikStarleta(0,0,3.7f+dodatakX,-0.85f,0.4f);	
				*/
				
			  
				//faIgr.dodajBr_12BossIzvanzemaljacProgramiranZaIzlazIzTanjura(0,3,1.89f+dodatakX,1.86f,0.7f);
				//faIgr.dodajBr_3MMA(6,0,3.7f+dodatakX,-0.85f,0.8f);
				///// 1val
				faIgr.dodajBr_7ProtivnikCistacica(0,5,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(0,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(0,1,3.7f+dodatakX,-0.85f,0.6f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_6ProtivnikDebeli(0,1,3.7f+dodatakX,-0.85f,0.6f);					 
				faIgr.dodajBr_7ProtivnikCistacica(0,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(0,0,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_4Reper(0,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,1,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_6ProtivnikDebeli(0,0,3.7f+dodatakX,-0.85f,0.8f);	
				
				faIgr.dodajBr_7ProtivnikCistacica(0,5,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(0,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(0,1,3.7f+dodatakX,-0.85f,0.6f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_6ProtivnikDebeli(0,1,3.7f+dodatakX,-0.85f,0.6f);					 
				faIgr.dodajBr_7ProtivnikCistacica(0,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(0,0,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_4Reper(0,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,1,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_6ProtivnikDebeli(0,0,3.7f+dodatakX,-0.85f,0.8f);	
				///// 2 val
				faIgr.dodajBr_7ProtivnikCistacica(1,5,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(1,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(1,1,3.7f+dodatakX,-0.85f,0.6f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_6ProtivnikDebeli(1,1,3.7f+dodatakX,-0.85f,0.6f);					 
				faIgr.dodajBr_7ProtivnikCistacica(1,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(1,0,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_4Reper(1,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_6ProtivnikDebeli(1,0,3.7f+dodatakX,-0.85f,0.8f);	

				faIgr.dodajBr_9ProtivnikPas(1,5,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.5f);	
	
				faIgr.dodajBr_7ProtivnikCistacica(1,10,3.7f+dodatakX,-0.85f,0.2f);		
				faIgr.dodajBr_1Radnik(1,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_4Reper(1,1,3.7f+dodatakX,-0.85f,0.4f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,0,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_6ProtivnikDebeli(1,1,3.7f+dodatakX,-0.85f,0.3f);					 
				faIgr.dodajBr_7ProtivnikCistacica(1,1,3.7f+dodatakX,-0.85f,0.4f);		
				faIgr.dodajBr_1Radnik(1,0,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_4Reper(1,1,3.7f+dodatakX,-0.85f,0.4f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_6ProtivnikDebeli(1,0,3.7f+dodatakX,-0.85f,0.3f);	
				
				faIgr.dodajBr_9ProtivnikPas(1,8,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(1,1,3.7f+dodatakX,-0.85f,0.2f);	
				/////////////3. val/////////////////////////////////////

				faIgr.dodajBr_10ProtivnikPolicajac(2,5,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(2,0,3.7f+dodatakX,-0.85f,0.8f);	
				
				faIgr.dodajBr_7ProtivnikCistacica(2,5,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(2,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(2,1,3.7f+dodatakX,-0.85f,0.6f);		
				faIgr.dodajBr_5ProtivnikPocetni(2,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_6ProtivnikDebeli(2,1,3.7f+dodatakX,-0.85f,0.7f);					 
				faIgr.dodajBr_7ProtivnikCistacica(2,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(2,0,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_4Reper(2,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(2,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_6ProtivnikDebeli(2,0,3.7f+dodatakX,-0.85f,0.8f);	
				
				faIgr.dodajBr_10ProtivnikPolicajac(2,10,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(2,0,3.7f+dodatakX,-0.85f,0.8f);	
		
				faIgr.dodajBr_7ProtivnikCistacica(2,7,3.7f+dodatakX,-0.85f,0.2f);		
				faIgr.dodajBr_1Radnik(2,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_4Reper(2,1,3.7f+dodatakX,-0.85f,0.5f);		
				faIgr.dodajBr_5ProtivnikPocetni(2,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_6ProtivnikDebeli(2,1,3.7f+dodatakX,-0.85f,0.2f);	
				
				faIgr.dodajBr_9ProtivnikPas(2,8,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(2,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(2,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(2,1,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(2,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(2,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(2,1,3.7f+dodatakX,-0.85f,0.5f);	
				/////////4 val///////////////////////////////////////////////////
				
				faIgr.dodajBr_15ProtivnikStarleta(3,5,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.8f);
				
				faIgr.dodajBr_7ProtivnikCistacica(3,0,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(3,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(3,1,3.7f+dodatakX,-0.85f,0.6f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_6ProtivnikDebeli(3,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.8f);
				
				faIgr.dodajBr_10ProtivnikPolicajac(3,10,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(3,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_10ProtivnikPolicajac(3,3,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(3,1,3.7f+dodatakX,-0.85f,0.8f);	
		
				faIgr.dodajBr_7ProtivnikCistacica(3,5,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(3,0,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_4Reper(3,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_6ProtivnikDebeli(3,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_15ProtivnikStarleta(3,1,3.7f+dodatakX,-0.85f,0.8f);
				

				
				faIgr.dodajBr_9ProtivnikPas(3,6,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(3,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(3,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(3,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(3,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(3,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(3,1,3.7f+dodatakX,-0.85f,0.5f);	
				
			
				///////////////////5. val//////////////////////////////////////
				
				
				faIgr.dodajBr_7ProtivnikCistacica(4,0,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(4,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(4,1,3.7f+dodatakX,-0.85f,0.6f);		
				faIgr.dodajBr_5ProtivnikPocetni(4,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_6ProtivnikDebeli(4,1,3.7f+dodatakX,-0.85f,0.7f);					 
				faIgr.dodajBr_7ProtivnikCistacica(4,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(4,0,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_4Reper(4,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.8f);
				
				
				faIgr.dodajBr_15ProtivnikStarleta(4,10,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_10ProtivnikPolicajac(4,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(4,0,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_15ProtivnikStarleta(4,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_10ProtivnikPolicajac(4,7,3.7f+dodatakX,-0.85f,0.7f);	
				
				faIgr.dodajBr_9ProtivnikPas(4,6,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.5f);	
				
				faIgr.dodajBr_10ProtivnikPolicajac(4,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_3MMA(4,0,3.7f+dodatakX,-0.85f,0.3f);
				faIgr.dodajBr_9ProtivnikPas(4,8,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(4,1,3.7f+dodatakX,-0.85f,0.2f);	
                ///////////////////6. val//////////////////////////////////////
				faIgr.dodajBr_7ProtivnikCistacica(5,7,3.7f+dodatakX,-0.85f,0.2f);		
				faIgr.dodajBr_1Radnik(5,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(5,1,3.7f+dodatakX,-0.85f,0.4f);		
				faIgr.dodajBr_5ProtivnikPocetni(5,0,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_6ProtivnikDebeli(5,1,3.7f+dodatakX,-0.85f,0.2f);					 
				faIgr.dodajBr_7ProtivnikCistacica(5,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(5,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_4Reper(5,1,3.7f+dodatakX,-0.85f,0.8f);		
				faIgr.dodajBr_7ProtivnikCistacica(5,0,3.7f+dodatakX,-0.85f,0.2f);		
				faIgr.dodajBr_1Radnik(5,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_4Reper(5,1,3.7f+dodatakX,-0.85f,0.4f);		
				faIgr.dodajBr_5ProtivnikPocetni(5,0,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_6ProtivnikDebeli(5,1,3.7f+dodatakX,-0.85f,0.2f);					 
				faIgr.dodajBr_7ProtivnikCistacica(5,1,3.7f+dodatakX,-0.85f,0.7f);		
				faIgr.dodajBr_1Radnik(5,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_4Reper(5,1,3.7f+dodatakX,-0.85f,0.8f);
				
				faIgr.dodajBr_15ProtivnikStarleta(5,2,3.7f+dodatakX,-0.85f,0.2f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.3f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.4f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_10ProtivnikPolicajac(5,2,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_10ProtivnikPolicajac(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(5,5,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.5f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.5f);
				
				faIgr.dodajBr_3MMA(5,12,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_10ProtivnikPolicajac(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);			
				faIgr.dodajBr_15ProtivnikStarleta(5,0,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.8f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_10ProtivnikPolicajac(3,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_10ProtivnikPolicajac(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);	
			
				
				faIgr.dodajBr_15ProtivnikStarleta(5,5,3.7f+dodatakX,-0.85f,0.2f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.3f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.4f);
				faIgr.dodajBr_15ProtivnikStarleta(5,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_10ProtivnikPolicajac(5,2,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_10ProtivnikPolicajac(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_3MMA(5,1,3.7f+dodatakX,-0.85f,0.8f);
				
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,8,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,3.7f+dodatakX,-0.85f,0.6f);
			
				
				
				faIgr.dodajBr_9ProtivnikPas(5,8,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.3f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.5f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.7f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.8f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.4f);	
				faIgr.dodajBr_9ProtivnikPas(5,1,3.7f+dodatakX,-0.85f,0.2f);	
				faIgr.dodajBr_9ProtivnikPas(5,0,3.7f+dodatakX,-0.85f,0.5f);
				
				faIgr.dodajBr_9ProtivnikPas(5,15,3.7f+dodatakX,-0.85f,0.3f);	
				
				
			
				/////////////////////7. val/////////////////////////////////////////////////
				faIgr.dodajBr_12BossIzvanzemaljacProgramiranZaIzlazIzTanjura(6,3,1.89f+dodatakX,1.86f,0.7f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,35,3.7f+dodatakX,-0.85f,0.6f);	
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,3.7f+dodatakX,-0.85f,0.6f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,3.7f+dodatakX,-0.85f,0.7f);
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,3.7f+dodatakX,-0.85f,0.6f);
			
		
				////////////////////////////////////////////////////////////////////////////
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8ulazak0u0planinu,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			SpriteHendler dodatak=new SpriteHendler(1);
			dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8borva0suma,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(501, dodatak);
			SpriteHendler dodatak1=new SpriteHendler(1);  
			dodatak1.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8rub0gore0lijevo,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(502, dodatak1);
			SpriteHendler dodatak2=new SpriteHendler(1);  
			dodatak2.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8rub0gore0desno,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(503, dodatak2);
			SpriteHendler dodatak3=new SpriteHendler(1);  
			dodatak3.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8tunel0gore,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(504, dodatak3);
			SpriteHendler dodatak4=new SpriteHendler(1);  
			dodatak4.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8tunel0srednji,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(505, dodatak4);
			SpriteHendler dodatak5=new SpriteHendler(1);  
			dodatak5.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8tunel0donji,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(506, dodatak5);
			SpriteHendler dodatak6=new SpriteHendler(1);  
			dodatak6.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8vrata0od0leteceg0tanjura,opts),12, 1,4);//ispaljenje
			faIgr.dodajSprite(507, dodatak6);
			SpriteHendler dodatak7=new SpriteHendler(1);  
			dodatak7.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8leteci0tanjur,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(508, dodatak7);
			SpriteHendler dodatak8=new SpriteHendler(1);  
			dodatak8.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8ulaz0u0planinu8rub0gore0desno0desno,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(509, dodatak8);
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
