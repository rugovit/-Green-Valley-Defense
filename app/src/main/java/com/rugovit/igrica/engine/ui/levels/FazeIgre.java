package com.rugovit.igrica.engine.ui.levels;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import rugovit.igrica.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.SoundPool;
import android.os.Debug;
import android.util.Log;

import com.rugovit.igrica.engine.ui.DodatakNaMapu;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.logic.elements.GameLogicBranitelj;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.logic.elements.ObjectIgre;
import com.rugovit.igrica.engine.logic.elements.ProjektilL;
import com.rugovit.igrica.engine.logic.elements.Protivnik;
import com.rugovit.igrica.engine.logic.elements.PutL;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.Taskbar;
import com.rugovit.igrica.engine.logic.elements.ToranjL;
import com.rugovit.igrica.engine.logic.elements.ToranjLAlkemicar;
import com.rugovit.igrica.engine.logic.elements.ToranjLKasarna;
import com.rugovit.igrica.engine.logic.elements.ToranjLMinobacac;
import com.rugovit.igrica.engine.logic.elements.ToranjLStrijelci;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.elements.IzbornikZaToranj;
import com.rugovit.igrica.engine.ui.elements.ObjectPrikaza;
import com.rugovit.igrica.engine.ui.elements.ProjektilP;
import com.rugovit.igrica.engine.ui.elements.PutP;
import com.rugovit.igrica.engine.ui.elements.ToranjP;
import com.rugovit.igrica.engine.ui.elements.ToranjPAlkemicar;
import com.rugovit.igrica.engine.ui.elements.ToranjPKasarna;
import com.rugovit.igrica.engine.ui.elements.ToranjPMinobacac;
import com.rugovit.igrica.engine.ui.elements.ToranjPStrijelci;


public class FazeIgre {

	private float armorZaAlkemicare=0;// postavlja se samo kada se upgreda
	/////////  varijable crtanja load ekrana//////////////
	private SpriteHendler sprUvoda;
	float dxLodiranja;
	float postotakLodiranjaFaze=0;
	boolean tekPoceo=true;
	RectF recCrt=null, rectLoad=null, rectLoad2=null;
	Paint paint=null,paint2=null;
	float xLoad=0;
	float yLoad=0;
	float vis=0;
	float sir=0;
	///////////////////////////////////////////////////////
	////////zvukovi///////////
    
	static public LinkedList<Integer> listaSvikPustenihZvukova;
	static public float koeficijentPojacanjaZvuka=0.5f;// volume sound factor
	private Random generator=new Random();
	public  SoundPool soundPool;
	private LinkedList<Integer> listaZvukova;
	private HashMap<String,Integer> listaImenaZvukova;
	///////////parametri igre
	static public float smanjenjeTouchVisineZaTornjeve=35;
	public float koeficijentBrzineUdarca=1f;  //hit speed factor
	static public float sansaZaUdarac=50; // chance of hit
	public float koeficijentBrzine=1.6f; // defenders speed  factor
	public float koeficijentBrzineProtivnika=2f; //zombies speed factor
	public float redukcijaKodProdaje=0.7f; //  reduction of price while selling towers
	public float maxOcekivanaVelGradevinaZaPravilnoSortiranjeCm=2.4f; // max height of buildings
	public int sjenaAlpha=50; // shadow alpha
    public int sjenaRead=0;
    public int sjenaGreen=0;
    public int sjenaBlue=0;
    public float secStojanjaOnomatopeje=2;
    public float velXOnomatopejeCm=0.25f;
    public float velYOnomatopejeCm=0.25f;
    /////////////////////////////
    /////neke velicine slicica
    private float sirRankCm=0.4f;
    //////
	public  String IDSlotaIzFI;
	public  String IDKoristeneFazeIzFI;
    public  int brZvjezdica;
    public int tezina=0;
  /////
  //ability vrijeme  
public float timerZaAbilityreinforcementSek=40; // time for reinforcement
    public float timerZaAbilitylavabombSek=70; // timer for metor/ lava bomb
  ///
  private float novacPoSekundi=0;
  private static float mnoziteljVisineLikova=0.55f; // character size height  factor
  private static float mnoziteljSirnineLikova=0.75f;// character size width factor
  private int brProtivnika=0;
  private float xVala=-10000;
  private float yVala=-10000;
  private int ukupniNovac=0;
  private int brojValova=0, velListe;
  private int trenutniVal=0;
  private int trenutniValZaPrik=1;
  private int brZivota=0;
  private Taskbar taskbar;
  private int najveciRedniBrojCeste=0;
  ////
  private int secDoNovogVala;
  private boolean skociNaSljedeci=false;
  private LinkedList<Integer> listaUcitanihNaredbi;
  private HashMap<Integer,Float> listaToranjRofF;
  private float mnoziteljRadijusaStrijelci=1,mnoziteljRadijusaKasarna=1,mnoziteljRadijusaMinobacac=1,mnoziteljRadijusaAlkemicar=1;
  private float mnoziteljSteteHelthStrijelci=1,mnoziteljSteteHelthKasarna=1,mnoziteljSteteHelthMinobacac=1,mnoziteljSteteHelthAlkemicar=1;
  private float mnoziteljSteteArmorStrijelci=1,mnoziteljSteteArmorKasarna=1,mnoziteljSteteArmorMinobacac=1,mnoziteljSteteArmorAlkemicar=1;
  private float mnoziteljUsporavanjaStrijelci=1,mnoziteljUsporavanjaKasarna=1,mnoziteljUsporavanjaMinobacac=1,mnoziteljUsporavanjaAlkemicar=1;
  private float mnoziteljPostotkaUsporavanjaStrijelci=1,mnoziteljPostotkaUsporavanjaKasarna=1,mnoziteljPostotkaUsporavanjaMinobacac=1,mnoziteljPostotkaUsporavanjaAlkemicar=1;
  private float mnoziteljRoFStrijelci=1,mnoziteljVremenaTreningaKasarna=1,mnoziteljRoFMinobacac=1,mnoziteljRoFAlkemicar=1;
  private float mnoziteljCijenaStrijelci=1,mnoziteljCijenaKasarna=1,mnoziteljCijenaMinobacac=1,mnoziteljCijenaAlkemicar=1;
  private float mnoziteljHelthakasarna=1,mnoziteljArmoraKasarna=1;
  public LinkedList listaAchievementsa;
  static private HashMap<String,Integer> listaUpgradsi;
  static private HashMap<Integer,Integer> listaCijenaUpgradesa;
  private HashMap<Integer,SpriteHendler> listaSprite;
  private HashMap<Integer,Float> listaCijena;
  private HashMap<Integer,Float> listaHeltha;
  private HashMap<Integer,Float> listaArmora;
  private HashMap<Integer,Float> listaSteteZaArmor;
  private HashMap<Integer,Float> listaSteteZaHelth;
  private HashMap<Integer,Float> listaVrijemeUsporavanja;
  private HashMap<Integer,Float> listaPostotakUsporavanja;
  private HashMap<Integer,Float> listaRadijusa;
  private HashMap<Integer,Float> listaBrzina;
  private IzbornikZaToranj izborZaToranj;//sprema referencu izbornika za toranj
  public float xPiksCm;
  public float yPiksCm;
  private GameLogic gLog;
  private int brFaze;
  private UIManager uiMan;
  private int staraSec=-1;// služi za indikaciji prošle vrijednosti sekunde jer se funkcija tokIgre poziva u milisekundama
  private int nextSec=0, nextMin=0;// sljedeæe vrijeme kad se treba nešto dogodit u igri
  private int iListe=0,iDodavanja[];//jedan inkriminira prilikom èitanja drugi prilikom dodavanja
  private float[][] listaDx;// za cestu smijer, neæe je zasad koristiti drugi objekti
  private float[][] listaDy;
  private float[][] listaSir;
  private float[][] listaVis;
  private float[][] listaPozX;
  private float[][] listaPozY;
  private float[][] listaDodat;
  private float[][] listaDodat2;
  private float[][] listaDodat3;
  private float[][] listaDodat4;
  private float[][] listaDodat5;
  private float[][] listaDodat6;
  private float[][] listaDodat7;
  private int[][] listaDelay;// kasnjenje svakog clana, treba biti skinkronizirano sa donjom listom(isti i)
  private int[] listaDelayIzmVal; // lista delaya izmedu valova
  private int[][] listaAkcija;// akcija, tj. metode koje æe se pozvati kada za njih fdoðe vrijeme
  private Context context;
  public FazeIgre(int brValova,int velListe,float xPiksCm,float yPiksCm ,int tezina,int brFaze,Context context){
	  listaUcitanihNaredbi=new LinkedList<Integer>();
	 
	  listaDelay=new int[brValova][velListe];
	  listaAkcija=new int[brValova][velListe];
	  listaPozX=new float[brValova][velListe];
	  listaPozY=new float[brValova][velListe];
	  listaSir=new float[brValova][velListe];
	  listaVis=new float[brValova][velListe];
	  listaDx=new float[brValova][velListe];
	  listaDy=new float[brValova][velListe];
	  iDodavanja=new int[brValova];
	  listaDodat=new float[brValova][velListe];
	  listaDodat2=new float[brValova][velListe];
	  listaDodat3=new float[brValova][velListe];
	  listaDodat4=new float[brValova][velListe];
	  listaDodat5=new float[brValova][velListe];
	  listaDodat6=new float[brValova][velListe];
	  listaDodat7=new float[brValova][velListe];
	  listaDelayIzmVal=new int[brValova];
	  brojValova=brValova;
	  this.brFaze=brFaze;
	  this.velListe=velListe;
	  listaSprite=new HashMap<Integer,SpriteHendler>();
	  this.xPiksCm=xPiksCm;
	  this.yPiksCm=yPiksCm;
	  listaCijena=new HashMap<Integer,Float>();
	  listaCijena.put(-1, 5f);
	  this.context=context;
	  listaHeltha=new HashMap<Integer,Float>();
	  listaArmora=new HashMap<Integer,Float>();
	  listaSteteZaArmor=new HashMap<Integer,Float>();
	  listaSteteZaHelth=new HashMap<Integer,Float>();
	  listaToranjRofF=new HashMap<Integer,Float>();
	  listaVrijemeUsporavanja=new HashMap<Integer,Float>();
	  listaPostotakUsporavanja=new HashMap<Integer,Float>();
	  listaBrzina=new HashMap<Integer,Float>();
	  listaSvikPustenihZvukova=new LinkedList<Integer>();
	  listaRadijusa=new HashMap<Integer,Float>();

	  postaviSveParametreElemenata(tezina);

      postaviListuCijenaUpgradesa();
      dodajVrijednostiUpgreda();
      
  }
  private void kopiranKonstruktorZaResetiranje(int brValova,int velListe,float xPiksCm,float yPiksCm ,int tezina,Context context){
	 // listaDelay=new int[brValova][velListe];
	 // listaAkcija=new int[brValova][velListe];
	  //listaPozX=new float[brValova][velListe];
	  //listaPozY=new float[brValova][velListe];
	  //listaSir=new float[brValova][velListe];
	  //listaVis=new float[brValova][velListe];
	 // listaDx=new float[brValova][velListe];
	 // listaDy=new float[brValova][velListe];
	 // iDodavanja=new int[brValova];
	//  listaDodat=new float[brValova][velListe];
	 // listaDodat2=new float[brValova][velListe];
	//  listaDelayIzmVal=new int[brValova];
	 // brojValova=brValova;
	  this.velListe=velListe;
	  listaSprite=new HashMap<Integer,SpriteHendler>();
	  this.xPiksCm=xPiksCm;
	  this.yPiksCm=yPiksCm;
	  listaCijena=new HashMap<Integer,Float>();
	  listaCijena.put(-1, 5f);
	  this.context=context;
	  listaHeltha=new HashMap<Integer,Float>();
	  listaArmora=new HashMap<Integer,Float>();
	  listaSteteZaArmor=new HashMap<Integer,Float>();
	  listaSteteZaHelth=new HashMap<Integer,Float>();
	  listaToranjRofF=new HashMap<Integer,Float>();
	  listaVrijemeUsporavanja=new HashMap<Integer,Float>();
	  listaPostotakUsporavanja=new HashMap<Integer,Float>();
	  
	  listaRadijusa=new HashMap<Integer,Float>();
	  postaviSveParametreElemenata(tezina);
	  lodirajAchievementse();
      postaviListuCijenaUpgradesa();
      dodajVrijednostiUpgreda();
  }
  public void ponovnoPokretanjeIsteFazeIzGameLogica(){
	  //this.kopiranKonstruktorZaResetiranje(brojValova,velListe,xPiksCm,yPiksCm ,tezina, context);
      ////////resetiraj varijable
	  tekPoceo=true;
	
	  staraSec=-1;
	  nextSec=0;
	  nextMin=0;
	  trenutniVal=0;
	  iListe=0;
	  trenutniValZaPrik=1;
	  secDoNovogVala=0;
	  skociNaSljedeci=false;
	  ///UI Man
	  this.uiMan.makniSveObjekteMultyThread();
	  ///////////////
      //GL 
	  this.gLog.ponovnoPunjenjePodatcimaGameLogic();
	  //////
	  
      ///taskbar
	  this.taskbar.ocistiDodatneBotune();
	  //this.taskbar.inicijalizirajDodatneBotune();
	  
	  uiMan.dodajElementUListu(izborZaToranj, 3);
  }
  //////////////////////////////////////
 
