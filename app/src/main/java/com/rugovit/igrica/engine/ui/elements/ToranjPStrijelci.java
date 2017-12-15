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
import com.rugovit.igrica.engine.logic.elements.ToranjLStrijelci;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;


public class ToranjPStrijelci implements UIManagerObject {
	////animacija stvaranja
	private Paint paintStvaranja=new Paint();
	private float pocetniAlfa;
	private double pocetakStvaranja;
	private boolean tekPoceoStvaranje=true;
	private  float duzinaStvaranja=0.6f;
	private  float  duzinaStvaranja1, duzinaStvaranja2, duzinaStvaranja3, duzinaStvaranja4, duzinaStvaranja5, duzinaStvaranja6;
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
	private GameEvent geSjena;
	private Sjena mojaSjena;
	private boolean crtajVatreni=false, crtajOtrovni=false;
	/////konstante
	private float visinaPlatformePosto=85;// na kojoj je visini platforma gdje su vojnici u odnosu na rectCrtanja
	///////////
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
	//////// varijable vremena
    private double poceoCekati=0;// vrijeme kada je poceo sa cekanjem
	////////varijable vojnika
	private boolean ciljanje=false;
	private boolean ispaljivanje=false;
	private boolean cekanje=false;
	private int brVojnika=1;
	private float xVoj1=0;
	private float yVoj1=0;
	private float xVoj2=0;
	private float yVoj2=0;
	private float xVoj3=0;
	private float yVoj3=0;
    private int ticVoj1=2;// za animaciju ispaljivanja, pocimaju od druge slicice
    private int ticVoj2=2;
    private int ticVoj3=2;
    private double vrijVoj1=0;
    private double vrijVoj2=0;
    private double vrijVoj3=0;
	private RectF recVoj;
	private int ispaljivanjeVojnikaBr=1;//
    ////////varijable protibnika
	private float xIspa=0;
	private float yIspa=0;
	private float xProt=0;
	private float yProt=0;
	private int smijerProt=0;// kut u radijanima
	private GameEvent geDod1,geDod2;
	////////////////////////////
	int testTic=0;
	///////////////////////
	private RectF recDodZastava,recDodNaVrhu1,recDodNaVrhu2;
	public ToranjPStrijelci(float xTor, float yTor,float rTor,float sirTor,float visTor,SpriteHendler spriteHend,int indikator ){
		this.rTor=2*rTor;
		this.yTor=yTor;
		this.xTor=xTor;
		this.visTor=visTor;
		this.sirTor=sirTor;
		this.indikator=indikator;
		this.spriteHend=spriteHend;
		rectCrtanja=new RectF();
		recVoj=new RectF();
		generator=new Random();
		geSjena=new GameEvent(this);
	}
///////////////BILDER
	public void stvoriRank(int brRanka,SpriteHendler spriteSaRankom,  float xOdLijevogRuba, float yOdVrha, float sir, float vis ){
		this.brRanka=brRanka;
		this.spriteSaRankom=spriteSaRankom;
		this.rankSir=sir;
		this.rankVis=vis;
		this.rankXOdLijevogRuba=xOdLijevogRuba;
		this.rankYOdVrha=yOdVrha;
	}
public void setVelVojnika(float sir, float vis){
	recVoj.set(0, 0, sir, vis);
}
/////////metode od interfacea object linker prikaz/////////
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
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
			this.poceoCekati=java.lang.System.currentTimeMillis();
			ge.indikatorBorbe=0; // vraca ga u pocetni polozaj
		}
		if(ge.indikatorBorbe==2){
			xProt=ge.pomNaX;
			yProt=ge.pomNaY;
			if(this.brVojnika==1) ispaljivanjeVojnikaBr=1;
			else if(this.brVojnika==2){
				ispaljivanjeVojnikaBr++;
				if(ispaljivanjeVojnikaBr==3) ispaljivanjeVojnikaBr=1;
			}
			else if(this.brVojnika==3){
				ispaljivanjeVojnikaBr++;
				if(ispaljivanjeVojnikaBr==4) ispaljivanjeVojnikaBr=1;
			}
			ToranjLStrijelci d=(ToranjLStrijelci)dvojnik;
			d.setPocIspaljivanja(xIspa, yIspa);
			this.ciljanje=false;
			ispaljivanje=true;
			cekanje=false;
			ge.indikatorBorbe=0; // vraca ga u pocetni polozaj
		}
		xProt=ge.pomNaX;
		yProt=ge.pomNaY;
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
		
	}
