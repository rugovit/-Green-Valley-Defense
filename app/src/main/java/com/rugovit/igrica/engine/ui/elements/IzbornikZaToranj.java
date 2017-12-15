package com.rugovit.igrica.engine.ui.elements;

import java.util.LinkedList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.logic.elements.ToranjL;
import com.rugovit.igrica.engine.logic.elements.ToranjLKasarna;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;


public class IzbornikZaToranj implements UIManagerObject {
///////////////ZASTAVICE
private boolean imTauchedZaIzbor=false;	
private boolean imTouched=false;// kada je kliknuto na izbornik on se postavlja od uimanagera u true i to mi je znak da primim xy toucha
private boolean pokrenutIzbornik=false;// oznaèava dali je jedan od tornjeva pokrenuo izbornik
private boolean prvPokTemIzb=true;// prvo pokretanje temp izbornika, služit æe najviše za to da neraèunna svaki put velièinu izbornika
//////////////////////////	
//////////////KONSTANTE	

private float polYUKockiciCijenaDnoPosto=20;
private float velXUOdnosuNaKock=30;
private float velYUOdnosuNaKock=30;
private float polXUKockiciCijenaPosto=35;
////Paintovi
private Paint paintProzor;
private Paint paintNekorBot;
private TextPaint paintNovaca;
///////
private float PpCmX;
private float PpCmY;
private GameLogic GL;// napravit æu funkciju za pauzu
private double vrijUvoda;// vrijeme koliko traje uvod
private int indikator;
private SpriteHendler sprHend;
private Canvas can;
private int xProz,yProz;
private float razOdRubaEkranaX=0f;
private float razOdRubaEkranaY=0f;
private float razIzmPoljUCmX=0f;// razmak izmeðu polja gdje æe biti dugmiæi, taj dio bi jedini trebao reagirati na dodir
private float razIzmPoljUCmY=0f;
private float velUCmPoljaX=1.0f;// velicina jednog polja za izbor u cm, poslije se proraèunnava velièina prozura u pikselima u odnosu na gustoæu ekrana
private float velUCmPoljaY=1.0f;
private int sirEkr;// visina i širina ekrana
private int visEkr;
private UIManager uiMan;
////////////////////////
///////////VARIJABLe
private Rect recText;
private float pomCanX=0,pomCanY=0;
private LinkedList<Integer> listaNekoristenih;// sprema listu nekoristenih polja, ona æe biti zamagljena i reagirat æe kao da je kliknuta izvan polja
private int frameUvoda=0;
private float touchedX;// sprema se gdje je izbornik dodirnut
private float touchedY;
private float tempXProz,tempYProz;// xy prozora proraèunava se posebno za svaki toranj pri pozivu od tornja
private float tempKonVelX, tempKonVelY;/////konaèna velièina prozora nakon  što se odigrao uvod
private ToranjL tempToranj;// trenutni korisnik klase
private int tempBrSlikeUSpriteu;// imat æe više slikka u svakom spriteu za ovu klasu, po jedna za svaki tip tornja
private int tempBrStu,tempBrRed;// odreðuje se broj redova i stupaca radi stvaranja pravilne geometrije za dodir 
private double vrijPocetka;
private float razSredDugmX=0;// ako postoje više od dva dugmiæa u redku ili stupcu onda se ostala razmièu za prostor koji je ostao nakn fiksiranja rubnih
private float razSredDugmY=0;
private int[] kodoviCrtanjaBotuna;
private boolean crtajAnimacijuDesnog=false,crtajAnimacijuLijevog=false, crtajAnimacijuSell=false;
private boolean crtajDesno=false,crtajLijevo=false, crtajSell=false, crtajOsovinu=false;
private float velKockice, razRubaPoljaOdRubaKock, sirGlOsovine,visGlOsovine, polOsovineXUSl,polOsovineYUSl;
private RectF tempRec=new RectF();
private RectF recBotunaLijevoDolje=new RectF();
private RectF recBotunaLijevoGore=new RectF();
private RectF recBotunaDesnoDolje=new RectF();
private RectF recBotunaDesnoGore=new RectF();
private RectF recBotunaSell=new RectF();
private RectF recVijkaDesno=new RectF();
private RectF recVijkaLijevo=new RectF();
private RectF recVijkaSell=new RectF();
private RectF recVijkaRez=new RectF();
private RectF recGlavnaOsovina=new RectF();
private GameEvent geTransKockLijevo,geTransKockDesno,geTransKockSell;
private boolean tekPoceoLijevo=true,tekPoceoDesno=true,tekPoceoSell=true;
private Paint paintBljesk=new Paint();

public IzbornikZaToranj(SpriteHendler sprHend,UIManager uiMan, GameLogic GL,int indikator) {
	this.GL=GL;
	this.sprHend=sprHend;
	this.uiMan=uiMan;
	this.indikator=indikator;
	recText=new Rect();
	//////stvaranje crno bijelog providnog painta
	paintNekorBot= new Paint();
	paintNekorBot.setAlpha(255);
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0);
    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
	paintNekorBot.setColorFilter(filter);
	////
	paintProzor=new Paint();
	paintProzor.setAlpha(220);
	////////////
	paintNovaca=new TextPaint();
	paintNovaca.setAntiAlias(true);
	paintNovaca.setTypeface(this.GL.faIgr.getTaskbar().fontBodova());
	paintNovaca.setColor(Color.argb(255, 255,210, 0));
	paintNovaca.setStyle(Style.FILL);
	paintNovaca.setTypeface(Typeface.DEFAULT_BOLD); 
	kodoviCrtanjaBotuna=new int[4];//pozitivni brojevvi ce biti redni broj (od nule) slicice u slici
	                                 
