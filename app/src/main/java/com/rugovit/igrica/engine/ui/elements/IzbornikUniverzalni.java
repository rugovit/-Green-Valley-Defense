package com.rugovit.igrica.engine.ui.elements;

import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;

public abstract class IzbornikUniverzalni  implements UIManagerObject {
	private boolean reagirajNaMargine=false;
private float uiManMarginaLijeva,	uiManMarginaDesna,uiManMarginaGore,uiManMarginaDolje;
private boolean crtajProzorZajednoSaUvodomOKrajem=false;	
private boolean direktnaSlikaProzora=false;
private SpriteHendler spriteDirektneSlikeProzora;
private int brDirSlikeUSliciProzora,brDirRedUSliciProzora,brDirStupUSliciProzora;

private float xKlik;
private float yKlik;
private UIManagerObject klikObj;
private boolean kliknutoPokraj=false;

private boolean iskljucenoCrtanje=false;	
///////////////ZASTAVICE
private boolean noveTouchVrijednosti=false;
private boolean ostaniUpaljenNakonToucha=false;
private boolean reagirajINaNekoristenaPolja=false;	
private boolean uiSaljeMeni=false;// kada je kliknuto na izbornik on se postavlja od uimanagera u true i to mi je znak da primim xy toucha
private boolean pokrenutIzbornik=false;// oznaèava dali je jedan od tornjeva pokrenuo izbornik
private boolean prvPokTemIzb=true;// prvo pokretanje temp izbornika, služit æe najviše za to da neraèunna svaki put velièinu izbornika
//////////////////////////	
//////////////KONSTANTE	
////Paintovi
private Paint paintProzor;
private Paint paintNekorBot;
///////
private float PpCmX;
private float PpCmY; 
private GameLogic GL;// napravit æu funkciju za pauzu
//private double ŠvrijUvoda;// vrijeme koliko traje uvod
private int indikator;
private SpriteHendler sprHend;
private Canvas can;
private RectF tempRectCijelogProzora;
private int xProz,yProz;
private float razIzmPoljUCmX;//=0.3f;// razmak izmeðu polja gdje æe biti dugmiæi, taj dio bi jedini trebao reagirati na dodir
private float razIzmPoljUCmY;//=0.3f;
private float razIzmPoljUPixX;
private float razIzmPoljUPixY;
private float marginaLijevaPix;
private float marginaDesnaPix;
private float marginaGornjaPix;
private float marginaDonjaPix;
private float marginaLijevaCm;
private float marginaDesnaCm;
private float marginaGornjaCm;
private float marginaDonjaCm;
private float naslovVelXPix;
private float naslovVelYPix;
private float naslovVelXCm;
private float naslovVelYCm;
private float naslovUdaljenostOdVrhaProzoraPix;
private float naslovUdaljenostOdVrhaProzoraCm;
private float naslovUdaljenostOdDesnKrajProzoraPix;
private String textNaslova=null;
private float naslovUdaljenostOdDesnKrajProzoraCm;
private float velUCmPoljaX;//=1.5f;// velicina jednog polja za izbor u cm, poslije se proraèunnava velièina prozura u pikselima u odnosu na gustoæu ekrana
private float velUCmPoljaY;//=1.5f;
private float velUPixPoljaX;
private float velUPixPoljaY;
private int sirEkr;// visina i širina ekrana
private int visEkr;
private UIManager uiMan;
////////////////////////
///////////VARIJABLe

private float ukupniDx=0,ukupniDy=0;
private HashMap<Integer,Integer[]> listaDugmicaSaPosebnimSlicicama; 
private HashMap<Integer,Integer[]> listaDugmicaSaDodatnimSlicicama; 
private HashMap<Integer,SpriteHendler> listaSpritovaZaDodatneSlicice;
private boolean uvodNacrtan=false;
private boolean zavrsiIzbornik=false;
private float pomCanX=0,pomCanY=0;
private float pomCanXZaStojeciIzb=0,pomCanYZaStojeciIzb=0;
private LinkedList<Integer> listaNekoristenih;// sprema listu nekoristenih polja, ona æe biti zamagljena i reagirat æe kao da je kliknuta izvan polja
private int frameUvoda=0;
private float touchedX;// sprema se gdje je izbornik dodirnut
private float touchedY;
private float tempXProz,tempYProz;// xy prozora proraèunava se posebno za svaki toranj pri pozivu od tornja
private float tempKonVelX, tempKonVelY;/////konaèna velièina prozora nakon  što se odigrao uvod
private int tempBrSlikeUSpriteu;// imat æe više slikka u svakom spriteu za ovu klasu, po jedna za svaki tip tornja
private int tempBrStu,tempBrRed;// odreðuje se broj redova i stupaca radi stvaranja pravilne geometrije za dodir 
private double vrijPocetka;
private float xProzoraCm=0;
private float yProzoraCm=0;
private float xProzoraPix=0;
private float yProzoraPix=0;
private HashMap<Integer,String> textZaDugmice;
private HashMap<Integer,TextPaint> paintZaDugmice;
private TextPaint paintZaNaslov;
private boolean velPoljaJeUPix=false;
private boolean razIzmPoljaJeUPix=false;
private boolean polozajprozJeUPix=false;
private boolean margineSuUPix=false;
private boolean textNaslovaJeUPix=false;
private boolean crtajIkadaNijePokrenut=false;
private boolean jeliStatickiIzbornik=false; 
private boolean crtajVanjskiIspodBezObzira=false,crtajVanjskiIznadBezObzira=false;
private float fps;
public IzbornikUniverzalni(SpriteHendler sprHend,UIManager uiMan,int brR, int brS,int indikator) {
	this.sprHend=sprHend;
	this.uiMan=uiMan;
	this.indikator=indikator;

	//////stvaranje crno bijelog providnog painta
	paintNekorBot= new Paint();
	paintNekorBot.setAlpha(170);
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0);
    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
	paintNekorBot.setColorFilter(filter);
	////
	paintProzor=new Paint();
	//paintProzor.setAlpha(220);
	////////////
	listaDugmicaSaPosebnimSlicicama=new HashMap<Integer,Integer[]>();
	listaDugmicaSaDodatnimSlicicama=new HashMap<Integer,Integer[]>();
	listaSpritovaZaDodatneSlicice=new HashMap<Integer,SpriteHendler>();
	tempBrStu=brS;
    tempBrRed=brR;
    this.textZaDugmice= new HashMap<Integer,String>();
    this.paintZaDugmice= new HashMap<Integer,TextPaint>();
    tempRectCijelogProzora=new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY);
}
//////////////NACRTAJ METODA
@Override
public void nacrtaj(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY) {
	this.can=can;
	this.uiMan=uiMan;
	this.pomCanY=pomCanY;
	this.pomCanX=pomCanX;
	 this.PpCmX=PpCmX;
	 this.PpCmY=PpCmY;
	 this.fps=fps;
	 if(reagirajNaMargine&&this.jeliStatickiIzbornik){
	       uiManMarginaLijeva=uiMan.getMarginuLijevu();
	       uiManMarginaDesna=uiMan.getMarginuDesnu();
	       uiManMarginaGore=uiMan.getMarginuGore();
	       uiManMarginaDolje=uiMan.getMarginuDolje();
	 }
	 else{
		  uiManMarginaLijeva=0;
	       uiManMarginaDesna=0;
	       uiManMarginaGore=0;
	       uiManMarginaDolje=0;
	 }
     if(can!=null){
    	 this.visEkr=can.getHeight();
    	 this.sirEkr=can.getWidth();
     }
	 if(this.jeliStatickiIzbornik){
		 pomCanXZaStojeciIzb=-pomCanX;
		 pomCanYZaStojeciIzb=-pomCanY;
	 }
	 if(crtajVanjskiIspodBezObzira)nacrtajVanjskiIspod( can,fps, uiMan,PpCmX, PpCmY,pomCanX, pomCanY);
	if(pokrenutIzbornik)
	   {
		if(kliknutoPokraj){
			
			kliknutoPokraj=false;
		   kliknutoSaStraneIliNaNekiDrugiObjekt(xKlik, yKlik,  klikObj );
		}
	
		 if(prvPokTemIzb){
			 if(!this.velPoljaJeUPix){
				 velUPixPoljaX=velUCmPoljaX*PpCmX;
					velUPixPoljaY=velUCmPoljaY*PpCmY;
				 }
			 if(!this.razIzmPoljaJeUPix){
				 razIzmPoljUPixX=razIzmPoljUCmX*PpCmX;
				 razIzmPoljUPixY=razIzmPoljUCmY*PpCmY;
			 }
			 if(!this.polozajprozJeUPix){
				 xProzoraPix= xProzoraCm*PpCmX;
				 yProzoraPix= yProzoraCm*PpCmY;
			 }
			 if(!margineSuUPix){
				 this.marginaDonjaPix=marginaDonjaCm*PpCmY;      
					this.marginaDesnaPix=marginaDesnaCm*PpCmX;
					this.marginaGornjaPix=marginaGornjaCm*PpCmY;
					this.marginaLijevaPix=marginaLijevaCm*PpCmX;
			 }
			 if(!textNaslovaJeUPix){
				 naslovVelYPix=naslovVelYCm*PpCmY;
				 naslovVelXPix= naslovVelXCm*PpCmX;
				 naslovUdaljenostOdVrhaProzoraPix=naslovUdaljenostOdVrhaProzoraCm*PpCmY;
				 naslovUdaljenostOdDesnKrajProzoraPix=naslovUdaljenostOdDesnKrajProzoraCm*PpCmX;
			 }
			 this.tempXProz=this.xProzoraPix;
			 this.tempYProz=this.yProzoraPix;
			 izracVelProzora();// raèuna v rrijednosti velièine prozora koje koriste ostale funkcije koje slijede, pokreæe se samo jedanput pri pozivanju izbornika radi uštede ciklusa
		 }
		
	     if(!iskljucenoCrtanje) nacrtajVanjskiIspod( can,fps, uiMan,PpCmX, PpCmY,pomCanX, pomCanY); 
			
	     if(!uvodNacrtan){// 
	    	 if(!iskljucenoCrtanje) uvodNacrtan= nacrtajUvod(fps,can);
	           }
	 	if(!iskljucenoCrtanje)   if(uvodNacrtan||crtajProzorZajednoSaUvodomOKrajem){
	    	
	    	if(!zavrsiIzbornik||crtajProzorZajednoSaUvodomOKrajem) {
	    		                           nacrtajProzor888();
	    	}
	         }
	    // if(true) {

	     if(uiSaljeMeni&&noveTouchVrijednosti)	reagirajNaDodir888();//imTauchedZaIzbor se postavlja kroz  tauchedobject i ako je isti kao ova klasa to znaèi da je kliknuto na prozor i treba se vidjeti jeli izabrano polje
	    		
	    	
	     if(zavrsiIzbornik){
	    	 if(!iskljucenoCrtanje){ if(nacrtajKraj(fps,can)){
	    		 iskljuciIzbornik();
	    		 uvodNacrtan=false;
	    		 zavrsenaZavrsnaAnimacija();
	    		 zavrsiIzbornik=false;
	    	     }
	    	 }
	    	 else {
	    		 iskljuciIzbornik();
	    		 uvodNacrtan=false;
	    		 zavrsenaZavrsnaAnimacija();
	    		 zavrsiIzbornik=false;
	    	 }
	    	
	     }
		 prvPokTemIzb =false;// postavlja ponovno da se neraèuna velièina izbornika tijekom animacije
	   }
	
	else if(crtajIkadaNijePokrenut){
		if(!iskljucenoCrtanje)nacrtajProzor888();
	}

    if(crtajVanjskiIznadBezObzira){
    	if(!iskljucenoCrtanje)nacrtajVanjskiIznad( can,fps, uiMan,PpCmX, PpCmY,pomCanX, pomCanY);	
	}
    
    this.touchedX=this.touchedY=0;
    noveTouchVrijednosti=false;
}
//////////////////////////////////////
public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
		
	  }
