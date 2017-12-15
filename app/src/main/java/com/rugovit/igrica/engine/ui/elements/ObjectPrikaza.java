package com.rugovit.igrica.engine.ui.elements;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.engine.ui.Sjena;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;

public class ObjectPrikaza implements UIManagerObject {//, ObjectLinkerPrikaz {
	
	//vanj crt iznad
	private GameEvent geVanjskiIznad=null;
	private int brSlVanjIznad=0;
	private int brRedVanjIznad=0;
	private boolean crtajVanjskiIznad=false;
	private RectF recVanjskiIznad=null;
	//////
	private boolean lijecenje=false;
	
	private boolean preskociPrviTakt=false;
	private GameEvent geSmrtBezLokve;
	private boolean crtanjeSmrtiBezLokve=false;
	private float smrtPomakX,smrtPomakY, smrtVelX,smrtVelY;
	private boolean tekPoceoRazHeltha=true;
	private float stariHelth=100;
	private float razlikHeltha;
	private double vrijemeRazlikeHeltha;
	private int vrijemeMiliStojanjaRazlikeHeltha=1700;
	private RectF recHelthRazlike=new RectF();
	
	private Paint paintZaArmorBar=new Paint();
	private Paint paintZaArmorBarRazlika=new Paint();
	private float armor;
	private boolean tekPoceoRazArmor=true;
	private float stariArmor=100;
	private float razlikArmor;
	private double vrijemeRazlikeArmor;
	private int vrijemeMiliStojanjaRazlikeArmor=1700;
	private RectF recArmorRazlike=new RectF();
	
	int brSlikeIstocno;
	int redSlIstocno;
	int brSlikeSjeveroIstocno;
	int redSlSjeveroIstocno;
	int brSlikeSjeverno;
	int redSlSjeverno;
	int brSlikeSjeveroZapadno;
	int redSlSjeveroZapadno;
	int brSlikeZapadno;
	int redSlZapadno;
	int brSlikeJugoZapadno; 
	int redSlJugoZapadno;
	int brSlikeJuzno;
	int redSlJuzno;
	int brSlikeJugoIstocno;
	int redSlJugoIstocno;
	
	
	private float postoSmanjenjaSjeneKodPika;
	private float picPosto;
    private float ukupniPomakSkokic;
	private int brSlikeZaHodanjeGore=0,brSlikeZaHodanjeDolje=0,brSlikeZaHodanjeDesno=0,brSlikeZaHodanjeLijevo=0,brSlikeZaBorbuLijevo=1,brSlikeZaBrbuDesno=1;
	private int brRedakaZaHodanjeGore=3,brRedakaZaHodanjeDolje=2,brRedakaZaHodanjeDesno=0,brRedakaZaHodanjeLijevo=1,brRedakaZaBorbuLijevo=1,brRedakaZaBrbuDesno=0;
	private boolean kliked=false;
	private RectF recSmrti=new RectF();
	private GameEvent geSmrti;
	private boolean dovrsioAnimacijuUmiranjaProtivnik=false;
	//private float xTelep,yTelep;
	private int tempBrRedIzlIzZemlje;
	private int tempBrSlIzIzZemlje;
	private RectF recIzlIzZemlje=new RectF();
	private RectF recIzlIzZemljeRez=new RectF();
	private RectF recTelep=new RectF();
	private GameEvent geTelep=new GameEvent(null);
	private GameEvent geIzlIzZemlje=new GameEvent(null);
	private boolean zavrsioCrtanjeTelPrviDio=false;
	private boolean nestanakProtivnika=false;
	
	private RectF recLijecenje=new RectF();
	private GameEvent geLijecenje=new GameEvent(null);
	
	private boolean crtajDaJeOznacen=false;
	private boolean kliknutoNaObjekt=false;
	private int randomKutOnomatopeje=0;
	private double vriStojOnomatopeje, vrijemePocOnomatopeje;
	private int randomSlikaOnomatopeje=0;
	private RectF recOnomatopeje;
	private boolean ispaliOnomatopeju=false;
	private boolean onomatopejaDesnoIliLijevo;
	private boolean crtajOnomatopeju=false;
	private boolean daliCrtamOnomatopeje=false;
	private SpriteHendler spriteOnomatopeje;
	///////////
	private int slikaLokve=-1;// ako ostane -1 zna�?i n da nema slike u spriteu
	private boolean gotovoNestajanje=false;
	private double vrijemeUmiranja=10000;
	private double poceoUmirati=-1;
	/////////strijelci varijable
	private boolean borbaNaLijevo=false,borbaNaDesno=false;
	private boolean ispaljivanje=false;
	private boolean ciljanje=false;
	private float xProt;
	private float yProt;
	
	private int smijerProt;
	private int slIspucavanja;
	private int ticVoj;
	private double ispaljivanjeProsloVrijeme;
	/////////konstante
	private int iHodanjaPocinjeOd=0;// od koje slièice u redu poèinje hodanje
	//// sprite vrrijeme cekanja
    private int zadnjaSlicicaHodanja,zadnjiRedakHodanja;
    private int zadnjaSlikaHodanja;
	////////// crtanje
    private double vrijemePocetkaCiklusHodaZaSkokic;

    private float picUSl1;
    private float pixVisSkoka1;
    private float vrijemeSkoka1;
    private float vrijemeKraja;
    private float pixPoSecSkok1;
    private float visSjene,sirSjene;
	private SpriteHendler spriteZaZajednickeEfekte;
    private float yPomakUOdnosuNaLogiku=0;
    private int brSlicStojanja=0;// broj slièice 0-3  stojanja
    private int brSlikeStojanja=0;
	private double poceoStojati=-1;
	
	private int vrijemePauze=0;
	private boolean udaranjeDesno=false;
	private boolean udaranjeLijevo=false;
	private int     iBorbeL=1, iBorbeD;
	private boolean posebnoNamjestenBorbaDx=false;
	private float borbaLeftDx,borbaRightDx;
	private boolean daliPostojiSlikaBorbe=false;// ta slik  će biti na drugoj pozicji

