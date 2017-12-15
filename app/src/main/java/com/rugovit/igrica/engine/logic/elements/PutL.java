package com.rugovit.igrica.engine.logic.elements;

import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class PutL implements GameLogicObject{
private float x,y,sir,vis;
private int redniBroj;
private float dx, dy;
private int indikator;
private boolean jesamLiInertniPut=false;
private RectF rec;
private boolean prvoPokretanje=true;
private GameLogic GL;
private boolean susjedGore=false;// oznaèavaju dali postoji cesta na nekoj strana s kojom se spaja ovvaj odjeljak
private boolean susjedDolje=false;
private boolean susjedDesno=false;
private boolean susjedLijevo=false;
public PutL(float x,float y, float sir, float vis, float dx, float dy, int indikator){
	this.x=x;
	this.y=y;
	this.sir=sir;
	this.vis=vis;
	this.dx=dx;
	this.dy=dy;
	this.indikator=indikator;
	rec=new RectF(x,y,x+sir,y+vis);// dodaje rect na korisniku je da uskladismijerove itd
	
}	
/// bilder metode
public void postaviRedniBroj(int b){
	this.redniBroj=b;
}
/////////////getersi
public float getDx(){
	return dx;
}
public float getDy(){
	return dy;
}

//////////// game logic interface metode
public float getXVelUPrikazu(){
	return 0;
}
public float getYVelUPrikazu(){
	return 0;
}
public int getOblZaKol(){
	return 1;// znaèi da je kvadrat
}
	@Override
	public int getIndikator() {
		// TODO Auto-generated method stub
		return indikator;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public RectF getRect() {
		// TODO Auto-generated method stub
		return rec;
	}

	@Override
	public void maliRun(GameLogic GL, FazeIgre FI, float TpS, boolean pause) {
		// TODO Auto-generated method stub
		this.GL=GL;
		if(prvoPokretanje){
			izvrsiSamoNaPocetku();
			prvoPokretanje=false;
		}   
	}
	///public metode
	public void setInertniPut(boolean jesam){
		jesamLiInertniPut=jesam;
	}
	public boolean jesamLiInertniPut(){
		return jesamLiInertniPut;
	}
	public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub){}
	public int redniBroj(){
		return this.redniBroj;
	}
	public boolean imamLiPutLijevo(){
		return susjedLijevo;
	}
	public boolean imamLiPutDesno(){
		return susjedDesno;
	}
	public boolean imamLiPutGore(){
		return susjedGore;
	}
	public boolean imamLiPutDolje(){
		return susjedDolje;
	}
	///privatne metode
	private void izvrsiSamoNaPocetku(){
		float tempDs=10;// za ovoliko æu poveæati rect tako da bi mogao detektirati koliziju i znati na kojoj se strani spajam sa drugim komaddom ceste
		/// proširuujje širinu smanjije visinu
		rec.set(rec.left-tempDs,rec.top+tempDs,rec.right+tempDs,rec.bottom-tempDs);// prvo poveæavam širinu pa onda visinu da nebi doslo do preklapanja na o
		GameLogicObject[] listaSudara=GL.provjeriKoliziju(this);                      
		rec.set(rec.left+tempDs,rec.top-tempDs,rec.right-tempDs,rec.bottom+tempDs);
		
		if(listaSudara!=null)
			for(int i=0; listaSudara.length>i;i++){
			if(listaSudara[i]!=null){
		    if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
			 if(listaSudara[i].getRect().right-tempDs<rec.left) this.susjedLijevo=true;// dodajem im ds u sluèaju da se preklapaju
			 if(listaSudara[i].getRect().bottom-tempDs<rec.top) this.susjedGore=true;
			 if(listaSudara[i].getRect().top+tempDs>rec.bottom) this.susjedDolje=true;
		     if(listaSudara[i].getRect().left+tempDs>rec.right) this.susjedDesno=true;
			 }
		

			}
		}
		///poveæava visinu smanjuje širinu
		rec.set(rec.left+tempDs,rec.top-tempDs,rec.right-tempDs,rec.bottom+tempDs);
	    listaSudara=GL.provjeriKoliziju(this);
		rec.set(rec.left-tempDs,rec.top+tempDs,rec.right+tempDs,rec.bottom-tempDs);
		if(listaSudara!=null)
			for(int i=0; listaSudara.length>i;i++){
			if(listaSudara[i]!=null){
		  if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
			 if(listaSudara[i].getRect().right-tempDs<rec.left) this.susjedLijevo=true;
			 if(listaSudara[i].getRect().bottom-tempDs<rec.top) this.susjedGore=true;
			 if(listaSudara[i].getRect().top+tempDs>rec.bottom) this.susjedDolje=true;
			 if(listaSudara[i].getRect().left+tempDs>rec.right) this.susjedDesno=true;
			 }
			}
     
		}
		
	}
	@Override
	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public float getXPozUPrik() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public float getYPozUPrik() {
		// TODO Auto-generated method stub
		return 0;
	}
}