//////UNIVERZALNI DODATAK////////////////////////////////
public SpriteHendler getGlavniSprite(){
	return this.sprHend;
}
public void setAlfaSvihDugmica(int alfa){
	if(paintZaDugmice!=null)for(int i=0;i<this.tempBrRed*this.tempBrStu;i++){
		                                             if(paintZaDugmice.containsKey(i)) this.paintZaDugmice.get(i).setAlpha(alfa);
	                                                   }
	
}
public void setAlfaNaslova(int alfa){
	if(paintZaNaslov!=null)this.paintZaNaslov.setAlpha(alfa);
}
public void setAlfaGlavnogProzora(int alfa){
	if(paintProzor!=null)this.paintProzor.setAlpha(alfa);
}
public void setPaintZaGlavniProzor(Paint p){
	this.paintProzor=p;
}
public void reagirajNaUiManMargine(boolean daNE){
	reagirajNaMargine=daNE;
} 
public void crtajProzorZajdnoSaAnimacijomUvodaIkraja(){
	crtajProzorZajednoSaUvodomOKrajem=true;
}
public void dodajSpriteGlavni(SpriteHendler sprite){
   this.sprHend=sprite;
}
public void ostaniUpaljenINakonEfektnogTouchaNaBotun(){
	ostaniUpaljenNakonToucha=true;
}
public void iskljuciCrtanje(boolean iskljCrtanje){
	this.iskljucenoCrtanje=iskljCrtanje;
}

