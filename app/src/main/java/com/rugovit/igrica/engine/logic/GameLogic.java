package com.rugovit.igrica.engine.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.RectF;
import android.text.format.Time;
import android.util.Log;

import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.logic.elements.GameLogicProtivnik;
import com.rugovit.igrica.engine.logic.elements.PutL;
import com.rugovit.igrica.engine.ui.Taskbar;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.elements.MusicManager;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;


public class GameLogic implements Runnable {
	private boolean  pokreniValove=false;
	
	private boolean pobjedaPoraz=false;
	private boolean provjeraKrajaGotova=false;
	
	
	
	private boolean systemPauze=false;
	private boolean dealWithSystemPauze=false;
	private boolean slikeUnlodirane=false;
	private boolean slikeLodirane=false;
	private boolean srediVarijableZaSystemPauze=false;
	private int prosjekVremena=0;
	private int brojacProsjeka=0;
	private int trenutacnoNajdaljaCesta=0;
	/////
    private long razlikaVremena;
	public boolean gasiIVratiNazad=false;
	private boolean resetirajIgru=false;
  ////////statièke varijable
 private  boolean Pauza=false;	
 private  boolean izracunajPauzu;
 private  double vrijemePauze;	
 private  int VelListeSud=20;
 /////parametri igre 
 private int iListeSudara=0;
 private int brZvjezdica=0;
 private boolean pocmiProvjeravatiZaKrajIgre=false;
 private float ukupniNovac=0;
 private int brZivota=0;
 private float bodovi=0;// bodovi æe biti */
 ////////////////
 private float fps=20;
 private LinkedList<GameLogicObject> listaMakniObj;// spremaju se objekti koji bi trebali biti oduzeti
 private GameLogicObject[] listaCesta;
 private  GameLogicObject[] ar; 
 private GameLogicObject temp;
 private RectF tempRect;
 boolean zahtjevZaPause=false;
 private Taskbar taskbar;
 private boolean runLista=false;
 private LinkedList<GameLogicObject> listaDodaObj;//spremaju se objekti koji trebaju biti dodani
 private Time time;	
 public FazeIgre faIgr;

 private Resources res;	// da bi mogao dohvaæati slike iz ove klase
 private UIManager uiMan;
 private int timeMs=0,timeS=0,timeM=0,timeH=0;// varijeble od vremena
 private boolean prviCiklus=true;// bit koji signalizira da je igra tek pokrenuta
 private LinkedList<GameLogicObject> listaProt;
 public LinkedList<GameLogicObject> listaBran;
 private int indexRun; 
 private GameLogicObject klijent,s;
 private int threadWait, clock;//clock se dobija kroz konstruktor a pomoæu njega se raèuna threadWAIT ZA DIREKTNU PRIMJENU U RUN-U
 private boolean runBit;//true/false bit od glavne petlje
 private Thread th; //thread od klase
 /////////dinamièko proraèunavanje TpS za objekte
 private int dinTicUSec=0;
 private double dinProsloVrijeme=0;
 /////////////////////////
 private Activity ac;
 public GameLogic( int clock,UIManager uiMan, FazeIgre faIgr, Activity ac){
	 this.ac=ac;
	 listaProt=new LinkedList<GameLogicObject>();  //u sluèaju da konstruktor ne prima listu mora je sam stvorit
	 listaBran=new LinkedList<GameLogicObject>();
	 this.clock=clock;  // broj izvršavanja po sekundi
	 threadWait=1000/clock;
	 this.uiMan=uiMan;

	 this.faIgr=faIgr;
	 listaDodaObj= new LinkedList<GameLogicObject>();
	 listaMakniObj= new LinkedList<GameLogicObject>();
	 ar=new GameLogicObject[VelListeSud];
 }
 ///////public metode///////////////////////////////////////////////////////////////////////////
 public PutL getPut(int brPuta){
	 if(brPuta>=listaCesta.length||brPuta<0) return null;
	 else return (PutL)this.listaCesta[brPuta];
 }
 public void setTrenutacnaCestaNaKojojSi(int cestaNaKojojSam){
	 if(cestaNaKojojSam>this.trenutacnoNajdaljaCesta){
		 trenutacnoNajdaljaCesta=cestaNaKojojSam;
	 }
 }
 public int getTrenutacnaNajdaljaCesta(){
	 return this.trenutacnoNajdaljaCesta;
 }
 public int getProsjekVremenaPetlje(){
	 return prosjekVremena;
 }
 public boolean daliDaGasimIvracamNazad(){
	 return gasiIVratiNazad;
 }
 public void resetirajIgru(){
	 resetirajIgru=true;
 }
 public int getBrProtivnika(){
	return this.listaProt.size();
}
 public void skociNaSljedeci(){
	 faIgr.setSkociNaSljedeci();
 }
 
