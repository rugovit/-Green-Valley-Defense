package com.rugovit.igrica.engine.logic.elements;



import java.util.LinkedList;
import java.util.Random;

import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class ObjectIgre implements GameLogicBranitelj {//,ObjectLinkerLogic  {

	/////// konstante 
	private boolean saljiDaCrtaKaoOznacenog=false;
	private boolean josNijePoslaoTornjuOkpljaliste=true;
	private float udaljenostOdCilja=0;
	public static float mnozitelj=0.4f;// odreduje udaljenost od protivnika tijekom borbe 0.4 je otprilike sredina prema gore predaleko prema dolje preblizu
	private ToranjL toranjPozivatelj;
	private float pocetniHelth;// služi za izraèinavanje postotka kada se šalje prikazniku
	private float pocetniArmor;// služi za izraèinavanje postotka kada se šalje prikazniku
	private float velXUPrik,velYUPrik;
	private float omjDx,omjDy; // množi se za brzinom tako da se dobiju brži i sporiji objekti
	private int grOdX=10,grDoX=50,grOdY=10,grDoY=50; // za random generator od-do, za grupni izbor
	private int brojGrupnihObj; // odreðuje max broj objekata u grupi
	private UIManagerObject touchObj;// objekt na koji je kliknuto poslije klika na ovaj
	private  float startX,startY,velX,velY ;
	private int indikator;
	private GameLogic GL;
	private float Dx,Dy;
	private double  prosloVrijemeUdarca;
    //samounistenje
	private boolean samounistenjeUkljuceno=false;
	private double samounistenjeZaMili,samounistenjePoceoUMili;
	///// varijable
	private boolean koristiSePOmakPremaTocciCentra=false;
	private double  vrijemeIspucavanja, RofFMili;
	//////////vremenske stete
	private float fpsUkupnoUsporavanja;
	private float postotakUsporavanjaOtrovano,postotakUsporavanjaZaledenje,postotakUsporavanjaGorenje ;
	private double pocetakHelthStete=0;
	private float heltStetePoSec=0;
	private boolean helthVremSteta=false;
	private boolean armorVremSteta=false;
	private double pocetakArmorStete=0;
	private float armorStetePoSec=0;
	private boolean boolKretanja=true;
	private double trenutakUsporavanja;
	private double trajanjeGorenja;
	private double trajanjeTrovanja;
	private float postotakUsporavanja;
	private double trajanjeUsporavanja;
	private boolean uspori=false;
	///varijable od strijelaca
	private float radijusStrijelci;
	    private RectF recStrijelci;
	    private boolean jesamLiStrijelac=false;
      	private ProjektilL projektil1;
      	private ProjektilL projektil2;
	    private GameLogicObject tempProtivnik;
	    private float xIspaljivanja=0;
	    private float yIspaljivanja=0;
	    private float rateOfFire;
	    private int ticProvSud=0;
	    private int ticFire,iFire=0;// sprema koliko puta se treba pozvati ova klasa prije nego se ispali projektill
	    private GameLogicProtivnik tempStari=null;
	    private GameLogicProtivnik tempNovi=null;
	//////////////////
	private float xPrijePocNapada,yPrijePocNapada;
	private float pomNaXSync;
	private float pomNaYSync ;
	private boolean pomakNaSync=false;
	private boolean izracunajPauzu=false;
	private double vrijemePauze=0;
	private int obZaKolizijiu=5;//znaci da je kvadrat
	private float tempTime;
	private boolean prvoPokretanje=true;
	private double prosloVrijeme;
	private double kut=0;
	private boolean neDesno=false,neLijevo=false,neGore=false,neDolje=false;
	private PutL tempPut;// 
	private float stetaArmor,stetaHelth,armor,helth;
	private int tipStete;
	private boolean napadPoceo,borbaPocela,protivnikZiv;
	private int mojaPozUListiNap=0;// sprema se pozicija ovog objekta u listi napadaèa objekta kojeg se napada
	private int ticBorbe;
	private float udaracaSvakihSekundi;
    private GameLogicProtivnik protivnik;
	private GameEvent ge;// ovdje ga instanciram pošto se stalno mora stvarat da bi poslao xy, vjeruem da æeovako brže raadit
	private float smjDx,smjDy;
	private float TpS;
	private Random generator=new Random();
	private LinkedList<GameLogicBranitelj> listaGrupe;//ovdje æe biti spremljene reference drugih objekata koji pripadaju istoj grupi kao i ovaj
	private float dx,dy,dxZaPrik,dyZaPrik;
	private float budiTuX,budiTuY;
	private float x,y, pomNaX=-1,pomNaY=-1;//-1 znaèi da su prazne varijeble i nije potrebno ništa s njima raditi
	private RectF rec;
	private GameLogicObject obj;
	private UIManagerObject dvojnik;
	private GameLogicObject tempCestaDodira;
	private GameLogicObject[] listaSudara; //lista u kojoj su reference objekata sa kojima se trenutaèno sudara ovaj objekt
	//////////bool stanja igre
	private boolean braniteljIzKasarne=false;
	private boolean samostalni=false;
	private boolean novoOdrediste=false;// služi da se provjerava dali je odredište u radijusu samo jedanput 
	private boolean pause=false;
	//private boolean boolKretanja=true;
	private boolean prepreka=false;// u slucaju da je naisa na prepreku
	//////////////////////////////
	public ObjectIgre(float velX,float velY,float startX,float startY,int indikator,float picPsecX,float picPsecY ){
		
		this.startX=startX-velX/2;
		this.startY=startY-velY/2;
		this.velX=velX;
		this.velY=velY;
		recStrijelci=new RectF();
		rec=new RectF(startX,startY,startX+velX,startY+velY);
		this.indikator=indikator;
		//this.eMan=eMan;  ///mora primiti referencu na EventManager da bi mogao slati poruke
		x=this.startX;  //centriranje x i y na sredinu objekta
	    y=this.startY;
		Dx=picPsecX; 
		Dy=picPsecY;
		brojGrupnihObj=5;// max broj objekata u grupi je po difoltu 5
		
		ge=new GameEvent(this);
		velXUPrik=velX;// namještam ih ovdje u sluèaju da se ne namjeste eksplicitnno pomoæu metoda 
		velYUPrik=velY;
		
		stetaArmor=10;
		stetaHelth=15;
		armor=100;
		helth=100;
		pocetniHelth=100;// služi za izraèinavanje postotka kada se šalje prikazniku
		pocetniArmor=100;
	
	}
	
	///////////METODE KORIŠTENE PRI INICIJALIZACIJI OBJEKTA////
	public void bilderStvoriBraniteljaIzKasarne(){
		this.braniteljIzKasarne=true;
		jesamLiStrijelac=false;
        samostalni=false;
	} 
	public void bilderStvoriSamostalnogBranitelja(){
		this.braniteljIzKasarne=false;
		jesamLiStrijelac=false;
        samostalni=true;
	} 
	public void bilderStvoriStrijelcaOdOvogObjekta(float radijus,float RofF,ProjektilL projektil){
		 RofFMili=1000/RofF;
		ge.indikator2=2;// oznacava za prikaz da je strijelac
		this.projektil1=projektil;
		 radijusStrijelci=radijus;
		 
		 this.braniteljIzKasarne= samostalni=false;
		recStrijelci.set(rec.centerX()- radijusStrijelci/2, rec.centerY()- radijusStrijelci/2, rec.centerX()+ radijusStrijelci/2, rec.centerY()+ radijusStrijelci/2);
		jesamLiStrijelac=true;
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

	//////////MALI RUN!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void maliRun(GameLogic GL, FazeIgre FI, float TpS, boolean pause) { // mora primiti referencu da bi mogao komunicirati sa GameLogic-om
        this.TpS=TpS;
        this.GL=GL;
        dyZaPrik=0;
        dxZaPrik=0;
 
        koristiSePOmakPremaTocciCentra=false;// resetira zastavicu
   
      if(  prvoPokretanje){
    	  stvariKojeSeIzvrsavajuSamoNaPocetku();
    	  prvoPokretanje=false;
      }
      if(izracunajPauzu){
			vrijemePauze=System.currentTimeMillis()-vrijemePauze;
			izracunajPauzu=false;
	  }
      if(pomakNaSync){
    	  this.setGrupniXY(this.pomNaXSync,this.pomNaYSync);
    	  pomakNaSync=false;
      }
      
      obradiVremenskeStete();
      udaljenostOdCilja=(float)udaljenostDvijeTocke(rec.centerX(),rec.centerY(),this.xPrijePocNapada,yPrijePocNapada);
      
      dx=dxPomak();
      dy=dyPomak();
      prosloVrijeme=System.currentTimeMillis();
      if(this.braniteljIzKasarne){
        if(novoOdrediste)  jeliUnutarRadijusaKruzniceTockaPomaka();
		if(helth>0){
			listaSudara= GL.provjeriKoliziju(this);
		    boolean b=false;
		    b=napadniProtivnike888();
	        if(!b) {
	        	 ge.indikatorBorbe=0;
	        	 //provjeriJesiLiUnutarCeste888();
	        	 pomakniNaPoziciju();
	        	// pomakniRect();
	        	  //if(pomNaX==-1&&pomNaY==-1){
	        	 if(udaljenostOdCilja<(this.velXUPrik+this.velYUPrik)/2){
	        		  provjeriJesiLiUnutarCeste888();
	        		
	   		   }
	        	
	        	  provjeriJesiLiIzvanDodatakaNaCestu();
	        }
	        pomakniRect();
		}
	    else ubijMe(true);
		//posaljiTornjuNovoOkupljaliste();// salje tornju kasarni novo okupljalište
		updateStanjaZaPrikaz(); //salje update polozaja objektu prikaza
      }
      else if(this.jesamLiStrijelac){
        if(novoOdrediste)  jeliUnutarRadijusaKruzniceTockaPomaka();
  		if(helth>0){
  			listaSudara= GL.provjeriKoliziju(this);
  		    boolean b=false;
  		    b=napadniProtivnike888();
  	        if(!b) {
  	        	 ge.indikatorBorbe=0;
  	        	 //provjeriJesiLiUnutarCeste888();
  	        	 pomakniNaPoziciju();
  	        	 if(this.pomNaX==-1&&this.pomNaY==-1){// ako objekt miruje i ne bori se tek onda se provjerava dli ima na koga pucati
  	        		strijelciGlavnaMetoda();
  	        	 }
  	        	 
  	        	 else provjeriJesiLiIzvanDodatakaNaCestu();
  	        	
  	 	        
  	        }
  	        pomakniRect();
  		}
  	    else ubijMe(true);
  		//posaljiTornjuNovoOkupljaliste();// salje tornju kasarni novo okupljalište
  		updateStanjaZaPrikaz(); //salje update polozaja objektu prikaza
        }
      ///// dio za junaka tj, onog koji nije ogranièen sa radijusom tornja
      else if(this.samostalni){
    	  if(helth>0){
  			listaSudara= GL.provjeriKoliziju(this);
  		    boolean b=false;
  		    b=napadniProtivnike888();
  		    if(!b) {
	        	 ge.indikatorBorbe=0;
	        	 //provjeriJesiLiUnutarCeste888();
	        	 pomakniNaPoziciju();
	        	// pomakniRect();
	        	  //if(pomNaX==-1&&pomNaY==-1){
	        	 if(udaljenostOdCilja<(this.velXUPrik+this.velYUPrik)/2){
	        		  provjeriJesiLiUnutarCeste888();
	        		 
	   		      }
	        	 provjeriJesiLiIzvanDodatakaNaCestu();
  		    }
  	       
  	        pomakniRect();
  		}
  	    else ubijMe(true); 
  		updateStanjaZaPrikaz(); //salje update polozaja objektu prikaza
        }
     if(samounistenjeUkljuceno){
    	 samounistenjePoceoUMili+=this.vrijemePauze;
		if(samounistenjeZaMili+samounistenjePoceoUMili<System.currentTimeMillis()){
			this.ubijMe(false);
		}
		
		}
      if(pause){
			izracunajPauzu=true;
			vrijemePauze=System.currentTimeMillis();
		}
		else vrijemePauze=0; 
      this.ge.medic=false;
      }
	
	
	////getersi//////////////////////////////////////////////
   public  float getHealth(){
	   return this.helth;
   } 

   
	public float getXVelUPrikazu(){
		return velXUPrik;
	}
	public float getYVelUPrikazu(){
		return velYUPrik;
	}
	public int getOblZaKol(){
		return obZaKolizijiu;// vraca 5 znaèi da je kvadrat
	}
	public RectF getRect(){ ///vraca svoj rect radi koalision detectiona
		return rec;
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
		// TODO Auto-generated method stub
		return dy;
	}
	@Override
	public float getDx() {
		// TODO Auto-generated method stub
		return dx;
	}
	/////setersi/////////////////////////////////////////////////
	public void setSamounistenjeZa(float sec){
		this.samounistenjeUkljuceno=true;
		samounistenjeZaMili=1000*sec;
		samounistenjePoceoUMili=System.currentTimeMillis();
	}
	@Override
	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
		dvojnik=obj;
	}
	public void jaSamTeStvorio(ToranjL poziv){// ako ga toranj stvara šalje mu svoju referencu radi daljnje komunikacije
		toranjPozivatelj=poziv;
	}
	@Override
	public void setUdaracaSvakihSekundi(float sekunde) {
		udaracaSvakihSekundi=sekunde*1000;
		
	}
	@Override
	public void setMrtavProtivnikPrestani() {
		ge.indikatorBorbe=0;
		napadPoceo=false;
		borbaPocela=false;
		 
         protivnikZiv=false;
		mojaPozUListiNap=0; // ako je bio prvi mora se vratiti u 0 jer inaèe petlja neæe krenuti
		protivnik=null;// obavještava prijje smrti branitelja da je umro tako da nastavi sa normalnim ponašanjem
	}
	public void setGameLogicReferencu(GameLogic gl){GL=gl;}
	public void setXVelUPrik(float xp){
		velXUPrik=xp;
	}
	public void setYVelUPrik(float yp){
		velYUPrik=yp;
	}
	public void setBrojGrupnihObj(int br){
		brojGrupnihObj=br;
	}
	public void setListaGrupe(LinkedList<GameLogicBranitelj> lg){//pomoæu ove metode se grupiraju objekti
		listaGrupe=lg;
	}
	public void setGrupniXY(float px, float  py){
		boolean b=false;
		if(this.GL!=null){// moram ovako jer se ova funkcija pozivaod drugih objekata prije nego u malomrunu dobije referencu gamelogica koja treba jelitockanacesti funkciji
		    int i=0;
		    xPrijePocNapada=pomNaX;
	        yPrijePocNapada=pomNaY;
			while(!b){
				
		        pomNaX=randIzmeduPlusMinus(px,this.velXUPrik);//namješta koordinae na koje se treba pomaknuti
		        pomNaY=randIzmeduPlusMinus(py,this.velXUPrik);
		       
		        novoOdrediste=true;
	        	if(this.braniteljIzKasarne) b= jeliTockaNaCesti( pomNaX,pomNaY);
	        	else if(this.jesamLiStrijelac){
	        		b=this.jeliTockaIzvanDodatakaNaMapu(pomNaX,pomNaY);
	        	}
	        	if(i>10) {
	            	pomNaX=-1;
			        pomNaY=-1;
			        xPrijePocNapada=pomNaX;
			        yPrijePocNapada=pomNaY;
			       break;
	            }
	        	i++;
			}
		}
		else{
			  pomNaX=randIzmeduPlusMinus(px,this.velXUPrik);//namješta koordinae na koje se treba pomaknuti
		        pomNaY=randIzmeduPlusMinus(py,this.velYUPrik);
		        xPrijePocNapada=pomNaX;
		        yPrijePocNapada=pomNaY;
		        novoOdrediste=true;
		}
	     

	}
	public void setGrupniXYSync(float px, float  py){
		pomNaXSync= px;
		pomNaYSync= py;
		pomakNaSync=true;
	}
	public void setGrupniTouchObj(UIManagerObject po){
		touchObj=po;
	}
	public void setGrupnuListu(LinkedList<GameLogicBranitelj> listaG){
		listaGrupe=listaG;
	}
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) { 
		/* if(e.touched){
			 pause=!pause;  //zaustavlja i pusta malirun
			 e.touched=false;
		 }*/
		this.posaljiGrupiDaCrtajuDSuOznaceni(e.imTouched);
		if(e.imTouched){ // ovo se izvršava samo ako je ovaj objekt odabran
		
		  if(e.pomNaX!=-1 && e.pomNaY!=-1){			    
				obradiPomakNa(e.pomNaX,e.pomNaY);
			
		  }
		    if(e.touchedObj!=null){
			   touchObj=e.touchedObj;
			   posaljiGrupiTouchedObj(touchObj);
		   }
		     else touchObj=null; // da bi se prenjela informacija u ovaj objekt da više neba touchObjekta
	    }
		else{
			josNijePoslaoTornjuOkpljaliste=true;//resetira
		}
	if(braniteljIzKasarne||jesamLiStrijelac){
		  ToranjLKasarna temp;
		  temp=(ToranjLKasarna)this.toranjPozivatelj;
		  temp.vojnikIzabranSam(e.imTouched&&josNijePoslaoTornjuOkpljaliste);
		  }
	 }
	
	public void saljiPrikazuDcrtaLikaKaoOznacenog(boolean daNe){
		saljiDaCrtaKaoOznacenog=daNe;
	}
	@Override
	public GameLogicProtivnik getProtivnikaKojegNapadas(){
		return this.protivnik;
	}
	///////PRIVATNE METODE
	private void obradiVremenskeStete(){
		/////trovanje///
		if(this.helthVremSteta){	
			  if(System.currentTimeMillis()<this.pocetakHelthStete+this.trajanjeTrovanja){
				   tempTime=(float)(-prosloVrijeme+System.currentTimeMillis()-vrijemePauze)/1000;
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
					   tempTime=(float)(-prosloVrijeme+System.currentTimeMillis()-vrijemePauze)/1000;
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
	private float dxPomak(){
		tempTime=(float)((-prosloVrijeme+System.currentTimeMillis()-vrijemePauze)/1000);
		if(tempTime>0.5) return Dx/2;
		else return   Dx*tempTime+0.000001f;
	}
	private float dyPomak(){
		tempTime=(float)((-prosloVrijeme+System.currentTimeMillis()-vrijemePauze)/1000);
		if(tempTime>0.5) return Dy/2;
		else return   Dy*tempTime+0.000001f;
	}
	private void  stvariKojeSeIzvrsavajuSamoNaPocetku(){
		if(this.braniteljIzKasarne){
		  ge.x3= this.toranjPozivatelj.getRect().centerX();
		  ge.y3= this.toranjPozivatelj.getRect().centerY();
		  ge.float1= this.toranjPozivatelj.getRect().width()/2;
		}
		 this.vrijemeIspucavanja=System.currentTimeMillis();
	this.prosloVrijeme=System.currentTimeMillis();
	this.velXUPrik=this.dvojnik.getGlavniRectPrikaza().width();
	this.velYUPrik=this.dvojnik.getGlavniRectPrikaza().height();
	}
	private void posaljiTornjuNovoOkupljaliste(float xOku, float yOku){
		toranjPozivatelj.namjestiOkupljaliste(xOku, yOku);
	}
	public void ubijMe(boolean mrtavIliBrisanje){
		this.ge.medic=false;
		if(this.projektil1!=null){
			projektil1.ubijSe();
		}
		if(this.projektil2!=null){
			projektil2.ubijSe();
		}
		if(!mrtavIliBrisanje) ge.indikator=-100;
		  ge.jesamLiZiv=false;
		  dvojnik.GameLinkerIzvrsitelj(ge);
		  GL.mrtavSam(this);
		  ToranjLKasarna temp;
		 if(braniteljIzKasarne||jesamLiStrijelac) {temp=(ToranjLKasarna)this.toranjPozivatelj;
		                                          temp.vojnikIzabranSam(false);}
		 if( protivnik!=null) protivnik.setViseTeNeNapadam(this);
		if( braniteljIzKasarne||jesamLiStrijelac )if(listaGrupe!=null) if(listaGrupe.contains(this)) listaGrupe.remove(this);// i mièe sebe iz liste ggrupe
		  
	}
	private void provjeriJesiLiUnutarCeste888(){
		int i=0;
		neDesno=neLijevo=neGore=neDolje=false;
        while(listaSudara[i]!=null){
			
			if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
			  if(rec.centerX()-this.velXUPrik/2<=( listaSudara[i].getRect().left+listaSudara[i].getRect().width())&& 
					  rec.centerX()+this.velXUPrik/2>=listaSudara[i].getRect().left
						&& rec.centerY()-this.velYUPrik/2<=(listaSudara[i].getRect().top+listaSudara[i].getRect().height())&& 
						rec.centerY()+this.velYUPrik/2>=listaSudara[i].getRect().top){	
				   tempPut=(PutL)listaSudara[i];
				   if(!tempPut.imamLiPutGore()){
					    if(tempPut.getRect().top>rec.centerY() +(this.velYUPrik*1)/4){
					    	if(neGore==false){// akoje veæ i predhodnom while ciklusu naiša na nekoga kojiga je veæ pomakao prema dole da se netrza
					    	y+=dy;// onda ga sao pomièe prema dolje ne naglo nego pomalo sa svakim ciklusom
					    	this.yPrijePocNapada=y+rec.height()/2;
					    	neGore=true;
					    	}
					       }
				       }
				   if(!tempPut.imamLiPutDolje()){// provjerava za dolje 
				    	 if(tempPut.getRect().bottom<rec.centerY()+this.velYUPrik/2){
				    		if(neDolje==false){
				    			y-=dy;
				    			this.yPrijePocNapada=y+rec.height()/2;
				    		    neDolje=true;
				    	      }
				    	 }
				    }
				   if(!tempPut.imamLiPutDesno()){
				    	if(tempPut.getRect().right<rec.centerX()+this.velXUPrik/2) {
				    		if(neDesno==false){
				    		x-=dx;
				    		this.xPrijePocNapada=x+rec.width()/2;
				    		neDesno=true;
				    		}
				    	}
				    }
				   if(!tempPut.imamLiPutLijevo()){
					   if(tempPut.getRect().left>rec.centerX()-this.velXUPrik/2) {
						if(neLijevo==false){ 
						   x+=dx;
						   this.xPrijePocNapada=x+rec.width()/2;
						   neLijevo=true;
						}
					   }
				    }
			   
			  }
			}
			i++;
			if(i>=listaSudara.length) break;
		}
		
	}
	private void provjeriJesiLiIzvanDodatakaNaCestu(){
		int i=0;
		
	
		//////////////////ubacivanje lažnih vrijednosti
		
		int temTipObZaKol=this.getOblZaKol();
		obZaKolizijiu=6;// k
		GameLogicObject[] tempListaSudara=this.GL.provjeriKoliziju(this);
		obZaKolizijiu=temTipObZaKol;
		///////////////////////////////////////////
		neDesno=neLijevo=neGore=neDolje=false;
        while(tempListaSudara[i]!=null){
			
			if(tempListaSudara[i].getIndikator()>=500&&tempListaSudara[i].getIndikator()<600){
				RectF temRec=new RectF();
				temRec.set(this.rec.centerX()-this.velXUPrik/2, this.rec.centerY()+this.velYUPrik/4, this.rec.centerX()+this.velXUPrik/2, this.rec.centerY()+this.velYUPrik/2);
			if(temRec.intersect(tempListaSudara[i].getRect())){
				              RectF recDod=tempListaSudara[i].getRect();
				             if(temRec.left<recDod.right){
				            	 x+=dx;
				             }
				             else if(temRec.right>recDod.left){
				            	 x-=dx;
				             }
				             else if(temRec.top<recDod.bottom){
                            	 y+=dy;
				             }
				             else if(temRec.bottom>recDod.top){
				            	 y-=dy;
				             }
				             this.pomNaY=-1;
				             this.pomNaX=-1;
				             this.touchObj=null;
				             xPrijePocNapada=-1;
					         yPrijePocNapada=-1;
			               }
			           break;
			
			}
			i++;
			if(i>=tempListaSudara.length) break;
		}
		
	}
	private boolean napadniProtivnike888(){
		boolean b=false;
        boolean predalekoOdTornja=false;
        boolean blizuBranitelja=false;
		int i=0;
		GameLogicProtivnik tempProtivnikStar=null,tempProtivnikNov=null;
		if(protivnik!=null)if(!this.protivnik.getJesiLiZiv())  this.protivnik=null;
		//if(listaSudara.length>0){// provjerava dali uopæe ima elemenata u listi
		////////////////////////POCETAK POMICANJA
		     while(mojaPozUListiNap!=1&&listaSudara.length>i){// u sluèaju da je prvi napadac nece provjeravat ima li drugih sa manje napadaca na sebi
			   if(listaSudara[i]!=null)if(listaSudara[i] instanceof GameLogicProtivnik){
				   tempProtivnikNov=(GameLogicProtivnik) listaSudara[i];
				   if(!tempProtivnikNov.jesamLiImunNaBranitelje()){
			       if(listaSudara[i].getIndikator()<0&&listaSudara[i].getIndikator()>-101){
			    	 if(this.braniteljIzKasarne) if(udaljenostDvijeTocke(listaSudara[i].getRect().centerX(),listaSudara[i].getRect().centerY(), toranjPozivatelj.getRect().centerX(),toranjPozivatelj.getRect().centerY())
			    			>toranjPozivatelj.getRect().width()/2) predalekoOdTornja=true;
			    	 if(udaljenostDvijeTocke(listaSudara[i].getRect().centerX(),listaSudara[i].getRect().centerY(),this.rec.centerX(),this.rec.centerY())
				    			<2*(this.velXUPrik+this.velYUPrik)/5+2*(listaSudara[i].getXVelUPrikazu()+listaSudara[i].getYVelUPrikazu())/5)  blizuBranitelja=true;
			    	 if(!predalekoOdTornja|| blizuBranitelja){ // provjerava dali je taj objekt dovoljno blizu tornja 
			         
			          if(tempProtivnikStar==null)tempProtivnikStar=tempProtivnikNov;
			          else if(tempProtivnikStar.getBrojNapadaca()> tempProtivnikNov.getBrojNapadaca()){ 
			        	  								 tempProtivnikStar=tempProtivnikNov;                                    
			                                             }
			    	 }
			        }
			     
			      }
			   }
			      i++;
		       }
		     if(tempProtivnikStar==null&&!this.jesamLiStrijelac&&!this.samostalni){
		    	 tempProtivnikStar= pogledajTrebaLiPomocKolegama();
		     }
		     int tempBrNap=0;
		     if(tempProtivnikStar!=null)tempBrNap=tempProtivnikStar.getBrojNapadaca();
		     if(protivnik!=tempProtivnikStar&&(mojaPozUListiNap==0||mojaPozUListiNap>(tempBrNap+1))){
			 napadPoceo=false;// vraæa zastavice u poèetno stanje da bi se mogao napasti novi protivnik
             borbaPocela=false;
             protivnikZiv=true;
			 if(protivnik!=null) protivnik.setViseTeNeNapadam(this);// služi za punjenje protivnik varijable za daljenj korištenje i za brisanje iz listeNapadaèa prošlog protivnika
			 protivnik=tempProtivnikStar;
	    	}
		
		
		    if(protivnik!=null){
		      if(!borbaPocela){// kada borba poèinje ovo se izvršava i više se ne poziva funkcija za pomicanje
		    	  ge.indikatorBorbe=0;
		    	  borbaPocela= pomakPremaNapadacu();
		    	  ticBorbe=0;
		       }
		      if(!napadPoceo&&protivnik!=null) {// kada tek poèinje sa napadom ovo se izvršava
		    	napadPoceo=true;
		    	protivnik.setJaTeNapadam(this);// salje poruku protivniku da ga napada
		    	mojaPozUListiNap=protivnik.getBrojNapadaca();// pošto je on zadnji koji se prrijavio  to je njegov redni broj
		        }
		      if(borbaPocela&&protivnik!=null){
		    	if(protivnik.getRect().centerX()>rec.centerX())this.ge.indikatorBorbe=1;// borba nadesno
		  		else ge.indikatorBorbe=2;// borba nalijevo 
		    	naciniStetuProtivniku(protivnik);
		       }
			b=true;	 // vraæa true ako je našao objekt i pokrenuo proces napada
		    }
		    else {// ako nema protivnika u blizini vraća se ba mjesto koje je dobio preko touch eventa
		    	ge.indikatorBorbe=0;
		          if(xPrijePocNapada>0&&this.yPrijePocNapada>0){
		    		
			           pomNaX= xPrijePocNapada;
			           pomNaY=yPrijePocNapada;
			        ///novoOdrediste=true;
		    	    }
		    	
		        
		       }
		
		////////////////
		return b;
	}
	private GameLogicProtivnik pogledajTrebaLiPomocKolegama(){
		GameLogicProtivnik  temp=null;
		for(int i=0;this.listaGrupe.size()>i;i++){
			temp=listaGrupe.get(i).getProtivnikaKojegNapadas();
			if(temp!=null){
				if(temp.getJesiLiZiv()){
				
					break;
				}
				else {
					temp=null;
				}
			}
			
		}
		return temp;
	}
	
	private void naciniStetuProtivniku(GameLogicProtivnik temp){
		/*float tics=TpS*udaracaSvakihSekundi;
		if(ticBorbe>tics||ticBorbe==0){// ovo nula je za prvi put da odmah udari
		temp.setStetu(tipStete, stetaHelth, stetaArmor,0,0, 0, 0,0, 0);
		if(temp.getRect().centerX()>rec.centerX())this.ge.indikatorBorbe=3;// udarac nadesno
		else ge.indikatorBorbe=4;// udarac nalijevo
		ticBorbe=0;
		}
		ticBorbe++;*/
		prosloVrijemeUdarca+=this.vrijemePauze;
		if(udaracaSvakihSekundi+ prosloVrijemeUdarca<System.currentTimeMillis()){// ovo nula je za prvi put da odmah udari
		if(this.randIzmeduPlusMinus(0, 100)<this.GL.faIgr.sansaZaUdarac){
		  	temp.setStetu(tipStete, stetaHelth, stetaArmor,0,0,0,0,0,0);
		 
		    if(rec.centerX()>temp.getRect().centerX()) this.ge.indikatorBorbe=4;
		    else ge.indikatorBorbe=3;
		    }
		else{
			
			if(rec.centerX()>temp.getRect().centerX()) this.ge.indikatorBorbe=2;
		    else ge.indikatorBorbe=1;
		}
		prosloVrijemeUdarca=System.currentTimeMillis();
		}
		else{
		    //ako se ne udara mora stojat u napadackom polozaju
		    if(rec.centerX()>temp.getRect().centerX()) this.ge.indikatorBorbe=2;
		    else ge.indikatorBorbe=1;
		 }
	}
	private boolean pomakPremaNapadacu(){//kada doðe do toèke fraæa true
		boolean b=false,xNaCilju=false,yNaCilju=false;
		boolean pomakni =true;
		
		if(this.braniteljIzKasarne)// u sluèaju da je branitelj  iz kasarne  provjerava se dali je izašao iz radijusa kasarne   
			if(udaljenostDvijeTocke(protivnik.getRect().centerX(),protivnik.getRect().centerY(), toranjPozivatelj.getRect().centerX(),toranjPozivatelj.getRect().centerY())
	    			>=toranjPozivatelj.getRect().width()/2+(this.velXUPrik+this.velYUPrik)/2) pomakni=false;
			/*if(udaljenostDvijeTocke(toranjPozivatelj.getRect().centerX(),toranjPozivatelj.getRect().centerY(), toranjPozivatelj.getRect().centerX(),toranjPozivatelj.getRect().centerY())
	    			>=toranjPozivatelj.getRect().width()/2) pomakni=false;*/
		 if(udaljenostDvijeTocke(protivnik.getRect().centerX(),protivnik.getRect().centerY(),this.rec.centerX(),this.rec.centerY())
	    			<(this.velXUPrik+this.velYUPrik)/4)  pomakni=true;
		if(pomakni){
			  ge.indikatorBorbe=0;
		      float dodatakX;
		      if(protivnik.getRect().centerX()>rec.centerX()) dodatakX= mnozitelj*(-velXUPrik)-mnozitelj*(protivnik.getXVelUPrikazu());//radi korekciju do koje toèke treba da, tj do ruba hdje se spajaju dvije slièice
		      else dodatakX= mnozitelj*(velXUPrik)+mnozitelj*(protivnik.getXVelUPrikazu());// dodatci u odnosu na velièinu u prikazu
		      float ciljX=protivnik.getRect().centerX()+dodatakX;//-dvojnik.getGlavniRectPrikaza().width();
		      float ciljY=0;//=protivnik.getRect().centerY(); 
		      if(mojaPozUListiNap==1)ciljY=protivnik.getYPozUPrik()+protivnik.getYVelUPrikazu()-this.velYUPrik/2;// ako je na udaljenosti dy  od cilja i ako je prvi napadac
	    	   else if(mojaPozUListiNap%2==0)ciljY=protivnik.getYPozUPrik()+protivnik.getYVelUPrikazu()-this.velYUPrik/2+protivnik.getYVelUPrikazu()/3; // rasporeðuje ih tako da se svi vide kada napadaju
		       else if(mojaPozUListiNap%2!=0)ciljY=protivnik.getYPozUPrik()+protivnik.getYVelUPrikazu()-this.velYUPrik/2-protivnik.getYVelUPrikazu()/3;
		
		      if( !xNaCilju&&rec.centerX()>=ciljX-dx&&rec.centerX()<=dx+ciljX) {// ako je blizu cilja samo ga namješta direktno na poziciju
			       x=ciljX-rec.width()/2;
		           xNaCilju=true;
		            }
		      if(xNaCilju&&!yNaCilju&&rec.centerY()>=ciljY-dy&&rec.centerY()<=dy+ciljY){// ako je blizu cilja samo ga namješta direktno na poziciju, ali samo kada je x veæ na cilju
		    	   y=ciljY-rec.height()/2;
		    	   /*if(mojaPozUListiNap==1)y=ciljY-rec.height()/2;// ako je na udaljenosti dy  od cilja i ako je prvi napadac
		    	   else if(mojaPozUListiNap%2==0)y=ciljY-rec.height()/2+protivnik.getYVelUPrikazu()/3; // rasporeðuje ih tako da se svi vide kada napadaju
			       else if(mojaPozUListiNap%2!=0)y=ciljY-rec.height()/2-protivnik.getYVelUPrikazu()/3;*/
	    		   yNaCilju=true;
	     	  }
		      if(!xNaCilju||!yNaCilju){
		    	  dx=3*dx/5;// smanjuje brzinu borbe, neæe imati utjecajua na sljedeæi poziv jer se ponovo odreðume u run-u
		    	  dy=3*dy/5;
		    	
		    	  pomakPremaTockiCentar(ciljX,ciljY);// pomièe samo u sluèaju ako jedan od varijabli nije na cilju
		      }
		      else b=true;
		 }
		 else{
			 protivnik.setViseTeNeNapadam(this);
			 mojaPozUListiNap=0;
			 protivnik=null;
		 }
		  return b;
}
	
	private void pomakniUnutarCeste(GameLogicObject obj){// pomièe unutar ceste u odnosu na smijer koji pokazuje cesta , da nebi bilo problema pri prijelazu imeðu segmenata puta
			if(obj.getDx()>0||obj.getDx()<0){//znaèi vodoravni put
                if(y<obj.getY()) y=obj.getY();
                else if(y+velY>(obj.getY()+obj.getRect().height())) y=obj.getRect().height()+obj.getY()-velY;
			}
			else if(obj.getDy()>0||obj.getDy()<0){//naèi vodoravni put
                if(x<obj.getX()) x=obj.getX();
                else if(x+velX>(obj.getX()+obj.getRect().width()))x=obj.getRect().width()+obj.getX()-velX;
			}
	}
	
	private void hodajPoCesti(){// iskoristit æu ovo za napravit "pse" koji æe trèat u suprotnom smijerui tako napadat protivnike
		int i=0;
		smjDx=smjDy=0; // stavljam ih sve na nulu tako da nema grešaka iz prošlog pokreta
		int k=0;	
		  while(listaSudara[i]!=null&&listaSudara.length>=i){	
				if(k<2&&(listaSudara[i].getIndikator()>200)&&
						(listaSudara[i].getIndikator()<301)	){ //k je zato da provjerava samo  2 odjeljka ceste istovremeno
	                    if(smjDx!=listaSudara[i].getDx()){ /*provjerava dali je dx i dy jednak prijašnjem ako je onda ga ne uzima*/
		                       if(listaSudara[i].getIndikator()==202)smjDx+=listaSudara[i].getDx()*0.7; // radi redukciju na okuci, vrijedi samo za pravi kut                                          
	                           else if(listaSudara[i].getIndikator()==201) {
	                        	                                    smjDx+=listaSudara[i].getDx();
	                        	                                    pomakniUnutarCeste(listaSudara[i]);
	                                                                }
	                                                }
	                    if(smjDy!=listaSudara[i].getDy()){  
	                           if(listaSudara[i].getIndikator()==202) smjDy+=listaSudara[i].getDy()*0.7;  
	                           else if(listaSudara[i].getIndikator()==201){
	                        	   smjDy+=listaSudara[i].getDy();
	                        	   pomakniUnutarCeste(listaSudara[i]);  
	                               }
	                    }
				        pomNaY=-1;// da ne radi pomak na poziciju
				        pomNaX=-1;
				        k++;                        
				     }        
				i++;
				}
		x+=smjDx*omjDx;
		y+=smjDy*omjDy;
		
		
		
	}
	private void updateStanjaZaPrikaz(){
		  ge.x=rec.centerX();
		  ge.y=rec.centerY();
		  ge.vrijemePauze=(int)vrijemePauze;
		  ge.x2= dxZaPrik;
		  ge.y2= dyZaPrik;
		  if(!koristiSePOmakPremaTocciCentra){
			  ge.x2=0;
			  ge.y2=0;
		  }
		  if(!this.samostalni){ge.x3= this.toranjPozivatelj.getRect().centerX();
		                       ge.y3= this.toranjPozivatelj.getRect().centerY();
		                      ge.float1= this.toranjPozivatelj.getRect().width()/2;
	                             }
		  ge.helth=helth/(pocetniHelth/100);// salje postotak heltha u odnosu na poèetni koji mu je postavljen
		  ge.armor=armor/(pocetniHelth/100);// racuna u onosu na pocetni gekth tako da ce u biti biti oo helth armor prikazan
		  dvojnik.GameLinkerIzvrsitelj(ge);
		  ge.indikator=0;//vraca indikator na nulu koji služi isklju�?ivo za izbacivanje  prikaznog dijela kao izabranog u uimanageru
		  ge.zastavica1=saljiDaCrtaKaoOznacenog;
		  saljiDaCrtaKaoOznacenog=false;// trebalo bi se stalno osvjezavati jer bi se moglo dogoditi da brabitelj umre i ne uspije poslati da vieš nije ozna�?en
		  
  
	}
	private void pomakniRect(){
		this.xIspaljivanja=rec.centerX();
		this.yIspaljivanja=rec.centerY();
		rec.set(x, y,x+velX, y+velY);
		recStrijelci.set(rec.centerX()- radijusStrijelci/2, rec.centerY()- radijusStrijelci/2, rec.centerX()+ radijusStrijelci/2, rec.centerY()+ radijusStrijelci/2);
	}
	private void pomakniNaPoziciju(){
		/*	
		 if(rec.centerX()!=pomNaX) kut= Math.atan(Math.abs((rec.centerY()-pomNaY)/(rec.centerX()-pomNaX)));
			  if(pomNaX!=-1) {if(rec.centerY()>pomNaY) y-=dy*Math.sin(kut);
			  else y+=dy*Math.sin(kut);
			}
			if(pomNaX!=-1){
				if(rec.centerX()>pomNaX) x-=dx*Math.cos(kut);
			    else x+=dx*Math.cos(kut);
			}
		    if(rec.centerX()<(pomNaX+dx)&&rec.centerX()>(pomNaX-dx)){ x=pomNaX-rec.width()/2;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
            pomNaX=-1;//oznaèava da je stigao do toèke
                      }
			if(rec.centerY()<(pomNaY+dy)&&rec.centerY()>(pomNaY-dy)){ y=pomNaY-rec.height()/2;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
            pomNaY=-1;//oznaèava da je stigao do toèke
   }*/
			
	/*		
		if(touchObj!=null){ pomNaX=touchObj.getX();
         pomNaY=touchObj.getY();
               }  
		
		    if(pomNaX!=-1){ // znaèi da je došao do toèke i nije potrebno micanje
		    	ge.setIndikatorBorbe(0);
	           if(rec.centerX()>pomNaX&&!neDesno) {
	        	                                    x-=dx;
	                                              }
	           else if(rec.centerX()<pomNaX&&!neLijevo){
	        	                                    x+=dx;
	                                              }
	           if(rec.centerX()<(pomNaX+dx)&&rec.centerX()>(pomNaX-dx)){ x=pomNaX-rec.width()/2;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
	                                     pomNaX=-1;//oznaèava da je stigao do toèke
	                            }
		     }
		   if(pomNaY!=-1){ // znaèi da je došao do toèke i nije potrebno micanje
			   ge.setIndikatorBorbe(0);
		        if(rec.centerY()>pomNaY&&!neGore){
		        	                              y-=dy;
		                                          }
		        else if(rec.centerY()<pomNaY&&!neDolje){
		        	                              y+=dy;
		                                          }
		        if(rec.centerY()<(pomNaY+dy)&&rec.centerY()>(pomNaY-dy)){ y=pomNaY-rec.height()/2;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
		                                     pomNaY=-1;//oznaèava da je stigao do toèke
		                            }
			    }*/
		   /////
		   if(touchObj!=null){ pomNaX=touchObj.getX();
	         pomNaY=touchObj.getY();
	               }  
		   if(pomNaX!=-1||pomNaY!=-1){
			   
			   if(rec.centerX()<(pomNaX+dx+0.5f)&&rec.centerX()>(pomNaX-dx-0.5f)){ x=pomNaX-rec.width()/2;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
               pomNaX=-1;//oznaèava da je stigao do toèke
                    }
			   if(rec.centerY()<(pomNaY+dy+0.5f)&&rec.centerY()>(pomNaY-dy-0.5f)){ y=pomNaY-rec.height()/2;//ako je blizu toèke na koju se treba pomaknuti automatski æe skoèiti na nju
               pomNaY=-1;//oznaèava da je stigao do toèke
                }
			   if(pomNaX!=-1||pomNaY!=-1) pomakPremaTockiCentar(pomNaX,pomNaY);
		   }
		  
		 
	}
    private void posaljiGrupiXY(float px, float py){//trebala bi slati svim obektima iz grupe itu naredbu za pommicanje
	if(listaGrupe!=null){ 
		                 int i=0; 
		                 while(i<=listaGrupe.size()-1){
		                 if( listaGrupe.get(i)!=null)  listaGrupe.get(i).setGrupniXY(px,py); 
		                	 // listaGrupe.get(i).setGrupniXY(randIzmeduPlusMinus(px,velXUPrik),randIzmeduPlusMinus(py,velYUPrik));
		                 i++;
	                         }
	               }
	  }
    private void posaljiGrupiXYSync(float px, float py){//trebala bi slati svim obektima iz grupe itu naredbu za pommicanje
    	if(listaGrupe!=null){ 
    		                 int i=0; 
    		                 while(i<=listaGrupe.size()-1){
    		                 if( listaGrupe.get(i)!=null)  listaGrupe.get(i).setGrupniXYSync(px,py); 
    		                	 // listaGrupe.get(i).setGrupniXY(randIzmeduPlusMinus(px,velXUPrik),randIzmeduPlusMinus(py,velYUPrik));
    		                 i++;
    	                         }
    	               }
    	  }
private void posaljiGrupiTouchedObj(UIManagerObject po){//trebala bi slati svim obektima iz grupe itu naredbu za pommicanje
	      if(listaGrupe!=null)
	    	 {int i=0;
		      while(listaGrupe.size()-1>=i){
			  listaGrupe.get(i).setGrupniTouchObj(po);
			  i++;
		      }
	    	 }
  }
private void posaljiGrupiDaCrtajuDSuOznaceni(boolean daNE){//trebala bi slati svim obektima iz grupe itu naredbu za pommicanje
    if(listaGrupe!=null)
  	 {int i=0;
	      while(listaGrupe.size()-1>=i){
	    	  ObjectIgre temp=(ObjectIgre)( listaGrupe.get(i));
	    	  temp.saljiPrikazuDcrtaLikaKaoOznacenog(daNE);
		  i++;
	      }
  	 }
}
private void  jeliUnutarRadijusaKruzniceTockaPomaka(){
	float [] ar=null;
	if(udaljenostDvijeTocke(pomNaX,pomNaY,toranjPozivatelj.getRect().centerX(),toranjPozivatelj.getRect().centerY())
			>toranjPozivatelj.getRect().width()/2){// ako je toèka gdje se treba iæi izvan radijusa tornja
		    ar=izracunajTockePresjekaKruzniceIPravca(toranjPozivatelj.getRect().centerX(),toranjPozivatelj.getRect().centerY(),toranjPozivatelj.getRect().width()/2,pomNaX,pomNaY,rec.centerX(),rec.centerY());
		    if(ar[0]==Float.NEGATIVE_INFINITY||ar[0]==Float.POSITIVE_INFINITY||ar[1]==Float.NEGATIVE_INFINITY||ar[1]==Float.POSITIVE_INFINITY|| 
		    		ar[2]==Float.NEGATIVE_INFINITY||ar[2]==Float.POSITIVE_INFINITY||ar[3]==Float.NEGATIVE_INFINITY||ar[3]==Float.POSITIVE_INFINITY){// ako završi izvan kruga onda æe joj jedna od toèaka biti beskonaèno, u to m se sluèaju mora vratiti u središta
		    	    pomNaX=-1;
			        pomNaY=-1;
			        xPrijePocNapada=pomNaX;
			        yPrijePocNapada=pomNaY;
		    }
		    else{
		         if(udaljenostDvijeTocke(ar[0],ar[1],pomNaX,pomNaY)<udaljenostDvijeTocke(ar[2],ar[3],pomNaX,pomNaY)){
		        	 pomNaX=ar[0];
				        pomNaY=ar[1];
				        xPrijePocNapada=pomNaX;
				        yPrijePocNapada=pomNaY;
		         }
		         else{
		        	 pomNaX=ar[2];
				        pomNaY=ar[3];
				        xPrijePocNapada=pomNaX;
				        yPrijePocNapada=pomNaY;
		         }
		    }
		    
	}
	novoOdrediste=false;
}
////////NEKE C
private boolean jeliTockaNaCesti(float xT,float yT){
	RectF tempRect=new RectF(xT,yT,xT+1,yT+1);
	//////////////////spremam vrijednosti stvarnih varijebli prije nego što ubacim lažne da bih mogao iskoristiti koalizion detection sustav
	int temTipObZaKol=this.getOblZaKol();
	RectF temRectGlavni=this.getRect();
	//////////////////ubacivanje lažnih vrijednosti
	obZaKolizijiu=2;// kvadrat kojeg zanima samo sudar sa cestom
	this.rec=tempRect;
	//////////////////sada konacno provjerava dali je tocka na cesti
	boolean tocNaCest=false;
	GameLogicObject[] tempListaSudara=this.GL.provjeriKoliziju(this);
	if(tempListaSudara!=null)	if(tempListaSudara[0]!=null){
		tocNaCest=true;
		tempCestaDodira=tempListaSudara[0];
	}
	////////////////vraca prijasnje vrijednostia
	obZaKolizijiu=temTipObZaKol;
	rec=temRectGlavni;
	//////////////////
	return tocNaCest;
	
} 
private boolean jeliTockaIzvanDodatakaNaMapu(float xT,float yT){
	RectF tempRect=new RectF(xT,yT,xT+1,yT+1);
	//////////////////spremam vrijednosti stvarnih varijebli prije nego što ubacim lažne da bih mogao iskoristiti koalizion detection sustav
	int temTipObZaKol=this.getOblZaKol();
	RectF temRectGlavni=this.getRect();
	//////////////////ubacivanje lažnih vrijednosti
	obZaKolizijiu=6;// kvadrat kojeg zanima samo sudar sa cestom
	this.rec=tempRect;
	//////////////////sada konacno provjerava dali je tocka na cesti
	boolean tockaIzvan=true;
	
	GameLogicObject[] tempListaSudara=this.GL.provjeriKoliziju(this);
	if(tempListaSudara!=null)for(int i=0; tempListaSudara.length>i;i++){
		if(tempListaSudara[i]!=null){
		if(tempListaSudara[i].getIndikator()==501) tockaIzvan=false;
		
	                           }
	}
	////////////////vraca prijasnje vrijednostia
	obZaKolizijiu=temTipObZaKol;
	rec=temRectGlavni;
	//////////////////
	return tockaIzvan;
	
} 
private float[] izracunajTockePresjekaKruzniceIPravca(float p,float q,float r, float x1,float y1, float x2, float y2 ){
	float a=0, b=0, c=0,xa=0,xb=0,ya=0,yb=0;
	float A=(y2-y1)/(x2-x1);
	if(A!=Float.NEGATIVE_INFINITY&&A!=Float.POSITIVE_INFINITY){// u sluèaju da je vertikalna 
	    float AA=A*A;
	     a =1+AA;
	    //float b=-2*p-2*y1*AA-2*A*q+2*A*y1;
	     b=-2*p-2*x1*AA+2*A*y1-2*A*q;
	    //float c=2*A*x1*q-2*A*x1*y1+(-q+y1)*(-q+y1)-r*r+AA*x1*x1;
	     c=p*p+AA*x1*x1-2*A*x1*y1+2*A*x1*q+(y1-q)*(y1-q)-r*r;
	 	 xa=(float)((-b-Math.sqrt(b*b-4*a*c))/(2*a));
		 xb=(float)((-b+Math.sqrt(b*b-4*a*c))/(2*a));
		 ya= A*xa-A*x1+y1;
		 yb= A*xb-A*x1+y1;
	}
	else{
		xa=x1;
		xb=x1;
	  	a=1;
	  	b=-2*q;
	    c=q*q-r*r+(x1-p)*(x1-p);
	    ya=(float)((-b-Math.sqrt(b*b-4*a*c))/(2*a));
		yb=(float)((-b+Math.sqrt(b*b-4*a*c))/(2*a));
	}

	
	float[] ar= {xa,ya,xb,yb};
	return ar;
}
private void pomakPremaTockiCentar(float xC,float yC){//namješta centar ovog kvadarata
	ge.indikatorBorbe=0;
	double kut;
	koristiSePOmakPremaTocciCentra=true;;
    kut=izracunajKut(rec.centerX(),rec.centerY(),xC,yC);// saljem mu centar ovog kvadrata
    float pomak=0;
	if(yC>0){
		
		if(xC>0){
			pomak=(float) (dy*Math.sin(kut));
		}
		else pomak=dy;
	
		
		if(rec.centerY()>yC){
		    	y-=pomak;
		    	dyZaPrik=-pomak;
		    }
	    else{
	    	  y+=pomak;
	    	  dyZaPrik=pomak;
	    }
	        }
	if(xC>0){
		if(yC>0){	pomak=(float) (dx*Math.cos(kut));
		         }
        else {
        	pomak=dx;
		}
        if(rec.centerX()>xC){
        	   x-=pomak;
        	   dxZaPrik=-pomak;
              }
        else {
        	   x+=pomak;
        	   dxZaPrik=pomak;
            }
		  
		
	}
}

private float randIzmeduPlusMinus(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak
	if(b==0) b=1;
	float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
	if(generator.nextBoolean()){r=r*-1;}
	return r+a;
 }
private double udaljenostDvijeTocke(float ax,float ay, float bx,float by){// predpostavlja da se sve odvija u prvom kvadrantu
	 return Math.hypot(ax-bx,ay-by);
}
private double izracunajKut(float ax,float ay,float bx, float by){// vraæa kut izmeðu pravca kroz dvije tocke i horizontale
	double k=0;
	if(ax!=bx) k= Math.atan(Math.abs((ay-by)/(ax-bx)));
	return k;
}
public void obradiPomakNa(float x, float y){
	if(this.braniteljIzKasarne){
		if(jeliTockaNaCesti(x,y)&&this.jeliTockaIzvanDodatakaNaMapu(x,y)){
       RectF tempRec=new RectF();
       tempRec.set(toranjPozivatelj.getRect().left-10*toranjPozivatelj.getRect().width()/100, toranjPozivatelj.getRect().top-10*toranjPozivatelj.getRect().height()/100,
    		   toranjPozivatelj.getRect().right+10*toranjPozivatelj.getRect().width()/100, toranjPozivatelj.getRect().bottom+10*toranjPozivatelj.getRect().height()/100);
	   if(!tempRec.contains(x,y)){
		   josNijePoslaoTornjuOkpljaliste=false;
		   ge.indikator=2;
		   
	    }
	   else {
		   this.setGrupniXYSync(x,y);//namještam gdje treba iæi preko ove funkcije da bi se provjerilo jeli unutar radijusa tornja
		   posaljiGrupiXYSync(x,y);
		   posaljiTornjuNovoOkupljaliste(x,y);// salje tornju kasarni novo okupljalište
	   }
	  }
	}
	else if(this.jesamLiStrijelac){
		if(this.jeliTockaIzvanDodatakaNaMapu(x,y)){
		   this.setGrupniXYSync(x,y);//namještam gdje treba iæi preko ove funkcije da bi se provjerilo jeli unutar radijusa tornja
		   posaljiGrupiXYSync(x,y);
		   posaljiTornjuNovoOkupljaliste(x,y);// salje tornju kasarni novo okupljalište
		   RectF tempRec=new RectF();
	       tempRec.set(toranjPozivatelj.getRect().left-10*toranjPozivatelj.getRect().width()/100, toranjPozivatelj.getRect().top-10*toranjPozivatelj.getRect().height()/100,
	    		   toranjPozivatelj.getRect().right+10*toranjPozivatelj.getRect().width()/100, toranjPozivatelj.getRect().bottom+10*toranjPozivatelj.getRect().height()/100);
		   if(!tempRec.contains(x,y)){
			   josNijePoslaoTornjuOkpljaliste=false;
			   ge.indikator=2;
			   
		   }
		}
	}
	ge.pomNaX=-1;//poništava prijašnje moguæe ciljeve pomaka tj. koordinate
	ge.pomNaY=-1;// namješta ga na -1 što znaèi da se neæe zapisivat više u varijable

}
/////METODE OD STRIJELCA


private void strijelciGlavnaMetoda(){
	 ticFire=Math.round(TpS/rateOfFire);
	   listaSudara=null;
	   RectF tempRectF=new RectF(rec);
	   tempRectF.set(rec);
	   rec.set(this.recStrijelci);
	   listaSudara= GL.provjeriKoliziju(this);
	   rec.set(tempRectF);
	   vrijemeIspucavanja+=vrijemePauze;
	   if(vrijemeIspucavanja+this.RofFMili<System.currentTimeMillis()){
		  
		   tempProtivnik=izvuciObjKojegSeGada();
	          if(tempProtivnik!=null){
	            // if(prosloVrijeme+rateOfFire*1000<System.currentTimeMillis()){
	        	  ge.indikatorBorbe=11;
	        	 //posaljiPrikazu();// ovo sam stavio ovdje jer je u izvrsitelju od prkaza metoda koja ce vratiti nazad podatke od pocetku 
			      ispucajProjektil();
			      ge.pomNaX=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
			      ge.pomNaY=tempProtivnik.getRect().centerY();
			      prosloVrijeme=System.currentTimeMillis();
	              iFire=0;
	          
	          }
	          else ge.indikatorBorbe=0;// znaci da nema nikoga
	     }
	     //else  ge.indikatorBorbe=0;// znaèi da se ništa ne dogada
	   else{ 
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
	       }

	   ge.x3=1000/this.rateOfFire;// salje vrijeme izmeðu dva ispaljivanja
	//posaljiPrikazuStrijelci();
} 

private void ispucajProjektil(){
	// projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik.getRect().centerX(),this.tempProtivnik.getRect().bottom);
	if(projektil1.jesiDosaoNaCilj()) {
		 ge.indikatorBorbe=12;// znaèi da je ispaljen
		projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik,true);
		vrijemeIspucavanja=System.currentTimeMillis();
	}
	
}
private GameLogicObject izvuciObjKojegSeGada(){
	int i=0;
	tempStari=null;
	tempNovi=null;
	while(listaSudara[i]!=null){
		if(listaSudara[i].getIndikator()>=-100&&listaSudara[i].getIndikator()<=-1)// provjerava dali je igraè od protivnika
			tempNovi=(GameLogicProtivnik)listaSudara[i];
		{   if(tempStari==null) tempStari=tempNovi;
		   else if(tempNovi.getRedBrPutaNaKojemSi()>tempStari.getRedBrPutaNaKojemSi())  tempStari=tempNovi;
		         
		      }
		i++;
		if(listaSudara.length<=i) break;
	}
	return (GameLogicObject)tempStari;
}

//////////////////////////////////////
/////METODE ZA HELTH
public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){
	
	if(tip==1){
		 float minus=(helth/100)*this.armor;
	     this.helth-=helth-minus;
	     this.armor-=armor;
	     if(armor<0)this.armor=0;
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
	     if(armor<0)this.armor=0;
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
		   if(armor<0)this.armor=0;
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
	     if(armor<0)this.armor=0;
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
		   if(armor<0)this.armor=0;
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
	     if(armor<0)this.armor=0;
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
		   if(armor<0)this.armor=0;
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
}
/*
public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){//tip 1// normalno pomoæu formule, 2- direktno bez formule
	if(tip==1){
			 float minus=(helth/100)*this.armor;
		     this.helth-=helth-minus;
		     this.armor-=armor;
		     if(armor<0)this.armor=0;
	     
	}
	if(tip==2){
		       this.helth-=helth;
		       this.armor-=armor;
			   if(armor<0)this.armor=0;
	}
	else if(tip==3){//3 i 4 oznacava zaledivanje i animacija ce biti zaledivanje
		float minus=(helth/100)*this.armor;
	     this.helth-=helth-minus;
	     this.armor-=armor;
	     if(armor<0)this.armor=0;
	     if(postUsp>0)uspori=true;
	     this.ge.zaleden=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
	     this.trenutakUsporavanja=System.currentTimeMillis();
	     this.postotakUsporavanja=postUsp;
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
		   if(armor<0)this.armor=0;
		   if(postUsp>0)  uspori=true;
		   this.ge.zaleden=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
		     this.trenutakUsporavanja=System.currentTimeMillis();
		     this.postotakUsporavanja=postUsp;
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
	     if(armor<0)this.armor=0;
	     if(postUsp>0)uspori=true;
	     this.ge.gorim=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
	     if(postUsp>0)  this.trenutakUsporavanja=System.currentTimeMillis();
	     if(postUsp>0) this.postotakUsporavanja=postUsp;
	     this.trajanjeGorenja=vrijUsp*1000;
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
		   if(armor<0)this.armor=0;
		   if(postUsp>0)  uspori=true;
		   this.ge.gorim=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
		   if(postUsp>0) this.trenutakUsporavanja=System.currentTimeMillis();
		   if(postUsp>0) this.postotakUsporavanja=postUsp;
		     this.trajanjeGorenja=vrijUsp*1000;
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
	     if(armor<0)this.armor=0;
	     if(postUsp>0)uspori=true;
	     this.ge.otrovan=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
	     if(postUsp>0)  this.trenutakUsporavanja=System.currentTimeMillis();
	     if(postUsp>0) this.postotakUsporavanja=postUsp;
	     this.trajanjeTrovanja=vrijUsp*1000;
	     pocetakHelthStete=System.currentTimeMillis();
	     heltStetePoSec=helthPoSecGub;
	 	helthVremSteta=true;
	 	if(armorPoSecGub>0) this.trajanjeGorenja=vrijUsp*1000;
	 	if(armorPoSecGub>0)armorVremSteta=true;
	 	if(armorPoSecGub>0) pocetakArmorStete=System.currentTimeMillis();
	 	if(armorPoSecGub>0)armorStetePoSec=armorPoSecGub;
	    		 
	     
	}
	else if(tip==8){//7 i 8 oznacava trovanje i animacija ce biti trovanje
	       this.helth-=helth;
	       this.armor-=armor;
		   if(armor<0)this.armor=0;
		   if(postUsp>0)  uspori=true;
		   this.ge.otrovan=true;// ovdje se pokreće animacija zaledivanja i samo ce zato sluziti tip napada + i za ona prva dva
		   if(postUsp>0) this.trenutakUsporavanja=System.currentTimeMillis();
		   if(postUsp>0) this.postotakUsporavanja=postUsp;
		     this.trajanjeTrovanja=vrijUsp*1000;
		     pocetakHelthStete=System.currentTimeMillis();
		     heltStetePoSec=helthPoSecGub;
		 	helthVremSteta=true;
		 	if(armorPoSecGub>0) this.trajanjeGorenja=vrijUsp*1000;
		 	if(armorPoSecGub>0)armorVremSteta=true;
		 	if(armorPoSecGub>0) pocetakArmorStete=System.currentTimeMillis();
		 	if(armorPoSecGub>0)armorStetePoSec=armorPoSecGub;
   }
}
*/
@Override
public void setTipStete(int tipStet) {
	this.tipStete=tipStete;
}
@Override
public void dodajHelth(float helth) {
	if(helth+this.helth>this.pocetniHelth)this.helth=this.pocetniHelth;
	else this.helth+=helth;
}
@Override
public void dodajArmor(float armor) {
	if(armor+this.armor>this.pocetniArmor)this.armor=this.pocetniArmor;
	else this.helth+=helth;
}

@Override
public void setPocetniXY(float x, float y) {
	budiTuX=x;
	budiTuY=y;
	
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
public boolean lijeciMe(float helth,float armor) {
	// TODO Auto-generated method stub
	
	
	boolean b=false;
	if(this.helth<this.pocetniHelth&&helth>0){
		b=true;
	}
	
	
	
	if(this.armor<this.pocetniArmor&&armor>0){
		b=true;
		
	}
	this.helth+=helth;
	if(this.helth>this.pocetniHelth) this.helth=pocetniHelth;
	this.armor+=armor;
	if(this.armor>this.pocetniHelth) this.helth=pocetniHelth;
	this.ge.medic=b;
	  dvojnik.GameLinkerIzvrsitelj(ge);
	return b;
}







}