public void crtajNacrtajVanjskiIznadBezObzira(boolean crtajBezObzira){
	this.crtajVanjskiIznadBezObzira=crtajBezObzira;
}
public void crtajNacrtajVanjskiIspodBezObzira(boolean crtajBezObzira){
	this.crtajVanjskiIspodBezObzira=crtajBezObzira;
}
public void postaviPaintNekoristenihBoruna(Paint paintNekoristenih){
	paintNekorBot= paintNekoristenih;
	
}
public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){}
public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){}
public void regirajINaNekoristenaPolja(){
	reagirajINaNekoristenaPolja=true;
} 

public void postaviStatickiIzbornik(){
	this.jeliStatickiIzbornik=true;
} 
public void postaviNaslovIzbornikaPix(String textnaslova,float xVelPix , float yVelPix,float udaljenostOdDesnKrajPlusMinusPix,float udaljenostOdVrhaPlusMinusPix,TextPaint paintZaNaslov){
	textNaslova= textnaslova;
	textNaslovaJeUPix=true;
	naslovVelYPix=yVelPix;
	naslovVelXPix=xVelPix;
	naslovUdaljenostOdDesnKrajProzoraPix=udaljenostOdDesnKrajPlusMinusPix;
	this.paintZaNaslov=paintZaNaslov;
	naslovUdaljenostOdVrhaProzoraPix=udaljenostOdVrhaPlusMinusPix;
}
public void postaviNaslovIzbornikaCm(String textnaslova,float xVelCm, float yVelCm,float udaljenostOdDesnKrajPlusMinusCm,float udaljenostOdVrhaPlusMinusCm,TextPaint paintZaNaslov){
	textNaslova= textnaslova;
	naslovVelYCm=yVelCm;
	naslovVelXCm=xVelCm;
	naslovUdaljenostOdDesnKrajProzoraCm=udaljenostOdDesnKrajPlusMinusCm;
	naslovUdaljenostOdVrhaProzoraCm=udaljenostOdVrhaPlusMinusCm;
	this.paintZaNaslov=paintZaNaslov;
}
public void crtajIkadaNijePokrenut(boolean daNe){
	crtajIkadaNijePokrenut=daNe;
} 
public void zavrsenaZavrsnaAnimacija(){
	pokrenutIzbornik=false;
}
public boolean pokreniZavrsnuAnimaciju(){
	pokrenutIzbornik=true;
	zavrsiIzbornik=true;
	
	return this.pokrenutIzbornik;
}
public RectF dajMiRectCijelogProzora(){
	tempRectCijelogProzora=new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY);
	tempRectCijelogProzora.set(tempRectCijelogProzora.left+pomCanXZaStojeciIzb+ukupniDx, tempRectCijelogProzora.top+pomCanYZaStojeciIzb+ukupniDy, 
			tempRectCijelogProzora.right+pomCanXZaStojeciIzb+ukupniDx, tempRectCijelogProzora.bottom+pomCanYZaStojeciIzb+ukupniDy);
	return this.tempRectCijelogProzora;}
 public void backBotunStisnut(){
	Activity ac=(Activity)this.uiMan.context;
	uiMan.onBackPressJeNormalan=true;
	ac.onBackPressed();// salje activitiju ponovno da je stisnut ali  mjenja zastavicu u uiMan koju activity provjerava
}
public abstract void univerzalniIzvrsitelj(int brTouchPolja);
public abstract void izbornikNaMeniUniverzalni(boolean izbornikNaMeni);
//blder
public void postaviDirektnoGlavnuSlikuProzora(SpriteHendler sprite,int brSlike,int brRedka,int brStupca){
	direktnaSlikaProzora=true;
	spriteDirektneSlikeProzora=sprite;
	brDirSlikeUSliciProzora=brSlike;
    brDirRedUSliciProzora= brRedka;
    brDirStupUSliciProzora=brStupca;
}

