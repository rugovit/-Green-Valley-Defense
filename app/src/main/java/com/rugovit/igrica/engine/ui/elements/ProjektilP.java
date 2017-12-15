package com.rugovit.igrica.engine.ui.elements;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.logic.elements.ProjektilL;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;

public class ProjektilP  implements UIManagerObject {
	private GameEvent geIspaljiv=new GameEvent(null);
	
	private float postotakVremenaPocetkaAlfaSegmenta;
	
    private Paint p,paintZaRep;
	private SpriteHendler projektil;
	private float udaljDoCilja;
	private float prosliIgnoriraniPutRepa;
	private int indikator;
	private GameEvent ge;
	/////REP VARIJABLE
	  private  int krajRepa;// koji je dio arraysa kraj repa , to æe se mjenjati kako se projektil kreæe kroz zrak, iæi æe u krug 
      private int poceRepa; 
      private int indikatorBorbe;
	   //iscrtavanje 
        //mrlja//
        private GameEvent eventMrlje=new GameEvent(this);
        private UIManagerObject mrlja;
        private boolean mrljaInicijalizirana=false;
        private boolean crtajMrlju=false;
        private float sirMrlje,visMrlje;
        private float vrijemeTrajanjaMrlje;
        /////////
        private float udaljPrestIscrLet=0;// udaljenost prestanka iscrtavanja leta mjereno od cilja
        private float fps, xCilj=0,yCilj=0;
        private float zadnjiKut;
        private float kutPromasaja;
        private boolean lomPoceo=false;// kad doce blizu kraja jedna ce dimenzija skociti na kraj stvarajuci lom kuta koji izgleda glupo ovo ce sprijecavati to
	    private boolean obicniRepa=false;
	    private boolean zrakaRep=false;
	    private int[] stupacNaKojemJeStao;
	    private double[] prosloVrijeme;
	    private double[] pocetakSegmenta;
	    private boolean pogSlijediCilj=false;
	    private boolean ctanjePogotkaSamkAkoJeNacinioStetu=false, crtajPogodakAkoStetaNacinjena=true; 
	    private boolean crtajPromasajIzSlikeLeta=false, pokreniCrtanjePromasaja=false;
	    private RectF recPromasajaRezanje;
	    private RectF recPromasajaCrtanje;
	    private float pixRezanjeSlikeLeta;
	    private double pocetakCrtanjaPromasaja;
	    private float duzinaCrtanjaPromasaja;
	    private GameLogicObject objCilja;
	    private RectF rectRepa;
	   // trajektorija
	    private float pocPolX;
	    private float pocPolY;
	    private float zadnjiXrepa;
	    private float zadnjiYrepa;
	    private double vrijZivSegmenta;
	    private  float[] polozajX;
	    private  float[] polozajY;
	    private  float[] kut;
	    private float udaljOkidanja;// tj. udaljenost nakon kje se stvara nova slièica repa i briše  zadnja
	///PROJEKTIL VARIJABLE
	    private float kutLeta=0;
	    private RectF recLet;
	    private RectF recPogotka;
	    private RectF recIspaljivanja;  
	    private float xPogotka ,xPromasaja,yPromasaja;
	    private float yPogotka;
	    private float xIspaljivanja;
	    private float yIspaljivanja;
	    private boolean postojiSlikaIspaljivanja=false;
	    private boolean postojiSlikaProjektila=false;
	    private boolean postojiSlikaRepa=false;
	    private boolean postojiSlikaPogotka=false;
	    private boolean konstantniRepIliObicni=true;
	////////////////////
	////stanja projektila
	private boolean zrakaTekAktivirana=true;// zastavica da se namjeste stanja kada se tek aktivira zraka
	private boolean zrakaAktivna=false;    
	private boolean ispaljivanje=false,praznjenje=false;
	private boolean let=false;
	private boolean pogodak=false;
	///////vremena za iscrtavanje
	private double prosVrijIspaljivanje=0;
	private double prosVrijLet=0;
	private double prosVrijPogodak=0;
	private double prosVrijRep=0;
	//////br slike na kojoj je stao
	private int brSlPogodtka=0;
	private int brSlLet=0;
	private int brSlIspaljivanje=0,brSlPunjenja=0,brSlNapunjen=0;
	//////////////////
	///genericki rep
	private float[] alfaStupca;
	private Paint genPaint;
	private boolean generickiRep=false;
	private float alfa=1;
	private float alfaKoef=1;
	///////////velicine tornja pozivatelja
	private float xVelTornjPoz;
	private float yVelTornjPoz;
	private float xPozTornjPoz;
	private float yPozTornjPoz;
	//////////  velicine frezz topa
    private float xVelTopa;
	private float yVelTopa;
	private float xPozTopa;
	private float yPozTopa;
	private boolean animPunjIliPra=true;
	///////////////
	private int vrijemePauze;
	private Canvas can;
	private ProjektilL dvojnik;
	private UIManager uiMan;
	private float dodatakY=0;
	private float yZaSortiranje=0;// služi za 
	private boolean zivSam=true;// služi ubjanje objekta
	private float xSvjezi,ySvjezi,x,y;
	private float  xPros,yPros,velIspaX,velIspaY, velLetX,velLetY,velPogodakX,velPogodakY;
	private boolean tekPoceo=true;
////////////////////KONSTRUKTOR
public ProjektilP(float x, float y, int indikator){
		this.x=x;
		this.y=y;
		this.indikator=indikator;	
		p=new Paint();
		p.setARGB(255,255,0,0);
		p.setStyle(Paint.Style.FILL);
		this.recIspaljivanja=new RectF();
		this.recPogotka=new RectF();
		this.recLet=new RectF();
	}
//////BILDER
public void crtanjePogotkaMrlja(float secTrajanjaMrlje, float sir, float vis, final float pomakOdTocEksplX, final float pomakOdTocEksplY){
	this.visMrlje=vis;
	this.sirMrlje=sir;
	this.vrijemeTrajanjaMrlje=secTrajanjaMrlje;
	this.mrljaInicijalizirana=true;
	
	 mrlja = new UIManagerObject(){
	    GameEvent gameEventZaVanjskoSpremanjeAnimacije=new GameEvent(this);
		private boolean kaskadnoIliSamoJedna=true;
		private float sir=sirMrlje, vis=visMrlje;
		private float  pomakOdTockEksplX=pomakOdTocEksplX, pomakOdTockEksplY=pomakOdTocEksplY;
		private Paint paint;
		private boolean pocmiCrtanjeMrlje=false;
		private RectF rectMrlje=new RectF(0,0,sir,vis);
		private UIManager uiMan;
		private void pocmiCrtatiMrlju(float xCentra,float yCentra){
			if(projektil.postojiLiSlikNaMjestu(4)){
				if(projektil.brojStupaca(4)!=1){
					projektil.resetirajTekPoceoAlfaRedaKaskadno(4,0);
					kaskadnoIliSamoJedna=true;
				}
                else {
                	gameEventZaVanjskoSpremanjeAnimacije.jesamLiZiv=true;
                	paint=new Paint();
                	kaskadnoIliSamoJedna=false;
				}
				
				rectMrlje.set( xCentra-rectMrlje.width()/2+pomakOdTockEksplX, yCentra-rectMrlje.height()/2+pomakOdTockEksplY,xCentra+rectMrlje.width()/2+pomakOdTockEksplX,yCentra+rectMrlje.height()/2+pomakOdTockEksplY);
				pocmiCrtanjeMrlje=true;
				
			}
			
		}
		@Override
		public void GameLinkerIzvrsitelj(GameEvent e) {
			// TODO Auto-generated method stub
			if(e.indikator==1){
				pocmiCrtatiMrlju(e.x,e.y);
				e.indikator=0;
			}
		}

		@Override
		public void setDvojnikaULogici(GameLogicObject obj) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void nacrtaj(Canvas can, float fps,
				UIManager uiMan, float PpCmX, float PpCmY, float pomCanX,
				float pomCanY) {
			this.uiMan=uiMan;
			// TODO Auto-generated method stub
			if(pocmiCrtanjeMrlje){
			  	if(kaskadnoIliSamoJedna)pocmiCrtanjeMrlje=!projektil.animacijaAlfaCijelogRedaKaskadno(4, 0, vrijemeTrajanjaMrlje,  rectMrlje, can, vrijemePauze);
			  	else{
			  		pocmiCrtanjeMrlje=!projektil.animacijaAlfaVanjskoSpremanje(gameEventZaVanjskoSpremanjeAnimacije,255, 0, paint,vrijemeTrajanjaMrlje, vrijemePauze);
			  		projektil.nacrtajSprite(can, 4, 0, 0,  rectMrlje, paint);
			  	}
			}
			
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
		public RectF getGlavniRectPrikaza() {
			// TODO Auto-generated method stub
			return null;
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
		public void onSystemResume() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean getDaliDaIgnoriramTouch() {
			// TODO Auto-generated method stub
			return false;
		}
		
	};
}
public void crtajPromasajIzSlikeLeta(float duzinaCrtanjaPromasajaSec, float postoRezanjaSlikeLeta){
	crtajPromasajIzSlikeLeta=true;
	this.duzinaCrtanjaPromasaja=duzinaCrtanjaPromasajaSec*1000;
	recPromasajaRezanje=new RectF();
	recPromasajaCrtanje=new RectF();
	this.pixRezanjeSlikeLeta=postoRezanjaSlikeLeta*this.velLetY/100;
	recPromasajaRezanje.set(0, 0, 100/this.projektil.brojStupaca(1), 100-postoRezanjaSlikeLeta);
}
public void crtanjePogotkaSamoAkoJeNacinioStetu(){
	ctanjePogotkaSamkAkoJeNacinioStetu=true;
}
public void stvoriRep(int brojSegmenata, double vrijZivSegmenta, float udaljOkidanja,float postoVrijPocAlfa, float sir, float vis,boolean konstantniRepIliObicni){
	stupacNaKojemJeStao= new int[brojSegmenata];
	prosloVrijeme=new double[brojSegmenata];
	pocetakSegmenta=new double[brojSegmenata];
	polozajX=new float[brojSegmenata];
	polozajY=new float[brojSegmenata];
	kut=new float[brojSegmenata];
	this.konstantniRepIliObicni=konstantniRepIliObicni;
	this.udaljOkidanja=udaljOkidanja;
	this.vrijZivSegmenta=vrijZivSegmenta;
	obicniRepa=true;// omoguæava se iscrtavanje repa
	this.rectRepa= new RectF(0,0,sir,vis);
	 this.postotakVremenaPocetkaAlfaSegmenta=postoVrijPocAlfa;
	 paintZaRep=new Paint();
}
public void stvoriGenerickiRep(int brojSegmenata, double vrijZivSegmenta, float udaljOkidanja, float sir, float vis,float alfaKoef,boolean konstantniRepIliObicni){
	stupacNaKojemJeStao= new int[brojSegmenata];
	prosloVrijeme=new double[brojSegmenata];
	pocetakSegmenta=new double[brojSegmenata];
	polozajX=new float[brojSegmenata];
	polozajY=new float[brojSegmenata];
	alfaStupca=new float[brojSegmenata];
	this.konstantniRepIliObicni=konstantniRepIliObicni;
	int i=0;
	while(alfaStupca.length>i){
		alfaStupca[i]=alfa;
		i++;
	}
	
	kut=new float[brojSegmenata];
	this.udaljOkidanja=udaljOkidanja;
	this.vrijZivSegmenta=vrijZivSegmenta;
	this.rectRepa= new RectF(0,0,sir,vis);
    generickiRep=true;
    this.alfaKoef=alfaKoef;
    genPaint=new Paint();
   
}
public void stvoriRepZraku(float sirSeg,float visSeg,int brojSegmenata, GameLogicObject objectPozivatelj, float xVelTopa, float yVelTopa,float xRelPozTopa,float yRelPozTopa){
	this.rectRepa=new RectF(0,0,sirSeg,visSeg);
	this.zrakaRep=true;
	 xVelTornjPoz=objectPozivatelj.getXVelUPrikazu();
	 yVelTornjPoz=objectPozivatelj.getYVelUPrikazu();
	 xPozTornjPoz=objectPozivatelj.getRect().centerX();
	 yPozTornjPoz=objectPozivatelj.getRect().centerY();
	 this.xVelTopa=xVelTopa;
	 this.yVelTopa=yVelTopa;
	 xPozTopa=objectPozivatelj.getXPozUPrik()+objectPozivatelj.getXVelUPrikazu()/2+xRelPozTopa;//- xVelTornjPoz/2;
	 yPozTopa=objectPozivatelj.getYPozUPrik()+objectPozivatelj.getYVelUPrikazu()/2+yRelPozTopa;//- yVelTornjPoz/2;
	stupacNaKojemJeStao= new int[brojSegmenata];
	prosloVrijeme=new double[brojSegmenata];
	pocetakSegmenta=new double[brojSegmenata];
	polozajX=new float[brojSegmenata];
	polozajY=new float[brojSegmenata];
	kut=new float[brojSegmenata];
	this.udaljOkidanja=udaljOkidanja;
	this.vrijZivSegmenta=1000000;// stavljam na ogroman broj tako da se stalno pušta
}

public void stvoriProjektil(float velIspaX,float velIspaY,float velLetX, float velLetY, float velPogodakX, float velPogodakY,
		SpriteHendler projektil,float udaljPrestIscrLet){
	this.udaljPrestIscrLet=udaljPrestIscrLet;
	this.velLetX=velLetX;
	this.velLetY=velLetY;
	this.velPogodakX=velPogodakX;
	this.velPogodakY=velPogodakY;
	this.projektil=projektil;
	this.velIspaX=velIspaX;
	this.velIspaY=velIspaY;
	
	this.recLet.set(x-velLetX/2, y-velLetY/2, x+velLetX/2,y+ velLetY/2);
}
public void stvoriProjektil(float velIspaX,float velIspaY,float velLetX, float velLetY, float velPogodakX, float velPogodakY,
		boolean pogSlijCilj,SpriteHendler projektil,float udaljPrestIscrLet){
	this.velLetX=velLetX;
	this.velLetY=velLetY;
	this.udaljPrestIscrLet=udaljPrestIscrLet;
	this.velPogodakX=velPogodakX;
	this.velPogodakY=velPogodakY;
	this.projektil=projektil;
	this.velIspaX=velIspaX;
	this.velIspaY=velIspaY;
	pogSlijediCilj=pogSlijCilj;
	this.recLet.set(x-velLetX/2, y-velLetY/2, x+velLetX/2,y+ velLetY/2);

}
//////////////
///////////privatne metode/////////////////////////////////

private void samoubojstvo(){// 
	 uiMan.makniObjekt(this);// skida sa liste u UI manageru
}
public void nacrtajLetVanjski(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){}
private void nacrtajLet(){
	if(this.postojiSlikaProjektila){
	 if(let){
			
	 // if(udaljPrestIscrLet<Math.abs(Math.hypot(this.xCilj-recLet.centerX(),this.yCilj-recLet.centerY()))){
		this.recLet.set(x-velLetX/2, y-velLetY/2, x+velLetX/2,y+ velLetY/2);
		//if(!lomPoceo) kutLeta=90+(float)this.izracunajKut(xPros,yPros, x, y);
		kutLeta=90+(float)this.izracunajKut(xPros,yPros, x, y);
		if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(1)+this.prosVrijLet+this.vrijemePauze){
		   projektil.nacrtajSpriteRotiran(can, 1, brSlLet, 0,recLet,  kutLeta, p);
		   brSlLet++;
		   prosVrijLet=System.currentTimeMillis();
		   if(projektil.brojStupaca(1)<=brSlLet) brSlLet=0;  
		}
		else    projektil.nacrtajSpriteRotiran(can, 1, brSlLet, 0,recLet,  kutLeta, p);
	    // }
		
		
		
	  }
	}
	 else let=false;
}
public void crtanjePogotkaVanjski(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){}
	

private void crtanjePogotka(){


	if(pokreniCrtanjePromasaja&&this.postojiSlikaProjektila){
		if(System.currentTimeMillis()<this.pocetakCrtanjaPromasaja+this.duzinaCrtanjaPromasaja+this.vrijemePauze){
			this.recPromasajaCrtanje.set(xPromasaja-this.velLetX/2, yPromasaja+pixRezanjeSlikeLeta-this.velLetY/2,xPromasaja+this.velLetX/2,yPromasaja+this.velLetY/2);
			projektil.nacrtjDioBitmapa(can, 1, this.recPromasajaRezanje,this.recPromasajaCrtanje,kutPromasaja, xPromasaja,yPromasaja+this.velLetY/2,p);

		}
		else pokreniCrtanjePromasaja=false;
	}
	else if(this.postojiSlikaPogotka){
	 if(pogodak&&crtajPogodakAkoStetaNacinjena){
		if(pogSlijediCilj&&objCilja!=null){
			recPogotka.set(objCilja.getRect().centerX()-velPogodakX/2, objCilja.getRect().centerY()-velPogodakY/2, objCilja.getRect().centerX()+velPogodakX/2,objCilja.getRect().centerY()+ velPogodakY/2);
		  }
	    else recPogotka.set(xPogotka-velPogodakX/2, yPogotka-velPogodakY/2, xPogotka+velPogodakX/2,yPogotka+ velPogodakY/2);
	    if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(2)+this.prosVrijPogodak+this.vrijemePauze){
		   
		   projektil.nacrtajSprite(can, 2, brSlPogodtka, 0,recPogotka,  p);
		   brSlPogodtka++;
		   prosVrijPogodak=System.currentTimeMillis();
			if(this.brSlPogodtka>=projektil.brojStupaca(2)){
                pogodak=false;
                brSlPogodtka=0;
                objCilja=null;
        }
		  }
		else   projektil.nacrtajSprite(can, 2, brSlPogodtka, 0,recPogotka,  p);

	 }
	 
	}
	 else pogodak=false;
	
}
public void crtanjeIspaljivanja(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){}
private void crtanjeIspaljivanja(){
	if(postojiSlikaIspaljivanja){
		
		if(ispaljivanje){
			recIspaljivanja.set(xIspaljivanja-this.velIspaX/2, yIspaljivanja-velIspaY, xIspaljivanja+velIspaX/2,yIspaljivanja);
			ispaljivanje=!projektil.nacrtajInternuAnimaciju(0, this.geIspaljiv, can, 0,this.recIspaljivanja,p);
	
		/*if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(0)+this.prosVrijIspaljivanje+this.vrijemePauze){
		   
		   projektil.nacrtajSprite(can, 0, brSlIspaljivanje, 0,recIspaljivanja,  p);
		   brSlIspaljivanje++;
		   prosVrijIspaljivanje=System.currentTimeMillis();
			if(this.brSlIspaljivanje>=projektil.brojStupaca(0)){
                ispaljivanje=false;
                brSlIspaljivanje=0;
     }
		}
		else   projektil.nacrtajSprite(can, 0, brSlIspaljivanje, 0,recIspaljivanja,  p);
		*/

	 }
   }
	else ispaljivanje=false;	
}
private void crtanjeGenerickogRepa(){
	if(this.generickiRep){	
	  dodavanjeNovihSegmenataRepa();
	  if(System.currentTimeMillis()>this.pocetakSegmenta[krajRepa]+ this.vrijZivSegmenta+this.vrijemePauze) {
	    	pocetakSegmenta[krajRepa]=0;// ovaj se yrray koristi za sve tako da je samo njega potrebno namjestit na nulu da se oznaèi da je prazno mjesto
	        krajRepa++;
	        if(krajRepa>=this.pocetakSegmenta.length) krajRepa=0;
	    }
	  int i=krajRepa-1;
	  boolean prosaJedanKrug=false;
	  while(true){
		 i++;
		 if(pocetakSegmenta[i]==0) break;
		 ///iscrtavanje
		 rectRepa.set(this.polozajX[i]-rectRepa.width()/2, this.polozajY[i]-rectRepa.height()/2,this.polozajX[i]+rectRepa.width()/2,this.polozajY[i]+rectRepa.height()/2);
		 if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(1)+prosloVrijeme[i]+this.vrijemePauze){
			if(i==this.poceRepa) alfaStupca[i]=alfa;
			alfaStupca[i]=alfaStupca[i]*alfaKoef;
			genPaint.setAlpha((int)alfaStupca[i]);
			this.projektil.nacrtajSpriteRotiran(can, 1, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], genPaint);
			this.prosloVrijeme[i]=System.currentTimeMillis();
			stupacNaKojemJeStao[i]=stupacNaKojemJeStao[i]+1;
		    if(stupacNaKojemJeStao[i]>=projektil.brojStupaca(1)) stupacNaKojemJeStao[i]=0;// nulta slika je za rep, prva æe biti za projektil
		
		 }
		 else this.projektil.nacrtajSpriteRotiran(can, 1, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], genPaint);
		 /////
		 if(i==krajRepa&&prosaJedanKrug) break;
		 if(pocetakSegmenta.length-1<=i){// ako dode do kraje ide ispocetka
			i=-1;
			prosaJedanKrug=true;
		}
		
	 }
	 zadnjiXrepa=polozajX[poceRepa];
     zadnjiYrepa=polozajY[poceRepa];
	 }
	
}
private void crtanjeRepaKonstantnog(){
	if(this.postojiSlikaRepa){
	  dodavanjeNovihSegmenataRepa();
	  if(System.currentTimeMillis()>this.pocetakSegmenta[krajRepa]+ this.vrijZivSegmenta+this.vrijemePauze) {
	    	pocetakSegmenta[krajRepa]=0;// ovaj se yrray koristi za sve tako da je samo njega potrebno namjestit na nulu da se oznaèi da je prazno mjesto
	        krajRepa++;
	        if(krajRepa>=this.pocetakSegmenta.length) krajRepa=0;
	    }
	  int i=krajRepa-1;
	  boolean prosaJedanKrug=false;
	  while(true){
		 i++;
		 if(pocetakSegmenta[i]==0) break;
		 ///iscrtavanje
		 rectRepa.set(this.polozajX[i]-rectRepa.width()/2, this.polozajY[i]-rectRepa.height()/2,this.polozajX[i]+rectRepa.width()/2,this.polozajY[i]+rectRepa.height()/2);
		 if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(3)+prosloVrijeme[i]+this.vrijemePauze){
			this.projektil.nacrtajSpriteRotiran(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], p);
			this.prosloVrijeme[i]=System.currentTimeMillis();
			stupacNaKojemJeStao[i]=stupacNaKojemJeStao[i]+1;
		    if(stupacNaKojemJeStao[i]>=projektil.brojStupaca(3)) stupacNaKojemJeStao[i]=0;// nulta slika je za rep, prva æe biti za projektil
		
		 }
		 else this.projektil.nacrtajSpriteRotiran(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], p);
		 /////
		 if(i==krajRepa&&prosaJedanKrug) break;
		 if(pocetakSegmenta.length-1<=i){// ako dode do kraje ide ispocetka
			i=-1;
			prosaJedanKrug=true;
		}
		
	 }
	 zadnjiXrepa=polozajX[poceRepa];
     zadnjiYrepa=polozajY[poceRepa];
	 
	}
	else obicniRepa=false; 
}
private void crtanjeRepa(){
	int j=0;
	if(this.vrijemePauze!=0){
		while(j>=pocetakSegmenta.length){
			
		if(pocetakSegmenta[j]!=0){
			pocetakSegmenta[j]+=vrijemePauze;
			prosloVrijeme[j]+=vrijemePauze;
		     }
		j++;
	
	   }
	}
	if(this.postojiSlikaRepa){
	  //dodavanjeNovihSegmenataRepa();
		
	
			        if(this.konstantniRepIliObicni)dodavanjeNovihSegmenataKonstantnogRepa();
		            else this.dodavanjeNovihSegmenataRepa();
		             
	/*  if(System.currentTimeMillis()>this.pocetakSegmenta[krajRepa]+ this.vrijZivSegmenta) {
	    	pocetakSegmenta[krajRepa]=0;// ovaj se yrray koristi za sve tako da je samo njega potrebno namjestit na nulu da se oznaèi da je prazno mjesto
	        krajRepa++;
	        if(krajRepa>=this.pocetakSegmenta.length) krajRepa=0;
	    }*/
	  int i=krajRepa-1;
	  boolean prosaJedanKrug=false;
	  while(true){
		 i++;
		 if(pocetakSegmenta[i]==0) break;
		 ///iscrtavanje
		 rectRepa.set(this.polozajX[i]-rectRepa.width()/2, this.polozajY[i]-rectRepa.height()/2,this.polozajX[i]+rectRepa.width()/2,this.polozajY[i]+rectRepa.height()/2);
		
		 float razlikaVremena=(float)(System.currentTimeMillis()-pocetakSegmenta[i]);
		 float postoVre=100*razlikaVremena/((int)vrijZivSegmenta);
		 if(postoVre<postotakVremenaPocetkaAlfaSegmenta){
			 paintZaRep.setAlpha(255);
		 }
		 else if(postoVre>postotakVremenaPocetkaAlfaSegmenta&&postoVre<100){
			 float postoAlfa=100*(100-postoVre)/(100-postotakVremenaPocetkaAlfaSegmenta);
			 paintZaRep.setAlpha((int)(postoAlfa*255/100));
		 }
		 else if(postoVre>100){
			 paintZaRep.setAlpha(0);
		 }
		 if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(3)+prosloVrijeme[i]+this.vrijemePauze){
			this.projektil.nacrtajSpriteRotiran(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i],paintZaRep);
			this.prosloVrijeme[i]=System.currentTimeMillis();
			stupacNaKojemJeStao[i]=stupacNaKojemJeStao[i]+1;
		    if(stupacNaKojemJeStao[i]>=projektil.brojStupaca(3)) stupacNaKojemJeStao[i]=0;// nulta slika je za rep, prva æe biti za projektil
		
		 }
		 else this.projektil.nacrtajSpriteRotiran(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], paintZaRep);
		 /////
		/* if(System.currentTimeMillis()>this.pocetakSegmenta[i]+ this.vrijZivSegmenta+this.vrijemePauze) {
		    	pocetakSegmenta[i]=0;
		 }*/
		 /////
		 if(i==krajRepa&&prosaJedanKrug) break;
		 if(pocetakSegmenta.length-1<=i){// ako dode do kraje ide ispocetka
			i=-1;
			prosaJedanKrug=true;
		}
		
	 }
	 zadnjiXrepa=polozajX[poceRepa];
     zadnjiYrepa=polozajY[poceRepa];
	 
	}
	else obicniRepa=false; 
