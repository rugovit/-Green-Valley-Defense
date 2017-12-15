package com.rugovit.igrica.engine.logic.elements;

import java.util.LinkedList;
import java.util.Random;

import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.ObjectPrikaza;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class Protivnik implements GameLogicProtivnik { //,ObjectLinkerLogic{
	private int vrijedimZivota=0;
	private float pomakOdY, pomakOdX;
	private float trimIspaOdY, trimIspaOdX;
	private boolean pokrenutoPucanje=false;
	private double pocetakPucanja=0;
	private boolean pokreniPorod=false;
	private boolean jesamLiRoditelj=false;
	private int brojDjece=0;
	private double vrijemePoroda=0;
	private int vrijemeTrajanjaTrudnoce=0;
	private int tipDjeteta=0;
	private int tikIspPoradaja=-1;
	
	private boolean jesamLiLeteci=false;
	private RectF recLeteceg;
	private float[] xTel;
	private float[] yTel;
	private float[] sekundaStojanja;
	private int iClanaTeleportacije=0;
	private boolean pokreniSlijedTeleportacija=false;
	private double pocetakSlijedaTeleportacije=0;
	private boolean jesamLiImunNaKonstantuStetu=false;
	private double  vrijemeIspucavanja, RofFMili;
private int vrijemeCekanjaPoslijeHitca;
	private boolean jesamLiProtivnikSaViseZivota=false;
	private int brZivota;
	private int brZivotaPocetni;
	private int brKopiranja;
	private boolean animacijaTelepotacijePriRodnju=false;
	private boolean animacijaIzlaskaIzPodaPriRodnju=false;
	private float xPolTelportacije,yPolTeleportacije;
	
	private boolean jesamLiNaInertnomPutu=false;
	private double prosloVrijemeUdarca;
	///varijable od strijelaca
	private boolean jesamLiTowerBuster=false;
		private float radijusStrijelci;
		    private RectF recStrijelci;
		    private boolean jesamLiStrijelac=false;
	      	private ProjektilL projektil1;
	 
		    private GameLogicObject tempProtivnik;
		    private float rateOfFire;
		    private int ticProvSud=0;
		    private int ticFire,iFire=0;// sprema koliko puta se treba pozvati ova klasa prije nego se ispali projektill
		    private GameLogicObject tempStari=null;
		    private GameLogicObject tempNovi=null;
		//////////////////
/////// konstante
	private float razmakCrtanjaIHodaPoCestiX,razmakCrtanjaIHodaPoCestiY;
	private boolean zivSam=true;
	private float dxZaPrik,dyZaPrik;
	private boolean izracunajPauzu=false;
	private double vrijemePauze=0;
	private float polNaCesti;// odreðuje gdje se treba nalaziti lik na cesti
	private float vrijednost;
	private float pocetniHelth;// služi za izraèinavanje postotka kada se šalje prikazniku
	private float pocetniArmor;
	private float velXUPrik,velYUPrik;
	private float dx,dy,omjDx,omjDy; // množi se za brzinom tako da se dobiju brži i sporiji objekti
	private UIManagerObject touchObj;// objekt na koji je kliknuto poslije klika na ovaj
	private  float startX,startY,velX,velY ;
	private int indikator;
	private GameLogic GL;
	///// varijable
	private int redBrPuta=0;
	   // varijable za korekciju polozaja
	      private PutL tempPutNaKojemSi;
	      private GameLogicObject tempObj;
	      private float tempZeljeniPolozaj;
	private float tempTime, tempMnoziteljUsporavanja=1;
	private boolean prvoPokretanje=true;
	private double prosloVrijeme;
	private PutL tempPut;
	private boolean borbaPocela;
	private GameLogicBranitelj branitelj=null;
	private boolean napadnutSam=false;
	private float stetaArmor,stetaHelth,armor,helth;
	private int tipStete,ticBorbe;
	private float udaracaSvakihSekundi;
	private LinkedList<GameLogicBranitelj> listaNapadaca;
	private GameEvent ge;// ovdje ga instanciram pošto se stalno mora stvarat da bi poslao xy, vjeruem da æeovako brže raadit
	private float smjDx,smjDy;
	private Random generator;
	private float ds;
	private float TpS;
	private float budiTuX,budiTuY;
	private float x,y, pomNaX=-1,pomNaY=-1;//-1 znaèi da su prazne varijeble i nije potrebno ništa s njima raditi

	private RectF rec;
	private GameLogicObject obj;
	private UIManagerObject dvojnik;
	private GameLogicObject[] listaSudara; //lista u kojoj su reference objekata sa kojima se trenutaèno sudara ovaj objekt
	//////////vremenske stete
	private double pocetakHelthStete=0;
	private float heltStetePoSec=0;
	private boolean helthVremSteta=false;
	private boolean armorVremSteta=false;
	private double pocetakArmorStete=0;
	private float armorStetePoSec=0;
	////////////////////////
	//////////bool stanja igre
	private boolean teleportiran=false;
	private boolean izlazakIzZemlje=false;
	private boolean teleportiranNaPokozaj=false;
	private boolean boolKretanja=true;
	private double trenutakUsporavanja;
	private double trajanjeGorenja;
	private double trajanjeTrovanja;
	private float fpsUkupnoUsporavanja;
	private float postotakUsporavanjaOtrovano,postotakUsporavanjaZaledenje,postotakUsporavanjaGorenje ;
	private float trajanjeUsporavanja;
	private float postotakUsporavanja=0;
	private float vrijHelthGub;
	private float helthPoSecGub;
	private float vrijArmorGub;
	private float armorPoSecGub;
	private boolean uspori=false;
	private boolean imunNaBranitelje=false,imunNaToranjMinobacac=false;
	//////////////////////////////
	public Protivnik(float velX,float velY,float startX,float startY,int indikator,float omjerX,float omjerY, float vrijednost){
		rec=new RectF((int)startX,(int)startY,(int)(startX+velX),(int)(startY+velY));
		recStrijelci=new RectF();
		this.vrijednost=vrijednost;
		this.startX=startX;
		this.startY=startY;
		this.velX=velX;
		this.velY=velY;
		this.indikator=indikator;
		//this.eMan=eMan;  ///mora primiti referencu na EventManager da bi mogao slati poruke
		x=startX;  //centriranje x i y na sredinu objekta
	    y=startY;
	   
		generator=new Random();
		float randDod=this.randIzmeduPlusMinus(0, (omjerX+ omjerY)/10);
		 this.omjDx=omjerX+randDod;
		    this.omjDy=omjerY+randDod;
		ge=new GameEvent(this);
		velXUPrik=velX;// namještam ih ovdje u sluèaju da se ne namjeste eksplicitnno pomoæu metoda 
		velYUPrik=velY;
		listaNapadaca=new LinkedList<GameLogicBranitelj>();
		udaracaSvakihSekundi=2*1000;// postavljam dddefolt vrijednost ona se može poslije promjeniti
		stetaArmor=10;
		stetaHelth=15;
		armor=100;
		helth=100;
		
	
		
		
	}
///////////METODE KORIŠTENE PRI INICIJALIZACIJI OBJEKTA////
	public void setProtivnikKojiVrijediViseZivota(int vrijednost){
		this.vrijedimZivota=vrijednost;
	} 
	public  void postaviDaSamRoditelj(int tipDjeteta, int brDjece, float secTrajanjaTrudnoce){
		jesamLiRoditelj=true;
		this.brojDjece=brDjece;
		vrijemePoroda=System.currentTimeMillis();
		vrijemeTrajanjaTrudnoce=(int)(secTrajanjaTrudnoce*1000);
		this.tipDjeteta=tipDjeteta;
		
	}
	public void postaviSlijedTeleportacijaNaknRodenja(float[] xTel, float[] yTel, float[] sekundaStojanja){
		this.xTel=xTel;
		this.yTel=yTel; 
		this.sekundaStojanja=sekundaStojanja;
		pokreniSlijedTeleportacija=true;
	}
	
	public void setImunNaKonstantnuStetu(){
		jesamLiImunNaKonstantuStetu=true;
	}
	public void bilderStvoriStrijelcaOdOvogObjekta(boolean jesamLiTowerBuster,float xTrim,float yTrim,float radijus,float RofF,ProjektilL projektil, float vrijemeCekanjaPoslijeHitca){
		 RofFMili=1000/RofF;
		 this.vrijemeCekanjaPoslijeHitca=(int)vrijemeCekanjaPoslijeHitca*1000;
		ge.indikator2=2;// oznacava za prikaz da je strijelac
		this.projektil1=projektil;
		 radijusStrijelci=radijus;
		 trimIspaOdX=xTrim;
		 trimIspaOdY=yTrim;
	    this.jesamLiTowerBuster=jesamLiTowerBuster;
		recStrijelci.set(rec.centerX()- radijusStrijelci/2, rec.centerY()- radijusStrijelci/2, rec.centerX()+ radijusStrijelci/2, rec.centerY()+ radijusStrijelci/2);
		jesamLiStrijelac=true;
	}
	public void setProtivniSaViseZivota(int brZivota, int brZivotaPocetni,int brojKopiranja){
		this.brZivota=brZivota;
		this.jesamLiProtivnikSaViseZivota=true;
		this.brKopiranja=brojKopiranja;
		this.brZivotaPocetni=brZivotaPocetni;
	}
	public void setAnimacijuIzlaskaIzPoda(){
		animacijaIzlaskaIzPodaPriRodnju=true;
	}
	public void setAnimacjuTeleportacijePrRodenju(){
		this.animacijaTelepotacijePriRodnju=true;
	}
	
	public void setTrimSjene(float trimX,float trimY){
		ge.trimSjeneX=trimX;
		ge.trimSjeneY=trimY;
	}
	public void setTrimTouchPol(float trimX,float trimY){
		ge.trimTouchPolX=trimX;
		ge.trimTouchPolY=trimY;
	}
	public void setPolozajNaCesti(float pol){
		polNaCesti=pol;
	}
	public void setRazmakCrtanjaIHodaPoCestiZaLetece(float razmakX, float razmakY){
		this.recLeteceg=new RectF();
		this.jesamLiLeteci=true;
		//rec=new RectF((int)x+razmakX,(int)y+razmakY,(int)(x+velX+razmakX),(int)(y+velY+razmakY));
		recLeteceg=new RectF((int)x-razmakX,(int)y-razmakY,(int)(x+velX-razmakX),(int)(y+velY-razmakY));
		
		x=x+razmakX;
		y=y+razmakY;
		razmakCrtanjaIHodaPoCestiX=razmakX;
		razmakCrtanjaIHodaPoCestiY=razmakY;
	}
	public void setHelthArmorTipnapada(float helth,float armor,int tipStete){
		this.stetaHelth=helth;
		this.stetaArmor=armor;
		this.tipStete=tipStete;
	}
	public void setHelthArmor(float helth,float armor){
		this.pocetniHelth=helth;
		this.pocetniArmor=armor;
		this.helth=helth;
		this.armor=armor;
	}
	@Override
	public void setJesamLiImunNaBranitelje(boolean b){
		imunNaBranitelje=b;
	}
	@Override
	public void setJesamLiImunNaToranjMinobacac(boolean b){
		imunNaToranjMinobacac=b;
	}		
	@Override
	public boolean jesamLiImunNaBranitelje(){
		return imunNaBranitelje;
	}
	@Override
	public boolean jesamLiImunNaToranjMinobacac(){
		return imunNaToranjMinobacac;
	}
	//////////MALI RUN!!!!!!!!!!!!!!!!!!!!!!!!!!
 	public void maliRun(GameLogic GL, FazeIgre FI, float TpS, boolean pause) { // mora primiti referencu da bi mogao komunicirati sa GameLogic-om
		this.TpS=TpS;
		this.GL=GL;
		dyZaPrik=0;
        dxZaPrik=0;
		 if(  prvoPokretanje){
	    	  stvariKojeSeIzvrsavajuSamoNaPocetku();
	    	  prvoPokretanje=false;
	    	  if(this.animacijaTelepotacijePriRodnju){
	    		  teleportirajMeNaPolozaj(x,y);
	    		  x=-10000;
	    		  y=-10000;
	    		  pomakniRect();
	    		  
	    		  updateStanjaZaPrikaz();
	    	  }
	      }
		 
		 if(pokreniSlijedTeleportacija){
			 slijednaTeleportacija();
		 }
		if(izracunajPauzu){
			vrijemePauze=System.currentTimeMillis()-vrijemePauze;
			izracunajPauzu=false;
		}
		///pauze dodavanje
		this.prosloVrijeme+=vrijemePauze;
		this.trenutakUsporavanja+=vrijemePauze;
		this.pocetakHelthStete+=vrijemePauze;
		this.pocetakArmorStete+=vrijemePauze;
		
		
		obradiVremenskeStete();
		if(helth>0){
		
			if(razmakCrtanjaIHodaPoCestiX!=0||razmakCrtanjaIHodaPoCestiY!=0){
				   x-=razmakCrtanjaIHodaPoCestiX;
			       y-=razmakCrtanjaIHodaPoCestiY;
    	        rec=new RectF((int)x,(int)y,(int)(x+velX),(int)(y+velY));
		      
    	        listaSudara= GL.provjeriKoliziju(this);
			}
			else  listaSudara= GL.provjeriKoliziju(this);
		
		
	if(!ge.izlazakIzZemlje) if(!ge.teleportacija){
			  if(izlazakIzZemlje){
			    	ge.izlazakIzZemlje=true;
			    	izlazakIzZemlje=false;
			    }
			  else if(teleportiran){
			     teleportacija();
		     }
		   
		    else if(teleportiranNaPokozaj){
		    	teleportacijaNaPolozaj(xPolTelportacije, yPolTeleportacije);
		    }
		    else if(!napadnutSam){
		    	
				    
				        if(this.jesamLiStrijelac){
				        	if(!strijelciGlavnaMetoda()){
				        		if(this.vrijemeIspucavanja+vrijemeCekanjaPoslijeHitca<System.currentTimeMillis()){
				        		    listaSudara= GL.provjeriKoliziju(this);
				        			  ge.indikatorBorbe=0;
				        		    hodajPoCesti88();
				        		}
				        		{	  ge.indikatorBorbe=0;
				        		
				        		}
				        	}
				        }
				        else if(roditeljGlavnaMetoda()){
				        	
				        }
				        else {
				        	hodajPoCesti88();
				        }
				
		         }
		     else{
		    	  ge.indikatorBorbe=0;// znaci da nema nikoga
		    	    prosloVrijeme=System.currentTimeMillis();
		    	 pokrenutoPucanje=false;
		    	 napadniBranitelje88();
		     }
		    }
		  
			if(razmakCrtanjaIHodaPoCestiX!=0||razmakCrtanjaIHodaPoCestiY!=0){
				  x+=razmakCrtanjaIHodaPoCestiX;
			       y+=razmakCrtanjaIHodaPoCestiY;
   	        rec=new RectF((int)x,(int)y,(int)(x+velX),(int)(y+velY));
			}
		       pomakniRect(); // pomièe pravokutnikk
			
		}
	    else ubijMe();
		updateStanjaZaPrikaz();
		if(pause){
			izracunajPauzu=true;
			vrijemePauze=System.currentTimeMillis();
		}
		else vrijemePauze=0; 
		///
		if(this.tempPutNaKojemSi!=null){
			this.jesamLiNaInertnomPutu=this.tempPutNaKojemSi.jesamLiInertniPut();
		}
		///
		 //salje update polozaja objektu prikaza
		prosloVrijeme=System.currentTimeMillis();
	    this.GL.setTrenutacnaCestaNaKojojSi(this.redBrPuta);
	 
	
	}
 	public void animacijaIzlaskaIzZemlje(){
 		izlazakIzZemlje=true;
 	}
    public void teleportirajMe(){
    	teleportiran=true;
    }
    public void teleportirajMeNaPolozaj(float xPolTelportacije,float yPolTeleportacije){
    	teleportiranNaPokozaj=true;
    	this.xPolTelportacije=xPolTelportacije;
        this.yPolTeleportacije=yPolTeleportacije;
    }
	////getersi//////////////////////////////////////////////
    public boolean daliSeTeleportiram(){
 
    	boolean b=false;
       	if(this.ge!=null){
    		b=ge.teleportacija;
    	}
    	return b;
    } 
    public boolean jesamLiImunNaKonstantnuStetu(){
    	return this.jesamLiImunNaKonstantuStetu;
    }
    @Override
    public boolean jesamLiNaInertnomPutu(){
    	boolean b=false;
    	if(ge!=null){
    		b=ge.teleportacija;
    	}
    	return jesamLiNaInertnomPutu||pokreniSlijedTeleportacija||b;
    } 
	@Override
	public int getBrojNapadaca() {
		return listaNapadaca.size();// vraca 
	}

	public float getXVelUPrikazu(){
		return velXUPrik;
	}
	public float getYVelUPrikazu(){
		return velYUPrik;
	}
	public int getOblZaKol(){
		return 3;// znaèi da je kvadrat i da ga zanima samo sudar sa cestom
	}
	public RectF getRect(){ ///vraca svoj rect radi koalision detectiona
		if(!ge.teleportacija)return this.rec;
		else return new RectF(-1000,-10000,-1000,-1000);
	
	}
	
	public int getIndikator(){   ///koja tipa objekta, može biti igraè, kutija, projektil...
		return indikator;
	}
	public float getX() {  ///od interfacea GameLogicObject
		// TODO Auto-generated method stub   
		return  x;                          
	}
	@Override
	public float getY() {  ///od interfacea GameLogicObject
		// TODO Auto-generated method stub
		return  y;
	}
	@Override
	public float getDy() {
		// TODO Auto-generated method stubdx*omjDx
		if(!napadnutSam){
			if(this.uspori) return (smjDy*omjDy/100)*(100-this.postotakUsporavanjaOtrovano-postotakUsporavanjaZaledenje-postotakUsporavanjaGorenje);
			else            return this.smjDy*omjDy;
		}
		else  return 0;
	}
	@Override
	public float getDx() {
		// TODO Auto-generated method stub
		if(!napadnutSam){
			if(this.uspori) return (smjDx*omjDx/100)*(100-this.postotakUsporavanjaOtrovano-postotakUsporavanjaZaledenje-postotakUsporavanjaGorenje);
			else            return this.smjDx*omjDx;// vraca srednji omak ne trenutaèni
		}
		else return 0;
	}
	public boolean getJesiLiZiv(){
		return this.zivSam;
	}
	/////setersi/////////////////////////////////////////////////
	public void trimLogikeIprikaza(float trimXOdSredineLogike,float trimYOdSredineLogike){
		 pomakOdY= trimYOdSredineLogike;
		 pomakOdX= trimXOdSredineLogike;
	} 
	@Override
 	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
	    dvojnik=obj;	
	}
	@Override
	public void setUdaracaSvakihSekundi(float sekunde) {
		udaracaSvakihSekundi=sekunde*1000;
		
	}
	public void setViseTeNeNapadam(GameLogicBranitelj obj){
		listaNapadaca.remove(obj);
		if(obj==branitelj) branitelj=null;// ako je taj objekt kojeg se trenutaèno napada vræa se njegova referenca u null da bi se petlja mogla ponovno izvršavat
		if(listaNapadaca.isEmpty()) napadnutSam=false;// ako se izbriše zadnji objekt iz liste vraæa se zastavica u  false da se omoguæi kretanje po cesti
	}
	public void setJaTeNapadam(GameLogicBranitelj obj){
		napadnutSam=true;
		listaNapadaca.addLast(obj);// stavio sam ga zadnjeg tako da uvijek se okreæe prema onom koji ga je prvi napao, inaèe mi mogao doæi u situaciju da se trza lijevo desno
	}

	public void setXVelUPrik(float xp){
		velXUPrik=xp;
	}
	public void setYVelUPrik(float yp){
		velYUPrik=yp;
	}
	
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) { // ne koristim e jer je isti gameevent objekt
		/* if(e.touched){
			 pause=!pause;  //zaustavlja i pusta malirun
			 e.touched=false;
		 }*/
		if(ge.imTouched){ // ovo se izvršava samo ako je ovaj objekt odabran
		  if(ge.pomNaX!=-1 && ge.pomNaY!=-1){
			
				pomNaX=ge.pomNaX;
				pomNaY=ge.pomNaY;
				ge.pomNaX=-1;
				ge.pomNaY=-1;// namješta ga na -1 što znaèi da se neæe zapisivat više u varijable
			
			
		  }
		    if(ge.touchedObj!=null){
			   touchObj=ge.touchedObj;
			  
		   }
		     else touchObj=null; // da bi se prenjela informacija u ovaj objekt da više neba touchObjekta
	    }
	 }
