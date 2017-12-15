package com.rugovit.igrica.engine.ui;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;

public class DodatakNaMapu implements UIManagerObject  {
	private boolean daliDaCrtamamoJedanput=false;
	private int brSlikeNaKojojCuStati;
	private boolean daliDaIgnoriramTouch=false;
	private Random generator=new Random();
	private boolean poceoPustati=false;
	private float sansa=0;
	private double prosloRandomVrijeme=0;
	private boolean nacrtajGlavniRandom=false;
	 private float xDod;
	  private float yDod;
	  private   float sirDod;
	  private float ySortir;
	  private  float visDod;
	  private  SpriteHendler spriteHend;
	  private  int indikator;
	  private RectF recCrtanjaGl;
	  private Paint paintGl;
	  private Canvas can;
	  private boolean tekPoceo=true;
	private UIManager uiMan;
	private  int indikatorSpritea;
	  private GameEvent ge= new GameEvent(null);
	  private GameLogicObject objDvojnik;
	  private Paint p;
	public DodatakNaMapu (float xDod, float yDod,float sirDod,float visDod,int indikatorSpritea,int pocmiOdStupca,int indikator ){
		this.xDod=xDod;
		this.   yDod= yDod;
		ySortir= yDod;
		this.  sirDod=sirDod;
		this. visDod=visDod;
		this.  spriteHend=spriteHend;
		this.indikatorSpritea=indikatorSpritea;
		recCrtanjaGl=new RectF();
		recCrtanjaGl.set(xDod, yDod, xDod+sirDod, yDod+visDod);
	    paintGl=new Paint();
	    ge.jesamLiZiv=true;
	    this.ge.indikator=pocmiOdStupca;
	    p=new Paint();
		p.setARGB(100,200,100,200);
		p.setStyle(Paint.Style.FILL);
	}
	
	////BILDER
	public void postaviCrtamJedanputIStanemNaSlici(int brSlikeNaKojojCuStati){
		daliDaCrtamamoJedanput=true;
		this.brSlikeNaKojojCuStati=brSlikeNaKojojCuStati;
	}
	public void setDaliDaIgnoriramTouch(boolean ignoriraj){
		daliDaIgnoriramTouch= ignoriraj;
	}
	public void postaviPomakSortiranja(float pomak){
		ySortir+=pomak;
	}
	public void postaviRandomCrtanje(float sansa){
		this.sansa=sansa;
		nacrtajGlavniRandom=true;
	}
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDvojnikaULogici(GameLogicObject obj) {
		// TODO Auto-generated method stub
		objDvojnik=obj;
	}

	@Override
	public void nacrtaj(Canvas can, float fps,
			UIManager uiMan, float PpCmX, float PpCmY, float pomCanX,
			float pomCanY) {
		    this.can=can;
		    this.uiMan=uiMan;
		//  nacrtajTestPodatke();
		    if(tekPoceo){
		         stvariKojeSeIzvrsavajuSamoNaPOcetku();
		         tekPoceo=false;
		    }
		   if(!nacrtajGlavniRandom) this.nacrtajGlavni();
		   else 	this.nacrtajGlavniRandom();
		 
			
			
		// TODO Auto-generated method stub
		
	}
    ///PRIVATNE METODE
	private void nacrtajTestPodatke(){
		TextPaint tp=new TextPaint();
		tp.set(p);
		 //can.drawRect(xDod, yDod, xDod+sirDod, yDod+visDod, p);
		 can.drawRect(objDvojnik.getRect(), p);
		  nacrtajTextUnutarPravokutnika(xDod,yDod, sirDod, visDod,  tp,String.valueOf(this.indikatorSpritea));
	}
	private void stvariKojeSeIzvrsavajuSamoNaPOcetku(){
		spriteHend=this.uiMan.GL.faIgr.getSprite(indikatorSpritea);
        ge.jesamLiZiv=true;
	}
	private void nacrtajGlavni(){
		if(spriteHend!=null)if(spriteHend.postojiLiSlikNaMjestu(0)){
			if(spriteHend.brojStupaca(0)==1){
				this.spriteHend.nacrtajSprite(can, 0, 0, 0,this.recCrtanjaGl, paintGl);
			}
			else if(this.daliDaCrtamamoJedanput&&ge.indikator==this.brSlikeNaKojojCuStati){
				this.spriteHend.nacrtajSprite(can, 0, brSlikeNaKojojCuStati, 0,this.recCrtanjaGl, paintGl);
			}
			else{
				spriteHend.vrtiAnimacijuReda(can, ge, 0, 0,recCrtanjaGl, paintGl);
			}
		}
	}
	private void nacrtajGlavniRandom(){
		if(!poceoPustati){
			if(System.currentTimeMillis()>this.prosloRandomVrijeme+1000){
				if(randIzmedu(0,100)<=sansa){
					poceoPustati=true;
				}
				prosloRandomVrijeme=System.currentTimeMillis();
			}
		  }
		if(poceoPustati){
			if(spriteHend!=null)if(spriteHend.postojiLiSlikNaMjestu(0)){
			    if(spriteHend.brojStupaca(0)==1){
				    this.spriteHend.nacrtajSprite(can, 0, 0, 0,this.recCrtanjaGl, paintGl);
			      }
		    	else{
		    		poceoPustati=!spriteHend.vrtiAnimacijuReda(can, ge, 0, 0,recCrtanjaGl, paintGl);
		    		if(!poceoPustati){
		    			prosloRandomVrijeme=System.currentTimeMillis();
		    		}
			    }
		      }
	       }
		else  if(spriteHend.postojiLiSlikNaMjestu(0)) this.spriteHend.nacrtajSprite(can, 0, 0, 0,this.recCrtanjaGl, paintGl);
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
		return this.xDod;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return this.ySortir;
	}

	@Override
	public float getSir() {
		// TODO Auto-generated method stub
		return this.sirDod;
	}

	@Override
	public float getVis() {
		// TODO Auto-generated method stub
		return this.visDod;
	}
	private void nacrtajTextUnutarPravokutnika(float pozX,float pozY,float sir,float vis, TextPaint p, String text){
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

	@Override
	public RectF getGlavniRectPrikaza() {
		// TODO Auto-generated method stub
		RectF rect=new RectF();
		rect.set(recCrtanjaGl);
		return rect;
	}

	@Override
	public void onSystemResume() {
		// TODO Auto-generated method stub
		spriteHend=this.uiMan.GL.faIgr.getSprite(indikatorSpritea);
	}
// cesto koristene metode
	private float randIzmedu(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak
		if(b==0) b=1;
		float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
	
		return r+a;
	 }

	@Override
	public boolean getDaliDaIgnoriramTouch() {
		// TODO Auto-generated method stub
		return daliDaIgnoriramTouch;
	}
}
