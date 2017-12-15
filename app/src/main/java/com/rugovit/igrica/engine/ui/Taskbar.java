package com.rugovit.igrica.engine.ui;

import java.util.LinkedList;
import java.util.Random;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.logic.elements.ProjektilL;
import com.rugovit.igrica.engine.logic.elements.PutL;
import com.rugovit.igrica.engine.logic.elements.ToranjL;
import com.rugovit.igrica.engine.ui.elements.IzbornikUniverzalni;
import com.rugovit.igrica.engine.ui.elements.MusicManager;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class Taskbar implements UIManagerObject {
	
private boolean zvukAbilitijaReenforcePokrenut=false;	
private boolean zvukAbilitijaMeteorPokrenut=false;	
////KONSTANTE//////////////////////////	
private float velPojaSmanj,velMjerPoj;	

private int zvukPobjede=-1;
private double vrijemeZaPljesak=0;

private float sirDolPro=1.2f;
private float visDolPro=1.2f;
private float lijevoPostoGore=2;// postotak od lijeve strane ekrana gdje se nalazi prvi dio taskbara
private float lijevoPostoDole=2;
private float desnoPostoDole=2;// postotak od desne strane gdje se nalazi kraj taskbara
private float desnoPostoGore=0;
private float gorePosto=0;// postotak od gornje strane ektaan gdje se nnalazi taskbar
private float dolePosto=0;// postotak od doledonjeg djela taskbara
private float sirBodovaPosto=20;
private float visBodovaPosto=20;
private float sirKontrDugmPosto=20;
private float visKontrDugmPosto=20;
private float sirMenuDugmPosto=9.5f;
private float visMenuDugmPosto=7;
private float sirDodFunkPosto=9.5f;
private float visDodFunkPosto=7;
private float sirBodovaPics=0;
private float visBodovaPics=0;
private float sirPauze=0;
private float sirResume=0;
private float sirMenuDugmPics=0;
private float visMenuDugmPics=0;
private float sirDodFunkPics=0;
private float visDodFunkPics=0;
private float PpCmX;
private float PpCmY;
private float sirEkr;
private float visEkr;
private Canvas can;
private TextPaint textPaintBodovi;
private TextPaint textPaintVal;
private TextPaint textPaintNovac;
private TextPaint textPaintZivot;
private Paint paintZaHodanje;
private GameLogic GL;
private IzbornikUniverzalni izborLavaBomb,izborRenforce,izbornikKontroleZvuka,izborEndGame,izborExit,izborRestart,izborMute;
////spriteovi
private SpriteHendler  sprUpustvaAbiliti;
private SpriteHendler  sprMute;
private SpriteHendler  sprBodova;
private SpriteHendler sprKontroleZvuka;
private SpriteHendler restartSprite;
private SpriteHendler exitSprite;
private SpriteHendler sprAbility;
private SpriteHendler resumeSprite;
private SpriteHendler pauseSprite;
private SpriteHendler dolProtSprite;
/////////ZASTAVICE/////////////////////////////////
private boolean crtajOblacicDolaskaProtivnika=false;
private boolean tekPoceoSaCrtajOblacicIzbornikom=true;
private boolean pokakUpustvaTornjeviGtovPrviDio=false,pokakUpustvaTornjeviGtovDrugiDio=false;
private boolean inicijalizirajUpustvaTornjevi=false, gotovaAnimacijaUpustvaTornjevi=false, gotovPrviDioAnimacijeTornjevi=false, gotovDrugiDioAnimacijeTornjevi=false, gotovTreciDioAnimacijeTornjevi=false;
private boolean tekPoceoUpustvaTornjeviPrviDio=true,tekPoceoUpustvaTornjeviTreciDio=true;

private boolean inicijalizirajUpustvaAbilit=false, gotovaAnimacijaUpustvaAbiliti=false, gotovPrviDioAnimacijeAbilitiUpustva=false, gotovDrugiDioAnimacijeAbilitiUpustva=false;
private boolean iskljuciCrtanje=false,iskljuciCrtDolaskaProt=false,iskljuciCrtBodova=false,iskljuciCrtPodnozja=false,iskljuciCrtanjeResetBotuna=false,iskljuciCrtanjeBackBotuna=false;
private boolean animPoraz=false, animPobjeda=false;
private boolean tekPoceoSaZavrsnomAnimacijom=true,zavrAnimTranslacija=false, zavrAnimPovecanje,zavrsenoPljeskanje=false;
private boolean crtanjeSvijetlaPodnozja=true;
private boolean tekpoceoSaPljeskanjem=true;	
private boolean crtaKrajDolaska;
private boolean poslaoNovceGameLogicu=false;
private boolean pokrenutExitIzbornik=false;
private boolean endGameIzbornikPokrenut=false;
private boolean zavrsnaAnimacijaPokrenuta=false;
private boolean zavrsnaAnimacijaGotova=false;
private boolean pause=false,pauseOdPauzeGumica=false;
private boolean tekPoceo=true;
private boolean touchedMenu=false;// oznaèava na koji je dio ntaskbara kliknuto
private  boolean touchedDodatneFunkcije=false;
private  boolean touchedPauseResume=false;
private  boolean touchedDolPro=false;
private boolean abilityRenforceUvodZavrsen=false,abilityMeteorUvodZavrsen;
/////////VARIJABLE/////////////////////////////////

private LinkedList<IzbornikUniverzalni> listaPocmiValoveIzbornika;
private boolean stisnutoNaPocetakValova=false;
private LinkedList<Integer> listaBotunaNaPocetkuXPozicija;
private LinkedList<Integer> listaBotunaNaPocetkuYPozicija;
private float dyPolStari;
private RectF recPomUputeToranj,recPomUputeToranj2;
private int brSlAnimTornj=0;
private float visUpustvaTornjevi,sirUpustvaTornjevi, polXUpustvaTornjevi, polYUpustvaTornjevi;
private SpriteHendler spriteUpustvaTornjevi;
private double pocetakUpustvaToranjPrviDio,pocetakUpustvaToranjDrugiDio,pocetakUpustvaToranjTreciDio;

private double poceoUpustvaAbilitiDrugiDioStojanje;
private GameEvent geUpustva1=new GameEvent(null),geUpustva2=new GameEvent(null);
private float pomakYEndGameIzb,velGameEndIzb;
//ability varijablež
private float xPocAbilitija;
private float randomRazmakCm=0.2f;
private Paint paintPunjenja;
private float velicinaPodnozjaPix;
private int brOznacenogAbilitija=-1;
private int zadnjiBrojAktiviranogAbilitija=0;
private float sirinAbilityPolja;
private float razmakIzmeđuAbilityPolja;
////varijable svijetla podnozja

private int podR,podG,podB=20;
private float bojaPoSec=10;
private double proslaBoja;
private Paint paintZaSvjetloPodnozja=new Paint();;
private float xPolPljeskanja;
private float yPolPljeskanja;
private float novacPoSekundiPrijeDolaskaNovogVala=0;
private float postotakUvecanjaZavrsne=0;
private float pomakZavrAnimY=0;
private double prosVrijObl=0;
private double prosVrijHod=0;
private double prosVrijKraj=0;
private int brSlObl=0;
private int brSlHod=0;
private int brSlKraj=0;
private float xVala;
private float yVala;
private int secDoNovVala=-1;// oznacava da val traje
private int prosSecNovVala=0;
private RectF recNovaca,recValova,recZivota,recPodlogaBodova,recSvijetloZivota;
private Paint paintSvijetlaZivota=new Paint();
private  RectF recPodnozja;
private  RectF recPljeskanja1;
private  RectF recPljeskanja2;
private  RectF recPljeskanja3;
private  RectF recZavrsneAnimacije;
private  RectF recTempZavrAnim;
private  RectF recDolaProt;
private  RectF rectMenu;// slat æe kroz rect objekte velièine dugmiæa uimanageru
private  RectF rectDodatneFunkcije;
private  RectF rectPause;
private  RectF rectResume;
private  RectF recLazni=new RectF();
private float pomCanX;// koliko se kanvas pomakao 
private float pomCanY;
private float lijevoPicsGore=0;// 
private float lijevoPicsDole=0;
private float desnoPicsDole=0;// 
private float desnoPicsGore=0;
private float  gorePics=0;// 
private float dolePics=0;
private float touchedX;// sprema varijablu gdje je kliknuto na ekran
private float touchedY;
private Paint pauseResumePaint;
     //bodovi
private int brZvjezdica=0;
private Rect recText;
private int bodovi=0;
private int prosliVal=0;
private int trenutniVal=0;
private int brojValova=0;
private int ukupniNovac=0;
private int brojZivota=0;
private Typeface font;
private SpriteHendler spriteZavrsneAnimacije, spriteZavrsneAnimacije2,spriteZavrsneAnimacije3;
private LinkedList<Typeface> listaFontova;
private UIManager uiMan;
public Taskbar(LinkedList<Typeface> listaFontova){
	   this.listaFontova=listaFontova;
	   rectMenu=new RectF();
	   rectDodatneFunkcije=new RectF();
	   rectPause=new RectF();
	   rectResume=new RectF();
	   recDolaProt=new RectF();
	   recPodnozja=new RectF();
	   recZavrsneAnimacije=new RectF();
	   recTempZavrAnim=new RectF();
	   recPljeskanja1=new RectF();
	   recPljeskanja2=new RectF();
	   recPljeskanja3=new RectF();
	   recNovaca=new RectF();
	   recValova=new RectF();
	   recZivota=new RectF();
	   recSvijetloZivota=new RectF();
	   recPodlogaBodova=new RectF();
	   ////
	    font=listaFontova.get(0);
	   ///
	   this.paintZaHodanje=new Paint();
	   ///
	   textPaintBodovi=new TextPaint();
	   textPaintVal=new TextPaint();
	   textPaintNovac=new TextPaint();
	   textPaintZivot=new TextPaint();
	
	   textPaintBodovi.setAntiAlias(true);
	   textPaintBodovi.setTypeface(font);
	   textPaintBodovi.setColor(Color.RED);
	   textPaintBodovi.setStyle(Style.FILL);
	   
	   textPaintVal.setAntiAlias(true);
	   textPaintVal.setTypeface(font);
	   textPaintVal.setColor(Color.argb(255,85, 146, 0));
	   textPaintVal.setStyle(Style.FILL);
	   
	   textPaintNovac.setAntiAlias(true);
	   textPaintNovac.setTypeface(font);
	   textPaintNovac.setColor(Color.argb(255, 255, 188, 8));
	   textPaintNovac.setStyle(Style.FILL);
	   textPaintZivot.setAntiAlias(true); 
	   textPaintZivot.setTypeface(font);
	   textPaintZivot.setColor(Color.argb(255, 233, 4, 4));
	   textPaintZivot.setStyle(Style.FILL);
	   
	   pauseResumePaint=new Paint();
	    recText=new Rect();
}
	
/////////BILDERI/////
public void dodajBotuneZaPocetakValova(int pozicijaX,int pozicijaY, UIManager uiMan){
	   if(uiMan!=null){
		   this.uiMan=uiMan;
	   }
	  //X KOREKCIJA
	  if(pozicijaX-this.sirDolPro*uiMan.getPixPoCmX()/2<0){
		  pozicijaX=(int)(sirDolPro*uiMan.getPixPoCmX()/2);
	    }
	  else if(pozicijaX+this.sirDolPro*uiMan.getPixPoCmX()/2>this.uiMan.getRectGlavnePozadine().width()){
		  pozicijaX=(int)(this.uiMan.getRectGlavnePozadine().width()-this.sirDolPro*uiMan.getPixPoCmX()/2);
		    
	  }
	  //Y KOREKCIJA
	  if(pozicijaY-this.visDolPro*uiMan.getPixPoCmY()/2<sirPauze){
		  pozicijaY=(int)( sirPauze+this.visDolPro*uiMan.getPixPoCmY()/2);
		  //this.recDolaProt.set(recDolaProt.left, 0-pomCanY+this.rectPause.bottom,recDolaProt.right,0+recDolaProt.height()-pomCanY+this.rectPause.bottom);
	  }
	  else if(pozicijaY+this.visDolPro*uiMan.getPixPoCmY()/2>this.uiMan.getRectGlavnePozadine().height()-velMjerPoj){
		  pozicijaY=(int)( this.uiMan.getRectGlavnePozadine().height()-this.visDolPro*uiMan.getPixPoCmY()/2-velMjerPoj);
		  
	  }
	if(listaBotunaNaPocetkuXPozicija==null){
	                     listaBotunaNaPocetkuXPozicija=new LinkedList<Integer>();
                         listaBotunaNaPocetkuYPozicija=new LinkedList<Integer>();
	 }
	 listaBotunaNaPocetkuXPozicija.add(pozicijaX);
	 listaBotunaNaPocetkuYPozicija.add(pozicijaY);
}
public void dodajZvukpobjede(int zvuk){
	this.zvukPobjede=zvuk;
} 
public void dodajSpriteZavrsneAnimacije(SpriteHendler spr){
	spriteZavrsneAnimacije=spr;
}
/*public void bilderZaDolazakProtivnika(float xDodNaPolCm, float yDodNaPolCm){
	this.xDodNaPol=xDodNaPolCm;
	this.yDodNaPol=yDodNaPolCm;
}*/

////////////////////

///////////NACRTAJ
	@Override
public void nacrtaj(Canvas can, float fps,
			UIManager uiMan, float PpCmX, float PpCmY, float pomCanX, float pomCanY) {
		this.pomCanX=pomCanX;
		this.pomCanY=pomCanY;
		this.PpCmX=PpCmX;
		this.PpCmY=PpCmY;
		this.can=can;
		this.uiMan=uiMan;
		if(tekPoceo) {
			geUpustva1.jesamLiZiv=true;
			stvariKojeSeTrebajuObavitSamoJedanput();
			inicijalizirajDodatneBotune();
			 tekPoceo=false;
			 ;
		}
		if(!GL.jeliPauza()&&this.pause){//jer ako je upaljen exit izbornik i klikne se sa strane na pauze onda se ikljuci izbornik upali pauze ali se islkulju pauze u gl ali ostane slika pauze
			GL.pause();
		}
		provjeriTouchedNaDugiceIIzvrsi888();
		if(!iskljuciCrtanje)
		{
		if(this.inicijalizirajUpustvaAbilit){
			if(!gotovaAnimacijaUpustvaAbiliti&&abilityRenforceUvodZavrsen ){
				gotovaAnimacijaUpustvaAbiliti=nacrtajUpustvaAbiliti();
			}
		}
		if(this.inicijalizirajUpustvaTornjevi){
			if(!gotovaAnimacijaUpustvaTornjevi){
				gotovaAnimacijaUpustvaTornjevi=nacrtajUpustvaTornjeviPrviDio();
			}
		}
		pomakniRectove888();
		
		// if(!iskljuciCrtDolaskaProt)nacrtajDolazakProtivnika888();
		
		if(!iskljuciCrtBodova)nacrtajBodove888();
		nacrtajKontrolneDugmice888();
		nacrtajPodnozje();
		nacrtajMenuDugmic888();
		nacrtajDodatneFunkcije888();
		
		if(zavrsnaAnimacijaPokrenuta){
			if(animPoraz)this.nacrtajZavrsnuAnimacijuPoraz(fps);
			else if(animPobjeda)this.nacrtajZavrsnuAnimacijuPobjeda(fps);
		}	
		}
	}	
//////////////////PUBLIC METODE
   /////BILDER METODEž

public void inicijalizirajTornjeviUpustva(SpriteHendler sprite){
	spriteUpustvaTornjevi=sprite;
	this.inicijalizirajUpustvaTornjevi=true;
}
public void inicializirajAbilitiUpustva(SpriteHendler upustva){
	inicijalizirajUpustvaAbilit=true;
	sprUpustvaAbiliti=upustva;
	
}	
public void dodajSpriteMute(SpriteHendler sprite){
		sprMute=sprite;
	}	
public void dodajSpriteBodova(SpriteHendler sprite){
		sprBodova=sprite;
	}
public void dodajSpriteKontroleZvuka(SpriteHendler sprite){
		sprKontroleZvuka=sprite;
	}
public void dodajRestartSprite(SpriteHendler sprite){
		 this.restartSprite=sprite; }
 public void dodajExitSprite(SpriteHendler sprite){
	 this.exitSprite=sprite; }
 public void postaviNovcePrijeDolaskaNovogVala(float novacPoSekundi){
		   this.novacPoSekundiPrijeDolaskaNovogVala=novacPoSekundi;
	  }
 public void dodajDolazakProtivnikaSprite(SpriteHendler spr){
	this.dolProtSprite=spr;
   }
 public void dodajPauseSprite(SpriteHendler spr){
	pauseSprite=spr;
   }
 public void dodajResumeSprite(SpriteHendler spr){
	resumeSprite=spr;
   }	
   //////	
 public RectF getRecPodnozja(){
	 return this.recPodnozja;
 } 
 public float getVisBotunaMjeracaPojacanja(){
	 return velMjerPoj;
 }
 public float getVisBotunaDodatnihFunkcija(){
	 return this.sirinAbilityPolja;
 }
 public Typeface fontBodova(){
	 return font;
 }
 public void ponovnoPostaviZastavice(){
	 
	 crtajOblacicDolaskaProtivnika=false;
	 tekPoceoSaCrtajOblacicIzbornikom=true;
	 iskljuciCrtanje=false;
	 iskljuciCrtDolaskaProt=false;
	 iskljuciCrtBodova=iskljuciCrtPodnozja=iskljuciCrtanjeResetBotuna=iskljuciCrtanjeBackBotuna=false;
	animPoraz=animPobjeda=false;
	zavrAnimTranslacija=zavrAnimPovecanje=zavrsenoPljeskanje=false;
	  tekPoceoSaZavrsnomAnimacijom=true;
	crtanjeSvijetlaPodnozja=true;
	 tekpoceoSaPljeskanjem=true;	
	crtaKrajDolaska=false;
	 poslaoNovceGameLogicu=false;
	 pokrenutExitIzbornik=false;
	 endGameIzbornikPokrenut=false;
	 zavrsnaAnimacijaPokrenuta=false;
	 zavrsnaAnimacijaGotova=false;
	 pause=pauseOdPauzeGumica=false;
	tekPoceo=true;
	touchedMenu=false;// oznaèava na koji je dio ntaskbara kliknuto
	 touchedDodatneFunkcije=false;
	 touchedPauseResume=false;
	 touchedDolPro=false;
	 stisnutoNaPocetakValova=false;
	 dodajBotuneZaPocetakValova();
	 inicijalizirajPocmiValoveBotune();
	
 }
 public void iskljuciCrtanjeAnimacijeNaKraju(){
	
	    	zavrsnaAnimacijaPokrenuta=false;
	    	this.tekPoceoSaZavrsnomAnimacijom=true;
	    	this.tekpoceoSaPljeskanjem=true;
	    
 }
 public void iskljuciCrtanjeNaKraju(boolean b){
	 //   proslaBoja=0;
		//iskljuciCrtanje=b;
		crtanjeSvijetlaPodnozja=!b;
		iskljuciCrtanjeResetBotuna=b;
		iskljuciCrtanjeBackBotuna=b;
	    this.iskljuciCrtPodnozja=b;
	   
		if(this.izborLavaBomb!=null) {
			izborLavaBomb.iskljuciCrtanje(b);
		}
		if(this.izborRenforce!=null) {
			izborRenforce.iskljuciCrtanje(b);
		}
		if(this.izborMute!=null){
			izborMute.iskljuciCrtanje(b);
		}
		if(izbornikKontroleZvuka!=null){
			izbornikKontroleZvuka.iskljuciCrtanje(b);
		}
		if(this.izborExit!=null) {
			izborExit.iskljuciCrtanje(b);
		}
		if(this.izborRestart!=null) {
			izborRestart.iskljuciCrtanje(b);
		}
		
	}
 public void iskljuciCrtanjePotpuno(boolean b){
	 //   proslaBoja=0;
		iskljuciCrtanje=b;
		crtanjeSvijetlaPodnozja=!b;
	
		if(this.izborLavaBomb!=null) {
			izborLavaBomb.iskljuciCrtanje(b);
		}
		if(this.izborRenforce!=null) {
			izborRenforce.iskljuciCrtanje(b);
		}
		if(izborMute!=null){
			izborMute.iskljuciCrtanje(b);
		}
		if(izbornikKontroleZvuka!=null){
			izbornikKontroleZvuka.iskljuciCrtanje(b);
		}
		if(this.izborExit!=null) {
			izborExit.iskljuciCrtanje(b);
		}
		if(this.izborRestart!=null) {
			izborRestart.iskljuciCrtanje(b);
		}
		if(this.izborEndGame!=null) {
			izborEndGame.iskljuciCrtanje(b);
		}

	}    
public void iskljuciCrtanjeNekihUPauzi(boolean b){
	//proslaBoja=0;
	iskljuciCrtBodova=b;
	iskljuciCrtDolaskaProt=b;
	crtanjeSvijetlaPodnozja=!b;
	
	if(this.izborLavaBomb!=null) {
		izborLavaBomb.iskljuciCrtanje(b);
	}
	if(this.izborRenforce!=null) {
		izborRenforce.iskljuciCrtanje(b);
	}
	if(this.izborRenforce!=null) {
		izborRenforce.iskljuciCrtanje(b);
	}
	/*if(izbornikKontroleZvuka!=null){
		izbornikKontroleZvuka.iskljuciCrtanje(b);
	}
	if(this.izborExit!=null) {
		izborExit.iskljuciCrtanje(b);
	}*/

}  

public void setGameLogic(GameLogic g){
	GL=g;
}	
public void setTouchedMenuPolje(float x, float y){
	touchedMenu=true;
	touchedX=x;
	touchedY=y;
	} 	
public void setTouchedDodatneFunkcje(float x, float y){
	touchedDodatneFunkcije=true;
	touchedX=x;
	touchedY=y;
	}
public void setTouchedPauseResume(float x, float y){
	touchedPauseResume=true;
	touchedX=x;
	touchedY=y;
	}
public void setTouchedDolazakProt(float x, float y){
	this.touchedDolPro=true;
	touchedX=x;
	touchedY=y;
	}
public RectF getDimenzijeDolaskaProtivnik(){
	if(this.secDoNovVala>0)return this.recDolaProt;
	else return this.recLazni;// u slucaju da se neceka novi val vraca mu se rec u kojem su sve cetri tocke u nuli
}
public RectF getDimenzijeMenuPolja(){
	return rectMenu;
} 	
public RectF getDimenzijeDodatnihFunkcja(){
	return rectDodatneFunkcije;
}
public RectF getDimenzijePauseResume(){
	if(!pause){
		return rectPause;// ako nije stisnuto pause vraæa gdje se nalazi dugmiæ pause
	}
	else {
		 return rectResume;// ako je stisnuto pause vraæa gdje je resume digmiæ
	}
}
public void setBrojValova(int val){// samo preusmjerava poziv na faze igre
	 brojValova=val;
}
public void setTrenutniVal(int tval){
	  trenutniVal=tval;
	   if(trenutniVal>this.prosliVal){
			if(listaPocmiValoveIzbornika!=null){
            	for(int i=0;i< listaPocmiValoveIzbornika.size();i++){
            	     
            	        	listaPocmiValoveIzbornika.get(i).pokreniZavrsnuAnimaciju();
            	        
              	}
    	}
	   }
	   this.prosliVal=this.trenutniVal;
}
public void setUkupniNovac(int novac){
	ukupniNovac=novac;
}
public void setBrojZivota(int brzivota){
	brojZivota=brzivota;
}
public void setBodevi(int bodovi){
	this.bodovi=bodovi;
}
public void setSecDoNovogValaIPoziciju(int sec,float x, float y){
	this.secDoNovVala=sec;
	if(secDoNovVala<0) this.prosSecNovVala=-2;
	else if(this.secDoNovVala>0){
		poslaoNovceGameLogicu=false;//resetira na pocetku novog vala
	//	crtaKrajDolaska=true;
		if(tekPoceoSaCrtajOblacicIzbornikom){
			inicijalizirajPocmiValoveBotune();
			tekPoceoSaCrtajOblacicIzbornikom=false;
		}
		crtajOblacicDolaskaProtivnika=true;
	}
	xVala=x;
	yVala=y;
	
}
public boolean pokreniZavrsnuAnimacijuIJeliGotovaPobjeda(int brZvjezdica){
	
	this.animPobjeda=true;
	this.brZvjezdica=brZvjezdica;
	zavrsnaAnimacijaPokrenuta=true;
	iskljuciCrtanjeNaKraju(true);
	return zavrsnaAnimacijaGotova;
}
public boolean pokreniZavrsnuAnimacijuIJeliGotovaPoraz(){

	this.animPoraz=true;
	zavrsnaAnimacijaPokrenuta=true;
	iskljuciCrtanjeNaKraju(true);
	return zavrsnaAnimacijaGotova;
}
public void pokreniGameEndIzbornik(){
	if(this.izborEndGame!=null){
		this.izborEndGame.pokreniMojIzbornik(null);
	}
	else{
		GL.zavrsiFazuIVRatiNaMapu();
	}
}
///////////PRIVATNE METODE
public void dodajBotuneZaPocetakValova(){

	listaBotunaNaPocetkuXPozicija=null;
	listaBotunaNaPocetkuYPozicija=null;
	
	for(int i=0; i<this.GL.getListuCesta().length;i++){
		PutL put=(PutL)this.GL.getListuCesta()[i];
	      if(put.redniBroj()==0){
	    	dodajBotuneZaPocetakValova((int)put.getRect().centerX(),(int)put.getRect().centerY(), this.uiMan);
	      }
	}
}
private void stvoriKontroleZvuka(){ 
	 
	MusicManager.namjestiGlasnocu(MusicManager.maxJakostZvukaUOdnosuNaPojacanje*GL.faIgr.koeficijentPojacanjaZvuka);
	
	this.izbornikKontroleZvuka=new IzbornikUniverzalni(null,uiMan,1,3,302){
        float postoManjeZasjeOdBotunaMjeraca=10; 
        float pixManjeSjenaOdBotunMj;
        Paint paintSjene;
        float razMjerIBot= (velMjerPoj-velPojaSmanj)/2;
   	    boolean mute=false,max=false;
   	    
		Canvas canvas;
		float fps;UIManager uiMan;float PpCmX; float PpCmY;float pomCanX;
		float pomCanY;
		
		RectF  recSmanji,recPojacaj,recMjerac,recSjena;
		boolean tekPoceo=true;
       
        private void smanji(){
        	if(GL.faIgr.koeficijentPojacanjaZvuka>1){
        		
        		GL.faIgr.koeficijentPojacanjaZvuka=1;
        		
        	}
        	GL.faIgr.koeficijentPojacanjaZvuka-=0.1;
        	if(GL.faIgr.koeficijentPojacanjaZvuka<=0){
        		GL.faIgr.koeficijentPojacanjaZvuka=0;
        		mute=true;
        		pauzeResumeSoundTrik();
        	}
        	MusicManager.namjestiGlasnocu(MusicManager.maxJakostZvukaUOdnosuNaPojacanje*GL.faIgr.koeficijentPojacanjaZvuka);

        }
        private void pojacaj(){
        	if(GL.faIgr.koeficijentPojacanjaZvuka<0){
        		GL.faIgr.koeficijentPojacanjaZvuka=0;
        		
        	}
        	
        	mute=false;
        	pauzeResumeSoundTrik();
        	GL.faIgr.koeficijentPojacanjaZvuka+=0.1;
        	if(GL.faIgr.koeficijentPojacanjaZvuka>1){
        		GL.faIgr.koeficijentPojacanjaZvuka=1;
        		max=true;
        	}
        	MusicManager.namjestiGlasnocu(MusicManager.maxJakostZvukaUOdnosuNaPojacanje*GL.faIgr.koeficijentPojacanjaZvuka);
        }
        
	    private void pomakniRectove(){
	    	RectF recProz=this.getGlavniRectPrikaza();
	    
	    	this.recSmanji.set(recProz.left+razMjerIBot, recProz.bottom-velPojaSmanj, recProz.left+razMjerIBot+velPojaSmanj, recProz.bottom);
	    
	    	recMjerac.set(recSmanji.right+razMjerIBot,recProz.bottom-velMjerPoj, recSmanji.right+razMjerIBot+velMjerPoj,recProz.bottom-velMjerPoj+velMjerPoj);
	    	recPojacaj.set(recMjerac.right+razMjerIBot, recProz.bottom-velPojaSmanj, recMjerac.right+razMjerIBot+velPojaSmanj,recProz.bottom);
	        
	    	
	    	recSjena.set(recMjerac.left+pixManjeSjenaOdBotunMj+GL.faIgr.koeficijentPojacanjaZvuka*recSjena.height(),recMjerac.top+pixManjeSjenaOdBotunMj,
					recMjerac.right-pixManjeSjenaOdBotunMj, recMjerac.bottom-pixManjeSjenaOdBotunMj);
	    
	    }
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			if(brTP==1){
			   this.smanji();
			}
			else if(brTP==2){
				    if(mute){
				    	mute=false;
				    	pauzeResumeSoundTrik();
				    	GL.faIgr.koeficijentPojacanjaZvuka=0.5f;
				    	
				    	
				    	
				    
				    }
				    else  {
				    	GL.faIgr.koeficijentPojacanjaZvuka=0;
				    	
				    	max=false;
				    	mute=true;
				    	pauzeResumeSoundTrik();
				    	
				    }
				    
				}
			else if(brTP==3){
				this.pojacaj();
			}
			this.pokreniMojIzbornik(null);// ovo stavljam samo zbog zastavice pokrenutIzbornik koja treba biti truze a bi se nastavio dalje izvršavati izbornik
		}
        @TargetApi(Build.VERSION_CODES.FROYO)
		private void pauzeResumeSoundTrik(){
        	if(mute){
        		 for(int i = 0; i< FazeIgre.listaSvikPustenihZvukova.size(); i++){
        		     
        			     
        			 GL.faIgr.soundPool.stop(FazeIgre.listaSvikPustenihZvukova.get(i));
        		  }
        		
        		
        	}
        	
        }
		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			this.canvas=can;
		    this.fps=fps;
		    this.uiMan=uiMan;
		    this.PpCmX=PpCmX;
		    this.PpCmY=PpCmY;
		    this.pomCanX=pomCanX;
		    this.pomCanY=pomCanY;
		    
		
	   			
	   }
		@Override
		public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		    this.canvas=can;
		    this.fps=fps;
		    this.uiMan=uiMan;
		    this.PpCmX=PpCmX;
		    this.PpCmY=PpCmY;
		    this.pomCanX=pomCanX;
		    this.pomCanY=pomCanY;
		if(tekPoceo){
			
			RectF recProz=this.getGlavniRectPrikaza();
			this.recSmanji=new RectF();
			this.recSmanji.set(recProz.left+razMjerIBot, recProz.bottom-velPojaSmanj, recProz.left+razMjerIBot+velPojaSmanj, recProz.bottom);
			
			this.recMjerac=new RectF();
			recMjerac.set(recSmanji.right+razMjerIBot,recProz.bottom-velMjerPoj, recSmanji.right+razMjerIBot+velMjerPoj,recProz.bottom-velMjerPoj+velMjerPoj);
			pixManjeSjenaOdBotunMj=this.postoManjeZasjeOdBotunaMjeraca*recMjerac.width()/100;
			
			
			this.recPojacaj=new RectF();
			recPojacaj.set(recMjerac.right+razMjerIBot, recProz.bottom-velPojaSmanj, recMjerac.right+razMjerIBot+velPojaSmanj,recProz.bottom);
			
			this.recSjena=new RectF();
			recSjena.set(0, 0,
					recMjerac.width()-2*pixManjeSjenaOdBotunMj,recMjerac.width()-2*pixManjeSjenaOdBotunMj);
			
			
			
			
			paintSjene=new Paint();
			paintSjene.setColor(Color.BLACK);
			paintSjene.setAlpha(150);
			
			tekPoceo=false;
		}
		if(GL.faIgr.koeficijentPojacanjaZvuka>=1){
			GL.faIgr.koeficijentPojacanjaZvuka=1;
			this.max=true;
			this.mute=false;
		}
		else if(GL.faIgr.koeficijentPojacanjaZvuka<=0){
			GL.faIgr.koeficijentPojacanjaZvuka=0;
			this.max=false;
			this.mute=true;
		}
		else{
			this.max=false;
			this.mute=false;
		}
		this.pomakniRectove();
		///smanjenje
		 sprKontroleZvuka.nacrtajSprite(can, 0, 0,0,recSmanji);
		///mjerac
		 sprKontroleZvuka.nacrtajSprite(can, 1, 0,0,recMjerac);
	     can.drawRect(recSjena, paintSjene);
		//pojacanje	
	     sprKontroleZvuka.nacrtajSprite(can, 2, 0,0,recPojacaj);	
			
		}
		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
		
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
    	
    };
  //  izbornikKontroleZvuka.crtajNacrtajVanjskiIznadBezObzira(true);
	
       
	 
	  
    
    izbornikKontroleZvuka.postaviStatickiIzbornik();
    izbornikKontroleZvuka.postaviLijeviVrhProzoraPix(this.sirEkr/2-velMjerPoj*1.5f,this.visEkr-80*velMjerPoj/100);
   // izbornikKontroleZvuka.postaviRazmakIzmeduPoljaPix(velMjerPoj/10,velMjerPoj/10);
    izbornikKontroleZvuka.crtajIkadaNijePokrenut(true);
    izbornikKontroleZvuka.postaviVelicinuPoljaUPix(velMjerPoj,80*velMjerPoj/100);
    izbornikKontroleZvuka.pokreniMojIzbornik(null);
	   uiMan.dodajElementUListu(izbornikKontroleZvuka,4);
      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
      
}
private void inicijalizirajExitIzbornik(){
	
	final float efekSir=this.uiMan.getSirEkrana();
	final float efekVis=this.uiMan.getVisEkrana();
	
	this.izborExit=new IzbornikUniverzalni( null,uiMan,2,1,302){
		boolean izracunataVelProz=false;
		 boolean tekPoc=true;
		 float pomakX, temDx, prosliX;
		 GameEvent ge=new GameEvent(this);
		 RectF rec=new RectF();
		 RectF recNo=new RectF();
		 RectF recYes=new RectF();
		 
		  @Override
	    public boolean	nacrtajUvod(float fps,Canvas can){
			 
			 if(tekPoc) {
				 ge.jesamLiZiv=true;
				 izracunataVelProz=true;
				 izborExit.crtajIkadaNijePokrenut(true);
              // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
				 izborExit.postaviLijeviVrhProzoraPix(-izborExit.dajMiRectCijelogProzora().width(), efekVis/2-izborExit.dajMiRectCijelogProzora().height()/2);
				 izborExit.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
			     tekPoc=false;
			     temDx=0;
			     uiMan.iskljuciTouchEvente();
			     pomakX=izborExit.dajMiRectCijelogProzora().width()+(efekSir-izborExit.dajMiRectCijelogProzora().width())/2;
			   
			     this.rec.set(izborExit.dajMiRectCijelogProzora());
			     prosliX=this.rec.left;
			     
			     
			  }
			   temDx=this.rec.left- prosliX;
			   prosliX=this.rec.left;
				this.pomakniIzbornikAkomulirajuci(temDx, 0); 
				//rec.set(this.dajMiRectCijelogProzora());
				tekPoc=exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,pomakX,0,1f,0,this. rec);
				izborExit.nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
				//}
			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
	    	 if(tekPoc){
	    		
	    		
	    		 uiMan.ukljuciTouchEvente();
	    		 this.postaviLijeviVrhProzoraPix(efekSir/2-izborExit.dajMiRectCijelogProzora().width()/2,  efekVis/2-izborExit.dajMiRectCijelogProzora().height()/2);
	    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
	    		//if(postojiUpgrade)reset.pokreniMojIzbornik(null);
	    		uiMan.setOznacenSam(this);
	    		//this.crtajProzorBezObziraNaSve(false);
	    	 }
			 return  tekPoc;
	    	}
		  public boolean nacrtajKraj(float fps,Canvas can){
				 if(tekPoc) {
					
					  ge.jesamLiZiv=true;
  			
  			     
                  // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
  				 //this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
  			     tekPoc=false;
  				 this.postaviLijeviVrhProzoraPix(efekSir/2-izborExit.dajMiRectCijelogProzora().width()/2,  efekVis/2-izborExit.dajMiRectCijelogProzora().height()/2);
	    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
  			     uiMan.iskljuciTouchEvente();
  			    
  			     //this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
  			     this.rec.set(izborExit.dajMiRectCijelogProzora());
  			     prosliX=this.rec.left;
  			     
  			    
  			  }
  			//if(!translacijaGotova){
  			
				 temDx=this.rec.left- prosliX;
				 prosliX=this.rec.left;
				 izborExit.pomakniIzbornikAkomulirajuci(temDx, 0);
  				 //rec.set(this.dajMiRectCijelogProzora());
  				 tekPoc= exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,-pomakX,0,1f,0, this.rec);
  				//nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
  				//}
  			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
  			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
		    	 if(tekPoc){
		    		 //izborExit.crtajIkadaNijePokrenut(false);
		    		 izborExit.crtajIkadaNijePokrenut(false);
		    		 uiMan.ukljuciTouchEvente();
		    		 izborExit.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
		    		 izborExit.postaviLijeviVrhProzoraPix(-izborExit.dajMiRectCijelogProzora().width(),efekVis/2-izborExit.dajMiRectCijelogProzora().height()/2);
		    		//this.crtajProzorBezObziraNaSve(false);
		    	 }
  			 return  tekPoc;
			  
		  }   
		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
  			  this.pokreniZavrsnuAnimaciju();
  		  }
		 @Override
		public void pokreniMojIzbornik(LinkedList<Integer> listaNekoristenih){
	        super.pokreniMojIzbornik(listaNekoristenih);
	        uiMan.postaviTempUniverzalniIzbornik(izborExit);
	  
		}
		  /*public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY)
		  {
		
			 if(pokrenutExitIzbornik&&izracunataVelProz){
				 exitSprite.nacrtajSprite(can, 1, 0, 0, this.getGlavniRectPrikaza());
				 }
		     
			 // this.iskljuciCrtanje(iskljuciCrtanje);
			  
		  }*/
		 
		 @Override
			public void backBotunStisnut(){
			   this.pokreniZavrsnuAnimaciju();
			}
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			
			if(brTP==1){
				
				GL.zavrsiFazuIVRatiNaMapu();
				
			}
           else if (brTP==2){// 
        	    this.pokreniZavrsnuAnimaciju();
        	
			}
		}
		  public void zavrsenaZavrsnaAnimacija(){
			  uiMan.ukljuciTouchEvente();
		     if(!pauseOdPauzeGumica){
		    	 kliknutoNaResume();
		     }
		    izborExit.postaviLijeviVrhProzoraPix(-10000, -10000);
			pokrenutExitIzbornik=false;
			uiMan.postaviTempUniverzalniIzbornik(null);
			uiMan.setNisamViseOznacen(izborExit);
		  }
		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
   	
   };
   
   float velProz=(this.uiMan.getSirEkrana()-this.rectResume.width())/2  - 2*this.velPojaSmanj;
      
   
   
   
       Typeface font = Typeface.createFromAsset(
		     uiMan.context.getAssets(), 
		    "fonts/black.ttf");

	   TextPaint tp=new TextPaint();
	   tp.setAntiAlias(true);
	   tp.setTypeface(font);
	   tp.setARGB(250, 56, 48,79);
	  //tp.setColor(Color.BLUE);
	   tp.setStyle(Style.FILL);
	   TextPaint tp2=new TextPaint();
	   tp2.set(tp);
	   tp2.setARGB(250, 134, 1, 1);
	   izborExit.postaviStatickiIzbornik();
	   izborExit.crtajIkadaNijePokrenut(false);
	   
	   izborExit.postaviDodatnuSlicicuZaDugmic(1, this.exitSprite, 3, 0, 0);
	   izborExit.postaviDodatnuSlicicuZaDugmic(2, this.exitSprite, 2, 0, 0);
	  // izbor.postaviTextDugmica(2, "<< Back",tp);
	   float odnos =(exitSprite.getSirRezanja(1)/exitSprite.getVisRezanja(1));
	 
	   float visProzora= 3*efekVis/5;
	   float sirProzora=odnos*visProzora;
	   float marginaVertikalna=visProzora/8;
	   float marginaHorizontalna=odnos*marginaVertikalna;
	   float visPolja=visProzora/2-2*marginaVertikalna;
	   float sirPolja=sirProzora-2*marginaHorizontalna;
	
	   izborExit.postaviMargineLijevaDesnaGornjaDonjaPix( marginaHorizontalna,marginaVertikalna, marginaHorizontalna, marginaVertikalna);
	   izborExit.crtajProzorZajdnoSaAnimacijomUvodaIkraja();
	   izborExit.postaviDirektnoGlavnuSlikuProzora(exitSprite, 1, 0, 0);
	   //izborExit.postaviNaslovIzbornikaPix("Exit game?:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
	   //izborExit.pokreniMojIzbornik(null);
	   //izborExit.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
	   izborExit.postaviVelicinuPoljaUPix(sirPolja,visPolja);
	   uiMan.onBackPressJeNormalan=true;

       uiMan.postaviTempUniverzalniIzbornik(izborExit);// funkcija sluzi za back botun 	   
       uiMan.dodajElementUListu(izborExit,4);
}
private void inicijalizirajRestartIzbornik(){
	
	final float efekSir=this.uiMan.getSirEkrana();
	final float efekVis=this.uiMan.getVisEkrana();
	if(izborRestart==null){
	this.izborRestart=new IzbornikUniverzalni( null,uiMan,2,1,302){
		boolean izracunataVelProz=false;
		 boolean tekPoc=true;
		 float pomakX, temDx, prosliX;
		 GameEvent ge=new GameEvent(this);
		 RectF rec=new RectF();
		 RectF recNo=new RectF();
		 RectF recYes=new RectF();
		
		  @Override
	    public boolean	nacrtajUvod(float fps,Canvas can){
			 
			 if(tekPoc) {
				 ge.jesamLiZiv=true;
				 izracunataVelProz=true;
				 izborRestart.crtajIkadaNijePokrenut(true);
              // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
				 izborRestart.postaviLijeviVrhProzoraPix(efekSir, efekVis/2-izborRestart.dajMiRectCijelogProzora().height()/2);
				 izborRestart.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
			     tekPoc=false;
			     temDx=0;
			     uiMan.iskljuciTouchEvente();
			     pomakX=izborRestart.dajMiRectCijelogProzora().width()+(efekSir-izborRestart.dajMiRectCijelogProzora().width())/2;
			   
			     this.rec.set(izborRestart.dajMiRectCijelogProzora());
			     prosliX=this.rec.left;
			     
			     
			  }
			   temDx=this.rec.left- prosliX;
			   prosliX=this.rec.left;
				this.pomakniIzbornikAkomulirajuci(temDx, 0); 
				//rec.set(this.dajMiRectCijelogProzora());
				tekPoc=exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,-pomakX,0,1f,0,this. rec);
				izborRestart.nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
				//}
			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
	    	 if(tekPoc){
	    		
	    		
	    		 uiMan.ukljuciTouchEvente();
	    		 this.postaviLijeviVrhProzoraPix(efekSir/2-izborRestart.dajMiRectCijelogProzora().width()/2,  efekVis/2-izborRestart.dajMiRectCijelogProzora().height()/2);
	    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
	    		//if(postojiUpgrade)reset.pokreniMojIzbornik(null);
	    		uiMan.setOznacenSam(this);
	    		//this.crtajProzorBezObziraNaSve(false);
	    	 }
			 return  tekPoc;
	    	}
		  public boolean nacrtajKraj(float fps,Canvas can){
				 if(tekPoc) {
					
					  ge.jesamLiZiv=true;
  			
  			     
                  // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
  				 //this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
  			     tekPoc=false;
  				 this.postaviLijeviVrhProzoraPix(efekSir/2-izborRestart.dajMiRectCijelogProzora().width()/2,  efekVis/2-izborRestart.dajMiRectCijelogProzora().height()/2);
	    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
  			     uiMan.iskljuciTouchEvente();
  			    
  			     //this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
  			     this.rec.set(izborRestart.dajMiRectCijelogProzora());
  			     prosliX=this.rec.left;
  			     
  			    
  			  }
  			//if(!translacijaGotova){
  			
				 temDx=this.rec.left- prosliX;
				 prosliX=this.rec.left;
				 izborRestart.pomakniIzbornikAkomulirajuci(temDx, 0);
  				 //rec.set(this.dajMiRectCijelogProzora());
  				 tekPoc= exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,pomakX,0,1f,0, this.rec);
  				//nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
  				//}
  			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
  			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
		    	 if(tekPoc){
		    		 //izborExit.crtajIkadaNijePokrenut(false);
		    		 izborRestart.crtajIkadaNijePokrenut(false);
		    		 uiMan.ukljuciTouchEvente();
		    		 izborRestart.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
		    		 izborRestart.postaviLijeviVrhProzoraPix(efekSir,efekVis/2-izborRestart.dajMiRectCijelogProzora().height()/2);
		    		//this.crtajProzorBezObziraNaSve(false);
		    	 }
  			 return  tekPoc;
			  
		  }   
		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
  			  this.pokreniZavrsnuAnimaciju();
  		  }
		 @Override
		public void pokreniMojIzbornik(LinkedList<Integer> listaNekoristenih){
	        super.pokreniMojIzbornik(listaNekoristenih);
	        uiMan.postaviTempUniverzalniIzbornik(izborRestart);
	        tekPoc=true;
	  
		}
		  /*public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY)
		  {
		
			 if(pokrenutExitIzbornik&&izracunataVelProz){
				 exitSprite.nacrtajSprite(can, 1, 0, 0, this.getGlavniRectPrikaza());
				 }
		     
			 // this.iskljuciCrtanje(iskljuciCrtanje);
			  
		  }*/
		 
		 @Override
			public void backBotunStisnut(){
			   this.pokreniZavrsnuAnimaciju();
			}
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			
			if(brTP==1){
				uiMan.postaviTempUniverzalniIzbornik(izborExit);
				GL.resetirajIgru();// akonije po�?eosa završnomanimacijom onda se više nemože resetirati
				this.pokreniZavrsnuAnimaciju();
			
				
				
			}
            else if (brTP==2){// 
            	uiMan.postaviTempUniverzalniIzbornik(izborExit);
        	    this.pokreniZavrsnuAnimaciju();
        	
			}
		}
		  public void zavrsenaZavrsnaAnimacija(){
			  uiMan.ukljuciTouchEvente();
		     if(!pauseOdPauzeGumica){
		    	 kliknutoNaResume();
		     }
		     izborRestart.postaviLijeviVrhProzoraPix(-10000, -10000);
			pokrenutExitIzbornik=false;
			uiMan.postaviTempUniverzalniIzbornik(izborExit);
			uiMan.setNisamViseOznacen(izborRestart);
		  }
		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
   	
   };
   
   float velProz=(this.uiMan.getSirEkrana()-this.rectResume.width())/2  - 2*this.velPojaSmanj;
      
   
   
   
       Typeface font = Typeface.createFromAsset(
		     uiMan.context.getAssets(), 
		    "fonts/black.ttf");

	   TextPaint tp=new TextPaint();
	   tp.setAntiAlias(true);
	   tp.setTypeface(font);
	   tp.setARGB(250, 56, 48,79);
	  //tp.setColor(Color.BLUE);
	   tp.setStyle(Style.FILL);
	   TextPaint tp2=new TextPaint();
	   tp2.set(tp);
	   tp2.setARGB(250, 134, 1, 1);
	   izborRestart.postaviStatickiIzbornik();
	   izborRestart.crtajIkadaNijePokrenut(false);
	   
	   izborRestart.postaviDodatnuSlicicuZaDugmic(1, this.exitSprite, 4, 0, 0);
	   izborRestart.postaviDodatnuSlicicuZaDugmic(2, this.exitSprite, 2, 0, 0);
	  // izbor.postaviTextDugmica(2, "<< Back",tp);
	   float odnos =(exitSprite.getSirRezanja(1)/exitSprite.getVisRezanja(1));
	 
	   float visProzora=3*efekVis/5;
	   float sirProzora=odnos*visProzora;
	   float marginaVertikalna=visProzora/8;
	   float marginaHorizontalna=odnos*marginaVertikalna;
	   float visPolja=visProzora/2-2*marginaVertikalna;
	   float sirPolja=sirProzora-2*marginaHorizontalna;
	
	   izborRestart.postaviMargineLijevaDesnaGornjaDonjaPix( marginaHorizontalna,marginaVertikalna, marginaHorizontalna, marginaVertikalna);
	   izborRestart.crtajProzorZajdnoSaAnimacijomUvodaIkraja();
	   izborRestart.postaviDirektnoGlavnuSlikuProzora(exitSprite, 1, 0, 0);
	   //izborExit.postaviNaslovIzbornikaPix("Exit game?:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
	   //izborExit.pokreniMojIzbornik(null);
	   //izborExit.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
	   izborRestart.postaviVelicinuPoljaUPix(sirPolja,visPolja);
		
	   
       uiMan.postaviTempUniverzalniIzbornik(izborRestart);// funkcija sluzi za back botun 	   
	}
	uiMan.dodajElementUListu(izborRestart,4);
}
private void inicijalizirajGameEndIzbornik(){

	final float efekSir=this.uiMan.getSirEkrana();
	final float efekVis=this.uiMan.getVisEkrana();
	if(izborEndGame==null){
	this.izborEndGame=new IzbornikUniverzalni( null,uiMan,2,1,302){
		private boolean pokrenutIzbornik=false;
		boolean izracunataVelProz=false;
		 boolean tekPoc=true;
		 float pomakY, temDy, prosliY;
		 GameEvent ge=new GameEvent(this);
		 RectF rec=new RectF();
		 RectF recNo=new RectF();
		 RectF recYes=new RectF();
		
		  @SuppressWarnings("static-access")
		@Override
	    public boolean	nacrtajUvod(float fps,Canvas can){
			 
			 if(tekPoc) {
				 pokrenutIzbornik=true;
				 ge.jesamLiZiv=true;
				 izracunataVelProz=true;
				 izborEndGame.crtajIkadaNijePokrenut(true);
              // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
				//izborEndGame.postaviLijeviVrhProzoraPix(efekSir/2-izborEndGame.dajMiRectCijelogProzora().width()/2, -efekVis);
				 izborEndGame.postaviLijeviVrhProzoraPix(efekSir/2-izborEndGame.dajMiRectCijelogProzora().width()/2, -this.getGlavniRectPrikaza().height());
				 izborEndGame.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
			     tekPoc=false;
			     temDy=0;
			     uiMan.iskljuciTouchEvente();
			    // pomakY=efekVis+(efekVis-izborEndGame.dajMiRectCijelogProzora().height());
			//   pomakY= recPljeskanja1.top-izborEndGame.dajMiRectCijelogProzora().bottom;
			     pomakY=this.getGlavniRectPrikaza().height()+pomakYEndGameIzb;//- recPljeskanja1.height()/4;
			     this.rec.set(izborEndGame.dajMiRectCijelogProzora());
			     prosliY=this.rec.top;
			     
			     
			  }
			   temDy=this.rec.top- prosliY;
			   prosliY=this.rec.top;
				this.pomakniIzbornikAkomulirajuci(0,  temDy); 
				//rec.set(this.dajMiRectCijelogProzora());
				tekPoc=exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,pomakY,0,1f,this. rec);
				izborEndGame.nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
				//}
			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
	    	 if(tekPoc){
	    		
	    		
	    		 uiMan.ukljuciTouchEvente();
	    		// this.postaviLijeviVrhProzoraPix(efekSir/2-izborEndGame.dajMiRectCijelogProzora().width()/2,  efekVis/2-izborEndGame.dajMiRectCijelogProzora().height());
	    		 this.postaviLijeviVrhProzoraPix(efekSir/2-izborEndGame.dajMiRectCijelogProzora().width()/2,pomakYEndGameIzb);
	    		 this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
	    		//if(postojiUpgrade)reset.pokreniMojIzbornik(null);
	    		uiMan.setOznacenSam(this);
	    		//this.crtajProzorBezObziraNaSve(false);
	    	 }
			 return  tekPoc;
	    	}
		  @SuppressWarnings("static-access")
		public boolean nacrtajKraj(float fps,Canvas can){
			  pokrenutIzbornik=false;
				/* if(tekPoc) {
					
					  ge.jesamLiZiv=true;
  			
  			     
                  // sprUpgGlavniPr.animacijaSlaganjeTranslacija(-rec.width(),0,1f,0, fps, rec);
  				 //this.postaviLijeviVrhProzoraPix(poz.dajMiRectCijelogProzora().width(), 0);
  			     tekPoc=false;
  			   this.postaviLijeviVrhProzoraPix(efekSir/2-izborEndGame.dajMiRectCijelogProzora().width()/2,  recPljeskanja1.top-izborEndGame.dajMiRectCijelogProzora().height()); 
  			   this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
  			     uiMan.iskljuciTouchEvente();
  			    
  			     //this.pomakniIzbornikAkomulirajuciApsolutno(pomakX, 0);
  			     this.rec.set(izborEndGame.dajMiRectCijelogProzora());
  			     prosliY=this.rec.top;
  			     
  			    
  			  }
  			//if(!translacijaGotova){
  			
				 temDy=this.rec.top- prosliY;
				 prosliY=this.rec.top;
				 izborEndGame.pomakniIzbornikAkomulirajuci(0, temDy);
  				 //rec.set(this.dajMiRectCijelogProzora());
  				 tekPoc= exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(ge,0,-pomakY,0,1f, this.rec);
  				//nacrtajProzor888();// pozivam je ovdje jer se ne crta dok se ne zavrsi izbornik
  				//}
  			// sprUpgGlavniPr.animacijaSlaganjeNacrtaj(can, rec, 1, 0, fps, null,false);
  			//tekPoc= sprUpgGlavniPr.animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(can, 1, 0, 10, 10, fps,1f, rec, null,false);
		    	 if(tekPoc){
		    		 //izborExit.crtajIkadaNijePokrenut(false);
		    		 izborEndGame.crtajIkadaNijePokrenut(false);
		    		 uiMan.ukljuciTouchEvente();
		    		 izborEndGame.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
		    		 izborEndGame.postaviLijeviVrhProzoraPix(efekSir/2-izborEndGame.dajMiRectCijelogProzora().width()/2, -efekVis);
		    		//this.crtajProzorBezObziraNaSve(false);
		    	 }*/
			  postaviLijeviVrhProzoraPix(-10000, -10000);
			  tekPoc=true;
  			 return  tekPoc;
			  
		  }   
		 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
  			//  this.pokreniZavrsnuAnimaciju();
  		  }
		 @Override
		public void pokreniMojIzbornik(LinkedList<Integer> listaNekoristenih){
	        super.pokreniMojIzbornik(listaNekoristenih);
	        uiMan.postaviTempUniverzalniIzbornik(izborEndGame);
	        tekPoc=true;
	  
		}
		  /*public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY)
		  {
		
			 if(pokrenutExitIzbornik&&izracunataVelProz){
				 exitSprite.nacrtajSprite(can, 1, 0, 0, this.getGlavniRectPrikaza());
				 }
		     
			 // this.iskljuciCrtanje(iskljuciCrtanje);
			  
		  }*/
		 @Override
		 public float getX() {
		 	// TODO Auto-generated method stub
		 	if( pokrenutIzbornik) return 0;// naredne 4 varijable su važne jer bi inaèe bili izabrani objekti ispod izbornika i isto tako se nebi dobili x i y
		 	else return super.getX();
		 }
		 @Override
		 public float getY() {
		 	// TODO Auto-generated method stub
			 if( pokrenutIzbornik) return 0;
			 else return super.getY();
		 }
		 @Override
		 public float getSir() {
		 	// TODO Auto-generated method stub
			 if( pokrenutIzbornik) return uiMan.getSirEkrana();
			 else return  super.getSir();
		 }
		 @Override
		 public float getVis() {
		 	// TODO Auto-generated method stub
			 if( pokrenutIzbornik) return uiMan.getVisEkrana();
			 else return super.getVis();
		 }
		 @Override
			public void backBotunStisnut(){
			//   this.pokreniZavrsnuAnimaciju();
			}
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			
			if(brTP==1){
				GL.zavrsiFazuIVRatiNaMapu();
				
			}
            else if (brTP==2){// 
            	iskljuciCrtanjeAnimacijeNaKraju();
            	iskljuciCrtanjeNaKraju(false);
            	uiMan.postaviTempUniverzalniIzbornik(izborExit);
				GL.resetirajIgru();// akonije po�?eosa završnomanimacijom onda se više nemože resetirati
				this.pokreniZavrsnuAnimaciju();
        	
			}
		}
		  public void zavrsenaZavrsnaAnimacija(){
			  uiMan.ukljuciTouchEvente();
		     if(!pauseOdPauzeGumica){
		    	 kliknutoNaResume();
		     }
		     izborEndGame.postaviLijeviVrhProzoraPix(-10000, -10000);
			pokrenutExitIzbornik=false;
			uiMan.postaviTempUniverzalniIzbornik(izborExit);
			uiMan.setNisamViseOznacen(izborEndGame);
		  }
		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
   	
   };
   
   float velProz=(this.uiMan.getSirEkrana()-this.rectResume.width())/2  - 2*this.velPojaSmanj;
      
   
   
   
       Typeface font = Typeface.createFromAsset(
		     uiMan.context.getAssets(), 
		    "fonts/black.ttf");

	   TextPaint tp=new TextPaint();
	   tp.setAntiAlias(true);
	   tp.setTypeface(font);
	   tp.setARGB(250, 56, 48,79);
	  //tp.setColor(Color.BLUE);
	   tp.setStyle(Style.FILL);
	   TextPaint tp2=new TextPaint();
	   tp2.set(tp);
	   tp2.setARGB(250, 134, 1, 1);
	   izborEndGame.postaviStatickiIzbornik();
	   izborEndGame.crtajIkadaNijePokrenut(false);
	   
	   izborEndGame.postaviDodatnuSlicicuZaDugmic(1, this.exitSprite, 3, 0, 0);
	   izborEndGame.postaviDodatnuSlicicuZaDugmic(2, this.exitSprite, 4, 0, 0);
	  // izbor.postaviTextDugmica(2, "<< Back",tp);
	   float odnos =(exitSprite.getSirRezanja(1)/exitSprite.getVisRezanja(1));
	 
	   float visProzora= 3*efekVis/5;
	   float sirProzora=odnos*visProzora;
	   float marginaVertikalna=visProzora/8;
	   float marginaHorizontalna=odnos*marginaVertikalna;
	   float visPolja=visProzora/2-2*marginaVertikalna;
	   float sirPolja=sirProzora-2*marginaHorizontalna;
	   this.izborEndGame.postaviLijeviVrhProzoraPix(-10000, -10000);
	   izborEndGame.postaviMargineLijevaDesnaGornjaDonjaPix( marginaHorizontalna,marginaVertikalna, marginaHorizontalna, marginaVertikalna);
	   izborEndGame.crtajProzorZajdnoSaAnimacijomUvodaIkraja();
	   izborEndGame.postaviDirektnoGlavnuSlikuProzora(exitSprite, 1, 0, 0);
	   //izborExit.postaviNaslovIzbornikaPix("Exit game?:", sirPolja,visPolja,desnaMargina+razmakX/3, visPolja, tp);
	   //izborExit.pokreniMojIzbornik(null);
	   //izborExit.postaviRazmakIzmeduPoljaPix(razmakX, razmakY);
	   izborEndGame.postaviVelicinuPoljaUPix(sirPolja,visPolja);
	
	}
	uiMan.dodajElementUListu(izborEndGame,4);
	izborEndGame.izracVelProzora();
	velGameEndIzb= izborEndGame.getGlavniRectPrikaza().width();
}
///////////INICIJALIZACIJA DODATNIH BOTUNA///////////
private IzbornikUniverzalni   inicijalizirajPocmiValoveBotun(float x, float y){ 
	
	IzbornikUniverzalni izbor=new IzbornikUniverzalni( null,uiMan,1,1,302){
       // IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
	    private GameEvent ge=new GameEvent(null);
	    private GameEvent ge2=new GameEvent(null);
	    private boolean tekPoceoKraj=true;
	    
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			if(brTP==1){
				if(!crtajOblacicDolaskaProtivnika){//ako je tek pocetak staze
				        stisnutoNaPocetakValova=true;
				        uiMan.GL.pokreniValove();
				        this.pokreniZavrsnuAnimaciju();
				}
				else{// akoje novi val
					kliknutoNaDolazakProtivnika();
					this.pokreniZavrsnuAnimaciju();
					crtajOblacicDolaskaProtivnika=false;
				}
			}
			
		}

		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean nacrtajUvod(float fps, Canvas can){
			ge.jesamLiZiv=true;
			ge2.jesamLiZiv=true;
			return true;
		}
		@Override
		public boolean nacrtajKraj(float fps, Canvas can){
			boolean b=false;
		if(!iskljuciCrtDolaskaProt){	
			if(tekPoceoKraj){
			 	uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukPocetakVala,100, 160, 0, 0);
				if(listaPocmiValoveIzbornika!=null){
	            	for(int i=0;i< listaPocmiValoveIzbornika.size();i++){
	            	        if(listaPocmiValoveIzbornika.get(i)!=this){
	            	        	listaPocmiValoveIzbornika.get(i).pokreniZavrsnuAnimaciju();
	            	        }
	              	}
        	}
				ge=new GameEvent(null);
				ge.jesamLiZiv=true;
				tekPoceoKraj=false;
			}
		   b=	dolProtSprite.vrtiAnimacijuReda(can, ge, 2, 0, this.getGlavniRectPrikaza(),null);//nacrtajSprite(can, 0, this.brSlObl, 0,this.get);
			if(b){
				tekPoceoSaCrtajOblacicIzbornikom=true;
		
				uiMan.makniObjekt(this);
			}
		}
		    return b;
		}
		@Override
		public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			 //POMAK OBLAČIĆA AKO JE IZVAN EKRANA
			if(!iskljuciCrtDolaskaProt){
			if(!stisnutoNaPocetakValova&&!crtajOblacicDolaskaProtivnika){// pocetak staze
				/////////CRTANJE OBLACICA
		  
					dolProtSprite.vrtiAnimacijuReda(can, ge, 0, 0, this.getGlavniRectPrikaza(),null);//nacrtajSprite(can, 0, this.brSlObl, 0,this.get);
	
	           //////////CRTANJE HODANJA
					dolProtSprite.vrtiAnimacijuReda(can, ge, 1, 0, this.getGlavniRectPrikaza(),null);
				
				
			}
			else if(crtajOblacicDolaskaProtivnika){// novi val
				
				if(secDoNovVala>0){
				    /////////CRTANJE OBLACICA
				  
				    dolProtSprite.vrtiAnimacijuReda(can, ge, 0, 0, this.getGlavniRectPrikaza(),null);//nacrtajSprite(can, 0, this.brSlObl, 0,this.get);
  
                 //////////CRTANJE HODANJA
		     		odrediAlfuZaHodanje();
			    	dolProtSprite.vrtiAnimacijuReda(can, ge, 1, 0, this.getGlavniRectPrikaza(),paintZaHodanje);
				}
				
			}
			}
			
		}

		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
    	
    };
       izbor.crtajNacrtajVanjskiIznadBezObzira(true);
	 
       
	  /* float sirPolja=1*this.uiMan.getSirEkrana()/10;
	   float visPolja=1*this.uiMan.getSirEkrana()/10;*/
       float sirPolja=sirDolPro*this.PpCmX;
	   float visPolja=visDolPro*this.PpCmY;
	//   izbor.postaviStatickiIzbornik();
	   izbor.postaviLijeviVrhProzoraPix(x-sirPolja/2,y-visPolja/2);
	   izbor.pokreniMojIzbornik(null);
	   //izbor.crtajIkadaNijePokrenut(true);
	   izbor.postaviVelicinuPoljaUPix(sirPolja,visPolja);
	 
	   uiMan.dodajElementUListu(izbor,3);
      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	  
	   return izbor;
   
}
private void inicijalizirajMuteBotun(){ 
	
	izborMute=new IzbornikUniverzalni( null,uiMan,1,1,302){
       // IzbornikUniverzalni upgProzor=pokreniUpgradeProzorGlavniIzbornik(this);
	
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			if(brTP==1){
				MusicManager.setMute(!MusicManager.getIsMute());
			}
			
		}

		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		   if(MusicManager.getIsMute()){
			   sprMute.nacrtajSprite(can, 1, 1, 0,dajMiRectCijelogProzora());
		   }
		   else{
			   sprMute.nacrtajSprite(can, 1, 0, 0,dajMiRectCijelogProzora());
		   }
			
		}

		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
    	
    };
    izborMute.crtajNacrtajVanjskiIznadBezObzira(true);
	 
       
	  /* float sirPolja=1*this.uiMan.getSirEkrana()/10;
	   float visPolja=1*this.uiMan.getSirEkrana()/10;*/
       float sirPolja=this.velPojaSmanj;
	   float visPolja=this.velPojaSmanj;
	   izborMute.postaviStatickiIzbornik();
	   izborMute.postaviLijeviVrhProzoraPix(uiMan.getSirEkrana()/2 - 4*sirPolja,uiMan.getVisEkrana()-visPolja);
	   izborMute.pokreniMojIzbornik(null);
	   izborMute.crtajIkadaNijePokrenut(true);
	   izborMute.postaviVelicinuPoljaUPix(sirPolja,visPolja);
	  
	   uiMan.dodajElementUListu(izborMute,4);
      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
      
}
public void ocistiDodatneBotune(){
	this.uiMan.makniObjekt(izborLavaBomb);
	this.uiMan.makniObjekt(izborRenforce);
	this.uiMan.makniObjekt(izbornikKontroleZvuka);
	this.uiMan.makniObjekt(izborEndGame);
	this.uiMan.makniObjekt(izborExit);
	this.uiMan.makniObjekt(izborRestart);
	this.uiMan.makniObjekt(izborMute);
}
public void inicijalizirajDodatneBotune(){
	pomakniRectove888();
	paintPunjenja=new Paint();
	paintPunjenja.setARGB(120, 0,0,0);
	 float maxDuzina=this.rectMenu.left-this.rectDodatneFunkcije.right;
	
	 zadnjiBrojAktiviranogAbilitija=-1;
	 int brAbilityja=5;
	 
	 inicijalizirajGameEndIzbornik();
	 inicijalizirajRestartIzbornik();
	 inicijalizirajExitIzbornik();
	 sirinAbilityPolja=maxDuzina/(13);// predpostavljam da će biti 5 komada
	 razmakIzmeđuAbilityPolja=sirinAbilityPolja/3;// +1 je zbog toga što mora napraviti razmak s obje strane
	 velicinaPodnozjaPix= sirinAbilityPolja+sirinAbilityPolja/9;//+4*sirinAbilityPolja/11;
	 velPojaSmanj= sirinAbilityPolja;
	 velMjerPoj=sirinAbilityPolja*1.5f;
	 
	 sirDodFunkPics=(((sirEkr+visEkr)/2)/100)*sirDodFunkPosto;
	 visDodFunkPics=(((sirEkr+visEkr)/2)/100)*visDodFunkPosto;
	 
	 
	sirPauze=velMjerPoj;
	 sirResume=velMjerPoj+2*razmakIzmeđuAbilityPolja;
	 dodajBotuneZaPocetakValova();
	 inicijalizirajPocmiValoveBotune();
	 ///provjeri broj abilitija
	     int  tempUkBrAbi=0;
	         boolean reanforce=false;
	         if(this.GL.faIgr.listaAchievementsa.contains(IgricaActivity.achievementNoOneShellPas)) {
	        	 tempUkBrAbi++;
	        	 reanforce=true;
	         }
	         boolean lava=false;
	         if(this.GL.faIgr.listaAchievementsa.contains(IgricaActivity.achievementDeathFromAbove)){
	        	 tempUkBrAbi++;
	        	 lava=true;
	         }
	 ////
	 xPocAbilitija=tempUkBrAbi*razmakIzmeđuAbilityPolja+tempUkBrAbi* sirinAbilityPolja;
	 xPocAbilitija= this.sirEkr/2-xPocAbilitija/2 ;    
	 //////////////////////////////////////////////
	 
	 inicijalizirajMuteBotun();
	 stvoriKontroleZvuka();
	 
	 
	if(!this.GL.faIgr.listaAchievementsa.isEmpty())if(reanforce) {
		++this.zadnjiBrojAktiviranogAbilitija;
        stvoriReanforcmentBotun();
	}
	if(!this.GL.faIgr.listaAchievementsa.isEmpty())if(lava) {
		++this.zadnjiBrojAktiviranogAbilitija;
		stvoriLavaBombBotun();
	}

}
public void inicijalizirajPocmiValoveBotune(){
	if(listaPocmiValoveIzbornika!=null){
		for(int i=0;i<this.listaPocmiValoveIzbornika.size();i++){
			uiMan.makniObjekt(listaPocmiValoveIzbornika.get(i));
			listaPocmiValoveIzbornika.remove(i);
			i--;
		 }
	}
	
	else if(this.listaBotunaNaPocetkuXPozicija!=null&&this.listaBotunaNaPocetkuXPozicija.size()>0){
		listaPocmiValoveIzbornika= new LinkedList<IzbornikUniverzalni>();
	}
	if(this.listaBotunaNaPocetkuXPozicija!=null&&this.listaBotunaNaPocetkuXPozicija.size()>0){
	 for(int i=0;i<this.listaBotunaNaPocetkuXPozicija.size();i++){
		  this.listaPocmiValoveIzbornika.add( inicijalizirajPocmiValoveBotun(listaBotunaNaPocetkuXPozicija.get(i), listaBotunaNaPocetkuYPozicija.get(i)));
	 }
 }
}
////////////////////////////////////////////////