/////////////////PRIVATENE METODE
	private boolean roditeljGlavnaMetoda(){
		boolean b=false;
		if(this.jesamLiRoditelj){
			             
		            	this.vrijemePoroda+=this.vrijemePauze;
			           if(this.vrijemeTrajanjaTrudnoce+this.vrijemePoroda<System.currentTimeMillis()){
			        	   ///////////////////////////////////////////
	                            this.pokreniPorod=true;
			        	    //////////////////////////////////////////////////////////////////////////////////////////////////// 
			        	      b=true;
			        	    	tikIspPoradaja=0;
			        	      this.vrijemePoroda=System.currentTimeMillis();
			        		  ge.indikatorBorbe=12;		           
			           }
			       if( ge.indikatorBorbe!=12&&this.tikIspPoradaja>-1&&this.tikIspPoradaja<brojDjece){
						ge.indikatorBorbe=12;
						tikIspPoradaja++;
					}
					else if(tikIspPoradaja>=brojDjece){
		                  if(this.pokreniPorod){
				        	   float xPoroda=0,yPoroda=0;
				        	   ///////////////////////////////////////////////////////////////////////////////////////////////
				        	     for(int i=0; i<this.brojDjece;i++){//vrti broj djece
				        	    ///////////////////////////////////////////////////////////////////////////////////////////////
				        	    	 PutL tempPut=null;
				        	   	for(int j=0; j<10; j++){// vrti random polozaj
				        	    	  if( this.generator.nextBoolean()){
				        	    		 xPoroda=this.randIzmeduPlusMinus(this.rec.centerX()-1.5f*this.velXUPrik, 1f*this.velXUPrik);
				        	    		}
	    	                           else{
	    	                        	   xPoroda=this.randIzmeduPlusMinus(this.rec.centerX()+1.5f*this.velXUPrik, 1f*this.velXUPrik);
				        	             } 
				        	    	  if( this.generator.nextBoolean()){
					        	    		 yPoroda=this.randIzmeduPlusMinus(this.rec.centerY()-1.5f*this.velXUPrik, 1f*this.velXUPrik);
					        	    		}
		    	                           else{
		    	                        	   yPoroda=this.randIzmeduPlusMinus(this.rec.centerY()+1.5f*this.velXUPrik, 1f*this.velXUPrik);
					        	             }   	    	
				        	    	      tempPut= jeliTockaNaCestiVratiCestuAkoJe(xPoroda, yPoroda);
		        	    		          if(tempPut!=null){
		        	    		        	  
		        	    		        	  break;
		        	    		          }
		        	    		          xPoroda=rec.centerX();
		        	    		          yPoroda=rec.centerY();
				        	    	}
				        	   /////////////////////////////////////////////////////////////////////////////////////////////////////
				        	   	/////////stvaranje djeteta/////////
				        	   	float tempPolozaj=izvuciPolozajNaCesti( xPoroda, yPoroda,tempPut);
				        	   	if(tempPolozaj<0) tempPolozaj=0.5f;
				        	   	else if(tempPolozaj<0.2){
				        	   	 tempPolozaj=0.2f;
				        	   	}
				        	 	else if(tempPolozaj>0.8){
					        	   	 tempPolozaj=0.8f;
					        	   	}
				        	    this.GL.faIgr.inkriminiajBrojProtivnika();
				        	   	this.GL.faIgr.stvoriteljObjekata(this.tipDjeteta, xPoroda, yPoroda,  tempPolozaj, 0, 0, 0);       
				        	   	////////////////////////////////////
				        	     }
				        	     this.pokreniPorod=false;
		                  }
						tikIspPoradaja=-1;
					}
					if(ge.indikatorBorbe==12){
						b=true;
						
					}
					if(b){
						ObjectPrikaza temp= (ObjectPrikaza)this.dvojnik;
						//!!!!!!!!!!!!!!!!!!KADA BUDEM DODA SLIKU STAVIT CU DAOVO RADI
				        temp.vanjskiCrtacIznad(this.rec.centerX()-this.velYUPrik, this.rec.centerY()-1.5f*this.velYUPrik, 2*this.velYUPrik, 2*this.velYUPrik, 4, 0);
					}
		}
	
		return b;
		
	}
	private void slijednaTeleportacija(){

		if(pocetakSlijedaTeleportacije==0){//ako je tek poceo
			 teleportirajMeNaPolozaj(xTel[iClanaTeleportacije]*this.GL.faIgr.xPiksCm,yTel[iClanaTeleportacije]*this.GL.faIgr.yPiksCm);
   		  x=-10000;
   		  y=-10000;
   		  pomakniRect();
   		  updateStanjaZaPrikaz();
   		 pocetakSlijedaTeleportacije=System.currentTimeMillis();
   	  iClanaTeleportacije++;
   		
		}
		else{
	     	int razlika=(int)(sekundaStojanja[iClanaTeleportacije-1]*1000+this.pocetakSlijedaTeleportacije-System.currentTimeMillis());
		
	     	if(razlika<0){
	     		
	     		teleportirajMeNaPolozaj(xTel[iClanaTeleportacije]*this.GL.faIgr.xPiksCm,yTel[iClanaTeleportacije]*this.GL.faIgr.yPiksCm);
	     		  pomakniRect();
	     		  updateStanjaZaPrikaz();
			      pocetakSlijedaTeleportacije=razlika+System.currentTimeMillis();
			      iClanaTeleportacije++;
		     }
	     	
		}
		if(iClanaTeleportacije>=sekundaStojanja.length){
			pokreniSlijedTeleportacija=false;
		}
	}
	private boolean strijelciGlavnaMetoda(){
		boolean b=false;
		 ticFire=Math.round(TpS/rateOfFire);
		   listaSudara=null;
		   RectF tempRectF=new RectF(rec);
		   tempRectF.set(rec);
		   rec.set(this.recStrijelci);
		   
		   rec.set(tempRectF);
		   vrijemeIspucavanja+=vrijemePauze;
		   if(vrijemeIspucavanja+this.RofFMili<System.currentTimeMillis()){
		
			   tempProtivnik=izvuciObjKojegSeGada();
		          if(tempProtivnik!=null){
		        	  if( !pokrenutoPucanje){
		        		  pokrenutoPucanje=true;
		        		  pocetakPucanja=System.currentTimeMillis();
		        	  }  
		        	  {
		        		  
		        	  }
		        	
		        	  b=true;
		        	  pocetakPucanja+=this.vrijemePauze;
		        	  ge.indikatorBorbe=11;
		        	  ge.pomNaX=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
		                 ge.pomNaY=tempProtivnik.getRect().centerY();
		        	  if(  pocetakPucanja+1500<System.currentTimeMillis()){
		            // if(prosloVrijeme+rateOfFire*1000<System.currentTimeMillis()){
		        	 
		        	 //posaljiPrikazu();// ovo sam stavio ovdje jer je u izvrsitelju od prkaza metoda koja ce vratiti nazad podatke od pocetku 
				                      ispucajProjektil();
				                 vrijemeIspucavanja=System.currentTimeMillis();
				                 b=true;
				              
				                 prosloVrijeme=System.currentTimeMillis();
				           	  pokrenutoPucanje=false;
		        	  }
		             
		          
		          }
		          else {
		        	  b=false;
		        	  pokrenutoPucanje=false;
		        	  ge.indikatorBorbe=0;// znaci da nema nikoga
		          }
		     }
		     //else  ge.indikatorBorbe=0;// znaèi da se ništa ne dogada
		   else{
			   ge.indikatorBorbe=0;
		   }
		  /* else{ 
			  //if(this.ticProvSud>=ticFire/4){ 
			                                  tempProtivnik=izvuciObjKojegSeGada();
			                                  if(tempProtivnik!=null){
			                                	  ge.x2=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
			                                	  ge.y2=tempProtivnik.getRect().centerY();
			            					      
			                                	  ge.indikatorBorbe=11;// znaèi da cilja
			                                  }
			                                  else ge.indikatorBorbe=0;// znaèi da nema nikoga
			                                  ticProvSud=-1;// nije nula zato što æe ga poslije odmah inkriminirati za jedan
			                         //         }
			 // else ge.indikatorBorbe=0;// ovo znaèi jednostavno da ema podataka jer se nije oèitao colision
			      ticProvSud++;
		          iFire++;
		       }*/

		   ge.x3=1000/this.rateOfFire;// salje vrijeme izmeðu dva ispaljivanja
		//posaljiPrikazuStrijelci();
		   return b;
	} 
	private void ispucajProjektil(){
		// projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik.getRect().centerX(),this.tempProtivnik.getRect().bottom);
		if(projektil1.jesiDosaoNaCilj()) {
			 ge.indikatorBorbe=12;// znaèi da je ispaljen
			projektil1.pokreniSePremaCilju(rec.centerX()+ pomakOdX+ trimIspaOdX,rec.centerY()+ pomakOdY+ trimIspaOdY,this.tempProtivnik,true);
		}
		
	}
	private GameLogicObject izvuciObjKojegSeGada(){
		int i=0;
		if(!this.jesamLiTowerBuster){
			RectF tempRect= new RectF();
			tempRect.set(rec.centerX()- this.radijusStrijelci/2, rec.centerY()- this.radijusStrijelci/2,rec.centerX()+this.radijusStrijelci/2,rec.centerY()+this.radijusStrijelci/2);
			listaSudara= GL.provjeriKoliziju(6, tempRect);
		    tempStari=null;
		    tempNovi=null;
		    while(listaSudara[i]!=null){
		        	if(listaSudara[i].getIndikator()>=0&&listaSudara[i].getIndikator()<=100)// provjerava dali je igraè od protivnika
		        	 {  	tempNovi=listaSudara[i];
			          if(tempStari==null) tempStari=tempNovi;
			         else if( udaljenostDvijeTocke(tempNovi.getRect().centerX(),tempNovi.getRect().centerY(),rec.centerX(),rec.centerY())>
			                  udaljenostDvijeTocke(tempStari.getRect().centerX(),tempStari.getRect().centerY(),rec.centerX(),rec.centerY()))  tempStari=tempNovi;
			         
			        }
			   i++;
			   if(listaSudara.length<=i) break;
		        }
		   }
		else{
			RectF tempRect= new RectF();
			tempRect.set(rec.centerX()- this.radijusStrijelci/2, rec.centerY()- this.radijusStrijelci/2,rec.centerX()+this.radijusStrijelci/2,rec.centerY()+this.radijusStrijelci/2);
			listaSudara= GL.provjeriKoliziju(7, tempRect);
		    tempStari=null;
		    tempNovi=null;
		    while(listaSudara[i]!=null){
		        	if(listaSudara[i].getIndikator()>=100&&listaSudara[i].getIndikator()<200)// provjerava dali je igraè od protivnika
		        	{  	tempNovi=listaSudara[i];
			        if(tempStari==null) tempStari=tempNovi;
			         else if( udaljenostDvijeTocke(tempNovi.getRect().centerX(),tempNovi.getRect().centerY(),rec.centerX(),rec.centerY())>
		                      udaljenostDvijeTocke(tempStari.getRect().centerX(),tempStari.getRect().centerY(),rec.centerX(),rec.centerY()))  tempStari=tempNovi;
		         
			        }
			   i++;
			   if(listaSudara.length<=i) break;
		        }
		     }
		return tempStari;
	}
	private void obradiVremenskeStete(){
		/////trovanje///
		if(this.helthVremSteta){	
			  if(System.currentTimeMillis()<this.pocetakHelthStete+this.trajanjeTrovanja){
				   tempTime=(float)((-prosloVrijeme+System.currentTimeMillis())/1000);
				   this.helth-=this.heltStetePoSec*tempTime;
				   this.armor-=this.armorStetePoSec*tempTime;
			      }
			  else{
				  this.trajanjeTrovanja=0;
                  heltStetePoSec=0;
				  this.helthVremSteta=false;
				  this.ge.otrovan=false;
				  }
			  
		     }
		//////////////
	    /////gorenje///
			if(this.armorVremSteta){	
				  if(System.currentTimeMillis()<this.pocetakArmorStete+this.trajanjeGorenja){
					   tempTime=(float)((-prosloVrijeme+System.currentTimeMillis())/1000);
					   this.helth-=this.heltStetePoSec*tempTime;
					   this.armor-=this.armorStetePoSec*tempTime;
				      }
				  else{
					  this.trajanjeGorenja=0;
					 	armorStetePoSec=0;
					  
					  this.armorVremSteta=false;
					  this.ge.gorim=false;
					  }
				  
			     }
			//////////////
		
	}
	private float dxPomakFpS(){
		if(this.uspori){
			this.fpsUkupnoUsporavanja++;
			if(fpsUkupnoUsporavanja<this.TpS*this.trajanjeUsporavanja/1000){
				 tempMnoziteljUsporavanja=(100-this.postotakUsporavanjaOtrovano-postotakUsporavanjaZaledenje-postotakUsporavanjaGorenje)/100;
				 if(tempMnoziteljUsporavanja<0) tempMnoziteljUsporavanja=0.1f;
			}
				else{
					uspori=false;// ako je vrijeme usporavanja isteklo vraca zastavicu da više ne provjerava usporavanje
					 tempMnoziteljUsporavanja=1;
					 fpsUkupnoUsporavanja=0;
				}
		     }
		
		return tempMnoziteljUsporavanja*smjDx/this.TpS;
		
	}
	private float dyPomakFpS(){
		if(this.uspori){
			this.fpsUkupnoUsporavanja++;
			if(fpsUkupnoUsporavanja<this.TpS*this.trajanjeUsporavanja/1000){
				 tempMnoziteljUsporavanja=(100-this.postotakUsporavanjaOtrovano-postotakUsporavanjaZaledenje-postotakUsporavanjaGorenje)/100;
				 if(tempMnoziteljUsporavanja<0) tempMnoziteljUsporavanja=0.1f;
			}
				else{
					uspori=false;// ako je vrijeme usporavanja isteklo vraca zastavicu da više ne provjerava usporavanje
					 tempMnoziteljUsporavanja=1;
					 fpsUkupnoUsporavanja=0;
				}
		     }
		return tempMnoziteljUsporavanja*smjDy/this.TpS;
	}
	private float dxPomak(){
		if(this.uspori){
			 
			if(System.currentTimeMillis()<this.trenutakUsporavanja+this.trajanjeUsporavanja){
				tempTime=(float)(-prosloVrijeme+System.currentTimeMillis())/1000;
				 tempMnoziteljUsporavanja=(100-this.postotakUsporavanjaOtrovano-postotakUsporavanjaZaledenje-postotakUsporavanjaGorenje)/100;
				 if(tempMnoziteljUsporavanja<0) tempMnoziteljUsporavanja=0.1f;
				 else if(tempMnoziteljUsporavanja>100) tempMnoziteljUsporavanja=1f;
				if(tempTime>0.5) return smjDx/2;
				else return   smjDx*tempTime*tempMnoziteljUsporavanja;
				 
			}
				else{
					 this.trajanjeUsporavanja=0;
					 	armorStetePoSec=0;
					  
					  
					  this.ge.zaleden=false;
				  uspori=false;
					
					tempTime=(float)(-prosloVrijeme+System.currentTimeMillis())/1000;
					if(tempTime>0.5) return smjDx/2;
					else return   smjDx*tempTime+0.000001f;
				}
		     }
		else{
		tempTime=(float)(-prosloVrijeme+System.currentTimeMillis())/1000;
		if(tempTime>0.5) return smjDx/2;
		else return   smjDx*tempTime+0.000001f;// poslije pauze akda je jako malo vrijeme zbog float ograni�?enja  na minus 7 bude nula pa se detektira kao da stoji 
		}
	}
	private float dyPomak(){
		if(this.uspori){	
		  if(System.currentTimeMillis()<this.trenutakUsporavanja+this.trajanjeUsporavanja){
			   tempTime=(float)(-prosloVrijeme+System.currentTimeMillis())/1000;
			   
			   if(tempTime>0.5) return smjDy/2;
			   else return   smjDy*tempTime*tempMnoziteljUsporavanja;
		      }
		  else{
				 this.trajanjeUsporavanja=0;
				 	armorStetePoSec=0;
				  
				  
				  this.ge.zaleden=false;
			  uspori=false;
			  tempTime=(float)(-prosloVrijeme+System.currentTimeMillis())/1000;
				if(tempTime>0.5) return smjDy/2;
				else return   smjDy*tempTime+0.000001f;
		  }
		}
		else{tempTime=(float)(-prosloVrijeme+System.currentTimeMillis())/1000;
		if(tempTime>0.5) return smjDy/2;
		else return   smjDy*tempTime+0.000001f;}
	}
	private void  stvariKojeSeIzvrsavajuSamoNaPocetku(){
		 this.vrijemeIspucavanja=System.currentTimeMillis();
	this.prosloVrijeme=System.currentTimeMillis();
	this.velXUPrik=this.dvojnik.getGlavniRectPrikaza().width();
	this.velYUPrik=this.dvojnik.getGlavniRectPrikaza().height();
	}
	private void jeliStigaoDoKraja888(){
		int i=0;
		  while(listaSudara[i]!=null){
				
				if(listaSudara[i].getIndikator()==204){
					stigaoDoKraja();
					    }
				i++;
				if(listaSudara.length<=i) break;
		  }
	}
	private void stigaoDoKraja(){
		  ge.jesamLiZiv=false;
		  ge.indikator=-100;
		  dvojnik.GameLinkerIzvrsitelj(ge);
		  if(jesamLiProtivnikSaViseZivota){
			  int brZivotaZaIzgubiti=1;
			  for(int i=1; i<brZivota;i++){
				  brZivotaZaIzgubiti+=brKopiranja*brKopiranja;
			  }
			  for(int i=0; i< brZivotaZaIzgubiti;i++){
				  if( vrijedimZivota>1){
					  for(int j=0;j< vrijedimZivota;j++){
						  GL.setZivotManje();
					  }
				  }
				  else   GL.setZivotManje();
			  }
				
		  }
		  else{
			  if( vrijedimZivota>1){
				  for(int j=0;j< vrijedimZivota;j++){
					  GL.setZivotManje();
				  }
			  }
			  else   GL.setZivotManje();
		  }
		  GL.mrtavSam(this);
		  int i=0;
		  while(listaNapadaca.size()>i){//salje svim svojim napadaèima da je mrtav
			  listaNapadaca.get(i).setMrtavProtivnikPrestani();
		      i++;
		  }
		
	}
	private void ubijMe(){
		  //this.indikator=0;
		if(this.jesamLiProtivnikSaViseZivota) if(this.brZivota>1){
			 float xtemp=0,ytemp=0;
			--this.brZivota;
			for(int i=0;i<brKopiranja;i++){
	 	    	 PutL tempPut=null;
	        	   	for(int j=0; j<10; j++){// vrti random polozaj
	        	    	  if( this.generator.nextBoolean()){
	        	    		  xtemp=this.randIzmeduPlusMinus(this.rec.centerX()-1f*this.velXUPrik, 1f*this.velXUPrik);
	        	    		}
                        else{
                        	xtemp=this.randIzmeduPlusMinus(this.rec.centerX()+1f*this.velXUPrik, 1f*this.velXUPrik);
	        	             } 
	        	    	  if( this.generator.nextBoolean()){
	        	    		  ytemp=this.randIzmeduPlusMinus(this.rec.centerY()-1f*this.velXUPrik, 1f*this.velXUPrik);
		        	    		}
	                           else{
	                        	  ytemp=this.randIzmeduPlusMinus(this.rec.centerY()+1f*this.velXUPrik, 1f*this.velXUPrik);
		        	             }   	    	
	        	    	      tempPut= jeliTockaNaCestiVratiCestuAkoJe(xtemp,ytemp);
 	    		          if(tempPut!=null){
 	    		        	  
 	    		        	  break;
 	    		          }
 	    		         xtemp=rec.centerX();
 	    		       ytemp=rec.centerY();
	        	    	}
	        	   	float tempPolozaj=izvuciPolozajNaCesti(xtemp,ytemp,tempPut);
	        	   	if(tempPolozaj<0) tempPolozaj=0.5f;
	        	   	else if(tempPolozaj<0.2){
	        	   	 tempPolozaj=0.2f;
	        	   	}
	        	 	else if(tempPolozaj>0.8){
		        	   	 tempPolozaj=0.8f;
		        	   	}
			    this.GL.faIgr.inkriminiajBrojProtivnika();
			    this.GL.faIgr.stvoriteljObjekata(this.indikator,xtemp,ytemp,  tempPolozaj,brZivota, this.brZivotaPocetni,0);
			}
		}
		  ge.jesamLiZiv=false;
		  zivSam=false;
		  GL.setUbijenProtivnikRijesiBodove(this);
		  GL.mrtavSam(this);
		  int i=0;
		  if(listaNapadaca!=null)while(listaNapadaca.size()>i){//salje svim svojim napadaèima da je mrtav
			  listaNapadaca.get(i).setMrtavProtivnikPrestani();
		      i++;
		  }
		// listaNapadaca=null;
		 // this.updateStanjaZaPrikaz();
	}
	private void napadniBranitelje88(){
		boolean postojiProtivnikDovoljnoBlizuDaStanem=false;
		int i=0;
		branitelj=null;
		while(!listaNapadaca.isEmpty()&&listaNapadaca.size()>i){
			ObjectIgre tem=(ObjectIgre)listaNapadaca.get(i);
			if( tem.getHealth()<0){
				listaNapadaca.remove(i);
				
			}
			else{
			  if(udaljenostDvijeTocke(rec.centerX(),rec.centerY(),listaNapadaca.get(i).getRect().centerX(),listaNapadaca.get(i).getRect().centerY())<velXUPrik*2){
					postojiProtivnikDovoljnoBlizuDaStanem=true;
					if(rec.centerX()>listaNapadaca.get(i).getRect().centerX()) this.ge.indikatorBorbe=2;
					else ge.indikatorBorbe=1;
				}
		if(udaljenostDvijeTocke(rec.centerX(),rec.centerY(),listaNapadaca.get(i).getRect().centerX(),listaNapadaca.get(i).getRect().centerY())
					<=velXUPrik+velXUPrik*ObjectIgre.mnozitelj+listaNapadaca.get(i).getXVelUPrikazu()*ObjectIgre.mnozitelj){// ako je prvi napadaè u dometu napada se,dodao sam desetinu širine radi toga što nije 100% toèno pa mi mogao završiti izvan dometa
			  if(Math.abs(rec.centerX() -  listaNapadaca.get(i).getRect().centerX())
						<=velXUPrik*0.1+velXUPrik*ObjectIgre.mnozitelj+listaNapadaca.get(i).getXVelUPrikazu()*ObjectIgre.mnozitelj){
			  if(!borbaPocela)ticBorbe=0;// namješteno je kad je nula da odmah udari                                 
				borbaPocela=true;
				branitelj=listaNapadaca.get(i);// uzima uvijek prvog na redu jel je on najprije prozvao napad
				naciniStetuBranitelju(i);
				postojiProtivnikDovoljnoBlizuDaStanem=true;// ako je prvi, onda æe break zasjeniti sljedeæi if
				break;
			}
		}
			i++;
	    	}
		}
		if(postojiProtivnikDovoljnoBlizuDaStanem==false){
			hodajPoCesti88();// posto je veæ preskoèena hodajPoCesti metoda ja je ponovno vraæam na izvršavanje ako su branitelji jako daleko
		
		}
		
	}
	private void naciniStetuBranitelju( int i){
		// i je index napadaca  na kojem je stao
		//float tics=TpS*udaracaSvakihSekundi;
		prosloVrijemeUdarca+=this.vrijemePauze;
		if(udaracaSvakihSekundi+ prosloVrijemeUdarca<System.currentTimeMillis()){			
			if(this.randIzmeduPlusMinus(0, 100)<this.GL.faIgr.sansaZaUdarac){
		       //branitelj.setStetu(tipStete, stetaHelth, stetaArmor,0,0,0,0,0,0);
				listaNapadaca.get(i).setStetu(tipStete,stetaHelth, stetaArmor,trajanjeUsporavanja,postotakUsporavanja,vrijHelthGub, helthPoSecGub, vrijArmorGub, armorPoSecGub);
		       if(rec.centerX()>listaNapadaca.get(i).getRect().centerX()) this.ge.indikatorBorbe=4;
		       else ge.indikatorBorbe=3;
		       ticBorbe=0;
			}
			else{
				 if(rec.centerX()>listaNapadaca.get(i).getRect().centerX()) this.ge.indikatorBorbe=2;
				 else ge.indikatorBorbe=1;
			}
			prosloVrijemeUdarca=System.currentTimeMillis();
		}
		else{
		    //ako se ne udara mora stojat u napadackom polozaju
		    if(rec.centerX()>listaNapadaca.get(i).getRect().centerX()) this.ge.indikatorBorbe=2;
		    else ge.indikatorBorbe=1;
		 }
		ticBorbe++;
	}
	private void AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAprovjeriJesiLiUnutarCeste888(){
		int i=0;
        while(listaSudara[i]!=null){
			
			if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
				tempPut=(PutL)listaSudara[i];
				 if(!tempPut.imamLiPutGore()){
					    if(tempPut.getRect().top<rec.bottom)	y+=dy;// onda ga sao pomièe prema dolje ne naglo nego pomalo sa svakim ciklusom
				       }
				 if(!tempPut.imamLiPutDolje()){// provjerava za dolje 
				    	 if(tempPut.getRect().bottom<rec.bottom) y-=dy;
				    }
				 if(!tempPut.imamLiPutDesno()){
				    	if(tempPut.getRect().right<rec.right) x-=dx;
				    }
				 if(!tempPut.imamLiPutLijevo()){
				    	if(tempPut.getRect().left>rec.left) x+=dx;
				    }
			}
			i++;
			if(listaSudara.length<=i) break;
		}
		
	}
	private void hodajPoCesti88(){
		int i=0;
		smjDx=smjDy=0; // stavljam ih sve na nulu tako da nema grešaka iz prošlog pokreta
		int k=0;
		int prosliRedBr=0;
		ge.indikatorBorbe=0;
		redBrPuta=0;
        boolean pomakUnuCest=false;
		 while(listaSudara[i]!=null){
			    tempObj=listaSudara[i];
			    if(listaSudara[i].getIndikator()==204){// ako je dosao do segmenta Kraj, onda se pokreæe samouništenje i dodavanje bodova
			    	stigaoDoKraja();
			    	break;
			    }
				if(k<2&&(listaSudara[i].getIndikator()>200)&&
						(listaSudara[i].getIndikator()<301)	){//k je zato da provjerava samo  2 odjeljka ceste istovremeno
					    prosliRedBr= redBrPuta;
					    tempPutNaKojemSi=(PutL)tempObj;
					    redBrPuta=tempPutNaKojemSi.redniBroj();
					 
					    ////////////dx
					   if(listaSudara[i].getIndikator()==203) {
                             smjDx=tempPutNaKojemSi.getDx();
                            if(redBrPuta>prosliRedBr) 
                            	{
                            	pomakUnuCest=true;
                            	}
                             }
                         else if(listaSudara[i].getIndikator()==205) {
                             smjDx=tempPutNaKojemSi.getDx();
                             if(redBrPuta>prosliRedBr){
                            	 pomakUnuCest=false;
                             }
                          
                             }
                         else  if(smjDx!=listaSudara[i].getDx()){ //provjerava dali je dx i dy jednak prijašnjem ako je onda ga ne uzima
		                       if(listaSudara[i].getIndikator()==202){
		                    	   if(redBrPuta>prosliRedBr) pomakUnuCest=false;
		                    	   smjDx=listaSudara[i].getDx()*0.7f; // radi redukciju na okuci, vrijedi samo za pravi kut                                          
		                       }
	                           else if(listaSudara[i].getIndikator()==201) {
	                        	                                    smjDx=tempPutNaKojemSi.getDx();
	                        	                                    if(redBrPuta>prosliRedBr) pomakUnuCest=true;
	                                                                }
	                      
	                                          }
	                    //////dy
	                    if(listaSudara[i].getIndikator()==203){
                     	   smjDy=tempPutNaKojemSi.getDy();
                     	  if(redBrPuta>prosliRedBr) pomakUnuCest=true;
                            }
                        else if(listaSudara[i].getIndikator()==205){
                     	   smjDy=tempPutNaKojemSi.getDy();
                     	  if(redBrPuta>prosliRedBr)pomakUnuCest=false;
                     	    
                            }   
	                    else if(smjDy!=listaSudara[i].getDy()){  
	                           if(listaSudara[i].getIndikator()==202){
	                        	   if(redBrPuta>prosliRedBr)pomakUnuCest=false;
	                        	   smjDy=listaSudara[i].getDy()*0.7f;  
	                           }
	                           else if(listaSudara[i].getIndikator()==201){
	                        	   smjDy+=tempPutNaKojemSi.getDy();
	                        	   if(redBrPuta>prosliRedBr)pomakUnuCest=true;
	                        	 
	                               }
	                         
	                    }
				        pomNaY=-1;    // da ne radi pomak na poziciju
				        pomNaX=-1;
				        k++;                        
				     }        
				i++;
				if(listaSudara.length<=i) break;
		 }
	
	    dx= dxPomak();
	    dy=dyPomak();
		 if(pomakUnuCest){
			 pomakniUnutarCeste(tempPutNaKojemSi);}
	    //pomak od ceste
	        x+=dx*omjDx;
		    y+=dy*omjDy;
		    dxZaPrik=dx*omjDx;
			 dyZaPrik=dy*omjDy;
		///
		/// pomak od korekcije polozaja na cesti    
	   if(tempPutNaKojemSi!=null) if(tempPutNaKojemSi.getIndikator()==201||tempPutNaKojemSi.getIndikator()==203){
	    	if(Math.abs(tempPutNaKojemSi.getDx())>Math.abs(tempPutNaKojemSi.getDy())){
	    		tempZeljeniPolozaj=tempPutNaKojemSi.getRect().height()*this.polNaCesti+tempPutNaKojemSi.getRect().top;
	    		if(tempZeljeniPolozaj+Math.abs((dx+dy))*omjDy/6>=rec.centerY()+this.velYUPrik/2&&tempZeljeniPolozaj-Math.abs((dx+dy))*omjDy/6<=rec.centerY()+this.velYUPrik/2) y=tempZeljeniPolozaj-rec.height()/2-this.velYUPrik/2;
	    		else{
	    			if(tempZeljeniPolozaj<rec.centerY()+this.velYUPrik/2) y-=Math.abs((dx+dy))*omjDy/3;//uzimam dx jer je on sigurno pozitivan pošto je veæi
	    			else y+=Math.abs((dx+dy))*omjDy/3;
	    		}
	    	}
	    	else{
	    		tempZeljeniPolozaj=tempPutNaKojemSi.getRect().width()*this.polNaCesti+tempPutNaKojemSi.getRect().left;
	    		if(tempZeljeniPolozaj+Math.abs((dx+dy))*omjDx/6>=rec.centerX()&&tempZeljeniPolozaj-Math.abs((dx+dy))*omjDx/6<=rec.centerX()) x=tempZeljeniPolozaj-rec.width()/2;
	    		else{
	    			if(tempZeljeniPolozaj<rec.centerX()) x-=Math.abs((dx+dy))*omjDx/3;//uzimam dy jer je on sigurno pozitivan pošto je veæi
	    			else x+=Math.abs((dx+dy))*omjDx/3;
	    		}
	    	}
	    }
		
		
		
		
	}
    private void teleportacija(){
    	
   
    	if(tempPutNaKojemSi!=null){
    		ge.teleportacija=true;
    		GameLogicObject[]  tempLista=this.GL.getListuCesta();
    		PutL tempPut=null;
    		for(int i=0;i<tempLista.length;i++ ){
    			tempPut=(PutL)tempLista[i];
    			if(Math.abs(tempPut.redniBroj())==Math.abs(tempPutNaKojemSi.redniBroj())-2){
    				break;
    			}
    			tempPut=null;
    		}
    		if(tempPut!=null){
    			x=randIzmeduPlusMinus(tempPut.getRect().centerX(),tempPut.getRect().width()/3 )-this.rec.width()/2;//namješta koordinae na koje se treba pomaknuti
    	        y=randIzmeduPlusMinus(tempPut.getRect().centerY(),tempPut.getRect().height()/3)-this.rec.height()/2;
      
    		}
    		else {
    		    x=randIzmeduPlusMinus(tempPutNaKojemSi.getRect().centerX(),this.tempPutNaKojemSi.getRect().width()/3 )-this.rec.width()/2;//namješta koordinae na koje se treba pomaknuti
	            y=randIzmeduPlusMinus(tempPutNaKojemSi.getRect().centerY(),this.tempPutNaKojemSi.getRect().height()/3)-this.rec.height()/2;
    		}
    		this.pomakniRect();
    		int i=0;
    		  while(listaNapadaca.size()>i){//salje svim svojim napadaèima da je mrtav
    			  listaNapadaca.get(i).setMrtavProtivnikPrestani();
    		      i++;
    		  }
    	}
    	this.teleportiran=false;
    }	
    private void teleportacijaNaPolozaj(float xPolozaj, float yPolozaj){
    	
    
    
    		ge.teleportacija=true;
    		
    		x=xPolozaj-this.rec.width()/2;//namješta koordinae na koje se treba pomaknuti
	        y=yPolozaj-this.rec.height()/2;
  
    		this.pomakniRect();
    		int i=0;
    		  while(listaNapadaca.size()>i){//salje svim svojim napadaèima da je mrtav
    			  listaNapadaca.get(i).setMrtavProtivnikPrestani();
    		      i++;
    		  }
    	
    	teleportiranNaPokozaj=false;
    
    }
    private void pomakniUnutarCeste(GameLogicObject obj){// pomièe unutar ceste u odnosu na smijer koji pokazuje cesta , da nebi bilo problema pri prijelazu imeðu segmenata puta
		if(Math.abs(obj.getDx())>Math.abs(obj.getDy())){//znaèi vodoravni put
            if(rec.centerY()-3*velYUPrik/4<obj.getY()) {
            	y+=Math.abs((dx+dy)*omjDy/4);// zato sto je dy u vecini slucajeva 0
            }
            //else if(y+velY>(obj.getY()+obj.getRect().height())) y=obj.getRect().height()+obj.getY()-velY;
            else if(rec.centerY()+velYUPrik/2>(obj.getRect().bottom)) {
            	y-=Math.abs((dx+dy)*omjDy/4);
            }
		}
		else {//naèi okomiti put
            if(rec.centerX()-velXUPrik/2<obj.getX()){
            	x+=Math.abs((dx+dy)*omjDx/4);
            }
            else if(rec.centerX()+velXUPrik>(obj.getRect().right)){
            	x-=Math.abs((dx+dy)*omjDx/4);
            }
		}
}
	private void updateStanjaZaPrikaz(){
		if(this.jesamLiLeteci){
			ge.x=rec.centerX();
		    ge.y=rec.centerY();
		}
		else{  
			ge.x=rec.centerX()+ pomakOdX;
		    ge.y=rec.centerY()+pomakOdY;
		}
		  ge.vrijemePauze=(int)this.vrijemePauze;
		  ge.x2=this.dxZaPrik;
		  ge.y2=this.dyZaPrik;
		  ge.helth=helth/(pocetniHelth/100);// salje postotak heltha u odnosu na poèetni koji mu je postavljen
		  ge.armor=armor/(pocetniHelth/100);// racuna u onosu na pocetni gekth tako da ce u biti biti oo helth armor prikazan
		  dvojnik.GameLinkerIzvrsitelj(ge);
  
	}
	private void pomakniRect(){
		if(recLeteceg!=null){
		recLeteceg.set((int)x-razmakCrtanjaIHodaPoCestiX,(int)y-razmakCrtanjaIHodaPoCestiY,
				(int)(x+velX-razmakCrtanjaIHodaPoCestiX),(int)(y+velY-razmakCrtanjaIHodaPoCestiY));
		}
		rec.set(x, y,x+velX, y+velY);
	}
	private void AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAApomakniNaPoziciju(){
		if(touchObj!=null){ pomNaX=touchObj.getX();
         pomNaY=touchObj.getY();
               }  
		 if(boolKretanja){
		    if(pomNaX!=-1){ // znaèi da je došao do toèke i nije potrebno micanje
	           if(x>pomNaX) x-=dx;
	           else if(x<pomNaX) x+=dx;
	           if(x<(pomNaX+dx)&&x>(pomNaX-dx)){ x=pomNaX;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
	                                     pomNaX=-1;//oznaèava da je stigao do toèke
	                            }
		     }
		   if(pomNaY!=-1){ // znaèi da je došao do toèke i nije potrebno micanje
		        if(y>pomNaY) y-=dy;
		        else if(y<pomNaY) y+=dy;
		        if(y<(pomNaY+dy)&&y>(pomNaY-dy)){ y=pomNaY;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
		                                     pomNaY=-1;//oznaèava da je stigao do toèke
		                            }
			    }
		}
		
	}

