package com.rugovit.igrica.engine.logic.elements;
import java.util.LinkedList;

import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.IzbornikZaToranj;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class ToranjLAlkemicar extends ToranjL implements GameLogicObject {
	private boolean teleport=false,medic=false;
	private double teleportLastTime;
	private float teleportFireTime=4000;
	private int brProtZaTelepor=1; // koliko ce protivnnika teleportirati
    private GameLogicObject[] listaSudaraMedic;
	private double medicLastTime=0;
	private float helthPoSec;
	private float armorPoSec;
	private float provjeravajObliznjeSvakih=1000;
	private double zadnjaProvjeraOblitznjih;
	
	private float velXUPrik=0;
	private float velYUPrik=0;
	private double  vrijemeIspucavanja, RofFMili;
	private double vrijemePauze=0;
	private boolean izracunajPauzu=false;
	private ProjektilL projektil1;
	private ProjektilL projektil2;
	private GameLogicObject tempProtivnik;
	private GameLogicProtivnik temp1Prot=null;
	private GameLogicProtivnik temp2Prot=null;
	private GameLogicProtivnik temp3Prot=null;
	private GameLogicProtivnik tempStari=null;
	private GameLogicProtivnik tempNovi=null;
	private float xIspaljivanja=-1;
	private float yIspaljivanja=-1;
	///////////////////
	///////izbornik varijable
	private IzbornikZaToranj izbor;

	//////////////////////////
	////zastavice
	private boolean pocObukeNovogVojnika=false;
	private boolean izborNaMeni=false;
	private boolean tekPoceo=true;
	///////
  
    private UIManagerObject dvojnik;
    private GameLogic GL;
	private FazeIgre FI;
	private SpriteHendler sprHendZaProjekt;
	private GameLogicObject[] objPoPravcima; 
	private GameLogicObject maxDolje, maxGore,maxDesno,maxLijevo;
	private int indikator;
	private float rateOfFire;
	private int ticProvSud=0;
	private float xTor,yTor, radijus;
	//private float rTor;
	private RectF rec;
	private Math mat;
	private float TpS=10;// ticks per sec, broj poziva ove klase po sekundi definiro sam ga da je u startu 10 ali to se može promjeniti pri pozivu malog runa
	private int ticFire,iFire=0;// sprema koliko puta se treba pozvati ova klasa prije nego se ispali projektill
	private float xKraj,yKraj; // x i y kraja kojeg se brani
	private float razVrhSlIsp;// razlika vrh wlicice i ispaljivanja
	private GameLogicObject[] listaSudara;// spremaju se objekti sa kojima se sudara
	//////// varijable od tornja kasarne
	private float xOkupljanja,yOkupljanja;
	private double startTime=0;
	private float secDoNovogVojnika=4;// sekunde do kada æe se gennerirati novi vojnik ako fali koji defolt 10
	private int maxBrVojnika=4;// maksimalni broj vojnika, defolt 4
	private LinkedList<GameLogicBranitelj> listaGrupe;// svaki æe toranj  održavat listu grupe za svoje objekte
	///////////////////
	private float xGrup,yGrup;
	private boolean jesamLiZiv=true;
	public ToranjLAlkemicar(float xTor, float yTor,float radijus,float rOfF,IzbornikZaToranj izbor,int indikator){
		super(indikator,izbor,xTor,yTor,radijus);
		this.yTor=yTor;
		this.xTor=xTor;
		this.indikator=indikator;
		this.radijus=radijus;
		this.izbor=izbor;
		this.rateOfFire=rOfF;
		listaGrupe=new LinkedList<GameLogicBranitelj>();
		srediZastavice();
		ge=new GameEvent(this);
		 RofFMili=1000/rOfF;
		objPoPravcima=new GameLogicObject[4];
		rec=new RectF(xTor,yTor,radijus*2+xTor,radijus*2+yTor);// treba mi rect zbog toga što ga zahtjevaju drugi djelovi igrice
	}
	private void srediZastavice(){// sa istom klasom æu izraðivat više vrsta tornjeva
         if(indikator==150){
        	
        	 
         }
         
         
		
	}
///////////PUBLIC METODE////////////////////////////////////////////  
	 
	public void stvoriTeleport(int brProtZaTel, float vrijemeIzmeduTelSec){
		this.brProtZaTelepor=brProtZaTel;
		this.teleport=true;
		this.teleportFireTime=1000*vrijemeIzmeduTelSec;
	}
	public void stvoriMedic(float helthPoSec,float armorPoSec){
		this.helthPoSec=helthPoSec/1000;
		this.armorPoSec=armorPoSec/1000;
		this.medic=true;
		

	}
	@Override
	public void ubijMe(){
		if(this.projektil1!=null){
			projektil1.ubijSe();
		}
		if(this.projektil2!=null){
			projektil2.ubijSe();
		}
		  ge.jesamLiZiv=false;
		  dvojnik.GameLinkerIzvrsitelj(ge);
		  GL.mrtavSam(this);
	 }
 public void setPocIspaljivanja(float xIspa, float yIspa){
    		 this.xIspaljivanja=xIspa;
    		 this.yIspaljivanja=yIspa;
   }        
 public void setXVelUPrik(float xp){
	 velXUPrik=xp;
 }
 public void setYVelUPrik(float yp){
	 velYUPrik=yp;
 }	
 public void setRateOfFire(float rof){
	 rateOfFire=rof;
	 RofFMili=1000/rof;
 }
 public void izbornikNaMeni(boolean b){
	 //izborNaMeni=b;
	
 }	
 public void namjestiOkupljaliste(float x, float y){// kada izlaze iz kasarne gdje æe se okupit
	 xOkupljanja=x;
	 yOkupljanja=y;
 }	
 public void namjestiCudoviste(int brVoj, float sec){// ova æe se metoda koristiti prilikom stvaranja ovog objekta
	 this.maxBrVojnika=brVoj;
	 this.secDoNovogVojnika=sec*1000;
 }	
 public void vratiTouchPolje(int polje){// vraæa polje na koje je kliknuto
/////////////toranj alkemicar pve razine Test
	 if(indikator==175){
	
	 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(176)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(176));	 
		    FI.br176ToranjAlkemicar2Razina(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==176){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(177)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(177));	 
		    FI.br177ToranjAlkemicar3Razina(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==177){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(178)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(178));	 
		    FI.br178ToranjAlkemicarTeleport(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	      else if( GL.getUkupniNovac()>=FI.cijenaObjecta(179)&&polje==4){//TORANJ  alkemicar dugmiæ 4
			    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(179));	 
			    FI.br179ToranjAlkemicarMedic(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
			    ubijMe();
		      }
	 }
	 else if(indikator==178){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(180)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(180));	 
		    FI.br180ToranjAlkemicarTeleport1Rank(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==180){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(182)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(182));	 
		    FI.br181ToranjAlkemicarTeleport2Rank(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==181){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(182)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(182));	 
		    FI.br182ToranjAlkemicarTeleport3Rank(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==179){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(183)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(183));	 
		    FI.br183ToranjAlkemicarMedic1Rank(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==183){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(184)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(184));	 
		    FI.br184ToranjAlkemicarMedic2Rank(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
	 else if(indikator==184){
			
 		 
	      if( GL.getUkupniNovac()>=FI.cijenaObjecta(185)&&polje==3){//TORANJ  alkemicar dugmiæ 4
		    GL.dodajNovacPlusMinus(-FI.cijenaObjecta(185));	 
		    FI.br185ToranjAlkemicarMedic3Rank(this.xTor+this.radijus,this.yTor+this.radijus,this.getNovacZaProdaju());
		    ubijMe();
	      }
	 }
 }	

//////////////////////////////////////////////////////////////////////	
///////privatne metode///////////////////////////////////////7//
 private void izljeciObliznje(){
	 float heltTemp=0;
	 float armorTemp=0;
	   medicLastTime+=this.vrijemePauze;
	 if(medicLastTime!=0){
	           heltTemp=(float)((this.helthPoSec*(System.currentTimeMillis()-medicLastTime)));
	           armorTemp=(float)((this.armorPoSec*(System.currentTimeMillis()-medicLastTime)));
	 }
	
	 boolean naisaNaPatnike=false;
	if(listaSudaraMedic!=null) for(int i=0;this.listaSudaraMedic.length>i;i++){
		 
		 if(listaSudaraMedic[i]!=null)if(this.listaSudaraMedic[i].getIndikator()<100){
		
		     GameLogicBranitelj temp=( GameLogicBranitelj )listaSudaraMedic[i];
		     naisaNaPatnike=naisaNaPatnike||temp.lijeciMe(heltTemp, armorTemp);
		     
		 }
	 }
	 medicLastTime=System.currentTimeMillis();
	 this.ge.medic=naisaNaPatnike;
 }
 private void   teleportiraj(){
	 izvuciObjKojegSeGadaTeleport();
	 
	 if(temp1Prot!=null)if(this.brProtZaTelepor>0){
		Protivnik temp= (Protivnik)temp1Prot;
		temp.teleportirajMe();
		this.ge.teleportacija=true;
		 teleportLastTime=System.currentTimeMillis();
		 //this.ge.teleportacija=false;
	 }
	 if(temp2Prot!=null) if(this.brProtZaTelepor>1){
    	Protivnik temp= (Protivnik)temp2Prot;
		temp.teleportirajMe();
		
	}
	 if(temp3Prot!=null) if(this.brProtZaTelepor>2){
    	Protivnik temp= (Protivnik)temp3Prot;
		temp.teleportirajMe();
		
		 
	 }
	 
	
 }
 private GameLogicObject izvuciObjKojegSeGadaTeleport(){
		
		
		tempStari=null;
		tempNovi=null;
		temp3Prot= null;
	   temp2Prot= null;
	   temp1Prot=null;
         for(int i=0;listaSudara.length>i;i++){
         if(listaSudara[i]!=null){ 	
			  if(listaSudara[i].getIndikator()>=-100&&listaSudara[i].getIndikator()<=-1){// provjerava dali je igraè od protivnika
				  tempNovi=(GameLogicProtivnik)listaSudara[i];
				  
			if(!tempNovi.jesamLiNaInertnomPutu()&&tempNovi.getRedBrPutaNaKojemSi()>0){	  
			    if(tempStari==null){
				   tempStari=tempNovi;
				   temp3Prot= temp2Prot;
		    	   temp2Prot= temp1Prot;
		    	   temp1Prot= tempStari;
			    }
			       else if(tempNovi.getRedBrPutaNaKojemSi()>tempStari.getRedBrPutaNaKojemSi()){
			    	   tempStari=tempNovi;
			    	   temp3Prot= temp2Prot;
			    	   temp2Prot= temp1Prot;
			    	   temp1Prot= tempStari;
			    	  
			    	   }
			       else {
			    	   if(temp2Prot==null){
			    		   temp2Prot= tempNovi;
			    	   }
			    	   else  if(tempNovi.getRedBrPutaNaKojemSi()>temp2Prot.getRedBrPutaNaKojemSi()){
			    		   temp3Prot= temp2Prot;
				    	   temp2Prot= tempNovi;
				    	   
			    	   }
			    	   else if(temp3Prot==null){
			    		   temp3Prot= tempNovi;
			    	   }
			    	   else  if(tempNovi.getRedBrPutaNaKojemSi()>temp3Prot.getRedBrPutaNaKojemSi()){
			    		   
				    	   temp3Prot= tempNovi;
				    	   
			    	   }
			       }
			       }
                }
			       }
			
           }
		   
		
		return (GameLogicObject)tempStari;
	}
 private GameLogicObject izvuciObjKojegSeGada(){
		
		tempStari=null;
		tempNovi=null;
		temp3Prot= null;
 	   temp2Prot= null;
 	   temp1Prot=null;
            for(int i=0;listaSudara.length>i;i++){
            if(listaSudara[i]!=null){ 	
			  if(listaSudara[i].getIndikator()>=-100&&listaSudara[i].getIndikator()<=-1){// provjerava dali je igraè od protivnika
				  tempNovi=(GameLogicProtivnik)listaSudara[i];
				  
			if(!tempNovi.jesamLiNaInertnomPutu()){	  
			    if(tempStari==null){
				   tempStari=tempNovi;
				   temp3Prot= temp2Prot;
		    	   temp2Prot= temp1Prot;
		    	   temp1Prot= tempStari;
			    }
			       else if(Math.abs(tempNovi.getRedBrPutaNaKojemSi())>Math.abs(tempStari.getRedBrPutaNaKojemSi())) {
			    	   tempStari=tempNovi;
			    	   temp3Prot= temp2Prot;
			    	   temp2Prot= temp1Prot;
			    	   temp1Prot= tempStari;
			    	  
			    	   }
			       else {
			    	   if(temp2Prot==null){
			    		   temp2Prot= tempNovi;
			    	   }
			    	   else  if(Math.abs(tempNovi.getRedBrPutaNaKojemSi())>Math.abs(tempStari.getRedBrPutaNaKojemSi())) {
			    		   temp3Prot= temp2Prot;
				    	   temp2Prot= tempNovi;
				    	   
			    	   }
			    	   else if(temp3Prot==null){
			    		   temp3Prot= tempNovi;
			    	   }
			    	   else  if(Math.abs(tempNovi.getRedBrPutaNaKojemSi())>Math.abs(tempStari.getRedBrPutaNaKojemSi())) {
			    		   
				    	   temp3Prot= tempNovi;
				    	   
			    	   }
			       }
			       }
                   }
			       }
			
              }
		   
		
		return (GameLogicObject)tempStari;
	}
 private void ispucajProjektil(){
		// projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik.getRect().centerX(),this.tempProtivnik.getRect().bottom);
		if(projektil1.jesiDosaoNaCilj()) {
			projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik,false);
		}
		/*else if(projektil2.jesiDosaoNaCilj()){
			projektil2.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik,false);
		}*/
	 }
	 private void stvariKOjeSeIzvrsavajuSamoJedanput(){// samo æe se izvršit na pocetku ova metoda
		 dodajCijenuBotunima();
	
		if(xIspaljivanja==-1&&yIspaljivanja==-1){ xIspaljivanja=this.rec.centerX();
		                                    yIspaljivanja=this.rec.top;
		                           }
		 this.vrijemeIspucavanja=System.currentTimeMillis();
		 ////Toranj kasarna 1 razine
		 listaSudara=GL.provjeriKoliziju(this);
		 stvoriProjektilePripadajucegTipa(); 
	 }
	 private void stvoriProjektilePripadajucegTipa(){
			 /*projektil1 =GL.faIgr.br403ProjektilFreezZraka(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br403ProjektilFreezZraka(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);*/
		 if(indikator==175){
		          projektil1 =GL.faIgr.br412ProjektilFreezTop1Razina(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		          projektil2 =GL.faIgr.br412ProjektilFreezTop1Razina(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		 }
		 else if(indikator==176){
			   projektil1 =GL.faIgr.br413ProjektilFreezTop2Razina(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
	          // projektil2 =GL.faIgr.br413ProjektilFreezTop2Razina(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		 }
		 else if(indikator>=177||indikator<=185){
			   projektil1 =GL.faIgr.br403ProjektilFreezZraka(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
	           projektil2 =GL.faIgr.br403ProjektilFreezZraka(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		 }
	 }
	    private void posaljiPrikazu(){
	    	ge.vrijemePauze=(int) vrijemePauze;
	    	dvojnik.GameLinkerIzvrsitelj(ge);
	    	 ge.izbornikUpaljen=izborNaMeni;
	    }
 /*private void ubijMe(){
	  ge.jesamLiZiv=false;
	  dvojnik.GameLinkerIzvrsitelj(ge);
	  GL.mrtavSam(this);
 }*/
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
	 if(indikator==175){ 
		 cijenaBotun3=FI.cijenaObjecta(176);
     }
	 else  if(indikator==176){ 
		 cijenaBotun3=FI.cijenaObjecta(177);
     }
	 else  if(indikator==177){ 
		 cijenaBotun3=FI.cijenaObjecta(178);
		 cijenaBotun4=FI.cijenaObjecta(179);
     }
	 else  if(indikator==178){ 
		 cijenaBotun3=FI.cijenaObjecta(180);
     }
	 else  if(indikator==180){ 
		 cijenaBotun3=FI.cijenaObjecta(181);
     }
	 else  if(indikator==181){ 
		 cijenaBotun3=FI.cijenaObjecta(182);
     }
	 else  if(indikator==179){ 
		 cijenaBotun3=FI.cijenaObjecta(183);
     }
	 else  if(indikator==183){ 
		 cijenaBotun3=FI.cijenaObjecta(184);
     }
	 else  if(indikator==184){ 
		 cijenaBotun3=FI.cijenaObjecta(185);
     }

 }
 private void stvoriNovogVojnikaPripadajuæegTipa(){
	 if(indikator==150){
	     GameLogicBranitelj  temp;
         temp=FI.br1VojnikRazina1(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
 }
 private void stvoriNovogVojnikaAkojeVrijeme888(){
	 if(listaGrupe.size()<this.maxBrVojnika){
		  if(pocObukeNovogVojnika==false){
			  startTime= System.currentTimeMillis();
			  pocObukeNovogVojnika=true;
		  }
		  else {
			  if(startTime+secDoNovogVojnika<System.currentTimeMillis()){
				  stvoriNovogVojnikaPripadajuæegTipa();
				  pocObukeNovogVojnika=false;
			  }
		  

		 }
	 } 
 } 
/////////////////////////////////////////////////////////////////	
///// metode od interfejsa gamelogicobject//////////////////////
	public float getXVelUPrikazu(){
		return  velXUPrik;
	}
	public float getYVelUPrikazu(){
		return  velYUPrik;
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
		this.FI=FI;
		this.GL=GL;
		izborNaMeni=izbor.izbornikNaMeni(this);
		if(tekPoceo){// 
			super.dodajReference(GL,FI);
			stvariKOjeSeIzvrsavajuSamoJedanput();
			tekPoceo=false;
		}
		if(izracunajPauzu){
			vrijemePauze=System.currentTimeMillis()-vrijemePauze;
			izracunajPauzu=false;
		}
		if(ge.bool1=true){
		if(izborNaMeni)izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
		   this.TpS=TpS;
		   ticFire=Math.round(TpS/rateOfFire);
		   listaSudara=null;
		   if(this.medic){
			  
			   zadnjaProvjeraOblitznjih+=this.vrijemePauze;
	
		
			   if( System.currentTimeMillis()>zadnjaProvjeraOblitznjih+  provjeravajObliznjeSvakih){
				   listaSudaraMedic= GL.provjeriKoliziju(6, this.rec);
				   zadnjaProvjeraOblitznjih=System.currentTimeMillis();
			   }
			   izljeciObliznje();
			   
		   }
		   if(this.teleport){
			   teleportFireTime+=vrijemePauze;
			   
			   if(System.currentTimeMillis()>this.teleportFireTime+this.teleportLastTime ){
				   listaSudara= GL.provjeriKoliziju(this);
				   teleportiraj();
				   
			   }
		   }
		   if(vrijemeIspucavanja+this.RofFMili+vrijemePauze<System.currentTimeMillis()){
			   listaSudara= GL.provjeriKoliziju(this);
			   tempProtivnik=izvuciObjKojegSeGada();
		          if(tempProtivnik!=null){
		            // if(prosloVrijeme+rateOfFire*1000<System.currentTimeMillis()){
		        	  ge.indikatorBorbe=2;// znaèi da je ispaljen
		        	  posaljiPrikazu();// ovo sam stavio ovdje jer je u izvrsitelju od prkaza metoda koja ce vratiti nazad podatke od pocetku 
				      ispucajProjektil();
				      ge.pomNaX=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
				      ge.pomNaY=tempProtivnik.getRect().centerY();
				      this.vrijemeIspucavanja=System.currentTimeMillis();
		              iFire=0;
		          
		          }
		          else ge.indikatorBorbe=3;// znaci da nema nikoga
		     }
		     //else  ge.indikatorBorbe=0;// znaèi da se ništa ne dogada
		   else{ 
			  if(this.ticProvSud>=ticFire/4){ listaSudara= GL.provjeriKoliziju(this);
			                                  tempProtivnik=izvuciObjKojegSeGada();
			                                  if(tempProtivnik!=null){
			                            	      ge.pomNaX=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
			            					      ge.pomNaY=tempProtivnik.getRect().centerY();
			            					      
			                                	  ge.indikatorBorbe=1;// znaèi da cilja
			                                  }
			                                  else ge.indikatorBorbe=3;// znaèi da nema nikoga
			                                  ticProvSud=-1;// nije nula zato što æe ga poslije odmah inkriminirati za jedan
			                                  }
			  else ge.indikatorBorbe=0;// ovo znaèi jednostavno da ema podataka jer se nije oèitao colision
			      ticProvSud++;
		          iFire++;
		       }
		}
		ge.helth=1000/this.rateOfFire;// salje vrijeme izmeðu dva ispaljivanja, pošto se ne koristi helth za tornjeve
		if(!super.jesamLiZiv()) ubijMe();
		posaljiPrikazu();
		 if(pause){
			    izracunajPauzu=true;
				vrijemePauze=System.currentTimeMillis();
			}
			else vrijemePauze=0; 
	}
/////////////////////////////////////////////////////////	
////////metode od interfacea gamelinkerologic////////////
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		if(e.imTouched){
			izbor.pokreniMojIzbornik(this,izborStanjeNekoristenihBotuna());// ako detektira da je kliknuto na njega pokreæe izbornik
		  //  izborNaMeni=true;
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