/////////////////////////////////////////////////////	
	/// privatne metode
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
			
           if(indikator==101){
        	   brVojnika=1;
        	   xVoj1=rectCrtanja.centerX();
        	   yVoj1=rectCrtanja.bottom-(rectCrtanja.height()/100)*visinaPlatformePosto; 
            }
           else if(indikator==102){
        	   brVojnika=2;
        	   xVoj1=rectCrtanja.centerX()-rectCrtanja.width()/6;
        	   yVoj1=rectCrtanja.bottom-(rectCrtanja.height()/100)*visinaPlatformePosto;
        	   xVoj2=rectCrtanja.centerX()+rectCrtanja.width()/6;
        	   yVoj2=rectCrtanja.bottom-(rectCrtanja.height()/100)*visinaPlatformePosto;
           }
           else if(indikator==103||indikator==104||indikator==105||this.indikator==106||this.indikator==107||this.indikator==108||
        		   this.indikator==109||this.indikator==110||this.indikator==111){
        	   brVojnika=3;
        	   xVoj1=rectCrtanja.centerX()+rectCrtanja.width()/25;
        	   yVoj1=rectCrtanja.bottom-(rectCrtanja.height()/100)*visinaPlatformePosto;
        	   xVoj2=rectCrtanja.centerX()+rectCrtanja.width()/4+rectCrtanja.width()/25;
        	   yVoj2=rectCrtanja.bottom-(rectCrtanja.height()/100)*visinaPlatformePosto; 
        	   xVoj3=rectCrtanja.centerX()-rectCrtanja.width()/4+rectCrtanja.width()/25;
        	   yVoj3=rectCrtanja.bottom-(rectCrtanja.height()/100)*visinaPlatformePosto;
           }
           stvoriPripadajuceDodatke();
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
private void nacrtajVojnike(){
	izvuciSmijerProtivnika();
	if(brVojnika==1){
		/////////vojnik 1
		recVoj.set(xVoj1-recVoj.width()/2,yVoj1-recVoj.height(), xVoj1+recVoj.width()/2, yVoj1);
		xIspa=recVoj.centerX();
		yIspa=recVoj.top+recVoj.height()/2;
		if(cekanje) {
			spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		}
		else if(this.ispaljivanjeVojnikaBr==1){
		    if(ciljanje) spriteHend.nacrtajSprite(can,1,1, smijerProt, recVoj);
	    	else if(ispaljivanje&&vrijVoj1+1000/spriteHend.brojPrikazaPoSekundi(1)<java.lang.System.currentTimeMillis()){ 
        	       spriteHend.nacrtajSprite(can,1,ticVoj1, smijerProt, recVoj);
                   ticVoj1++;
                   vrijVoj1=java.lang.System.currentTimeMillis();
                   if(spriteHend.brojStupaca(1)<=ticVoj1) {
    	                                     ticVoj1=2;
    	                                     ispaljivanje=false;
    	                                    
                                             }
               }
              else{
        	          spriteHend.nacrtajSprite(can,1,ticVoj1, smijerProt, recVoj);
                    }
           
		}
		else spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
	}
	////////////
	////////////
	else if(brVojnika==2){
		/////////vojnik 1
		recVoj.set(xVoj1-recVoj.width()/2,yVoj1-recVoj.height(), xVoj1+recVoj.width()/2, yVoj1);
		if(cekanje) {
			spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
			
		}
		else if(this.ispaljivanjeVojnikaBr==1){
			xIspa=recVoj.centerX();
			yIspa=recVoj.top+recVoj.height()/2;
		    if(ciljanje) spriteHend.nacrtajSprite(can,1,1, smijerProt, recVoj);
	    	else if(ispaljivanje&&vrijVoj1+1000/spriteHend.brojPrikazaPoSekundi(1)<java.lang.System.currentTimeMillis()){ 
        	       spriteHend.nacrtajSprite(can,1,ticVoj1, smijerProt, recVoj);
                   ticVoj1++;
                   vrijVoj1=java.lang.System.currentTimeMillis();
                   if(spriteHend.brojStupaca(1)<=ticVoj1) {
    	                                     ticVoj1=2;
    	                                     ispaljivanje=false;
    	                                   
                                             }
               }
              else{
        	          spriteHend.nacrtajSprite(can,1,ticVoj1, smijerProt, recVoj);
                    }
           
		}
		else spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		//////vojnik2
		recVoj.set(xVoj2-recVoj.width()/2,yVoj2-recVoj.height(), xVoj2+recVoj.width()/2, yVoj2);
		if(cekanje) {
			spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		}
		else if(this.ispaljivanjeVojnikaBr==2){
			xIspa=recVoj.centerX();
			yIspa=recVoj.top+recVoj.height()/2;
		   if(ciljanje) spriteHend.nacrtajSprite(can,1,1, smijerProt, recVoj);
	       else if(ispaljivanje&&vrijVoj2+1000/spriteHend.brojPrikazaPoSekundi(1)<java.lang.System.currentTimeMillis()){ 
        	      spriteHend.nacrtajSprite(can,1,ticVoj2, smijerProt, recVoj);
                  ticVoj2++;
                  vrijVoj2=java.lang.System.currentTimeMillis();
                  if(spriteHend.brojStupaca(1)<=ticVoj2) {
    	                                     ticVoj2=2;
    	                                     ispaljivanje=false;
    	                            
                                             }
            }
	       
          else{
        	  spriteHend.nacrtajSprite(can,1,ticVoj2, smijerProt, recVoj);
          }
        }
		else spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
	}
	///////////////
	//////////////
	else if(brVojnika==3){
		/////////vojnik 1
		recVoj.set(xVoj1-recVoj.width()/2,yVoj1-recVoj.height(), xVoj1+recVoj.width()/2, yVoj1);
		if(cekanje) {
			spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
			
		}
		else if(this.ispaljivanjeVojnikaBr==1){
			xIspa=recVoj.centerX();
			yIspa=recVoj.top+recVoj.height()/2;
		    if(ciljanje) spriteHend.nacrtajSprite(can,1,1, smijerProt, recVoj);
	    	else if(ispaljivanje&&vrijVoj1+1000/spriteHend.brojPrikazaPoSekundi(1)<java.lang.System.currentTimeMillis()){ 
        	       spriteHend.nacrtajSprite(can,1,ticVoj1, smijerProt, recVoj);
                   ticVoj1++;
                   vrijVoj1=java.lang.System.currentTimeMillis();
                   if(spriteHend.brojStupaca(1)<=ticVoj1) {
    	                                     ticVoj1=2;
    	                                     ispaljivanje=false;
    	                                   
                                             }
               }
              else{
        	          spriteHend.nacrtajSprite(can,1,ticVoj1, smijerProt, recVoj);
                    }
           
		}
		else spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		//////vojnik2
		recVoj.set(xVoj2-recVoj.width()/2,yVoj2-recVoj.height(), xVoj2+recVoj.width()/2, yVoj2);
		if(cekanje) {
			spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		}
		else if(this.ispaljivanjeVojnikaBr==2){
			xIspa=recVoj.centerX();
			yIspa=recVoj.top+recVoj.height()/2;
		   if(ciljanje) spriteHend.nacrtajSprite(can,1,1, smijerProt, recVoj);
	       else if(ispaljivanje&&vrijVoj2+1000/spriteHend.brojPrikazaPoSekundi(1)<java.lang.System.currentTimeMillis()){ 
        	      spriteHend.nacrtajSprite(can,1,ticVoj2, smijerProt, recVoj);
                  ticVoj2++;
                  vrijVoj2=java.lang.System.currentTimeMillis();
                  if(spriteHend.brojStupaca(1)<=ticVoj2) {
    	                                     ticVoj2=2;
    	                                     ispaljivanje=false;
    	                                  
                                             }
            }
	       
          else{
        	  spriteHend.nacrtajSprite(can,1,ticVoj2, smijerProt, recVoj);
          }
        }
		else spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		/////////vojnik 3
		recVoj.set(xVoj3-recVoj.width()/2,yVoj3-recVoj.height(), xVoj3+recVoj.width()/2, yVoj3);
		if(cekanje) {
			spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
		}
		else if(this.ispaljivanjeVojnikaBr==3){
			xIspa=recVoj.centerX();
			yIspa=recVoj.top+recVoj.height()/2;
		   if(ciljanje) spriteHend.nacrtajSprite(can,1,1, smijerProt, recVoj);
		   else if(ispaljivanje&&vrijVoj3+1000/spriteHend.brojPrikazaPoSekundi(1)<java.lang.System.currentTimeMillis()){ 
    	      spriteHend.nacrtajSprite(can,1,ticVoj3, smijerProt, recVoj);
              ticVoj3++;
              vrijVoj3=java.lang.System.currentTimeMillis();
              if(spriteHend.brojStupaca(1)<=ticVoj3) {
	                                     ticVoj3=2;
	                                     ispaljivanje=false;
	                                     //ispaljivanjeVojnikaBr=1;// ponovno se stavlja prvi za ispaljivanje
                                         }
               }
           else{
    	          spriteHend.nacrtajSprite(can,1,ticVoj3, smijerProt, recVoj);
                }
		}
		else spriteHend.nacrtajSprite(can,1,0, smijerProt, recVoj);
	}
}
private void izvuciSmijerProtivnika(){
	double dy=rectCrtanja.centerY()-yProt;
    double dx=xProt-rectCrtanja.centerX();
	double kut=57.29577951*Math.atan(Math.abs(dy)/Math.abs(dx));
	if(dy>=0&&dx<=0) kut=180-kut;
	else if(dy<=0&&dx<=0) kut=180+kut;
	else if(dy<=0&&dx>=0) kut=360-kut;
	
	
	if(kut<22.5&&kut>-0||kut>337.5) smijerProt=0;//     istoèno
	else if(kut<67.5&&kut>22.5) smijerProt=1;// sjevero-istocno
	else if(kut<112.6&&kut>67.5) smijerProt=2;// sjever
	else if(kut<157.6&&kut>112.6) smijerProt=3;// sjevero-zapadno
	else if(kut<202.6&&kut>157.6) smijerProt=4;// zapad
	else if(kut<247.6&&kut>202.6) smijerProt=5;// jugo-zapad
	else if(kut<292.6&&kut>247.6) smijerProt=6;// jug
	else if(kut<337.6&&kut>292.6) smijerProt=7;// jugo-istok
	/*if(kut<0.3925&&kut>0) smijerProt=0;//     istoèno
	if(kut>5.8875) smijerProt=0;
	else if(kut<1.1775&&kut>0.3925) smijerProt=1;// sjevero-istocno
	else if(kut<1.9625&&kut>1.1775) smijerProt=2;// sjever
	else if(kut<2.7475&&kut>1.9625) smijerProt=3;// sjevero-zapadno
	else if(kut<3.925&&kut>2.7475) smijerProt=4;// zapad
	else if(kut<4.3175&&kut>3.925) smijerProt=5;// jugo-zapad
	else if(kut<5.1025&&kut>4.3175) smijerProt=6;// jug
	else if(kut<5.8875&&kut>5.1025) smijerProt=7;// jugo-i>*/
}	
private void nacrtajToranj(){
       spriteHend.nacrtajSprite(can, 0, 0, 0, rectCrtanja);
	        }
