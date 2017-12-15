package com.rugovit.igrica.engine.ui.elements;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.ui.Sjena;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.logic.elements.ToranjLAlkemicar;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;


public class ToranjPAlkemicar implements UIManagerObject {
	////animacija stvaranja
	private Paint paintStvaranja=new Paint();
	private float pocetniAlfa;
	private double pocetakStvaranja;
	private boolean tekPoceoStvaranje=true;
	private  float  duzinaStvaranja1, duzinaStvaranja2, duzinaStvaranja3, duzinaStvaranja4, duzinaStvaranja5, duzinaStvaranja6;
	private  float duzinaStvaranja=0.6f;
	private boolean animacijaStvaranjaGotova=false;
	private boolean zavrsioKockicu1=false,zavrsioKockicu2=false,zavrsioKockicu3=false,zavrsioKockicu4=false,zavrsioKockicu5=false,zavrsioKockicu6=false;
	private GameEvent geKosk1=new GameEvent(null);
	private GameEvent geKosk2=new GameEvent(null);
	private GameEvent geKosk3=new GameEvent(null);
	private GameEvent geKosk4=new GameEvent(null);
	private GameEvent geKosk5=new GameEvent(null);
	private GameEvent geKosk6=new GameEvent(null);
	
	private float udaljenostKock1;
	private float udaljenostKock2;
	private float udaljenostKock3;
	private float udaljenostKock4;
	private float udaljenostKock5;
	private float udaljenostKock6;
	
	private RectF recStvarCrt1=new RectF();
	private RectF recStvarCrt2=new RectF();
	private RectF recStvarCrt3=new RectF();
	private RectF recStvarCrt4=new RectF();
	private RectF recStvarCrt5=new RectF();
	private RectF recStvarCrt6=new RectF();
	
	private RectF recStvarSl1=new RectF();
	private RectF recStvarSl2=new RectF();
	private RectF recStvarSl3=new RectF();
	private RectF recStvarSl4=new RectF();
	private RectF recStvarSl5=new RectF();
	private RectF recStvarSl6=new RectF();
	//////////////////
	private float smanjenjeZatouch=0;
	private SpriteHendler spriteSaRankom;
	private RectF recRanka;
	private int brRanka;
	private float rankXOdLijevogRuba,  rankYOdVrha,  rankSir,  rankVis;
	private boolean crtajTeleport=false, crtajMedic=false;
	private boolean vrtiTeleport=false,vrtiMedic=false;
	private boolean pocmiTeleport=false;
	private GameEvent geDodatka;
	private RectF recDodatka;
	private int vrijemePauze;
	/////konstante
	private float visinaPlatformePosto=90;// na kojoj je visini platforma gdje su vojnici u odnosu na rectCrtanja
	///////////
	private GameEvent geSjena;
	private Sjena mojaSjena;
	///varijable freeez topa///
	private int brSlikeZaTop;
	private int brSlNapunjen=0,brSlPunjenja=0,brSlIspaljivanje=0;
	private boolean inicijaliziranTop=false;;
	private boolean postojiSlikaIspaljivanja=false;
	private boolean praznjenje,animPunjIliPra=true;
	private float xPozTopa,yPozTopa,xVelTopa,yVelTopa;
	private double prosloVrijTopa;
	////////////////////////////
	private Random generator;
	private boolean emItouched=false;
	private boolean boolZaSlanje=false;
	private boolean izbornikUpaljen=false;
	private float xTor,yTor;
	private int indikator;
	private float rTor;
	private float sirTor,visTor;
	private GameEvent ge;
	private Canvas can;
	private GameLogicObject dvojnik;
	private SpriteHendler spriteHend;
	private UIManager uiMan;
	private boolean jesamLiZiv=true;
	private boolean tekPoceo=true;
	private RectF rectCrtanja;
	private RectF rectTopa;
	private boolean ciljanje=false;//je zapravo punjenje
	private boolean napunjen=false;//ovog æepokreæati kada zavrsi punjenje
	private boolean ispaljivanje=false;// ispaljivanje je isto
	private boolean cekanje=false;// cekanje ce biti samoo nakon punjenja tako znaèi da se neæe upotrebljavati 
	  ////////varijable protibnika
	
