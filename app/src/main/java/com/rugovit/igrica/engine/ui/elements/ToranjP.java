package com.rugovit.igrica.engine.ui.elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;


public class ToranjP implements UIManagerObject {
	private boolean emItouched=false;
	private boolean boolZaSlanje=false;
	private boolean izbornikUpaljen=false;
	private float xTor,yTor;
	private int indikator;
	private float rTor;
	private float sirTor,visTor;
	private RectF kvadratCrtanja;
	private GameEvent ge;
	private Canvas can;
	private GameLogicObject dvojnik;
	private SpriteHendler spriteHend;
	private UIManager uiMan;
	private boolean jesamLiZiv=true;
	private boolean tekPoceo=true;
	private RectF rectCrtanja;
	///privremnei
	Paint p;
	Paint pK;
	public ToranjP(int indikator){
		this.indikator=indikator;
	}
	public ToranjP(float xTor, float yTor,float rTor,float sirTor,float visTor,SpriteHendler spriteHend,int indikator ){
		this.rTor=rTor;
		this.yTor=yTor;
		this.xTor=xTor;
		this.visTor=visTor;
		this.sirTor=sirTor;
		this.indikator=indikator;
		this.spriteHend=spriteHend;
		this.uiMan=uiMan;
		///privremeni
		p=new Paint();
		p.setARGB(50,150,150,50);
		p.setStyle(Paint.Style.FILL);
      
		pK=new Paint();
		pK.setARGB(240,150,0,150);
		pK.setStyle(Paint.Style.FILL);
		rectCrtanja=new RectF();
	}
/////////metode od interfacea object linker prikaz/////////
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		if(ge==null) ge=e;
		izbornikUpaljen=ge.izbornikUpaljen;
		if(!e.jesamLiZiv){
			jesamLiZiv=false;
		}
	}
/////////////////////////////////////////////////////	
/// privatne metode
	private void stvariKojeSeIzvrsavajuSamoNaPocetku(){
		
		if(indikator==200){
			rectCrtanja.set(xTor, yTor, xTor+sirTor, yTor+visTor);
		  }
		else if(indikator==150){
			rectCrtanja.set(xTor, yTor-visTor/2, xTor+sirTor, yTor+visTor);

		}
		else if(indikator==101){
			rectCrtanja.set(xTor, yTor-3*visTor/4, xTor+sirTor, yTor+visTor);

		}
	}
	private void nacrtajPripadajuciToranj(){
		if(indikator==200){
		      spriteHend.nacrtajSprite(can, 0, 0, 0, rectCrtanja);
		  }
		else if(indikator==150){
			spriteHend.nacrtajSprite(can, 0, 0, 0, rectCrtanja);
		}
		else if(indikator==101){
			spriteHend.nacrtajSprite(can, 0, 0, 0, rectCrtanja);
		}
	}
	private void ubijMe(){
		 uiMan.makniObjekt(this);// skida sa liste u UI manageru
	}
private void posaljiPorukuLogici888(){
	if(boolZaSlanje&&ge!=null){// kad se prvi put pokrene možda neæe biti spremljen u ovu referencu game event
		 ge.imTouched= emItouched;
	     dvojnik.GameLinkerIzvrsitelj(ge);
	     boolZaSlanje=false;
	}
}	
///////NACRTAJ
@Override
public void nacrtaj(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY) {
	// TODO Auto-generated method stub
	this.can=can;
	this.uiMan=uiMan;
	if(tekPoceo){
		stvariKojeSeIzvrsavajuSamoNaPocetku();
		tekPoceo=false;
	}
	if(indikator!=200&&this.izbornikUpaljen){
                    
	                 // can.drawCircle(xTor+sirTor/2,yTor+visTor/2,rTor, p);
	                  }
	nacrtajPripadajuciToranj();
	posaljiPorukuLogici888();// stavio sam da svaki šalje poruku svom parnjaku u logici, inaèe je bilo  u metodama (imtouched...)koje zapravo poziva thread od toucha 
    if(jesamLiZiv==false) ubijMe();
}
//////////metode od interfacea ui manager object////
@Override
public void setImTouched(boolean b) {
	// TODO Auto-generated method stub
	 emItouched=b;
	 boolZaSlanje=true;
}

@Override
public void setTouchedObject(UIManagerObject obj) {
	// TODO Auto-generated method stub
	
}

@Override
public void setTouchedXY(float x, float y) {
	// TODO Auto-generated method stub
	if(ge!=null){
		ge.pomNaX=x;
		ge.pomNaY=y;/// koristim pomak na metodu iako nije najbollje ime ona korisit baš za slanje na koju je toèku kliknuto
	    dvojnik.GameLinkerIzvrsitelj(ge);
	    boolZaSlanje=true;
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
	return xTor;
}

@Override
public float getY() {
	// TODO Auto-generated method stub
	return yTor;
}


@Override
public float getSir() {
	// TODO Auto-generated method stub
	return sirTor;
}

@Override
public float getVis() {
	// TODO Auto-generated method stub
	return visTor;
}  ///ovdje završavaju metode od ui manager object
///////////////////////////////////////////////////////////
@Override
public void setDvojnikaULogici(GameLogicObject obj) {
	// TODO Auto-generated method stub
	dvojnik=obj;
}
@Override
public RectF getGlavniRectPrikaza() {
	RectF rect=new RectF();
	rect.set(this.rectCrtanja);
	return rect;
}
@Override
public void onSystemResume() {
	// TODO Auto-generated method stub
	spriteHend=this.uiMan.GL.faIgr.getSprite(this.indikator);
}
@Override
public boolean getDaliDaIgnoriramTouch() {
	// TODO Auto-generated method stub
	return false;
}
}
