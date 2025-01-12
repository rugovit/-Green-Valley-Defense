

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

public class FazaPlacenaSuma extends Activity {

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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8placena8suma, opts);
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
	        faIgr=new FazeIgre(8,400,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
		  
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

				 
	
				 float polX=7.16f;
				 float polY=6.35f;
				 float sir=2.4f;
				 float vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				  polX=-0.39f;
				  polY=6.07f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=-1.76f;
				  polY=0.85f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=-1.48f;
				  polY=-0.21f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=11.75f;
				  polY=-0.35f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=11.29f;
				  polY=-1.59f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=-1.2f;
				  polY=-1.73f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=2.93f;
				  polY=-1.91f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=3.35f;
				  polY=-2.33f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=2.26f;
				  polY=-2.26f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=7.27f;
				  polY=-2.05f;
				  sir=2.4f;
				 vis=3.21f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=3.67f;
				  polY=2.4f;
				  sir=1.34f;
				 vis=2.47f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 502,0,2,polX, polY, sir,vis,polX, polY+3*vis/4, sir,vis/4);
				 
		  /////dodavanje èlanova igrice
				float dodatak=-0.15f; 
	 	///gornji
				
	 	//faIgr.dodajBr200ToranjEmbrio(0,0,11.85f,1.94f);//
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.93f,3.26f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.72f,5.77f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.34f,3.3f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.65f,5.95f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.5f,3.92f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.25f,1.66f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.58f,1.86f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.66f,3.46f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.71f,5.65f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.5663f,3.26f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.24f,5.96f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.49f,5.78f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.84f,5.95f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,0.99f,3.92f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.24f,1.66f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.9f,1.86f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.83f,3.46f);//*
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.78f,5.65f);//*

	 	

	
	 
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
			    ///////////////////////////desno//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
				   brPLijevi=6;
			    /////7
			    xPP=10.25f;
				yPP=-0.36f;
			    
			    sirP=0.95f;
			    visP=0.67f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,260,++brPLijevi);
	
				///
			   /////8
			    xPP=10.17f;
				yPP=0.3f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,260,++brPLijevi);
	
				///
			    /////9
			    xPP=10.09f;
				yPP=0.96f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,260,++brPLijevi);
	
				///
		    	/////10
			    xPP=10.03f;
				yPP=1.62f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,260,++brPLijevi);
	
				///
				///desno-srednje
				brPLijevi=4;
		    	/////5
			    xPP=6.71f;
				yPP=1.99f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
	
				///
				/////6
			    xPP=7.37f;
				yPP=2.04f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
	
				///
				/////7
			    xPP=8.03f;
				yPP=2.09f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
	
				///
				/////8
			    xPP=8.69f;
				yPP=2.15f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
	
				///
				/////9
			    xPP=9.35f;
				yPP=2.23f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,10,++brPLijevi);
	
				///
				/////10
			    xPP=10.02f;
				yPP=2.29f;
			    
			    sirP=0.48f;
			    visP=0.94f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,++brPLijevi);
	
				///
				/////11
			    xPP=10.49f;
				yPP=2.29f;
			    
			    sirP=0.47f;
			    visP=0.94f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
	
				///
				/////12
			    xPP=10.03f;
				yPP=3.23f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
	
				///
				/////13
			    xPP=10.09f;
				yPP=3.89f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
	
				///
				/////14
			    xPP=10.17f;
				yPP=4.55f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
	
				///
				/////15
			    xPP=10.23f;
				yPP=5.21f;
			    
			    sirP=0.94f;
			    visP=0.66f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
	
				///
				/////16
			    xPP=10.23f;
				yPP=5.89f;
			    
			    sirP=0.95f;
			    visP=0.48f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,230,++brPLijevi);
	
				///
				/////17
			    xPP=10.23f;
				yPP=6.37f;
			    
			    sirP=0.95f;
			    visP=0.48f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
	
				///
				/////18
			    xPP=9.57f;
				yPP=5.95f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
	
				///
				/////19
			    xPP=8.9f;
				yPP=6.0f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
	
				///
				/////20
			    xPP=8.24f;
				yPP=6.08f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
	
				///
				/////21
			    xPP=7.58f;
				yPP=6.15f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
	
				///
				/////22
			    xPP=6.92f;
				yPP=6.23f;
			    
			    sirP=0.66f;
			    visP=0.94f;
				
				
				faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
	
				///
				/////23
			    xPP=6.58f;
				yPP=6.28f;
			    
			    sirP=0.34f;
			    visP=0.94f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,220,++brPLijevi);
	
				///
				/////24
			    xPP=6.24f;
				yPP=6.28f;
			    
			    sirP=0.34f;
			    visP=0.94f;
				
				
				faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
	
				///
				////////////////////////////////////////////////////////lijevo/////////////////////////////////////////////////////////////////////////////////
				
				 
					   brPLijevi=6;
		
					    /////7
					    xPP=1.3f;
						yPP=-0.36f;
					    
					    sirP=0.95f;
					    visP=0.67f;
						
						
						faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
			
						///
						 /////8
					    xPP=1.39f;
						yPP=0.3f;
					    
					    sirP=0.95f;
					    visP=0.67f;
						
						
						faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
			
						///
						 /////9
					    xPP=1.46f;
						yPP=0.96f;
					    
					    sirP=0.95f;
					    visP=0.67f;
						
						
						faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
			
						///
						 /////10
					    xPP=1.52f;
						yPP=1.62f;
					    
					    sirP=0.94f;
					    visP=0.66f;
						
						
						faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
			
						///
						//desno-srednje
						   brPLijevi=4;
						    /////5
						    xPP=5.12f;
							yPP=1.99f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				
							///
						    /////6
						    xPP=4.46f;
							yPP=2.04f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				
							///
							   /////7
						    xPP=3.8f;
							yPP=2.09f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				
							///
							   /////8
						    xPP=3.13f;
							yPP=2.15f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,190,++brPLijevi);
				
							///
							   /////9
						    xPP=2.47f;
							yPP=2.23f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,++brPLijevi);
				
							///
							   /////10
						    xPP=2.00f;
							yPP=2.29f;
						    
						    sirP=0.48f;
						    visP=0.94f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,220,++brPLijevi);
				
							///
							   /////11
						    xPP=1.52f;
							yPP=2.29f;
						    
						    sirP=0.47f;
						    visP=0.94f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				
							///
							   /////12
						    xPP=1.52f;
							yPP=3.23f;
						    
						    sirP=0.94f;
						    visP=0.66f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				
							///
							   /////13
						    xPP=1.46f;
							yPP=3.89f;
						    
						    sirP=0.94f;
						    visP=0.66f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				
							///
							
							   /////14
						    xPP=1.39f;
							yPP=4.55f;
						    
						    sirP=0.94f;
						    visP=0.66f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				
							///
							   /////15
						    xPP=1.32f;
							yPP=5.21f;
						    
						    sirP=0.94f;
						    visP=0.66f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,280,++brPLijevi);
				
							///
						
							/////16
						    xPP=1.31f;
							yPP=5.88f;
						    
						    sirP=0.95f;
						    visP=0.48f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,++brPLijevi);
				
							///
							/////17
						    xPP=1.31f;
							yPP=6.37f;
						    
						    sirP=0.95f;
						    visP=0.48f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,++brPLijevi);
				
							///
							   /////18
						    xPP=2.26f;
							yPP=5.95f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				
							///
							   /////19
						    xPP=2.92f;
							yPP=6.0f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				
							///
							   /////20
						    xPP=3.58f;
							yPP=6.08f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				
							///
							   /////21
						    xPP=4.25f;
							yPP=6.15f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				
							///
							   /////22
						    xPP=4.91f;
							yPP=6.23f;
						    
						    sirP=0.66f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,350,++brPLijevi);
				
							///
							   /////23
						    xPP=5.57f;
							yPP=6.28f;
						    
						    sirP=0.34f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,++brPLijevi);
				
							///
							   /////24
						    xPP=5.91f;
							yPP=6.28f;
						    
						    sirP=0.34f;
						    visP=0.94f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				
							///
							///////////////////////////////////////srednji gore //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
						    brPLijevi=-1;
						    /////0
						    xPP=5.78f;
							yPP=-0.66f;
						    
						    sirP=0.95f;
						    visP=0.67f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				
							///
							  /////1
						    xPP=5.78f;
							yPP=0.0f;
						    
						    sirP=0.95f;
						    visP=0.67f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				
							///
							  /////2
						    xPP=5.78f;
							yPP=0.66f;
						    
						    sirP=0.95f;
						    visP=0.67f;
							
							
							faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				
							///
							  /////3
						    xPP=5.78f;
							yPP=1.33f;
						    
						    sirP=0.95f;
						    visP=0.67f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
				
							///
							  /////4
						    xPP=5.78f;
							yPP=1.99f;
						    
						    sirP=0.46f;
						    visP=0.71f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,220,++brPLijevi);
				
							///
							  /////4
						    xPP=6.26f;
							yPP=1.98f;
						    
						    sirP=0.48f;
						    visP=0.71f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,320,brPLijevi);
				
							///
							  /////-4 posebni
						    xPP=5.78f;
							yPP=2.68f;
						    
						    sirP=0.46f;
						    visP=0.25f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,180,-brPLijevi);
				
							///
							  /////-4 posebni
						    xPP=6.24f;
							yPP=2.68f;
						    
						    sirP=0.46f;
						    visP=0.25f;
							
							
							faIgr.dodajBr205OkukaKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,0,-brPLijevi);
				
							///
                ///////////////////////////////////////srednji dolje //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				    brPLijevi=24;
				    /////25
				    xPP=5.78f;
					yPP=7.21f;
				    
				    sirP=0.94f;
				    visP=0.66f;
					
					
					faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
		
					///
					 /////26
				    xPP=5.77f;
					yPP=7.88f;
				    
				    sirP=0.94f;
				    visP=0.66f;
					
					
					faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,++brPLijevi);
		
					///
					 /////27
				    xPP=5.77f;
					yPP=8.54f;
				    
				    sirP=0.94f;
				    visP=0.66f;
					
					
					faIgr.dodajBr204Kraj(0,0,xPP, yPP, sirP, visP, 0, speed, ++brPLijevi);
		
					///
							////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				////////////////////////////////////////////////////
				///////////////////////Z///////////////////////////
				///
		/////////////////////////////////////////////////////////////////////////////////////1. Val//////////////////////////////////////////////////////////////////////////////////////////
		//lijevo	
			//	faIgr.dodajBr_13BossTowerBuster(0,1,1.89f+dodatakX,1.86f,0.2f);
		//faIgr.dodajBr_12BossIzvanzemaljac(0,0,1.89f+dodatakX,1.86f,0.2f);
			/////////////////////////////////////////         1 val        //////////////////////////////////////////////////////////////////////////
					
					
					//  test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		     ///////////////srednje
				    /////6
				    xPP=10.27f;
					yPP=-1.02f;
				    
				    sirP=0.95f;
				    visP=0.67f;
					
					
					faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,0);

					///
					   /////6
				    xPP=1.29f;
					yPP=-1.03f;
				    
				    sirP=0.95f;
				    visP=0.67f;
					
					
					faIgr.dodajBr203RavniPutKut(0,0,xPP+dodatakX, yPP, sirP, visP,speed,270,0);

					///
					
					
					
					
					
					
					
					
					
					
					
	 		faIgr.dodajBr_7ProtivnikCistacica(0,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(0,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(0,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(0,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(0,1,6.35f,-1.03f,0.8f); 	
	 			
	 		faIgr.dodajBr_6ProtivnikDebeli(0,13,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_6ProtivnikDebeli(0,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(0,0,6.35f,-1.03f,0.8f); 	
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(0,7,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(0,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(0,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(0,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(0,1,6.35f,-1.03f,0.8f); 	
	 		
	 		faIgr.dodajBr_6ProtivnikDebeli(0,13,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_6ProtivnikDebeli(0,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(0,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_6ProtivnikDebeli(0,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(0,0,6.35f,-1.03f,0.8f); 	
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(0,7,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(0,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(0,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(0,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(0,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(0,1,6.35f,-1.03f,0.8f); 	
             /////////////////////////////////////////////////////////2 val  //////////////////////////////////////////////////////////////////////
		    /////6
		    xPP=10.27f;
			yPP=-1.02f;
		    
		    sirP=0.95f;
		    visP=0.67f;
			
			
			faIgr.dodajBr203RavniPutKut(0,1,xPP+dodatakX, yPP, sirP, visP,speed,270,0);

			///
			   /////6
		    xPP=1.29f;
			yPP=-1.03f;
		    
		    sirP=0.95f;
		    visP=0.67f;
			
			
			faIgr.dodajBr203RavniPutKut(0,1,xPP+dodatakX, yPP, sirP, visP,speed,270,0);

			///
            ///////////////lijevo
					
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(1,7,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(1,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(1,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(1,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(1,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(1,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(1,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(1,1,1.73f,-1.03f,0.8f); 	
	 		
             //////////////////////////
          ///////////////desno
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(1,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(1,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(1,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(1,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(1,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(1,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(1,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(1,1,10.73f,-1.03f,0.8f); 	
           //////////////////////////   				 		       
			/////////////////srednje
	 		faIgr.dodajBr_3MMA(1,8,6.35f,-1.03f,0.2f); 
	 		
	 		faIgr.dodajBr_6ProtivnikDebeli(1,4,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_6ProtivnikDebeli(1,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(1,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_6ProtivnikDebeli(1,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(1,0,6.35f,-1.03f,0.8f); 	
	 		
	 		faIgr.dodajBr_3MMA(1,8,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_7ProtivnikCistacica(1,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(1,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(1,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(1,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(1,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(1,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(1,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(1,1,6.35f,-1.03f,0.8f); 	
				/////////////////////////////////////3. val///////////////////////////////////////////////////////////////////////////////////////////////////
               ///////////////lijevo
					
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(2,7,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(2,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,1.73f,-1.03f,0.8f); 	
	 		
             //////////////////////////
          ///////////////desno
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(2,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(2,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,10.73f,-1.03f,0.8f); 	
			/////////////////srednje
	 
	 		faIgr.dodajBr_15ProtivnikStarleta(2,8,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(2,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_15ProtivnikStarleta(2,2,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(2,1,6.35f,-1.03f,0.4f); 	 
          
	 	
	 		faIgr.dodajBr_7ProtivnikCistacica(2,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(2,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,6.35f,-1.03f,0.8f); 	
	 		
	 		faIgr.dodajBr_15ProtivnikStarleta(2,8,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(2,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_15ProtivnikStarleta(2,2,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(2,1,6.35f,-1.03f,0.4f); 	 
           ///////////////lijevo
			
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(2,7,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(2,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,1.73f,-1.03f,0.8f); 	
	 		
           //////////////////////////
        ///////////////desno
	 		
	 		faIgr.dodajBr_7ProtivnikCistacica(2,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(2,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(2,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(2,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(2,1,10.73f,-1.03f,0.8f); 	
            ////////////////////////////////////4. val///////////////////////////////////////////////////////////////////////////////////////////////////

	 		
	 		///////////////mjesano lijevo/desno
	 		faIgr.dodajBr_7ProtivnikCistacica(3,7,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(3,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(3,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_4Reper(3,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(3,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(3,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(3,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(3,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(3,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(3,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(3,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(3,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_1Radnik(3,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(3,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(3,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(3,1,10.73f,-1.03f,0.8f); 	 		
             //////////////////////////
	 		
			faIgr.dodajBr_9ProtivnikPas(3,3,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(3,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(3,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.2f); 
	 		
	 		faIgr.dodajBr_15ProtivnikStarleta(3,0,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(3,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_15ProtivnikStarleta(3,2,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(3,1,6.35f,-1.03f,0.4f); 	 
          
	 	
	 		faIgr.dodajBr_7ProtivnikCistacica(3,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(3,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(3,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(3,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(3,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(3,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(3,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(3,1,6.35f,-1.03f,0.8f); 	
	 		
	 		faIgr.dodajBr_15ProtivnikStarleta(3,3,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(3,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_15ProtivnikStarleta(3,2,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(3,1,6.35f,-1.03f,0.4f); 	 
	 		///////////mjesano lijevo desno
	 		faIgr.dodajBr_15ProtivnikStarleta(3,0,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(3,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_15ProtivnikStarleta(3,0,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(3,1,6.35f,-1.03f,0.4f); 	 
	 		faIgr.dodajBr_9ProtivnikPas(3,0,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(3,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(3,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(3,1,6.35f,-1.03f,0.2f); 
	 		//////////////////////////////////////////////////////////////////////////5 VAL ////////////////////////////////////////////////////////////////////////////
	 	    //////////////////////////srednje 
	 		 		faIgr.dodajBr_3MMA(4,0,6.35f,-1.03f,0.2f); 
	 		 		faIgr.dodajBr_3MMA(4,0,6.35f,-1.03f,0.4f); 
	 		///////////////mjesano lijevo/desno
	 		faIgr.dodajBr_7ProtivnikCistacica(4,7,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_4Reper(4,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_1Radnik(4,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(3,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,10.73f,-1.03f,0.8f); 	 		
             //////////////////////////srednje 
	 		faIgr.dodajBr_3MMA(4,0,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_3MMA(4,0,6.35f,-1.03f,0.4f); 
	 		faIgr.dodajBr_6ProtivnikDebeli(4,0,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_3MMA(4,1,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,6.35f,-1.03f,0.5f); 	
			faIgr.dodajBr_6ProtivnikDebeli(4,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(4,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_6ProtivnikDebeli(4,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(4,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,6.35f,-1.03f,0.8f); 	
	 		/////////////////srednje
	 		 
	 		faIgr.dodajBr_16DebeliSerac(4,8,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_16DebeliSerac(4,1,6.35f,-1.03f,0.2f); 	 	
	 		///////////////mjesano lijevo/desno
	 		faIgr.dodajBr_7ProtivnikCistacica(4,12,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_4Reper(4,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_1Radnik(4,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,10.73f,-1.03f,0.8f); 	 	
	 		/////////////////srednje
	 		 
	 		faIgr.dodajBr_16DebeliSerac(4,8,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_16DebeliSerac(4,1,6.35f,-1.03f,0.2f); 	 	
             //////////////////////////srednje 
	 		faIgr.dodajBr_3MMA(4,0,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_3MMA(4,0,6.35f,-1.03f,0.4f); 
	 		faIgr.dodajBr_6ProtivnikDebeli(4,0,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_3MMA(4,1,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,6.35f,-1.03f,0.5f); 	
			faIgr.dodajBr_6ProtivnikDebeli(4,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(4,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(4,1,6.35f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(4,1,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(4,1,6.35f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_6ProtivnikDebeli(4,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(4,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(4,1,6.35f,-1.03f,0.8f); 	
			faIgr.dodajBr_9ProtivnikPas(4,3,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(4,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(4,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(4,1,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(4,1,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(4,1,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(4,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(4,1,6.35f,-1.03f,0.2f); 
	 		//////////////////////////////////////////////////////////6 VAL ////////////////////////////////////////////////////////////////////////////////////
	 	    //////////////////////////srednje 
	 		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.2f); 
	 		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.4f); 
	 		 		faIgr.dodajBr_16DebeliSerac(5,8,6.35f,-1.03f,0.8f); 
	 		 		faIgr.dodajBr_16DebeliSerac(5,1,6.35f,-1.03f,0.2f); 	 	
	 		///////////////mjesano lijevo/desno
	 		faIgr.dodajBr_6ProtivnikDebeli(5,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(5,0,6.35f,-1.03f,0.8f); 			
	 		faIgr.dodajBr_7ProtivnikCistacica(5,7,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(5,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(5,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_4Reper(5,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(5,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(5,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(5,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(5,1,1.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(5,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_1Radnik(5,1,10.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(5,1,10.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(5,1,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_1Radnik(5,1,1.73f,-1.03f,0.5f); 	
	 		faIgr.dodajBr_5ProtivnikPocetni(5,1,1.73f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_7ProtivnikCistacica(5,0,10.73f,-1.03f,0.2f); 	 		
	 		faIgr.dodajBr_4Reper(5,1,10.73f,-1.03f,0.8f); 	 
	 	    //////////////////////////srednje 
		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.2f); 
		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.4f); 
		 		faIgr.dodajBr_16DebeliSerac(5,8,6.35f,-1.03f,0.8f); 
		 		faIgr.dodajBr_16DebeliSerac(5,1,6.35f,-1.03f,0.2f); 	 	
	 		///////////mjesano lijevo desno
	 		faIgr.dodajBr_6ProtivnikDebeli(5,2,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_6ProtivnikDebeli(5,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_15ProtivnikStarleta(5,0,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(5,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_15ProtivnikStarleta(5,0,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_15ProtivnikStarleta(5,1,6.35f,-1.03f,0.4f); 	 
	 		faIgr.dodajBr_9ProtivnikPas(5,0,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(5,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(5,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(5,1,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_9ProtivnikPas(5,1,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(5,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_9ProtivnikPas(5,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_9ProtivnikPas(5,1,6.35f,-1.03f,0.2f); 
	 		///////////mjesano lijevo desno
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,12,6.35f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,6.35f,-1.03f,0.8f); 	
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,6.35f,-1.03f,0.6f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,6.35f,-1.03f,0.4f); 	 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,0,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,6.35f,-1.03f,0.2f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,1.73f,-1.03f,0.8f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,1.73f,-1.03f,0.2f); 	 	
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,2,6.35f,-1.03f,0.8f); 
	 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(5,1,6.35f,-1.03f,0.2f); 
	 	    //////////////////////////srednje 
		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.2f); 
		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.4f); 
		 		faIgr.dodajBr_16DebeliSerac(5,8,6.35f,-1.03f,0.8f); 
		 		faIgr.dodajBr_16DebeliSerac(5,1,6.35f,-1.03f,0.2f); 	 	
	         //////////////////////////srednje 
		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.2f); 
		 		faIgr.dodajBr_3MMA(5,0,6.35f,-1.03f,0.4f); 
		 		faIgr.dodajBr_6ProtivnikDebeli(5,0,6.35f,-1.03f,0.2f); 
		 		faIgr.dodajBr_3MMA(5,1,6.35f,-1.03f,0.8f); 
		 		faIgr.dodajBr_7ProtivnikCistacica(5,1,6.35f,-1.03f,0.2f); 	 		
		 		faIgr.dodajBr_4Reper(5,1,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_1Radnik(4,1,6.35f,-1.03f,0.5f); 	
				faIgr.dodajBr_6ProtivnikDebeli(5,1,6.35f,-1.03f,0.2f); 	 	
		 		faIgr.dodajBr_6ProtivnikDebeli(5,0,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_5ProtivnikPocetni(5,1,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_7ProtivnikCistacica(4,1,6.35f,-1.03f,0.2f); 	 		
		 		faIgr.dodajBr_4Reper(5,1,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_1Radnik(5,1,6.35f,-1.03f,0.5f); 	
		 		faIgr.dodajBr_6ProtivnikDebeli(5,2,6.35f,-1.03f,0.2f); 	 	
		 		faIgr.dodajBr_6ProtivnikDebeli(5,0,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_5ProtivnikPocetni(5,1,6.35f,-1.03f,0.8f); 	
		 		////////////////////////////////////////////////////////7 val //////////////////////////////////////////////////////////////////////////////////////
		 		  /////6
			    xPP=10.27f;
				yPP=-1.02f;
			    
			    sirP=0.95f;
			    visP=0.67f;
				
				
				faIgr.dodajBr203RavniPutKut(6,1,xPP+dodatakX, yPP, sirP, visP,speed,270,0);

				///
				   /////6
			    xPP=1.29f;
				yPP=-1.03f;
			    
			    sirP=0.95f;
			    visP=0.67f;
				
				
				faIgr.dodajBr203RavniPutKut(6,1,xPP+dodatakX, yPP, sirP, visP,speed,270,0);

				///
		 	    //////////////////////////srednje 
 		 		faIgr.dodajBr_3MMA(6,0,6.35f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_3MMA(6,0,6.35f,-1.03f,0.4f); 
 		 		faIgr.dodajBr_16DebeliSerac(6,8,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_16DebeliSerac(6,1,6.35f,-1.03f,0.2f); 	 	
 		 		///////////////mjesano lijevo/desno
 		 		faIgr.dodajBr_6ProtivnikDebeli(6,2,6.35f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_6ProtivnikDebeli(6,0,6.35f,-1.03f,0.8f); 			
 		 		faIgr.dodajBr_7ProtivnikCistacica(6,7,1.73f,-1.03f,0.2f); 	 		
 		 		faIgr.dodajBr_4Reper(6,1,1.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_1Radnik(6,1,1.73f,-1.03f,0.5f); 	
 		 		faIgr.dodajBr_4Reper(6,1,10.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_1Radnik(6,1,10.73f,-1.03f,0.5f); 	
 		 		faIgr.dodajBr_5ProtivnikPocetni(6,1,10.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_5ProtivnikPocetni(6,1,1.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_7ProtivnikCistacica(6,1,1.73f,-1.03f,0.2f); 	 		
 		 		faIgr.dodajBr_4Reper(6,1,1.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_1Radnik(6,1,10.73f,-1.03f,0.5f); 	
 		 		faIgr.dodajBr_5ProtivnikPocetni(6,1,10.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_7ProtivnikCistacica(6,1,10.73f,-1.03f,0.2f); 	 		
 		 		faIgr.dodajBr_1Radnik(6,1,1.73f,-1.03f,0.5f); 	
 		 		faIgr.dodajBr_5ProtivnikPocetni(6,1,1.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_7ProtivnikCistacica(6,0,10.73f,-1.03f,0.2f); 	 		
 		 		faIgr.dodajBr_4Reper(6,1,10.73f,-1.03f,0.8f); 	 
 		 	    //////////////////////////srednje 
 	 		 		faIgr.dodajBr_3MMA(6,0,6.35f,-1.03f,0.2f); 
 	 		 		faIgr.dodajBr_3MMA(6,0,6.35f,-1.03f,0.4f); 
 	 		 		faIgr.dodajBr_16DebeliSerac(6,8,6.35f,-1.03f,0.8f); 
 	 		 		faIgr.dodajBr_16DebeliSerac(6,1,6.35f,-1.03f,0.2f); 	
 		 		///////////mjesano lijevo srednje desno
 
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,1.73f,-1.03f,0.2f); 	 	
 				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.2f); 	 	
	 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,6.35f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,2,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,6.35f,-1.03f,0.2f); 
 				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.2f); 	 	
	 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.8f); 	
 		 	    //////////////////////////srednje 
 	 		 		faIgr.dodajBr_3MMA(6,0,6.35f,-1.03f,0.2f); 
 	 		 		faIgr.dodajBr_3MMA(6,0,6.35f,-1.03f,0.4f); 
 	 		 		faIgr.dodajBr_16DebeliSerac(6,8,6.35f,-1.03f,0.8f); 
 	 		 		faIgr.dodajBr_16DebeliSerac(6,1,6.35f,-1.03f,0.2f); 	
 		 		///////////mjesano lijevo desno
 		 		faIgr.dodajBr_15ProtivnikStarleta(6,0,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_15ProtivnikStarleta(6,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_15ProtivnikStarleta(6,0,6.35f,-1.03f,0.6f); 
 		 		faIgr.dodajBr_15ProtivnikStarleta(6,1,6.35f,-1.03f,0.4f); 	 
 		 		faIgr.dodajBr_9ProtivnikPas(6,0,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(6,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_9ProtivnikPas(6,2,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(6,1,6.35f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_9ProtivnikPas(6,1,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(6,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_9ProtivnikPas(6,2,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(6,1,6.35f,-1.03f,0.2f); 
 		 		///////////mjesano lijevo srednje desno
	 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.2f); 	 	
	 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,6.35f,-1.03f,0.2f); 	 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,1.73f,-1.03f,0.8f); 
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,1.73f,-1.03f,0.2f); 	
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,10.73f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,6.35f,-1.03f,0.6f); 
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,1,6.35f,-1.03f,0.4f); 	 
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(6,0,1.73f,-1.03f,0.8f); 
		 		/////////////////////////////////////////////////////////////////////////////8 VAL ////////////////////////////////////////////////////////////
		 		
		 	    //////////////////////////srednje 
 		 		faIgr.dodajBr_3MMA(7,0,6.35f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_3MMA(7,0,6.35f,-1.03f,0.4f); 
 		 		faIgr.dodajBr_16DebeliSerac(7,8,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_16DebeliSerac(7,1,6.35f,-1.03f,0.2f); 	 	
 		 		///////////mjesano lijevo  desno
 		 		faIgr.dodajBr_10ProtivnikPolicajac(6,0,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_10ProtivnikPolicajac(6,0,10.73f,-1.03f,0.8f); 	
 		 		faIgr.dodajBr_3MMA(7,0,1.73f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_3MMA(7,0,10.73f,-1.03f,0.4f); 
 		 		///////////mjesano lijevo srednje desno
	 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,10.73f,-1.03f,0.2f); 	 	
	 		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,10.73f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,6.35f,-1.03f,0.2f); 	 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,6.35f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,1.73f,-1.03f,0.8f); 
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,1,1.73f,-1.03f,0.2f); 	
				faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,10.73f,-1.03f,0.2f); 	 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,10.73f,-1.03f,0.8f); 	
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,6.35f,-1.03f,0.6f); 
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,1,6.35f,-1.03f,0.4f); 	 
		 		faIgr.dodajBr_11ProtivnikDebeliPrdonja(7,0,1.73f,-1.03f,0.8f); 
 		 		///////////mjesano lijevo desno
 		 		faIgr.dodajBr_15ProtivnikStarleta(7,0,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_15ProtivnikStarleta(7,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_15ProtivnikStarleta(7,0,6.35f,-1.03f,0.6f); 
 		 		faIgr.dodajBr_15ProtivnikStarleta(7,1,6.35f,-1.03f,0.4f); 	 
 		 		faIgr.dodajBr_9ProtivnikPas(7,0,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(7,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_9ProtivnikPas(7,2,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(7,1,6.35f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_9ProtivnikPas(7,1,1.73f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(7,1,1.73f,-1.03f,0.2f); 	 	
 		 		faIgr.dodajBr_9ProtivnikPas(7,2,6.35f,-1.03f,0.8f); 
 		 		faIgr.dodajBr_9ProtivnikPas(7,1,6.35f,-1.03f,0.2f); 
 		 		faIgr.dodajBr_5ProtivnikPocetni(7,1,1.73f,-1.03f,0.8f); 	

 	           //////////////////////////
 			///////////////mjesano lijevo/desno
 		 		 		faIgr.dodajBr_6ProtivnikDebeli(7,2,6.35f,-1.03f,0.2f); 	 	
 		 		 		faIgr.dodajBr_6ProtivnikDebeli(7,0,6.35f,-1.03f,0.8f); 			
 		 		 		faIgr.dodajBr_7ProtivnikCistacica(7,7,1.73f,-1.03f,0.2f); 	 		
 		 		 		faIgr.dodajBr_4Reper(7,1,1.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_1Radnik(7,1,1.73f,-1.03f,0.5f); 	
 		 		 		faIgr.dodajBr_4Reper(7,1,10.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_1Radnik(7,1,10.73f,-1.03f,0.5f); 	
 		 		 		faIgr.dodajBr_5ProtivnikPocetni(7,1,10.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_5ProtivnikPocetni(7,1,1.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_7ProtivnikCistacica(7,1,1.73f,-1.03f,0.2f); 	 		
 		 		 		faIgr.dodajBr_4Reper(7,1,1.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_1Radnik(7,1,10.73f,-1.03f,0.5f); 	
 		 		 		faIgr.dodajBr_5ProtivnikPocetni(7,1,10.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_7ProtivnikCistacica(7,1,10.73f,-1.03f,0.2f); 	 		
 		 		 		faIgr.dodajBr_1Radnik(7,1,1.73f,-1.03f,0.5f); 	
 		 		 		faIgr.dodajBr_5ProtivnikPocetni(7,1,1.73f,-1.03f,0.8f); 	
 		 		 		faIgr.dodajBr_7ProtivnikCistacica(7,0,10.73f,-1.03f,0.2f); 	 		
 		 		 		faIgr.dodajBr_4Reper(7,1,10.73f,-1.03f,0.8f); 	 
 	
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8placena8suma,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			SpriteHendler dodatak=new SpriteHendler(1);
			dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8borva0suma,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(501, dodatak);
			SpriteHendler dodatak1=new SpriteHendler(1);  
			dodatak1.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8staza8placena8suma8obelisk,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(502, dodatak1);
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