  private void lodirajAchievementse(){
		/////lodiranje upgradsa
		this.listaAchievementsa=new LinkedList<String>();
		

		SQLiteDatabase bazaPodataka;
		bazaPodataka= context.openOrCreateDatabase(IgricaActivity.glavniDB,Activity.MODE_PRIVATE, null);
		Cursor cur3=bazaPodataka.query(IgricaActivity.zadnjiKoristenSlot, null,null, null, null, null, null);
		 
		  cur3.moveToFirst();
		 this.IDSlotaIzFI=cur3.getString(cur3.getColumnIndex(IgricaActivity.IDSlota));
	
		Cursor cur4=bazaPodataka.query(IgricaActivity.listaAchievementsa, null,null, null, null, null, null);
		cur4.moveToFirst();  
		
		while(cur4.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
      	    String IDAchi=cur4.getString(cur4.getColumnIndex(IgricaActivity.IDAchievementsa));	
      	   
      	    if(cur4.getString(cur4.getColumnIndex(IgricaActivity.IDSlota)).equals(this.IDSlotaIzFI)){
      	    	 this.listaAchievementsa.addLast(IDAchi);// 
      	    }
      	    

   	    cur4.moveToNext();// za pomicanje u bazi podataka   
         
            }
		 cur3.close();
		 cur4.close();
		 bazaPodataka.close();
		
	}
  private void lodirajUpgradse(){
	/////lodiranje upgradsa
		listaUpgradsi=new HashMap<String,Integer>();
		SQLiteDatabase bazaPodataka;
		bazaPodataka= context.openOrCreateDatabase(IgricaActivity.glavniDB,Activity.MODE_PRIVATE, null);
		Cursor cur4=bazaPodataka.query(IgricaActivity.listaUpgradesa, null,null, null, null, null, null);
		cur4.moveToFirst();  
		while(cur4.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
    	    String IDUpgrada=cur4.getString(cur4.getColumnIndex(IgricaActivity.IDUpgradesaSlotPlusBrUpgrada));	
    	   
    	    Integer brojUpg=cur4.getInt(cur4.getColumnIndex(IgricaActivity.brojUpgradesa));
    	 
    	     this.listaUpgradsi.put(IDUpgrada,brojUpg);// 

 	    cur4.moveToNext();// za pomicanje u bazi podataka   
       
          }
		 Cursor cur3=bazaPodataka.query(IgricaActivity.zadnjiKoristenSlot, null,null, null, null, null, null);
		 
			  cur3.moveToFirst();
			 this.IDSlotaIzFI=cur3.getString(cur3.getColumnIndex(IgricaActivity.IDSlota));
			 cur3.close();
			 cur4.close();
		bazaPodataka.close();
  }
  private void dodajVrijednostiUpgreda(){
	  lodirajUpgradse();
	  ///////////////////////////////////STRIJELCI////////////////////////////////////////////////////////////////
	  float rOfPovecanje=0;
	  float radiusPovecanje=0;
	  float armorDamage=0;
	  float healthDamage=0;
	    //1. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+1)){
	    	rOfPovecanje+=7*(mnoziteljRoFStrijelci/100);// povecavam za 4%
	    	radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     //2. upgrade
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+2)){
	    	 rOfPovecanje+=7*(mnoziteljRoFStrijelci/100);// povecavam za 7%
	    	 //this.mnoziteljRadijusaStrijelci=mnoziteljRadijusaStrijelci+7*(mnoziteljRadijusaStrijelci/100);
	     }
	   //3. upgrade
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+3)){
	    	 armorDamage+=10*(mnoziteljSteteArmorStrijelci/100);// povecavam za 7%
	    	 //this.mnoziteljRadijusaStrijelci=mnoziteljRadijusaStrijelci+7*(mnoziteljRadijusaStrijelci/100);
	     }
	   //4. upgrade
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+4)){
	    	 healthDamage+=10*(mnoziteljSteteHelthStrijelci/100);// povecavam za 7%
	    	 //this.mnoziteljRadijusaStrijelci=mnoziteljRadijusaStrijelci+7*(mnoziteljRadijusaStrijelci/100);
	     }
	   //5. upgrade
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+5)){
	    	 healthDamage+=10*(mnoziteljSteteHelthStrijelci/100);// povecavam za 7%
	    	 radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	    	 //this.mnoziteljRadijusaStrijelci=mnoziteljRadijusaStrijelci+7*(mnoziteljRadijusaStrijelci/100);
	     }
	     mnoziteljRoFStrijelci+= rOfPovecanje;
	     mnoziteljRadijusaStrijelci+=radiusPovecanje;
	     mnoziteljSteteArmorStrijelci+=armorDamage;
	     mnoziteljSteteHelthStrijelci+=healthDamage;
		 rOfPovecanje=0;
		 radiusPovecanje=0;
		 armorDamage=0;
		 healthDamage=0;
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////
	  ////////////////////////KASARNA//////////////////////////////////////////////////////////////////////////
		//1. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+6)){
	    	rOfPovecanje+=7*(mnoziteljVremenaTreningaKasarna/100);// povecavam za 4%
	    	healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	   //2. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+7)){
	    	rOfPovecanje+=7*(mnoziteljVremenaTreningaKasarna/100);// povecavam za 4%
	    	healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	   //3. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+8)){
	    		healthDamage+=10*(mnoziteljHelthakasarna/100);
	     }
	   //4. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+9)){
	    		armorDamage+=10*(mnoziteljArmoraKasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     //5. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+10)){
	    		armorDamage+=10*(mnoziteljArmoraKasarna/100);
	    		healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     mnoziteljVremenaTreningaKasarna+=rOfPovecanje;
	     mnoziteljHelthakasarna+=healthDamage;
	     mnoziteljArmoraKasarna+=armorDamage;
	     rOfPovecanje=0;
		 radiusPovecanje=0;
		 armorDamage=0;
		 healthDamage=0;
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////
	  ////////////////////////////////////MINOBACAC////////////////////////////////////////////////////////////
	   //1. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+11)){
	    	rOfPovecanje+=7*(mnoziteljRoFMinobacac/100);// povecavam za 4%
	    	//healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     //2. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+12)){
	    	rOfPovecanje+=7*(mnoziteljRoFMinobacac/100);// povecavam za 4%
	    	//healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     //3. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+13)){
	    	 radiusPovecanje+=7*(mnoziteljRadijusaMinobacac/100);// povecavam za 4%
	    	//healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     //4. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+14)){
	    	 radiusPovecanje+=7*(mnoziteljRadijusaMinobacac/100);// povecavam za 4%
	    	//healthDamage+=10*(mnoziteljHelthakasarna/100);
	    	//radiusPovecanje+=7*(mnoziteljRadijusaStrijelci/100);
	     }
	     //5. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+15)){
	    	 armorDamage+=10*(mnoziteljSteteArmorMinobacac/100);
	    	healthDamage+=10*(mnoziteljSteteHelthMinobacac/100);
	     }
	     mnoziteljSteteArmorMinobacac+= armorDamage;
	     mnoziteljSteteHelthMinobacac+=healthDamage;
	     mnoziteljRoFMinobacac+=rOfPovecanje;
	     mnoziteljRadijusaMinobacac+=radiusPovecanje;
	     rOfPovecanje=0;
		 radiusPovecanje=0;
		 armorDamage=0;
		 healthDamage=0;
	  /////////////////////////////////////////////////////////////////////////////////////////////////////////   
		 /////////////////////////////////ALKEMICAR////////////////////////////////////////////////////////////
		 //1. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+16)){
	    	 rOfPovecanje+=7*(mnoziteljRoFAlkemicar/100);
	     }
	     //2. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+17)){
	    	 rOfPovecanje+=7*(mnoziteljRoFAlkemicar/100);
	     }
	     //3. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+18)){
	    	 healthDamage+=7*(mnoziteljSteteHelthAlkemicar/100);
	     }
	     //4. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+19)){
	    	 armorZaAlkemicare=1.5f;// dodajem direktno vrijednost za armor damage jer nema nikakav do 4 razine
	     }
	     //5. upgrade 
	     if(listaUpgradsi.containsKey(IDSlotaIzFI+20)){
	    	
	    	 armorZaAlkemicare=2.5f;
	     }
	     mnoziteljSteteHelthAlkemicar+=healthDamage;
	     mnoziteljRoFAlkemicar+=rOfPovecanje;
	     mnoziteljSteteArmorAlkemicar+=armorDamage;
		 rOfPovecanje=0;
		 radiusPovecanje=0;
		 armorDamage=0;
		 healthDamage=0;
		 //////////////////////////////////////////////////////////////////////////////////////////////////////
	     mnoziteljRadijusaMinobacac=0.8f*mnoziteljRadijusaMinobacac;
	     mnoziteljRadijusaAlkemicar=0.8f*mnoziteljRadijusaAlkemicar;
	     mnoziteljRadijusaStrijelci=0.8f*mnoziteljRadijusaStrijelci;
	     
	     
	     
	  ////////////////////POSLIJE PROVJERE UPGREDSA LODIRAM VRIJEDNOSTI U LISTE///////////////
	     mnoziteljSteteHelthKasarna=mnoziteljSteteHelthKasarna*2;
	     parametriTornjevaMinobacaca(this.mnoziteljRoFMinobacac,this.mnoziteljSteteHelthMinobacac,mnoziteljSteteArmorMinobacac,mnoziteljCijenaMinobacac, mnoziteljUsporavanjaMinobacac,mnoziteljPostotkaUsporavanjaMinobacac,mnoziteljRadijusaMinobacac);
		  parametriTornjevaStrijelaca(this.mnoziteljRoFStrijelci,this.mnoziteljSteteHelthStrijelci,mnoziteljSteteArmorStrijelci,mnoziteljCijenaStrijelci, mnoziteljUsporavanjaStrijelci,mnoziteljPostotkaUsporavanjaStrijelci,mnoziteljRadijusaStrijelci);
		  parametriTornjevaAlkemicara(this.mnoziteljRoFAlkemicar,this.mnoziteljSteteHelthAlkemicar,mnoziteljSteteArmorAlkemicar,mnoziteljCijenaAlkemicar, mnoziteljUsporavanjaAlkemicar,mnoziteljPostotkaUsporavanjaAlkemicar,mnoziteljRadijusaAlkemicar);
		  parametriTornjevaKasarna( mnoziteljVremenaTreningaKasarna,this.mnoziteljHelthakasarna,mnoziteljArmoraKasarna,mnoziteljSteteHelthKasarna,mnoziteljSteteArmorKasarna,mnoziteljCijenaKasarna,mnoziteljUsporavanjaKasarna,mnoziteljPostotkaUsporavanjaKasarna, mnoziteljRadijusaKasarna);
  } 
  static private void postaviListuCijenaUpgradesa(){
	  listaCijenaUpgradesa=new HashMap<Integer,Integer>();
	  ///STRIJELCI 
	  //1.
	  listaCijenaUpgradesa.put(1, 1); //1 bod
	  listaCijenaUpgradesa.put(2, 2); 
	  listaCijenaUpgradesa.put(3, 3); 
	  listaCijenaUpgradesa.put(4, 4); 
	  listaCijenaUpgradesa.put(5, 5); 
	  
	  //////////////////
	///KASARNA 
	  //1.
	  listaCijenaUpgradesa.put(6, 1); //1 bod
	  listaCijenaUpgradesa.put(7, 2); 
	  listaCijenaUpgradesa.put(8, 3); 
	  listaCijenaUpgradesa.put(9, 4); 
	  listaCijenaUpgradesa.put(10, 5); 
	  //////////////////
	///MINOBACAC 
	  //1.
	  listaCijenaUpgradesa.put(11, 1); //1 bod
	  listaCijenaUpgradesa.put(12, 2); 
	  listaCijenaUpgradesa.put(13, 3); 
	  listaCijenaUpgradesa.put(14, 4); 
	  listaCijenaUpgradesa.put(15, 5); 
	  //////////////////
	///ALKEMICAR
	  //1.
	  listaCijenaUpgradesa.put(16, 1); //1 bod
	  listaCijenaUpgradesa.put(17, 2); 
	  listaCijenaUpgradesa.put(18, 3); 
	  listaCijenaUpgradesa.put(19, 4); 
	  listaCijenaUpgradesa.put(20, 5); 
	  //////////////////
  }
  private void postaviSveParametreElemenata(int tezina){
	 /* float mnoziteljSteteHel=1;
	  float mnoziteljSteteArm=1;
	  float mnoziteljHeltha=1;
	  float mnoziteljArmora=1;
	  float mnoziteljProfita=1;*/
	  float mnoziteljSteteHel=1f;
	  float mnoziteljSteteArm=1f;
	  float mnoziteljHeltha=0.8f;
	  float mnoziteljArmora=0.8f;
	  float mnoziteljProfita=1f;
	  
	  
	  float mnoziteljSteteHelBra=1;
	  float mnoziteljSteteArmBra=1;
	  float mnoziteljHelthaBra=1;
	  float mnoziteljArmoraBra=1;
	  float mnoziteljCijene=1;
	  if(tezina==1){
		  
		  parametriProtivnika( mnoziteljSteteHel,mnoziteljSteteArm,mnoziteljHeltha,mnoziteljArmora,mnoziteljProfita);
		  ///////////
		  
		  //parametriBranitelja( mnoziteljSteteHelBra,mnoziteljSteteArmBra,mnoziteljHelthaBra,mnoziteljArmoraBra,mnoziteljCijene);
		  ///////////
	  }
	  else if(tezina==2){
		  /*
		  mnoziteljSteteHel=1.3f;
		  mnoziteljSteteArm=1.3f;
		  mnoziteljHeltha=1.3f;
		  mnoziteljArmora=1.3f;
		  mnoziteljProfita=1.3f;
		  
		  parametriProtivnika( mnoziteljSteteHel,mnoziteljSteteArm,mnoziteljHeltha,mnoziteljArmora,mnoziteljProfita);
		  */
		  
		  mnoziteljSteteHel=1f;
		  mnoziteljSteteArm=1f;
		  mnoziteljHeltha=1f;
		  mnoziteljArmora=1f;
		  mnoziteljProfita=1f;
		  
		  parametriProtivnika( mnoziteljSteteHel,mnoziteljSteteArm,mnoziteljHeltha,mnoziteljArmora,mnoziteljProfita);
		  ///////////
		  
		  //parametriBranitelja( mnoziteljSteteHelBra,mnoziteljSteteArmBra,mnoziteljHelthaBra,mnoziteljArmoraBra,mnoziteljCijene);
		  ///////////
		  
	  }
	  else if(tezina==3){
		  mnoziteljSteteHel=2f;
		   mnoziteljSteteArm=2f;
		  mnoziteljHeltha=2f;
		  mnoziteljArmora=2f;
		  mnoziteljProfita=2f;
		  
		  parametriProtivnika( mnoziteljSteteHel,mnoziteljSteteArm,mnoziteljHeltha,mnoziteljArmora,mnoziteljProfita);
		  ///////////
		  
		 // parametriBranitelja( mnoziteljSteteHelBra,mnoziteljSteteArmBra,mnoziteljHelthaBra,mnoziteljArmoraBra,mnoziteljProfita);
		  ///////////
		  
	  }
  }
  private void parametriProtivnika(float mnoziteljSteteHel,float mnoziteljSteteArm, float mnoziteljHeltha, float mnoziteljArmora, float mnoziteljProfita){
	  mnoziteljSteteHel= mnoziteljSteteHel;
	  mnoziteljSteteArm= mnoziteljSteteArm;
	  mnoziteljHeltha=1.0f*mnoziteljHeltha;
	  mnoziteljArmora=mnoziteljArmora;
	  mnoziteljProfita= mnoziteljProfita;
	  
	  
	  //protivnik br radnik
	  listaCijena.put(-1,( 3*mnoziteljProfita));
	  listaHeltha.put(-1,(100*mnoziteljHeltha));
	  listaArmora.put(-1,(30*mnoziteljArmora));
	  listaSteteZaArmor.put(-1,(0.3f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-1,(2*mnoziteljSteteHel));
	  listaBrzina.put(-1,(620.47f* koeficijentBrzineProtivnika));
	  
	  
	  ////////////////////////////// 
	  //protivnik br2  sminkerica
	  listaCijena.put(-2,( 3*mnoziteljProfita));
	  listaHeltha.put(-2,(120*mnoziteljHeltha));
	  listaArmora.put(-2,(0*mnoziteljArmora));
	  listaSteteZaArmor.put(-2,(0.8f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-2,(3*mnoziteljSteteHel));
	  listaBrzina.put(-2,(390.45f*koeficijentBrzineProtivnika));
	  listaVrijemeUsporavanja.put(-2,( 15.5f));
	  listaPostotakUsporavanja.put(-2,(0f));
	  //////////////////////////////
	  //protivnik br3 mma
	  listaCijena.put(-3,( 7*mnoziteljProfita));
	  listaHeltha.put(-3,(200*mnoziteljHeltha));
	  listaArmora.put(-3, (100*mnoziteljArmora));
	  listaSteteZaArmor.put(-3,(0.20f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-3,(10*mnoziteljSteteHel));
	  listaBrzina.put(-3,(300.43f*koeficijentBrzineProtivnika));
	  //////////////////////////////
	  //protivnik br -4 reper
	  listaCijena.put(-4,( 2*mnoziteljProfita));
	  listaHeltha.put(-4,(80*mnoziteljHeltha));
	  listaArmora.put(-4,(0*mnoziteljArmora));
	  listaSteteZaArmor.put(-4,(0*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-4,(7*mnoziteljSteteHel));	
	  listaBrzina.put(-4,(300.47f*koeficijentBrzineProtivnika));
	  ////////////////////////////// 
	  //protivnik br-5 pocetni
	  listaCijena.put(-5,( 2*mnoziteljProfita));
	  listaHeltha.put(-5,(80*mnoziteljHeltha));
	  listaArmora.put(-5, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(-5,(0.20f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-5,(1*mnoziteljSteteHel));
	  listaBrzina.put(-5,(300.47f*koeficijentBrzineProtivnika));
	  //////////////////////////////
	  //protivnik br-6 debeli
	  listaCijena.put(-6,( 3*mnoziteljProfita));
	  listaHeltha.put(-6,(100*mnoziteljHeltha));
	  listaArmora.put(-6, (10*mnoziteljArmora));
	  listaSteteZaArmor.put(-6,(0.20f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-6,(1*mnoziteljSteteHel));
	  listaBrzina.put(-6,(300.47f*koeficijentBrzineProtivnika));
	  //////////////////////////////
	  //protivnik br-7 cistacica
	  listaCijena.put(-7,( 2*mnoziteljProfita));
	  listaHeltha.put(-7,(80*mnoziteljHeltha));
	  listaArmora.put(-7, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(-7,(0.20f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-7,(1.5f*mnoziteljSteteHel));
	  listaBrzina.put(-7,(500.47f*koeficijentBrzineProtivnika));
	  //////////////////////////////
	  //protivnik br-8 kuhar
	  listaCijena.put(-8,( 4*mnoziteljProfita));
	  listaHeltha.put(-8,(130*mnoziteljHeltha));
	  listaArmora.put(-8, (50*mnoziteljArmora));
	  listaSteteZaArmor.put(-8,(0.50f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-8,(10.5f*mnoziteljSteteHel));
	  listaBrzina.put(-8,(300.47f*koeficijentBrzineProtivnika));
	  //////////////////////////////
	  //protivnik br-9 pas
	  listaCijena.put(-9,( 1*mnoziteljProfita));
	  listaHeltha.put(-9,(90*mnoziteljHeltha));
	  listaArmora.put(-9, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(-9,(0f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-9,(1.5f*mnoziteljSteteHel));
	  listaBrzina.put(-9,(800.47f*koeficijentBrzineProtivnika));
	//protivnik br-10 policajac
	  listaCijena.put(-10,( 5*mnoziteljProfita));
	  listaHeltha.put(-10,(110*mnoziteljHeltha));
	  listaArmora.put(-10, (100*mnoziteljArmora));
	  listaSteteZaArmor.put(-10,(3f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-10,(15f*mnoziteljSteteHel));
	  listaBrzina.put(-10,(300.47f*koeficijentBrzineProtivnika));
	//protivnik br-11 debeli prdonja
	  listaCijena.put(-11,( 4*mnoziteljProfita));
	  listaHeltha.put(-11,(150*mnoziteljHeltha));
	  listaArmora.put(-11, (10*mnoziteljArmora));
	  listaSteteZaArmor.put(-11,(0f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-11,(0f*mnoziteljSteteHel));
	  listaBrzina.put(-11,(300.47f*koeficijentBrzineProtivnika));
	//protivnik br-12 alien
	  listaCijena.put(-12,( 3*mnoziteljProfita));
	  listaHeltha.put(-12,(550*mnoziteljHeltha));
	  listaArmora.put(-12, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(-12,(15f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-12,(40f*mnoziteljSteteHel));
	  listaBrzina.put(-12,(100.47f*koeficijentBrzineProtivnika));
	//protivnik br-13 tower buster
	  listaCijena.put(-13,( 3*mnoziteljProfita));
	  listaHeltha.put(-13,(1100*mnoziteljHeltha));
	  listaArmora.put(-13, (150*mnoziteljArmora));
	  listaSteteZaArmor.put(-13,(50f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-13,(150f*mnoziteljSteteHel));
	  listaBrzina.put(-13,(240.47f*koeficijentBrzineProtivnika));
	 //protivnik br-14 studentica
	  listaCijena.put(-14,( 3*mnoziteljProfita));
	  listaHeltha.put(-14,(140*mnoziteljHeltha));
	  listaArmora.put(-14, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(-14,(4f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-14,(12f*mnoziteljSteteHel));
	  listaBrzina.put(-14,(300f*koeficijentBrzineProtivnika));
	  //protivnik br-15 starleta
	  listaCijena.put(-15,( 4*mnoziteljProfita));
	  listaHeltha.put(-15,(100*mnoziteljHeltha));
	  listaArmora.put(-15, (40*mnoziteljArmora));
	  listaSteteZaArmor.put(-15,(3f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-15,(15f*mnoziteljSteteHel));
	  listaBrzina.put(-15,(450f*koeficijentBrzineProtivnika));
	  //protivnik br-16 debeli serac
	  listaCijena.put(-16,( 9*mnoziteljProfita));
	  listaHeltha.put(-16,(400*mnoziteljHeltha));
	  listaArmora.put(-16, (100*mnoziteljArmora));
	  listaSteteZaArmor.put(-16,(5f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-16,(20f*mnoziteljSteteHel));
	  listaBrzina.put(-16,(250f*koeficijentBrzineProtivnika));
	  
	  ////projektil od serca govance
		  listaSteteZaArmor.put(419,( 1*mnoziteljSteteArm));
		  listaSteteZaHelth.put(419,(20*mnoziteljSteteHel));
		  listaVrijemeUsporavanja.put(406,( 0f));
      listaPostotakUsporavanja.put(406,( 0f));
    /////////////protivnik br-17 biznisman//////////////////////////
	  listaCijena.put(-17,( 1*mnoziteljProfita));
	  listaHeltha.put(-17,(130*mnoziteljHeltha));
	  listaArmora.put(-17, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(-17,(0.20f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(-17,(1.5f*mnoziteljSteteHel));
	  listaBrzina.put(-17,(400.47f*koeficijentBrzineProtivnika));
	  /////////////////////////////////////////////////////////////	  
	    /////////////protivnik br-18 kapitalista//////////////////////////
		  listaCijena.put(-18,( 2*mnoziteljProfita));
		  listaHeltha.put(-18,(240*mnoziteljHeltha));
		  listaArmora.put(-18, (40*mnoziteljArmora));
		  listaSteteZaArmor.put(-18,(1.20f*mnoziteljSteteArm));
		  listaSteteZaHelth.put(-18,(3.5f*mnoziteljSteteHel));
		  listaBrzina.put(-18,(170.47f*koeficijentBrzineProtivnika));
		  /////////////////////////////////////////////////////////////	  
	  /*
	  listaSteteZaArmor.put(417,( 0*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(417,(95*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(417,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(417,( 0*mnoziteljUsporavanja));*/
	  /////////

	  
  }
  private void parametriBranitelja(float mnoziteljSteteHel,float mnoziteljSteteArm, float mnoziteljHeltha, float mnoziteljArmora, float mnoziteljProfita){
	  
	  

	  //////////////////////////
	  
	  
  }
  private void parametriTornjevaMinobacaca(float mnoziteljRofF,float mnoziteljSteteHel,float mnoziteljSteteArm,float cijena,float mnoziteljUsporavanja, float mnoziteljPostotkaUsporavanja,float mnoziteljRadijusa){
	 /*
	  ////toranj minobacac 1, razina/////////////////////////////////////////////////////
	  listaSteteZaArmor.put(401,( 0*mnoziteljSteteArm));//setira granate koje su 401
	  listaSteteZaHelth.put(401,(40*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(401,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(401,( 0*mnoziteljUsporavanja));
	  
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float rTor=2.5f*PpCm;
	  listaToranjRofF.put(125,( 0.1f*mnoziteljRofF));
	  listaRadijusa.put(125,( rTor*mnoziteljRadijusa));
      listaCijena.put(125,( 100*cijena));
      
	  ////toranj minobacac 2, razina tnt/////////////////////////////////////////////////
	  listaSteteZaArmor.put(408,( 0*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(408,(60*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(408,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(408,( 0*mnoziteljUsporavanja));
	  
	  PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=2.9f*PpCm;
	  listaToranjRofF.put(126,( 0.1f*mnoziteljRofF));
	  listaRadijusa.put(126,( rTor*mnoziteljRadijusa));
      listaCijena.put(126,( 120*cijena));
	  //////////////////////////////////////////////////////////////////////////////////
	  ////toranj minobacac 3, razina armor
	  listaSteteZaArmor.put(409,( 15*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(409,(70*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(409,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(409,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.1f*PpCm;
	  listaToranjRofF.put(127,( 0.1f*mnoziteljRofF));
	  listaRadijusa.put(127,( rTor*mnoziteljRadijusa));
      listaCijena.put(127,( 140*cijena));
	  ////////////////////////
	  ////toranj minobacac CLUSTER///////////////////////////////////////////////////////
	  listaSteteZaArmor.put(410,( 50*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(410,(60*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(410,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(410,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.3f*PpCm;
	  listaToranjRofF.put(128,( 0.1f*mnoziteljRofF));
	  listaRadijusa.put(128,( rTor*mnoziteljRadijusa));
      listaCijena.put(128,( 160*cijena));
	  ////toranj NAPALM////////////////////////////////////////////////////////////////////
	  listaSteteZaArmor.put(411,( 0*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(411,(80*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(411,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(411,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.3f*PpCm;
	  listaToranjRofF.put(129,( 0.1f*mnoziteljRofF));
	  listaRadijusa.put(129,( rTor*mnoziteljRadijusa));
      listaCijena.put(129,( 160*cijena));
	  ////////////////////////
 */
	  //TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	  ////toranj minobacac 1, razina/////////////////////////////////////////////////////
	  listaSteteZaArmor.put(401,( 0*mnoziteljSteteArm));//setira granate koje su 401
	  listaSteteZaHelth.put(401,(40*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(401,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(401,( 0*mnoziteljUsporavanja));
	  
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float rTor=2.5f*PpCm;
	  listaToranjRofF.put(125,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(125,( rTor*mnoziteljRadijusa));
      listaCijena.put(125,( 56*cijena));
	  ////toranj minobacac 2, razina tnt/////////////////////////////////////////////////
	  listaSteteZaArmor.put(408,( 0*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(408,(60*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(408,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(408,( 0*mnoziteljUsporavanja));
	  
	  PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=2.9f*PpCm;
	  listaToranjRofF.put(126,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(126,( rTor*mnoziteljRadijusa));
      listaCijena.put(126,( 64*cijena));
	  //////////////////////////////////////////////////////////////////////////////////
	  ////toranj minobacac 3, razina armor
	  listaSteteZaArmor.put(409,( 20*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(409,(70*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(409,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(409,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.1f*PpCm;
	  listaToranjRofF.put(127,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(127,( rTor*mnoziteljRadijusa));
      listaCijena.put(127,( 72*cijena));
	  ////////////////////////
	  ////toranj minobacac CLUSTER///////////////////////////////////////////////////////
	  listaSteteZaArmor.put(410,( 60*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(410,(70*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(410,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(410,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.3f*PpCm;
	  listaToranjRofF.put(128,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(128,( rTor*mnoziteljRadijusa));
      listaCijena.put(128,( 96*cijena));
	  ////toranj NAPALM////////////////////////////////////////////////////////////////////
	  listaSteteZaArmor.put(411,( 0*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(411,(80*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(411,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(411,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.3f*PpCm;
	  listaToranjRofF.put(129,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(129,( rTor*mnoziteljRadijusa));
      listaCijena.put(129,( 96*cijena));
	 
	  ////////////////////////
      ////toranj NAPALM rank1////////////////////////////////////////////////////////////////////
  	  listaSteteZaArmor.put(415,( 0*mnoziteljSteteArm));//setira granate koje su 407
  	  listaSteteZaHelth.put(415,(85*mnoziteljSteteHel));
  	  listaVrijemeUsporavanja.put(415,( 0*mnoziteljPostotkaUsporavanja));
  	  listaPostotakUsporavanja.put(415,( 0*mnoziteljUsporavanja));
  	  
  	   
        PpCm=(xPiksCm+yPiksCm)/2;
  	  rTor=3.4f*PpCm;
  	  listaToranjRofF.put(133,( 0.14f*mnoziteljRofF));
  	  listaRadijusa.put(133,( rTor*mnoziteljRadijusa));
        listaCijena.put(133,( 70*cijena));
  	 
  	  ////////////////////////
    ////toranj NAPALM rank2////////////////////////////////////////////////////////////////////
    	  listaSteteZaArmor.put(416,( 0*mnoziteljSteteArm));//setira granate koje su 407
    	  listaSteteZaHelth.put(416,(90*mnoziteljSteteHel));
    	  listaVrijemeUsporavanja.put(416,( 0*mnoziteljPostotkaUsporavanja));
    	  listaPostotakUsporavanja.put(416,( 0*mnoziteljUsporavanja));
    	  
    	   
          PpCm=(xPiksCm+yPiksCm)/2;
    	  rTor=3.5f*PpCm;
    	  listaToranjRofF.put(134,( 0.14f*mnoziteljRofF));
    	  listaRadijusa.put(134,( rTor*mnoziteljRadijusa));
          listaCijena.put(134,( 70*cijena));
    	 
    	  ////////////////////////
      ////toranj NAPALM rank3////////////////////////////////////////////////////////////////////
    	  listaSteteZaArmor.put(417,( 0*mnoziteljSteteArm));//setira granate koje su 407
    	  listaSteteZaHelth.put(417,(95*mnoziteljSteteHel));
    	  listaVrijemeUsporavanja.put(417,( 0*mnoziteljPostotkaUsporavanja));
    	  listaPostotakUsporavanja.put(417,( 0*mnoziteljUsporavanja));
    	  
    	   
          PpCm=(xPiksCm+yPiksCm)/2;
    	  rTor=3.6f*PpCm;
    	  listaToranjRofF.put(135,( 0.14f*mnoziteljRofF));
    	  listaRadijusa.put(135,( rTor*mnoziteljRadijusa));
          listaCijena.put(135,( 70*cijena));
    	 
    	  ////////////////////////
      ////toranj minobacac CLUSTER rank 1///////////////////////////////////////////////////////
	  listaSteteZaArmor.put(412,( 65*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(412,(70*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(412,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(412,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.4f*PpCm;
	  listaToranjRofF.put(130,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(130,( rTor*mnoziteljRadijusa));
      listaCijena.put(130,( 120*cijena));
      ////toranj minobacac CLUSTER rank 2///////////////////////////////////////////////////////
	  listaSteteZaArmor.put(413,( 75*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(413,(70*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(413,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(413,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.5f*PpCm;
	  listaToranjRofF.put(131,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(131,( rTor*mnoziteljRadijusa));
      listaCijena.put(131,( 70*cijena));
      ////toranj minobacac CLUSTER rank 3///////////////////////////////////////////////////////
	  listaSteteZaArmor.put(414,( 80*mnoziteljSteteArm));//setira granate koje su 407
	  listaSteteZaHelth.put(414,(70*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(414,( 0*mnoziteljPostotkaUsporavanja));
	  listaPostotakUsporavanja.put(414,( 0*mnoziteljUsporavanja));
	  
	   
      PpCm=(xPiksCm+yPiksCm)/2;
	  rTor=3.6f*PpCm;
	  listaToranjRofF.put(132,( 0.14f*mnoziteljRofF));
	  listaRadijusa.put(132,( rTor*mnoziteljRadijusa));
      listaCijena.put(132,( 70*cijena));
  }
  private void parametriTornjevaStrijelaca(float mnoziteljRofF,float mnoziteljSteteHel,float mnoziteljSteteArm,float cijena,float mnoziteljUsporavanja, float mnoziteljPostotkaUsporavanja,float mnoziteljRadijusa){
	  ////toranj strijelci 1,2,3, razina
	  listaSteteZaArmor.put(402,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(402,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(402,( 0*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(402,( 0*mnoziteljPostotkaUsporavanja));
	  ////vatrene strijelice
	  listaSteteZaArmor.put(406,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(406,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(406,( 0*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(406,( 0*mnoziteljPostotkaUsporavanja));
	////otrovne strijelice
		  listaSteteZaArmor.put(407,( 1*mnoziteljSteteArm));
		  listaSteteZaHelth.put(407,(18*mnoziteljSteteHel));
		  listaVrijemeUsporavanja.put(407,( 4*mnoziteljUsporavanja));
		  listaPostotakUsporavanja.put(407,( 50*mnoziteljPostotkaUsporavanja));
	  
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float rTor=2.5f*PpCm;
	  listaToranjRofF.put(101,( 0.5f*mnoziteljRofF));
	  listaRadijusa.put(101,( rTor*mnoziteljRadijusa));
      listaCijena.put(101,( 30*cijena));
      
      listaToranjRofF.put(102,( 1f*mnoziteljRofF));
	  listaRadijusa.put(102,( rTor*mnoziteljRadijusa));
      listaCijena.put(102,( 40*cijena));
      
      listaToranjRofF.put(103,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(103,( rTor*mnoziteljRadijusa));
      listaCijena.put(103,( 53*cijena));
      
      listaToranjRofF.put(104,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(104,( rTor*mnoziteljRadijusa));
      listaCijena.put(104,( 71*cijena));
      
      listaToranjRofF.put(105,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(105,( rTor*mnoziteljRadijusa));
      listaCijena.put(105,( 71*cijena));
      ////strijeli vatreni 1 klasa
      listaSteteZaArmor.put(421,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(421,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(421,( 0*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(421,( 0*mnoziteljPostotkaUsporavanja));
	  
	  listaToranjRofF.put(106,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(106,( rTor*mnoziteljRadijusa));
      listaCijena.put(106,( 71*cijena));
       ////strijeli vatreni 2 klasa
      listaSteteZaArmor.put(422,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(422,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(422,( 0*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(422,( 0*mnoziteljPostotkaUsporavanja));
	  
	  listaToranjRofF.put(107,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(107,( rTor*mnoziteljRadijusa));
      listaCijena.put(107,( 71*cijena));
      ////strijeli vatreni 3 klasa
      listaSteteZaArmor.put(423,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(423,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(423,( 0*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(423,( 0*mnoziteljPostotkaUsporavanja));
	  
	  listaToranjRofF.put(108,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(108,( rTor*mnoziteljRadijusa));
      listaCijena.put(108,( 71*cijena));
      //strijelci otrovni 1 rank
      listaSteteZaArmor.put(418,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(418,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(418,( 4.5f*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(418,( 50*mnoziteljPostotkaUsporavanja));
	  
	  listaToranjRofF.put(109,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(109,( rTor*mnoziteljRadijusa));
      listaCijena.put(109,( 71*cijena));
      //strijelci otrovni 2 rank
      listaSteteZaArmor.put(419,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(419,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(419,( 5*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(419,( 50*mnoziteljPostotkaUsporavanja));
	  
	  listaToranjRofF.put(110,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(110,( rTor*mnoziteljRadijusa));
      listaCijena.put(110,( 71*cijena));
    //strijelci otrovni 3 rank
      listaSteteZaArmor.put(420,( 1*mnoziteljSteteArm));
	  listaSteteZaHelth.put(420,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(420,( 5.5f*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(420,( 50*mnoziteljPostotkaUsporavanja));
	  
	  listaToranjRofF.put(111,( 1.5f*mnoziteljRofF));
	  listaRadijusa.put(111,( rTor*mnoziteljRadijusa));
      listaCijena.put(111,( 71*cijena));
	  
	
  }
  private void parametriTornjevaAlkemicara(float mnoziteljRofF,float mnoziteljSteteHel,float mnoziteljSteteArm,float cijena,float mnoziteljUsporavanja, float mnoziteljPostotkaUsporavanja,float mnoziteljRadijusa){
	
	  listaSteteZaArmor.put(403,(armorZaAlkemicare*mnoziteljSteteArm));//setira granate koje su 401
	  listaSteteZaHelth.put(403,(18*mnoziteljSteteHel));
	  listaVrijemeUsporavanja.put(403,( 2f*mnoziteljUsporavanja));
	  listaPostotakUsporavanja.put(403,( 80*mnoziteljPostotkaUsporavanja));
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float rTor=2.6f*PpCm;
	  listaToranjRofF.put(177,( 0.5f*mnoziteljRofF));
	  listaRadijusa.put(177,( rTor*mnoziteljRadijusa));
	  listaCijena.put(177, 71f);
	  ////////////////////////
	////toranj alkemicar 1, razina
		  listaSteteZaArmor.put(412,( armorZaAlkemicare*mnoziteljSteteArm));//setira granate koje su 401
		  listaSteteZaHelth.put(412,(10f*mnoziteljSteteHel));
		  listaVrijemeUsporavanja.put(412,(1f*mnoziteljUsporavanja));
		  listaPostotakUsporavanja.put(412,( 70*mnoziteljPostotkaUsporavanja));
		   PpCm=(xPiksCm+yPiksCm)/2;
		   rTor=2.7f*PpCm;
		  listaToranjRofF.put(175,( 0.5f*mnoziteljRofF));
		  listaRadijusa.put(175,( rTor*mnoziteljRadijusa));
		  listaCijena.put(175, 30f);
		  ////////////////////////
		////toranj alkemicar 2, razina
				  listaSteteZaArmor.put(413,( armorZaAlkemicare*mnoziteljSteteArm));//setira granate koje su 401
				  listaSteteZaHelth.put(413,(13f*mnoziteljSteteHel));
				  listaVrijemeUsporavanja.put(413,(1.5f*mnoziteljUsporavanja));
				  listaPostotakUsporavanja.put(413,( 70*mnoziteljPostotkaUsporavanja));
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.8f*PpCm;
				  listaToranjRofF.put(176,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(176,( rTor*mnoziteljRadijusa));
				  listaCijena.put(176, 40f);
				  ////////////////////////
		   ////toranj alkemicar teleport 1 rank
			
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.8f*PpCm;
				  listaToranjRofF.put(180,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(180,( rTor*mnoziteljRadijusa));
				  listaCijena.put(180, 70f);
				  ////////////////////////	
				  ////toranj alkemicar teleport 2 rank
				 
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.9f*PpCm;
				  listaToranjRofF.put(181,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(181,( rTor*mnoziteljRadijusa));
				  listaCijena.put(181, 70f);
				  ////////////////////////	
				  ////toranj alkemicar teleport 3 rank
				
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=3f*PpCm;
				  listaToranjRofF.put(182,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(182,( rTor*mnoziteljRadijusa));
				  listaCijena.put(182, 70f);
				  ////////////////////////	
				  ////toranj alkemicar teleport
				 
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.7f*PpCm;
				  listaToranjRofF.put(178,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(178,( rTor*mnoziteljRadijusa));
				  listaCijena.put(178, 80f);
				  ////////////////////////	
			 ////toranj alkemicar medic
				 
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.7f*PpCm;
				  listaToranjRofF.put(179,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(179,( rTor*mnoziteljRadijusa));
				  listaCijena.put(179, 80f);
				  ////////////////////////				
				  ////toranj alkemicar medic 1 rank
				
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.8f*PpCm;
				  listaToranjRofF.put(183,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(183,( rTor*mnoziteljRadijusa));
				  listaCijena.put(183,70f);
				  ////////////////////////	
				  ////toranj alkemicar medic 2 rank
				
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=2.9f*PpCm;
				  listaToranjRofF.put(184,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(184,( rTor*mnoziteljRadijusa));
				  listaCijena.put(184,70f);
				  ////////////////////////		
				  ////toranj alkemicar medic 3 rank
				 
				   PpCm=(xPiksCm+yPiksCm)/2;
				   rTor=3f*PpCm;
				  listaToranjRofF.put(185,( 0.4f*mnoziteljRofF));
				  listaRadijusa.put(185,( rTor*mnoziteljRadijusa));
				  listaCijena.put(185,70f);
				  ////////////////////////	
	
  }
  private void parametriTornjevaKasarna(float mnoziteljVrTreninga,float mnoziteljHeltha,float mnoziteljArmora,float mnoziteljSteteHel,float mnoziteljSteteArm,float cijena,float mnoziteljUsporavanja, float mnoziteljPostotkaUsporavanja,float mnoziteljRadijusa){
	  ////toranj kasarna 1, razina
	///branitelj 1. razina//
	  int IDToranj=150;
	  int IDBranitelja=1;
	  listaHeltha.put(IDBranitelja,(50*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (10*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(0.5f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(1*mnoziteljSteteHel));
	 
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 30f);
	  ////////////////////////
	///branitelj 2. razina//
	  IDBranitelja=2;
	  IDToranj=151;
	  listaHeltha.put(IDBranitelja,(60*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (15*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(0.75f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(1.5f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	///branitelj 3. razina//
	  IDToranj=152;
	  IDBranitelja=3;
	  listaHeltha.put(IDBranitelja,(70*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (20*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(1f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(2.0f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	///branitelj gorstak//
	  IDToranj=153;
	  IDBranitelja=4;
	  listaHeltha.put(IDBranitelja,(100*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (10*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(3*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(6*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 50f);
	  ////////////////////////
	///branitelj gorstak 1 rank//
	  IDToranj=159;
	  IDBranitelja=9;
	  listaHeltha.put(IDBranitelja,(110*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (15*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(3.5f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(7f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	///branitelj gorstak 2 rank//
	  IDToranj=160;
	  IDBranitelja=10;
	  listaHeltha.put(IDBranitelja,(120*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (20*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(4f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(8f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	///branitelj gorstak 3 rank//
	  IDToranj=161;
	  IDBranitelja=11;
	  listaHeltha.put(IDBranitelja,(130*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (25*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(4.5f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(9f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	  ///branitelj vitez//
	  IDToranj=154;
	  IDBranitelja=5;
	  listaHeltha.put(IDBranitelja,(100*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (40*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(1.5f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(3f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 50f);
	  ////////////////////////
	  ///branitelj vitez rank1//
	  IDToranj=156;
	  IDBranitelja=6;
	  listaHeltha.put(IDBranitelja,(110*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (45*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(2f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(3.0f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	  ///branitelj vitez rank2//
	  IDToranj=157;
	  IDBranitelja=7;
	  listaHeltha.put(IDBranitelja,(120*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (50*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(2.5f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(4f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	  ///branitelj vitez rank3//
	  IDToranj=158;
	  IDBranitelja=8;
	  listaHeltha.put(IDBranitelja,(130*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (55*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(3f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(5f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	
	  ////////////////////////
	  ///branitelj strijelci//
	  IDToranj=155;
	  IDBranitelja=51;
	  listaHeltha.put(IDBranitelja,(100*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(0*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(1f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 50f);
	  ////////////////////////
	///branitelj strijelci 1 rank//
	  IDToranj=162;
	  IDBranitelja=52;
	  listaHeltha.put(IDBranitelja,(110*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(0f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(2*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	///branitelj strijelci 2 rank//
	  IDToranj=163;
	  IDBranitelja=53;
	  listaHeltha.put(IDBranitelja,(120*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(0f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(3f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
	///branitelj strijelci 3 rank//
	  IDToranj=164;
	  IDBranitelja=54;
	  listaHeltha.put(IDBranitelja,(130*mnoziteljHeltha));
	  listaArmora.put(IDBranitelja, (0*mnoziteljArmora));
	  listaSteteZaArmor.put(IDBranitelja,(0f*mnoziteljSteteArm));
	  listaSteteZaHelth.put(IDBranitelja,(4f*mnoziteljSteteHel));
	   PpCm=(xPiksCm+yPiksCm)/2;
	   rTor=2f*PpCm;
	  listaToranjRofF.put(IDToranj,( 10f*mnoziteljVrTreninga));
	  listaRadijusa.put(IDToranj,( rTor*mnoziteljRadijusa));
	  listaCijena.put(IDToranj, 40f);
	  ////////////////////////
  }
  
  public void setNaknadnoParametreElementa(int indikator, float helth, float armor,float stetazahelth, float stetazaarmor,float cijena){
	  if(helth>=0) this.listaHeltha.put(indikator,helth);
	  if(armor>=0) this.listaArmora.put(indikator,armor);
	  if(stetazahelth>=0) this.listaSteteZaHelth.put(indikator,stetazahelth);
	  if(stetazaarmor>=0) this.listaSteteZaArmor.put(indikator,stetazaarmor);
	  if(cijena>=0) this.listaCijena.put(indikator,cijena);
  }
  ////////////////////////////////////////
  ///////PUBLIC METODE/////////////////
  public void nacrtajLoadEkran(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){

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
		 

		
		
			tekPoceo=false;
			sir=1*can.getWidth()/3;
		 	if(sprUvoda!=null){	
		             vis=  sir* (sprUvoda.getVisRezanja(0)/sprUvoda.getSirRezanja(0));
		 	}
		    recCrt.set(can.getWidth()/2-sir/2,can.getHeight()/2-vis/2,can.getWidth()/2+sir/2,can.getHeight()/2+vis/2);
			rectLoad.set( recCrt.left,  recCrt.top,recCrt.left+postotakLodiranjaFaze*sir/100, recCrt.top +vis);
		}
		if(can!=null){
		can.drawRGB(0, 0, 0);
     	if(sprUvoda!=null){	
     		sprUvoda.nacrtajSprite(can, 0, 0, 0, recCrt);
	
	   }
     	rectLoad.set( recCrt.left,  recCrt.bottom,recCrt.left+postotakLodiranjaFaze*sir/100, recCrt.bottom +vis/20);

		    can.drawRect(rectLoad, paint);
		}
  } 
  public void lodirajResurse(Activity activity, SoundPool soundPool,BitmapFactory.Options opts, GameLogic gl, UIManager uiMan){
	  this.uiMan=uiMan;
	  this.gLog=gl;
	  lodirajNekeRandomPodatke();
	  lodirajAchievementse();
	  lodirajSpriteoeSpecificneZaPojedinuFazu(activity,soundPool,opts);
	  lodirajStandardneSlike(activity,soundPool,opts);
	  bilderLodirajZvukoveUSpriteoveINekeStandardneSpriteove(activity,soundPool,opts);
  }
  private void lodirajNekeRandomPodatke(){
	  if(this.brFaze>=8){
	   // dodaje abiliti///////
	  SQLiteDatabase bazaPodataka;
        bazaPodataka= this.context.openOrCreateDatabase(IgricaActivity.glavniDB,Activity.MODE_PRIVATE, null);
        bazaPodataka.execSQL(" INSERT OR REPLACE INTO " +  
        		IgricaActivity.listaAchievementsa +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.IDAchievementsa+"')"+ // ne ubacujem tezinu na pocetku
   	                     " Values ('"+ this.IDSlotaIzFI+"','"+ IgricaActivity.achievementDeathFromAbove +"');");
       bazaPodataka.close();
        ////////////////
	  }
	  if(this.brFaze>=5){
		  // dodaje abiliti///////
	        
			  SQLiteDatabase bazaPodataka;
		        bazaPodataka=this.context.openOrCreateDatabase(IgricaActivity.glavniDB,Activity.MODE_PRIVATE, null);
		        bazaPodataka.execSQL(" INSERT OR REPLACE INTO " +  
		        		IgricaActivity.listaAchievementsa +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.IDAchievementsa+"')"+ // ne ubacujem tezinu na pocetku
		   	                     " Values ('"+ this.IDSlotaIzFI +"','"+ IgricaActivity.achievementNoOneShellPas +"');");
		       bazaPodataka.close();
	    ////////////////
	  }
  }
   private void lodirajOpceniteSpriteove(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
	   Bitmap bi;
	   //ability spriteovi
		 SpriteHendler abilitySprite=new SpriteHendler(6);
		 abilitySprite.dodajNoviSprite(null,1,1,0);// za botune
       
		 
		 
		 
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8okvir,opts);// za glavni prozor
	
		 abilitySprite.dodajNoviSprite(bi,1,1,0);// ubacio sam podozje ovdje malo bedasto izgleda ali mi se nije dalo raditi posebno
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8podnozje,opts);
		 abilitySprite.dodajNoviSprite(bi,1,1,0);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8bljestanje,opts);
		 abilitySprite.dodajNoviSprite(bi,1,1,0);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8reinforcement,opts);// sljedeci su za crtanje ispod glvnog  za svaki od abilitoija
		 abilitySprite.dodajNoviSprite(bi,1,1,0);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8lavabomb,opts);// sljedeci su za crtanje ispod glvnog  za svaki od abilitoija
		 abilitySprite.dodajNoviSprite(bi,1,1,0);
		 dodajSprite(700,abilitySprite);
		 //////////	
		 //////////
		 this.postotakLodiranjaFaze+=this.dxLodiranja;
		 uiMan.nacrtajIzvanThreadno();
		 ///////////
		 ///////////
		 ////////////////////////////////////////
		 ///dodatci za lokove onomatopeje
		
		 SpriteHendler dodatakNaLikoveOnomatopeje=new SpriteHendler(3);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8zvuk0borbe1,opts);
		 dodatakNaLikoveOnomatopeje.dodajNoviSprite(bi,1,1,0);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8zvuk0borbe2,opts);
		 dodatakNaLikoveOnomatopeje.dodajNoviSprite(bi,1,1,0);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8zvuk0borbe3,opts);
		 dodatakNaLikoveOnomatopeje.dodajNoviSprite(bi,1,1,0);
		 dodajSprite(98,dodatakNaLikoveOnomatopeje);
		 //////////	
		 //////////
		 this.postotakLodiranjaFaze+=this.dxLodiranja;
		 uiMan.nacrtajIzvanThreadno();
		 ///////////
		 ///////////
		////dodatci na likove
		SpriteHendler dodatakNaLikove=new SpriteHendler(8,20, soundPool);
	    bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8vatra,opts);
		dodatakNaLikove.dodajNoviSprite(bi,6,1,6);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8otrovan,opts);
		dodatakNaLikove.dodajNoviSprite(bi,6,1,6);
		 bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8zaleden,opts);
		dodatakNaLikove.dodajNoviSprite(bi,6,1,6);
		bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8lokva0branitelj,opts);
		dodatakNaLikove.dodajNoviSprite(bi,3,1,0);
		bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8lokva0napadaci,opts);
		dodatakNaLikove.dodajNoviSprite(bi,3,1,0);
		bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8teleport,opts);
		dodatakNaLikove.dodajNoviSprite(bi,7,1,6);
		bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8lijecenje,opts);
		dodatakNaLikove.dodajNoviSprite(bi,12,1,8);
		bi=BitmapFactory.decodeResource(activity.getResources(), R.drawable.dodatak8lik8napadac8smrt,opts);
		dodatakNaLikove.dodajNoviSprite(bi,5,1,5);
		
		
		
		dodajSprite(99,dodatakNaLikove);
		 //////////	
		 //////////
		 this.postotakLodiranjaFaze+=this.dxLodiranja;
		 uiMan.nacrtajIzvanThreadno();
		 ///////////
		 ///////////
		////////////////////
		
		 ///////////
  /////// izbornik
  					SpriteHendler sprIzb=new SpriteHendler(21);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8glavna0osovina,opts),0, 0,0);
  				 	sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8kockica,opts),0, 0,0);
  				 	sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8navoj,opts),0, 0,0);
  				 	sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8nekoristen,opts),4, 1,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8sell,opts),0, 0,0);
  				 	sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8osnovni0dugmici,opts),4, 0,0);
  				 	sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8duplistrijelci,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8troduplistrijelci,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8kasarna2razina,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8kasarna3razina,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8kasarna8racvanje,opts),3, 1,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8strijelci8vatreni0otrovni,opts),2, 1,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8minobacac8tnt,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8minobacac8armor,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8minobacac8kluster0naplam,opts),2, 1,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8frezz1razina,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8frezz2razina,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8teleport8medic,opts),2, 1,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8upgrade1,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8upgrade2,opts),0, 0,0);
  					sprIzb.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8toranj8upgrade3,opts),0, 0,0);
  		
  					IzbornikZaToranj izbTor=new IzbornikZaToranj(sprIzb,uiMan,gLog,301);
  				    dodajIzbornikZaTornjeve(izbTor);
  					uiMan.dodajElementUListu(izbTor,3);
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  					SpriteHendler reset=new SpriteHendler(1);  
  					reset.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8restart0gumbic,opts),1, 1,0);
  					//reset.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.drawable.izbornik8exitgame,opts),1, 1,0);  
  					///exit
  					SpriteHendler exit=new SpriteHendler(5);  
  					 exit.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8strelicagumbicpng,opts),1, 1,0);
  					 exit.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8exitgame,opts),1, 1,0);  
  					 exit.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8no,opts),1, 1,0); 
  					 exit.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8exit,opts),1, 1,0); 
  					 exit.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.izbornik8restartbotun,opts),1, 1,0); 
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  					 ////mute botu
  					  SpriteHendler sprMute=new SpriteHendler(2);
  					 	sprMute.dodajNoviSprite(null,0, 0,0);
  					 sprMute.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.botun8mute,opts),2, 1,0);
  					this.taskbar.dodajSpriteMute(sprMute);
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  						///bodovi
  						SpriteHendler bodovi=new SpriteHendler(2);  
  						bodovi.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.tasbar8bodovi,opts),3, 1,0);
  						bodovi.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8bodovi8podloga,opts),1, 1,0);
  						 //////////	
  						 //////////
  						 this.postotakLodiranjaFaze+=this.dxLodiranja;
  						 uiMan.nacrtajIzvanThreadno();
  						 ///////////
  						 ///////////
  						///pause
  					SpriteHendler pause=new SpriteHendler(1);  
  					pause.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8pause,opts),1, 1,0);
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  					//resume
  					SpriteHendler resume=new SpriteHendler(1);  
  					resume.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8resume,opts),1, 1,0);
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  					//dolazak protivnika
  					        /////test
  					///kontrole zvuka
  					SpriteHendler kontroleZvuka=new SpriteHendler(3);  
  					kontroleZvuka.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8zvuk8smanji,opts),1, 1,0);
  					kontroleZvuka.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8zvuk8mjerac,opts),1, 1,0); 
  					kontroleZvuka.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8zvuk8pojacaj,opts),1, 1,0);
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  					SpriteHendler dolazak=new SpriteHendler(3,20,soundPool);  
  					dolazak.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8dolazak0prot8oblacic,opts),10,1, 7);
  					dolazak.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8dolazak0prot8koraci,opts),10,1, 7);
  					dolazak.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.taskbar8dolazak0prot8kraj,opts),10,1, 15);
  					dolazak.dodajZvukISincSaSlRandomSamostalni(soundPool.load(context, R.raw.zvuk8pocetak0vala,1),1, 1f,120,100,70);
  					 //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  				
  					
  					//task.bilderZaDolazakProtivnika(0.3f,0.3f);
  					
  					taskbar.dodajSpriteBodova(bodovi);
  					taskbar.dodajDolazakProtivnikaSprite(dolazak);
  					taskbar.dodajRestartSprite(reset);
  			        taskbar.dodajPauseSprite(pause);
  			        taskbar.dodajResumeSprite(resume);
  			        taskbar.dodajExitSprite(exit);
  			        taskbar.dodajSpriteKontroleZvuka(kontroleZvuka);
  			      
  			        ///////meteor 
  			        SpriteHendler projektilMeteor=new SpriteHendler(5,20,soundPool);  
  			        projektilMeteor.dodajNoviSprite(null,1, 1,12);//ispaljenje
  			        projektilMeteor.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8meteor8projektil,opts),4, 1,5);//let
  			        projektilMeteor.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8meteor8eksplozion,opts),10, 1,7);// pogodak
  			        projektilMeteor.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8meteor0rep,opts),5, 1,5);// rep
  			        projektilMeteor.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ability8meteor8mrlja,opts),3, 1,0);// mrlja eksplozije
  			  
  			        dodajSprite(405,projektilMeteor);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  					///////bomba
  			        SpriteHendler projektil=new SpriteHendler(5,20,soundPool);  
  			        Bitmap isp=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8bomba0ispaljivanje0859kb,opts);
  			        projektil.dodajNoviSprite(isp,11, 1,12);//ispaljenje
  			        projektil.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8bomba024_4kb,opts),5, 1,6);//let
  			        Bitmap eks=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8bomba0eksplozija0859kb,opts);
  			        projektil.dodajNoviSprite(eks,11, 1,12);// pogodak
  			        Bitmap rep=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8bomba0rep0625kb,opts);
  			        projektil.dodajNoviSprite(rep,8, 1,12);// rep
  			        Bitmap mrlj=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8bomba8mrlja,opts);
  			        projektil.dodajNoviSprite(mrlj,1, 1,0);// mrlja eksplozije
  			      int[] vre=new int[11];
  		        int[] sli=new int[11];
  		        int[] stu=new int[11];
  		        int[] red=new int[11];
  			        //sl1
  			        vre[0]=100; vre[1]=100; vre[2]=100; vre[3]=100; vre[4]=100; vre[5]=100; vre[6]=150; vre[7]=150; vre[8]=150; vre[9]=150; vre[10]=150;
  			        sli[0]=0; sli[1]=0; sli[2]=0; sli[3]=0; sli[4]=0; sli[5]=0; sli[6]=0; sli[7]=0; sli[8]=0; sli[9]=0; sli[10]=0; 
  			        stu[0]=0;  stu[1]=1;  stu[2]=2;  stu[3]=3;  stu[4]=4;  stu[5]=5; stu[6]=6;  stu[7]=7;  stu[8]=8;  stu[9]=9;  stu[10]=10;
  					red[0]=	0; red[1]=	0; red[2]=	0; red[3]=	0; red[4]=	0; red[5]=	0;  red[6]=	0; red[7]=	0; red[8]=	0; red[9]=	0; red[10]=	0; 
  			       int  kodAnim=0;
  			        
  			        projektil.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
  			        dodajSprite(401,projektil);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////bomba tnt
  			        SpriteHendler projektilTNT=new SpriteHendler(5,20,soundPool);  
  			        
  			        projektilTNT.dodajNoviSprite(isp,11, 1,12);//ispaljenje
  			        projektilTNT.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tnt,opts),0, 0,6);//let
  			        Bitmap bitEksTNT=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8bomba0eksplozija8tnt);
  			        projektilTNT.dodajNoviSprite(bitEksTNT,11, 1,12);// pogodak
  			        projektilTNT.dodajNoviSprite(rep,8, 1,12);// rep
  			        projektilTNT.dodajNoviSprite(mrlj,1, 1,0);// mrlja eksplozije
  			     
  	                projektilTNT.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
  			        dodajSprite(408,projektilTNT);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////bomba armor
  			        SpriteHendler projektilARM=new SpriteHendler(5,20,soundPool);  
  			        
  			        projektilARM.dodajNoviSprite(isp,11, 1,12);//ispaljenje
  			        projektilARM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8armorbomba,opts),0, 0,6);//let
  			        projektilARM.dodajNoviSprite(bitEksTNT,11, 1,12);// pogodak
  			        projektilARM.dodajNoviSprite(rep,8, 1,12);// rep
  			        projektilARM.dodajNoviSprite(mrlj,1, 1,0);// mrlja eksplozije
  			   
  	                projektilARM.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
  			        dodajSprite(409,projektilARM);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////bomba kluster
  			        SpriteHendler projektilKLUSTER=new SpriteHendler(5,20,soundPool);  
  			        
  			        projektilKLUSTER.dodajNoviSprite(isp,11, 1,12);//ispaljenje
  			        projektilKLUSTER.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8cluster,opts),0, 0,6);//let
  			        projektilKLUSTER.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8cluster8eksplozija),11, 1,7);// pogodak
  			        projektilKLUSTER.dodajNoviSprite(rep,8, 1,12);// rep
  			        projektilKLUSTER.dodajNoviSprite(mrlj,1, 1,0);// mrlja eksplozije
  			      
  	                projektilKLUSTER.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
  			        dodajSprite(410,projektilKLUSTER);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////bomba naplam
  			        SpriteHendler projektilNAPALM=new SpriteHendler(5,20,soundPool);  
  			        
  			        projektilNAPALM.dodajNoviSprite(isp,11, 1,12);//ispaljenje
  			        projektilNAPALM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8naplam,opts),5, 0,5);//let
  			        projektilNAPALM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8naplam8eksplozija),12, 1,7);// pogodak
  			        projektilNAPALM.dodajNoviSprite(rep,8, 1,12);// rep
  			        projektilNAPALM.dodajNoviSprite(mrlj,1, 1,0);// mrlja eksplozije
  			      
  	                projektilNAPALM.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
  			        dodajSprite(411,projektilNAPALM);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			    
  			        ///////strijela
  			        SpriteHendler strijela=new SpriteHendler(4,20,soundPool);  
  			        strijela.dodajNoviSprite(null,11, 1,12);//ispaljenje
  			        strijela.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijela,opts),1, 1,0);//let
  			        strijela.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijela0pogodak0136kb,opts),7, 1,15);// pogodak
  			        strijela.dodajNoviSprite(null,8, 1,12);// rep
  			        
  			        dodajSprite(402,strijela);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////strijela vatrena
  			        SpriteHendler strijelaVatr=new SpriteHendler(4,20,soundPool);  
  			        strijelaVatr.dodajNoviSprite(null,11, 1,12);//ispaljenje
  			        strijelaVatr.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijelavatrena,opts),4, 1,0);//let
  			        strijelaVatr.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijelavatrena8pogodak,opts),4, 1,7);// pogodak
  			        strijelaVatr.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijelavatrena8rep,opts),4, 1,7);// rep
  			      
  			        dodajSprite(406,strijelaVatr);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////strijela otrovna
  			        SpriteHendler strijelaOtrovna=new SpriteHendler(4,20,soundPool);  
  			        strijelaOtrovna.dodajNoviSprite(null,11, 1,12);//ispaljenje
  			        strijelaOtrovna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijelaotrovana,opts),1, 1,0);//let
  			        strijelaOtrovna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijelaotrovna8pogodak,opts),4, 1,7);// pogodak
  			        strijelaOtrovna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8strijelaotrovna8rep,opts),4, 1,7);// rep
  			        
  			        dodajSprite(407,strijelaOtrovna);
  			         //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////freez zraka 3. razina
  			        SpriteHendler freezZraka=new SpriteHendler(4,20,soundPool);  
  			        freezZraka.dodajNoviSprite(null,0, 0,0);//ispaljenje
  			        //freezZraka.dodajNoviSprite(BitmapFactory.decodeResource(getResources(), R.raw.mapa8tekstura,opts),4,4,5);//ispaljenje
  			        freezZraka.dodajNoviSprite(null,4, 1,15);//let
  			        Bitmap biFrezPog=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freezzraka8pogodak029kb,opts);
  			        freezZraka.dodajNoviSprite(biFrezPog,6, 1,15);// pogodak
  			        
  			        freezZraka.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freezzraka8rep0117kb,opts),12, 1,5);// rep
  			        
  			        dodajSprite(403,freezZraka);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////freez  1. razina
  			        SpriteHendler freezProj1Razina=new SpriteHendler(4,20,soundPool);  
  			        freezProj1Razina.dodajNoviSprite(null,11, 1,12);//ispaljenje
  			        freezProj1Razina.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freeztop1razina,opts),3, 1,4);//let
  			        freezProj1Razina.dodajNoviSprite(biFrezPog,6, 1,10);// pogodak
  			        freezProj1Razina.dodajNoviSprite(null,4, 1,7);// rep
  			       
  			        dodajSprite(412,freezProj1Razina);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  			        ///////freez  2. razina
  			        SpriteHendler freezProj2Razina=new SpriteHendler(4,20,soundPool);  
  			        freezProj2Razina.dodajNoviSprite(null,11, 1,12);//ispaljenje
  			        freezProj2Razina.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freeztop1razina,opts),3, 1,4);//let
  			        freezProj2Razina.dodajNoviSprite(biFrezPog,6, 1,10);// pogodak
  			        freezProj2Razina.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freeztop2razina8rep,opts),6, 1,7);// rep
  			       
  			        dodajSprite(413,freezProj2Razina);
  			      //////////	
  					 //////////
  					 this.postotakLodiranjaFaze+=this.dxLodiranja;
  					 uiMan.nacrtajIzvanThreadno();
  					 ///////////
  					 ///////////
  }
  private void lodirajBraniteljeSpriteove(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
	     {
             
	           ///////lik pojacanje
						
						SpriteHendler pojacanje=new SpriteHendler(3,20,soundPool);
						Bitmap b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8prva0razina,opts);
						pojacanje.dodajNoviSprite(b,6, 4, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8prva0razina8borba,opts);
						pojacanje.dodajNoviSprite(b,4, 2, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.lik8ribar8mrtav,opts);
						pojacanje.dodajNoviSprite(b, 1, 1, 0);
				
						
						
						dodajSprite(49,pojacanje);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
				        ///////////////////////////////////
						///////lik ribar
						
						SpriteHendler bratic=new SpriteHendler(3,20,soundPool);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8prva0razina,opts);
						bratic.dodajNoviSprite(b,6, 4, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8prva0razina8borba,opts);
						bratic.dodajNoviSprite(b,4, 2, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.lik8ribar8mrtav,opts);
						bratic.dodajNoviSprite(b, 1, 1, 0);
				
			
						dodajSprite(1,bratic);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						//////////////////////branitelj druga razina
						SpriteHendler barnitelj2razina=new SpriteHendler(3,20,soundPool);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8druga0razina,opts);
						barnitelj2razina.dodajNoviSprite(b,6, 4, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8druga0razina8borba,opts);
						barnitelj2razina.dodajNoviSprite(b,4, 2, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.lik8ribar8mrtav,opts);
						barnitelj2razina.dodajNoviSprite(b, 1, 1, 0);
						dodajSprite(2,barnitelj2razina);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///////////////////////branitelj treca razina
						SpriteHendler barnitelj3razina=new SpriteHendler(3,20,soundPool);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8treca0razina,opts);
						barnitelj3razina.dodajNoviSprite(b,6, 4, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8treca0razina8borba,opts);
						barnitelj3razina.dodajNoviSprite(b,4, 2, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.lik8ribar8mrtav,opts);
						barnitelj3razina.dodajNoviSprite(b, 1, 1, 0);
					
				
						dodajSprite(3,barnitelj3razina);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						////////////////////////branitelj cetvrta razina
						SpriteHendler barniteljGorstak=new SpriteHendler(2,20,soundPool);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8gorstak,opts);
						barniteljGorstak.dodajNoviSprite(b,6, 4, 6);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8gorstak8borba,opts);
						
						barniteljGorstak.dodajNoviSprite(b,4, 2, 6);
					
						dodajSprite(4,barniteljGorstak);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						////////////////////////////vitez 
						SpriteHendler barniteljVitez=new SpriteHendler(2,20,soundPool);
					
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8vitez,opts);
			
						barniteljVitez.dodajNoviSprite(b,6, 4, 6);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8vitez8borba,opts);
						barniteljVitez.dodajNoviSprite(b,4, 2, 5);
						
						dodajSprite(5,barniteljVitez);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						/////////////////////////////branitelj strijelci
						SpriteHendler barniteljStrijelci=new SpriteHendler(4,20,soundPool);
						
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8strijelac,opts);
						barniteljStrijelci.dodajNoviSprite(b,5, 4, 5);
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8strijelci8borba,opts);
						barniteljStrijelci.dodajNoviSprite(b,4, 2, 3);
						barniteljStrijelci.dodajNoviSprite(null,0, 0, 0);
						
				    	 Log.d("game onCreate", "memorije GlobalAllocSize poslije lodiranje=" + Debug.getGlobalAllocSize()/1024);
				    	 Log.d("game onCreate", "memorije ThreadAllocSize poslije lodiranje=" + Debug.getThreadAllocSize()/1024);	
						b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.branitelj8strijelci8ispaljivanje,opts);
						barniteljStrijelci.dodajNoviSprite(b,4,8, 5);
			
						dodajSprite(51,barniteljStrijelci);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						////TORNJEVI
					     if(this.brFaze==11){
					    		///toranj embrio blatni
								SpriteHendler toranj=new SpriteHendler(2);  
								toranj.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8embrio8blatni,opts),1, 1,0);
								toranj.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8rank,opts),1, 1,0);
								dodajSprite(200, toranj);
					     }
					     else if(this.brFaze==12||this.brFaze==13){
					    	   ///toranj embrio kamena
								SpriteHendler toranj=new SpriteHendler(2);  
								toranj.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8embrio8kameniti,opts),1, 1,0);
								toranj.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8rank,opts),1, 1,0);
								dodajSprite(200, toranj);
					     }
					     else {
					    	///toranj embrio obicni
								SpriteHendler toranj=new SpriteHendler(2);  
								toranj.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8embrio,opts),1, 1,0);
								toranj.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8rank,opts),1, 1,0);
								dodajSprite(200, toranj);
					     }
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///toranj alkemicar  3. razina 
						SpriteHendler toranjAlkemicar=new SpriteHendler(2,1,soundPool); 
						Bitmap biTorAl=BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8alkemicar0651kb,opts);
						toranjAlkemicar.dodajNoviSprite(biTorAl,1, 1,0);
						Bitmap frezTOP=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8frezzraka8freez0top,opts);
						toranjAlkemicar.dodajNoviSprite( frezTOP,7, 3,5);
						
						dodajSprite(177, toranjAlkemicar);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///toranj alkemicar  teleport 
						SpriteHendler toranjAlkemicarTel=new SpriteHendler(3,1,soundPool); 
						toranjAlkemicarTel.dodajNoviSprite(biTorAl,1, 1,0);
						
						toranjAlkemicarTel.dodajNoviSprite(frezTOP,7, 3,5);
						toranjAlkemicarTel.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8alkemicar8teleport,opts),5, 1,3);
						
						
						dodajSprite(178, toranjAlkemicarTel);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///toranj alkemicar  medic 
						SpriteHendler toranjAlkemicarMed=new SpriteHendler(3,1,soundPool); 
						toranjAlkemicarMed.dodajNoviSprite(biTorAl,1, 1,0);
						toranjAlkemicarMed.dodajNoviSprite(frezTOP,7, 3,5);
						toranjAlkemicarMed.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8alkemicar8medic,opts),5, 1,5);
						
						dodajSprite(179, toranjAlkemicarMed);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///toranj alkemicar 1 razina   
						SpriteHendler toranjAlkemicar1=new SpriteHendler(2,1,soundPool); 
						toranjAlkemicar1.dodajNoviSprite(biTorAl,1, 1,0);
						toranjAlkemicar1.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freeztop1razina8top,opts),3, 3,5);
						
						dodajSprite(175, toranjAlkemicar1);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///toranj alkemicar 2. razina   
						SpriteHendler toranjAlkemicar2=new SpriteHendler(2,1,soundPool); 
						toranjAlkemicar2.dodajNoviSprite(biTorAl,1, 1,0);
						toranjAlkemicar2.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8freeztop2razina8top,opts),3, 3,5);
						
						dodajSprite(176, toranjAlkemicar2);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						///toranj kasarna 1 razine  
						SpriteHendler toranjKasarna=new SpriteHendler(6); 
						toranjKasarna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8kasarna0449,opts),1, 1,0);
						toranjKasarna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8kasarna8zvjezdica,opts),1, 1,0);
						toranjKasarna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8kasarna8grm,opts),1, 1,0);
						toranjKasarna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8kasarna8stit,opts),1, 1,0);
						toranjKasarna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8kasarna8zastavica,opts),1, 1,0);
						toranjKasarna.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8kasarna8kiss,opts),1, 1,0);
				
						dodajSprite(150, toranjKasarna);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						//////minobacac
						SpriteHendler minobacac= new SpriteHendler(4);
						Bitmap min=BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac,opts);
						minobacac.dodajNoviSprite(min,0, 0,0);
						minobacac.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8punjenje,opts),7,1,4);
						Bitmap pot =BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8potezanje,opts);
						minobacac.dodajNoviSprite(pot,3,1,3);
						minobacac.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8hrpa0kugli,opts),1,1,0);
						dodajSprite(125, minobacac);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						 //////minobacac armor
						SpriteHendler minobacacARM= new SpriteHendler(4);
										
						minobacacARM.dodajNoviSprite(min,0, 0,0);
						minobacacARM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8punjenje8armor,opts),7,1,4);
						minobacacARM.dodajNoviSprite(pot,3,1,3);
					    minobacacARM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8hrpa0armor,opts),1,1,0);
					   dodajSprite(127, minobacacARM);
					   //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
					   //////minobacac tnt
						SpriteHendler minobacacTNT= new SpriteHendler(4);
										
						minobacacTNT.dodajNoviSprite(min,0, 0,0);
						minobacacTNT.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8punjenje8tnt,opts),7,1,4);
						minobacacTNT.dodajNoviSprite(pot,3,1,3);
					    minobacacTNT.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8hrpa0tnt,opts),1,1,0);
					   dodajSprite(126, minobacacTNT);
					   //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
					   //////minobacac kluster
						SpriteHendler minobacacKLUSTER= new SpriteHendler(4);
								
						
				        minobacacKLUSTER.dodajNoviSprite(min,0, 0,0);
						minobacacKLUSTER.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8punjenje8kluster,opts),7,1,4);
						minobacacKLUSTER.dodajNoviSprite(pot,3,1,3);
						minobacacKLUSTER.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8hrpa0klustera,opts),1,1,0);
						dodajSprite(128, minobacacKLUSTER);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
					    //////minobacac kluster
						SpriteHendler minobacacNAPALM= new SpriteHendler(4);
																		
						 minobacacNAPALM.dodajNoviSprite(min,0, 0,0);
						 minobacacNAPALM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8punjenje8naplam,opts),7,1,4);
						 minobacacNAPALM.dodajNoviSprite(pot,3,1,3);
					     minobacacNAPALM.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8minobacac8hrpa0naplam,opts),1,1,0);
						dodajSprite(129, minobacacNAPALM);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
				    	//////strijelci
						SpriteHendler strijelci= new SpriteHendler(6,20,soundPool); 
						strijelci.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8strijelci234kb,opts),0, 0,0);
						strijelci.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8strijelaci0vojnici,opts),4, 8,5);
						strijelci.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8strijelci8vatra0zastava,opts),1, 1,0);
						strijelci.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8strijelci8baklja,opts),5, 1,5);
						strijelci.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8strijelci8otrov0zastava,opts),1, 1,0);
						strijelci.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.toranj8strijelci8kotao,opts),5, 1,5);
						 //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
						dodajSprite(101, strijelci);
						dodajSprite(102, strijelci);
						dodajSprite(103, strijelci);
						dodajSprite(104, strijelci);				
						dodajSprite(105, strijelci);
          }
  }
  private void lodirajProtivnikeSpriteove(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
	       
	         if(this.listaUcitanihNaredbi.contains(-1))  {
	            ////lik radnik
		       Bitmap b;
		       SpriteHendler sprHend=new SpriteHendler(2,20,soundPool);
		
	            b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8radnik,opts);
	   
	           b.setDensity(Bitmap.DENSITY_NONE);
		       sprHend.dodajNoviSprite(b, 4, 4, 5);
		
		       b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8radnik8borba,opts);
		       b.setDensity(Bitmap.DENSITY_NONE);
	           sprHend.dodajNoviSprite(b, 4, 2, 5);
	
		       dodajSprite(-1,sprHend );
		       //////////	
				 //////////
				 this.postotakLodiranjaFaze+=this.dxLodiranja;
				 uiMan.nacrtajIzvanThreadno();
				 ///////////
				 ///////////
                   }
	         if(this.listaUcitanihNaredbi.contains(-2))   {
	        	   ////lik sminkerica
					
					
					SpriteHendler mrtvKop=new SpriteHendler(2,20,soundPool);
				
				   Bitmap bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8sminkerica,opts);
				 //  bm= namjestiProzirnost( bm);
				    
				    mrtvKop.dodajNoviSprite(bm, 4, 4, 5);
		             bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8sminkerica8borba,opts);
					 //  bm= namjestiProzirnost( bm);
					    
					    mrtvKop.dodajNoviSprite(bm, 4, 2, 4);
					 
			         dodajSprite(-2,mrtvKop);
				/////
			         //////////	
					 //////////
					 this.postotakLodiranjaFaze+=this.dxLodiranja;
					 uiMan.nacrtajIzvanThreadno();
					 ///////////
					 ///////////
	           }
	         
		        ////////////////////////////
	         if(this.listaUcitanihNaredbi.contains(-3)) {
		        	////lik mrtvac teski
						Bitmap bmm;
						SpriteHendler mrtvTes=new SpriteHendler(2,20,soundPool);
					    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8mma,opts);
					    mrtvTes.dodajNoviSprite(bmm, 4, 4, 5);					
					    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8mma8borba,opts);	   
					    mrtvTes.dodajNoviSprite(bmm, 4, 2, 5);
					    dodajSprite(-3,mrtvTes);
					/////    
					    //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
		           }
	               if(this.listaUcitanihNaredbi.contains(-4))   {
		         ////lik mrtvac mali mac mac
		       Bitmap b;
				 uiMan.nacrtajIzvanThreadno();
				 SpriteHendler sprHend=new SpriteHendler(2,20,soundPool);
			    double vrijemeLodiranja=System.currentTimeMillis();
			    b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8reper,opts);
			    vrijemeLodiranja=(System.currentTimeMillis()-vrijemeLodiranja);
			     Log.d("Load vrijeme", "Lodiranje mrtvacmacmanj=" +vrijemeLodiranja);
			    b.setDensity(Bitmap.DENSITY_NONE);
				sprHend.dodajNoviSprite(b, 4, 4, 5);				
				 b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8reper8borba,opts);
				 sprHend.dodajNoviSprite(b, 4, 2, 5);
				
				dodajSprite(-4,sprHend );
				 //////////	
				 //////////
				 this.postotakLodiranjaFaze+=this.dxLodiranja;
				 uiMan.nacrtajIzvanThreadno();
				 ///////////
				 ///////////
                 }
	               if(this.listaUcitanihNaredbi.contains(-5))   {
		        	   ////lik pocetni
			        	Bitmap bm;
				
			         	SpriteHendler mrtvPoc=new SpriteHendler(2,20,soundPool);
				   
			           bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8pocetni,opts);
			
			    
			             mrtvPoc.dodajNoviSprite(bm, 4, 4, 5);
			  
			
			    
			            bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8pocetni8borba,opts);
			            mrtvPoc.dodajNoviSprite(bm, 4,2, 5);
				
				  
		                dodajSprite(-5,mrtvPoc);
		                //////////	
		       		 //////////
		       		 this.postotakLodiranjaFaze+=this.dxLodiranja;
		       		 uiMan.nacrtajIzvanThreadno();
		       		 ///////////
		       		 ///////////
		          }
	               if(this.listaUcitanihNaredbi.contains(-6)){
			      		 ////lik debeli
							SpriteHendler mrtvDeb=new SpriteHendler(2,20,soundPool);
						
						  Bitmap  bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8debeli,opts);
						 //  bm= namjestiProzirnost( bm);
						    
						    mrtvDeb.dodajNoviSprite(bm, 6, 4, 5);
						 
						    
						    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8debeli8borba,opts);
							 //  bm= namjestiProzirnost( bm);		    
						    mrtvDeb.dodajNoviSprite(bm,5, 2, 5);
					       dodajSprite(-6,mrtvDeb);
					       //////////	
							 //////////
							 this.postotakLodiranjaFaze+=this.dxLodiranja;
							 uiMan.nacrtajIzvanThreadno();
							 ///////////
							 ///////////
			           }
	               if(this.listaUcitanihNaredbi.contains(-7)) {
		        	     ////lik spremacica
						SpriteHendler mrtvCist=new SpriteHendler(2,20,soundPool);
					
					   Bitmap bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8cistacica,opts);
					 //  bm= namjestiProzirnost( bm);
					    
					    mrtvCist.dodajNoviSprite(bm, 4, 4, 5);
					 
					   
					    
					    
					    
					    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8cistacica8borba,opts);
						 //  bm= namjestiProzirnost( bm);
						    
					    mrtvCist.dodajNoviSprite(bm, 5, 2, 5);
					  
				       dodajSprite(-7,mrtvCist);
				        
				       //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
				        
				        
		           }
		           if(this.listaUcitanihNaredbi.contains(-8)){
		        	    ////lik kuhar
	   					SpriteHendler mrtvKuhar=new SpriteHendler(2,20,soundPool);
	   				
	   				  Bitmap  bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8kuhar,opts);
	 
	   				    
	   				 mrtvKuhar.dodajNoviSprite(bm, 4, 4, 4);
	   				 
	   				    
	   				    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8kuhar8borba,opts);

	   					    
	   				 mrtvKuhar.dodajNoviSprite(bm,4, 2, 4);
	   				
	   			       dodajSprite(-8,mrtvKuhar);
	   			    //////////	
	   				 //////////
	   				 this.postotakLodiranjaFaze+=this.dxLodiranja;
	   				 uiMan.nacrtajIzvanThreadno();
	   				 ///////////
	   				 ///////////
		           }
		          
		           if(this.listaUcitanihNaredbi.contains(-9))  {
		        	      ////lik pas
	   					SpriteHendler mrtvPas=new SpriteHendler(2,20,soundPool);
	   					
	   				   Bitmap bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8pas,opts);
	   				 //  bm= namjestiProzirnost( bm);
	   				    
	   				   mrtvPas.dodajNoviSprite(bm, 4, 4, 4);
	   				 
	   				    
	   				    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8pas8borba,opts);
	   				  mrtvPas.dodajNoviSprite(bm,4, 2, 4);
	 			        dodajSprite(-9,mrtvPas);
	 			        
	 			        
	 			       //////////	
	 					 //////////
	 					 this.postotakLodiranjaFaze+=this.dxLodiranja;
	 					 uiMan.nacrtajIzvanThreadno();
	 					 ///////////
	 					 ///////////
	 			        
		           }
		         
		           if(this.listaUcitanihNaredbi.contains(-10))  {
		        	      ////zombie policjac
	   					SpriteHendler spr=new SpriteHendler(2,20,soundPool);
	   					
	   				   Bitmap bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8policajac,opts);
	   				 //  bm= namjestiProzirnost( bm);
	   				    
	   				spr.dodajNoviSprite(bm, 4, 4, 4);
	   				 
	   				    
	   				    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8policajac8borba,opts);
	   				 spr.dodajNoviSprite(bm,4, 2, 4);
	 			        dodajSprite(-10,spr);
	 			        
	 			        
	 			       //////////	
	 					 //////////
	 					 this.postotakLodiranjaFaze+=this.dxLodiranja;
	 					 uiMan.nacrtajIzvanThreadno();
	 					 ///////////
	 					 ///////////
	 			        
		           }
		           if(this.listaUcitanihNaredbi.contains(-11))  {
		        	      ////lik leteci prdonja
	   					SpriteHendler spr=new SpriteHendler(3,20,soundPool);
	   					
	   				   Bitmap bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8leteci0prdonja,opts);
	   				 //  bm= namjestiProzirnost( bm);
	   				    
	   				   spr.dodajNoviSprite(bm, 10, 1, 4);
	   				 
	   				  spr.dodajNoviSprite(null,0, 0,0);
	   				  bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8leteci0prdonja8smrt,opts);
	   				 //  bm= namjestiProzirnost( bm);
	   				    
	   				   spr.dodajNoviSprite(bm, 6, 1, 5);
	   				  
	 			        dodajSprite(-11,spr);
	 			        
	 			        
	 			       //////////	
	 					 //////////
	 					 this.postotakLodiranjaFaze+=this.dxLodiranja;
	 					 uiMan.nacrtajIzvanThreadno();
	 					 ///////////
	 					 ///////////
	 			        
		           }
		           if(this.listaUcitanihNaredbi.contains(-12))  {
		        	    ////zombie alien
	   					SpriteHendler spr=new SpriteHendler(2,20,soundPool);
	   					
	   				   Bitmap bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8alien,opts);
	   				 //  bm= namjestiProzirnost( bm);
	   				    
	   				spr.dodajNoviSprite(bm,10, 4, 4);
	   				 
	   				    
	   				    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8alien8borba,opts);
	   				 spr.dodajNoviSprite(bm,5, 2, 4);
	 			        dodajSprite(-12,spr);
	 			        
	 			        
	 			       //////////	
	 					 //////////
	 					 this.postotakLodiranjaFaze+=this.dxLodiranja;
	 					 uiMan.nacrtajIzvanThreadno();
	 					 ///////////
	 					 ///////////
	 			        
	 			        
		           }
		          
		           if(this.listaUcitanihNaredbi.contains(-13))   {
		           ////lik tower buster
 					SpriteHendler mrtvTowerBuster=new SpriteHendler(4,20,soundPool);
 					
 				  Bitmap   bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8tower8buster,opts);
 			
 				    
 				 mrtvTowerBuster.dodajNoviSprite(bm, 6, 4, 4);
 				 
 				    
 				    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8tower8buster8borba,opts);

 					    
 				 mrtvTowerBuster.dodajNoviSprite(bm,5, 2, 5);
 				 mrtvTowerBuster.dodajNoviSprite(null,0, 0, 0);
				
				Bitmap b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8tower8buster8ispaljivanje,opts);
				 mrtvTowerBuster.dodajNoviSprite(b,3,4, 3);
				  
				
 			     dodajSprite(-13,  mrtvTowerBuster);
 			   //////////	
 				 //////////
 				 this.postotakLodiranjaFaze+=this.dxLodiranja;
 				 uiMan.nacrtajIzvanThreadno();
 				 ///////////
 				 ///////////
 				  /// buster projektil
 				 
 		        SpriteHendler projektilTowerBuster=new SpriteHendler(5,20,soundPool);  
 		        Bitmap ispa=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tower8buster8ispaljivanje,opts);
 		        projektilTowerBuster.dodajNoviSprite(ispa,11, 1,12);//ispaljenje
 		        projektilTowerBuster.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tower8buster,opts),5, 1,6);//let
 		        Bitmap eksp=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tower8buster8eksplozija,opts);
 		        projektilTowerBuster.dodajNoviSprite(eksp,11, 1,12);// pogodak
 		        Bitmap repp=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tower8buster8rep,opts);
 		        projektilTowerBuster.dodajNoviSprite(repp,8, 1,12);// rep
 		        Bitmap mrlja=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tower8buster8mrlja,opts);
 		        projektilTowerBuster.dodajNoviSprite(mrlja,1, 1,0);// mrlja eksplozije
 		        
 		        int[] vre=new int[11];
 		        int[] sli=new int[11];
 		        int[] stu=new int[11];
 		        int[] red=new int[11];
 		        //sl1
 		        vre[0]=100; vre[1]=100; vre[2]=100; vre[3]=100; vre[4]=100; vre[5]=100; vre[6]=150; vre[7]=150; vre[8]=150; vre[9]=150; vre[10]=150;
 		        sli[0]=0; sli[1]=0; sli[2]=0; sli[3]=0; sli[4]=0; sli[5]=0; sli[6]=0; sli[7]=0; sli[8]=0; sli[9]=0; sli[10]=0; 
 		        stu[0]=0;  stu[1]=1;  stu[2]=2;  stu[3]=3;  stu[4]=4;  stu[5]=5; stu[6]=6;  stu[7]=7;  stu[8]=8;  stu[9]=9;  stu[10]=10;
 				red[0]=	0; red[1]=	0; red[2]=	0; red[3]=	0; red[4]=	0; red[5]=	0;  red[6]=	0; red[7]=	0; red[8]=	0; red[9]=	0; red[10]=	0; 
 		        int kodAnim=0;
 		        
 		        projektilTowerBuster.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
 		        dodajSprite(418,projektilTowerBuster);
 		        //////////	
 				 //////////
 				 this.postotakLodiranjaFaze+=this.dxLodiranja;
 				 uiMan.nacrtajIzvanThreadno();
 				 ///////////
		           }
		           
		       if(this.listaUcitanihNaredbi.contains(-14)) {
			        	////lik mrtvac teski
							Bitmap bmm;
							SpriteHendler mrtvTes=new SpriteHendler(2,20,soundPool);
						    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8studentica,opts);
						    mrtvTes.dodajNoviSprite(bmm, 5, 4, 5);					
						    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zommbi8studentica8borba,opts);	   
						    mrtvTes.dodajNoviSprite(bmm, 4, 2, 4);
						    dodajSprite(-14,mrtvTes);
						/////    
						    //////////	
							 //////////
							 this.postotakLodiranjaFaze+=this.dxLodiranja;
							 uiMan.nacrtajIzvanThreadno();
							 ///////////
							 ///////////
			           }
		       if(this.listaUcitanihNaredbi.contains(-15)) {
		        	////lik mrtvac teski
						Bitmap bmm;
						SpriteHendler mrtvTes=new SpriteHendler(2,20,soundPool);
					    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zombi8starleta,opts);
					    mrtvTes.dodajNoviSprite(bmm, 4, 4, 5);					
					    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8zobi8starleta8borba,opts);	   
					    mrtvTes.dodajNoviSprite(bmm, 5, 2, 4);
					    dodajSprite(-15,mrtvTes);
					/////    
					    //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
		           }
		       if(this.listaUcitanihNaredbi.contains(-16))   {
		           ////lik debeli serac
 					SpriteHendler mrtv=new SpriteHendler(4,20,soundPool);
 					
 				  Bitmap   bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8debeli0serac,opts);
 			
 				    
 				 mrtv.dodajNoviSprite(bm, 6, 4, 4);
 				 
 				    
 				    bm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8debeli0serac8borba,opts);

 					    
 				 mrtv.dodajNoviSprite(bm,8, 2, 5);
 				 mrtv.dodajNoviSprite(null,0, 0, 0);
				
				Bitmap b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8debeli0serac8ispaljivanje,opts);
				 mrtv.dodajNoviSprite(b,4,1, 3);
				  
				
 			     dodajSprite(-16,  mrtv);
 			     ///dodaje i svoj projektil
 			   //////////	
 				 //////////
 				 this.postotakLodiranjaFaze+=this.dxLodiranja;
 				 uiMan.nacrtajIzvanThreadno();
 				 ///////////
 				 ///////////
 				  /// buster projektil
 				 
 		        SpriteHendler projektil=new SpriteHendler(4,20,soundPool);  
 		        Bitmap ispa=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8govance8ispaljivanje,opts);
 		        projektil.dodajNoviSprite(ispa,11, 1,12);//ispaljenje
 		        projektil.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8govance8let,opts),3, 1,3);//let
 		        Bitmap eksp=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8govance8eksplozija,opts);
 		        projektil.dodajNoviSprite(eksp,11, 1,12);// pogodak
 		        Bitmap repp=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8govance8rep,opts);
 		        projektil.dodajNoviSprite(repp,3, 1,3);// rep
 		        /*Bitmap mrlja=BitmapFactory.decodeResource(activity.getResources(), R.drawable.projektil8tower8buster8mrlja,opts);
 		        projektilTowerBuster.dodajNoviSprite(mrlja,1, 1,0);// mrlja eksplozije*/
 		        
 		        int[] vre=new int[11];
 		        int[] sli=new int[11];
 		        int[] stu=new int[11];
 		        int[] red=new int[11];
 		        //sl1
 		        vre[0]=100; vre[1]=100; vre[2]=100; vre[3]=100; vre[4]=100; vre[5]=100; vre[6]=150; vre[7]=150; vre[8]=150; vre[9]=150; vre[10]=150;
 		        sli[0]=0; sli[1]=0; sli[2]=0; sli[3]=0; sli[4]=0; sli[5]=0; sli[6]=0; sli[7]=0; sli[8]=0; sli[9]=0; sli[10]=0; 
 		        stu[0]=0;  stu[1]=1;  stu[2]=2;  stu[3]=3;  stu[4]=4;  stu[5]=5; stu[6]=6;  stu[7]=7;  stu[8]=8;  stu[9]=9;  stu[10]=10;
 				red[0]=	0; red[1]=	0; red[2]=	0; red[3]=	0; red[4]=	0; red[5]=	0;  red[6]=	0; red[7]=	0; red[8]=	0; red[9]=	0; red[10]=	0; 
 		        int kodAnim=0;
 		        
 		        projektil.setInternuAnimaciju(kodAnim,sli,red,stu,vre);
 		        dodajSprite(419,projektil);
 		        //////////	
 				 //////////
 				 this.postotakLodiranjaFaze+=this.dxLodiranja;
 				 uiMan.nacrtajIzvanThreadno();
 				 ///////////
		           }
		       if(this.listaUcitanihNaredbi.contains(-17)||this.listaUcitanihNaredbi.contains(-18)) {// jer kapitalista stvara biznismana
		        	////lik biznisman
						Bitmap bmm;
						SpriteHendler mrtvTes=new SpriteHendler(2,20,soundPool);
					    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8biznisman,opts);
					    mrtvTes.dodajNoviSprite(bmm, 4, 4, 5);					
					    bmm=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8biznisman8borba,opts);	   
					    mrtvTes.dodajNoviSprite(bmm, 4, 2, 4);
					    dodajSprite(-17,mrtvTes);
					/////    
					    //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
		           }
		       if(this.listaUcitanihNaredbi.contains(-18)) {//kapitalita
		        	////lik apitalista
		    	   SpriteHendler kapit=new SpriteHendler(5,20,soundPool);
					
				Bitmap	b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8kapitalist,opts);
				      kapit.dodajNoviSprite(b,4, 4, 5);
				      
					b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8kapitalist8borba,opts);
					kapit.dodajNoviSprite(b,4, 2, 3);
					kapit.dodajNoviSprite(null,0, 0, 0);

					b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8kapitalista8bacanje0novaca,opts);
					kapit.dodajNoviSprite(b,3,1, 3);
					b=BitmapFactory.decodeResource(activity.getResources(), R.drawable.napadac8kapitalista8makeitrain,opts);
					kapit.dodajNoviSprite(b,7,1, 4);	
					    dodajSprite(-18,kapit);
					/////    
					    //////////	
						 //////////
						 this.postotakLodiranjaFaze+=this.dxLodiranja;
						 uiMan.nacrtajIzvanThreadno();
						 ///////////
						 ///////////
		           }
		
		          
  }
  private void lodirajSpriteoeSpecificneZaPojedinuFazu(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
	  if(this.brFaze==5){//faza most grad
		  SpriteHendler spr= new SpriteHendler(3);
		  spr.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.upute8abiliti8zaokruzeno,opts), 7,1, 5);
		  spr.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.uputstva8abiliti8krizic,opts), 11,1, 5);
		  spr.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.upustva8abiliti8brojevi,opts), 2,1, 0);
		  this.taskbar.inicializirajAbilitiUpustva(spr);
	  }
  } 
 private void lodirajStandardneSlike(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
	   //////////load izbornik
	   sprUvoda=new SpriteHendler(1);
	  int rand= generator.nextInt(4);
		/*if(rand==0){
			   sprUvoda.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.karakteristike0strijelci,opts), 0,0, 0);
		}
		if(rand==1){
			sprUvoda.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.karakteristike0minobacac,opts), 0,0, 0);
		}
		if(rand==2){
			sprUvoda.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.karakteristike0kasarna,opts), 0,0, 0);
		}
		if(rand==3){
			sprUvoda.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.karakteristike0alkemicar,opts), 0,0, 0);
		}*/
		sprUvoda.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.load,opts), 0,0, 0);
        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
       this.dxLodiranja=100/50;
	     
	   lodirajOpceniteSpriteove(activity,soundPool,opts);
	   lodirajBraniteljeSpriteove( activity, soundPool, opts);
	   lodirajProtivnikeSpriteove( activity, soundPool, opts);
	   
  }
  private void lodirajSamostojeceZvukove(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
/////////boooo
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk8booo, 1), IgricaActivity.zvukBooo, soundPool);
/////////robotski 1
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk8robotski1, 1), IgricaActivity.zvukRobotski1, soundPool);
/////////robotski2
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk8robotski2, 1), IgricaActivity.zvukRobotski2, soundPool);
/////////robotski3
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk8robotski3, 1), IgricaActivity.zvukRobotski3, soundPool);