public void postaviDirektnoSlicicuZaDugmic(int brDug, int brRedSpr, int brStuSpr){
    Integer[]lista ;
    lista=null;
    if(this.sprHend.brojRedova(0)-1>=this.sprHend.brojStupaca(0)-1&&this.tempBrStu>=brStuSpr){
    	 lista =new Integer[2];
    	 lista[0]=brRedSpr;
         lista[1]=brStuSpr;
         this.listaDugmicaSaPosebnimSlicicama.put(brDug,lista);
        }
	
   } 
public void postaviDodatnuSlicicuZaDugmic(int brDug,SpriteHendler spr,int brSl,int brRedSpr, int brStuSpr){
    Integer[]lista ;
    lista=null;
   
    	 lista =new Integer[3];
    	 lista[0]=brRedSpr;
         lista[1]=brStuSpr;
         lista[2]=brSl;
         this.listaDugmicaSaDodatnimSlicicama.put(brDug,lista);
         this.listaSpritovaZaDodatneSlicice.put(brDug, spr);
        
	
   }
public void izbrisiDodatnuSlicicuZaDugmic(int brDug){
    if(this.listaDugmicaSaDodatnimSlicicama.containsKey(brDug)){
         this.listaDugmicaSaDodatnimSlicicama.remove(brDug);
         this.listaSpritovaZaDodatneSlicice.remove(brDug);
    }
	
   }
public void izbrisiSveDodatneSliciceZaDugmic(){

         this.listaDugmicaSaDodatnimSlicicama.clear();
         this.listaSpritovaZaDodatneSlicice.clear();
    
	
   }  

public void pomakniIzbornikAkomulirajuci(float dx, float dy){
	this.ukupniDx+=dx;
	this.ukupniDy+=dy;
}
public void pomakniIzbornikAkomulirajuciApsolutno(float ukupniDx, float ukupniDy){
	this.ukupniDx=ukupniDx;
	this.ukupniDy=ukupniDy;
}

