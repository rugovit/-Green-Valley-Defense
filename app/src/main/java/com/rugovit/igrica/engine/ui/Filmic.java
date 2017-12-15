package com.rugovit.igrica.engine.ui;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.MapActivity;

public class Filmic implements UIManagerObject {
	 private String IDSlota;
	 private int brZvjezdica;
	 private int tezina;
	   public Canvas can; 
	   public Thread th; //tread od klase
	   private SpriteHendler slike;
	   private double  vrijemePrikaza[];
	   private int zadnjaPrikazana=0;
	   private double tempZadVrijeme=0;
	   private int brStaze;
	   private boolean tekPocSaCrt=true;
	   private RectF recCrt;
	   private MapActivity mAc;
	   private int tipIgre=0;
	   private String IDKoristeneFaze;
	   private boolean tekPoceo=true;
	 public Filmic(MapActivity ac,int brStaze,String IDKoristeneFaze,  String IDSlota,int brZvjezdica, int tezina, int tipIgre){
		 this.IDSlota=IDSlota;
		   this.brStaze=brStaze;
		   this.mAc=ac;  
		    this.brZvjezdica=brZvjezdica;
		   this.tezina=tezina;
		   this.tipIgre=tipIgre;
		   this.IDKoristeneFaze=IDKoristeneFaze;
		   recCrt= new RectF();
	   }
	@Override
	public void nacrtaj(Canvas can, float fps,
			UIManager uiMan, float PpCmX, float PpCmY, float pomCanX,
			float pomCanY) {
		// TODO Auto-generated method stub
		this.can=can;
		if(tekPoceo){
			uiMan.daliDaProvjeravamPovlacenje(false);
		    recCrt.set(0, 0, can.getWidth(), can.getHeight());// stavlja recCrt na cijeli ekran
			tekPoceo=false;
		}
		if(nacrtajUvod()) zavrsiSaUvodom();
	    
	}
/////////////BILDER///////////////////////////////////////////////
	public void dodajSlikuUvoda(SpriteHendler slike, double vrijeme[]){
		this.slike=slike;
		vrijemePrikaza=vrijeme;
	}
//////////////////////////////////////////////////////////////////	
	////privatne metode
	private void zavrsiSaUvodom(){
        if(this.mAc!=null) mAc.stvoriNovuFazu(brStaze,IDKoristeneFaze,IDSlota, brZvjezdica,  tezina,tipIgre);
		mAc=null;
	}
	private boolean nacrtajUvod(){
		boolean b=false;
		if(tekPocSaCrt){
			this.tempZadVrijeme=System.currentTimeMillis();
			tekPocSaCrt=false;
		}
		 if(slike!=null){  
			 if(slike.brojSlika()>this.zadnjaPrikazana){
		       if(this.tempZadVrijeme+this.vrijemePrikaza[zadnjaPrikazana]<System.currentTimeMillis()) {
			        this.slike.nacrtajSprite(can, zadnjaPrikazana, 0, 0, recCrt);
			        tempZadVrijeme=System.currentTimeMillis();
			        zadnjaPrikazana++;
		          }
		       else this.slike.nacrtajSprite(can, zadnjaPrikazana, 0, 0, recCrt);
		      }
		     else b=true;
		 }
		 else b=true;// prekida se iscrtavanje
		 return b;
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
	public void setImTouched(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTouchedObject(UIManagerObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTouchedXY(float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndikator() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getSir() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVis() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public RectF getGlavniRectPrikaza() {
		RectF rect=new RectF();
		rect.set(this.recCrt);
		return rect;
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