/////////konstrukcija  tornja6
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk0konstrukcija0tornja6, 1), IgricaActivity.zvukToranjKonstrukcija6, soundPool);

/////////konstrukcija  tornja 5
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk0konstrukcija0tornja5, 1), IgricaActivity.zvukToranjKonstrukcija5, soundPool);

/////////konstrukcija  tornja 4
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk0konstrukcija0tornja4, 1), IgricaActivity.zvukToranjKonstrukcija4, soundPool);

    ////////konstrukcija  tornja 3
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk0konstrukcija0tornja3, 1), IgricaActivity.zvukToranjKonstrukcija3, soundPool);

        ////////konstrukcija  tornja 2
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk0konstrukcija0tornja2, 1), IgricaActivity.zvukToranjKonstrukcija2, soundPool);

	  /////////konstrukcija  tornja 1
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk0konstrukcija0tornja1, 1), IgricaActivity.zvukToranjKonstrukcija1, soundPool);

	  /////////zivot manje
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk8zivot0manje, 1), IgricaActivity.zvukZivotManje, soundPool);

	  /////////pocetak vala
	    ubaciZvuk(soundPool.load(activity, R.raw.zvuk8pocetak0vala, 1), IgricaActivity.zvukPocetakVala, soundPool);
  
	  /////////novi abiliti
	    ubaciZvuk(soundPool.load(activity, R.raw.novi0abiliti, 1), IgricaActivity.zvukTruba1, soundPool);
    
		////meteor let zvuk
		ubaciZvuk(soundPool.load(activity, R.raw.zvuk8meteor8let3sec, 1), IgricaActivity.zvukLetMeteora, soundPool);
		
		
		///////////////taskbar zvuk//////////////
		//zvuk pobjeda
		this.taskbar.dodajZvukpobjede(soundPool.load(activity, R.raw.zvuk8pobjeda, 1));
		//SLIKA I ZVUK ZAVRSNE ANIMACIJE
		  SpriteHendler zavrAnim=new SpriteHendler(3,20,soundPool); 
	        int[] listaZvukovaPljesak= new int[7];
	        listaZvukovaPljesak[0]=soundPool.load(activity, R.raw.zvuk8clap1, 1);
	        listaZvukovaPljesak[1]=soundPool.load(activity, R.raw.zvuk8clap2, 1);
	        listaZvukovaPljesak[2]=soundPool.load(activity, R.raw.zvuk8clap3, 1);
	        listaZvukovaPljesak[3]=soundPool.load(activity, R.raw.zvuk8clap4, 1);
	        listaZvukovaPljesak[4]=soundPool.load(activity, R.raw.zvuk8clap5, 1);
	        listaZvukovaPljesak[5]=soundPool.load(activity, R.raw.zvuk8clap6, 1);
	        listaZvukovaPljesak[6]=soundPool.load(activity, R.raw.zvuk8clap7, 1);

			
	        zavrAnim.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pobjeda,opts),1,1, 0);
	        zavrAnim.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.poraz,opts),1,1, 0);
	        zavrAnim.dodajNoviSprite(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pobjeda8pljeskanje,opts),5,1, 4);
	        zavrAnim.dodajZvukISincSaSlRandom(listaZvukovaPljesak, 2, 0, 3, 0.9f,101,100,50);
	        this.taskbar.dodajSpriteZavrsneAnimacije(zavrAnim);
	        
	        
  }
  private void lodirajZvukoveProjektila(Activity activity, SoundPool soundPool){
	
	
	
	 
	
	
	

	
	  /////////////////////////////standardni zvukovi///////////////////
	  int bang=soundPool.load(activity, R.raw.zvuk8bang, 1);
      int boom=soundPool.load(activity, R.raw.zvuk8boom, 1);
      int pogodakStrijelaObicna=soundPool.load(activity, R.raw.zvuk8strijela0pogodak, 1);
      int freezZraka=soundPool.load(activity, R.raw.zvuk8zrakatoranj, 1);
      ///////////////////////////////////////////////////////////////////
 	 ///405  lava meteor/////////
	  SpriteHendler sprite=this.listaSprite.get(405);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(bang, 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(boom, 2, 0,0, 0.99f,150,100,70);
	  }
	  /////////////////////
	  ///401  projektil granata/////////
	  sprite=this.listaSprite.get(401);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8bang, 1), 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8boom, 1), 2, 0,0, 0.99f,150,100,70);
	  }
	  /////////////////////
	  ///408  projektil tnt/////////
	  sprite=this.listaSprite.get(408);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8bang, 1), 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8boom, 1), 2, 0,0, 0.99f,150,100,70);
	  }
	  /////////////////////
	  ///409  projektil armor/////////
	  sprite=this.listaSprite.get(409);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8bang, 1), 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8boom, 1), 2, 0,0, 0.99f,150,100,70);
	  }
	  /////////////////////
	  ///410 projektil kluster/////////
	  sprite=this.listaSprite.get(410);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8bang, 1), 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8minobacac8cluster, 1), 2, 0,0, 0.99f,150,100,70);
	  }
	  /////////////////////
	  ///411 projektil naplam/////////
	  sprite=this.listaSprite.get(411);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8bang, 1), 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8boom, 1), 2, 0,0, 0.99f,150,100,70);
	  }
	  /////////////////////
	  ///
	
	  ///101 strijelci toranj isti sprite za sve /////////
	  sprite=this.listaSprite.get(402);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(pogodakStrijelaObicna, 2, 0,0, 0.4f,150,100,50);
	  }
	  /////////////////////
	  ///406 projektil strijela vatrena/////////
	  sprite=this.listaSprite.get(101);
	  if(sprite!=null){
			int soundstr =soundPool.load(activity, R.raw.zvuk8strijela0let, 1);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 0,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 1,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 2,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 3,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 4,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 5,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 6,2, 1f,150,100,50);
			 sprite.dodajZvukISincSaSlRandom(soundstr, 1, 7,2, 1f,150,100,50);
		 
	  }
	  /////////////////////
	///407 projektil strijela otrovna/////////
	  sprite=this.listaSprite.get(407);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(pogodakStrijelaObicna, 2, 0,0, 0.4f,150,100,50);
	  }
	  /////////////////////
	  ///412 (175) projektil freez top 1. razina koristi toranj za pustanje 175/////////
	  sprite=this.listaSprite.get(175);
	  if(sprite!=null){

		 
			
		  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8zrakatoranj, 1), 1,2,0, 0.7f,150,100,50);
	  }
	  /////////////////////
	  ///413 (176) projektil freez top 2. razina koristi toranj za pustanje 176/////////
	  sprite=this.listaSprite.get(176);
	  if(sprite!=null){
         sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8zrakatoranj, 1), 1,2,0, 0.7f,150,100,50);
	  }
	  /////////////////////
	  ///403 (177) projektil freeztop 3. razina koristi toranj za pustanje 177/////////
	  sprite=this.listaSprite.get(177);
	  if(sprite!=null){
         sprite.dodajZvukISincSaSlRandom(freezZraka, 1,2,0, 0.7f,150,100,50);
	  }
	  /////////////////////
	  
	  ///403 (179) projektil freeztop Medic razina koristi toranj za pustanje 179/////////
	  sprite=this.listaSprite.get(179);
	  if(sprite!=null){
         sprite.dodajZvukISincSaSlRandom(freezZraka, 1,2,0, 0.7f,150,100,50);
	  }
	  /////////////////////
	  ///403 (178) projektil freeztopTeleport koristi toranj za pustanje 178/////////
	  sprite=this.listaSprite.get(178);
	  if(sprite!=null){
         sprite.dodajZvukISincSaSlRandom(freezZraka, 1,2,0, 0.7f,150,100,50);
	  }
	  ///418 projektil tower buster

      sprite=this.listaSprite.get(418);
	  if(sprite!=null){
		  sprite.dodajZvukISincSaSlRandom(bang, 0, 0,0, 0.8f,150,100,50);
		  sprite.dodajZvukISincSaSlRandom(boom, 2, 0,0, 0.99f,150,100,70);
	  }      
      /////////////////////
	  ///419 projektil govance

      sprite=this.listaSprite.get(419);
	  if(sprite!=null){
			int[] listaZvukova= new int[2];
			listaZvukova[0]=soundPool.load(activity, R.raw.zvuk8projektil8govance8ispaljivanje1, 1);
			listaZvukova[1]=soundPool.load(activity, R.raw.zvuk8projektil8govance8ispaljivanje2, 1);
		
		  sprite.dodajZvukISincSaSlRandom(listaZvukova, 0, 0,0, 1f,150,100,90);
		  sprite.dodajZvukISincSaSlRandom( soundPool.load(activity, R.raw.zvuk8projektil8govance8pogodak, 1), 2, 0,0, 0.99f,120,100,90);
	  }      
      /////////////////////
	  
  }
  private void  lodirajZvukoveBranitelja(Activity activity, SoundPool soundPool){
	  int[] listaZvukovaMacUdar= new int[8];
		listaZvukovaMacUdar[0]=soundPool.load(activity, R.raw.branitelj8standardni8udarac1, 1);
		listaZvukovaMacUdar[1]=soundPool.load(activity, R.raw.branitelj8standardni8udarac2, 1);
		listaZvukovaMacUdar[2]=soundPool.load(activity, R.raw.branitelj8standardni8udarac3, 1);
		listaZvukovaMacUdar[3]=soundPool.load(activity, R.raw.branitelj8standardni8udarac4, 1);
		listaZvukovaMacUdar[4]=soundPool.load(activity, R.raw.branitelj8standardni8udarac5, 1);
		listaZvukovaMacUdar[5]=soundPool.load(activity, R.raw.branitelj8standardni8udarac6, 1);
		listaZvukovaMacUdar[6]=soundPool.load(activity, R.raw.branitelj8standardni8udarac7, 1);
		listaZvukovaMacUdar[7]=soundPool.load(activity, R.raw.branitelj8standardni8udarac8, 1);
		
		
		 ///1 branitelj prva razina/////////
		  SpriteHendler sprite=this.listaSprite.get(1);
		  if(sprite!=null){

			 
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 1, 0, 0.5f,100,100,20);
		  }
		  /////////////////////
		  ///2 branitelj druga razina/////////
		 sprite=this.listaSprite.get(2);
		  if(sprite!=null){

			 
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 1, 0, 0.5f,100,100,20);
		  }
		  /////////////////////
		  ///3 branitelj treca razina/////////
			 sprite=this.listaSprite.get(3);
		 if(sprite!=null){

				 
					
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 0, 0, 0.5f,100,100,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 1, 0, 0.5f,100,100,20);
			  }
			  /////////////////////
		  ///4 branitelj gorstak/////////
		 sprite=this.listaSprite.get(4);
		 if(sprite!=null){

			 
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 1, 0, 0.5f,100,100,20);
		  }
		  /////////////////////
		  ///5 branitelj vitez/////////
		 sprite=this.listaSprite.get(5);
		 if(sprite!=null){

			 
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 1, 0, 0.5f,100,100,20);
		  }
		  /////////////////////	 
		  ///51 branitelj strijelci/////////
		  sprite=this.listaSprite.get(51);
		  if(sprite!=null){

			 
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaMacUdar,1, 1, 0, 0.5f,100,100,20);
		  }
		  
		  /////////////////////¸
		  ///99 dodatci na likove////////
		  sprite=this.listaSprite.get(99);
		  if(sprite!=null){

			  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8telepot1, 1),5, 0, 0, 0.8f,120,100,80);
			  sprite.dodajZvukISincSaSlRandom(soundPool.load(activity, R.raw.zvuk8zombi8smrt, 1),7, 0, 0, 1f,120,0,0);
			  
		  }
		  /////////////////////
		  
			  
  }
  private void lodirajZvukoveZombija(Activity activity, SoundPool soundPool){
	  ///////////////standardni zvukovi
	
		int[] listaZvukovaZenskiHod= new int[6];
		listaZvukovaZenskiHod[0]=soundPool.load(activity, R.raw.zombie8zenski8hod1, 1);
		listaZvukovaZenskiHod[1]=soundPool.load(activity, R.raw.zombie8zenski8hod2, 1);
		listaZvukovaZenskiHod[2]=soundPool.load(activity, R.raw.zombie8zenski8hod3, 1);
		listaZvukovaZenskiHod[3]=soundPool.load(activity, R.raw.zombie8zenski8hod4, 1);
		listaZvukovaZenskiHod[4]=soundPool.load(activity, R.raw.zombie8zenski8hod5, 1);
		listaZvukovaZenskiHod[5]=soundPool.load(activity, R.raw.zombie8zenski8hod6, 1);
	
		
		int[] listaZvukovaStandardniHod= new int[8];
		listaZvukovaStandardniHod[0]=soundPool.load(activity, R.raw.zombie8standardni8hod, 1);
		listaZvukovaStandardniHod[1]=soundPool.load(activity, R.raw.zombie8standardni8hod2, 1);
		listaZvukovaStandardniHod[2]=soundPool.load(activity, R.raw.zombie8standardni8hod3, 1);
		listaZvukovaStandardniHod[3]=soundPool.load(activity, R.raw.zombie8standardni8hod4, 1);
		listaZvukovaStandardniHod[4]=soundPool.load(activity, R.raw.zombie8standardni8hod5, 1);
		listaZvukovaStandardniHod[5]=soundPool.load(activity, R.raw.zombie8standardni8hod6, 1);
		listaZvukovaStandardniHod[6]=soundPool.load(activity, R.raw.zombie8standardni8hod7, 1);
		listaZvukovaStandardniHod[7]=soundPool.load(activity, R.raw.zombie8standardni8hod8, 1);
		int[] listaZvukovaStandardniUjed= new int[5];
		listaZvukovaStandardniUjed[0]=soundPool.load(activity, R.raw.zombie8standardni8ujed, 1);
		listaZvukovaStandardniUjed[1]=soundPool.load(activity, R.raw.zombie8standardni8ujed2, 1);
		listaZvukovaStandardniUjed[2]=soundPool.load(activity, R.raw.zombie8standardni8ujed3, 1);
		listaZvukovaStandardniUjed[3]=soundPool.load(activity, R.raw.zombie8standardni8ujed4, 1);
		listaZvukovaStandardniUjed[4]=soundPool.load(activity, R.raw.zombie8standardni8ujed5, 1);
		
		
		/////////////////////////////////////
		
	   ///-1 radnik zombie/////////
	  SpriteHendler sprite=this.listaSprite.get(-1);
	  if(sprite!=null){

		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
			
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
	  }
	  /////////////////////
	  ///-2 sminkerica zombie/////////
	 sprite=this.listaSprite.get(-2);
	  if(sprite!=null){

		  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 0, 2, 0.7f,100,10,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 1, 2, 0.7f,100,10,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 2, 2, 0.7f,100,10,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 3, 2, 0.7f,100,10,20);
			
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
		  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
	  }
	  /////////////////////
	  ///-3 MMA zombie/////////
		 sprite=this.listaSprite.get(-3);
		  if(sprite!=null){

			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
		  }
		  /////////////////////
		  ///-4 reper zombie/////////
			 sprite=this.listaSprite.get(-4);
			  if(sprite!=null){

				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
					
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
			  }
			  /////////////////////
	      ///-5 pocetni zombie/////////
			 sprite=this.listaSprite.get(-5);
			  if(sprite!=null){

				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
					
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
			  }
			  /////////////////////
			  
			  ///-6 debeli zombie/////////
				 sprite=this.listaSprite.get(-6);
				  if(sprite!=null){

					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
						
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
				  }
		 /////////////////////
				  
		///-7 cistacica zombie/////////
					 sprite=this.listaSprite.get(-7);
		  if(sprite!=null){

						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 0, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 1, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 2, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 3, 2, 0.7f,100,10,20);
							
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
		 sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
		 }
	    /////////////////////		
		  
		 ///-8 kuhar zombie/////////
			 sprite=this.listaSprite.get(-8);
			  if(sprite!=null){

				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
					
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
			  }
	         /////////////////////		  
			  ///-9 pas zombie/////////
			  sprite=null;
				 sprite=this.listaSprite.get(-9);
		    if(sprite!=null){
		    	int[] listaPasHod= new int[4];
		    	       listaPasHod[0]=soundPool.load(activity, R.raw.zombie8pas8urlik1, 1);
		    	       listaPasHod[1]=soundPool.load(activity, R.raw.zombie8pas8urlik2, 1);
		    	       listaPasHod[2]=soundPool.load(activity, R.raw.zombie8pas8urlik3, 1);
		    	       listaPasHod[3]=soundPool.load(activity, R.raw.zombie8pas8urlik4, 1);
					  sprite.dodajZvukISincSaSlRandom(listaPasHod,0, 0, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaPasHod,0, 1, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaPasHod,0, 2, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaPasHod,0, 3, 2, 0.7f,100,10,20);
										  
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
				  }
		    /////////////////////		  	
		    ///-10 polocajac zombie/////////
			 sprite=this.listaSprite.get(-10);
		     if(sprite!=null){

				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
					
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
			  }
	    /////////////////////	
		   ///-11 debeli prdonja zombie/////////
			 sprite=this.listaSprite.get(-11);
		 if(sprite!=null){

				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
		
			  }
	    /////////////////////		
		  ///-12 boss izvanzemaljac zombie/////////
		 sprite=this.listaSprite.get(-12);
	     if(sprite!=null){

			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
		  }
        /////////////////////			
	     ///-13 boss tower buster zombie/////////
		 sprite=this.listaSprite.get(-13);
		  if(sprite!=null){

			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
				
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
			  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
			  
			  
		  }
        /////////////////////
		     ///-14 studentica zombie/////////
		 sprite=this.listaSprite.get(-14);
		 if(sprite!=null){

				  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 0, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 1, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 2, 2, 0.7f,100,10,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 3, 2, 0.7f,100,10,20);
					
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
				  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
				  
				  
			  }
	        /////////////////////
		    ///-15 studentica zombie/////////
				 sprite=this.listaSprite.get(-15);
				 if(sprite!=null){

						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 0, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 1, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 2, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaZenskiHod,0, 3, 2, 0.7f,100,10,20);
							
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
						  
						  
					  }
			        /////////////////////
				 ///-16 boss debeli serac zombie/////////
				 sprite=this.listaSprite.get(-16);
				  if(sprite!=null){

					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
						
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,80);
					  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,80);
					  
					  
				  }
		        /////////////////////
				  ///-17 biznis man zombie/////////
					 sprite=this.listaSprite.get(-17);
					  if(sprite!=null){

						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
							
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
						  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
						  
						  
					  }
			        /////////////////////
					  ///-18 biznis man zombie/////////
						 sprite=this.listaSprite.get(-18);
						  if(sprite!=null){

							  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 0, 2, 0.7f,100,10,20);
							  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 1, 2, 0.7f,100,10,20);
							  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 2, 2, 0.7f,100,10,20);
							  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniHod,0, 3, 2, 0.7f,100,10,20);
								
							  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 0, 0, 0.5f,100,100,20);
							  sprite.dodajZvukISincSaSlRandom(listaZvukovaStandardniUjed,1, 1, 0, 0.5f,100,100,20);
							  
							  
						  }
				        /////////////////////
			 
		 
  }
  private void bilderLodirajZvukoveUSpriteoveINekeStandardneSpriteove(Activity activity, SoundPool soundPool,BitmapFactory.Options opts){
	
	  lodirajZvukoveZombija(activity,soundPool);
        /////////////////////				
	  lodirajZvukoveBranitelja(activity,soundPool);			 
		/////////////////////	
	  lodirajZvukoveProjektila( activity,soundPool);
	    /////////////////////
	  lodirajSamostojeceZvukove(activity,soundPool,opts);
  }
  public boolean daliPostojiSljedeciUOvomValu(){
	  boolean b=false;
	  int iLisTemp=iListe+1;
	  if(iLisTemp<this.velListe&&this.trenutniVal<this.brojValova){
	            if( listaAkcija[this.trenutniVal][iLisTemp]!=0){
	            	b=true;
	            }
	        }
	  else if(this.trenutniVal<this.brojValova){//ako postoji sljedeci val
		  b=true;
	  }
	  return b;
  } 
  public int brNarucenihProtivnika(){
	  return brProtivnika;
  }
  public SoundPool vratiSoundPool(){
	  return this.soundPool;
  }
  public int vratiKodZvuka(String imeZvuka){
	  int kod=-1;
	  if(this.listaImenaZvukova!=null){
		 kod= this.listaZvukova.get(listaImenaZvukova.get(imeZvuka));
	  }
	  return kod;
  }
  public int vratiKodZvuka(int redniBroj){
	  int kod=-1;
	  if(this.listaZvukova!=null){
		 kod= this.listaZvukova.get(redniBroj);
	  }
	  return kod;
  }
  public void pustiZvuk(String imeZvuka,float pojacanje,int prioritet,int sansaZaZvuk, float randomPojacanjeOdGlavnogPosto){
	  int redniBrojZvuka=vratiRedniBrojZvukaZaPustanje(imeZvuka);
	  float razlika=100- randomPojacanjeOdGlavnogPosto;

	  if(redniBrojZvuka>=0) {		
		  
		   if(sansaZaZvuk>0) {//ako uopæe postoji vrijednost za to
			  if(generator.nextInt(101)<sansaZaZvuk){
				  if(razlika>0&&razlika<100) pojacanje=pojacanje-generator.nextInt((int)(razlika));
			
			   
			    FazeIgre.listaSvikPustenihZvukova.add( soundPool.play(listaZvukova.get(redniBrojZvuka),pojacanje*FazeIgre.koeficijentPojacanjaZvuka/100, pojacanje*FazeIgre.koeficijentPojacanjaZvuka/100, prioritet, 0, 1f));
			      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
			    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
			      }
			  }
		     }
			else {
				if(razlika>0&&razlika<100) pojacanje=pojacanje-generator.nextInt((int)(razlika));
				   
				   
				    FazeIgre.listaSvikPustenihZvukova.add( soundPool.play(listaZvukova.get(redniBrojZvuka),pojacanje*FazeIgre.koeficijentPojacanjaZvuka/100, pojacanje*FazeIgre.koeficijentPojacanjaZvuka/100, prioritet, 0, 1f));
				      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
				    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
				      }
			}
	    }
 }
  public void pustiZvuk(int redniBrojZvuka,float pojacanje,int prioritet,int sansaZaZvuk, float randomPojacanjeOdGlavnogPosto){
	   if(redniBrojZvuka>=0) {		
		   float razlika=100- randomPojacanjeOdGlavnogPosto;
		   if(sansaZaZvuk>0) {//ako uopæe postoji vrijednost za to
			  if(generator.nextInt(101)<sansaZaZvuk){
				  if(razlika>0&&razlika<100) pojacanje=pojacanje-generator.nextInt((int)(razlika));
			    
			    
			    FazeIgre.listaSvikPustenihZvukova.add(soundPool.play(this.listaZvukova.get(redniBrojZvuka),pojacanje*FazeIgre.koeficijentPojacanjaZvuka, pojacanje*FazeIgre.koeficijentPojacanjaZvuka, prioritet, 0, 1f));
			      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
			    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
			      }
			  }
		     }
			else {
				if(razlika>0&&razlika<100) pojacanje=pojacanje-generator.nextInt((int)(razlika));
				   
				    FazeIgre.listaSvikPustenihZvukova.add( soundPool.play(listaZvukova.get(redniBrojZvuka),pojacanje*FazeIgre.koeficijentPojacanjaZvuka/100, pojacanje*FazeIgre.koeficijentPojacanjaZvuka/100, prioritet, 0, 1f));
				      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
				    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
				      }
			}
	    }
  }
  public void ubaciZvuk(int kodZvuka, String imeZvuka,SoundPool soundPool){
	  ////
	  
		 
	  ////
	  if(soundPool!=null) this.soundPool=soundPool;
	  if(this.listaZvukova==null){
		  listaZvukova=new LinkedList<Integer>();
	  }
	  if(this.listaImenaZvukova==null){
		  listaImenaZvukova=new HashMap<String,Integer>();
	  }
	  int redniBrojZvuka;
	  redniBrojZvuka=this.listaZvukova.size();
	  listaImenaZvukova.put(imeZvuka,redniBrojZvuka);// ova ce lista povezovati redne brjeve kodova zvuka u listi zvukova sa imenima svakog zvuka
	  this.listaZvukova.add(kodZvuka);
  } 
  public int vratiRedniBrojZvukaZaPustanje(String imeZvuka){
	  int i=-1;
	  if(listaImenaZvukova!=null){
		 Integer inte= this.listaImenaZvukova.get(imeZvuka);
		 if(inte!=null) i=(int)inte;
	  }
	return i;  
  }
  public void postaviSjenu(int alpha,int read,int green,int blue){
	  this.sjenaAlpha=alpha;
	  this.sjenaBlue=blue;
	  this.sjenaGreen=green;
	  this.sjenaRead=read;
  }
  public void dodajParametreFazeIzDB(String IDKoristeneFaze,int brZvjezdica,int tezina){
	  this.brZvjezdica=brZvjezdica;
	  this.tezina=tezina;
		 IDKoristeneFazeIzFI=IDKoristeneFaze;
  } 
  public void setDelayIzmVal(int delay){// namjesta isti delay izmedu svakog vala
	  int i=0;
	  while(this.listaDelayIzmVal.length>i){
		  listaDelayIzmVal[i]=delay;
		  i++;
	  }
  }
  public void setDelayIzmVal(int brVal,int delay){// namjesta posebni delay izmedu valaova
	if(brVal>=this.listaDelayIzmVal.length) brVal=this.listaDelayIzmVal.length-1;// u slucaju da je veci brvala od moguceg stgavlja se da je zdnji
	  listaDelayIzmVal[brVal]=delay;
	
  }
  public SpriteHendler getSprite(int kodSpritea){
	  return this.listaSprite.get(kodSpritea);
  } 
  ////recikliranje spriteova
  public void reciklirajSveSpriteove(){
	  tekPoceo=true;//za load ekran da crta load ispocetka
	  for (SpriteHendler hend : listaSprite.values()) {
		  hend.reciklirajSveBitmapove();
		  }
	  listaSprite.clear();
  }
  /////setersi
  public void setTrenutniVal(int trenutniVal){
	  this.trenutniVal=trenutniVal;
  this.nextMin=0;
  this.nextSec=0;
        iListe=0;
      secDoNovogVala=-1;
      staraSec=-1;// tako da slucajno ne zavrsi na istoj
      skociNaSljedeci=false;// vraca na pocetnu da vise ne ska
  }
  public int getBrFaze(){
	  return this.brFaze;
  }
  public void postaviNovcePrijeDolaskaNovogVala(float novacPoSekundi){
	   this.novacPoSekundi=novacPoSekundi;
  }
  /////getersi
  public int getBrTrenutacnoNajdaljeCeste(){
	  return this.gLog.getTrenutacnaNajdaljaCesta();
  }
  public int getMaxBrIndexaCesta(){
	  return najveciRedniBrojCeste;
  }
  static public int cijenaUpgrada(int brUpgrada){
	  int i=-1;
	  postaviListuCijenaUpgradesa();
	  if(listaCijenaUpgradesa.containsKey(brUpgrada)){
		  i=listaCijenaUpgradesa.get(brUpgrada);
	  }
	  return i;
  }
  public int izracujanBrZvjezdicaNaKraju(int temBrZivota){
	  int brZvjezdica=0;
	 if(temBrZivota<3) brZvjezdica=0;
	 else  if( this.brZivota/2>=temBrZivota) brZvjezdica=1;
	 else if(3*this.brZivota/4>=temBrZivota) brZvjezdica=2;
	 else if(4*this.brZivota/5>=temBrZivota) brZvjezdica=3;
	 else brZvjezdica=3;
	 return brZvjezdica;
  }
  public float cijenaObjecta(int obj){
	  float d;
	  if(!listaCijena.containsKey(obj)) d=-1; 
	  else d=listaCijena.get(obj);
	  return  d;
  }