	private float xProt=0;
	private float yProt=0;
	// vremena 
	private double zadVriTopa=0;
	private int ticTopaPunjenja=0;
	private int ticTopaNapunjen=0;
	private int ticTopaPraznjenja=0;
	///////////////////////
	///privremnei
	Paint p;
	Paint pK;
	public ToranjPAlkemicar(float xTor, float yTor,float rTor,float sirTor,float visTor,SpriteHendler spriteHend,int indikator ){
		this.rTor=2*rTor;
		this.yTor=yTor;
		this.xTor=xTor;
		this.visTor=visTor;
		this.sirTor=sirTor;
		this.indikator=indikator;
		this.spriteHend=spriteHend;
		this.uiMan=uiMan;
		///privremeni
		p=new Paint();
		p.setARGB(50,150,150,50);
		p.setStyle(Paint.Style.FILL);
		pK=new Paint();
		pK.setARGB(240,150,0,150);
		pK.setStyle(Paint.Style.FILL);
		rectCrtanja=new RectF();
		rectCrtanja.set(xTor, yTor, xTor+sirTor, yTor+visTor);
		generator=new Random();
		
		geSjena=new GameEvent(this);
	}
/////////////BILDER
	public void stvoriRank(int brRanka,SpriteHendler spriteSaRankom,  float xOdLijevogRuba, float yOdVrha, float sir, float vis ){
		this.brRanka=brRanka;
		this.spriteSaRankom=spriteSaRankom;
		this.rankSir=sir;
		this.rankVis=vis;
		this.rankXOdLijevogRuba=xOdLijevogRuba;
		this.rankYOdVrha=yOdVrha;
	}
	public void stvoriTop(int brSlUSpriteu, float xVel,float yVel, float pomakUOdnosuNaVrh,float pomakUOdnosuNaCentar){
		this.yVelTopa=yVel;
		this.xVelTopa=xVel;
		this.yPozTopa=this.rectCrtanja.top+pomakUOdnosuNaVrh;
		this.xPozTopa=this.rectCrtanja.centerX()-xVel/2+pomakUOdnosuNaCentar;
		this.rectTopa=new RectF();
		this.rectTopa.set(xPozTopa, yPozTopa,xPozTopa+xVelTopa, yPozTopa+yVelTopa);
		this.inicijaliziranTop=true;
		this.brSlikeZaTop=brSlUSpriteu;
		this.postojiSlikaIspaljivanja=this.spriteHend.postojiLiSlikNaMjestu(brSlikeZaTop);
	   
	}

	public void stvoriTelepoter(float xOdLijevogRuba,float yOdVrha, float sir, float vis){
		 this.geDodatka= new GameEvent(null);
   	  geDodatka.jesamLiZiv=true;
   	  this.crtajTeleport=true;
   	  recDodatka= new RectF();
   	 

   	  recDodatka.set(xTor+xOdLijevogRuba, yTor+yOdVrha,xTor+xOdLijevogRuba+sir, yTor+yOdVrha+vis);
	}
	public void stvoriMedic(float xOdLijevogRuba,float yOdVrha, float sir, float vis){
		 this.geDodatka= new GameEvent(null);
   	     geDodatka.jesamLiZiv=true;
   	     this.crtajMedic=true;
   	     recDodatka= new RectF();
  	 

  	      recDodatka.set(xTor+xOdLijevogRuba, yTor+yOdVrha,xTor+xOdLijevogRuba+sir, yTor+yOdVrha+vis);
	}
	/////////metode od interfacea object linker prikaz/////////
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		vrijemePauze=e.vrijemePauze;
		if(ge==null) ge=e;
		izbornikUpaljen=ge.izbornikUpaljen;
		if(!e.jesamLiZiv){
			jesamLiZiv=false;
		}
		if(ge.indikatorBorbe==1){
			ciljanje=true;
			cekanje=false;
			ispaljivanje=false;
			ge.indikatorBorbe=0;
		}
		if(ge.indikatorBorbe==3){// znaèi da nema nikoga
			cekanje=true;
			this.ciljanje=false;
			ispaljivanje=false;
			ge.indikatorBorbe=0; // vraca ga u pocetni polozaj
		}
		if(ge.indikatorBorbe==2){
			xProt=ge.pomNaX;
			yProt=ge.pomNaY;
			ToranjLAlkemicar d=(ToranjLAlkemicar)dvojnik;
	
			this.ciljanje=false;
			ispaljivanje=true;
			cekanje=false;
			ge.indikatorBorbe=0; // vraca ga u pocetni polozaj
			
			praznjenje=true;
            animPunjIliPra=true;
		}
		if(this.geSjena!=null){
			
			geSjena.jesamLiZiv=ge.jesamLiZiv;
			if(this.izbornikUpaljen){
			   this.geSjena.indikator=2;
		      }
	    	else{
			   this.geSjena.indikator=0;
		    }
			if(this.mojaSjena!=null)this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
	     }
		
		
	     this.vrtiMedic=ge.medic;
	     