	                                  //negativni ce biti poseben slicice za zajednicko crtanje
	
	//sprit hend 
	//0. slika glavne osovine 
	//1. slika okvira
	//2. slika vijka
	//3. slika mali slicica koje ce se prikazivati u sluucaju da se ne koristi botun, one će se oznacavati negativnim brojem u listi kodaviCrtanjaBotuna
	//4. slika sell
	//ostale ce biti slicice  tornjeva
	
	geTransKockLijevo=new GameEvent(null);
	geTransKockDesno=new GameEvent(null);
	geTransKockSell=new GameEvent(null);
	
	paintBljesk.setColor(Color.argb(255,255,246,0));
	paintBljesk.setStyle(Paint.Style.FILL);
	
}
//////////////NACRTAJ METODA
@Override
public void nacrtaj(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY) {
	this.can=can;
	this.PpCmX=PpCmX;
	 this.PpCmY=PpCmY;
	this.uiMan=uiMan;
	this.pomCanY=pomCanY;
	this.pomCanX=pomCanX;
	 if(prvPokTemIzb) {
		//vel kockice 74% sirine polja
		 velKockice=90*this.velUCmPoljaX*PpCmX/100;
		  /// sir gl osovine 84.5%
		 sirGlOsovine=84.5f*3*this.velUCmPoljaX*PpCmX/100;
		  // vis glavne osovine	 44%	 
		 visGlOsovine=44*4*this.velUCmPoljaX*PpCmX/100;
		 //pol X u sl 7.5%
		 polOsovineXUSl=7.5f*3*this.velUCmPoljaX*PpCmX/100;
		 //pol Y u sl 12.5%
		  polOsovineYUSl=12.5f*4*this.velUCmPoljaX*PpCmX/100;
		 razRubaPoljaOdRubaKock=(velUCmPoljaX*PpCmX-velKockice)/2;//predpostavlkjam da ce biti na sredini
		 this.visEkr=can.getHeight();
		 this.sirEkr=can.getWidth();
		 prvPokTemIzb =false;// 
		
	 }
	if(pokrenutIzbornik)
	   {
		
	     if(vrijPocetka+vrijUvoda>System.currentTimeMillis()){// ako je trenutaèno vrijeme manje od trenutka kada bi trebao završit uvod
		      nacrtajUvod888();
	           }
	     else{
	    	if(imTauchedZaIzbor) {
	    		reagirajNaDodir888();//imTauchedZaIzbor se postavlja kroz  tauchedobject i ako je isti kao ova klasa to znaèi da je kliknuto na prozor i treba se vidjeti jeli izabrano polje
	    		imTauchedZaIzbor=false;
	    	     }
	    	 
	    	 nacrtajProzor888();
	         }
		 
	   }
	 else {// u sluèaju da nitko netraži izbornik njegove geometrijske velièine se postavljaju na vrijenost koja neæe smetati ostatju igrice
		 tempYProz=-100;
		 tempKonVelY=0;
		 tempXProz=-100;
		 tempKonVelX=0;
	  } 
}
//////////////////////////////////////

