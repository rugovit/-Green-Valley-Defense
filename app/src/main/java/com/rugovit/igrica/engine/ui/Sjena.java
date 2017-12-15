package com.rugovit.igrica.engine.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;

public class Sjena implements UIManagerObject {
	private boolean crtaj=true;
	private boolean oznacenSam=false;
	private boolean jesamLiZiv=true;
	private float x,y,sir,vis, stroke;
	private Paint p,p2,pOznacen;
	private Canvas can;
	int redniBroj=0;
	private RectF rec,rec2, recOznacen;
	private int indikatorStanja=0;
	private UIManager uiMan;
	private Path oval;
	private GameEvent geAlfaKaskad=new GameEvent(this),geAlfaPoj=new GameEvent(this);
	private SpriteHendler spriteZaZajednickeEfekte;	
	private float trajanjeAnimacijeLokve=0;
	public Sjena( float sir, float vis,int alpha,int read,int green,int blue){
		
		this.sir=sir;
		this.vis=vis;
		p=new Paint();
		rec=new RectF();
		rec2=new RectF();
		recOznacen=new RectF();
		rec.set(x, y, x+sir, y+vis);
		p.setARGB(alpha, read,green, blue);
		p.setStyle(Paint.Style.FILL);
		p2=new Paint();
		p2.setARGB(alpha/2, read,green, blue);
		oval =new Path();
		oval.arcTo(rec, 0, 360);
		
	    oval.close();
	
		p2.setStyle(Paint.Style.STROKE);
		 stroke=(vis+vis)/70;
		 
		p2.setStrokeWidth(stroke);
		geAlfaKaskad.jesamLiZiv=true;
		
		pOznacen=new Paint();
		pOznacen.setStyle(Paint.Style.STROKE);
		pOznacen.setColor(Color.argb(255, 250, 173,56));
		pOznacen.setStyle(Paint.Style.FILL);


	}
 
	////////UI manager metode
    
	@Override
	public void nacrtaj(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY) {
		// TODO Auto-generated method stub
		this.can=can;
		this.uiMan=uiMan;
	  
	  if(!jesamLiZiv&&(indikatorStanja!=101||indikatorStanja!=100)) ubijMe();// dodao sam još i u slu�?aju da nije pokrenuta amimacija umiranja jer se onda sjena sam uništava po završetku animacije
	  else if(jesamLiZiv&&crtaj){ 
		  if(indikatorStanja==1){
			  if(this.oznacenSam)	{
				  recOznacen.set(rec.centerX()-4*sir/6, rec.centerY()-4*vis/6, rec.centerX()+4*sir/6, rec.centerY()+4*vis/6);
					pOznacen.setStyle(Paint.Style.STROKE);
					 pOznacen.setStrokeWidth(rec.width()/6);
					pOznacen.setShader(new RadialGradient(rec.centerX() ,rec.centerY() ,
							rec.width()*0.7f ,Color.argb(0, 250, 173,56), Color.argb(190, 250, 173,56), TileMode.CLAMP));
				  can.drawOval( recOznacen, this.pOznacen);
			  }
		    //can.drawOval(rec, p);
		    oval=new Path();
		    oval.arcTo(rec, 0,359);
		    oval.close();
		    uiMan.dodajSjenu(oval);
		 // oval.moveTo(rec.centerX(),rec.centerY());
		
	     // can.drawPath(oval, p);
	       }
	      else  if(indikatorStanja==2){
		                        can.drawOval(rec, p);
		                        can.drawOval(rec2, p2);
	      }
	     else if(indikatorStanja==100){// mrlja od smrti za branitelje
		    if(spriteZaZajednickeEfekte!=null){
			   if(spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(3)){
				   
	 				 if(spriteZaZajednickeEfekte.animacijaAlfaCijelogRedaKaskadnoVanjskoSpremanje(this.geAlfaKaskad,this.geAlfaPoj,3,0,trajanjeAnimacijeLokve,this.rec, can, 0))this.ubijMe();
				//  if( spriteZaZajednickeEfekte.animacijaAlfaCijelogRedaKaskadno(3,0,trajanjeAnimacijeLokve,this.rec, can, 0))this.ubijMe();
			
			   }
			   else this.ubijMe();
		    }
		   else this.ubijMe();
	     }
        else if(indikatorStanja==101){// mrlja od smrti za protivnike
    	   if(spriteZaZajednickeEfekte!=null){
 			  if(spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(4)){
 		
 				 if(spriteZaZajednickeEfekte.animacijaAlfaCijelogRedaKaskadnoVanjskoSpremanje(this.geAlfaKaskad,this.geAlfaPoj,4,0,trajanjeAnimacijeLokve,this.rec, can, 0))this.ubijMe();
 			 	 // if(spriteZaZajednickeEfekte.animacijaAlfaCijelogRedaKaskadno(4,0,trajanjeAnimacijeLokve,this.rec, can, 0))this.ubijMe();
 			  }
 			  else this.ubijMe();
 		   }
 		   else this.ubijMe();
	     }
		
		/*TextPaint tp=new TextPaint();
		tp.set(p);
		
		nacrtajTextUnutarPravokutnika(x,y, sir, vis,  tp,String.valueOf(this.redniBroj));*/
	   }
	  else if(!jesamLiZiv) ubijMe();
	}
	////bilder////
	public void bilderDoddajDodatniSpriteZaZajednickeEfekte(SpriteHendler sprite){
	
			spriteZaZajednickeEfekte=sprite;
		
	}
	/////////// 
    private void ubijMe(){
    	this.uiMan.makniObjekt(this);
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
		return x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}

	@Override
	public float getSir() {
		// TODO Auto-generated method stub
		return sir;
	}

	@Override
	public float getVis() {
		// TODO Auto-generated method stub
		return vis;
	}
    public void namjestiVelicinuSjene(float sir,float vis){
    	this.sir=sir;
    	this.vis=vis;
    }
	@Override
 	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		oznacenSam=e.imTouched;
		x=e.x;
		y=e.y;
		crtaj=!e.teleportacija;
		trajanjeAnimacijeLokve=e.pomNaX;
		this.indikatorStanja=e.indikator;
		jesamLiZiv=e.jesamLiZiv;
		rec.set(x+e.x3*sir/100, y+e.x3*vis/100, x+sir-e.x3*sir/100, y+vis-e.x3*vis/100);
		rec2.set(rec.left+stroke/2, rec.top+stroke/2, rec.right-stroke/2, rec.bottom-stroke/2);
		
	}

	@Override
	public void setDvojnikaULogici(GameLogicObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RectF getGlavniRectPrikaza() {
		RectF rect=new RectF();
		rect.set(this.rec);
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