 public void dodajNovacPlusMinus(float nov){// namješta d
	 ukupniNovac+=nov;
 }
 public float getUkupniNovac(){
	 return ukupniNovac;// vraæa mu trenutnu kolièinu novca 
 }
 public void dodajCestuNaknadno(GameLogicObject cesta){
	 GameLogicObject[] temp=new  GameLogicObject[this.listaCesta.length+1];
	for(int i =0; i<listaCesta.length;i++){
		temp[i]=listaCesta[i];
	    }
	temp[listaCesta.length]=cesta;
	listaCesta=temp;
 }
 public void dodajObjekt(GameLogicObject klijent){
	 if(!runLista){
		         if(klijent.getIndikator()<0) listaProt.add(klijent);
		         else listaBran.add(klijent);
	 }
	 else listaDodaObj.add(klijent);
 }
 /*public GameLogicObject[] provjeriKoliziju(GameLogicObject klijent){
	 int index=0,i=0,indexProt=-1,indexBran=-1,indexObjZaKol=0;;
	 this.klijent=klijent; //sprema ga za metodu sudaraLiSe()
	 indexObjZaKol=klijent.getOblZaKol();

	 ///trebaproæ kroz  hasmap izracunat koji mu je najblizi i pozvat metodu za sudeare
	 /// zbog toga što može biti razlièita tiba sudara napravit æu posebnu metodu zato
	 if(indexObjZaKol!=3){
		 if(klijent.getIndikator()<0) indexProt=listaProt.indexOf(klijent); //dobavlja index klijenta
		 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
		    if(indexProt==index) index++; //da neprovjerava sam sa sobom jel se sudara
	    	if(index>listaProt.size()-1) break; //postavio na vrh u slucaju da jklijent zadnji u listi gornje dodavanje na index bi onda javilo gresku jer ne postoji taj index
		    temp=listaProt.get(index);
		    tempRect=temp.getRect();
		    if(klijent.getRect().left<=(  tempRect.left+ tempRect.width())&& 
				klijent.getRect().left+klijent.getRect().width()>= tempRect.left
				&& klijent.getRect().top<=(  tempRect.top+ tempRect.height())&& 
				klijent.getRect().top+klijent.getRect().height()>= tempRect.top)
		        {
			     if(sudaraLiSe(temp)){ 
			    	                             ar[i]=temp;
			                                     i++;
			                                     }
			     if(i>=VelListeSud) break;
		        }
		
		        index++;  //inkriminira osnovnu listu objekata
	            }
		 index=0;
		if(indexObjZaKol!=4&&indexObjZaKol!=5){// toranj npr. je ima taj index 4 znaèi zanemarije branitelje, branitelj ima 5 znaèi da je kvadrat i zanemaruje druge branotelje
			 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
			 if(klijent.getIndikator()>0) indexBran=listaProt.indexOf(klijent); //dobavlja index klijenta
		    	if(index>listaBran.size()-1) break; //postavio na vrh u slucaju da jklijent zadnji u listi gornje dodavanje na index bi onda javilo gresku jer ne postoji taj index
			    temp=listaBran.get(index);
			    tempRect=temp.getRect();
			    if(klijent.getRect().left<=(  tempRect.left+ tempRect.width())&& 
					klijent.getRect().left+klijent.getRect().width()>= tempRect.left
					&& klijent.getRect().top<=(  tempRect.top+ tempRect.height())&& 
					klijent.getRect().top+klijent.getRect().height()>= tempRect.top)
			        {
				     if(sudaraLiSe(temp)){ 
				    	                             ar[i]=temp;
				                                     i++;
				                                     }
				     if(i>=VelListeSud) break;
			        }
			
			        index++;  //inkriminira osnovnu listu objekata
		            }
		      
		    }
		 
	    }
	 index=0; 
	 int l=listaCesta.length-1;
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
			  if(klijent==listaCesta[index]){
		    	index++; //da neprovjerava sam sa sobom jel se sudara
		    	if(index>l) break;
		    } 
		    temp=listaCesta[index];
		    tempRect=temp.getRect();
		    if(klijent.getRect().left<=(  tempRect.left+ tempRect.width())&& 
				klijent.getRect().left+klijent.getRect().width()>= tempRect.left
				&& klijent.getRect().top<=(  tempRect.top+ tempRect.height())&& 
				klijent.getRect().top+klijent.getRect().height()>= tempRect.top)
		        {
			     if(sudaraLiSe(temp)){           if(i>=VelListeSud) break;
			    	                             ar[i]=temp;
			                                     i++;
			    
		        }
		        }
		        index++;  //inkriminira osnovnu listu objekata
		    	if(index>l) break;
	            
	     }
	        if(i<VelListeSud-1) Arrays.fill(ar, i,VelListeSud-1 , null);
	 
	 return ar; //vraca objekt s kojim se sudara
 }*/
 /////////kolizija bez gamelogic objekta////
 public GameLogicObject[] provjeriKoliziju(int indexObjZaKol, RectF rectKlijenta){
	 int index=0,indexProt=-1,indexBran=-1;

	 iListeSudara=0;
	 for(int i=0; ar.length>i;i++){
		 ar[i]=null;
	 }
	// Arrays.fill(ar, 0,VelListeSud-1 , null);
	 ///trebaproæ kroz  hasmap izracunat koji mu je najblizi i pozvat metodu za sudeare
	 /// zbog toga što može biti razlièita tiba sudara napravit æu posebnu metodu zato
     if( indexObjZaKol==3||indexObjZaKol==2||indexObjZaKol==1){// protivnke(kvadrat) samo zanima sudar sa cestom i itrazenje jeli to�?ka na cesti(krug) u objekt igre klasi
    	 sudarSaCestom(rectKlijenta,indexObjZaKol);
     }
     if( indexObjZaKol==5){// branitelj zanima sudar sa cestom i protivnicima
    	 sudarSaCestom(rectKlijenta,indexObjZaKol);
    	 sudarSaProtivnicima( rectKlijenta,indexObjZaKol,false);
     }
     if( indexObjZaKol==4){// toranj(krug) protivnicima
    	 
    	 sudarSaProtivnicima( rectKlijenta,indexObjZaKol,false);
     }
   if( indexObjZaKol==6){// toranj(krug) braniteljima
    	 
    	 sudarSaBraniteljima( rectKlijenta,indexObjZaKol);
     }
   if( indexObjZaKol==7){// protivnik toranj(krug) 
  	 
  	 sudarSaBraniteljima( rectKlijenta,indexObjZaKol);
   }
	// if(iListeSudara<VelListeSud-1) Arrays.fill(ar, iListeSudara,VelListeSud-1 , null);
	 GameLogicObject[] lista ;
	 lista=ar.clone();
	 return lista; //vraca objekt s kojim se sudara
 }
 private void sudarSaProtivnicima(RectF rectKlijenta, int oblZaKolKlijent, boolean racunajLeteci){
	 int index=0;
	
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
	 
    	if(index>listaProt.size()-1) break; //postavio na vrh u slucaju da jklijent zadnji u listi gornje dodavanje na index bi onda javilo gresku jer ne postoji taj index
	    temp=listaProt.get(index);
	  
	    	GameLogicProtivnik tempProt=(GameLogicProtivnik)temp;
	    	if(tempProt.jesamLileteci()&&racunajLeteci){
	    		tempRect= tempProt.getRecLeteceg();
	    	}
	    	else{
	    		tempRect= tempProt.getRect();
	    	}
	    
	   
	    if(rectKlijenta.left<=(  tempRect.left+ tempRect.width())&& 
	    		rectKlijenta.left+rectKlijenta.width()>= tempRect.left
			&& rectKlijenta.top<=(  tempRect.top+ tempRect.height())&& 
					rectKlijenta.top+rectKlijenta.height()>= tempRect.top)
	        {
		     if(sudaraLiSe(temp.getOblZaKol(),oblZaKolKlijent,tempRect,rectKlijenta)){ 
		    	                             ar[iListeSudara]=temp;
		    	                             iListeSudara++;
		                                     }
		     if(iListeSudara>=VelListeSud) break;
	        }
	
	        index++;  //inkriminira osnovnu listu objekata
            }
	
	 
 } 
 private void sudarSaBraniteljima(RectF rectKlijenta, int oblZaKolKlijent){
	 int index=0;
	
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
	 
    	if(index>listaBran.size()-1) break; //postavio na vrh u slucaju da jklijent zadnji u listi gornje dodavanje na index bi onda javilo gresku jer ne postoji taj index
	    temp=listaBran.get(index);
	    tempRect=temp.getRect();
	    if(rectKlijenta.left<=(  tempRect.left+ tempRect.width())&& 
	    		rectKlijenta.left+rectKlijenta.width()>= tempRect.left
			&& rectKlijenta.top<=(  tempRect.top+ tempRect.height())&& 
					rectKlijenta.top+rectKlijenta.height()>= tempRect.top)
	        {
		     if(sudaraLiSe(temp.getOblZaKol(),oblZaKolKlijent,tempRect,rectKlijenta)){ 
		    	                             ar[iListeSudara]=temp;
		    	                             iListeSudara++;
		                                     }
		     if(iListeSudara>=VelListeSud) break;
	        }
	
	        index++;  //inkriminira osnovnu listu objekata
            }
	
	 
 } 
  private void sudarSaCestom(RectF rectKlijenta, int oblZaKolKlijent){
	 int index=0;
	
	 int l=listaCesta.length-1;
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
		 if(l<0) break;
		    temp=listaCesta[index];
		    tempRect=temp.getRect();
		    if(rectKlijenta.left<=(  tempRect.left+ tempRect.width())&& 
		    		rectKlijenta.left+rectKlijenta.width()>= tempRect.left
				&& rectKlijenta.top<=(  tempRect.top+ tempRect.height())&& 
						rectKlijenta.top+rectKlijenta.height()>= tempRect.top)
		        {
			     if(sudaraLiSe(temp.getOblZaKol(),oblZaKolKlijent,tempRect,rectKlijenta)){           if(iListeSudara>=VelListeSud) break;
			    	                             ar[iListeSudara]=temp;
			    	                             iListeSudara++;
			    
		        }
		        }
		        index++;  //inkriminira osnovnu listu objekata
		    	if(index>l) break;
	            
	     }
	
	 
 }
 ///////////////////////////////////////////
 public GameLogicObject[] provjeriKoliziju(GameLogicObject klijent){
	 int index=0,indexProt=-1,indexBran=-1,indexObjZaKol=0;;
	 this.klijent=klijent; //sprema ga za metodu sudaraLiSe()
	 indexObjZaKol=klijent.getOblZaKol();
	 iListeSudara=0;
	 boolean provjeravajZaLeteceg=false;
	 if(klijent.getIndikator()>100&&klijent.getIndikator()<200){
		 provjeravajZaLeteceg=true;
	 }
	 ///trebaproæ kroz  hasmap izracunat koji mu je najblizi i pozvat metodu za sudeare
	 /// zbog toga što može biti razlièita tiba sudara napravit æu posebnu metodu zato
     if( indexObjZaKol==3||indexObjZaKol==2||indexObjZaKol==1){// protivnke(kvadrat) samo zanima sudar sa cestom i itrazenje jeli to�?ka na cesti(krug) u objekt igre klasi
    	 sudarSaCestom(klijent.getRect(),indexObjZaKol);
     }
     if( indexObjZaKol==5){// branitelj zanima sudar sa cestom i protivnicima
    	 sudarSaCestom(klijent.getRect(),indexObjZaKol);
    	 sudarSaProtivnicima(klijent.getRect(),indexObjZaKol,provjeravajZaLeteceg);
     }
     if( indexObjZaKol==4){// toranj(krug) protivnicima
    	 
    	 sudarSaProtivnicima(klijent.getRect(),indexObjZaKol,provjeravajZaLeteceg);
     }
     if( indexObjZaKol==6){// toranj(krug) braniteljima
    	 
    	 sudarSaBraniteljima(klijent.getRect(),indexObjZaKol);
     }
     if( indexObjZaKol==7){// protivnik toranj(krug) 
      	 
      	 sudarSaBraniteljima( klijent.getRect(),indexObjZaKol);
       }
	 if(iListeSudara<VelListeSud-1) Arrays.fill(ar, iListeSudara,VelListeSud , null);
	 
	 return ar; //vraca objekt s kojim se sudara
 }
 private void sudarSaProtivnicima(){
	 int index=0,indexProt=-1,indexBran=-1,indexObjZaKol=0;
	 if(klijent.getIndikator()<0) indexProt=listaProt.indexOf(klijent); //dobavlja index klijenta
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
	    if(indexProt==index) index++; //da neprovjerava sam sa sobom jel se sudara
    	if(index>listaProt.size()-1) break; //postavio na vrh u slucaju da jklijent zadnji u listi gornje dodavanje na index bi onda javilo gresku jer ne postoji taj index
	    temp=listaProt.get(index);
	    tempRect=temp.getRect();
	    if(klijent.getRect().left<=(  tempRect.left+ tempRect.width())&& 
			klijent.getRect().left+klijent.getRect().width()>= tempRect.left
			&& klijent.getRect().top<=(  tempRect.top+ tempRect.height())&& 
			klijent.getRect().top+klijent.getRect().height()>= tempRect.top)
	        {
		     if(sudaraLiSe(temp.getOblZaKol(),klijent.getOblZaKol(),tempRect,klijent.getRect())){ 
		    	                             ar[iListeSudara]=temp;
		    	                             iListeSudara++;
		                                     }
		     if(iListeSudara>=VelListeSud) break;
	        }
	
	        index++;  //inkriminira osnovnu listu objekata
            }
	
	 
 } 
 private void sudarSaBraniteljima(){
	 int index=0,indexProt=-1,indexBran=-1,indexObjZaKol=0;
	 if(klijent.getIndikator()<0) indexProt=listaProt.indexOf(klijent); //dobavlja index klijenta
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
		 if(klijent.getIndikator()>0) indexBran=listaProt.indexOf(klijent); //dobavlja index klijenta
	    	if(index>listaBran.size()-1) break; //postavio na vrh u slucaju da jklijent zadnji u listi gornje dodavanje na index bi onda javilo gresku jer ne postoji taj index
		    temp=listaBran.get(index);
		    tempRect=temp.getRect();
		    if(klijent.getRect().left<=(  tempRect.left+ tempRect.width())&& 
				klijent.getRect().left+klijent.getRect().width()>= tempRect.left
				&& klijent.getRect().top<=(  tempRect.top+ tempRect.height())&& 
				klijent.getRect().top+klijent.getRect().height()>= tempRect.top)
		        {
			     if(sudaraLiSe(temp.getOblZaKol(),klijent.getOblZaKol(),tempRect,klijent.getRect())){ 
			    	                             ar[iListeSudara]=temp;
			    	                             iListeSudara++;
			                                     }
			     if(iListeSudara>=VelListeSud) break;
		        }
		
		        index++;  //inkriminira osnovnu listu objekata
	            }
	
	 
 }
 private void sudarSaCestom(){
	 int index=0,indexProt=-1,indexBran=-1,indexObjZaKol=0;
	 if(klijent.getIndikator()<0) indexProt=listaProt.indexOf(klijent); //dobavlja index klijenta
	 int l=listaCesta.length-1;
	 while(runBit){ // ovdje sam ubacio runbit jer mi bez ovoga nije htijelo ugasit thred pravilno
			  if(klijent==listaCesta[index]){
		    	index++; //da neprovjerava sam sa sobom jel se sudara
		    	if(index>l) break;
		    } 
		    temp=listaCesta[index];
		    tempRect=temp.getRect();
		    if(klijent.getRect().left<=(  tempRect.left+ tempRect.width())&& 
				klijent.getRect().left+klijent.getRect().width()>= tempRect.left
				&& klijent.getRect().top<=(  tempRect.top+ tempRect.height())&& 
				klijent.getRect().top+klijent.getRect().height()>= tempRect.top)
		        {
			     if(sudaraLiSe(temp.getOblZaKol(),klijent.getOblZaKol(),tempRect,klijent.getRect())){           if(iListeSudara>=VelListeSud) break;
			    	                             ar[iListeSudara]=temp;
			    	                             iListeSudara++;
			    
		        }
		        }
		        index++;  //inkriminira osnovnu listu objekata
		    	if(index>l) break;
	            
	     }
	
	 
 }
 ////////////w

 public void ponovnoPunjenjePodatcimaGameLogic(){
	 Pauza=false;
	 izracunajPauzu=false;
	 vrijemePauze=0;
	 provjeraKrajaGotova=false;
	 listaProt=new LinkedList<GameLogicObject>();  
	 listaBran=new LinkedList<GameLogicObject>();
	 this.listaCesta=null;
	 timeMs=0;
	 timeS=0;
	 timeM=0;
	 timeH=0;
	 threadWait=1000/clock;
	 stvariKojeSeIzvrsavajuSamoJedanputNaPocetku();
	 Pauza=false;
	 izracunajPauzu=false;
	 vrijemePauze=0;
	 bodovi=0;
	 taskbar. ponovnoPostaviZastavice();
	 pokreniValove=false;
	 posaljiNoveVrijednostiTaskbaru();
	
 }
 public void mrtavSam(GameLogicObject temp){
	 if(!runLista){
		 if(temp.getIndikator()<0) listaProt.remove(temp); //garbige collector bi se trebao pobrinut za memoriju od ovog
	     else listaBran.remove(temp);
	 }
	 else this.listaMakniObj.add(temp);
	
 }           
 ///////////////////RUN////////////////////////////////////////////////////////////////////
