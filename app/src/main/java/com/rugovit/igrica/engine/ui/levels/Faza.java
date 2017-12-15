package com.rugovit.igrica.engine.ui.levels;

import java.util.LinkedList;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.elements.GameLogicObject;
import com.rugovit.igrica.MapActivity;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManager;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.IzbornikUniverzalni;
import com.rugovit.igrica.engine.ui.elements.IzbornikZaFazu;

public class Faza implements UIManagerObject {
    private int testI; 
    private boolean testb=false, testb2=false;
    private  RectF recPljeskanja1;
    private  RectF recPljeskanja2;
    private  RectF recPljeskanja3;
    private boolean tekPoceoSaRukama=true;
///podatci iz baze podataka
    private int brStaze;
    private String IDKoristeneFaze;
    private String IDSlota;
    private int stanjeFaze;
    private int brZvjezdica;
    private boolean dBJesamLiZadnja=false; 

    private int  dBTezina=0;
///zastavice
	private boolean izborNaMeni=false;
	private boolean imTouched=false;
	private boolean pokrenutaFaza1Dio=false;
	private boolean tekPoceo=true;
	private boolean pokrenutaFaza2Dio=false;
	private boolean pokrenutaFaza3Dio=false;
//// dinamicke varijable
    private double prosloVrijeme=0;	
    private float miliSecPoPrikMacevi=0;
    private int brSliciceMacevi=0;
/// staticke varijable
    private SpriteHendler spriteIzb;
    private Canvas can;

