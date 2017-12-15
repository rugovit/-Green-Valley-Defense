package com.rugovit.igrica.engine.ui.elements;
import java.util.LinkedList;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.rugovit.igrica.engine.ui.levels.Faza;
import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;

public class IzbornikZaFazu implements UIManagerObject {
///////////////ZASTAVICE
private boolean imTauchedZaIzbor=false;	
private boolean imTouched=false;// kada je kliknuto na izbornik on se postavlja od uimanagera u true i to mi je znak da primim xy toucha
private boolean pokrenutIzbornik=false;// oznaèava dali je jedan od tornjeva pokrenuo izbornik
private boolean prvPokTemIzb=false;// prvo pokretanje temp izbornika, služit æe najviše za to da neraèunna svaki put velièinu izbornika
//////////////////////////	
//////////////KONSTANTE	
////Paintovi
private Paint paintProzor;
private Paint paintNekorBot;
///////
private float PpCmX;
private float PpCmY; 
private GameLogic GL;// napravit æu funkciju za pauzu
private double vrijUvoda;// vrijeme koliko traje uvod
private int indikator;
private SpriteHendler sprHend;
private Canvas can;
private int xProz,yProz;
private float razIzmPoljUCmX=0.3f;// razmak izmeðu polja gdje æe biti dugmiæi, taj dio bi jedini trebao reagirati na dodir
private float razIzmPoljUCmY=0.3f;
private float velUCmPoljaX=1.5f;// velicina jednog polja za izbor u cm, poslije se proraèunnava velièina prozura u pikselima u odnosu na gustoæu ekrana
private float velUCmPoljaY=1.5f;
private int sirEkr;// visina i širina ekrana
private int visEkr;
private UIManager uiMan;
////////////////////////
///////////VARIJABLe
private float pomCanX=0,pomCanY=0;
private LinkedList<Integer> listaNekoristenih;// sprema listu nekoristenih polja, ona æe biti zamagljena i reagirat æe kao da je kliknuta izvan polja
private int frameUvoda=0;
private float touchedX;// sprema se gdje je izbornik dodirnut
private float touchedY;
private float tempXProz,tempYProz;// xy prozora proraèunava se posebno za svaki toranj pri pozivu od tornja
private float tempKonVelX, tempKonVelY;/////konaèna velièina prozora nakon  što se odigrao uvod
private Faza tempFaza;// trenutni korisnik klase
private int tempBrSlikeUSpriteu;// imat æe više slikka u svakom spriteu za ovu klasu, po jedna za svaki tip tornja
private int tempBrStu,tempBrRed;// odreðuje se broj redova i stupaca radi stvaranja pravilne geometrije za dodir 
private double vrijPocetka;
public IzbornikZaFazu(SpriteHendler sprHend,UIManager uiMan,int indikator) {
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
	if(pokrenutIzbornik)
	   {
		 if(prvPokTemIzb){
			 this.visEkr=can.getHeight();
			 this.sirEkr=can.getWidth();
			 izracVelProzora();// raèuna v rrijednosti velièine prozora koje koriste ostale funkcije koje slijede, pokreæe se samo jedanput pri pozivanju izbornika radi uštede ciklusa
		 }
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
		 prvPokTemIzb =false;// postavlja ponovno da se neraèuna velièina izbornika tijekom animacije
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
			     if(touchedY>tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*r&&
					touchedY<tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*(r+1)){
					                nasaRed=true;
				                    break;
			               }
		     	r++;
		       }

		      while(tempBrStu>s){// inkrementira kroz stupce
		      if(touchedX>tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*s&&
			       touchedX<tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*(s+1)){
			                 nasaStu=true;
		                     break;// izlazi iz petlje kada naðe i istovremeno mu ostaje zapamæen broj stupaca
		                 }
		            s++;	
		      }
	         if(nasaRed&&nasaStu){
		               polje=(tempBrStu*r)+s+1;
		               if(listaNekoristenih!=null){// u sluèaju da je korisnik izbornika oznaèio neka polja kao nekoristena provjerava se dali je kliknuto na to polje i reagira se kao da je kliknuto izvan polja
			                 if(listaNekoristenih.contains(polje)){// ako je kliknuto polje oznaèeno kao nekorisšetno reagira se kao da je kliknuto izvan polja
				                    uiMan.setOznacenSam(this);
			                     }
			                  else {
				                 pokrenutIzbornik=false;
				                 tempFaza.vratiTouchPolje(polje);//a ako ne, vraæa broj polja poèevši od  jedan
		 	                        }
		               }
		      else{
			           pokrenutIzbornik=false;
			           tempFaza.vratiTouchPolje(polje);// vraæa broj polja poèevši od  jedan
		             }
	               } 
	         else{// zbog toga što ako primi imTouched to znaèi da je definitivno kliknuto na izbornik ali na razmak izmeðu polja ali ui manager automatski prestane slati xy izborniku pa se treba ponovno prijaviti
		                 uiMan.setOznacenSam(this);
	             }
	  
	     }
}
private void nacrtajProzor888(){
	// prvo se crta podloga prozora koja se treba nalaziti u prvoj slici u sprateu, u nultoj je spremljena slika za animaciju uvoda
	sprHend.nacrtajSprite(can, 1,0, 0,new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY),paintProzor);
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
						new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*s,tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*r,
								tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*(s+1),tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*(r+1)));
			      }
			      
			  else{ // u sluèaju da se botunu ne koriste crtaju se sa paintom koji je providan i crno bijeli
				     sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,d-1, 0,
				      	new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*s,tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*r,
							tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*(s+1),tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*(r+1)),paintNekorBot);
		     	  }
			}
			else{// ako nepostoji lista onda se svi crtaju normalno
				  sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,d-1, 0,
							new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*s,tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*r,
									tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*(s+1),tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*(r+1)));
			}
		 s++;
		 d++;
		}
	 s=0;// vraæa stupce u nula	
	 r++;	
	}


}
private void nacrtajUvod888(){
	
}
//////////////neke cesto koristene privatne
private void iskljuciIzbornik(){
	pokrenutIzbornik=false;
	if( tempFaza!=null)  tempFaza.izbornikNaMeni(false);
}
private void nacrtajTextUnutarPravokutnika(float pozX,float pozY,float sir,float vis, TextPaint p, String text){
	int brClanova=text.length();
	////
	TextPaint tp=new TextPaint();
	tp.set(p);
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
	can.drawText(text, pozX, pozY+sir, tp);
}
private void izracVelProzora(){//proraèunnava vel prozora u odnosu na  gustoæu ekrana i njegovu velièinu i broj polja od  dotiènog tornja
	 if(velUCmPoljaX*tempBrStu*PpCmX+razIzmPoljUCmX*PpCmX*(tempBrStu+1)>sirEkr||// u sluèaju da je izbornik veæi od cijelog ekrana  trebalo bi ga namjestit da ispunjava cijeli ekran
			 velUCmPoljaY*tempBrRed*PpCmY+razIzmPoljUCmY*PpCmY*(tempBrRed+1)>visEkr){// dodaje na osnovnu velièinu polja i razmak izmeðu njih
		 
	 }
	 else{// ako ne nakjestiti mu velièinu u odnosu na gustoæu ekrana i predodreæenu velièinu polja 
		 tempKonVelX=velUCmPoljaX* tempBrStu*PpCmX+razIzmPoljUCmX*PpCmX*(tempBrStu+1);// dodaje i razmake izmeðu polja
		 tempKonVelY= tempBrRed*velUCmPoljaY*PpCmY+razIzmPoljUCmY*PpCmY*(tempBrRed+1);
		 tempXProz=tempFaza.getRect().centerX()- tempKonVelX;// postavlja izbornik lijevo od tornja
		 tempYProz=tempFaza.getRect().centerY()- tempKonVelY;
		 /////namjestanje ako prijeðe granice ekrana
		 if(tempXProz<0-pomCanX){// ako je  preblizu ruba ekrana pomièe se u nulu
			 tempXProz=0-pomCanX;
		 }
		 else if(tempXProz+tempKonVelX>sirEkr-pomCanX){// ako je desni kut prozora otišao izvan desne strane ekrana vreæea se prema lijevo koliko treba
			 tempXProz-=tempXProz+tempKonVelX-sirEkr-pomCanX;// pomièe se ulijevo za razliku koliko je prešao
		 }
		 if(tempYProz<0-pomCanY){// ako je  preblizu ruba ekrana pomièe se u nulu
			 tempYProz=0-pomCanY;
		 }
		 else if(tempYProz+tempKonVelY>visEkr-pomCanY){// ako je desni kut prozora otišao izvan desne strane ekrana vreæea se prema lijevo koliko treba
			 tempYProz-=tempYProz+tempKonVelY-visEkr-pomCanY;// pomièe se ulijevo za razliku koliko je prešao
		 }
		 //////
	 }
}
///////////////////////////////////////////////////////////////
////////////////////////PUBLIC METODE
public void izmjenilistuNekoristenih(LinkedList<Integer> listaNekoristenih){// u sluèju da se tijekom korištenja izbornik promijeni stanje, da se ta promjena prikže
	this.listaNekoristenih=listaNekoristenih;
}
public void pokreniMojIzbornik(Faza obj,LinkedList<Integer> listaNekoristenih){
	this.listaNekoristenih=listaNekoristenih;
	vrijPocetka=System.currentTimeMillis();// bilježi vrijeme kada je zatražen izbornik
	tempFaza=obj;// sprema koji je toranj zatražio izbornik
	frameUvoda=0;// postavlja animaciju uvoda na pocetak
	pokrenutIzbornik=true;
	prvPokTemIzb=true;
	uiMan.setOznacenSam(this);// postavlja seba kao oznaèenog u ui manageru tako da dobije podatke o xy 
	tempBrStu=2;
    tempBrRed=2;
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
	if(obj==this) {
		imTauchedZaIzbor=true;// ako je isti objekt na koj se kliknulo to znaèi da je korisnik izabrao polje ili promašio
	}
}
@Override
public void setTouchedXY(float x, float y) {
	// TODO Auto-generated method stub
	touchedX=x;
	touchedY=y;
	if(imTauchedZaIzbor==false){/// ova metoda se pokreæe uvijek kada se klikne na ekran i poziva se u objektu koji je oznaèen sa Imtouched, tako da znaèi ako  nije ponovno kliknnuto na izbornik moa da je kliknuto pokraj
		iskljuciIzbornik();
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