public void postaviMargineLijevaDesnaGornjaDonjaPix(float marginaLijevaPix, float marginaDesnaPix ,float marginaGornjaPix,
		float marginaDonjaPix){
	this.marginaDonjaPix=marginaDonjaPix;
	this.marginaDesnaPix=marginaDesnaPix;
	this.marginaGornjaPix=marginaGornjaPix;
	this.marginaLijevaPix=marginaLijevaPix;
	margineSuUPix=true;
	this.prvPokTemIzb=true;
}
public void postaviMargineLijevaDesnaGornjaDonjaCm(float marginaLijevaCm, float marginaDesnaCm ,float marginaGornjaCm,
		float marginaDonjaCm){
	this.marginaDonjaCm=marginaDonjaCm;
	this.marginaDesnaCm=marginaDesnaCm;
	this.marginaGornjaCm=marginaGornjaCm;
	this.marginaLijevaCm=marginaLijevaCm;
	this.prvPokTemIzb=true;
}
public void postaviVelicinuPoljaUCm(float xVelCm, float yVelCm){
	velUCmPoljaX=xVelCm;
	velUCmPoljaY=yVelCm;
	this.prvPokTemIzb=true;
} 
public void postaviVelicinuPoljaUPix(float xVelPix, float yVelPix){
	velUPixPoljaX=xVelPix;
	velUPixPoljaY=yVelPix;
	/*if(!this.prvPokTemIzb){
		izracVelProzora();
	}*/
	this.velPoljaJeUPix=true;
	this.prvPokTemIzb=true;
	
} 
public void postaviRazmakIzmeduPoljaCm(float xRazCm, float yRazCm){
	razIzmPoljUCmX= xRazCm;
	razIzmPoljUCmY= yRazCm;
	this.prvPokTemIzb=true;
}
public void postaviRazmakIzmeduPoljaPix(float xRazPix, float yRazPix){
	razIzmPoljUPixX= xRazPix;
	razIzmPoljUPixY= yRazPix;
	this.razIzmPoljaJeUPix=true;
	this.prvPokTemIzb=true;
}
public void postaviLijeviVrhProzoraCm(float x, float y){
	 xProzoraCm=x;//-this.pomCanX;
	 yProzoraCm=y;//-this.pomCanY;
	 xProzoraPix= xProzoraCm*PpCmX;
	 yProzoraPix= yProzoraCm*PpCmY;
}
public void postaviLijeviVrhProzoraPix(float xPix, float yPix){
	 xProzoraPix=xPix;//-this.pomCanX;
	 yProzoraPix=yPix;//-this.pomCanY;
	 tempXProz=xProzoraPix;
	 tempYProz=yProzoraPix;
	 this.polozajprozJeUPix=true;
	 
}
public void postaviTextDugmica(int indexDugmica, String text,TextPaint tp){
	this.textZaDugmice.put(indexDugmica, text);
	this.paintZaDugmice.put(indexDugmica, tp);
}
public float dajMiPomakCanvasaX(){
	return pomCanX;
} 
public float dajMiPomakCanvasaY(){
	return pomCanY;
} 