	private int brStuBorbe;
	private int pocSlic;
	private double prosVrijeme,prosloVrijemeSkokica;
	///////////
	   private double prosVrijemeGorenja;
       private int iCrtanjaGorenja=0;
    /////////////
    ///////////
	   private double prosVrijemeTrovanja;
       private int iCrtanjaTrovanja=0;
    /////////////
       ///////////
	   private double prosVrijemeZaledivanja;
       private int iCrtanjaZaledivanja=0;
    /////////////   
	private float dxHod;
	private float dyHod;
	private float omjHod;
	private int maxIndxSl1;
	private float miliSecSl1=0,miliSecSl2;
	private float miliSecBorbaLijevo=0,miliSecBorbaDesno;
	private float dxLog,dyLog;
	private float maxAkceleracijaPosto ;
	////////
	private Random generator;
	private float PpCmX; 
	private float PpCmY;
	private float helth;// helth je u postotcima
	private int iHodanjaDesno=0,iHodanjaLijevo=0,iHodanjaGore=0,iHodanjaDolje=0;
	private float sirIscr,visIscr,x=-10000,y=-10000,xPros=-10000,yPros=-10000, tempX=-10000,tempY=-10000,fps;
	private float xPolToucha, yPolToucha,visinaTouch,sirinaTouch;
	private GameLogicObject dvojnik;
	private RectF kvadratCrtanja,kvadratCrtanjaSkokici;
	private RectF kvadratCrtanjaPremaLijevo;
	private GameEvent ge,geSjena;
	private Canvas can;
	private int indikator;
	private UIManagerObject tempTauchedObj;// objekt na koji se usmjerava ovaj objekt
	private boolean zivSam=true;// stanje objekta, mjenjaju ga samo objekti logike
	private boolean prvoPokretanje=true;//sluzi za slanje pocetnih informacija objektu igre
	private boolean boolZaSlanje=false;// oznaèava da je napravljena promjena na EventObjektu
	private boolean imTouched=false;// sluzi za signalizaciju da se oèekuje sljedeæi touch
	private SpriteHendler spriteHend; // upravlja sprijtevima
	private Paint paintZaHelthBar, paintZaHelthBarRazlika= new Paint();
	private UIManager uiMan;
	private Sjena mojaSjena;
	public  ObjectPrikaza(float x, float y,SpriteHendler spriteHend,float sirinaTouch,float visinaTouch,float sirIscr,float visIscr, int indikator){
		this.x=x;
		this.y=y;
		if(indikator>0) dovrsioAnimacijuUmiranjaProtivnik=true;
		geSmrti=new GameEvent(null);
		geSmrti.jesamLiZiv=true;
		ge=new GameEvent(this);
		geSjena=new GameEvent(this);
		this.indikator=indikator;
		this.sirinaTouch=sirinaTouch;// visina i sirina polja gdje æe se detektirati dodir, treba biti iste velièine kao i kvadrat objekta igre
		this.visinaTouch=visinaTouch;
		this.sirIscr=sirIscr;
		this.visIscr=visIscr;
		if(indikator >0) iHodanjaPocinjeOd=1;// ako je branitelj ostavlja se jedna slièica za stojanje
		else iHodanjaPocinjeOd=0;// ako je protivnik onda stalno hoda tako da krece od 0
		//this.eMan=eMan;  ///moram imat referencu na eeventmanager da bi mogao slati poruke
		//ge= new GameEvent(this);  // stvara se GameEvent objekt za komunikaciju
		this.spriteHend=spriteHend;
		
		/*try {
			this.spriteHend=(SpriteHendler)spriteHend.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		paintZaHelthBar=new Paint();
		generator=new Random();
		
		 kvadratCrtanja=new RectF(tempX,tempY,tempX+sirIscr,tempY+visIscr);
		 kvadratCrtanjaSkokici=new RectF(tempX,tempY,tempX+sirIscr,tempY+visIscr);
		 
		    brSlikeIstocno=3;//     istoèno
			redSlIstocno=0;
			
		brSlikeSjeveroIstocno=3;
		redSlSjeveroIstocno=1;
			
		brSlikeSjeverno=3;
		redSlSjeverno=2;
		
		brSlikeSjeveroZapadno=3;
		redSlSjeveroZapadno=3;
		
		brSlikeZapadno=3;
		redSlZapadno=4;
		
		brSlikeJugoZapadno=3; 
		redSlJugoZapadno=5;
		
		brSlikeJuzno=3;
		redSlJuzno=6;
		
		brSlikeJugoIstocno=3;
		redSlJugoIstocno=7;
				
		}

	
		// bilderSetirajSkokiceUHodu(50, kvadratCrtanja.height()/10);
		///	TEST///
		//p=new Paint();
		//p.setARGB(200,50,250,50);
	//	p.setStyle(Paint.Style.FILL);
		/////////////
	
	public void nacrtaj(Canvas can,float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
		///////
		this.fps=fps;
		this.can=can;
		this.uiMan=uiMan;
		if(!preskociPrviTakt){
		xPolToucha=x+ge.trimTouchPolX;
    	yPolToucha=y+ge.trimTouchPolY;
		tempX=x; //mora sam uvrstit ovako da mi se ne promjeni x tijekom crtanja
	    tempY=y;  //ovdje se stvara x i y velièina za cijelu klasu xTemp i yTemp oznaèavaju vidljivi položaj objekta na ekranu
	    //TEST //sve ove testove treba izbrisat poslije ubacio samih da nacrtam kvadrat umjesto spritea
	  //  can.drawRect(tempX,tempY,tempX+sirIscr,tempY+visIscr, p);
	    kliked=false;
	    //////
	    kvadratCrtanja.set(tempX,tempY,tempX+sirIscr,tempY+visIscr);
	    if( lijecenje){
	    	this.recLijecenje.set(kvadratCrtanja.centerX()-0.65f*kvadratCrtanja.height(), kvadratCrtanja.top-kvadratCrtanja.height()/3-1.3f*kvadratCrtanja.height(),kvadratCrtanja.centerX()+0.6f*kvadratCrtanja.height(), kvadratCrtanja.top-kvadratCrtanja.height()/3);
	    	crtajLijecenje();
	    }
		if(this.ge.teleportacija){
			this.recTelep.set(kvadratCrtanja.left-kvadratCrtanja.width()/6, kvadratCrtanja.top-kvadratCrtanja.height()/6, kvadratCrtanja.right+kvadratCrtanja.width()/6, kvadratCrtanja.bottom+kvadratCrtanja.height()/6);
		}
	
        
	     if(prvoPokretanje){
	    	 this.can=can;
	    	 this.PpCmX=PpCmX;
	 		 this.PpCmY=PpCmY;
	    	 stvariKojeSeIzvrsavajuSamoJedanput();
	    	 prvoPokretanje=false;
	     }
		//kvadratCrtanjaPremaLijevo=new RectF(tempX-sirIscr,tempY,tempX,tempY+visIscr);
	    //can.drawBitmap(slika,null,kvadratCrtanja,null);
		//spriteHend.nacrtajSprite(can, 0, 0, 0, kvadratCrtanja);
	 	if(ge.izlazakIzZemlje){
			crtajIzlazakIzZemlje();
		}
	     if(zivSam){
	    	 if(!nestanakProtivnika){
		      if(!this.ciljanje&&!this.ispaljivanje){
			     hodanjeIliBorba();
		        }
		      else {
			     nacrtajVojnikeStrijelce();
		          }
		     nacrtajHelthBar();
		     nacrtajArmorBar();
		     if(spriteZaZajednickeEfekte!=null)if(this.ge.otrovan&&this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(1)) crtajTrovanja();
		     if(spriteZaZajednickeEfekte!=null) if(this.ge.gorim&&this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(0)) crtajGorenje();
		  
		     if(spriteZaZajednickeEfekte!=null) if(this.ge.zaleden&&this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(2)) crtajZaledivanja();
		     if(daliCrtamOnomatopeje){ 
		    	 this.crtajOnomatopejuUdarca(ispaliOnomatopeju );
		    	 }
		     crtajVanjskiIznad();
	         }
		     if(ge.teleportacija)crtajTeleportaciju();
		      posaljiEvent();  // posaljiEvent kod se izvršava u ovisnosti o boolZaSlanje varijabli
		      xPros=tempX;
		      yPros=tempY;
		     
		  
		      }
		  else{
			   zapocmiProcesUmiranja();
		      }
		}
		preskociPrviTakt=false;
		 
	}
    
	////////////
	public boolean jeliGotovVanjskiCrtacIznad(){
		return  crtajVanjskiIznad;
	}
	public void vanjskiCrtacIznad(float x, float y, float sir, float vis, int brSl, int brRed){
		if(geVanjskiIznad==null){
			geVanjskiIznad=new GameEvent(null);
			geVanjskiIznad.jesamLiZiv=true;
		}
	
	    brSlVanjIznad=brSl;
       brRedVanjIznad=brRed;
		crtajVanjskiIznad=true;
		recVanjskiIznad=new RectF();
		this.recVanjskiIznad.set(x,y,x+sir, y+vis);
	}
	////////////bilder metode
	public void preskociPrviTakt(){
		preskociPrviTakt=true;
	}
	public void postaviCrtanjeSmrtiBezLokve(float pomX,float pomY,float velXDodNaGlavni,float velYDodNaGlavni){
		crtanjeSmrtiBezLokve=true;
		this. smrtPomakX=pomX;
		this. smrtPomakY=pomY;
		this. smrtVelX=velXDodNaGlavni;
		this.smrtVelY=velYDodNaGlavni;
		geSmrtBezLokve=new GameEvent(null);
		geSmrtBezLokve.jesamLiZiv=true;
	}
	public void postaviRucnoAnimacijeHoda(int brSlikeGore,int brRedGore, int brSlikeDolje,int brRedDolje,int brSlikeDesno, int brRedDesno,int brSlikeLijevo, int brRedLijevo){
		brSlikeZaHodanjeGore= brSlikeGore;
		brRedakaZaHodanjeGore=brRedGore;
		brSlikeZaHodanjeDolje=brSlikeDolje;
		brRedakaZaHodanjeDolje=brRedDolje;
		
		brSlikeZaHodanjeDesno=brSlikeDesno;
		brRedakaZaHodanjeDesno=brRedDesno;
		brRedakaZaHodanjeLijevo=brRedLijevo;
		brSlikeZaHodanjeLijevo=brSlikeLijevo;
		
	} 
	public void postaviRucnoAnimacijeBorba(int brSlikeDesno,int brRedDesno, int brSlikeLijevo,int brRedLijevo){

		this.brSlikeZaBrbuDesno=brSlikeDesno;
		this.brRedakaZaBrbuDesno=brRedDesno;
		this.brSlikeZaBorbuLijevo=brRedLijevo;
		this.brRedakaZaBorbuLijevo=brSlikeLijevo;
		
	} 
	public void postaviRucnoAnimacijeIspucavanja(int brSlikeIstocno, int redSlIstocno,
			int brSlikeSjeveroIstocno, int redSlSjeveroIstocno,int brSlikeSjeverno,int redSlSjeverno,
			int brSlikeSjeveroZapadno, int redSlSjeveroZapadno, int brSlikeZapadno, int redSlZapadno
			,int brSlikeJugoZapadno, int redSlJugoZapadno,int brSlikeJuzno, int redSlJuzno,int brSlikeJugoIstocno, int redSlJugoIstocno){
		    this.brSlikeIstocno=brSlikeIstocno;
		    this. redSlIstocno=redSlIstocno;
		    this. brSlikeSjeveroIstocno=brSlikeSjeveroIstocno;
		    this. redSlSjeveroIstocno=redSlSjeveroIstocno;
		    this.brSlikeSjeverno=brSlikeSjeverno;
		    this.redSlSjeverno=redSlSjeverno;
		    this.brSlikeSjeveroZapadno=brSlikeSjeveroZapadno;
		    this.redSlSjeveroZapadno=redSlSjeveroZapadno;
		    this.brSlikeZapadno=brSlikeZapadno;
		    this.redSlZapadno=redSlZapadno;
		    this.brSlikeJugoZapadno=brSlikeJugoZapadno; 
		    this.redSlJugoZapadno=redSlJugoZapadno;
		    this.brSlikeJuzno=brSlikeJuzno;
		    this. redSlJuzno=redSlJuzno;
		    this.brSlikeJugoIstocno=brSlikeJugoIstocno;
		    this.redSlJugoIstocno=redSlJugoIstocno;
	} 
	public void bilderDodajSpriteOnomatopeja(SpriteHendler sprite){
		if(sprite!=null){
			daliCrtamOnomatopeje=true;
		
			spriteOnomatopeje=sprite;
			
		}
		else this.daliCrtamOnomatopeje=false;
		
	} 
	public void bilderDoddajDodatniSpriteZaZajednickeEfekte(SpriteHendler sprite){
		spriteZaZajednickeEfekte=sprite;
		/*try {
			spriteZaZajednickeEfekte=(SpriteHendler)sprite.clone();
			
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	public void bilderPomakObjektUOdnosuNaLogiku(float pomakPix){
		this.yPomakUOdnosuNaLogiku=pomakPix;
	}
	public void bilderSetirajDxPomakZaSjenuTijekomBorbe(float borbaLeftDx,float borbaRightDx){
		this.borbaLeftDx=borbaLeftDx;
		this.borbaRightDx=borbaRightDx;
		posebnoNamjestenBorbaDx=true;
	}
	public void bilderSetirajSkokiceUHodu(float prviPikPostoCiklusa,float pixVisSkoka1,float postoSmanjenjaSjeneKodPika ){
	   if(spriteHend!=null) {
		   picPosto=prviPikPostoCiklusa;
		   this.postoSmanjenjaSjeneKodPika=postoSmanjenjaSjeneKodPika;
			miliSecSl1=1000/spriteHend.brojPrikazaPoSekundi(brSlikeZaHodanjeDesno);
		    this.pixVisSkoka1=pixVisSkoka1;
            this.picUSl1=((float)spriteHend.brojStupaca(brSlikeZaHodanjeDesno)/100)*prviPikPostoCiklusa;
		    this.vrijemeSkoka1=prviPikPostoCiklusa*((spriteHend.brojStupaca(brSlikeZaHodanjeDesno)*miliSecSl1)/100);
		
		    this.pixPoSecSkok1=pixVisSkoka1/vrijemeSkoka1;
		    vrijemeKraja=spriteHend.brojStupaca(brSlikeZaHodanjeDesno)*miliSecSl1;
		    this.maxAkceleracijaPosto= maxAkceleracijaPosto ;
		}
	}
	////////////privatne metode
	private boolean jeliTockaUnutarKruga(float xTocke,float yTocke,float xCentra,float yCentra, float radius){
		boolean b=false;
		
		if(udaljenostDvijeTocke(xTocke,yTocke,xCentra,yCentra)<radius){
			b=true;
		}
		
		return b;
	}
	private double udaljenostDvijeTocke(float ax,float ay, float bx,float by){// predpostavlja da se sve odvija u prvom kvadrantu
		 return Math.hypot(ax-bx,ay-by);
	}
	private void crtajOnomatopejuUdarca(boolean tekPoceo){
		if(tekPoceo){			
			randomKutOnomatopeje=randIzmeduCijeli( 0,60);
			float xRandomPos=randIzmeduCijeli( 0,10);
			float yRandomPos=randIzmeduCijeli( 0,30);
			if(randIzmeduCijeli( 0,1)==1)xRandomPos=-xRandomPos;
			if(randIzmeduCijeli( 0,1)==1)yRandomPos=-yRandomPos;
			if(randIzmeduCijeli( 0,1)==1) randomKutOnomatopeje=-randomKutOnomatopeje; 
			vrijemePocOnomatopeje=System.currentTimeMillis();
			if(this.onomatopejaDesnoIliLijevo)recOnomatopeje.set(this.kvadratCrtanja.centerX()+xRandomPos*(recOnomatopeje.width()/100), kvadratCrtanja.top-2*recOnomatopeje.height()+yRandomPos*(recOnomatopeje.height()/100), this.kvadratCrtanja.centerX()+recOnomatopeje.width()+xRandomPos*(recOnomatopeje.width()/100), kvadratCrtanja.top-1*recOnomatopeje.height()+yRandomPos*(recOnomatopeje.height()/100));
			else recOnomatopeje.set(this.kvadratCrtanja.centerX()-recOnomatopeje.width()+xRandomPos*(recOnomatopeje.width()/100), kvadratCrtanja.top-2*recOnomatopeje.height()+yRandomPos*(recOnomatopeje.height()/100), this.kvadratCrtanja.centerX()+xRandomPos*(recOnomatopeje.width()/100), kvadratCrtanja.top-1*recOnomatopeje.height()+yRandomPos*(recOnomatopeje.height()/100));
			ispaliOnomatopeju=false;
			randomSlikaOnomatopeje=randIzmeduCijeli( 0, spriteOnomatopeje.brojSlika()-1);
			crtajOnomatopeju=true;
		}
		if(crtajOnomatopeju){
			vrijemePocOnomatopeje+=this.vrijemePauze;
			if(vrijemePocOnomatopeje+this.vriStojOnomatopeje>System.currentTimeMillis()) this.spriteOnomatopeje.nacrtajSpriteRotiran(can, randomSlikaOnomatopeje, 0,0, recOnomatopeje,randomKutOnomatopeje,null);
			else crtajOnomatopeju=false; 
		
		}
		
	}
	private void zapocmiProcesUmiranja(){
		 if(ge.indikator==-100){
			 dovrsioAnimacijuUmiranjaProtivnik=true;// znaci da je brisanje i necrta animaciju umiranja
		}
		if(crtanjeSmrtiBezLokve&&!dovrsioAnimacijuUmiranjaProtivnik){
			if(this.geSjena.jesamLiZiv){
				this.geSjena.jesamLiZiv=false;
				this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
				}
			crtanjeSmrtiBezLokve=true;
			RectF temRec=new RectF();
			temRec.set(    smrtPomakX+this.kvadratCrtanja.left-smrtVelX/2, kvadratCrtanja.top+smrtPomakY-smrtVelY/2, smrtPomakX+kvadratCrtanja.right+smrtVelX/2, kvadratCrtanja.bottom+smrtPomakY+smrtVelY/2);
			dovrsioAnimacijuUmiranjaProtivnik=this.spriteHend.vrtiAnimacijuReda(can, geSmrtBezLokve, 2, 0, temRec, null);
			if(this.dovrsioAnimacijuUmiranjaProtivnik)uiMan.makniObjekt(this);
		} 
		
		else {
		if(!dovrsioAnimacijuUmiranjaProtivnik){
			float povecanje=kvadratCrtanja.width()/6;
			 recSmrti.set(kvadratCrtanja.left-povecanje, kvadratCrtanja.top-2*povecanje, kvadratCrtanja.right+povecanje,kvadratCrtanja. bottom-povecanje);
			// recSmrti.set(kvadratCrtanja.left-kvadratCrtanja.width()/4, kvadratCrtanja.top-kvadratCrtanja.height()/4, kvadratCrtanja.right+kvadratCrtanja.width()/4,kvadratCrtanja. bottom+kvadratCrtanja.height()/4);
			dovrsioAnimacijuUmiranjaProtivnik=spriteZaZajednickeEfekte.vrtiAnimacijuReda(can,geSmrti,7,0, recSmrti, null);
	      
	    	visSjene=3*this.recSmrti.width()/10;
	    	sirSjene=this.recSmrti.width();
	    	  this.mojaSjena.namjestiVelicinuSjene(sirSjene,visSjene);
	    	geSjena.x=(this. recSmrti.centerX()-this.sirSjene/2);
	    	geSjena.y=this. recSmrti.bottom-0.75f*this.visSjene;
	    	
		}
		
		if(this.spriteHend.postojiLiSlikNaMjestu(2)&&ge.indikator!=-100){
			if(poceoUmirati<0){
				paintZaHelthBar=new Paint();
				poceoUmirati=System.currentTimeMillis();
				if(this.spriteZaZajednickeEfekte!=null){
                    if(this.indikator>0){
                    	if(this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(3)){
                    		this.slikaLokve=3;
                    	}
                    }	
                    else if(this.indikator<0){
                        if(this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(4)){
                        	this.slikaLokve=4;
                    	}
                    }
				}
			}
			poceoUmirati+=this.vrijemePauze;
			double razlika =poceoUmirati+this.vrijemeUmiranja-System.currentTimeMillis();
			double prag=3000;
			if(razlika<prag){
				 if(!this.gotovoNestajanje) {
					 gotovoNestajanje =spriteHend.animacijaAlfa(2,0, 0, 255, 0,paintZaHelthBar, 3, vrijemePauze);
				 }
				/*int alfa=(int)((255/100)*(razlika/(prag/100)));
				if(alfa<0){
					alfa=0;}
				paintZaHelthBar.setAlpha(alfa);*/
				
			}
			if(razlika>0){
				
				this.kvadratCrtanja.set(kvadratCrtanja.left, kvadratCrtanja.top+kvadratCrtanja.height()/2, kvadratCrtanja.right, kvadratCrtanja.bottom);
			/*	this.spriteZaZajednickeEfekte.animacijaAlfaCijelogRedaKaskadno(3,0,10,kvadratCrtanja, can, vrijemePauze);*/
				if(this.slikaLokve>0){
					this.spriteZaZajednickeEfekte.nacrtajSprite(can, slikaLokve, 0, 0, this.mojaSjena.getGlavniRectPrikaza());
				}
				spriteHend.nacrtajSprite(can, 2,0, 0, kvadratCrtanja,paintZaHelthBar);
			}
			else{
				if(this.indikator>0){
					if(this.slikaLokve>0){
						this.spriteZaZajednickeEfekte.nacrtajSprite(can, slikaLokve, 0, 0, this.mojaSjena.getGlavniRectPrikaza());
					}
					this.geSjena.pomNaX=20;// oadređuje trajanje lokve na 10 sek
					this.geSjena.indikator=100;// za branitelja
					this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
				}
				
				else if(this.indikator<0){
					if(this.slikaLokve>0){
						this.spriteZaZajednickeEfekte.nacrtajSprite(can, slikaLokve, 0, 0, this.mojaSjena.getGlavniRectPrikaza());
					}
					this.geSjena.pomNaX=20;// oadređuje trajanje lokve na 10 sek
					this.geSjena.indikator=101;// za protivnika
					this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
				}
				uiMan.makniObjekt(this);
				
			
			}
		}
		else{
			// prije nego umre salje svojoj sjeni ako ima lokvu za prikazati da prikaze ako ne da se i ona uništi
	    if(ge.indikator!=-100){// zna�?i da nije izbrisan ako je onda se direktno namješta da se ubbija
	    	  if(this.spriteZaZajednickeEfekte!=null){
                if(this.indikator>0){
                	if(this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(3)){
                		this.slikaLokve=3;
                	}
                }	
                else if(this.indikator<0){
                    if(this.spriteZaZajednickeEfekte.postojiLiSlikNaMjestu(4)){
                    	this.slikaLokve=4;
                	}
                }
			}
			if(this.indikator>0){
				
				this.geSjena.pomNaX=20;// oadređuje trajanje lokve na 10 sek
				this.geSjena.indikator=100;// za branitelja
				this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
			}
			
			else if(this.indikator<0){
				this.geSjena.pomNaX=20;// oadređuje trajanje lokve na 10 sek
				this.geSjena.indikator=101;// za protivnika
				this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
			  }
	       }
	     else{
	    	 this.geSjena.jesamLiZiv=false;
	    			 this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
	     }
			// umire ovaj objekt
			if(this.dovrsioAnimacijuUmiranjaProtivnik)uiMan.makniObjekt(this);
		}
	  }
	}
	private  void dodajPomakZaSkokice(){
		this.kvadratCrtanjaSkokici.set(this.kvadratCrtanjaSkokici.left+x-xPros, this.kvadratCrtanjaSkokici.top+y-yPros, kvadratCrtanjaSkokici.right+x-xPros,kvadratCrtanjaSkokici.bottom+y-yPros);

		
	
	}
	/*private void dodajPomakZaSkokiceNaPromjeniSlicice(){
		float dyPomak=0;
		if(picUSl1>=this.zadnjaSlicicaHodanja){//prva polovica
			float postotak=zadnjaSlicicaHodanja/((float)spriteHend.brojStupaca(0)/100);
			dyPomak=(this.pixVisSkoka1/100)*postotak;
		}
    	else{//druga polovica
    		float postotak=(zadnjaSlicicaHodanja-this.picUSl1)/((float)spriteHend.brojStupaca(0)/100);
			dyPomak=pixVisSkoka1-(this.pixVisSkoka1/100)*postotak;
	       }
		prosloVrijemeSkokica=System.currentTimeMillis();
		this.kvadratCrtanjaSkokici.set(this.kvadratCrtanja.left, this.kvadratCrtanja.top+dyPomak, this.kvadratCrtanja.right, this.kvadratCrtanja.bottom+dyPomak);
	}*/
	private void dodajPomakZaSkokiceNaPromjeniSlicice(){
		this.kvadratCrtanjaSkokici.set(this.kvadratCrtanja.left, this.kvadratCrtanja.top-ukupniPomakSkokic, this.kvadratCrtanja.right, this.kvadratCrtanja.bottom-ukupniPomakSkokic);
		float temp=(1000/spriteHend.brojPrikazaPoSekundi(brSlikeZaHodanjeDesno))*spriteHend.brojStupaca(brSlikeZaHodanjeDesno);
		float postotak=(float) (System.currentTimeMillis()-vrijemePocetkaCiklusHodaZaSkokic)/(temp/100);
		if(postotak>100){
			postotak=100;
		}
		if(  picPosto>=postotak){//prva polovica
		//this.geSjena.x3=postoSmanjenjaSjeneKodPika*(postotak/this.picPosto/100)/100;
			this.geSjena.x3=-postoSmanjenjaSjeneKodPika*(postotak)/100;///this.picPosto/100);
			ukupniPomakSkokic=-(this.pixVisSkoka1/100)*postotak;
		}
    	else{//druga polovica
    		//this.geSjena.x3=postoSmanjenjaSjeneKodPika*(((100-postotak)/this.picPosto/100))/100;
    		this.geSjena.x3=-postoSmanjenjaSjeneKodPika*(100-postotak)/100;///(this.picPosto/100));
    		ukupniPomakSkokic=-pixVisSkoka1+(this.pixVisSkoka1/100)*postotak;
	       }
		prosloVrijemeSkokica=System.currentTimeMillis();
		this.kvadratCrtanjaSkokici.set(this.kvadratCrtanja.left, this.kvadratCrtanja.top+ukupniPomakSkokic, this.kvadratCrtanja.right, this.kvadratCrtanja.bottom+ukupniPomakSkokic);
	}
    private void stvariKojeSeIzvrsavajuSamoJedanput(){
    	//provjera postojanja slike borbe
    	paintZaHelthBar.setARGB(200,50,250,50);
		paintZaHelthBar.setStyle(Paint.Style.FILL);
		paintZaHelthBarRazlika.setStyle(Paint.Style.FILL);
    	this.geLijecenje.jesamLiZiv=true;
    	if(this.spriteHend.postojiLiSlikNaMjestu(1)){
    		daliPostojiSlikaBorbe=true;  		
    		brStuBorbe=spriteHend.brojStupaca(brSlikeZaBorbuLijevo);// koliko imaju stupaca ti dodani redovi
    	}
    	else daliPostojiSlikaBorbe=false;
		///slika 1
    	xPolToucha=x;
    	yPolToucha=y;
    	vriStojOnomatopeje=1000*uiMan.GL.faIgr.secStojanjaOnomatopeje;
    	recOnomatopeje=new RectF();
		recOnomatopeje.set(0, 0, this.uiMan.GL.faIgr.velXOnomatopejeCm*this.PpCmX,this.uiMan.GL.faIgr.velYOnomatopejeCm*this.PpCmY);
		
    	visSjene=3*this.kvadratCrtanja.width()/10;
    	sirSjene=6*this.kvadratCrtanja.width()/8;
    	this.mojaSjena=new Sjena(this.sirSjene,this.visSjene,this.uiMan.GL.faIgr.sjenaAlpha,this.uiMan.GL.faIgr.sjenaRead,this.uiMan.GL.faIgr.sjenaGreen,this.uiMan.GL.faIgr.sjenaBlue);
		this.mojaSjena.bilderDoddajDodatniSpriteZaZajednickeEfekte(spriteZaZajednickeEfekte);
    	uiMan.dodajElementUListu(mojaSjena, 1);
        if(!posebnoNamjestenBorbaDx){
		          this.borbaLeftDx=this.sirIscr/2;
		          this.borbaRightDx=sirIscr/2;
         }

		maxIndxSl1=spriteHend.brojStupaca(brSlikeZaHodanjeLijevo)-1;
		miliSecSl1=1000/spriteHend.brojPrikazaPoSekundi(brSlikeZaHodanjeGore);
		miliSecBorbaDesno=1000/spriteHend.brojPrikazaPoSekundi(this.brSlikeZaBrbuDesno);
		miliSecBorbaLijevo=1000/spriteHend.brojPrikazaPoSekundi(this.brSlikeZaBorbuLijevo);
		pocSlic=randIzmeduAiB(iHodanjaPocinjeOd,spriteHend.brojStupaca(brSlikeZaHodanjeLijevo)-1);// predpostavio sam da æe biti nulta slièica uvijek. izralunava poèetnu slièicu tako da svaki objek iste vrste ima svoj tempo animacije
		iHodanjaLijevo=pocSlic;
		iHodanjaDesno=pocSlic;
		iHodanjaGore=pocSlic;
		iHodanjaDolje=pocSlic;
    }
	private void nacrtajHelthBar(){
		
		if(tekPoceoRazHeltha){
			this.stariHelth=this.helth;
		if(this.helth>0)	tekPoceoRazHeltha=false;
			razlikHeltha=0;
		}
		
		RectF tempRec=new RectF();
		tempRec.set(x,y-0.1f*PpCmY+ukupniPomakSkokic, x+helth*this.kvadratCrtanja.width()/100 , y-0.03f*PpCmY+ukupniPomakSkokic);
		can.drawRect(tempRec, paintZaHelthBar);
		if(stariHelth<helth){// ako se lijeci
			vrijemeRazlikeHeltha=System.currentTimeMillis();
			this.paintZaHelthBarRazlika.setARGB(200,0, 30, 255);
			if(vrijemeMiliStojanjaRazlikeHeltha+vrijemeRazlikeHeltha>System.currentTimeMillis()){
				razlikHeltha=	razlikHeltha+stariHelth-helth;
			}
			else razlikHeltha=stariHelth-helth;
			recHelthRazlike.set(tempRec.right-(helth-stariHelth)*this.kvadratCrtanja.width()/100, tempRec.top,tempRec. right, tempRec.bottom);
		}
		else if(stariHelth>helth){// ako ga napadaju
			vrijemeRazlikeHeltha=System.currentTimeMillis();
			if(vrijemeMiliStojanjaRazlikeHeltha+vrijemeRazlikeHeltha>System.currentTimeMillis()){
				razlikHeltha=	razlikHeltha+stariHelth-helth;
			}
			else razlikHeltha=stariHelth-helth;
			this.paintZaHelthBarRazlika.setARGB(200,255, 60, 0);
			
			
		}
		if(vrijemeMiliStojanjaRazlikeHeltha+vrijemeRazlikeHeltha>System.currentTimeMillis()){
			
	
		    if(razlikHeltha<0){
			recHelthRazlike.set(tempRec.right, tempRec.top,tempRec.right+razlikHeltha*this.kvadratCrtanja.width()/100, tempRec.bottom);
		     }
	    	else if(razlikHeltha>0) {
			recHelthRazlike.set(tempRec.right, tempRec.top,tempRec.right+razlikHeltha*this.kvadratCrtanja.width()/100, tempRec.bottom);
		    }
		can.drawRect(recHelthRazlike, paintZaHelthBarRazlika);
		}
		else {
		this.razlikHeltha=0;
		}
		stariHelth=helth;
		/*tempRec.set(x,y-0.1f*PpCmX+ukupniPomakSkokic, x+this.kvadratCrtanja.width() , y-0.03f*PpCmX+ukupniPomakSkokic);
		tempRec.set(tempRec.left-5*tempRec.width()/100, tempRec.top-5*tempRec.height()/100, tempRec.right+5*tempRec.width()/100, tempRec.bottom+5*tempRec.height()/100);
		this.spriteZaZajednickeEfekte.nacrtajSprite(can, 8, 0, 0,tempRec);*/
	}
    private void nacrtajArmorBar(){
		
		if(tekPoceoRazArmor){
			paintZaArmorBar.setARGB(200, 67,66, 38);
			paintZaArmorBarRazlika.setARGB(200,89, 88, 57);
			paintZaArmorBarRazlika.setStyle(Paint.Style.FILL);
			paintZaArmorBar.setStyle(Paint.Style.FILL);
			this.stariArmor=this.armor;
		if(this.armor>0)	tekPoceoRazArmor=false;
			razlikArmor=0;
		}
		
		RectF tempRec=new RectF();
		tempRec.set(x,y-(0.1f+0.03f)*PpCmY+ukupniPomakSkokic, x+armor*this.kvadratCrtanja.width()/100 , y-0.1f*PpCmY+ukupniPomakSkokic);
		can.drawRect(tempRec, paintZaArmorBar);
		if(stariArmor<armor){// ako se lijeci
			vrijemeRazlikeArmor=System.currentTimeMillis();
			
			if(vrijemeMiliStojanjaRazlikeArmor+vrijemeRazlikeArmor>System.currentTimeMillis()){
				razlikArmor=	razlikArmor+stariArmor-armor;
			}
			else razlikArmor=stariArmor-armor;
			recArmorRazlike.set(tempRec.right-(armor-stariHelth)*this.kvadratCrtanja.width()/100, tempRec.top,tempRec. right, tempRec.bottom);
		}
		else if(stariArmor>armor){// ako ga napadaju
			vrijemeRazlikeArmor=System.currentTimeMillis();
			if(vrijemeMiliStojanjaRazlikeArmor+vrijemeRazlikeArmor>System.currentTimeMillis()){
				razlikArmor=	razlikArmor+stariArmor-armor;
			}
			else razlikArmor=stariArmor-armor;
			this.paintZaArmorBarRazlika.setARGB(200,255, 60, 0);
			
			
		}
		if(vrijemeMiliStojanjaRazlikeArmor+vrijemeRazlikeArmor>System.currentTimeMillis()){
			
	
		    if(razlikArmor<0){
			recArmorRazlike.set(tempRec.right, tempRec.top,tempRec.right+razlikArmor*this.kvadratCrtanja.width()/100, tempRec.bottom);
		     }
	    	else if(razlikHeltha>0) {
			recArmorRazlike.set(tempRec.right, tempRec.top,tempRec.right+razlikArmor*this.kvadratCrtanja.width()/100, tempRec.bottom);
		    }
		can.drawRect(recArmorRazlike, paintZaArmorBarRazlika);
		}
		else {
		this.razlikArmor=0;
		}
		stariArmor=armor;
		/*tempRec.set(x,y-0.1f*PpCmX+ukupniPomakSkokic, x+this.kvadratCrtanja.width() , y-0.03f*PpCmX+ukupniPomakSkokic);
		tempRec.set(tempRec.left-5*tempRec.width()/100, tempRec.top-5*tempRec.height()/100, tempRec.right+5*tempRec.width()/100, tempRec.bottom+5*tempRec.height()/100);
		this.spriteZaZajednickeEfekte.nacrtajSprite(can, 8, 0, 0,tempRec);*/
	}
	private void hodanjeIliBorba(){
		 dxHod=tempX-xPros;
		 dyHod=tempY-yPros;
		 
		 float dLog=(dxLog+dyLog)/2;
		 float korektor= 1.15f;
		 boolean b=true;
		 omjHod=Math.abs(korektor*(dxHod)/(dyHod));
		  if(ge!=null) if(ge.indikatorBorbe>0||udaranjeDesno||udaranjeLijevo){
			   if(ge.indikatorBorbe==3){// ovi se pamte pomoæu  
                   this.udaranjeDesno=true;
                  
                   b=false;
                     }
               else if(ge.indikatorBorbe==4){
                   this.udaranjeLijevo=true;
                 
                   b=false;
                    }  
                if(udaranjeDesno)  {
				   if(this.daliPostojiSlikaBorbe)  udaracNaDesno();
				   else  udaranjeDesno=false;
				   b=false;
			   }
			   else if(udaranjeLijevo){
			    	if(this.daliPostojiSlikaBorbe)	udaracNaLijevo();
			    	 else  udaranjeLijevo=false;
			    	b=false;
			    }
			
		       else if(ge.indikatorBorbe==1)  {//ovi  reagiraju direktno jer se u fizici stanjje ostaje zapamæeno
				                                                          borbaNaDesno();
				                                                          b=false;
			                                                              }
			   else if(ge.indikatorBorbe==2){
				                                                          borbaNaLijevo();
				                                                          b=false;
			                                                               }
			   
			}
		  if(b){ 
			     borbaNaLijevo=false;
				 this.borbaNaDesno=false;
		       if(Math.abs(dxLog*korektor)<Math.abs(dyLog)){
		    	 if(dyLog>0){
			    	 hodanjeDolje();
			     }
			     else if(dyLog<0) hodanjeGore();
			     else  stojanjeNaMjestu();
		        }
		       else if(Math.abs(dxLog*korektor)>Math.abs(dyLog)){
		    	 if(dxLog>0) hodanjeDesno();
			     else  if(dxLog<0)hodanjeLijevo();
			     else  stojanjeNaMjestu();
		        }
		       else stojanjeNaMjestu();
		       }
		/*if(b){ 
		       if(Math.abs(dxHod*korektor)<Math.abs(dyHod)){
		    	 if(Math.abs(dyHod)<1)  stojanjeNaMjestu();
		    	 else if(dyHod>0+dyLog){
			    	 hodanjeDolje();
			     }
			     else if(dyHod<0-dyLog) hodanjeGore();
			     else  stojanjeNaMjestu();
		        }
		       else if(Math.abs(dxHod*korektor)>Math.abs(dyHod)){
		    	 if(Math.abs(dxHod)<1)  stojanjeNaMjestu();
		         else if(dxHod>0+dxLog) hodanjeDesno();
			     else  if(dxHod<0-dxLog)hodanjeLijevo();
			     else  stojanjeNaMjestu();
		        }
		       else stojanjeNaMjestu();
		       }*/
	}
	private void borbaNaDesno(){
		this.borbaNaDesno=true;
		this.borbaNaLijevo=false;
		iHodanjaGore=iHodanjaPocinjeOd;
		iHodanjaDolje=iHodanjaPocinjeOd;
		iHodanjaLijevo=iHodanjaPocinjeOd;
		iHodanjaDesno=iHodanjaPocinjeOd;
		poceoStojati=-1;// oznaèava da je varijabla prazna 
		if(daliPostojiSlikaBorbe){
			         this.kvadratCrtanja.set(kvadratCrtanja.left-borbaRightDx/2, kvadratCrtanja.top, kvadratCrtanja.right+borbaRightDx/2, kvadratCrtanja.bottom);
		             spriteHend.nacrtajSprite(can,this.brSlikeZaBrbuDesno,0,brRedakaZaBrbuDesno, kvadratCrtanja);
		             this.kvadratCrtanja.set(kvadratCrtanja.left+borbaRightDx/2, kvadratCrtanja.top, kvadratCrtanja.right-borbaRightDx/2, kvadratCrtanja.bottom);
		             }
		else {// ako ne ostoji slika borbe cta se okreenut na lijevo
			 spriteHend.nacrtajSprite(can, brSlikeZaBrbuDesno,0, brRedakaZaBrbuDesno, kvadratCrtanja);
		}
	}
	private void borbaNaLijevo(){
		this.borbaNaDesno=false;
		this.borbaNaLijevo=true;
		iHodanjaGore=iHodanjaPocinjeOd;
		iHodanjaDolje=iHodanjaPocinjeOd;
		iHodanjaLijevo=iHodanjaPocinjeOd;
		iHodanjaDesno=iHodanjaPocinjeOd;
		poceoStojati=-1;// oznaèava da je varijabla prazna 
		if(daliPostojiSlikaBorbe){
			this.kvadratCrtanja.set(kvadratCrtanja.left-this.borbaLeftDx/2, kvadratCrtanja.top, kvadratCrtanja.right+borbaLeftDx/2, kvadratCrtanja.bottom);
			spriteHend.nacrtajSprite(can, brSlikeZaBorbuLijevo,    0, brRedakaZaBorbuLijevo, kvadratCrtanja);
			this.kvadratCrtanja.set(kvadratCrtanja.left+this.borbaLeftDx/2, kvadratCrtanja.top, kvadratCrtanja.right-borbaLeftDx/2, kvadratCrtanja.bottom);
            }
        else {// ako ne ostoji slika borbe cta se okreenut na lijevo
	        spriteHend.nacrtajSprite(can,brSlikeZaBorbuLijevo,0,brRedakaZaBorbuLijevo, kvadratCrtanja);
          }
		
	}
	private void udaracNaDesno(){
		this.borbaNaDesno=true;
		this.borbaNaLijevo=false;
		poceoStojati=-1;// oznaèava da je varijabla prazna 
		this.kvadratCrtanja.set(kvadratCrtanja.left-borbaRightDx/2, kvadratCrtanja.top, kvadratCrtanja.right+borbaRightDx/2, kvadratCrtanja.bottom);
		prosVrijeme+=this.vrijemePauze;
		if(prosVrijeme+miliSecBorbaDesno<=System.currentTimeMillis()){	
		    if(iBorbeD<=brStuBorbe-1){
			   spriteHend.nacrtajSprite(can, brSlikeZaBrbuDesno,iBorbeD,brRedakaZaBrbuDesno, kvadratCrtanja);
			   iBorbeD++;
		    }
		    else {spriteHend.nacrtajSprite(can,brSlikeZaBrbuDesno,0,brRedakaZaBrbuDesno, kvadratCrtanja);//iscrtava stojanje prije da se izbjegne nestanak lka sa ekrana
		    	   iBorbeD=1;
		    	   onomatopejaDesnoIliLijevo=true;
                   ispaliOnomatopeju=true;// brisat ce zastavicu tekpoceo dio u crtanju onomatopeje metodi
		           udaranjeDesno=false;
		           ge.indikatorBorbe=0;
		        
		         
		    }
		    iBorbeL=1;
		   iHodanjaDesno=pocSlic;
		   iHodanjaDolje=pocSlic;
		   iHodanjaLijevo=pocSlic;// tako kad se poðe kretat ulijevo da krene od prve slièice
		   prosVrijeme=System.currentTimeMillis();
		 }
		else {
			if(iBorbeD>brStuBorbe-1){  
				               spriteHend.nacrtajSprite(can, brSlikeZaBrbuDesno,0,brRedakaZaBrbuDesno, kvadratCrtanja);
                                                             iBorbeD=1;
                                                             onomatopejaDesnoIliLijevo=true;
                                                             ispaliOnomatopeju=true;// brisat ce zastavicu tekpoceo dio u crtanju onomatopeje metodi
			                                                 udaranjeDesno=false;
			                                              
			                                                }
			else spriteHend.nacrtajSprite(can,brSlikeZaBrbuDesno,iBorbeD,brRedakaZaBrbuDesno, kvadratCrtanja);
		}	
		this.kvadratCrtanja.set(kvadratCrtanja.left+borbaRightDx/2, kvadratCrtanja.top, kvadratCrtanja.right-borbaRightDx/2, kvadratCrtanja.bottom);
	}
	private void udaracNaLijevo(){
		this.borbaNaDesno=false;
		this.borbaNaLijevo=true;
		poceoStojati=-1;// oznaèava da je varijabla prazna 
		this.kvadratCrtanja.set(kvadratCrtanja.left-this.borbaLeftDx/2, kvadratCrtanja.top, kvadratCrtanja.right+borbaLeftDx/2, kvadratCrtanja.bottom);
		prosVrijeme+=this.vrijemePauze;
		if(prosVrijeme+miliSecBorbaLijevo<=System.currentTimeMillis()){	
		    if(iBorbeL<=brStuBorbe-1){
			   spriteHend.nacrtajSprite(can,brSlikeZaBorbuLijevo,iBorbeL,brRedakaZaBorbuLijevo, kvadratCrtanja);
			   iBorbeL++;
		    }
		    else {
		    	spriteHend.nacrtajSprite(can,brSlikeZaBorbuLijevo,    0,brRedakaZaBorbuLijevo, kvadratCrtanja);
		    iBorbeL=1;
		    onomatopejaDesnoIliLijevo=false;
            ispaliOnomatopeju=true;// brisat ce zastavicu tekpoceo dio u crtanju onomatopeje metodi
		    udaranjeLijevo=false;
		    ge.indikatorBorbe=0;
		    
		    }
		    iBorbeD=1;
		   iHodanjaDesno=pocSlic;
		   iHodanjaDolje=pocSlic;
		   iHodanjaLijevo=pocSlic;// tako kad se poðe kretat ulijevo da krene od prve slièice
		   prosVrijeme=System.currentTimeMillis();
		 }
		else{
			if(iBorbeL>brStuBorbe-1){   
				           spriteHend.nacrtajSprite(can, brSlikeZaBorbuLijevo,iBorbeL-1,brRedakaZaBorbuLijevo, kvadratCrtanja);
				                                              iBorbeL=1;
			                                                  udaranjeLijevo=false;
			                                                  onomatopejaDesnoIliLijevo=false;
	                                                             ispaliOnomatopeju=true;// brisat ce zastavicu tekpoceo dio u crtanju onomatopeje metodi
			                                                }
			              
			else {
				spriteHend.nacrtajSprite(can,brSlikeZaBorbuLijevo,iBorbeL,brRedakaZaBorbuLijevo, kvadratCrtanja);
			}
		}	
		this.kvadratCrtanja.set(kvadratCrtanja.left+this.borbaLeftDx/2, kvadratCrtanja.top, kvadratCrtanja.right-borbaLeftDx/2, kvadratCrtanja.bottom);
	}

	private void hodanjeGore(){
	     poceoStojati=-1;// oznaèava da je varijabla prazna 
	     zadnjaSlicicaHodanja=iHodanjaGore;
		 zadnjiRedakHodanja=brRedakaZaHodanjeGore;
		 zadnjaSlikaHodanja=brSlikeZaHodanjeGore;
		 if(iHodanjaGore==iHodanjaPocinjeOd){//spremam novo vrijeme za ra�?unanje skoka
			 prosloVrijemeSkokica=System.currentTimeMillis();
				this.vrijemePocetkaCiklusHodaZaSkokic=System.currentTimeMillis();
			}
		 prosVrijeme+=this.vrijemePauze;
			if(prosVrijeme+miliSecSl1<=System.currentTimeMillis()){	
			    if(iHodanjaGore<=maxIndxSl1){
			    	
				   spriteHend.nacrtajSprite(can, brSlikeZaHodanjeGore,iHodanjaGore, brRedakaZaHodanjeGore, kvadratCrtanjaSkokici);
				 
				   iHodanjaGore++;
				   if(iHodanjaGore>maxIndxSl1){  
					   iHodanjaGore=iHodanjaPocinjeOd;
					   }
			    }
			    else {
			    	iHodanjaGore=iHodanjaPocinjeOd;
			   
			    spriteHend.nacrtajSprite(can, brSlikeZaHodanjeGore,iHodanjaGore, brRedakaZaHodanjeGore, kvadratCrtanjaSkokici);
			    }
			    iBorbeL=1;
			    iBorbeD=1;
			   iHodanjaDesno=iHodanjaGore;
			   iHodanjaDolje=iHodanjaGore;
			   iHodanjaLijevo=iHodanjaGore;// tako kad se poðe kretat ulijevo da krene od prve slièice
			   prosVrijeme=System.currentTimeMillis();
			 }
			else{
				if(iHodanjaGore>maxIndxSl1) iHodanjaGore=iHodanjaPocinjeOd;
				 iHodanjaDesno=iHodanjaGore;
				   iHodanjaDolje=iHodanjaGore;
				   iHodanjaLijevo=iHodanjaGore;
				zadnjaSlicicaHodanja=iHodanjaGore;
				zadnjiRedakHodanja=3;
				spriteHend.nacrtajSprite(can,brSlikeZaHodanjeGore,iHodanjaGore,brRedakaZaHodanjeGore, kvadratCrtanjaSkokici);
			}	
	}
	private void hodanjeDolje(){
		 poceoStojati=-1;// oznaèava da je varijabla prazna 
		  zadnjaSlicicaHodanja=iHodanjaDolje;
		  zadnjiRedakHodanja=brRedakaZaHodanjeDolje;
			 zadnjaSlikaHodanja=brSlikeZaHodanjeDolje;
			if(iHodanjaDolje==iHodanjaPocinjeOd){//spremam novo vrijeme za ra�?unanje skoka
				
				prosloVrijemeSkokica=System.currentTimeMillis();
				this.vrijemePocetkaCiklusHodaZaSkokic=System.currentTimeMillis();
			}
			 prosVrijeme+=this.vrijemePauze;
			if(prosVrijeme+miliSecSl1<=System.currentTimeMillis()){	
			    if(iHodanjaDolje<=maxIndxSl1){
			    	
				   spriteHend.nacrtajSprite(can, brSlikeZaHodanjeDolje,iHodanjaDolje,brRedakaZaHodanjeDolje, kvadratCrtanjaSkokici);
				   iHodanjaDolje++;
				   if(iHodanjaDolje>maxIndxSl1){  
					   iHodanjaDolje=iHodanjaPocinjeOd;
					   }
			    }
			    else {iHodanjaDolje=iHodanjaPocinjeOd;
			  
			    spriteHend.nacrtajSprite(can, brSlikeZaHodanjeDolje,iHodanjaDolje, brRedakaZaHodanjeDolje,kvadratCrtanjaSkokici);
			    }
			    iBorbeL=1;
			    iBorbeD=1;
			   iHodanjaDesno=iHodanjaDolje;
			   iHodanjaGore=iHodanjaDolje;
			   iHodanjaLijevo=iHodanjaDolje;// tako kad se poðe kretat ulijevo da krene od prve slièice
			   prosVrijeme=System.currentTimeMillis();
			 }
			else{
				if(iHodanjaDolje>maxIndxSl1) iHodanjaDolje=iHodanjaPocinjeOd;
				  iHodanjaDesno=iHodanjaDolje;
				   iHodanjaGore=iHodanjaDolje;
				   iHodanjaLijevo=iHodanjaDolje;
				zadnjaSlicicaHodanja=iHodanjaDolje;
				zadnjiRedakHodanja=2;
				spriteHend.nacrtajSprite(can,brSlikeZaHodanjeDolje,iHodanjaDolje,brRedakaZaHodanjeDolje, kvadratCrtanjaSkokici);
			}
	}
	private void hodanjeDesno(){
		poceoStojati=-1;// oznaèava da je varijabla prazna 
		  zadnjaSlicicaHodanja=iHodanjaDesno;
		  zadnjiRedakHodanja=brRedakaZaHodanjeDesno;
		  zadnjaSlikaHodanja=brSlikeZaHodanjeDesno;
			if(iHodanjaDesno==iHodanjaPocinjeOd){//spremam novo vrijeme za ra�?unanje skoka
				prosloVrijemeSkokica=System.currentTimeMillis();
				this.vrijemePocetkaCiklusHodaZaSkokic=System.currentTimeMillis();
			}
			 prosVrijeme+=this.vrijemePauze;
		if(prosVrijeme+miliSecSl1<=System.currentTimeMillis()){	
		    if(iHodanjaDesno<=maxIndxSl1){
		    	
			   spriteHend.nacrtajSprite(can, brSlikeZaHodanjeDesno,iHodanjaDesno, brRedakaZaHodanjeDesno,kvadratCrtanjaSkokici);
			   iHodanjaDesno++;
			   if(iHodanjaDesno>maxIndxSl1){  
				   iHodanjaDesno=iHodanjaPocinjeOd;
				   }
		    }
		    else {iHodanjaDesno=iHodanjaPocinjeOd;
		   
		    spriteHend.nacrtajSprite(can, 0,iHodanjaDesno, 0,kvadratCrtanjaSkokici);
		    }
		    iBorbeL=1;
		    iBorbeD=1;
		   iHodanjaGore=iHodanjaDesno;
		   iHodanjaDolje=iHodanjaDesno;
		   iHodanjaLijevo=iHodanjaDesno;// tako kad se poðe kretat ulijevo da krene od prve slièice
		   prosVrijeme=System.currentTimeMillis();
		 }
		else{
			if(iHodanjaDesno>maxIndxSl1) iHodanjaDesno=iHodanjaPocinjeOd;
			 iHodanjaGore=iHodanjaDesno;
			   iHodanjaDolje=iHodanjaDesno;
			   iHodanjaLijevo=iHodanjaDesno;
			   zadnjaSlicicaHodanja=iHodanjaDesno;
				zadnjiRedakHodanja=0;
			spriteHend.nacrtajSprite(can,brSlikeZaHodanjeDesno,iHodanjaDesno,brRedakaZaHodanjeDesno,kvadratCrtanjaSkokici);
		}
		
	}
	
  
	private void hodanjeLijevo(){
		poceoStojati=-1;// oznaèava da je varijabla prazna 
		  zadnjaSlicicaHodanja=iHodanjaLijevo;
		  zadnjiRedakHodanja=brRedakaZaHodanjeLijevo;
		  zadnjaSlikaHodanja=brSlikeZaHodanjeLijevo;
			if(iHodanjaLijevo==iHodanjaPocinjeOd){//spremam novo vrijeme za ra�?unanje skoka
				prosloVrijemeSkokica=System.currentTimeMillis();
				this.vrijemePocetkaCiklusHodaZaSkokic=System.currentTimeMillis();
			}
			 prosVrijeme+=this.vrijemePauze;
		if(prosVrijeme+miliSecSl1<=System.currentTimeMillis()){
		    
		    if(iHodanjaLijevo<=maxIndxSl1){
		    	
			   spriteHend.nacrtajSprite(can,brSlikeZaHodanjeLijevo,iHodanjaLijevo, brRedakaZaHodanjeLijevo,kvadratCrtanjaSkokici);
			   iHodanjaLijevo++;
			   if(iHodanjaLijevo>maxIndxSl1){  
				   iHodanjaLijevo=iHodanjaPocinjeOd;
				   }
		    }
		   else {iHodanjaLijevo=iHodanjaPocinjeOd;
		    
		         spriteHend.nacrtajSprite(can,brSlikeZaHodanjeLijevo,iHodanjaLijevo,brRedakaZaHodanjeLijevo, kvadratCrtanjaSkokici);
		        }
		    iBorbeL=1;
		    iBorbeD=1;
		   iHodanjaGore=iHodanjaLijevo;
		   iHodanjaDolje=iHodanjaLijevo;
		   iHodanjaDesno=iHodanjaLijevo;// tako kad se poðe kretat ulijevo da krene od prve slièice
		   prosVrijeme=System.currentTimeMillis();
		 }
		else {
			 if(iHodanjaLijevo>maxIndxSl1) iHodanjaLijevo=iHodanjaPocinjeOd;
			 iHodanjaGore=iHodanjaLijevo;
			   iHodanjaDolje=iHodanjaLijevo;
			   iHodanjaDesno=iHodanjaLijevo;
			   zadnjaSlicicaHodanja=iHodanjaLijevo;
				zadnjiRedakHodanja=1;
			 spriteHend.nacrtajSprite(can, brSlikeZaHodanjeLijevo,iHodanjaLijevo,brRedakaZaHodanjeLijevo,kvadratCrtanjaSkokici);
		     }
	
	    
	}
    private void crtajIzlazakIzZemlje(){
   
    	if(!nestanakProtivnika){
    		geIzlIzZemlje.jesamLiZiv=true;
    		recIzlIzZemljeRez.set(0,0, 100,0); 
    		recIzlIzZemlje.set(this.kvadratCrtanja.left, this.kvadratCrtanja.bottom, this.kvadratCrtanja.right, this.kvadratCrtanja.bottom);
    	   if(this.generator.nextBoolean()){
    		     if(this.generator.nextBoolean()){//lijevo
    			
    		    	    tempBrRedIzlIzZemlje=this.brRedakaZaHodanjeLijevo;
    		    		tempBrSlIzIzZemlje=this.brSlikeZaHodanjeLijevo;
    		     }
    		    else{//desno
    		    	  tempBrRedIzlIzZemlje=this.brRedakaZaHodanjeDesno;
  		    		tempBrSlIzIzZemlje=this.brSlikeZaHodanjeDesno;
    	     	}
    	      }
    	   else{
                  if(this.generator.nextBoolean()){//gore
                	  tempBrRedIzlIzZemlje=this.brRedakaZaHodanjeGore;
    		    		tempBrSlIzIzZemlje=this.brSlikeZaHodanjeGore;
    			
    	     	}
    		     else{//dolje
    		    	 tempBrRedIzlIzZemlje=this.brRedakaZaHodanjeDolje;
   		    		tempBrSlIzIzZemlje=this.brSlikeZaHodanjeDolje;
    	    	}
    	   }
    	   nestanakProtivnika=true;
    	}
    		
      	 nestanakProtivnika=ge.izlazakIzZemlje=!SpriteHendler.animacijaSlaganjeUvecanjeVremenska(geIzlIzZemlje, 0,100,0,3, recIzlIzZemljeRez);	
    	 recIzlIzZemlje.set(this.kvadratCrtanja.left, this.kvadratCrtanja.bottom-recIzlIzZemljeRez.height()*kvadratCrtanja.height()/100, this.kvadratCrtanja.right, this.kvadratCrtanja.bottom);
    	 recIzlIzZemljeRez.set(0, 0, 100, recIzlIzZemljeRez.height());
    	 spriteHend.nacrtjDioBitmapa(can, tempBrSlIzIzZemlje,tempBrRedIzlIzZemlje,0,this.recIzlIzZemljeRez, recIzlIzZemlje, 0, 0,0, null);
    	
    }
    private void crtajTeleportaciju(){
    	
    	if(!zavrsioCrtanjeTelPrviDio){
    		zavrsioCrtanjeTelPrviDio=spriteZaZajednickeEfekte.vrtiAnimacijuReda(can,this.geTelep, 5, 0,  recTelep, null);
    		if(geTelep.indikator>(int)spriteZaZajednickeEfekte.brojStupaca(5)/2){
    			this.nestanakProtivnika=true;
    		}
    	}
    	else {
    		
    		zavrsioCrtanjeTelPrviDio=!spriteZaZajednickeEfekte.vrtiAnimacijuReda(can,this.geTelep, 5, 0,recTelep, null);
    		if(geTelep.indikator>(int)spriteZaZajednickeEfekte.brojStupaca(5)/2) {
    			this.nestanakProtivnika=false;
    		}
    		if(!zavrsioCrtanjeTelPrviDio)this.ge.teleportacija=false;
    	     }
	}
    private void crtajLijecenje(){
    	
    	
    	spriteZaZajednickeEfekte.vrtiAnimacijuReda(can,this.geLijecenje, 6, 0,  recLijecenje, null);
    		
	}
    private void crtajGorenje(){
		prosVrijemeGorenja+=this.vrijemePauze;
		if(prosVrijemeGorenja+1000/this.spriteZaZajednickeEfekte.brojPrikazaPoSekundi(0)<=System.currentTimeMillis()){
		    
		    if(iCrtanjaGorenja<=spriteZaZajednickeEfekte.brojStupaca(0)-1){
		    	spriteZaZajednickeEfekte.nacrtajSprite(can, 0,iCrtanjaGorenja, 0,kvadratCrtanja);
		    	
		    }
		   else {iCrtanjaGorenja=0;
		        spriteZaZajednickeEfekte.nacrtajSprite(can, 0,iCrtanjaGorenja,0, kvadratCrtanja);
		        }
		    iCrtanjaGorenja++;
		    prosVrijemeGorenja=System.currentTimeMillis();
		 }
		else {
			 if(iCrtanjaGorenja>spriteZaZajednickeEfekte.brojStupaca(0)-1) iCrtanjaGorenja=0;
			 spriteZaZajednickeEfekte.nacrtajSprite(can, 0,iCrtanjaGorenja, 0,kvadratCrtanja);
		     }
	
	    
	}
	private void crtajTrovanja(){
		RectF temRect=new RectF(this.kvadratCrtanja.centerX()-this.kvadratCrtanja.width()/3,this.kvadratCrtanja.top-2*this.kvadratCrtanja.width()/3-this.kvadratCrtanja.width()/4,this.kvadratCrtanja.centerX()+this.kvadratCrtanja.width()/3,this.kvadratCrtanja.top-this.kvadratCrtanja.width()/4);
		prosVrijemeTrovanja+=this.vrijemePauze;
		if(prosVrijemeTrovanja+1000/this.spriteZaZajednickeEfekte.brojPrikazaPoSekundi(1)<=System.currentTimeMillis()){
		    
		    if(iCrtanjaTrovanja<=spriteZaZajednickeEfekte.brojStupaca(1)-1){
		    	spriteZaZajednickeEfekte.nacrtajSprite(can, 1,iCrtanjaTrovanja, 0,temRect);
		    	
		    }
		   else {iCrtanjaTrovanja=0;
		        spriteZaZajednickeEfekte.nacrtajSprite(can, 1,iCrtanjaTrovanja,0, temRect);
		        }
		    iCrtanjaTrovanja++;
		   prosVrijemeTrovanja=System.currentTimeMillis();
		 }
		else {
			 if(iCrtanjaTrovanja>spriteZaZajednickeEfekte.brojStupaca(1)-1) iCrtanjaTrovanja=0;
			 spriteZaZajednickeEfekte.nacrtajSprite(can, 1,iCrtanjaTrovanja, 0,temRect);
		     }
	
	    
	}
	private void crtajVanjskiIznad(){

		if(crtajVanjskiIznad){
			if(spriteHend!=null)if(spriteHend.postojiLiSlikNaMjestu(brSlVanjIznad))crtajVanjskiIznad=this.spriteHend.vrtiAnimacijuReda(can, this.geVanjskiIznad, brSlVanjIznad,  brRedVanjIznad, recVanjskiIznad, null);
		}
	}
	private void crtajZaledivanja(){
		RectF temRect=new RectF(this.kvadratCrtanja.left,this.kvadratCrtanja.bottom-this.kvadratCrtanja.height()/3,this.kvadratCrtanja.right,this.kvadratCrtanja.bottom);
		prosVrijemeZaledivanja+=this.vrijemePauze;
		if(prosVrijemeZaledivanja+1000/this.spriteZaZajednickeEfekte.brojPrikazaPoSekundi(2)<=System.currentTimeMillis()){
		  
		    if(iCrtanjaZaledivanja<=spriteZaZajednickeEfekte.brojStupaca(2)-1){
		    
		    	spriteZaZajednickeEfekte.nacrtajSprite(can, 2,iCrtanjaZaledivanja, 0,temRect);
		    	
		    }
		   else {iCrtanjaZaledivanja=0;
		
		        spriteZaZajednickeEfekte.nacrtajSprite(can, 2,iCrtanjaZaledivanja,0,temRect);
		        }
		    iCrtanjaZaledivanja++;
		
		   prosVrijemeZaledivanja=System.currentTimeMillis();
		 }
		else {
			 if(iCrtanjaZaledivanja>spriteZaZajednickeEfekte.brojStupaca(2)-1) iCrtanjaZaledivanja=0;
			
			 spriteZaZajednickeEfekte.nacrtajSprite(can, 2,iCrtanjaZaledivanja, 0,temRect);
		     }
	
	    
	}
	public void stojanjeNaMjestu(){
		this.kvadratCrtanjaSkokici.set(this.kvadratCrtanja);

		if(poceoStojati<0){// ako je tek poceo stojati
			//dodajPomakZaSkokice();
			int rand=0;
            rand=randIzmeduCijeli( 0, 3);    
            if(rand==0){//desno
            	brSlicStojanja=this.brRedakaZaHodanjeDesno;
            	brSlikeStojanja=this.brSlikeZaHodanjeDesno;
            }
            else if(rand==1){//lijevo
             	brSlicStojanja=this.brRedakaZaHodanjeLijevo;
            	brSlikeStojanja=this.brSlikeZaHodanjeLijevo;
            }
            else if(rand==2){//gore
             	brSlicStojanja=this.brRedakaZaHodanjeGore;
            	brSlikeStojanja=this.brSlikeZaHodanjeGore;
            }
            else {//dolje
             	brSlicStojanja=this.brRedakaZaHodanjeDolje;
            	brSlikeStojanja=this.brSlikeZaHodanjeDolje;
            }
			iHodanjaGore=iHodanjaPocinjeOd;
			iHodanjaDolje=iHodanjaPocinjeOd;
			iHodanjaLijevo=iHodanjaPocinjeOd;
			iHodanjaDesno=iHodanjaPocinjeOd;
			poceoStojati=System.currentTimeMillis();
		}
		poceoStojati+=vrijemePauze;
		if(poceoStojati+200<System.currentTimeMillis()){
			if(poceoStojati+randIzmeduCijeli( 5, 20)*1000<System.currentTimeMillis()){
				int rand=0;
	            rand=randIzmeduCijeli( 0, 3);    
	            if(rand==0){//desno
	            	brSlicStojanja=this.brRedakaZaHodanjeDesno;
	            	brSlikeStojanja=this.brSlikeZaHodanjeDesno;
	            }
	            else if(rand==1){//lijevo
	             	brSlicStojanja=this.brRedakaZaHodanjeLijevo;
	            	brSlikeStojanja=this.brSlikeZaHodanjeLijevo;
	            }
	            else if(rand==2){//gore
	             	brSlicStojanja=this.brRedakaZaHodanjeGore;
	            	brSlikeStojanja=this.brSlikeZaHodanjeGore;
	            }
	            else {//dolje
	             	brSlicStojanja=this.brRedakaZaHodanjeDolje;
	            	brSlikeStojanja=this.brSlikeZaHodanjeDolje;
	            }
			poceoStojati=System.currentTimeMillis();
		   }
		   spriteHend.nacrtajSprite(can, brSlikeStojanja,0, brSlicStojanja, kvadratCrtanja);
		 }
		else spriteHend.nacrtajSprite(can,  zadnjaSlikaHodanja,this.zadnjaSlicicaHodanja, this.zadnjiRedakHodanja, kvadratCrtanja);// ako slu�?ajno nešto zasšteka da osane u istom pokretu gdje je stao
	}
	private float izracunajKut(float tX,float tY){
		double kut=0;
		kut=Math.atan((tY-yPros)/(tX-xPros));
		kut=Math.toDegrees(kut);
		yPros=tY;
		xPros=tX;
		return (float)kut;
	}
	private int randIzmeduAiB(int a, int b){
       int r=a-b;
       if(r<0){
    	   r=generator.nextInt(Math.abs(r));
    	   r=b-r;
       }
       else {
    	   r=generator.nextInt(Math.abs(r));
    	   r=a-r;
       }
       return r;
    }
	private int randIzmeduCijeli(int a, int b){ // 
		if(b==0) b=1;
		int r= generator.nextInt((int)Math.abs(b+1));
		return r+a;
	 }
	private float randIzmeduPlusMinus(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak

		if(b==0) b=1;
		float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
		if(generator.nextBoolean()){r=r*-1;}
		return r+a;
	 }
    ///DIO ZA STRIJELCE
	private void nacrtajVojnikeStrijelce(){
		izvuciSmijerProtivnika();
	
			    if(ciljanje){
			    	spriteHend.nacrtajSprite(can,slIspucavanja,1, smijerProt, kvadratCrtanja);
			    }
		    	else if(ispaljivanje&&ispaljivanjeProsloVrijeme+1000/spriteHend.brojPrikazaPoSekundi(slIspucavanja)<java.lang.System.currentTimeMillis()){ 
	        	       spriteHend.nacrtajSprite(can,slIspucavanja,ticVoj, smijerProt, kvadratCrtanja);
	                   ticVoj++;
	                   ispaljivanjeProsloVrijeme=java.lang.System.currentTimeMillis();
	                   if(spriteHend.brojStupaca(slIspucavanja)<=ticVoj) {
	    	                                     ticVoj=1;
	    	                                     ispaljivanje=false;
	    	                                     ciljanje=false;
	    	                                    if(ge.indikatorBorbe==12){
	    	                                    	ge.indikatorBorbe=0;
	    	                                    }
	                                             }
	               }
	              else{
	        	          spriteHend.nacrtajSprite(can,slIspucavanja,ticVoj, smijerProt,kvadratCrtanja);
	                    }
	           
	}
	private void izvuciSmijerProtivnika(){
	   
		
		
		double dy=kvadratCrtanja.centerY()-yProt;
	    double dx=xProt-kvadratCrtanja.centerX();
		double kut=57.29577951*Math.atan(Math.abs(dy)/Math.abs(dx));
		if(dy>=0&&dx<=0) kut=180-kut;
		else if(dy<=0&&dx<=0) kut=180+kut;
		else if(dy<=0&&dx>=0) kut=360-kut;
		
		
		if(kut<22.5&&kut>-0||kut>337.5) {
			smijerProt=redSlIstocno;//     istoèno
			slIspucavanja=brSlikeIstocno;
		}
		else if(kut<67.5&&kut>22.5) {
			smijerProt=redSlSjeveroIstocno;
			slIspucavanja=brSlikeSjeveroIstocno;
		}// sjevero-istocno
		else if(kut<112.6&&kut>67.5) // sjever
		{
			smijerProt=redSlSjeverno;
			slIspucavanja=brSlikeSjeverno;
		}
		else if(kut<157.6&&kut>112.6)// sjevero-zapadno
		{
			smijerProt=redSlSjeveroZapadno;
			slIspucavanja=brSlikeSjeveroZapadno;
		}
		else if(kut<202.6&&kut>157.6)// zapad
		{
			{
				smijerProt=redSlZapadno;
				slIspucavanja=brSlikeZapadno;
			}
		}
		else if(kut<247.6&&kut>202.6) // jugo-zapad
		{
			smijerProt=redSlJugoZapadno;
			slIspucavanja=brSlikeJugoZapadno;
		}
		else if(kut<292.6&&kut>247.6)// jug
		{
			smijerProt=redSlJuzno;
			slIspucavanja=brSlikeJuzno;
		}
		else if(kut<337.6&&kut>292.6)// jugo-istok
		{
			smijerProt=redSlJugoIstocno;
			slIspucavanja=brSlikeJugoIstocno;
		}
		/*if(kut<0.3925&&kut>0) smijerProt=0;//     istoèno
		if(kut>5.8875) smijerProt=0;
		else if(kut<1.1775&&kut>0.3925) smijerProt=1;// sjevero-istocno
		else if(kut<1.9625&&kut>1.1775) smijerProt=2;// sjever
		else if(kut<2.7475&&kut>1.9625) smijerProt=3;// sjevero-zapadno
		else if(kut<3.925&&kut>2.7475) smijerProt=4;// zapad
		else if(kut<4.3175&&kut>3.925) smijerProt=5;// jugo-zapad
		else if(kut<5.1025&&kut>4.3175) smijerProt=6;// jug
		else if(kut<5.8875&&kut>5.1025) smijerProt=7;// jugo-i>*/
	}
	
	///////////////setersi

	@Override
	public void setDvojnikaULogici(GameLogicObject obj) {
		dvojnik=obj;
	}
	public void setTouchedObject(UIManagerObject obj){
		if(imTouched&&obj!=this)//ako je veæ kliknuto na ovaj objekt i ako objekt na koji je trenutaèno kliknuto nije ovaj objekt
		{
		tempTauchedObj=obj;
		namjestiTouchedObject();
		boolZaSlanje=true;//signalizira da postoje novi podaci za slanje
		imTouched=false; // stisnut je neki drugi objek i više ovaj ne oèekuje da primi informacije
		}
		if( obj.getIndikator()>=-100&& obj.getIndikator()<200){
		
			kliknutoNaObjekt=true;
			uiMan.setOznacenSam(this);
			boolZaSlanje=true;//signalizira da postoje novi podaci za slanje
			namjestiPomNa(obj.getGlavniRectPrikaza().centerX(),obj.getGlavniRectPrikaza().centerY());
			
		}
		
	}
	public void setTouchedXY(float px,float py){
		if(!kliked){
		     if(this.kliknutoNaObjekt){
			      uiMan.setOznacenSam(this);
		         }
		     if(jeliTockaUnutarKruga(px,py,ge.x3,ge.y3,120*ge.float1/100)||ge.float1<=0){
			      if(imTouched){
				  namjestiPomNa(px,py);
		       	} 
		      }
		     else {
		       	imTouched=false;
			    crtajDaJeOznacen=false;
			    if(ge!=null){
				    ge.imTouched=false;// salje game eventu da je kliknuto na objekt
			       }
		         }
		     boolZaSlanje=true; // omoguæava slanje poruke pri sljedeæem ozivu ovog objekta u uimanageru	
	       }
	}
	public void setImTouched(boolean b){
		imTouched=b;
		crtajDaJeOznacen=b;
		if(ge!=null){
			ge.imTouched=b;// salje game eventu da je kliknuto na objekt
		    boolZaSlanje=true; // omoguæava slanje poruke pri sljedeæem ozivu ovog objekta u uimanageru
		}
		}