@Override
public void run() {
    long timeMili;
    
	timeMili=threadWait;
	long dinThreadWait=threadWait;



	timeMili=System.currentTimeMillis();
	while(runBit){
		timeMili=System.currentTimeMillis();
		if(prviCiklus){
			loadMetoda();// ovo se pokreæe samo na poèetku ako treba nešto obavit samo jedanput u igri
			slikeLodirane=true;
			stvariKojeSeIzvrsavajuSamoJedanputNaPocetku();
			prviCiklus=false;
		}
		if(srediVarijableZaSystemPauze&&slikeLodirane){
			this.srediVarijableZaSystemPauze();
			srediVarijableZaSystemPauze=false;
		}
		 if(dealWithSystemPauze){
			 this.systemPauzeLogic();
		 }
	///////////dinamièko proraèinaanje TpS za objekte
/*	if(System.currentTimeMillis()>this.dinProsloVrijeme){
		clock=dinTicUSec;
		dinProsloVrijeme=System.currentTimeMillis();
		dinTicUSec=0;
	}*/
    //////////////////////
	///////////dinamièka brzina	
  
    boolean zahtjevZaPauzeRadni=this.zahtjevZaPause;
   // dinTicUSec++; // služi za dinamièko proraèunavanje TpS za objekte
    //////////////////////////
    if(!Pauza&&izracunajPauzu){
		vrijemePauze=System.currentTimeMillis()-vrijemePauze;
		izracunajPauzu=false;
      }
    
  
 
    if(!Pauza){
    	 trenutacnoNajdaljaCesta=0;
	time();
    if( !slikeUnlodirane&&pokreniValove)faIgr.tokIgre(timeS, timeM, uiMan,this);
	indexRun=0;
	for(int i=0;i< listaProt.size();i++){   //pokreæe sve svoje objekte
		                             // ako je lista prazna ne pokreæe se
		 //if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
		runLista=true;// oznaèava da je lista krenula sa izvršavanjem  novi objekti se mogu dodati tek na kraju 	 
		listaProt.get(indexRun).maliRun(this,faIgr,clock,zahtjevZaPauzeRadni);
	
		indexRun++;
		
	    }
	 indexRun=0;
	 for(int i=0;i< listaBran.size();i++){
		// if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije//pokreæe sve svoje objekte
         // ako je lista prazna ne pokreæe se
         runLista=true;// oznaèava da je lista krenula sa izvršavanjem  novi objekti se mogu dodati tek na kraju 	 
         listaBran.get(indexRun).maliRun(this,faIgr,clock,zahtjevZaPauzeRadni);
         indexRun++;
       
          }
	runLista=false;// dopušta ubacivanje elemenata u glavnu listu 
	izbaciIzListeElementeNaCekanju();
	ubaciUListuElementeNaCekanju();// ubacuje zahtjeve koji su se nakupili dok se petlje izvodila
	posaljiNoveVrijednostiTaskbaru();
	
		
		
		//if(!this.zahtjevZaPause) {
			
	//	}
	 this.uiMan.setNeCrtajZaPauze(false);
	}
    if(!dealWithSystemPauze){
      this.uiMan.postaviZahtjevZaPause(Pauza);
      this.uiMan.runBestredni(fps,this);
    }
    
   
    if(!izracunajPauzu)  vrijemePauze=0;// briše tako da se u sljedećem ciklusu ponovno ne ura�?unava
  
 if(zahtjevZaPauzeRadni){
	Pauza=true;
	if(!izracunajPauzu)vrijemePauze=System.currentTimeMillis();
	izracunajPauzu=true;
	
 }
 else{
		Pauza=false;
	 }
 /////////////////////
 razlikaVremena=System.currentTimeMillis()-timeMili;// uzima razliku od poèetka petlje tj. vrijeme izvoðenja petlje
 racunajProsjek();
 dinThreadWait=threadWait- razlikaVremena;
 fps=1000/(timeMili+1);
 if(dinThreadWait<=10) dinThreadWait=0;// u sluèaju da izraèuna prekratko vrijeme
 //else	try{Thread.sleep(dinThreadWait);}catch(InterruptedException e){e.printStackTrace();}
 //////////////////////////////////
 if(resetirajIgru){
	 
	 this.faIgr.ponovnoPokretanjeIsteFazeIzGameLogica();
	
	 resetirajIgru=false;
 }
 if(pocmiProvjeravatiZaKrajIgre)		provjeriJeliKrajIgre();	
 if(dealWithSystemPauze){
	 this.systemPauzeLogic();
 }
 
 }
	
}
///////load metda
public boolean jeliPauza(){
	return this.Pauza;
}
public double getVrijemePauze(){
	if(Pauza) return 0;
	else if(izracunajPauzu) return 0;
	else return this.vrijemePauze;
}
public  void loadMetoda(){}
public void loadSlika(){
	
}

