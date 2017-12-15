package com.rugovit.igrica.engine.ui.elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;

public class PutP implements UIManagerObject {
	private float x,y,sir,vis;
	private Paint p;
	private Canvas can;
	
	int redniBroj=0;
	public PutP(float x, float y, float sir, float vis){
		this.x=x;
		this.y=y;
		this.sir=sir;
		this.vis=vis;
		p=new Paint();
		p.setARGB(100,100,200,200);
		p.setStyle(Paint.Style.FILL);
	}
 
	////////UI manager metode

	@Override
	public void nacrtaj(Canvas can, float fps, UIManager uiMan, float PpCmX, float PpCmY, float pomCanX, float pomCanY) {
		// TODO Auto-generated method stub
	    this.can=can;
	    /*
	    can.drawRect(x, y, x+sir, y+vis, p);
		TextPaint tp=new TextPaint();
		tp.set(p);
		
		nacrtajTextUnutarPravokutnika(x,y, sir, vis,  tp,String.valueOf(this.redniBroj));*/
	}
	public void postaviRedniBroj(int b){
		this.redniBroj=b;
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
		return null;
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