///////////public metode za taskbar itd.
  public float getNovacPoSekundiPrijeDolaskaNovogVala(){
	  return this.novacPoSekundi;
  }
  public void setSkociNaSljedeci(){
	  this.skociNaSljedeci=true;
	  
  }
  public void dodajSprite(int kodObjekta, SpriteHendler sprite){
	  listaSprite.put(kodObjekta,sprite);
	  
  }
  public int secDoNovogVala(){
	  return this.secDoNovogVala;// -1 onaèava da val nije gotov još
  }
  public float xVala(){
	  if(xVala==-10000){
		  try{
			  xVala=listaPozX[0][0]+listaSir[0][0]/2;
              
		  }
		  catch(Exception e){
			  
		  }
		  
	  }
	 return  xVala;
  }
  public float yVala(){
	  if(yVala==-10000){
		  try{
			  yVala=listaPozY[0][0]+listaVis[0][0]/2;
              
		  }
		  catch(Exception e){
			  
		  }
		  
	  }
	  return yVala;
  }
  public int getBrZivota(){
	  return brZivota;
  }
  public int getUkupniNovacNaPocetku(){// koristit æe ih tasbar i game logic za dobivanje vrijednosti 
 	 return ukupniNovac;
  }
  public int getBrojValova(){
 	 return brojValova;
  }
  public int getTrenutniVal(){
 	 return trenutniValZaPrik;
  }
  public Taskbar getTaskbar(){// ovu metodu æe koristit GameLogic
	  return taskbar;
  }
  public void inkriminiajBrojProtivnika(){
	  brProtivnika++;
  }
  //////////////////
  public void dodajTaskbar(Taskbar task){
	  taskbar=task;
  }
  public void dodajPocetneParametreIgre(int novac,int brzivota){// ovo se namješta na poèetku 
	  ukupniNovac=novac;
	  brZivota=brzivota;
  }
  public void dodajIzbornikZaTornjeve(IzbornikZaToranj izbor){// dodaje posebno izbornik za tornjeve, jer æe biti samo jedan a neželikm ga stavljat u posebnu listu jer æe se poveæavat zajedno sa brojem svih èlanova igrice
	  izborZaToranj=izbor;
  }
