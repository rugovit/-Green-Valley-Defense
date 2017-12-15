package com.rugovit.igrica;

import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.Uvod;
import com.rugovit.igrica.engine.ui.elements.IzbornikUniverzalni;
import com.rugovit.igrica.engine.ui.elements.MusicManager;

import rugovit.igrica.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class IgricaActivity extends Activity {
    /** Called when the activity is first created. */
	/////////OSNOVNE VARIJABLE
	
	public static String imeIgre="igrica";// trebalo bi biti zapisano malim slovima
	//////////////////
	public static String brFaze="brfaze";
	////imena zvukova/////
	public static final String  zvukBooo="zvukbooo";
	public static final String  zvukRobotski1="zvukrobotski1";
	public static final String  zvukRobotski2="zvukrobotski2";
	public static final String  zvukRobotski3="zvukrobotski3";
	public static final String  zvukToranjKonstrukcija1="toranjkonstrukcijazvuk1";
	public static final String  zvukToranjKonstrukcija2="toranjkonstrukcijazvuk2";
	public static final String  zvukToranjKonstrukcija3="toranjkonstrukcijazvuk3";
	public static final String  zvukToranjKonstrukcija4="toranjkonstrukcijazvuk4";
	public static final String  zvukToranjKonstrukcija5="toranjkonstrukcijazvuk5";
	public static final String  zvukToranjKonstrukcija6="toranjkonstrukcijazvuk6";
	public static final String  zvukZivotManje="zivotmanjezvuk";
	public static final String  zvukPocetakVala="pocetakvalazvuk";
	public static final String  zvukLetMeteora="letmeteora";
	public static final String  zvukTruba1="truba1";
	//////////////////////
	public static  FrameLayout trenutacniFrame;
	public static int maxBrojZvukova=10;
	public static final int apiLevel=Build.VERSION.SDK_INT;
	public static final String  achievementNoOneShellPas="noOneShellPas";
	public static final String  achievementDeathFromAbove="deathFromAbove";
	 ////////////////////STATICKE VARIJABLE ZA BAZU PODATAKA//////////////////
	////placeni objekti
	public static final String imePlacenogObjektaNaInternetuFazaDodatna1="green0valley0defense8extra0level1";
	public static final String PlacenObjektExtraFaza1="extrafaza1";
	/////
	public static final String  IDPlacenogObjekta="idplacenogobjekta";
	public static final String  listaPlacenihElemenata="listaPlacenihElemenata";
	public static final String  tipIgre="tipigre";
	public static final String  listaAchievementsa="listaAchievementsa";
	public static final String  IDAchievementsa="lidachievementsa";
	public static final String zadnjiKoristenSlot="zadnjiKoristenSlot";
	public static final String glavniDB="glavniDB";
	public static final String listaSlotova="listaSlotova";
	public static final String listaFaza="listaFaza";
	public static final String listaBodovaUpgradesa="listabodovaupgradsa";
	public static final String bodoviUpgradesa="bodoviupgradsa";
	public static final String brojUpgradesa="brojupgradsa";//1- znci otkljucana - 0- zakljucana 2-pređena
	public static final String IDUpgradesaSlotPlusBrUpgrada="idUpgradesaSlotPlusBrUpgrada";
	public static final String listaUpgradesa="listaUpgradesa";
	public static final String listaKoristenihFaza="listaKoristenihFaza";
	public static final String IDSlota="idSlota";
	public static final String IDFaze="idFaze";
	public static final String IDKoristeneFaze="idKorFaze";
	public static final String redniBrojKoristeneFazeUSlotu="rednibrojkoristenefazeuslotu";
	//public static final String otkljucana="otkljucana";
	public static final String tezina="tezina";
	public static final String stanjeFaze="stanjeFaze";//1 otkljucana, 0 zakljucana
	public static final String brojZvijezdica="brojZvijezdica";
	public static final String imeSlota="imeSlota";
	public static final String stanjeSlota="stanjeSlota";
	/////////////////////////////////////////////////////////////////////////
//	public static MusicManager musicManager= new MusicManager(); 
	////////////////////////////////////////////////////////////////////////
	private UIManager uiMan;
	private IzbornikUniverzalni izPoc;
	private IzbornikUniverzalni izLoad;
	 private  SoundPool soundPool;
	 private MediaPlayer muzika;
	 private int id;
	public int efekSir,efekVis;
	public static Intent getOpenFacebookIntent(Context context) {

		   try {
		    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/593644974104368"));
		   } catch (Exception e) {
		    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/pages/Green-Valley-Defence/593644974104368"));
		   }
		}
	public void stvoriMapu(){
		 final Intent intent=new Intent("android.intent.action.pokreni_mapu");
		 // Intent intent =new Intent("android.intent.action.pokreni_igricu");
		/*
		final Context cont=this;
		   Thread timer= new Thread()
			{
				public void run()
				{
					try{
						  soundPool = new SoundPool(2,AudioManager.STREAM_MUSIC, 0);
						   id=soundPool.load(IgricaActivity.this, R.drawable.zvuk8danzig, 1);
 						   sleep(1000);// mora èekati da se lodira inace nesvira, metodq koja provjerava jeli lodirano nije podrzamo do api8
						
					} catch(InterruptedException e){
						e.printStackTrace();
						
					}
					soundPool.play(id, 0.99f, 0.99f, 1, 0, 1f);
					try{ 
						   sleep(5000);
					} catch(InterruptedException e){
						e.printStackTrace();
					}
				   // IgricaActivity.musicManager.stanjeIgre(cont,1,-1, -1);
					soundPool.release();// instantno ubija soundpool tako da se nestavlja odmah iza pozivanja play
					soundPool=null;
					  Bundle bundle= new Bundle();
					  bundle.putInt("sir",efekSir );
					  bundle.putInt("vis", efekVis);
					  intent.putExtras(bundle);
					IgricaActivity.this.startActivity(intent);
					IgricaActivity.this.finish(); 
					finish();
			   }
				
				
			};
			timer.start();
			
			*/
			 Bundle bundle= new Bundle();
			  bundle.putInt("sir",efekSir );
			  bundle.putInt("vis", efekVis);
			  intent.putExtras(bundle);
		   	IgricaActivity.this.startActivity(intent);
			IgricaActivity.this.finish(); 
		
	}

	@Override
   public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
  	   requestWindowFeature(Window.FEATURE_NO_TITLE);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
       
       
       

       
       
       
       
       
      // muzika=MediaPlayer.create(this, R.raw.danzig);//nije htijelo radit kda je bilo izvan onCreate-a
	  // muzika.start();
       /////////////////////PARAMETRI EKRANA//////////////////////////////////////////////
   ////parametri ekrana
	    DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int yp=metrics.heightPixels;
		int xp=metrics.widthPixels;
		float xdpi=metrics.xdpi;
		float ydpi=metrics.ydpi;
		float density=metrics.densityDpi; 
		float xPiksCm=xdpi/2.54f;
		float yPiksCm=ydpi/2.54f;
		 /////odredivanje smanjenja
       long maxMemory=0;
       int smanjenje=1;
       // odredivanje smanjenja
       	      Runtime rt = Runtime.getRuntime();
                 maxMemory = rt.maxMemory()/1000000;
                 if(maxMemory<24) smanjenje=4;
                 else if(maxMemory>=18&&maxMemory<24) smanjenje=3;
                 else  if(maxMemory>=24&&maxMemory<45) smanjenje=2;
                 else smanjenje=1;
      
       
		BitmapFactory.Options 
		opts = new BitmapFactory.Options();// stavlja da ne mjenja velicinu
		///uzimanje parametara pozadine
		  opts.inJustDecodeBounds = true;// omogucava dobijanje parametara slike bez uèitavanja u memoriju
		  BitmapFactory.decodeResource(getResources(), R.drawable.mapa, opts);
		  int pozVis = opts.outHeight;
		  int pozSir = opts.outWidth;
		  opts.inJustDecodeBounds = false;
	///pozadina    
	float xPozCm=12.6f;// vazna brojka jer se u odnosu na nju poztavljaju ostali objekti na ekran
	float yPozCm= 8.4f;
	/////povecanje zbog veæeg ekrana
	if(pozSir<xp) {
		xPiksCm=xp/xPozCm;//povecava tako da ispuni ekran 
	}
	else if(xPozCm*xPiksCm<xp)xPiksCm=xp/xPozCm;//povecava tako da ispuni ekran 
	if(pozVis<yp) {
		yPiksCm=yp/yPozCm;// povecava tako da ispuni ekran
	}
	else if(yPozCm*yPiksCm<yp)yPiksCm=yp/yPozCm;//povecava tako da ispuni ekran 
	opts.inScaled =true;
	opts.inSampleSize=smanjenje;
    Bitmap pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.mapa8tekstura,opts); 
    ///////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
       //////////////////////////////////////////////////////////////////////////////////////
      /////BAZA PODATAKA/////////////////////////////////////////////////////////////////
      //////////////////////////////////////////////////////////////////////
   	    SQLiteDatabase bazaPodataka;
        bazaPodataka= this.openOrCreateDatabase(this.glavniDB,this.MODE_PRIVATE, null);
        //zadnji koristen slot
        ///////lista placenih elemenata 
      
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.  listaPlacenihElemenata+
	               " ("+this. IDPlacenogObjekta+" TEXT NOT NULL PRIMARY KEY  )");
        //lista achivmentsa/////
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.listaAchievementsa+
	               " ("+this.IDSlota+" TEXT , "+this.IDAchievementsa+" TEXT NOT NULL PRIMARY KEY  )");
        //////////////////////////  
        /*
        ////test
        bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  
          		this.listaAchievementsa +"('"+this.IDSlota+"','"+this.IDAchievementsa+"')"+ // ne ubacujem tezinu na pocetku
   	                     " Values ('"+ "slot1" +"','"+ achievementNoOneShellPas +"');");
        bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  
          		this.listaAchievementsa +"('"+this.IDSlota+"','"+this.IDAchievementsa+"')"+ // ne ubacujem tezinu na pocetku
   	                     " Values ('"+ "slot1" +"','"+ this.achievementDeathFromAbove +"');");
        /////////
        */
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.zadnjiKoristenSlot +
	               " ("+this.IDSlota+" TEXT)");
    	//stvaranje listi
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.listaBodovaUpgradesa+
	               " ("+this.IDSlota+" TEXT NOT NULL PRIMARY KEY ,"+this.bodoviUpgradesa+" INTEGER)");
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.listaUpgradesa +
	               " ("+this.IDUpgradesaSlotPlusBrUpgrada+" TEXT NOT NULL PRIMARY KEY ,"+this.IDSlota+" TEXT , "+this.brojUpgradesa+" INTEGER)");
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.listaFaza +
	               " ("+this.IDFaze+" TEXT NOT NULL PRIMARY KEY )");
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.listaSlotova +
	               " ("+this.IDSlota+" TEXT NOT NULL PRIMARY KEY ,"+this.imeSlota+" TEXT,"+this.stanjeSlota+" TEXT,"+this.tezina+" INTEGER)");// stanje slota 0-nekoristen,1-koristen
        bazaPodataka.execSQL("CREATE TABLE IF NOT EXISTS " +
	    		   this.listaKoristenihFaza +
	               " ("+this.IDKoristeneFaze+" TEXT NOT NULL PRIMARY KEY , "+this.IDSlota+" TEXT , "+this.IDFaze +" TEXT, "+this.stanjeFaze+" TEXT,"+this.brojZvijezdica+" INTEGER,"+this.redniBrojKoristeneFazeUSlotu+" INTEGER)");
      /////DODAVANJE ELEMENATA STATICNO////////////////////////////
         //faze///////////////////
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  /// prva faza postoji
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza0');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  /// prva faza postoji
       		this.listaFaza +"('"+this.IDFaze+"')"+
	                     " Values ('faza1');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  /// druga faza postoji
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza2');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  /// treca faza postoji
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza3');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  //
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza4');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza5');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza6');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza7');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza8');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza9');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza10');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza11');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza12');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza13');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza14');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza15');");
        bazaPodataka.execSQL("INSERT OR IGNORE INTO " +  
           		this.listaFaza +"('"+this.IDFaze+"')"+
    	                     " Values ('faza16');");
         //slotovi////////////////
        bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  /// prvi slot
           		this.listaSlotova +"('"+this.IDSlota+"','"+this.imeSlota+"','"+this.stanjeSlota+"')"+ // ne ubacujem tezinu na pocetku
    	                     " Values ('slot1','empty','0');");
        bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  /// prvi slot
           		this.listaSlotova +"('"+this.IDSlota+"','"+this.imeSlota+"','"+this.stanjeSlota+"')"+ // ne ubacujem tezinu na pocetku
    	                     " Values ('slot2','empty','0');");
        bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  /// prvi slot
           		this.listaSlotova +"('"+this.IDSlota+"','"+this.imeSlota+"','"+this.stanjeSlota+"')"+ // ne ubacujem tezinu na pocetku
    	                     " Values ('slot3','empty','0');");
        bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  /// prvi slot
           		this.listaSlotova +"('"+this.IDSlota+"','"+this.imeSlota+"','"+this.stanjeSlota+"')"+ // ne ubacujem tezinu na pocetku
    	                     " Values ('slot4','empty','0');");
      //////DODAVANJE ELEMENATA DINAMICNO///////////////////////////////sluzi samo da nemoram rucno ubacivati u svaki slot sve faze, ne mjenja atribite faze samo ih updata u listu od slota
        Cursor cur1=bazaPodataka.query(this.listaSlotova, null,null, null, null, null, null);
	    cur1.moveToFirst();
	         String IDSlota;
	         String IDFaze;
	         String IDKoristeneFaze;
	         
			while(cur1.isAfterLast()==false){ 	
	               Cursor cur2=bazaPodataka.query(this.listaFaza, null,null,null, null, null, null);
	               cur2.moveToFirst();  
	               boolean tekPoceo=true;
	               int redniBrojFazeUslotu=0;
	               while(cur2.isAfterLast()==false){ 	
	            	   IDSlota=cur1.getString(cur1.getColumnIndex(this.IDSlota));
	            	   IDFaze=cur2.getString(cur2.getColumnIndex(this.IDFaze));	
	            	   IDKoristeneFaze=IDSlota+IDFaze;
	            	   if(tekPoceo){// da otkljuca prvu fazu a daljnje ce se autmatski
	            		   tekPoceo=false;
	            		   bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  
		                      		this.listaKoristenihFaza +"('"+this.IDKoristeneFaze+"','"+this.IDSlota+"','"+this.IDFaze+"','"+stanjeFaze+"','"+this.brojZvijezdica+"','"+this.redniBrojKoristeneFazeUSlotu+"')"+ // ne ubacujem tezinu na pocetku
		               	                     " Values ('"+ IDKoristeneFaze +"','"+ IDSlota +"','"+ IDFaze +"','"+ 1 +"','"+ 0 +"','"+ redniBrojFazeUslotu +"');");
	            	   }
	            	   else  bazaPodataka.execSQL(" INSERT OR IGNORE INTO " +  
	                      		this.listaKoristenihFaza +"('"+this.IDKoristeneFaze+"','"+this.IDSlota+"','"+this.IDFaze+"','"+stanjeFaze+"','"+this.brojZvijezdica+"','"+this.redniBrojKoristeneFazeUSlotu+"')"+ // ne ubacujem tezinu na pocetku
          	                     " Values ('"+ IDKoristeneFaze +"','"+ IDSlota +"','"+ IDFaze +"','"+ 0 +"','"+ 0 +"','"+ redniBrojFazeUslotu +"');");
	                /*
	                 * STANJA FAZE:
	                 *     1-znaci da je otkljucana i nepredena
	                 *     2-znaci da je zakljucana i nepredena
	                 *     3-znaci da je pređena i otkljkucana
	                 * 
	                 * */
	             
	         	    cur2.moveToNext();// za pomicanje u bazi podataka  
	         	   redniBrojFazeUslotu++;
	                     }
	               cur2.close();
	             cur1.moveToNext();// za pomicanje u bazi podataka
               }
			 cur1.close();
			  bazaPodataka.close();
			    //////////////// 
      ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	  ////////////UI MANAGER
