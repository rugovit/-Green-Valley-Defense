package com.rugovit.igrica.engine.ui;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;

public interface UIManagerObject {
///////////	setersi
public void GameLinkerIzvrsitelj(GameEvent e);
public void setDvojnikaULogici(GameLogicObject obj);
public void nacrtaj(Canvas can,float fps,UIManager uiMan, float PpCmX,float PpCmY,float pomCanX,float pomCanY );
public void setImTouched(boolean b); //kad se dotakne neki objekt
//public void setBoolZaSlanje(boolean b);
public void setTouchedObject(UIManagerObject obj);
public void setTouchedXY(float x,float y);
public void onSystemResume();// sluzi za ponovno ubacivanje referenci za slike pošto ih planiram maknuti kada ode u backroud
public RectF getGlavniRectPrikaza();
//////////getersi
public boolean getDaliDaIgnoriramTouch();
public int getIndikator();
public float getX(); 
public float getY();
public float getSir();
public float getVis();
}