///////setersi/////////////////////////////////////////////////////////////////////////////////////////////ć
public void pokreniValove(){
	this.pokreniValove=true;
} 
private void srediVarijableZaSystemPauze(){
	// this.uiMan.postaviZahtjevZaPause(true);
	// this.uiMan.setSystemPauze(true);
	 MusicManager.stopAndRelease(0);
	 this.faIgr.reciklirajSveSpriteove();
	 uiMan.odobriPauzuDirektno(true);
	 uiMan.reciklirajPozadinu();
	 uiMan.reciklirajTeksturu();
	 dealWithSystemPauze=true;
	 slikeUnlodirane=true;
	 slikeLodirane=false;
	  MusicManager.stanjeIgre(ac,4, 0,this.faIgr.getBrFaze());
	 pause();
}
public void setSystemPauze(boolean systemPauze){
	if(systemPauze!=this.systemPauze){// ako se vise puta isti pozove iako nista neznaci  za ovu funkciju jer see samo upisujee  ali ako bude budućih promjena
		this.systemPauze=systemPauze;
	    if(systemPauze){

		srediVarijableZaSystemPauze=true;
	}
	}
}
public void pocmiProvjeravatiZaKrajIgre(){
	pocmiProvjeravatiZaKrajIgre=true;
}
public void setUbijenProtivnikRijesiBodove( GameLogicProtivnik prot){
	
	   if(prot.getIndikator()<=-1&&prot.getIndikator()>=-100) {
	         ukupniNovac+=prot.getVrijednostProtivnika();
	         dodajBodove(prot.getVrijednostProtivnika());
           }
}
public void setZivotManje(){
	this.faIgr.pustiZvuk(IgricaActivity.zvukZivotManje, 100,160, 0, 0);
   brZivota--;
   pocmiProvjeravatiZaKrajIgre();
}
public void resume(){
	this.zahtjevZaPause=false;
	//pokreniGlavnuPetlju();
	
	//th.notify();
}
public void pause(){
	this.zahtjevZaPause=true;
}
public void ugasiGlavnuPetlju(){
	 runBit=false;

	/* try{
			th.join();
		   
		} catch(InterruptedException e){
			e.printStackTrace();
		   }*/
	        // th=null;	
    }