	     if(ge.teleportacija&&!vrtiTeleport){
				this.pocmiTeleport=true;	
			}
		     this.vrtiTeleport=ge.teleportacija;
		     
		xProt=ge.pomNaX;
		yProt=ge.pomNaY;
	}
public RectF getRectTopa(){
	return this.rectTopa;
}
/////////////////////////////////////////////////////	
private boolean animirajStvaranje(){
	boolean b=false;
	if(this.tekPoceoStvaranje){
		pocetakStvaranja=java.lang.System.currentTimeMillis();
		pocetniAlfa=80;
		paintStvaranja.setAlpha((int)pocetniAlfa);
		
		
		this.recStvarSl1.set(0, 0, 34, 50);
		this.recStvarSl2.set(34, 0, 68, 50);
		this.recStvarSl3.set(68,0, 100, 50);
		
		this.recStvarSl4.set(0, 50 ,34, 100);
		this.recStvarSl5.set(34, 50, 68, 100);
		this.recStvarSl6.set(68,50, 100, 100);
	      geKosk1.jesamLiZiv=true;
	      geKosk2.jesamLiZiv=true;
	      geKosk3.jesamLiZiv=true;
	      geKosk4.jesamLiZiv=true;
	      geKosk5.jesamLiZiv=true;
	      geKosk6.jesamLiZiv=true;
		///rubovi idu ukoso, djelim gibanje ena x i y , jednakostranični trokut samo množim sa 0.707
			 udaljenostKock1=0.707f*this.randIzmeduCijeli((int)(rectCrtanja.width()/2), 3*(int)rectCrtanja.width()/2);
		     udaljenostKock2=this.randIzmeduCijeli((int)(rectCrtanja.width()/2), 3*(int)rectCrtanja.width()/2);
			 udaljenostKock3=0.707f*this.randIzmeduCijeli((int)(rectCrtanja.width()/2), 3*(int)rectCrtanja.width()/2);
		     udaljenostKock4=0.707f*this.randIzmeduCijeli((int)(rectCrtanja.width()/2), 3*(int)rectCrtanja.width()/2);
			 udaljenostKock5=this.randIzmeduCijeli((int)(rectCrtanja.width()/2),3* (int)rectCrtanja.width()/2);
			 udaljenostKock6=0.707f*this.randIzmeduCijeli((int)(rectCrtanja.width()/2),3* (int)rectCrtanja.width()/2);
		
		 float dodatak=randIzmeduCijeli(0,(int)(100*3*duzinaStvaranja/4));
		 dodatak=dodatak/100;
		 duzinaStvaranja1=this.duzinaStvaranja+ dodatak;
		 
		 dodatak=randIzmeduCijeli(0,(int)(100*3*duzinaStvaranja/4));
		 dodatak=dodatak/100;
		 duzinaStvaranja2=this.duzinaStvaranja+ dodatak;
		 
		 dodatak=randIzmeduCijeli(0,(int)(100*3*duzinaStvaranja/4));
		 dodatak=dodatak/100;
		 duzinaStvaranja3=this.duzinaStvaranja+ dodatak;
		 
		 dodatak=randIzmeduCijeli(0,(int)(100*3*duzinaStvaranja/4));
		 if(generator.nextBoolean()){
			 dodatak=- dodatak;
		 }
		 dodatak=dodatak/100;
		 duzinaStvaranja4=this.duzinaStvaranja+ dodatak;
		 
		 dodatak=randIzmeduCijeli(0,(int)(100*3*duzinaStvaranja/4));
		 if(generator.nextBoolean()){
			 dodatak=- dodatak;
		 }
		 dodatak=dodatak/100;
		 duzinaStvaranja5=this.duzinaStvaranja+ dodatak;
		 
		 dodatak=randIzmeduCijeli(0,(int)(100*3*duzinaStvaranja/4));
		 if(generator.nextBoolean()){
			 dodatak=- dodatak;
		 }
		 dodatak=dodatak/100;
		 duzinaStvaranja6=this.duzinaStvaranja+ dodatak;
		 
		    recStvarCrt1.set(this.rectCrtanja.left+recStvarSl1.left*this.rectCrtanja.width()/100,this.rectCrtanja.top+recStvarSl1.top*this.rectCrtanja.height()/100,this.rectCrtanja.left+recStvarSl1.right*this.rectCrtanja.width()/100, this.rectCrtanja.top+recStvarSl1.bottom*this.rectCrtanja.height()/100);
		    recStvarCrt2.set(this.rectCrtanja.left+recStvarSl2.left*this.rectCrtanja.width()/100,this.rectCrtanja.top+recStvarSl2.top*this.rectCrtanja.height()/100,this.rectCrtanja.left+recStvarSl2.right*this.rectCrtanja.width()/100, this.rectCrtanja.top+recStvarSl2.bottom*this.rectCrtanja.height()/100);
		    recStvarCrt3.set(this.rectCrtanja.left+recStvarSl3.left*this.rectCrtanja.width()/100,this.rectCrtanja.top+recStvarSl3.top*this.rectCrtanja.height()/100,this.rectCrtanja.left+recStvarSl3.right*this.rectCrtanja.width()/100, this.rectCrtanja.top+recStvarSl3.bottom*this.rectCrtanja.height()/100);
		    recStvarCrt4.set(this.rectCrtanja.left+recStvarSl4.left*this.rectCrtanja.width()/100,this.rectCrtanja.top+recStvarSl4.top*this.rectCrtanja.height()/100,this.rectCrtanja.left+recStvarSl4.right*this.rectCrtanja.width()/100, this.rectCrtanja.top+recStvarSl4.bottom*this.rectCrtanja.height()/100);
		    recStvarCrt5.set(this.rectCrtanja.left+recStvarSl5.left*this.rectCrtanja.width()/100,this.rectCrtanja.top+recStvarSl5.top*this.rectCrtanja.height()/100,this.rectCrtanja.left+recStvarSl5.right*this.rectCrtanja.width()/100, this.rectCrtanja.top+recStvarSl5.bottom*this.rectCrtanja.height()/100);
		    recStvarCrt6.set(this.rectCrtanja.left+recStvarSl6.left*this.rectCrtanja.width()/100,this.rectCrtanja.top+recStvarSl6.top*this.rectCrtanja.height()/100,this.rectCrtanja.left+recStvarSl6.right*this.rectCrtanja.width()/100, this.rectCrtanja.top+recStvarSl6.bottom*this.rectCrtanja.height()/100);
		    
			  recStvarCrt1.set( recStvarCrt1.left- udaljenostKock1,  recStvarCrt1.top- udaljenostKock1,  recStvarCrt1.right- udaljenostKock1, recStvarCrt1. bottom- udaljenostKock1);
				recStvarCrt2.set( recStvarCrt2.left,  recStvarCrt2.top- udaljenostKock2,  recStvarCrt2.right, recStvarCrt2. bottom- udaljenostKock2);
				recStvarCrt3.set( recStvarCrt3.left+ udaljenostKock3,  recStvarCrt3.top- udaljenostKock3,  recStvarCrt3.right+ udaljenostKock3, recStvarCrt3. bottom- udaljenostKock3);
				recStvarCrt4.set( recStvarCrt4.left- udaljenostKock4,  recStvarCrt4.top+udaljenostKock4,  recStvarCrt4.right- udaljenostKock4, recStvarCrt4. bottom+ udaljenostKock4);
				recStvarCrt5.set( recStvarCrt5.left,  recStvarCrt5.top+ udaljenostKock5,  recStvarCrt5.right, recStvarCrt5. bottom+ udaljenostKock5);
				recStvarCrt6.set( recStvarCrt6.left+ udaljenostKock6,  recStvarCrt6.top+udaljenostKock6,  recStvarCrt6.right+ udaljenostKock6, recStvarCrt6. bottom+udaljenostKock6);
		
		this.tekPoceoStvaranje=false;
	}
	
	this.pocetakStvaranja+=this.ge.vrijemePauze;
	float alfa=0;
	int razlikaVremena=(int)((this.duzinaStvaranja1*1000)-(java.lang.System.currentTimeMillis()-this.pocetakStvaranja));
	razlikaVremena=(int)(razlikaVremena/(duzinaStvaranja1*1000/100));
	if(razlikaVremena<0){
		alfa=255;
	}
	else{
	alfa=(100-razlikaVremena)*(255-this.pocetniAlfa)/ 100;
	alfa=this.pocetniAlfa+alfa;
	}
	paintStvaranja.setAlpha((int)alfa);
	///
	if(!this.zavrsioKockicu1){
		zavrsioKockicu1= SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geKosk1, udaljenostKock1, udaljenostKock1,duzinaStvaranja1, duzinaStvaranja1, recStvarCrt1);
		        this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl1, recStvarCrt1, 0, 0,0,paintStvaranja);
		        if(this.zavrsioKockicu1){
		        	this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukToranjKonstrukcija1, 70,100, 0, 70);
		        }
	}
	else {
	    this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl1, recStvarCrt1, 0, 0,0,paintStvaranja);
	}
	///
	razlikaVremena=(int)((this.duzinaStvaranja2*1000)-(java.lang.System.currentTimeMillis()-this.pocetakStvaranja));
	razlikaVremena=(int)(razlikaVremena/(duzinaStvaranja2*1000/100));
	if(razlikaVremena<0){
		alfa=255;
	}
	else{
	alfa=(100-razlikaVremena)*(255-this.pocetniAlfa)/ 100;
	alfa=this.pocetniAlfa+alfa;
	}
	paintStvaranja.setAlpha((int)alfa);
	if(!this.zavrsioKockicu2){
		zavrsioKockicu2= SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geKosk2, 0, udaljenostKock2,duzinaStvaranja2, duzinaStvaranja2, recStvarCrt2);
		        this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl2, recStvarCrt2, 0, 0,0,paintStvaranja);
		        if(this.zavrsioKockicu2){
		        	this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukToranjKonstrukcija2, 70,100, 0, 70);
		        }
	}
	else {
	    this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl2, recStvarCrt2, 0, 0,0,paintStvaranja);
	}
	//////////////
	razlikaVremena=(int)((this.duzinaStvaranja3*1000)-(java.lang.System.currentTimeMillis()-this.pocetakStvaranja));
	razlikaVremena=(int)(razlikaVremena/(duzinaStvaranja3*1000/100));
	if(razlikaVremena<0){
		alfa=255;
	}
	else{
	alfa=(100-razlikaVremena)*(255-this.pocetniAlfa)/ 100;
	alfa=this.pocetniAlfa+alfa;
	}
	paintStvaranja.setAlpha((int)alfa);
	if(!this.zavrsioKockicu3){
		zavrsioKockicu3= SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geKosk3,- udaljenostKock3, +udaljenostKock3,duzinaStvaranja3, duzinaStvaranja3, recStvarCrt3);
		        this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl3, recStvarCrt3, 0, 0,0,paintStvaranja);
		        if(this.zavrsioKockicu3){
		        	this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukToranjKonstrukcija3, 70,100, 0, 70);
		        }
	}
	else {
	    this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl3, recStvarCrt3, 0, 0,0,paintStvaranja);
	}
	/////////////
	razlikaVremena=(int)((this.duzinaStvaranja4*1000)-(java.lang.System.currentTimeMillis()-this.pocetakStvaranja));
	razlikaVremena=(int)(razlikaVremena/(duzinaStvaranja4*1000/100));
	if(razlikaVremena<0){
		alfa=255;
	}
	else{
	alfa=(100-razlikaVremena)*(255-this.pocetniAlfa)/ 100;
	alfa=this.pocetniAlfa+alfa;
	}
	paintStvaranja.setAlpha((int)alfa);
	if(!this.zavrsioKockicu4){
		zavrsioKockicu4= SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geKosk4, udaljenostKock4, -udaljenostKock4,duzinaStvaranja4, duzinaStvaranja4, recStvarCrt4);
		        this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl4, recStvarCrt4, 0, 0,0,paintStvaranja);
		        if(this.zavrsioKockicu4){
		        	this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukToranjKonstrukcija4,70,100, 0, 70);
		        }
	}
	else {
	    this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl4, recStvarCrt4, 0, 0,0,paintStvaranja);
	}
	//////////////
	razlikaVremena=(int)((this.duzinaStvaranja5*1000)-(java.lang.System.currentTimeMillis()-this.pocetakStvaranja));
	razlikaVremena=(int)(razlikaVremena/(duzinaStvaranja5*1000/100));
	if(razlikaVremena<0){
		alfa=255;
	}
	else{
	alfa=(100-razlikaVremena)*(255-this.pocetniAlfa)/ 100;
	alfa=this.pocetniAlfa+alfa;
	}
	paintStvaranja.setAlpha((int)alfa);
	if(!this.zavrsioKockicu5){
		zavrsioKockicu5= SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geKosk5, 0, -udaljenostKock5,duzinaStvaranja5, duzinaStvaranja5, recStvarCrt5);
		        this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl5, recStvarCrt5, 0, 0,0,paintStvaranja);
		        if(this.zavrsioKockicu5){
		        	this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukToranjKonstrukcija5, 70,100, 0, 70);
		        }
	}
	else {
	    this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl5, recStvarCrt5, 0, 0,0,paintStvaranja);
	}
	/////////////
	razlikaVremena=(int)((this.duzinaStvaranja6*1000)-(java.lang.System.currentTimeMillis()-this.pocetakStvaranja));
	razlikaVremena=(int)(razlikaVremena/(duzinaStvaranja6*1000/100));
	if(razlikaVremena<0){
		alfa=255;
	}
	else{
	alfa=(100-razlikaVremena)*(255-this.pocetniAlfa)/ 100;
	alfa=this.pocetniAlfa+alfa;
	}
	paintStvaranja.setAlpha((int)alfa);
	if(!this.zavrsioKockicu6){
		zavrsioKockicu6= SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geKosk6, -udaljenostKock6, -udaljenostKock6,duzinaStvaranja6, duzinaStvaranja6, recStvarCrt6);
		        this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl6, recStvarCrt6, 0, 0,0,paintStvaranja);
		        if(this.zavrsioKockicu6){
		        	this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukToranjKonstrukcija6, 70,100, 0, 70);
		        }
	}
	else {
	    this.spriteHend.nacrtjDioBitmapa(can,0, recStvarSl6, recStvarCrt6, 0, 0,0,paintStvaranja);
	}
	/////////////
	if(zavrsioKockicu1&&zavrsioKockicu2&&zavrsioKockicu3&&zavrsioKockicu4&&zavrsioKockicu5&&zavrsioKockicu6){
		b=true;
		ge.bool1=true;
	}
	return b;
} 
private void stvariKojeSeIzvrsavajuSamoNaPocetku(){
	        
	
	       rectCrtanja.set(xTor, yTor, xTor+sirTor, yTor+visTor);
	       
	       this.mojaSjena=new Sjena(dvojnik.getRect().height()*1.2f,dvojnik.getRect().height(),20,43, 59, 233);
	       this.geSjena.x=this.dvojnik.getRect().centerX()-this.dvojnik.getRect().width()*1.2f/2;
			this.geSjena.y=this.dvojnik.getRect().centerY()-dvojnik.getRect().height()/2;
			
			uiMan.dodajElementUListu(mojaSjena, 1); 
			/*  if(this.indikator==178){// ako je teleport
	        	  this.geDodatka= new GameEvent(null);
	        	  geDodatka.jesamLiZiv=true;
	        	  this.crtajTeleport=true;
	        	  recDodatka= new RectF();
	        	  float vis=68.66f*rectCrtanja.height()/100;
	        	  float sir=51f*rectCrtanja.width()/100;
	        	  
	        	  float yDod=3.29f*rectCrtanja.height()/100;
	        	  float xDod=81.22f*rectCrtanja.width()/100;
	        	  
	        	  recDodatka.set(rectCrtanja.left+xDod, rectCrtanja.top+yDod,rectCrtanja.left+xDod+sir, rectCrtanja.top+yDod+vis);
	          }
	          if(this.indikator==179){// ako je medivc
	        	  this.geDodatka= new GameEvent(null);
	        	  geDodatka.jesamLiZiv=true;
	        	  this.crtajMedic=true;
	        	  recDodatka= new RectF();
	        	  float vis=38.1f*rectCrtanja.height()/100;
	        	  float sir=100f*rectCrtanja.width()/100;
	        	  
	        	  float yDod=15.16f*rectCrtanja.height()/100;
	        	  float xDod=11f*rectCrtanja.width()/100;
	        	  
	        	  recDodatka.set(rectCrtanja.left+xDod, rectCrtanja.top+yDod,rectCrtanja.left+xDod+sir, rectCrtanja.top+yDod+vis);
	          }
	        */
			
			
			
			this.recRanka=new RectF();
			
			this.recRanka.set(rectCrtanja.left+this.rankXOdLijevogRuba, rectCrtanja.top+this.rankYOdVrha,
					rectCrtanja.left+this.rankXOdLijevogRuba+this.rankSir, rectCrtanja.top+this.rankYOdVrha+this.rankVis);
			this.smanjenjeZatouch=this.uiMan.GL.faIgr.smanjenjeTouchVisineZaTornjeve*this.visTor/100;
	}
