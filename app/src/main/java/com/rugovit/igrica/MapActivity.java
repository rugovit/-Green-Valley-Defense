package com.rugovit.igrica;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.PurchaseResponse;
import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.IzbornikUniverzalni;
import com.rugovit.igrica.engine.ui.elements.MusicManager;
import com.rugovit.igrica.engine.ui.levels.Faza;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

import rugovit.igrica.R;
import android.graphics.Canvas;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MapActivity extends Activity{
	/////////////////velicine za konstante odnose na ekranu
	private float velXDugMutePostoEkrana=15;
	private float velXDugNazadPostoEkrana=15;
	private float velXDugUpgradePostoEkrana=20;
	private float velXDugKarakteristikePostoEkrana=20;
	private float  velXUpgradeProzora=90;
	private float  velXKarakteristikeProzora=90;
	
	////////////////////////////////////////////////////////
	
	private float pomakKarakteristikaManjiX,pomakKarakteristikaManjiY;
	private float  marginaKarakteristika=0;
	private float sirinaUpgradeBotuna;
	private float xZadKorFaz,yZadKorFaz;
	private int efekSir;
	private int efekVis;
	private LinkedList<String> listaPlacenihObjekata;
	private boolean pokreceFazuNeIskljucujZvuk=false;
	/*public float postotakLodiranjaFaze=0;
	public boolean poceoCrtatiUvod=false;
	public static MapActivity mapAct;*/
	public Typeface font,fontZaPocetneBotune;
	private LinkedList tempUpgradeListaNekoristenih;
	private SoundPool soundPool;
	private boolean pokrenutResetIzbornik=false, postojiUpgrade=false, zavrsenaAnimacijaGlavnog=false, pokrenutUpgradeBotun=false, pokrenutAchivmentBotun=false;
	public void stvoriNovuFazu(int i,String IDKoristeneFaze,String IDSlota,int brZvjezdica, int tezina,int tipIgre){
		 ///budle dio
		pokreceFazuNeIskljucujZvuk=true;
		  Bundle bundle= new Bundle();
		   bundle.putString(IgricaActivity.IDSlota,IDSlota);
		   bundle.putString(IgricaActivity.IDKoristeneFaze, IDKoristeneFaze);
		   bundle.putInt(IgricaActivity.brojZvijezdica, brZvjezdica);
		   bundle.putInt(IgricaActivity.tezina, tezina);
		   bundle.putInt("sir",efekSir );
		   bundle.putInt("vis", efekVis);
		   bundle.putInt(IgricaActivity.brFaze, i);
		   bundle.putInt(IgricaActivity.tipIgre, tipIgre);
		/////
		Intent intent=null;
		reciklirajSe();
		/*uiMan.stop();
		this.sprIzb.reciklirajSveBitmapove();
		this.fazaSpr.reciklirajSveBitmapove();
		 uiMan.reciklirajPozadinu();
		uiMan.nacrtajTaksturu();
	
		 uiMan.unlockCanvasAndPost();
		uiMan.reciklirajTeksturu();*/
		//if(i==1) intent =new Intent("android.intent.action.pokreni_igricu");
		if(i==6){
			intent =new Intent("android.intent.action.fazarijecniprolaz"); 
		}
		else if(i==0){
			intent =new Intent("android.intent.action.fazasumapocetna");
		}
		else if(i==7){
			intent =new Intent("android.intent.action.fazapoljejabuka");
		}
		else if(i==1){
			intent =new Intent("android.intent.action.fazagradribarski");
		}
		
		else if(i==3){
			intent =new Intent("android.intent.action.fazabrdovita");
		}
		else if(i==2){
			intent =new Intent("android.intent.action.fazasumskiprolaz");
		}
		else if(i==4){
			intent =new Intent("android.intent.action.fazamost");
		}
		else if(i==5){
			intent =new Intent("android.intent.action.fazagradmost");
		}
		else if(i==6){
			intent =new Intent("android.intent.action.fazaracvanje");
		}
		else if(i==8){
			intent =new Intent("android.intent.action.fazazapaljenoselo");
		}
		else if(i==9){
			intent =new Intent("android.intent.action.fazazapaljengrad");
		}
		else if(i==10){
			intent =new Intent("android.intent.action.fazaulazubrdo");
		}
		else if(i==12){
			intent =new Intent("android.intent.action.fazasupljina");
		}
		else if(i==11){
			intent =new Intent("android.intent.action.fazaborovasuma");
		}
		else if(i==13){
			intent =new Intent("android.intent.action.fazavrhplanine");
		}
		else if(i==14){
			intent =new Intent("android.intent.action.fazaplacenasuma");
		}
		else if(i==15){
			intent =new Intent("android.intent.action.fazaplacenazvijezda");
		} 
		else if(i==16){
			intent =new Intent("android.intent.action.fazaplacenapiramida");
		} 
		intent.putExtras(bundle);
		//intent.setAction(Intent.ACTION_MAIN);
		//intent.addCategory(Intent.CATEGORY_LAUNCHER);
		//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		
		startActivity(intent);
		finish();
		
	}
	public void reciklirajSe(){
		pozadina=pocetnaPozadina=tekstura=null;
		if(uiMan!=null){
			uiMan.reciklirajPozadinu();
			uiMan.reciklirajTeksturu();
			uiMan.makniSveObjekteMultyThread();
			uiMan.stop();
		}
		if(	facebookBotun!=null){
			facebookBotun.reciklirajSveBitmapove();
			facebookBotun=null;
		}
		if(	sprBack!=null){
			sprBack.reciklirajSveBitmapove();
			sprBack=null;
		}
		if(	karakteristikaBotuni!=null){
			karakteristikaBotuni.reciklirajSveBitmapove();
			karakteristikaBotuni=null;
		}
		if(karakteristikaKasarna!=null){
			karakteristikaKasarna.reciklirajSveBitmapove();
			karakteristikaKasarna=null;
		}
		if(karakteristikaMinobacac!=null){
			karakteristikaMinobacac.reciklirajSveBitmapove();
			karakteristikaMinobacac=null;
		}
		if(karakteristikaStrijelci!=null){
			karakteristikaStrijelci.reciklirajSveBitmapove();
			karakteristikaStrijelci=null;
		}
		if(karakteristikaAlkemicar!=null){
			karakteristikaAlkemicar.reciklirajSveBitmapove();
			karakteristikaAlkemicar=null;
		}
		if(sprAchieBotun!=null){
			sprAchieBotun.reciklirajSveBitmapove();
			sprAchieBotun=null;
		}
		if(sprReset!=null){
			sprReset.reciklirajSveBitmapove();
			sprReset=null;
		}
		if(sprBrValoraVnjski!=null){
			sprBrValoraVnjski.reciklirajSveBitmapove();
			sprBrValoraVnjski=null;
		}
		if(sprKvacicaKrug!=null){
			sprKvacicaKrug.reciklirajSveBitmapove();
			sprKvacicaKrug=null;
		}
		if(sprUpgSpor!=null){
			sprUpgSpor.reciklirajSveBitmapove();
			sprUpgSpor=null;
		}
		if(sprAchieGlavniPr!=null){
			sprAchieGlavniPr.reciklirajSveBitmapove();
			sprAchieGlavniPr=null;
		}
		if(sprUpgGlavniPr!=null){
			sprUpgGlavniPr.reciklirajSveBitmapove();
			sprUpgGlavniPr=null;
		}
		if(sprBotUpg!=null){
			sprBotUpg.reciklirajSveBitmapove();
			sprBotUpg=null;
		}
		if(sprAchieBotun!=null){
			sprAchieBotun.reciklirajSveBitmapove();
			sprAchieBotun=null;
		}
		if(sprIzb!=null){
			sprIzb.reciklirajSveBitmapove();
			sprIzb=null;
		}
		if(fazaSpr!=null) {
			fazaSpr.reciklirajSveBitmapove();
			fazaSpr=null;
		}
		if(loadIzb!=null) {
			loadIzb.reciklirajSveBitmapove();
			loadIzb=null;
		}
		if(pocIzb!=null) {
			pocIzb.reciklirajSveBitmapove();
			pocIzb=null;
		}
		this.finish();
		izbLoad=null;
		izbFight=null;
		faza1=null;
	}
    private boolean pceoSaGlazbom=false;
	private UIManager uiMan;
	public SpriteHendler load,karakteristikaStrijelci,karakteristikaMinobacac,karakteristikaKasarna,karakteristikaAlkemicar,karakteristikaBotuni,facebookBotun;
	private SpriteHendler sprAchiIkone,sprAchieBotun,sprAchieGlavniPr,sprIzb, pocIzb, loadIzb, sprIzbTez,sprBotUpg,sprUpgGlavniPr,sprUpgSpor,sprKvacicaKrug,sprBrValoraVnjski,sprReset,sprMute,sprBack;
	private BitmapFactory.Options opts;
	private IzbornikUniverzalni izbLoad, izbFight, izbBotunKarakteristike,izbFacebook;//,//izPoc;
	private Faza faza1;
	private float xPozCm,yPozCm;
	private String idZadnjegSlota="";
	private int tezina=0;
	private LinkedList<String> listaAchievementsa;
	private HashMap<String,Integer> listaStanjaSlotova;
	private HashMap<String,String> listaImenaSlotova;
	private HashMap<String,String> koristeneFazePoSlotovima;
	private HashMap<String,Integer> koristeneFazeBrZvjezdica;
	private HashMap<String,Integer> koristeneFazeUpgradi;
	private HashMap<String,Integer> koristeneFazeTezina;
	private HashMap<String,Integer> slotoviTezina;
	private HashMap<String,Integer> koristeneFazeStanje;
	private HashMap<String,Integer> listaUpgradsi;
	private HashMap<String,Integer> brUpgradeBodova;
	private Bitmap pozadina,pocetnaPozadina, tekstura;
	private float xPiksCm, yPiksCm;
	private int zvukMacevi;
	SpriteHendler fazaSpr;
	private Activity activity;
	////////////AMAZON
	@Override
	
    public void onCreate(Bundle savedInstanceState) {

		
        super.onCreate(savedInstanceState); 
        activity=this;
        /////random glazba
    	Random generator=new Random();
    	MusicManager.brGlazbeZaPustitiStaza=1+generator.nextInt(1);
        /////////////
        /////////////////////////////AMAZON DIO////////////////////////////////////////////////////////////////////////////////
        AMAZONProdajaListener temList= new AMAZONProdajaListener(this.getApplicationContext()){
        	@Override
       	 public void  kupljenoNesto(PurchaseResponse response){
        		dodajElementeMapeUUIIPokreni();
        	}
        	
        };
	       PurchasingService.registerListener(this.getApplicationContext(),temList);
	       Log.i(AMAZONProdajaListener.TAG, "onCreate: sandbox mode is:" + PurchasingService.IS_SANDBOX_MODE);
	       
	       
	       PurchasingService.getPurchaseUpdates(true);
	       PurchasingService.getUserData();
	       /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
	  
        
		 
		 
		 
		 
		 
        //IgricaActivity.musicManager.stanjeIgre(this,1,-1, -1);
       /*font= Typeface.createFromAsset(
			    getAssets(), 
			    "fonts/king.ttf");*/
        font= Typeface.createFromAsset(
			    getAssets(), 
			    "fonts/boogaloo-regular.ttf");
        fontZaPocetneBotune= Typeface.createFromAsset(
			    getAssets(), 
			    "fonts/annstone.ttf");
    ////orientacija 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		///puni ekran
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		////////+
	       
        Bundle bun= getIntent().getExtras();
        this.efekVis=bun.getInt("vis");
        this.efekSir=bun.getInt("sir");
        boolean pokreniSaMape=bun.getBoolean("pokriSaMape");
        this.soundPool=new SoundPool(20,AudioManager.STREAM_MUSIC, 0);
        /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
         setContentView(R.layout.main);
        botun1=(Button)findViewById(R.id.button1);
        botun1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent("android.intent.action.pokreni_igricu"));
			}
		});*/
        ////parametri ekrana
	    DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int yp=metrics.heightPixels;
		int xp=metrics.widthPixels;
		float xdpi=metrics.xdpi;
		float ydpi=metrics.ydpi;
		float density=metrics.densityDpi; 
		 xPiksCm=xdpi/2.54f;
		 yPiksCm=ydpi/2.54f;
		 /////odredivanje smanjenja
        long maxMemory=0;
        int smanjenje=1;
        // odredivanje smanjenja
        	      Runtime rt = Runtime.getRuntime();
                  maxMemory = rt.maxMemory()/1000000;
                  if(maxMemory<18) smanjenje=5;
                  else if(maxMemory>=18&&maxMemory<24) smanjenje=4;
                  else  if(maxMemory>=24&&maxMemory<35) smanjenje=3;
                  else  if(maxMemory>=35&&maxMemory<45) smanjenje=2;
                  else smanjenje=1;
       
        
		BitmapFactory.Options opts = new BitmapFactory.Options();// stavlja da ne mjenja velicinu
		///uzimanje parametara pozadine
		  opts.inJustDecodeBounds = true;// omogucava dobijanje parametara slike bez uèitavanja u memoriju
		  BitmapFactory.decodeResource(getResources(), R.drawable.mapa, opts);
		  int pozVis = opts.outHeight;
		  int pozSir = opts.outWidth;
		  opts.inJustDecodeBounds = false;
	///pozadina    
	 xPozCm=12.6f;// vazna brojka jer se u odnosu na nju poztavljaju ostali objekti na ekran
	 yPozCm= 8.4f;
	/////povecanje zbog veæeg ekrana
	/*if(pozSir<xp) {
		xPiksCm=xp/xPozCm;//povecava tako da ispuni ekran 
	}
	else if(xPozCm*xPiksCm<xp)xPiksCm=xp/xPozCm;//povecava tako da ispuni ekran 
	if(pozVis<yp) {
		yPiksCm=yp/yPozCm;// povecava tako da ispuni ekran
	}*/
	/////povecanje zbog veæeg ekrana
	/*if(pozSir<efekSir) {
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
	}
	else if(yPozCm*yPiksCm<yp)yPiksCm=yp/yPozCm;//povecava tako da ispuni ekran */
	//opts.inScaled =false;
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

	opts.inPreferredConfig = Bitmap.Config.RGB_565;
	opts.inSampleSize=smanjenje;
	
	if(smanjenje>4){
		    opts.inSampleSize=4;
		    }
       pozadina=BitmapFactory.decodeResource(getResources(), R.drawable.mapa,opts); 
       opts.inSampleSize=smanjenje;
      	tekstura=BitmapFactory.decodeResource(getResources(), R.drawable.mapa8tekstura,opts); 
      
	 		
		///////////////////////////tea<yyyyyyyy 
      	
      	/////////////////////////
      	karakteristikaStrijelci=new SpriteHendler(2);
      	karakteristikaMinobacac=new SpriteHendler(2);
      	karakteristikaKasarna=new SpriteHendler(2);
      	karakteristikaAlkemicar=new SpriteHendler(2);
    	karakteristikaBotuni=new SpriteHendler(2,20,soundPool);
    	load=new SpriteHendler(1);
    	
       	karakteristikaStrijelci.dodajNoviSprite(null, 0,0, 0);
     	karakteristikaStrijelci.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.karakteristike0strijelci,opts), 0,0, 0);
     	
      	karakteristikaMinobacac.dodajNoviSprite(null, 0,0, 0);
     	karakteristikaMinobacac.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.karakteristike0minobacac,opts), 0,0, 0);
     	
     	karakteristikaKasarna.dodajNoviSprite(null, 0,0, 0);
     	karakteristikaKasarna.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.karakteristike0kasarna,opts), 0,0, 0);
     	
     	karakteristikaAlkemicar.dodajNoviSprite(null, 0,0, 0);
     	karakteristikaAlkemicar.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.karakteristike0alkemicar,opts), 0,0, 0);
     	
      	karakteristikaBotuni.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.karakteristike8botuni,opts), 4,1, 0);
      	karakteristikaBotuni.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8achieve9gavniprozor,opts),1, 1,0);
      	int izvlacenje=soundPool.load(this, R.raw.zvuk8izvlacenje0load0veci,1);
      	int uvlacenje=soundPool.load(this, R.raw.zvuk8uvlacenje0load0veci,1);
      	
      	
      	
      	karakteristikaBotuni.dodajZvukISincSaSlRandomSamostalni(izvlacenje,1, 1f,120,100,100);
      	karakteristikaBotuni.dodajZvukISincSaSlRandomSamostalni(uvlacenje,2,1f,120,100,100);
      	
      	
      	
     	load.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.load,opts),1, 1,0);
    ////UIManager
        uiMan=new UIManager(this,14,xPiksCm, yPiksCm,efekSir,efekVis,"MapActivity");/*{
        	boolean tekPoceo=true;
			RectF recCrt=null, rectLoad=null, rectLoad2=null;
			Paint paint=null,paint2=null;
			float xLoad;
			float yLoad;
			float vis;
			float sir;
			@Override 
			public void nacrtajDodatnoIznadGlavnePetlje(Canvas can, float fps,EventManager eMan,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
				
			
				
				if(poceoCrtatiUvod){
						
					if(tekPoceo){
						xLoad=can.getWidth()/4;
						yLoad=3*can.getHeight()/4;
						vis=can.getHeight()/5;
						sir=3*can.getWidth()/4;
						paint=new Paint();
						paint.setARGB(255, 204, 26, 2);
						paint.setStyle(Paint.Style.FILL);
						paint2=new Paint();
						paint2.setARGB(255, 204, 26, 2);
						paint2.setStyle(Paint.Style.STROKE);
						paint2.setStrokeWidth(vis/3);
						uiMan.daliDaProvjeravamPovlacenje(false);
					    recCrt=new RectF();
					    rectLoad=new RectF();
					    rectLoad2=new RectF();
						recCrt.set(0, 0, can.getWidth(), can.getHeight());// stavlja recCrt na cijeli ekran
						rectLoad.set(xLoad, yLoad, xLoad+postotakLodiranjaFaze*sir/100, yLoad +vis);
						rectLoad2.set(xLoad, yLoad, xLoad+sir, yLoad +vis);
						tekPoceo=false;
						uiMan.setFPSGlavnePetlje(1);
						
						
					}
					sprUvoda.nacrtajSprite(can, 0, 0, 0, recCrt);
					rectLoad.set(xLoad, yLoad, xLoad+postotakLodiranjaFaze*sir/100, yLoad +vis);
					can.drawRect(rectLoad, paint);
					can.drawOval(rectLoad2, paint2);
					
					
					
					if(postotakLodiranjaFaze>=100){
						uiMan.stop();
						uiMan=null;
						sprUvoda.reciklirajSveBitmapove();
						postotakLodiranjaFaze=0;
						rugovit.igrica.MapActivity.mapAct.finish();
						rugovit.igrica.MapActivity.mapAct=null;
						poceoCrtatiUvod=false;
					}
				}
				
				
			} 
				// TODO Auto-generated method stub
				
			
        	
        };*/
     // izb  back
	 	  sprBack=new SpriteHendler(2);
	 	 sprBack.dodajNoviSprite(null,1, 1,6);
	 	  sprBack.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbonik8mapa0nazad,opts),1, 1,6);
	 	 
	    /////SPRITEOVI ZA FAZE
	    fazaSpr=new SpriteHendler(4,1,soundPool);  
		fazaSpr.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.mapa8macevi,opts),10,1,10);
		fazaSpr.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.pobjeda8pljeskanje,opts),5,1,10);
		//dodavanje zvuka
	 	zvukMacevi=soundPool.load(this, R.raw.zvuk8mac0rez3, 1);
	 	fazaSpr.dodajZvukISincSaSl( zvukMacevi, 0, 0, 1, 100, 100);
		/////////
	    /////// izbornik  za fazu
	    sprIzb=new SpriteHendler(2,20,soundPool);
		sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8faza8dugmici,opts),9, 1,18);
		sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8faza,opts),14, 0,18);
		sprIzb.dodajZvukISincSaSlRandomSamostalni(soundPool.load(this, R.raw.zvuk8faza0pocetak0izbornik,1),1, 1f,120,100,70);
 	
		/////
		// Faza faza1=new Faza( xPiksCm*2.9f,yPiksCm*6.0f, xPiksCm*0.8f, yPiksCm*0.8f,izbFaz,fazaSpr,this,"faza1");// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica
	 	//otkljucajSveFaze("slot2");
	 	lodirajIzDB();
	 	//br valora
	 	this.sprReset=new SpriteHendler(2);
	 	sprReset.dodajNoviSprite(null,1, 1,6);
	 	sprReset.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrades8reset,opts),1, 1,6);
	 	//br valora
	 	this.sprBrValoraVnjski=new SpriteHendler(1);
	 	sprBrValoraVnjski.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgraddes8brvalora0vanjski,opts),1, 1,6);
	 	
	 	//kvacica krug
	 	sprKvacicaKrug=new SpriteHendler(2);
	 	sprKvacicaKrug.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrade8botuni0kvacica,opts),1, 1,6);
	 	sprKvacicaKrug.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrade8botuni0zaokruzeno,opts),1, 1,6);
	     // izb mute botun
	 	  this.sprMute=new SpriteHendler(2);
	 	 this.sprMute.dodajNoviSprite(null,0, 0,0);
	 	this.sprMute.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.botun8mute,opts),2, 1,0);
	 // izb upg botun
         facebookBotun=new SpriteHendler(2);
         facebookBotun.dodajNoviSprite(null,1, 1,0);
         facebookBotun.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8facebook,opts),1, 1,0);
	 	 
	 	// izb upg botun
	 	  sprBotUpg=new SpriteHendler(2);
	 	 sprBotUpg.dodajNoviSprite(null,1, 1,6);
	 	  sprBotUpg.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrades8glavni0botun,opts),1, 1,6);
	 	 

		 /// iz achive botun sprAchieBotun
	 	 sprAchieBotun=new SpriteHendler(2);
	 	sprAchieBotun.dodajNoviSprite(null,1, 1,6);
	 	sprAchieBotun.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8karakteristike0botun,opts),1, 1,0);
	 	  //izb upg sporedni
	 	 
	 	 sprUpgSpor=new SpriteHendler(2,20,soundPool);
	 	sprUpgSpor.dodajNoviSprite(null,1, 5,6);
	 	sprUpgSpor.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrades8sporedni0prozor,opts),1, 1,6);
	 	sprUpgSpor.dodajZvukISincSaSlRandomSamostalni(soundPool.load(this, R.raw.zvuk8izvlacenje0load0manji, 1),1,1f,120,100,80);
	 	sprUpgSpor.dodajZvukISincSaSlRandomSamostalni(soundPool.load(this, R.raw.zvuk8uvlacenje0load0manji, 1),2,1f,120,100,80);
	 	  
	 	//izb achivments glavni prozor
	 	sprAchieGlavniPr=new SpriteHendler(2);
	 //	sprAchieGlavniPr.dodajNoviSprite(null,1, 5,0);
	 	//sprAchieGlavniPr.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8achieve9gavniprozor,opts),1, 1,0);
	 	//izb upg glavni prozor
	 	 sprUpgGlavniPr=new SpriteHendler(2,20,soundPool);
	 	 sprUpgGlavniPr.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrade8botuni,opts),1, 5,0);
	 	 sprUpgGlavniPr.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.upgrades8glavni0prozor,opts),1, 1,0);
	 	 sprUpgGlavniPr.dodajZvukISincSaSlRandomSamostalni(izvlacenje,1, 1f,120,100,80);
	 	 sprUpgGlavniPr.dodajZvukISincSaSlRandomSamostalni(uvlacenje,2, 1f,120,100,80);
		 //izb tezine
	 	 int papir1=soundPool.load(this, R.raw.zvuk8load8glavni1,1);
	 	 int papir2=soundPool.load(this, R.raw.zvuk8load8glavni2,1);		 
	 	 Bitmap load=BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8load,opts);
		 sprIzbTez=new SpriteHendler(2,20,this.soundPool);
		 sprIzbTez.dodajNoviSprite(null,1, 1,6);
		 sprIzbTez.dodajNoviSprite(load,1, 1,6);
		 sprIzbTez.dodajZvukISincSaSlRandomSamostalni(papir1,1, 1f,120,100,70);
		 sprIzbTez.dodajZvukISincSaSlRandomSamostalni(papir2,2, 1f,120,100,70);
		 //////////load izbornik
		  this.loadIzb=new SpriteHendler(2,20,this.soundPool);
		  loadIzb.dodajNoviSprite(null,1, 1,6);
		  loadIzb.dodajNoviSprite(load,1, 1,6);
		  loadIzb.dodajZvukISincSaSlRandomSamostalni(papir1,1, 1f,120,100,70);
		  loadIzb.dodajZvukISincSaSlRandomSamostalni(papir2,2,1f,120,100,70);
		 //////pocetak izbornik
		
		 pocIzb=new SpriteHendler(3);
		 pocIzb.dodajNoviSprite(null,1, 4,0);
		 
		 pocetnaPozadina=BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8pocetni,opts); 
		 
		 //uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
	    if(!pokreniSaMape) {        
	    	//dodajElementeMapeUUIIPokreni();
	    	pokreniPocIzbornik();
	    	
	    }
	    else {
	    	//pokreniPocIzbornik();
	    	dodajElementeMapeUUIIPokreni();
	    	//this.izPoc.univerzalniIzvrsitelj(1);
	    }
		stvoriMuteBotun();
		// pokreniPocIzbornik();
	    
		 setContentView(uiMan);
		 
    }
	private void ucitajPodatkeUSistem(String IdSlota){
		 SQLiteDatabase bazaPodataka;
		     
		    bazaPodataka= uiMan.context.openOrCreateDatabase(IgricaActivity.glavniDB,uiMan.context.MODE_PRIVATE, null);
		    bazaPodataka.delete(IgricaActivity.zadnjiKoristenSlot, null, null);
		    bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  
	        		 IgricaActivity.zadnjiKoristenSlot +"('"+IgricaActivity.IDSlota+"')"+ // ne ubacujem tezinu na pocetku
	     	                     " Values ('"+IdSlota+"');");
		bazaPodataka.close();
		lodirajIzDB();
		tezina=slotoviTezina.get(IdSlota);
		idZadnjegSlota=IdSlota;
	}
    private void dodajElementeMapeUUIIPokreni(){
		//uiMan.postaviPomakCanvasaRelativno(this.faza1.getX(), this.faza1.getY());
	//	uiMan.makniObjekt(faza1);
	//	uiMan.makniObjekt(izbFaz);
	 	   //izPoc=new IzbornikUniverzalni(null,uiMan,2,1,302){
    
   	 uiMan.makniSveObjekteMultyThread();
	 stvoriMuteBotun();
    	pokrenutResetIzbornik=zavrsenaAnimacijaGlavnog=pokrenutUpgradeBotun=pokrenutAchivmentBotun=false;
    	 lodirajkKupljeneElemente();
    	 stvoriFazeIzDB();     
		/////////////////////
    	 uiMan.postaviPomakCanvasaApsolutno(xZadKorFaz-this.efekSir/2, yZadKorFaz-this.efekVis/2);     
		 uiMan.stvoriTeksturu(tekstura, xPozCm, yPozCm,52);
		 uiMan.stvoriPozadinuCm(pozadina, xPozCm, yPozCm);
		 stvoriUpgradeBotun();
		 stvoriBackBotun();
		 stvoriFacebookBotun();
		 stvoriAchievementsBotun();
		  if(this.listaPlacenihObjekata.contains(IgricaActivity.imePlacenogObjektaNaInternetuFazaDodatna1)){
			  stvoriIndikatorKupovine ();
		  }
		// stvoriAchievementsBotun();
		 uiMan.postaviTempUniverzalniIzbornik(null);
		 MusicManager.stanjeIgre(this,2,-1, -1);
		 pceoSaGlazbom=true;
		
		 stvoriIndikatorTezine();
	   
		//uiMan.pokreniGlavnuPetlju();
	}

	private void credits(){}
	private 	IzbornikUniverzalni  stvoriIzbornikSuportDeveloper(){

		IzbornikUniverzalni izbor=new IzbornikUniverzalni( this.loadIzb,uiMan,6,2,302){
			 GameEvent geKut=new GameEvent(this);
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 GameEvent geRazmo=new GameEvent(this);
    		 GameEvent ge=new GameEvent(this);
    		 RectF rec=new RectF();
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    			
    			  rec.set(this.dajMiRectCijelogProzora());
    			  rec.offsetTo(rec.left, efekVis);
    			  top=efekVis-dajMiRectCijelogProzora().top;
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  uiMan.iskljuciTouchEvente();
    			  ge.jesamLiZiv=true;
    			  geKut.jesamLiZiv=true;
    			  geRazmo.jesamLiZiv=true;
    			  }
    			if(!translacijaGotova){
    				translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.3f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska( geKut, -90, 90,0.3f);
    			}
    			 tekPoc= sprIzbTez.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,0.5f, rec, null,false);
  		    	 if(tekPoc) uiMan.ukljuciTouchEvente();	
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    				  uiMan.iskljuciTouchEvente();	
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;
        			  ge.jesamLiZiv=true;
        			  geKut.jesamLiZiv=true;
        			  geRazmo.jesamLiZiv=true;
    			      }
    			  if(!translacijaGotova){
    				  translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.3f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska(geKut,0, -90,0.3f);
      			  }
    			  tekPoc= sprIzbTez.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,0.5f, rec, null,false);
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			   uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
	 			
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==11||brTP==9){
					this.pokreniZavrsnuAnimaciju();
					String requestId = PurchasingService.purchase(IgricaActivity.imePlacenogObjektaNaInternetuFazaDodatna1).toString();
				/*	uiMan.makniObjekt(this);
					uiMan.postaviTempUniverzalniIzbornik(null);
				
				    uiMan.makniObjekt(poz);
					ucitajPodatkeUSistem(IdSlota);
					dodajElementeMapeUUIIPokreni();
					
					uiMan.makniObjekt(izbLoad);
				    uiMan.makniObjekt(izbFight);					  
				    izbFight=null;
			        izbLoad=null;*/
					
				}
                else if (brTP==12||brTP==10){// mice ovaj izbornik i pokrece pocetni
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
	   
	
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(200,5,97,1);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		 //  izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   TextPaint tp2=new TextPaint();
		   tp2.set(tp);
		   tp2.setARGB(200, 51,49, 76);
		   TextPaint tp3=new TextPaint();
		   tp3.set(tp);
		   tp3.setARGB(200,136,22, 4);
		   izbor.postaviTextDugmica(0, "You like my game?",tp2);
		   izbor.postaviTextDugmica(2, "Support me and ",tp2);
		   izbor.postaviTextDugmica(4, "get extra stages!",tp2);
		   izbor.postaviTextDugmica(8, "Sure",tp);
		   izbor.postaviTextDugmica(9, " No you",tp3);
		   izbor.postaviTextDugmica(11, "  suck!",tp3);

		  // izbor.postaviTextDugmica(2, "<< Back",tp);
		   float sirPolja=(efekSir-3*efekSir/10)/2;
		   float visPolja= (efekVis-3*efekSir/10)/6;
		   float razmakY= 2*visPolja/10;
		   float razmakX= 2*visPolja/10;
		   float margY= 5*visPolja/10;
		   float margX= 8*visPolja/10;
		   float desnaMargina=sirPolja/4;
           izbor.postaviStatickiIzbornik();
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix(0, margX,2*margY,2*margY);
	//	   izbor.postaviLijeviVrhProzoraPix((efekSir/2)-(sirPolja-razmakX*2-margX*2), (efekVis-visPolja*6-razmakY*6-margY*2)/2);
		   izbor.postaviLijeviVrhProzoraPix((efekSir/9)+(razmakX),razmakY+margY);
		   izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		
	   return 	izbor ;
	} 
	private void pokreniIzborTezineizbornikIPokreniMapu(IzbornikUniverzalni pozivatelj){ 
		final IzbornikUniverzalni poz=pozivatelj;
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprIzbTez,uiMan,3,1,302){
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 GameEvent ge=new GameEvent(this);
    		 RectF rec=new RectF();
    		 float vrhProz,lijeviRubProz;
    		 GameEvent geKut=new GameEvent(this);
    		 GameEvent geRazmo=new GameEvent(this);
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 this.getGlavniSprite().pustiZvukSamostalno(1);
    			  uiMan.iskljuciTouchEvente();	
    			  rec.set(this.dajMiRectCijelogProzora());
    			  vrhProz=rec.top;
    			  lijeviRubProz=rec.left;
    			  rec.set(  rec.left, efekVis,   rec.right, efekVis+rec.height());
    			  top=efekVis-vrhProz;
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  ge.jesamLiZiv=true;
    			  }
    			if(!translacijaGotova){
    				translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.5f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska(geKut,-90, 90,0.5f);
    			}
    			 tekPoc= sprIzbTez.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo ,geKut,1, 0, 10, 10,0.7f, rec, null,false);
    			 if(tekPoc){
    				 uiMan.ukljuciTouchEvente();	
    			 }
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    					 this.getGlavniSprite().pustiZvukSamostalno(2);
    				  uiMan.iskljuciTouchEvente();
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;
        			  ge.jesamLiZiv=true;
        			  }
    			  if(!translacijaGotova){
      				translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.3f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska(geKut,0, -90,0.3f);
      			  }
    			  tekPoc= sprIzbTez.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10 ,0.5f, rec, null,false);
    			  
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
	 				uiMan.makniObjekt(this);
	 				pokreniPocIzbornik();
	 				
	 				uiMan.postaviTempUniverzalniIzbornik(poz);
	 				 uiMan.ukljuciTouchEvente();
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					    tezina=1;
					    SQLiteDatabase bazaPodataka;
					    bazaPodataka= uiMan.context.openOrCreateDatabase(IgricaActivity.glavniDB,uiMan.context.MODE_PRIVATE, null);
					         Cursor cur1=bazaPodataka.query(IgricaActivity.listaSlotova, null,null, null, null, null, null);
					         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
					        		 IgricaActivity.listaSlotova +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.imeSlota+"','"+IgricaActivity.stanjeSlota+"','"+IgricaActivity.tezina+"')"+ // ne ubacujem tezinu na pocetku
     	                     " Values ('slot1','Normal','1','1');");
					       cur1.close();
					    bazaPodataka.close();
					   // idZadnjegSlota="slot1";// ovaj izbornik se pokrece samo pri prom pokretaju igre tako da je uvijek slot1
					    ucitajPodatkeUSistem("slot1");
					    dodajElementeMapeUUIIPokreni();
					    uiMan.postaviTempUniverzalniIzbornik(null);
					    uiMan.makniObjekt(izbLoad);
					    uiMan.makniObjekt(izbFight);
					    uiMan.makniObjekt(this);
					    izbFight=null;
				        izbLoad=null;  
				}
				else if(brTP==2){
					    tezina=2;
	
					    SQLiteDatabase bazaPodataka;
					    bazaPodataka= uiMan.context.openOrCreateDatabase(IgricaActivity.glavniDB,uiMan.context.MODE_PRIVATE, null);
					         Cursor cur1=bazaPodataka.query(IgricaActivity.listaSlotova, null,null, null, null, null, null);
					         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
					        		 IgricaActivity.listaSlotova +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.imeSlota+"','"+IgricaActivity.stanjeSlota+"','"+IgricaActivity.tezina+"')"+ // ne ubacujem tezinu na pocetku
					     	                     " Values ('slot1','Hard','1','2');");
					         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
					        		 IgricaActivity.zadnjiKoristenSlot +"('"+IgricaActivity.IDSlota+"')"+ // ne ubacujem tezinu na pocetku
					     	                     " Values ('slot1');");
					         cur1.close();
					    bazaPodataka.close();
					    ucitajPodatkeUSistem("slot1");
					    dodajElementeMapeUUIIPokreni();
					    uiMan.postaviTempUniverzalniIzbornik(izbLoad);					   
					    uiMan.makniObjekt(this);
					   
					    uiMan.makniObjekt(izbLoad);
					    uiMan.makniObjekt(izbFight);					  
					    izbFight=null;
				        izbLoad=null;
				}
			
                else if (brTP==3){// mice ovaj izbornik i pokrece pocetni
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
	    
	
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		  tp.setARGB(250,112, 0,0);
		   tp.setStyle(Style.FILL);
		   TextPaint tp2=new TextPaint();
		   tp2.setAntiAlias(true);
		   tp2.setTypeface(font);
		  tp2.setARGB(250, 56, 48,79);
		   tp2.setStyle(Style.FILL);
		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   izbor.postaviTextDugmica(0, "Normal",tp2);
		   izbor.postaviTextDugmica(1, "Hard",tp2);
		   izbor.postaviTextDugmica(2, "<< Back",tp);
	
		   float visPolja= efekVis/9;
		  // float sirPolja=4*efekSir/7;
	
		   float razmakX=0;
		   float razmakY= 2*visPolja/10;
		  
		   float desnaMargina=2*visPolja;
		   float sirPolja=3*visPolja+5*razmakY;
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix(desnaMargina,desnaMargina, desnaMargina,desnaMargina);
		   izbor.postaviNaslovIzbornikaPix("Start:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
		   izbor.postaviLijeviVrhProzoraPix((efekSir-sirPolja-desnaMargina*2)/2, (efekVis-visPolja*3-razmakY*4-2*desnaMargina)/2);
		   izbor.pokreniMojIzbornik(null);
		   izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   uiMan.setOznacenSam(izbor);
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izbor,3);
	}
	private void pokreniStartThisSavedGameIzbornik(IzbornikUniverzalni pozivatelj,String IDSlota){
		final IzbornikUniverzalni poz=pozivatelj;
		final String IdSlota=IDSlota;
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprIzbTez,uiMan,1,2,302){
			 GameEvent geKut=new GameEvent(this);
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 GameEvent geRazmo=new GameEvent(this);
    		 GameEvent ge=new GameEvent(this);
    		 RectF rec=new RectF();
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 this.getGlavniSprite().pustiZvukSamostalno(1);
    			  rec.set(this.dajMiRectCijelogProzora());
    			  rec.offsetTo(rec.left, efekVis);
    			  top=efekVis-dajMiRectCijelogProzora().top;
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  uiMan.iskljuciTouchEvente();
    			  ge.jesamLiZiv=true;
    			  geKut.jesamLiZiv=true;
    			  geRazmo.jesamLiZiv=true;
    			  }
    			if(!translacijaGotova){
    				translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.3f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska( geKut, -90, 90,0.3f);
    			}
    			 tekPoc= sprIzbTez.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,0.5f, rec, null,false);
  		    	 if(tekPoc) uiMan.ukljuciTouchEvente();	
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    					 this.getGlavniSprite().pustiZvukSamostalno(2);
    				  uiMan.iskljuciTouchEvente();	
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;
        			  ge.jesamLiZiv=true;
        			  geKut.jesamLiZiv=true;
        			  geRazmo.jesamLiZiv=true;
    			      }
    			  if(!translacijaGotova){
    				  translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.3f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska(geKut,0, -90,0.3f);
      			  }
    			  tekPoc= sprIzbTez.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,0.5f, rec, null,false);
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			  uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
	 				if(uiMan.dajMiTempOznacenObject()==this) {
	 					uiMan.setOznacenSam(poz);// samo ako je ovaj još uvijek ozna�?en ina�?e je kliknuto na neko dugme od predhodnog i pokrenut taj izbornik
                        uiMan.postaviTempUniverzalniIzbornik(poz);
                     }
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					uiMan.makniObjekt(this);
					uiMan.postaviTempUniverzalniIzbornik(null);
				
				    uiMan.makniObjekt(poz);
					ucitajPodatkeUSistem(IdSlota);
					dodajElementeMapeUUIIPokreni();
					
					uiMan.makniObjekt(izbLoad);
				    uiMan.makniObjekt(izbFight);					  
				    izbFight=null;
			        izbLoad=null;
					
				}
                else if (brTP==2){// mice ovaj izbornik i pokrece pocetni
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
	   
	
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(250, 56, 48,79);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		 //  izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   TextPaint tp2=new TextPaint();
		   tp2.set(tp);
		   tp2.setARGB(250, 134, 1, 1);
		   izbor.postaviTextDugmica(0, "Yes",tp2);
		   izbor.postaviTextDugmica(1, "No",tp2);
		  // izbor.postaviTextDugmica(2, "<< Back",tp);
		   float sirPolja=2*efekSir/7;
		   float visPolja= efekVis/9;
		   float razmakY=0;
		   float razmakX= 2*visPolja/10;
		   float desnaMargina=sirPolja/4;
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix( sirPolja/4,sirPolja/4, visPolja*2, visPolja/2);
		   izbor.postaviLijeviVrhProzoraPix((efekSir-sirPolja-razmakX*2)/2, (efekVis-visPolja*5-razmakY*5)/2);
		   izbor.postaviNaslovIzbornikaPix("Start Game:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
		   izbor.pokreniMojIzbornik(null);
		   izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   uiMan.setOznacenSam(izbor);
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izbor,3);
	} 
	private void pokreniIzborTezineLoadizbornik(IzbornikUniverzalni pozivatelj,String IDSlota){ 
		final IzbornikUniverzalni poz=pozivatelj;
		final String IdSlota=IDSlota;
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprIzbTez,uiMan,3,1,302){
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 GameEvent geKut=new GameEvent(this);
    		 GameEvent ge=new GameEvent(this);
    		 RectF rec=new RectF();
    		 GameEvent geRazmo=new GameEvent(this);
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 this.getGlavniSprite().pustiZvukSamostalno(1);
    			  rec.set(this.dajMiRectCijelogProzora());
    			  rec.offsetTo(rec.left, efekVis);
    			  top=efekVis-dajMiRectCijelogProzora().top;
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  uiMan.setOznacenSam(this);
    			  uiMan.iskljuciTouchEvente();	
    			  ge.jesamLiZiv=true;
    			  geKut.jesamLiZiv=true;
    			  geRazmo.jesamLiZiv=true;
    			  }
    			if(!translacijaGotova){
    				translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.3f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska( geKut,-90, 90,0.3f);
    			}
    			 tekPoc= sprIzbTez.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut,1, 0, 10, 10,0.5f, rec, null,false);
    			 if(tekPoc)uiMan.ukljuciTouchEvente();	
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    					 this.getGlavniSprite().pustiZvukSamostalno(2);
    				  uiMan.iskljuciTouchEvente();	
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;
        			  ge.jesamLiZiv=true;
        			  geKut.jesamLiZiv=true;
        			  geRazmo.jesamLiZiv=true;
        			  }
    			  if(!translacijaGotova){
      				translacijaGotova=  sprIzbTez.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.3f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova=  sprIzbTez.animacijaSlaganjeRotacijaVremenska(geKut,0, -90,0.3f);
      			  }
    			  tekPoc= sprIzbTez.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo, geKut,1, 0, 10, 10,0.5f, rec, null,false);
    			  
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			    uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
	 				
	 				if(uiMan.dajMiTempOznacenObject()==this) {uiMan.setOznacenSam(poz);// samo ako je ovaj još uvijek ozna�?en ina�?e je kliknuto na neko dugme od predhodnog i pokrenut taj izbornik
	 				                                         uiMan.postaviTempUniverzalniIzbornik(poz);}
    		  }
    		  
    		  public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
    			  this.pokreniZavrsnuAnimaciju();
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
				    tezina=1;
				    SQLiteDatabase bazaPodataka;
				    bazaPodataka= uiMan.context.openOrCreateDatabase(IgricaActivity.glavniDB,uiMan.context.MODE_PRIVATE, null);
				         Cursor cur1=bazaPodataka.query(IgricaActivity.listaSlotova, null,null, null, null, null, null);
				         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
				        		 IgricaActivity.listaSlotova +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.imeSlota+"','"+IgricaActivity.stanjeSlota+"','"+IgricaActivity.tezina+"')"+ // ne ubacujem tezinu na pocetku
 	                     " Values ('"+IdSlota+"','Normal','1','1');");
				         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
				        		 IgricaActivity.zadnjiKoristenSlot +"('"+IgricaActivity.IDSlota+"')"+ // ne ubacujem tezinu na pocetku
				     	                     " Values ('"+IdSlota+"');");
				         cur1.close();
				    bazaPodataka.close();
				   // idZadnjegSlota="slot1";// ovaj izbornik se pokrece samo pri prom pokretaju igre tako da je uvijek slot1
				    ucitajPodatkeUSistem(IdSlota);
				    dodajElementeMapeUUIIPokreni();
				    uiMan.postaviTempUniverzalniIzbornik(null);
				    uiMan.makniObjekt(poz);
				    uiMan.makniObjekt(this);
				    
				    uiMan.makniObjekt(izbLoad);
				    uiMan.makniObjekt(izbFight);					  
				    izbFight=null;
			        izbLoad=null;
			}
			else if(brTP==2){
				    tezina=2;

				    SQLiteDatabase bazaPodataka;
				    bazaPodataka= uiMan.context.openOrCreateDatabase(IgricaActivity.glavniDB,uiMan.context.MODE_PRIVATE, null);
				         Cursor cur1=bazaPodataka.query(IgricaActivity.listaSlotova, null,null, null, null, null, null);
				         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
				        		 IgricaActivity.listaSlotova +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.imeSlota+"','"+IgricaActivity.stanjeSlota+"','"+IgricaActivity.tezina+"')"+ // ne ubacujem tezinu na pocetku
				     	                     " Values ('"+IdSlota+"','Hard','1','2');");
				         bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prvi slot
				        		 IgricaActivity.zadnjiKoristenSlot +"('"+IgricaActivity.IDSlota+"')"+ // ne ubacujem tezinu na pocetku
				     	                     " Values ('"+IdSlota+"');");
				         cur1.close();
				    bazaPodataka.close();
				    ucitajPodatkeUSistem(IdSlota);
				    dodajElementeMapeUUIIPokreni();
				    uiMan.postaviTempUniverzalniIzbornik(null);
				    uiMan.makniObjekt(poz);
				    uiMan.makniObjekt(this);
				    uiMan.makniObjekt(izbLoad);
				    uiMan.makniObjekt(izbFight);					  
				    izbFight=null;
			        izbLoad=null;
			}
			
                else if (brTP==3){// mice ovaj izbornik i pokrece pocetni
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
	   
	    TextPaint tp2=new TextPaint();
		   tp2.setAntiAlias(true);
		   tp2.setTypeface(font);
		  tp2.setARGB(250,112, 0,0);
		   tp2.setStyle(Style.FILL);  
	    
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(250, 56, 48,79);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		   izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   izbor.postaviTextDugmica(0, "Normal",tp);
		   izbor.postaviTextDugmica(1, "Hard",tp);
		   izbor.postaviTextDugmica(2, "<<Back",tp2);
		   float sirPolja=2*efekSir/7;
		   float visPolja= efekVis/9;
		   float razmakX=0;
		   float razmakY= 2*visPolja/10;
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix( sirPolja/4,sirPolja/4, visPolja/2, visPolja/2);
		   izbor.postaviLijeviVrhProzoraPix((efekSir-sirPolja-razmakX*2)/2, (efekVis-visPolja*5-razmakY*5)/2);
		   izbor.pokreniMojIzbornik(null);
		   izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix(visPolja,visPolja,visPolja,visPolja);
	   uiMan.setOznacenSam(izbor);	   
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izbor,3);
	}
	//111111111111111
    private IzbornikUniverzalni pokreniUpgradeProzorGlavniIzbornik(IzbornikUniverzalni pozivatelj){
		final IzbornikUniverzalni poz=pozivatelj;
		int brRed=5;
		int brStup=4;
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprUpgGlavniPr,uiMan,brRed,brStup,302){
			 GameEvent ge=new GameEvent(this);
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float pomakX,dxZaDugmic,prosliXZaDugmic=0;
    		 final IzbornikUniverzalni reset =stvoriResetUpgradsBotun(this, poz);
    		 RectF rec=new RectF();
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			 // this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 
    				 ge.jesamLiZiv=true;
    				 dxZaDugmic=0;
    			    
    				 ///
                    // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
    				 this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
    				 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
    			     tekPoc=false;
    			     zavrsenaAnimacijaGlavnog=false;
    			     rotacijaGotova=false;
    			     translacijaGotova=false;
    			     uiMan.iskljuciTouchEvente();
    			     this.getGlavniSprite().pustiZvukSamostalno(1);
    			     pomakX=can.getWidth()-this.dajMiRectCijelogProzora().left-uiMan.pomakCanvasaX();
    			     this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
    			     rec.set(this.dajMiRectCijelogProzora());
    					uiMan.makniObjekt( izbBotunKarakteristike);
    					uiMan.makniObjekt( 	izbFacebook);
    			     prosliXZaDugmic=rec.left;
    			  }
    			//if(!translacijaGotova){
    				
    				prosliXZaDugmic=rec.left;
    				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
    				//rec.set(this.dajMiRectCijelogProzora());
    				tekPoc= sprUpgGlavniPr.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,-pomakX,0,1f,0, rec);
    				dxZaDugmic=rec.left-prosliXZaDugmic;
    				poz.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0);
    				 nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
    				//}
    			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
    			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
  		    	 if(tekPoc){
  		    		 reset.	postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().right,poz.dajMiRectCijelogProzora().top+5* reset.getGlavniRectPrikaza().height()/2);
  		    		crtajIkadaNijePokrenut(true);
  		    		zavrsenaAnimacijaGlavnog=true;
  		    		 uiMan.ukljuciTouchEvente();
  		    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
  		    		poz.pomakniIzbornikAkomulirajuciApsolutno(-(can.getWidth()-this.dajMiRectCijelogProzora().left)+uiMan.pomakCanvasaX(), 0);
  		    		//if(postojiUpgrade)reset.pokreniMojIzbornik(null);
  		    		uiMan.setOznacenSam(this);
  		    
  		    		//this.crtajProzorBezObziraNaSve(false);
  		    	 }
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    				 if(tekPoc) {
    					 ge.jesamLiZiv=true;
    					 zavrsenaAnimacijaGlavnog=false;
    					 reset.pokreniZavrsnuAnimaciju();
        				 dxZaDugmic=0;
        			     
                        // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
        				 //this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
        			     tekPoc=false;
        			     rotacijaGotova=false;
        			     translacijaGotova=false;
        			     uiMan.iskljuciTouchEvente();
        			     pomakX=can.getWidth()-this.dajMiRectCijelogProzora().left-uiMan.pomakCanvasaX();
        			     //this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
        			     rec.set(this.dajMiRectCijelogProzora());
        			     this.getGlavniSprite().pustiZvukSamostalno(2);
        			     prosliXZaDugmic=rec.left;
        			    
        			  }
        			//if(!translacijaGotova){
        				
        				prosliXZaDugmic=rec.left;
        				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
        				//rec.set(this.dajMiRectCijelogProzora());
        				tekPoc= sprUpgGlavniPr.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,pomakX,0,1f,0, rec);
        				dxZaDugmic=rec.left-prosliXZaDugmic;
        			    poz.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0);
        				nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
        				//}
        			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
        			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
      		    	 if(tekPoc){
      		    		 uiMan.dodajElementUListu( izbBotunKarakteristike, 4);
      		    		 uiMan.dodajElementUListu( izbFacebook, 4);
      		    		crtajIkadaNijePokrenut(false);
      		    		zavrsenaAnimacijaGlavnog=false;
      		    		 uiMan.ukljuciTouchEvente();
      		    		this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
      		    		poz.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
      		    		//this.crtajProzorBezObziraNaSve(false);
      		    	 }
        			 return  tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    		
    			    uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				uiMan.makniObjekt(reset);
	 				//pokreniPocIzbornik();
	 				
	 				uiMan.setOznacenSam(poz);
	 				uiMan.postaviTempUniverzalniIzbornik(null);
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			    
	 				if(zavrsenaAnimacijaGlavnog==true)this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				//STRIJELCI
				String naslov="";
				String podnaslov1="",podnaslov2="",podnaslov3="";
				if(brTP==17){//1.strijelci
					 naslov="Archers";
					 podnaslov1="Drill sergeant 1:";
					 podnaslov2="+ range";
					 podnaslov3="+ rate of fire";
					if(!listaUpgradsi.containsKey(idZadnjegSlota+1)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
						if(brUpgradeBodova.containsKey(idZadnjegSlota)){
							if(FazeIgre.cijenaUpgrada(1)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,1);// ako ima dovoljno valor bodova
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,1);
						}
						else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,1);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
					}
					else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,1);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					
				}
				if(brTP==13){//2.strijelci
					 naslov="Archers";
					 podnaslov1="Drill sergeant 2:";
					// podnaslov2="+ range";
					 podnaslov2="+ rate of fire";
					if(listaUpgradsi.containsKey(idZadnjegSlota+1)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+2)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(2)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,2);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,2);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,2);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,2);//ako ga  sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==9){//3.strijelci
					 naslov="Archers";
					 podnaslov1="Bodkin point:";
					 podnaslov2="+armor damage";
					 podnaslov3="";
					if(listaUpgradsi.containsKey(idZadnjegSlota+2)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+3)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(3)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,3);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,3);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,3);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,3);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==5){//4.strijelci
					
					 naslov="Archers";
					 podnaslov1="Plaited string:";
					 podnaslov2="+health damage";
					if(listaUpgradsi.containsKey(idZadnjegSlota+3)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+4)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(4)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,4);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,4);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,4);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,4);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==1){//5.strijelci
					naslov="Archers";
					 podnaslov1="Compound bow:";
					 podnaslov2="++range";
					 podnaslov3="++health damage";
					 
					
					if(listaUpgradsi.containsKey(idZadnjegSlota+4)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+5)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(5)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,5);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,5);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,5);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,5);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				//KASARNA
				if(brTP==18){//1.kasarna
					naslov="Barracks";
					 podnaslov1="Drill sergeant 1:";
					 podnaslov2="-training time";
					 podnaslov3="+health damage";
					if(!listaUpgradsi.containsKey(idZadnjegSlota+6)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
						if(brUpgradeBodova.containsKey(idZadnjegSlota)){
							if(FazeIgre.cijenaUpgrada(6)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,6);// ako ima dovoljno valor bodova
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,6);
						}
						else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,6);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
					}
					else  pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,6);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					
				}
				if(brTP==14){//2.kasarna
					naslov="Barracks";
					 podnaslov1="Drill sergeant 2:";
					 podnaslov2="--training time";
					 podnaslov3="++health damage";
					if(listaUpgradsi.containsKey(idZadnjegSlota+6)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+7)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(7)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,7);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,7);;
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,7);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,7);//ako ga  sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==10){//3.kasarna
					naslov="Barracks";
					 podnaslov1="Meat diet:";
					 podnaslov2="+++health";
					if(listaUpgradsi.containsKey(idZadnjegSlota+7)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+8)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(8)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,8);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,8);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,8);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,8);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==6){//4.kasarna
					naslov="Barracks";
					 podnaslov1="Tempered steel:";
					 podnaslov2="+armor";
					if(listaUpgradsi.containsKey(idZadnjegSlota+8)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+9)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(9)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,9);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,9);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,9);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,9);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==2){//5.kasarna
					
					naslov="Barracks";
					 podnaslov1="Camp followers:";
					 podnaslov2="++health";
					 podnaslov3="++++armor";
					if(listaUpgradsi.containsKey(idZadnjegSlota+9)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+10)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(10)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,10);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,10);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,10);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,10);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				//MINOBACAC
				if(brTP==19){//1.minobacac
					naslov="Mortar:";
					 podnaslov1="Drill sergeant 1:";
					 podnaslov2="-reload time";
					
					if(!listaUpgradsi.containsKey(idZadnjegSlota+11)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
						if(brUpgradeBodova.containsKey(idZadnjegSlota)){
							if(FazeIgre.cijenaUpgrada(11)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,11);// ako ima dovoljno valor bodova
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,11);
						}
						else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,11);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
					}
					else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,11);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					
				}
				if(brTP==15){//2.minobacac
					naslov="Mortar:";
					 podnaslov1="Drill sergeant 2:";
					 podnaslov2="-reload time";
					if(listaUpgradsi.containsKey(idZadnjegSlota+11)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+12)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(12)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,12);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,12);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,12);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,12);//ako ga  sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==11){//3.minobacac
					naslov="Mortar:";
					 podnaslov1="Rifled bore:";
					 podnaslov2="+range";
					if(listaUpgradsi.containsKey(idZadnjegSlota+12)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+13)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(13)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,13);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,13);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,13);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,13);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==7){//4.minobacac
					naslov="Mortar:";
					 podnaslov1="Standard ammo:";
					 podnaslov2="++range";
					if(listaUpgradsi.containsKey(idZadnjegSlota+13)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+14)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(14)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,14);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,14);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,14);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,14);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==3){//5.minovacac
					naslov="Mortar:";
					podnaslov1="Quality explosives:";
					 podnaslov2="+health damage";
					 podnaslov3="+armor damage";
					
					if(listaUpgradsi.containsKey(idZadnjegSlota+14)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+15)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(15)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,15);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,15);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,15);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,15);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				//ALKEMICAR
				if(brTP==20){//1.alkemicar
					naslov="Alchemist ";
					 podnaslov1="Drill sergeant 1:";
					 podnaslov2="-relode time";
					
					if(!listaUpgradsi.containsKey(idZadnjegSlota+16)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
						if(brUpgradeBodova.containsKey(idZadnjegSlota)){
							if(FazeIgre.cijenaUpgrada(16)<=brUpgradeBodova.get(idZadnjegSlota))pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,16);// ako ima dovoljno valor bodova
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,16);
						}
						else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,16);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
					}
					else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,16);;//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					
				}
				if(brTP==16){//2.alkemicar
					naslov="Alchemist ";
					 podnaslov1="Drill sergeant 2:";
					 podnaslov2="--relode time";
					if(listaUpgradsi.containsKey(idZadnjegSlota+16)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+17)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(17)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,17);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,17);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,17);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,17);//ako ga  sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==12){//3.alkemicar
					naslov="Alchemist ";
					podnaslov1="Frog supply";
					 podnaslov1="+health damage";
					if(listaUpgradsi.containsKey(idZadnjegSlota+17)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+18)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(18)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,18);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,18);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,18);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,18);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==8){//4.alkemicar
					naslov="Alchemist ";
					 podnaslov1="Alchemy university";					
					 podnaslov1="+armor damage";
					if(listaUpgradsi.containsKey(idZadnjegSlota+18)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+19)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(19)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,19);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,19);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,19);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,19);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				if(brTP==4){//5.alkemicar
					naslov="Alchemist";
					 podnaslov1="Book of dead";
					 podnaslov1="++health damage";
					 podnaslov1="++armor damage";
					if(listaUpgradsi.containsKey(idZadnjegSlota+19)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+20)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							if(brUpgradeBodova.containsKey(idZadnjegSlota)){
								if(FazeIgre.cijenaUpgrada(20)<=brUpgradeBodova.get(idZadnjegSlota)) pokreniUpgradeManji(this,0,naslov,podnaslov1, podnaslov2,podnaslov3,20);// ako ima dovoljno valor bodova
								else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,20);
							}
							else pokreniUpgradeManji(this,2,naslov,podnaslov1, podnaslov2,podnaslov3,20);// ako nema crta se bezz dugmica "use" "nop" i sa porukom o nedostatku valora
						}
						else pokreniUpgradeManji(this,1,naslov,podnaslov1, podnaslov2,podnaslov3,20);//ako ga ne sadrzi znaci da je otkljucan i onda se pokrece izbornik ali bez "use" "nop" izbora
					}
					
				}
				//////////////////////
                
			}
			@Override
			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
				    
				    	if(postojiUpgrade){
				    		if(!pokrenutResetIzbornik&&zavrsenaAnimacijaGlavnog){
				    			uiMan.dodajElementUListu(reset,5);
				    			reset.pokreniMojIzbornik(null);		    		
				    			pokrenutResetIzbornik=true;
				    			
				    		}
				    	}
				       if(pokrenutResetIzbornik&&!postojiUpgrade){
				    	   pokrenutResetIzbornik=false;
				    		 reset.pokreniZavrsnuAnimaciju();
				    	       }
				    	
				    
				    //STRIJELCI
					if(!listaUpgradsi.containsKey(idZadnjegSlota+1)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
					
						this.postaviDodatnuSlicicuZaDugmic(17, sprKvacicaKrug, 1, 0, 0);
					}
					else{//ako sadrzi znaci da je otkljucan i zato kvacica
					
						this.postaviDodatnuSlicicuZaDugmic(17, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+1)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+2)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(13, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(13, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+2)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+3)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(9, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(9, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+3)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+4)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(5, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(5, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+4)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+5)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(1, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(1, sprKvacicaKrug, 0, 0, 0);
					}
					
					
					//KASARNA
					if(!listaUpgradsi.containsKey(idZadnjegSlota+6)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
					
						this.postaviDodatnuSlicicuZaDugmic(18, sprKvacicaKrug, 1, 0, 0);
					}
					else{//ako sadrzi znaci da je otkljucan i zato kvacica
					
						this.postaviDodatnuSlicicuZaDugmic(18, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+6)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+7)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(14, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(14, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+7)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+8)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(10, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(10, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+8)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+9)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(6, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(6, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+9)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+10)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(2, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(2, sprKvacicaKrug, 0, 0, 0);
					}
					//MINOBACAC
					if(!listaUpgradsi.containsKey(idZadnjegSlota+11)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
					
						this.postaviDodatnuSlicicuZaDugmic(19, sprKvacicaKrug, 1, 0, 0);
					}
					else{//ako sadrzi znaci da je otkljucan i zato kvacica
					
						this.postaviDodatnuSlicicuZaDugmic(19, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+11)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+12)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(15, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(15, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+12)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+13)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(11, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(11, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+13)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+14)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(7, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(7, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+14)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+15)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(3, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(3, sprKvacicaKrug, 0, 0, 0);
					}
					//ALKEMICAR
					if(!listaUpgradsi.containsKey(idZadnjegSlota+16)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
					
						this.postaviDodatnuSlicicuZaDugmic(20, sprKvacicaKrug, 1, 0, 0);
					}
					else{//ako sadrzi znaci da je otkljucan i zato kvacica
					
						this.postaviDodatnuSlicicuZaDugmic(20, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+16)){//ako sadrzi jedan znaci da je otkljucan predhodni i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+17)){// ako ne sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(16, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(16, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+17)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+18)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(12, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(12, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+18)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+19)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(8, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(8, sprKvacicaKrug, 0, 0, 0);
					}
					
				
				
					if(listaUpgradsi.containsKey(idZadnjegSlota+19)){//ako ne sadrzi jedan znaci da je otkljucan i ovaj moze biti izabran kao sljedeci 
						if(!listaUpgradsi.containsKey(idZadnjegSlota+20)){// ako sadrzi taj botun znaci da je nekoristen sto znaci da je neotkljucan
							this.postaviDodatnuSlicicuZaDugmic(4, sprKvacicaKrug, 1, 0, 0);
						}
						else this.postaviDodatnuSlicicuZaDugmic(4, sprKvacicaKrug, 0, 0, 0);
					}
					
					
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
			
	    	
	    };
	   
	     Paint paintNekorBot= new Paint();
		paintNekorBot.setAlpha(70);
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
		paintNekorBot.setColorFilter(filter);
	       izbor.postaviPaintNekoristenihBoruna(paintNekorBot);
		   float sirPolja= (efekSir-poz.dajMiRectCijelogProzora().width()-efekSir/10)/10;
		   float visPolja= efekVis/10;
		   float razmakY=0;
		   float desnaMargina= sirPolja;
		   //if(desnaMargina<0) desnaMargina=sirPolja;
		   //prvi stupac
		   izbor.postaviDirektnoSlicicuZaDugmic(1,0, 0);
		   
		   izbor.postaviDirektnoSlicicuZaDugmic(5,1, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(9,2, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(13,3, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(17,4, 0);
		   //drugi stupac
		   izbor.postaviDirektnoSlicicuZaDugmic(2,0, 0);
		   
		   izbor.postaviDirektnoSlicicuZaDugmic(6,1, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(10,2, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(14,3, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(18,4, 0);
		   //treci stupac
		   izbor.postaviDirektnoSlicicuZaDugmic(3,0, 0);
		   
		   izbor.postaviDirektnoSlicicuZaDugmic(7,1, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(11,2, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(15,3, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(19,4, 0);
		   //cetvrti stupac
		   izbor.postaviDirektnoSlicicuZaDugmic(4,0, 0);
		   
		   izbor.postaviDirektnoSlicicuZaDugmic(8,1, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(12,2, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(16,3, 0);
		   izbor.postaviDirektnoSlicicuZaDugmic(20,4, 0);
		   izbor.postaviStatickiIzbornik();
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix( 0,0, 2*visPolja-visPolja/2, 3*visPolja-visPolja/2);
		  // izbor.postaviLijeviVrhProzoraPix(efekSir/5, efekVis/5);
		
		   izbor.postaviRazmakIzmeduPoljaPix(desnaMargina, visPolja/10);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   ucitavanjeNekoristenihUpgradeBotuna(this.idZadnjegSlota);
		   izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
		   
		  
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 
	  // izbor.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width()/2, 0);
	   uiMan.dodajElementUListu(izbor,4);
	   izbor.regirajINaNekoristenaPolja();// crtat ce ih kao nekoristena ali ce reagirati na dodir, a ja cu poslije odlucivati sto cu napraviti s time
	   
	   return izbor;
	} 
   //11111111
    private void stvoriMuteBotun(){ 
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( null,uiMan,1,1,302){
           // IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					MusicManager.setMute(!MusicManager.getIsMute());
				}
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			   if(MusicManager.getIsMute()){
				   sprMute.nacrtajSprite(can, 1, 1, 0,dajMiRectCijelogProzora());
			   }
			   else{
				   sprMute.nacrtajSprite(can, 1, 0, 0,dajMiRectCijelogProzora());
			   }
				
			}

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
	       izbor.crtajNacrtajVanjskiIznadBezObzira(true);
		 
	       
		   float sirPolja=this.velXDugMutePostoEkrana*efekVis/100;
		   float visPolja=sirPolja;
		  
		   izbor.postaviStatickiIzbornik();
		   izbor.postaviLijeviVrhProzoraPix((sirPolja/10), (visPolja/10));
		   izbor.pokreniMojIzbornik(null);
		   izbor.crtajIkadaNijePokrenut(true);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		  
		   uiMan.dodajElementUListu(izbor,4);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
    private void stvoriIndikatorTezine(){ 
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( null,uiMan,2,1,302){
           // IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
			
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(225,133,2,1);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
	       
		   float sirPolja=1*efekSir/50;
		   float visPolja=2*efekSir/50;
		  if(tezina==1){
			   tp.setARGB(225,1,48,11);
			 // izbor.postaviTextDugmica(0,"Difficulty:", tp);
			  izbor.postaviTextDugmica(0,"Normal", tp);
		  }
		  else if(tezina==2){
			   tp.setARGB(225,209,1,1);
			 // izbor.postaviTextDugmica(0,"Difficulty:", tp);
			  izbor.postaviTextDugmica(0,"Hard", tp);
		  }
		   izbor.postaviStatickiIzbornik();
		   izbor.postaviLijeviVrhProzoraPix(this.efekSir/2-sirPolja/2, this.efekVis-5*visPolja/4);
		   izbor.pokreniMojIzbornik(null);
		   izbor.crtajIkadaNijePokrenut(true);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		  
		   uiMan.dodajElementUListu(izbor,3);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
 private void stvoriIndikatorKupovine (){ 
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( null,uiMan,1,1,302){
           // IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
			
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
	
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
	       
		   
		   
		   float sirPolja=10*efekSir/50;
		   float visPolja=3*efekSir/50;
	
			   tp.setARGB(225,255,168,0);
			  izbor.postaviTextDugmica(0,"Suporter!", tp);
		  

		   izbor.postaviStatickiIzbornik();
		   izbor.postaviLijeviVrhProzoraPix(this.efekSir-1.1f*sirPolja, -1*visPolja/10);
		   izbor.pokreniMojIzbornik(null);
		   izbor.crtajIkadaNijePokrenut(true);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		  
		   uiMan.dodajElementUListu(izbor,3);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
 
	private void pokreniPocIzbornik(){
		  ///////izbornici//////////////////
		 	   //izPoc=new IzbornikUniverzalni(null,uiMan,2,1,302){
		//uiMan.postaviPomakCanvasaApsolutno(0, 0);
		MusicManager.stanjeIgre(this,1,-1, -1);
		pceoSaGlazbom=true;
		if(izbLoad==null){ 
			stvoriMuteBotun();
	    uiMan.stvoriTeksturu(null, xPozCm, yPozCm,52);
		uiMan.stvoriPozadinuPix( pocetnaPozadina, efekSir,efekVis);// dodajem posebnu pozadinu za izbornik i stavljem velicinu ekrana u pixelima da bude preko cijelog ekrana
		/*izPoc=new IzbornikUniverzalni(pocIzb,uiMan,3,1,302){   
		  
		 			public void univerzalniIzvrsitelj(int brTP) {
		 				// TODO Auto-generated method stub
		 				if(brTP==1){
		 					if(idZadnjegSlota.equals("")){
		 						pokreniIzborTezineizbornikIPokreniMapu(this);
		 						
		 					}
		 					else{// u ovom ce se djelu ucitavati zadnji 
		 						ucitajPodatkeUSistem(idZadnjegSlota);
		 						dodajElementeMapeUUIIPokreni();
							    uiMan.postaviTempUniverzalniIzbornik(null);
							    uiMan.makniObjekt(this);
							    izPoc=null;
		 					}
		 				}
		 				else if(brTP==2){
		 					pokreniLoadIzbornik();
		 					//uiMan.makniObjekt(izPoc);
		 					//izPoc=null;
		 					}
		 				else if (brTP==3){
		 					credits();
		 				}
		 			}

		 			@Override
		 			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
		 				// TODO Auto-generated method stub
		 				
		 			}

					@Override
					public RectF getGlavniRectPrikaza() {
						// TODO Auto-generated method stub
						return null;
					}
		 	    	
		 	    };
		 	  
		 	*/
		 		 
	///////////////////////////////////////////////////////////////////////////////////////////////
		 		   ////LOAD
		 		  izbLoad=new IzbornikUniverzalni(null,uiMan,1,1,302){   
		 			 float vrijemePlulsiranjaSek=3;
			 			
		 			    float sirinaBljestanja=1*efekSir/20;
		 			    float visinaBljestanja=1*efekSir/20;
		 			    Paint paintBljesk=new Paint();
			 			boolean maxPovecanje=false, tekPoceo=true;
			 			RectF recBljestanjeRadni=new RectF();
			 			RectF recBljestanje=new RectF();
			 			GameEvent geZaPulsiranje=new GameEvent(null);
			 			public void univerzalniIzvrsitelj(int brTP) {
			 				// TODO Auto-generated method stub
			 			
			 				 if(brTP==1){
			 					pokreniLoadIzbornik();
			 					//uiMan.makniObjekt(izPoc);
			 					//izPoc=null;
			 					}
			 				 else if(brTP==2){
				 					pokreniLoadIzbornik();
				 					//uiMan.makniObjekt(izPoc);
				 					//izPoc=null;
				 					}
			 				 else if(brTP==3){
				 					pokreniLoadIzbornik();
				 					//uiMan.makniObjekt(izPoc);
				 					//izPoc=null;
				 					}
			 				
			 			}
			 			@Override
			 			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			 		                   if(tekPoceo){
			 		                	  recBljestanje.set(0, 0, sirinaBljestanja, visinaBljestanja);
			 		                	  geZaPulsiranje.jesamLiZiv=true;
			 		                	   tekPoceo=false;
			 		                   }
			 			
			 			    	
			 			    	       
			 		                    if(!maxPovecanje){//smanuje
			 			    	        	maxPovecanje=SpriteHendler.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,-recBljestanje.width()/3,-recBljestanje.width()/3,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
			 			    	       
			 			    	        }
			 			    	        else {
			 			    	        	maxPovecanje=!SpriteHendler.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,recBljestanje.width()/3,recBljestanje.width()/3,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
			 			    	        	if(!maxPovecanje)recBljestanje.set(0, 0, sirinaBljestanja, visinaBljestanja);
			 			    	        }
			 			    	        recBljestanjeRadni.set(this.getGlavniRectPrikaza());
			 			    	       recBljestanjeRadni.set(recBljestanjeRadni.left-recBljestanje.width(),recBljestanjeRadni.centerY()-recBljestanje.height()/2, recBljestanjeRadni.left,recBljestanjeRadni.centerY()+recBljestanje.height()/2);
			 			    	        //recBljestanjeRadni.set(recBljestanjeRadni.centerX()-recBljestanje.width()/2, recBljestanjeRadni.centerY()-3*recBljestanje.height()/4, recBljestanjeRadni.centerX()+recBljestanje.width()/2, recBljestanjeRadni.centerY()+1*recBljestanje.height()/4);

			 					    	paintBljesk.setColor(Color.argb(255,255,246,0));
			 							paintBljesk.setStyle(Paint.Style.FILL);
			 							paintBljesk.setShader(new RadialGradient(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
			 								(int)(recBljestanjeRadni.width()*0.8), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
			 							
			 						    can.drawCircle(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
			 									(int)(recBljestanjeRadni.width()*0.8), paintBljesk);
			 			             
			 			 
			 		   }

			 			@Override
			 			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			 				// TODO Auto-generated method stub
			 				
			 			}

			 			@Override
						public RectF getGlavniRectPrikaza() {
							RectF rect=new RectF();
							
							rect.set(this.dajMiRectCijelogProzora());
							return rect;
						}
			 	    	
			 	    };
			 	   TextPaint tp=new TextPaint();
		 		   tp.setAntiAlias(true);
		 		   tp.setTypeface( fontZaPocetneBotune);
		 		  tp.setColor(Color.argb(255, 255,204, 0));
		 		   tp.setStyle(Style.FILL);
		 		  float sirPolja=5*this.efekSir/20;
	               float visPolja=1*this.efekSir/20;
		 		  izbLoad.postaviStatickiIzbornik();
		 		 izbLoad.ostaniUpaljenINakonEfektnogTouchaNaBotun();
		 		// izbLoad.postaviLijeviVrhProzoraPix(1*this.efekSir/40, 5*this.efekVis/19);
		 		 izbLoad.postaviLijeviVrhProzoraPix(1*this.efekSir/40, 9*this.efekVis/19+2*visPolja);
		 		 izbLoad.pokreniMojIzbornik(null);
		 		 izbLoad.crtajIkadaNijePokrenut(true);
		 		 izbLoad.postaviVelicinuPoljaUPix(sirPolja, visPolja);
		 		izbLoad.postaviTextDugmica(0, "Load", tp);
		 		uiMan.postaviTempUniverzalniIzbornik(izbLoad);
				   uiMan.dodajElementUListu( izbLoad,3);
				   ////FIGHT/////////////////////////////////////////////////
			 		  izbFight=new IzbornikUniverzalni(null,uiMan,2,1,302){ 
			 			   float vrijemePlulsiranjaSek=3;
			 			
			 			    float sirinaBljestanja=1*efekSir/20;
			 			    float visinaBljestanja=1*efekSir/20;
			 			    Paint paintBljesk=new Paint();
				 			boolean maxPovecanje=false, tekPoceo=true;
				 			RectF recBljestanjeRadni=new RectF();
				 			RectF recBljestanje=new RectF();
				 			GameEvent geZaPulsiranje=new GameEvent(null);
				 			public void univerzalniIzvrsitelj(int brTP) {
				 				// TODO Auto-generated method stub
				 			
				 				if(brTP==1){
				 					if(idZadnjegSlota.equals("")){
				 						pokreniIzborTezineizbornikIPokreniMapu(this);
				 						
				 					}
				 					else{// u ovom ce se djelu ucitavati zadnji 
				 						ucitajPodatkeUSistem(idZadnjegSlota);
				 						dodajElementeMapeUUIIPokreni();
									    uiMan.postaviTempUniverzalniIzbornik(null);
									    uiMan.makniObjekt(this);
									    uiMan.makniObjekt( izbLoad);
									    izbLoad=null;
									    izbFight=null;
				 					}
				 				}
				 				else 	if(brTP==2){
				 					if(idZadnjegSlota.equals("")){
				 						pokreniIzborTezineizbornikIPokreniMapu(this);
				 						
				 					}
				 					else{// u ovom ce se djelu ucitavati zadnji 
				 						ucitajPodatkeUSistem(idZadnjegSlota);
				 						dodajElementeMapeUUIIPokreni();
									    uiMan.postaviTempUniverzalniIzbornik(null);
									    uiMan.makniObjekt(this);
									    uiMan.makniObjekt( izbLoad);
									    izbLoad=null;
									    izbFight=null;
				 					}
				 				}
				 				
				 				
				 			}
				 			@Override
				 			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
				 		                   if(tekPoceo){
				 		                	  recBljestanje.set(0, 0, sirinaBljestanja, visinaBljestanja);
				 		                	  geZaPulsiranje.jesamLiZiv=true;
				 		                	   tekPoceo=false;
				 		                   }
				 			
				 			    	
				 			    	       
				 			    	        if(!maxPovecanje){//smanuje
				 			    	        	maxPovecanje=SpriteHendler.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,-recBljestanje.width()/3,-recBljestanje.width()/3,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
				 			    	           
				 			    	        }
				 			    	        else {
				 			    	        	maxPovecanje=!SpriteHendler.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,recBljestanje.width()/3,recBljestanje.width()/3,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
				 			    	        	if(!maxPovecanje)recBljestanje.set(0, 0, sirinaBljestanja, visinaBljestanja);
				 			    	        }
				 			    	        recBljestanjeRadni.set(this.getGlavniRectPrikaza());
				 			    	      if( idZadnjegSlota.equals("")) recBljestanjeRadni.set(recBljestanjeRadni.left-recBljestanje.width(),recBljestanjeRadni.centerY()-recBljestanje.height()/2, recBljestanjeRadni.left,recBljestanjeRadni.centerY()+recBljestanje.height()/2);
				 			    	      else   recBljestanjeRadni.set(recBljestanjeRadni.left-recBljestanje.width(),recBljestanjeRadni.top+3*recBljestanjeRadni.height()/4-recBljestanje.height()/2, recBljestanjeRadni.left,recBljestanjeRadni.top+3*recBljestanjeRadni.height()/4+recBljestanje.height()/2);
				 			    	      //recBljestanjeRadni.set(recBljestanjeRadni.centerX()-recBljestanje.width()/2,efekVis-recBljestanje.height()/2, recBljestanjeRadni.centerX()+recBljestanje.width()/2,efekVis+recBljestanje.height()/2);

				 					    	paintBljesk.setColor(Color.argb(255,255,246,0));
				 							paintBljesk.setStyle(Paint.Style.FILL);
				 							paintBljesk.setShader(new RadialGradient(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
				 								(int)(recBljestanjeRadni.width()*0.8), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
				 							
				 						    can.drawCircle(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
				 									(int)(recBljestanjeRadni.width()*0.8), paintBljesk);
				 			             
				 			 
				 		   }
				 			@Override
				 			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				 				// TODO Auto-generated method stub
				 				
				 			}

							@Override
							public RectF getGlavniRectPrikaza() {
								RectF rect=new RectF();
								
								rect.set(this.dajMiRectCijelogProzora());
								return rect;
							}
				 	    	
				 	    };
		               sirPolja=9*this.efekSir/20;
		              visPolja=1*this.efekSir/20;
				 	   izbFight.postaviStatickiIzbornik();
				 	 // izbFight.postaviLijeviVrhProzoraPix(this.efekSir/2-sirPolja/2,efekVis-2*visPolja);
				 	  izbFight.postaviLijeviVrhProzoraPix(1*this.efekSir/40, 7*this.efekVis/19);
				 	 izbFight.ostaniUpaljenINakonEfektnogTouchaNaBotun();
				 	izbFight.crtajIkadaNijePokrenut(true);
				 	izbFight.postaviVelicinuPoljaUPix(sirPolja, visPolja);
				 	
					  
		 	if(!this.idZadnjegSlota.equals("")){// ako ima vec nesto spremljeno u nekom od slotova
		 		izbFight.postaviTextDugmica(0, "", tp);
		 		izbFight.postaviTextDugmica(1, "Continue", tp);
		 		
		 	}
		 	else {
		 		izbFight.postaviTextDugmica(0, "New", tp);
		 		izbFight.postaviTextDugmica(1, "Game", tp);
		 		
		 	}
		 	 uiMan.dodajElementUListu( izbFight,3);
		 	 izbFight.pokreniMojIzbornik(null);
		 	 
		 	 
		 
		 	       }
		else {
			uiMan.stvoriTeksturu(null, xPozCm, yPozCm,52);
			 uiMan.stvoriPozadinuPix( pocetnaPozadina, efekSir,efekVis);// dodajem posebnu pozadinu za izbornik i stavljem velicinu ekrana u pixelima da bude preko cijelog ekrana
			
		}
		//stvoriMuteBotun();
		 	  
		    }
private void stvoriFacebookBotun(){ 
		
	this.izbFacebook=new IzbornikUniverzalni( this.facebookBotun,uiMan,1,1,302){
            IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					if(!pokrenutUpgradeBotun){
						uiMan.context.startActivity(IgricaActivity.getOpenFacebookIntent(uiMan.context));
					/*	upgProzor.pokreniMojIzbornik(tempUpgradeListaNekoristenih);
						uiMan.dodajElementUListu(upgProzor,4);
						uiMan.postaviTempUniverzalniIzbornik(upgProzor);
						pokrenutUpgradeBotun=true;*/
					}
					else{
						upgProzor.pokreniZavrsnuAnimaciju();
						
						pokrenutUpgradeBotun=false;
					}
				}
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
	
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
	    izbFacebook.crtajNacrtajVanjskiIznadBezObzira(true);
	
	       float visBot=0.6f*this.velXDugNazadPostoEkrana*efekVis/100;
	       float sirBot=0.6f*this.velXDugNazadPostoEkrana*efekVis/100;
	    		  
		  
	       izbFacebook.postaviStatickiIzbornik();
	
	       izbFacebook.postaviStatickiIzbornik();
	       izbFacebook.postaviLijeviVrhProzoraPix((efekSir-this.velXDugNazadPostoEkrana*efekVis/200- sirBot/2), (efekVis-  this.velXDugNazadPostoEkrana*efekVis/200-visBot/2));
	       izbFacebook.pokreniMojIzbornik(null);
	       izbFacebook.crtajIkadaNijePokrenut(true);
	       izbFacebook.postaviVelicinuPoljaUPix(visBot,  sirBot);
		  
		   uiMan.dodajElementUListu(izbFacebook,4);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
	/////////111111111111
	private void stvoriUpgradeBotun(){ 
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprBotUpg,uiMan,1,1,302){
            IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					if(!pokrenutUpgradeBotun){
						upgProzor.pokreniMojIzbornik(tempUpgradeListaNekoristenih);
						uiMan.dodajElementUListu(upgProzor,4);
						uiMan.postaviTempUniverzalniIzbornik(upgProzor);
						pokrenutUpgradeBotun=true;
					}
					else{
						upgProzor.pokreniZavrsnuAnimaciju();
						
						pokrenutUpgradeBotun=false;
					}
				}
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
				if(brUpgradeBodova.containsKey(idZadnjegSlota))if(brUpgradeBodova.get(idZadnjegSlota)>0){
				
				RectF rec=new RectF(this.dajMiRectCijelogProzora());
				rec.offsetTo(rec.left, rec.top-rec.height());
				TextPaint p=new TextPaint();
				p.setARGB(250,255, 216, 0);
				if(sprBrValoraVnjski!=null){
					  if(brUpgradeBodova.get(idZadnjegSlota)<10)sprBrValoraVnjski.nacrtajTextUnutarPravokutnika(rec.left+2*rec.width()/8,rec.top+2*rec.height()/8,4*rec.width()/6,4*rec.height()/8, p,Integer.toString(brUpgradeBodova.get(idZadnjegSlota)), can);
					  else sprBrValoraVnjski.nacrtajTextUnutarPravokutnika(rec.left+1*rec.width()/8,rec.top+2*rec.height()/8,4*rec.width()/6,4*rec.height()/8, p,Integer.toString(brUpgradeBodova.get(idZadnjegSlota)), can);
					  sprBrValoraVnjski.nacrtajSprite(can,0,0, 0,rec); 
				}
				}
				
			}

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
	       izbor.crtajNacrtajVanjskiIznadBezObzira(true);
	       float sirPolja=this.velXDugUpgradePostoEkrana*efekVis/100;
		   float visPolja=sirPolja;
		   sirinaUpgradeBotuna=sirPolja;
		  
		  
		   izbor.postaviStatickiIzbornik();
		   izbor.postaviLijeviVrhProzoraPix((efekSir-sirinaUpgradeBotuna), (efekVis- visPolja-this.velXDugNazadPostoEkrana*this.efekVis/100));
		   izbor.pokreniMojIzbornik(null);
		   izbor.crtajIkadaNijePokrenut(true);
		   izbor.postaviVelicinuPoljaUPix(sirinaUpgradeBotuna, visPolja);
		  
		   uiMan.dodajElementUListu(izbor,4);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
       ///111111111
private void stvoriBackBotun(){ 
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprBack,uiMan,1,1,302){
            IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					activity.runOnUiThread(new Runnable() {
					    public void run() {
							onBackPressed();
					    }
					});
			

			
			
					
				
				}
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			
				
			}

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
	       izbor.crtajNacrtajVanjskiIznadBezObzira(true);
		 
	       float visBot=this.velXDugNazadPostoEkrana*efekVis/100;
	       float sirBot=this.velXDugUpgradePostoEkrana*efekVis/100;
	    		  
		  
		   izbor.postaviStatickiIzbornik();
		   izbor.postaviLijeviVrhProzoraPix(0, efekVis-visBot);
		   izbor.pokreniMojIzbornik(null);
		   izbor.crtajIkadaNijePokrenut(true);
		   izbor.postaviVelicinuPoljaUPix(sirBot,visBot);
		  
		   uiMan.dodajElementUListu(izbor,4);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
    private IzbornikUniverzalni stvoriResetUpgradsBotun(IzbornikUniverzalni pozivatelj, IzbornikUniverzalni upgradeBotun){ 
		   final IzbornikUniverzalni poz=pozivatelj;
		   final IzbornikUniverzalni upgBot=upgradeBotun;
		   IzbornikUniverzalni izbor=new IzbornikUniverzalni(sprReset,uiMan,1,1,302){

			boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
			float prosliXOdPozivatelja;
   		    float pomakX,dxZaDugmic,prosliXZaDugmic=0,pomakApsolutni=0, zalizak=0;
   		    RectF rec=new RectF();
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					resetirajUpgradse(idZadnjegSlota);
					poz.izmjenilistuNekoristenih(tempUpgradeListaNekoristenih);
					poz.izbrisiSveDodatneSliciceZaDugmic();
				}
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			  @Override
	  		    public boolean	nacrtajUvod(float fps,Canvas can){
	    			 
	    			 if(tekPoc) {
	    				 
	    				 dxZaDugmic=0;
	    				 pomakniIzbornikAkomulirajuciApsolutno(0,0);
	                    // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
	    				 postaviLijeviVrhProzoraPix(upgBot.dajMiRectCijelogProzora().right+uiMan.pomakCanvasaX(),upgBot.dajMiRectCijelogProzora().top-5*upgBot.dajMiRectCijelogProzora().height()/2+uiMan.pomakCanvasaY());
	    			     tekPoc=false;
	    			     translacijaGotova=false;
	    			     //zalizak=this.dajMiRectCijelogProzora().width()/5;
	    			     pomakX=this.dajMiRectCijelogProzora().width()-zalizak;
	    			     
	    			     rec.set(this.dajMiRectCijelogProzora());
	    			     
	    			     prosliXZaDugmic=rec.left;
	    			     
	    			  }
	    			//if(!translacijaGotova){
	    				dxZaDugmic=rec.left-prosliXZaDugmic;
	    				
	    				prosliXZaDugmic=rec.left;
	    				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
	    				//rec.set(this.dajMiRectCijelogProzora());
	    				tekPoc= sprReset.animacijaSlaganjeTranslacijaVremenska(0,0,-pomakX,0,1f,0, rec);
	    				
	    				//}
	    			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
	    			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
	  		    	 if(tekPoc){
	  		    		pokrenutResetIzbornik=true;
	  		    		 this.pomakniIzbornikAkomulirajuciApsolutno(-pomakX, 0);
	  		    		
	  		    		//this.crtajProzorBezObziraNaSve(false);
	  		    	 }
	    			 return  tekPoc;
	  		    	}
			  @Override
	  		    public boolean	nacrtajKraj(float fps,Canvas can){
	    			 
	    			 if(tekPoc) {
	    				 pomakniIzbornikAkomulirajuciApsolutno(0,0);
	    				 dxZaDugmic=0;
	    				 postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().left-this.dajMiRectCijelogProzora().width()+zalizak+uiMan.pomakCanvasaX(),upgBot.dajMiRectCijelogProzora().top-5*upgBot.dajMiRectCijelogProzora().height()/2+uiMan.pomakCanvasaY());
	                      this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
	    				 // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
	    				 pomakApsolutni=0;
	    			     tekPoc=false;
	    			     translacijaGotova=false;
	    			    // zalizak=this.dajMiRectCijelogProzora().width()/5;
	    			     pomakX=this.dajMiRectCijelogProzora().width()-zalizak;
	    			     rec.set(this.dajMiRectCijelogProzora());
	    			     prosliXOdPozivatelja=poz.getGlavniRectPrikaza().left;
	    			     prosliXZaDugmic=rec.left;
	    			     //uiMan.dodajElementUListu(this,5);
	    			  }
	    			//if(!translacijaGotova){
	    				dxZaDugmic=rec.left-prosliXZaDugmic;
	    				dxZaDugmic+=poz.getGlavniRectPrikaza().left-prosliXOdPozivatelja;
	    				pomakApsolutni+=dxZaDugmic;
	    				prosliXZaDugmic=rec.left;
	    				prosliXOdPozivatelja=poz.getGlavniRectPrikaza().left;
	    				this.pomakniIzbornikAkomulirajuciApsolutno(pomakApsolutni+poz.dajMiRectCijelogProzora().left-this.dajMiRectCijelogProzora().width()+uiMan.pomakCanvasaX(), 0); 
	    				//rec.set(this.dajMiRectCijelogProzora());
	    				tekPoc= sprReset.animacijaSlaganjeTranslacijaVremenska(0,0,pomakX,0,0.7f,0,  rec);
	    				
	    				//}
	    			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
	    			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
	  		    	 if(tekPoc){
	  		    		 uiMan.makniObjekt(this);
	  		    		pokrenutResetIzbornik=false;
	  		    		 this.pomakniIzbornikAkomulirajuciApsolutno(poz.dajMiRectCijelogProzora().right+uiMan.pomakCanvasaX(), 0);
	  		    		
	  		    		//this.crtajProzorBezObziraNaSve(false);
	  		    	 }
	    			 return  tekPoc;
	  		    	}
			  public void zavrsenaZavrsnaAnimacija(){
    			
	 				//uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
    		  }

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
	       izbor.crtajNacrtajVanjskiIznadBezObzira(true);
	
		   float sirPolja=1*efekSir/8;
		   float visPolja=1*efekSir/8;
	
		
		   izbor.postaviStatickiIzbornik();
		   izbor.postaviLijeviVrhProzoraPix(upgradeBotun.dajMiRectCijelogProzora().right,upgradeBotun.dajMiRectCijelogProzora().top+5*visPolja/2);
		   //izbor.pokreniMojIzbornik(null);
		   izbor.crtajIkadaNijePokrenut(true);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   izbor.crtajProzorZajdnoSaAnimacijomUvodaIkraja();
		   uiMan.setMargine(uiMan.getMarginuLijevu(), sirPolja,uiMan.getMarginuGore(), uiMan.getMarginuDolje());
		   uiMan.dodajElementUListu(izbor,5);
     
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 
		   return izbor;
	      
	}	
    private void pokreniUpgradeManji(IzbornikUniverzalni pozivatelj, int stanje,String naslov,String prviPodnaslov,String drugiPodnaslov,String treciPodnaslov,int brUpgrada){
		final IzbornikUniverzalni poz=pozivatelj;
		 boolean tempB=false;
		 boolean crtajNoMoney=false;
		final int brUpgradea=brUpgrada;
		//final int brUpgradaFinal=brUpgrada;
		final  TextPaint tp=new TextPaint();
		final  TextPaint tpValor=new TextPaint();
		if(stanje==0){//zakljucana
			tempB=false;
		}
		else if(stanje==1){// otkljucana, znaci nece prikazivati dugmice "use" "nop"
			
				tempB=true;
			
		}
		else if(stanje==2){// otkljucana ali nemac dovoljno novaca prikazati izbornik bez dugmica i sa porukom o nedostatku novaca
			tempB=true;
			crtajNoMoney=true;
		
	}
		final boolean otkljucano=tempB;
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprUpgSpor,uiMan,4,2,302){
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 RectF rec=new RectF();
    		 GameEvent geKut=new GameEvent(this);
    		 GameEvent geRazmo=new GameEvent(this);
    		 GameEvent ge=new GameEvent(this);
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 ge.jesamLiZiv=true;
    				 this.postaviLijeviVrhProzoraPix((efekSir-this.dajMiRectCijelogProzora().width())/2, (efekVis-this.dajMiRectCijelogProzora().height())/2);
    			  rec.set(this.dajMiRectCijelogProzora());
    			     this.getGlavniSprite().pustiZvukSamostalno(1);
    			  top=efekVis-this.dajMiPomakCanvasaY()-dajMiRectCijelogProzora().top;
    			  rec.offsetTo(rec.left, efekVis-this.dajMiPomakCanvasaY());
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  uiMan.iskljuciTouchEvente();
    			  }
    			if(!translacijaGotova){
    				translacijaGotova=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.6f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova=  sprUpgSpor.animacijaSlaganjeRotacijaVremenska( geKut, -90, 90,0.6f);
    			}
   		
    			 tekPoc= sprUpgSpor.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut,  1, 0, 10, 10,0.6f, rec, null,false);
  		    	 if(tekPoc) uiMan.ukljuciTouchEvente();	
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    				     this.getGlavniSprite().pustiZvukSamostalno(2);
    				  ge.jesamLiZiv=true;
    				  uiMan.iskljuciTouchEvente();	
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;}
    			  if(!translacijaGotova){
      				translacijaGotova=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.6f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova=  sprUpgSpor.animacijaSlaganjeRotacijaVremenska(geKut, 0, -90,0.6f);
      			  }
    			  tekPoc= sprUpgSpor.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo, geKut, 1, 0, 10, 10,0.6f, rec, null,false);
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			  uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
	 				uiMan.setOznacenSam(poz);
	 				uiMan.postaviTempUniverzalniIzbornik(poz);
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==7&&!otkljucano){
					oduzmiBodoveUDB(FazeIgre.cijenaUpgrada(brUpgradea),idZadnjegSlota);
					otkljucajNoveUpgradeUDB(brUpgradea,idZadnjegSlota);
					lodirajUpgradse();
					ucitavanjeNekoristenihUpgradeBotuna(idZadnjegSlota);
					lodirajUpgradse();
					poz.izmjenilistuNekoristenih(tempUpgradeListaNekoristenih);
					this.pokreniZavrsnuAnimaciju();
					
					
				}
                else {//if (brTP==6&&!otkljucano){// mice ovaj izbornik i pokrece pocetni
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}
			@Override
			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
				RectF tempRec=new RectF();
				float sir=rec.height()/5;
				tempRec.set(rec.right- 2.2f*sir, rec.top+sir/2,rec.right- 2.2f*sir+sir,rec.top+sir/2+sir);
				fazaSpr.nacrtajSprite(can, 1, 0, 0, tempRec);
				tempRec.set(tempRec.left+sir, tempRec.top, tempRec.right+sir, tempRec.bottom);
				this.nacrtajTextUnutarPravokutnika(tempRec.left,tempRec.top,tempRec.width(),tempRec.height(), tpValor,Integer.toString((int)FazeIgre.cijenaUpgrada(brUpgradea)));
			}
			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(250, 67, 112,67);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		   TextPaint tp2=new TextPaint();
		   TextPaint tp1=new TextPaint();
		   tpValor .set(tp);
		   tpValor .setARGB(250, 255, 156, 0);
		   tp2.set(tp);
		   tp2.setARGB(250, 155, 0, 0);
		   tp1.set(tp);
		   tp1.setARGB(250, 192, 2, 10);
		   izbor.postaviStatickiIzbornik();
		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   izbor.postaviTextDugmica(0,prviPodnaslov,tp);
		   izbor.postaviTextDugmica(2, drugiPodnaslov,tp);
		   izbor.postaviTextDugmica(4, treciPodnaslov,tp);
		   //izbor.postaviTextDugmica(0, "-increases rate of fire",tp);
		   //izbor.postaviTextDugmica(2, "-slightly increases range",tp);
		   if(!otkljucano){
			              izbor.postaviTextDugmica(6, "Use ",tp2);
		                  izbor.postaviTextDugmica(7, "Nope",tp2);}
		   else if(crtajNoMoney){
			   izbor.postaviTextDugmica(6, "Need more ",tp);
               izbor.postaviTextDugmica(7, "      valor",tp);
		   }
		  // izbor.postaviTextDugmica(2, "<< Back",tp);
		   float sirPolja=2*efekSir/7;
		   float visPolja= efekVis/9;
		   float razmakY=2*visPolja/10;
		   float razmakX= 4*visPolja/10;
		   float desnaMargina=3*sirPolja;
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix( sirPolja/2,sirPolja/4, 2*visPolja, visPolja);
		   izbor.postaviLijeviVrhProzoraPix((efekSir-izbor.dajMiRectCijelogProzora().width())/2, (efekVis-izbor.dajMiRectCijelogProzora().height())/2);
		   //izbor.postaviNaslovIzbornikaPix("Drill sergeant:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
		   izbor.postaviNaslovIzbornikaPix(naslov, 2*sirPolja,visPolja,2* razmakX, 2*visPolja/3, tp1);
		   izbor.pokreniMojIzbornik(null);
		   izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   uiMan.setOznacenSam(izbor);
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izbor,4);
	}
	private void otkljucajNoveUpgradeUDB(int brUpgrada,String idSlota){
		SQLiteDatabase bazaPodataka;
		bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);

		    bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prva faza postoji
		    		IgricaActivity.listaUpgradesa +"('"+IgricaActivity.IDUpgradesaSlotPlusBrUpgrada+"','"+IgricaActivity.IDSlota+"','"+IgricaActivity.brojUpgradesa+"')"+
			                     " Values ('"+ idSlota+brUpgrada +"','"+ idSlota +"','"+ brUpgrada +"');");
		  bazaPodataka.close();
	}
	private void oduzmiBodoveUDB(int brBodova,String idSlota){
		SQLiteDatabase bazaPodataka;
		bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
		Integer ukupnoBodova=0;
		  if(brUpgradeBodova.containsKey(idSlota)){
			  ukupnoBodova=(Integer) brUpgradeBodova.get(idSlota);
			  ukupnoBodova-=brBodova;
		  }
		  
		    bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prva faza postoji
		    		IgricaActivity.listaBodovaUpgradesa +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.bodoviUpgradesa+"')"+
			                     " Values ('"+ idSlota +"','"+ukupnoBodova+"');");
		  bazaPodataka.close();
	}
	private void lodirajIzDB(){
    	slotoviTezina=new HashMap<String,Integer>();
    	listaImenaSlotova=new HashMap<String,String>() ;
    	koristeneFazePoSlotovima=new HashMap<String,String>() ;
        koristeneFazeBrZvjezdica=new HashMap<String,Integer>();
        koristeneFazeTezina=new HashMap<String,Integer>();
    	koristeneFazeStanje=new HashMap<String,Integer>();
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
	         String IDKoristeneFaze="";
	        String imeSlota;
	        int stanjeSlota;
	        int brZvjezdica=0;
	        int tezina;
	       int stanje=0;
			while(cur1.isAfterLast()==false){// ucitava slot po slot
				IDSlota=cur1.getString(cur1.getColumnIndex(IgricaActivity.IDSlota));
			    imeSlota=cur1.getString(cur1.getColumnIndex(IgricaActivity.imeSlota));
	               Cursor cur2=bazaPodataka.query(IgricaActivity.listaKoristenihFaza, null,null,null, null, null, IgricaActivity.redniBrojKoristeneFazeUSlotu+ " ASC");
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
	            	     
	            	     
	            	     IDKoristeneFaze=cur2.getString(cur2.getColumnIndex(IgricaActivity.IDKoristeneFaze));
	            	    
	            	     //////////////////	OTKLJUCAVANJE SLJEDEĆE
	            	     //VARIJABLA STANJE IMA VRIJEDNOST OD PROŠLE ITERACIJE
	            	     
	            	     if(stanje==2){//ako je prošla pređena sljedeću stavljam kao samo otklju�?anu, alia ko je i ona pređena samo će se prebrisati sa podatcima
            	    		// brZvjezdica=0;
            	    		int promjenaStanja=1;// stavljam da je sljedeća otkljucana
            	    	    this.koristeneFazePoSlotovima.put(IDSlota,IDKoristeneFaze);// mapiram koristene faze u slotove tako da se samo pomocu imena slota mogu naci svi atributi određene faze
	            	        this.koristeneFazeBrZvjezdica.put(IDKoristeneFaze,brZvjezdica);
	            	        this.koristeneFazeTezina.put(IDKoristeneFaze, tezina);
	            	       
	            	        stanje=cur2.getInt(cur2.getColumnIndex(IgricaActivity.stanjeFaze));
	            	        if(stanje!=0){// ako trenuta�?no je nekakvo stanje osim zakljucano u�?itavam to stanje
	            	        	promjenaStanja=stanje;
	            	        	// u slicaju da je zakljucana stanje =0 onda ucitavam stanje 1 tj otkljucana
	            	        }
	            	        koristeneFazeStanje.put(IDKoristeneFaze, promjenaStanja);
	            	        
            	         }
	            	     
	            	     else{
	            	    	 stanje=cur2.getInt(cur2.getColumnIndex(IgricaActivity.stanjeFaze));// ako nije u�?itavam normalno
	            	    	 koristeneFazeStanje.put(IDKoristeneFaze, stanje);
	            	     }
	                     //////////////////////////
	            	     
	            	     
	            	     
	            	     this.koristeneFazePoSlotovima.put(IDSlota,IDKoristeneFaze);// mapiram koristene faze u slotove tako da se samo pomocu imena slota mogu naci svi atributi određene faze
	            	     this.koristeneFazeBrZvjezdica.put(IDKoristeneFaze,brZvjezdica);
	            	     this.koristeneFazeTezina.put(IDKoristeneFaze, tezina);
	            	     
	            	    
	            	   }
	     
	         	    cur2.moveToNext();// za pomicanje u bazi podataka   
	          
	                  }
	               cur2.close();
	             cur1.moveToNext();// za pomicanje u bazi podataka
           
			}
			cur3.close();
	cur1.close();
			//////////////////
			   
			  bazaPodataka.close();
			  lodirajUpgradse();//lodiram ih posebno jer ce ih trebati upgrade izbornici i onda sam razdovi lodiranje da se dzaba ne lodiraju ostale stvari
 } 
	private void otkljucajSveFaze(String idSlotaZaOtkljucati){
    
    	
	 SQLiteDatabase bazaPodataka;
     bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
	  int ukupnoBodova=0;
	  
	   
		
	               Cursor cur2=bazaPodataka.query(IgricaActivity.listaKoristenihFaza, null,null,null, null, null, IgricaActivity.redniBrojKoristeneFazeUSlotu+ " ASC");
	               String IDKoristeneFaze="";
	               cur2.moveToFirst(); 
	               while(cur2.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 
	            	   
	            	   
	            	     
	         		
	            	   
	         		  
	         		  
	            	   String idSlottemp=cur2.getString(cur2.getColumnIndex(IgricaActivity.IDSlota));
	            	   if(idSlotaZaOtkljucati.equals(idSlottemp)){
	            		   IDKoristeneFaze=cur2.getString(cur2.getColumnIndex(IgricaActivity.IDKoristeneFaze));
	            	  
	            	     
	            	     bazaPodataka.execSQL(" INSERT OR REPLACE INTO " +  
		         				  IgricaActivity.listaKoristenihFaza +"('"+IgricaActivity.IDKoristeneFaze+"','"+IgricaActivity.IDSlota+"','"+IgricaActivity.stanjeFaze+"','"+IgricaActivity.brojZvijezdica+"')"+ // ne ubacujem tezinu na pocetku
		              	                     " Values ('"+    IDKoristeneFaze +"','"+ idSlotaZaOtkljucati+"','"+ 2 +"','"+3+"');");
		         		  
    	     
	            	     ukupnoBodova+=3;
	            	   }
	     
	         	    cur2.moveToNext();// za pomicanje u bazi podataka   
	               
	                  }
	               cur2.close();
	           
           
			
	
			//////////////////
	               
	     
	     		    bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prva faza postoji
	     		    		IgricaActivity.listaBodovaUpgradesa +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.bodoviUpgradesa+"')"+
	     			                     " Values ('"+  idSlotaZaOtkljucati +"','"+ukupnoBodova+"');");
			  bazaPodataka.close();
			  lodirajUpgradse();//lodiram ih posebno jer ce ih trebati upgrade izbornici i onda sam razdovi lodiranje da se dzaba ne lodiraju ostale stvari
 } 
	private void lodirajUpgradse(){
		/////lodiranje upgradsa
		listaUpgradsi=new HashMap<String,Integer>();
		brUpgradeBodova=new HashMap<String,Integer>();
		SQLiteDatabase bazaPodataka;
		bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
		
		Cursor cur4=bazaPodataka.query(IgricaActivity.listaUpgradesa, null,null, null, null, null, null);
		cur4.moveToFirst();  
		 this.postojiUpgrade=false;
		while(cur4.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
        	    String IDUpgrada=cur4.getString(cur4.getColumnIndex(IgricaActivity.IDUpgradesaSlotPlusBrUpgrada));	
        	   
        	    Integer brojUpg=cur4.getInt(cur4.getColumnIndex(IgricaActivity.brojUpgradesa));
        	   
        	    if(cur4.getString(cur4.getColumnIndex(IgricaActivity.IDSlota)).equals(this.idZadnjegSlota)){
        	    	this.postojiUpgrade=true;
        	    }
        	     this.listaUpgradsi.put(IDUpgrada,brojUpg);// 
 
     	    cur4.moveToNext();// za pomicanje u bazi podataka   
           
              }
		Cursor cur5=bazaPodataka.query(IgricaActivity.listaBodovaUpgradesa, null,null, null, null, null, null);
		cur5.moveToFirst(); 
		while(cur5.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
     	    String IDSlotaU=cur5.getString(cur5.getColumnIndex(IgricaActivity.IDSlota));	
     	   
     	    Integer bodoviUpg=cur5.getInt(cur5.getColumnIndex(IgricaActivity.bodoviUpgradesa));
     	   brUpgradeBodova.put(IDSlotaU,bodoviUpg);
     	    

  	    cur5.moveToNext();// za pomicanje u bazi podataka   
        
           }
		cur4.close();
		cur5.close();
		 bazaPodataka.close();
		
	}
	private void resetirajUpgradse(String IdSlota){
		/////lodiranje upgradsa
		listaUpgradsi=new HashMap<String,Integer>();
		brUpgradeBodova=new HashMap<String,Integer>();
		SQLiteDatabase bazaPodataka;
		bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
		////ponovno ucitavanje liste br bodova za savki slu�?aj
		Cursor cur5=bazaPodataka.query(IgricaActivity.listaBodovaUpgradesa, null,null, null, null, null, null);
		cur5.moveToFirst(); 
		while(cur5.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
     	    String IDSlotaU=cur5.getString(cur5.getColumnIndex(IgricaActivity.IDSlota));	
     	   
     	    Integer bodoviUpg=cur5.getInt(cur5.getColumnIndex(IgricaActivity.bodoviUpgradesa));
     	   brUpgradeBodova.put(IDSlotaU,bodoviUpg);
     	    

  	    cur5.moveToNext();// za pomicanje u bazi podataka   
        
           }
	    ////ZBRANJANJE BODOVA KOJE TREBA DODATI NAKON RESETIRANJA
		Cursor cur4=bazaPodataka.query(IgricaActivity.listaUpgradesa, null,null, null, null, null, null);
		cur4.moveToFirst();
		
		int bodoviZaDodati=brUpgradeBodova.get(IdSlota);
		while(cur4.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 
			
        	 if(cur4.getString(cur4.getColumnIndex(IgricaActivity.IDSlota)).equals(IdSlota)){ 
         	    Integer brojUpgrada=cur4.getInt(cur4.getColumnIndex(IgricaActivity.brojUpgradesa));
        	    bodoviZaDodati+=   FazeIgre.cijenaUpgrada(brojUpgrada);
        	   
        	 }
 
     	    cur4.moveToNext();// za pomicanje u bazi podataka   
           
              }
		//konacno dodavanje bodova u listu
	    bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prva faza postoji
	    		IgricaActivity.listaBodovaUpgradesa +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.bodoviUpgradesa+"')"+
		                     " Values ('"+ IdSlota +"','"+bodoviZaDodati+"');");
	
		
		////BRISANJE 
		bazaPodataka.delete(IgricaActivity.listaUpgradesa, IgricaActivity.IDSlota+"=?",new String[]{IdSlota});
		cur4.close();
		cur5.close();
		 bazaPodataka.close();
	    ///POZIOVANJE LODIRAJ UPGRADSE FUNKCIJE
		 lodirajUpgradse();
		 ucitavanjeNekoristenihUpgradeBotuna(IdSlota);
	}
	private void stvoriFazeIzDB(){
		////////////////faza 0//////////////
	    String IDKoristeneFaze=this.idZadnjegSlota+"faza0";
	    int stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*1.0f;
			yZadKorFaz=yPiksCm*5.0f;
			Faza faza0=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,0);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza0.dodajParametreIzDB(0,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      boolean jesamLiZadnja=false;
  	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
  	      if(stanje==1) jesamLiZadnja=true;
          faza0.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      
  	      uiMan.dodajElementUListu(faza0, 2);
		}
		/////////////////////////
		////////////////faza 1//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza1";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*2.9f;
			yZadKorFaz=yPiksCm*6.0f;
		  faza1=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,1);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza1.dodajParametreIzDB(1,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      boolean jesamLiZadnja=false;
  	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
  	      if(stanje==1) jesamLiZadnja=true;
          faza1.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      
  	      uiMan.dodajElementUListu(faza1, 2);
		}
		/////////////////////////
        ////////////////faza 2//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza2";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*4.2f;
			yZadKorFaz=yPiksCm*6.6f;
		
		  Faza faza2=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,2);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza2.dodajParametreIzDB(2,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza2.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza2, 2);
		}
		 ////////////////faza 3//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza3";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*5.7f;
			yZadKorFaz=yPiksCm*6.6f;
		
			Faza faza3=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,2);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza3.dodajParametreIzDB(3,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza3.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza3, 2);
		}
		/////////////////////////
		 ////////////////faza 4//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza4";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*6.9f;
			yZadKorFaz=yPiksCm*6.6f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,2);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(4,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 5//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza5";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*8f;
			yZadKorFaz=yPiksCm*6.8f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,5);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(5,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 6//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza6";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*7.7f;
			yZadKorFaz=yPiksCm*5.4f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,6);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(6,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 7//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza7";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*6.8f;
			yZadKorFaz=yPiksCm*4.6f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,7);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(7,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 8//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza8";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*5.5f;
			yZadKorFaz=yPiksCm*4.8f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,8);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(8,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 9//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza9";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*4.3f;
			yZadKorFaz=yPiksCm*4.2f;
		
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,9);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(9,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 10//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza10";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*2.6f;
			yZadKorFaz=yPiksCm*3.7f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,10);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(10,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
		/////////////////////////
		 ////////////////faza 11//////////////
	    IDKoristeneFaze=this.idZadnjegSlota+"faza11";
	    stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		if(stanje==1||stanje==2){
			xZadKorFaz=xPiksCm*3.6f;
			yZadKorFaz=yPiksCm*3.0f;
		
			Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,11);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
  	      faza4.dodajParametreIzDB(11,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
  	      
  	      boolean jesamLiZadnja=false;
	      int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
	      if(stanje==1) jesamLiZadnja=true;
          faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
  	      uiMan.dodajElementUListu(faza4, 2);
		}
        ////////////////faza 12//////////////
         IDKoristeneFaze=this.idZadnjegSlota+"faza12";
         stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
         if(stanje==1||stanje==2){
        	 xZadKorFaz=xPiksCm*4.8f;
 			 yZadKorFaz=yPiksCm*3.0f;

	     Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,12);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
         faza4.dodajParametreIzDB(12,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
    
         boolean jesamLiZadnja=false;
         int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
         if(stanje==1) jesamLiZadnja=true;
         faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
         uiMan.dodajElementUListu(faza4, 2);
         
             }
         ////////////////faza 13//////////////
         IDKoristeneFaze=this.idZadnjegSlota+"faza13";
         stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
         if(stanje==1||stanje==2){
        	 xZadKorFaz=xPiksCm*5.9f;
 			 yZadKorFaz=yPiksCm*2.5f;;

	     Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,13);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
         faza4.dodajParametreIzDB(13,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
    
         boolean jesamLiZadnja=false;
         int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
         if(stanje==1) jesamLiZadnja=true;
         faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
         uiMan.dodajElementUListu(faza4, 2);
         
             }
/////////////////////////
		   if(this.listaPlacenihObjekata.contains(IgricaActivity.imePlacenogObjektaNaInternetuFazaDodatna1)){
			   //////////////////////OVDJE UBACIVATI DODATNE FAZE KAO I PRIJE SA ISTIM MEHANIZMIMA
			   ///////////////14
			   IDKoristeneFaze=this.idZadnjegSlota+"faza14";
		         stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
		         if(stanje==1||stanje==2){
		  		   xZadKorFaz=xPiksCm*9.0f;
	               yZadKorFaz=yPiksCm*2.5f;;

			     Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,14);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
		         faza4.dodajParametreIzDB(14,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
		    
		         boolean jesamLiZadnja=false;
		         int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
		         if(stanje==1) jesamLiZadnja=true;
		         faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
		         uiMan.dodajElementUListu(faza4, 2);
		         
		           }
		  	   ///////////////15
				   IDKoristeneFaze=this.idZadnjegSlota+"faza15";
			         stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
			         if(stanje==1||stanje==2){
			  		   xZadKorFaz=xPiksCm*9.0f;
		               yZadKorFaz=yPiksCm*4.5f;;

				     Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,15);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
			         faza4.dodajParametreIzDB(15,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
			    
			         boolean jesamLiZadnja=false;
			         int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
			         if(stanje==1) jesamLiZadnja=true;
			         faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
			         uiMan.dodajElementUListu(faza4, 2);
			         
			             }
			  	   ///////////////16
					   IDKoristeneFaze=this.idZadnjegSlota+"faza16";
				         stanje=this.koristeneFazeStanje.get( IDKoristeneFaze);
				         if(stanje==1||stanje==2){
				  		   xZadKorFaz=xPiksCm*9.4f;
			               yZadKorFaz=yPiksCm*6.5f;;

					     Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,sprIzb,uiMan,16);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
				         faza4.dodajParametreIzDB(16,IDKoristeneFaze,idZadnjegSlota,this.koristeneFazeStanje.get(IDKoristeneFaze),this.koristeneFazeBrZvjezdica.get(IDKoristeneFaze));
				    
				         boolean jesamLiZadnja=false;
				         int dBTezina=this.koristeneFazeTezina.get(IDKoristeneFaze);
				         if(stanje==1) jesamLiZadnja=true;
				         faza4.namjestiStanjeFaze(jesamLiZadnja, dBTezina);
				         uiMan.dodajElementUListu(faza4, 2);
				         
				             }
		   }
		   else if(stanje!=1&&stanje!=0){
         ////////////////test placena faza//////////////
			   IDKoristeneFaze="kupovina";
			   xZadKorFaz=xPiksCm*9.0f;
               yZadKorFaz=yPiksCm*2.5f;;

	                      Faza faza4=new Faza( xZadKorFaz,yZadKorFaz, xPiksCm*0.8f, yPiksCm*0.8f,fazaSpr,uiMan,stvoriIzbornikSuportDeveloper(),4,13);// mora ime faze biti jednako kao i u DB i u gornjoj funkciji za pokretanje filmica 	
                          faza4.dodajParametreIzDB(0,IDKoristeneFaze,idZadnjegSlota,0,0);
    
                          boolean jesamLiZadnja=true;
                  
                          faza4.namjestiStanjeFaze(jesamLiZadnja, 1);
                         uiMan.dodajElementUListu(faza4, 2);
         
             }