/*	  uiMan=new UIManager(this,null,14,null,xPiksCm, yPiksCm){

		@Override
		public void izvrsiNakonPrvogTakta() {
			// TODO Auto-generated method stub
			efekSir=uiMan.can.getWidth();
			efekVis=uiMan.can.getHeight();
			uiMan.ugasiGlavnuPetlju();
		}
		  
	  };*/
	//  uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
	//  setContentView(uiMan);
	//  ukljuciNovuIgru();
	  
	 // pokreniPocIzbornik();
	  //pokreniLoadIzbornik();
	  ////////////////////////////////////////////////////////////////////////////////////////////////////////////  
	  
	  
	   Uvod uvod=new Uvod(this,14,null,this);
	   //uvod.pokreniGlavnuPetlju();
	   
	   setContentView(uvod);
    }
  
   
	@Override
	protected void onPause(){
		super.onPause();
		MusicManager.stopAndRelease(1500);
		
		/*gLog.pause();
		uiMan.stop();*/
		//faIgr.reciklirajSveSpriteove();
		//uiMan.reciklirajPozadinu();
	}
	
	@Override
	protected void onRestart() {

		super.onRestart();
	}

	@Override
	protected void onResume() {
	
		super.onResume();

	      
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MusicManager.stopAndRelease(1500);
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		MusicManager.stopAndRelease(1500);
	
		
		super.onStop();
	
	}
    @Override
	/////////sprijeèava promjenu  orjentacije 
	public void onConfigurationChanged(Configuration newConfig) {// iz nekog razloga ponovno je pokretao ovuj activiti kada poème sljedeæi
	    super.onConfigurationChanged(newConfig);
	    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}