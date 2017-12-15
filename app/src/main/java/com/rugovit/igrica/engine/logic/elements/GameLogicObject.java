package com.rugovit.igrica.engine.logic.elements;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public interface GameLogicObject {
	////Getersi
	public int getOblZaKol();//vraca tipu objekta za koliziju 1)kvadrat 2)krug 3)možda slika
	public int getIndikator();
	public float getX();
	public float getY();
	public float getDx();
	public float getDy();
	public float getXVelUPrikazu();
	public float getYVelUPrikazu();
	public float getXPozUPrik();
	public float getYPozUPrik();
	public RectF getRect();
  //////mali run
	public void maliRun(GameLogic GL, FazeIgre FI, float ticksPerSec, boolean pause);
	////setersi
	public void setDvojnikaUPrikazu(UIManagerObject obj);
	public void GameLinkerIzvrsitelj(GameEvent e);
	/////////
	public void setStetu(int tip,float helth,float armor, float vrijUsp, float postUsp, float vrijHelthGub, float helthPoSecGub, float vrijArmorGub, float armorPoSecGub);
	

}