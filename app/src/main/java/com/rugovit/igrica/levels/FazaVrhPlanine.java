
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

public class FazaVrhPlanine extends Activity {

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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8vrh0planine, opts);
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
	        faIgr=new FazeIgre(7,400,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		  
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
			
				 faIgr. dodajPocetneParametreIgre(100,10);
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				 faIgr.setDelayIzmVal(30);

				 
				 //dodatak planina gore	
				 //log
				 float polXLog=0f;
				 float polYLog=3.21f;
				 float sirLog=1.23f;
				 float visLog=1.09f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0,0,0,polXLog, polYLog,sirLog,visLog);
                 //log
				 polXLog=1.23f;
				 polYLog=3.1f;
				 sirLog=0.46f;
				 visLog=1.16f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 //log
				 polXLog=1.69f;
				 polYLog=3.32f;
				 sirLog=0.56f;
				 visLog=1.2f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 //log 3
				 polXLog=2.26f;
				 polYLog=3.18f;
				 sirLog=0.6f;
				 visLog=1.38f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);

				//log 4
				 polXLog=2.86f;
				 polYLog=3.03f;
				 sirLog=0.39f;
				 visLog=1.27f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 5
				 polXLog=3.25f;
				 polYLog=2.82f;
				 sirLog=0.67f;
				 visLog=1.34f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 6
				 polXLog=3.92f;
				 polYLog=3.1f;
				 sirLog=0.14f;
				 visLog=1.38f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 7
				 polXLog=4.06f;
				 polYLog=3.42f;
				 sirLog=0.18f;
				 visLog=1.16f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);

				//log 8
				 polXLog=4.23f;
				 polYLog=3.85f;
				 sirLog=0.25f;
				 visLog=0.85f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 9
				 polXLog=4.48f;
				 polYLog=4.02f;
				 sirLog=0.18f;
				 visLog=0.85f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 10
				 polXLog=4.66f;
				 polYLog=4.37f;
				 sirLog=0.28f;
				 visLog=0.49f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 11
				 polXLog=4.94f;
				 polYLog=4.62f;
				 sirLog=0.21f;
				 visLog=0.39f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 12
				 polXLog=5.15f;
				 polYLog=4.8f;
				 sirLog=0.21f;
				 visLog=0.25f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				//log 13
				 polXLog=5.36f;
				 polYLog=4.9f;
				 sirLog=0.18f;
				 visLog=0.18f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 14
				 polXLog=6.1f;
				 polYLog=2.36f;
				 sirLog=0.64f;
				 visLog=1.09f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 15
				 polXLog=6.24f;
				 polYLog=3.46f;
				 sirLog=0.07f;
				 visLog=0.42f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 16
				 polXLog=6.28f;
				 polYLog=3.88f;
				 sirLog=0.18f;
				 visLog=0.14f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 17
				 polXLog=6.42f;
				 polYLog=4.02f;
				 sirLog=0.14f;
				 visLog=0.11f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 18
				 polXLog=6.49f;
				 polYLog=4.13f;
				 sirLog=0.14f;
				 visLog=0.11f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 19
				 polXLog=6.53f;
				 polYLog=4.23f;
				 sirLog=0.14f;
				 visLog=0.07f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 20
				 polXLog=6.6f;
				 polYLog=4.3f;
				 sirLog=0.14f;
				 visLog=0.07f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 21
				 polXLog=6.67f;
				 polYLog=4.37f;
				 sirLog=0.14f;
				 visLog=0.07f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 22
				 polXLog=6.77f;
				 polYLog=4.45f;
				 sirLog=0.18f;
				 visLog=0.07f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 23
				 polXLog=6.88f;
				 polYLog=4.52f;
				 sirLog=0.11f;
				 visLog=0.07f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 24
				 polXLog=6.74f;
				 polYLog=2.54f;
				 sirLog=0.25f;
				 visLog=0.95f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 25
				 polXLog=6.99f;
				 polYLog=2.79f;
				 sirLog=0.39f;
				 visLog=0.78f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 26
				 polXLog=7.37f;
				 polYLog=2.89f;
				 sirLog=0.39f;
				 visLog=0.71f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 27
				 polXLog=7.76f;
				 polYLog=2.86f;
				 sirLog=0.32f;
				 visLog=0.78f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 28
				 polXLog=8.08f;
				 polYLog=2.82f;
				 sirLog=0.39f;
				 visLog=0.71f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 29
				 polXLog=8.47f;
				 polYLog=2.93f;
				 sirLog=0.32f;
				 visLog=0.56f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 30
				 polXLog=8.78f;
				 polYLog=2.96f;
				 sirLog=0.32f;
				 visLog=0.67f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 31
				 polXLog=9.1f;
				 polYLog=3.0f;
				 sirLog=0.78f;
				 visLog=0.67f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 32
				 polXLog=9.88f;
				 polYLog=2.89f;
				 sirLog=0.39f;
				 visLog=0.74f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 33
				 polXLog=10.27f;
				 polYLog=2.89f;
				 sirLog=0.95f;
				 visLog=0.81f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 34
				 polXLog=11.22f;
				 polYLog=2.86f;
				 sirLog=0.53f;
				 visLog=0.6f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 35
				 polXLog=11.75f;
				 polYLog=2.65f;
				 sirLog=0.74f;
				 visLog=0.64f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 36
				 polXLog=11.47f;
				 polYLog=2.65f;
				 sirLog=0.28f;
				 visLog=0.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 37
				 polXLog=0f;
				 polYLog=7.41f;
				 sirLog=5.01f;
				 visLog=1.09f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 38
				 polXLog=5.01f;
				 polYLog=7.34f;
				 sirLog=5.04f;
				 visLog=1.16f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 39
				 polXLog=10.05f;
				 polYLog=7.51f;
				 sirLog=1.06f;
				 visLog=0.99f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 40
				 
				 polXLog=11.15f;
				 polYLog=7.37f;
				 sirLog=1.41f;
				 visLog=1.37f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 41
				 polXLog=0f;
				 polYLog=0f;
				 sirLog=1.2f;
				 visLog=0.67f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 42
				 polXLog=1.2f;
				 polYLog=0f;
				 sirLog=0.42f;
				 visLog=0.81f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 43
				 polXLog=1.62f;
				 polYLog=0f;
				 sirLog=0.6f;
				 visLog=0.71f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 44
				 polXLog=2.22f;
				 polYLog=0f;
				 sirLog=0.32f;
				 visLog=0.88f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 45
				 polXLog=2.54f;
				 polYLog=0f;
				 sirLog=0.88f;
				 visLog=0.81f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 46
				 polXLog=3.42f;
				 polYLog=0f;
				 sirLog=1.27f;
				 visLog=0.67f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 47
				 polXLog=4.69f;
				 polYLog=0f;
				 sirLog=0.25f;
				 visLog=0.6f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 48
				 polXLog=4.94f;
				 polYLog=0f;
				 sirLog=0.56f;
				 visLog=0.46f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 49
				 polXLog=5.5f;
				 polYLog=0f;
				 sirLog=0.49f;
				 visLog=0.53f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 50
				 polXLog=6.0f;
				 polYLog=0f;
				 sirLog=1.27f;
				 visLog=0.35f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 51
				 polXLog=7.27f;
				 polYLog=0f;
				 sirLog=0.99f;
				 visLog=0.28f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
				//log 52
				 polXLog=8.26f;
				 polYLog=0f;
				 sirLog=1.66f;
				 visLog=0.14f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 0,0,1,0, 0, 0,0,polXLog, polYLog,sirLog,visLog);
				 ///
		
		  /////dodavanje èlanova igrice
				float dodatak=-0.15f; 
	 	///gornji
				
	 	//faIgr.dodajBr200ToranjEmbrio(0,0,11.85f,1.94f);//
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.63f,2.2f);//*
		faIgr.dodajBr200ToranjEmbrio(0,0,8.47f,0.58f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.69f,2.39f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.41f,1.5f);//*
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.68f,2.73f);//*
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.02f,2.66f);//*
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.88f,4.79f);
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.34f,5.08f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.99f,5.13f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.67f,6.82f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.22f,6.95f);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.94f,6.96f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.74f,5.61f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.01f,4.39f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.78f,4.03f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.47f,4.29f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.85f,5.52f);
		faIgr.dodajBr200ToranjEmbrio(0,0,9.78f,5.56f);
		faIgr.dodajBr200ToranjEmbrio(0,0,10.06f,6.72f);
	
	 
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
			    xPP=10.76f;
				yPP=-0.78f;
			    
			    sirP=0.95f;
			    visP=0.78f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
			    /////1
			    xPP=10.75f;
				yPP=0f;
			    
			    sirP=0.94f;
			    visP=0.78f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////2
			    xPP=10.75f;
				yPP=0.78f;
			    
			    sirP=0.94f;
			    visP=0.47f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,255,++brPLijevi);
				///
				 /////3
			    xPP=10.75f;
				yPP=1.24f;
			    
			    sirP=0.94f;
			    visP=0.47f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////4
			    xPP=9.98f;
				yPP=0.78f;
			    
			    sirP=0.77f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////5
			    xPP=9.2f;
				yPP=0.86f;
			    
			    sirP=0.77f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////6
			    xPP=8.43f;
				yPP=0.94f;
			    
			    sirP=0.78f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				///
				 /////7
			    xPP=7.66f;
				yPP=1.01f;
			    
			    sirP=0.77f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////8
			    xPP=6.88f;
				yPP=0.94f;
			    
			    sirP=0.77f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,170,++brPLijevi);
				///
				 /////9
			    xPP=5.99f;
				yPP=0.94f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////10
			    xPP=5.45f;
				yPP=0.94f;
			    
			    sirP=0.54f;
			    visP=0.94f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,220,++brPLijevi);
				///
				 /////11
			    xPP=4.91f;
				yPP=0.94f;
			    
			    sirP=0.54f;
			    visP=0.94f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////12
			    xPP=4.87f;
				yPP=1.88f;
			    
			    sirP=1.05f;
			    visP=0.78f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////13
			    xPP=4.77f;
				yPP=2.65f;
			    
			    sirP=1.04f;
			    visP=0.61f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////14
			    xPP=4.82f;
				yPP=3.26f;
			    
			    sirP=0.9f;
			    visP=0.43f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				///
				 /////15
			    xPP=4.96f;
				yPP=3.69f;
			    
			    sirP=0.85f;
			    visP=0.42f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				///
				 /////16
			    xPP=5.07f;
				yPP=4.11f;
			    
			    sirP=0.74f;
			    visP=0.29f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,290,++brPLijevi);
				///
				 /////17
			    xPP=5.25f;
				yPP=4.4f;
			    
			    sirP=0.56f;
			    visP=0.29f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,345,++brPLijevi);
				///
				 /////18
			    xPP=5.81f;
				yPP=4.09f;
			    
			    sirP=0.32f;
			    visP=0.6f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,++brPLijevi);
				///
				 /////19
			    xPP=6.13f;
				yPP=4.16f;
			    
			    sirP=0.31f;
			    visP=0.53f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,++brPLijevi);
				///
				 /////20
			    xPP=5.64f;
				yPP=4.69f;
			    
			    sirP=0.8f;
			    visP=0.31f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,345,++brPLijevi);
				///
				 /////21
			    xPP=5.81f;
				yPP=5f;
			    
			    sirP=0.64f;
			    visP=0.61f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,345,++brPLijevi);
				///
				 /////22
			    xPP=6.44f;
				yPP=4.56f;
			    
			    sirP=0.39f;
			    visP=0.75f;
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,++brPLijevi);
				///
				 /////23
			    xPP=6.82f;
				yPP=4.68f;
			      
			    sirP=0.38f;
			    visP=0.64f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////24
			    xPP=6.33f;
				yPP=5.31f;
			    
			    sirP=0.87f;
			    visP=0.48f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////25
			    xPP=6.43f;
				yPP=5.79f;
			    
			    sirP=0.77f;
			    visP=0.47f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,240,++brPLijevi);
				///
				 /////26
			    xPP=6.43f;
				yPP=6.26f;
			    
			    sirP=0.77f;
			    visP=0.47f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////27
			    xPP=5.55f;
				yPP=5.79f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////28
			    xPP=4.66f;
				yPP=5.73f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,170,++brPLijevi);
				///
				 /////29
			    xPP=3.77f;
				yPP=5.65f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,170,++brPLijevi);
				///
				 /////30
			    xPP=2.89f;
				yPP=5.59f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////31
			    xPP=2.0f;
				yPP=5.51f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////32
			    xPP=1.11f;
				yPP=5.45f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////33
			    xPP=0.22f;
				yPP=5.38f;
			    
			    sirP=0.89f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////34
			    xPP=-0.66f;
				yPP=5.31f;
			    
			    sirP=0.88f;
			    visP=0.95f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////35
			    xPP=-1.52f;
				yPP=5.33f;
			    
			    sirP=0.85f;
			    visP=0.95f;

				faIgr.dodajBr204Kraj(0,0,xPP+dodatakX, yPP, sirP, visP,-speed,0,++brPLijevi);
				///////////////////lijevo gore//////////////////////7
				brPLijevi=-1;
				 /////0
			    xPP=-0.77f;
				yPP=1.18f;
			    
			    sirP=0.78f;
			    visP=0.95f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////1
			    xPP=0f;
				yPP=1.18f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////2
			    xPP=0.62f;
				yPP=1.24f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////3
			    xPP=1.24f;
				yPP=1.31f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////4
			    xPP=1.86f;
				yPP=1.4f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////5
			    xPP=2.48f;
				yPP=1.49f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////6
			    xPP=3.1f;
				yPP=1.61f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////7
			    xPP=3.72f;
				yPP=1.71f;
			    
			    sirP=0.62f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////8
			    xPP=4.35f;
				yPP=1.8f;
			    
			    sirP=0.52f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				////////////////////desno dolje 
				brPLijevi=-1;
				 /////0
			    xPP=12.49f;
				yPP=6.26f;
			    
			    sirP=0.78f;
			    visP=0.95f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////1
			    xPP=11.95f;
				yPP=6.31f;
			    
			    sirP=0.54f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,170,++brPLijevi);
				///
				 /////2
			    xPP=11.41f;
				yPP=6.2f;
			    
			    sirP=0.54f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////3
			    xPP=10.98f;
				yPP=6.21f;
			    
			    sirP=0.44f;
			    visP=0.94f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,130,++brPLijevi);
				///
				 /////4
			    xPP=10.54f;
				yPP=6.21f;
			    
			    sirP=0.44f;
			    visP=0.94f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,++brPLijevi);
				///
				 /////5
			    xPP=10.45f;
				yPP=5.72f;
			    
			    sirP=0.94f;
			    visP=0.5f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,100,++brPLijevi);
				///
				 /////6
			    xPP=10.32f;
				yPP=5.22f;
			    
			    sirP=0.94f;
			    visP=0.5f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,90,++brPLijevi);
				///
				 /////7
			    xPP=10.78f;
				yPP=4.43f;
			    
			    sirP=0.47f;
			    visP=0.8f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////8
			    xPP=10.32f;
				yPP=4.43f;
			    
			    sirP=0.47f;
			    visP=0.8f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,130,++brPLijevi);
				///
				 /////9
			    xPP=9.79f;
				yPP=4.43f;
			    
			    sirP=0.52f;
			    visP=0.75f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				//
				 /////10
			    xPP=9.27f;
				yPP=4.43f;
			    
			    sirP=0.52f;
			    visP=0.75f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////11
			    xPP=8.86f;
				yPP=4.43f;
			    
			    sirP=0.41f;
			    visP=0.75f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,230,++brPLijevi);
				///
				 /////12
			    xPP=8.45f;
				yPP=4.43f;
			    
			    sirP=0.41f;
			    visP=0.75f;

				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////13
			    xPP=8.45f;
				yPP=5.18f;
			    
			    sirP=0.82f;
			    visP=0.48f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////14
			    xPP=8.45f;
				yPP=5.65f;
			    
			    sirP=0.82f;
			    visP=0.48f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////15
			    xPP=8.45f;
				yPP=6.13f;
			    
			    sirP=0.82f;
			    visP=0.34f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,230,++brPLijevi);
				///
				 /////16
			    xPP=8.45f;
				yPP=6.46f;
			    
			    sirP=0.82f;
			    visP=0.34f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////17
			    xPP=7.83f;
				yPP=6.00f;
			    
			    sirP=0.62f;
			    visP=0.8f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				///
				 /////¸18
			    xPP=7.21f;
				yPP=6.0f;
			    
			    sirP=0.63f;
			    visP=0.75f;

				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,170,++brPLijevi);
				///
				////////////////////////////////////////////////////
				///////////////////////Z///////////////////////////
				///
		/////////////////////////////////////////////////////////////////////////////////////1. Val//////////////////////////////////////////////////////////////////////////////////////////
		//lijevo	
			//	faIgr.dodajBr_13BossTowerBuster(0,1,1.89f+dodatakX,1.86f,0.2f);
		//faIgr.dodajBr_12BossIzvanzemaljac(0,0,1.89f+dodatakX,1.86f,0.2f);

	    /////lijevo gore

	    xPP=-0.77f;
		yPP=1.18f;
	    
	    sirP=0.78f;
	    visP=0.95f;
	

	 		      
	 		faIgr.dodajBr_3MMA(0,4,-0.77f+dodatakX,1.6f,0.4f); 
	 		faIgr.dodajBr_10ProtivnikPolicajac(0,1,-0.77f+dodatakX,1.6f,0.8f); 

	    /////////////////////
			  /////desno gore glavni
		    xPP=10.76f;
			yPP=-0.78f; 
		    
		    sirP=0.95f;
		    visP=0.78f;
	    faIgr.dodajBr_16DebeliSerac(0,4,11.16f+dodatakX,-0.78f,0.2f);
	
		//////////////////////////