	private int faza;
	private IzbornikZaFazu izbor;
	private IzbornikUniverzalni izborUni, izborUniNeovisni;
    private RectF rec,tempRec;
    private UIManager uiMana;
    private float x,sir;// sluzi kao konstanta zbog toga sto se rect moze stalno mjenjati i onda se moze izgubiti osnovna vrijednost
    private float y,vis;
    private SpriteHendler spriteHend;
    private int brZivota=10; 
    private UIManager uiMena;
    private int tipIgre;
    private boolean otkljucajSveIgre=false;
    private int polozajDodavanjaUI;
 	public Faza(float x,float y, float sir, float vis,IzbornikZaFazu izbor,SpriteHendler spriteHend,int faza){
		this.x=x;
		this.y=y;
		this.faza=faza;
		this.sir=sir;
		this.vis=vis;
		rec=new RectF(x,y,x+sir,y+vis);
		tempRec=new RectF(x,y,x+sir,y+vis);
	    this.izbor=izbor;
		this.spriteHend=spriteHend;
		
		
	}
	public Faza(float xx,float yy, float sir, float vis,SpriteHendler spriteHend,UIManager uiMan ,IzbornikUniverzalni izbornik,int polozajDodavanjaUI,int faza){
		this.x=xx;
		this.y=yy;
		this.sir=sir;
		this.vis=vis;
		this.faza=faza;
		this.uiMana=uiMan;
		rec=new RectF(x,y,x+sir,y+vis);
		tempRec=new RectF(x,y,x+sir,y+vis);
		this.uiMena=uiMan;
		 izborUniNeovisni= izbornik;

	     this.polozajDodavanjaUI= polozajDodavanjaUI;
		this.spriteHend=spriteHend;
		
	}
	public Faza(float xx,float yy, float sir, float vis,SpriteHendler spriteHend,SpriteHendler izbornikSprite,UIManager uiMan ,int faza){
		this.x=xx;
		this.y=yy;
		this.sir=sir;
		this.vis=vis;
		this.faza=faza;
		this.uiMana=uiMan;
		this.spriteIzb=izbornikSprite;
		rec=new RectF(x,y,x+sir,y+vis);
		tempRec=new RectF(x,y,x+sir,y+vis);
		this.uiMena=uiMan;
	    this.izborUni=new IzbornikUniverzalni(null,uiMan,1,1,302){
	    	GameEvent geSvitak=new GameEvent(this);
	    	GameEvent geSlova=new GameEvent(this);
	    	GameEvent geKut=null;//new GameEvent(this);
	    	 boolean tekPoc=true,prviDioGotov=false;
	    	// float top;
	    	 RectF rect=new RectF();
	    	 
			@Override
			public void univerzalniIzvrsitelj(int brTouchPolja) {
				// TODO Auto-generated method stub
				vratiTouchPolje(brTouchPolja);
				
				
			}
			@Override
			 public void kliknutoSaStraneIliNaNekiDrugiObjekt(float xKlik,float yKlik, UIManagerObject klikObj ){
	   			  this.pokreniZavrsnuAnimaciju();
	   		  }
			@Override
			public void izbornikNaMeniUniverzalni(boolean izbornikNaMeni) {
				// TODO Auto-generated method stub
				izborNaMeni=izbornikNaMeni;
				tekPoc=true;
				
			}
	         public boolean	nacrtajKraj(float fps,Canvas can){
	        		
		            
	    			 if(tekPoc) {
	    				 postaviLijeviVrhProzoraPix(rec.centerX()-izborUni.dajMiRectCijelogProzora().width()/2, rec.centerY()-izborUni.dajMiRectCijelogProzora().height()/2);
	    				 this.izracVelProzora();
	    				if(this.getGlavniRectPrikaza().right>uiMena.getSirinuVirtualogProstora()-uiMena.getMarginuDesnu()){
	    					
	    					
	    					this.postaviLijeviVrhProzoraPix(uiMena.getSirinuVirtualogProstora()-uiMena.getMarginuDesnu()-this.getGlavniRectPrikaza().width(), this.getGlavniRectPrikaza().top);
	    				}
	    				 this.izracVelProzora();
	    				 uiMana.iskljuciTouchEvente();	
	    			
	    				 rect.set(this.dajMiRectCijelogProzora());
	    				 //rect.offsetTo(rect.left, can.getHeight());
	    			 // top=can.getHeight()-dajMiRectCijelogProzora().top;
	    			  tekPoc=false;
	    			  prviDioGotov=false;
	    			  geSvitak.jesamLiZiv=true;
	    			  geSlova.jesamLiZiv=true;
	    			  }
	    			 
	    			/*if(!translacijaGotova){
	    				translacijaGotova=spriteIzb.animacijaSlaganjeTranslacija(0,-top, 0,0.9f, fps,  rect);
	    			}
	    			if(!rotacijaGotova){
	    				rotacijaGotova= spriteIzb.animacijaSlaganjeRotacija(-90, 90,0.9f, fps);
	    			}*/
	    			// tekPoc=spriteIzb.animacijaSlaganjeRazmotajSvitakVertikalnoNACRTAJ(can,geSvitak,geKut, 1, 0, 10, 10,0.7f,  rect, null,false);
	    		/*	 if(!prviDioGotov){
	    				 spriteIzb.nacrtajSprite(can, 1, spriteIzb.brojStupaca(1)-1, 0, rect);
	    				 prviDioGotov=spriteIzb.vrtiAnimacijuRedaUnatrag(can,geSlova, 0, 0, rect, null);
	    				 
	    			 }
	    			 
	    			 else*/ 
	    			 uiMana.iskljuciTouchEvente();	
	    			 if(!tekPoc){
	    				 tekPoc=spriteIzb.vrtiAnimacijuRedaUnatrag(can,geSvitak, 1, 0, rect, null);
	    			 }
	    			 if(tekPoc)uiMana.ukljuciTouchEvente();	
	    			 return  tekPoc;
	    			
	  		    	}
            public boolean	nacrtajUvod(float fps,Canvas can){
	
	            
    			 if(tekPoc) {
    				
    				 postaviLijeviVrhProzoraPix(rec.centerX()-izborUni.dajMiRectCijelogProzora().width()/2,rec.centerY()-izborUni.dajMiRectCijelogProzora().height()/2);
    				 this.izracVelProzora();
    				if(this.getGlavniRectPrikaza().right>uiMena.getSirinuVirtualogProstora()-uiMena.getMarginuDesnu()){
    					
    					
    					this.postaviLijeviVrhProzoraPix(uiMena.getSirinuVirtualogProstora()-uiMena.getMarginuDesnu()-this.getGlavniRectPrikaza().width(), this.getGlavniRectPrikaza().top);
    				}
    				 this.izracVelProzora();
    				 uiMana.iskljuciTouchEvente();	
    				// uiMana.postaviTempUniverzalniIzbornik(izborUni);// funkcija sluzi za back botun
    				 rect.set(this.dajMiRectCijelogProzora());
    				 //rect.offsetTo(rect.left, can.getHeight());
    			 // top=can.getHeight()-dajMiRectCijelogProzora().top;
    			  tekPoc=false;
    			  prviDioGotov=false;
    			  geSvitak.jesamLiZiv=true;
    			  geSlova.jesamLiZiv=true;
    			  }
    			 
    			/*if(!translacijaGotova){
    				translacijaGotova=spriteIzb.animacijaSlaganjeTranslacija(0,-top, 0,0.9f, fps,  rect);
    			}
    			if(!rotacijaGotova){
    				rotacijaGotova= spriteIzb.animacijaSlaganjeRotacija(-90, 90,0.9f, fps);
    			}*/
    			// tekPoc=spriteIzb.animacijaSlaganjeRazmotajSvitakVertikalnoNACRTAJ(can,geSvitak,geKut, 1, 0, 10, 10,0.7f,  rect, null,false);
    			 uiMana.iskljuciTouchEvente();	
    			 if(!prviDioGotov){
    				 prviDioGotov=spriteIzb.vrtiAnimacijuReda(can,geSvitak, 1, 0, rect, null);
    				 if( prviDioGotov){
    					 spriteIzb.pustiZvukSamostalno(1);
    				 }
    			 }
    			 else if(!tekPoc){
    				 spriteIzb.nacrtajSprite(can, 1, spriteIzb.brojStupaca(1)-1, 0, rect);
    				 tekPoc=spriteIzb.vrtiAnimacijuReda(can,geSlova, 0, 0, rect, null);
    			 }
    			 if(tekPoc)uiMana.ukljuciTouchEvente();	
    			 return  tekPoc;
    			
  		    	}
            public void nacrtajVanjskiIznad(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
            	spriteIzb.nacrtajSprite(can, 1, spriteIzb.brojStupaca(1)-1, 0, rect);
            	spriteIzb.nacrtajSprite(can, 0,spriteIzb.brojStupaca(0)-1, 0, rect);
            }
            /*
    		  public boolean nacrtajKraj(float fps,Canvas can){
    			  if(tekPoc) {
    				  uiMana.iskljuciTouchEvente();	
    				  uiMana.postaviTempUniverzalniIzbornik(null);
    				  rect.set(this.dajMiRectCijelogProzora());
        			  //top=can.getHeight();
        			  tekPoc=false;
        				 prviDioGotov=false;
        			}
 
    			  tekPoc=spriteIzb.animacijaSlaganjeSmotajSvitakVertikalnoNACRTAJ(can, geSvitak,geKut,1, 0, 10, 10,0.5f,  rect, null,false);
    			  if(tekPoc){
    				  uiMana.ukljuciTouchEvente();	
    				  this.postaviLijeviVrhProzoraPix(-10000, -10000);
    			  }
    			  return tekPoc;
    			  
    		  }
	    */
    		  public void zavrsenaZavrsnaAnimacija(){
  			    
	 				uiMana.makniObjekt(this);
	 				
  		  }
    		  @Override
	 			public void backBotunStisnut(){
    			  tekPoc=true;
	 				this.pokreniZavrsnuAnimaciju();
	 			}

			@Override
			public RectF getGlavniRectPrikaza() {
				RectF rect=new RectF();
				rect.set(this.rect);
				return rect;
			}
	    };
	 
	    izborUni.postaviRazmakIzmeduPoljaPix(0.3f*uiMan.getPixPoCmX(), 0.3f*uiMan.getPixPoCmY());
	    izborUni.postaviVelicinuPoljaUPix(1.5f*uiMan.getPixPoCmX(), 1.5f*uiMan.getPixPoCmY());
	    izborUni.izracVelProzora();
	    izborUni.postaviLijeviVrhProzoraPix(-10000, -10000);
	    izborUni.izracVelProzora();
	    if(izborUni.getGlavniRectPrikaza().right>uiMena.getSirinuVirtualogProstora()-uiMena.getMarginuDesnu()){
	    	izborUni.postaviLijeviVrhProzoraPix(uiMena.getSirinuVirtualogProstora()-uiMena.getMarginuDesnu()-izborUni.getGlavniRectPrikaza().width(), izborUni.getGlavniRectPrikaza().top);
		}
	    izborUni.izracVelProzora();
	    uiMan.dodajElementUListu(izborUni,3);
		this.spriteHend=spriteHend;
		
	}
////BILDER
	public void pokreniSveTipoveIgri(){
		otkljucajSveIgre=true;
	}

