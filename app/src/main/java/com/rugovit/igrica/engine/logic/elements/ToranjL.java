package com.rugovit.igrica.engine.logic.elements;

import java.util.LinkedList;

import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.IzbornikZaToranj;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class ToranjL implements GameLogicObject{
	
	private float novacZaProdaju=0;
	
	private float velXUPrik=0;
	private float velYUPrik=0;
	private double prosloVrijeme;
	/////(/( varijable tornja sa projektilima
	private ProjektilL projektil1;
	private ProjektilL projektil2;
	private GameLogicObject tempProtivnik;
	private GameLogicProtivnik tempStari=null;
	private GameLogicProtivnik tempNovi=null;
	//////// varijable od tornja kasarne
	private float xOkupljanja,yOkupljanja;
	private double startTime=0;
	private float secDoNovogVojnika=4;// sekunde do kada æe se gennerirati novi vojnik ako fali koji defolt 10
	private int maxBrVojnika=4;// maksimalni broj vojnika, defolt 4
	private LinkedList<GameLogicBranitelj> listaGrupe;// svaki æe toranj  održavat listu grupe za svoje objekte
	///////////////////
	///////izbornik varijable
	private IzbornikZaToranj izbor;
	protected float cijenaBotun1=-1,cijenaBotun2=-1,cijenaBotun3=-1,cijenaBotun4=-1;// -1 znaèi da se ne korristi taj botun
	//////////////////////////
	////zastavice
	private boolean izborNaMeni=false;
	private boolean tekPoceo=true;
	private boolean pocObukeNovogVojnika=false;
	///////
    protected GameEvent ge;
    private UIManagerObject dvojnik;
    private GameLogic GL;
	private FazeIgre FI;
	private SpriteHendler sprHendZaProjekt;
	private GameLogicObject[] objPoPravcima; 
	private GameLogicObject maxDolje, maxGore,maxDesno,maxLijevo;
	private int indikator,rateOfFire;
	private float xTor,yTor, radijus;
	//private float rTor;
	private float xGrup,yGrup;
	private RectF rec;
	private Math mat;
	private int TpS=10;// ticks per sec, broj poziva ove klase po sekundi definiro sam ga da je u startu 10 ali to se može promjeniti pri pozivu malog runa
	private int ticFire,iFire=0;// sprema koliko puta se treba pozvati ova klasa prije nego se ispali projektill
	private float xKraj,yKraj; // x i y kraja kojeg se brani
	private GameLogicObject[] listaSudara;// spremaju se objekti sa kojima se sudara
	private boolean jesamLiZiv=true;
	public ToranjL(int indikator,IzbornikZaToranj izbor,float xTor, float yTor,float radijus){
		this.indikator=indikator;
		this.yTor=yTor;
		this.xTor=xTor;
		this.izbor=izbor;
		this.radijus=radijus;
	}
	public ToranjL(float xTor, float yTor,float radijus,int RofF,IzbornikZaToranj izbor,int indikator){
		this.yTor=yTor;
		this.xTor=xTor;
		this.indikator=indikator;
		this.radijus=radijus;
		this.rateOfFire=RofF;
		this.izbor=izbor;
		listaGrupe=new LinkedList<GameLogicBranitelj>();
		ge=new GameEvent(this);
		objPoPravcima=new GameLogicObject[4];
		rec=new RectF(xTor,yTor,radijus*2+xTor,radijus*2+yTor);// treba mi rect zbog toga što ga zahtjevaju drugi djelovi igrice
		ticFire=1;// ovdje odreðujemm defoltni 
	}

///////////PUBLIC METODE////////////////////////////////////////////  
	public void uhvatiPozicijuKlikaIzvanIzbornika(float x, float y){
		
	}
	public float getCijenuBotuna(int brBotuna){
		float novac=0;
		if(brBotuna==1) novac=this.cijenaBotun1;
		else if(brBotuna==2) novac=this.cijenaBotun2;
		else if(brBotuna==3) novac=this.cijenaBotun3;
		else if(brBotuna==4) novac=this.cijenaBotun4;
		return novac;
	}
	public void namjestiNovacZaProdaju(float novac){
		novacZaProdaju+=novac;
	}
	public float getNovacZaProdaju(){
		return novacZaProdaju;
	}
////////child za metode
	     public boolean jesamLiZiv(){
	    	 return jesamLiZiv;
	     }
         public void updatajIzbornik(){// sluzi za child da moze pokrenuti izbornik
	      dodajCijenuBotunima();
	      izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
         }
         public void dodajReference(GameLogic GL,FazeIgre FI){// prima game logic referencu od childa
	      this.GL=GL;
	      this.FI=FI;
         }	 
         public void namjestiOkupljaliste(float x, float y){// samo da se moze overridati u childu
        	 
         }	
    ///////////
 public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){
	 
 }
 public void setXVelUPrik(float xp){
	 velXUPrik=xp;
 }
 public void setYVelUPrik(float yp){
	 velYUPrik=yp;
 }	
 public void izbornikNaMeni(boolean b){
	 izborNaMeni=b;
	
 }	
 public void vratiTouchPolje(int polje){// vraæa polje na koje je kliknuto
/////////////TORANJ EMBRIO
	 if(indikator==200){// ako je ova klasa toranj embrio
		 if( GL.getUkupniNovac()>=FI.cijenaObjecta(125)&&polje==1){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
		      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(125));	 
		      FI.br125ToranjMinobacac(rec.centerX(),rec.centerY());
		      ubijMe();
	     }
	     else if( GL.getUkupniNovac()>=FI.cijenaObjecta(150)&&polje==2){//TORANJ KASARNA dugmiæ 2
			           GL.dodajNovacPlusMinus(-FI.cijenaObjecta(150));	 
			           FI.br150ToranjKasarna(rec.centerX(),rec.centerY());
			           ubijMe();
		        }
	    else if( GL.getUkupniNovac()>=FI.cijenaObjecta(101)&&polje==3){//TORANJ  strijelci dugmiæ 3
		 GL.dodajNovacPlusMinus(-FI.cijenaObjecta(101));	 
		 FI.br101ToranjStrijelci(this.xTor+this.radijus,this.yTor+this.radijus);
		 ubijMe();
	     }
	    else if( GL.getUkupniNovac()>=FI.cijenaObjecta(175)&&polje==4){//TORANJ  alkemicar dugmiæ 4
			 GL.dodajNovacPlusMinus(-FI.cijenaObjecta(175));	 
			 FI.br175ToranjAlkemicar1Razina(this.xTor+this.radijus,this.yTor+this.radijus);
			 ubijMe();
	    }
	 }

 }	
 public void setRateOfFire(int rof){
	 rateOfFire=rof;
	 ticFire=TpS/rof;
 }
