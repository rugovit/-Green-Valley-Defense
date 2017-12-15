package com.rugovit.igrica.engine.logic;

import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManagerObject;

public class GameEvent {
	public float trimSjeneX, trimSjeneY, trimTouchPolY,trimTouchPolX;
	public float float1,float2,float3;
	public boolean zastavica1=false,zastavica2=false;
	public double vrijeme1, vrijeme2;
	public boolean zaleden=false, gorim=false, otrovan=false,teleportacija=false,medic=false,izlazakIzZemlje=false;
	public float helth=0;
	public float armor=0;
	public boolean bool1=false;
	public boolean jesamLiZiv=true;
	public boolean izbornikUpaljen=false;
	public boolean imTouched=false;
	public int vrijemePauze=0;
	public int indikator=0;
	public int indikator2=0;
	public SpriteHendler sprite;
	public int indikatorBorbe;// 0.ne bori se, 1 bori se na desno, 2 bori se na lijevoi, 3 udarac na deesno, 4 udarac na lijevo
	//// koristi se i za projektil 0- stoji, 1- ispaljivanje , 2-let, 3-pogodak
	public float x=-1,y=-1,x2=0,y2=0,x3=0,y3;
	public float pomNaX=-1,pomNaY=-1;//-1 sam uzeo kao znak da je prazna varijabla 
	public UIManagerObject touchedObj;
	public GameLogicObject objektLogike;// vazno je nulirati ovu varijablu odmah poslije primanja radi garbge colectora
	public GameEvent(Object source){

		
	}
	///seters
	/*public  synchronized void setIndikatorBorbe(int i){
		this.indikatorBorbe=i;
	}
	public void setIzbornikUpaljen(boolean jeliUpaljen){
		izbornikUpaljen=jeliUpaljen;
	}
	public void setHelth(float helth){// trebao bi poslati vrijednost u postotcima
		this.helth=helth;
	}
	public void setMrtviSmo(){ jesamLiZiv=false;}
	public void setImTouched(boolean b){// namješta se kada korisnik izabera "ovaj" objekt
		imTouched=b;
	}
	public void setTouchedObj(UIManagerObject obj){
		touchedObj=obj;  //sprema objek na koji je kliknuto
	}
	public void setXY(float x, float y){   ///za slanje xy koordinata objektu prikaza a možda i nazad za nekakav touch ili nešto takvo
		this.x=x;
		this.y=y;
	}
	public void setXYekrana(float xe,float ye){
		xEkra=xe;
		yEkra=ye;
	}
	public void setPomakniNaXY(float px,float py){//sprema koordinate na koje se treba pomaknuti 
		pomNaX=px;
		pomNaY=py;
	}
	///geters
	public int getIndikatorBorbe(){
		return this.indikatorBorbe;
	}
	public boolean izbornikUpaljen(){
		return izbornikUpaljen;
	}
	public float getHelth(){
		return helth;
	}
	public boolean jesmoLiZivi(){return jesamLiZiv;}// trebalo bi prikazni dio provjeravat prije nego krene sa èitanjem ostalih vrijednosti
	public boolean getEmITouched(){// vraæa jeli on izabran od korisnika
		return imTouched;
	}
	public float getX(){  ///namještanje koordinata
		return x;
	}
	public float getY(){
		return y;
	}
	public float getXekrana(){
		return xEkra;
	}
	public float getYekrana(){
		return yEkra;
	}
	public float getPomNaX(){return pomNaX;}// vraæa x koordinatu na kju se treba pomaknuti objekt igre
	public float getPomNaY(){return pomNaY;}// vraæa y koordinatu na kju se treba pomaknuti objekt igre
	public UIManagerObject getTouchedObj(){ return touchedObj;}*/
}