////////NEKE CESTO KORISTENE FUNKCIJE
	private float izvuciPolozajNaCesti(float x, float y, PutL cesta){
		float polozaj=0;
		if(cesta.getIndikator()==203||cesta.getIndikator()==201){
		     if(cesta.getDx()>this.getDy()){
		    	 if(y>cesta.getRect().bottom){
		    		 polozaj=-1;
		    	 }
		    	 else {
		    		 polozaj=1-(cesta.getRect().bottom-y)/cesta.getRect().height();
		    	 }
		    }
	    	else {
	    		 if(x>cesta.getRect().right){
		    		 polozaj=-1;
		    	 }
		    	 else {
		    		 polozaj=1-(cesta.getRect().right-x)/cesta.getRect().height();
		    	 }
	     	}
	    }
		else polozaj=-1;
		return polozaj;
	}
	private boolean jeliTockaNaCesti(float xT,float yT){

		RectF tempRect=new RectF(xT,yT,xT+1,yT+1);
	
		boolean tocNaCest=false;
		GameLogicObject[] tempListaSudara=this.GL.provjeriKoliziju(2,tempRect);
		if(tempListaSudara!=null)	if(tempListaSudara[0]!=null){
			tocNaCest=true;
			
		}
	
		//////////////////
		return tocNaCest;
		
	} 
	private PutL  jeliTockaNaCestiVratiCestuAkoJe(float xT,float yT){
		PutL temp=null;
		RectF tempRect=new RectF(xT,yT,xT+1,yT+1);
	
		boolean tocNaCest=false;
		GameLogicObject[] tempListaSudara=this.GL.provjeriKoliziju(2,tempRect);
		if(tempListaSudara!=null)	if(tempListaSudara[0]!=null){
			temp=(PutL)tempListaSudara[0];
			
		}
	
		//////////////////
		return  temp;
		
	} 
	private float randIzmeduPlusMinus(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak
		if(b==0) b=1;
		float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
		if(generator.nextBoolean()){r=r*-1;}
		return r+a;
	 }
