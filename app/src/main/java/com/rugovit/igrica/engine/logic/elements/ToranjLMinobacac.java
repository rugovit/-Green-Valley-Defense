package com.rugovit.igrica.engine.logic.elements;

import java.util.LinkedList;
import java.util.Random;

import android.graphics.Point;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.IzbornikZaToranj;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class ToranjLMinobacac extends ToranjL implements GameLogicObject{
	private Random generator=new Random();
	private float velXUPrik=0;
	private float velYUPrik=0;
	private double  vrijemeIspucavanja, RofFMili;
	private double vrijemePauze=0;
	private boolean izracunajPauzu=false;
	/////(/( varijable tornja sa projektilima
	private ProjektilL projektil1;
	private ProjektilL projektil2;
	private GameLogicObject tempProtivnik;
	private GameLogicProtivnik tempStari=null;
	private GameLogicProtivnik tempNovi=null;
	//////// varijable od tornja kasarne
	private float xOkupljanja,yOkupljanja;
	private double startTime=0;
	private float secDoNovogVojnika=4;// sekunde do kada æe se gennerirati novi vojnik ako fali koji defolt 10
	private int maxBrVojnika=4;// maksimalni broj vojnika, defolt 4
	private LinkedList<GameLogicBranitelj> listaGrupe;// svaki æe toranj  održavat listu grupe za svoje objekte
	///////////////////
	///////izbornik varijable
	private IzbornikZaToranj izbor;
	//private float cijenaBotun1=-1,cijenaBotun2=-1,cijenaBotun3=-1,cijenaBotun4=-1;// -1 znaèi da se ne korristi taj botun
	//////////////////////////
	////zastavice
	private boolean izborNaMeni=false;
	private boolean tekPoceo=true;
	///////

    private UIManagerObject dvojnik;
    private GameLogic GL;
	private FazeIgre FI;
	private SpriteHendler sprHendZaProjekt;
	private GameLogicObject[] objPoPravcima; 
	private GameLogicObject maxDolje, maxGore,maxDesno,maxLijevo;
	private int indikator;
	float rateOfFire;
	private float xTor,yTor, radijus;
	//private float rTor;
	private RectF rec;
	private Math mat;
	private float TpS=10;// ticks per sec, broj poziva ove klase po sekundi definiro sam ga da je u startu 10 ali to se može promjeniti pri pozivu malog runa
	private int ticFire,iFire=0;// sprema koliko puta se treba pozvati ova klasa prije nego se ispali projektill
	private float xKraj,yKraj; // x i y kraja kojeg se brani
	private float razVrhSlIsp;// razlika vrh wlicice i ispaljivanja
	private GameLogicObject[] listaSudara;// spremaju se objekti sa kojima se sudara
	public ToranjLMinobacac(float xTor, float yTor,float radijus,float RofF,float razVrhSlIsp,IzbornikZaToranj izbor,int indikator){
		super(indikator,izbor,xTor,yTor,radijus);
		this.yTor=yTor;
		this.xTor=xTor;
		this.indikator=indikator;
		this.radijus=radijus;
		this.rateOfFire=RofF;
		this.izbor=izbor;
		this.razVrhSlIsp=razVrhSlIsp;
		listaGrupe=new LinkedList<GameLogicBranitelj>();
		 RofFMili=1000/RofF;
		ge=new GameEvent(this);
		objPoPravcima=new GameLogicObject[4];
		rec=new RectF(xTor,yTor,radijus*2+xTor,radijus*2+yTor);// treba mi rect zbog toga što ga zahtjevaju drugi djelovi igrice
		ticFire=1;// ovdje odreðujemm defoltni 
	}
///////////public metode////////////////////////////////////////////  
/*	 public void vratiTouchPolje(int polje){// vraæa polje na koje je kliknuto
/////////////TORANJ EMBRIO
	 if(indikator==200){// ako je ova klasa toranj embrio
		 if( GL.getUkupniNovac()>=FI.cijenaObjecta(125)&&polje==1){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
		      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(125));	 
		      FI.br125ToranjMinobacac(rec.centerX(),rec.centerY());
		      ubijMe();
	     }
	 else if( GL.getUkupniNovac()>=FI.cijenaObjecta(150)&&polje==2){//TORANJ KASARNA dugmiæ 2
			           GL.dodajNovacPlusMinus(-FI.cijenaObjecta(150));	 
			           FI.br150ToranjKasarna(rec.centerX(),rec.centerY());
			           ubijMe();
		        }
	 }
/////////////////////////////
/////////////toranj minobacac
	 if(indikator==125){
		 if( GL.getUkupniNovac()>=FI.cijenaObjecta(150)&&polje==2){//TORANJ KASARNA dugmiæ 2
			 GL.dodajNovacPlusMinus(-FI.cijenaObjecta(150));	 
			 FI.br150ToranjKasarna(rec.centerX(),rec.centerY());
			 ubijMe();
		 }
	 
	 }

}	*/	 
 public void setXVelUPrik(float xp){
	 velXUPrik=xp;
 }
 public void setYVelUPrik(float yp){
	 velYUPrik=yp;
 }	

 public void setRateOfFire(float rof){
	 rateOfFire=rof;
	 RofFMili=1000/rof;
 }
//////////////////////////////////////////////////////////////////////	
///////privatne metode///////////////////////////////////////7//
/* private  LinkedList<Integer>  izborStanjeNekoristenihBotuna(){
	 LinkedList<Integer>  listaNekoristenih=new  LinkedList<Integer>();
	 int i =GL.getUkupniNovac();
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
	                                    }
	 return listaNekoristenih;
 }
 private void dodajCijenuBotunima(){
	
	
		              cijenaBotun1=FI.cijenaObjecta(125);
                      cijenaBotun2=FI.cijenaObjecta(150);
	
   ////
 }
*/
 /*private void ubijMe(){
	  ge.jesamLiZiv=false;
	  dvojnik.GameLinkerIzvrsitelj(ge);
	  GL.mrtavSam(this);
 }*/
 @Override
 public void ubijMe(){
	 if(this.projektil1!=null){
			projektil1.ubijSe();
		}
		if(this.projektil2!=null){
			projektil2.ubijSe();
		}
 	  ge.jesamLiZiv=false;
 	  dvojnik.GameLinkerIzvrsitelj(ge);
 	  GL.mrtavSam(this);
  }
 private  LinkedList<Integer>  izborStanjeNekoristenihBotuna(){
	 LinkedList<Integer>  listaNekoristenih=new  LinkedList<Integer>();
	 float i =GL.getUkupniNovac();
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
	                                    }
	 return listaNekoristenih;
 }
 private void dodajCijenuBotunima(){
	if(indikator==125){
		cijenaBotun3=FI.cijenaObjecta(126);
	                   }
	else if(indikator==126){
		cijenaBotun3=FI.cijenaObjecta(127);
    }
	else if(indikator==127){
		cijenaBotun3=FI.cijenaObjecta(128);
		cijenaBotun4=FI.cijenaObjecta(129);
    }
	else if(indikator==128){
		cijenaBotun3=FI.cijenaObjecta(130);
	
    }
	//kluster rankivi
	else if(indikator==130){
		cijenaBotun3=FI.cijenaObjecta(131);
	
    }
	else if(indikator==131){
		cijenaBotun3=FI.cijenaObjecta(132);
	
    }
	else if(indikator==129){
		cijenaBotun3=FI.cijenaObjecta(133);
	
    }
	else if(indikator==133){
		cijenaBotun3=FI.cijenaObjecta(134);
	
    }
	else if(indikator==134){
		cijenaBotun3=FI.cijenaObjecta(135);
	
    }
	///////
	
	
   ////
 }
 private void ispucajProjektil(){
	 projektil1.pokreniSePremaCilju( tempProtivnik.getRect().centerX()+randIzmeduPlusMinus(0,this.velXUPrik/5),  tempProtivnik.getRect().centerY()+randIzmeduPlusMinus(0,this.velXUPrik/5));
	
 }
 private void stvariKOjeSeIzvrsavajuSamoJedanput(){// samo æe se izvršit na pocetku ova metoda
	 dodajCijenuBotunima();
	 this.vrijemeIspucavanja=System.currentTimeMillis();
	 ////Toranj kasarna 1 razine
	 listaSudara=GL.provjeriKoliziju(this);
	
 }
 private void stvoriProjektilePripadajucegTipa(){
		if(this.indikator==125) projektil1 =GL.faIgr.br401ProjektilGranata(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		else  if(this.indikator==126) projektil1 =GL.faIgr.br408ProjektilTNT(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		//projektil2 =GL.faIgr.br401ProjektilGranata(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp); 
		else  if(this.indikator==127) projektil1 =GL.faIgr.br409ProjektilARMOR(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		//projektil2 =GL.faIgr.br401ProjektilGranata(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp); 
		else  if(this.indikator==128) projektil1 =GL.faIgr.br410ProjektilKLUSTER(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		//projektil2 =GL.faIgr.br401ProjektilGranata(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp); 
		else  if(this.indikator==129) projektil1 =GL.faIgr.br411ProjektilNAPALM(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		//projektil2 =GL.faIgr.br401ProjektilGranata(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp); 
		else  if(this.indikator==130) projektil1 =GL.faIgr.br412ProjektilKLUSTERrank1(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		else  if(this.indikator==131) projektil1 =GL.faIgr.br413ProjektilKLUSTERrank2(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		else  if(this.indikator==132) projektil1 =GL.faIgr.br414ProjektilKLUSTERrank3(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		
		else  if(this.indikator==133) projektil1 =GL.faIgr.br415ProjektilNAPALMrank1(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		else  if(this.indikator==134) projektil1 =GL.faIgr.br416ProjektilNAPALMrank2(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		else  if(this.indikator==135) projektil1 =GL.faIgr.br417ProjektilNAPALMrank3(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp);
		//projektil2 =GL.faIgr.br401ProjektilGranata(this,this.dvojnik.getGlavniRectPrikaza().centerX(), this.dvojnik.getGlavniRectPrikaza().top+razVrhSlIsp); 
 }
    private void posaljiPrikazu(){
    	dvojnik.GameLinkerIzvrsitelj(ge);
    	 ge.izbornikUpaljen=izborNaMeni;
    }

	private GameLogicObject izvuciObjKojegSeGada(){
		int i=0;
		tempStari=null;
		tempNovi=null;
		while(listaSudara[i]!=null){
			
			
			if(listaSudara[i].getIndikator()>=-100&&listaSudara[i].getIndikator()<=-1){// provjerava dali je igraè od protivnika
				tempNovi=(GameLogicProtivnik)listaSudara[i];
				if(!tempNovi.jesamLiNaInertnomPutu()){	
			      if(!tempNovi.jesamLiImunNaToranjMinobacac()){   if(tempStari==null) tempStari=tempNovi;
			       else if(Math.abs(tempNovi.getRedBrPutaNaKojemSi())>Math.abs(tempStari.getRedBrPutaNaKojemSi()))  tempStari=tempNovi;
			      }
			      }
			}
			
			i++;
			
			if(listaSudara.length<=i) break;
			
		}
		return (GameLogicObject)tempStari;
	}
////
//neke èesto korištene metode
	private float randIzmeduPlusMinus(float a, float b){ // raæa sluèajni broj izmeðu ukljuèujuæi i predznak
		if(b==0) b=1;
		float r= (float)generator.nextInt((int)Math.abs(b)*100)/100;
		if(generator.nextBoolean()){r=r*-1;}
		return r+a;
	 }
	private double udaljenostDvijeTocke(float ax,float ay, float bx,float by){// predpostavlja da se sve odvija u prvom kvadrantu
		 return Math.hypot(ax-bx,ay-by);
	}
	 public static boolean linesIntersect(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
	      // Return false if either of the lines have zero length
	      if (x1 == x2 && y1 == y2 ||
	            x3 == x4 && y3 == y4){
	         return false;
	      }
	      // Fastest method, based on Franklin Antonio's "Faster Line Segment Intersection" topic "in Graphics Gems III" book (http://www.graphicsgems.org/)
	      double ax = x2-x1;
	      double ay = y2-y1;
	      double bx = x3-x4;
	      double by = y3-y4;
	      double cx = x1-x3;
	      double cy = y1-y3;

	      double alphaNumerator = by*cx - bx*cy;
	      double commonDenominator = ay*bx - ax*by;
	      if (commonDenominator > 0){
	         if (alphaNumerator < 0 || alphaNumerator > commonDenominator){
	            return false;
	         }
	      }else if (commonDenominator < 0){
	         if (alphaNumerator > 0 || alphaNumerator < commonDenominator){
	            return false;
	         }
	      }
	      double betaNumerator = ax*cy - ay*cx;
	      if (commonDenominator > 0){
	         if (betaNumerator < 0 || betaNumerator > commonDenominator){
	            return false;
	         }
	      }else if (commonDenominator < 0){
	         if (betaNumerator > 0 || betaNumerator < commonDenominator){
	            return false;
	         }
	      }
	      if (commonDenominator == 0){
	         // This code wasn't in Franklin Antonio's method. It was added by Keith Woodward.
	         // The lines are parallel.
	         // Check if they're collinear.
	         double y3LessY1 = y3-y1;
	         double collinearityTestForP3 = x1*(y2-y3) + x2*(y3LessY1) + x3*(y1-y2);   // see http://mathworld.wolfram.com/Collinear.html
	         // If p3 is collinear with p1 and p2 then p4 will also be collinear, since p1-p2 is parallel with p3-p4
	         if (collinearityTestForP3 == 0){
	            // The lines are collinear. Now check if they overlap.
	            if (x1 >= x3 && x1 <= x4 || x1 <= x3 && x1 >= x4 ||
	                  x2 >= x3 && x2 <= x4 || x2 <= x3 && x2 >= x4 ||
	                  x3 >= x1 && x3 <= x2 || x3 <= x1 && x3 >= x2){
	               if (y1 >= y3 && y1 <= y4 || y1 <= y3 && y1 >= y4 ||
	                     y2 >= y3 && y2 <= y4 || y2 <= y3 && y2 >= y4 ||
	                     y3 >= y1 && y3 <= y2 || y3 <= y1 && y3 >= y2){
	                  return true;
	               }
	            }
	         }
	         return false;
	      }
	      return true;
	   }
	 public static Point getLineLineIntersection(double x1, double y1, double x2, double y2,//racuna beskonacnu liniju
			 double x3, double y3, double x4, double y4) {
	      double det1And2 = det(x1, y1, x2, y2);
	      double det3And4 = det(x3, y3, x4, y4);
	      double x1LessX2 = x1 - x2;
	      double y1LessY2 = y1 - y2;
	      double x3LessX4 = x3 - x4;
	      double y3LessY4 = y3 - y4;
	      double det1Less2And3Less4 = det(x1LessX2, y1LessY2, x3LessX4, y3LessY4);
	      if (det1Less2And3Less4 == 0){
	         // the denominator is zero so the lines are parallel and there's either no solution (or multiple solutions if the lines overlap) so return null.
	         return null;
	      }
	      double x = (det(det1And2, x1LessX2,
	            det3And4, x3LessX4) /
	            det1Less2And3Less4);
	      double y = (det(det1And2, y1LessY2,
	            det3And4, y3LessY4) /
	            det1Less2And3Less4);
	      return new Point((int)x,(int)y);
	   }
	   protected static double det(double a, double b, double c, double d) {
	      return a * d - b * c;
	   }
/////////////////////////////////////////////////////////////////
	   @Override
	   public void vratiTouchPolje(int polje){
			if(indikator==125){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(126)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(126));	 
				      
				      FI.br126ToranjMinobacacTNT(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==126){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(127)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(127));	 
				      
				      FI.br127ToranjMinobacacARMOR(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==127){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(128)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(128));	 
				      
				      FI.br128ToranjMinobacacCLUSTER(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
				 else if( GL.getUkupniNovac()>=FI.cijenaObjecta(129)&&polje==4){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(129));	 
				      
				      FI.br129ToranjMinobacacNAPALM(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			//rankivi klustera
			else if(indikator==128){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(130)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(130));	 
				      
				      FI.br130ToranjMinobacacCLUSTER1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==130){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(131)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(131));	 
				      
				      FI.br131ToranjMinobacacCLUSTER2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==131){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(132)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(132));	 
				      
				      FI.br132ToranjMinobacacCLUSTER3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==129){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(133)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(133));	 
				      
				      FI.br133ToranjMinobacacNAPALM1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==133){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(134)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(134));	 
				      
				      FI.br134ToranjMinobacacNAPALM2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			else if(indikator==134){// ako je ova klasa toranj embrio
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(135)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(135));	 
				      
				      FI.br135ToranjMinobacacNAPALM3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      ubijMe();
			       }
			}
			//////////////////
	   }
///// metode od interfejsa gamelogicobject//////////////////////
	public float getXVelUPrikazu(){
		return this.velXUPrik;
	}
	public float getYVelUPrikazu(){
		return this.velYUPrik;
	}
	public int getOblZaKol(){
		return 4;// znaèi da je krug, presjek mi je sirina rec-a
	}
	@Override
	public int getIndikator() {
		// TODO Auto-generated method stub
		return indikator;
	}

	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return xTor;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return yTor;
	}

	@Override
	public float getDx() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getDy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RectF getRect() {
		// TODO Auto-generated method stub
		return rec;
	}
	@Override
	public void maliRun(GameLogic GL,FazeIgre FI, float TpS,boolean pause) {
		// TODO Auto-generated method stub
		this.FI=FI;
		this.GL=GL;
		izborNaMeni=izbor.izbornikNaMeni(this);
		if(tekPoceo){// 
			super.dodajReference(GL,FI);
			stvariKOjeSeIzvrsavajuSamoJedanput();
			tekPoceo=false;
		}
		if(izracunajPauzu){
			vrijemePauze=System.currentTimeMillis()-vrijemePauze;
			izracunajPauzu=false;
		}
		if(ge.bool1=true){
		if(izborNaMeni)izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
		   this.TpS=TpS;
		   ticFire=Math.round(TpS/rateOfFire);
		   listaSudara=null;
		   vrijemeIspucavanja+=vrijemePauze;
		   if(vrijemeIspucavanja+this.RofFMili<System.currentTimeMillis()&& projektil1!=null){
		     listaSudara= GL.provjeriKoliziju(this);
		     tempProtivnik=izvuciObjKojegSeGada();
		          if(tempProtivnik!=null){
		            // if(prosloVrijeme+rateOfFire*1000<System.currentTimeMillis()){
			        
				      ispucajProjektil();
				      ge.indikatorBorbe=2;// znaèi da je ispaljen
				      this.vrijemeIspucavanja=System.currentTimeMillis();
		              iFire=0;
		          
		          }
		          else ge.indikatorBorbe=3;// znaèi da stoji napunjen
		     }
		     //else  ge.indikatorBorbe=0;// znaèi da se ništa ne dogada
		   else{ 
		         if(iFire==0){
		        	 ge.indikatorBorbe=1;// znaèi da se puni, samo kad je nula znaèi samo na poèetku
		         }
		          iFire++;
		       }
		}
		ge.helth=(float) RofFMili;// salje vrijeme izmeðu dva ispaljivanja, pošto se ne koristi helth za tornjeve
		if(!super.jesamLiZiv()) ubijMe();
		posaljiPrikazu();
		 if(pause){
			    izracunajPauzu=true;
				vrijemePauze=System.currentTimeMillis();
			}
			else vrijemePauze=0; 
	}
/////////////////////////////////////////////////////////	
////////metode od interfacea gamelinkerologic////////////
	@Override
	public void GameLinkerIzvrsitelj(GameEvent e) {
		// TODO Auto-generated method stub
		if(e.imTouched){
			izbor.pokreniMojIzbornik(this,izborStanjeNekoristenihBotuna());// ako detektira da je kliknuto na njega pokreæe izbornik
			super.GameLinkerIzvrsitelj(e);
			super.updatajIzbornik();
			}
		if(e.indikator2==1){// ako je prikaz zavrsio sa svojim  namještanjem
			 stvoriProjektilePripadajucegTipa(); 
			 e.indikator2=0;
		}
	}
////////////////////////////////////////////////////////
	@Override
	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
		dvojnik=obj;
	}


}