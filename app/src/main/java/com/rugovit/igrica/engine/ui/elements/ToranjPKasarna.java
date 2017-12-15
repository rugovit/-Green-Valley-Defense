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
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;


public class ToranjPKasarna implements UIManagerObject {
	////animacija stvaranja
	private Paint paintStvaranja=new Paint();
	private float pocetniAlfa;
	private double pocetakStvaranja;
	private boolean tekPoceoStvaranje=true;
	private  float  duzinaStvaranja=0.6f;
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
	private boolean crtajGrm=false, crtajStit=false,crtajKiss=false;
	/////konstante
	private float visinaPlatformePosto=90;// na kojoj je visini platforma gdje su vojnici u odnosu na rectCrtanja
	///////////
	private Random generator;
	private boolean emItouched=false;
	private boolean boolZaSlanje=false;
	private boolean izbornikUpaljen=false;
	private float xTor,yTor;
	private float xOkupljaliste,yOkupljaliste;
	private int indikator;
	private float rTor;
	private float sirTor,visTor;
	private GameEvent ge,	geSjena;
	private Canvas can;
	private GameLogicObject dvojnik;
	private SpriteHendler spriteHend;
	private UIManager uiMan;
	private boolean jesamLiZiv=true;
	private boolean tekPoceo=true;
	private RectF rectCrtanja,recZvjezdica1,recZvjezdica2,recZvjezdica3,recZastavica,recTempZastavica;
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
	private Sjena mojaSjena;
    ////////varijable protibnika
	private float xIspa=0;
	private float yIspa=0;
	private float xProt=0;
	private float yProt=0;
	private int smijerProt=0;// kut u radijanima
	private Paint paintZastavice;
	
