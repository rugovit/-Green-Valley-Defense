

package com.rugovit.igrica.engine.ui.levels;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.Taskbar;
import com.rugovit.igrica.engine.ui.UIManager;

public class FazaSumaPocetna extends Activity {
	
	public float postotakLodiranjaFaze=0;
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
			BitmapFactory.decodeResource(getResources(), R.drawable.staza8suma0pocetna, opts);
			int pozVis = opts.outHeight;
			int pozSir = opts.outWidth;
			opts.inDither=false;
			
			opts.inJustDecodeBounds = false;
			
		/////povecanje zbog veæeg ekrana
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
	
		
		
		/*
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
		}*/
		//opts.inScaled =false;
		opts.inSampleSize=smanjenje;
	    NativeHeapAllocatedSize= Debug.getNativeHeapAllocatedSize()/1024;
		 GlobalAllocSize=Debug.getGlobalAllocSize()/1024;
		 ThreadAllocSize= Debug.getThreadAllocSize()/1024;                              
	       opts.inPreferredConfig = Bitmap.Config.ARGB_4444;   
	    //////////inicijalizacija gamelogica i uimanager pozadine
	    /////////ui manager*/
	        faIgr=new FazeIgre(2,200,xPiksCm,yPiksCm,tezina,bun.getInt(IgricaActivity.brFaze),activity);
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
		    faIgr.dodajParametreFazeIzDB(IDKoristeneFaze,bun.getInt(IgricaActivity.brojZvijezdica),bun.getInt(IgricaActivity.tezina));
            
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
			@Override
			public void loadSlika(){
				
				
				  ///////// ZVUK
		 	    soundPool = new SoundPool(IgricaActivity.maxBrojZvukova,AudioManager.STREAM_MUSIC, 0);
		 		 ///upute toranj
					SpriteHendler uputeTornjevi=new SpriteHendler(3);
					uputeTornjevi.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.upustva8tornjevi8izbornik,opts),1, 1,0);
					uputeTornjevi.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.upute8abiliti8zaokruzeno,opts),7, 1,8);
					uputeTornjevi.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.upustva8tornjevi8taktika,opts),1, 1,0);
					task.inicijalizirajTornjeviUpustva(uputeTornjevi);
		 	       
			        this.faIgr.lodirajResurse(activity, soundPool,opts,gLog,uiMan);
			}
			
			@Override
			public void loadMetoda() {


				// TODO Auto-generated method stub
			    ///////		
				getWindow().setFormat(PixelFormat.RGBA_4444);
				opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
				 postotakLodiranjaFaze=5;
				 uiMan.nacrtajIzvanThreadno();
				 float polX=5.64f;
				 float polY=3.46f;
				 float sir=3.56f;
				 float vis=3.28f;
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=0f;
				 polY=7.14f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=10.94f;
				 polY=7.44f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=6.31f;
				 polY=5.36f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=9.1f;
				 polY=5.75f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=-0.74f;
				 polY=-1.83f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=10.23f;
				 polY=-1.94f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=14.68f;
				 polY=-0.56f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 polX=11.43f;
				 polY=0.18f;			
				 faIgr.dodaj501DodatakNaMapu(0,0, 501,0,2,polX, polY, sir,vis,0,0,0,0);
				 
				 sir=0.85f;
				 vis=0.64f;
				 polX=3.07f;
				 polY=0.11f;			
				 faIgr.dodaj502DodatakNaMapuRandomPustanje(0,0, 502,0,2,polX, polY, sir,vis,0,0,0,0,35);
				 
	
				 
				
		  /////dodavanje èlanova igrice
	 	///gornji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.58f,4.59f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,10.58f,2.82f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,9.31f,1.43f);
	 	faIgr.dodajBr200ToranjEmbrio(0,0,7.56f,1.26f);
	 	//srednji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.16f,1.06f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,8.21f,3.43f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,6.6f,3.13f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.19f,2.91f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.63f,2.83f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,3.35f,4.98f);
        //donji
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.44f,2.24f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.28f,4.08f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,1.28f,6.31f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,2.83f,6.95f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,4.46f,7.23f);
	 	
	 	faIgr.dodajBr200ToranjEmbrio(0,0,5.24f,5.32f);
	 

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
			    //0
			    sirP=0.85f;
				visP=1.23f;				
				xPP=13.49f;
				yPP=5.3f;
			
			    faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brP);
			    //1
				sirP=0.85f;
				visP=1.24f;				
				xPP=12.64f;
				yPP=5.3f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brP);
				//2
				sirP=0.85f;
				visP=1.24f;				
				xPP=11.79f;
				yPP=5.19f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brP);
				//3
				sirP=0.85f;
				visP=1.24f;				
				xPP=10.947f;
				yPP=5.09f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,180,++brP);
				//4
				sirP=0.85f;
				visP=1.24f;				
				xPP=10.09f;
				yPP=4.98f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,160,++brP);
				//5
				visP=0.85f;
				sirP=1.24f;				
				xPP=8.85f;
				yPP=5.6f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 90,++brP);
				//gornji dio///////////////////////////////////////////////////////////////
				brPGornji=brP;
				//6
				sirP=1.24f;
				visP=0.85f;				
				xPP=8.85f;
				yPP=4.76f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 110,++brP);
				//7
				sirP=1.24f;
				visP=0.85f;				
				xPP=8.74f;
				yPP=3.91f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 90,++brP);
				//8
				sirP=1.24f;
				visP=0.85f;				
				xPP=8.63f;
				yPP=3.06f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 90,++brP);
				//9
				sirP=0.85f;
				visP=1.24f;				
				xPP=9.13f;
				yPP=1.83f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 170,++brP);
				//10
				sirP=0.85f;
				visP=1.24f;				
				xPP=8.28f;
				yPP=1.83f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 150,++brP);
				//11
				sirP=0.85f;
				visP=1.24f;				
				xPP=7.43f;
				yPP=1.75f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 170,++brP);
				//12
				sirP=0.85f;
				visP=1.24f;				
				xPP=6.58f;
				yPP=1.61f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 170,++brP);
				//13
				sirP=0.85f;
				visP=1.24f;				
				xPP=5.73f;
				yPP=1.49f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 170,++brP);
				//14
				sirP=0.85f;
				visP=1.24f;				
				xPP=4.88f;
				yPP=1.35f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 170,++brP);
				//15
				sirP=0.85f;
				visP=1.24f;				
				xPP=4.03f;
				yPP=1.21f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brP);
				//16
				sirP=0.85f;
				visP=1.24f;				
				xPP=3.18f;
				yPP=1.29f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 180,++brP);
				//17
				sirP=1.24f;
				visP=0.85f;				
				xPP=1.95f;
				yPP=1.13f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brP);
				//18
				sirP=1.24f;
				visP=0.85f;				
				xPP=1.95f;
				yPP=1.96f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 210,++brP);
				//19
				sirP=1.24f;
				visP=0.85f;				
				xPP=1.86f;
				yPP=2.81f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brP);
				//20
				sirP=1.24f;
				visP=0.85f;				
				xPP=1.76f;
				yPP=3.66f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brP);
				//21
				sirP=1.24f;
				visP=0.85f;				
				xPP=1.67f;
				yPP=4.51f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brP);
			
				//22
				sirP=0.85f;
				visP=1.24f;				
				xPP=1.45f;
				yPP=5.36f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 0,++brP);
				//23
				sirP=0.85f;
				visP=1.24f;				
				xPP=2.29f;
				yPP=5.36f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 340,++brP);
				//24
				sirP=0.85f;
				visP=1.24f;				
				xPP=3.13f;
				yPP=5.49f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 0,++brP);
				//25
				sirP=0.85f;
				visP=1.24f;				
				xPP=3.98f;
				yPP=5.59f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed,0,++brP);
				
				//26
				sirP=1.24f;
				visP=0.85f;				
				xPP=4.83f;
				yPP=5.44f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed,270,++brP);
				//27
				sirP=1.24f;
				visP=0.84f;				
				xPP=4.83f;
				yPP=6.29f;
				faIgr.dodajBr205OkukaKut(0,0,xPP, yPP, sirP, visP,speed, 300,++brP);
				//28
				sirP=1.24f;
				visP=0.85f;				
				xPP=4.92f;
				yPP=7.14f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brP);
				//29
				sirP=1.24f;
				visP=0.85f;				
				xPP=5.01f;
				yPP=7.99f;
				faIgr.dodajBr203RavniPutKut(0,0,xPP, yPP, sirP, visP,speed, 270,++brP);
				//kraj
				sirP=1.23f;
				visP=0.85f;				
				xPP=5.01f;
				yPP=8.84f;
				faIgr.dodajBr204Kraj(0,0,xPP, yPP, sirP, visP,0,speed,++brP);
				////////////////////////////////////////////////////////////////////////////////
			    
				faIgr.dodajBr_5ProtivnikPocetni(1,00,13.8f,5.9f,0.8f);		
				faIgr.dodajBr_5ProtivnikPocetni(0,3, 13.8f,5.9f,0.4f);
				
				faIgr.dodajBr_5ProtivnikPocetni(0,7,13.8f,5.9f,0.8f);	
				faIgr.dodajBr_5ProtivnikPocetni(0,3, 13.8f,5.9f,0.4f);
				
				faIgr.dodajBr_5ProtivnikPocetni(0,7, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_5ProtivnikPocetni(0,1, 13.8f,5.9f,0.2f);
				
				
				faIgr.dodajBr_7ProtivnikCistacica(0,7, 13.8f,5.9f,0.3f);
				faIgr.dodajBr_5ProtivnikPocetni(0,3, 13.8f,5.9f,0.4f);
				
				faIgr.dodajBr_5ProtivnikPocetni(0,7, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(0,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(0,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_7ProtivnikCistacica(0,7, 13.8f,5.9f,0.3f);
				faIgr.dodajBr_5ProtivnikPocetni(0,3, 13.8f,5.9f,0.4f);			
				faIgr.dodajBr_5ProtivnikPocetni(0,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(0,7, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, 13.8f,5.9f,0.3f);			
				faIgr.dodajBr_5ProtivnikPocetni(0,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(0,1, 13.8f,5.9f,0.3f);
			
				//2 VAL
				faIgr.dodajBr_5ProtivnikPocetni(1,7, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,3, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,4, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				
				faIgr.dodajBr_5ProtivnikPocetni(1,8, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);			
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);				
				faIgr.dodajBr_5ProtivnikPocetni(1,1, 13.8f,5.9f,0.4f);
				faIgr.dodajBr_7ProtivnikCistacica(1,1, 13.8f,5.9f,0.3f);
				  ////////////poseban metoda za lod slika
				 loadSlika();

												 Log.d("game onCreate", "memorije nativ poslije lodiranje=" + Debug.getNativeHeapAllocatedSize()/1024);
										    	 Log.d("game onCreate", "memorije GlobalAllocSize poslije lodiranje=" + Debug.getGlobalAllocSize()/1024);
										    	 Log.d("game onCreate", "memorije ThreadAllocSize poslije lodiranje=" + Debug.getThreadAllocSize()/1024);												
												    postotakLodiranjaFaze=100;
												    uiMan.nacrtajIzvanThreadno();
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
			pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.staza8suma0pocetna,opts);
			uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
			   SpriteHendler dodatak;
			  dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.dodatak0faza8sumica1,opts),1, 1,0);//ispaljenje
			
			 faIgr.dodajSprite(501, dodatak);
			 dodatak=new SpriteHendler(1);  
				dodatak.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.doadatak8faza8koza,opts),8, 1,8);//ispaljenje
						 
			 faIgr.dodajSprite(502, dodatak);
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
