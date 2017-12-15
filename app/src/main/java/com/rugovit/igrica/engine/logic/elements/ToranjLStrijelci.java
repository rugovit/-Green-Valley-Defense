package com.rugovit.igrica.engine.logic.elements;

import java.util.LinkedList;

import android.graphics.Point;
import android.graphics.RectF;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.SpriteHendler;
import com.rugovit.igrica.engine.ui.UIManagerObject;
import com.rugovit.igrica.engine.ui.elements.IzbornikZaToranj;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;

public class ToranjLStrijelci extends ToranjL implements GameLogicObject {
	private float velXUPrik=0;
	private float velYUPrik=0;
	private double  vrijemeIspucavanja, RofFMili;
	private double vrijemePauze=0;
	private boolean izracunajPauzu=false;
	
	private ProjektilL projektil1;
	private ProjektilL projektil2;
	private GameLogicObject tempProtivnik;
	private GameLogicProtivnik tempStari=null;
	private GameLogicProtivnik tempNovi=null;
	private float xIspaljivanja=0;
	private float yIspaljivanja=0;
	///////////////////
	///////izbornik varijable
	private IzbornikZaToranj izbor;
	
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
	private float rateOfFire;
	private int ticProvSud=0;
	private float xTor,yTor, radijus;
	//private float rTor;
	private RectF rec;
	private Math mat;
	private float TpS=10;// ticks per sec, broj poziva ove klase po sekundi definiro sam ga da je u startu 10 ali to se može promjeniti pri pozivu malog runa
	private int ticFire,iFire=0;// sprema koliko puta se treba pozvati ova klasa prije nego se ispali projektill
	private float xKraj,yKraj; // x i y kraja kojeg se brani
	private float razVrhSlIsp;// razlika vrh wlicice i ispaljivanja
	private GameLogicObject[] listaSudara;// spremaju se objekti sa kojima se sudara
	