//////////////////PRIVATNE METODE
private void reagirajNaDodir888(){
	if(imTouched){
		int s=0;// brojaè stupca
		int r=0;// brojjaè retka
		int polje;// polje tj broj dugmiæa
		boolean nasaRed=false;
		boolean nasaStu=false;
		/////////////traženje
		    while(tempBrRed>r ){// inkrimentira kroz retke
			     if(touchedY>tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*r+this.razOdRubaEkranaY*PpCmY&&
					touchedY<tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*(r+1)+this.razOdRubaEkranaY*PpCmY){
					                nasaRed=true;
				                    break;
			               }
		     	r++;
		       }

		      while(tempBrStu>s){// inkrementira kroz stupce
		      if(touchedX>tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*s+this.razOdRubaEkranaX*PpCmX&&
			       touchedX<tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*(s+1)+this.razOdRubaEkranaX*PpCmX){
			                 nasaStu=true;
		                     break;// izlazi iz petlje kada naðe i istovremeno mu ostaje zapamæen broj stupaca
		                 }
		            s++;	
		      }
	         if(nasaRed&&nasaStu){
	        	 /// dodatak za izbornik za toranj
	        	 
		               polje=(tempBrStu*r)+s+1;
		               if(polje==4||polje==6||polje==10||polje==12){
		            	   if(polje==4) polje=1;
		            	   else if(polje==6) polje=2;
		            	   else if(polje==10) polje=3;
		            	   else if(polje==12) polje=4;
		            	   
		                if(listaNekoristenih!=null){// u sluèaju da je korisnik izbornika oznaèio neka polja kao nekoristena provjerava se dali je kliknuto na to polje i reagira se kao da je kliknuto izvan polja
			                 
		            	     if(listaNekoristenih.contains(polje)){// ako je kliknuto polje oznaèeno kao nekorisšetno reagira se kao da je kliknuto izvan polja
		            	    	    if(this.tempToranj instanceof ToranjLKasarna)   {
		     		            	   tempToranj.uhvatiPozicijuKlikaIzvanIzbornika(this.touchedX,this.touchedY);
		     		               }
				                    uiMan.setOznacenSam(this);
				                    
			                     }
			                  else {
				                 pokrenutIzbornik=false;
				                 tempToranj.vratiTouchPolje(polje);//a ako ne, vraæa broj polja poèevši od  jedan
		 	                        }
		                
		                     }
		                  else{
			                 pokrenutIzbornik=false;
			                 tempToranj.vratiTouchPolje(polje);// vraæa broj polja poèevši od  jedan
		                     }
		                 }
		               else if(polje==2){// u slucaju da je stisnuto na brisanje
		            	   if(this.crtajSell){
		            		   prodajTrenutacniToranj();
		            	   }
		            	   else {// u slucajuda se ne crta ostaje ukljucen
		            		   uiMan.setOznacenSam(this);
		            		    if(this.tempToranj instanceof ToranjLKasarna)   {
		 		            	   tempToranj.uhvatiPozicijuKlikaIzvanIzbornika(this.touchedX,this.touchedY);
		 		               }
		            	   }
		               }
		               else {
		            	   if(this.tempToranj instanceof ToranjLKasarna)   {// 
	 		            	   tempToranj.uhvatiPozicijuKlikaIzvanIzbornika(this.touchedX,this.touchedY);
	 		               }
		               }
	               } 
	         else{// zbog toga što ako primi imTouched to znaèi da je definitivno kliknuto na izbornik ali na razmak izmeðu polja ali ui manager automatski prestane slati xy izborniku pa se treba ponovno prijaviti
		               if(this.tempToranj instanceof ToranjLKasarna)   {
		            	   tempToranj.uhvatiPozicijuKlikaIzvanIzbornika(this.touchedX,this.touchedY);
		               }
	        	       uiMan.setOznacenSam(this);
	             }
	  
	     }
}
private void prodajTrenutacniToranj(){
	float xProd,yProd;
	
	xProd=this.tempToranj.getRect().centerX();
	yProd=this.tempToranj.getRect().centerY();
	this.GL.dodajNovacPlusMinus(tempToranj.getNovacZaProdaju()*GL.faIgr.redukcijaKodProdaje);
	this.tempToranj.ubijMe();
	this.GL.faIgr.br200ToranjEmbrio(xProd, yProd);
	this.iskljuciIzbornik();
}
private void nacrtajProzor888(){
	// prvo se crta podloga prozora koja se treba nalaziti u prvoj slici u sprateu, u nultoj je spremljena slika za animaciju uvoda
	/*sprHend.nacrtajSprite(can, 1,0, 0,new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY),paintProzor);
    //can.drawRect(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY,this.paintProzor);
	//onda se na to crtaju dugmiæi, koji su spremljeni u spriteu na slicu rednog broja tempBrSlikeUSpriteu
	int s=0;// brojaè stupca
	int r=0;// brojjaè retka
	int d=1;// brojac dugmiæa
	while(tempBrRed>r ){// inkrementira kroz redtke
		while(tempBrStu>s){// inkrementira kroz stupce
			if(listaNekoristenih!=null)//ako lista postoji onda se svaki9 pojedinaèno provjerava dali je nekorišten
			{	
			  if(!listaNekoristenih.contains(d)){// ako taj dugmiæ nije u listi nekorištenih
				   sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,d-1, 0,
							new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*s+this.razOdRubaEkranaX*PpCmX,tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*r+this.razOdRubaEkranaY*PpCmY,
									tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*(s+1)+this.razOdRubaEkranaX*PpCmX,tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*(r+1)+this.razOdRubaEkranaY*PpCmY));
			      }
			      
			  else{ // u sluèaju da se botunu ne koriste crtaju se sa paintom koji je providan i crno bijeli
				     sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,d-1, 0,
								new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*s+this.razOdRubaEkranaX*PpCmX,tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*r+this.razOdRubaEkranaY*PpCmY,
										tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*(s+1)+this.razOdRubaEkranaX*PpCmX,tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*(r+1)+this.razOdRubaEkranaY*PpCmY),paintNekorBot);
		     	  }
			}
			else{// ako nepostoji lista onda se svi crtaju normalno
				   sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,d-1, 0,
						   new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*s+this.razOdRubaEkranaX*PpCmX,tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*r+this.razOdRubaEkranaY*PpCmY,
									tempXProz+razIzmPoljUCmX*PpCmX*(s)+velUCmPoljaX*PpCmX*(s+1)+this.razOdRubaEkranaX*PpCmX,tempYProz+razIzmPoljUCmY*PpCmY*(r)+velUCmPoljaY*PpCmY*(r+1)+this.razOdRubaEkranaY*PpCmY));
			}
		 s++;
		 d++;
		}
	 s=0;// vraæa stupce u nula	
	 r++;	*/
	 if(this.crtajSell)nacrtajIzvlacenjeSell();
	 if(this.crtajOsovinu) nacrtajGlavnuOsovinu();
	 if(this.crtajLijevo)nacrtajIzvlacenjeLijevo();
	 if(this.crtajDesno)nacrtajIzvlacenjeDesno();
	 
	}