private double udaljenostDvijeTocke(float ax,float ay, float bx,float by){// predpostavlja da se sve odvija u prvom kvadrantu
	 return Math.hypot(Math.abs(ax-bx),Math.abs(ay-by));
}
//////////////////////////////////////
/////METODE ZA HELTH
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

public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){
	
	 if(tip==1){
		 float minus=(helth/100)*this.armor;
	     this.helth-=helth-minus;
	     this.armor-=armor;
      }
   	else if(tip==2){
	       this.helth-=helth;
	       this.armor-=armor;
		   if(armor<0)this.armor=0;
		   
		   
      }
	else if(tip==3){//3 i 4 oznacava zaledivanje i animacija ce biti zaledivanje
		float minus=(helth/100)*this.armor;
	     this.helth-=helth-minus;
	     this.armor-=armor;
	     if(postUsp>0)uspori=true;
	     this.ge.zaleden=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
	     this.trenutakUsporavanja=System.currentTimeMillis();
	    postotakUsporavanjaZaledenje=postUsp;
	     this.trajanjeUsporavanja=vrijUsp*1000;
	     pocetakHelthStete=System.currentTimeMillis();
	     heltStetePoSec=helthPoSecGub;
	 	if(helthPoSecGub>0)helthVremSteta=true;
	 	if(armorPoSecGub>0)armorVremSteta=true;
	 	 pocetakArmorStete=System.currentTimeMillis();
	 	 armorStetePoSec=armorPoSecGub;
	    		 
	     
	}
	else if(tip==4){//3 i 4 oznacava zaledivanje i animacija ce biti zaledivanje 
	       this.helth-=helth;
	       this.armor-=armor;
		   if(postUsp>0)  uspori=true;
		   this.ge.zaleden=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
		     this.trenutakUsporavanja=System.currentTimeMillis();
		     postotakUsporavanjaZaledenje=postUsp;
		     this.trajanjeUsporavanja=vrijUsp*1000;
		     pocetakHelthStete=System.currentTimeMillis();
		     heltStetePoSec=helthPoSecGub;
		 	if(helthPoSecGub>0)helthVremSteta=true;
		 	if(armorPoSecGub>0)armorVremSteta=true;
		 	 pocetakArmorStete=System.currentTimeMillis();
		 	 armorStetePoSec=armorPoSecGub;
   }
	else if(tip==5){//5 i 6 oznacava gorenje i animacija ce biti gorenje
		float minus=(helth/100)*this.armor;
	     this.helth-=helth-minus;
	     this.armor-=armor;
	     if(postUsp>0)uspori=true;
	     this.ge.gorim=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
	     if(postUsp>0)  this.trenutakUsporavanja=System.currentTimeMillis();
	     if(postUsp>0) postotakUsporavanjaGorenje=postUsp;
	     this.trajanjeGorenja=vrijHelthGub*1000;
	     if(helthPoSecGub>0) this.trajanjeTrovanja=vrijUsp*1000;    
	     if(helthPoSecGub>0) pocetakHelthStete=System.currentTimeMillis();
		 	if(helthPoSecGub>0)  heltStetePoSec=helthPoSecGub;
		 	if(helthPoSecGub>0)helthVremSteta=true;
	 	 armorVremSteta=true;
	 	 pocetakArmorStete=System.currentTimeMillis();
	 	 armorStetePoSec=armorPoSecGub;
	    		 
	     
	}
	else if(tip==6){//5 i 6 oznacava gorenje i animacija ce biti gorenje
	       this.helth-=helth;
	       this.armor-=armor;
		   if(postUsp>0)  uspori=true;
		   this.ge.gorim=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
		   if(postUsp>0) this.trenutakUsporavanja=System.currentTimeMillis();
		   if(postUsp>0) postotakUsporavanjaGorenje=postUsp;
		     this.trajanjeGorenja=vrijHelthGub*1000;
		     if(helthPoSecGub>0) this.trajanjeTrovanja=vrijUsp*1000;
		     if(helthPoSecGub>0) pocetakHelthStete=System.currentTimeMillis();
		 	if(helthPoSecGub>0)  heltStetePoSec=helthPoSecGub;
		 	if(helthPoSecGub>0)helthVremSteta=true;
		 	armorVremSteta=true;
		 	 pocetakArmorStete=System.currentTimeMillis();
		 	 armorStetePoSec=armorPoSecGub;
   }
	else if(tip==7){//7 i 8 oznacava trovanje i animacija ce biti trovanje
		float minus=(helth/100)*this.armor;
	     this.helth-=helth-minus;
	     this.armor-=armor;
	     if(postUsp>0)uspori=true;
	     this.ge.otrovan=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
	     if(postUsp>0)  this.trenutakUsporavanja=System.currentTimeMillis();
	     if(postUsp>0) this.postotakUsporavanjaOtrovano=postUsp;
	     this.trajanjeTrovanja=vrijUsp*1000;
	     pocetakHelthStete=System.currentTimeMillis();
	     heltStetePoSec=helthPoSecGub;
	     this.trajanjeUsporavanja=vrijUsp*1000;
	 	helthVremSteta=true;
	 	if(armorPoSecGub>0) this.trajanjeGorenja=vrijUsp*1000;
	 	if(armorPoSecGub>0)armorVremSteta=true;
	 	if(armorPoSecGub>0) pocetakArmorStete=System.currentTimeMillis();
	 	if(armorPoSecGub>0)armorStetePoSec=armorPoSecGub;
	    		 
	     
	}
	else if(tip==8){//7 i 8 oznacava trovanje i animacija ce biti trovanje
	       this.helth-=helth;
	       this.armor-=armor;

		   if(postUsp>0)  uspori=true;
		   this.ge.otrovan=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
		   if(postUsp>0) this.trenutakUsporavanja=System.currentTimeMillis();
		   if(postUsp>0) postotakUsporavanjaOtrovano=postUsp;
		     this.trajanjeTrovanja=vrijUsp*1000;
		     pocetakHelthStete=System.currentTimeMillis();
		     heltStetePoSec=helthPoSecGub;
		     this.trajanjeUsporavanja=vrijUsp*1000;
		 	helthVremSteta=true;
		 	if(armorPoSecGub>0) this.trajanjeGorenja=vrijUsp*1000;
		 	if(armorPoSecGub>0)armorVremSteta=true;
		 	if(armorPoSecGub>0) pocetakArmorStete=System.currentTimeMillis();
		 	if(armorPoSecGub>0)armorStetePoSec=armorPoSecGub;
   }
	   if( this.armor<0)this.armor=0;
	   if( this.helth<0)this.helth=0;
}