///////////////getersi
	public int getIndikator(){return indikator;}
	public float getX(){ return xPolToucha;}  //ove metode koristi UIManager za traženje na koji je objekt kliknuto
	public float getY(){ return yPolToucha;}
	public float getSir(){ return sirinaTouch;}
	public float getVis(){ return visinaTouch;}
/////////game event metode
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) { //od interfacea ObjectLinkerPrikaz
		// TODO Auto-generated method stu
		 ge=e;
		 lijecenje=ge.medic;
	    this.armor=ge.armor;
	    zivSam=e.jesamLiZiv;
		if(e.indikator==2){
		  if(uiMan!=null)	this.uiMan.setNisamViseOznacen(this);
			
		}
		if(ge.indikatorBorbe==0){
			ciljanje=false;
			//udaranjeDesno=udaranjeLijevo=false;
			this.borbaNaDesno=false;
			this.borbaNaLijevo=false;
		}
		if(ge.indikatorBorbe==3||ge.indikatorBorbe==4){
			ciljanje=false;
			ispaljivanje=false;
		
		}
		//DIO OD STRIJELACA
		if(ge.indikatorBorbe==11&&!ispaljivanje){
			ciljanje=true;
			ispaljivanje=false;
		
		}
		if(ge.indikatorBorbe==12&&!ispaljivanje){
			ispaljivanje=true;
			ciljanje=false;
		//	ge.indikatorBorbe=0; // vraca ga u pocetni polozaj
		}
		xProt=ge.pomNaX;
		yProt=ge.pomNaY;
		/////////////////////
		
		vrijemePauze=e.vrijemePauze;
		if(!ge.teleportacija){/// kada pocima prvi dio teleportacije
		   dxLog=e.x2;
		   dyLog=e.y2;
		   geTelep.jesamLiZiv=true;
		   x=e.x-this.sirIscr/2;//koordinate na kojima je objekt igre
		   y=e.y+yPomakUOdnosuNaLogiku-this.visIscr/2;
		   geSjena.teleportacija=false;
		   }
		else {// drugi dio tj kada se već premjesti
			if(this.nestanakProtivnika){
				geSjena.teleportacija=true;
				dxLog=e.x2;
				dyLog=e.y2;
				x=e.x-this.sirIscr/2;//koordinate na kojima je objekt igre
				y=e.y+yPomakUOdnosuNaLogiku-this.visIscr/2;
	         }
		}
		
		helth=e.helth;
		
		this.crtajDaJeOznacen=ge.zastavica1;
		if(this.mojaSjena!=null){
			//geSjena.jesamLiZiv=e.jesamLiZiv;
			
			geSjena.imTouched=crtajDaJeOznacen;
			geSjena.x=x+(this.kvadratCrtanja.width()-this.sirSjene)/2+ge.trimSjeneX;
	    	geSjena.y=y+this.kvadratCrtanja.height()-0.75f*this.visSjene+ge.trimSjeneY;
		    if(e.indikatorBorbe==0){
		    	 dodajPomakZaSkokiceNaPromjeniSlicice();
		    	 dodajPomakZaSkokice();
		        
			   
		        }
		    else {
		    	 //dodajPomakZaSkokice();
		    	
		    	}
		    geSjena.indikator=1;
			this.mojaSjena.GameLinkerIzvrsitelj(geSjena);
		}
	}
	private void posaljiEvent(){  // salje event objektu igre.
		
		if((boolZaSlanje==true||imTouched)&&ge!=null) {// imTouched sam uvbaacio kao uvjet zato da uvijek salje kada je oznacen da obnavljha varijabu za ctanje ozna�?enosti na drugim objektima
			if(kliknutoNaObjekt){
				ge.imTouched=true;// salje game eventu da je kliknuto na objekt
				
				uiMan.setOznacenSam(this);
			}
			dvojnik.GameLinkerIzvrsitelj(ge);
			boolZaSlanje=false;
		    }
		if(kliknutoNaObjekt){
			uiMan.setNisamViseOznacen(this);
			kliknutoNaObjekt=false;
		}
	}
	private void namjestiPomNa(float px,float py){
		if(ge!=null){
			ge.touchedObj=null;// poništava prijašnji moguæi cilj pomaka tj. drugi objekt
		    ge.pomNaX=px;//sprema u eventmenager na koji se x treba pomaknut objekt igre
		    ge.pomNaY=py;
		      }
		}
	
    private void namjestiTouchedObject(){
    	if(ge!=null){
    		ge.pomNaX=-1;//poništava prijašnje moguæe ciljeve pomaka tj. koordinate
    		ge.pomNaY=-1;
    	    ge.touchedObj=tempTauchedObj; // sprema objekt na koji je kliknuto u eventobject	
        	}
    	}

	@Override
	public RectF getGlavniRectPrikaza() {
		RectF rect=new RectF();
		rect.set(this.kvadratCrtanja);
		return rect;
	}

	@Override
	public void onSystemResume() {
		// TODO Auto-generated method stub
		bilderDodajSpriteOnomatopeja(this.uiMan.GL.faIgr.getSprite(98));
		this.bilderDoddajDodatniSpriteZaZajednickeEfekte(this.uiMan.GL.faIgr.getSprite(99));
		this.spriteHend=(this.uiMan.GL.faIgr.getSprite(this.indikator));
	}

	@Override
	public boolean getDaliDaIgnoriramTouch() {
		// TODO Auto-generated method stub
		return false;
	}


	

	

}