private void nacrtajUvod888(){
	
}
private void nacrtajGlavnuOsovinu(){
	this.recGlavnaOsovina.set(tempXProz+this.polOsovineXUSl, tempYProz+this.polOsovineYUSl, tempXProz+this.polOsovineXUSl+ sirGlOsovine, tempYProz+this.polOsovineYUSl+ visGlOsovine);
    this.sprHend.nacrtajSprite(can, 0, 0,0,recGlavnaOsovina);
}
private void nacrtajIzvlacenjeLijevo(){
	float polXCij= polXUKockiciCijenaPosto*recBotunaDesnoDolje.width()/100;
	float polYCij=polYUKockiciCijenaDnoPosto*recBotunaDesnoDolje.height()/100;
	float velX=velXUOdnosuNaKock*recBotunaDesnoDolje.width()/100;
	float velY=	velYUOdnosuNaKock*recBotunaDesnoDolje.height()/100;
    float zaPomaknuti=2*velUCmPoljaY*PpCmY;
    if(this.tekPoceoLijevo){
    	
    	  recBotunaLijevoDolje.set(tempXProz+razRubaPoljaOdRubaKock,tempYProz+velUCmPoljaY*PpCmY, tempXProz+this.velKockice+razRubaPoljaOdRubaKock,tempYProz+velUCmPoljaY*PpCmY+this.velKockice);
    	  this.tekPoceoLijevo=false;
  		GL.faIgr.pustiZvuk(IgricaActivity.zvukRobotski2,65,110, 100, 0);
    }
	if(crtajAnimacijuLijevog)crtajAnimacijuLijevog=!SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(this.geTransKockLijevo, 0, zaPomaknuti, 0,0.3f, recBotunaLijevoDolje);
	recBotunaLijevoGore.set(tempXProz+razRubaPoljaOdRubaKock,tempYProz+velUCmPoljaY*PpCmY, tempXProz+this.velKockice+razRubaPoljaOdRubaKock,tempYProz+this.velKockice+velUCmPoljaY*PpCmY);
  
    recVijkaLijevo.set(recBotunaLijevoGore.left+recBotunaLijevoGore.width()/6, recBotunaLijevoGore.bottom, recBotunaLijevoGore.right-recBotunaLijevoGore.width()/6,recBotunaLijevoDolje.top);
    recVijkaRez.set(0, (zaPomaknuti-(recBotunaLijevoDolje.top-recBotunaLijevoGore.bottom))/(zaPomaknuti/100), 100, 100);
    //vijak
    this.sprHend.nacrtjDioBitmapa(can,2,  recVijkaRez, recVijkaLijevo, 0,0, 0, null);
    //3.
    this.tempRec.set(recBotunaLijevoDolje.left+recBotunaLijevoDolje.width()/7, recBotunaLijevoDolje.top+recBotunaLijevoDolje.height()/7,
    		recBotunaLijevoDolje.right-recBotunaLijevoDolje.width()/7,recBotunaLijevoDolje. bottom-recBotunaLijevoDolje.height()/7);
    if(listaNekoristenih.contains(3)){
    	if(kodoviCrtanjaBotuna[2]>=0)this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[2],0, tempRec, paintNekorBot);
    	else if(kodoviCrtanjaBotuna[2]<0) {
    		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaLijevoDolje, paintNekorBot);
    	}
    	//can.drawRect(recBotunaLijevoDolje, paintNekorBot);
    	this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaLijevoDolje);//okvir
    }
    else{
    	
	                 paintBljesk.setShader(new RadialGradient(recBotunaLijevoDolje.centerX() ,recBotunaLijevoDolje.centerY() ,
			            (int)(recBotunaLijevoDolje.width()*1), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
	         can.drawCircle(recBotunaLijevoDolje.centerX() ,recBotunaLijevoDolje.centerY() ,
				(int)(recBotunaLijevoDolje.width()*1), paintBljesk);
	         if(kodoviCrtanjaBotuna[2]>=0)this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[2],0, tempRec);
	     	else if(kodoviCrtanjaBotuna[2]<0) {
	    		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaLijevoDolje, paintNekorBot);
	    	}
	         this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaLijevoDolje);//okvir
    }
    if(tempToranj!=null)if(this.tempToranj.getCijenuBotuna(3)>0) nacrtajTextUnutarPravokutnika( recBotunaLijevoDolje.left+polXCij-velX/2,recBotunaLijevoDolje.bottom-velY-polYCij,velX,velY,paintNovaca,Integer.toString((int)this.tempToranj.getCijenuBotuna(3)));
    //1.
    this.tempRec.set(recBotunaLijevoGore.left+recBotunaLijevoGore.width()/7, recBotunaLijevoGore.top+recBotunaLijevoGore.height()/7,
    		recBotunaLijevoGore.right-recBotunaLijevoGore.width()/7,recBotunaLijevoGore. bottom-recBotunaLijevoGore.height()/7);
    if(listaNekoristenih.contains(1)){
    	if(kodoviCrtanjaBotuna[0]>=0)this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[0], 0,tempRec, paintNekorBot);
    	else if(kodoviCrtanjaBotuna[0]<0) {
    		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaLijevoGore, paintNekorBot);
    	}
    	//can.drawRect(recBotunaLijevoGore, paintNekorBot);
    	 this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaLijevoGore);//okvir
    }
    else{
    	    

           	paintBljesk.setShader(new RadialGradient(recBotunaLijevoGore.centerX() ,recBotunaLijevoGore.centerY() ,
   			        (int)(recBotunaLijevoGore.width()*1), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
   	        can.drawCircle(recBotunaLijevoGore.centerX() ,recBotunaLijevoGore.centerY() ,
   				   (int)(recBotunaLijevoGore.width()*1), paintBljesk);
   	     if(kodoviCrtanjaBotuna[0]>=0)this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[0], 0,tempRec);
   		else if(kodoviCrtanjaBotuna[0]<0) {
    		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaLijevoGore, paintNekorBot);
    	}
         this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaLijevoGore);//okvir
       }
    if(tempToranj!=null)  if(this.tempToranj.getCijenuBotuna(1)>0)nacrtajTextUnutarPravokutnika(recBotunaLijevoGore.left+polXCij-velX/2,recBotunaLijevoGore.bottom-velY-polYCij,velX,velY,paintNovaca,Integer.toString((int)this.tempToranj.getCijenuBotuna(1)));
  
}
private void nacrtajIzvlacenjeDesno(){
	float polXCij= polXUKockiciCijenaPosto*recBotunaDesnoDolje.width()/100;
	float polYCij=polYUKockiciCijenaDnoPosto*recBotunaDesnoDolje.height()/100;
	float velX=velXUOdnosuNaKock*recBotunaDesnoDolje.width()/100;
	float velY=	velYUOdnosuNaKock*recBotunaDesnoDolje.height()/100;
    float zaPomaknuti=2*velUCmPoljaY*PpCmY;
    if(this.tekPoceoDesno){
    	GL.faIgr.pustiZvuk(IgricaActivity.zvukRobotski2,65,110, 100, 0);
    	  recBotunaDesnoDolje.set(tempXProz+razRubaPoljaOdRubaKock+2*velUCmPoljaX*PpCmX,tempYProz+velUCmPoljaY*PpCmY, tempXProz+velKockice+2*velUCmPoljaX*PpCmX+razRubaPoljaOdRubaKock,tempYProz+velUCmPoljaY*PpCmY+this.velKockice);
    	this.tekPoceoDesno=false;
    }
    if(crtajAnimacijuDesnog)crtajAnimacijuDesnog=!SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(this.geTransKockDesno, 0, zaPomaknuti, 0, 0.3f, recBotunaDesnoDolje);
	recBotunaDesnoGore.set(tempXProz+razRubaPoljaOdRubaKock+2*velUCmPoljaX*PpCmX,tempYProz+velUCmPoljaY*PpCmY, tempXProz+this.velKockice+razRubaPoljaOdRubaKock+2*velUCmPoljaX*PpCmX,tempYProz+this.velKockice+velUCmPoljaY*PpCmY);
  
    recVijkaDesno.set(recBotunaDesnoGore.left+recBotunaDesnoGore.width()/6, recBotunaDesnoGore.bottom, recBotunaDesnoGore.right-recBotunaDesnoGore.width()/6,recBotunaDesnoDolje.top);
    recVijkaRez.set(0,(zaPomaknuti-(recBotunaDesnoDolje.top-recBotunaDesnoGore.bottom))/(zaPomaknuti/100), 100, 100);
    //vijak
    this.sprHend.nacrtjDioBitmapa(can,2,  recVijkaRez, recVijkaDesno, 0,0, 0, null);
  //4.
    this.tempRec.set(recBotunaDesnoDolje.left+recBotunaDesnoDolje.width()/7, recBotunaDesnoDolje.top+recBotunaDesnoDolje.height()/7, recBotunaDesnoDolje.right-recBotunaDesnoDolje.width()/7,recBotunaDesnoDolje. bottom-recBotunaDesnoDolje.height()/7);
    if(listaNekoristenih.contains(4)){
    	if(kodoviCrtanjaBotuna[3]>=0){
    		
    		this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[3],0,tempRec , paintNekorBot);//trenutacna slicica za srtati na tome mjestu
    	}
    	else if(kodoviCrtanjaBotuna[3]<0) {
    		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaDesnoDolje, paintNekorBot);
    	}
    	// can.drawRect(recBotunaDesnoDolje, paintNekorBot);
        this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaDesnoDolje);//okvir
    }
    else{
    	
    	
         paintBljesk.setShader(new RadialGradient(recBotunaDesnoDolje.centerX() ,recBotunaDesnoDolje.centerY() ,
   			(int)(recBotunaDesnoDolje.width()*1), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
   	    can.drawCircle(recBotunaDesnoDolje.centerX() ,recBotunaDesnoDolje.centerY() ,
   				(int)(recBotunaDesnoDolje.width()*1), paintBljesk);
   	 if(kodoviCrtanjaBotuna[3]>=0)  this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[3],0, tempRec);//trenutacna slicica za srtati na tome mjestu
   	 else if(kodoviCrtanjaBotuna[3]<0) {
		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaDesnoDolje, paintNekorBot);
	}
   	 this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaDesnoDolje);//okvir
    }
    if(tempToranj!=null)if(this.tempToranj.getCijenuBotuna(4)>0)nacrtajTextUnutarPravokutnika(recBotunaDesnoDolje.left+polXCij-velX/2,recBotunaDesnoDolje.bottom-velY-polYCij,velX,velY,paintNovaca,Integer.toString((int)this.tempToranj.getCijenuBotuna(4)));
    //2.
    this.tempRec.set(recBotunaDesnoGore.left+recBotunaDesnoGore.width()/7, recBotunaDesnoGore.top+recBotunaDesnoGore.height()/7, recBotunaDesnoGore.right-recBotunaDesnoGore.width()/7,recBotunaDesnoGore. bottom-recBotunaDesnoGore.height()/7);
    if(listaNekoristenih.contains(2)){
    	if(kodoviCrtanjaBotuna[1]>=0) this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[1],0, tempRec, paintNekorBot);//trenutacna slicica za srtati na tome mjestu
        else if(kodoviCrtanjaBotuna[1]<0) {
    		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaDesnoGore, paintNekorBot);
    	}
        //can.drawRect(recBotunaDesnoGore, paintNekorBot);
        this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaDesnoGore);//okvir
       }
    else{
    
     paintBljesk.setShader(new RadialGradient(recBotunaDesnoGore.centerX() ,recBotunaDesnoGore.centerY() ,
   			(int)(recBotunaDesnoGore.width()*1), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
   	      can.drawCircle(recBotunaDesnoGore.centerX() ,recBotunaDesnoGore.centerY() ,
   				(int)(recBotunaDesnoGore.width()*1), paintBljesk);
   	   if(kodoviCrtanjaBotuna[1]>=0)this.sprHend.nacrtajSprite(can, this.tempBrSlikeUSpriteu, this.kodoviCrtanjaBotuna[1],0, tempRec);//trenutacna slicica za srtati na tome mjestu
   	   else if(kodoviCrtanjaBotuna[1]<0) {
 		this.sprHend.nacrtajSprite(can,3, 0,0, recBotunaDesnoGore, paintNekorBot);
 	   }
        this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaDesnoGore);//okvir
        }
    if(tempToranj!=null)if(this.tempToranj.getCijenuBotuna(2)>0) nacrtajTextUnutarPravokutnika(recBotunaDesnoGore.left+polXCij-velX/2,recBotunaDesnoGore.bottom-velY-polYCij,velX,velY,paintNovaca,Integer.toString((int)this.tempToranj.getCijenuBotuna(2)));
 
}
private void nacrtajIzvlacenjeSell(){
	 float zaPomaknuti=-velUCmPoljaY*PpCmY;
	    if(this.tekPoceoSell){
	    	  recBotunaSell.set(tempXProz+razRubaPoljaOdRubaKock+velUCmPoljaX*PpCmX,tempYProz+velUCmPoljaY*PpCmY, tempXProz+this.velKockice+razRubaPoljaOdRubaKock+velUCmPoljaX*PpCmX,tempYProz+velUCmPoljaY*PpCmY+this.velKockice);
	    	  this.tekPoceoSell=false;
	    	  GL.faIgr.pustiZvuk(IgricaActivity.zvukRobotski1,60,110, 100, 0);
	    }
		if(crtajAnimacijuSell)crtajAnimacijuSell=!SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(this.geTransKockSell, 0, zaPomaknuti, 0, 1,  recBotunaSell);
	    
		 recVijkaSell.set( recBotunaSell.left+recBotunaSell.width()/6,recBotunaSell.bottom ,  recBotunaSell.right-recBotunaSell.width()/6,this.recGlavnaOsovina.top+50*this.recGlavnaOsovina.height()/100);
	    //recVijkaSell.set( recBotunaSell.left+recBotunaSell.width()/6,recBotunaSell.bottom ,  recBotunaSell.right-recBotunaSell.width()/6,  tempYProz+velUCmPoljaY*PpCmY+velUCmPoljaY*PpCmY/4);
	   //recVijkaRez.set(0, 0, 100, 100);
		 float postotak=-zaPomaknuti/100;
		 postotak=((this.recGlavnaOsovina.top+50*this.recGlavnaOsovina.height()/100)-recBotunaSell.bottom)/postotak;
		 recVijkaRez.set(0, postotak, 100, 100);
	   // recVijkaRez.set(0, (-zaPomaknuti-(tempYProz+velUCmPoljaY*PpCmY+velUCmPoljaY*PpCmY/4)-recBotunaSell.bottom)/(-2*zaPomaknuti/100), 100, 100);
	    //vijak
	    this.sprHend.nacrtjDioBitmapa(can,2,  recVijkaRez, recVijkaSell, 0,0, 0, null);
	    this.sprHend.nacrtajSprite(can,4, 0,0, recBotunaSell);
        //can.drawRect(recBotunaLijevoDolje, paintNekorBot);
        this.sprHend.nacrtajSprite(can, 1, 0,0, recBotunaSell);//
 
}
//////////////neke cesto koristene privatne
private void iskljuciIzbornik(){
	pokrenutIzbornik=false;
	if( tempToranj!=null)  tempToranj.izbornikNaMeni(false);
	tempToranj=null;
	//resetiranje zastavica crtanja
	crtajAnimacijuDesnog=false;
    crtajAnimacijuLijevog=false;
    crtajAnimacijuSell=false;
    
    crtajDesno=false;
    crtajLijevo=false;
    crtajSell=false;
    crtajOsovinu=false;
    
    geTransKockLijevo.jesamLiZiv=false;
	  geTransKockDesno.jesamLiZiv=false;
    geTransKockSell.jesamLiZiv=false;

    tekPoceoLijevo=false;
    tekPoceoDesno=false;
    tekPoceoSell=false;
	
}
private void nacrtajTextUnutarPravokutnika(float pozX,float pozY,float sir,float vis, TextPaint tp, String text){
	int brClanova=text.length();
	////
	
	tp.getTextBounds(text,0,brClanova ,recText);
	tp.setTextSize(150);// namještam size na malo veæu vrijednost radi int zaokruživanja velièine
	tp.getTextBounds(text,0,brClanova ,recText);
	float visTexta=recText.height();
	float size=tp.getTextSize();
	tp.setTextSize(vis*size/visTexta);
	tp.getTextBounds(text,0,brClanova ,recText);
	float size2=tp.getTextSize();
	float sirTexta=recText.width();
	if(sirTexta>sir) tp.setTextSize(sir*size2/sirTexta);
	can.drawText(text, pozX, pozY+vis, tp);
}
private void izracVelProzora(){//proraèunnava vel prozora u odnosu na  gustoæu ekrana i njegovu velièinu i broj polja od  dotiènog tornja
		 tempKonVelX=velUCmPoljaX* tempBrStu*PpCmX+razIzmPoljUCmX*PpCmX*(tempBrStu-1)+2*this.razOdRubaEkranaX*PpCmX;// dodaje i razmake izmeðu polja
		 tempKonVelY= tempBrRed*velUCmPoljaY*PpCmY+razIzmPoljUCmY*PpCmY*(tempBrRed-1)+2*this.razOdRubaEkranaY*PpCmY;
		 tempXProz=tempToranj.getRect().centerX()- tempKonVelX/2;// 
		 tempYProz=tempToranj.getRect().centerY()- tempKonVelY/2;
		 //////
		 /* ovaj je radio korekcijuu odnosu na vidljivi ekran više mi netreba*/
		
		 /////namjestanje ako prijeðe granice ekrana
		 if(tempXProz<0-pomCanX){// ako je  preblizu ruba ekrana pomièe se u nulu
			 tempXProz=0-pomCanX;
		 }
		 
		  
		 
		 
		 else if(tempXProz+tempKonVelX>sirEkr-pomCanX){// ako je desni kut prozora otišao izvan desne strane ekrana vreæea se prema lijevo koliko treba
			 tempXProz-=tempXProz+tempKonVelX-sirEkr+pomCanX;// pomièe se ulijevo za razliku koliko je prešao
		 }
		 
		 
		 if(tempYProz<0-pomCanY+this.GL.faIgr.getTaskbar().getVisBotunaDodatnihFunkcija()){// ako je  preblizu ruba ekrana pomièe se u nulu
			 tempYProz=0-pomCanY+this.GL.faIgr.getTaskbar().getVisBotunaDodatnihFunkcija();
		 }
		 
		 else if(tempYProz+tempKonVelY>visEkr-pomCanY-this.GL.faIgr.getTaskbar().getVisBotunaMjeracaPojacanja()){// 
			 tempYProz-=tempYProz+tempKonVelY-visEkr+pomCanY+this.GL.faIgr.getTaskbar().getVisBotunaMjeracaPojacanja();// 
		 }
		 
		 //////
		
	 
}
///////////////////////////////////////////////////////////////
////////////////////////PUBLIC METODE
 