	public void dodajParametreIzDB(int brStaze,String IDKoristeneFaze,String IDSlota,int stanjeFaze, int brZvjezdica){
		this.brStaze=brStaze;
		this.IDKoristeneFaze=IDKoristeneFaze;
		this.IDSlota=IDSlota;
		this.stanjeFaze=stanjeFaze;
		this.brZvjezdica=brZvjezdica;
	}	
	public void namjestiStanjeFaze(boolean jesamLiZadnja,int dBTezina){// sluzi samo za prikaz na mapi
		this.dBJesamLiZadnja=jesamLiZadnja;
		this.dBTezina=dBTezina;
	}
	
////PRIVATNE METODE
	 private  LinkedList<Integer>  izborStanjeNekoristenihBotuna(){
	
		 /* int i =GL.getUkupniNovac();
		 if(GL.getUkupniNovac()<cijenaBotun1||cijenaBotun1==-1) {
			                                listaNekoristenih.add(1); 
		                                      }
		 if(GL.getUkupniNovac()<cijenaBotun2||cijenaBotun2==-1)  {
			                                listaNekoristenih.add(2); 
		                                     }
		 if(GL.getUkupniNovac()<cijenaBotun3||cijenaBotun3==-1){
			                                listaNekoristenih.add(3); 
		                                     }
		 if(GL.getUkupniNovac()<cijenaBotun4||cijenaBotun4==-1){
			                                listaNekoristenih.add(4); 
		                                    }*/
		 LinkedList<Integer>  listaNekoristenih=null;
	/*	 if(!otkljucajSveIgre){
			 listaNekoristenih=new  LinkedList<Integer>();
		     listaNekoristenih.add(2);
		     listaNekoristenih.add(3);
		     listaNekoristenih.add(4);
		 }*/
		 return listaNekoristenih;
		 
	 }
	 private boolean nacrtajMaceveIVratiTrueKadZavrsis(){
		 boolean b=false;
		 if(prosloVrijeme+miliSecPoPrikMacevi<=System.currentTimeMillis()){	
			    if(this.brSliciceMacevi<spriteHend.brojStupaca(0)-1){
				   spriteHend.nacrtajSprite(can, 0,brSliciceMacevi, 0, rec);
				   brSliciceMacevi++;
			    }
			    else {
			       b=true;
			    }	   
			   this.prosloVrijeme=System.currentTimeMillis();
			 }
		 else{   spriteHend.nacrtajSprite(can, 0,brSliciceMacevi, 0, rec); }
		return b;
	 }
		private void stvariKojeSeIzvrsavajuSamoJedanput(){
			this.miliSecPoPrikMacevi=1000/spriteHend.brojPrikazaPoSekundi(0);// predpostavio sam da su macevi u nultoj slici
		}
	
////PUBLIC METODE
	public void izbornikNaMeni(boolean b){
		 izborNaMeni=b;
	}
	 public void vratiTouchPolje(int polje){
		 if(polje==1){// normalna
			 tipIgre=1;
			 pokrenutaFaza1Dio=true;
		 }
		 else if(polje==2){// man to man
			 tipIgre=2;
			 pokrenutaFaza1Dio=true;
		 }
		 else if(polje==3){// fire away
			 tipIgre=3;
			 pokrenutaFaza1Dio=true;
		 }
		 else if(polje==4){// spell them
			 tipIgre=4;
			 pokrenutaFaza1Dio=true;
		 }
	 }
	 public RectF getRect(){
		 return rec;
	 }
//////	
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDvojnikaULogici(GameLogicObject obj) {
		// TODO Auto-generated method stub
		
	}
////////////NACRTAJ/////////////////
	@Override
	public void nacrtaj(Canvas can, float fps,
			UIManager uiMan, float PpCmX, float PpCmY, float pomCanX, float pomCanY) {
		// TODO Auto-generated method stub
		
		if(tekPoceo){
	        this.can=can;
			stvariKojeSeIzvrsavajuSamoJedanput();
			tekPoceo=false;
		}
	
		if(izborNaMeni){
			if(izbor!=null)izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());// namjesta konstantno koji se botuni koriste za dinamièko alociranje botuna
			else this.izborUni.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
		}
		if(imTouched){
			    if(!izborNaMeni){
				if(izbor!=null){
					if(!uiMan.daliPostojiObjekt(izbor))  uiMan.dodajElementUListu(izbor,3);
					
					izbor.pokreniMojIzbornik(this,izborStanjeNekoristenihBotuna());// ako detektira da je kliknuto na njega pokreæe izbornik
				}
				else if(izborUni!=null){
					if(!uiMan.daliPostojiObjekt(izborUni))  uiMan.dodajElementUListu(izborUni,3);
					this.izborUni.pokreniMojIzbornik(izborStanjeNekoristenihBotuna());
					uiMana.dodajElementUListu(izborUni, 3);
				}
				else if( izborUniNeovisni!=null){
					this.izborUniNeovisni.pokreniMojIzbornik();
					uiMana.dodajElementUListu(this.izborUniNeovisni, this.polozajDodavanjaUI);
					
				}
				//  izborNaMeni=true;
			     }
		 }
		 if( !pokrenutaFaza1Dio&&!pokrenutaFaza2Dio&&!pokrenutaFaza3Dio){
			 nacrtajRuke( fps);
			// this.spriteHend.nacrtjDioBitmapa(can, 0, new RectF(0,0,10,100), rec, null);
			 if(this.dBJesamLiZadnja){      boolean b=false;
			       if(!testb){
				 
				       this.spriteHend.animacijaSlaganjeTranslacijaVremenska(0,0,0,-this.vis/3, 0,1, tempRec);
				  
				       testb= spriteHend.animacijaSlaganjeUvecanjeVremenska(0,0,this.sir/3, this.vis/3, 1f, 1f, tempRec);
			        }
				  else {
					   this.spriteHend.animacijaSlaganjeTranslacijaVremenska(0,0,0,this.vis/3, 0,1, tempRec);
				
					   testb2=	spriteHend.animacijaSlaganjeUvecanjeVremenska(0,0,-this.sir/3, -this.vis/3, 1,1, tempRec);
				    }
			      if(testb2) {
				       testb2=false;
				       testb=false;
				       tempRec.set(rec);
			      }
			       spriteHend.animacijaSlaganjeNacrtaj(can, tempRec, 0, 0, fps,null,false);
			       }
			 else spriteHend.animacijaSlaganjeNacrtaj(can, tempRec, 0, 0, fps,null,false);
			 //spriteHend.nacrtajSprite(can, 0,0, 0, rec);
			// this.spriteHend.animacijaPovecajISmanjiUOdnosuNaTaktove(can, rec, 0, 0,fps*1,fps*1, 15, 15,false);
            /* testI++; 
			 if(testI>7) {
            	  testI=0;
            	  b=true;
              }
              this.spriteHend.animacijaPovecajISmanjiUOdnosuNaSliciceKontinuirano(can, rec, 0, 0, 5, 5, 20, 20,fps);*/
            // this.spriteHend.animacijaPovecajISmanjiUOdnosuNaSlicice(can, rec, 0, 0, 5, 5, 20, 20,b);
		 }
		 if(pokrenutaFaza1Dio){// prvi dio mou biti oni macevi
			 nacrtajRuke( fps);
			 uiMana.iskljuciTouchEvente();
			 pokrenutaFaza2Dio= nacrtajMaceveIVratiTrueKadZavrsis();
			 if(pokrenutaFaza2Dio) pokrenutaFaza1Dio=false;
		 }
		 if(pokrenutaFaza2Dio){// drugii dio moze biti kratki strip
			 spriteHend.nacrtajSprite(can, 0,9,0, rec);
			// act.reciklirajSe();
			/* Uvod uvod= new Uvod((Context)act,8, act,faza);
			 act.bildajUvodZaFazu(uvod, faza);
			 uiMan.unlockCanvasAndPost();
			 uvod.pokreniGlavnuPetlju();
			 act.addContentView(uvod,null);*/
			 //act.stvoriNovuFazu(faza);
			 uiMan.reciklirajPozadinu();
			 uiMan.reciklirajTeksturu();
			 Activity ac=(Activity)uiMan.context;
			 MapActivity act=( MapActivity)ac;
			 uiMan.stop();
			// act.reciklirajSe();
			 uiMan.pokreniGlavnuPetlju("Faza klasa nakon 2 dijela");
			
			 act.stvoriNovuFazu(brStaze,IDKoristeneFaze,IDSlota,this.brZvjezdica,this.dBTezina,tipIgre);
			
			
		
			 pokrenutaFaza3Dio=true;
			 if(pokrenutaFaza3Dio) pokrenutaFaza2Dio=false;
		 }
		 if(pokrenutaFaza3Dio){// treci dio moze biti konacno pokretanje faze
			 Activity ac=(Activity)uiMan.context;
			 MapActivity act=( MapActivity)ac;
			 
		 }
		 
		 
	}