////////////////////////////////////////////////////////////
//////////////////PRIVATNE METODE
private void reagirajNaDodir888(){
	
		int s=0;// brojaè stupca
		int r=0;// brojjaè retka
		int polje;// polje tj broj dugmiæa
		boolean nasaRed=false;
		boolean nasaStu=false;
		/////////////traženje
		    while(tempBrRed>r ){// inkrimentira kroz retke
			     if(touchedY>ukupniDy+pomCanYZaStojeciIzb+this.marginaGornjaPix+tempYProz+razIzmPoljUPixY*(r+1)+velUPixPoljaY*r&&
					touchedY<ukupniDy+pomCanYZaStojeciIzb+this.marginaGornjaPix+tempYProz+razIzmPoljUPixY*(r+1)+velUPixPoljaY*(r+1)){
					                nasaRed=true;
				                    break;
			               }
		     	r++;
		       }

		      while(tempBrStu>s){// inkrementira kroz stupce
		      if(touchedX>ukupniDx+pomCanXZaStojeciIzb+this.marginaDesnaPix+tempXProz+razIzmPoljUPixX*(s+1)+velUPixPoljaX*s&&
			       touchedX<ukupniDx+pomCanXZaStojeciIzb+this.marginaDesnaPix+tempXProz+razIzmPoljUPixX*(s+1)+velUPixPoljaX*(s+1)){
			                 nasaStu=true;
		                     break;// izlazi iz petlje kada naðe i istovremeno mu ostaje zapamæen broj stupaca
		                 }
		            s++;	
		      }
	         if(nasaRed&&nasaStu){
		               polje=(tempBrStu*r)+s+1;
		               if(listaNekoristenih!=null){// u sluèaju da je korisnik izbornika oznaèio neka polja kao nekoristena provjerava se dali je kliknuto na to polje i reagira se kao da je kliknuto izvan polja
			                 if(!reagirajINaNekoristenaPolja&&listaNekoristenih.contains(polje)){// ako je kliknuto polje oznaèeno kao nekorisšetno reagira se kao da je kliknuto izvan polja
				                    uiMan.setOznacenSam(this);
			                     }
			                  else {
				                if(!ostaniUpaljenNakonToucha) pokrenutIzbornik=false;
				                 this.univerzalniIzvrsitelj(polje);//a ako ne, vraæa broj polja poèevši od  jedan
		 	                        }
		               }
		      else{
			           if(!ostaniUpaljenNakonToucha)pokrenutIzbornik=false;
			           this.univerzalniIzvrsitelj(polje);// vraæa broj polja poèevši od  jedan
		             }
	               } 
	         else{// zbog toga što ako primi imTouched to znaèi da je definitivno kliknuto na izbornik ali na razmak izmeðu polja ali ui manager automatski prestane slati xy izborniku pa se treba ponovno prijaviti
		                 uiMan.setOznacenSam(this);
	             }
	  
	     
}
public void nacrtajProzor888(){
	
	// prvo se crta podloga prozora koja se treba nalaziti u prvoj slici u sprateu, u nultoj je spremljena slika za animaciju uvoda
	tempRectCijelogProzora=new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY);
	tempRectCijelogProzora.set(tempRectCijelogProzora.left+pomCanXZaStojeciIzb+ukupniDx, tempRectCijelogProzora.top+pomCanYZaStojeciIzb+ukupniDy, 
			tempRectCijelogProzora.right+pomCanXZaStojeciIzb+ukupniDx, tempRectCijelogProzora.bottom+pomCanYZaStojeciIzb+ukupniDy);
	if(this.direktnaSlikaProzora){
		this.spriteDirektneSlikeProzora.nacrtajSprite(can, this.brDirSlikeUSliciProzora,this.brDirStupUSliciProzora,this.brDirRedUSliciProzora,tempRectCijelogProzora,paintProzor);
	}
	else if(sprHend!=null)if(sprHend.postojiLiSlikNaMjestu(1))sprHend.nacrtajSprite(can, 1,0, 0,tempRectCijelogProzora,paintProzor);
    
	//onda se na to crtaju dugmiæi, koji su spremljeni u spriteu na slicu rednog broja tempBrSlikeUSpriteu
	int s=0;// brojaè stupca
	int r=0;// brojjaè retka
	int d=1;// brojac dugmiæa
	int stu=0;// brojacstupca u spriteu 
	int red=0;// brojac redka u spriteu
	float x1;
	float x2;
	float y1;
	float y2;
	float naslovX=this.tempRectCijelogProzora.left+this.naslovUdaljenostOdDesnKrajProzoraPix;
	float naslovY=this.tempRectCijelogProzora.top+this.naslovUdaljenostOdVrhaProzoraPix;
	if(textNaslova!=null)nacrtajTextUnutarPravokutnika(naslovX,naslovY,this.naslovVelXPix,this.naslovVelYPix,paintZaNaslov,textNaslova);
	while(tempBrRed>r ){// inkrementira kroz redtke
		
		while(tempBrStu>s){// inkrementira kroz stupce
			  x1=ukupniDx +pomCanXZaStojeciIzb+this.marginaDesnaPix+tempXProz+razIzmPoljUPixX*(s+1)+velUPixPoljaX*s;
			  y1=ukupniDy+pomCanYZaStojeciIzb+this.marginaGornjaPix+tempYProz+razIzmPoljUPixY*(r+1)+velUPixPoljaY*r;
			  x2=ukupniDx+pomCanXZaStojeciIzb+this.marginaDesnaPix+tempXProz+razIzmPoljUPixX*(s+1)+velUPixPoljaX*(s+1);
			  y2=ukupniDy+pomCanYZaStojeciIzb+this.marginaGornjaPix+tempYProz+razIzmPoljUPixY*(r+1)+velUPixPoljaY*(r+1);
			  
			 if(listaNekoristenih!=null)//ako lista postoji onda se svaki9 pojedinaèno provjerava dali je nekorišten
			
			{	
				
			  if(!listaNekoristenih.contains(d)){// ako taj dugmiæ nije u listi nekorištenih
				  if(sprHend!=null){
				      if(this.listaDugmicaSaPosebnimSlicicama.containsKey(d)){
				    	  Integer[] lista=listaDugmicaSaPosebnimSlicicama.get(d);
				    	  sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,lista[1],lista[0],
								  
									new RectF(x1,y1,
											x2,y2)); 
				      }
			          else {   
				       sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,stu, red,
			  
						new RectF(x1,y1,
								x2,y2)); 
			          }
				       }
				  if(this.textZaDugmice.containsKey(d-1)) {
					  nacrtajTextUnutarPravokutnika(x1,y1,x2-x1,y2-y1, this.paintZaDugmice.get((Integer)d-1),this.textZaDugmice.get((Integer)d-1));
					  }
		     	  }
			 
				
			  else{ // u sluèaju da se botunu ne koriste crtaju se sa paintom koji je providan i crno bijeli
				  if(sprHend!=null){
					      if(this.listaDugmicaSaPosebnimSlicicama.containsKey(d)){
					    	  Integer[] lista=listaDugmicaSaPosebnimSlicicama.get(d);
					    	  sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,lista[1],lista[0],
									  
										new RectF(x1,y1,
												x2,y2),paintNekorBot); 
					      }
				          else {   
					       sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,stu, red,
				  
							new RectF(x1,y1,
									x2,y2),paintNekorBot); 
				          }
					       }
				  if(this.textZaDugmice.containsKey(d-1)) {
				  TextPaint tp=new TextPaint();
				 if(this.paintZaDugmice.get((Integer)d-1)!=null) tp.set(this.paintZaDugmice.get((Integer)d-1));
				  tp.setAlpha(170);
				  
					  nacrtajTextUnutarPravokutnika(x1,y1,x2-x1,y2-y1,tp,this.textZaDugmice.get((Integer)d-1));
				  }
			     	  }
				 
				  
			}
			else{// ako nepostoji lista onda se svi crtaju normalno
				 if(sprHend!=null){	
					 
					 if(this.listaDugmicaSaPosebnimSlicicama.containsKey(d)){
						            Integer[] lista=listaDugmicaSaPosebnimSlicicama.get(d);
						            sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,lista[1],lista[0],
								   
									new RectF(x1,y1,
											x2,y2));
					                }
					   else{   sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,stu, red,
					   
							new RectF(x1,y1,
									x2,y2));
					   }
				     }
				if(this.textZaDugmice.containsKey(d-1)) {
					nacrtajTextUnutarPravokutnika(x1,y1,x1-x2,y2-y1, this.paintZaDugmice.get((Integer)d-1),this.textZaDugmice.get((Integer)d-1));
				}
				 
			}
			 if(this.listaDugmicaSaDodatnimSlicicama.containsKey(d)){
					Integer[] lista=listaDugmicaSaDodatnimSlicicama.get(d);
					
					this.listaSpritovaZaDodatneSlicice.get(d).nacrtajSprite(can, lista[2],lista[1],lista[0],
							  
							new RectF(x1,y1,
									x2,y2)); 
				}
		if(sprHend!=null){
			if(sprHend.brojStupaca(tempBrSlikeUSpriteu)-1<=stu) {
		    red++;
			stu=0;
			
		   }
		   else	  stu++;
			
		}
		 s++;
		 d++;
		}
	 s=0;// vraæa stupce u nula	
	 r++;	
	}
	if(!crtajVanjskiIznadBezObzira)nacrtajVanjskiIznad( can,fps, uiMan,PpCmX, PpCmY,pomCanX, pomCanY);

}
public boolean nacrtajUvod(float fps, Canvas can){
	return true;
}
public boolean nacrtajKraj(float fps, Canvas can){
	return true;
} 
//////////////neke cesto koristene privatne
private void iskljuciIzbornik(){
	//if(ignorirajKlikovePokrajIz==false){// ako je namjesteno da se ignorira onda se preskace iskljucivanje izbornika
	          pokrenutIzbornik=false;
              izbornikNaMeniUniverzalni(false);
	//}
}
public void nacrtajTextUnutarPravokutnika(float pozX,float pozY,float sir,float vis, TextPaint p, String text){
	int brClanova=text.length();
	////
	TextPaint tp=new TextPaint();
	if(p!=null)tp.set(p);
	Rect recText=new Rect();
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

public void izracVelProzora(){//proraèunnava vel prozora u odnosu na  gustoæu ekrana i njegovu velièinu i broj polja od  dotiènog tornja
	
	/*if(velUCmPoljaX*tempBrStu*PpCmX+razIzmPoljUCmX*PpCmX*(tempBrStu+1)>sirEkr||// u sluèaju da je izbornik veæi od cijelog ekrana  trebalo bi ga namjestit da ispunjava cijeli ekran
			 velUCmPoljaY*tempBrRed*PpCmY+razIzmPoljUCmY*PpCmY*(tempBrRed+1)>visEkr){// dodaje na osnovnu velièinu polja i razmak izmeðu njih
		 
	 }
	 else{*/// ako ne nakjestiti mu velièinu u odnosu na gustoæu ekrana i predodreæenu velièinu polja 
		 tempKonVelX=this.marginaDesnaPix+this.marginaLijevaPix+velUPixPoljaX* tempBrStu+razIzmPoljUPixX*(tempBrStu+1);// dodaje i razmake izmeðu polja
		 tempKonVelY=this.marginaDonjaPix+this.marginaGornjaPix+ tempBrRed*velUPixPoljaY+razIzmPoljUPixY*(tempBrRed+1);
		 //tempXProz=tempFaza.getRect().centerX()- tempKonVelX;// postavlja izbornik lijevo od tornja
		 //tempYProz=tempFaza.getRect().centerY()- tempKonVelY;
		 /////namjestanje ako prijeðe granice ekrana
		if(!this.jeliStatickiIzbornik){ 
			  if(tempXProz<uiManMarginaLijeva-pomCanX){// ako je  preblizu ruba ekrana pomièe se u nulu
			      tempXProz=0-pomCanX;
		      }
		      else if(tempXProz+tempKonVelX>sirEkr-pomCanX-uiManMarginaDesna){// ako je desni kut prozora otišao izvan desne strane ekrana vreæea se prema lijevo koliko treba
			// tempXProz-=tempXProz+tempKonVelX-sirEkr-pomCanX;// pomièe se ulijevo za razliku koliko je prešao
			        tempXProz=sirEkr-pomCanX-tempKonVelX;
		       }
		      if(tempYProz<uiManMarginaGore-pomCanY){// ako je  preblizu ruba ekrana pomièe se u nulu
			     tempYProz=0-pomCanY;
		       }
		     else if(tempYProz+tempKonVelY>visEkr-pomCanY-uiManMarginaDolje){// ako je desni kut prozora otišao izvan desne strane ekrana vreæea se prema lijevo koliko treba
			// tempYProz-=tempYProz+tempKonVelY-visEkr-pomCanY;// pomièe se ulijevo za razliku koliko je prešao
		        tempYProz=visEkr-pomCanY-tempKonVelY;
		    }
		 }
		 //////
		 tempRectCijelogProzora=new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY);
}
///////////////////////////////////////////////////////////////
////////////////////////PUBLIC METODE
public void izmjenilistuNekoristenih(LinkedList<Integer> listaNekoristenih){// u sluèju da se tijekom korištenja izbornik promijeni stanje, da se ta promjena prikže
	this.listaNekoristenih=listaNekoristenih;
}
public void pokreniMojIzbornik(LinkedList<Integer> listaNekoristenih){
	this.listaNekoristenih=listaNekoristenih;
	vrijPocetka=System.currentTimeMillis();// bilježi vrijeme kada je zatražen izbornik
	frameUvoda=0;// postavlja animaciju uvoda na pocetak
	pokrenutIzbornik=true;
	zavrsiIzbornik=false;
	prvPokTemIzb=true;
	uvodNacrtan=false;
	uiMan.setOznacenSam(this);// postavlja seba kao oznaèenog u ui manageru tako da dobije podatke o xy 
	this.tempXProz=xProzoraPix;
	this.tempYProz=yProzoraPix;
	izracVelProzora();
   /* /////////////case-switch izmeðu raznik tipa tornjeva, provjeravat æe indikator i puniti varijable koje æe koristiti ostatak klase
	switch(tempFaza.getIndikator()){
	case 101: tempBrSlikeUSpriteu=2;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
	          tempBrStu=1;
	          tempBrRed=4;
	    break;
	case 200: tempBrSlikeUSpriteu=2;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
              tempBrStu=1;
              tempBrRed=4;
        break;
	case 150: tempBrSlikeUSpriteu=2;// poèinje od 2, nulta æe uvijek biti za podlogu,1 za animaciju uvoda, sve ostale æe se prikazivati na vrhu te podloge kao dugmiæi
              tempBrStu=1;
              tempBrRed=4;
        break;          
	default: // u sluèaju da nepostoji nijedan takav u switch-caseu   
		tempFaza=null;
		pokrenutIzbornik=false;
		break;
	}*/
  //  tempToranj.izbornikNaMeni(true);// govvori mu da je zaista on taj koji ima izbornik
	////////////////////////////////
}
public void pokreniMojIzbornik(){
	vrijPocetka=System.currentTimeMillis();// bilježi vrijeme kada je zatražen izbornik
	frameUvoda=0;// postavlja animaciju uvoda na pocetak
	pokrenutIzbornik=true;
	zavrsiIzbornik=false;
	prvPokTemIzb=true;
	uvodNacrtan=false;
	uiMan.setOznacenSam(this);// postavlja seba kao oznaèenog u ui manageru tako da dobije podatke o xy 
	this.tempXProz=xProzoraPix;
	this.tempYProz=yProzoraPix;
	izracVelProzora();
   
}
/////////////////////////////////////////////////////////////
////////////////////metode interfecea uimanagerobject////////////////