private void stvoriLavaBombBotun(){ 
   
	
	final int redniBrojOvogBotuna=zadnjiBrojAktiviranogAbilitija;
	
	this.izborLavaBomb=new IzbornikUniverzalni( sprAbility,uiMan,1,1,302){
		 private GameEvent geUvod=new GameEvent(null);
		 private float  temDy,prosliY;
		 private RectF rectUvodPmicanja=new RectF();
		
		   Paint paintBljesk=new Paint();
		private boolean abilityOznacen=false,ponovnoKliknutoNaIsti=false;
		private double timeAbilityPocetak=System.currentTimeMillis();
		private ProjektilL projektil;
		GameEvent geZaPulsiranje=new GameEvent(this),geZaBljeskove=new GameEvent(this);
		private float postoPovecanjeAkoIzabran=20;
		private Random generator=new Random();
		private double vrijemePocetkaLavaBomba;
		private boolean pocmiCrtanjeAnimacije=false;;
		 /*!!!!!!!!!!!!!!PROMJENITI ZA SLJEDEĆI BOTUN!!!!!!!!*/int brKoristeneSlike=5;
		float postotak=0;
		
		float vrijemePlulsiranjaSek=2.5f;
		 boolean maxPovecanje=true;
		 boolean izvrsiAkciju=false;
		 boolean tekPoceoAb=true;
		float x,y,xCilj,yCilj;
		RectF recBljestanjeRadni=new RectF();
		RectF recBljestanje=new RectF();
		boolean promjenaVelFlipFlop=true;
		Canvas can; float fps;UIManager uiMan;float PpCmX; float PpCmY;float pomCanX; float sirEkrana; float visEkrana;
		float pomCanY;
		boolean bljesakGotov=false,smanjenjeGotovo=false,projektilBljesakGotov=false,projektilSmanjenjeGotovo=false;
		RectF bljeskanjeNebo, bljeskanjeProjektil;
		Paint mPaint = new Paint();
		private void crtanjeDodatneAnimacijeZaProjektil(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		     
			if(!bljesakGotov&&!smanjenjeGotovo){
				
				bljesakGotov=sprAbility.animacijaSlaganjeUvecanjeVremenska(this.geZaBljeskove,this.sirEkrana/4,this.sirEkrana/4,0.5f,0.5f,bljeskanjeNebo);
				bljeskanjeNebo.set(xCilj-bljeskanjeNebo.width()/2, -pomCanY-3*bljeskanjeNebo.height()/5, xCilj+bljeskanjeNebo.width()/2,-pomCanY+2*bljeskanjeNebo.height()/5);
				 ///////test
				mPaint.setColor(Color.WHITE);
		        mPaint.setStyle(Paint.Style.FILL);
			    mPaint.setShader(new RadialGradient(bljeskanjeNebo.centerX() ,bljeskanjeNebo.centerY() ,
			    		bljeskanjeNebo.width()/2+1 , Color.WHITE, Color.argb(0, 255, 255,255), TileMode.CLAMP));
		        
		    
		     
		     can.drawCircle(bljeskanjeNebo.centerX() ,bljeskanjeNebo.centerY() ,bljeskanjeNebo.height()/2, mPaint);
		     if(bljesakGotov){
		    	 geZaBljeskove.jesamLiZiv=true;
		    	 geZaBljeskove.vrijeme1=0;
		     }
				 ////////////////////////////////////
		    }
			else if(bljesakGotov&&!smanjenjeGotovo){
				geZaBljeskove.vrijemePauze=(int) this.uiMan.GL.getVrijemePauze();
				smanjenjeGotovo=sprAbility.animacijaSlaganjeUvecanjeVremenska(this.geZaBljeskove,-this.sirEkrana/4,-this.sirEkrana/4,3f, 3f,bljeskanjeNebo);
				
				bljeskanjeNebo.set(xCilj-bljeskanjeNebo.width()/2, -pomCanY-3*bljeskanjeNebo.height()/5, xCilj+bljeskanjeNebo.width()/2,-pomCanY+2*bljeskanjeNebo.height()/5);
				 ///////test
				mPaint.setColor(Color.WHITE);
		        mPaint.setStyle(Paint.Style.FILL);
			    mPaint.setShader(new RadialGradient(bljeskanjeNebo.centerX() ,bljeskanjeNebo.centerY() ,
			    		bljeskanjeNebo.width()/2+1 , Color.WHITE, Color.argb(0, 255, 255,255), TileMode.CLAMP));
			    if(smanjenjeGotovo){
			    	 projektil.namjestiBrzinu(4f*(PpCmX+PpCmY)/2);
			    	 bljeskanjeProjektil.set(projektil.getX()-projektil.getXVelUPrikazu()/2,projektil.getY() -projektil.getYVelUPrikazu()/2, projektil.getX()+projektil.getXVelUPrikazu()/2,projektil.getY()+projektil.getYVelUPrikazu()/2);
			    	 geZaBljeskove.jesamLiZiv=true;
			    	 geZaBljeskove.vrijeme1=0;
			    	 projektil.namjestiNoviPocetniPolozaj(xCilj,yCilj-this.visEkrana);
					 projektil.pokreniSePremaCilju(xCilj, yCilj);
					
			     }
		        
		    
		     
		     can.drawCircle(bljeskanjeNebo.centerX() ,bljeskanjeNebo.centerY() ,bljeskanjeNebo.height()/2, mPaint);
			}
			else if(smanjenjeGotovo&&!projektilBljesakGotov){
				 bljeskanjeProjektil.set(projektil.getX()-bljeskanjeProjektil.width()/2,projektil.getY() -bljeskanjeProjektil.height()/2, projektil.getX()+bljeskanjeProjektil.width()/2,projektil.getY()+bljeskanjeProjektil.height()/2);
				 geZaBljeskove.vrijemePauze=(int) this.uiMan.GL.getVrijemePauze();
				 projektilBljesakGotov = sprAbility.animacijaSlaganjeUvecanjeVremenska(this.geZaBljeskove,20*projektil.getXVelUPrikazu(),20*projektil.getXVelUPrikazu(),1.0f, 1.0f,bljeskanjeProjektil);
				 float pocetnaBrzina=4f*(PpCmX+PpCmY)/2;
				 float razlika = bljeskanjeProjektil.width();
				 razlika=(razlika)/(20*projektil.getXVelUPrikazu()/100);// postaje postotak
				 projektil.namjestiBrzinu(pocetnaBrzina-razlika*pocetnaBrzina/250);
				 ///////test
					mPaint.setColor(Color.WHITE);
			        mPaint.setStyle(Paint.Style.FILL);
				    mPaint.setShader(new RadialGradient(bljeskanjeProjektil.centerX() ,bljeskanjeProjektil.centerY() ,
				    		bljeskanjeProjektil.width()/2 , Color.WHITE, Color.argb(0, 255, 255,255), TileMode.CLAMP));
				    can.drawCircle(bljeskanjeProjektil.centerX() ,bljeskanjeProjektil.centerY() ,bljeskanjeProjektil.width()/2, mPaint);
				 
				    
				    if(projektilBljesakGotov){
				    	bljeskanjeProjektil.set(projektil.getX()-5*projektil.getXVelUPrikazu(),projektil.getY() -5*projektil.getYVelUPrikazu(), projektil.getX()+5*projektil.getXVelUPrikazu(),projektil.getY()+5*projektil.getYVelUPrikazu());
				    	 geZaBljeskove.jesamLiZiv=true;
				    	 geZaBljeskove.vrijeme1=0;
				  }
				     
			}
			else if(this.projektilBljesakGotov&&!projektilSmanjenjeGotovo){
				 bljeskanjeProjektil.set(projektil.getX()-bljeskanjeProjektil.width()/2,projektil.getY() -bljeskanjeProjektil.height()/2, projektil.getX()+bljeskanjeProjektil.width()/2,projektil.getY()+bljeskanjeProjektil.height()/2);
				 geZaBljeskove.vrijemePauze=(int) this.uiMan.GL.getVrijemePauze();
				 projektilSmanjenjeGotovo = sprAbility.animacijaSlaganjeUvecanjeVremenska(this.geZaBljeskove,-9*projektil.getXVelUPrikazu(),-9*projektil.getYVelUPrikazu(),1.5f, 1.5f,bljeskanjeProjektil);
				
				 ///////test
					mPaint.setColor(Color.WHITE);
			        mPaint.setStyle(Paint.Style.FILL);
				    mPaint.setShader(new RadialGradient(bljeskanjeProjektil.centerX() ,bljeskanjeProjektil.centerY() ,
				    		bljeskanjeProjektil.width()/2 , Color.WHITE, Color.argb(0, 255, 255,255), TileMode.CLAMP));
				    can.drawCircle(bljeskanjeProjektil.centerX() ,bljeskanjeProjektil.centerY() ,bljeskanjeProjektil.width()/2, mPaint);
				 
				    
				    if(projektilSmanjenjeGotovo){
				     
				    	 geZaBljeskove.jesamLiZiv=true;
				    	 geZaBljeskove.vrijeme1=0;
				  }
				
			}
			else if(projektilSmanjenjeGotovo){
				 bljesakGotov=false;
				 smanjenjeGotovo=false;
				 projektilBljesakGotov=false;
				 projektilSmanjenjeGotovo=false;
				 pocmiCrtanjeAnimacije=false;
					geZaBljeskove.jesamLiZiv=true;
					 geZaBljeskove.vrijeme1=0;
			}
			else{
				
				
				 
			 }
		}
		private float randIzmeduPlusMinus(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak
			if(b==0) b=1;
			float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
			if(generator.nextBoolean()){r=r*-1;}
			return r+a;
		 }
		private boolean jeliTockaNaCesti(float xT,float yT){
			RectF tempRect=new RectF(xT,yT,xT+1,yT+1);
			//////////////////spremam vrijednosti stvarnih varijebli prije nego što ubacim lažne da bih mogao iskoristiti koalizion detection sustav
			
		
			//////////////////sada konacno provjerava dali je tocka na cesti
			boolean tocNaCest=false;
			GameLogicObject[] tempListaSudara=GL.provjeriKoliziju(2,tempRect);
			if(tempListaSudara!=null)	if(tempListaSudara[0]!=null){
				tocNaCest=true;
			
			}
			////////////////vraca prijasnje vrijednostia
			
			
			//////////////////
			return tocNaCest;
			
			
		} 
		private void promjeniVelBotuna(){
	        if(abilityOznacen){
	        	 
	    	        
				this.postaviLijeviVrhProzoraPix(xPocAbilitija+razmakIzmeđuAbilityPolja+ redniBrojOvogBotuna*(sirinAbilityPolja+razmakIzmeđuAbilityPolja)-(postoPovecanjeAkoIzabran/2)*sirinAbilityPolja/100,razmakIzmeđuAbilityPolja/4+(postoPovecanjeAkoIzabran/2)*sirinAbilityPolja/100);
				postaviVelicinuPoljaUPix(sirinAbilityPolja+postoPovecanjeAkoIzabran*sirinAbilityPolja/100,sirinAbilityPolja+postoPovecanjeAkoIzabran*sirinAbilityPolja/100);
				this.izracVelProzora();
				 if(!promjenaVelFlipFlop){
		 	        	geZaPulsiranje.jesamLiZiv=true;
		 				   recBljestanje.set(getGlavniRectPrikaza().left,getGlavniRectPrikaza(). top, getGlavniRectPrikaza().right, this.getGlavniRectPrikaza().bottom);
		 				   promjenaVelFlipFlop=true;
		 	                } 
	    	    	 
		    	   
			}
			else {
				
				this.postaviLijeviVrhProzoraPix(xPocAbilitija+razmakIzmeđuAbilityPolja+ redniBrojOvogBotuna*(sirinAbilityPolja+razmakIzmeđuAbilityPolja),razmakIzmeđuAbilityPolja/4);
				postaviVelicinuPoljaUPix(sirinAbilityPolja,sirinAbilityPolja);
				this.izracVelProzora();
				 if(promjenaVelFlipFlop){
		 	        	geZaPulsiranje.jesamLiZiv=true;
		 				   recBljestanje.set(this.getGlavniRectPrikaza().left,this.getGlavniRectPrikaza(). top, this.getGlavniRectPrikaza().right, this.getGlavniRectPrikaza().bottom);
		 				   promjenaVelFlipFlop=false;
		 	                } 
				
			}
		}
        private void izvrsiAkciju(float x, float y){
        	
        
        		         
        		         if(projektil==null){
        		        		projektil= GL.faIgr.br405ProjektilLavaMeteor(null, this.getGlavniRectPrikaza().centerX(), this.getGlavniRectPrikaza().centerY());
        		        	}
        		        
        		    
        	
        		     	geZaBljeskove.vrijeme1=System.currentTimeMillis();
        		     	bljeskanjeNebo.set(0, 0,this.sirEkrana/4, this.sirEkrana/4);
        		     	bljeskanjeProjektil=new RectF();
        		     	 bljeskanjeProjektil.set(projektil.getX()-projektil.getXVelUPrikazu()/2,projektil.getY() -projektil.getYVelUPrikazu()/2, projektil.getX()+projektil.getXVelUPrikazu()/2,projektil.getY()+projektil.getYVelUPrikazu()/2);
        	pocmiCrtanjeAnimacije=true;
        	vrijemePocetkaLavaBomba=System.currentTimeMillis();
        	izvrsiAkciju=false;
        	postotak=100;
        	timeAbilityPocetak=System.currentTimeMillis();
        	xCilj=x;
        	yCilj=y;
        	 
        	 
        }
        @Override
        public void setTouchedXY(float x, float y) {
        	super.setTouchedXY(x, y);
        	if(abilityOznacen){
        		this.x=x;
            	this.y=y;
        		 izvrsiAkciju=true;
        		 abilityOznacen=false;
        		 promjeniVelBotuna();
        		 brOznacenogAbilitija=-1;
        	}
        	// TODO Auto-generated method stub
        	
        	}
		@Override
		public void setTouchedObject(UIManagerObject obj) {// ako primi isti touched object kao i on onda to znaèi da se kliknulo u podruèje izbornika
			// TODO Auto-generated method stub
			super.setTouchedObject(obj);
			if(obj!=this&&abilityOznacen) {// ako je kliknuto na neki drugi objekt i ako je oznacen provjerava jeli neprijatelj ako je ispucava ako nije vraca stanje u false
				if(obj.getIndikator()<=200){// AKO JE NEPRIJATELJ ilineki od branitelja
					x=uiMan.getXZadnjegDodira();
					y=uiMan.getYZadnjegDodira();
					izvrsiAkciju=true;
					abilityOznacen=false;
					promjeniVelBotuna();
			      }
				else{// AKO JE TORANJ ILI NEŠTO OD BRANITELJA
					izvrsiAkciju=false;
					abilityOznacen=false;
					promjeniVelBotuna();
				}
				}
				else{// AKO kliknuto na isti botun ondase gasi
					if(abilityOznacen)ponovnoKliknutoNaIsti=true;
					izvrsiAkciju=false;
					abilityOznacen=false;
					
					promjeniVelBotuna();
				}
					
				
			
		}
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			if(brTP==1){
			brOznacenogAbilitija=redniBrojOvogBotuna;	
			uiMan.setOznacenSam(this);
			if(!ponovnoKliknutoNaIsti)abilityOznacen=true;// tako kada se stisne na ovaj gumb dok je ozna�?en da se smanji ne da trzne jer ga touchobjekt metoda vrati a oa ga poveća
			else {
				abilityOznacen=false;
				ponovnoKliknutoNaIsti=false;
			}
			promjeniVelBotuna();
			}
			this.pokreniMojIzbornik(null);// ovo stavljam samo zbog zastavice pokrenutIzbornik koja treba biti truze a bi se nastavio dalje izvršavati izbornik
		}

		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			this.can=can;
		    this.fps=fps;
		    this.uiMan=uiMan;
		    this.PpCmX=PpCmX;
		    this.PpCmY=PpCmY;
		    this.pomCanX=pomCanX;
		    this.pomCanY=pomCanY;
		   if(this.pocmiCrtanjeAnimacije){
			   
			   crtanjeDodatneAnimacijeZaProjektil(can,fps, uiMan, PpCmX,  PpCmY,pomCanX,pomCanY);
		   }
		    if(postotak<0){
		    	
		    	    
		    	        if(!maxPovecanje){//smanuje
		    	        	maxPovecanje=sprAbility.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,-this.getGlavniRectPrikaza().width()/4,-this.getGlavniRectPrikaza().width()/4,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
		    	        }
		    	        else {
		    	        	maxPovecanje=!sprAbility.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,this.getGlavniRectPrikaza().width()/4,this.getGlavniRectPrikaza().width()/4,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
		    	        }
                        recBljestanjeRadni.set(this.getGlavniRectPrikaza());
		    	        
		    	        recBljestanjeRadni.set(recBljestanjeRadni.centerX()-recBljestanje.width()/2, recBljestanjeRadni.centerY()-recBljestanje.height()/2, recBljestanjeRadni.centerX()+recBljestanje.width()/2,recBljestanjeRadni.centerY()+recBljestanje.height()/2);

				    	paintBljesk.setColor(Color.argb(255,255,246,0));
						paintBljesk.setStyle(Paint.Style.FILL);
						paintBljesk.setShader(new RadialGradient(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
							(int)(recBljestanjeRadni.width()*0.8), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
					    can.drawCircle(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
								(int)(recBljestanjeRadni.width()*0.8), paintBljesk);
		    	      
		    	       // sprAbility.nacrtajSprite(can, 3, 0, 0, recBljestanjeRadni);  
		             } 
		    
			if(izvrsiAkciju&&postotak<0){
		    if(jeliTockaNaCesti(x,y)) izvrsiAkciju( x,  y);
		    else izvrsiAkciju=false;
	   			}
	   }
		@Override
		public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			
			
		    this.can=can;
		    this.fps=fps;
		    this.uiMan=uiMan;
		    this.PpCmX=PpCmX;
		    this.PpCmY=PpCmY;
		    this.pomCanX=pomCanX;
		    this.pomCanY=pomCanY;
		 
		    visEkrana=uiMan.getVisEkrana();
		    sirEkrana=uiMan.getSirEkrana();
		    if(!stisnutoNaPocetakValova){// tako da se ne pokrene pije nego se počme igra
		    	timeAbilityPocetak=System.currentTimeMillis();
		    }
		    if(tekPoceoAb){
		    	 geUvod.jesamLiZiv=true;
		    	 if(!abilityMeteorUvodZavrsen)   this.pomakniIzbornikAkomulirajuciApsolutno(0, -this.dajMiRectCijelogProzora().height());
				  rectUvodPmicanja.set(0,0, this.dajMiRectCijelogProzora().width(),this.dajMiRectCijelogProzora().height());
				   

	    
				  
				  
		    	
		    	
		    	bljeskanjeNebo=new RectF();
		     	
		     	
		    	geZaBljeskove.jesamLiZiv=true;
		    	geZaPulsiranje.jesamLiZiv=true;
				   maxPovecanje=true;
		        	  recBljestanje.set(this.getGlavniRectPrikaza().left+pomCanX,pomCanY+this.getGlavniRectPrikaza(). top,pomCanX+ this.getGlavniRectPrikaza().right,pomCanY+ this.getGlavniRectPrikaza().bottom);
		    	  
		        	
				   tekPoceoAb=false;
			   }
		    
		    
		    
		    if(!abilityMeteorUvodZavrsen&&abilityRenforceUvodZavrsen){
				   temDy=rectUvodPmicanja.top- prosliY;
				   prosliY=rectUvodPmicanja.top;
					 if( !zvukAbilitijaMeteorPokrenut){
						 GL.faIgr.pustiZvuk(IgricaActivity.zvukRobotski3,60,110, 100, 0);
							zvukAbilitijaMeteorPokrenut=true;	 
					 }
					//rec.set(this.dajMiRectCijelogProzora());
				   this.pomakniIzbornikAkomulirajuci(0,  temDy);
				   abilityMeteorUvodZavrsen=exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geUvod,0,rectUvodPmicanja.height(),0,2f,rectUvodPmicanja);
			       if(abilityMeteorUvodZavrsen){

			    	   temDy=rectUvodPmicanja.top- prosliY;
			    	   this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
			       }
			       
			   }
		    if(brOznacenogAbilitija!=redniBrojOvogBotuna&&brOznacenogAbilitija>0){// broznaccenogabilitijy povezuje sve ability botune tako da ako se stisne na neki drugi prije izabrani nece vise biti ozncen
		    	this.abilityOznacen=false;
		    	promjeniVelBotuna();
		    	izvrsiAkciju=false;
		    }
		    if(this.postotak>0){
		    	izvrsiAkciju=false;
		    }
		
			sprAbility.nacrtajSprite(can,brKoristeneSlike, 0, 0, this.getGlavniRectPrikaza()); 
			timeAbilityPocetak+=this.uiMan.GL.getVrijemePauze();
			postotak=(float) (timeAbilityPocetak+uiMan.GL.faIgr.timerZaAbilitylavabombSek*1000-System.currentTimeMillis())/(uiMan.GL.faIgr.timerZaAbilitylavabombSek*1000/100);
			
			if(postotak>0){
				RectF rec=new RectF();
				rec.set(this.getGlavniRectPrikaza());	
				rec.set(rec.left, rec.top, rec.right, rec.bottom-(100-postotak)*rec.height()/100);
				can.drawRect(rec,paintPunjenja );
			}
			
			
			
			
		}
		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
			
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
		@Override
		public void onSystemResume(){
			super.onSystemResume();
			this.dodajSpriteGlavni( sprAbility);
		}
    };
   // izborLavaBomb.crtajNacrtajVanjskiIznadBezObzira(true);
	
       
    //izborLavaBomb.crtajNacrtajVanjskiIspodBezObzira(true);
	  
	 
    izborLavaBomb.postaviStatickiIzbornik();
    izborLavaBomb.postaviLijeviVrhProzoraPix(xPocAbilitija+razmakIzmeđuAbilityPolja+ this.zadnjiBrojAktiviranogAbilitija*(sirinAbilityPolja+razmakIzmeđuAbilityPolja),razmakIzmeđuAbilityPolja/4);
    izborLavaBomb.crtajIkadaNijePokrenut(true);
    izborLavaBomb.postaviVelicinuPoljaUPix(sirinAbilityPolja,sirinAbilityPolja);
    izborLavaBomb.pokreniMojIzbornik(null);
	  
	   uiMan.dodajElementUListu(izborLavaBomb,3);
      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
      
}
private void stvoriReanforcmentBotun(){ 
   
	final int redniBrojOvogBotuna=zadnjiBrojAktiviranogAbilitija;
	
	this.izborRenforce=new IzbornikUniverzalni( sprAbility,uiMan,1,1,302){
		 private GameEvent geUvod=new GameEvent(null);
		 private float  temDy,prosliY;
		 private RectF rectUvodPmicanja=new RectF();
		
		 Paint paintBljesk=new Paint();
		private boolean abilityOznacen=false,ponovnoKliknutoNaIsti=false;
		private double timeAbilityPocetak=System.currentTimeMillis();
		private float postoPovecanjeAkoIzabran=20;
		private Random generator=new Random();
		 /*!!!!!!!!!!!!!!PROMJENITI ZA SLJEDEĆI BOTUN!!!!!!!!*/int brKoristeneSlike=4;
		float postotak=0;
		
		GameEvent geZaPulsiranje=new GameEvent(this);
		float vrijemePlulsiranjaSek=1.5f;
		 boolean maxPovecanje=true;
		 boolean izvrsiAkciju=false;
		 boolean tekPoceoAb=true;
		float x,y;
		RectF recBljestanje=new RectF();
		RectF recBljestanjeRadni=new RectF();
		boolean promjenaVelFlipFlop=true;
		Canvas canvas;
		float fps;UIManager uiMan;float PpCmX; float PpCmY;float pomCanX;
		float pomCanY;
		private void promjeniVelBotuna(){
	        if(abilityOznacen){
	        	this.postaviLijeviVrhProzoraPix(xPocAbilitija+razmakIzmeđuAbilityPolja+ redniBrojOvogBotuna*(sirinAbilityPolja+razmakIzmeđuAbilityPolja)-(postoPovecanjeAkoIzabran/2)*sirinAbilityPolja/100,razmakIzmeđuAbilityPolja/4+(postoPovecanjeAkoIzabran/2)*sirinAbilityPolja/100);
				postaviVelicinuPoljaUPix(sirinAbilityPolja+postoPovecanjeAkoIzabran*sirinAbilityPolja/100,sirinAbilityPolja+postoPovecanjeAkoIzabran*sirinAbilityPolja/100);
				this.izracVelProzora();
				if(!promjenaVelFlipFlop){
		        	geZaPulsiranje.jesamLiZiv=true;
					   recBljestanje.set(this.getGlavniRectPrikaza().left,this.getGlavniRectPrikaza(). top,this.getGlavniRectPrikaza().right,this.getGlavniRectPrikaza().bottom);
					   promjenaVelFlipFlop=true;
		                }  
			}
			else {
				 
				this.postaviLijeviVrhProzoraPix(xPocAbilitija+razmakIzmeđuAbilityPolja+ redniBrojOvogBotuna*(sirinAbilityPolja+razmakIzmeđuAbilityPolja),razmakIzmeđuAbilityPolja/4);
				postaviVelicinuPoljaUPix(sirinAbilityPolja,sirinAbilityPolja);
				this.izracVelProzora();
				if(promjenaVelFlipFlop){
		        	geZaPulsiranje.jesamLiZiv=true;
					   recBljestanje.set(this.getGlavniRectPrikaza().left,this.getGlavniRectPrikaza(). top,this.getGlavniRectPrikaza().right,this.getGlavniRectPrikaza().bottom);
					   promjenaVelFlipFlop=false;
		                } 
			
	        	
				
			}
		}
		private float randIzmeduPlusMinus(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak
			if(b==0) b=1;
			float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
			if(generator.nextBoolean()){r=r*-1;}
			return r+a;
		 }
		private boolean jeliTockaNaCesti(float xT,float yT){
			RectF tempRect=new RectF(xT,yT,xT+1,yT+1);
			//////////////////spremam vrijednosti stvarnih varijebli prije nego što ubacim lažne da bih mogao iskoristiti koalizion detection sustav
			
		
			//////////////////sada konacno provjerava dali je tocka na cesti
			boolean tocNaCest=false;
			GameLogicObject[] tempListaSudara=GL.provjeriKoliziju(2,tempRect);
			if(tempListaSudara!=null)	if(tempListaSudara[0]!=null){
				tocNaCest=true;
			
			}
			////////////////vraca prijasnje vrijednostia
			
			
			//////////////////
			return tocNaCest;
			
		} 
        private void izvrsiAkciju(float x, float y){
        	
        	if(true){
        		int  j=0;
        		float xR=0,yR=0;
        		 while(j<4){// stvara toliko objekata koliko je navedeno u stvaranju objekkta tornja
        				int i=0;
        			     while(true){
        			        xR=randIzmeduPlusMinus(x,randomRazmakCm*this.PpCmX);//namješta koordinae na koje se treba pomaknuti
        			        yR=randIzmeduPlusMinus(y,randomRazmakCm*this.PpCmY);
        			    
        			        if(jeliTockaNaCesti(xR,yR)){
        			        	break;
        			        }
        		       
        		        	if(i>10) {
        		        		xR=x;
        		        		yR=y;
        				       break;
        		            }
        		        	i++;
        				}
        			     GL.faIgr.br49Junak(xR, yR,4*uiMan.GL.faIgr.timerZaAbilityreinforcementSek/5);
        		         j++;
        		         }
        		 
        	}
        	
        	
        	 izvrsiAkciju=false;
        	 postotak=100;
        	 timeAbilityPocetak=System.currentTimeMillis();
        	 
        	 
        }
        @Override
        public void setTouchedXY(float x, float y) {
        	super.setTouchedXY(x, y);
        	if(abilityOznacen){
        		this.x=x;
            	this.y=y;
        		 izvrsiAkciju=true;
        		 abilityOznacen=false;
        		 promjeniVelBotuna();
        	}
        	// TODO Auto-generated method stub
        	
        	}
		@Override
		public void setTouchedObject(UIManagerObject obj) {// ako primi isti touched object kao i on onda to znaèi da se kliknulo u podruèje izbornika
			// TODO Auto-generated method stub
	
		    if(obj!=this&&abilityOznacen) {// ako je kliknuto na neki drugi objekt i ako je oznacen provjerava jeli neprijatelj ako je ispucava ako nije vraca stanje u false
				if(obj.getIndikator()<=200){// AKO JE NEPRIJATELJ ili neki od branitelja ili neki od tornjeva
					x=uiMan.getXZadnjegDodira();
					y=uiMan.getYZadnjegDodira();
					izvrsiAkciju=true;
					abilityOznacen=false;
					promjeniVelBotuna();
				
				}
				else{// AKO JE TORANJ ILI NEŠTO OD BRANITELJA
					abilityOznacen=false;
					promjeniVelBotuna();
					izvrsiAkciju=false;
				}
					
				
			}
			else{// AKO kliknuto na isti botun onda se gasi
				if(abilityOznacen)ponovnoKliknutoNaIsti=true;
				abilityOznacen=false;
				promjeniVelBotuna();
				izvrsiAkciju=false;
			
			}
		}
		@Override
		public void univerzalniIzvrsitelj(int brTP) {
			// TODO Auto-generated method stub
			if(brTP==1){
				brOznacenogAbilitija=redniBrojOvogBotuna;
			uiMan.setOznacenSam(this);
			if(!ponovnoKliknutoNaIsti)abilityOznacen=true;// tako kada se stisne na ovaj gumb dok je ozna�?en da se smanji ne da trzne jer ga touchobjekt metoda vrati a oa ga poveća
			else {
				abilityOznacen=false;
				ponovnoKliknutoNaIsti=false;
			}
			promjeniVelBotuna();
			}
			this.pokreniMojIzbornik(null);// ovo stavljam samo zbog zastavice pokrenutIzbornik koja treba biti truze a bi se nastavio dalje izvršavati izbornik
		}

		@Override
		public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
			this.canvas=can;
		    this.fps=fps;
		    this.uiMan=uiMan;
		    this.PpCmX=PpCmX;
		    this.PpCmY=PpCmY;
		    this.pomCanX=pomCanX;
		    this.pomCanY=pomCanY;
		    
		    if(postotak<0){
		    	
		    	       
		    	        if(!maxPovecanje){//smanuje
		    	        	maxPovecanje=sprAbility.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,-this.getGlavniRectPrikaza().width()/4,-this.getGlavniRectPrikaza().width()/4,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
		    	       
		    	        }
		    	        else {
		    	        	maxPovecanje=!sprAbility.animacijaSlaganjeUvecanjeVremenska(geZaPulsiranje,this.getGlavniRectPrikaza().width()/4,this.getGlavniRectPrikaza().width()/4,vrijemePlulsiranjaSek,vrijemePlulsiranjaSek,   recBljestanje);
		    	        }
		    	        recBljestanjeRadni.set(this.getGlavniRectPrikaza());
		    	        
		    	        recBljestanjeRadni.set(recBljestanjeRadni.centerX()-recBljestanje.width()/2, recBljestanjeRadni.centerY()-recBljestanje.height()/2, recBljestanjeRadni.centerX()+recBljestanje.width()/2,recBljestanjeRadni.centerY()+recBljestanje.height()/2);

				    	paintBljesk.setColor(Color.argb(255,255,246,0));
						paintBljesk.setStyle(Paint.Style.FILL);
						paintBljesk.setShader(new RadialGradient(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
							(int)(recBljestanjeRadni.width()*0.8), Color.argb(160, 255,246,0), Color.argb(0, 255,246,0), TileMode.CLAMP));
						
					    can.drawCircle(recBljestanjeRadni.centerX() ,recBljestanjeRadni.centerY() ,
								(int)(recBljestanjeRadni.width()*0.8), paintBljesk);
		    	      
		             } 
		    
			if(izvrsiAkciju&&postotak<0){
		  if(jeliTockaNaCesti(x,y)){
				
		    	izvrsiAkciju( x,  y);
		    }
		    else izvrsiAkciju=false;
		    
	   			}
	   }
		@Override
		public void nacrtajVanjskiIspod(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		    this.canvas=can;
		    this.fps=fps;
		    this.uiMan=uiMan;
		    this.PpCmX=PpCmX;
		    this.PpCmY=PpCmY;
		    this.pomCanX=pomCanX;
		    this.pomCanY=pomCanY;
		    if(!stisnutoNaPocetakValova){// tako da se ne pokrene pije nego se počme igra
		    	timeAbilityPocetak=System.currentTimeMillis();
		    }
		   if(tekPoceoAb){
			   geUvod.jesamLiZiv=true;
			 if(!abilityRenforceUvodZavrsen) this.pomakniIzbornikAkomulirajuciApsolutno(0, -this.dajMiRectCijelogProzora().height());
			  rectUvodPmicanja.set(0,0, this.dajMiRectCijelogProzora().width(),this.dajMiRectCijelogProzora().height());
			   
			   maxPovecanje=true;
			   geZaPulsiranje.jesamLiZiv=true;
			   recBljestanje.set(this.getGlavniRectPrikaza().left+pomCanX,pomCanY+this.getGlavniRectPrikaza(). top,pomCanX+ this.getGlavniRectPrikaza().right,pomCanY+ this.getGlavniRectPrikaza().bottom);
			
			   tekPoceoAb=false;
	
		   }
		   if(!abilityRenforceUvodZavrsen){
				 if( !zvukAbilitijaReenforcePokrenut){
					 GL.faIgr.pustiZvuk(IgricaActivity.zvukRobotski3,60,110, 100, 0);
						zvukAbilitijaReenforcePokrenut=true;
					 }
			   temDy=rectUvodPmicanja.top- prosliY;
			   prosliY=rectUvodPmicanja.top;
			  
				//rec.set(this.dajMiRectCijelogProzora());
			   this.pomakniIzbornikAkomulirajuci(0,  temDy);
				abilityRenforceUvodZavrsen=exitSprite.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geUvod,0,rectUvodPmicanja.height(),0,2f,rectUvodPmicanja);
		       if(abilityRenforceUvodZavrsen){
		    
		    	   temDy=rectUvodPmicanja.top- prosliY;
		    	   this.pomakniIzbornikAkomulirajuciApsolutno(0, 0);
		       }
		       
		   }
		    if(brOznacenogAbilitija!=redniBrojOvogBotuna&&brOznacenogAbilitija>0){// broznaccenogabilitijy povezuje sve ability botune tako da ako se stisne na neki drugi prije izabrani nece vise biti ozncen
		    	this.abilityOznacen=false;
		    	promjeniVelBotuna();
		    	izvrsiAkciju=false;
		    }
		    if(this.postotak>0){
		    	izvrsiAkciju=false;
		    }
			sprAbility.nacrtajSprite(can,brKoristeneSlike, 0, 0, this.getGlavniRectPrikaza()); 
			
			timeAbilityPocetak+=this.uiMan.GL.getVrijemePauze();
			postotak=(float) ( timeAbilityPocetak+uiMan.GL.faIgr.timerZaAbilityreinforcementSek*1000-System.currentTimeMillis())/(uiMan.GL.faIgr.timerZaAbilityreinforcementSek*1000/100);
			if(postotak>0){
				RectF rec=new RectF();
				rec.set(this.getGlavniRectPrikaza());	
				rec.set(rec.left, rec.top, rec.right, rec.bottom-(100-postotak)*rec.height()/100);
				can.drawRect(rec,paintPunjenja );
			}
			
			
			
			
		}
		@Override
		public RectF getGlavniRectPrikaza() {
			RectF rect=new RectF();
		
			rect.set(this.dajMiRectCijelogProzora());
			return rect;
		}
		@Override
		public void onSystemResume(){
			super.onSystemResume();
			this.dodajSpriteGlavni( sprAbility);
		}
    };
   // izborRenforce.crtajNacrtajVanjskiIznadBezObzira(true);
    //izborRenforce.crtajNacrtajVanjskiIspodBezObzira(true);
       
	 
	  
	 

    izborRenforce.postaviStatickiIzbornik();
    izborRenforce.postaviLijeviVrhProzoraPix(xPocAbilitija+razmakIzmeđuAbilityPolja+this.zadnjiBrojAktiviranogAbilitija*(sirinAbilityPolja+razmakIzmeđuAbilityPolja),razmakIzmeđuAbilityPolja/4);
	   
    izborRenforce.crtajIkadaNijePokrenut(true);
    izborRenforce.postaviVelicinuPoljaUPix(sirinAbilityPolja,sirinAbilityPolja);
    izborRenforce.pokreniMojIzbornik(null);
	   uiMan.dodajElementUListu(izborRenforce,3);
      // uiMan.postaviTempUniverzalniIzbornik(izbor);// funkcija sluzi za back botun 	   
      
}
private void nacrtajPodnozje(){
	if(!iskljuciCrtPodnozja){
       if(crtanjeSvijetlaPodnozja){
	       RectF tempRec=new RectF();
	       tempRec.set(recPodnozja.left, recPodnozja.top-recPodnozja.height()/4, recPodnozja.right, recPodnozja.top);
	       stvoriRgbZaPodnozje();
	    
		   paintZaSvjetloPodnozja.setColor(Color.argb(255,this.podR, 255-podG, this.podB));
		   paintZaSvjetloPodnozja.setStyle(Paint.Style.FILL);
		   paintZaSvjetloPodnozja.setShader(
				new LinearGradient(
						tempRec.left, tempRec.bottom,tempRec.left, tempRec.top, Color.argb(255, this.podR, 255-podG, this.podB), Color.argb(0, this.podR, 255-podG, this.podB), TileMode.CLAMP));
             can.drawRect(tempRec, paintZaSvjetloPodnozja);
      }
    
 
	  this.sprAbility.nacrtajSprite(can, 2, 0,0, this.recPodnozja);
	}
}
private boolean nacrtajUpustvaTornjeviPrviDio(){
	boolean b=false;
	///////////////////////////////////////////////prvi dio////////////////////////////////////////////////////////////
	if(tekPoceoUpustvaTornjeviPrviDio){
		tekPoceoUpustvaTornjeviPrviDio=false;
		this.pocetakUpustvaToranjPrviDio=System.currentTimeMillis();
		visUpustvaTornjevi=60*this.visEkr/100;
		sirUpustvaTornjevi=visUpustvaTornjevi*2;
		polXUpustvaTornjevi=(sirEkr-sirUpustvaTornjevi)/2;
		polYUpustvaTornjevi=(visEkr-visUpustvaTornjevi)/2;
		geUpustva2.jesamLiZiv=true;
		recPomUputeToranj=new RectF();
		recPomUputeToranj.set(polXUpustvaTornjevi-pomCanX,this.visEkr-pomCanY,polXUpustvaTornjevi+sirUpustvaTornjevi-pomCanX,visUpustvaTornjevi+ this.visEkr-pomCanY);
		recPomUputeToranj2=new RectF();
		recPomUputeToranj2.set(polXUpustvaTornjevi-pomCanX,-visUpustvaTornjevi-pomCanY,polXUpustvaTornjevi+sirUpustvaTornjevi-pomCanX,-pomCanY);
		dyPolStari=recPomUputeToranj2.top;
	   
	}
	if(!this.gotovPrviDioAnimacijeTornjevi&&pocetakUpustvaToranjPrviDio+2000<System.currentTimeMillis()){
		if(pocetakUpustvaToranjPrviDio+10000>System.currentTimeMillis()){
			
			if( !pokakUpustvaTornjeviGtovPrviDio){
				pokakUpustvaTornjeviGtovPrviDio=SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geUpustva2,0,-(this.visEkr- polYUpustvaTornjevi),0,1f,this. recPomUputeToranj);
				
			}
			else {
				geUpustva2.jesamLiZiv=true;
				
				recPomUputeToranj.set(polXUpustvaTornjevi-pomCanX, polYUpustvaTornjevi-pomCanY,polXUpustvaTornjevi+sirUpustvaTornjevi-pomCanX, polYUpustvaTornjevi+visUpustvaTornjevi-pomCanY);
			      
			}
		
			spriteUpustvaTornjevi.nacrtajSprite(can, 0, 0,0, recPomUputeToranj);
		}
		else if(pocetakUpustvaToranjPrviDio+20000>System.currentTimeMillis()){
			
			if( !pokakUpustvaTornjeviGtovDrugiDio){

				pokakUpustvaTornjeviGtovDrugiDio=SpriteHendler.animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(geUpustva2,0,(this.visEkr- polYUpustvaTornjevi),0,1f,	recPomUputeToranj2);

				recPomUputeToranj.set(polXUpustvaTornjevi-pomCanX, polYUpustvaTornjevi-pomCanY+recPomUputeToranj2.top-dyPolStari,polXUpustvaTornjevi+sirUpustvaTornjevi-pomCanX, polYUpustvaTornjevi+visUpustvaTornjevi-pomCanY+recPomUputeToranj2.top-dyPolStari);
				
				spriteUpustvaTornjevi.nacrtajSprite(can, 0, 0,0, recPomUputeToranj);
			}
			else recPomUputeToranj2.set(polXUpustvaTornjevi-pomCanX, polYUpustvaTornjevi-pomCanY,polXUpustvaTornjevi+sirUpustvaTornjevi-pomCanX, polYUpustvaTornjevi+visUpustvaTornjevi-pomCanY);
		
			spriteUpustvaTornjevi.nacrtajSprite(can, 2, 0,0, recPomUputeToranj2);
		}
		else {
			gotovPrviDioAnimacijeTornjevi=true;
		}
	}
	
	if(gotovDrugiDioAnimacijeTornjevi&&System.currentTimeMillis()>pocetakUpustvaToranjPrviDio+20500){
	
			b=nacrtajUpustvaTornjeviTreciDio();
		
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	return b;
}
private boolean nacrtajUpustvaTornjeviTreciDio(){
	boolean b=false;
	///////////////////////////////////////////////prvi dio////////////////////////////////////////////////////////////
	if(tekPoceoUpustvaTornjeviTreciDio){
		tekPoceoUpustvaTornjeviTreciDio=false;
		this.pocetakUpustvaToranjTreciDio=System.currentTimeMillis();
	}
	if(pocetakUpustvaToranjTreciDio+5000>System.currentTimeMillis()){
		if(pocetakUpustvaToranjTreciDio+(brSlAnimTornj+1)*(1000/spriteUpustvaTornjevi.brojPrikazaPoSekundi(1))<System.currentTimeMillis()){
			brSlAnimTornj++;
		}
		if(spriteUpustvaTornjevi.brojStupaca(1)<=brSlAnimTornj){
			gotovTreciDioAnimacijeTornjevi=true;}
		if(!gotovTreciDioAnimacijeTornjevi){
			for(int i=0;this.GL.listaBran.size()>i;i++){
				RectF temRec=new RectF();
			if(GL.listaBran.get(i)  instanceof ToranjL)	{
				               ToranjL tempTor=(ToranjL)GL.listaBran.get(i);
				                   temRec.set(tempTor.getXPozUPrik()-pomCanX, tempTor.getYPozUPrik()-pomCanY, 
				                		   tempTor.getXPozUPrik()+ tempTor.getXVelUPrikazu()-pomCanX,tempTor.getYPozUPrik()+tempTor.getYVelUPrikazu()-pomCanY);
			                        this.spriteUpustvaTornjevi.nacrtajSprite(can, 1,	brSlAnimTornj, 0, temRec);
		            	}
			}
			
		}
		else {
		
			for(int i=0;this.GL.listaBran.size()>i;i++){
				RectF temRec=new RectF();
				if(GL.listaBran.get(i)  instanceof ToranjL)	{
			        	ToranjL tempTor=(ToranjL)GL.listaBran.get(i);
				        temRec.set(tempTor.getXPozUPrik()-pomCanX, tempTor.getYPozUPrik()-pomCanY, 
                		   tempTor.getXPozUPrik()+ tempTor.getXVelUPrikazu()-pomCanX,tempTor.getYPozUPrik()+tempTor.getYVelUPrikazu()-pomCanY);
			             this.spriteUpustvaTornjevi.nacrtajSprite(can, 1,spriteUpustvaTornjevi.brojStupaca(1)-1, 0, temRec);
				}
			}
		}
	}
	else {
		b=true;
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	return b;
}
private boolean nacrtajUpustvaAbiliti(){
	boolean b=false;
	///////////////////////////////////////////////prvi dio////////////////////////////////////////////////////////////
	
	RectF tempRectRenforce=this.izborRenforce.getGlavniRectPrikaza();
	RectF tempRect= new RectF();
	
	tempRect.set(tempRectRenforce.left-tempRectRenforce.width()/4, tempRectRenforce.top-tempRectRenforce.height()/4, tempRectRenforce.right+tempRectRenforce.width()/4, tempRectRenforce.bottom+tempRectRenforce.height()/4);
	if(!gotovPrviDioAnimacijeAbilitiUpustva){
		gotovPrviDioAnimacijeAbilitiUpustva=this.sprUpustvaAbiliti.vrtiAnimacijuReda(can, geUpustva1, 0, 0,  tempRect, null);
		if(gotovPrviDioAnimacijeAbilitiUpustva){// resetira za drugi dio
			geUpustva1.jesamLiZiv=true;
			poceoUpustvaAbilitiDrugiDioStojanje=System.currentTimeMillis();
		}
	}
	else {
		
		this.sprUpustvaAbiliti.nacrtajSprite(can, 0, sprUpustvaAbiliti.brojStupaca(0)-1, 0, tempRect);
	}
	//broj1
	tempRect.set(tempRect.left-tempRect.width(), tempRect.top+tempRect.width(), tempRect.right-tempRect.width(), tempRect.bottom+tempRect.width());
	this.sprUpustvaAbiliti.nacrtajSprite(can, 2, 0, 0, tempRect);
	///////////////////////////////////////////////drugi dio//////////////////////////////////////////////////////////
	float vrijeme1=1000;
	float vrijeme2=5000;
	tempRect.offsetTo(8.2f*this.PpCmX, 2.8f*this.PpCmY);
	tempRect.set(tempRect.left, tempRect.top, tempRect.right, tempRect.bottom+3*tempRect.height()/20);
	if(gotovPrviDioAnimacijeAbilitiUpustva&&poceoUpustvaAbilitiDrugiDioStojanje+vrijeme1<System.currentTimeMillis()){
		if(!gotovDrugiDioAnimacijeAbilitiUpustva){
			gotovDrugiDioAnimacijeAbilitiUpustva=this.sprUpustvaAbiliti.vrtiAnimacijuReda(can, geUpustva1, 1, 0,  tempRect, null);
			if(gotovDrugiDioAnimacijeAbilitiUpustva){
				
			}
		}
		else {
			if(poceoUpustvaAbilitiDrugiDioStojanje+vrijeme2+vrijeme1>System.currentTimeMillis()){
				this.sprUpustvaAbiliti.nacrtajSprite(can, 1, sprUpustvaAbiliti.brojStupaca(1)-1, 0, tempRect);
			}
			else {b=true;}
		}
		//broj2
		tempRect.set(tempRect.left-tempRect.width(), tempRect.top+tempRect.width(), tempRect.right-tempRect.width(), tempRect.top+2*tempRect.width());
		this.sprUpustvaAbiliti.nacrtajSprite(can, 2, 1, 0, tempRect);
	}
	
	
	return b;
}

private void stvoriRgbZaPodnozje(){
	int maxCesta=this.GL.faIgr.getMaxBrIndexaCesta();
	int trenutCesta=this.GL.getTrenutacnaNajdaljaCesta();
	float postotak= trenutCesta/((1+(float)maxCesta)/100f);
	int trenutacnaBoja=Math.round(postotak*510/100);//0-255 crvena, + 255-0 zelena
	proslaBoja+=this.GL.getVrijemePauze();
	float razlikaVremena=(float) (System.currentTimeMillis()-proslaBoja);
	///za crvenu
		if(trenutacnaBoja<podR){// ako se smanjuje
			podR-=Math.round(2*razlikaVremena*this.bojaPoSec/1000);
			if(trenutacnaBoja>podR){// ako preleti preko vrijjednosti ako je nula automatski ce se zadrzati na njoj
			   podR=trenutacnaBoja;
			}
		}
		else if(trenutacnaBoja>podR){
			podR+=Math.round(razlikaVremena*this.bojaPoSec/1000);
			if(trenutacnaBoja<podR){// ako preleti preko vrijednosti
				   podR=trenutacnaBoja;
				}
		}
		if(podR>255)podR=255;
		
	 ///za zelenu
	
		if(trenutacnaBoja-255<podG){// ako se smanjuje
			podG-=Math.round(2*razlikaVremena*this.bojaPoSec/1000);
			if(trenutacnaBoja-255>podG){// ako preleti preko vrijjednosti ako je nula automatski ce se zadrzati na njoj
			   podG=trenutacnaBoja-255;
			}
		}
		else if(trenutacnaBoja-255>podG){
			podG+=Math.round(razlikaVremena*this.bojaPoSec/1000);
			if(trenutacnaBoja-255<podG){// ako preleti preko vrijednosti
				   podG=trenutacnaBoja-255;
				}
		}
		if(podG>255)podG=255;
		else if(podG<0)podG=0;
		
		
		proslaBoja=System.currentTimeMillis();
	
} 
private void nacrtajDolazakProtivnika888(){
	this.recDolaProt.set(xVala-recDolaProt.width()/2, yVala-recDolaProt.width()/2, xVala+recDolaProt.width()/2,yVala+recDolaProt.height()/2);
	  //POMAK OBLAČIĆA AKO JE IZVAN EKRANA
	  //X KOREKCIJA
	  if(this.recDolaProt.left<-this.pomCanX){
	    	this.recDolaProt.set(0-pomCanX, recDolaProt.top, 0+recDolaProt.width()-pomCanX,recDolaProt.bottom);
	    }
	  else if(this.recDolaProt.right>this.sirEkr-this.pomCanX){
		    this.recDolaProt.set(this.sirEkr-this.recDolaProt.width()-pomCanX,recDolaProt.top, this.sirEkr-pomCanX,recDolaProt.bottom);
	  }
	  //Y KOREKCIJA
	  if(this.recDolaProt.top<-this.pomCanY+this.rectPause.bottom){
		  this.recDolaProt.set(recDolaProt.left, 0-pomCanY+this.rectPause.bottom,recDolaProt.right,0+recDolaProt.height()-pomCanY+this.rectPause.bottom);
	  }
	  else if(this.recDolaProt.bottom>this.visEkr-this.pomCanY-velMjerPoj){
		  this.recDolaProt.set(recDolaProt.left,this.visEkr-this.recDolaProt.height()-pomCanY-velMjerPoj,recDolaProt.right,this.visEkr-pomCanY-velMjerPoj);
	  }
	if(this.secDoNovVala>0){
	  
			/////////CRTANJE OBLACICA
	        if(System.currentTimeMillis()>1000/this.dolProtSprite.brojPrikazaPoSekundi(0)+this.prosVrijObl){
				dolProtSprite.nacrtajSprite(can, 0, this.brSlObl, 0,recDolaProt);
				brSlObl++;
			    prosVrijObl=System.currentTimeMillis();
			    if(dolProtSprite.brojStupaca(0)<=brSlObl) brSlObl=0;  
			}
			else   dolProtSprite.nacrtajSprite(can, 0, this.brSlObl, 0,recDolaProt);
           //////////CRTANJE HODANJA
	       odrediAlfuZaHodanje();
	       if(System.currentTimeMillis()>1000/this.dolProtSprite.brojPrikazaPoSekundi(1)+this.prosVrijHod){
				dolProtSprite.nacrtajSprite(can, 1, this.brSlHod, 0,recDolaProt,this.paintZaHodanje);
				brSlHod++;
			    prosVrijHod=System.currentTimeMillis();
			    if(dolProtSprite.brojStupaca(1)<=brSlHod) brSlHod=0;  
			}
			else   dolProtSprite.nacrtajSprite(can, 1, this.brSlHod, 0,recDolaProt,this.paintZaHodanje);
		     
		  }
	else if(crtaKrajDolaska){
		
		if(System.currentTimeMillis()>1000/this.dolProtSprite.brojPrikazaPoSekundi(2)+this.prosVrijKraj){
			dolProtSprite.nacrtajSprite(can, 2, this.brSlKraj, 0,recDolaProt);
			brSlKraj++;
		    prosVrijKraj=System.currentTimeMillis();
		    if(dolProtSprite.brojStupaca(2)<=brSlKraj){
		    	brSlKraj=0;
		    	crtaKrajDolaska=false;
		    }
		}
		else   dolProtSprite.nacrtajSprite(can, 2, this.brSlKraj, 0,recDolaProt);
	}
}
private void pomakniRectove888(){
	recPodnozja.set(-pomCanX,visEkr- velicinaPodnozjaPix-pomCanY,sirEkr-pomCanX,visEkr-pomCanY);
	rectMenu.set(sirEkr-desnoPicsDole-sirMenuDugmPics-pomCanX,visEkr-dolePics-visMenuDugmPics-pomCanY,sirEkr-desnoPicsDole-pomCanX,visEkr-dolePics-pomCanY);
    rectDodatneFunkcije.set(lijevoPicsDole-pomCanX,visEkr-dolePics-visDodFunkPics-pomCanY,lijevoPicsDole+sirDodFunkPics-pomCanX,visEkr-dolePics-pomCanY);
	 rectPause.set(sirEkr-desnoPicsGore-sirPauze-pomCanX,gorePics-pomCanY,sirEkr-desnoPicsGore-pomCanX,gorePics+sirPauze-pomCanY);
	 rectResume.set(sirEkr/2-sirResume/2-pomCanX,visEkr/2-pomCanY-sirResume/2,sirEkr/2-pomCanX+sirResume/2,visEkr/2+sirResume/2-pomCanY);
}
private void nacrtajBodove888(){
	 float razdVer=sirBodovaPics/2;
	 float razdHor1=visBodovaPics/4;
	 float razdHor2=visBodovaPics/2;
	 //////racunanje vrijednosti
	 this.recNovaca.set(lijevoPicsGore-pomCanX,gorePics-pomCanY,lijevoPicsGore-pomCanX+this.rectMenu.width()/2,gorePics-pomCanY+this.rectMenu.height()/2);
	 this.recValova.set(recNovaca.right+recNovaca.width(),recNovaca.top,recNovaca.right+2*recNovaca.width(), recNovaca.bottom);
	 this.recZivota.set(recValova.right+recValova.width(),recValova.top,recValova.right+2*recValova.width(), recValova.bottom);
	 float vis=140*recNovaca.height()/100;
	float sir=2*recNovaca.width()+ 2*recValova.width()+ 2*recZivota.width();
	 recPodlogaBodova.set(recNovaca.left, recNovaca.top, recNovaca.left+sir, recNovaca.top+vis);
	 recPodlogaBodova.set( recPodlogaBodova.left-7*sir/100,  recPodlogaBodova.top-7*sir/100,  recPodlogaBodova.right+5*sir/100, recPodlogaBodova. bottom);
	 ///////////////////////////crtanje pdloe/////////////////////////////////////////////////////
	 this.sprBodova.nacrtajSprite(can, 1, 0,0, recPodlogaBodova);
	 ////////////////////////crtanje novaca/////////////////////
	 
	 if(sprBodova!=null) this.sprBodova.nacrtajSprite(can, 0, 1,0, recNovaca);
	 nacrtajTextUnutarPravokutnika( recNovaca.right,recNovaca.top,recNovaca.width(),recNovaca.height(), textPaintNovac,Integer.toString(ukupniNovac));
	 //nacrtajTextUnutarPravokutnika( lijevoPicsGore-pomCanX,gorePics+razdHor2-pomCanY,sirBodovaPics,visBodovaPics-razdHor2, textPaintNovac,Integer.toString(ukupniNovac));
	 ///////////////////////////////////////////////////////////
	// nacrtajTextUnutarPravokutnika( lijevoPicsGore-pomCanX,gorePics-pomCanY,razdVer,razdHor1,textPaintBodovi,Integer.toString(bodovi));
	///////////////////////////crtanje valova
	
	 if(sprBodova!=null)this.sprBodova.nacrtajSprite(can, 0, 2,0, recValova);
	 nacrtajTextUnutarPravokutnika(recValova.right,recValova.top,recValova.width(),recValova.height(),textPaintVal,Integer.toString(trenutniVal)+"/"+Integer.toString(brojValova));
	// nacrtajTextUnutarPravokutnika( lijevoPicsGore-pomCanX,gorePics+razdHor1-pomCanY,razdVer,razdHor2-razdHor1,textPaintVal,Integer.toString(trenutniVal)+"/"+Integer.toString(brojValova));
	 //////////////////////////crtanje zivota

        if(brojZivota<5){
	            recSvijetloZivota.set(recZivota.left-recZivota.width()/4, recZivota.top-recZivota.width()/4, recZivota.right+5*recZivota.width()/4,recZivota. bottom-recZivota.width()/4);
	           paintSvijetlaZivota.setColor(Color.argb(255,255,222,0));
            	 paintSvijetlaZivota.setStyle(Paint.Style.FILL);
	            paintSvijetlaZivota.setShader(new RadialGradient(recSvijetloZivota.centerX() ,recSvijetloZivota.centerY() ,
		    	                (int)(recSvijetloZivota.width()*0.8), Color.argb(160, 255,222,0), Color.argb(0, 255,222,0), TileMode.CLAMP));
	                            can.drawCircle(recSvijetloZivota.centerX() ,recSvijetloZivota.centerY() ,
				                (int)(recSvijetloZivota.width()*0.8), paintSvijetlaZivota);
     }  
	 if(sprBodova!=null)this.sprBodova.nacrtajSprite(can, 0, 0,0,recZivota);
	 nacrtajTextUnutarPravokutnika(recZivota.right,recZivota.top,recZivota.width(),recZivota.height(),textPaintZivot,Integer.toString(brojZivota));
	 
	// nacrtajTextUnutarPravokutnika( lijevoPicsGore+razdVer-pomCanX,gorePics-pomCanY, sirBodovaPics-razdVer,razdHor2,textPaintZivot,Integer.toString(brojZivota));
	 }
private void nacrtajKontrolneDugmice888(){
	/*pauseResumePaint.setAntiAlias(true);
	pauseResumePaint.setFilterBitmap(true);
	pauseResumePaint.setDither(true);*/
    if(!pause) pauseSprite.nacrtajSprite(can, 0, 0, 0, rectPause,pauseResumePaint);
	else{
	
		if(this.uiMan.stvorenPauzeBitmap){
			
			pauseResumePaint.setColor(Color.BLACK);
		    pauseResumePaint.setStyle(Paint.Style.FILL);
		    pauseResumePaint.setShader(new RadialGradient(rectResume.centerX() ,rectResume.centerY() ,
				(int)(rectResume.width()*0.7), Color.BLACK, Color.argb(0, 0,0,0), TileMode.CLAMP));
        
    
    //can.drawColor(Color.argb(160, 67,54,90));
     can.drawCircle(rectResume.centerX() ,rectResume.centerY() ,
				(int)(rectResume.width()*0.7), pauseResumePaint);
     }
	
		resumeSprite.nacrtajSprite(can, 0,0, 0, rectResume,pauseResumePaint);
	}
}
private void nacrtajMenuDugmic888(){

	if(!iskljuciCrtanjeResetBotuna)restartSprite.nacrtajSprite(can, 0, 0, 0,rectMenu,pauseResumePaint);
}
private void nacrtajDodatneFunkcije888(){
	
	if(!iskljuciCrtanjeBackBotuna)exitSprite.nacrtajSprite(can, 0, 0, 0, this.rectDodatneFunkcije,pauseResumePaint);
}
private void nacrtajZavrsnuAnimacijuPobjeda( float fps){
	
	
	
	 if(tekPoceoSaZavrsnomAnimacijom) {
		MusicManager.stanjeIgre(this.uiMan.context, 4, 1,this.GL.faIgr.getBrFaze());
		 postotakUvecanjaZavrsne=60;
		 float formula=50;
		 this.yPolPljeskanja=this.uiMan.getVisEkrana()-recPljeskanja1.height()- recPljeskanja1.height()/4;
		 
		 this.recTempZavrAnim.set(this.recZavrsneAnimacije);
		 this.xPolPljeskanja=recTempZavrAnim.left;
		 
		 pomakZavrAnimY=4*this.visEkr/10;
		  recTempZavrAnim.set(recZavrsneAnimacije.left+(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.width()/100,recZavrsneAnimacije.top+(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.height()/100, recZavrsneAnimacije.right-(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.width()/100, recZavrsneAnimacije.bottom-(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.height()/100);
		// recTempZavrAnim.set(recZavrsneAnimacije);
		  recPljeskanja1.offsetTo(xPolPljeskanja+recPljeskanja1.width(),yPolPljeskanja);
		  recPljeskanja2.offsetTo(xPolPljeskanja+2*recPljeskanja1.width(),yPolPljeskanja);
		  recPljeskanja3.offsetTo(xPolPljeskanja+3*recPljeskanja1.width(),yPolPljeskanja);
		  recTempZavrAnim.set(recTempZavrAnim.left,recTempZavrAnim.top+pomakZavrAnimY,recTempZavrAnim.right,recTempZavrAnim.bottom+pomakZavrAnimY);
		  tekPoceoSaZavrsnomAnimacijom=false;
		  zavrAnimTranslacija=false;
		  zavrAnimPovecanje=false;
		  
		  pomakYEndGameIzb=recPljeskanja1.top-3*this.velGameEndIzb/4;
	    if(this.zvukPobjede>0){
	    	
	    	FazeIgre.listaSvikPustenihZvukova.add(spriteZavrsneAnimacije.soundPool.play(this.zvukPobjede, 100*FazeIgre.koeficijentPojacanjaZvuka,100*FazeIgre.koeficijentPojacanjaZvuka, 200, 0, 1f));
		      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
		    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
		      }
	              }
	
		  }
	    
		if(!zavrAnimTranslacija){
			zavrAnimTranslacija= this.spriteZavrsneAnimacije.animacijaSlaganjeTranslacijaVremenska(0,0,0,-pomakZavrAnimY, 0,0.8f, recTempZavrAnim);
			
		}
		if(!zavrAnimPovecanje){
		   zavrAnimPovecanje= spriteZavrsneAnimacije.animacijaSlaganjeUvecanjeVremenskaPix(0,0,postotakUvecanjaZavrsne*recZavrsneAnimacije.width()/100, postotakUvecanjaZavrsne*recZavrsneAnimacije.height()/100, 1f, 1f, recTempZavrAnim);
		}
		if(zavrAnimTranslacija&&zavrAnimPovecanje){
			if(this.brZvjezdica==1){
				if(tekpoceoSaPljeskanjem){
					vrijemeZaPljesak=System.currentTimeMillis();
					 recPljeskanja1.offsetTo(recTempZavrAnim.centerX()-recPljeskanja1.width()/2,yPolPljeskanja);
					  tekpoceoSaPljeskanjem=false;
				}
				if(System.currentTimeMillis()-vrijemeZaPljesak>1000) spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recPljeskanja1.left-this.pomCanX,recPljeskanja1.top-this.pomCanY,recPljeskanja1.right-this.pomCanX,recPljeskanja1.bottom-this.pomCanY), 2, 0, fps,null,true);
				if(System.currentTimeMillis()-vrijemeZaPljesak>9000) zavrsenoPljeskanje=true;
			}
			else if(this.brZvjezdica==2){
				if(tekpoceoSaPljeskanjem){
					vrijemeZaPljesak=System.currentTimeMillis();
					 recPljeskanja1.offsetTo(recTempZavrAnim.centerX()-5*recPljeskanja1.width()/4,yPolPljeskanja);
					  recPljeskanja2.offsetTo(recTempZavrAnim.centerX()+1*recPljeskanja1.width()/5,yPolPljeskanja);
					  
					  tekpoceoSaPljeskanjem=false;
				}
				if(System.currentTimeMillis()-vrijemeZaPljesak>1000)	spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recPljeskanja1.left-this.pomCanX,recPljeskanja1.top-this.pomCanY,recPljeskanja1.right-this.pomCanX,recPljeskanja1.bottom-this.pomCanY), 2, 0, fps,null,true);
				if(System.currentTimeMillis()-vrijemeZaPljesak>4000) spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recPljeskanja2.left-this.pomCanX,recPljeskanja2.top-this.pomCanY,recPljeskanja2.right-this.pomCanX,recPljeskanja2.bottom-this.pomCanY), 2, 0, fps,null,true);
				if(System.currentTimeMillis()-vrijemeZaPljesak>11000) zavrsenoPljeskanje=true;
			}
            else if(this.brZvjezdica==3){
            	if(tekpoceoSaPljeskanjem){
            		vrijemeZaPljesak=System.currentTimeMillis();
					 recPljeskanja1.offsetTo(recTempZavrAnim.centerX()-7*recPljeskanja1.width()/4,yPolPljeskanja);
					  recPljeskanja2.offsetTo(recTempZavrAnim.centerX()-2*recPljeskanja2.width()/4,yPolPljeskanja);
					  recPljeskanja3.offsetTo(recTempZavrAnim.centerX()+3*recPljeskanja3.width()/4,yPolPljeskanja);
					  tekpoceoSaPljeskanjem=false;
				}
            	if(System.currentTimeMillis()-vrijemeZaPljesak>1000){
            		spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recPljeskanja1.left-this.pomCanX,recPljeskanja1.top-this.pomCanY,recPljeskanja1.right-this.pomCanX,recPljeskanja1.bottom-this.pomCanY), 2, 0, fps,null,true);
            	}
            	if(System.currentTimeMillis()-vrijemeZaPljesak>2000){
            		spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recPljeskanja2.left-this.pomCanX,recPljeskanja2.top-this.pomCanY,recPljeskanja2.right-this.pomCanX,recPljeskanja2.bottom-this.pomCanY), 2, 0, fps,null,true);
            	}
            	if(System.currentTimeMillis()-vrijemeZaPljesak>3000){
            		spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recPljeskanja3.left-this.pomCanX,recPljeskanja3.top-this.pomCanY,recPljeskanja3.right-this.pomCanX,recPljeskanja3.bottom-this.pomCanY), 2, 0, fps,null,true);
            	}
            	if(System.currentTimeMillis()-vrijemeZaPljesak>4000){
            		zavrsenoPljeskanje=true;
            	}
			}
            else{
            	zavrsenoPljeskanje=true;
            }
			
		}
		if(this.zavrsenoPljeskanje){
			this.zavrsnaAnimacijaGotova=true;
		
			if(!endGameIzbornikPokrenut){
				pokreniGameEndIzbornik();
				endGameIzbornikPokrenut=true;
			}
			
		}
		//spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,this.recTempZavrAnim, 0, 0, fps,null,false);
		
		spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recTempZavrAnim.left-this.pomCanX,recTempZavrAnim.top-this.pomCanY,recTempZavrAnim.right-this.pomCanX,recTempZavrAnim.bottom-this.pomCanY), 0, 0, fps,null,false);
		
         
}
private void nacrtajZavrsnuAnimacijuPoraz( float fps){
	
	 if(tekPoceoSaZavrsnomAnimacijom) {
		 this.uiMan.GL.faIgr.pustiZvuk(IgricaActivity.zvukBooo, 100, 200, 100, 0);
		 postotakUvecanjaZavrsne=60;
		 float formula=50;
		 MusicManager.stanjeIgre(this.uiMan.context, 4, 2,this.GL.faIgr.getBrFaze());
		 this.recTempZavrAnim.set(this.recZavrsneAnimacije);
		 pomakZavrAnimY=-this.visEkr;
		  recTempZavrAnim.set(recZavrsneAnimacije.left+(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.width()/100,recZavrsneAnimacije.top+(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.height()/100, recZavrsneAnimacije.right-(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.width()/100, recZavrsneAnimacije.bottom-(postotakUvecanjaZavrsne/2)*recZavrsneAnimacije.height()/100);
		// recTempZavrAnim.set(recZavrsneAnimacije);
		  
		  recTempZavrAnim.set(recTempZavrAnim.left,recTempZavrAnim.top+pomakZavrAnimY,recTempZavrAnim.right,recTempZavrAnim.bottom+pomakZavrAnimY);
		  tekPoceoSaZavrsnomAnimacijom=false;
		  zavrAnimTranslacija=false;
		  zavrAnimPovecanje=false;
		  
		  pomakYEndGameIzb=uiMan.getVisEkrana()-velGameEndIzb;//izborEndGame.getGlavniRectPrikaza().height();
		 // pomakYEndGameIzb=postotakUvecanjaZavrsne*recZavrsneAnimacije.height()/100 +recZavrsneAnimacije.bottom;
		
		  }
		if(!zavrAnimTranslacija){
			zavrAnimTranslacija= this.spriteZavrsneAnimacije.animacijaSlaganjeTranslacijaVremenska(0,0,0,-pomakZavrAnimY, 0,2.8f, recTempZavrAnim);
		}
		if(!zavrAnimPovecanje){
		   zavrAnimPovecanje= spriteZavrsneAnimacije.animacijaSlaganjeUvecanjeVremenskaPix(0,0,postotakUvecanjaZavrsne*recZavrsneAnimacije.width()/100, postotakUvecanjaZavrsne*recZavrsneAnimacije.height()/100, 1f, 1f, recTempZavrAnim);
		}

		if(zavrAnimTranslacija){//&&zavrAnimPovecanje){
			if(!endGameIzbornikPokrenut){
				pokreniGameEndIzbornik();
				endGameIzbornikPokrenut=true;
			}
			this.zavrsnaAnimacijaGotova=true;
		}
		
	
	
		spriteZavrsneAnimacije.animacijaSlaganjeNacrtaj(can,new RectF(recTempZavrAnim.left-this.pomCanX,recTempZavrAnim.top-this.pomCanY,recTempZavrAnim.right-this.pomCanX,recTempZavrAnim.bottom-this.pomCanY), 1, 0, fps,null,false);

	
		
	
	
	
	
}
private void provjeriTouchedNaDugiceIIzvrsi888(){
	
	 if(touchedPauseResume){// provjerava na koju je stranu dugmiæa kliknuto lijevu ili desnu
		if(!pause){
			
			pauseOdPauzeGumica=true;
			kliknutoNaPause();
			pause=true;// daje doznanja ostatku logike da je kliknuto na pause
		}
		else{
			kliknutoNaResume();
			pause=false;// daje doznanja ostatku logike da više nije u stanju pauze
		}
		touchedDolPro=false;
		touchedPauseResume=false;
	}
	 
	    if(touchedMenu){// pošto je samo taj dugmiæ jednostavno se izvršava
		  kliknutoNaReset();
		  touchedMenu=false;
	    }
	    else if(touchedDodatneFunkcije){
		   kliknutoNaExit();
		   touchedDodatneFunkcije=false;
     	}
	    if(!pause){
	    	if(touchedDolPro){
	    	kliknutoNaDolazakProtivnika() ;
		   this.touchedDolPro=false;
	    }
	 }
}
private void stvariKojeSeTrebajuObavitSamoJedanput(){// na poèetku run metode i poslie se ne izvršava u sledeæim ciklusima
	 this.sirEkr=can.getWidth();
	 this.visEkr=can.getHeight();
	 proslaBoja=System.currentTimeMillis();
	 sprAbility=this.uiMan.GL.faIgr.getSprite(700);

	 izracunajDimennzijeZaPrikaz();
	 this.recZavrsneAnimacije.set(0, 0, sirEkr,3*visEkr/4);
	 recPljeskanja1.set(0,0,2*recZavrsneAnimacije.height()/6,2*recZavrsneAnimacije.height()/6);
	 recPljeskanja2.set(0,0,2*recZavrsneAnimacije.height()/6,2*recZavrsneAnimacije.height()/6);
	 recPljeskanja3.set(0,0,2*recZavrsneAnimacije.height()/6,2*recZavrsneAnimacije.height()/6);
	 recDolaProt.set(0, 0, this.sirDolPro*this.PpCmX, this.visDolPro*this.PpCmY);
	 rectMenu.set(sirEkr-desnoPicsDole-sirMenuDugmPics,visEkr-dolePics-visMenuDugmPics,sirEkr-desnoPicsDole,visEkr-dolePics);
     rectDodatneFunkcije.set(lijevoPicsDole,visEkr-dolePics-visDodFunkPics,lijevoPicsDole+sirDodFunkPics,visEkr-dolePics);
	
	 this.tekPoceo=false;
}
private void odrediAlfuZaHodanje(){
   if(secDoNovVala!=this.prosSecNovVala){	
	prosSecNovVala=secDoNovVala;
	if(this.secDoNovVala<2) this.paintZaHodanje.setAlpha(255);
    else if(this.secDoNovVala<3) this.paintZaHodanje.setAlpha(230);
    else if(this.secDoNovVala<4) this.paintZaHodanje.setAlpha(210);
    else if(this.secDoNovVala<5) this.paintZaHodanje.setAlpha(200);
    else if(this.secDoNovVala<6) this.paintZaHodanje.setAlpha(190);
    else if(this.secDoNovVala<7) this.paintZaHodanje.setAlpha(180);
    else if(this.secDoNovVala<8) this.paintZaHodanje.setAlpha(170);
    else if(this.secDoNovVala<9) this.paintZaHodanje.setAlpha(160);
    else if(this.secDoNovVala<10) this.paintZaHodanje.setAlpha(150);
    else if(this.secDoNovVala<11) this.paintZaHodanje.setAlpha(140);
    else if(this.secDoNovVala<12) this.paintZaHodanje.setAlpha(130);
    else if(this.secDoNovVala<13) this.paintZaHodanje.setAlpha(120);
    else if(this.secDoNovVala<14) this.paintZaHodanje.setAlpha(110);
    else if(this.secDoNovVala<15) this.paintZaHodanje.setAlpha(100);
    else if(this.secDoNovVala<16) this.paintZaHodanje.setAlpha(90);
    else if(this.secDoNovVala<17) this.paintZaHodanje.setAlpha(80);
    else if(this.secDoNovVala<18) this.paintZaHodanje.setAlpha(70);
    else if(this.secDoNovVala<19) this.paintZaHodanje.setAlpha(60);
    else if(this.secDoNovVala<20) this.paintZaHodanje.setAlpha(50);
    else if(this.secDoNovVala<21) this.paintZaHodanje.setAlpha(40);
    else if(this.secDoNovVala<22) this.paintZaHodanje.setAlpha(30);
    else if(this.secDoNovVala<23) this.paintZaHodanje.setAlpha(20);
    else if(this.secDoNovVala<24) this.paintZaHodanje.setAlpha(10);
 
   /* else if(this.secDoNovVala<15) this.paintZaHodanje.setAlpha(10);
    else if(this.secDoNovVala<16) this.paintZaHodanje.setAlpha(85);
    else if(this.secDoNovVala<17) this.paintZaHodanje.setAlpha(80);
    else if(this.secDoNovVala<18) this.paintZaHodanje.setAlpha(75);
    else if(this.secDoNovVala<19) this.paintZaHodanje.setAlpha(70);
    else if(this.secDoNovVala<20) this.paintZaHodanje.setAlpha(65);
    else if(this.secDoNovVala<21) this.paintZaHodanje.setAlpha(60);
    else if(this.secDoNovVala<22) this.paintZaHodanje.setAlpha(55);
    else if(this.secDoNovVala<23) this.paintZaHodanje.setAlpha(50);
    else if(this.secDoNovVala<24) this.paintZaHodanje.setAlpha(45);
	
    else if(this.secDoNovVala<25) this.paintZaHodanje.setAlpha(40);
    else if(this.secDoNovVala<26) this.paintZaHodanje.setAlpha(35);
    else if(this.secDoNovVala<27) this.paintZaHodanje.setAlpha(30);
    else if(this.secDoNovVala<28) this.paintZaHodanje.setAlpha(28);
    else if(this.secDoNovVala<29) this.paintZaHodanje.setAlpha(26);
    else if(this.secDoNovVala<30) this.paintZaHodanje.setAlpha(24);
    else if(this.secDoNovVala<31) this.paintZaHodanje.setAlpha(22);
    else if(this.secDoNovVala<32) this.paintZaHodanje.setAlpha(21);
    else if(this.secDoNovVala<33) this.paintZaHodanje.setAlpha(20);
    else if(this.secDoNovVala<34) this.paintZaHodanje.setAlpha(19);
    else if(this.secDoNovVala<35) this.paintZaHodanje.setAlpha(18);
    else if(this.secDoNovVala<36) this.paintZaHodanje.setAlpha(17);
    else if(this.secDoNovVala<37) this.paintZaHodanje.setAlpha(16);
    else if(this.secDoNovVala<38) this.paintZaHodanje.setAlpha(15);
    else if(this.secDoNovVala<39) this.paintZaHodanje.setAlpha(14);*/
    else this.paintZaHodanje.setAlpha(13);
   }
}
private void kliknutoNaReset(){// ovo se izvršava u slièaju da je kliknuto na jedan od dugmiæa
 if(tekPoceoSaZavrsnomAnimacijom) {
	
	 if(!pokrenutExitIzbornik){
		 uiMan.postaviTempUniverzalniIzbornik(izborRestart);// funkcija sluzi za back botun 	 
		this.izborRestart.pokreniMojIzbornik(null);
		if(!pauseOdPauzeGumica)kliknutoNaPause();
		pokrenutExitIzbornik=true;
	}
 }
}
private void kliknutoNaDolazakProtivnika(){
	if(!this.poslaoNovceGameLogicu){
		this.GL.dodajNovacPlusMinus(this.secDoNovVala*this.novacPoSekundiPrijeDolaskaNovogVala);
	    this.poslaoNovceGameLogicu=true;
	}
	this.GL.skociNaSljedeci();
	
}
@TargetApi(Build.VERSION_CODES.FROYO)
private void pauzirajZvuk(){
	if(IgricaActivity.apiLevel>=8) GL.faIgr.soundPool.autoPause();
	MusicManager.pause(0);
}
private void kliknutoNaPause(){

	GL.pause();
	pauzirajZvuk();
}
@TargetApi(Build.VERSION_CODES.FROYO)
private void resumeZvuk(){
	if(IgricaActivity.apiLevel>=8)  GL.faIgr.soundPool.autoResume();;
}
private void kliknutoNaResume(){
    GL.resume();
    resumeZvuk();
    pauseOdPauzeGumica=false;
    MusicManager.play(0);
}
public void kliknutoNaExit(){
	if(!pokrenutExitIzbornik&&uiMan!=null){// bug 1.
		uiMan.postaviTempUniverzalniIzbornik(izborExit);// funkcija sluzi za back botun 	 
		this.izborExit.pokreniMojIzbornik(null);
		if(!pauseOdPauzeGumica)kliknutoNaPause();
		pokrenutExitIzbornik=true;
	}
}

private void izracunajDimennzijeZaPrikaz(){
	 lijevoPicsGore=(sirEkr/100)*lijevoPostoGore;
	 lijevoPicsDole=(sirEkr/100)*lijevoPostoDole;
	 desnoPicsDole=(sirEkr/100)*desnoPostoDole;// 
	 desnoPicsGore=(sirEkr/100)*desnoPostoGore;
	 gorePics=(visEkr/100)*gorePosto;// 
	 dolePics=(visEkr/100)*dolePosto;
	 this.sirDodFunkPics= this.sirDodFunkPosto*(((sirEkr+visEkr)/2)/100);
	 this.visDodFunkPics=this.visDodFunkPosto*(((sirEkr+visEkr)/2)/100);
	 sirMenuDugmPics=sirMenuDugmPosto*(((sirEkr+visEkr)/2)/100);
	 visMenuDugmPics=visMenuDugmPosto*(((sirEkr+visEkr)/2)/100);
	
	 sirBodovaPics=(((sirEkr+visEkr)/2)/100)*sirBodovaPosto;
	 visBodovaPics=(((sirEkr+visEkr)/2)/100)*visBodovaPosto;
	
}
///////////////////////////////
//////// neke cesto koristene metode
private void nacrtajTextUnutarPravokutnika(float pozX,float pozY,float sir,float vis, TextPaint tp, String text){
	int brClanova=text.length();
	////
	
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

///////ui manager object interfejs metode		
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
		//bug 2
		if(sprAbility!=null&&uiMan.GL.faIgr!=null)sprAbility=this.uiMan.GL.faIgr.getSprite(700);
		if(izborLavaBomb!=null)this.izborLavaBomb.onSystemResume();// u slucaju da se pokrenu prije nego se taskabr uspije updatat
		if(izborRenforce!=null)this.izborRenforce.onSystemResume();
	}

	@Override
	public boolean getDaliDaIgnoriramTouch() {
		// TODO Auto-generated method stub
		return false;
	}

}