////test/////*
	/*
	if(this.let){
	     float[][] lista=this.dvojnik.izracunavanjePolozajaUProslosti(50);
	     if( this.test!=null){
	    	 float [][] nova;
	    	 nova= new float [2][];
	    	 nova[0]= new float [test[0].length+lista[0].length] ;
	    	 nova[1]= new float [test[1].length+lista[1].length] ;
	    	 for(int i=0;i< nova[0].length;i++){
			      if(i<test[0].length){
			    	  nova[0][i]=test[0][i];
			      }
			      else {
			    	  nova[0][i]=lista[0][i-test[0].length];
			      }
		      }
	    	 for(int i=0;i< nova[1].length;i++){
			      if(i<test[0].length){
			    	  nova[1][i]=test[1][i];
			      }
			      else {
			    	  nova[1][i]=lista[1][i-test[1].length];
			      }
		      }
	    	 test=nova;
	     }
	     else  test=lista;
	     Paint p=new Paint();
		 p.setARGB(255, 50, 100, 40);
	     for(int i=0;i<test[0].length;i++){
		      can.drawCircle(test[0][i], test[1][i], this.recLet.width()/2, p);
	      }
	   }
	else test=null;*/
	////////////
}
private void crtanjeZrakaRepa(){
	if(postojiSlikaRepa){
		if(this.zrakaAktivna){
		 
		  polozajX[0]=this.pocPolX;// sprema centar
	      polozajY[0]=this.pocPolY;
	      dodavanjeNovihSegmenataRepaZaZraku();
		  if(System.currentTimeMillis()>this.pocetakSegmenta[krajRepa]+ this.vrijZivSegmenta+this.vrijemePauze) {
		    	pocetakSegmenta[krajRepa]=0;// ovaj se yrray koristi za sve tako da je samo njega potrebno namjestit na nulu da se oznaèi da je prazno mjesto
		        krajRepa++;
		        if(krajRepa>=this.pocetakSegmenta.length) krajRepa=0;
		    }
		  int i= poceRepa;
		  int i2=0;
		  //int i=-1;
		  boolean prosaJedanKrug=false;
		  
		// ////////////okrecem samo jedanput na poèetku canvas jer svi segmenti zrake imaju isti kut
		  zadnjiKut=90	-(float)this.izracunajKut( x, y,this.pocPolX,this.pocPolY);
		  can.save();
		  can.rotate(zadnjiKut, this.pocPolX, this.pocPolY);//rotate(90+(float)this.izracunajKut( x, y,this.pocPolX,this.pocPolY));
		  /////////////////////
		  
		  while(true){	  
			
			 //if(brClana> this.brojClanovaZrake) break;
			// if(pocetakSegmenta[i]==0) break;
			 
			 ///iscrtavanje
						// rectRepa.set(this.pocPolX -rectRepa.width()/2, this.pocPolY+  i2*rectRepa.height(),this.pocPolX +rectRepa.width()/2,this.pocPolY+(i2+1)*rectRepa.height());
			  if(i==this.krajRepa){}
			  rectRepa.set(this.pocPolX -rectRepa.width()/2, this.pocPolY+  i2*rectRepa.height(),this.pocPolX +rectRepa.width()/2,this.pocPolY+(i2+1)*rectRepa.height());
			  float razlika=rectRepa.bottom-pocPolY;
			  if(razlika>udaljDoCilja){
				  razlika=razlika-this.udaljDoCilja;
				  rectRepa.set(rectRepa.left,rectRepa. top-razlika, rectRepa.right,rectRepa. bottom-razlika);
			  }
			  if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(3)+prosloVrijeme[i]+this.vrijemePauze){
				//this.projektil.nacrtajSpriteRotiran(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], p);
				 this.projektil.nacrtajSprite(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, p);
			     this.prosloVrijeme[i]=System.currentTimeMillis();
				stupacNaKojemJeStao[i]=stupacNaKojemJeStao[i]+1;
			    if(stupacNaKojemJeStao[i]>=projektil.brojStupaca(3)) stupacNaKojemJeStao[i]=0;// nulta slika je za rep, prva æe biti za projektil
			
			 }
			 else{
				 //this.projektil.nacrtajSpriteRotiran(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa, this.kut[i], p);
				 this.projektil.nacrtajSprite(can, 3, stupacNaKojemJeStao[i], 0, this.rectRepa,p);
			 }
			
			 //if(i==krajRepa&&prosaJedanKrug) break;
			 if(i==krajRepa) break;
			 
			
			 ///	
			 i++;
			 i2++;
			 ////
			 
			 if(pocetakSegmenta.length<=i){// ako dode do kraje ide ispocetka
					i=0;
					prosaJedanKrug=true;
				}
		 }
		  
		  ///////////
			 can.restore();
		  ////////////
			 
			 
		 zadnjiXrepa=this.pocPolX;
	     zadnjiYrepa=this.pocPolY;
	     ////////////kada zavrsi sa crtanjem repa odmahcrta pogodak 
	     if(pogSlijediCilj&&objCilja!=null){
				recPogotka.set(objCilja.getRect().centerX()-velPogodakX/2, objCilja.getRect().centerY()-velPogodakY/2, objCilja.getRect().centerX()+velPogodakX/2,objCilja.getRect().centerY()+ velPogodakY/2);
			  }
		 else recPogotka.set(x-velPogodakX/2, y-velPogodakY/2, x+velPogodakX/2,y+ velPogodakY/2);
		 if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(2)+this.prosVrijPogodak+this.vrijemePauze){
			   projektil.nacrtajSprite(can, 2, brSlPogodtka, 0,recPogotka,  p);
			   brSlPogodtka++;
			   prosVrijPogodak=System.currentTimeMillis();
				if(this.brSlPogodtka>=projektil.brojStupaca(2)){
	                brSlPogodtka=0;
	     }
			}
			else   projektil.nacrtajSprite(can, 2, brSlPogodtka, 0,recPogotka,  p);
		
	}
	}
	else zrakaRep=false;	
}
private void dodavanjeNovihSegmenataRepaZaZraku(){
	int i=0;
	 int brSeg=Math.round((y-this.pocPolY)/this.rectRepa.height());
	 this.poceRepa=0;
	 this.krajRepa=0;
	 this.zrakaTekAktivirana=true;
	 udaljDoCilja=(float) Math.hypot(Math.abs(this.pocPolX-x),Math.abs(this.pocPolY-y));
	 int ukBrojSeg= (int)(udaljDoCilja/(this.rectRepa.height()+this.rectRepa.height()/6));
	 double k=this.izracunajKut( x, y,this.pocPolX,this.pocPolY);
	/*while(Math.hypot(Math.abs(this.pocPolX-polozajX[krajRepa]),Math.abs(this.pocPolY-polozajY[krajRepa]))//+this.rectRepa.width() // dodajem sirinu repa zbog toga da ne prelazi preko cilja
			<Math.hypot(Math.abs(this.pocPolX+x),Math.abs(this.pocPolY+y))||i==0) {*/
	 while(i<ukBrojSeg){//MOZDA SAM MOGAO I BEZ OVE CIJELE FUNKCIJE ALI NISAM HTIO PREVIŠE MJENJAT OD OSTALIH FUNKCIJA
		   /*poceRepa++;
		      if(poceRepa>=this.pocetakSegmenta.length){
		    	  poceRepa=0;// ako je otišao u krug
		                 }
		      if(poceRepa==krajRepa){// ako je naletio na rep
		    	                     krajRepa++;
    
		                        }
		      if(krajRepa>=this.pocetakSegmenta.length) {
             	 krajRepa=0;
                   }*/
		
	                        
		      stupacNaKojemJeStao[poceRepa]=0;// resetira na pocetak animaciju za ovaj segment
		      pocetakSegmenta[poceRepa]=System.currentTimeMillis();
		    
		     zadnjiXrepa=zadnjiXrepa+ (float)(i*this.rectRepa.height()*Math.acos(k));//i*this.rectRepa.width();//
		     zadnjiYrepa= zadnjiYrepa+ (float)(i*this.rectRepa.height()*Math.asin(k));//i*this.rectRepa.height();//
		      polozajX[krajRepa]=zadnjiXrepa;
		      polozajY[krajRepa]=zadnjiYrepa;
		     
		      i++;
		      krajRepa++;
		     
		      if( !zrakaTekAktivirana){ 
	              if(krajRepa>poceRepa){
	        	      if(krajRepa>=this.pocetakSegmenta.length)  krajRepa=0;
	        	      if(krajRepa==poceRepa) poceRepa++;
	                   }
	              else if(krajRepa==poceRepa){
	        	      poceRepa++;
	        	      if(poceRepa>=this.pocetakSegmenta.length) {
	              	        poceRepa=0;
	        	        }  
	                 }
		     }
		     else {
		    	 zrakaTekAktivirana=false;
		     }
		      polozajX[krajRepa]=zadnjiXrepa;
		      polozajY[krajRepa]=zadnjiYrepa;
		     
		      }
	int j=1;
	j++;
}
private void dodavanjeNovihSegmenataKonstantnogRepa(){
	
	//float[][] lista=this.dvojnik.izracunavanjePolozajaUProslosti(20);
	float[][] lista=this.dvojnik.getListuIzracunatihMedupolozaja();
	float ukupnaUdaljenostIzListe;
	int trecina1,trecina2;
	if(lista!=null)if(lista[0]!=null){
		trecina1=(int)(lista[0].length/3);
		trecina2=(int)(2*lista[0].length/3);
		ukupnaUdaljenostIzListe=(float)Math.hypot(Math.abs(lista[0][0]-lista[0][trecina1]),Math.abs(lista[1][0]-lista[1][trecina1]));
		ukupnaUdaljenostIzListe=ukupnaUdaljenostIzListe+(float)Math.hypot(Math.abs(lista[0][trecina1]-lista[0][trecina2]),Math.abs(lista[1][trecina1]-lista[1][trecina2]));
		ukupnaUdaljenostIzListe=ukupnaUdaljenostIzListe+(float)Math.hypot(Math.abs(lista[0][trecina2]-lista[0][lista[0].length-1]),Math.abs(lista[1][trecina2]-lista[1][lista[0].length-1]));
		ukupnaUdaljenostIzListe+=prosliIgnoriraniPutRepa;
		float brSegmenataZaDodati=ukupnaUdaljenostIzListe/this.rectRepa.height();
		prosliIgnoriraniPutRepa=ukupnaUdaljenostIzListe-(int)(brSegmenataZaDodati)*this.rectRepa.height()	;
		brSegmenataZaDodati=(int)brSegmenataZaDodati;
		
	
		int preskoci=(int)(lista[0].length/brSegmenataZaDodati)-1;// koliko ce se iz liste dodatnih polozaja preskakati u svakoj iteraciji
		if(preskoci<0) preskoci=0;
	
			for(int i=0;i<brSegmenataZaDodati;i++) {
			   
			      stupacNaKojemJeStao[poceRepa]=0;// resetira na pocetak animaciju za ovaj segment
			      pocetakSegmenta[poceRepa]=System.currentTimeMillis();
			      
			      
			      kut[poceRepa]=90+(float)this.izracunajKut(lista[0][(i)*preskoci],lista[1][(i)*preskoci], lista[0][(i+1)*preskoci], lista[1][(i+1)*preskoci]);
			    
			      zadnjiKut=kut[poceRepa];
			      polozajX[poceRepa]=lista[0][(i+1)*preskoci];// sprema centar
			      polozajY[poceRepa]=lista[1][(i+1)*preskoci];
			      
			      poceRepa++;
			      if(poceRepa>=this.pocetakSegmenta.length){
			    	  poceRepa=0;// ako je otišao u krug
			                 }
			      if(poceRepa==krajRepa){// ako je naletio na rep
			    	                     krajRepa++;
			    	                    
			    	                     if(krajRepa>=this.pocetakSegmenta.length) {
			    	                    	 krajRepa=0;
			    	                          }
			                        }
			    
			      }	
	}
}
private void dodavanjeNovihSegmenataRepa(){

	while(Math.hypot(Math.abs(x-polozajX[poceRepa]),Math.abs(y-polozajY[poceRepa]))
			>=this.udaljOkidanja+rectRepa.height()) {
		    
		      poceRepa++;
		      if(poceRepa>=this.pocetakSegmenta.length){
		    	  poceRepa=0;// ako je otišao u krug
		                 }
		      if(poceRepa==krajRepa){// ako je naletio na rep
		    	                     krajRepa++;
		    	                    
		    	                     if(krajRepa>=this.pocetakSegmenta.length) {
		    	                    	 krajRepa=0;
		    	                          }
		                        }
		      stupacNaKojemJeStao[poceRepa]=0;// resetira na pocetak animaciju za ovaj segment
		      pocetakSegmenta[poceRepa]=System.currentTimeMillis();
		      if(!lomPoceo)kut[poceRepa]=90+(float)this.izracunajKut(zadnjiXrepa,zadnjiYrepa, x, y);
		      else kut[poceRepa] =zadnjiKut; 
		      zadnjiKut=kut[poceRepa];
		      polozajX[poceRepa]=x;// sprema centar
		      polozajY[poceRepa]=y;
		      
		      }
}
/*
private void crtanjeFrezzTopa(){
	if(postojiSlikaIspaljivanja){
		recIspaljivanja.set(this.xPozTopa,this.yPozTopa, this.xPozTopa+this.xVelTopa,this.yPozTopa+this.yVelTopa);
		if(praznjenje){
		if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(0)+this.prosVrijIspaljivanje+this.vrijemePauze){
		   
		  
			  projektil.nacrtajSprite(can, 0, brSlIspaljivanje, 2,recIspaljivanja,  p);
			  brSlIspaljivanje++;
		  
		  
		  
		   prosVrijIspaljivanje=System.currentTimeMillis();
			if(this.brSlIspaljivanje>=projektil.brojStupaca(0)){
               
                brSlIspaljivanje=0;
                brSlPunjenja=0;
                animPunjIliPra=true;
     }
		}
		else   projektil.nacrtajSprite(can, 0, brSlIspaljivanje,2,recIspaljivanja,  p);

	 }
		else if(animPunjIliPra){
			if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(0)+this.prosVrijIspaljivanje+this.vrijemePauze){
			   projektil.nacrtajSprite(can, 0, brSlPunjenja, 0,recIspaljivanja,  p);  
			    brSlPunjenja++;
			    prosVrijIspaljivanje=System.currentTimeMillis();
				 if(this.brSlPunjenja>=projektil.brojStupaca(0)){
					 brSlIspaljivanje=0;
	                brSlPunjenja=0;
	                animPunjIliPra=false;
	     
				 }
			}
			else{
				projektil.nacrtajSprite(can, 0, brSlPunjenja, 0,recIspaljivanja,  p);
			}
		}
		else{
			if(System.currentTimeMillis()>1000/projektil.brojPrikazaPoSekundi(0)+this.prosVrijIspaljivanje+this.vrijemePauze){
			   
			   projektil.nacrtajSprite(can, 0, brSlNapunjen, 1,recIspaljivanja,  p);
			   brSlNapunjen++;
			   prosVrijIspaljivanje=System.currentTimeMillis();
				if(this.brSlNapunjen>=projektil.brojStupaca(0)){
	               // ispaljivanje=false;
	                brSlNapunjen=0;
	     }
			}
			else   projektil.nacrtajSprite(can, 0, brSlNapunjen,1,recIspaljivanja,  p);
		}
   }
	else praznjenje=false;	
}
*/
///// cesto koristene metode
private double izracunajKut(float ax,float ay,float bx, float by){// vraæa kut izmeðu pravca kroz dvije tocke i horizontale
	double k=0;
	double dy=ay-by;
    double dx=bx-ax;
    k=57.29577951*Math.atan(Math.abs(dy)/Math.abs(dx));
	if(dy>=0&&dx<=0) k=180-k;
	else if(dy<=0&&dx<=0) k=180+k;
	else if(dy<=0&&dx>=0) k=360-k;
	
	/*if(ax==bx) ax=ax+0.0000000000000001f;
		double dx=bx-ax;
		double dy=ay-by;
		k=Math.atan((dy)/(dx));
		k=Math.toDegrees(k);
		// k= Math.atan(Math.abs((ay-by)/(ax-bx)));
	*/
	return k;
}
///////////////////////////////////////////////////////////	
////////metode interfacea object linker prikaz
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		ge=e;
		xSvjezi=e.x;
		ySvjezi=e.y;
		xCilj=e.x2;
		yCilj=e.y2;
		vrijemePauze=e.vrijemePauze;
		pocPolX=e.pomNaX;
		pocPolY=e.pomNaY;
		indikatorBorbe=e.indikatorBorbe;
		if(this.ctanjePogotkaSamkAkoJeNacinioStetu){
			if(e.izbornikUpaljen){
                //resetiranje varijabli za rep
				/*this.krajRepa=0;
				if(pocetakSegmenta!=null)	for(int i=0;i<pocetakSegmenta.length;i++){
					pocetakSegmenta[i]=0;
					 stupacNaKojemJeStao[i]=0;
					 polozajX[i]=0;// sprema centar
				      polozajY[i]=0;
				      this.kut[i]=0;
				}*/
				//////////////////////////
				
				pokreniCrtanjePromasaja=false;// ako sljedeća strijela pogodi gasim zastavicu jer bi se nda istovremeno crta i pogodak i promašaj
				crtajPogodakAkoStetaNacinjena=true;
				
				
				 e.izbornikUpaljen=false;// oznacavat ce da je na�?injena šteta  poštose ova zastavica ne koristi , gasi je kada je jednom o�?ita
			}
			else crtajPogodakAkoStetaNacinjena=false;
		}
		if(e.objektLogike!=null){
			objCilja=e.objektLogike;
			e.objektLogike=null;
		}
		dodatakY=e.helth;
	    lomPoceo=e.imTouched;
		if(!e.jesamLiZiv){
			 zivSam=false;
		}
		if(e.indikatorBorbe==0){
			zrakaAktivna=false;
			praznjenje=false;
		}
		if(e.indikatorBorbe==1){
            this.geIspaljiv.jesamLiZiv=true;
			ispaljivanje=true;
			praznjenje=true;
            animPunjIliPra=true;
			zrakaAktivna=true;
			e.indikatorBorbe=0;
			let=false;
			xIspaljivanja=xSvjezi;
			yIspaljivanja=ySvjezi;
		}
		else if(e.indikatorBorbe==2) {
			let=true;
			yZaSortiranje=ySvjezi;// sluzi za sortiranje
		}
		else if(e.indikatorBorbe==3){
			  //resetiranje varijabli za rep
			
			
			//////////////////////////
			if(mrljaInicijalizirana){
				eventMrlje.indikator=1;
				eventMrlje.x=xSvjezi;
				eventMrlje.y=ySvjezi;
				this.mrlja.GameLinkerIzvrsitelj(eventMrlje);
			}
			pogodak = true;
			xPogotka=xSvjezi;
			yPogotka=ySvjezi;
			yZaSortiranje=ySvjezi;// inaèe bi poslije vracanja projektila u toranj iscrtavao ispod tornja eksploziju
			e.indikatorBorbe=0;
			
			let=false;
			if(pogodak&&!crtajPogodakAkoStetaNacinjena&&crtajPromasajIzSlikeLeta){
				 pokreniCrtanjePromasaja=true;
				 kutPromasaja=this.kutLeta;
				 xPromasaja=xSvjezi;
					yPromasaja=ySvjezi;
				 this.pocetakCrtanjaPromasaja=System.currentTimeMillis();
			 }
		}
	}