/////////////////////////////////
	private void nacrtajRuke( float fps){
		
		
		
		 if(tekPoceoSaRukama) {

			 recPljeskanja1=new RectF(0,0,3*rec.height()/7,3*rec.width()/7);
			 recPljeskanja2=new RectF(0,0,3*rec.height()/7,3*rec.width()/7);
			 recPljeskanja3=new RectF(0,0,3*rec.height()/7,3*rec.width()/7);
				if(this.brZvjezdica==1){
					

						 recPljeskanja1.offsetTo(this.rec.centerX()-recPljeskanja1.width()/2,rec.top-2*rec.height()/4);
						  
					}
					

				
				else if(this.brZvjezdica==2){
				
						 recPljeskanja1.offsetTo(rec.centerX()-4*recPljeskanja1.width()/4,rec.top-2*rec.height()/4);
						  recPljeskanja2.offsetTo(rec.centerX()+1*recPljeskanja1.width()/5,rec.top-2*rec.height()/4);
						  
						 
					}
			
				
	            else if(this.brZvjezdica==3){
	            	
	            		
						 recPljeskanja1.offsetTo(rec.centerX()-6*recPljeskanja1.width()/4,rec.top-1*rec.height()/4);
						  recPljeskanja2.offsetTo(rec.centerX()-2*recPljeskanja2.width()/4,rec.top-2*rec.height()/4);
						  recPljeskanja3.offsetTo(rec.centerX()+2*recPljeskanja3.width()/4,rec.top-1*rec.height()/4);
						 
					}
				tekPoceoSaRukama=false;
		        }
	            	
		          if(brZvjezdica>0){     
		        	    spriteHend.animacijaSlaganjeNacrtaj(can,recPljeskanja1, 1, 0, fps,null,false);
		          }
		          if(brZvjezdica>1){   
		                spriteHend.animacijaSlaganjeNacrtaj(can,recPljeskanja2,1, 0, fps,null,false);
		          }
		          if(brZvjezdica>2){   
		                spriteHend.animacijaSlaganjeNacrtaj(can,recPljeskanja3,1, 0, fps,null,false);
	            	
		          }
				}
				
		
	@Override
	public void setImTouched(boolean b) {
		// TODO Auto-generated method stub
		imTouched=b;
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
		return 1;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return rec.left;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return rec.top;
	}

	@Override
	public float getSir() {
		// TODO Auto-generated method stub
		return rec.right-rec.left;
	}

	@Override
	public float getVis() {
		// TODO Auto-generated method stub
		return rec.bottom-rec.top;
	}
	@Override
	public RectF getGlavniRectPrikaza() {
		// TODO Auto-generated method stub
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
