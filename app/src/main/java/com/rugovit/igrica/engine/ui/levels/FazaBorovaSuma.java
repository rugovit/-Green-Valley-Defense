
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

public class FazaBorovaSuma extends Activity {

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
		
		       

	        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
	        getWindow().setFormat(PixelFormat.RGBA_4444);
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
				       Bitmap  bi;
			  
					 
	
			        this.faIgr.lodirajResurse(activity, soundPool,opts,gLog,uiMan);
			}
			
			@Override
			public void loadMetoda() {


				// TODO Auto-generated method stub
			    ///////		
			
				 float omjer=1.1f;
				 float polX=-0.49f;
				 float polY=5.47f;
				 float sir=2.4f;
				 float vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 faIgr. dodajPocetneParametreIgre(100,10);
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
		
				    faIgr.setDelayIzmVal(0,10);
				    faIgr.setDelayIzmVal(1,15);
				    faIgr.setDelayIzmVal(2,30);
				    faIgr.setDelayIzmVal(3,40);
				    faIgr.setDelayIzmVal(4,40);
				    faIgr.setDelayIzmVal(5,40);
				 faIgr.setDelayIzmVal(30);
				   //drveca
				 polX=2.89f;
				 polY=-0.42f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=4.59f;
				 polY=-0.49f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
			      
				 polX=0.64f;
				 polY=6.31f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=0.56f;
				 polY=-2.33f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=0.88f;
				 polY=-2.19f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=11.54f;
				 polY=-1.69f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=11.89f;
				 polY=4.16f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=10.55f;
				 polY=5.5f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=9.28f;
				 polY=6.35f;
				 sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);

			
				 //litica 2
				 polX=8.75f;
				 polY=0f;
				 sir=1.76f;
				 vis=1.55f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 504,0,2,polX, polY, sir,vis,polX, polY, sir,vis);
			
	
		  /////dodavanje èlanova igrice
				float dodatak=-0.15f; 
	 	///gornji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.42f,2.75f);//
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.42f,4.68f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.58f,1.7f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.87f,3.39f);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.94f,3.18f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.78f,2.99f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.25f,5.27f);
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.61f,5.28f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.44f,1.94f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.3f,3.36f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.82f,5.04f);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,12.02f,2.23f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.94f,3.7f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.3f,7.46f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.41f,7.36f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.31f,7.34f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.46f,7.34f);
		faIgr.dodajBr200ToranjEmbrio(0,0,2.93f,5.04f);
		
	 
		  //pocetak
				int brP=0;
				int brPLijevi=0;
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
			  
			    /////1
				sirP=1.23f;
				visP=0.85f;
						
				xPP=10.35f;
				yPP=-0.03f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////2
				sirP=1.24f;
				visP=0.85f;
						
				xPP=10.27f;
				yPP=0.83f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////3
				sirP=1.24f;
				visP=0.85f;
						
				xPP=10.18f;
				yPP=1.68f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////4
				sirP=1.24f;
				visP=0.85f;
						
				xPP=10.12f;
				yPP=2.53f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////5
				sirP=1.24f;
				visP=0.85f;
						
				xPP=10.04f;
				yPP=3.38f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////6
				sirP=1.24f;
				visP=0.85f;
						
				xPP=9.97f;
				yPP=4.23f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////7
				sirP=1.24f;
				visP=0.85f;
						
				xPP=9.87f;
				yPP=5.08f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,265,++brPLijevi);
				///
			    /////8
				sirP=1.24f;
				visP=0.62f;
						
				xPP=9.87f;
				yPP=5.93f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,220,++brPLijevi);
				///
			    /////9
				sirP=1.24f;
				visP=0.62f;
						
				xPP=9.87f;
				yPP=6.55f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////10
				sirP=0.85f;
				visP=1.24f;
						
				xPP=9.02f;
				yPP=5.93f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,175,++brPLijevi);
				///
			    ////11
				sirP=0.85f;
				visP=1.24f;
						
				xPP=8.17f;
				yPP=5.85f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,175,++brPLijevi);
				///
			    /////12
				sirP=0.85f;
				visP=1.24f;
						
				xPP=7.32f;
				yPP=5.75f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,175,++brPLijevi);
				///
			    /////13
				sirP=0.85f;
				visP=1.24f;
						
				xPP=6.47f;
				yPP=5.75f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////14
				sirP=0.85f;
				visP=1.24f;
						
				xPP=5.62f;
				yPP=5.75f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////15
				sirP=0.85f;
				visP=1.24f;
						
				xPP=4.77f;
				yPP=5.75f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
			    /////16
				sirP=0.62f;
				visP=1.24f;
						
				xPP=4.15f;
				yPP=5.75f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,105,++brPLijevi);
				///
			    /////17
				sirP=0.62f;
				visP=1.24f;
						
				xPP=3.53f;
				yPP=5.75f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,85,++brPLijevi);
				///
			    /////18
				sirP=1.24f;
				visP=0.85f;
						
				xPP=3.53f;
				yPP=4.9f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,++brPLijevi);
				///
			    /////19
				sirP=1.24f;
				visP=0.61f;
						
				xPP=3.53f;
				yPP=4.29f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,50,++brPLijevi);
				///
			    /////20
				sirP=1.24f;
				visP=0.61f;
						
				xPP=3.53f;
				yPP=3.68f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
			    /////21
				sirP=0.85f;
				visP=1.24f;
						
				xPP=4.77f;
				yPP=3.68f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,5,++brPLijevi);
				///
			    /////22
				sirP=0.85f;
				visP=1.24f;
						
				xPP=5.62f;
				yPP=3.61f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,5,++brPLijevi);
				///
			    /////23
				sirP=0.85f;
				visP=1.24f;
						
				xPP=6.47f;
				yPP=3.54f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,5,++brPLijevi);
				///
			    /////24
				sirP=0.67f;
				visP=1.24f;
						
				xPP=7.32f;
				yPP=3.54f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,50,++brPLijevi);
				///
			    /////25
				sirP=0.67f;
				visP=1.24f;
						
				xPP=7.99f;
				yPP=3.54f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,++brPLijevi);
				///
			    /////26
				sirP=1.24f;
				visP=0.85f;
						
				xPP=7.42f;
				yPP=2.69f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,85,++brPLijevi);
				///
			    /////27
				sirP=1.24f;
				visP=0.85f;
						
				xPP=7.51f;
				yPP=1.84f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,85,++brPLijevi);
				///
			    /////28
				sirP=1.06f;
				visP=0.6f;
						
				xPP=7.76f;
				yPP=1.23f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,85,++brPLijevi);
				///
			    /////.1 posebni
							sirP=0.18f;
							visP=0.6f;
									
							xPP=7.58f;
							yPP=1.23f;
						    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,65,-brPLijevi);
							///
			    /////29
				sirP=0.99f;
				visP=0.39f;
						
				xPP=7.87f;
				yPP=0.85f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,++brPLijevi);
				///
			    /////.1 posebni
				sirP=0.18f;
				visP=0.39f;
						
				xPP=7.69f;
				yPP=0.85f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,-brPLijevi);
				///
			    /////30
				sirP=1.4f;
				visP=0.44f;
						
				xPP=7.8f;
				yPP=0.42f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,110,++brPLijevi);
				///
			    /////.1 posebni
							sirP=0.25f;
							visP=0.42f;
									
							xPP=7.55f;
							yPP=0.42f;
						    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,-brPLijevi);
							///
			    /////31
				sirP=1.24f;
				visP=0.44f;
						
				xPP=7.54f;
				yPP=0f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,110,++brPLijevi);
				///
			    /////32
				sirP=0.39f;
				visP=0.79f;
						
				xPP=7.16f;
				yPP=0.0f;
			    faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,100,++brPLijevi);
				///
			    /////33
				sirP=1.23f;
				visP=0.85f;
						
				xPP=6.77f;
				yPP=-0.85f;
			    faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,++brPLijevi);
				///
			    /////34 kraj
				sirP=2f;
				visP=0.85f;
						
				xPP=6.77f;
				yPP=-0.85f;
			    faIgr.dodajBr204Kraj(0,0,xPP+dodatakX, yPP, sirP, visP, 0, -speed, ++brPLijevi);
				///////////////////////////lijevi dio//////////////////////////
			    brPLijevi=-1;
			    //////0
				sirP=0.85f;
				visP=1.23f;
						
				xPP=-0.85f;
				yPP=0.99f;
				
				 faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
			    //////1
			    sirP=0.85f;
				visP=1.24f;
							
				xPP=0.0f;
				yPP=0.99f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				  //////2
			    sirP=0.61f;
				visP=1.24f;
							
				xPP=0.85f;
				yPP=0.99f;
					
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,340,++brPLijevi);
				///
				  //////3
			    sirP=0.61f;
				visP=1.24f;
							
				xPP=1.45f;
				yPP=0.99f;
					
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				//////4
			    sirP=1.24f;
				visP=0.85f;
							
				xPP=0.82f;
				yPP=2.23f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,275,++brPLijevi);
				///
				  //////5
			    sirP=1.24f;
				visP=0.85f;
							
				xPP=0.91f;
				yPP=3.08f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,275,++brPLijevi);
				///
				  //////6
			    sirP=1.24f;
				visP=0.85f;
							
				xPP=1.0f;
				yPP=3.93f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,275,++brPLijevi);
				///
				  //////7
			    sirP=1.24f;
				visP=0.85f;
							
				xPP=1.06f;
				yPP=4.78f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,275,++brPLijevi);
				///
				  //////8
			    sirP=1.24f;
				visP=0.68f;
							
				xPP=1.05f;
				yPP=5.633f;
					
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,300,++brPLijevi);
				///
				  //////9
			    sirP=1.24f;
				visP=0.68f;
							
				xPP=1.05f;
				yPP=6.31f;
					
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				  //////10
			    sirP=0.62f;
				visP=1.24f;
							
				xPP=2.29f;
				yPP=5.75f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				  //////11
			    sirP=0.63f;
				visP=1.24f;
							
				xPP=2.91f;
				yPP=5.75f;
					
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				/////////////////////////////////////////////////////////////////////////////////////
				///
		//1. Val
		//desno	
				sirP=1.23f;
			    visP=0.85f;
			
				xPP=10.34f;
				yPP=-0.85f;
		/*
			
		faIgr.dodajBr_5ProtivnikPocetni(0,1,11f+dodatakX,-0.85f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,0,11f+dodatakX,-0.85f,0.8f);	
			
		faIgr.dodajBr_9ProtivnikPas(0,0,11f+dodatakX,-0.85f,0.8f); 
		faIgr.dodajBr_9ProtivnikPas(0,0,11f+dodatakX,-0.85f,0.2f); */
		//lijevo
		sirP=0.85f;
		visP=1.23f;
				
		xPP=-0.85f;
		yPP=0.99f;
		
		//////////////////////////////////////////////////////////1 val///////////////////////////////////////////////////////////////////////////////////////////////	
		faIgr.dodajBr_14ProtivnikStudentica(0,5,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,3,-0.85f+dodatakX,1.7f,0.8f);	
		faIgr.dodajBr_4Reper(0,2,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f+dodatakX,1.7f,0.8f);	
		
		faIgr.dodajBr_14ProtivnikStudentica(0,5,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f+dodatakX,1.7f,0.6f);	
		faIgr.dodajBr_4Reper(0,3,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f+dodatakX,1.7f,0.8f);	
		faIgr.dodajBr_14ProtivnikStudentica(0,1,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_4Reper(0,1,-0.85f+dodatakX,1.7f,0.5f);	
		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(0,15,-0.85f+dodatakX,1.7f,0.8f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(0,2,-0.85f+dodatakX,1.7f,0.5f);		
		
		faIgr.dodajBr_14ProtivnikStudentica(0,5,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f+dodatakX,1.7f,0.6f);	
		faIgr.dodajBr_4Reper(0,1,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f+dodatakX,1.7f,0.8f);	
		faIgr.dodajBr_14ProtivnikStudentica(0,1,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_4Reper(0,1,-0.85f+dodatakX,1.7f,0.5f);	
		/////////////////////////////////////////////////////////2 val//////////////////////////////////////////////////////////////////////////////////////////////////
		brPLijevi=-1;
		/////0
		sirP=1.23f;
	    visP=0.85f;
	
		xPP=10.34f;
		yPP=-0.85f;
		faIgr.dodajBr203RavniPutKut(0,1,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
		///
		/////lijevo//
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(1,7,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(1,3,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(1,3,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(1,3,-0.85f+dodatakX,1.7f,0.5f);		
		///desno///
		faIgr.dodajBr_8ProtivnikKuhar(1,1,11f+dodatakX,-0.85f,0.3f);	
		faIgr.dodajBr_8ProtivnikKuhar(1,2,11f+dodatakX,-0.85f,0.5f);	
		faIgr.dodajBr_10ProtivnikPolicajac(1,5,11f+dodatakX,-0.85f,0.2f);			
		faIgr.dodajBr_10ProtivnikPolicajac(1,3,11f+dodatakX,-0.5f,0.2f);		
		faIgr.dodajBr_10ProtivnikPolicajac(1,2,11f+dodatakX,-0.45f,0.2f);				
		////lijevo//////////////
		faIgr.dodajBr_2Sminkerica(1,8,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_2Sminkerica(1,2,-0.85f+dodatakX,1.7f,0.8f);		
		faIgr.dodajBr_2Sminkerica(1,2,-0.85f+dodatakX,1.7f,0.2f);	
		
		faIgr.dodajBr_1Radnik(1,5,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_1Radnik(1,3,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_5ProtivnikPocetni(1,3,-0.85f+dodatakX,1.7f,0.6f);	
		
		
		faIgr.dodajBr_14ProtivnikStudentica(1,5,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.85f+dodatakX,1.7f,0.6f);		
		faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.85f+dodatakX,1.7f,0.8f);	
		faIgr.dodajBr_14ProtivnikStudentica(1,3,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_4Reper(1,3,-0.85f+dodatakX,1.7f,0.2f);	
	  ////////////////////////////////////////////////////3. val/////////////////////////////////////////////////
		//lijevo
		faIgr.dodajBr_18Kapitalista(2,25,11f+dodatakX,-0.45f,0.2f);
		//desno
		faIgr.dodajBr_18Kapitalista(2,0,-0.85f+dodatakX,1.7f,0.2f);
		////mjesano
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f+dodatakX,1.7f,0.2f);
		
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f+dodatakX,1.7f,0.5f);
		
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f+dodatakX,1.7f,0.8f);
		
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f+dodatakX,1.7f,0.2f);
		
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f+dodatakX,1.7f,0.8f);
		
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f+dodatakX,1.7f,0.2f);			
		/////lijevo//
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,10,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,3,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,3,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,3,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,3,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,3,-0.85f+dodatakX,1.7f,0.5f);	
		
		faIgr.dodajBr_2Sminkerica(2,0,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f+dodatakX,1.7f,0.8f);		
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f+dodatakX,1.7f,0.2f);	
		
		faIgr.dodajBr_1Radnik(2,5,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_14ProtivnikStudentica(2,1,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.85f+dodatakX,1.7f,0.6f);	
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f+dodatakX,1.7f,0.8f);		
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_6ProtivnikDebeli(2,5,-0.85f+dodatakX,1.7f,0.5f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.85f+dodatakX,1.7f,0.8f);	
		faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,5,-0.85f+dodatakX,1.7f,0.5f);
		faIgr.dodajBr_14ProtivnikStudentica(2,1,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_4Reper(2,1,-0.85f+dodatakX,1.7f,0.2f);	
		faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.85f+dodatakX,1.7f,0.2f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.85f+dodatakX,1.7f,0.2f);	
			
		////////desno	
		faIgr.dodajBr_8ProtivnikKuhar(2,0,11f+dodatakX,-0.85f,0.3f);	
		faIgr.dodajBr_8ProtivnikKuhar(2,0,11f+dodatakX,-0.85f,0.5f);	
		faIgr.dodajBr_3MMA(2,0,11f+dodatakX,-0.85f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,11f+dodatakX,-0.85f,0.2f);			
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,11f+dodatakX,-0.5f,0.2f);		
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,11f+dodatakX,-0.45f,0.2f);		
		faIgr.dodajBr_3MMA(2,1,11f+dodatakX,-0.45f,0.2f);
		
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.2f);
	    
		faIgr.dodajBr_8ProtivnikKuhar(2,12,11f+dodatakX,-0.85f,0.3f);	
		faIgr.dodajBr_8ProtivnikKuhar(2,0,11f+dodatakX,-0.85f,0.5f);	
		faIgr.dodajBr_3MMA(2,0,11f+dodatakX,-0.85f,0.8f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,11f+dodatakX,-0.85f,0.2f);			
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,11f+dodatakX,-0.5f,0.2f);		
		faIgr.dodajBr_10ProtivnikPolicajac(2,5,11f+dodatakX,-0.45f,0.2f);		
		faIgr.dodajBr_3MMA(2,1,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_10ProtivnikPolicajac(2,1,11f+dodatakX,-0.45f,0.2f);		
	
		faIgr.dodajBr_9ProtivnikPas(2,5,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(2,1,11f+dodatakX,-0.45f,0.2f);
		////////////////////4. val///////////////////////////////////////////////////////////////////////
		////desno
		faIgr.dodajBr_18Kapitalista(3,20,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(3,5,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.2f);
	   /////lijevo//
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,20,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,3,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,1,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,3,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,1,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,3,-0.85f+dodatakX,1.7f,0.5f);	
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,3,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,1,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,3,-0.85f+dodatakX,1.7f,0.5f);	
			
			faIgr.dodajBr_2Sminkerica(3,5,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_2Sminkerica(3,1,-0.85f+dodatakX,1.7f,0.8f);		
			faIgr.dodajBr_2Sminkerica(3,1,-0.85f+dodatakX,1.7f,0.2f);	
			faIgr.dodajBr_2Sminkerica(3,1,-0.85f+dodatakX,1.7f,0.8f);		
			faIgr.dodajBr_2Sminkerica(3,1,-0.85f+dodatakX,1.7f,0.2f);	
			
			faIgr.dodajBr_1Radnik(3,3,-0.85f+dodatakX,1.7f,0.2f);	
			faIgr.dodajBr_14ProtivnikStudentica(3,1,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.85f+dodatakX,1.7f,0.6f);	
			faIgr.dodajBr_2Sminkerica(3,1,-0.85f+dodatakX,1.7f,0.8f);		
			faIgr.dodajBr_2Sminkerica(3,1,-0.85f+dodatakX,1.7f,0.2f);	
			faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.85f+dodatakX,1.7f,0.5f);		
			faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.85f+dodatakX,1.7f,0.2f);	
			faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.85f+dodatakX,1.7f,0.8f);	
			faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.85f+dodatakX,1.7f,0.2f);		
			faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.85f+dodatakX,1.7f,0.5f);
			faIgr.dodajBr_14ProtivnikStudentica(3,1,-0.85f+dodatakX,1.7f,0.2f);		
			faIgr.dodajBr_4Reper(3,1,-0.85f+dodatakX,1.7f,0.2f);	
			faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.85f+dodatakX,1.7f,0.2f);		
			faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.85f+dodatakX,1.7f,0.2f);	
			//////desno 
			

			faIgr.dodajBr_8ProtivnikKuhar(3,6,11f+dodatakX,-0.85f,0.3f);	
			faIgr.dodajBr_8ProtivnikKuhar(3,0,11f+dodatakX,-0.85f,0.5f);	
			faIgr.dodajBr_3MMA(3,0,11f+dodatakX,-0.85f,0.8f);
			faIgr.dodajBr_10ProtivnikPolicajac(3,1,11f+dodatakX,-0.85f,0.2f);			
			faIgr.dodajBr_10ProtivnikPolicajac(3,1,11f+dodatakX,-0.5f,0.2f);		
			faIgr.dodajBr_10ProtivnikPolicajac(3,2,11f+dodatakX,-0.45f,0.2f);		
			faIgr.dodajBr_3MMA(3,1,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_10ProtivnikPolicajac(3,1,11f+dodatakX,-0.45f,0.2f);		
			
			faIgr.dodajBr_9ProtivnikPas(3,2,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.5f);
			faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.8f);
			faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.8f);
			faIgr.dodajBr_9ProtivnikPas(3,1,11f+dodatakX,-0.45f,0.2f);
			//////////////////////////////5 val /////////////////////////////////////////////////////////////////////
			//desno
			faIgr.dodajBr_18Kapitalista(4,1,-0.85f+dodatakX,1.7f,0.2f);
			
			faIgr.dodajBr_9ProtivnikPas(4,5,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.5f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.8f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.8f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.2f);
			///lijevo
			faIgr.dodajBr_18Kapitalista(4,20,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(4,5,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.5f);
			faIgr.dodajBr_18Kapitalista(4,3,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.8f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_18Kapitalista(4,3,11f+dodatakX,-0.45f,0.2f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.8f);
			faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.2f);
		
		//////desno 
			     
					faIgr.dodajBr_8ProtivnikKuhar(4,6,11f+dodatakX,-0.85f,0.3f);	
					faIgr.dodajBr_8ProtivnikKuhar(4,0,11f+dodatakX,-0.85f,0.5f);	
					faIgr.dodajBr_3MMA(4,0,11f+dodatakX,-0.85f,0.8f);
					faIgr.dodajBr_10ProtivnikPolicajac(4,1,11f+dodatakX,-0.85f,0.2f);			
					faIgr.dodajBr_10ProtivnikPolicajac(4,1,11f+dodatakX,-0.5f,0.2f);		
					faIgr.dodajBr_10ProtivnikPolicajac(4,2,11f+dodatakX,-0.45f,0.2f);		
					faIgr.dodajBr_3MMA(4,1,11f+dodatakX,-0.45f,0.2f);
					faIgr.dodajBr_10ProtivnikPolicajac(4,1,11f+dodatakX,-0.45f,0.2f);		
					
					faIgr.dodajBr_9ProtivnikPas(4,2,11f+dodatakX,-0.45f,0.2f);
					faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.5f);
					faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.8f);
					faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.2f);
					faIgr.dodajBr_9ProtivnikPas(4,1,11f+dodatakX,-0.45f,0.8f);
				/////lijevo//
					  faIgr.dodajBr_18Kapitalista(4,1,-0.85f+dodatakX,1.7f,0.2f);
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,7,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,3,-0.85f+dodatakX,1.7f,0.5f);	
					
					faIgr.dodajBr_2Sminkerica(4,0,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_2Sminkerica(4,1,-0.85f+dodatakX,1.7f,0.8f);		
					faIgr.dodajBr_2Sminkerica(4,1,-0.85f+dodatakX,1.7f,0.2f);	
					
					faIgr.dodajBr_1Radnik(4,5,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_14ProtivnikStudentica(4,1,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f+dodatakX,1.7f,0.6f);	
					faIgr.dodajBr_2Sminkerica(4,1,-0.85f+dodatakX,1.7f,0.8f);		
					faIgr.dodajBr_2Sminkerica(4,1,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_6ProtivnikDebeli(4,5,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_6ProtivnikDebeli(4,1,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_7ProtivnikCistacica(4,1,-0.85f+dodatakX,1.7f,0.8f);	
					faIgr.dodajBr_6ProtivnikDebeli(4,1,-0.85f+dodatakX,1.7f,0.2f);		
					faIgr.dodajBr_6ProtivnikDebeli(4,5,-0.85f+dodatakX,1.7f,0.5f);
					faIgr.dodajBr_14ProtivnikStudentica(4,1,-0.85f+dodatakX,1.7f,0.2f);		
					faIgr.dodajBr_4Reper(4,1,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_6ProtivnikDebeli(4,1,-0.85f+dodatakX,1.7f,0.2f);		
					faIgr.dodajBr_6ProtivnikDebeli(4,1,-0.85f+dodatakX,1.7f,0.2f);	
					//////////////////////////6 val//////////////////////
					//////desno 		     
				    
				    faIgr.dodajBr_16DebeliSerac(5,25,-0.85f+dodatakX,1.7f,0.2f);
				    
			    	faIgr.dodajBr_18Kapitalista(5,1,-0.85f+dodatakX,1.7f,0.2f);
			    	
			    	faIgr.dodajBr_9ProtivnikPas(5,2,11f+dodatakX,-0.45f,0.2f);
					faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.5f);
					faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.8f);
					faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.2f);
					faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.8f);
					
					
					/////lijevo//
					  faIgr.dodajBr_16DebeliSerac(5,0,11f+dodatakX,-0.45f,0.2f);
					  
					  faIgr.dodajBr_18Kapitalista(5,3,11f+dodatakX,-0.45f,0.2f);
			    	//////desno 		     
					    
					    faIgr.dodajBr_16DebeliSerac(5,1,-0.85f+dodatakX,1.7f,0.2f);
					    
				    	faIgr.dodajBr_18Kapitalista(5,1,-0.85f+dodatakX,1.7f,0.2f);
				    	
				    	faIgr.dodajBr_9ProtivnikPas(5,2,11f+dodatakX,-0.45f,0.2f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.5f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.8f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.2f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.8f);
						////lijevo
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,7,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,3,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,3,-0.85f+dodatakX,1.7f,0.5f);	
					
					faIgr.dodajBr_2Sminkerica(5,0,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_2Sminkerica(5,1,-0.85f+dodatakX,1.7f,0.8f);		
					faIgr.dodajBr_2Sminkerica(5,1,-0.85f+dodatakX,1.7f,0.2f);	
					
					faIgr.dodajBr_1Radnik(5,5,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_14ProtivnikStudentica(5,1,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f+dodatakX,1.7f,0.6f);	
					faIgr.dodajBr_2Sminkerica(5,1,-0.85f+dodatakX,1.7f,0.8f);		
					faIgr.dodajBr_2Sminkerica(5,1,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_6ProtivnikDebeli(5,5,-0.85f+dodatakX,1.7f,0.5f);		
					faIgr.dodajBr_6ProtivnikDebeli(5,1,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_7ProtivnikCistacica(5,1,-0.85f+dodatakX,1.7f,0.8f);	
					faIgr.dodajBr_6ProtivnikDebeli(5,1,-0.85f+dodatakX,1.7f,0.2f);		
					faIgr.dodajBr_6ProtivnikDebeli(5,5,-0.85f+dodatakX,1.7f,0.5f);
					faIgr.dodajBr_14ProtivnikStudentica(5,1,-0.85f+dodatakX,1.7f,0.2f);		
					faIgr.dodajBr_4Reper(5,1,-0.85f+dodatakX,1.7f,0.2f);	
					faIgr.dodajBr_6ProtivnikDebeli(5,1,-0.85f+dodatakX,1.7f,0.2f);		
					faIgr.dodajBr_6ProtivnikDebeli(5,1,-0.85f+dodatakX,1.7f,0.2f);	
				//////desno	     
					faIgr.dodajBr_16DebeliSerac(5,0,-0.85f+dodatakX,1.7f,0.2f);
					  
					  faIgr.dodajBr_18Kapitalista(5,3,-0.85f+dodatakX,1.7f,0.2f);
					  
						
						faIgr.dodajBr_8ProtivnikKuhar(5,6,11f+dodatakX,-0.85f,0.3f);	
						faIgr.dodajBr_8ProtivnikKuhar(5,0,11f+dodatakX,-0.85f,0.5f);	
						faIgr.dodajBr_3MMA(5,0,11f+dodatakX,-0.85f,0.8f);
						faIgr.dodajBr_10ProtivnikPolicajac(5,1,11f+dodatakX,-0.85f,0.2f);			
						faIgr.dodajBr_10ProtivnikPolicajac(5,1,11f+dodatakX,-0.5f,0.5f);		
						faIgr.dodajBr_10ProtivnikPolicajac(5,2,11f+dodatakX,-0.45f,0.2f);		
						faIgr.dodajBr_3MMA(5,1,11f+dodatakX,-0.45f,0.8f);
						faIgr.dodajBr_10ProtivnikPolicajac(5,1,11f+dodatakX,-0.45f,0.2f);		
						

						  faIgr.dodajBr_16DebeliSerac(5,0,-0.85f+dodatakX,1.7f,0.2f);
						  
						  faIgr.dodajBr_18Kapitalista(5,3,-0.85f+dodatakX,1.7f,0.2f);
						  
						faIgr.dodajBr_9ProtivnikPas(5,2,11f+dodatakX,-0.45f,0.2f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.5f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.8f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.2f);
						faIgr.dodajBr_9ProtivnikPas(5,1,11f+dodatakX,-0.45f,0.8f);
					
						
						  
		///////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8borova0suma,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			SpriteHendler dodatak=new SpriteHendler(1);
			dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8borva0suma,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(501, dodatak);
			/*	SpriteHendler dodatak1=new SpriteHendler(1);  
			dodatak1.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8boroa0suma0litica1,opts),1, 1,0);//ispaljenje
		faIgr.dodajSprite(502, dodatak1);
			SpriteHendler dodatak2=new SpriteHendler(1);  
			dodatak2.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8boroa0suma0litica2,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(503, dodatak2);*/
			SpriteHendler dodatak3=new SpriteHendler(1);  
			dodatak3.dodajNoviSprite(BitmapFactory.decodeResource(getResources(),  R.drawable.dodatak8faza8boroa0suma0litica3,opts),1, 1,0);//ispaljenje
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