////////////////////////////////////////////////////	
private void stvariKojeSeIzvrsavajuSamoNaPocetku(){
	if(mrlja!=null){
		this.uiMan.dodajElementUListu(mrlja, 1);
	}
	if(projektil.postojiLiSlikNaMjestu(0)) this.postojiSlikaIspaljivanja=true;
	if(projektil.postojiLiSlikNaMjestu(1)) this.postojiSlikaProjektila=true;
	if(projektil.postojiLiSlikNaMjestu(2)) this.postojiSlikaPogotka=true;
	if(projektil.postojiLiSlikNaMjestu(3)) this.postojiSlikaRepa=true;
}
/////////////metode interfacea ui manager object////
@Override
public void nacrtaj(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY) {
	// TODO Auto-generated method stub
	this.fps=fps;
	this.can=can;
	this.uiMan=uiMan;
	if(this.tekPoceo){
		stvariKojeSeIzvrsavajuSamoNaPocetku();
	    tekPoceo=false;
	}
	x=xSvjezi;
	y=ySvjezi;
	if(this.obicniRepa) crtanjeRepa();
	else if(this.generickiRep) crtanjeGenerickogRepa();
	else if(this.zrakaRep){
		if(this.yCilj>this.yPozTopa+this.yVelTopa){// ako je cilj ispod crta zraku iznad topa
			//crtanjeFrezzTopa();
			crtanjeZrakaRepa();
			
		  }
		else{
			crtanjeZrakaRepa();
			//crtanjeFrezzTopa();
		}
		
	}
	if(!zrakaRep){
		if(this.let)	nacrtajLetVanjski( can,  fps,uiMan, PpCmX,PpCmY,pomCanX,pomCanY);
		nacrtajLet();
	    
	}
	if(!zrakaRep){
		if(pokreniCrtanjePromasaja||this.pogodak)crtanjePogotkaVanjski(can,  fps,uiMan, PpCmX,PpCmY,pomCanX,pomCanY);
		crtanjePogotka();// pogodak se crta pri crtanju repa mora biti konstantno
		
	}
	if(!zrakaRep){
		if(this.ispaljivanje){
			crtanjeIspaljivanja( can,  fps,uiMan, PpCmX, PpCmY,pomCanX,pomCanY);
		}
		crtanjeIspaljivanja();// ispaljivanje se zasad necrta za zraku
		
	}
	//sprite.nacrtajSprite(can, 0,1, 1, new RectF(x,y,x+velLetX,y+velLetY));
	//can.drawCircle(x+velLetX/2,y+velLetY/2,velLetX/2, p);
	if(!zivSam) samoubojstvo();// stavio sam ga ovdje tako da svaki objekt sebe ubija tako da pripadajuæi menageri imaju pravilne podatke o broju onbjekata
    xPros=x;
    yPros=y;
}
@Override
public void setDvojnikaULogici(GameLogicObject obj) {
	// TODO Auto-generated method stub
     dvojnik=(ProjektilL)obj;	
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
	//if(this.zrakaRep){ return this.yPozTornjPoz+this.yVelTornjPoz/2 + this.yVelTopa+3;}
	//else{
	   if(ge!=null) if(ge.indikatorBorbe!=3){
	    	            yZaSortiranje=ge.y;// u slucaju da je dosao na cilj uzimam ta y jer je vraca poslije projektil u toranj
	                    this.dodatakY=ge.helth;      
	    }
		if(pokreniCrtanjePromasaja){
			return this.yPromasaja+this.velLetY/2;
		}
		
		else return yZaSortiranje+this.dodatakY;
	//}
}

@Override
public float getSir() {
	// TODO Auto-generated method stub
	return this.velLetX;
}

@Override
public float getVis() {
	// TODO Auto-generated method stub
	return this.velLetY;
}
////////////////////////////////////////////////////////////
@Override
public RectF getGlavniRectPrikaza() {
	return recLet;
}
@Override
public void onSystemResume() {
	// TODO Auto-generated method stub
	projektil=this.uiMan.GL.faIgr.getSprite(this.indikator);
}
@Override
public boolean getDaliDaIgnoriramTouch() {
	// TODO Auto-generated method stub
	return false;
}
     
}
