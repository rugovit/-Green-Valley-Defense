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

public class FazaBrdovita  extends Activity {

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
			BitmapFactory.decodeResource(getResources(), R.drawable.faza3, opts);
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
	        faIgr=new FazeIgre(4,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		    faIgr.postaviNovcePrijeDolaskaNovogVala(2);
		    faIgr.setDelayIzmVal(5);
		    faIgr.setDelayIzmVal(0,5);
		    faIgr.setDelayIzmVal(1,10);
		    faIgr.setDelayIzmVal(2,15);
		    faIgr.setDelayIzmVal(3,10);
		    faIgr.setDelayIzmVal(4,10);
		    faIgr.setDelayIzmVal(5,15);
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


				//	// TODO Auto-generated method stub
			    ///////		
				getWindow().setFormat(PixelFormat.RGBA_4444);
				opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
				
				 faIgr. dodajPocetneParametreIgre(100,10);
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				 faIgr.setDelayIzmVal(30);
		
		  /////dodavanje èlanova igrice
				  /////dodavanje èlanova igrice
			 	
			 	faIgr.dodajBr200ToranjEmbrio(0,0,0.53f,6.72f);
			    faIgr.dodajBr200ToranjEmbrio(0,0,4.16f,1.33f);
			    faIgr.dodajBr200ToranjEmbrio(0,0,5.93f,1.33f);
				faIgr.dodajBr200ToranjEmbrio(0,0,7.76f,1.33f);
				faIgr.dodajBr200ToranjEmbrio(0,0,9.63f,1.45f);
				faIgr.dodajBr200ToranjEmbrio(0,0,11.75f,4.57f);
			    faIgr.dodajBr200ToranjEmbrio(0,0,11.04f,2.77f);
				faIgr.dodajBr200ToranjEmbrio(0,0,7.05f,3.9f);
				faIgr.dodajBr200ToranjEmbrio(0,0,8.54f,3.9f);
				faIgr.dodajBr200ToranjEmbrio(0,0,8.99f,5.56f);
				faIgr.dodajBr200ToranjEmbrio(0,0,6.68f,5.56f);
				faIgr.dodajBr200ToranjEmbrio(0,0,2.72f,3.9f);
				faIgr.dodajBr200ToranjEmbrio(0,0,4.16f,3.9f);
				faIgr.dodajBr200ToranjEmbrio(0,0,3.6f,5.45f);
			  //  faIgr.dodajBr50Junak(0,4.46f,2.9f, 0.5f,0.5f);
				
				
				faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,0, 0.67f, 2.12f, 6.67f-2.12f, 0, 4.34f, 1.86f,5.32f-4.34f);
			
		         //pocetak
				int brP=-1;
				float xPP=0;
				float yPP=0;
				float sirP=0;
				float visP=0;
				float speed=0.0005f;
				///////
				
				//0.ravni put
					xPP=-0.85f;
					yPP=1.83f;
					sirP=0.85f;
					visP=1.55f;
					faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed, 0, ++brP);
				//1.ravni put
				xPP=0.0f;
				yPP=1.83f;
				sirP=0.85f;
				visP=1.55f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed, 0, ++brP);
			      ////2
				xPP=0.85f;
				yPP=1.83f;
				sirP=0.85f;
				visP=1.55f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed, 0, ++brP);
				//3
						
				xPP=1.69f;
				yPP=1.83f;
				sirP=0.85f;
				visP=1.55f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed, 0, ++brP);
				//4
				xPP=2.54f;
				yPP=1.83f;
				sirP=0.85f;
				visP=1.55f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP, speed, 0, ++brP);
				//5.
				xPP=3.39f;
				yPP=1.83f;
				sirP=0.85f;
				visP=1.55f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP, speed, 0, ++brP);
				//6
				xPP=4.23f;
				yPP=1.83f;
				sirP=0.85f;
				visP=1.55f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,0, ++brP);
				
				////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				///////////////////desni dio////////////////////////
				//7
				xPP=5.08f;
				yPP=1.74f;
				sirP=0.85f;
				visP=0.8f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,0, ++brP);
				//////8
				xPP=5.93f;
				yPP=1.84f;
				sirP=0.85f;
				visP=1.76f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,0, ++brP);
				//9
				xPP=6.77f;
				yPP=1.9f;
				sirP=0.85f;
				visP=1.7f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,0, ++brP);
				//10
				xPP=7.62f;
				yPP=1.94f;
				sirP=0.85f;
				visP=1.66f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed, 0, ++brP);
				//11
				xPP=8.47f;
				yPP=1.94f;
				sirP=0.85f;
				visP=1.66f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,330, ++brP);
				//12
				xPP=9.31f;
				yPP=2.12f;
				sirP=1.09f;
				visP=1.48f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed, 280, ++brP);
				//13
				xPP=9.35f;
				yPP=3.6f;
				sirP=1.41f;
				visP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed, 300, ++brP);
				//14
				xPP=9.38f;
				yPP=4.44f;
				sirP=1.66f;
				visP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed, 300, ++brP);
				//15
				xPP=9.7f;
				yPP=5.29f;
				sirP=1.66f;
				visP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed, 290, ++brP);
				//16
				xPP=10.9f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.41f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				////17
				xPP=10.05f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,220, ++brP);
			    ////18
				xPP=9.21f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////19
				xPP=8.36f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////20
				xPP=7.51f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////21
				xPP=6.67f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////22
				xPP=5.82f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////23
				xPP=4.97f;
				yPP=6.14f;
				sirP=0.85f;
				visP=1.27f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////24
				xPP=4.13f;
				yPP=6.14f;
				sirP=0.85f;
			    visP=0.71f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
			    ////25
				xPP=4.13f;
				yPP=6.84f;
				sirP=0.85f;
			    visP=0.59f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				
				 ////26
				xPP=3.39f;
				yPP=6.14f;
				sirP=0.74f;
				visP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////27
				xPP=2.65f;
				yPP=6f;
				sirP=0.74f;
				visP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,180, ++brP);
				 ////28
				xPP=1.23f;
				yPP=6f;
				sirP=1.41f;
			    visP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,230, ++brP);
				 ////29
				xPP=1.09f;
				yPP=6.84f;
				sirP=1.55f;
			    visP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,250, ++brP);
				 ////30
				xPP=0.99f;
				yPP=7.69f;
				sirP=1.55f;
			    visP=1.45f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,270, ++brP);
				 //////////////////////////////////////////KRAJ//////////////////////////////////////////////////////////
				xPP=0.95f;
				yPP=9.14f;
				sirP=1.55f;
			    visP=1.02f;
				faIgr.dodajBr204Kraj(0,0, xPP, yPP, sirP, visP,0,speed,++brP);
