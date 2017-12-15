
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

public class FazaMost extends Activity {

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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8most, opts);
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
	        faIgr=new FazeIgre(5,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		   
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
			
			        this.faIgr.lodirajResurse(activity, soundPool,opts,gLog,uiMan);
			}
			
			@Override
			public void loadMetoda() {


				// TODO Auto-generated method stub
			    ///////		
				getWindow().setFormat(PixelFormat.RGBA_4444);
				opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
				
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				    faIgr.setDelayIzmVal(30);
				    faIgr.setDelayIzmVal(0,30);
				    faIgr.setDelayIzmVal(1,30);
				    faIgr.setDelayIzmVal(2,30);
				    faIgr.setDelayIzmVal(3,30);
				    faIgr.setDelayIzmVal(4,40);
				
				    //faIgr=new FazeIgre(200,getResources(),60,60);
				    faIgr. dodajPocetneParametreIgre(100,10);
				 
				 uiMan.nacrtajIzvanThreadno();
				 float omjer=2.983f;
				 float polX=4.1f;
				 float polY=-0.2f;
				 float sir=2f;
				 float vis=sir/omjer;
				 
				 omjer=1.364f;
				 polX=9.38f;
				 polY=1.27f;
				 sir=3.22f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				/* omjer=1.364f;
				 polX=10.55f;
				 polY=0.21f;
				 sir=3.07f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0);*/
			     
				 omjer=1.085f;
				 polX=6.84f;
				 polY=2.19f;
				 sir=3.56f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 503,0,2,polX, polY, sir,vis,0, 0f, 0,0); polX=0.3f;
				 

				 polX=-1.45f;
				 polY=6.49f;
				 sir=3.56f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 503,0,2,polX, polY, sir,vis,0, 0f, 0,0); polX=0.3f;
				 
				 omjer=0.4646f;
				 polX=11.92f;
				 polY=4.8f;
				 sir=0.46f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 504,0,1,polX, polY, sir,vis,0, 0, 0,0);
				 
				 
				 omjer=0.4646f;
				 polX=11.61f;
				 polY=6.17f;
				 sir=0.46f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 504,3,1,polX, polY, sir,vis,0,0,0,0);
				 
				 
				 omjer=0.4646f;
				 polX=12.38f;
				 polY=3.88f;
				 sir=0.46f;
				 vis=sir/omjer;
				 faIgr.dodaj501DodatakNaMapu(0,0, 504,5,1,polX, polY, sir,vis,0,0,0,0);
				 
				 
				 //rijeka samo logika
				
				 polX=10.97f;
				 polY=2.26f;
				 sir=1.52f;
				 vis=6.24f;
				 faIgr.dodaj501DodatakNaMapu(0,0,0,0,1,0, 0, 0,0,polX, polY, sir,vis);
	
		  /////dodavanje èlanova igrice
	 	///gornji
		faIgr.dodajBr200ToranjEmbrio(0,0,4.16f,1.43f);
		faIgr.dodajBr200ToranjEmbrio(0,0,1.55f,1.61f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.27f,7.05f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.91f,6.01f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.53f,3.26f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.53f,4.99f);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.83f,6.7f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.95f,6.7f);//
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.72f,6.9f);//
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.55f,7.16f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.42f,3.55f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.95f,3.65f);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.47f,4.74f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.35f,4.6f);
	 	
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
				////////////////////////////////////Ravni put 1//////////////////////////////////////////////////////////////
			    float visina=0.85f;
			    float sirina=1.23f;
			    float sirina2=1.7f;
				//0
				sirP=1f;
				visP=0.81f;
				xPP=12.31f;
				yPP=0.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				//1
				sirP=0.56f;
				visP=0.67f;
				xPP=11.32f;
				yPP=1.41f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////-1 poseebna
				sirP=0.6f;
				visP=0.18f;
				xPP=11.89f;
				yPP=1.06f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 2500,-1);
				 ////-1 poseebna
				sirP=0.6f;
				visP=0.74f;
				xPP=11.89f;
				yPP=1.23f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 180,-1);
			    ////2
				sirP=0.53f;
				visP=0.56f;
				xPP=10.8f;
				yPP=1.52f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.53f;
				visP=0.25f;
				xPP=10.8f;
				yPP=1.27f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 250,-1);
				 ////0 poseebna
				sirP=0.56f;
				visP=0.25f;
				xPP=11.32f;
				yPP=1.16f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 250,-1);
				////3
				sirP=0.85f;
				visP=0.92f;
				xPP=9.95f;
				yPP=1.41f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.85f;
				visP=0.14f;
				xPP=9.1f;
				yPP=1.45f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////4
				sirP=0.85f;
				visP=1.16f;
				xPP=9.1f;
				yPP=1.59f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
			    ////0 poseebna
				sirP=0.99f;
				visP=0.14f;
				xPP=8.11f;
				yPP=1.52f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////5
				sirP=0.85f;
				visP=1.23f;
				xPP=8.26f;
				yPP=1.66f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.99f;
				visP=0.14f;
				xPP=7.27f;
				yPP=1.66f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////6
				sirP=0.85f;
				visP=1.23f;
				xPP=7.41f;
				yPP=1.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				////7
				sirP=0.85f;
				visP=1.23f;
				xPP=6.56f;
				yPP=1.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.71f;
				visP=0.14f;
				xPP=6.56f;
				yPP=1.66f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////8
				sirP=0.85f;
				visP=1.23f;
				xPP=5.72f;
				yPP=1.87f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.71f;
				visP=0.14f;
				xPP=5.86f;
				yPP=1.73f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////9
				sirP=0.85f;
				visP=1.23f;
				xPP=4.87f;
				yPP=1.87f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.85f;
				visP=0.14f;
				xPP=4.02f;
				yPP=1.83f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////10
				sirP=0.85f;
				visP=1.23f;
				xPP=4.02f;
				yPP=1.98f;			
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				 ////0 poseebna
				sirP=0.85f;
				visP=0.14f;
				xPP=3.18f;
				yPP=1.94f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 230,-1);
				////11
				sirP=0.85f;
				visP=1.23f;
				xPP=3.18f;
				yPP=2.08f;		
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brPLijevi);
				////
				 ////0 poseebna
				sirP=1.55f;
				visP=0.28f;
				xPP=1.62f;
				yPP=1.94f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,-1);
				//////////////////////////////////Okuka1////////////////////////////////////////////////////
				///12
				sirP=0.85f;
				visP=1.23f;
				xPP=2.33f;
				yPP=2.22f;				
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 195,++brPLijevi);
				////13
				sirP=0.85f;
				visP=1.23f;
				xPP=1.48f;
				yPP=2.22f;					
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 260,++brPLijevi);
				 ////0 poseebna
				sirP=0.21f;
				visP=4.16f;
				xPP=1.27f;
				yPP=2.22f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,-1);
				////14
				visP=0.85f;
				sirP=1.23f;
				xPP=1.48f;
				yPP=3.46f;									
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 260,++brPLijevi);
				////15
				visP=0.85f;
				sirP=1.23f;
				xPP=1.48f;
				yPP=4.3f;						
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 260,++brPLijevi);
				////16
				sirP=0.85f;
				visP=1.23f;
				xPP=1.48f;
				yPP=5.15f;	
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 340,++brPLijevi);
				////
				//////////////////////////////////Ravni put 2////////////////////////////////////////////
				//17
				sirP=0.85f;
				visP=1.23f;
				xPP=2.33f;
				yPP=5.15f;									
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,350,++brPLijevi);
				////18
				sirP=0.85f;
				visP=1.23f;
				xPP=3.18f;
				yPP=5.15f;	
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				////19
				sirP=0.85f;
				visP=1.23f;
				xPP=4.02f;
				yPP=5.15f;	
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				////20
				sirP=0.85f;
				visP=1.23f;
				xPP=4.87f;
				yPP=5.33f;	
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				////
			    //////////////////////////////////////Okuka 2///////////////////////////////////////////
				//21
				sirP=0.85f;
				visP=1.23f;
				xPP=5.72f;
				yPP=5.57f;		
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,++brPLijevi);
				 ////0 poseebna
				sirP=0.99f;
				visP=0.25f;
				xPP=5.72f;
				yPP=5.33f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 310,-1);
				////22
				sirP=0.85f;
				visP=1.23f;
				xPP=6.56f;
				yPP=5.57f;						
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,++brPLijevi);
				////23
				sirP=0.85f;
				visP=1.23f;
				xPP=7.41f;
				yPP=5.75f;	
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				////24
				visP=0.85f;
				sirP=1.23f;
				xPP=8.26f;
				yPP=5.75f;	
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,290,++brPLijevi);
				////25
				visP=0.85f;
				sirP=1.23f;
				xPP=8.26f;
				yPP=6.6f;	
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,270,++brPLijevi);
				////
			    ////26
			    sirP=1.23f;
				visP=1.38f;
				xPP=8.4f;
				yPP=7.44f;	
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,270,++brPLijevi);
				 sirP=1.23f;
					visP=0.46f;
					xPP=8.4f;
					yPP=8.82f;	
				faIgr.dodajBr204Kraj(0,0,xPP, yPP, sirP, visP, 0, speed,++brPLijevi);
				//////////////////////////////////////////////////////////////////////////////////////////////////
		/*faIgr.dodajBr_3Protivnik(0,1, 13.2f,1.6f,0.4f);
		faIgr.dodajBr_1Protivnik(0,1, 13.2f,1.6f,0.4f);
		faIgr.dodajBr_2Protivnik(0,1, 13.2f,1.6f,0.4f);
		faIgr.dodajBr_4Reper(0,1,13.2f,1.6f,0.4f);
		
		faIgr.dodajBr_3Protivnik(0,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_1Protivnik(0,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_2Protivnik(0,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_4Reper(0,1,13.2f,1.6f,0.8f);
		*/
		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,6, 13.2f,1.6f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(0,4,13.2f,1.6f,0.6f);
		faIgr.dodajBr_5ProtivnikPocetni(0,3,13.2f,1.6f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.6f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,2,13.2f,1.6f,0.2f);
		
		faIgr.dodajBr_7ProtivnikCistacica(0,10,13.2f,1.6f,0.8f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f,1.6f,0.8f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f,1.6f,0.5f);
		
		faIgr.dodajBr_5ProtivnikPocetni(0,7,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.2f);		
		faIgr.dodajBr_5ProtivnikPocetni(0,2,13.2f,1.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f,1.6f,0.6f);
		
		faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f,1.6f,0.7f);
	
		///val br 2
	
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		
		faIgr.dodajBr_7ProtivnikCistacica(1,10,13.2f,1.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(1,5,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_5ProtivnikPocetni(1,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(1,0,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(1,0,13.2f,1.6f,0.4f);
		
		faIgr.dodajBr_9ProtivnikPas(1,8,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_5ProtivnikPocetni(1,5,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(1,0,13.2f,1.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,0,13.2f,1.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.7f);
	

		faIgr.dodajBr_5ProtivnikPocetni(1,5,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_5ProtivnikPocetni(1,5,13.2f,1.6f,0.7f);		
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.5f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,5,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,13.2f,1.6f,0.5f);		
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f,1.6f,0.4f);
		///3. val
		faIgr.dodajBr_9ProtivnikPas(2,0,13.2f,1.6f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(2,3,13.2f,1.6f,0.5f);		
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.4f);
		
		faIgr.dodajBr_5ProtivnikPocetni(2,7,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.6f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(2,3,13.2f,1.6f,0.5f);		
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.6f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.8f);
		
		faIgr.dodajBr_5ProtivnikPocetni(2,7,13.2f,1.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1,13.2f,1.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(2,5,13.2f,1.6f,0.2f);
		
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,5,13.2f,1.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,3,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.6f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1,13.2f,1.6f,0.7f);
		faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f,1.6f,0.4f);
		
		faIgr.dodajBr_6ProtivnikDebeli(2,15,13.2f,1.6f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(2,3,13.2f,1.6f,0.5f);		
		faIgr.dodajBr_9ProtivnikPas(2,1,13.2f,1.6f,0.8f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0,13.2f,1.6f,0.2f);
		faIgr.dodajBr_5ProtivnikPocetni(2,0,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,0,13.2f,1.6f,0.8f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0, 13.2f,1.6f,0.2f);
		
		faIgr.dodajBr_6ProtivnikDebeli(2,0,13.2f,1.6f,0.4f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0,13.2f,1.6f,0.2f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0,13.2f,1.6f,0.8f);
		faIgr.dodajBr_5ProtivnikPocetni(2,0,13.2f,1.6f,0.4f);		
		faIgr.dodajBr_6ProtivnikDebeli(2,0,13.2f,1.6f,0.4f);
		faIgr.dodajBr_7ProtivnikCistacica(2,0,13.2f,1.6f,0.2f);
		faIgr.dodajBr_6ProtivnikDebeli(2,0,13.2f,1.6f,0.8f);
		//4 val
		   faIgr.dodajBr_9ProtivnikPas(3,10,13.2f,1.6f,0.5f);		
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.6f);
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.4f);
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.5f);
		   
		   faIgr.dodajBr_9ProtivnikPas(3,5,13.2f,1.6f,0.4f);		
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.5f);
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.6f);
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.5f); 
		   
		   faIgr.dodajBr_9ProtivnikPas(3,5,13.2f,1.6f,0.4f);		
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.5f);
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.6f);
		   faIgr.dodajBr_9ProtivnikPas(3,1,13.2f,1.6f,0.5f); 
		   
				faIgr.dodajBr_8ProtivnikKuhar(3,1, 13.2f,1.6f,0.4f);
				
				faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f,1.6f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f,1.6f,0.4f);	
				faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f,1.6f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,13.2f,1.6f,0.4f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f,1.6f,0.2f);
				
		        faIgr.dodajBr_8ProtivnikKuhar(3,5,13.2f,1.6f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f,1.6f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f,1.6f,0.8f);	
				
				faIgr.dodajBr_5ProtivnikPocetni(3,1,13.2f,1.6f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f,1.6f,0.8f);
				
				faIgr.dodajBr_8ProtivnikKuhar(3,7,13.2f,1.6f,0.8f);
		        faIgr.dodajBr_8ProtivnikKuhar(3,2,13.2f,1.6f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f,1.6f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f,1.6f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,13.2f,1.6f,0.4f);
			
				faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f,1.6f,0.2f);
				faIgr.dodajBr_8ProtivnikKuhar(3,1,13.2f,1.6f,0.8f); 
				faIgr.dodajBr_3MMA(3,2,13.2f,1.6f,0.2f);
				faIgr.dodajBr_3MMA(3,1,13.2f,1.6f,0.8f);
		//5
			    faIgr.dodajBr_9ProtivnikPas(4,5,13.2f,1.6f,0.5f);		
			    faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f); 
				   
				faIgr.dodajBr_9ProtivnikPas(4,5,13.2f,1.6f,0.4f);		
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f); 
				
				faIgr.dodajBr_8ProtivnikKuhar(4,7,13.2f,1.6f,0.2f);
		        faIgr.dodajBr_8ProtivnikKuhar(4,2,13.2f,1.6f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f,1.6f,0.5f);		
				faIgr.dodajBr_5ProtivnikPocetni(4,1,13.2f,1.6f,0.8f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f); 
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.2f);
				faIgr.dodajBr_8ProtivnikKuhar(4,1,13.2f,1.6f,0.8f); 

			    faIgr.dodajBr_9ProtivnikPas(4,5,13.2f,1.6f,0.5f);		
			    faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f); 
				   
				faIgr.dodajBr_9ProtivnikPas(4,5,13.2f,1.6f,0.5f);		
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f); 
				
				faIgr.dodajBr_3MMA(4,2,13.2f,1.6f,0.2f);
				faIgr.dodajBr_3MMA(4,1,13.2f,1.6f,0.8f);
				
				faIgr.dodajBr_3MMA(4,2,13.2f,1.6f,0.3f);
				faIgr.dodajBr_3MMA(4,1,13.2f,1.6f,0.7f);				
				faIgr.dodajBr_8ProtivnikKuhar(4,1,13.2f,1.6f,0.2f);
		        faIgr.dodajBr_8ProtivnikKuhar(4,2,13.2f,1.6f,0.5f);
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f,1.6f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f); 
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_8ProtivnikKuhar(4,1,13.2f,1.6f,0.8f); 
				
				faIgr.dodajBr_3MMA(4,25,13.2f,1.6f,0.2f); 
				
				faIgr.dodajBr_3MMA(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_3MMA(4,1,13.2f,1.6f,0.8f);
				faIgr.dodajBr_3MMA(4,2,13.2f,1.6f,0.6f);
				
                
				faIgr.dodajBr_8ProtivnikKuhar(4,1,13.2f,1.6f,0.2f);
		        faIgr.dodajBr_8ProtivnikKuhar(4,2,13.2f,1.6f,0.5f);
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f,1.6f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_8ProtivnikKuhar(4,1,13.2f,1.6f,0.8f); 
				faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f,1.6f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_8ProtivnikKuhar(4,1,13.2f,1.6f,0.8f); 
				
				faIgr.dodajBr_9ProtivnikPas(4,5,13.2f,1.6f,0.4f);		
			    faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);	
			    faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f); 
		
				
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f);		
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.4f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.6f);
				faIgr.dodajBr_9ProtivnikPas(4,1,13.2f,1.6f,0.5f); 
				
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8most,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			   SpriteHendler dodatak=new SpriteHendler(1);
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8most8most0blizi,opts),1, 1,0);
			
			 faIgr.dodajSprite(501, dodatak);
			 
			 /*dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8most8most0dalji,opts),1, 1,0);
			
			 faIgr.dodajSprite(502, dodatak);*/
			 
			 dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8most8sumica,opts),1, 1,0);
			
			 faIgr.dodajSprite(503, dodatak);
				
			 dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.staza8most8animacija0vode0rub,opts),6, 1,4);
			
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