	////////////////////////////
	int testTic=0;
	///////////////////////
	///privremnei
	Paint p;
	Paint pK;
	public ToranjPKasarna(float xTor, float yTor,float rTor,float sirTor,float visTor,SpriteHendler spriteHend,int indikator ){
		this.rTor=2*rTor;
		this.yTor=yTor;
		this.xTor=xTor;
		this.visTor=visTor;
		this.sirTor=sirTor;
		geSjena=new GameEvent(this);
		this.indikator=indikator;
		this.spriteHend=spriteHend;
		this.uiMan=uiMan;
		///privremeni
		p=new Paint();
		p.setARGB(50,150,150,50);
		p.setStyle(Paint.Style.FILL);
		pK=new Paint();
		recTempZastavica=new RectF();
		paintZastavice=new Paint();
		pK.setARGB(240,150,0,150);
		pK.setStyle(Paint.Style.FILL);
		rectCrtanja=new RectF();
	     rectCrtanja.set(xTor, yTor, xTor+sirTor, yTor+visTor);
		recVoj=new RectF();
		generator=new Random();
		recZastavica=new RectF();
	}
		//
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		if(ge==null) ge=e;
		xOkupljaliste=ge.x2;
		yOkupljaliste=ge.y2;
		izbornikUpaljen=ge.izbornikUpaljen;	
		if(ge.jesamLiZiv==false){
			this.uiMan.makniObjekt(this);
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
		
		
	}
/////////////////////////////////////////////////////	
	//bilder
	public void stvoriRank(int brRanka,SpriteHendler spriteSaRankom,  float xOdLijevogRuba, float yOdVrha, float sir, float vis ){
		this.brRanka=brRanka;
		this.spriteSaRankom=spriteSaRankom;
		this.rankSir=sir;
		this.rankVis=vis;
		this.rankXOdLijevogRuba=xOdLijevogRuba;
		this.rankYOdVrha=yOdVrha;
	}
/// privatne metode
	private void stvariKojeSeIzvrsavajuSamoNaPocetku(){
		
	
	       rectCrtanja.set(xTor, yTor, xTor+sirTor, yTor+visTor);
	       recZastavica.set(0, 0, visTor/4, visTor/3);
	       recZvjezdica1=new RectF();
	       
	       recZvjezdica1.set(0, 0, sirTor/5, sirTor/5);
	       recZvjezdica2=new RectF();
	       recZvjezdica3=new RectF();
	       
	       
	       this.mojaSjena=new Sjena(dvojnik.getRect().height()*1.2f,dvojnik.getRect().height(),20,43, 59, 233);
	       this.geSjena.x=this.dvojnik.getRect().centerX()-this.dvojnik.getRect().width()*1.2f/2;
			this.geSjena.y=this.dvojnik.getRect().centerY()-dvojnik.getRect().height()/2;
			
			uiMan.dodajElementUListu(mojaSjena, 1);    
			
	       if(indikator==150){
	    	  
	    	   recZvjezdica1.set(rectCrtanja.centerX()-recZvjezdica1.width()/2, rectCrtanja.top+this.recZvjezdica1.height(), 
	    			   rectCrtanja.centerX()+recZvjezdica1.width()/2,rectCrtanja.top+2*this.recZvjezdica1.height());
	       }
	       else if(indikator==151){
	    	   recZvjezdica1.set(rectCrtanja.centerX()-3*recZvjezdica1.width()/2, rectCrtanja.top+this.recZvjezdica1.height(), 
	    			   rectCrtanja.centerX()-1*recZvjezdica1.width()/2,rectCrtanja.top+2*this.recZvjezdica1.height());
	    	   recZvjezdica2.set(rectCrtanja.centerX()+1*recZvjezdica1.width()/2, rectCrtanja.top+this.recZvjezdica1.height(), 
	    			   rectCrtanja.centerX()+3*recZvjezdica1.width()/2,rectCrtanja.top+2*this.recZvjezdica1.height());
	       }
	       else if(indikator==152){
	    	   recZvjezdica1.set(rectCrtanja.centerX()-4*recZvjezdica1.width()/2, rectCrtanja.top+this.recZvjezdica1.height(), 
	    			   rectCrtanja.centerX()-2*recZvjezdica1.width()/2,rectCrtanja.top+2*this.recZvjezdica1.height());
	    	   recZvjezdica2.set(rectCrtanja.centerX()-1*recZvjezdica1.width()/2, rectCrtanja.top+this.recZvjezdica1.height(), 
	    			   rectCrtanja.centerX()+1*recZvjezdica1.width()/2,rectCrtanja.top+2*this.recZvjezdica1.height());
	    	   recZvjezdica3.set(rectCrtanja.centerX()+2*recZvjezdica1.width()/2, rectCrtanja.top+this.recZvjezdica1.height(), 
	    			   rectCrtanja.centerX()+4*recZvjezdica1.width()/2,rectCrtanja.top+2*this.recZvjezdica1.height());
	       }
	       else if(indikator==153||indikator==159||indikator==160||indikator==161){
	    	   this.crtajGrm=true;
	    	   float vis=rectCrtanja.top+rectCrtanja.height()/5;
	    	   float sir=2*rectCrtanja.height()/5;
	    	   recZvjezdica1.set(rectCrtanja.centerX()-sir/2,  vis, 
	    			   rectCrtanja.centerX()+sir/2, vis+sir);
	       }
	       else if(indikator==154||indikator==156||indikator==157||indikator==158){
	    	   this.crtajStit=true;
	    	   float vis=rectCrtanja.top+rectCrtanja.height()/5;
	    	   float sir=2*rectCrtanja.height()/5;
	    	   recZvjezdica1.set(rectCrtanja.centerX()-sir/2,  vis, 
	    			   rectCrtanja.centerX()+sir/2, vis+sir);
	       }
	       else if(indikator==155||indikator==162||indikator==163||indikator==164){
	    	   this.crtajKiss=true;
	    	   float vis=rectCrtanja.top+rectCrtanja.height()/5;
	    	   float sir=2*rectCrtanja.height()/5;
	    	   recZvjezdica1.set(rectCrtanja.centerX()-sir/2,  vis, 
	    			   rectCrtanja.centerX()+sir/2, vis+sir);
	       }
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
			
	private void nacrtajDodatak(){
		
		if(this.indikator==150){
		       
			spriteHend.nacrtajSprite(can, 1, 0, 0,recZvjezdica1 );
		}
		else if(this.indikator==151){
		       
			spriteHend.nacrtajSprite(can, 1, 0, 0,recZvjezdica1 );
			spriteHend.nacrtajSprite(can, 1, 0, 0,recZvjezdica2 );
		}
		else if(this.indikator==152){
		       
			spriteHend.nacrtajSprite(can, 1, 0, 0,recZvjezdica1 );
			spriteHend.nacrtajSprite(can, 1, 0, 0,recZvjezdica2 );
			spriteHend.nacrtajSprite(can, 1, 0, 0,recZvjezdica3 );
		}
		else if(this.crtajGrm){
		       
			spriteHend.nacrtajSprite(can, 2, 0, 0,recZvjezdica1 );
			
		}
		else if(this.crtajStit){
		       
			spriteHend.nacrtajSprite(can, 3, 0, 0,recZvjezdica1 );
			
		}
		else if(this.crtajKiss){
		       
			spriteHend.nacrtajSprite(can, 5, 0, 0,recZvjezdica1 );
			
		}
	}
private void nacrtajZastavicu(){
	
	   spriteHend.nacrtajSprite(can, 4, 0, 0,recTempZastavica,paintZastavice);
}
private void nacrtajToranj(){
       spriteHend.nacrtajSprite(can, 0, 0, 0, rectCrtanja);
       nacrtajDodatak();
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
	        if(this.izbornikUpaljen){
		             
		              
	                  this.recTempZastavica.set(recZastavica.left+this.xOkupljaliste-recZastavica.width()/2,recZastavica. top+this.yOkupljaliste-recZastavica.height(),recZastavica. right+this.xOkupljaliste-recZastavica.width()/2, recZastavica.bottom+this.yOkupljaliste-recZastavica.height());
	                  if(this.rectCrtanja.contains(recZastavica)){
	                	      paintZastavice.setAlpha(170);
	                	  if(this.rectCrtanja.bottom>this.recZastavica.bottom){
	                		  nacrtajZastavicu();
	                	      nacrtajToranj();
	                	  }
	                	  else{
	                		  nacrtajToranj();
	                		  nacrtajZastavicu();
	                	     
	                	  }
	                  }
	                  else{//crta ga iznad tornja li providno ako je otpunosti iza tornja tkao da se ipak vidi
	                	      paintZastavice.setAlpha(210);
	                		  
	                	      nacrtajToranj();
	                	      nacrtajZastavicu();
	                
	                     }
	                  }
	else {
		
		nacrtajToranj();
	}
}
	posaljiPorukuLogici888();// stavio sam da svaki šalje poruku svom parnjaku u logici, inaèe je bilo  u metodama (imtouched...)koje zapravo poziva thread od toucha 
	nacrtajRank();
	if(jesamLiZiv==false) ubijMe();
}
///animacija stvaranja

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