public boolean izbornikNaMeni(ToranjL t){
	if(t==this.tempToranj) return true;
	else return false;
}
public void izmjenilistuNekoristenih(LinkedList<Integer> listaNekoristenih){// u sluèju da se tijekom korištenja izbornik promijeni stanje, da se ta promjena prikže
	this.listaNekoristenih=listaNekoristenih;
}
public void pokreniMojIzbornik(ToranjL obj,LinkedList<Integer> listaNekoristenih){
	tempBrStu=3;
    tempBrRed=4;
	this.listaNekoristenih=listaNekoristenih;
	vrijPocetka=System.currentTimeMillis();// bilježi vrijeme kada je zatražen izbornik
	tempToranj=obj;// sprema koji je toranj zatražio izbornik
	frameUvoda=0;// postavlja animaciju uvoda na pocetak
	pokrenutIzbornik=true;
	izracVelProzora();
     
	uiMan.setOznacenSam(this);// postavlja seba kao oznaèenog u ui manageru tako da dobije podatke o xy 
    /////////////case-switch izmeðu raznik tipa tornjeva, provjeravat æe indikator i puniti varijable koje æe koristiti ostatak klase
	switch(tempToranj.getIndikator()){
	   case 175: 
		       tempBrSlikeUSpriteu=15;//stavlja sliku sljedeæe razine izbornika
              crtajAnimacijuDesnog=false;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;

              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;

              kodoviCrtanjaBotuna[0]=-1;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=0;
              kodoviCrtanjaBotuna[3]=-1;

              geTransKockLijevo.jesamLiZiv=true;
              geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
      break; 
	   case 176: 
	       tempBrSlikeUSpriteu=16;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=false;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=-1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
  break;    
	   case 177: 
	       tempBrSlikeUSpriteu=17;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=true;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
  break;    
	  case 125: tempBrSlikeUSpriteu=12;//stavlja sliku sljedeæe razine izbornika
              crtajAnimacijuDesnog=false;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;

              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;

              kodoviCrtanjaBotuna[0]=-1;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=0;
              kodoviCrtanjaBotuna[3]=-1;

              geTransKockLijevo.jesamLiZiv=true;
              geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
    break;    
	  case 126: 
		  tempBrSlikeUSpriteu=13;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=false;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=-1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
    break; 
	  case 127: 
		  tempBrSlikeUSpriteu=14;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=true;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
    break; 
	  case 128: case 129: case 154: case 153: case 155:case 105:case 104:case 178:case 179://RANK 1
		  tempBrSlikeUSpriteu=18;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=false;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
    break; 
	  case 130: case 133: case 156: case 159: case 162:case 106:case 109:case 180:case 183: //RANK 2
		  tempBrSlikeUSpriteu=19;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=false;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
    break; 
	  case 131: case 134: case 157: case 160:case 163:case 107:case 110:case 181:case 184://RANK 3
		  tempBrSlikeUSpriteu=20;//stavlja sliku sljedeæe razine izbornika
          crtajAnimacijuDesnog=false;
          crtajAnimacijuLijevog=true;
          crtajAnimacijuSell=true;

          crtajDesno=true;
          crtajLijevo=true;
          crtajSell=true;
          crtajOsovinu=true;

          kodoviCrtanjaBotuna[0]=-1;
          kodoviCrtanjaBotuna[1]=-1;
          kodoviCrtanjaBotuna[2]=0;
          kodoviCrtanjaBotuna[3]=1;

          geTransKockLijevo.jesamLiZiv=true;
          geTransKockDesno.jesamLiZiv=true;
          geTransKockSell.jesamLiZiv=true;

          tekPoceoLijevo=true;
          tekPoceoDesno=true;
          tekPoceoSell=true;
    break; 
	case 101: tempBrSlikeUSpriteu=6;//stavlja sliku sljedeæe razine izbornika
	          crtajAnimacijuDesnog=false;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;
    
              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;
    
              kodoviCrtanjaBotuna[0]=-1;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=0;
              kodoviCrtanjaBotuna[3]=-1;
    
              geTransKockLijevo.jesamLiZiv=true;
	          geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
	       
	    break;
	case 102: tempBrSlikeUSpriteu=7;//stavlja sliku sljedeæe razine izbornika
	          crtajAnimacijuDesnog=false;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;

              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;

              kodoviCrtanjaBotuna[0]=-1;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=0;
              kodoviCrtanjaBotuna[3]=-1;

              geTransKockLijevo.jesamLiZiv=true;
              geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
    break;    
	case 103: 
		tempBrSlikeUSpriteu=11;//stavlja sliku sljedeæe razine izbornika
        crtajAnimacijuDesnog=true;
        crtajAnimacijuLijevog=true;
        crtajAnimacijuSell=true;

        crtajDesno=true;
        crtajLijevo=true;
        crtajSell=true;
        crtajOsovinu=true;

        kodoviCrtanjaBotuna[0]=-1;
        kodoviCrtanjaBotuna[1]=-1;
        kodoviCrtanjaBotuna[2]=0;
        kodoviCrtanjaBotuna[3]=1;

        geTransKockLijevo.jesamLiZiv=true;
        geTransKockDesno.jesamLiZiv=true;
        geTransKockSell.jesamLiZiv=true;

       tekPoceoLijevo=true;
       tekPoceoDesno=true;
       tekPoceoSell=true;
break;    
	case 200: tempBrSlikeUSpriteu=5;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
              
              
              crtajAnimacijuDesnog=true;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=false;
              
              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=false;
              crtajOsovinu=true;
              
              kodoviCrtanjaBotuna[0]=0;
              kodoviCrtanjaBotuna[1]=1;
              kodoviCrtanjaBotuna[2]=2;
              kodoviCrtanjaBotuna[3]=3;
              
              geTransKockLijevo.jesamLiZiv=true;
          	  geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;
     
              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
              
        break;
    ///toranj kasarna
	case 150: tempBrSlikeUSpriteu=8;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
	          
              crtajAnimacijuDesnog=false;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;

              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;

              kodoviCrtanjaBotuna[0]=-1;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=0;
              kodoviCrtanjaBotuna[3]=-1;

              geTransKockLijevo.jesamLiZiv=true;
              geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
        break;
      ///toranj kasarna 2.
	case 151: tempBrSlikeUSpriteu=9;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
	          crtajAnimacijuDesnog=false;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;

              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;

              kodoviCrtanjaBotuna[0]=-1;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=0;
              kodoviCrtanjaBotuna[3]=-1;

              geTransKockLijevo.jesamLiZiv=true;
              geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
        break;
      ///toranj kasarna3
	case 152: tempBrSlikeUSpriteu=10;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
	          crtajAnimacijuDesnog=true;
              crtajAnimacijuLijevog=true;
              crtajAnimacijuSell=true;

              crtajDesno=true;
              crtajLijevo=true;
              crtajSell=true;
              crtajOsovinu=true;

              kodoviCrtanjaBotuna[0]=0;
              kodoviCrtanjaBotuna[1]=-1;
              kodoviCrtanjaBotuna[2]=1;
              kodoviCrtanjaBotuna[3]=2;

              geTransKockLijevo.jesamLiZiv=true;
              geTransKockDesno.jesamLiZiv=true;
              geTransKockSell.jesamLiZiv=true;

              tekPoceoLijevo=true;
              tekPoceoDesno=true;
              tekPoceoSell=true;
        break;
	
	
    /////////////////    
	   
	default: // u sluèaju da nepostoji nijedan takav u switch-caseu   
		tempToranj=null;
		pokrenutIzbornik=false;
		break;
	}
  //  tempToranj.izbornikNaMeni(true);// govvori mu da je zaista on taj koji ima izbornik
	////////////////////////////////
}
/////////////////////////////////////////////////////////////