////////////////////desno dolje 
	 		  xPP=12.49f;
				yPP=6.26f;
			    
			    sirP=0.78f;
			    visP=0.95f;
	 		faIgr.dodajBr_9ProtivnikPas(0,10,13.2f+dodatakX,6.7f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(0,1,13.2f+dodatakX,6.7f,0.8f); 	
	 		faIgr.dodajBr_9ProtivnikPas(0,1,13.2f+dodatakX,6.7f,0.2f); 

	////////desno  goree
	 		faIgr.dodajBr_9ProtivnikPas(0,10,11.16f+dodatakX,-0.78f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(0,1,11.16f+dodatakX,-0.78f,0.8f); 	
	 		faIgr.dodajBr_9ProtivnikPas(0,1,11.16f+dodatakX,-0.78f,0.2f); 
	 		////////lijevo goree
	 		faIgr.dodajBr_9ProtivnikPas(0,0,-0.77f+dodatakX,1.6f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(0,1,-0.77f+dodatakX,1.6f,0.8f); 	
	 		faIgr.dodajBr_9ProtivnikPas(0,1,-0.77f+dodatakX,1.6f,0.2f); 
	 		////////
	 		////////	      
	 		
	 		/////lijevo gore
		 		faIgr.dodajBr_3MMA(0,20,-0.77f+dodatakX,1.6f,0.4f); 
		 		faIgr.dodajBr_10ProtivnikPolicajac(0,1,-0.77f+dodatakX,1.6f,0.8f); 
		 		faIgr.dodajBr_8ProtivnikKuhar(0,2,-0.77f+dodatakX,1.6f,0.4f); 
		 		faIgr.dodajBr_10ProtivnikPolicajac(0,1,-0.77f+dodatakX,1.6f,0.8f); 
		 		  /////desno gore glavni

		           faIgr.dodajBr_16DebeliSerac(0,5,11.16f+dodatakX,-0.78f,0.2f);
                ////////////////////desno dolje 
 		        faIgr.dodajBr_2Sminkerica(0,0,13.2f+dodatakX,6.7f,0.5f);
 	        	faIgr.dodajBr_4Reper(0,1,13.2f+dodatakX,6.7f,0.2f);
 		      faIgr.dodajBr_5ProtivnikPocetni(0,1,13.2f+dodatakX,6.7f,0.8f);
 		      faIgr.dodajBr_7ProtivnikCistacica(0,1,13.2f+dodatakX,6.7f,0.5f);
 		      faIgr.dodajBr_6ProtivnikDebeli(0,1,13.2f+dodatakX,6.7f,0.2f);
 		       faIgr.dodajBr_1Radnik(0,1,13.2f+dodatakX,6.7f,0.5f);
 		       
 	         	////////desno  goree
 		 		faIgr.dodajBr_9ProtivnikPas(0,10,11.16f+dodatakX,-0.78f,0.2f); 
 		 		faIgr.dodajBr_9ProtivnikPas(0,1,11.16f+dodatakX,-0.78f,0.8f); 	
 		 		faIgr.dodajBr_9ProtivnikPas(0,1,11.16f+dodatakX,-0.78f,0.2f); 
 		 		////////lijevo goree
 		 		faIgr.dodajBr_9ProtivnikPas(0,4,-0.77f+dodatakX,1.6f,0.2f); 
 		 		faIgr.dodajBr_9ProtivnikPas(0,1,-0.77f+dodatakX,1.6f,0.8f); 	
 		 		faIgr.dodajBr_9ProtivnikPas(0,1,-0.77f+dodatakX,1.6f,0.2f); 	
                ////////////////////desno dolje 
 
 	          	faIgr.dodajBr_9ProtivnikPas(0,4,13.2f+dodatakX,6.7f,0.2f); 
 		       faIgr.dodajBr_9ProtivnikPas(0,1,13.2f+dodatakX,6.7f,0.8f); 	
 		       faIgr.dodajBr_9ProtivnikPas(0,1,13.2f+dodatakX,6.7f,0.2f); 
 		       //////////////////////////////////////////////////////////////2 VAL///////////////////////////////////////////////////////////////////////////////////////////
 		      /////desno gore glavni
 		    faIgr.dodajBr_18Kapitalista(1,5,11.16f+dodatakX,-0.78f,0.5f);
 	    	////////desno  goree
		 		faIgr.dodajBr_9ProtivnikPas(1,10,11.16f+dodatakX,-0.78f,0.2f); 
		 		faIgr.dodajBr_9ProtivnikPas(1,1,11.16f+dodatakX,-0.78f,0.8f); 	
		 		faIgr.dodajBr_9ProtivnikPas(1,1,11.16f+dodatakX,-0.78f,0.2f); 
		 		////////lijevo goree
		 		faIgr.dodajBr_9ProtivnikPas(1,4,-0.77f+dodatakX,1.6f,0.2f); 
		 		faIgr.dodajBr_9ProtivnikPas(1,1,-0.77f+dodatakX,1.6f,0.8f); 	
		 		faIgr.dodajBr_9ProtivnikPas(1,1,-0.77f+dodatakX,1.6f,0.2f); 	
            ////////////////////desno dolje 

	          	faIgr.dodajBr_9ProtivnikPas(1,4,13.2f+dodatakX,6.7f,0.2f); 
		       faIgr.dodajBr_9ProtivnikPas(1,1,13.2f+dodatakX,6.7f,0.8f); 	
		       faIgr.dodajBr_9ProtivnikPas(1,1,13.2f+dodatakX,6.7f,0.2f); 
		       
		       ///////////////////////////////////
		       ///////////////////////////////////
				/////lijevo gore
		 		faIgr.dodajBr_3MMA(1,20,-0.77f+dodatakX,1.6f,0.4f); 
		 		faIgr.dodajBr_10ProtivnikPolicajac(1,1,-0.77f+dodatakX,1.6f,0.8f); 
		 		faIgr.dodajBr_8ProtivnikKuhar(1,2,-0.77f+dodatakX,1.6f,0.4f); 
		 		faIgr.dodajBr_10ProtivnikPolicajac(1,1,-0.77f+dodatakX,1.6f,0.8f); 
		 	     /////desno gore glavni
	 		    faIgr.dodajBr_18Kapitalista(1,5,11.16f+dodatakX,-0.78f,0.5f);
               ////////////////////desno dolje 
		        faIgr.dodajBr_2Sminkerica(1,25,13.2f+dodatakX,6.7f,0.5f);
	        	faIgr.dodajBr_4Reper(1,1,13.2f+dodatakX,6.7f,0.2f);
		      faIgr.dodajBr_5ProtivnikPocetni(1,1,13.2f+dodatakX,6.7f,0.8f);
		      faIgr.dodajBr_7ProtivnikCistacica(1,1,13.2f+dodatakX,6.7f,0.5f);
		      faIgr.dodajBr_6ProtivnikDebeli(1,1,13.2f+dodatakX,6.7f,0.2f);
		       faIgr.dodajBr_1Radnik(1,1,13.2f+dodatakX,6.7f,0.5f);
		       ////////////////////desno dolje 

	          	faIgr.dodajBr_9ProtivnikPas(1,4,13.2f+dodatakX,6.7f,0.2f); 
		       faIgr.dodajBr_9ProtivnikPas(1,1,13.2f+dodatakX,6.7f,0.8f); 	
		       faIgr.dodajBr_9ProtivnikPas(1,1,13.2f+dodatakX,6.7f,0.2f);   
		       ///////////////////////////////////////////////////////3. val////////////////////////////////////////////////////////////////////////////////////////////////////////////
		       faIgr.dodajBr_12BossIzvanzemaljac(2,5,10.5f+dodatakX,1.2f,3,0.5f);
		       
		       ////////////////////desno dolje 
		        faIgr.dodajBr_2Sminkerica(2,7,13.2f+dodatakX,6.7f,0.5f);
	        	faIgr.dodajBr_4Reper(2,1,13.2f+dodatakX,6.7f,0.2f);
		      faIgr.dodajBr_5ProtivnikPocetni(2,1,13.2f+dodatakX,6.7f,0.8f);
		      faIgr.dodajBr_7ProtivnikCistacica(2,1,13.2f+dodatakX,6.7f,0.5f);
		      faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f+dodatakX,6.7f,0.2f);
		       faIgr.dodajBr_1Radnik(2,1,13.2f+dodatakX,6.7f,0.5f);
		       ////////////////////desno dolje 
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,12,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		       ////////////////////////////////
		       ////////////////////////////////
		       ////////////////////desno dolje 
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,35,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(2,2,13.2f+dodatakX,6.7f,0.5f);
		        /////lijevo gore
		 		faIgr.dodajBr_3MMA(2,0,-0.77f+dodatakX,1.6f,0.4f); 
		 		faIgr.dodajBr_10ProtivnikPolicajac(2,1,-0.77f+dodatakX,1.6f,0.8f); 
		 		faIgr.dodajBr_8ProtivnikKuhar(2,2,-0.77f+dodatakX,1.6f,0.4f); 
		 		faIgr.dodajBr_10ProtivnikPolicajac(2,1,-0.77f+dodatakX,1.6f,0.8f); 
		        ////////////////////desno dolje 

	          	faIgr.dodajBr_9ProtivnikPas(2,8,13.2f+dodatakX,6.7f,0.2f); 
		       faIgr.dodajBr_9ProtivnikPas(2,1,13.2f+dodatakX,6.7f,0.8f); 	
		       faIgr.dodajBr_9ProtivnikPas(2,1,13.2f+dodatakX,6.7f,0.2f); 
		       faIgr.dodajBr_9ProtivnikPas(2,1,13.2f+dodatakX,6.7f,0.8f); 	
		       faIgr.dodajBr_9ProtivnikPas(2,1,13.2f+dodatakX,6.7f,0.2f); 
		   	
		 	   ////////////////////desno dolje 
 		        faIgr.dodajBr_2Sminkerica(2,8,13.2f+dodatakX,6.7f,0.5f);
 	        	faIgr.dodajBr_4Reper(2,1,13.2f+dodatakX,6.7f,0.2f);
 		      faIgr.dodajBr_5ProtivnikPocetni(2,1,13.2f+dodatakX,6.7f,0.8f);
 		      faIgr.dodajBr_7ProtivnikCistacica(2,1,13.2f+dodatakX,6.7f,0.5f);
 		      faIgr.dodajBr_6ProtivnikDebeli(2,1,13.2f+dodatakX,6.7f,0.2f);
 		       faIgr.dodajBr_1Radnik(2,1,13.2f+dodatakX,6.7f,0.5f);
 		       //////////////////////////////////////////////////////////////////////////////////4 VAL////////////////////////////////////////////////////////////////////////////////////////////////
 		   
 		     /////lijevo  gore glavni
 	 		    faIgr.dodajBr_18Kapitalista(3,5,-0.77f+dodatakX,1.6f,0.5f);
 	 		    
 	 		   faIgr.dodajBr_12BossIzvanzemaljac(3,7,10.5f+dodatakX,1.2f,3,0.5f);
 	 		  ////////////////////desno dolje 
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,5,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,13.2f+dodatakX,6.7f,0.5f);
		        faIgr.dodajBr_11ProtivnikDebeliPrdonja(3,2,13.2f+dodatakX,6.7f,0.5f);
		   
			 	   ////////////////////desno dolje 
	 		        faIgr.dodajBr_2Sminkerica(3,8,13.2f+dodatakX,6.7f,0.5f);
	 	        	faIgr.dodajBr_4Reper(3,1,13.2f+dodatakX,6.7f,0.2f);
	 		      faIgr.dodajBr_5ProtivnikPocetni(3,1,13.2f+dodatakX,6.7f,0.8f);
	 		      faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f+dodatakX,6.7f,0.5f);
	 		      faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f+dodatakX,6.7f,0.2f);
	 		       faIgr.dodajBr_1Radnik(3,1,13.2f+dodatakX,6.7f,0.5f);
	 		      /////lijevo gore
			 		faIgr.dodajBr_3MMA(3,10,-0.77f+dodatakX,1.6f,0.4f); 
			 		faIgr.dodajBr_10ProtivnikPolicajac(3,1,-0.77f+dodatakX,1.6f,0.8f); 
			 		faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.77f+dodatakX,1.6f,0.4f); 
			 		faIgr.dodajBr_10ProtivnikPolicajac(3,1,-0.77f+dodatakX,1.6f,0.8f); 
			 		faIgr.dodajBr_8ProtivnikKuhar(3,2,-0.77f+dodatakX,1.6f,0.4f); 
			 		faIgr.dodajBr_10ProtivnikPolicajac(3,1,-0.77f+dodatakX,1.6f,0.8f); 
			 		///////////////////////////
			 		//////////////////////////////
			 	    /////lijevo  gore glavni
			 		  faIgr.dodajBr_12BossIzvanzemaljac(3,25,5.3f+dodatakX,2.9f,3,0.5f);
	 	 		    faIgr.dodajBr_18Kapitalista(3,8,-0.77f+dodatakX,1.6f,0.5f);
	 	 		    
	 	 		 
			 	   ////////////////////desno dolje 
	 		        faIgr.dodajBr_2Sminkerica(3,12,13.2f+dodatakX,6.7f,0.5f);
	 	        	faIgr.dodajBr_4Reper(3,1,13.2f+dodatakX,6.7f,0.2f);
	 		      faIgr.dodajBr_5ProtivnikPocetni(3,1,13.2f+dodatakX,6.7f,0.8f);
	 		      faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f+dodatakX,6.7f,0.5f);
	 		      faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f+dodatakX,6.7f,0.2f);
	 		       faIgr.dodajBr_1Radnik(3,1,13.2f+dodatakX,6.7f,0.5f);
	 		      ////////////////////desno dolje 

		          	faIgr.dodajBr_9ProtivnikPas(3,4,13.2f+dodatakX,6.7f,0.2f); 
			       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.8f); 	
			       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.2f); 
			    	faIgr.dodajBr_9ProtivnikPas(3,4,13.2f+dodatakX,6.7f,0.2f); 
				       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.8f); 	
				       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.2f); 
				       ////////////////////desno dolje 
		 		        faIgr.dodajBr_2Sminkerica(3,12,13.2f+dodatakX,6.7f,0.5f);
		 	        	faIgr.dodajBr_4Reper(3,1,13.2f+dodatakX,6.7f,0.2f);
		 		      faIgr.dodajBr_5ProtivnikPocetni(3,1,13.2f+dodatakX,6.7f,0.8f);
		 		      faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f+dodatakX,6.7f,0.5f);
		 		      faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f+dodatakX,6.7f,0.2f);
		 		       faIgr.dodajBr_1Radnik(3,1,13.2f+dodatakX,6.7f,0.5f);
		 		      faIgr.dodajBr_7ProtivnikCistacica(3,1,13.2f+dodatakX,6.7f,0.5f);
		 		      faIgr.dodajBr_6ProtivnikDebeli(3,1,13.2f+dodatakX,6.7f,0.2f);
		 		       faIgr.dodajBr_1Radnik(3,1,13.2f+dodatakX,6.7f,0.5f);
				       ////////////////////desno dolje 

			          	faIgr.dodajBr_9ProtivnikPas(3,7,13.2f+dodatakX,6.7f,0.2f); 
				       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.8f); 	
				       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.2f); 
				    	faIgr.dodajBr_9ProtivnikPas(3,4,13.2f+dodatakX,6.7f,0.2f); 
					       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.8f); 	
					       faIgr.dodajBr_9ProtivnikPas(3,1,13.2f+dodatakX,6.7f,0.2f); 
			///////////////////////////////////////////////////////////////5 VAL/////////////////////////////////////////////////////////////////////////////////////////////////////////
					       /////lijevo  gore glavni
			 	 		   faIgr.dodajBr_18Kapitalista(4,5,-0.77f+dodatakX,1.6f,0.5f);	 	
			 	 		  ////DESNO GORE
			 	 		   faIgr.dodajBr_16DebeliSerac(4,0,11.16f+dodatakX,-0.78f,0.2f);		
			 	 		   faIgr.dodajBr_12BossIzvanzemaljac(4,7,10.5f+dodatakX,1.2f,3,0.5f);
			 	 		   ////////////////////desno dolje 
					        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,5,13.2f+dodatakX,6.7f,0.5f);
					        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
					        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
					        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
					        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
					        ////////////////////desno dolje 
					           faIgr.dodajBr_9ProtivnikPas(4,5,13.2f+dodatakX,6.7f,0.2f); 
						       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.8f); 	
						       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.2f); 
						       faIgr.dodajBr_9ProtivnikPas(4,4,13.2f+dodatakX,6.7f,0.2f); 
							   faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.8f); 	
							   faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.2f); 
						        /////lijevo gore
						 		faIgr.dodajBr_3MMA(4,5,-0.77f+dodatakX,1.6f,0.4f); 
						 		faIgr.dodajBr_10ProtivnikPolicajac(4,1,-0.77f+dodatakX,1.6f,0.8f); 
						 		faIgr.dodajBr_8ProtivnikKuhar(4,2,-0.77f+dodatakX,1.6f,0.4f); 
						 		faIgr.dodajBr_10ProtivnikPolicajac(4,1,-0.77f+dodatakX,1.6f,0.8f); 
						 		faIgr.dodajBr_8ProtivnikKuhar(4,2,-0.77f+dodatakX,1.6f,0.4f); 
						 		faIgr.dodajBr_10ProtivnikPolicajac(4,1,-0.77f+dodatakX,1.6f,0.8f); 
			 		       ////////////////////desno dolje 
			 		       faIgr.dodajBr_2Sminkerica(4,0,13.2f+dodatakX,6.7f,0.5f);
			 	           faIgr.dodajBr_4Reper(4,1,13.2f+dodatakX,6.7f,0.2f);
			 		       faIgr.dodajBr_5ProtivnikPocetni(4,1,13.2f+dodatakX,6.7f,0.8f);
			 		       faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		       faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f+dodatakX,6.7f,0.2f);
			 		       faIgr.dodajBr_1Radnik(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		       faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		       faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f+dodatakX,6.7f,0.2f);
			 		       faIgr.dodajBr_1Radnik(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		      ////////////////////desno dolje 
				           faIgr.dodajBr_9ProtivnikPas(4,4,13.2f+dodatakX,6.7f,0.2f); 
					       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.8f); 	
					       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.2f); 
					       faIgr.dodajBr_9ProtivnikPas(4,4,13.2f+dodatakX,6.7f,0.2f); 
						   faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.8f); 	
						   faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.2f); 
						   ////////////////////desno dolje 
			 		       faIgr.dodajBr_2Sminkerica(4,0,13.2f+dodatakX,6.7f,0.5f);
			 	           faIgr.dodajBr_4Reper(4,1,13.2f+dodatakX,6.7f,0.2f);
			 		       faIgr.dodajBr_5ProtivnikPocetni(4,1,13.2f+dodatakX,6.7f,0.8f);
			 		       faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		       faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f+dodatakX,6.7f,0.2f);
			 		       faIgr.dodajBr_1Radnik(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		       faIgr.dodajBr_7ProtivnikCistacica(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		       faIgr.dodajBr_6ProtivnikDebeli(4,1,13.2f+dodatakX,6.7f,0.2f);
			 		       faIgr.dodajBr_1Radnik(4,1,13.2f+dodatakX,6.7f,0.5f);
			 		   
					    //////////////////////////////////////////////
			 		    ////////////////////////////////////////////
			 		      /////lijevo  gore glavni
			 	 		   faIgr.dodajBr_18Kapitalista(4,10,-0.77f+dodatakX,1.6f,0.5f);	 	
			 	 		  ////DESNO GORE
			 	 		   faIgr.dodajBr_16DebeliSerac(4,0,11.16f+dodatakX,-0.78f,0.2f);		
			 	 		   faIgr.dodajBr_12BossIzvanzemaljac(4,7,10.5f+dodatakX,1.2f,3,0.5f);
			 	 	////////desno  goree
			 		 		faIgr.dodajBr_9ProtivnikPas(4,10,11.16f+dodatakX,-0.78f,0.2f); 
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,11.16f+dodatakX,-0.78f,0.8f); 	
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,11.16f+dodatakX,-0.78f,0.2f); 
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,11.16f+dodatakX,-0.78f,0.2f); 
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,11.16f+dodatakX,-0.78f,0.8f); 	
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,11.16f+dodatakX,-0.78f,0.2f); 
			 		 		////////lijevo goree
			 		 		faIgr.dodajBr_9ProtivnikPas(4,0,-0.77f+dodatakX,1.6f,0.2f); 
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,-0.77f+dodatakX,1.6f,0.8f); 	
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,-0.77f+dodatakX,1.6f,0.2f); 	
			 		 		faIgr.dodajBr_9ProtivnikPas(4,0,-0.77f+dodatakX,1.6f,0.2f); 
			 		 		faIgr.dodajBr_9ProtivnikPas(4,1,-0.77f+dodatakX,1.6f,0.8f); 	
			 		
			                ////////////////////desno dolje 
			 
			 	          	faIgr.dodajBr_9ProtivnikPas(4,0,13.2f+dodatakX,6.7f,0.2f); 
			 		       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.8f); 	
			 		       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.2f); 
			 		    	faIgr.dodajBr_9ProtivnikPas(4,0,13.2f+dodatakX,6.7f,0.2f); 
				 		       faIgr.dodajBr_9ProtivnikPas(4,1,13.2f+dodatakX,6.7f,0.8f); 	
				 	
				 		      /////lijevo gore
						 		faIgr.dodajBr_3MMA(4,10,-0.77f+dodatakX,1.6f,0.4f); 
						 		faIgr.dodajBr_10ProtivnikPolicajac(4,1,-0.77f+dodatakX,1.6f,0.8f); 
						 		faIgr.dodajBr_8ProtivnikKuhar(4,2,-0.77f+dodatakX,1.6f,0.4f); 
						 		faIgr.dodajBr_10ProtivnikPolicajac(4,1,-0.77f+dodatakX,1.6f,0.8f); 
						 		faIgr.dodajBr_8ProtivnikKuhar(4,2,-0.77f+dodatakX,1.6f,0.4f); 
						 		faIgr.dodajBr_10ProtivnikPolicajac(4,1,-0.77f+dodatakX,1.6f,0.8f); 
				 			  ////////////////////desno dolje 
						        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,0,13.2f+dodatakX,6.7f,0.5f);
						        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
						        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
						        faIgr.dodajBr_11ProtivnikDebeliPrdonja(4,2,13.2f+dodatakX,6.7f,0.5f);
						 //////////////////////////////////////////////////////////////////////6 VAL///////////////////////////////////////////////////////////////////////////////////////////////////////////////
					 		      /////lijevo  gore glavni
					       	 faIgr.dodajBr_18Kapitalista(5,30,-0.77f+dodatakX,1.6f,0.5f);	 	
					 	 	   	 faIgr.dodajBr_16DebeliSerac(5,0,-0.77f+dodatakX,1.6f,0.2f);	
					 	 		  ////DESNO GORE
					 	 		  faIgr.dodajBr_16DebeliSerac(5,0,11.16f+dodatakX,-0.78f,0.2f);		
					 	 		  faIgr.dodajBr_12BossIzvanzemaljac(5,7,10.5f+dodatakX,1.2f,3,0.5f);
					 	 	 	////////desno  goree
							 		faIgr.dodajBr_9ProtivnikPas(5,0,11.16f+dodatakX,-0.78f,0.2f); 
							 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.8f); 	
							 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.2f); 
							 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.8f); 	
							 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.2f); 
							 		////////lijevo goree
							 		faIgr.dodajBr_9ProtivnikPas(5,0,-0.77f+dodatakX,1.6f,0.2f); 
							 		faIgr.dodajBr_9ProtivnikPas(5,1,-0.77f+dodatakX,1.6f,0.8f); 	
							 		faIgr.dodajBr_9ProtivnikPas(5,0,-0.77f+dodatakX,1.6f,0.2f); 	
							 		faIgr.dodajBr_9ProtivnikPas(5,1,-0.77f+dodatakX,1.6f,0.8f); 	
							 		faIgr.dodajBr_9ProtivnikPas(5,0,-0.77f+dodatakX,1.6f,0.2f); 	
					            ////////////////////desno dolje 

						          	faIgr.dodajBr_9ProtivnikPas(5,0,13.2f+dodatakX,6.7f,0.2f); 
							       faIgr.dodajBr_9ProtivnikPas(5,1,13.2f+dodatakX,6.7f,0.8f); 	
							       faIgr.dodajBr_9ProtivnikPas(5,0,13.2f+dodatakX,6.7f,0.2f); 
							       faIgr.dodajBr_9ProtivnikPas(5,1,13.2f+dodatakX,6.7f,0.8f); 	
							       faIgr.dodajBr_9ProtivnikPas(5,0,13.2f+dodatakX,6.7f,0.2f); 
							       ////////////////////desno dolje 
					 		       faIgr.dodajBr_2Sminkerica(5,0,13.2f+dodatakX,6.7f,0.5f);
					 	           faIgr.dodajBr_4Reper(5,1,13.2f+dodatakX,6.7f,0.2f);
					 	          faIgr.dodajBr_14ProtivnikStudentica(5,1,13.2f+dodatakX,6.7f,0.5f);
					 		       faIgr.dodajBr_5ProtivnikPocetni(5,1,13.2f+dodatakX,6.7f,0.8f);
					 		       faIgr.dodajBr_7ProtivnikCistacica(5,1,13.2f+dodatakX,6.7f,0.5f);
					 		       faIgr.dodajBr_6ProtivnikDebeli(5,1,13.2f+dodatakX,6.7f,0.2f);
					 		       faIgr.dodajBr_1Radnik(5,1,13.2f+dodatakX,6.7f,0.8f);
					 		      faIgr.dodajBr_14ProtivnikStudentica(5,1,13.2f+dodatakX,6.7f,0.5f);
					 		       faIgr.dodajBr_7ProtivnikCistacica(5,1,13.2f+dodatakX,6.7f,0.8f);
					 		       faIgr.dodajBr_6ProtivnikDebeli(5,1,13.2f+dodatakX,6.7f,0.2f);
					 		       faIgr.dodajBr_1Radnik(5,1,13.2f+dodatakX,6.7f,0.5f);
							       /////lijevo gore
							 		faIgr.dodajBr_3MMA(5,20,-0.77f+dodatakX,1.6f,0.4f); 
							 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
							 		faIgr.dodajBr_8ProtivnikKuhar(5,2,-0.77f+dodatakX,1.6f,0.4f); 
							 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
							 		faIgr.dodajBr_8ProtivnikKuhar(5,2,-0.77f+dodatakX,1.6f,0.4f); 
							 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
							 	     /////DESNO gore
							 		faIgr.dodajBr_3MMA(5,15,13.2f+dodatakX,6.7f,0.4f); 
							 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,13.2f+dodatakX,6.7f,0.8f); 
							 		faIgr.dodajBr_8ProtivnikKuhar(5,2,13.2f+dodatakX,6.7f,0.4f); 
							 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,13.2f+dodatakX,6.7f,0.8f); 
							 		faIgr.dodajBr_8ProtivnikKuhar(5,2,13.2f+dodatakX,6.7f,0.4f); 
							 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,13.2f+dodatakX,6.7f,0.8f); 
			                    	////////////////////desno dolje 
						 		       faIgr.dodajBr_2Sminkerica(5,0,13.2f+dodatakX,6.7f,0.5f);
						 	           faIgr.dodajBr_4Reper(5,1,13.2f+dodatakX,6.7f,0.2f);
						 	          faIgr.dodajBr_14ProtivnikStudentica(5,1,13.2f+dodatakX,6.7f,0.5f);
						 		       faIgr.dodajBr_5ProtivnikPocetni(5,1,13.2f+dodatakX,6.7f,0.8f);
						 		       faIgr.dodajBr_7ProtivnikCistacica(5,1,13.2f+dodatakX,6.7f,0.5f);
						 		       faIgr.dodajBr_6ProtivnikDebeli(5,1,13.2f+dodatakX,6.7f,0.2f);
						 		       faIgr.dodajBr_1Radnik(5,1,13.2f+dodatakX,6.7f,0.8f);
						 		      faIgr.dodajBr_14ProtivnikStudentica(5,1,13.2f+dodatakX,6.7f,0.5f);
						 		       faIgr.dodajBr_7ProtivnikCistacica(5,1,13.2f+dodatakX,6.7f,0.8f);
						 		       faIgr.dodajBr_6ProtivnikDebeli(5,1,13.2f+dodatakX,6.7f,0.2f);
						 		       faIgr.dodajBr_1Radnik(5,1,13.2f+dodatakX,6.7f,0.5f);
							 		//////////////////////////////////////////
						 		       ////////////////////////////////////////////
						 		      /////lijevo  gore glavni
							 	 		 faIgr.dodajBr_18Kapitalista(5,20,-0.77f+dodatakX,1.6f,0.5f);	 	
							 	 	   	 faIgr.dodajBr_16DebeliSerac(5,0,-0.77f+dodatakX,1.6f,0.2f);	
							 	 		  ////DESNO GORE
							 	 		  faIgr.dodajBr_16DebeliSerac(5,0,11.16f+dodatakX,-0.78f,0.2f);		
							 	 		  faIgr.dodajBr_12BossIzvanzemaljac(5,7,10.5f+dodatakX,1.2f,3,0.5f);
							 	 		  ////////////////////desno dolje 
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,13.2f+dodatakX,6.7f,0.5f);
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,13.2f+dodatakX,6.7f,0.5f);
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,13.2f+dodatakX,6.7f,0.5f);
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,13.2f+dodatakX,6.7f,0.5f);
							 	 		////////desno  goree
									 		faIgr.dodajBr_9ProtivnikPas(5,0,11.16f+dodatakX,-0.78f,0.2f); 
									 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.2f); 
									 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(5,1,11.16f+dodatakX,-0.78f,0.2f); 
									 		////////lijevo goree
									 		faIgr.dodajBr_9ProtivnikPas(5,0,-0.77f+dodatakX,1.6f,0.2f); 
									 		faIgr.dodajBr_9ProtivnikPas(5,1,-0.77f+dodatakX,1.6f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(5,0,-0.77f+dodatakX,1.6f,0.2f); 	
									 		faIgr.dodajBr_9ProtivnikPas(5,1,-0.77f+dodatakX,1.6f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(5,0,-0.77f+dodatakX,1.6f,0.2f); 	
							            ////////////////////desno dolje 

								          	faIgr.dodajBr_9ProtivnikPas(5,0,13.2f+dodatakX,6.7f,0.2f); 
									       faIgr.dodajBr_9ProtivnikPas(5,1,13.2f+dodatakX,6.7f,0.8f); 	
									       faIgr.dodajBr_9ProtivnikPas(5,0,13.2f+dodatakX,6.7f,0.2f); 
									       faIgr.dodajBr_9ProtivnikPas(5,1,13.2f+dodatakX,6.7f,0.8f); 	
									       faIgr.dodajBr_9ProtivnikPas(5,0,13.2f+dodatakX,6.7f,0.2f); 
									       
									       ////////////////////desno dolje 
							 		       faIgr.dodajBr_2Sminkerica(5,0,13.2f+dodatakX,6.7f,0.5f);
							 	           faIgr.dodajBr_4Reper(5,1,13.2f+dodatakX,6.7f,0.2f);
							 	          faIgr.dodajBr_14ProtivnikStudentica(5,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_5ProtivnikPocetni(5,1,13.2f+dodatakX,6.7f,0.8f);
							 		       faIgr.dodajBr_7ProtivnikCistacica(5,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_6ProtivnikDebeli(5,1,13.2f+dodatakX,6.7f,0.2f);
							 		       faIgr.dodajBr_1Radnik(5,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_7ProtivnikCistacica(5,1,13.2f+dodatakX,6.7f,0.8f);
							 		      faIgr.dodajBr_14ProtivnikStudentica(5,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_6ProtivnikDebeli(5,1,13.2f+dodatakX,6.7f,0.2f);
							 		       faIgr.dodajBr_1Radnik(5,1,13.2f+dodatakX,6.7f,0.5f);
							 		       
							 		      /////lijevo gore
									 		faIgr.dodajBr_3MMA(5,20,-0.77f+dodatakX,1.6f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
									 		faIgr.dodajBr_8ProtivnikKuhar(5,2,-0.77f+dodatakX,1.6f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
									 		faIgr.dodajBr_8ProtivnikKuhar(5,2,-0.77f+dodatakX,1.6f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
									 		faIgr.dodajBr_8ProtivnikKuhar(5,2,-0.77f+dodatakX,1.6f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,-0.77f+dodatakX,1.6f,0.8f); 
									 	   /////DESNO gore
									 		faIgr.dodajBr_3MMA(5,0,11.16f+dodatakX,-0.78f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.16f+dodatakX,-0.78f,0.8f); 
									 		faIgr.dodajBr_8ProtivnikKuhar(5,2,11.16f+dodatakX,-0.78f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.16f+dodatakX,-0.78f,0.8f); 
									 		faIgr.dodajBr_8ProtivnikKuhar(5,2,11.16f+dodatakX,-0.78f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.16f+dodatakX,-0.78f,0.8f); 
									 		faIgr.dodajBr_8ProtivnikKuhar(5,2,11.16f+dodatakX,-0.78f,0.4f); 
									 		faIgr.dodajBr_10ProtivnikPolicajac(5,1,11.16f+dodatakX,-0.78f,0.8f); 
									 		
									 		///////////////////////////
									 		//////////////////////////
									 		  faIgr.dodajBr_12BossIzvanzemaljac(5,45,5.6f+dodatakX,4.2f,3,0.5f);//SREDINA
									 		 faIgr.dodajBr_12BossIzvanzemaljac(5,2,1.9f+dodatakX,1.7f,3,0.5f);//LIJEVO GORE
									 		faIgr.dodajBr_12BossIzvanzemaljac(5,0,11f+dodatakX,6.5f,3,0.5f);//DOLJE DESNO
									 		faIgr.dodajBr_12BossIzvanzemaljac(5,1,8.6f+dodatakX,1.5f,3,0.5f);//DESNO GORE							 	
									 	//	faIgr.dodajBr_12BossIzvanzemaljac(5,0,5f+dodatakX,6.1f,3,0.5f);//DOLJE LIJEVO
									 		
									 		
									 		/////////////////////////////////////////////////////////////// 7  VAL////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
									 		 ////////////////////desno dolje 
									 	
									   faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
									        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
							 	 		////////desno  goree
									 		faIgr.dodajBr_9ProtivnikPas(6,0,11.16f+dodatakX,-0.78f,0.2f); 
									 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
									 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
									 		////////lijevo goree
									 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
									 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
									 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
									 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
									 	    /////lijevo  gore glavni
								 	 		 faIgr.dodajBr_18Kapitalista(6,0,-0.77f+dodatakX,1.6f,0.5f);	 	
								 	 	   	 faIgr.dodajBr_16DebeliSerac(6,0,-0.77f+dodatakX,1.6f,0.2f);	
								 	 	     faIgr.dodajBr_12BossIzvanzemaljac(6,5,1.9f+dodatakX,1.7f,3,0.5f);//LIJEVO GORE
								 	 		  ////DESNO GORE
								 	 		  faIgr.dodajBr_16DebeliSerac(6,0,11.16f+dodatakX,-0.78f,0.2f);		
								 	 		 faIgr.dodajBr_18Kapitalista(6,1,10.5f+dodatakX,1.2f,0.5f);	
								 	 		  faIgr.dodajBr_12BossIzvanzemaljac(6,7,10.5f+dodatakX,1.2f,3,0.5f);		
								 	 		  ////////////////////desno dolje 
								 		       faIgr.dodajBr_2Sminkerica(6,30,13.2f+dodatakX,6.7f,0.5f);
								 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);
								 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
								 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
								 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
								 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
								 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
								 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.8f);
								 		      faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
								 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
								 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
								 	
								 		       
								 		      /////lijevo gore
										 		faIgr.dodajBr_3MMA(6,0,-0.77f+dodatakX,1.6f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
										 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
										 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
										 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
										 	   /////DESNO gore
										 		faIgr.dodajBr_3MMA(6,0,11.16f+dodatakX,-0.78f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
										 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
										 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
										 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
										 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
										 		
											      ////////////////////desno dolje 
									 		       faIgr.dodajBr_2Sminkerica(6,15,13.2f+dodatakX,6.7f,0.5f);
									 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
									 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
									 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
									 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
									 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
											       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
											       ////////////////////desno dolje 
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
									 		       //////////////////////////////////////////////
									 		       //////////////////////////////////////////////
												      ////////////////////desno dolje 
											    	 faIgr.dodajBr_16DebeliSerac(6,30,13.2f+dodatakX,6.7f,0.2f);	
											    	 
									 		       faIgr.dodajBr_2Sminkerica(6,6,13.2f+dodatakX,6.7f,0.5f);
									 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
									 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
									 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
									 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
									 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
									 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
											       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
											       
											  	 faIgr.dodajBr_16DebeliSerac(6,7,13.2f+dodatakX,6.7f,0.2f);	

											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
										    	 
											        faIgr.dodajBr_2Sminkerica(6,0,13.2f+dodatakX,6.7f,0.5f);
										 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
										 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
										 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
										 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
										 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
												       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
												 		 ////////////////////desno dolje 
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
										 	 		////////desno  goree
												 		faIgr.dodajBr_9ProtivnikPas(6,0,11.16f+dodatakX,-0.78f,0.2f); 
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
												 		////////lijevo goree
												 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
												 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
												 		//////////////////////////////////////////////
												 		/////////////////////////////////////////////
												 	    faIgr.dodajBr_12BossIzvanzemaljac(6,30,5.6f+dodatakX,4.2f,3,0.5f);//SREDINA
													    faIgr.dodajBr_12BossIzvanzemaljac(6,2,1.9f+dodatakX,1.7f,3,0.5f);//LIJEVO GORE
													 	faIgr.dodajBr_12BossIzvanzemaljac(6,0,11f+dodatakX,6.5f,3,0.5f);//DOLJE DESNO
													 	faIgr.dodajBr_12BossIzvanzemaljac(6,1,8.6f+dodatakX,1.5f,3,0.5f);//DESNO GORE	
													  	faIgr.dodajBr_12BossIzvanzemaljac(6,0,5f+dodatakX,6.1f,3,0.5f);//DOLJE LIJEVO
											 	 		////////desno  goree
												 		faIgr.dodajBr_9ProtivnikPas(6,0,11.16f+dodatakX,-0.78f,0.2f); 
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
												 		////////lijevo goree
												 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
												 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
												 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
												 		/////////////////////////////////////////////////
												 		//////////////////////////////////////////////////
												 	
												 	    /////lijevo gore
												 		faIgr.dodajBr_3MMA(6,40,-0.77f+dodatakX,1.6f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
												 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
												 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
												 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
												 	   /////DESNO gore
												 		faIgr.dodajBr_3MMA(6,0,11.16f+dodatakX,-0.78f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
												 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
												 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
												 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
												 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
												 	    ////////////////////desno dolje 
												    	 faIgr.dodajBr_16DebeliSerac(6,30,13.2f+dodatakX,6.7f,0.2f);	
												    	 
										 		       faIgr.dodajBr_2Sminkerica(6,6,13.2f+dodatakX,6.7f,0.5f);
										 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
										 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
										 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
										 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
										 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
										 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
												       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
												       
												  	 faIgr.dodajBr_16DebeliSerac(6,7,13.2f+dodatakX,6.7f,0.2f);	

												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											    	 
												        faIgr.dodajBr_2Sminkerica(6,0,13.2f+dodatakX,6.7f,0.5f);
											 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
											 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
											 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
											 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
											 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
													       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
													 		 ////////////////////desno dolje 
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
											 	 		////////desno  goree
													 		faIgr.dodajBr_9ProtivnikPas(6,0,11.16f+dodatakX,-0.78f,0.2f); 
													 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
													 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
													 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
													 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
													 		////////lijevo goree
													 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
													 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
													 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
													 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
													 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 	
													 		
													 		
													 		////////////////////////////////////
													 		///////////////////////////////////
													 	    ////////////////////desno dolje 
													    	 faIgr.dodajBr_16DebeliSerac(6,30,13.2f+dodatakX,6.7f,0.2f);	
													    	 
											 		       faIgr.dodajBr_2Sminkerica(6,6,13.2f+dodatakX,6.7f,0.5f);
											 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
											 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
											 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
											 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
											 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
											 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
													       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
													       
													  	 faIgr.dodajBr_16DebeliSerac(6,7,13.2f+dodatakX,6.7f,0.2f);	

													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,5,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
													        faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,13.2f+dodatakX,6.7f,0.5f);
												    	 
													        faIgr.dodajBr_2Sminkerica(6,0,13.2f+dodatakX,6.7f,0.5f);
												 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);							 	           
												 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
												 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
												 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
												 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
												 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
												 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
												 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
												 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
														       faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
														       

														 	    /////lijevo gore
														 		faIgr.dodajBr_3MMA(6,00,-0.77f+dodatakX,1.6f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
														 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
														 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
														 		faIgr.dodajBr_8ProtivnikKuhar(6,2,-0.77f+dodatakX,1.6f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,-0.77f+dodatakX,1.6f,0.8f); 
														 	   /////DESNO gore
														 		faIgr.dodajBr_3MMA(6,0,11.16f+dodatakX,-0.78f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
														 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
														 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
														 		faIgr.dodajBr_8ProtivnikKuhar(6,2,11.16f+dodatakX,-0.78f,0.4f); 
														 		faIgr.dodajBr_10ProtivnikPolicajac(6,1,11.16f+dodatakX,-0.78f,0.8f); 
														 		
														 		 /////lijevo  gore glavni
													 	 		 faIgr.dodajBr_18Kapitalista(6,0,-0.77f+dodatakX,1.6f,0.5f);	 	
													 	 		 faIgr.dodajBr_18Kapitalista(6,1,-0.77f+dodatakX,1.6f,0.5f);	 	
													 	 	   	 faIgr.dodajBr_16DebeliSerac(6,0,-0.77f+dodatakX,1.6f,0.2f);														 
													 	 		  ////DESNO GORE
													 	 		  faIgr.dodajBr_16DebeliSerac(6,0,11.16f+dodatakX,-0.78f,0.2f);		
													 	 		 faIgr.dodajBr_18Kapitalista(6,1,10.5f+dodatakX,1.2f,0.5f);	
													 	 		faIgr.dodajBr_18Kapitalista(6,1,10.5f+dodatakX,1.2f,0.5f);	
							/////////////////////////////////////////////////////////////////////
							/////////////////////////////////////////////////////////////////////
	                       //   	faIgr.dodajBr_13BossTowerBuster(6,5,11.16f+dodatakX,-0.78f,0.5f);	
	                          	
	                         	////////desno  goree
				 		 		faIgr.dodajBr_9ProtivnikPas(6,10,11.16f+dodatakX,-0.78f,0.2f); 
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
				 		 		////////lijevo goree
				 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.2f); 	
				 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
				 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
				 		
				                ////////////////////desno dolje 		 
				 	          	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
				 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
				 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.2f); 
				 		    	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
					 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
					 		       
					 		  	////////////////////desno dolje 
					 		       faIgr.dodajBr_2Sminkerica(6,0,13.2f+dodatakX,6.7f,0.5f);
					 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);
					 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
					 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
					 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
					 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
					 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.8f);
					 		      faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
					 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.8f);
					 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
					 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
					 		       ///////////
					 		////////desno  goree
					 		 		faIgr.dodajBr_9ProtivnikPas(6,15,11.16f+dodatakX,-0.78f,0.2f); 
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
					 		 		////////lijevo goree
					 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.2f); 	
					 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
					 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
					 		
					                ////////////////////desno dolje 		 
					 	          	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
					 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
					 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.2f); 
					 		    	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
						 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
						 		     
						 		  	////////////////////desno dolje 
						 		       faIgr.dodajBr_2Sminkerica(6,0,13.2f+dodatakX,6.7f,0.5f);
						 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);
						 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
						 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
						 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
						 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
						 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.8f);
						 		      faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
						 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.8f);
						 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
						 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
						 		  	////////desno  goree
						 		 		faIgr.dodajBr_9ProtivnikPas(6,15,11.16f+dodatakX,-0.78f,0.2f); 
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
						 		 		////////lijevo goree
						 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.2f); 	
						 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
						 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
						 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
						 		
						                ////////////////////desno dolje 		 
						 	          	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
						 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
						 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.2f); 
						 		    	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
							 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
							 		  	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
							 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
							 		       
							 		  	////////////////////desno dolje 
							 		       faIgr.dodajBr_2Sminkerica(6,0,13.2f+dodatakX,6.7f,0.5f);
							 	           faIgr.dodajBr_4Reper(6,1,13.2f+dodatakX,6.7f,0.2f);
							 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,13.2f+dodatakX,6.7f,0.8f);
							 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
							 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.8f);
							 		      faIgr.dodajBr_14ProtivnikStudentica(6,1,13.2f+dodatakX,6.7f,0.5f);
							 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,13.2f+dodatakX,6.7f,0.8f);
							 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,13.2f+dodatakX,6.7f,0.2f);
							 		       faIgr.dodajBr_1Radnik(6,1,13.2f+dodatakX,6.7f,0.5f);
							 		       
								 		  	////////////////////desno gore
								 		       faIgr.dodajBr_2Sminkerica(6,0,11.16f+dodatakX,-0.78f,0.5f);
								 	           faIgr.dodajBr_4Reper(6,1,11.16f+dodatakX,-0.78f,0.2f);
								 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,11.16f+dodatakX,-0.78f,0.5f);
								 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,11.16f+dodatakX,-0.78f,0.8f);
								 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,11.16f+dodatakX,-0.78f,0.5f);
								 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,11.16f+dodatakX,-0.78f,0.2f);
								 		       faIgr.dodajBr_1Radnik(6,1,11.16f+dodatakX,-0.78f,0.8f);
								 		      faIgr.dodajBr_14ProtivnikStudentica(6,1,11.16f+dodatakX,-0.78f,0.5f);
								 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,11.16f+dodatakX,-0.78f,0.8f);
								 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,11.16f+dodatakX,-0.78f,0.2f);
								 		       faIgr.dodajBr_1Radnik(6,1,11.16f+dodatakX,-0.78f,0.5f);
								 			  	////////////////////desno gore
								 		       faIgr.dodajBr_2Sminkerica(6,0,-0.77f+dodatakX,1.6f,0.5f);
								 	           faIgr.dodajBr_4Reper(6,1,-0.77f+dodatakX,1.6f,0.2f);
								 	          faIgr.dodajBr_14ProtivnikStudentica(6,1,-0.77f+dodatakX,1.6f,0.5f);
								 		       faIgr.dodajBr_5ProtivnikPocetni(6,1,-0.77f+dodatakX,1.6f,0.8f);
								 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,-0.77f+dodatakX,1.6f,0.5f);
								 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,-0.77f+dodatakX,1.6f,0.2f);
								 		       faIgr.dodajBr_1Radnik(6,1,-0.77f+dodatakX,1.6f,0.8f);
								 		      faIgr.dodajBr_14ProtivnikStudentica(6,1,-0.77f+dodatakX,1.6f,0.5f);
								 		       faIgr.dodajBr_7ProtivnikCistacica(6,1,-0.77f+dodatakX,1.6f,0.8f);
								 		       faIgr.dodajBr_6ProtivnikDebeli(6,1,-0.77f+dodatakX,1.6f,0.2f);
								 		       faIgr.dodajBr_1Radnik(6,1,-0.77f+dodatakX,1.6f,0.5f);
								 		////////desno  goree
								 		 		faIgr.dodajBr_9ProtivnikPas(6,15,11.16f+dodatakX,-0.78f,0.2f); 
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.8f); 	
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,11.16f+dodatakX,-0.78f,0.2f); 
								 		 		////////lijevo goree
								 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.2f); 	
								 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
								 		 		faIgr.dodajBr_9ProtivnikPas(6,0,-0.77f+dodatakX,1.6f,0.2f); 
								 		 		faIgr.dodajBr_9ProtivnikPas(6,1,-0.77f+dodatakX,1.6f,0.8f); 	
								 		
								                ////////////////////desno dolje 		 
								 	          	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
								 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
								 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.2f); 
								 		    	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
									 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
									 		  	faIgr.dodajBr_9ProtivnikPas(6,0,13.2f+dodatakX,6.7f,0.2f); 
									 		       faIgr.dodajBr_9ProtivnikPas(6,1,13.2f+dodatakX,6.7f,0.8f); 	
									 		       
									
													 	 	
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8vrh0planine,opts);
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
