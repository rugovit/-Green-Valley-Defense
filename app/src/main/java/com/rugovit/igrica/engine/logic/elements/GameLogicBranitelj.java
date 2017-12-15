package com.rugovit.igrica.engine.logic.elements;

import com.rugovit.igrica.engine.ui.UIManagerObject;

import java.util.LinkedList;

public interface GameLogicBranitelj extends GameLogicObject{
	public GameLogicProtivnik getProtivnikaKojegNapadas();
	///setersi za helth
	
	public void setTipStete(int tipStet);
	public void dodajHelth(float deltaHelth);
	public void dodajArmor(float deltaArmor);
	////////setersi za grupni izbor
	public void setGrupniXY(float px, float  py);
	public void setGrupniXYSync(float px, float  py);
	public void setGrupniTouchObj(UIManagerObject po);
	public void setGrupnuListu(LinkedList<GameLogicBranitelj> listaG);
	///setersi
	public boolean lijeciMe(float helth,float armor);
	public void jaSamTeStvorio(ToranjL poziv);// salje referencu  branitelju od tornja koji  ga je stvorio
	public void setUdaracaSvakihSekundi(float sekunde);
	public void setMrtavProtivnikPrestani();
	public void setPocetniXY(float x, float y);
}