public void pokreniGlavnuPetlju( ){
	runBit=true;
	th=new Thread(this);
	th.setName(ac.getLocalClassName());
    th.start();
}

public  GameLogicObject[] getListuCesta(){
	return listaCesta.clone();
}
//////PRIVATNE METODE/////////////////////////////////////////////////////////////////////////////////
private void racunajProsjek(){
	if(brojacProsjeka+1> fps ){// ako je presao razinu inta onda se vrca u 1 tako da zadrzava vec izracunati prosjek
		brojacProsjeka=1;
	}
	this.prosjekVremena=(int)((prosjekVremena*this.brojacProsjeka+razlikaVremena)/(1+brojacProsjeka));
	brojacProsjeka++;

		 Log.d("Prosjek", "na N="+brojacProsjeka+", je : " + prosjekVremena+ " razlika Trenutacna je : "+ this.razlikaVremena);
	
	
}
private void systemPauzeLogic(){
       if(!systemPauze&&Pauza&&slikeUnlodirane){
    	  MusicManager.stanjeIgre(ac,3, 0,this.faIgr.getBrFaze());
 	
		 this.loadSlika();
		 slikeLodirane=true;
		    uiMan.postaviPomakCanvasaApsolutno(0, 0);
		 this.uiMan.setNeCrtajZaPauze(true);
		 this.uiMan.obradiSystemPauzeNaSvimObjektima();
		 dealWithSystemPauze=false;
		 slikeUnlodirane=false;
		// this.uiMan.setSystemPauze(false);
			uiMan.daliDaProvjeravamPovlacenje(true);
		 this.resume();
		
		
	 }
	 
}
public void zavrsiFazuIVRatiNaMapu(){
	 //trebam upisati u bazu broj zvjezdica i cinjenicu da je trenutacna faza zavsena ako je poraz ne upisujem nista 
	MusicManager.stanjeIgre(ac, 2, 0, 0);
	  if(pobjedaPoraz){
		  int brZvj=this.faIgr.brZvjezdica;
		  int razlika=0;
		  if(this.brZvjezdica>brZvj){
			  razlika=brZvjezdica-brZvj;
			  brZvj=brZvjezdica;	  
		  }
		  SQLiteDatabase bazaPodataka;
	        bazaPodataka= ac.openOrCreateDatabase(IgricaActivity.glavniDB,ac.MODE_PRIVATE, null);
	        
	        
		  bazaPodataka.execSQL(" INSERT OR REPLACE INTO " +  
				  IgricaActivity.listaKoristenihFaza +"('"+IgricaActivity.IDKoristeneFaze+"','"+IgricaActivity.IDSlota+"','"+IgricaActivity.stanjeFaze+"','"+IgricaActivity.brojZvijezdica+"')"+ // ne ubacujem tezinu na pocetku
     	                     " Values ('"+ this.faIgr.IDKoristeneFazeIzFI +"','"+ this.faIgr.IDSlotaIzFI +"','"+ 2 +"','"+brZvj+"');");
		  
		  
		  HashMap  brUpgradeBodova=new HashMap<String,Integer>();
		  Cursor cur5=bazaPodataka.query(IgricaActivity.listaBodovaUpgradesa, null,null, null, null, null, null);
			
		   cur5.moveToFirst(); 
			while(cur5.isAfterLast()==false){// puni liste sa atributima koje cita iz liste 	  
	     	    String IDSlotaU=cur5.getString(cur5.getColumnIndex(IgricaActivity.IDSlota));	
	     	   
	     	    Integer bodoviUpg=cur5.getInt(cur5.getColumnIndex(IgricaActivity.bodoviUpgradesa));
	     	   brUpgradeBodova.put(IDSlotaU,bodoviUpg);
	     	    

	  	    cur5.moveToNext();// za pomicanje u bazi podataka   
	        
	           }
			Integer ukupnoBodova=0;
		  if(brUpgradeBodova.containsKey(faIgr.IDSlotaIzFI))ukupnoBodova=(Integer) brUpgradeBodova.get(this.faIgr.IDSlotaIzFI);
		  ukupnoBodova+=razlika;
		    bazaPodataka.execSQL("INSERT OR REPLACE INTO " +  /// prva faza postoji
		    		IgricaActivity.listaBodovaUpgradesa +"('"+IgricaActivity.IDSlota+"','"+IgricaActivity.bodoviUpgradesa+"')"+
			                     " Values ('"+ this.faIgr.IDSlotaIzFI +"','"+ukupnoBodova+"');");
		    cur5.close();
		  bazaPodataka.close();
	  }
	 

	    gasiIVratiNazad=true;
	    ac.runOnUiThread(new Runnable() {
/////bug 7.
		    public void run() {
		    	if(ac!=null){
		    		ac.onBackPressed();
		    		 ac=null;
		    	}
		    }
		});
	
			ugasiGlavnuPetlju();
}
private void provjeriJeliKrajIgre(){
	// ako se zavrsi sa cjelokupnim valovima
	if(!provjeraKrajaGotova){
	   if(this.brZivota<0){
		 if(this.taskbar.pokreniZavrsnuAnimacijuIJeliGotovaPoraz()){
			 this.pobjedaPoraz=false;
			 provjeraKrajaGotova=true;
		    
		 }
	     }
	   else if(this.faIgr.getBrojValova()==faIgr.getTrenutniVal()&&this.getBrProtivnika()==0&&!this.faIgr.daliPostojiSljedeciUOvomValu()&&this.faIgr.brNarucenihProtivnika()<=0){
		  if(this.brZivota>=0){
			brZvjezdica=this.faIgr.izracujanBrZvjezdicaNaKraju(brZivota);
			if(this.taskbar.pokreniZavrsnuAnimacijuIJeliGotovaPobjeda(brZvjezdica)){
				 this.pobjedaPoraz=true;
				 provjeraKrajaGotova=true;
				 
		       }
		      }
		 else{ 
			   if(this.taskbar.pokreniZavrsnuAnimacijuIJeliGotovaPoraz()){
					 this.pobjedaPoraz=false;
					 provjeraKrajaGotova=true;
					  
		         }
		   }
	    }
	}
	// ako ponestane zivota tijekom igre
  
} 
private void izbaciIzListeElementeNaCekanju(){
	int i=0;
	while(i<listaMakniObj.size()&&!listaMakniObj.isEmpty()){// dodaje sve objekte koji  èekaju na dodavanje u glavnu listu
		 mrtavSam(listaMakniObj.get(i));
		 i++;
	}
	listaMakniObj.clear();
}
private void pokreniRunCesta(){// pokreæe run cesta da bi svaka skenirala svoju okolinu 
	int l= listaCesta.length;
	for (int i=0;i<l;i++){
		listaCesta[i].maliRun(this,faIgr,clock,zahtjevZaPause);
	}
}
private void izvuciCestu(){
	int i=0;
	int brojCesta=0;
	int brojCeste=0;
	 while(!listaBran.isEmpty()){   //pokreæe sve svoje objekte
         // ako je lista prazna ne pokreæe se
          runLista=true;// oznaèava da je lista krenula sa izvršavanjem  novi objekti se mogu dodati tek na kraju 	 
          temp=listaBran.get(i);
          if(temp.getIndikator()>200&&
                  temp.getIndikator()<301) brojCesta++;
          i++;
          if(i>listaBran.size()-1) break;
           }
	 listaCesta= new  GameLogicObject[brojCesta];
	 i=0;
	 while(!listaBran.isEmpty()){   //pokreæe sve svoje objekte
         // ako je lista prazna ne pokreæe se
          temp=listaBran.get(i);
          if(temp.getIndikator()>200&&
                                  temp.getIndikator()<301) {
        	             listaCesta[ brojCeste]=temp;
        	             brojCeste++;
        	             listaBran.remove(i);
                   }
          else   i++;
          if(i>listaBran.size()-1) break;
           }
	 i++;
}
private void pauzirajPetlju(){
	/*try {
		th.wait();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	this.runBit=false;
}
private void dodajBodove(float vrij){
	bodovi+=vrij;// bodovi æe biti zapravo vrijednost protivnika moæi æe se poslije dodat nekakva funkcija zato je u posebnoj metodi
}

private void stvariKojeSeIzvrsavajuSamoJedanputNaPocetku(){
	time();
	faIgr.tokIgre(0,0, uiMan,this);
	this.dinProsloVrijeme=System.currentTimeMillis();
	time();
	//faIgr.tokIgre(timeS, timeM, eMan, uiMan,this);
	izvuciCestu();
	pokreniRunCesta();
  //  this.faIgr.setTrenutniVal(6);
	taskbar=faIgr.getTaskbar();// sprema referencu za taskbar iz 
	taskbar.setGameLogic(this);
	this.taskbar.postaviNovcePrijeDolaskaNovogVala(faIgr.getNovacPoSekundiPrijeDolaskaNovogVala());
	ukupniNovac=faIgr.getUkupniNovacNaPocetku();
	brZivota=faIgr.getBrZivota();
	uiMan.setTaskbar(taskbar);
	MusicManager.stanjeIgre(ac,3, 0,this.faIgr.getBrFaze());

	
	//Debug.startMethodTracing("dodatno.pob.na.coal.det.3.liste.29.7.14fps.f20fps");
}
private void posaljiNoveVrijednostiTaskbaru(){// na kraju sakog ciklusa updatat æe taskbar sa novim vrijednostima, jer ako bi ih on sam tražio moglobi doæi do greške jer je na drugom threadu
	taskbar.setBrojValova(faIgr.getBrojValova());
	taskbar.setTrenutniVal(faIgr.getTrenutniVal());
	taskbar.setUkupniNovac(Math.round(ukupniNovac));
	//taskbar.setBrojZivota(brZivota);
	taskbar.setBrojZivota(this.brZivota);
	taskbar.setBodevi((int)bodovi);
	taskbar.setSecDoNovogValaIPoziciju(faIgr.secDoNovogVala(),faIgr.xVala(),faIgr.yVala());
   
	
}
private void ubaciUListuElementeNaCekanju(){
	int i=0;
	while(true){// dodaje sve objekte koji  èekaju na dodavanje u glavnu listu
		if(i>=listaDodaObj.size()||listaDodaObj.isEmpty()) break;
		 if(listaDodaObj.get(i).getIndikator()<0) listaProt.add( listaDodaObj.get(i));
		 else listaBran.add( listaDodaObj.get(i));
		i++;
		
	}
	listaDodaObj.clear();

}
private boolean sudarSaKrugom(RectF kvadrat,RectF krug){
	boolean b=false;
	float xCKr, yCKr, xKv,yKv,sirKv,visKv,r;
	xCKr=krug.centerX();// x centra kruga
	yCKr=krug.centerY();// c centrra kruga
	xKv=kvadrat.left;//x pvi kut kvadrata gornji lijevi
	yKv=kvadrat.top;// y od istog
	sirKv=kvadrat.width();// sirina kvadrata
	visKv=kvadrat.height();// visina kvadrata
	r=krug.width()/2;// polumjer kruga
	if(xCKr<xKv&&yCKr<yKv){
		if(r>Math.hypot((double)(xKv-xCKr),(double)(yKv-yCKr))) b=true;
	     }
	else if(xCKr>(xKv+sirKv)&&yCKr<yKv){
		if(r>Math.hypot((double)(xCKr-sirKv-xKv),(double)(yKv-yCKr))) b=true;
	     }
	else if(xCKr>(xKv+sirKv)&&yCKr>yKv+visKv){
		double h=Math.hypot((double)(xCKr-sirKv-xKv),(double)(yCKr-yKv-visKv));
		if(r>h) b=true;
	     }
	else if(xCKr<xKv&&yCKr>yKv+visKv){
		if(r>Math.hypot((double)(xKv-xCKr),(double)(yCKr-yKv-visKv))) b=true;
	    }
	else b=true;// ako ovo gore ništa nije ispunjeno onda znaèi da krug presjeca kvadrat jer rect od tog objekta presjeca inaèe nebi funkcija bila pozvana
	return b;
}
private boolean sudaraLiSe(int tempObjOblik,int klijentOblik,RectF recTemp,RectF recKlijent){
	boolean b=false;
	int iK=0, iT=0;
	iK=klijentOblik;
	iT=tempObjOblik;   
	if((iK==1||iK==3||iK==5)&&(iT==1||iT==3||iT==5)) b=true; //vraæa istinu jer se veæ provjerilo za sudare izmeðu rectova
	else if((iK==1||iK==3||iK==5||iK==7)&&(iT==2||iT==4)) b=sudarSaKrugom(recKlijent,recTemp);// razdvaja za funkciju za provjeravanje sudara kruga i kvadrata
	else if((iK==2||iK==4||iK==6)&&(iT==1||iT==3||iT==5)) b=sudarSaKrugom(recTemp,recKlijent);
	return b;
	//return true; //TEST
}
//////////////
private void time(){
	if(this.pokreniValove){
	timeMs+=razlikaVremena;// dodaje vrijeme èekanja glavne petlje
	if(timeMs>=1000){
		timeS+=1;
		timeMs-=1000;
	}
	if(timeS>=60){
		timeM+=1;
		timeS-=60;
	}
	if(timeM>=60){
		timeH+=1;
		timeM-=60;
	}
	}
}
/////////////////////////////////////////////////////////////////////////////////////////////////////
}