/////////////////////////
	}
	private void ucitavanjeNekoristenihUpgradeBotuna(String IDSlota){
	    	tempUpgradeListaNekoristenih=new LinkedList<Integer>();
			// dodajev sve kao nekoristene pa cu ih poslije oduzimati
			tempUpgradeListaNekoristenih.addLast(1);tempUpgradeListaNekoristenih.addLast(2);tempUpgradeListaNekoristenih.addLast(3);tempUpgradeListaNekoristenih.addLast(4);
			tempUpgradeListaNekoristenih.addLast(5);tempUpgradeListaNekoristenih.addLast(6);tempUpgradeListaNekoristenih.addLast(7);tempUpgradeListaNekoristenih.addLast(8);
			tempUpgradeListaNekoristenih.addLast(9);tempUpgradeListaNekoristenih.addLast(10);tempUpgradeListaNekoristenih.addLast(11);tempUpgradeListaNekoristenih.addLast(12);
			tempUpgradeListaNekoristenih.addLast(13);tempUpgradeListaNekoristenih.addLast(14);tempUpgradeListaNekoristenih.addLast(15);tempUpgradeListaNekoristenih.addLast(16);
			tempUpgradeListaNekoristenih.addLast(17);tempUpgradeListaNekoristenih.addLast(18);tempUpgradeListaNekoristenih.addLast(19);tempUpgradeListaNekoristenih.addLast(20);
			if(this.listaUpgradsi.containsKey(IDSlota+1)){
				Integer inti=new Integer(17);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+2)){
				Integer inti=new Integer(13);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+3)){
				Integer inti=new Integer(9);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+4)){
				Integer inti=new Integer(5);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			
			if(this.listaUpgradsi.containsKey(IDSlota+5)){
				Integer inti=new Integer(1);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			
			
			
			if(this.listaUpgradsi.containsKey(IDSlota+6)){
				Integer inti=new Integer(18);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+7)){
				Integer inti=new Integer(14);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+8)){
				Integer inti=new Integer(10);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+9)){
				Integer inti=new Integer(6);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+10)){
				Integer inti=new Integer(2);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			
			
			
			if(this.listaUpgradsi.containsKey(IDSlota+11)){
				Integer inti=new Integer(19);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+12)){
				Integer inti=new Integer(15);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+13)){
				Integer inti=new Integer(11);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+14)){
				Integer inti=new Integer(7);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+15)){
				Integer inti=new Integer(3);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			
			
			
			if(this.listaUpgradsi.containsKey(IDSlota+16)){
				Integer inti=new Integer(20);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+17)){
				Integer inti=new Integer(16);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+18)){
				Integer inti=new Integer(12);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+19)){
				Integer inti=new Integer(8);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			if(this.listaUpgradsi.containsKey(IDSlota+20)){
				Integer inti=new Integer(4);
				tempUpgradeListaNekoristenih.remove(inti);
			}
			
	       
	    }   
    private void pokreniLoadIzbornik(){
    	 ///////////////////////////////////////////////////////////////////////////////////////////////////////
    	 SQLiteDatabase bazaPodataka;
         bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
         ///////
         Cursor cur=bazaPodataka.query(IgricaActivity.listaSlotova, null,null, null, null, null,IgricaActivity.IDSlota+ " ASC");
 	     cur.moveToFirst();
 	         String IDSlota1;
 	         String imeSlota1;
 	        IDSlota1=cur.getString(cur.getColumnIndex(IgricaActivity.IDSlota));
 	        imeSlota1=cur.getString(cur.getColumnIndex(IgricaActivity.imeSlota));
 	        cur.moveToNext();
 	       String IDSlota2;
	         String imeSlota2;
	        IDSlota2=cur.getString(cur.getColumnIndex(IgricaActivity.IDSlota));
	        imeSlota2=cur.getString(cur.getColumnIndex(IgricaActivity.imeSlota));
	        cur.moveToNext();
	        String IDSlota3;
	         String imeSlota3;
	        IDSlota3=cur.getString(cur.getColumnIndex(IgricaActivity.IDSlota));
	        imeSlota3=cur.getString(cur.getColumnIndex(IgricaActivity.imeSlota));
	        cur.moveToNext();
	        String IDSlota4;
	         String imeSlota4;
	        IDSlota4=cur.getString(cur.getColumnIndex(IgricaActivity.IDSlota));
	        imeSlota4=cur.getString(cur.getColumnIndex(IgricaActivity.imeSlota));
	        cur.close();
	        bazaPodataka.close();
	        ///////////////////////////////////////////////////////////////////////////////////////
    	IzbornikUniverzalni izLoad=new IzbornikUniverzalni(loadIzb,uiMan,5,1,302){
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 RectF rec=new RectF();
    		 GameEvent ge=new GameEvent(this);
    		 GameEvent geKut=new GameEvent(this);
    		 GameEvent geRazmo=new GameEvent(this);
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 this.getGlavniSprite().pustiZvukSamostalno(1);
    				 ge.jesamLiZiv=true;
    				 uiMan.iskljuciTouchEvente();
    			  rec.set(this.dajMiRectCijelogProzora());
    			  rec.offsetTo(rec.left, efekVis);
    			  top=efekVis-dajMiRectCijelogProzora().top;
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  }
    			if(!translacijaGotova){
    				translacijaGotova= loadIzb.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.5f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova= loadIzb.animacijaSlaganjeRotacijaVremenska(geKut,-90, 90,0.5f);
    			}
    			 tekPoc=loadIzb.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,0.7f, rec, null,false);
    			 if(tekPoc)uiMan.ukljuciTouchEvente();
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    					 this.getGlavniSprite().pustiZvukSamostalno(2);
    				  ge.jesamLiZiv=true;
    				  uiMan.iskljuciTouchEvente();
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;}
    			  if(!translacijaGotova){
      				translacijaGotova= loadIzb.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.5f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova= loadIzb.animacijaSlaganjeRotacijaVremenska(geKut,0, -90,0.5f);
      			  }
    			  tekPoc=loadIzb.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,0.7f, rec, null,false);
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			  uiMan.ukljuciTouchEvente();
	 				uiMan.makniObjekt(this);
	 				uiMan.postaviTempUniverzalniIzbornik(izbLoad);
	 				pokreniPocIzbornik();
	 				/*uiMan.postaviTempUniverzalniIzbornik(izPoc);
	 				uiMan.setOznacenSam(izPoc);*/
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
				  if(listaStanjaSlotova.get("slot1")==0){
					  pokreniIzborTezineLoadizbornik(this,"slot1");
				  }
				  else if(listaStanjaSlotova.get("slot1")==1){
					  pokreniStartThisSavedGameIzbornik(this,"slot1");
				  }
				}
				else if(brTP==2){
					if(listaStanjaSlotova.get("slot2")==0){
						pokreniIzborTezineLoadizbornik(this,"slot2");
					  }
					  else if(listaStanjaSlotova.get("slot2")==1){
						  pokreniStartThisSavedGameIzbornik(this,"slot2");
					  }
				}
				else if (brTP==3){
					if(listaStanjaSlotova.get("slot3")==0){
						pokreniIzborTezineLoadizbornik(this,"slot3");
					  }
					  else if(listaStanjaSlotova.get("slot3")==1){
						  pokreniStartThisSavedGameIzbornik(this,"slot3");
					  }
				}
                else if (brTP==4){
                	if (listaStanjaSlotova.get("slot4")==0){
                		pokreniIzborTezineLoadizbornik(this,"slot4");
  				  }
  				  else if(listaStanjaSlotova.get("slot4")==1){
  					pokreniStartThisSavedGameIzbornik(this,"slot4");
  				  }
				}
                else if (brTP==5){// mice ovaj izbornik i pokrece pocetni
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };
	    TextPaint tp2=new TextPaint();
		   tp2.setAntiAlias(true);
		   tp2.setTypeface(font);
		  tp2.setARGB(250,112, 0,0);
		   tp2.setStyle(Style.FILL); 
	
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		  tp.setARGB(250,26, 32,50);
		   tp.setStyle(Style.FILL);
		   izLoad.crtajIkadaNijePokrenut(true);
		   izLoad.ostaniUpaljenINakonEfektnogTouchaNaBotun();
		 //  izLoad.postaviIgnorirajKlikovePokrajIzbornika();
		   izLoad.postaviTextDugmica(0, IDSlota1+": "+ imeSlota1,tp);
		   izLoad.postaviTextDugmica(1, IDSlota2+": "+ imeSlota2,tp);
		   izLoad.postaviTextDugmica(2, IDSlota3+": "+ imeSlota3,tp);
		   izLoad.postaviTextDugmica(3, IDSlota4+ ": "+imeSlota4,tp);
		   izLoad.postaviTextDugmica(4, "<< Back",tp2);
		  
		   float visPolja= efekVis/9;
		   float razmakY= 3*visPolja/10;
		   float sirPolja=razmakY*6+5*visPolja;//4*efekSir/7;
		   izLoad.postaviLijeviVrhProzoraPix((efekSir-sirPolja-2*visPolja)/2, (efekVis-5*visPolja)/2);
		   izLoad.pokreniMojIzbornik(null);
		   izLoad.postaviRazmakIzmeduPoljaPix(0, razmakY);
		   izLoad.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   izLoad.postaviMargineLijevaDesnaGornjaDonjaPix( visPolja,  visPolja,  visPolja, visPolja);
		  uiMan.setOznacenSam(izLoad);
	   uiMan.postaviTempUniverzalniIzbornik(izLoad);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izLoad,3);
	   
    }
  ///////////11111111111111111111111
    private void stvoriAchievementsBotun(){ 
		
    	 izbBotunKarakteristike=new IzbornikUniverzalni( sprAchieBotun,uiMan,1,1,302){
            IzbornikUniverzalni upgProzor= pokreniKarakteristikeProzor(this);
    	
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==1){
					if(!pokrenutAchivmentBotun){
						upgProzor.pokreniMojIzbornik(null);
						uiMan.dodajElementUListu(upgProzor,4);
						uiMan.postaviTempUniverzalniIzbornik(upgProzor);
						pokrenutAchivmentBotun=true;
					}
					else{
						upgProzor.pokreniZavrsnuAnimaciju();
						
						pokrenutAchivmentBotun=false;
					}
				}
				
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
		

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
	    izbBotunKarakteristike.crtajNacrtajVanjskiIznadBezObzira(true);
		
	       
		   float sirPolja=this.velXDugKarakteristikePostoEkrana*efekVis/100;
		   float visPolja=sirPolja;
	
		   izbBotunKarakteristike.postaviStatickiIzbornik();
		   izbBotunKarakteristike.postaviLijeviVrhProzoraPix((efekSir-sirPolja), (this.velXDugMutePostoEkrana*this.efekVis/100));
		   izbBotunKarakteristike.pokreniMojIzbornik(null);
		   izbBotunKarakteristike.crtajIkadaNijePokrenut(true);
		   izbBotunKarakteristike.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		  
		   uiMan.dodajElementUListu( izbBotunKarakteristike,4);
	      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	      
	}
    
  /////////11111111111111111111111111111111
 
    private IzbornikUniverzalni pokreniKarakteristikeProzor(IzbornikUniverzalni pozivatelj){
  		final IzbornikUniverzalni poz=pozivatelj;
  		int brRed=2;
  		int brStup=2;
  		IzbornikUniverzalni izbor=new IzbornikUniverzalni( this.karakteristikaBotuni,uiMan,brRed,brStup,302){
      		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
      		 float pomakX,dxZaDugmic,prosliXZaDugmic=0;
      		 GameEvent ge=new GameEvent(this);
      		 RectF rec=new RectF();
      		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
     			  this.pokreniZavrsnuAnimaciju();
     		  }
      		  @Override
    		    public boolean	nacrtajUvod(float fps,Canvas can){
      			 
      			 if(tekPoc) {
      				 ge.jesamLiZiv=true;
      				 dxZaDugmic=0;
      				uiMan.makniObjekt(izbFacebook);
                      // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
      			   	pomakKarakteristikaManjiX=poz.dajMiRectCijelogProzora().width();
    	   	  	   	pomakKarakteristikaManjiY=0;
      				 this.postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX, 	pomakKarakteristikaManjiY);
      				 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
      			     tekPoc=false;
      			     this.getGlavniSprite().pustiZvukSamostalno(1);
      			     zavrsenaAnimacijaGlavnog=false;
      			     rotacijaGotova=false;
      			     translacijaGotova=false;
      			     uiMan.iskljuciTouchEvente();
      			     pomakX=can.getWidth()-this.dajMiRectCijelogProzora().left-uiMan.pomakCanvasaX();
      			     this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
      			     rec.set(this.dajMiRectCijelogProzora());
      		
      			     prosliXZaDugmic=rec.left;
      			  }
      			//if(!translacijaGotova){
      		
      				
      				prosliXZaDugmic=rec.left;
      				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
      				//rec.set(this.dajMiRectCijelogProzora());
      				tekPoc= sprAchieGlavniPr.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,-pomakX,0,1f,0, rec);
      				dxZaDugmic=rec.left-prosliXZaDugmic;
      				poz.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0);
      				nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
      				//}
      			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
      			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
    		    	 if(tekPoc){
    		    		crtajIkadaNijePokrenut(true);
    		    		zavrsenaAnimacijaGlavnog=true;
    		    		 uiMan.ukljuciTouchEvente();
    		    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
    		    		poz.pomakniIzbornikAkomulirajuciApsolutno(-(can.getWidth()-this.dajMiRectCijelogProzora().left)+uiMan.pomakCanvasaX(), 0);
    		    
    		    		//if(postojiUpgrade)reset.pokreniMojIzbornik(null);
    		    		uiMan.setOznacenSam(this);
    		    		//this.crtajProzorBezObziraNaSve(false);
    		    	 }
      			 return  tekPoc;
    		    	}
      		  public boolean nacrtajKraj(float fps,Canvas can){
      				 if(tekPoc) {
      					 zavrsenaAnimacijaGlavnog=false;
      					  ge.jesamLiZiv=true;
          				 dxZaDugmic=0;
          			     
                          // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
          				 //this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
          			     tekPoc=false;
          			     rotacijaGotova=false;
          			     translacijaGotova=false;
          			     uiMan.iskljuciTouchEvente();
          			   this.getGlavniSprite().pustiZvukSamostalno(2);
          			     pomakX=can.getWidth()-this.dajMiRectCijelogProzora().left-uiMan.pomakCanvasaX();
          			     //this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
          			     rec.set(this.dajMiRectCijelogProzora());
          			 	
          			     prosliXZaDugmic=rec.left;
          			    
          			  }
          			//if(!translacijaGotova){
          				
          				prosliXZaDugmic=rec.left;
          				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
          				//rec.set(this.dajMiRectCijelogProzora());
          				tekPoc= sprAchieGlavniPr.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,pomakX,0,1f,0, rec);
          				dxZaDugmic=rec.left-prosliXZaDugmic;
          				poz.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0);
          				nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
          				//}
          			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
          			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
        		    	 if(tekPoc){
        		     		 uiMan.dodajElementUListu( izbFacebook, 4);
        		    		crtajIkadaNijePokrenut(false);
        		    		zavrsenaAnimacijaGlavnog=true;
        		    		 uiMan.ukljuciTouchEvente();
        		    		this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
        		    		poz.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
        		    		//this.crtajProzorBezObziraNaSve(false);
        		    	 }
          			 return  tekPoc;
      			  
      		  }   
      		  public void zavrsenaZavrsnaAnimacija(){
      		
      			    uiMan.ukljuciTouchEvente();	
  	 				uiMan.makniObjekt(this);
  	 				
  	 				//pokreniPocIzbornik();
  	 				
  	 				uiMan.setOznacenSam(poz);
  	 				uiMan.postaviTempUniverzalniIzbornik(null);
      		  }
      		 @Override
  	 			public void backBotunStisnut(){
      			    
  	 				if(zavrsenaAnimacijaGlavnog==true)this.pokreniZavrsnuAnimaciju();
  	 			}
  			@Override
  			public void univerzalniIzvrsitelj(int brTP) {
  				if(brTP==1){
  					 pokreniKarakteristikeStrijelci(this);
  				}
  				else if(brTP==2){
  					 pokreniKarakteristikeKasarna(this);
  				}
  				else if(brTP==3){
  					 pokreniKarakteristikeMinobacac(this);
  				}
  				else if(brTP==4){
  					 pokreniKarakteristikeAlkemicar(this);
  				}
  		
  			}

  			@Override
  			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
  				// TODO Auto-generated method stub
  				
  			}
  			@Override
  			public RectF getGlavniRectPrikaza() {
  				RectF rect=new RectF();
  				rect.set(this.rec);
  				return rect;
  			}
  			
  	    	
  	    };
  	   
  	     Paint paintNekorBot= new Paint();
  		paintNekorBot.setAlpha(70);
  	    ColorMatrix cm = new ColorMatrix();
  	    cm.setSaturation(0);
  	    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
  		paintNekorBot.setColorFilter(filter);
  	
  	      izbor.postaviPaintNekoristenihBoruna(paintNekorBot);
  	   
  		 /* float sirPolja= (efekSir-poz.dajMiRectCijelogProzora().width())/10;
  		  float visPolja= efekVis/10;*/
  		  marginaKarakteristika= 2*efekVis/10;
  		  float sirPolja=(efekSir-this.velXDugKarakteristikePostoEkrana*this.efekVis/100-	  marginaKarakteristika*(brStup+1))/brStup ;
  		  float visPolja=(efekVis-  marginaKarakteristika*(brRed+1))/brRed;
  
  		  float razmakY=0;
  	
  		   //if(desnaMargina<0) desnaMargina=sirPolja;

  		  izbor.postaviStatickiIzbornik();
  //		  izbor.postaviMargineLijevaDesnaGornjaDonjaPix( 	1.2f*marginaKarakteristika,1.2f*marginaKarakteristika,	marginaKarakteristika, 	marginaKarakteristika);
  		 izbor.postaviRazmakIzmeduPoljaPix( marginaKarakteristika, marginaKarakteristika);
  		  // izbor.postaviLijeviVrhProzoraPix(efekSir/5, efekVis/5);
  		  izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
  		  ucitavanjeNekoristenihUpgradeBotuna(this.idZadnjegSlota);
  		  izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
  		 // izbor.postaviIgnorirajKlikovePokrajIzbornika();

  		
  	      uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 
  	     // izbor.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width()/2, 0);
  	      uiMan.dodajElementUListu(izbor,4);
  	      izbor.regirajINaNekoristenaPolja();// crtat ce ih kao nekoristena ali ce reagirati na dodir, a ja cu poslije odlucivati sto cu napraviti s time
  	   
  	      return izbor;
  	}  
   private IzbornikUniverzalni pokreniAchievementsProzor(IzbornikUniverzalni pozivatelj){
		final IzbornikUniverzalni poz=pozivatelj;
		
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprAchieGlavniPr,uiMan,5,4,302){
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float pomakX,dxZaDugmic,prosliXZaDugmic=0;
    		 GameEvent ge=new GameEvent(this);
    		 RectF rec=new RectF();
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 ge.jesamLiZiv=true;
    				 dxZaDugmic=0;
    			     
                    // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
    				 this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
    				 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
    			     tekPoc=false;
    			     zavrsenaAnimacijaGlavnog=false;
    			     rotacijaGotova=false;
    			     translacijaGotova=false;
    			     uiMan.iskljuciTouchEvente();
    			     pomakX=can.getWidth()-this.dajMiRectCijelogProzora().left-uiMan.pomakCanvasaX();
    			     this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
    			     rec.set(this.dajMiRectCijelogProzora());
    		
    			     prosliXZaDugmic=rec.left;
    			  }
    			//if(!translacijaGotova){
    		
    				
    				prosliXZaDugmic=rec.left;
    				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
    				//rec.set(this.dajMiRectCijelogProzora());
    				tekPoc= sprAchieGlavniPr.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,-pomakX,0,1f,0, rec);
    				dxZaDugmic=rec.left-prosliXZaDugmic;
    				poz.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0);
    				nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
    				//}
    			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
    			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
  		    	 if(tekPoc){
  		    		crtajIkadaNijePokrenut(true);
  		    		zavrsenaAnimacijaGlavnog=true;
  		    		 uiMan.ukljuciTouchEvente();
  		    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
  		    		poz.pomakniIzbornikAkomulirajuciApsolutno(-(can.getWidth()-this.dajMiRectCijelogProzora().left)+uiMan.pomakCanvasaX(), 0);
  		    		//if(postojiUpgrade)reset.pokreniMojIzbornik(null);
  		    		uiMan.setOznacenSam(this);
  		    		//this.crtajProzorBezObziraNaSve(false);
  		    	 }
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    				 if(tekPoc) {
    					 zavrsenaAnimacijaGlavnog=false;
    					  ge.jesamLiZiv=true;
        				 dxZaDugmic=0;
        			     
                        // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
        				 //this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
        			     tekPoc=false;
        			     rotacijaGotova=false;
        			     translacijaGotova=false;
        			     uiMan.iskljuciTouchEvente();
        			     pomakX=can.getWidth()-this.dajMiRectCijelogProzora().left-uiMan.pomakCanvasaX();
        			     //this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
        			     rec.set(this.dajMiRectCijelogProzora());
        			 	
        			     prosliXZaDugmic=rec.left;
        			    
        			  }
        			//if(!translacijaGotova){
        				
        				prosliXZaDugmic=rec.left;
        				this.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0); 
        				//rec.set(this.dajMiRectCijelogProzora());
        				tekPoc= sprAchieGlavniPr.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,pomakX,0,1f,0, rec);
        				dxZaDugmic=rec.left-prosliXZaDugmic;
        				poz.pomakniIzbornikAkomulirajuci(dxZaDugmic, 0);
        				nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
        				//}
        			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
        			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
      		    	 if(tekPoc){
      		    		crtajIkadaNijePokrenut(false);
      		    		zavrsenaAnimacijaGlavnog=true;
      		    		 uiMan.ukljuciTouchEvente();
      		    		this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
      		    		poz.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
      		    		//this.crtajProzorBezObziraNaSve(false);
      		    	 }
        			 return  tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    		
    			    uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				
	 				//pokreniPocIzbornik();
	 				
	 				uiMan.setOznacenSam(poz);
	 				uiMan.postaviTempUniverzalniIzbornik(null);
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			    
	 				if(zavrsenaAnimacijaGlavnog==true)this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				if(brTP==1){
					String naslov="None shall pass!";
					String red1="-no enemy passed your ";
					String red2=" defences";
					String red3="-reinforcement ability";
					pokreniAchievementsInfo( this,naslov, red1,red2,red3);
				}
                
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
			
	    	
	    };
	   
	     Paint paintNekorBot= new Paint();
		paintNekorBot.setAlpha(70);
	    ColorMatrix cm = new ColorMatrix();
	    cm.setSaturation(0);
	    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
		paintNekorBot.setColorFilter(filter);
	
	      izbor.postaviPaintNekoristenihBoruna(paintNekorBot);
		 /* float sirPolja= (efekSir-poz.dajMiRectCijelogProzora().width())/10;
		  float visPolja= efekVis/10;*/
		  float sirPolja=efekVis/6 ;
		  float visPolja= efekVis/6;
		  float razmakY=0;
		  float desnaMargina= sirPolja/7;
		   //if(desnaMargina<0) desnaMargina=sirPolja;

		  izbor.postaviStatickiIzbornik();
		  izbor.postaviMargineLijevaDesnaGornjaDonjaPix( desnaMargina,desnaMargina,desnaMargina, desnaMargina);
		  // izbor.postaviLijeviVrhProzoraPix(efekSir/5, efekVis/5);

		  izbor.postaviRazmakIzmeduPoljaPix(desnaMargina,desnaMargina);
		  izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		  ucitavanjeNekoristenihUpgradeBotuna(this.idZadnjegSlota);
		  izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
		 // izbor.postaviIgnorirajKlikovePokrajIzbornika();
		  lodirajAchievementse();  
		  LinkedList tempListaNekoristenih=new LinkedList<Integer>();
		  if(listaAchievementsa.contains(IgricaActivity.achievementNoOneShellPas)){
			  izbor.postaviDodatnuSlicicuZaDugmic(1, sprAchiIkone, 0,0,0);
			  tempListaNekoristenih.add(1);
			  
		  } 
		  izbor.izmjenilistuNekoristenih(tempListaNekoristenih);
		
	      uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 
	     // izbor.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width()/2, 0);
	      uiMan.dodajElementUListu(izbor,4);
	      izbor.regirajINaNekoristenaPolja();// crtat ce ih kao nekoristena ali ce reagirati na dodir, a ja cu poslije odlucivati sto cu napraviti s time
	   
	      return izbor;
	} 
    private void lodirajAchievementse(){
		/////lodiranje upgradsa
		this.listaAchievementsa=new LinkedList<String>();

		SQLiteDatabase bazaPodataka;
		bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
		
		Cursor cur4=bazaPodataka.query(IgricaActivity.listaAchievementsa, null,null, null, null, null, null);
		cur4.moveToFirst();  
		
		while(cur4.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
        	    String IDAchi=cur4.getString(cur4.getColumnIndex(IgricaActivity.IDAchievementsa));	
        	   
        	    if(cur4.getString(cur4.getColumnIndex(IgricaActivity.IDSlota)).equals(this.idZadnjegSlota)){
        	    	 this.listaAchievementsa.addLast(IDAchi);// 
        	    }
        	    
 
     	    cur4.moveToNext();// za pomicanje u bazi podataka   
           
              }
		cur4.close();
		 bazaPodataka.close();
		
	}
    private void lodirajkKupljeneElemente(){

  		this.listaPlacenihObjekata=new LinkedList<String>();

  		SQLiteDatabase bazaPodataka;
  		bazaPodataka= this.openOrCreateDatabase(IgricaActivity.glavniDB,this.MODE_PRIVATE, null);
  		
  		Cursor cur4=bazaPodataka.query(IgricaActivity.listaPlacenihElemenata, null,null, null, null, null, null);
  		cur4.moveToFirst();  
  		
  		while(cur4.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
          	    String IDAchi=cur4.getString(cur4.getColumnIndex(IgricaActivity.IDPlacenogObjekta));	
          	 
          	    	 this.listaPlacenihObjekata.addLast(IDAchi);// 
          	    
          	    
   
       	    cur4.moveToNext();// za pomicanje u bazi podataka   
             
                }
  		
  		cur4.close();
  		 bazaPodataka.close();
  		
  	}
    private void pokreniAchievementsInfo(IzbornikUniverzalni pozivatelj,String naslov, String red1,String red2,String red3){
		final IzbornikUniverzalni poz=pozivatelj;
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( sprUpgSpor,uiMan,5,1,302){
    		 boolean tekPoc=true,rotacijaGotova=false,translacijaGotova=false;
    		 float top;
    		 RectF rec=new RectF();
    		 GameEvent ge=new GameEvent(this);
    		 GameEvent geKut=new GameEvent(this);
    		 GameEvent geRazmo=new GameEvent(this);
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 ge.jesamLiZiv=true;
    				 this.postaviLijeviVrhProzoraPix((efekSir-this.dajMiRectCijelogProzora().width())/2, (efekVis-this.dajMiRectCijelogProzora().height())/2);
    			  rec.set(this.dajMiRectCijelogProzora());
    			  
    			  top=efekVis-this.dajMiPomakCanvasaY()-dajMiRectCijelogProzora().top;
    			  rec.offsetTo(rec.left, efekVis-this.dajMiPomakCanvasaY());
    			  tekPoc=false;
    			  rotacijaGotova=false;
    			  translacijaGotova=false;
    			  uiMan.iskljuciTouchEvente();
    			  }
    			if(!translacijaGotova){
    				translacijaGotova=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-top, 0,0.9f, rec);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova=  sprUpgSpor.animacijaSlaganjeRotacijaVremenska( geKut, -90, 90,0.9f);
    			}
    			 tekPoc= sprUpgSpor.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,1f, rec, null,false);
  		    	 if(tekPoc) uiMan.ukljuciTouchEvente();	
    			 return  tekPoc;
  		    	}
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    				  ge.jesamLiZiv=true;
    				  uiMan.iskljuciTouchEvente();	
        			  rec.set(this.dajMiRectCijelogProzora());
        			  top=efekVis;
        			  tekPoc=false;
        			  rotacijaGotova=false;
        			  translacijaGotova=false;}
    			  if(!translacijaGotova){
      				translacijaGotova=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,top, 0,0.9f, rec);
      			  }
      			  if(!rotacijaGotova){
      				rotacijaGotova=  sprUpgSpor.animacijaSlaganjeRotacijaVremenska(geKut,0, -90,0.9f);
      			  }
    			  tekPoc= sprUpgSpor.animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(can,geRazmo,geKut, 1, 0, 10, 10,1f, rec, null,false);
    			  return tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			  uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
	 				uiMan.setOznacenSam(poz);
	 				uiMan.postaviTempUniverzalniIzbornik(poz);
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
		
			    if (brTP==5
			    		){
					//uiMan.makniObjekt(izLoad);
					this.pokreniZavrsnuAnimaciju();
					//pokreniPocIzbornik();
				}
			}

			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rec);
				return rect;
			}
	    	
	    };

	
		  TextPaint tp=new TextPaint();
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(250, 56, 48,79);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		   TextPaint tp2=new TextPaint();
		   tp2.set(tp);
		   tp2.setARGB(250, 134, 1, 1);
		   izbor.postaviStatickiIzbornik();
		 //  izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   izbor.postaviTextDugmica(0, red1,tp);
		   izbor.postaviTextDugmica(1, red2,tp);
           izbor.postaviTextDugmica(2, red3,tp);
           izbor.postaviTextDugmica(3,"",tp);
           izbor.postaviTextDugmica(4, "Back",tp2);
		  // izbor.postaviTextDugmica(2, "<< Back",tp);
		   float sirPolja=2*efekSir/3;
		   float visPolja= efekVis/9;
		   float razmakY=0;
		   float razmakX= 2*visPolja/10;
		   float desnaMargina=sirPolja/4;
		   izbor.postaviMargineLijevaDesnaGornjaDonjaPix( sirPolja/10,sirPolja/10, visPolja*2, visPolja/2);
		   izbor.postaviLijeviVrhProzoraPix((efekSir-izbor.dajMiRectCijelogProzora().width())/2, (efekVis-izbor.dajMiRectCijelogProzora().height())/2);
		   izbor.postaviNaslovIzbornikaPix(naslov, sirPolja,visPolja,sirPolja/10, visPolja, tp);
		   izbor.pokreniMojIzbornik(null);
		   izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   uiMan.setOznacenSam(izbor);
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izbor,4);
	}
    private void pokreniKarakteristikeAlkemicar(IzbornikUniverzalni pozivatelj){
		final IzbornikUniverzalni poz=pozivatelj;

		//final int brUpgradaFinal=brUpgrada;
		final  TextPaint tp=new TextPaint();
		final  TextPaint tpValor=new TextPaint();
          int brRedova=8;
          int brStupaca=6;
		IzbornikUniverzalni izbor=new IzbornikUniverzalni( this.karakteristikaAlkemicar,uiMan,brRedova, brStupaca,302){
    		 boolean tekPoc=true;
    		 float top;
    	     RectF rec=new RectF();
    	     RectF rec1=new RectF();
    		 GameEvent geKut=new GameEvent(this);
    		 GameEvent geRazmo=new GameEvent(this);
    		 GameEvent ge=new GameEvent(this);
    		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
   			  this.pokreniZavrsnuAnimaciju();
   		  }
    		  @Override
  		    public boolean	nacrtajUvod(float fps,Canvas can){
    			 
    			 if(tekPoc) {
    				 ge.jesamLiZiv=true;
    				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
    			
    			  
    			  top=0;
    			  rec.set(0,0, 5, 5);
    			  tekPoc=false;
    
    			  }
    			if(!tekPoc){
    				tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,255, 0,1f, rec);
    			}
    			this.setAlfaNaslova((int)rec.top);
      			this.setAlfaSvihDugmica((int)rec.top);
    			this.setAlfaGlavnogProzora((int)rec.top);  	
    			this.nacrtajProzor888();
    			if(tekPoc) uiMan.ukljuciTouchEvente();	
    			 return  tekPoc;
  		    	}
    		  
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    				  ge.jesamLiZiv=true;
     				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
     			
     			  
     			  top=0;
     			  rec.set(0,255, 5, 300);
     			  tekPoc=false;
     	
        			  }
    				if(!tekPoc){
    					tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-255, 0,1f, rec);
        			}
    				this.setAlfaNaslova((int)rec.top);
          			this.setAlfaSvihDugmica((int)rec.top);
        			this.setAlfaGlavnogProzora((int)rec.top);  		 
        			this.nacrtajProzor888();
        			if(tekPoc) uiMan.ukljuciTouchEvente();	
        			 return  tekPoc;
    			  
    		  }   
    		  public void zavrsenaZavrsnaAnimacija(){
    			  uiMan.ukljuciTouchEvente();	
	 				uiMan.makniObjekt(this);
	 				//pokreniPocIzbornik();
	 				uiMan.setOznacenSam(poz);
	 				uiMan.postaviTempUniverzalniIzbornik(poz);
    		  }
    		 @Override
	 			public void backBotunStisnut(){
    			 
	 				this.pokreniZavrsnuAnimaciju();
	 			}
			@Override
			public void univerzalniIzvrsitelj(int brTP) {
				// TODO Auto-generated method stub
				if(brTP==44||brTP==45||brTP==46||brTP==47){
				
					this.pokreniZavrsnuAnimaciju();
					
					
				}
			
			}
			@Override
			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		        
			}
			@Override
			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		          
			}
			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.dajMiRectCijelogProzora());
				return rect;
			}
	    	
	    };
	    
	    tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(250, 255,168,0);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		   TextPaint tp2=new TextPaint();
		   TextPaint tp1=new TextPaint();
		   tpValor .set(tp);
		   tpValor .setARGB(250, 238, 190, 0);
		   tp2.set(tp);
		   tp2.setARGB(250,11, 11,11);
		   tp1.set(tp);
		   tp1.setARGB(250, 249,249, 249);
		   izbor.postaviStatickiIzbornik();
		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
		   
		   Paint  paint =new Paint();
	  	    paint.setAlpha(180);
		   izbor.setPaintZaGlavniProzor(paint);
		   izbor.postaviTextDugmica(0,"Pros:",tp1);
		   izbor.postaviTextDugmica(6, "-ignores armor",tp1);
		   izbor.postaviTextDugmica(12, "-freeze effect",tp1);
		   izbor.postaviTextDugmica(18, "Cons:",tp2);
		   izbor.postaviTextDugmica(24, "-slow rate of fire",tp2);
		   izbor.postaviTextDugmica(30, "-smaller radius",tp2);
		   
		   
		   
		   tpValor .setARGB(250, 226, 133, 0);
		   izbor.postaviTextDugmica(44, "<<back", tpValor );
	       izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
		   float sirPolja=(pozivatelj.getGlavniRectPrikaza().width()-this.marginaKarakteristika)/brStupaca;
		   float visPolja=( pozivatelj.getGlavniRectPrikaza().height()-this.marginaKarakteristika)/brRedova;
		   float razmakY=0;
		   izbor.postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
		   //izbor.postaviNaslovIzbornikaPix("Drill sergeant:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
		   izbor.postaviNaslovIzbornikaPix("Alkemist",3*sirPolja,visPolja, sirPolja, -visPolja, tp);
		   izbor.pokreniMojIzbornik(null);
		 //  izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		   uiMan.setOznacenSam(izbor);
	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
	   uiMan.dodajElementUListu(izbor,4);
	}
    
    private void pokreniKarakteristikeStrijelci(IzbornikUniverzalni pozivatelj){
  		final IzbornikUniverzalni poz=pozivatelj;

  		//final int brUpgradaFinal=brUpgrada;
  		final  TextPaint tp=new TextPaint();
  		final  TextPaint tpValor=new TextPaint();
            int brRedova=8;
            int brStupaca=6;
  		IzbornikUniverzalni izbor=new IzbornikUniverzalni( this.karakteristikaStrijelci,uiMan,brRedova, brStupaca,302){
      		 boolean tekPoc=true;
      		 float top;
      	     RectF rec=new RectF();
      	     RectF rec1=new RectF();
      		 GameEvent geKut=new GameEvent(this);
      		 GameEvent geRazmo=new GameEvent(this);
      		 GameEvent ge=new GameEvent(this);
      		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
     			  this.pokreniZavrsnuAnimaciju();
     		  }
      		  @Override
    		    public boolean	nacrtajUvod(float fps,Canvas can){
      			 
      			 if(tekPoc) {
      				 ge.jesamLiZiv=true;
      				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
      			
      			  
      			  top=0;
      			  rec.set(0,0, 5, 5);
      			  tekPoc=false;
      
      			  }
      			if(!tekPoc){
      				tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,255, 0,1f, rec);
      			}
      			this.setAlfaNaslova((int)rec.top);
      			this.setAlfaSvihDugmica((int)rec.top);
      			this.setAlfaGlavnogProzora((int)rec.top);  	
      			this.nacrtajProzor888();
      			if(tekPoc) uiMan.ukljuciTouchEvente();	
      			 return  tekPoc;
    		    	}
      		  public boolean nacrtajKraj(float fps,Canvas can){
      			  if(tekPoc) {
      				  ge.jesamLiZiv=true;
       				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
       			
       			  
       			  top=0;
       			  rec.set(0,255, 5, 300);
       			  tekPoc=false;
       	
          			  }
      				if(!tekPoc){
      					tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-255, 0,1f, rec);
          			}
      				this.setAlfaNaslova((int)rec.top);
          			this.setAlfaSvihDugmica((int)rec.top);
          			this.setAlfaGlavnogProzora((int)rec.top);  		 
          			this.nacrtajProzor888();
          			if(tekPoc) uiMan.ukljuciTouchEvente();	
          			 return  tekPoc;
      			  
      		  }   
      		  public void zavrsenaZavrsnaAnimacija(){
      			  uiMan.ukljuciTouchEvente();	
  	 				uiMan.makniObjekt(this);
  	 				//pokreniPocIzbornik();
  	 				uiMan.setOznacenSam(poz);
  	 				uiMan.postaviTempUniverzalniIzbornik(poz);
      		  }
      		 @Override
  	 			public void backBotunStisnut(){
      			 
  	 				this.pokreniZavrsnuAnimaciju();
  	 			}
  			@Override
  			public void univerzalniIzvrsitelj(int brTP) {
  				// TODO Auto-generated method stub
  				if(brTP==44||brTP==45||brTP==46||brTP==47){
  				
  					this.pokreniZavrsnuAnimaciju();
  					
  					
  				}
  			
  			}
  			@Override
  			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
  		        
  			}
  			@Override
  			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
  		          
  			}
  			@Override
  			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
  				// TODO Auto-generated method stub
  				
  			}
  			@Override
  			public RectF getGlavniRectPrikaza() {
  				RectF rect=new RectF();
  				rect.set(this.dajMiRectCijelogProzora());
  				return rect;
  			}
  	    	
  	    };
  	    
  	   tp.setAntiAlias(true);
	   tp.setTypeface(font);
	   tp.setARGB(250, 255,168,0);
	  //tp.setColor(Color.BLUE);
	   tp.setStyle(Style.FILL);
	   TextPaint tp2=new TextPaint();
	   TextPaint tp1=new TextPaint();
	   tpValor .set(tp);
	   tpValor .setARGB(250, 238, 190, 0);
	   tp2.set(tp);
	   tp2.setARGB(250,11,11, 11);
	   tp1.set(tp);
	   tp1.setARGB(250, 249,249, 249);
  		   izbor.postaviStatickiIzbornik();
  		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
  		   
  		   Paint  paint =new Paint();
  	  	    paint.setAlpha(180);
  		   izbor.setPaintZaGlavniProzor(paint);
  		   izbor.postaviTextDugmica(0,"Pros:",tp1);
  		   izbor.postaviTextDugmica(6, "-rate of fire",tp1);
  		   izbor.postaviTextDugmica(12, "-radius",tp1);
  		   izbor.postaviTextDugmica(18, "Cons:",tp2);
  		   izbor.postaviTextDugmica(24, "-bad vs armor",tp2);
  		
  		   
  		   tpValor .setARGB(250, 226, 133, 0);
		   izbor.postaviTextDugmica(44, "<<back", tpValor );
  	       izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
  		   float sirPolja=(pozivatelj.getGlavniRectPrikaza().width()-this.marginaKarakteristika)/brStupaca;
  		   float visPolja=( pozivatelj.getGlavniRectPrikaza().height()-this.marginaKarakteristika)/brRedova;
  		   float razmakY=0;
  		   izbor.postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
  		   //izbor.postaviNaslovIzbornikaPix("Drill sergeant:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
  		   izbor.postaviNaslovIzbornikaPix("Archers",3*sirPolja,visPolja, sirPolja, -visPolja, tp);
  		   izbor.pokreniMojIzbornik(null);
  		 //  izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
  		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
  		   uiMan.setOznacenSam(izbor);
  	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
  	   uiMan.dodajElementUListu(izbor,4);
  	}

    private void pokreniKarakteristikeKasarna(IzbornikUniverzalni pozivatelj){
  		final IzbornikUniverzalni poz=pozivatelj;

  		//final int brUpgradaFinal=brUpgrada;
  		final  TextPaint tp=new TextPaint();
  		final  TextPaint tpValor=new TextPaint();
            int brRedova=8;
            int brStupaca=6;
  		IzbornikUniverzalni izbor=new IzbornikUniverzalni( this.karakteristikaKasarna,uiMan,brRedova, brStupaca,302){
      		 boolean tekPoc=true;
      		 float top;
      	     RectF rec=new RectF();
      	     RectF rec1=new RectF();
      		 GameEvent geKut=new GameEvent(this);
      		 GameEvent geRazmo=new GameEvent(this);
      		 GameEvent ge=new GameEvent(this);
      		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
     			  this.pokreniZavrsnuAnimaciju();
     		  }
      		  @Override
    		    public boolean	nacrtajUvod(float fps,Canvas can){
      			 
      			 if(tekPoc) {
      				 ge.jesamLiZiv=true;
      				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
      			
      			  
      			  top=0;
      			  rec.set(0,0, 5, 5);
      			  tekPoc=false;
      
      			  }
      			if(!tekPoc){
      				tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,255, 0,1f, rec);
      			}
      			this.setAlfaGlavnogProzora((int)rec.top);  	
      			this.setAlfaNaslova((int)rec.top);
      			this.setAlfaSvihDugmica((int)rec.top);
      			this.nacrtajProzor888();
      			if(tekPoc) uiMan.ukljuciTouchEvente();	
      			 return  tekPoc;
    		    	}
      		  public boolean nacrtajKraj(float fps,Canvas can){
      			  if(tekPoc) {
      				  ge.jesamLiZiv=true;
       				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
       			
       			  
       			  top=0;
       			  rec.set(0,255, 5, 300);
       			  tekPoc=false;
       	
          			  }
      				if(!tekPoc){
      					tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-255, 0,1f, rec);
          			}
      				this.setAlfaNaslova((int)rec.top);
          			this.setAlfaSvihDugmica((int)rec.top);
          			this.setAlfaGlavnogProzora((int)rec.top);  		 
          			this.nacrtajProzor888();
          			if(tekPoc) uiMan.ukljuciTouchEvente();	
          			 return  tekPoc;
      			  
      		  }   
      		  public void zavrsenaZavrsnaAnimacija(){
      			  uiMan.ukljuciTouchEvente();	
  	 				uiMan.makniObjekt(this);
  	 				//pokreniPocIzbornik();
  	 				uiMan.setOznacenSam(poz);
  	 				uiMan.postaviTempUniverzalniIzbornik(poz);
      		  }
      		 @Override
  	 			public void backBotunStisnut(){
      			 
  	 				this.pokreniZavrsnuAnimaciju();
  	 			}
  			@Override
  			public void univerzalniIzvrsitelj(int brTP) {
  				// TODO Auto-generated method stub
  				if(brTP==44||brTP==45||brTP==46||brTP==47){
  				
  					this.pokreniZavrsnuAnimaciju();
  					
  					
  				}
  			
  			}
  			@Override
  			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
  		        
  			}
  			@Override
  			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
  		          
  			}
  			@Override
  			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
  				// TODO Auto-generated method stub
  				
  			}
  			@Override
  			public RectF getGlavniRectPrikaza() {
  				RectF rect=new RectF();
  				rect.set(this.dajMiRectCijelogProzora());
  				return rect;
  			}
  	    	
  	    };
  	    
  	   tp.setAntiAlias(true);
	   tp.setTypeface(font);
	   tp.setARGB(250, 255,168,0);
	  //tp.setColor(Color.BLUE);
	   tp.setStyle(Style.FILL);
	   TextPaint tp2=new TextPaint();
	   TextPaint tp1=new TextPaint();
	   tpValor .set(tp);
	   tpValor .setARGB(250, 238, 190, 0);
	   tp2.set(tp);
	   tp2.setARGB(250, 11, 11, 11);
	   tp1.set(tp);
	   tp1.setARGB(250, 249,249, 249);
  		   izbor.postaviStatickiIzbornik();
  		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
  		   
  		   Paint  paint =new Paint();
  	  	    paint.setAlpha(180);
  		   izbor.setPaintZaGlavniProzor(paint);
  		   izbor.postaviTextDugmica(0,"Pros:",tp1);
  		   izbor.postaviTextDugmica(6, "-stops opponents",tp1);
  		   izbor.postaviTextDugmica(18, "Cons:",tp2);
  		   izbor.postaviTextDugmica(24, "-low damage ",tp2);
  		   izbor.postaviTextDugmica(30, "-smaller radius",tp2);
  		   
  		   tpValor .setARGB(250, 226, 133, 0);
		   izbor.postaviTextDugmica(44, "<<back", tpValor );
  	       izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
  		   float sirPolja=(pozivatelj.getGlavniRectPrikaza().width()-this.marginaKarakteristika)/brStupaca;
  		   float visPolja=( pozivatelj.getGlavniRectPrikaza().height()-this.marginaKarakteristika)/brRedova;
  		   float razmakY=0;
  		   izbor.postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
  		   //izbor.postaviNaslovIzbornikaPix("Drill sergeant:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
  		   izbor.postaviNaslovIzbornikaPix("Barracks",3*sirPolja,visPolja, sirPolja, -visPolja, tp);
  		   izbor.pokreniMojIzbornik(null);
  		 //  izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
  		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
  		   uiMan.setOznacenSam(izbor);
  	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
  	   uiMan.dodajElementUListu(izbor,4);
  	}
    private void pokreniKarakteristikeMinobacac(IzbornikUniverzalni pozivatelj){
  		final IzbornikUniverzalni poz=pozivatelj;

  		//final int brUpgradaFinal=brUpgrada;
  		final  TextPaint tp=new TextPaint();
  		final  TextPaint tpValor=new TextPaint();
            int brRedova=8;
            int brStupaca=6;
  		IzbornikUniverzalni izbor=new IzbornikUniverzalni( this.karakteristikaMinobacac,uiMan,brRedova, brStupaca,302){
      		 boolean tekPoc=true;
      		 float top;
      	     RectF rec=new RectF();
      	     RectF rec1=new RectF();
      		 GameEvent geKut=new GameEvent(this);
      		 GameEvent geRazmo=new GameEvent(this);
      		 GameEvent ge=new GameEvent(this);
      		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
     			  this.pokreniZavrsnuAnimaciju();
     		  }
      		  @Override
    		    public boolean	nacrtajUvod(float fps,Canvas can){
      			 
      			 if(tekPoc) {
      				 ge.jesamLiZiv=true;
      				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
      			
      			  
      			  top=0;
      			  rec.set(0,0, 5, 5);
      			  tekPoc=false;
      
      			  }
      			if(!tekPoc){
      				tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,255, 0,1f, rec);
      			}
      			this.setAlfaGlavnogProzora((int)rec.top);  	
      			this.setAlfaNaslova((int)rec.top);
      			this.setAlfaSvihDugmica((int)rec.top);
      			this.nacrtajProzor888();
      			if(tekPoc) uiMan.ukljuciTouchEvente();	
      			 return  tekPoc;
    		    	}
      		  public boolean nacrtajKraj(float fps,Canvas can){
      			  if(tekPoc) {
      				  ge.jesamLiZiv=true;
       				 postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
       			
       			  
       			  top=0;
       			  rec.set(0,255, 5, 300);
       			  tekPoc=false;
       	
          			  }
      				if(!tekPoc){
      					tekPoc=  sprUpgSpor.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-255, 0,1f, rec);
          			}
      		
          			this.setAlfaGlavnogProzora((int)rec.top);  	
          			this.setAlfaNaslova((int)rec.top);
          			this.setAlfaSvihDugmica((int)rec.top);
          			this.nacrtajProzor888();
          			if(tekPoc) uiMan.ukljuciTouchEvente();	
          			 return  tekPoc;
      			  
      		  }   
      		  public void zavrsenaZavrsnaAnimacija(){
      			  uiMan.ukljuciTouchEvente();	
  	 				uiMan.makniObjekt(this);
  	 				//pokreniPocIzbornik();
  	 				uiMan.setOznacenSam(poz);
  	 				uiMan.postaviTempUniverzalniIzbornik(poz);
      		  }
      		 @Override
  	 			public void backBotunStisnut(){
      			 
  	 				this.pokreniZavrsnuAnimaciju();
  	 			}
  			@Override
  			public void univerzalniIzvrsitelj(int brTP) {
  				// TODO Auto-generated method stub
  				if(brTP==44||brTP==45||brTP==46||brTP==47){
  				
  					this.pokreniZavrsnuAnimaciju();
  					
  					
  				}
  			
  			}
  			@Override
  			public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
  		        
  			}
  			@Override
  			public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
  		          
  			}
  			@Override
  			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
  				// TODO Auto-generated method stub
  				
  			}
  			@Override
  			public RectF getGlavniRectPrikaza() {
  				RectF rect=new RectF();
  				rect.set(this.dajMiRectCijelogProzora());
  				return rect;
  			}
  	    	
  	    };
  	    
		   tp.setAntiAlias(true);
		   tp.setTypeface(font);
		   tp.setARGB(250, 255,168,0);
		  //tp.setColor(Color.BLUE);
		   tp.setStyle(Style.FILL);
		   TextPaint tp2=new TextPaint();
		   TextPaint tp1=new TextPaint();
		   tpValor .set(tp);
		   tpValor .setARGB(250, 238, 190, 0);
		   tp2.set(tp);
		   tp2.setARGB(250,11,11, 11);
		   tp1.set(tp);
		   tp1.setARGB(250, 249,249, 249);
  		   izbor.postaviStatickiIzbornik();
  		  // izbor.postaviIgnorirajKlikovePokrajIzbornika();
  		   
  		   Paint  paint =new Paint();
  	  	    paint.setAlpha(180);
  		   izbor.setPaintZaGlavniProzor(paint);
  		   izbor.postaviTextDugmica(0,"Pros:",tp1);
  		   izbor.postaviTextDugmica(6, "-damages multiple",tp1);
		   izbor.postaviTextDugmica(12, "-radius",tp1);
  		   izbor.postaviTextDugmica(18, "Cons:",tp2);
  		   izbor.postaviTextDugmica(24, "-slow rate of fire",tp2);
  		   izbor.postaviTextDugmica(30, "-can't attack air",tp2);
  		   
  		   tpValor .setARGB(250, 226, 133, 0);
		   izbor.postaviTextDugmica(44, "<<back", tpValor );
  	       izbor.ostaniUpaljenINakonEfektnogTouchaNaBotun();
  		   float sirPolja=(pozivatelj.getGlavniRectPrikaza().width()-this.marginaKarakteristika)/brStupaca;
  		   float visPolja=( pozivatelj.getGlavniRectPrikaza().height()-this.marginaKarakteristika)/brRedova;
  		   float razmakY=0;
  		   izbor.postaviLijeviVrhProzoraPix(   	pomakKarakteristikaManjiX+marginaKarakteristika/2,   	pomakKarakteristikaManjiY+marginaKarakteristika*0.6f);
  		   //izbor.postaviNaslovIzbornikaPix("Drill sergeant:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
  		   izbor.postaviNaslovIzbornikaPix("Mortar",3*sirPolja,visPolja, sirPolja, -visPolja, tp);
  		   izbor.pokreniMojIzbornik(null);
  		 //  izbor.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
  		   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
  		   uiMan.setOznacenSam(izbor);
  	   uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
  	   uiMan.dodajElementUListu(izbor,4);
  	}
    ///////AMAZON
    @Override
    protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		 /////////////////////////////AMAZON DIO////////////////////////////////////////////////////////////////////////////////
	       
		 PurchasingService.getUserData();
	       
	       /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(pceoSaGlazbom){
			
			MusicManager.stanjeIgre(this,MusicManager.getGlobalnoStanje(),0, 0);
				//IgricaActivity.musicManager.stopAndRelease(0);
				//IgricaActivity.musicManager=null;
			
		}
		 /////// izbornik
		    /*
		    sprIzb=new SpriteHendler(3);
			sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.raw.lik8parse,opts), 240,400,4, 4,6);
			sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.raw.menu8pozadina,opts), 600,600,0, 0,0);
		 	sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.raw.menu8botuni,opts), 300,75,4, 1,0);
			IzbornikZaFaz
			u izbFaz=new IzbornikZaFazu(xp,yp,sprIzb,uiMan,302);
			uiMan.stvoriPozadinu(BitmapFactory.decodeResource(getResources(), R.raw.mapa,opts), 12.6f, 8.4f);
	        uiMan.pokreniGlavnuPetlju();*/
	  }
    @Override
    protected void onPause() {
		// TODO Auto-generated method stub
		
		if(this.pokreceFazuNeIskljucujZvuk){
			//pokreceFazuNeIskljucujZvuk=false;
		}
		else if(!pokreceFazuNeIskljucujZvuk){
			if(pceoSaGlazbom)MusicManager.stopAndRelease(0);
			//IgricaActivity.musicManager.stopAndRelease(0);
			//IgricaActivity.musicManager=null;
		}
	
		super.onPause();
		
	  }
	 @Override
	 public void onBackPressed() {
	     // do nothing.
		 uiMan.ukljuciTouchEvente();
		 if(this.uiMan.tempUniverzalniIzbornik()!=null&&!uiMan.onBackPressJeNormalan){
			 uiMan.tempUniverzalniIzbornik().backBotunStisnut();// u slucaju da nije ovradana funkija za back press pozvat ce defoltna u kojoj se poziva ova funkcija ponovno i postavlja se zastavica da je onbackpress normalan i ovvafunkcija reagira po defoltur5r5r5r5r5r5r5r5r5r5r5r5r5r5
			 uiMan.onBackPressJeNormalan=false;// vvraca ovu zastavicu da sljedeci put opet pozove izbbornik ako 
		 }
		 else if(!uiMan.onBackPressJeNormalan){
			 uiMan.makniSveObjekteMultyThread();
			 this.pokreniPocIzbornik();
			 
		 }
		 else {
			// uiMan.makniSveObjekteMultyThread();
			 super.onBackPressed();// u sluccaju  da nema izbonika ili je izbornik  nije overrida metodu za back press ova funkcija rai normalno
		 }
	 }
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
		this.reciklirajSe();
     	//Debug.stopMethodTracing(); 
	}
	@Override
	/////////sprijeèava promjenu  orjentacije 
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    
	}
}