private void nacrtajRank(){
	RectF temprec=new RectF();
for(int i=0;i<this.brRanka; i++){
	temprec.set(recRanka.left, recRanka.top-this.recRanka.height()*i, recRanka.right, recRanka.bottom-this.recRanka.height()*i);
	
	this.spriteSaRankom.nacrtajSprite(can, 1, 0, 0,temprec);
}
}
private void nacrtajToranj(){
       spriteHend.nacrtajSprite(can, 0, 0, 0, rectCrtanja);
	        }
private void ubijMe(){
		 uiMan.makniObjekt(this);// skida sa liste u UI manageru
	}
private void posaljiPorukuLogici888(){
	if(boolZaSlanje&&ge!=null){// kad se prvi put pokrene možda neæe biti spremljen u ovu referencu game event
		 ge.imTouched= emItouched;
	     dvojnik.GameLinkerIzvrsitelj(ge);
	     boolZaSlanje=false;
	}
}
private int randIzmeduCijeli(int a, int b){ // 
	if(b==0) b=1;
	int r= generator.nextInt((int)Math.abs(b+1));
	return r+a;
 }
///////NACRTAJ
@Override
public void nacrtaj(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY) {
	// TODO Auto-generated method stub
	this.can=can;
	this.uiMan=uiMan;
	if(tekPoceo){
		stvariKojeSeIzvrsavajuSamoNaPocetku();
		tekPoceo=false;
	}  
	if(!this.animacijaStvaranjaGotova){
		animacijaStvaranjaGotova=animirajStvaranje();
	}
	else {
	nacrtajToranj();
	if(this.inicijaliziranTop) crtanjeFrezzTopa();
	crtajDodatak();
	posaljiPorukuLogici888();// stavio sam da svaki šalje poruku svom parnjaku u logici, inaèe je bilo  u metodama (imtouched...)koje zapravo poziva thread od toucha 
	nacrtajRank();
	}
	if(jesamLiZiv==false) ubijMe();
}

