
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

public class FazaZapaljenoSelo extends Activity {
	
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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8zapaljeno0selo, opts);
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
		
		double vrijemeLodiranja=System.currentTimeMillis();
	     vrijemeLodiranja=(System.currentTimeMillis()-vrijemeLodiranja);
	     Log.d("Load vrijeme", "Lodiranje faze=" +vrijemeLodiranja);
	    NativeHeapAllocatedSize= Debug.getNativeHeapAllocatedSize()/1024;
		 GlobalAllocSize=Debug.getGlobalAllocSize()/1024;
		 ThreadAllocSize= Debug.getThreadAllocSize()/1024;                             

	        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
	        vrijemeLodiranja=(System.currentTimeMillis()-vrijemeLodiranja);
		     Log.d("Load vrijeme", "Lodiranje scene=" +vrijemeLodiranja);
	   
	    //////////inicijalizacija gamelogica i uimanager pozadine
	    /////////ui manager*/
	        faIgr=new FazeIgre(6,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
	        faIgr.postaviNovcePrijeDolaskaNovogVala(2);
		    faIgr.setDelayIzmVal(5);
		    faIgr.setDelayIzmVal(0,10);
		    faIgr.setDelayIzmVal(1,10);
		    faIgr.setDelayIzmVal(2,10);
		    faIgr.setDelayIzmVal(3,20);
		    faIgr.setDelayIzmVal(4,20);
		    faIgr.setDelayIzmVal(5,20);
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
				 
				 faIgr. dodajPocetneParametreIgre(100,10);
				 faIgr.postaviNovcePrijeDolaskaNovogVala(1);
				 faIgr.setDelayIzmVal(30);
				   
				 polX=-0.78f;
				 polY=1.76f;
				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=10.34f;
				 polY=6.84f;
				 
			
				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=0.9f;
				 polY=-1.38f;
				 
				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 
				 polX=3.1f;
				 polY=7.2f;
				 
				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				
				 
				 polX=-0.64f;
				 polY=-1.06f;				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 ///
				 polX=2.31f;
				 polY=6.99f;
				 
				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				
				 polX=-0.95f;
				 polY=5.33f;				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 ///
				 polX=-0.56f;
				 polY=6.6f;				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 polX=-0.32f;
				 polY=2.72f;				
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0, 0f, 0,0);
				 ///
				 ///
				////dimovi
				polX=4.06f;
				polY=6.95f;
				 sir=0.64f;
				vis=0.95f;
				 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);
				 ///
					
					polX=2.43f;
					polY=6.7f;
					 sir=0.74f;
					vis=1.05f;
					 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);
					 
						polX=0.49f;
						polY=5.08f;
						 sir=0.44f;
						vis=0.85f;
						 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);
						 polX=0.85f;
							polY=6.24f;
							 sir=0.64f;
							vis=0.95f;
							 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);

		polX=11.11f;
	    polY=4.59f;
		sir=1.34f;
		vis=1.76f;
		faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);					 
							 
		 polX=0.85f;
		 polY=6.24f;
		 sir=0.44f;
		 vis=0.85f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);
		 polX=0.32f;
		 polY=6.53f;
		 sir=0.54f;
		 vis=0.85f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,8,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);				 			 	 
		 polX=0f;
		 polY=5.08f;
		 sir=0.64f;
		 vis=0.95f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,16,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);				 
		 polX=0.64f;
		 polY=1.83f;
		 sir=0.74f;
		 vis=1.05f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,4,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);		 
		 polX=0f;
		 polY=2.4f;
		 sir=0.64f;
		 vis=0.95f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,0,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);				 
		 polX=1.13f;
		 polY=2.36f;
		 sir=0.64f;
		 vis=0.95f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,12,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);				 			 	 
		 polX=0.14f;
		 polY=1.41f;
		 sir=0.74f;
		 vis=1.05f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 502,7,2,polX, polY, sir,vis,0, 0f, 0,0,4*vis);		
		 ///kuca
		 polX=10.97f;
		 polY=5.54f;
		 sir=2.08f;
		 vis=1.66f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 503,7,2,polX, polY, sir,vis,0, 0f, 0,0,0);		 
		 ///znak
		 polX=9.24f;
		 polY=4.8f;
		 sir=1.02f;
		 vis=1.2f;
		 faIgr.dodaj503DodatakNaMapuPomakSortiranja(0,0, 504,7,2,polX, polY, sir,vis,0, 0f, 0,0,0);	
		
		  /////dodavanje èlanova igrice
				float dodatak=-0.15f; 
	 	///gornji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.6f,5.47f);//
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.77f,6.24f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.68f,3.28f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,11.8f,4.66f);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.87f,6.83f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.04f,6.16f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.43f,5.69f);
	 	
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.64f,4.0f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.16f,4.23f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.94f,3.76f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.88f,2.84f);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.51f,2.81f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.71f,3.36f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.01f,0.97f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.35f,1.29f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.75f,1.05f);
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
			    ///////////////////////////donji segment/////////////////////////////////////
			    /////0
				visP=1.23f;
				sirP=0.85f;
				xPP=-0.85f;
				yPP=4.87f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				/////1
				visP=1.23f;
				sirP=0.85f;
				xPP=0f;
				yPP=4.9f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,355,++brPLijevi);
				///
			    /////2
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=0.85f;
				yPP=4.97f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 0,++brPLijevi);
				///
				  /////3
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=1.69f;
				yPP=4.9f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 5,++brPLijevi);
				///
			    /////4
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=2.54f;
				yPP=4.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 10,++brPLijevi);
				///
				 /////5
		         visP=1.38f;
				 sirP=0.85f;
			     xPP=3.39f;
				yPP=4.55f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 30,++brPLijevi);
				///
				 /////6
		         visP=1.48f;
				 sirP=0.85f;
			     xPP=4.23f;
				yPP=4.27f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 25,++brPLijevi);
				///
				 /////7
		         visP=0.78f;
				 sirP=1.06f;
			     xPP=5.08f;
				yPP=4.55f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 60,++brPLijevi);
				///
				 /////8
		         visP=0.78f;
				 sirP=1.62f;
			     xPP=5.08f;
				yPP=3.77f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 80,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.49f;
		         visP=0.18f;
			     xPP=4.97f;
				yPP=3.6f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,-brPLijevi);
				///
				 /////9
		         visP=0.67f;
				 sirP=1.41f;
			     xPP=5.47f;
				yPP=3.1f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 80,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.21f;
		         visP=0.53f;
			     xPP=4.87f;
				yPP=3.77f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,-brPLijevi);
				///
				 /////10
		         visP=0.71f;
				 sirP=0.95f;
			     xPP=5.93f;
				yPP=2.43f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 80,++brPLijevi);
				///
				 /////11
		         visP=0.85f;
				 sirP=0.95f;
			     xPP=5.93f;
				yPP=1.59f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 10,++brPLijevi);
				///
                /////////////////////////////////zadnj///////////////////////////////////////////////
				
				////////////12
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=6.88f;
				  yPP=1.41f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,10,++brPLijevi);
				///13
				 visP=1.23f;
				 sirP=0.85f;
			     xPP=7.73f;
				  yPP=1.27f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,15,++brPLijevi);
				///
				///14
				 visP=1.23f;
				 sirP=0.85f;
			     xPP=8.57f;
				  yPP=1.16f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				///15
				 visP=1.31f;
				 sirP=0.85f;
			     xPP=9.42f;
				  yPP=1.23f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,350,++brPLijevi);
				///
				 /////16
		         visP=0.85f;
				 sirP=1.09f;
			     xPP=10.27f;
				yPP=1.31f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 290,++brPLijevi);
				///
				 /////17
		         visP=0.74f;
				 sirP=0.85f;
			     xPP=10.27f;
				yPP=2.15f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 330,++brPLijevi);
				///
				 /////18
		         visP=0.74f;
				 sirP=0.92f;
			     xPP=11.11f;
				yPP=2.15f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brPLijevi);
				///
				///19
				 visP=0.85f;
				 sirP=1.31f;
			     xPP=10.69f;
				  yPP=2.89f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				 /////20
		         visP=1.16f;
				 sirP=0.85f;
			     xPP=11.08f;
				yPP=3.74f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 200,++brPLijevi);
				///
				///21
				 visP=1.48f;
				 sirP=0.85f;
			     xPP=10.23f;
				  yPP=3.74f;
				  
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,205,++brPLijevi);
				///
				///22
				 visP=1.58f;
				 sirP=0.85f;
			     xPP=9.38f;
				  yPP=4.34f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,220,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.85f;
		         visP=0.35f;
			     xPP=9.38f;
				yPP=3.99f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,-brPLijevi);
				///
				///23
				 visP=1.23f;
				 sirP=0.85f;
			     xPP=8.54f;
				  yPP=4.66f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,220,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.85f;
		         visP=0.35f;
			     xPP=8.54f;
				yPP=4.3f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,-brPLijevi);
				///
				///24
				 visP=1.38f;
				 sirP=0.85f;
			     xPP=7.69f;
				  yPP=4.76f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,200,++brPLijevi);
				///
				
				///25
				 visP=1.13f;
				 sirP=0.85f;
			     xPP=6.84f;
				  yPP=5.29f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,200,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.85f;
		         visP=0.32f;
			     xPP=6.84f;
				yPP=4.97f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,-brPLijevi);
				///
				///26
				 visP=0.64f;
				 sirP=0.78f;
			     xPP=6.07f;
				  yPP=5.82f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,260,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.53f;
		         visP=0.42f;
			     xPP=6.31f;
				yPP=5.43f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,-brPLijevi);
				///
				///27
				 visP=0.85f;
				 sirP=1.38f;
			     xPP=5.82f;
				  yPP=6.39f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				///28
				 visP=0.85f;
				 sirP=1.38f;
			     xPP=5.82f;
				  yPP=7.23f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				///29
				 visP=0.85f;
				 sirP=1.38f;
			     xPP=5.89f;
				  yPP=8.08f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,270,++brPLijevi);
				///
				///kraj
				
				 visP=0.85f;
				 sirP=1.38f;
				 xPP=5.89f;
			     yPP=8.93f;
			  
				faIgr.dodajBr204Kraj(0,0,xPP, yPP, sirP, visP,0,speed,++brPLijevi);
				////
				
				//////////////////////////////////gore//////////////////////////////////////////////////////////
				 brPLijevi=0;
			
				
				 /////1
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=0f;
				yPP=1.23f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 340,++brPLijevi);
				///
			     /////2
		         visP=1.34f;
				 sirP=0.85f;
			     xPP=0.85f;
				  yPP=1.38f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 345,++brPLijevi);
				///
			    /////3
		         visP=1.13f;
				 sirP=0.85f;
			     xPP=1.69f;
				  yPP=1.8f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 340,++brPLijevi);
				///
				 /////posebni -1
				 sirP=0.85f;
		         visP=0.28f;
			     xPP=1.69f;
				yPP=1.52f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,-brPLijevi);
				///
				  /////4
		         visP=1.13f;
				 sirP=0.85f;
			     xPP=2.54f;
				 yPP=1.91f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 340,++brPLijevi);
				///
			     /////posebni -1
				 sirP=0.85f;
		         visP=0.21f;
			     xPP=2.54f;
				yPP=1.69f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,-brPLijevi);
				///
				  /////5
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=3.39f;
				  yPP=1.91f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 350,++brPLijevi);
				///
				 /////6
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=4.23f;
				  yPP=1.98f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brPLijevi);
				///
				 /////7
		         visP=1.23f;
				 sirP=0.85f;
			     xPP=5.08f;
				  yPP=1.87f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,10,++brPLijevi);
				///
			   
				//////////////////////////////////////////////////////////////////////////////////////////////////
				visP=1.23f;
				sirP=0.85f;
				xPP=-0.85f;
				yPP=0.35f;
				
				  visP=0.85f;
					 sirP=1.23f;
				     xPP=8.93f;
					  yPP=-0.85f;
					  
					  visP=1.23f;
						 sirP=0.85f;
						 xPP=-0.85f;
					     yPP=5.36f;
				
	
		/*//LIJE GORE		
		/////0
	    visP=1.38f;
		sirP=0.85f;
	    xPP=-0.85f;
		yPP=1.13f;
		faIgr.dodajBr203RavniPutKut(0,1,xPP, yPP, sirP, visP,speed, 350,++brPLijevi);
		///
					        
		faIgr.dodajBr_16DebeliSerac(0,1,-0.85f,1.9f,0.2f);		
		faIgr.dodajBr_16DebeliSerac(0,1,-0.85f,1.9f,0.2f);		
		faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.8f);	
		
		faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.8f); */
					     
					     faIgr.dodajBr_16DebeliSerac(0,3,-0.85f,5.6f,0.5f);		
							faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.5f);					     
		////////////////////////////1. val ///////////////////////////////////////////////			     
		//lijev dolje
					 					     
		faIgr.dodajBr_5ProtivnikPocetni(0,5, -0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(0,0, -0.85f,5.6f,0.8f);	
	    faIgr.dodajBr_1Radnik(0,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(0,0,-0.85f,5.6f,0.9f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f,5.6f,0.6f);
		
		faIgr.dodajBr_5ProtivnikPocetni(0,7, -0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(0,0, -0.85f,5.6f,0.8f);	
	    faIgr.dodajBr_1Radnik(0,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(0,0,-0.85f,5.6f,0.9f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f,5.6f,0.6f);
	    
		faIgr.dodajBr_5ProtivnikPocetni(0,7, -0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(0,0, -0.85f,5.6f,0.8f);	
	    faIgr.dodajBr_1Radnik(0,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(0,0,-0.85f,5.6f,0.9f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f,5.6f,0.6f);
		faIgr.dodajBr_5ProtivnikPocetni(0,0, -0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(0,0, -0.85f,5.6f,0.8f);	
	    faIgr.dodajBr_1Radnik(0,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(0,0,-0.85f,5.6f,0.9f);
		faIgr.dodajBr_5ProtivnikPocetni(0,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(0,1,-0.85f,5.6f,0.6f);
		
		//////////////////////////////2. val /////////////////////////////////////////////////
		
		//LIJE GORE		
		/////0
	    visP=1.38f;
		sirP=0.85f;
		xPP=-0.85f;
		yPP=1.13f;
		faIgr.dodajBr203RavniPutKut(0,1,xPP, yPP, sirP, visP,speed, 0,0);
		///
							        
		/*faIgr.dodajBr_16DebeliSerac(0,1,-0.85f,1.9f,0.2f);		
		faIgr.dodajBr_16DebeliSerac(0,1,-0.85f,1.9f,0.2f);		
		faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.8f);				
	    faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_16DebeliSerac(0,0,-0.85f,1.9f,0.8f); */
		//lijev dolje
	     
	    faIgr.dodajBr_5ProtivnikPocetni(1,5,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(1,0,-0.85f,5.6f,0.7f);	
	    faIgr.dodajBr_1Radnik(1,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(1,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.85f,5.6f,0.6f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,7,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(1,0,-0.85f,5.6f,0.3f);	
	    faIgr.dodajBr_1Radnik(1,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(1,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.85f,5.6f,0.6f);
		
		faIgr.dodajBr_9ProtivnikPas(1,7,-0.85f,1.9f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(1,0,-0.85f,1.9f,0.5f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,7,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(1,0,-0.85f,5.6f,0.7f);	
	    faIgr.dodajBr_1Radnik(1,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(1,0,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.85f,5.6f,0.6f);
		///lije gore
		faIgr.dodajBr_9ProtivnikPas(1,7,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(1,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(1,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(1,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(1,0,-0.85f,1.9f,0.5f);
		
		faIgr.dodajBr_5ProtivnikPocetni(1,0,-0.85f,5.6f,0.5f);
	    faIgr.dodajBr_7ProtivnikCistacica(1,0,-0.85f,5.6f,0.3f);	
	    faIgr.dodajBr_1Radnik(1,1,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_4Reper(1,0,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_5ProtivnikPocetni(1,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(1,1,-0.85f,5.6f,0.6f);
		////////////// 3 val
		///lijevo dolje
		faIgr.dodajBr_2Sminkerica(2,5,-0.85f,5.6f,0.4f);
		faIgr.dodajBr_2Sminkerica(2,0,-0.85f,5.6f,0.7f);	
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(2,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f,5.6f,0.4f);
		faIgr.dodajBr_2Sminkerica(2,0,-0.85f,5.6f,0.5f);
		
		///lije gore
		faIgr.dodajBr_9ProtivnikPas(2,7,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,0,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(2,0,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(2,0,-0.85f,1.9f,0.5f);
		///loijevo dolje		
		faIgr.dodajBr_5ProtivnikPocetni(2,0,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(2,0,-0.85f,5.6f,0.7f);	
	    faIgr.dodajBr_1Radnik(2,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(2,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.85f,5.6f,0.6f);   
		
		faIgr.dodajBr_5ProtivnikPocetni(2,0,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(2,0,-0.85f,5.6f,0.7f);	
	    faIgr.dodajBr_1Radnik(2,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(2,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(2,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(2,1,-0.85f,5.6f,0.6f);   
		///lijevo dolje
		faIgr.dodajBr_2Sminkerica(2,5,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f,5.6f,0.7f);	
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(2,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_2Sminkerica(2,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(2,0,-0.85f,5.6f,0.7f);
				
		///lije gore
		faIgr.dodajBr_9ProtivnikPas(2,7,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(2,0,-0.85f,1.9f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(2,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(2,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(2,0,-0.85f,1.9f,0.5f);
		/////////////////////////////4 val/////////////////////////////////////////////////
		///mjrsano
		faIgr.dodajBr_2Sminkerica(3,5,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_2Sminkerica(3,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,5.6f,0.5f);
		
		faIgr.dodajBr_2Sminkerica(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(3,0,-0.85f,5.6f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,5.6f,0.7f);
		
		faIgr.dodajBr_2Sminkerica(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(3,0,-0.85f,1.9f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,5.6f,0.5f);
		
		faIgr.dodajBr_2Sminkerica(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(3,0,-0.85f,1.9f,0.8f);	
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,5.6f,0.5f);
		
		faIgr.dodajBr_2Sminkerica(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(3,0,-0.85f,1.9f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.3f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,5.6f,0.7f);
		
		faIgr.dodajBr_2Sminkerica(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_2Sminkerica(3,0,-0.85f,1.9f,0.8f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,5.6f,0.5f);
		///loijevo dolje		
		faIgr.dodajBr_5ProtivnikPocetni(3,7,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(3,0,-0.85f,5.6f,0.7f);	
	    faIgr.dodajBr_1Radnik(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(3,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.85f,5.6f,0.6f);   
		faIgr.dodajBr_5ProtivnikPocetni(3,0,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(3,0,-0.85f,5.6f,0.7f);	
		faIgr.dodajBr_1Radnik(3,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(3,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(3,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(3,1,-0.85f,5.6f,0.6f); 
		
		///mjesano
		faIgr.dodajBr_9ProtivnikPas(3,7,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(3,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(3,0,-0.85f,1.9f,0.5f);
		
		///////////////////////////////5. val //////////////////////////////////////////////
		faIgr.dodajBr_16DebeliSerac(4,3,-0.85f,5.6f,0.5f);		
		faIgr.dodajBr_16DebeliSerac(4,0,-0.85f,1.9f,0.5f);		
	
		///mjesano		
	    faIgr.dodajBr_5ProtivnikPocetni(4,17,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(4,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(4,0,-0.85f,1.9f,0.7f);
	    faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,5.6f,0.5f);
	    faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.6f);   				
		faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(4,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(4,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.6f); 
		 faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(4,1,-0.85f,1.9f,0.3f);
		faIgr.dodajBr_4Reper(4,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,5.6f,0.6f);   				
	    faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.7f);	
	    faIgr.dodajBr_1Radnik(4,1,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_4Reper(4,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(4,1,-0.85f,5.6f,0.5f);
        faIgr.dodajBr_7ProtivnikCistacica(4,0,-0.85f,1.9f,0.6f);  
        ///mjesano
		faIgr.dodajBr_9ProtivnikPas(4,7,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(4,0,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(4,0,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(4,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(4,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(4,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(4,0,-0.85f,1.9f,0.5f);
        faIgr.dodajBr_9ProtivnikPas(4,0,-0.85f,1.9f,0.5f);		
		////////////////////////////6. val///////////////////////////////////////////////////
        faIgr.dodajBr_16DebeliSerac(5,3,-0.85f,5.6f,0.3f);	
		faIgr.dodajBr_16DebeliSerac(5,0,-0.85f,5.6f,0.7f);	
		faIgr.dodajBr_16DebeliSerac(5,3,-0.85f,1.9f,0.3f);	
		faIgr.dodajBr_16DebeliSerac(5,0,-0.85f,1.9f,0.7f);	
		 ///mjesano
		faIgr.dodajBr_9ProtivnikPas(5,7,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.5f);
        faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.5f);	
        faIgr.dodajBr_5ProtivnikPocetni(5,17,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(5,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(5,0,-0.85f,1.9f,0.7f);
	    faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.5f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.6f);   				
		faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(5,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(5,0,-0.85f,1.9f,0.7f);
      ///mjesano		
        faIgr.dodajBr_16DebeliSerac(5,3,-0.85f,5.6f,0.3f);	
		faIgr.dodajBr_16DebeliSerac(5,0,-0.85f,5.6f,0.7f);	
		faIgr.dodajBr_16DebeliSerac(5,3,-0.85f,1.9f,0.3f);	
		faIgr.dodajBr_16DebeliSerac(5,0,-0.85f,1.9f,0.7f);	
		
	    faIgr.dodajBr_5ProtivnikPocetni(5,13,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(5,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(5,0,-0.85f,1.9f,0.7f);
	    faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.5f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.6f);   				
		faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(5,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_4Reper(5,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.6f); 
		 faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.1f);
	    faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.7f);	
		faIgr.dodajBr_1Radnik(5,1,-0.85f,1.9f,0.3f);
		faIgr.dodajBr_4Reper(5,0,-0.85f,5.6f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,5.6f,0.6f);   				
	    faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.3f);
		faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.7f);	
	    faIgr.dodajBr_1Radnik(5,1,-0.85f,5.6f,0.3f);
	    faIgr.dodajBr_4Reper(5,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_5ProtivnikPocetni(5,1,-0.85f,5.6f,0.5f);
        faIgr.dodajBr_7ProtivnikCistacica(5,0,-0.85f,1.9f,0.6f);  
        faIgr.dodajBr_9ProtivnikPas(5,7,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.5f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.6f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.2f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.7f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(5,1,-0.85f,1.9f,0.4f);
		faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.5f);
        faIgr.dodajBr_9ProtivnikPas(5,0,-0.85f,1.9f,0.5f);	
		/////////////////////////////////////////////////////////////////////////////////////
				
				
				
		
		
				
		
		
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8zapaljeno0selo,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			SpriteHendler dodatak=new SpriteHendler(1);
			dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8zapaljena0suma,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(501, dodatak);
			SpriteHendler dodatak1=new SpriteHendler(1);  
			dodatak1.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8dim0drveca,opts),17, 1,9);//ispaljenje
			faIgr.dodajSprite(502, dodatak1);
			SpriteHendler dodatak2=new SpriteHendler(1);  
			dodatak2.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8krcma,opts),1, 1,0);//ispaljenje
			faIgr.dodajSprite(503, dodatak2);
			SpriteHendler dodatak3=new SpriteHendler(1);  
			dodatak3.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak8faza8znak0krcma,opts),1, 1,0);//ispaljenje
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