@Override
public void setTipStete(int tipStet) {
	this.tipStete=tipStet;
}
@Override
public void dodajHelth(float helth) {
	this.helth=helth;
}
@Override
public void dodajArmor(float armor) {
	this.armor=armor;
}
@Override
public boolean lijeciMe(float helth,float armor) {
	// TODO Auto-generated method stub
	boolean b=false;
	if(this.helth<this.pocetniHelth&&helth>0){
		b=true;
	}
	if(this.armor<this.pocetniArmor&&armor>0){
		b=true;
	}
	this.helth*=helth;
	if(this.helth>this.pocetniHelth) this.helth=pocetniHelth;
	this.armor*=armor;
	if(this.armor>this.pocetniArmor) this.helth=pocetniHelth;
	this.ge.medic=true;
	return b;
}
@Override
public float getVrijednostProtivnika() {
	// TODO Auto-generated method stub
	return vrijednost;
}
@Override
public int getRedBrPutaNaKojemSi() {
	// TODO Auto-generated method stub
	return redBrPuta;
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
@Override
public boolean jesamLileteci() {
	
	// TODO Auto-generated method stub
	return this.jesamLiLeteci;
}
@Override
public RectF getRecLeteceg() {
	// TODO Auto-generated method stub
	if(!ge.teleportacija)return this.recLeteceg;
	else return new RectF(-1000,-10000,-1000,-1000);
}






}