private void crtajDodatak(){
	if(this.crtajTeleport){
		if(vrtiTeleport) {
            if(pocmiTeleport){
				this.geDodatka.jesamLiZiv=true;
			}
            pocmiTeleport=this.spriteHend.vrtiAnimacijuReda(can,this.geDodatka, 2, 0,recDodatka, null);
			ge.teleportacija=this.vrtiTeleport=!pocmiTeleport;
			
		}
		else spriteHend.nacrtajSprite(can, 2, 0, 0, recDodatka);
	}
	else if(this.crtajMedic){
		if(vrtiMedic){
		
	
			this.spriteHend.vrtiAnimacijuReda(can,this.geDodatka, 2, 0,recDodatka, null);
		
			
		}
		else spriteHend.nacrtajSprite(can, 2, 0, 0, recDodatka);
	}
}

private void crtanjeFrezzTopa(){
	if(postojiSlikaIspaljivanja){
	
		if(praznjenje){
		if(System.currentTimeMillis()>1000/spriteHend.brojPrikazaPoSekundi(brSlikeZaTop)+this.prosloVrijTopa+this.vrijemePauze){
		   
		  
			spriteHend.nacrtajSprite(can,brSlikeZaTop, brSlIspaljivanje, 2,rectTopa);
			  brSlIspaljivanje++;
		  
		  
		  
			  prosloVrijTopa=System.currentTimeMillis();
			if(this.brSlIspaljivanje>=spriteHend.brojStupaca(brSlikeZaTop)){
               
                brSlIspaljivanje=0;
                brSlPunjenja=0;
                praznjenje=false;
                animPunjIliPra=true;
     }
		}
		else   spriteHend.nacrtajSprite(can,brSlikeZaTop, brSlIspaljivanje,2,rectTopa);

	 }
		else if(animPunjIliPra){
			if(System.currentTimeMillis()>1000/spriteHend.brojPrikazaPoSekundi(brSlikeZaTop)+prosloVrijTopa+this.vrijemePauze){
				spriteHend.nacrtajSprite(can,brSlikeZaTop, brSlPunjenja, 0,rectTopa);  
			    brSlPunjenja++;
			    prosloVrijTopa=System.currentTimeMillis();
				 if(this.brSlPunjenja>=spriteHend.brojStupaca(brSlikeZaTop)){
					 brSlIspaljivanje=0;
	                brSlPunjenja=0;
	                animPunjIliPra=false;
	     
				 }
			}
			else{
				spriteHend.nacrtajSprite(can,brSlikeZaTop, brSlPunjenja, 0,rectTopa);
			}
		}
		else{
			if(System.currentTimeMillis()>1000/spriteHend.brojPrikazaPoSekundi(brSlikeZaTop)+prosloVrijTopa+this.vrijemePauze){
			   
			   spriteHend.nacrtajSprite(can,brSlikeZaTop, brSlNapunjen, 1,rectTopa);
			   brSlNapunjen++;
			   prosloVrijTopa=System.currentTimeMillis();
				if(this.brSlNapunjen>=spriteHend.brojStupaca(brSlikeZaTop)){
	               // ispaljivanje=false;
	                brSlNapunjen=0;
	     }
			}
			else   spriteHend.nacrtajSprite(can,brSlikeZaTop, brSlNapunjen,1,rectTopa);
		}
   }
	else praznjenje=false;	
}
//////////metode od interfacea ui manager object////
@Override
public void setImTouched(boolean b) {
	// TODO Auto-generated method stub
	 emItouched=b;
	 boolZaSlanje=true;
}

