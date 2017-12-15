package com.rugovit.igrica.engine.logic.elements;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class ProjektilL implements GameLogicObject {
	private boolean jelCiljLeteci=false;
	private float pomaZaSortiranjeLeteceg=0;
    private boolean towerBuster=false;
	private boolean izracunajPauzu=false;
	private double vrijemePauze=0;
    private float maxOcekivanaVelObjekataPix;
    private float udaljenostPocetkaOdCilja;
//////////////metode interfacea game logic object/////////
	///varijable zakrivljene putanje
	private boolean racunanjeProslihPozicija=false;
	private boolean racunanjeProslihPozicijaInterni=false;
	private  	float[][] listaIzracunatiMedupolozaja;
	private float prosliY,prosliX;
	private boolean preletioPik=false, preletioPikTaktPrije=false;
	private boolean kosiHitacIliRavno=true;
	private float kosiDodatY=0,kosiDodatYIzProslTakta;
	private float kosiR=0;
	private float kosiP=0;
	private float kosiTempX=0;
	private float kosiRedDelta=0;
	private float koeficijentVisine;
	/////////
	///vremensko pomicanje
	private float TpS;
	private double tempDelta,tempTime,tempDeltaRacProsPol;
	
	private double prosloVrijeme,prosloVrijemeTaktPrije;
	private boolean xNaCilju=false;
	private boolean yNaCilju=false;
	private boolean xNaCiljuProsliTakt,yNaCiljuProsliTakt;
	///////////
	private GameEvent ge;
	private GameLogic GL;
	private double kut=0,kutProsliTakt=0;// kut se treba samo jedanput izraèinat kada se gaða neka toèka , tak oda æe iæi brže ako ga samo spremim
	private UIManagerObject dvojnik;
	private float xCilj=-10000, yCilj=-10000,velX,velY,xCent,yCent, radijus;
	private float x,y,pocetX,pocetY;

	private float delta, proslaDelta;
	private GameLogicObject objCilj;
	private int indikator;
	private RectF rec;
	private boolean naPozIliSlijedi,kolSteta,prvoPokret=true,prvoPokretProsliTakt, ispaljenProjektil=false;
	private GameLogicObject[] listaSudara;
	private float stetaArmor,stetaHelth;
	private int tipStete;
	private RectF recKonstStete;
	private float stetaArmorKonstantniPoSec,stetaHelthKonstantniPoSec;
	private int tipSteteKonstantni;
	private double vrijemeTrajanjaKonstantneStete, vrijemeKadaJePokrenutaKonstantnaSteta,vrijemeZadnjeKonstantnaSteta;
	private boolean pokrenutaKonstantnaSteta=false,inicijaliziranaKonstantnaSteta=false; 
	
	
	private float vrijHelthGub;
	private float helthPoSecGub;
	private float vrijArmorGub;
	private float armorPoSecGub;
	private boolean dosaNaCilj=true;// u sluèaju da je doðšao na cilj 
	private boolean ostecujemSamoJednog=false;
	//////varijable za pravilno sortiranje
	private float visinaTopa=0;
	private float dnoTopa=0;
	///////////////////////////
	private boolean napadamBranitelje=false;
	private boolean napadamProtivnike=false;
	private GameLogicObject toranjPozivatelj;
	private double vrijStojNaObj=0;
	private boolean zrakaProjektil=false;
	private boolean ciljIznadIliIspodTornja=true;// true iznad , false ispod
	private double pocIspucavanja;
	private float postotakUsporavanja=0;
	private float trajanjeUsporavanja=0;
	private float redukcijaXBrzine;
	///
	private boolean predvidiPolozaj=false;
 	public ProjektilL(GameLogicObject objectPozivatelj,float x, float y, float velX, float velY ,float delta,boolean kolSteta,float radijus,float  koeficijentVisine,boolean napadamBranitelje,boolean napadamProtivnike,int indikator ){
		this.x=x;
		this.y=y;
		pocetX=x;
		pocetY=y;
		this.napadamBranitelje=napadamBranitelje;
	    this.napadamProtivnike=napadamProtivnike;
		this. koeficijentVisine= koeficijentVisine;
		this.toranjPozivatelj=objectPozivatelj;
		this.delta=delta;
		this.velX=velY;
		this.velY=velY;
		this.radijus=radijus;// koristim radijus da odredim kolika ce šteta kome pripasti a velXI velY za to kojeg æe uopæe detektirati kao ošteæenog
		this.indikator=indikator;
		rec=new RectF(x-velX/2,y-velY/2,x+velX/2,y+velY/2);
		if(toranjPozivatelj!=null){
		   visinaTopa=toranjPozivatelj.getYVelUPrikazu();
		   dnoTopa=toranjPozivatelj.getRect().bottom;
		}
		this.kolSteta=kolSteta;// oznaèava dali postoji kolateralna šteta
		naPozIliSlijedi=true;// oznaèava da ide na poziciju
		ge=new GameEvent(this);
		stetaArmor=15;// default vrijednosti u sluèaju da se eksplicitno ne namjesti pomoæu metode
		stetaHelth=15;
		tipStete=1;
		
	}
	///////////////////BILDER
 	public void stvoriTowerBusterProjektil(){
 		towerBuster=true;
 
 	}
 	public void redukcijaXBrzine(float posto){
 		this.redukcijaXBrzine=posto/100; 	}
 	public void predvidiPolozaj(){
 		predvidiPolozaj=true;
 	}
 	public void kosiHitacIliRavno(boolean kosiIliRavno){
 		 kosiHitacIliRavno=kosiIliRavno;
 	}
 	public void setOstecujemSamoJednog(){
 		ostecujemSamoJednog=true;
 	}
	public void setStetuZaHelthArmorTip(float helth,float armor,int tip,float vrijUspor, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){
		 stetaArmor=armor;
		 stetaHelth=helth;
		 tipStete=tip;
		postotakUsporavanja=postUsp;
		 trajanjeUsporavanja=vrijUspor;
		 this.vrijHelthGub=vrijHelthGub;
	     this.helthPoSecGub=helthPoSecGub;
	     this.vrijArmorGub=vrijArmorGub;
	     this.armorPoSecGub=armorPoSecGub;
	}
	public void stvoriZrakuProjektil(double vrijStojNaObj){
		this. vrijStojNaObj=vrijStojNaObj;
		this.zrakaProjektil=true;
	}
	//////////////////////////////////////////////////////////////////////
	//////PUBLIC METODE////////////////////////////////////////////
	public void namjestiBrzinu(float delta){
		this.delta=delta;
	}
	public void namjestiNoviPocetniPolozaj(float polozajX,float polozajY){
		pocetX=polozajX;
		pocetY=polozajY;
	}
	public boolean jesiDosaoNaCilj(){
		return this.dosaNaCilj;
	}
	///////////////////////////////////////////////////////////////
	////////PRIVATNE METODE////////////////////////////////////////
	//*
	private boolean daliSeProtivnikTeleportira(){
		boolean b=false;
				if(objCilj!=null)  if(this.objCilj instanceof Protivnik ){
					Protivnik temp=(Protivnik )  this.objCilj ;
					  b=temp.daliSeTeleportiram();
				  }
				return b;
	}
	//*
	private void naciniStetuTornju(){//zasad se koristi samo ubij me metoda  od tornja ostalu mehaniku sam ostavio u slucaju da budem htio napraviti helt od tornja
		if(objCilj!=null) {
		ToranjL ob=null;
		

		
				   ob=(ToranjL)this.objCilj;
			
		              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
		              ob.ubijMe();
		              this.GL.faIgr.br200ToranjEmbrio(ob.getX()+ob.getRect().width()/2, ob.getY()+ob.getRect().height()/2);
		              ob.ubijMe();
		             // ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja, vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
		}
	 }
	//*
	private void predvidiPolozajObjekta(){
		if(this.objCilj!=null){
			float dx=objCilj.getDx();
			float dy=objCilj.getDy();
			float  vrijeme=(float)Math.hypot(Math.abs(this.rec.centerX()-xCilj),Math.abs(this.rec.centerY()-yCilj))/delta;
			
		//vrijeme=1.6f*vrijeme;

				this.xCilj=objCilj.getRect().centerX()+vrijeme*dx;
			    this.yCilj=objCilj.getRect().centerY()+vrijeme*dy;
			
			
		    

		    
		}
	}  
	private double deltaPomak(){
		tempTime=(float)(-prosloVrijeme+System.currentTimeMillis()-vrijemePauze)/1000;
		if(tempTime>0.5) return 1/TpS;
		else return   delta*tempTime;
	}
//*	
private void naciniStetuJednomBranitelju(){
		int i=0;
		GameLogicBranitelj ob=null;
		listaSudara= GL.provjeriKoliziju(6,rec);
		if(naPozIliSlijedi)while(listaSudara.length>i){
	       if(listaSudara[i]==null) break;
	       if(listaSudara[i]instanceof GameLogicBranitelj){
	    	   ob=(GameLogicBranitelj) listaSudara[i];
	    	  /* if(!naPozIliSlijedi){//
	    		   if(ob!=(GameLogicBranitelj)this.objCilj){//ako nije objekt cilja noramalno procjenjuje štetu pa će na kraju posebno za objekt cilja , to radim zato ako rect vude manje od dx dy pomak  objekta jes se šteta proccjenjuje takt iza detekcija pogotka
	    			   float postotak=procijeniStetu (ob);
	 	              float stetaHel=(this.stetaHelth/100)*postotak ;
	 	              float stetaArm=(this.stetaHelth/100)*postotak;
	 	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	    			   break;
	    		   }
	    		   
	    	   }
	    	   else{*/
	    	      float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	              this.ge.objektLogike=ob;// namjestam koji je objekt pogodio za koristenje u prikazu
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              break;
	              //ob.setStetu(tipStete,steta, stetaArmor);
	           
		   }
	       i++;
		}
		
		if(objCilj!=null)    if(!naPozIliSlijedi&&objCilj instanceof GameLogicBranitelj){
			   ob=(GameLogicBranitelj)this.objCilj;
			   float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja, vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
		   }
	}
//*	
private void naciniStetuJednomProtivniku(){
		int i=0;
		GameLogicProtivnik ob=null;
		
		 listaSudara=GL.provjeriKoliziju(this);
		if(naPozIliSlijedi) while(listaSudara.length>i){
	       if(listaSudara[i]==null) break;
	       if(listaSudara[i]instanceof GameLogicProtivnik){
	    	   ob=(GameLogicProtivnik) listaSudara[i];
	    	   /*if(!naPozIliSlijedi){//
	    		   if(ob!=(GameLogicProtivnik)this.objCilj){//ako nije objekt cilja noramalno procjenjuje štetu pa će na kraju posebno za objekt cilja , to radim zato ako rect vude manje od dx dy pomak  objekta jes se šteta proccjenjuje takt iza detekcija pogotka
	    			   float postotak=procijeniStetu (ob);
	 	              float stetaHel=(this.stetaHelth/100)*postotak ;
	 	              float stetaArm=(this.stetaHelth/100)*postotak;
	 	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	    			   break;
	    		   }
	    		   
	    	   }
	    	   else{*/
	    	      float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	              this.ge.objektLogike=ob;// namjestam koji je objekt pogodio za koristenje u prikazu
	              break;
	              //ob.setStetu(tipStete,steta, stetaArmor);
	          
		   }
	       i++;
		}
		
		if(objCilj!=null)    if(!naPozIliSlijedi&&objCilj instanceof GameLogicProtivnik){
			   ob=(GameLogicProtivnik)this.objCilj;
			   float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja, vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
		   }
	}
//*	
private void naciniStetuBraniteljima(){
		int i=0;
		GameLogicBranitelj ob=null;
		
		listaSudara= GL.provjeriKoliziju(6,rec);
		while(listaSudara.length>i){
	       if(listaSudara[i]==null) break;
	       if(listaSudara[i]instanceof GameLogicBranitelj){
	    	   ob=(GameLogicBranitelj) listaSudara[i];
	    	   if(!naPozIliSlijedi){//
	    		   if(ob!=(GameLogicBranitelj)this.objCilj){//ako nije objekt cilja noramalno procjenjuje štetu pa će na kraju posebno za objekt cilja , to radim zato ako rect vude manje od dx dy pomak  objekta jes se šteta proccjenjuje takt iza detekcija pogotka
	    			   float postotak=procijeniStetu (ob);
	 	              float stetaHel=(this.stetaHelth/100)*postotak ;
	 	              float stetaArm=(this.stetaArmor/100)*postotak;
	 	             ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	 	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	    			   
	    		   }
	    		   
	    	   }
	    	   else{
	    	      float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              this.ge.objektLogike=ob;// namjestam koji je objekt pogodio za koristenje u prikazu
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	           //ob.setStetu(tipStete,steta, stetaArmor);
	           }
		   }
	       i++;
		}
		
		if(objCilj!=null)    if(!naPozIliSlijedi&&objCilj instanceof GameLogicBranitelj){
			   ob=(GameLogicBranitelj)this.objCilj;
			   float postotak=procijeniStetu (ob);		
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja, vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
		   }
	}
	private void naciniKonstantnuStetuProtivnicima(){
		int i=0;
		Protivnik ob=null;
		listaSudara=GL.provjeriKoliziju(this);
		kolSteta=true;
		
		vrijemeKadaJePokrenutaKonstantnaSteta+=this.vrijemePauze;
		vrijemeZadnjeKonstantnaSteta+=this.vrijemePauze;
		if(this.vrijemeKadaJePokrenutaKonstantnaSteta+this.vrijemeTrajanjaKonstantneStete>System.currentTimeMillis()){
			  while(listaSudara.length>i){
	          if(listaSudara[i]==null) break;
	          if(listaSudara[i]instanceof GameLogicProtivnik){
	    	      ob=(Protivnik) listaSudara[i];
	              if(!ob.jesamLiImunNaKonstantnuStetu()){
	    	         float postotak=procijeniStetu (ob);
	                 float stetaHel=(this.stetaHelthKonstantniPoSec/100)*postotak*(float)(System.currentTimeMillis()-this.vrijemeZadnjeKonstantnaSteta);
	                 float stetaArm=(this.stetaArmorKonstantniPoSec/100)*postotak*(float)(System.currentTimeMillis()-this.vrijemeZadnjeKonstantnaSteta);           
	                 ob.setStetu(this.tipSteteKonstantni, stetaHel, stetaArm,0,0,0, 0, 0, 0);
	              }
	              //ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	              //ob.setStetu(tipStete,steta, stetaArmor);
	           
		          }
	               i++;
		       }
			  vrijemeZadnjeKonstantnaSteta=System.currentTimeMillis();
		    }
		else {
			x=pocetX;
		    y=pocetY;
		    updateRect();
		    ispaljenProjektil=false;
		    prvoPokret=true;
		    xNaCilju=false;
		    yNaCilju=false;
			pokrenutaKonstantnaSteta=false;
		}
	}
//*    
private void naciniStetuProtivnicima(){
		int i=0;
		GameLogicProtivnik ob=null;
		 listaSudara=GL.provjeriKoliziju(this);
		while(listaSudara.length>i){
	       if(listaSudara[i]==null) break;
	       if(listaSudara[i]instanceof GameLogicProtivnik){
	    	   ob=(GameLogicProtivnik) listaSudara[i];
	    	   if(!naPozIliSlijedi){//
	    		   if(ob!=(GameLogicProtivnik)this.objCilj){//ako nije objekt cilja noramalno procjenjuje štetu pa će na kraju posebno za objekt cilja , to radim zato ako rect vude manje od dx dy pomak  objekta jes se šteta proccjenjuje takt iza detekcija pogotka
	    			   float postotak=procijeniStetu (ob);
	 	              float stetaHel=(this.stetaHelth/100)*postotak ;
	 	              float stetaArm=(this.stetaArmor/100)*postotak;
	 	             ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	 	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	    			   
	    		   }
	    		   
	    	   }
	    	   else{
	    	      float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              this.ge.objektLogike=ob;// namjestam koji je objekt pogodio za koristenje u prikazu
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
	           //ob.setStetu(tipStete,steta, stetaArmor);
	           }
		   }
	       i++;
		}
		
		if(objCilj!=null) 	   if(!naPozIliSlijedi&&objCilj instanceof GameLogicProtivnik){
			   ob=(GameLogicProtivnik)this.objCilj;
			   float postotak=procijeniStetu (ob);
	              float stetaHel=(this.stetaHelth/100)*postotak ;
	              float stetaArm=(this.stetaArmor/100)*postotak;
	              ge.izbornikUpaljen=true;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi
	              ob.setStetu(tipStete, stetaHel, stetaArm,trajanjeUsporavanja,postotakUsporavanja, vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
		   }
	}
//*	
public float procijeniStetu(GameLogicObject obj) {
		// TODO Auto-generated method stub
		float postotak=0;
		if(kolSteta){// raèuna udaljenost toèaka, linearno rasporeðuje štetu po udaljenosti
			double hipo=Math.hypot(rec.centerX()-obj.getRect().centerX(),rec.centerY()-obj.getRect().centerY());// izraèunava udaljenost dvaju centara objekta i projektila
			 postotak=100-(float)(hipo/(radijus/100));
			 
		  }
		else {
			if(naPozIliSlijedi){
				if(Math.hypot(rec.centerX()-obj.getRect().centerX(),rec.centerY()-obj.getRect().centerY())<=delta) postotak=100; // ako je udaljenost manja od jedne delte
			}
			else 	if(objCilj!=null) if(obj==objCilj) postotak=100;//u sluèaju da je namješten da slijedi onda usporeðuje dali su objekti isti
	
		}
		return (float)postotak;
	}
	private void posaljiDaUmiremPrikazniku(){
		 ge.jesamLiZiv=false;
	   	dvojnik.GameLinkerIzvrsitelj(ge);
	}
	private void posaljiStanjePrikazniku(){
		 
		  ge.x=x;
		  ge.y=y;
		  ge.x2=this.xCilj;
		  ge.y2=this.yCilj;
		  ge.vrijemePauze=(int)this.vrijemePauze;
		  if(toranjPozivatelj!=null){
			  if(zrakaProjektil){
				  if(y>this.dnoTopa-this.visinaTopa+8*this.visinaTopa/10&&y<dnoTopa)  ge.helth=maxOcekivanaVelObjekataPix;// salje se u heltu jer se ne koristi za ništa u projektilu, ova  vrijednost koristit ce se za bolje sortiranje 
			      else{			    	  
			    	  ge.helth=maxOcekivanaVelObjekataPix/10;
			         }
				  /*if(!ciljIznadIliIspodTornja){
			          if(y>dnoTopa) ge.helth=maxOcekivanaVelObjekataPix-y+dnoTopa;
			          else ge.helth=0;// kosiDod.. je uvijek minus tako da ga pretvaram u plus
			  
		     
		           }
		        else {
			    
			         if(y>dnoTopa-8*this.visinaTopa/10&&y<dnoTopa)  ge.helth=maxOcekivanaVelObjekataPix;// salje se u heltu jer se ne koristi za ništa u projektilu, ova  vrijednost koristit ce se za bolje sortiranje 
			         else{
			    	  
			    	  ge.helth=maxOcekivanaVelObjekataPix/6;
			      }
		        }*/
			  }
			  else {
		        if(!ciljIznadIliIspodTornja){
		        	
			        if(!this.jelCiljLeteci)  {
			        	                                  if(y>dnoTopa-maxOcekivanaVelObjekataPix) ge.helth=maxOcekivanaVelObjekataPix-(y-(dnoTopa-maxOcekivanaVelObjekataPix));
			                                            else ge.helth=this.yCilj-y;// kosiDod.. je uvijek minus tako da ga pretvaram u plus
			  
			                                               }
			        else{
			        	                                  if(y>dnoTopa-maxOcekivanaVelObjekataPix) ge.helth=maxOcekivanaVelObjekataPix-(y-(dnoTopa-maxOcekivanaVelObjekataPix));
                                                          else ge.helth=this.yCilj+this.pomaZaSortiranjeLeteceg-y;// kosiDod.. je uvijek minus tako da ga pretvaram u plus
			        }
		           }
		        else {
		        	   if(!this.jelCiljLeteci)  {
			                                if(y>dnoTopa-maxOcekivanaVelObjekataPix)  ge.helth=+maxOcekivanaVelObjekataPix;// salje se u heltu jer se ne koristi za ništa u projektilu, ova  vrijednost koristit ce se za bolje sortiranje 
			                                else{
			    	  
			    	                              ge.helth=this.yCilj-y;
			                                        }
		        	                       }
		        	   else{
		        		     if(y>dnoTopa-maxOcekivanaVelObjekataPix)  ge.helth=+maxOcekivanaVelObjekataPix;// salje se u heltu jer se ne koristi za ništa u projektilu, ova  vrijednost koristit ce se za bolje sortiranje 
                             else{
 	  
 	                              ge.helth=this.yCilj+this.pomaZaSortiranjeLeteceg-y;
                                     }
		        	   }
		             }
		    }
		  }
		  else {// ako je meteor ili tako nesto
			  if(this.yCilj-y>maxOcekivanaVelObjekataPix)  ge.helth=+maxOcekivanaVelObjekataPix;// salje se u heltu jer se ne koristi za ništa u projektilu, ova  vrijednost koristit ce se za bolje sortiranje 
		      else{
		    	  
		    	  ge.helth=this.yCilj-y;
		      }
		  }
		  
		//  if((this.yCilj-y)>visinaTopa) ge.helth=+visinaTopa;
		  dvojnik.GameLinkerIzvrsitelj(ge);
	}
	private void updateRect(){
	
		rec.set(this.x-velX/2,y-velY/2,x+velX/2,y+velY/2);
	}
	public float[][]  getListuIzracunatihMedupolozaja(){
		return  listaIzracunatiMedupolozaja;
	}
 	public float[][] izracunavanjePolozajaUProslosti(int brTocaka){
		
		///dodati pravi dodatak na Y i spremiti dodatak na y iz proslog takta
 		prosloVrijemeTaktPrije+=this.vrijemePauze;
		float razlikaVremena=(float)(System.currentTimeMillis()-this.prosloVrijemeTaktPrije);
		float deltaVrijeme=razlikaVremena/brTocaka;
		//int brTocaka=Math.round((float)(System.currentTimeMillis()-this.prosloVrijemeTaktPrije)/deltaVrijeme);
		
		float[][] lista= new float[2][] ;
        if(xCilj>-10000||xCilj>-10000){
    		lista[0]= new float[brTocaka] ;
    		lista[1]= new float[brTocaka] ;
    		///sprema vrijednosti da ne upropasti izvođenje u logici
    	         	float praviX=x,praviY=y,praviProsliY=prosliY,praviProsliX=prosliX, praviKosiDodatY=this.kosiDodatY ;
    	            double pravoProsloVrijeme= prosloVrijeme,pravoVrijemePauze=vrijemePauze;
    	        	boolean pravoPreletioPik=preletioPik,pravoPrvoPokret=prvoPokret;
    	        	double praviKut=kut;
    	        	float pravaDelta=delta;
    	        	boolean praviXNaCilju=xNaCilju,praviYNaCilju=yNaCilju;
    	        	////promjene vrijednosti
    	        	
    	        	
    	   ///////////////////////////////////////////////////////////  
    	    racunanjeProslihPozicija=true; 
    	    x=this.prosliX;
    	    y=this.prosliY;
    	    kut=this.kutProsliTakt;
    	    this.prvoPokret=prvoPokretProsliTakt;
    	    this.preletioPik=preletioPikTaktPrije;
    	    this.kosiDodatY=this.kosiDodatYIzProslTakta;
    	    xNaCilju= xNaCiljuProsliTakt;
    	    yNaCilju=yNaCiljuProsliTakt;
    	    delta=proslaDelta;
    	    tempDeltaRacProsPol=deltaPomak();
    		for(int i=0;i<brTocaka;i++){
    			
    		    ///////////////////////////////////
    			tempTime=deltaVrijeme/1000;
    			tempDelta= delta*tempTime;
    			if(tempTime>0.5) tempDelta= 1/TpS;
    			////////////////////////////////////
    		
    			if(this.kosiHitacIliRavno)pomakPremaKosiHitac(xCilj,yCilj);
    			else this.pomakPrema();
    			
    			lista[0][i]=x;
    			lista[1][i]=y;
    			
    		
    			
    		}
    		           ////vracanje starih vrijednosti
    		         x=praviX;
    		         y=praviY;
    		         prosliY= praviProsliY;
    		         prosliX= praviProsliX;
    		         prosloVrijeme=  pravoProsloVrijeme;
    		         vrijemePauze=pravoVrijemePauze;
    		         preletioPik= pravoPreletioPik;
    		         this.kosiDodatY=praviKosiDodatY;
    		         kut=praviKut;
    		         xNaCilju=praviXNaCilju;
    		         yNaCilju=praviYNaCilju;
    		         prvoPokret=pravoPrvoPokret;
    		         this.delta=pravaDelta;
    		           /////////////////////////////////
    		
    		
    		
    	    racunanjeProslihPozicija=false;
        }
		return lista;
	}
    private float[][] izracunavanjePolozajaUProslostiInterni(int brTocaka){
		
		///dodati pravi dodatak na Y i spremiti dodatak na y iz proslog takta
    	this.prosloVrijeme+=this.vrijemePauze;
		float razlikaVremena=(float)(System.currentTimeMillis()-this.prosloVrijeme);
		float deltaVrijeme=razlikaVremena/brTocaka;
		//int brTocaka=Math.round((float)(System.currentTimeMillis()-this.prosloVrijemeTaktPrije)/deltaVrijeme);
		
		float[][] lista= new float[2][] ;
        if(xCilj>-10000||yCilj>-10000){
    		lista[0]= new float[brTocaka] ;
    		lista[1]= new float[brTocaka] ;
    		///sprema vrijednosti da ne upropasti izvođenje u logici
    	        
    	        	////promjene vrijednosti
    	        	
    	        	
    	   ///////////////////////////////////////////////////////////  
    	
    	 
    	    racunanjeProslihPozicijaInterni=true;
    	    tempDeltaRacProsPol=deltaPomak();
    	    tempTime=deltaVrijeme/1000;
    	    tempDelta= delta*tempTime;
			if(tempTime>0.5) tempDelta= 1/TpS;
    		for(int i=0;i<brTocaka;i++){
    			
    		    ///////////////////////////////////
    			 tempTime=deltaVrijeme/1000;
    	    	    tempDelta= delta*tempTime;
    				if(tempTime>0.5) tempDelta= 1/TpS;
    		
    			
    			////////////////////////////////////
    			
    			if(this.kosiHitacIliRavno){
    				//kut=izracunajKut(xCilj,yCilj);
    				pomakPremaKosiHitac(xCilj,yCilj);
    			}
    			else this.pomakPrema();
    			lista[0][i]=x;
    			lista[1][i]=y;
    			
    		
    			
    		}
    		           ////vracanje starih vrijednosti
    		      
    	   
        }
        racunanjeProslihPozicijaInterni=false;
		return lista;
	}
	private void pomakPremaKosiHitac(float xC, float yC){
		prvoPokretProsliTakt=prvoPokret;
		if(prvoPokret){//||naPozIliSlijedi){
			if(predvidiPolozaj){
				if(this.objCilj!=null){
			         	predvidiPolozajObjekta();
				        predvidiPolozajObjekta();
				        predvidiPolozajObjekta();
				}
			}
		
			xC=xCilj;
		    yC=yCilj;
			//// prvo pokretanje je i kada toranj ponovno pozove projektil na novi cilj
			ge.indikatorBorbe=2;// znaèi da leti
			
			kosiR=Math.abs(xC-pocetX)/2;
			kosiP=kosiR;
			kut=izracunajKut(xC,yC);
			kutProsliTakt=kut;
		    prvoPokret=false; 
		    kosiDodatY=0;
		    prosloVrijeme=this.pocIspucavanja;
		    preletioPik=false;
		    prosliY=y;
		    udaljenostPocetkaOdCilja=(float) udaljenostDvijeTocke(xC,yC,pocetX,pocetY);
		}

		
		   preletioPikTaktPrije=this.preletioPik;
			if(this. prosliY>y){
				preletioPik=true;
			}
			proslaDelta=delta;
			prosliY=y;
 			prosliX=x;
		     ////vremensko pomicanje
		   if(!racunanjeProslihPozicija){ 
			                         this.kosiDodatYIzProslTakta=kosiDodatY;
			                      if(  !racunanjeProslihPozicijaInterni)   tempDelta=deltaPomak();
			                         prosloVrijemeTaktPrije=prosloVrijeme+this.vrijemePauze;
			                         prosloVrijeme=System.currentTimeMillis();
			                         xNaCiljuProsliTakt=xNaCilju;
			                         yNaCiljuProsliTakt=yNaCilju;
			                         
		   }
		  
		//////
	    ////redukcija Y
		   
		y+=kosiDodatY;// oduzima iz prošlog ciklusa prošli dodatak tako da ne utjeèe na proraèun osnovnog smijera
		if(!this.naPozIliSlijedi){
			/*kosiR=Math.abs(xC-pocetX)/2;
			kosiP=kosiR; */
			 udaljenostPocetkaOdCilja=(float) udaljenostDvijeTocke(xC,yC,pocetX,pocetY);
			kutProsliTakt=kut;
			kut=izracunajKut(xC,yC);
		}
		
		kosiTempX=Math.abs(x-pocetX);
		double tempsqr=Math.abs(-kosiTempX*kosiTempX+2*kosiTempX*kosiP);
		kosiDodatY=(float)Math.sqrt(tempsqr);// cudna stvar se dogada la sa korijenom dok je prije njega bio abs , nije uopće racuna korjen negoje samo pusta istu vrijednost , a nead je poveća vrijednost za par stotina milijuna puta
		//kosiDodatY=(float)Math.abs(Math.sqrt(Math.abs((double)(-kosiTempX*kosiTempX+2*kosiTempX*kosiP))));
		//kosiDodatY=(float)Math.abs(Math.sqrt(Math.abs((double)(kosiR*kosiR-kosiTempX*kosiTempX+2*kosiTempX*kosiP-kosiP*kosiP))));
		
		kosiDodatY=kosiDodatY*koeficijentVisine;
	
		
		
		
	  
	
		/////
		////redukcija X
		tempDelta=(1-redukcijaXBrzine)*tempDelta+ this.redukcijaXBrzine*  (tempDelta)*tempDelta*Math.abs((float)Math.cos((double)(udaljenostDvijeTocke(x,y,xC,yC)/udaljenostPocetkaOdCilja)*3.14));
		//if(tempDelta>1*delta) tempDelta=1*delta;//ogranièavam maximalnu brzinu
		
		/////
		
		
		
		////////
		if((x>=xCilj-tempDelta/2)&&(x<=xCilj+tempDelta/2)){
			xNaCilju=true;
			//if(Math.abs(y-yC)>2*tempDelta) xNaCilju=false;// ako je preletiotijekom uzdizanja
			
		}
		else xNaCilju=false;
		if(y>yCilj&&preletioPik){
			y=yCilj;
			yNaCilju=true;
		}
		else if((y>=yCilj-tempDelta/2)&&(y<=yCilj+tempDelta/2)){
			yNaCilju=true;
			if(tempDelta*Math.sin(kut)<0){
				
				yNaCilju=false;// ako se uzdize onda nebi trebao registrirati  uvijek bi trebao padati prema cilju
			} 
			//if(Math.abs(x-xC)>2*tempDelta) yNaCilju=false;
		}
		else yNaCilju=false;
		if(!racunanjeProslihPozicija||!racunanjeProslihPozicijaInterni){
		       if((x>=xCilj-tempDelta/2)&&(x<=xCilj+tempDelta/2)){
		              	xNaCilju=true;
			             //if(Math.abs(y-yC)>2*tempDelta) xNaCilju=false;// ako je preletiotijekom uzdizanja
			
		         }
		       else xNaCilju=false;
		       if(y>yCilj&&preletioPik){
					y=yCilj;
					yNaCilju=true;
				}
				else if((y>=yCilj-tempDelta/2)&&(y<=yCilj+tempDelta/2)){
			           yNaCilju=true;
			           if(tempDelta*Math.sin(kut)<0){
				       yNaCilju=false;// ako se uzdize onda nebi trebao registrirati  uvijek bi trebao padati prema cilju
			         } 
		    	//if(Math.abs(x-xC)>2*tempDelta) yNaCilju=false;
		         }
		        else yNaCilju=false;
	      }
		else {
			  if((x>=xCilj-tempDeltaRacProsPol/2)&&(x<=xCilj+tempDeltaRacProsPol/2)){
				 xNaCilju=true;
	              	
		             //if(Math.abs(y-yC)>2*tempDelta) xNaCilju=false;// ako je preletiotijekom uzdizanja
		
	         }
	       else xNaCilju=false;
			  if(y>yCilj&&preletioPik){
					y=yCilj;
					yNaCilju=true;
				}
				else if((y>=yCilj-tempDeltaRacProsPol/2)&&(y<=yCilj+tempDeltaRacProsPol/2)){
		           yNaCilju=true;
		           if(tempDeltaRacProsPol*Math.sin(kut)<0){
			       yNaCilju=false;// ako se uzdize onda nebi trebao registrirati  uvijek bi trebao padati prema cilju
		         } 
	    	//if(Math.abs(x-xC)>2*tempDelta) yNaCilju=false;
	         }
	        else yNaCilju=false;
		}
		
		////
		if(!yNaCilju){if(y>yC) y-=tempDelta*Math.sin(kut);
		              else y+=tempDelta*Math.sin(kut);
		             }
		if(!xNaCilju){if(x>xC) x-=tempDelta*Math.cos(kut);
		             else x+=tempDelta*Math.cos(kut);
		              }
		//else kosiDodatY=0;
		y-=kosiDodatY;// dodaje na kraju tako da neutjeèe na proraèun osnovnog smijer
		
		//if(kosiDodatY<4*tempDelta)tempDelta+=kosiDodatY;// ovo se radi da nedoðe do greške u provjeri jeli na cilju, jer kosiDodatY radi grešku nekad
	}
	
	private void pomakPrema(){
		if(prvoPokret){
			tempDelta=deltaPomak();
			if(predvidiPolozaj&&this.objCilj!=null){
				predvidiPolozajObjekta();
				predvidiPolozajObjekta();
				predvidiPolozajObjekta();
			}
			kut=izracunajKut(xCilj,yCilj);//u sluèaju da slijedi stalno æe se izraèunavat novi kut, a u sluèaju da ide na poziciju samo prvi put
		    prvoPokret=false;
		    
		}
		if(!naPozIliSlijedi){
			kut=izracunajKut(xCilj,yCilj);
			}
		ge.indikatorBorbe=2;// znaèi da leti
		   if(!racunanjeProslihPozicijaInterni&&!racunanjeProslihPozicija){ 
		         tempDelta=deltaPomak();
		    
		         }
		   prosloVrijemeTaktPrije=prosloVrijeme+this.vrijemePauze;
           prosloVrijeme=System.currentTimeMillis();
           xNaCiljuProsliTakt=xNaCilju;
           yNaCiljuProsliTakt=yNaCilju;
		if(y>yCilj) y-=tempDelta*Math.sin(kut);
		else y+=tempDelta*Math.sin(kut);
		if(x>xCilj) x-=tempDelta*Math.cos(kut);
		else x+=tempDelta*Math.cos(kut);
		if((!racunanjeProslihPozicijaInterni&&!racunanjeProslihPozicija)||naPozIliSlijedi){
		       if((x>=xCilj-tempDelta/2)&&(x<=xCilj+tempDelta/2)){
		              	xNaCilju=true;
			             //if(Math.abs(y-yC)>2*tempDelta) xNaCilju=false;// ako je preletiotijekom uzdizanja
			
		         }
		       else xNaCilju=false;
		       if((y>=yCilj-tempDelta/2)&&(y<=yCilj+tempDelta/2)){
			           yNaCilju=true;
			           if(tempDelta*Math.sin(kut)<0){
				       yNaCilju=false;// ako se uzdize onda nebi trebao registrirati  uvijek bi trebao padati prema cilju
			         } 
		    	//if(Math.abs(x-xC)>2*tempDelta) yNaCilju=false;
		         }
		        else yNaCilju=false;
	      }
		else {
			  if((x>=xCilj-tempDeltaRacProsPol/2)&&(x<=xCilj+tempDeltaRacProsPol/2)){
	              	xNaCilju=true;
		             //if(Math.abs(y-yC)>2*tempDelta) xNaCilju=false;// ako je preletiotijekom uzdizanja
		
	         }
	       else xNaCilju=false;
	       if((y>=yCilj-tempDeltaRacProsPol/2)&&(y<=yCilj+tempDeltaRacProsPol/2)){
		           yNaCilju=true;
		           if(tempDeltaRacProsPol*Math.sin(kut)<0){
			       yNaCilju=false;// ako se uzdize onda nebi trebao registrirati  uvijek bi trebao padati prema cilju
		         } 
	    	//if(Math.abs(x-xC)>2*tempDelta) yNaCilju=false;
	         }
	        else yNaCilju=false;
		}
	}
	private double izracunajKut(float xC, float yC){
		double k=0;
		 k= Math.atan(Math.abs((y-yC)/(x-xC+0.00000000000000000001)));
		return k;
	}
	//// cesto koristene funkcije+
	private double udaljenostDvijeTocke(float ax,float ay, float bx,float by){// predpostavlja da se sve odvija u prvom kvadrantu
		 return Math.hypot(Math.abs(ax-bx),Math.abs(ay-by));
	}
	////////////////////////////////////////////////////////////////
	/////////////////setersi/////////////////////////////////////////
	public void setKonstantnuStetuPoslijePogotka(float helthPoSec,float armorPoSec,int tip,float sir,float vis, float vrijemeSec){
		recKonstStete=new RectF();
		recKonstStete.set(0, 0, sir, vis);
		this.vrijemeTrajanjaKonstantneStete=1000*vrijemeSec;
		this.inicijaliziranaKonstantnaSteta=true;
		 this.stetaArmorKonstantniPoSec=armorPoSec/1000;
		 stetaHelthKonstantniPoSec=helthPoSec/1000;
		 tipSteteKonstantni=tip;
	}
	public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){
		
	}
	public void pokreniSePremaCilju(float ciljx, float ciljy){
		pokrenutaKonstantnaSteta=false;
		pocIspucavanja=  prosloVrijeme=System.currentTimeMillis()-this.GL.getProsjekVremenaPetlje();
		naPozIliSlijedi=true; // oznacava da gada toèku
		ge.imTouched=false;// vraca na normalno iscrtavanje kuta u prikazu
		xCilj=ciljx;
		yCilj=ciljy;
		if(GL!=null)if(GL.faIgr!=null)maxOcekivanaVelObjekataPix=this.GL.faIgr.maxOcekivanaVelGradevinaZaPravilnoSortiranjeCm*this.GL.faIgr.yPiksCm;
		  if(toranjPozivatelj!=null){
		
		  if(ciljy>this.toranjPozivatelj.getRect().centerY()+toranjPozivatelj.getYVelUPrikazu()/2) ciljIznadIliIspodTornja=false;// znaèi da je ispod
		  else ciljIznadIliIspodTornja=true;
		      
		  }
		dosaNaCilj=false;
		ispaljenProjektil=true;
		this.x=this.pocetX;
		this.y=this.pocetY;
		updateRect();
		prosliY=y;
		prosliX=x;
		xNaCilju=false;
		yNaCilju=false;
		this.prvoPokret=true;
		ge.indikatorBorbe=1;// znaèi da je ispaljen
		ge.pomNaX=this.x;
		ge.pomNaY=y;
		posaljiStanjePrikazniku();
	}
//*	
public void pokreniSePremaCilju(GameLogicObject objCilj, boolean pozIliSlijedi){
		pokrenutaKonstantnaSteta=false;
		pocIspucavanja= prosloVrijeme=System.currentTimeMillis()-this.GL.getProsjekVremenaPetlje();
		if(GL!=null)if(GL.faIgr!=null)maxOcekivanaVelObjekataPix=this.GL.faIgr.maxOcekivanaVelGradevinaZaPravilnoSortiranjeCm*this.GL.faIgr.yPiksCm;
		this.naPozIliSlijedi=pozIliSlijedi; 
		ge.imTouched=false;// vraca na normalno iscrtavanje kuta u prikazu
		this. objCilj= objCilj;
		ge.objektLogike=objCilj;

		     xCilj= objCilj.getRect().centerX();//+ 2*objCilj.getDx();
		     yCilj= objCilj.getRect().centerY();//+2* objCilj.getDy();
		
		 if(toranjPozivatelj!=null){
			
			  if(yCilj>this.toranjPozivatelj.getRect().centerY()+toranjPozivatelj.getYVelUPrikazu()/2) ciljIznadIliIspodTornja=false;// znaèi da je ispod
			  else ciljIznadIliIspodTornja=true;
			  if(objCilj!=null){
				  if(this.objCilj instanceof GameLogicProtivnik){
					  GameLogicProtivnik temp = (GameLogicProtivnik)this.objCilj;
					 if( temp.jesamLileteci()){
						 jelCiljLeteci=true;
						pomaZaSortiranjeLeteceg=temp.getRecLeteceg().centerY()-temp.getRect().centerY();
						  if(temp.getRecLeteceg().centerY()>this.toranjPozivatelj.getRect().centerY()+toranjPozivatelj.getYVelUPrikazu()/2) ciljIznadIliIspodTornja=false;// znaèi da je ispod
						  else ciljIznadIliIspodTornja=true;
					 }
				  }
				  
			  }
			  }
		dosaNaCilj=false;
		ispaljenProjektil=true;
		this.x=this.pocetX;
		this.y=this.pocetY;
		
		updateRect();
		prosliY=y;
			prosliX=x;
		xNaCilju=false;
		yNaCilju=false;
		this.prvoPokret=true;
		ge.indikatorBorbe=1;// znaèi da je ispaljen
		ge.pomNaX=this.x;
		ge.pomNaY=y;
		posaljiStanjePrikazniku();
		 
	}
//*	
public void pokreniSePremaCilju(float xPoc,float yPoc,GameLogicObject objCilj, boolean pozIliSlijedi){
		pokrenutaKonstantnaSteta=false;
		if(GL!=null)if(GL.faIgr!=null){maxOcekivanaVelObjekataPix=this.GL.faIgr.maxOcekivanaVelGradevinaZaPravilnoSortiranjeCm*this.GL.faIgr.yPiksCm;
		                             pocIspucavanja= prosloVrijeme=System.currentTimeMillis()-this.GL.getProsjekVremenaPetlje();
	                                 }
		
		this.naPozIliSlijedi=pozIliSlijedi; 
		ge.imTouched=false;// vraca na normalno iscrtavanje kuta u prikazu
		this. objCilj= objCilj;
		ge.objektLogike=objCilj;

		     xCilj= objCilj.getRect().centerX();//+ 2*objCilj.getDx();
		     yCilj= objCilj.getRect().centerY();//+2* objCilj.getDy();
		
		//predvidiPolozajObjekta();
		if(toranjPozivatelj!=null){
			  if(yCilj>this.toranjPozivatelj.getRect().centerY()+toranjPozivatelj.getYVelUPrikazu()/2) ciljIznadIliIspodTornja=false;// znaèi da je ispod
			  else ciljIznadIliIspodTornja=true;
			  }
		  if(objCilj!=null){
			  if(this.objCilj instanceof GameLogicProtivnik){
				  GameLogicProtivnik temp = (GameLogicProtivnik)this.objCilj;
				 if( temp.jesamLileteci()){
					 jelCiljLeteci=true;
					 pomaZaSortiranjeLeteceg=temp.getRecLeteceg().centerY()-temp.getRect().centerY();
					  if(temp.getRecLeteceg().centerY()>this.toranjPozivatelj.getRect().centerY()+toranjPozivatelj.getYVelUPrikazu()/2) ciljIznadIliIspodTornja=false;// znaèi da je ispod
					  else ciljIznadIliIspodTornja=true;
				 }
			  }
			  
		  }
		dosaNaCilj=false;
		ispaljenProjektil=true;
		pocetX=xPoc;
		pocetY=yPoc;
		
		this.x=this.pocetX;
		this.y=this.pocetY;
		updateRect();
		prosliY=y;
			prosliX=x;
		xNaCilju=false;
		yNaCilju=false;
		this.prvoPokret=true;
		ge.indikatorBorbe=1;// znaèi da je ispaljen
		ge.pomNaX=this.x;
		ge.pomNaY=y;
		posaljiStanjePrikazniku();
	}
	public void pokreniSePremaCilju(float xPoc,float yPoc,float xCilj, float yCilj){
		pokrenutaKonstantnaSteta=false;
		pocIspucavanja= prosloVrijeme=System.currentTimeMillis()-this.GL.getProsjekVremenaPetlje();
		if(GL!=null)if(GL.faIgr!=null)maxOcekivanaVelObjekataPix=this.GL.faIgr.maxOcekivanaVelGradevinaZaPravilnoSortiranjeCm*this.GL.faIgr.yPiksCm;
		naPozIliSlijedi=true; // oznacava da gada toèku
		ge.imTouched=false;// vraca na normalno iscrtavanje kuta u prikazu
		this.xCilj=xCilj;
		this.yCilj=yCilj;
		if(toranjPozivatelj!=null){
			  if(yCilj>this.toranjPozivatelj.getRect().centerY()+toranjPozivatelj.getYVelUPrikazu()/2) ciljIznadIliIspodTornja=false;// znaèi da je ispod
			  else ciljIznadIliIspodTornja=true;
			  }
		dosaNaCilj=false;
		ispaljenProjektil=true;
		pocetX=xPoc;
		pocetY=yPoc;
		this.x=this.pocetX;
		this.y=this.pocetY;
		updateRect();
		prosliY=y;
			prosliX=x;
		xNaCilju=false;
		yNaCilju=false;
		this.prvoPokret=true;
		ge.indikatorBorbe=1;// znaèi da je ispaljen
		ge.pomNaX=this.x;
		ge.pomNaY=y;
		posaljiStanjePrikazniku();
	}
	public void ubijSe(){ //
		posaljiDaUmiremPrikazniku();
	    GL.mrtavSam(this);
	}
	////////////////////////////////////////////////////////////////
	///////////////metode od interfacea game logic object//////////////////////
	public float getXVelUPrikazu(){
		return  dvojnik.getGlavniRectPrikaza().width();
	}
	public float getYVelUPrikazu(){
		return dvojnik.getGlavniRectPrikaza().height();
	}
	@Override
	public int getOblZaKol(){
		return 5;// znaèi da je kvadrat
	}
	public int getIndikator() {
		// TODO Auto-generated method stub
		return indikator;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public float getDx() {
		// TODO Auto-generated method stub
		return delta;
	}

	@Override
	public float getDy() {
		// TODO Auto-generated method stub
		return delta;
	}

	@Override
	public RectF getRect() {
		// TODO Auto-generated method stub
		return rec;
	}

	
	@Override
	public void maliRun(GameLogic GL, FazeIgre FI, float TpS, boolean pause) {
		// TODO Auto-generated method stub
		this.GL=GL;
		this.TpS=TpS;
		 listaIzracunatiMedupolozaja=null;
		if(izracunajPauzu){
			vrijemePauze=System.currentTimeMillis()-vrijemePauze;
			izracunajPauzu=false;
		}
		if(dosaNaCilj&&ispaljenProjektil){
			    updateRect();
			    if(pokrenutaKonstantnaSteta){
			    	rec.set(rec.centerX()-this.recKonstStete.width()/2,rec.centerY()-this.recKonstStete.height()/2, rec.centerX()+this.recKonstStete.width()/2,this.recKonstStete.height()/2 +rec.centerY());
			    }
			   
			    if(!pokrenutaKonstantnaSteta){
			    	  if(towerBuster){
			    		  naciniStetuTornju();
                      }
			          if(this.ostecujemSamoJednog){  
			    	                           if(this.napadamProtivnike)naciniStetuJednomProtivniku();
			                                   if(this.napadamBranitelje)naciniStetuJednomBranitelju();
			                                  
			                                 }
			          else{
			    	                           if(this.napadamProtivnike)naciniStetuProtivnicima();
                                               if(this.napadamBranitelje)naciniStetuBraniteljima();
			                                   }
			  /////////vazno da posalje trenutaènu poziciju radi prikaza eksplozije
					    ge.indikatorBorbe=3;// znaèi da je pogodio
					    posaljiStanjePrikazniku();
					    this.objCilj=null;
			    }
			   
			    
				////////////////////////////////
			    if(this.inicijaliziranaKonstantnaSteta&&!pokrenutaKonstantnaSteta){
			    	vrijemeZadnjeKonstantnaSteta=System.currentTimeMillis();
			    	 ge.indikatorBorbe=0;
			    	 posaljiStanjePrikazniku();
			    	this.vrijemeKadaJePokrenutaKonstantnaSteta=System.currentTimeMillis();
			    	this.pokrenutaKonstantnaSteta=true;
			    	
			    
			    }
			    if(pokrenutaKonstantnaSteta){
			    	
			    	naciniKonstantnuStetuProtivnicima();
			    }
			    else{
			    	x=pocetX;
				    y=pocetY;
				    updateRect();
				    ispaljenProjektil=false;
				    prvoPokret=true;
				    xNaCilju=false;
				    yNaCilju=false;
			    }
		}
		else {
	      if(!this.zrakaProjektil){	
		      if(!naPozIliSlijedi&&ispaljenProjektil) {// u sluèaju da nema fiksni cilj
	
		  
		  
		                  if(daliSeProtivnikTeleportira()){///iskljucujem projektil izvrsit će eksploziju
		                	  
		                	    ge.indikatorBorbe=3;
		                	    posaljiStanjePrikazniku();
							    this.objCilj=null;
		                  }
		                  else{  
		                		if(objCilj!=null) {
		                	        xCilj= objCilj.getRect().centerX();//+ 2*objCilj.getDx();
		    		               yCilj= objCilj.getRect().centerY();//+2* objCilj.getDy();
		                		}
		                  }
		      
		    		
		        	//predvidiPolozajObjekta();
			//predvidiPolozajObjekta();
		          }
	       	if(!dosaNaCilj&&ispaljenProjektil){
			   // if( kosiHitacIliRavno){
			    	//pomakPremaKosiHitac(xCilj,yCilj);
			    	 listaIzracunatiMedupolozaja=izracunavanjePolozajaUProslostiInterni(20);
			    
			    	
			   // }
		     	//else pomakPrema();
			
		//	y+=this.kosiDodatY;//oduzima dodatak za kosi hitac tako da se provjerava kao da je putanja na pravcu, inaèe je ta varijabla u nul	
			/*if((x>=xCilj-tempDelta/2)&&(x<=xCilj+tempDelta/2)) {
				x=xCilj;
				xNaCilju=true;
				ge.imTouched=true;// koeistit ce se da bi se poslalo prikazu da je jedna dimenzija na cilju dsa nebi bilo naglog loma kuta projektila
			}
			if((y>=yCilj-tempDelta/2)&&(y<=yCilj+tempDelta/2)){
				y=yCilj;
				ge.imTouched=true;
				yNaCilju=true;
			}*/
			      if(xNaCilju||yNaCilju)ge.imTouched=true;
		       	  if(xNaCilju&&yNaCilju)
			        {
			            x=xCilj;
			            y=yCilj;
			            updateRect();// tek kad doðe u položaj pomièe rect
			            dosaNaCilj=true;
			           }   
			      posaljiStanjePrikazniku();
		//	y-=this.kosiDodatY;
		          }
	         }
	        else if(ispaljenProjektil){
	        
	            if(daliSeProtivnikTeleportira()){///iskljucujem projektil izvrsit će eksploziju
              	  
            	    ge.indikatorBorbe=0;
            	    posaljiStanjePrikazniku();
				    this.objCilj=null;
              }
	            else {
	            	if(objCilj!=null){
	    		          x= objCilj.getRect().centerX();//+ 2*objCilj.getDx();
	    		         y= objCilj.getRect().centerY();//+2* objCilj.getDy();
	            	}
		                ge.indikatorBorbe=1;
		               if(this.vrijStojNaObj+this.pocIspucavanja-vrijemePauze<System.currentTimeMillis()){
			                                 updateRect();
			                               dosaNaCilj=true;
			                              ge.indikatorBorbe=0;
		                              }
		                     posaljiStanjePrikazniku();
	                      }
	                }
	     }
	   if(pause){
		    izracunajPauzu=true;
			vrijemePauze=System.currentTimeMillis();
		}
		else vrijemePauze=0; 
	   
	  
	}

	
///////////////////////////////////////////////////////////////
///////////////metode interfacea object linker logic
	public void setStetu(int tip,float helth,float armor){
		
	}	

	@Override
	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
		dvojnik=obj;
	}
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		
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