//				////////////////////////////////////
				/////////////////lijevi dio
				 ////7
				xPP=5.08f;
				yPP=2.54f;
				sirP=0.85f;
			    visP=1.06f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,270, 7);
				 ////8
				xPP=4.9f;
				yPP=3.6f;
				sirP=1.45f;
			    visP=0.85f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,270, 8);
				 ////9
				xPP=4.51f;
				yPP=4.44f;
				sirP=1.73f;
			    visP=0.85f;
				faIgr.dodajBr203RavniPutKut(0,0, xPP, yPP, sirP, visP,speed,270, 9);				
				 ////10
				xPP=4.23f;
				yPP=5.29f;
				sirP=1.9f;
			    visP=0.85f;
				faIgr.dodajBr205OkukaKut(0,0, xPP, yPP, sirP, visP,speed,230, 10);
			
				/////////////
				//
				//////
				
				/////
				//faIgr.dodajBr_3Protivnik(0,1,0f,-0.33f,0.8f);1
		    	faIgr.dodajBr_4Reper(0,1,0f,2f,0.2f);
		    	//1. Val
				faIgr.dodajBr_5ProtivnikPocetni(0,1, -0.8f,2f,0.9f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, -0.8f,2f,0.8f);
				faIgr.dodajBr_4Reper(0,10,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.8f,2f,0.7f);		
				faIgr.dodajBr_4Reper(0,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.8f,2f,0.7f);
				
				faIgr.dodajBr_4Reper(0,15,-0.8f,2f,0.8f);
				faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.8f,2f,0.7f);	
				faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_4Reper(0,1,-0.8f,2f,0.7f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.8f,2f,0.7f);
			
				
				//2. Val

				faIgr.dodajBr_5ProtivnikPocetni(1,0,-0.8f,2f,0.7f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.7f);
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.9f);	
				
				
				
				faIgr.dodajBr_5ProtivnikPocetni(1,15,-0.8f,2f,0.5f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				
				
				faIgr.dodajBr_5ProtivnikPocetni(1,15,-0.8f,2f,0.2f);		
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.9f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.9f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.2f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,15,-0.8f,2f,0.2f);		
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.8f,2f,0.2f);	
				faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1,0f,2f,0.2f);						
				faIgr.dodajBr_6ProtivnikDebeli(1,1,-0.8f,2f,0.2f);
			
				
				//3. val
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.7f);
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.7f);				
				faIgr.dodajBr_8ProtivnikKuhar(2,1,-0.8f,2f,0.2f);
				
				
				faIgr.dodajBr_5ProtivnikPocetni(2,15,-0.8f,2f,0.2f);		
				faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.8f,2f,0.8f);	
				faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.8f,2f,0.2f);
				
				faIgr.dodajBr_5ProtivnikPocetni(2,15,-0.8f,2f,0.2f);		
				faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.8f,2f,0.2f);		
				
				faIgr.dodajBr_1Radnik(2,10,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.7f);
				faIgr.dodajBr_1Radnik(2,1,-0.8f,2f,0.7f);
			
				faIgr.dodajBr_6ProtivnikDebeli(2,10,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.8f,2f,0.2f);	
				faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(2,1,-0.8f,2f,0.2f);
				//4 val
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.7f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.7f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.7f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				
				faIgr.dodajBr_8ProtivnikKuhar(3,10,-0.8f,2f,0.2f);				
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);	
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.8f,2f,0.2f);
				
		        faIgr.dodajBr_8ProtivnikKuhar(3,12,-0.7f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.7f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.7f,2f,0.2f);	
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.7f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.7f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.7f,2f,0.8f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.7f,2f,0.2f);
				
				faIgr.dodajBr_8ProtivnikKuhar(3,12,-0.8f,2f,0.2f);
		        faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.8f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.7f,2f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.7f,2f,0.2f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.7f,2f,0.2f);
				faIgr.dodajBr_8ProtivnikKuhar(3,1,-0.7f,2f,0.2f); 
				faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.7f,2f,0.2f);
				
				faIgr.dodajBr_8ProtivnikKuhar(3,8,-0.8f,2f,0.9f);
		        faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.8f,2f,0.9f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.8f,2f,0.9f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.9f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.7f,2f,0.9f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,09f);		
				faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.7f,2f,0.9f);
				faIgr.dodajBr_6ProtivnikDebeli(3,1,-0.7f,2f,0.9f);
				faIgr.dodajBr_8ProtivnikKuhar(3,1,-0.7f,2f,0.9f); 
				faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.7f,2f,0.9f);
				faIgr.dodajBr_8ProtivnikKuhar(3,3,-0.8f,2f,0.2f);
		        faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.8f,2f,0.2f);
				
				faIgr.dodajBr_4Reper(3,4,-0.8f,2f,0.8f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.82f);
				faIgr.dodajBr_4Reper(3,1,-0.8f,2f,0.9f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_4Reper(3,1, -0.8f,2f,0.9f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_4Reper(3,1,-0.8f,2f,0.9f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.8f);
				faIgr.dodajBr_4Reper(3,1,-0.8f,2f,0.9f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.8f);
				
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_4Reper(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_4Reper(3,1, -0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_4Reper(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_4Reper(3,1,-0.8f,2f,0.2f);
				faIgr.dodajBr_1Radnik(3,1,-0.8f,2f,0.2f);
				
				 
				  ////////////poseban metoda za lod slika
						 loadSlika();
														 Log.d("game onCreate", "memorije nativ poslije lodiranje=" + Debug.getNativeHeapAllocatedSize()/1024);
												    	 Log.d("game onCreate", "memorije GlobalAllocSize poslije lodiranje=" + Debug.getGlobalAllocSize()/1024);
												    	 Log.d("game onCreate", "memorije ThreadAllocSize poslije lodiranje=" + Debug.getThreadAllocSize()/1024);												
			   
			    uiMan.daliDaProvjeravamPovlacenje(true);
			}
        	
        };  
	
		gLog.pokreniGlavnuPetlju(); // izbacio sam opciju da se GameLogic sam pokreæe jer moglo biti grešaka pri pristupu listi  
		
		setContentView(uiMan); 
		  /*uiMan2.stop();
		uiMan2=null;
		sprUvoda.reciklirajSveBitmapove();
		postotakLodiranjaFaze=0;
              }
		activity=null		
	        	
	      };
	        uiMan2.daliDaProvjeravamPovlacenje(false);
	        setContentView(uiMan2); */
	
		
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
    public void dodajPozadinu(){
	    opts.inPreferredConfig = Bitmap.Config.RGB_565;
	    pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.faza3,opts);
		
			   SpriteHendler dodatak=new SpriteHendler(1);
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak0na0fazu3,opts),0, 0,0);//ispaljenje
			
			 faIgr.dodajSprite(501, dodatak);
		uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
		opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
	} 
	@Override
	protected void onPause(){
		super.onPause();
		super.onPause();
		if(gLog!=null){
		
			gLog.setSystemPauze(true);
		}
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
	if(gLog!=null){
		this.gLog.ugasiGlavnuPetlju();
		gLog=null;
	}
    if(uiMan!=null){			uiMan.reciklirajPozadinu();
			uiMan.reciklirajTeksturu();
			this.faIgr.reciklirajSveSpriteove();
		 uiMan.makniSveObjekteMultyThread();
		 uiMan=null;}
		 
		stvoriMapu();
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
		       		cur3.close();
		               cur2.close();
		             cur1.moveToNext();// za pomicanje u bazi podataka
	           
				}
				cur3.close();
				cur1.close();
				  bazaPodataka.close();
	 } 
}