 	public ToranjLStrijelci(float xTor, float yTor,float radijus,float RofF,float razVrhSlIsp,IzbornikZaToranj izbor,int indikator){
		super(indikator,izbor,xTor,yTor,radijus);
		this.yTor=yTor;
		this.xTor=xTor;
		this.indikator=indikator;
		this.radijus=radijus;
		this.rateOfFire=RofF;
		 RofFMili=1000/RofF;
		this.izbor=izbor;
		this.razVrhSlIsp=razVrhSlIsp;
		ge=new GameEvent(this);
		objPoPravcima=new GameLogicObject[4];
		rec=new RectF(xTor,yTor,radijus*2+xTor,radijus*2+yTor);// treba mi rect zbog toga što ga zahtjevaju drugi djelovi igrice
		ticFire=1;// ovdje odreðujemm defoltni 
	}
	public void vratiTouchPolje(int polje){
		if(indikator==101){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(102)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(102));	 
			      FI.br102ToranjStrijelciDupli(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==102){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(103)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(103));	 
			      FI.br103ToranjStrijelciTroDupli(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==103){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(104)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(104));	 
			      FI.br104ToranjStrijelciVatreni(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
			 else if( GL.getUkupniNovac()>=FI.cijenaObjecta(105)&&polje==4){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(105));	 
			      FI.br105ToranjStrijelciOtrovni(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==104){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(106)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(106));	 
			      FI.br106ToranjStrijelciVatreni1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==106){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(107)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(107));	 
			      FI.br107ToranjStrijelciVatreni2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==107){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(108)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(108));	 
			      FI.br108ToranjStrijelciVatreni3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==105){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(109)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(109));	 
			      FI.br109ToranjStrijelciOtrovni1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==109){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(110)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(110));	 
			      FI.br110ToranjStrijelciOtrovni2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
		else if(indikator==110){// ako je ova klasa toranj embrio
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(111)&&polje==3){//ALL-AROUND TORANJ 1 RAZINE dugmiæ 1
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(111));	 
			      FI.br111ToranjStrijelciOtrovni3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      ubijMe();
		       }
		}
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
		
		if(indikator==101){ cijenaBotun3=FI.cijenaObjecta(102);
	      }
		else if(indikator==102){ cijenaBotun3=FI.cijenaObjecta(103);
	    }
		else if(indikator==103){ cijenaBotun3=FI.cijenaObjecta(104);
	                    	cijenaBotun4=FI.cijenaObjecta(105);
	      }
		else if(indikator==104){ cijenaBotun3=FI.cijenaObjecta(106);
	      }
		else if(indikator==106){ cijenaBotun3=FI.cijenaObjecta(107);
	      }
		else if(indikator==107){ cijenaBotun3=FI.cijenaObjecta(108);
	      }
		else if(indikator==105){ cijenaBotun3=FI.cijenaObjecta(109);
	      }
		else if(indikator==109){ cijenaBotun3=FI.cijenaObjecta(110);
	      }
		else if(indikator==110){ cijenaBotun3=FI.cijenaObjecta(111);
	      }
	 }
	 public void setPocIspaljivanja(float xIspa, float yIspa){
		 this.xIspaljivanja=xIspa;
		 this.yIspaljivanja=yIspa;
	 }
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
	 /*private void ubijMe(){
		  ge.jesamLiZiv=false;
		  dvojnik.GameLinkerIzvrsitelj(ge);
		  GL.mrtavSam(this);
	 }*/
	 private void ispucajProjektil(){
		// projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik.getRect().centerX(),this.tempProtivnik.getRect().bottom);
		if(projektil1.jesiDosaoNaCilj()) {
			projektil1.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik,true);
		}
		else {
			projektil2.pokreniSePremaCilju(xIspaljivanja,yIspaljivanja,this.tempProtivnik,true);
		}
	 }
	 private void stvariKOjeSeIzvrsavajuSamoJedanput(){// samo æe se izvršit na pocetku ova metoda
		 
		 
		 dodajCijenuBotunima();
		 xIspaljivanja=rec.centerX();
		 yIspaljivanja=rec.centerY();
		 this.vrijemeIspucavanja=System.currentTimeMillis();
		 ////Toranj kasarna 1 razine
		 listaSudara=GL.provjeriKoliziju(this);
		 stvoriProjektilePripadajucegTipa(); 
	 }
	 private void stvoriProjektilePripadajucegTipa(){
		 if(this.indikator==101||this.indikator==102||this.indikator==103){	 
			projektil1 =GL.faIgr.br402ProjektilStrijela(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br402ProjektilStrijela(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp); 
		 }
		 else if( this.indikator==104){
			 projektil1 =GL.faIgr.br406ProjektilStrijelaVatrena(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br406ProjektilStrijelaVatrena(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp); 
		 }
		 else if( this.indikator==105){
			 projektil1 =GL.faIgr.br407ProjektilStrijelaOtrovana(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br407ProjektilStrijelaOtrovana(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp); 
		 }
		 else if( this.indikator==109){
			 projektil1 =GL.faIgr.br418ProjektilStrijelaOtrovanaRank1(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br418ProjektilStrijelaOtrovanaRank1(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		 }
		 else if( this.indikator==110){
			 projektil1 =GL.faIgr.br419ProjektilStrijelaOtrovanaRank2(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br419ProjektilStrijelaOtrovanaRank2(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		 }
		 else if( this.indikator==111){
			 projektil1 =GL.faIgr.br420ProjektilStrijelaOtrovanaRank3(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br420ProjektilStrijelaOtrovanaRank3(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
		 }
		 else if( this.indikator==106){
			 projektil1 =GL.faIgr.br421ProjektilStrijelaVatrenaRank1(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br421ProjektilStrijelaVatrenaRank1(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp); 
		 }
		 else if( this.indikator==107){
			 projektil1 =GL.faIgr.br422ProjektilStrijelaVatrenaRank2(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br422ProjektilStrijelaVatrenaRank2(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp); 
		 }
		 else if( this.indikator==108){
			 projektil1 =GL.faIgr.br423ProjektilStrijelaVatrenaRank3(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp);
			 projektil2 =GL.faIgr.br423ProjektilStrijelaVatrenaRank3(this,rec.centerX(), rec.centerY()-velYUPrik/2+razVrhSlIsp); 
		 }
	 }
	 
	    private void posaljiPrikazu(){
	     	ge.vrijemePauze=(int) vrijemePauze;
	    	dvojnik.GameLinkerIzvrsitelj(ge);
	    	 ge.izbornikUpaljen=izborNaMeni;
	    }

		
		private GameLogicObject izvuciObjKojegSeGada(){
			
			tempStari=null;
			tempNovi=null;
			
	            for(int i=0;listaSudara.length>i;i++){
	            if(listaSudara[i]!=null){ 	
				  if(listaSudara[i].getIndikator()>=-100&&listaSudara[i].getIndikator()<=-1)// provjerava dali je igraè od protivnika
					  tempNovi=(GameLogicProtivnik)listaSudara[i];
				  if(!tempNovi.jesamLiNaInertnomPutu()){	 
					  if(tempStari==null) tempStari=tempNovi;
				       else if(Math.abs(tempNovi.getRedBrPutaNaKojemSi())>Math.abs(tempStari.getRedBrPutaNaKojemSi()))   tempStari=tempNovi;
				         
				       }
				
	              }
			   }
			
			return (GameLogicObject)tempStari;
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
			if(izracunajPauzu){
				vrijemePauze=System.currentTimeMillis()-vrijemePauze;
				izracunajPauzu=false;
			}
			izborNaMeni=izbor.izbornikNaMeni(this);
			if(tekPoceo){// 
				super.dodajReference(GL,FI);
				stvariKOjeSeIzvrsavajuSamoJedanput();
				tekPoceo=false;
			}
			if(ge.bool1=true){
			if(izborNaMeni)izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
			   this.TpS=TpS;
			   ticFire=Math.round(TpS/rateOfFire);
			   listaSudara=null;
			   vrijemeIspucavanja+=vrijemePauze;
			   if(vrijemeIspucavanja+this.RofFMili<System.currentTimeMillis()){
				   listaSudara= GL.provjeriKoliziju(this);
				   tempProtivnik=izvuciObjKojegSeGada();
			          if(tempProtivnik!=null){
			            // if(prosloVrijeme+rateOfFire*1000<System.currentTimeMillis()){
			        	  ge.indikatorBorbe=2;// znaèi da je ispaljen
			        	  posaljiPrikazu();// ovo sam stavio ovdje jer je u izvrsitelju od prkaza metoda koja ce vratiti nazad podatke od pocetku 
					      ispucajProjektil();
					      ge.pomNaX=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
					      ge.pomNaY=tempProtivnik.getRect().centerY();
					      vrijemeIspucavanja=System.currentTimeMillis();
			              iFire=0;
			          
			          }
			          else ge.indikatorBorbe=3;// znaci da nema nikoga
			     }
			     //else  ge.indikatorBorbe=0;// znaèi da se ništa ne dogada
			   else{ 
				  if(this.ticProvSud>=ticFire/4){ listaSudara= GL.provjeriKoliziju(this);
				                                  tempProtivnik=izvuciObjKojegSeGada();
				                                  if(tempProtivnik!=null){
				                            	      ge.pomNaX=tempProtivnik.getRect().centerX();// spremam  ove toèan položajj protivn8ika
				            					      ge.pomNaY=tempProtivnik.getRect().centerY();
				            					      
				                                	  ge.indikatorBorbe=1;// znaèi da cilja
				                                  }
				                                  else ge.indikatorBorbe=3;// znaèi da nema nikoga
				                                  ticProvSud=-1;// nije nula zato što æe ga poslije odmah inkriminirati za jedan
				                                  }
				  else ge.indikatorBorbe=0;// ovo znaèi jednostavno da ema podataka jer se nije oèitao colision
				      ticProvSud++;
			          iFire++;
			       }
			}
			ge.helth=1000/this.rateOfFire;// salje vrijeme izmeðu dva ispaljivanja, pošto se ne koristi helth za tornjeve
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
				//super.GameLinkerIzvrsitelj(e);
			//	super.updatajIzbornik();
				}
		}
	////////////////////////////////////////////////////////
		@Override
		public void setDvojnikaUPrikazu(UIManagerObject obj) {
			// TODO Auto-generated method stub
			dvojnik=obj;
		}

}
