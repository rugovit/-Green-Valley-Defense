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
private boolean pokrenutIzbornik=false;// ozna�ava dali je jedan od tornjeva pokrenuo izbornik
private boolean prvPokTemIzb=false;// prvo pokretanje temp izbornika, slu�it �e najvi�e za to da nera�unna svaki put veli�inu izbornika
//////////////////////////	
//////////////KONSTANTE	
////Paintovi
private Paint paintProzor;
private Paint paintNekorBot;
///////
private float PpCmX;
private float PpCmY; 
private GameLogic GL;// napravit �u funkciju za pauzu
private double vrijUvoda;// vrijeme koliko traje uvod
private int indikator;
private SpriteHendler sprHend;
private Canvas can;
private int xProz,yProz;
private float razIzmPoljUCmX=0.3f;// razmak izme�u polja gdje �e biti dugmi�i, taj dio bi jedini trebao reagirati na dodir
private float razIzmPoljUCmY=0.3f;
private float velUCmPoljaX=1.5f;// velicina jednog polja za izbor u cm, poslije se prora�unnava veli�ina prozura u pikselima u odnosu na gusto�u ekrana
private float velUCmPoljaY=1.5f;
private int sirEkr;// visina i �irina ekrana
private int visEkr;
private UIManager uiMan;
////////////////////////
///////////VARIJABLe
private float pomCanX=0,pomCanY=0;
private LinkedList<Integer> listaNekoristenih;// sprema listu nekoristenih polja, ona �e biti zamagljena i reagirat �e kao da je kliknuta izvan polja
private int frameUvoda=0;
private float touchedX;// sprema se gdje je izbornik dodirnut
private float touchedY;
private float tempXProz,tempYProz;// xy prozora prora�unava se posebno za svaki toranj pri pozivu od tornja
private float tempKonVelX, tempKonVelY;/////kona�na veli�ina prozora nakon  �to se odigrao uvod
private Faza tempFaza;// trenutni korisnik klase
private int tempBrSlikeUSpriteu;// imat �e vi�e slikka u svakom spriteu za ovu klasu, po jedna za svaki tip tornja
private int tempBrStu,tempBrRed;// odre�uje se broj redova i stupaca radi stvaranja pravilne geometrije za dodir 
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
			 izracVelProzora();// ra�una v rrijednosti veli�ine prozora koje koriste ostale funkcije koje slijede, pokre�e se samo jedanput pri pozivanju izbornika radi u�tede ciklusa
		 }
	     if(vrijPocetka+vrijUvoda>System.currentTimeMillis()){// ako je trenuta�no vrijeme manje od trenutka kada bi trebao zavr�it uvod
		      nacrtajUvod888();
	           }
	     else{
	    	if(imTauchedZaIzbor) {
	    		reagirajNaDodir888();//imTauchedZaIzbor se postavlja kroz  tauchedobject i ako je isti kao ova klasa to zna�i da je kliknuto na prozor i treba se vidjeti jeli izabrano polje
	    		imTauchedZaIzbor=false;
	    	     }
	    	 nacrtajProzor888();
	         }
		 prvPokTemIzb =false;// postavlja ponovno da se nera�una veli�ina izbornika tijekom animacije
	   }
	 else {// u slu�aju da nitko netra�i izbornik njegove geometrijske veli�ine se postavljaju na vrijenost koja ne�e smetati ostatju igrice
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
		int s=0;// broja� stupca
		int r=0;// brojja� retka
		int polje;// polje tj broj dugmi�a
		boolean nasaRed=false;
		boolean nasaStu=false;
		/////////////tra�enje
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
		                     break;// izlazi iz petlje kada na�e i istovremeno mu ostaje zapam�en broj stupaca
		                 }
		            s++;	
		      }
	         if(nasaRed&&nasaStu){
		               polje=(tempBrStu*r)+s+1;
		               if(listaNekoristenih!=null){// u slu�aju da je korisnik izbornika ozna�io neka polja kao nekoristena provjerava se dali je kliknuto na to polje i reagira se kao da je kliknuto izvan polja
			                 if(listaNekoristenih.contains(polje)){// ako je kliknuto polje ozna�eno kao nekoris�etno reagira se kao da je kliknuto izvan polja
				                    uiMan.setOznacenSam(this);
			                     }
			                  else {
				                 pokrenutIzbornik=false;
				                 tempFaza.vratiTouchPolje(polje);//a ako ne, vra�a broj polja po�ev�i od  jedan
		 	                        }
		               }
		      else{
			           pokrenutIzbornik=false;
			           tempFaza.vratiTouchPolje(polje);// vra�a broj polja po�ev�i od  jedan
		             }
	               } 
	         else{// zbog toga �to ako primi imTouched to zna�i da je definitivno kliknuto na izbornik ali na razmak izme�u polja ali ui manager automatski prestane slati xy izborniku pa se treba ponovno prijaviti
		                 uiMan.setOznacenSam(this);
	             }
	  
	     }
}
private void nacrtajProzor888(){
	// prvo se crta podloga prozora koja se treba nalaziti u prvoj slici u sprateu, u nultoj je spremljena slika za animaciju uvoda
	sprHend.nacrtajSprite(can, 1,0, 0,new RectF(tempXProz,tempYProz,tempXProz+tempKonVelX,tempYProz+tempKonVelY),paintProzor);
   //onda se na to crtaju dugmi�i, koji su spremljeni u spriteu na slicu rednog broja tempBrSlikeUSpriteu
	int s=0;// broja� stupca
	int r=0;// brojja� retka
	int d=1;// brojac dugmi�a
	while(tempBrRed>r ){// inkrementira kroz redtke
		while(tempBrStu>s){// inkrementira kroz stupce
			if(listaNekoristenih!=null)//ako lista postoji onda se svaki9 pojedina�no provjerava dali je nekori�ten
			{	
			  if(!listaNekoristenih.contains(d)){// ako taj dugmi� nije u listi nekori�tenih
				   sprHend.nacrtajSprite(can, tempBrSlikeUSpriteu,d-1, 0,
						new RectF(tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*s,tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*r,
								tempXProz+razIzmPoljUCmX*PpCmX*(s+1)+velUCmPoljaX*PpCmX*(s+1),tempYProz+razIzmPoljUCmY*PpCmY*(r+1)+velUCmPoljaY*PpCmY*(r+1)));
			      }
			      
			  else{ // u slu�aju da se botunu ne koriste crtaju se sa paintom koji je providan i crno bijeli
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
	 s=0;// vra�a stupce u nula	
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
	tp.setTextSize(150);// namje�tam size na malo ve�u vrijednost radi int zaokru�ivanja veli�ine
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
private void izracVelProzora(){//prora�unnava vel prozora u odnosu na  gusto�u ekrana i njegovu veli�inu i broj polja od  doti�nog tornja
	 if(velUCmPoljaX*tempBrStu*PpCmX+razIzmPoljUCmX*PpCmX*(tempBrStu+1)>sirEkr||// u slu�aju da je izbornik ve�i od cijelog ekrana  trebalo bi ga namjestit da ispunjava cijeli ekran
			 velUCmPoljaY*tempBrRed*PpCmY+razIzmPoljUCmY*PpCmY*(tempBrRed+1)>visEkr){// dodaje na osnovnu veli�inu polja i razmak izme�u njih
		 
	 }
	 else{// ako ne nakjestiti mu veli�inu u odnosu na gusto�u ekrana i predodre�enu veli�inu polja 
		 tempKonVelX=velUCmPoljaX* tempBrStu*PpCmX+razIzmPoljUCmX*PpCmX*(tempBrStu+1);// dodaje i razmake izme�u polja
		 tempKonVelY= tempBrRed*velUCmPoljaY*PpCmY+razIzmPoljUCmY*PpCmY*(tempBrRed+1);
		 tempXProz=tempFaza.getRect().centerX()- tempKonVelX;// postavlja izbornik lijevo od tornja
		 tempYProz=tempFaza.getRect().centerY()- tempKonVelY;
		 /////namjestanje ako prije�e granice ekrana
		 if(tempXProz<0-pomCanX){// ako je  preblizu ruba ekrana pomi�e se u nulu
			 tempXProz=0-pomCanX;
		 }
		 else if(tempXProz+tempKonVelX>sirEkr-pomCanX){// ako je desni kut prozora oti�ao izvan desne strane ekrana vre�ea se prema lijevo koliko treba
			 tempXProz-=tempXProz+tempKonVelX-sirEkr-pomCanX;// pomi�e se ulijevo za razliku koliko je pre�ao
		 }
		 if(tempYProz<0-pomCanY){// ako je  preblizu ruba ekrana pomi�e se u nulu
			 tempYProz=0-pomCanY;
		 }
		 else if(tempYProz+tempKonVelY>visEkr-pomCanY){// ako je desni kut prozora oti�ao izvan desne strane ekrana vre�ea se prema lijevo koliko treba
			 tempYProz-=tempYProz+tempKonVelY-visEkr-pomCanY;// pomi�e se ulijevo za razliku koliko je pre�ao
		 }
		 //////
	 }
}
///////////////////////////////////////////////////////////////
////////////////////////PUBLIC METODE
public void izmjenilistuNekoristenih(LinkedList<Integer> listaNekoristenih){// u slu�ju da se tijekom kori�tenja izbornik promijeni stanje, da se ta promjena prik�e
	this.listaNekoristenih=listaNekoristenih;
}
public void pokreniMojIzbornik(Faza obj,LinkedList<Integer> listaNekoristenih){
	this.listaNekoristenih=listaNekoristenih;
	vrijPocetka=System.currentTimeMillis();// bilje�i vrijeme kada je zatra�en izbornik
	tempFaza=obj;// sprema koji je toranj zatra�io izbornik
	frameUvoda=0;// postavlja animaciju uvoda na pocetak
	pokrenutIzbornik=true;
	prvPokTemIzb=true;
	uiMan.setOznacenSam(this);// postavlja seba kao ozna�enog u ui manageru tako da dobije podatke o xy 
	tempBrStu=2;
    tempBrRed=2;
   /* /////////////case-switch izme�u raznik tipa tornjeva, provjeravat �e indikator i puniti varijable koje �e koristiti ostatak klase
	switch(tempFaza.getIndikator()){
	case 101: tempBrSlikeUSpriteu=2;// po�inje od 2, nulta �e uvijek biti za podlogu,1 za animaciju uvoda, sve ostale �e se prikazivati na vrhu te podloge kao dugmi�i
	          tempBrStu=1;
	          tempBrRed=4;
	    break;
	case 200: tempBrSlikeUSpriteu=2;// po�inje od 2, nulta �e uvijek biti za podlogu,1 za animaciju uvoda, sve ostale �e se prikazivati na vrhu te podloge kao dugmi�i
              tempBrStu=1;
              tempBrRed=4;
        break;
	case 150: tempBrSlikeUSpriteu=2;// po�inje od 2, nulta �e uvijek biti za podlogu,1 za animaciju uvoda, sve ostale �e se prikazivati na vrhu te podloge kao dugmi�i
              tempBrStu=1;
              tempBrRed=4;
        break;          
	default: // u slu�aju da nepostoji nijedan takav u switch-caseu   
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
		pokrenutIzbornik=true;//vra�a zastavice u po�etno stanje tako da se izbornik ugasi
	     }
	else {// u slu�aju da je kliknuto pokraj prozora izbornika uiMnager vra�a false
		pokrenutIzbornik=false;//vra�a zastavice u po�etno stanje tako da se izbornik ugasi
	}
}
@Override
public void setTouchedObject(UIManagerObject obj) {// ako primi isti touched object kao i on onda to zna�i da se kliknulo u podru�je izbornika
	// TODO Auto-generated method stub
	if(obj==this) {
		imTauchedZaIzbor=true;// ako je isti objekt na koj se kliknulo to zna�i da je korisnik izabrao polje ili proma�io
	}
}
@Override
public void setTouchedXY(float x, float y) {
	// TODO Auto-generated method stub
	touchedX=x;
	touchedY=y;
	if(imTauchedZaIzbor==false){/// ova metoda se pokre�e uvijek kada se klikne na ekran i poziva se u objektu koji je ozna�en sa Imtouched, tako da zna�i ako  nije ponovno kliknnuto na izbornik moa da je kliknuto pokraj
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
	return tempXProz;// naredne 4 varijable su va�ne jer bi ina�e bili izabrani objekti ispod izbornika i isto tako se nebi dobili x i y
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