@Override
public void setImTouched(boolean b) {
	// TODO Auto-generated method stub
	uiSaljeMeni=b;
	if(b){
		pokrenutIzbornik=true;//
	     }
	else{
		
	}
	
	}

@Override
public void setTouchedObject(UIManagerObject obj) {// ako primi isti touched object kao i on onda to znaèi da se kliknulo u podruèje izbornika
	// TODO Auto-generated method stub
	
	if(obj!=this&&this.uvodNacrtan){
		kliknutoPokraj=true;
		this.klikObj=obj;
	}
	else {
	
	}
}

@Override
public void setTouchedXY(float x, float y) {
	// TODO Auto-generated method stub
	noveTouchVrijednosti=true;
	touchedX=x;
	touchedY=y;
	if(this.uvodNacrtan){// uiman odmah �?im se izabere izbornik salje klikx /y ali ako postoji animacija moze je ocitati kao klik pokraj, zato se �?eka da se zavrse mogu�?e animacije
      
        if(!this.tempRectCijelogProzora.contains(x, y)){
        	  this.xKlik=x;
              this.yKlik=y;
              kliknutoPokraj=true;
         
        }
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
	return tempXProz+pomCanXZaStojeciIzb+ukupniDx;// naredne 4 varijable su važne jer bi inaèe bili izabrani objekti ispod izbornika i isto tako se nebi dobili x i y
    //
}
@Override
public float getY() {
	// TODO Auto-generated method stub
	return tempYProz+pomCanYZaStojeciIzb+ukupniDy;
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
public void onSystemResume(){
	
}
@Override
public void GameLinkerIzvrsitelj(GameEvent e) {
	// TODO Auto-generated method stub
	
}
@Override
public void setDvojnikaULogici(GameLogicObject obj) {
	// TODO Auto-generated method stub
	
}
@Override
public boolean getDaliDaIgnoriramTouch() {
	// TODO Auto-generated method stub
	return false;
}
}