//  public void dodajBr400Projektil(int sec)
  public void dodaj501DodatakNaMapu(int brVala,int sec,int brSlikeUListiSpriteova,int pocmiOdStupca,int kodSortiranja,float xSlike, float ySlike,float sirSlike, float visSlike,float xLog, float yLog,float sirLog, float visLog){
	  listaPozX[brVala][iDodavanja[brVala]]=(xSlike*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(ySlike*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sirSlike*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(visSlike*yPiksCm);
	  listaDx[brVala][iDodavanja[brVala]]=xLog*xPiksCm;
	  listaDy[brVala][iDodavanja[brVala]]=yLog*yPiksCm;
	  this.listaDodat[brVala][iDodavanja[brVala]]=sirLog*xPiksCm;
	  this.listaDodat2[brVala][iDodavanja[brVala]]=visLog*yPiksCm;
	  this.listaDodat3[brVala][iDodavanja[brVala]]=brSlikeUListiSpriteova;
	  this.listaDodat4[brVala][iDodavanja[brVala]]=kodSortiranja;
	  this.listaDodat5[brVala][iDodavanja[brVala]]=pocmiOdStupca;
	  listaAkcija[brVala][iDodavanja[brVala]]=501;
	  iDodavanja[brVala]++;
  }
  public void dodaj505DodatakNaMapuIzvrsiJedanputIZavrsiNa(int brVala,int sec,int brSlikeUListiSpriteova,int pocmiOdStupca,int zavrsiNaStupcu,int kodSortiranja,float xSlike, float ySlike,float sirSlike, float visSlike,float xLog, float yLog,float sirLog, float visLog,float yPomSortiranja){
	  listaPozX[brVala][iDodavanja[brVala]]=(xSlike*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(ySlike*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sirSlike*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(visSlike*yPiksCm);
	  listaDx[brVala][iDodavanja[brVala]]=xLog*xPiksCm;
	  listaDy[brVala][iDodavanja[brVala]]=yLog*yPiksCm;
	  this.listaDodat[brVala][iDodavanja[brVala]]=sirLog*xPiksCm;
	  this.listaDodat2[brVala][iDodavanja[brVala]]=visLog*yPiksCm;
	  this.listaDodat3[brVala][iDodavanja[brVala]]=brSlikeUListiSpriteova;
	  this.listaDodat4[brVala][iDodavanja[brVala]]=kodSortiranja;
	  this.listaDodat5[brVala][iDodavanja[brVala]]=pocmiOdStupca;
	  this.listaDodat6[brVala][iDodavanja[brVala]]=zavrsiNaStupcu;
	  this.listaDodat7[brVala][iDodavanja[brVala]]=yPomSortiranja*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=505;
	  iDodavanja[brVala]++;
  }
  public void dodaj502DodatakNaMapuRandomPustanje(int brVala,int sec,int brSlikeUListiSpriteova,int pocmiOdStupca,int kodSortiranja,float xSlike, float ySlike,float sirSlike, float visSlike,float xLog, float yLog,float sirLog, float visLog, float sansaZaPustanje){
	  listaPozX[brVala][iDodavanja[brVala]]=(xSlike*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(ySlike*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sirSlike*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(visSlike*yPiksCm);
	  listaDx[brVala][iDodavanja[brVala]]=xLog*xPiksCm;
	  listaDy[brVala][iDodavanja[brVala]]=yLog*yPiksCm;
	  this.listaDodat[brVala][iDodavanja[brVala]]=sirLog*xPiksCm;
	  this.listaDodat2[brVala][iDodavanja[brVala]]=visLog*yPiksCm;
	  this.listaDodat3[brVala][iDodavanja[brVala]]=brSlikeUListiSpriteova;
	  this.listaDodat4[brVala][iDodavanja[brVala]]=kodSortiranja;
	  this.listaDodat5[brVala][iDodavanja[brVala]]=pocmiOdStupca;
	  this.listaDodat6[brVala][iDodavanja[brVala]]=sansaZaPustanje;
	  listaAkcija[brVala][iDodavanja[brVala]]=502;
	  iDodavanja[brVala]++;
  }
  public void dodaj503DodatakNaMapuPomakSortiranja(int brVala,int sec,int brSlikeUListiSpriteova,int pocmiOdStupca,int kodSortiranja,float xSlike, float ySlike,float sirSlike, float visSlike,float xLog, float yLog,float sirLog, float visLog,float yPomSortiranja){
	  listaPozX[brVala][iDodavanja[brVala]]=(xSlike*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(ySlike*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sirSlike*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(visSlike*yPiksCm);
	  listaDx[brVala][iDodavanja[brVala]]=xLog*xPiksCm;
	  listaDy[brVala][iDodavanja[brVala]]=yLog*yPiksCm;
	  this.listaDodat[brVala][iDodavanja[brVala]]=sirLog*xPiksCm;
	  this.listaDodat2[brVala][iDodavanja[brVala]]=visLog*yPiksCm;
	  this.listaDodat3[brVala][iDodavanja[brVala]]=brSlikeUListiSpriteova;
	  this.listaDodat4[brVala][iDodavanja[brVala]]=kodSortiranja;
	  this.listaDodat5[brVala][iDodavanja[brVala]]=pocmiOdStupca;
	  this.listaDodat6[brVala][iDodavanja[brVala]]=yPomSortiranja*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=503;
	  iDodavanja[brVala]++;
  }
  public void dodaj504DodatakNaMapuRandomPustanjePomakSortiranja(int brVala,int sec,int brSlikeUListiSpriteova,int pocmiOdStupca,int kodSortiranja,float xSlike, float ySlike,float sirSlike, float visSlike,float xLog, float yLog,float sirLog, float visLog, float sansaZaPustanje,float pomakSortiranja){
	  listaPozX[brVala][iDodavanja[brVala]]=(xSlike*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(ySlike*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sirSlike*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(visSlike*yPiksCm);
	  listaDx[brVala][iDodavanja[brVala]]=xLog*xPiksCm;
	  listaDy[brVala][iDodavanja[brVala]]=yLog*yPiksCm;
	  this.listaDodat[brVala][iDodavanja[brVala]]=sirLog*xPiksCm;
	  this.listaDodat2[brVala][iDodavanja[brVala]]=visLog*yPiksCm;
	  this.listaDodat3[brVala][iDodavanja[brVala]]=brSlikeUListiSpriteova;
	  this.listaDodat4[brVala][iDodavanja[brVala]]=kodSortiranja;
	  this.listaDodat5[brVala][iDodavanja[brVala]]=pocmiOdStupca;
	  this.listaDodat6[brVala][iDodavanja[brVala]]=sansaZaPustanje;
	  this.listaDodat7[brVala][iDodavanja[brVala]]=pomakSortiranja*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=504;
	  iDodavanja[brVala]++;
  }
  public void dodajBr200ToranjEmbrio(int brVala,int sec,float xCentra, float yCentra){
	  listaPozX[brVala][iDodavanja[brVala]]=(xCentra*xPiksCm) ;
	  listaPozY[brVala][iDodavanja[brVala]]=(yCentra*yPiksCm) ;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=0;
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=200;
	  listaDx[brVala][iDodavanja[brVala]]=0;
	  listaDy[brVala][iDodavanja[brVala]]=0;
	  iDodavanja[brVala]++;
	  listaUcitanihNaredbi.add(200);
  }
  public void dodajBr101ToranjStrijelci(int brVala,int sec,float x, float y){
	  listaPozX[brVala][iDodavanja[brVala]]=(x*xPiksCm) ;
	  listaPozY[brVala][iDodavanja[brVala]]=(y*yPiksCm) ;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=0;
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=101;
	  listaDx[brVala][iDodavanja[brVala]]=0;// 
	  listaDy[brVala][iDodavanja[brVala]]=0;
	  iDodavanja[brVala]++;
	  listaUcitanihNaredbi.add(101);
  }
  public void dodajBr125ToranjMinobacac(int brVala,int sec,float x, float y){
	  listaPozX[brVala][iDodavanja[brVala]]=(x*xPiksCm) ;
	  listaPozY[brVala][iDodavanja[brVala]]=(y*yPiksCm) ;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=0;
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=125;
	  listaDx[brVala][iDodavanja[brVala]]=0;// koristim ovu listu za spremanje varijeble u kojem smijeru toranj misli da je cilj
	  listaDy[brVala][iDodavanja[brVala]]=0;
	  iDodavanja[brVala]++;
	  listaUcitanihNaredbi.add(125);
  }
  public void dodajBr202Okuka(int brVala,int sec,float x, float y,float sir, float vis,float dx,float dy,int redBr){
	  float dxx=dx;
	  float dyy=dy;
	  if(najveciRedniBrojCeste<redBr) najveciRedniBrojCeste=redBr;
	  listaPozX[brVala][iDodavanja[brVala]]=(x*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(y*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sir*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(vis*yPiksCm);
	  listaAkcija[brVala][iDodavanja[brVala]]=202;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  iDodavanja[brVala]++;
  }
  public void dodajBr205OkukaKut(int brVala,int sec,float x, float y,float sir, float vis,float jedinicnaBrzina,double kutStupnjevi,int redBr){
	  float dxx= jedinicnaBrzina*(float)Math.cos((2*Math.PI/360)*kutStupnjevi);
	  float dyy=-jedinicnaBrzina*(float)Math.sin ((2*Math.PI/360)*kutStupnjevi);
	  if(najveciRedniBrojCeste<redBr) najveciRedniBrojCeste=redBr;
	  listaPozX[brVala][iDodavanja[brVala]]=(x*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(y*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sir*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(vis*yPiksCm);
	  listaAkcija[brVala][iDodavanja[brVala]]=205;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  iDodavanja[brVala]++;
  }
  public void dodajBr204Kraj(int brVala,int sec,float x, float y,float sir, float vis,float dx,float dy, int redBr){
	  float dxx=dx;
	  float dyy=dy;
	  listaPozX[brVala][iDodavanja[brVala]]=(x*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(y*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sir*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(vis*yPiksCm);
	  listaAkcija[brVala][iDodavanja[brVala]]=204;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  iDodavanja[brVala]++;
  }
  public void dodajBr203RavniPutKut(int brVala,int sec,float x, float y,float sir, float vis,float jedinicnaBrzina,double kutStupnjevi, int redBr){
	  float dxx= jedinicnaBrzina*(float)Math.cos((2*Math.PI/360)*kutStupnjevi);
	  float dyy=-jedinicnaBrzina*(float)Math.sin ((2*Math.PI/360)*kutStupnjevi);
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  if(najveciRedniBrojCeste<redBr) najveciRedniBrojCeste=redBr;
	  listaSir[brVala][iDodavanja[brVala]]=sir*xPiksCm;
	  listaVis[brVala][iDodavanja[brVala]]=vis*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=203;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  iDodavanja[brVala]++;
  }
  public void dodajBr203RavniPutKutInertni(int brVala,int sec,float x, float y,float sir, float vis,float jedinicnaBrzina,double kutStupnjevi, int redBr){
	  float dxx= jedinicnaBrzina*(float)Math.cos((2*Math.PI/360)*kutStupnjevi);
	  float dyy=-jedinicnaBrzina*(float)Math.sin ((2*Math.PI/360)*kutStupnjevi);
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  if(najveciRedniBrojCeste<redBr) najveciRedniBrojCeste=redBr;
	  listaSir[brVala][iDodavanja[brVala]]=sir*xPiksCm;
	  listaVis[brVala][iDodavanja[brVala]]=vis*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=203;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  listaDodat2[brVala][iDodavanja[brVala]]=1;
	  iDodavanja[brVala]++;
  }
  public void dodajBr205OkukaKutInertni(int brVala,int sec,float x, float y,float sir, float vis,float jedinicnaBrzina,double kutStupnjevi,int redBr){
	  float dxx= jedinicnaBrzina*(float)Math.cos((2*Math.PI/360)*kutStupnjevi);
	  float dyy=-jedinicnaBrzina*(float)Math.sin ((2*Math.PI/360)*kutStupnjevi);
	  if(najveciRedniBrojCeste<redBr) najveciRedniBrojCeste=redBr;
	  listaPozX[brVala][iDodavanja[brVala]]=(x*xPiksCm);
	  listaPozY[brVala][iDodavanja[brVala]]=(y*yPiksCm);
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=(sir*xPiksCm);
	  listaVis[brVala][iDodavanja[brVala]]=(vis*yPiksCm);
	  listaAkcija[brVala][iDodavanja[brVala]]=205;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  iDodavanja[brVala]++;
	  listaDodat2[brVala][iDodavanja[brVala]]=1;
  }
  public void dodajBr201RavniPut(int brVala,int sec,float x, float y,float sir, float vis,float dx,float dy, int redBr){
	  float dxx=dx;
	  float dyy=dy;
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=sir*xPiksCm;
	  listaVis[brVala][iDodavanja[brVala]]=vis*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=201;
	  if(najveciRedniBrojCeste<redBr) najveciRedniBrojCeste=redBr;
	  listaDx[brVala][iDodavanja[brVala]]=dxx;
	  listaDy[brVala][iDodavanja[brVala]]=dyy;
	  listaDodat[brVala][iDodavanja[brVala]]=redBr;
	  iDodavanja[brVala]++;
	  
  }
  public void dodajBr1ObjectIgre(int brVala,int sec,float x, float y,float sir, float vis){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=sir*xPiksCm;
	  listaVis[brVala][iDodavanja[brVala]]=vis*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=1;
	  iDodavanja[brVala]++;
	  listaUcitanihNaredbi.add(1);
  }
  public void dodajBr50Junak(int brVala,int sec,float x, float y,float sir, float vis){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=sir*xPiksCm;
	  listaVis[brVala][iDodavanja[brVala]]=vis*yPiksCm;
	  listaAkcija[brVala][iDodavanja[brVala]]=50;
	  iDodavanja[brVala]++;
	  listaUcitanihNaredbi.add(50);
  }
  public void dodajBr_1Radnik(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-1;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-1);
  }
  public void dodajBr_2Sminkerica(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-2;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-2);
  }
  public void dodajBr_3MMA(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-3;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-3);
  }
  public void dodajBr_4Reper(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-4;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-4);
  }
  public void dodajBr_5ProtivnikPocetni(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-5;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-5);
  }
  public void dodajBr_6ProtivnikDebeli(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-6;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-6);
  }
  public void dodajBr_7ProtivnikCistacica(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-7;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-7);
  }
  public void dodajBr_8ProtivnikKuhar(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-8;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-8);
  }
  public void dodajBr_9ProtivnikPas(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-9;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-9);
  }
  public void dodajBr_10ProtivnikPolicajac(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-10;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-10);
	  
  }
  public void dodajBr_11ProtivnikDebeliPrdonja(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-11;
	  iDodavanja[brVala]++;
	  listaUcitanihNaredbi.add(-11);
	  brProtivnika++;
	  
  }
  public void dodajBr_12BossIzvanzemaljacProgramiranZaIzlazIzTanjura(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-1001;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-12);
  }
  public void dodajBr_12BossIzvanzemaljac(int brVala,int sec,float x, float y,int brZivota,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-12;
	  listaDodat[brVala][iDodavanja[brVala]]=brZivota;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-12);
  }
  public void dodajBr_13BossTowerBuster(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-13;
	  iDodavanja[brVala]++;
	  brProtivnika++;
	  listaUcitanihNaredbi.add(-13);
  }
  public void dodajBr_14ProtivnikStudentica(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-14;
	  listaUcitanihNaredbi.add(-14);
	  iDodavanja[brVala]++;
	  brProtivnika++;
	
	  
  }
  public void dodajBr_15ProtivnikStarleta(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-15;
	  listaUcitanihNaredbi.add(-15);
	  iDodavanja[brVala]++;
	  brProtivnika++;	  
  }
  public void dodajBr_16DebeliSerac(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-16;
	  listaUcitanihNaredbi.add(-16);
	  iDodavanja[brVala]++;
	  brProtivnika++;	  
  }
  public void dodajBr_17BiznisMan(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-17;
	  listaUcitanihNaredbi.add(-17);
	  iDodavanja[brVala]++;
	  brProtivnika++;	  
  }
  public void dodajBr_18Kapitalista(int brVala,int sec,float x, float y,float polNaCesti){// dodaje objekt u listu dogadaja, on æe se stvorit u naznaèeno vrijeme od predhodnog
	  listaPozX[brVala][iDodavanja[brVala]]=x*xPiksCm;
	  listaPozY[brVala][iDodavanja[brVala]]=y*yPiksCm;
	  listaDelay[brVala][iDodavanja[brVala]]=sec;
	  listaSir[brVala][iDodavanja[brVala]]=polNaCesti;// koristi ovu varijabu za odredivanje polozaja na cesti
	  listaVis[brVala][iDodavanja[brVala]]=0;
	  listaAkcija[brVala][iDodavanja[brVala]]=-18;
	  listaUcitanihNaredbi.add(-18);
	  iDodavanja[brVala]++;
	  brProtivnika++;	  
  }
  ///////PRIVATNE METODE////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////// TOK IGRE
public void tokIgre(int sec,int min, UIManager uiMan, GameLogic gLog){//ova metoda bi se trebala pozaivati iz game logiga i  u njoj bi se dogaðala sva magija

    
	if(skociNaSljedeci){
                 this.nextSec=sec;
                 this.nextMin=min;
                 secDoNovogVala=-1;
                 staraSec=-1;// tako da slucajno ne zavrsi na istoj
                 skociNaSljedeci=false;// vraca na pocetnu da vise ne skace
                   }
      if(nextSec==0&&nextMin==min) updateVrijeme(listaDelay[this.trenutniVal][iListe]);
     // if(nextSec<=sec&&nextMin<=min&&nextSec!=staraSec){
      boolean izvrsavaSe=false;
      while(nextSec+nextMin*60<=sec+60*min&&nextSec!=staraSec){
             this.uiMan=uiMan;
             this.gLog=gLog;
             izvrsavaSe=true;
             trenutniValZaPrik=trenutniVal+1;
             staraSec=nextSec;// sprema se za sljedeæi poziv funkciji, jer se poziva u misliekundama
             stvoriteljObjekata(listaAkcija[this.trenutniVal][iListe],listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe]);
             iListe++;
             while(listaDelay[this.trenutniVal][iListe]==0&&listaAkcija[this.trenutniVal][iListe]!=0){// stvara objekte istovremeno
	                           stvoriteljObjekata(listaAkcija[this.trenutniVal][iListe],listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe]);
	                           iListe++;
                }
             if(listaDelay[this.trenutniVal][iListe]!=0) updateVrijeme(listaDelay[this.trenutniVal][iListe]);//
             if(listaAkcija[this.trenutniVal][iListe]==0){// ako je jedan val zavrsio
	                           this.trenutniVal++;
	                           iListe=0;
	                           if(listaDelayIzmVal.length>trenutniVal){
		                                xVala=listaPozX[this.trenutniVal][iListe];
		                                yVala=listaPozY[this.trenutniVal][iListe];
		                                updateVrijeme(this.listaDelayIzmVal[this.trenutniVal]);
		                                secDoNovogVala=this.listaDelayIzmVal[this.trenutniVal];
		  
	                                                 }
              }
             else secDoNovogVala=-1;// oznaèava da val još traje
      }
       if(!izvrsavaSe&&secDoNovogVala>0) secDoNovogVala=60*this.nextMin+this.nextSec-sec-min*60;
   if(this.brProtivnika==0)  this.gLog.pocmiProvjeravatiZaKrajIgre();

}
  ////// ovdje se ruèno moraju zapisati svi moguæi objekti, no resursi za objekte æe biti u memoriji samo u sluèaju da njihova funkcija dodavanja bude pokrenuta
  public void stvoriteljObjekata(int kod,float x, float y, float sir, float vis,float dx, float dy){
	  switch(kod){
	  //// 1-100  vojnici od igraèa, jedanko kao i za indikator na objektima
	  ////101-200 graðevine od igraèa
	  //// 201-300 projektili od igraèa
	  ////-1-(-100) vojnici od protivnika
	  //// 201 ravni put
	  //// 201- okuka
	  case 1: br1ObjectIgre(x,y);
	         break;
	  case -1: br_1Radnik(x,y,sir);
             break;
	  case -2: br_2Protivnik(x,y,sir);
             break;
	  case -3: br_3ProtivnikMMA(x,y,sir);
             break;
	  case -4: br_4Protivnik(x,y,sir);
             break;
	  case -5: br_5ProtivnikPocetni(x,y,sir);
             break;
	  case -6: br_6ProtivnikDebeli(x,y,sir);
             break; 
	  case -7: br_7ProtivnikCistacica(x,y,sir);
             break;
	  case -8: br_8ProtivnikKuhar(x,y,sir);
             break; 
	  case -9: br_9ProtivnikPas(x,y,sir);
             break; 
	  case -10: br_10ProtivnikPolicajac(x,y,sir);
             break; 
	  case -11: br_11ProtivnikDebeliPrdonja(x,y,sir);
             break; 
	  case -12: br_12BossIzvanzemaljac(x,y,(int)vis,sir,(int)dx);
             break;  
	  case -13:  br_13BossTowerBuster(x,y,sir);
             break;   
	  case -14:  br_14ProtivnikStudentica(x,y,sir);
             break;  
	  case -15:  br_15ProtivnikStarleta(x,y,sir);
             break;  
	  case -16:   br_16BossDebeliSerac(x,y,sir);
             break;     
	  case -17:   this.br_17Biznisman(x,y,sir);
             break;  
	  case -18:   this.br_18Kapitalista(x,y,sir);
             break;               
	  case -1001:   this.br_12BossIzvanzemaljacIzlazakIzTanjura(x,y,(int)vis,sir);
             break;        
	  case 201: br201RavniPut(x,y,sir,vis,dx,dy);
	         break; 
	  case 202: br202Okuka(x,y,sir,vis,dx,dy);
	         break;
	  case 203: br203RavniPutKut(x,y,sir,vis,dx,dy);
             break; 
      case 205: br205OkukaKut(x,y,sir,vis,dx,dy);
             break;       
	  case 101: br101ToranjStrijelci(x,y);
             break;
	  case 125: br125ToranjMinobacac(x,y);
             break;       
	  case 200: br200ToranjEmbrio(x,y);
	         break;
	  case 204:  br204Kraj(x,y,sir,vis,dx,dy);
             break;
	  case 501:  br501DodatakNaMapu();
             break;
	  case 502:  br502DodatakNaMapuRandom();
             break;  
	  case 503:    br503DodatakNaMapuPomakS0rtiranja();
	         break;  
	  case 504:  br504DodatakNaMapuRandomPomakSortiranja();
            break; 
	  case 505:  br505DodatakNaMapuIzvrsiJedanputIStaniNa();
            break; 
            
            
	  }
	  
  }
  private void updateVrijeme(int s){
	  if(s>60){
		  s-=60;
		  nextMin++;
		  updateVrijeme(s);// rekurzivno se poziva u sluèaju da je broj sekundi veæi od 60
	  }  
	  else {nextSec+=s;
	    if(nextSec>60){
		    nextSec-=60;
		    nextMin++;
	    }  
	  }
  }
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //// stvaranje pojedinih objekata
  public void br175ToranjAlkemicar1Razina(float x, float y){
	  int i=175;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, sirTorCm*xPiksCm/9);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
  }
  public void br176ToranjAlkemicar2Razina(float x, float y,float novacZaProdajuPredhodnog){
	  int i=176;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, sirTorCm*xPiksCm/9);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
  }
  public void br177ToranjAlkemicar3Razina(float x, float y,float novacZaProdajuPredhodnog){
	  int i=177;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);

	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
  }
  public void br178ToranjAlkemicarTeleport(float x, float y,float novacZaProdajuPredhodnog){
	  int i=178;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=68.66f*objPrik.getGlavniRectPrikaza().height()/100;
   	  float sir=0.777f*vis;
   	  
   	  float yDod=3.29f*objPrik.getGlavniRectPrikaza().height()/100;
   	  float xDod=65.22f*objPrik.getGlavniRectPrikaza().width()/100;
	  oIgre.stvoriTeleport(1,4);
	  objPrik.stvoriTelepoter(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
  }
  public void br180ToranjAlkemicarTeleport1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=180;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(178).getVisRezanja(0)/listaSprite.get(178).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(178),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=68.66f*objPrik.getGlavniRectPrikaza().height()/100;
  	  float sir=0.777f*vis;
   	  
   	  float yDod=3.29f*objPrik.getGlavniRectPrikaza().height()/100;
   	  float xDod=81.22f*objPrik.getGlavniRectPrikaza().width()/100;
	  oIgre.stvoriTeleport(1,3);
	  objPrik.stvoriTelepoter(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
		float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
public void br181ToranjAlkemicarTeleport2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=181;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(178).getVisRezanja(0)/listaSprite.get(178).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(178),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=68.66f*objPrik.getGlavniRectPrikaza().height()/100;
  	  float sir=0.777f*vis;;
   	  
   	  float yDod=3.29f*objPrik.getGlavniRectPrikaza().height()/100;
   	  float xDod=81.22f*objPrik.getGlavniRectPrikaza().width()/100;
	  oIgre.stvoriTeleport(2,4);
	  objPrik.stvoriTelepoter(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
		float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br182ToranjAlkemicarTeleport3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=182;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(178).getVisRezanja(0)/listaSprite.get(178).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(178),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=68.66f*objPrik.getGlavniRectPrikaza().height()/100;
  	  float sir=0.777f*vis;
   	  
   	  float yDod=3.29f*objPrik.getGlavniRectPrikaza().height()/100;
   	  float xDod=81.22f*objPrik.getGlavniRectPrikaza().width()/100;
	  oIgre.stvoriTeleport(3,4);
	  objPrik.stvoriTelepoter(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
		float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br179ToranjAlkemicarMedic(float x, float y,float novacZaProdajuPredhodnog){
	  int i=179;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=60.1f*objPrik.getGlavniRectPrikaza().height()/100;
	  float sir=150f*objPrik.getGlavniRectPrikaza().width()/100f;
	  
	  float yDod=7.16f*objPrik.getGlavniRectPrikaza().height()/100;
	  float xDod=-5f*objPrik.getGlavniRectPrikaza().width()/100;
	  
	 
	  oIgre.stvoriMedic(7,0);
	  objPrik.stvoriMedic(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
  }
  public void br183ToranjAlkemicarMedic1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=183;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(179).getVisRezanja(0)/listaSprite.get(179).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(179),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=78.1f*objPrik.getGlavniRectPrikaza().height()/100;
	  float sir=150f*objPrik.getGlavniRectPrikaza().width()/100f;
	  
	  float yDod=7.16f*objPrik.getGlavniRectPrikaza().height()/100;
	  float xDod=-5f*objPrik.getGlavniRectPrikaza().width()/100;
	  
	 
	  oIgre.stvoriMedic(8,1);
	  objPrik.stvoriMedic(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
		float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br184ToranjAlkemicarMedic2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=184;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(179).getVisRezanja(0)/listaSprite.get(179).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(179),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=78.1f*objPrik.getGlavniRectPrikaza().height()/100;
	  float sir=150f*objPrik.getGlavniRectPrikaza().width()/100f;
	  
	  float yDod=7.16f*objPrik.getGlavniRectPrikaza().height()/100;
	  float xDod=-5f*objPrik.getGlavniRectPrikaza().width()/100;
	  
	 
	  oIgre.stvoriMedic(9,2);
	  objPrik.stvoriMedic(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
		float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br185ToranjAlkemicarMedic3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=185;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float sirTorCm=1.05f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(179).getVisRezanja(0)/listaSprite.get(179).getSirRezanja(0));
	  ToranjLAlkemicar oIgre=new ToranjLAlkemicar(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPAlkemicar objPrik=new ToranjPAlkemicar(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(179),i);
	  objPrik.stvoriTop(1, 4*sirTorCm*xPiksCm/6, 4*sirTorCm*yPiksCm/6,-3*sirTorCm*yPiksCm/6, 3*sirTorCm*xPiksCm/18);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  float vis=78.1f*objPrik.getGlavniRectPrikaza().height()/100;
	  float sir=150f*objPrik.getGlavniRectPrikaza().width()/100f;
	  
	  float yDod=7.16f*objPrik.getGlavniRectPrikaza().height()/100;
	  float xDod=-5f*objPrik.getGlavniRectPrikaza().width()/100;
	  
	 
	  oIgre.stvoriMedic(10,0);
	  objPrik.stvoriMedic(xDod, yDod, sir, vis);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setPocIspaljivanja(objPrik.getRectTopa().centerX(),objPrik.getRectTopa().centerY()-objPrik.getRectTopa().height()/4);
		float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br150ToranjKasarna(float x, float y){
	  int i=150;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public ToranjLKasarna br151ToranjKasarna(float x, float y,float novacZaProdajuPredhodnog){
	  int i=151;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(150),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
  }
  public ToranjLKasarna br152ToranjKasarna(float x, float y,float novacZaProdajuPredhodnog){
	  int i=152;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  //oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
  }
  public ToranjLKasarna br153ToranjKasarnaGorstak(float x, float y,float novacZaProdajuPredhodnog){
	  int i=153;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
  }
  public ToranjLKasarna br159ToranjKasarnaGorstak1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=159;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		 float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br160ToranjKasarnaGorstak2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=160;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		 float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br161ToranjKasarnaGorstak3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=161;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		 float sirRanka=sirRankCm*xPiksCm;
		  objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  
  public ToranjLKasarna br154ToranjKasarnaVitez(float x, float y,float novacZaProdajuPredhodnog){
	  int i=154;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
  }
  public ToranjLKasarna br156ToranjKasarnaVitez1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=156;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(150),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		float sirRanka=sirRankCm*xPiksCm;
		objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br157ToranjKasarnaVitez2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=157;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(150),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		float sirRanka=sirRankCm*xPiksCm;
		objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br158ToranjKasarnaVitez3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=158;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(150),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		float sirRanka=sirRankCm*xPiksCm;
		objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br155ToranjKasarnaStrijelci(float x, float y,float novacZaProdajuPredhodnog){
	  int i=155;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(150));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
  }
  public ToranjLKasarna br162ToranjKasarnaStrijelci1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=162;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(155));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		float sirRanka=sirRankCm*xPiksCm;
		objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br163ToranjKasarnaStrijelci2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=163;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(155));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		float sirRanka=sirRankCm*xPiksCm;
		objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
  }
  public ToranjLKasarna br164ToranjKasarnaStrijelci3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=164;
	  float rTor=listaRadijusa.get(i);
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  visTorCm=  sirTorCm* (listaSprite.get(150).getVisRezanja(0)/listaSprite.get(150).getSirRezanja(0));
	  listaSprite.put(i,listaSprite.get(155));//// tako da ostatak sustava koj racuna sa ovim ne prijavi gresku
	  ToranjLKasarna oIgre=new ToranjLKasarna(x-rTor, y-rTor,rTor,listaToranjRofF.get(i), izborZaToranj,i);
	  ToranjPKasarna objPrik=new ToranjPKasarna(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.namjestiToranjKasarnu(4,listaToranjRofF.get(i));
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		float sirRanka=sirRankCm*xPiksCm;
		objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
		return oIgre;
		
  }
  public void br200ToranjEmbrio(float x, float y){
	  int i=200;
	  float PpCm=(xPiksCm+yPiksCm)/2;
	  float rTor=1.29f*PpCm;
	  float sirTorCm=0.8f;// sirina visina u prikazu
	  float visTorCm=0.8f;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjL oIgre=new ToranjL(x-rTor, y-rTor,rTor,1, izborZaToranj,i);
	  ToranjP objPrik=new ToranjP(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-visTorCm*yPiksCm/2,rTor,sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  uiMan.dodajElementUListu(objPrik,1);
	  gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br101ToranjStrijelci(float x, float y){
	  int i=101;

	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	//  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i), listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  /*ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-visTorCm*yPiksCm,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);*/
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br102ToranjStrijelciDupli(float x, float y,float novacZaProdajuPredhodnog){
	  int i=102;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	 // visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br103ToranjStrijelciTroDupli(float x, float y,float novacZaProdajuPredhodnog){
	  int i=103;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	//  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br104ToranjStrijelciVatreni(float x, float y,float novacZaProdajuPredhodnog){
	  int i=104;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	//  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  
  }
  public void br106ToranjStrijelciVatreni1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=106;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	//  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(104),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br107ToranjStrijelciVatreni2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=107;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	  //visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(104),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br108ToranjStrijelciVatreni3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=108;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	//  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(104),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br105ToranjStrijelciOtrovni(float x, float y,float novacZaProdajuPredhodnog){
	  int i=105;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	//  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(i),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br109ToranjStrijelciOtrovni1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=109;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
	 // visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(105),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br110ToranjStrijelciOtrovni2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=110;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
//	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(105),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br111ToranjStrijelciOtrovni3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=111;
	  float sirTorCm=1.2f;// sirina visina u prikazu
	  float visTorCm=1.2f;
     //	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  ToranjLStrijelci oIgre=new ToranjLStrijelci(x-listaRadijusa.get(i), y-listaRadijusa.get(i),listaRadijusa.get(i),listaToranjRofF.get(i),visTorCm*yPiksCm/6, izborZaToranj,i);
	  ToranjPStrijelci objPrik=new ToranjPStrijelci(oIgre.getRect().centerX()-sirTorCm*xPiksCm/2, oIgre.getRect().centerY()-3*visTorCm*yPiksCm/4,listaRadijusa.get(i),sirTorCm*xPiksCm,visTorCm*yPiksCm,
			  listaSprite.get(105),i);
	  oIgre.setXVelUPrik(sirTorCm*xPiksCm);
	  oIgre.setYVelUPrik(visTorCm*yPiksCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  objPrik.setVelVojnika(sirTorCm*xPiksCm/2, visTorCm*yPiksCm/2);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm*xPiksCm, 2*visTorCm*yPiksCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br125ToranjMinobacac(float x, float y){
	  int i=125;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(i),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br126ToranjMinobacacTNT(float x, float y,float novacZaProdajuPredhodnog){
	  int i=126;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(i),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br127ToranjMinobacacARMOR(float x, float y,float novacZaProdajuPredhodnog){
	  int i=127;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(i),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br128ToranjMinobacacCLUSTER(float x, float y,float novacZaProdajuPredhodnog){
	  int i=128;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(i),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br130ToranjMinobacacCLUSTER1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=130;
	  float sirTor=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTor=1.2f*yPiksCm;
	  visTor=  sirTor* (listaSprite.get(128).getVisRezanja(0)/listaSprite.get(128).getSirRezanja(0));
	  float polPunjX=1 *(sirTor/100);
	  float polPunjY=-11.67f *(visTor/100);
	  float sirPunj=48 *(sirTor/100);
	  float visPunj=80 *(visTor/100);
	  
	  float polPotX=68 *(sirTor/100);
	  float polPotY=59 *(visTor/100);
	  float sirPot=46 *(sirTor/100);
	  float visPot=55.2f *(visTor/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTor/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTor/2, oIgre.getRect().centerY()-3*visTor/4,rTor,sirTor,visTor,
			  listaSprite.get(128),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTor);
	  oIgre.setYVelUPrik(visTor);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(1, listaSprite.get(200), sirTor, 2*visTor/5, sirRanka, 0.583f*sirRanka);
  }
  public void br131ToranjMinobacacCLUSTER2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=131;
	  float sirTor=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTor=1.2f*yPiksCm;
	  visTor=  sirTor* (listaSprite.get(128).getVisRezanja(0)/listaSprite.get(128).getSirRezanja(0));
	  float polPunjX=1 *(sirTor/100);
	  float polPunjY=-11.67f *(visTor/100);
	  float sirPunj=48 *(sirTor/100);
	  float visPunj=80 *(visTor/100);
	  
	  float polPotX=68 *(sirTor/100);
	  float polPotY=59 *(visTor/100);
	  float sirPot=46 *(sirTor/100);
	  float visPot=55.2f *(visTor/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTor/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTor/2, oIgre.getRect().centerY()-3*visTor/4,rTor,sirTor,visTor,
			  listaSprite.get(128),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTor);
	  oIgre.setYVelUPrik(visTor);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(2, listaSprite.get(200), sirTor, 2*visTor/5, sirRanka, 0.583f*sirRanka);
  }
  public void br132ToranjMinobacacCLUSTER3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=132;
	  float sirTor=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTor=1.2f*yPiksCm;
	  visTor=  sirTor* (listaSprite.get(128).getVisRezanja(0)/listaSprite.get(128).getSirRezanja(0));
	  float polPunjX=1 *(sirTor/100);
	  float polPunjY=-11.67f *(visTor/100);
	  float sirPunj=48 *(sirTor/100);
	  float visPunj=80 *(visTor/100);
	  
	  float polPotX=68 *(sirTor/100);
	  float polPotY=59 *(visTor/100);
	  float sirPot=46 *(sirTor/100);
	  float visPot=55.2f *(visTor/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTor/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTor/2, oIgre.getRect().centerY()-3*visTor/4,rTor,sirTor,visTor,
			  listaSprite.get(128),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTor);
	  oIgre.setYVelUPrik(visTor);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(3, listaSprite.get(200), sirTor, 2*visTor/5, sirRanka, 0.583f*sirRanka);
  }
  public void br129ToranjMinobacacNAPALM(float x, float y,float novacZaProdajuPredhodnog){
	  int i=129;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(i).getVisRezanja(0)/listaSprite.get(i).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(i),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public void br133ToranjMinobacacNAPALM1Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=133;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(129).getVisRezanja(0)/listaSprite.get(129).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(129),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(1, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br134ToranjMinobacacNAPALM2Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=134;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(129).getVisRezanja(0)/listaSprite.get(129).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(129),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(2, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }
  public void br135ToranjMinobacacNAPALM3Rank(float x, float y,float novacZaProdajuPredhodnog){
	  int i=135;
	  float sirTorCm=1.2f*xPiksCm;// sirina visina u prikazu
	  float visTorCm=1.2f*yPiksCm;
	  visTorCm=  sirTorCm* (listaSprite.get(129).getVisRezanja(0)/listaSprite.get(129).getSirRezanja(0));
	  float polPunjX=1 *(sirTorCm/100);
	  float polPunjY=-11.67f *(visTorCm/100);
	  float sirPunj=48 *(sirTorCm/100);
	  float visPunj=80 *(visTorCm/100);
	  
	  float polPotX=68 *(sirTorCm/100);
	  float polPotY=59 *(visTorCm/100);
	  float sirPot=46 *(sirTorCm/100);
	  float visPot=55.2f *(visTorCm/100);
	  float rTor=listaRadijusa.get(i);
	  ToranjLMinobacac oIgre=new ToranjLMinobacac(x-rTor, y-rTor, rTor,listaToranjRofF.get(i),sirTorCm/5, izborZaToranj,i);
	  ToranjPMinobacac objPrik=new ToranjPMinobacac(oIgre.getRect().centerX()-sirTorCm/2, oIgre.getRect().centerY()-3*visTorCm/4,rTor,sirTorCm,visTorCm,
			  listaSprite.get(129),i);
			  objPrik.postaviPolozajePunjenjaIPotezanja(polPunjX, polPunjY, sirPunj, visPunj, polPotX, polPotY,sirPot, visPot);
	  oIgre.setXVelUPrik(sirTorCm);
	  oIgre.setYVelUPrik(visTorCm);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i)+novacZaProdajuPredhodnog);
	  oIgre.namjestiNovacZaProdaju(this.gLog.faIgr.cijenaObjecta(i));
	  uiMan.dodajElementUListu(objPrik,2);
	  gLog.dodajObjekt(oIgre);
	  objPrik.setDvojnikaULogici(oIgre);
	  oIgre.setDvojnikaUPrikazu(objPrik);
	  float sirRanka=sirRankCm*xPiksCm;
	  objPrik.stvoriRank(3, listaSprite.get(200), sirTorCm, 2*visTorCm/5, sirRanka, 0.583f*sirRanka);
  }

 public ProjektilL br403ProjektilFreezZraka(GameLogicObject objectPozivatelj, float x, float y ){
      int i=403;
	    float picPsec=1f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.4f*xPiksCm;
	    float velY=0.4f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.8f*xPiksCm;
	    float ispY=0.8f*yPiksCm;
	    // pozicija frezz tipa u odnosu na  dno tornja toranj
	    float relPozX=-0.18f*xPiksCm;// ovo sam ruèno namješta jer neznma zašto mi je slika malo na lijevo
	    float relPozY=-objectPozivatelj.getYVelUPrikazu() ;// znaci vrh tornja
	    float letX=0.3f*xPiksCm;
	    float letY=0.3f*yPiksCm;
	    float pogX=0.5f*xPiksCm;
	    float pogY=0.5f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.1f,false,true,i);
        ProjektilP objPrik=new ProjektilP(x,y,i);
        objPrik.stvoriRepZraku(letY,letX,13, objectPozivatelj,ispX,ispY, relPozX, relPozY);
        objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
        oIgre.stvoriZrakuProjektil(1000);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		//oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 4,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 4,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);  
		return oIgre;
}
  public ProjektilL br412ProjektilFreezTop1Razina(GameLogicObject objectPozivatelj,float x, float y ){
      int i=412;
	    float picPsec=2.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.1763f*xPiksCm;
	    float letY=letX*1.667f;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.3f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.6f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(false);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,velY,true,listaSprite.get(i),letY*2);
      //objPrik.crtajPromasajIzSlikeLeta(4,5);
      //objPrik.stvoriGenerickiRep( 20, 1000,letY,10,letX,letX/2,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 4,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);

		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br413ProjektilFreezTop2Razina(GameLogicObject objectPozivatelj,float x, float y ){
      int i=413;
	    float picPsec=2.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.3f*xPiksCm;
	    float velY=0.3f*xPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.2263f*xPiksCm;
	    float letY=letX*1.667f;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.6f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(false);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letX*1.667f,pogX,pogY,true,listaSprite.get(i),letY*2);
      //objPrik.crtajPromasajIzSlikeLeta(4,5);
      
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		
		objPrik.stvoriRep( 5, 1000,0,10,letX,letX*2f,true);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 4,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);

		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		return oIgre;
}
  public ProjektilL br407ProjektilStrijelaOtrovana(GameLogicObject objectPozivatelj,float x, float y ){
      int i=407;
	    float picPsec=3.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.1f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(i),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,letY,10,letX,letX*1,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),4,5,10,0);

		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br418ProjektilStrijelaOtrovanaRank1(GameLogicObject objectPozivatelj,float x, float y ){
      int i=418;
	    float picPsec=3.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.1f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(407),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,letY,10,letX,letX*1,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),4,6,10,0);

		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br419ProjektilStrijelaOtrovanaRank2(GameLogicObject objectPozivatelj,float x, float y ){
      int i=419;
	    float picPsec=3.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.1f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(407),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,letY,10,letX,letX*1,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),4,7,10,0);

		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br420ProjektilStrijelaOtrovanaRank3(GameLogicObject objectPozivatelj,float x, float y ){
      int i=420;
	    float picPsec=3.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.1f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(407),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,letY,10,letX,letX*1,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),4,8,10,0);

		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br402ProjektilStrijela(GameLogicObject objectPozivatelj,float x, float y ){
        int i=402;
	    float picPsec=4.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.05f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.12f,false,true,i);
        oIgre.setOstecujemSamoJednog();
        oIgre.kosiHitacIliRavno(true);
        oIgre.predvidiPolozaj();
        ProjektilP objPrik=new ProjektilP(x,y,i);
        objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
        objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(i),letY*2);
        objPrik.crtajPromasajIzSlikeLeta(4,10);
        //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		///*TEST ISPARAVI POSLIJE OVO*/oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,3,0,2,5,2,5);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
 
  
  public ProjektilL br401ProjektilGranata(GameLogicObject objectPozivatelj,float x, float y ){
        int i=401;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
        ProjektilP objPrik=new ProjektilP(x,y,i);
        objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/8);
        objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
        objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
  }

  public ProjektilL br405ProjektilLavaMeteor(GameLogicObject objectPozivatelj,float x, float y ){
      int i=405;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.0f*xPiksCm;
	    float pogY=1.0f*yPiksCm;
	    float picPsec=3f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i){
    	  boolean ispaljenZvuk=false;
    	  int redBrZvuk=vratiRedniBrojZvukaZaPustanje(IgricaActivity.zvukLetMeteora);
    	  public void crtanjeIspaljivanja(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
    		  if(!ispaljenZvuk){      			
      			pustiZvuk(redBrZvuk,1,1,-1,0);
      			ispaljenZvuk=true;
      		    }
    	  }
    	  public void nacrtajLetVanjski(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
    		  
    	  }
           public void crtanjePogotkaVanjski(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
        	 if(ispaljenZvuk) {
        		 vratiSoundPool().stop(vratiKodZvuka(redBrZvuk));
        		 ispaljenZvuk=false;
        	 }
    	  }
      };
      objPrik.crtanjePogotkaMrlja(15, pogX, pogY,0,0);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1500,0,5,letX,letY,true);
      oIgre.setKonstantnuStetuPoslijePogotka(40,1,1,pogX*1.5f,pogY*1.5f ,15);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(40,10, 1,0,0,0,0,0,0);
		return oIgre;
}
  public ProjektilL br406ProjektilStrijelaVatrena(GameLogicObject objectPozivatelj,float x, float y ){
      int i=406;
	    float picPsec=2.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.6f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(i),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,0,10,letX,letX*1.423f,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),5,1,5,8);
		///*TEST ISPARAVI POSLIJE OVO*/oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,3,0,2,5,2,5);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br421ProjektilStrijelaVatrenaRank1(GameLogicObject objectPozivatelj,float x, float y ){
      int i=421;
	    float picPsec=2.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.6f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(406),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,0,10,letX,letX*1.423f,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),6,2,6,9);
		///*TEST ISPARAVI POSLIJE OVO*/oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,3,0,2,5,2,5);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br422ProjektilStrijelaVatrenaRank2(GameLogicObject objectPozivatelj,float x, float y ){
      int i=422;
	    float picPsec=2.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.6f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(406),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,0,10,letX,letX*1.423f,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),6,3,6,10);
		///*TEST ISPARAVI POSLIJE OVO*/oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,3,0,2,5,2,5);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br423ProjektilStrijelaVatrenaRank3(GameLogicObject objectPozivatelj,float x, float y ){
      int i=423;
	    float picPsec=2.5f*(xPiksCm+yPiksCm)/2;
	    float radijus=0.1f*(xPiksCm+yPiksCm)/2;
	    float velX=0.2f*xPiksCm;
	    float velY=0.2f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=0.2f*xPiksCm;
	    float ispY=0.2f*xPiksCm;
	    float letX=0.0763f*xPiksCm;
	    float letY=0.28f*yPiksCm;
	    float pogX=0.2f*xPiksCm;
	    float pogY=0.2f*yPiksCm;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,0.6f,false,true,i);
      oIgre.setOstecujemSamoJednog();
      oIgre.kosiHitacIliRavno(true);
      oIgre.predvidiPolozaj();
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaSamoAkoJeNacinioStetu();
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,true,listaSprite.get(406),letY*2);
      objPrik.crtajPromasajIzSlikeLeta(4,5);
      objPrik.stvoriRep( 20, 1000,0,10,letX,letX*1.423f,true);
      //objPrik.stvoriGenerickiRep( 5, 100, letY/6,letX,letY,20);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),7,4,7,11);
		///*TEST ISPARAVI POSLIJE OVO*/oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,3,0,2,5,2,5);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return oIgre;
}
  public ProjektilL br408ProjektilTNT(GameLogicObject objectPozivatelj,float x, float y ){
      int i=408;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/8);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br409ProjektilARMOR(GameLogicObject objectPozivatelj,float x, float y ){
      int i=409;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/8);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br410ProjektilKLUSTER(GameLogicObject objectPozivatelj,float x, float y ){
      int i=410;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/8);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br412ProjektilKLUSTERrank1(GameLogicObject objectPozivatelj,float x, float y ){
      int i=412;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/8);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(410),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br413ProjektilKLUSTERrank2(GameLogicObject objectPozivatelj,float x, float y ){
      int i=413;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(410),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br414ProjektilKLUSTERrank3(GameLogicObject objectPozivatelj,float x, float y ){
      int i=414;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(410),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br411ProjektilNAPALM(GameLogicObject objectPozivatelj,float x, float y ){
      int i=411;
	    float radijus=3.1f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),5,5,10,8);
		return oIgre;
}
  public ProjektilL br415ProjektilNAPALMrank1(GameLogicObject objectPozivatelj,float x, float y ){
      int i=415;
	    float radijus=3.1f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(411),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),6,6,10,9);
		return oIgre;
}
  public ProjektilL br416ProjektilNAPALMrank2(GameLogicObject objectPozivatelj,float x, float y ){
      int i=416;
	    float radijus=3.1f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(411),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),7,7,10,10);
		return oIgre;
}
  public ProjektilL br417ProjektilNAPALMrank3(GameLogicObject objectPozivatelj,float x, float y ){

      int i=417;
	    float radijus=3.1f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=1.2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,true,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(411),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 5,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),8,8,10,11);
		return oIgre;
}
  public ProjektilL br418ProjektilTowerBuster(GameLogicObject objectPozivatelj,float x, float y ){
      int i=418;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,false,false,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
      objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,0);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
     
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.stvoriTowerBusterProjektil();
		//oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  public ProjektilL br419ProjektilGovance(GameLogicObject objectPozivatelj,float x, float y ){
      int i=419;
	    float radijus=2.8f*(xPiksCm+yPiksCm)/2;
	    float velX=1.8f*xPiksCm;
	    float velY=1.8f*yPiksCm;
	    boolean kolSteta=false;
	    float ispX=1.0f*xPiksCm;
	    float ispY=1.0f*xPiksCm;
	    float letX=0.27f*xPiksCm;
	    float letY=0.27f*yPiksCm;
	    float pogX=1.8f*xPiksCm;
	    float pogY=1.8f*yPiksCm;
	    float picPsec=2f*(xPiksCm+yPiksCm)/2;
	    ProjektilL oIgre=new ProjektilL(objectPozivatelj,x,y,velX,velY,picPsec,kolSteta,radijus,2,true,false,i);
      ProjektilP objPrik=new ProjektilP(x,y,i);
     // objPrik.crtanjePogotkaMrlja(5, pogX/4, pogY/4,0,pogY/4);
      objPrik.stvoriProjektil(ispX,ispY,letX,letY,pogX,pogY,listaSprite.get(i),0);
      objPrik.stvoriRep( 20, 1000, letY/4,10,letX,letY,true);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.redukcijaXBrzine(40);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 7,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),7,10,10,0);
		//oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i), 1,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),0,0,0,0);
		return oIgre;
}
  
  public void br1ObjectIgre(float x, float y){// ovaj vojnim može sam stojat bez toranjaa
       // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	    int i=1;
	    float picPsecX=0.5f*xPiksCm* koeficijentBrzine;
	    float picPsecY=0.5f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,mnoziteljVisineLikova*vis,mnoziteljSirnineLikova*sir,mnoziteljVisineLikova*vis,i);
		ObjectIgre oIgre=new ObjectIgre(mnoziteljSirnineLikova*sir*4,mnoziteljVisineLikova*vis*4,x,y-vis,1,picPsecX,picPsecY);
		oIgre.setXVelUPrik(mnoziteljSirnineLikova*sir);
		oIgre.setYVelUPrik(mnoziteljVisineLikova*vis);  
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.bilderStvoriBraniteljaIzKasarne();
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
		oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
  }
  public GameLogicBranitelj br2Branitelj2Razina(float x, float y, ToranjL poziv){//
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=2;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br51BraniteljStrijelciIzKasarne(float x, float y, ToranjL poziv){// 
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=51;
	    float picPsecX=1f*xPiksCm;
	    float picPsecY=1f*yPiksCm;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float radijus=((xPiksCm+yPiksCm)/2)*3f;
	    float RofF=0.5f;
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.3f,vis*1.3f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
		oIgre.bilderStvoriBraniteljaIzKasarne();
		oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.7f);
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
	    oIgre.bilderStvoriStrijelcaOdOvogObjekta(radijus, RofF, br402ProjektilStrijela(oIgre,x,y));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br52BraniteljStrijelciIzKasarneRank1(float x, float y, ToranjL poziv){// 
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=52;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(51).getSirRezanja(0)/listaSprite.get(51).getVisRezanja(0));
	    float radijus=((xPiksCm+yPiksCm)/2)*3f;
	    float RofF=0.7f;
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(51),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.3f,vis*1.3f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(51).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(51).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
		oIgre.bilderStvoriBraniteljaIzKasarne();
		oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.7f);
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
	    oIgre.bilderStvoriStrijelcaOdOvogObjekta(radijus, RofF, br402ProjektilStrijela(oIgre,x,y));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br53BraniteljStrijelciIzKasarneRank2(float x, float y, ToranjL poziv){// 
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=53;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(51).getSirRezanja(0)/listaSprite.get(51).getVisRezanja(0));
	    float radijus=((xPiksCm+yPiksCm)/2)*3f;
	    float RofF=0.9f;
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(51),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.3f,vis*1.3f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(51).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(51).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
		oIgre.bilderStvoriBraniteljaIzKasarne();
		oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.7f);
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
	    oIgre.bilderStvoriStrijelcaOdOvogObjekta(radijus, RofF, br402ProjektilStrijela(oIgre,x,y));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br54BraniteljStrijelciIzKasarneRank3(float x, float y, ToranjL poziv){// 
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=54;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(51).getSirRezanja(0)/listaSprite.get(51).getVisRezanja(0));
	    float radijus=((xPiksCm+yPiksCm)/2)*3f;
	    float RofF=1.1f;
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(51),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.3f,vis*1.3f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(51).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(51).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
		oIgre.bilderStvoriBraniteljaIzKasarne();
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.7f);
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
	    oIgre.bilderStvoriStrijelcaOdOvogObjekta(radijus, RofF, br402ProjektilStrijela(oIgre,x,y));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br3Branitelj3Razina(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=3;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		 //izrac br prik po sec/////////
		    int brSlikeHoda=0;
		    float postotakOdSirSlPomak=120;
		
		    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
		    float sirUSec=(picPsecX+picPsecY)/2;
		    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
		    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
		    /////
		oIgre.bilderStvoriBraniteljaIzKasarne();
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br4BraniteljGorstak(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=4;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.3f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.9f);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br9BraniteljGorstakRank1(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=9;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.3f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(4).getSirRezanja(0)/listaSprite.get(4).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(4).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(4).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(4),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.9f);
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br10BraniteljGorstakRank2(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=10;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.3f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(4).getSirRezanja(0)/listaSprite.get(4).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(4).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(4).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(4),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.9f);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br11BraniteljGorstakRank3(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=11;
	    float picPsecX=1f*xPiksCm;
	    float picPsecY=1f*yPiksCm;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.3f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(4).getSirRezanja(0)/listaSprite.get(4).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(4).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(4).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(4),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.9f);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br5BraniteljVitez(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=5;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.1f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.8f);
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  
  public GameLogicBranitelj br6BraniteljVitezRank1(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=6;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.1f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(5).getSirRezanja(0)/listaSprite.get(5).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(5).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(5).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(5),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.8f);
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br7BraniteljVitezRank2(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=7;
	    float picPsecX=1f*xPiksCm *koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.1f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(5).getSirRezanja(0)/listaSprite.get(5).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(5).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(5).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(5),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.8f);
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public GameLogicBranitelj br8BraniteljVitezRank3(float x, float y, ToranjL poziv){// ovaj vojnim može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=8;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1.1f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(5).getSirRezanja(0)/listaSprite.get(5).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(5).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(5).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(5),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.8f);
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
 }
  public void br49Junak(float x, float y, float samounistenjeZaSec){// ovaj vojnik može sam stojat bez toranjaa
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	  int i=49;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*1f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.7f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x-sir/2,y-vis/2,i,picPsecX,picPsecY);
		
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.5f);
		 
		oIgre.bilderStvoriSamostalnogBranitelja();;
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(1),listaSteteZaArmor.get(1),1);
	    oIgre.setHelthArmor(listaHeltha.get(1),listaArmora.get(1));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		oIgre.setSamounistenjeZa(samounistenjeZaSec);
		
  }
  public GameLogicBranitelj br1VojnikRazina1(float x, float y, ToranjL poziv){// stvara vojnike pocetne klase
      // br1Sprite.dodajNoviSprite(BitmapFactory.decodeResource(res, R.drawable.princ), 1800,400,12, 4,12);
	    ///!!!!!!!!!!!!NE ZABORAVI UBACIT INDIKATOR U namjestiPocetneZastavice() metodi
	  int i=1;
	    float picPsecX=1f*xPiksCm* koeficijentBrzine;
	    float picPsecY=1f*yPiksCm* koeficijentBrzine;
	    float vis=mnoziteljVisineLikova*yPiksCm*0.97f;
	    float sir;//=mnoziteljSirnineLikova*xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
		ObjectIgre oIgre=new ObjectIgre(vis*1.8f,vis*1.8f,x,y-vis*1.8f/2-vis/2,i,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		oIgre.bilderStvoriBraniteljaIzKasarne();
		oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
	  
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;
	  /* int i=1;
	    float picPsecX=1f*xPiksCm;
	    float picPsecY=1f*yPiksCm;
	    float vis=yPiksCm*0.65f;
	    float sir;//=xPiksCm*0.65f;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    /////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,1);
		ObjectIgre oIgre=new ObjectIgre(sir*1.8f,vis*1.8f,x,y,1,picPsecX,picPsecY);
		oIgre.jaSamTeStvorio(poziv);// salje referencu radi daljnje komunikacije
		oIgre.setXVelUPrik(sir);
		oIgre.setYVelUPrik(vis);
		 oIgre.setUdaracaSvakihSekundi(0.6f);
		 oIgre.setUdaracaSvakihSekundi(0.5f);
		objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
		oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i),listaArmora.get(i));
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		return (GameLogicBranitelj) oIgre;*/
 }
 public void br201RavniPut(float x, float y,float sir, float vis,float dx,float dy){
	 PutL putL1=new PutL(x,y,sir,vis,listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],201);
	 PutP putP1=new PutP(x,y,sir,vis);
	 putP1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 putL1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 if(listaDodat2[this.trenutniVal][iListe]==1){
		 putL1.setInertniPut(true);
	 }
	
	 uiMan.dodajElementUListu(putP1,1);  
	 if((listaDelay[this.trenutniVal][iListe]>0||this.trenutniVal>0)&&(int) listaDodat[this.trenutniVal][iListe]==0){
		if(taskbar!=null) {
			gLog.dodajCestuNaknadno(putL1);
			this.taskbar.dodajBotuneZaPocetakValova();
			this.taskbar.inicijalizirajPocmiValoveBotune();
		}
	 }
	 else  gLog.dodajObjekt(putL1);
  }
 public void br202Okuka( float x,  float y, float sir,  float vis,float dx,float dy){
	 PutL putL1=new PutL(x,y,sir,vis,listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],202);
	 PutP putP1=new PutP(x,y,sir,vis);
	 putP1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 putL1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 if(listaDodat2[this.trenutniVal][iListe]==1){
		 putL1.setInertniPut(true);
	 }
	
	 uiMan.dodajElementUListu(putP1,1);  
	 if((listaDelay[this.trenutniVal][iListe]>0||this.trenutniVal>0)&&(int) listaDodat[this.trenutniVal][iListe]==0){
			if(taskbar!=null) {
				gLog.dodajCestuNaknadno(putL1);
				this.taskbar.dodajBotuneZaPocetakValova();
				this.taskbar.inicijalizirajPocmiValoveBotune();
			}
		 }
	 else  gLog.dodajObjekt(putL1);
   }
 public void br203RavniPutKut(float x, float y,float sir, float vis,float dx,float dy){
	 PutL putL1=new PutL(x,y,sir,vis,listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],203);
	 PutP putP1=new PutP(x,y,sir,vis);
	 putP1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 putL1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);

	 if(listaDodat2[this.trenutniVal][iListe]==1){
		 putL1.setInertniPut(true);
	 }
	 uiMan.dodajElementUListu(putP1,1);  
	 if((listaDelay[this.trenutniVal][iListe]>0||this.trenutniVal>0)&&(int) listaDodat[this.trenutniVal][iListe]==0){
			if(taskbar!=null) {
				gLog.dodajCestuNaknadno(putL1);
				this.taskbar.dodajBotuneZaPocetakValova();
				this.taskbar.inicijalizirajPocmiValoveBotune();
			}
		 }
	 else  gLog.dodajObjekt(putL1);
  }
 public void br205OkukaKut( float x,  float y, float sir,  float vis,float dx,float dy){
	 PutL putL1=new PutL(x,y,sir,vis,listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],205);
	 PutP putP1=new PutP(x,y,sir,vis);
	 putP1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 putL1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);

	 if(listaDodat2[this.trenutniVal][iListe]==1){
		 putL1.setInertniPut(true);
	 }
	 uiMan.dodajElementUListu(putP1,1);  
	 if((listaDelay[this.trenutniVal][iListe]>0||this.trenutniVal>0)&&(int) listaDodat[this.trenutniVal][iListe]==0){
			if(taskbar!=null) {
				gLog.dodajCestuNaknadno(putL1);
				this.taskbar.dodajBotuneZaPocetakValova();
				this.taskbar.inicijalizirajPocmiValoveBotune();
			}
		 }
	 else  gLog.dodajObjekt(putL1);
   }
 public void br204Kraj(float x, float y,float sir, float vis,float dx,float dy){
	 PutL putL1=new PutL(x,y,sir,vis,listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],204);
	 PutP putP1=new PutP(x,y,sir,vis);
	 putP1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);
	 putL1.postaviRedniBroj( (int) listaDodat[this.trenutniVal][iListe]);

	 uiMan.dodajElementUListu(putP1,1); 
	 if((listaDelay[this.trenutniVal][iListe]>0||this.trenutniVal>0)&&(int) listaDodat[this.trenutniVal][iListe]==0){
			if(taskbar!=null) {
				gLog.dodajCestuNaknadno(putL1);
				this.taskbar.dodajBotuneZaPocetakValova();
				this.taskbar.inicijalizirajPocmiValoveBotune();
			}
		 }
	 else  gLog.dodajObjekt(putL1);
  }
 public void br_1Radnik( float x,  float y, float polNaCesti){// zombi radnik
	    int i=-1;
	    float sir;//=mnoziteljSirnineLikova*0.5f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.95f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	   float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    ///////////////////////////////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,-1);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,-1,picPsecX,picPsecY,vrijednost);
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    objPrik.bilderSetirajDxPomakZaSjenuTijekomBorbe(sir/3,sir/3);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_2Protivnik( float x,  float y, float polNaCesti){// šminkerica zombie
	    int i=-2;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.9f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setStetuZaHelthArmorTip(listaSteteZaHelth.get(i), listaSteteZaArmor.get(i), 7,listaVrijemeUsporavanja.get(i),listaPostotakUsporavanja.get(i),listaVrijemeUsporavanja.get(i),20,listaVrijemeUsporavanja.get(i),0);
	   // oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),7);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_3ProtivnikMMA( float x,  float y, float polNaCesti){// mma zombie
	    int i=-3;
	    float sir;//=mnoziteljSirnineLikova*0.75f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.35f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float sirLog=0.2f*xPiksCm;
	    float visLog=0.2f*yPiksCm;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float omjerX=5;
	    float omjerY=5;
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=1.5f*listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sirLog,visLog,x,y,i,picPsecX,picPsecY,vrijednost);
	  //  objPrik.bilderPomakObjektUOdnosuNaLogiku(-(vis-visLog));
	    oIgre.setPolozajNaCesti(polNaCesti);
	    oIgre.setHelthArmorTipnapada(50,20,1);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmor(200, 100);
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1.5f);
	  
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
} 
 public void br_4Protivnik( float x,  float y, float polNaCesti){// reper zombie
	    int i=-4;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.9f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_5ProtivnikPocetni( float x,  float y, float polNaCesti){// pocetni zombie
	    int i=-5;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, 2*brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_6ProtivnikDebeli( float x,  float y, float polNaCesti){// debeli zombie
	    int i=-6;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.3f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_7ProtivnikCistacica( float x,  float y, float polNaCesti){// cistacica zombie
	    int i=-7;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.9f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}

 public void br_8ProtivnikKuhar( float x,  float y, float polNaCesti){// pocetni zombie
	    int i=-8;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.5f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=100;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/1000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.9f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_9ProtivnikPas( float x,  float y, float polNaCesti){// 
	    int i=-9;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.4f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/8000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}

 public void br_10ProtivnikPolicajac( float x,  float y, float polNaCesti){// pocetni zombie
	    int i=-10;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.3f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_11ProtivnikDebeliPrdonja( float x,  float y, float polNaCesti){// pocetni zombie
	    int i=-11;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.3f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	   /* float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);*/
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/4,vis/4,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    oIgre.setImunNaKonstantnuStetu();
	    
	    oIgre.setJesamLiImunNaBranitelje(true);
	    oIgre.setJesamLiImunNaToranjMinobacac(true);
	    oIgre.setRazmakCrtanjaIHodaPoCestiZaLetece(0,-1.5f*vis);
	    oIgre.setTrimSjene(0, 1.5f*vis);
	    oIgre.setTrimTouchPol(0, 1.5f*vis);
	    
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    objPrik.postaviRucnoAnimacijeHoda(0, 0, 0, 0, 0, 0, 0, 0);
	    objPrik.postaviRucnoAnimacijeBorba(0,0, 0,0); 
	    objPrik.bilderSetirajSkokiceUHodu(50,vis*0.8f,55);
	    objPrik.postaviCrtanjeSmrtiBezLokve(0, 0, sir/3, vis/3);
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_12BossIzvanzemaljac( float x,  float y,int brZivota, float polNaCesti,int brZivotaPocetni){// pocetni zombie
	    int i=-12;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.3f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    if(brZivotaPocetni==0){
	    	brZivotaPocetni=(int)this.listaDodat[this.trenutniVal][iListe];
	    }
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	   // int brZivotaPocetni=5;
	    float smanjenjeVelicine=1;
	    float povecanjeBrzine=1;
	    float smanjenjeHelthaIarmora=1;
	    if(brZivota==0){
	    	brZivota=brZivotaPocetni;
	    	
	    }
	    else {
	      smanjenjeVelicine=0.5f+0.5f*brZivota/brZivotaPocetni;
	     
		  povecanjeBrzine=1f+ (3.5f/brZivotaPocetni)*(brZivotaPocetni-brZivota);
		  smanjenjeHelthaIarmora=0.5f+0.5f*brZivota/brZivotaPocetni;
	    }
	    vis=smanjenjeVelicine*vis;
	    sir=smanjenjeVelicine*sir;
	    picPsecX=picPsecX*povecanjeBrzine;
	    picPsecY=picPsecY*povecanjeBrzine;
	    brPrikPoSec=brPrikPoSec*povecanjeBrzine;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    /////odavanje vise zivota///
	 
	    oIgre.setProtivniSaViseZivota(brZivota,brZivotaPocetni,2);
	    oIgre.setAnimacjuTeleportacijePrRodenju();
	    /////////////////////////////
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i)*smanjenjeHelthaIarmora,listaSteteZaArmor.get(i)*smanjenjeHelthaIarmora,1);
	    oIgre.setHelthArmor(listaHeltha.get(i)*smanjenjeHelthaIarmora, listaArmora.get(i)*smanjenjeHelthaIarmora);
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*2f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		objPrik.preskociPrviTakt();
		this.brProtivnika--;
}
 
 public void br_13BossTowerBuster(float x,  float y, float polNaCesti){// 
	 int i=-13;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.8f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    brPrikPoSec=brPrikPoSec*3f;
	    float radijus=((xPiksCm+yPiksCm)/2)*0.1f;
	    float RofF=0.1f;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setProtivnikKojiVrijediViseZivota(11);
	    oIgre.bilderStvoriStrijelcaOdOvogObjekta(true,0,-vis/2,radijus, RofF,  br418ProjektilTowerBuster(oIgre,x,y),3f);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    oIgre.trimLogikeIprikaza(0, sir/4-vis/2);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    objPrik.postaviRucnoAnimacijeIspucavanja(3, 0,
	    		3, 3,
	    		3, 3, 
	    		3, 3,
	    		3, 1,
	    		3, 2,
	    		3, 2, 
	    		3, 2);
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
		
} 
 public void br_14ProtivnikStudentica( float x,  float y, float polNaCesti){// cistacica zombie
	    int i=-14;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.9f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 public void br_15ProtivnikStarleta( float x,  float y, float polNaCesti){// cistacica zombie
	    int i=-15;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.9f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}
 
 public void br_16BossDebeliSerac(float x,  float y, float polNaCesti){// 
	 int i=-16;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.1f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    float radijus=((xPiksCm+yPiksCm)/2)*3.1f;
	    float RofF=0.3f;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.bilderStvoriStrijelcaOdOvogObjekta(false,0,0,radijus, RofF, br419ProjektilGovance(oIgre,x,y),3f);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    objPrik.postaviRucnoAnimacijeIspucavanja(3, 0,
	    		3, 0,
	    		3, 0, 
	    		3, 0,
	    		3, 0,
	    		3, 0,
	    		3, 0, 
	    		3, 0);
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*0.6f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
		
} 
 public void br_17Biznisman( float x,  float y, float polNaCesti){// cistacica zombie
	    int i=-17;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.9f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x-sir/2,y-vis/2,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x-sir/2,y-vis/2,i,picPsecX,picPsecY,vrijednost);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
		uiMan.dodajElementUListu(objPrik,2);
		oIgre.animacijaIzlaskaIzZemlje();
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		objPrik.preskociPrviTakt();
		this.brProtivnika--;
}
 
 public void br501DodatakNaMapu(){
	 int i=501;
	 PutL putL1=new PutL(listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],this.listaDodat[this.trenutniVal][iListe],this.listaDodat2[this.trenutniVal][iListe],0,0,i);
	 float inte =this.listaDodat3[this.trenutniVal][iListe];
	 int intei=(int) inte;
	
	 DodatakNaMapu dodNaMap=new DodatakNaMapu(listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],intei,(int)this.listaDodat5[this.trenutniVal][iListe],i);
	 dodNaMap.setDaliDaIgnoriramTouch(true);
	 dodNaMap.setDvojnikaULogici(putL1);
	 gLog.dodajObjekt(putL1);
	 uiMan.dodajElementUListu(dodNaMap,(int)listaDodat4[this.trenutniVal][iListe]);  
  }
 
 public void br502DodatakNaMapuRandom(){
	 int i=502;
	 PutL putL1=new PutL(listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],this.listaDodat[this.trenutniVal][iListe],this.listaDodat2[this.trenutniVal][iListe],0,0,i);
	 float inte =this.listaDodat3[this.trenutniVal][iListe];
	 int intei=(int) inte;
	
	 DodatakNaMapu dodNaMap=new DodatakNaMapu(listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],intei,(int)this.listaDodat5[this.trenutniVal][iListe],i);
	 dodNaMap.postaviRandomCrtanje(this.listaDodat6[this.trenutniVal][iListe]);
	 dodNaMap.setDaliDaIgnoriramTouch(true);
	 dodNaMap.setDvojnikaULogici(putL1);
	 gLog.dodajObjekt(putL1);
	 uiMan.dodajElementUListu(dodNaMap,(int)listaDodat4[this.trenutniVal][iListe]);  
  }
 public void br503DodatakNaMapuPomakS0rtiranja(){
	 int i=503;
	 PutL putL1=new PutL(listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],this.listaDodat[this.trenutniVal][iListe],this.listaDodat2[this.trenutniVal][iListe],0,0,i);
	 float inte =this.listaDodat3[this.trenutniVal][iListe];
	 int intei=(int) inte;
	
	 DodatakNaMapu dodNaMap=new DodatakNaMapu(listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],intei,(int)this.listaDodat5[this.trenutniVal][iListe],i);
	 dodNaMap.postaviPomakSortiranja(listaDodat6[this.trenutniVal][iListe]);
	 dodNaMap.setDaliDaIgnoriramTouch(true);
	 dodNaMap.setDvojnikaULogici(putL1);
	 gLog.dodajObjekt(putL1);
	 uiMan.dodajElementUListu(dodNaMap,(int)listaDodat4[this.trenutniVal][iListe]);  
  }
 public void br504DodatakNaMapuRandomPomakSortiranja(){
	 int i=504;
	 PutL putL1=new PutL(listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],this.listaDodat[this.trenutniVal][iListe],this.listaDodat2[this.trenutniVal][iListe],0,0,i);
	 float inte =this.listaDodat3[this.trenutniVal][iListe];
	 int intei=(int) inte;
	
	 DodatakNaMapu dodNaMap=new DodatakNaMapu(listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],intei,(int)this.listaDodat5[this.trenutniVal][iListe],i);
	 dodNaMap.postaviRandomCrtanje(this.listaDodat6[this.trenutniVal][iListe]);
	 dodNaMap.postaviPomakSortiranja(listaDodat7[this.trenutniVal][iListe]);
	 dodNaMap.setDaliDaIgnoriramTouch(true);
	 dodNaMap.setDvojnikaULogici(putL1);
	 gLog.dodajObjekt(putL1);
	 uiMan.dodajElementUListu(dodNaMap,(int)listaDodat4[this.trenutniVal][iListe]);  
  }
 public void br505DodatakNaMapuIzvrsiJedanputIStaniNa(){
	 int i=505;
	 PutL putL1=new PutL(listaDx[this.trenutniVal][iListe],listaDy[this.trenutniVal][iListe],this.listaDodat[this.trenutniVal][iListe],this.listaDodat2[this.trenutniVal][iListe],0,0,i);
	 float inte =this.listaDodat3[this.trenutniVal][iListe];
	 int intei=(int) inte;
	
	 DodatakNaMapu dodNaMap=new DodatakNaMapu(listaPozX[this.trenutniVal][iListe],listaPozY[this.trenutniVal][iListe],listaSir[this.trenutniVal][iListe],listaVis[this.trenutniVal][iListe],intei,(int)this.listaDodat5[this.trenutniVal][iListe],i);
	 dodNaMap.setDaliDaIgnoriramTouch(true);
	 dodNaMap.postaviCrtamJedanputIStanemNaSlici((int)this.listaDodat6[this.trenutniVal][iListe]);
	 dodNaMap.postaviPomakSortiranja(listaDodat7[this.trenutniVal][iListe]);
	 dodNaMap.setDvojnikaULogici(putL1);
	 gLog.dodajObjekt(putL1);
	 uiMan.dodajElementUListu(dodNaMap,(int)listaDodat4[this.trenutniVal][iListe]);  
  }
 public void br_12BossIzvanzemaljacIzlazakIzTanjura( float x,  float y,int brZivota, float polNaCesti){// pocetni zombie
	    int i=-12;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*1.3f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    ////////////////////////////////////////
	    float[] listaVremena=new float[5];
	    float[] listaX=new float[5];
	    float[] listaY=new float[5];
	    ///1 polozaj
	    listaVremena[0]=4f;
	    listaX[0]=6.67f;
	    listaY[0]=5.09f;
	    ///2 polozaj
	    listaVremena[1]=4f;
	    listaX[1]=5.13f;
	    listaY[1]=4.81f;
	    ///3 polozaj
	    listaVremena[1]=4f;
	    listaX[2]=5.3f;
	    listaY[2]=1.8f;
	    ///4 polozaj
	    listaVremena[1]=4f;
	    listaX[3]=1.78f;
	    listaY[3]=5.26f;
	    ///5 polozaj
	    listaVremena[1]=4f;
	    listaX[4]=1.71f;
	    listaY[4]=1.73f;
	    /////////////////////////////////////////
	    
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    /////odavanje vise zivota///
	    int brZivotaPocetni=5;
	    float smanjenjeVelicine=1;
	    float povecanjeBrzine=1;
	    float smanjenjeHelthaIarmora=1;
	    if(brZivota==0){
	    	brZivota=brZivotaPocetni;
	    	
	    }
	    else {
	      smanjenjeVelicine=0.5f+0.5f*brZivota/brZivotaPocetni;
	     
		  povecanjeBrzine=1f+ (3.5f/brZivotaPocetni)*(brZivotaPocetni-brZivota);
		  smanjenjeHelthaIarmora=0.5f+0.5f*brZivota/brZivotaPocetni;
	    }
	    vis=smanjenjeVelicine*vis;
	    sir=smanjenjeVelicine*sir;
	    picPsecX=picPsecX*povecanjeBrzine;
	    picPsecY=picPsecY*povecanjeBrzine;
	    brPrikPoSec=brPrikPoSec*povecanjeBrzine;
	    
	    oIgre.postaviSlijedTeleportacijaNaknRodenja(listaX, listaY, listaVremena);
	    oIgre.setProtivniSaViseZivota(brZivota,brZivotaPocetni,2);
	    //oIgre.setAnimacjuTeleportacijePrRodenju();
	    /////////////////////////////
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i)*smanjenjeHelthaIarmora, listaArmora.get(i)*smanjenjeHelthaIarmora);
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*2f);
		uiMan.dodajElementUListu(objPrik,2);
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
		this.brProtivnika--;
}

 public void br_18Kapitalista( float x,  float y, float polNaCesti){// cistacica zombie
	    int i=-18;
	    float sir;//=mnoziteljSirnineLikova*0.35f*xPiksCm;
	    float vis=mnoziteljVisineLikova*0.75f*yPiksCm;
	    sir=  vis* (listaSprite.get(i).getSirRezanja(0)/listaSprite.get(i).getVisRezanja(0));
	    float omjerX=10;
	    float omjerY=10;
	    float picPsecX=xPiksCm* this.listaBrzina.get(i);
	    float picPsecY=yPiksCm* this.listaBrzina.get(i);
	    float vrijednost= listaCijena.get(i);
	    //izrac br prik po sec/////////
	    int brSlikeHoda=0;
	    float postotakOdSirSlPomak=120;
	
	    float sirPomakaUJedCikl=postotakOdSirSlPomak*sir/100;
	    float sirUSec=(picPsecX+picPsecY)/2000;
	    float brPrikPoSec=listaSprite.get(i).brojStupaca(brSlikeHoda)*sirUSec/sirPomakaUJedCikl;
	    listaSprite.get(i).setBrojPrikazaPoSekundi(brSlikeHoda, brPrikPoSec);
	    
	    ////
	    ObjectPrikaza objPrik=new ObjectPrikaza(x,y,listaSprite.get(i),sir,vis,sir,vis,i);
	    Protivnik oIgre=new Protivnik(sir/2,vis/2,x,y,i,picPsecX,picPsecY,vrijednost);
	    oIgre.postaviDaSamRoditelj(-17, 5, 22);
	    oIgre.setPolozajNaCesti(polNaCesti);
	    objPrik.bilderDodajSpriteOnomatopeja(listaSprite.get(98));
	    oIgre.setHelthArmorTipnapada(listaSteteZaHelth.get(i),listaSteteZaArmor.get(i),1);
	    oIgre.setHelthArmor(listaHeltha.get(i), listaArmora.get(i));
	    oIgre.setUdaracaSvakihSekundi(koeficijentBrzineUdarca*1f);
	    objPrik.postaviRucnoAnimacijeIspucavanja(3, 0, 3,0,3, 0, 3, 0, 3, 0, 3,0, 3,0,3, 0);
		uiMan.dodajElementUListu(objPrik,2);
		oIgre.animacijaIzlaskaIzZemlje();
		gLog.dodajObjekt(oIgre);
		objPrik.bilderDoddajDodatniSpriteZaZajednickeEfekte(listaSprite.get(99));
		objPrik.setDvojnikaULogici(oIgre);
		oIgre.setDvojnikaUPrikazu(objPrik);
	
		this.brProtivnika--;
}


}