private void stvoriPripadajuceDodatke(){
	 if(this.indikator==104||this.indikator==106||this.indikator==107||this.indikator==108){// vatreni
		 crtajVatreni=true;
		 recDodZastava=new RectF();
		 recDodNaVrhu1=new RectF(); 
		 recDodNaVrhu2=new RectF(); 
		 //zastava
		 geDod1=new GameEvent(null);
		 geDod2=new GameEvent(null);
		 geDod1.jesamLiZiv=true;
		 geDod2.jesamLiZiv=true;
		 float visOdVrhTornjVrh=29f*this.rectCrtanja.height()/100;
		 float visOdVrhTornjDno=79f*this.rectCrtanja.height()/100;
		 recDodZastava.set(rectCrtanja.left,rectCrtanja.top+visOdVrhTornjVrh, rectCrtanja.right,rectCrtanja.top+visOdVrhTornjDno);
		 // baklje
		 visOdVrhTornjVrh=-25.45f*this.rectCrtanja.height()/100;
		
		 float bakljaVis=53.45f*this.rectCrtanja.height()/100;
		 recDodNaVrhu1.set(rectCrtanja.left, rectCrtanja.top+visOdVrhTornjVrh,rectCrtanja.left+bakljaVis*0.324f,  rectCrtanja.top +bakljaVis+visOdVrhTornjVrh);
		 
		 recDodNaVrhu2.set(rectCrtanja.right-bakljaVis*0.324f, rectCrtanja.top+visOdVrhTornjVrh,rectCrtanja.right,  rectCrtanja.top +bakljaVis+visOdVrhTornjVrh);
	 }
	 else if(this.indikator==105||this.indikator==109||this.indikator==110||this.indikator==111){// otrovni
		 crtajOtrovni=true;
		 recDodZastava=new RectF();
		 recDodNaVrhu1=new RectF(); 
	
		 geDod1=new GameEvent(null);
		 geDod1.jesamLiZiv=true;
		 float visOdVrhTornjVrh=29f*this.rectCrtanja.height()/100;
		 float visOdVrhTornjDno=79f*this.rectCrtanja.height()/100;
		 recDodZastava.set(rectCrtanja.left,rectCrtanja.top+visOdVrhTornjVrh, rectCrtanja.right,rectCrtanja.top+visOdVrhTornjDno);
		 
		 // kotao
	
		 float visKot=40.7f*this.rectCrtanja.height()/100;	
		 visOdVrhTornjVrh=-20.7f*this.rectCrtanja.height()/100;
		 visOdVrhTornjDno=visOdVrhTornjVrh+visKot;		 
		 float polOdLijRubaLij=12.1f*this.rectCrtanja.width()/100;
		 float polOdLijRubaDes=polOdLijRubaLij+0.614f*visKot;
		
		 recDodNaVrhu1.set( rectCrtanja.left+polOdLijRubaLij,  rectCrtanja.top+visOdVrhTornjVrh,  rectCrtanja.left+polOdLijRubaDes, rectCrtanja.top+visOdVrhTornjDno);
	 }
}		
private void nacrtajDodatne(){
	if(crtajVatreni){// vatreni
		
		 //zastava
		 spriteHend.nacrtajSprite(can, 2, 0, 0, recDodZastava);
		
		
		 // baklje

		 spriteHend.vrtiAnimacijuReda(can,this.geDod1, 3, 0,  recDodNaVrhu1, null);
		 
		 spriteHend.vrtiAnimacijuReda(can,this.geDod2, 3, 0,  recDodNaVrhu2, null);
		
	 }
	 else if(crtajOtrovni){// otrovni
		
		 spriteHend.nacrtajSprite(can, 4, 0, 0, recDodZastava);
		 
		 // kotao
		
		
		 spriteHend.vrtiAnimacijuReda(can,this.geDod1, 5, 0,  recDodNaVrhu1, null);
	 }
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
	nacrtajVojnike();
	nacrtajDodatne();
	posaljiPorukuLogici888();// stavio sam da svaki šalje poruku svom parnjaku u logici, inaèe je bilo  u metodama (imtouched...)koje zapravo poziva thread od toucha 
	nacrtajRank();
	}
	if(jesamLiZiv==false) ubijMe();
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