//////////////////////////////////////////////////////////////////////	
///////privatne metode///////////////////////////////////////7//

 public void ubijMe(){
	  ge.jesamLiZiv=false;
	  dvojnik.GameLinkerIzvrsitelj(ge);
	  GL.mrtavSam(this);
 }
 private  LinkedList<Integer>  izborStanjeNekoristenihBotuna(){
	 LinkedList<Integer>  listaNekoristenih=new  LinkedList<Integer>();
	 float i =GL.getUkupniNovac();
	 if(GL.getUkupniNovac()<cijenaBotun1||cijenaBotun1==-1) {
		                                listaNekoristenih.add(1); 
	                                      }
	 if(GL.getUkupniNovac()<cijenaBotun2||cijenaBotun2==-1)  {
		                                listaNekoristenih.add(2); 
	                                     }
	 if(GL.getUkupniNovac()<cijenaBotun3||cijenaBotun3==-1){
		                                listaNekoristenih.add(3); 
	                                     }
	 if(GL.getUkupniNovac()<cijenaBotun4||cijenaBotun4==-1){
		                                listaNekoristenih.add(4); 
	                                    }
	 return listaNekoristenih;
 }
 private void dodajCijenuBotunima(){
	if(indikator==200){ cijenaBotun1=FI.cijenaObjecta(125);
	                    cijenaBotun2=FI.cijenaObjecta(150);
	                    cijenaBotun3=FI.cijenaObjecta(101);
	                    cijenaBotun4=FI.cijenaObjecta(175);
	                   }

 }
 private void ispucajProjektil(){
	 projektil1.pokreniSePremaCilju( tempProtivnik.getRect().centerX(),  tempProtivnik.getRect().centerY());
 }
 private void stvariKOjeSeIzvrsavajuSamoJedanput(){// samo æe se izvršit na pocetku ova metoda
	 dodajCijenuBotunima();
 }

    private void posaljiPorukuPrikazu(){
    	dvojnik.GameLinkerIzvrsitelj(ge);
    	 ge.izbornikUpaljen=izborNaMeni;
    }
/////////////////////////////////////////////////////////////////	
///// metode od interfejsa gamelogicobject//////////////////////
	public float getXVelUPrikazu(){
		return dvojnik.getGlavniRectPrikaza().width();
	}
	public float getYVelUPrikazu(){
		return dvojnik.getGlavniRectPrikaza().height();
	}
	public int getOblZaKol(){
		return 4;// znaèi da je krug, presjek mi je sirina rec-a
	}
	@Override
	public int getIndikator() {
		// TODO Auto-generated method stub
		return indikator;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return xTor;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return yTor;
	}

	@Override
	public float getDx() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RectF getRect() {
		// TODO Auto-generated method stub
		return rec;
	}
	@Override
	public void maliRun(GameLogic GL,FazeIgre FI, float TpS,boolean pause) {
		// TODO Auto-generated method stub
		izborNaMeni=izbor.izbornikNaMeni(this);
		this.FI=FI;
		this.GL=GL;
		if(tekPoceo){// 
			stvariKOjeSeIzvrsavajuSamoJedanput();
			tekPoceo=false;
		}
		if(izborNaMeni)izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
		posaljiPorukuPrikazu();
	}
/////////////////////////////////////////////////////////	
////////metode od interfacea gamelinkerologic////////////
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		if(e.imTouched){
			izbor.pokreniMojIzbornik(this,izborStanjeNekoristenihBotuna());// ako detektira da je kliknuto na njega pokreæe izbornik
		    
		}
	}
////////////////////////////////////////////////////////
	@Override
	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
		dvojnik=obj;
	}
	@Override
	public float getXPozUPrik() {
		// TODO Auto-generated method stub
		return dvojnik.getGlavniRectPrikaza().left;
	}
	@Override
	public float getYPozUPrik() {
		// TODO Auto-generated method stub
		return dvojnik.getGlavniRectPrikaza().top;
	}


}
