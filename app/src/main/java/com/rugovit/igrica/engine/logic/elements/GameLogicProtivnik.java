package com.rugovit.igrica.engine.logic.elements;

import android.graphics.RectF;

public interface GameLogicProtivnik extends GameLogicObject {
	////////setersi za helth
	public void setTipStete(int tipStet);// sljedeæe 4 metode služe za namještanje karakterristika objekta
	public void dodajHelth(float deltaHelth);
	public void dodajArmor(float deltaArmor);
	public boolean lijeciMe(float helth,float armor);
	////////setersi
	public void setJesamLiImunNaBranitelje(boolean b);
	public void setJesamLiImunNaToranjMinobacac(boolean b);	

	public boolean getJesiLiZiv();
	public void setUdaracaSvakihSekundi(float secunde);
	public void setViseTeNeNapadam(GameLogicBranitelj obj);
	public void setJaTeNapadam(GameLogicBranitelj obj); 
	///////////getersi
	 public boolean jesamLiNaInertnomPutu();
	public boolean jesamLiImunNaBranitelje();
	public boolean jesamLiImunNaToranjMinobacac();
	public int getBrojNapadaca();
	public int getRedBrPutaNaKojemSi();
    ////////geters za vrijednost 
	public float getVrijednostProtivnika();
	public boolean jesamLileteci();
	public RectF getRecLeteceg(); 
	}