@Override
public void setTouchedObject(UIManagerObject obj) {
	// TODO Auto-generated method stub
	
}

@Override
public void setTouchedXY(float x, float y) {
	// TODO Auto-generated method stub
	if(ge!=null){
		ge.pomNaX=x;
		ge.pomNaY=y;/// koristim pomak na metodu iako nije najbollje ime ona korisit baš za slanje na koju je toèku kliknuto
	    dvojnik.GameLinkerIzvrsitelj(ge);
	    boolZaSlanje=true;
	    }
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
	return yTor+smanjenjeZatouch;
}


@Override
public float getSir() {
	// TODO Auto-generated method stub
	return sirTor;
}

@Override
public float getVis() {
	// TODO Auto-generated method stub
	return visTor-smanjenjeZatouch;
}  ///ovdje završavaju metode od ui manager object
///////////////////////////////////////////////////////////
@Override
public void setDvojnikaULogici(GameLogicObject obj) {
	// TODO Auto-generated method stub
	dvojnik=obj;
}

@Override
public RectF getGlavniRectPrikaza() {
	RectF rect=new RectF();
	rect.set(this.rectCrtanja);
	return rect;
}
@Override
public void onSystemResume() {
	// TODO Auto-generated method stub
	spriteHend=this.uiMan.GL.faIgr.getSprite(this.indikator);
}
@Override
public boolean getDaliDaIgnoriramTouch() {
	// TODO Auto-generated method stub
	return false;
}
}