////////////////////metode interfecea uimanagerobject////////////////

@Override
public void setImTouched(boolean b) {
	// TODO Auto-generated method stub
	imTouched=b;
	if(b){
		pokrenutIzbornik=true;//vraæa zastavice u poèetno stanje tako da se izbornik ugasi
	     }
	else {// u sluèaju da je kliknuto pokraj prozora izbornika uiMnager vraža false
		pokrenutIzbornik=false;//vraæa zastavice u poèetno stanje tako da se izbornik ugasi
	}
}
@Override
public void setTouchedObject(UIManagerObject obj) {// ako primi isti touched object kao i on onda to znaèi da se kliknulo u podruèje izbornika
	// TODO Auto-generated method stub
	if(obj==this) imTauchedZaIzbor=true;// ako je isti objekt na koj se kliknulo to znaèi da je korisnik izabrao polje ili promašio
	else{
		
		iskljuciIzbornik();
	}
}
@Override
public void setTouchedXY(float x, float y) {
	// TODO Auto-generated method stub
	touchedX=x;
	touchedY=y;
	if(imTauchedZaIzbor==false){/// ova metoda se pokreæe uvijek kada se klikne na ekran i poziva se u objektu koji je oznaèen sa Imtouched, tako da znaèi ako  nije ponovno kliknnuto na izbornik moa da je kliknuto pokraj
		if(tempToranj!=null)this.tempToranj.uhvatiPozicijuKlikaIzvanIzbornika( x,y);
		iskljuciIzbornik();
		this.tempToranj=null;
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
	return tempXProz;// naredne 4 varijable su važne jer bi inaèe bili izabrani objekti ispod izbornika i isto tako se nebi dobili x i y
}
@Override
public float getY() {
	// TODO Auto-generated method stub
	return tempYProz;
}
@Override
public float getSir() {
	// TODO Auto-generated method stub
	return tempKonVelX;
}
@Override
public float getVis() {
	// TODO Auto-generated method stub
	return tempKonVelY;
}
//////////////////////////////////////////////////////////
@Override
public void GameLinkerIzvrsitelj(GameEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void setDvojnikaULogici(GameLogicObject obj) {
	// TODO Auto-generated method stub
	
}
@Override
public RectF getGlavniRectPrikaza() {
	// TODO Auto-generated method stub
	return new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY);
}
@Override
public void onSystemResume() {
	// TODO Auto-generated method stub
	
}
@Override
public boolean getDaliDaIgnoriramTouch() {
	// TODO Auto-generated method stub
	return false;
}
}
