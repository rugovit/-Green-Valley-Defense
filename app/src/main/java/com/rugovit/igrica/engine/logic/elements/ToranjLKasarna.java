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

public class ToranjLKasarna extends ToranjL implements GameLogicObject {
	private boolean pocetnoStvaranjeGotovo=false;
	private boolean izabranVojnik=false;
	private int oblZaKoliziju;
	private float velXUPrik=0;
	private float velYUPrik=0;
	private double prosloVrijeme;
	private double vrijemePauze=0;
	private boolean izracunajPauzu=false;
	//////// varijable od tornja kasarne
	private float xOkupljanja=-1,yOkupljanja=-1;
	private double startTime=0;
	private float secDoNovogVojnika=4;// sekunde do kada æe se gennerirati novi vojnik ako fali koji defolt 10
	private int maxBrVojnika=0;// maksimalni broj vojnika, defolt 4
	private LinkedList<GameLogicBranitelj> listaGrupe;// svaki æe toranj  održavat listu grupe za svoje objekte
	///////////////////
	///////izbornik varijable
	private IzbornikZaToranj izbor;
	
	//////////////////////////
	////zastavice
	private boolean izborNaMeni=false;
	private boolean tekPoceo=true;
	private boolean pocObukeNovogVojnika=false;
	///////
  
    private UIManagerObject dvojnik;
    private GameLogic GL;
	private FazeIgre FI;
	private SpriteHendler sprHendZaProjekt;
	private GameLogicObject[] objPoPravcima; 
	private GameLogicObject maxDolje, maxGore,maxDesno,maxLijevo;
	private int indikator;
	private float xTor,yTor, radijus;
	//private float rTor;
	private float xGrup,yGrup;
	private RectF rec;
	private Math mat;
	private int TpS=10;// ticks per sec, broj poziva ove klase po sekundi definiro sam ga da je u startu 10 ali to se može promjeniti pri pozivu malog runa
	private float xKraj,yKraj; // x i y kraja kojeg se brani
	private GameLogicObject[] listaSudara;// spremaju se objekti sa kojima se sudara
	private boolean jesamLiZiv=true;
	public ToranjLKasarna(float xTor, float yTor,float polumjer,float RofF,IzbornikZaToranj izbor,int indikator){
		super(indikator,izbor,xTor,yTor,polumjer);
		this.yTor=yTor;
		this.xTor=xTor;
		this.indikator=indikator;
		this.radijus=2*polumjer;
		this.izbor=izbor;
		listaGrupe=new LinkedList<GameLogicBranitelj>();
		srediZastavice();
		ge=new GameEvent(this);
		objPoPravcima=new GameLogicObject[4];
		rec=new RectF(xTor,yTor,radijus+xTor,radijus+yTor);// treba mi rect zbog toga što ga zahtjevaju drugi djelovi igrice
		oblZaKoliziju=4;
	}
	private void srediZastavice(){// sa istom klasom æu izraðivat više vrsta tornjeva
         if(indikator==150){
        	
        	 
         }
         
         
		
	}
///////////PUBLIC METODE////////////////////////////////////////////  
	public int getOblZaKol(){
		return oblZaKoliziju;// znaèi da je krug, presjek mi je sirina rec-a
	}
    @Override
    public void uhvatiPozicijuKlikaIzvanIzbornika(float x, float y){
    	posaljiGrupiNovoOdrediste(x,y);
	}
	
////////child za metode
	     public boolean jesamLiZiv(){
	    	 return jesamLiZiv;
	     }
         public void updatajIzbornik(){// sluzi za child da moze pokrenuti izbornik
	      dodajCijenuBotunima();
	      izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
         }
         public void dodajReference(GameLogic GL,FazeIgre FI){// prima game logic referencu od childa
	      this.GL=GL;
	      this.FI=FI;
         }	 
    ///////////        
 public  void vojnikIzabranSam(boolean izabran){
	 izabranVojnik=izabran;
 }      
 public void setXVelUPrik(float xp){
	 velXUPrik=xp;
 }
 public void setYVelUPrik(float yp){
	 velYUPrik=yp;
 }	
 public void izbornikNaMeni(boolean b){
	 izborNaMeni=b;
	 posaljiGrupiDaCrtajuDSuOznaceni(b);
 }	
 public void namjestiOkupljaliste(float x, float y){// kada izlaze iz kasarne gdje æe se okupit
	 xOkupljanja=x;
	 yOkupljanja=y;
 }	
 public void namjestiToranjKasarnu(int brVoj, float sec){// ova æe se metoda koristiti prilikom stvaranja ovog objekta
	 this.maxBrVojnika=brVoj;
	 this.secDoNovogVojnika=sec*1000;
 }	
 /////DODAVANJE
 public void vratiTouchPolje(int polje){// vraæa polje na koje je kliknuto	 
/////////////toranj kasarna  pve razine Test
	 if(indikator==150){
		if(polje==3){
			 if( GL.getUkupniNovac()>=FI.cijenaObjecta(151)){
			      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(151));	 
			      ToranjLKasarna tempKas=FI.br151ToranjKasarna(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
			      tempKas. namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
			    
			      ubijeSveSvojeVojnike();
			      ubijMe();
		       }
		}
		 }
	 else  if(indikator==151){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(152)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(152));	 
				     
				      ToranjLKasarna tempKas= FI.br152ToranjKasarna(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			 }
	 else if(indikator==152){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(153)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(153));	 
				     
				      ToranjLKasarna tempKas= FI.br153ToranjKasarnaGorstak(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			else if(polje==1){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(154)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(154));	 
				     
				      ToranjLKasarna tempKas= FI.br154ToranjKasarnaVitez(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			else if(polje==4){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(155)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(155));	 
				     
				      ToranjLKasarna tempKas= FI.br155ToranjKasarnaStrijelci(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			 }
	 else if(indikator==154){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(156)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(156));	 
				     
				      ToranjLKasarna tempKas= FI.br156ToranjKasarnaVitez1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			 }
	 else if(indikator==156){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(157)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(157));	 
				     
				      ToranjLKasarna tempKas= FI.br157ToranjKasarnaVitez2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			 }
	 else if(indikator==157){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(157)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(157));	 
				     
				      ToranjLKasarna tempKas= FI.br158ToranjKasarnaVitez3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			 }
	 else if(indikator==153){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(159)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(159));	 
				     
				      ToranjLKasarna tempKas= FI.br159ToranjKasarnaGorstak1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
			
			 }
		else if(indikator==159){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(160)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(160));	 
				     
				      ToranjLKasarna tempKas= FI.br160ToranjKasarnaGorstak2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
		 }
		else if(indikator==160){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(161)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(161));	 
				     
				      ToranjLKasarna tempKas= FI.br161ToranjKasarnaGorstak3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
		 }
		else if(indikator==155){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(162)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(162));	 
				     
				      ToranjLKasarna tempKas= FI.br162ToranjKasarnaStrijelci1Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
		 }
		else if(indikator==162){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(163)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(163));	 
				     
				      ToranjLKasarna tempKas= FI.br163ToranjKasarnaStrijelci2Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
		 }
		else if(indikator==163){
			if(polje==3){
				 if( GL.getUkupniNovac()>=FI.cijenaObjecta(164)){
				      GL.dodajNovacPlusMinus(-FI.cijenaObjecta(164));	 
				     
				      ToranjLKasarna tempKas= FI.br164ToranjKasarnaStrijelci3Rank(rec.centerX(),rec.centerY(),this.getNovacZaProdaju());
				      tempKas.namjestiOkupljaliste(this.xOkupljanja,this.yOkupljanja);
				      ubijeSveSvojeVojnike();
				      ubijMe();
			       }
			}
		 }
/////////////////////////////		 

 }	

//////////////////////////////////////////////////////////////////////	
///////privatne metode///////////////////////////////////////7//
 private void posaljiGrupiDaCrtajuDSuOznaceni(boolean daNE){//trebala bi slati svim obektima iz grupe itu naredbu za pommicanje
	    if(listaGrupe!=null)
	  	 {int i=0;
		      while(listaGrupe.size()-1>=i){
		    	  ObjectIgre temp=(ObjectIgre)( listaGrupe.get(i));
		    	  temp.saljiPrikazuDcrtaLikaKaoOznacenog(daNE);
			  i++;
		      }
	  	 }
	}
 private void posaljiGrupiNovoOdrediste(float x, float y){
	    if(listaGrupe!=null){
	  
		 if(listaGrupe.size()>0){   	  ObjectIgre temp=(ObjectIgre)( listaGrupe.get(0));
		    	                          if(temp!=null) temp.obradiPomakNa(x, y);
		                         }
	    }
	}
 @Override
public void ubijMe(){   
	 ubijeSveSvojeVojnike();
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
 /////DODAVANJE
 private void dodajCijenuBotunima(){
	
	if(indikator==150){ cijenaBotun3=FI.cijenaObjecta(151);
    }
	else if(indikator==151){ cijenaBotun3=FI.cijenaObjecta(152);
  }
	else if(indikator==152){
		cijenaBotun1=FI.cijenaObjecta(154);
		cijenaBotun3=FI.cijenaObjecta(153);
		cijenaBotun4=FI.cijenaObjecta(155);  
	}
	else if(indikator==154){ cijenaBotun3=FI.cijenaObjecta(156);}
	else if(indikator==156){ cijenaBotun3=FI.cijenaObjecta(157);}
	else if(indikator==157){ cijenaBotun3=FI.cijenaObjecta(158);}
	else if(indikator==153){ cijenaBotun3=FI.cijenaObjecta(159);}
	else if(indikator==159){ cijenaBotun3=FI.cijenaObjecta(160);}
	else if(indikator==160){ cijenaBotun3=FI.cijenaObjecta(161);}
	else if(indikator==155){ cijenaBotun3=FI.cijenaObjecta(162);}
 }

 private void stvariKOjeSeIzvrsavajuSamoJedanput(){// samo æe se izvršit na pocetku ova metoda
	 dodajCijenuBotunima();
	 prosloVrijeme=System.currentTimeMillis();
	 ////Toranj kasarna 1 razine
	 listaSudara=GL.provjeriKoliziju(this);
	
		 int i=0;
		/* while(listaSudara[i]!=null){
			 if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
				 xOkupljanja= listaSudara[i].getRect().centerX();
				 yOkupljanja=listaSudara[i].getRect().centerY();
				 break;
			 }
			 i++;
		 }*/
		 Point pMinTemp1=null;
		 Point pMinTemp2=null;
		 Point pMin=null;
		 Point pTemp1=null;
		 Point pTemp2=null;
		 double temp1;
		 double temp2;
		 PutL temp;
		 PutL najveci=null;
		 int tempObjZaKol=this.oblZaKoliziju;
		 oblZaKoliziju=2;// pretvaram ga u krug koji traži samo sudar sa cestom

		 listaSudara=this.GL.provjeriKoliziju(this);
		 while(listaSudara[i]!=null){////kvadrant koji je najblizi cilju
			 
			
			 
			 if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
	             temp=(PutL)listaSudara[i];
	             if(najveci==null){//kada tek poème biti æe nula
                     	najveci=temp;            	 
	             }
	             else{
	            	 if(temp.redniBroj()>najveci.redniBroj()){
	            		 najveci=temp;
	            	 }
	             }
		          }
			 i++;
			 if(listaSudara.length<=i) break;
		 }
		 oblZaKoliziju=tempObjZaKol;
		if(najveci!=null&&xOkupljanja==-1&&yOkupljanja==-1){
			xOkupljanja= najveci.getRect().centerX();
		    yOkupljanja=najveci.getRect().centerY();
		}
		/*while(listaSudara[i]!=null){////traži se najbliži kvadrat
			 if(listaSudara[i].getIndikator()>=201&&listaSudara[i].getIndikator()<=300){
				 pTemp1=null;
                 pTemp2=null;
	            //prva vertikalna os kruga tornja  
				pTemp1=getLineLineIntersection(rec.centerX(),rec.centerY(),rec.centerX(),rec.centerY()+50,// gornja stranica kvadrata
						listaSudara[i].getRect().left,listaSudara[i].getRect().top,listaSudara[i].getRect().right,listaSudara[i].getRect().top	);
				pTemp2=getLineLineIntersection(rec.centerX(),rec.centerY(),rec.centerX(),rec.centerY()+50,// donja stranica kvadrata
						listaSudara[i].getRect().left,listaSudara[i].getRect().bottom,listaSudara[i].getRect().right,listaSudara[i].getRect().bottom	);	 
					 
			
			     if(pTemp1!=null){
			    	 if(pTemp2!=null){// ako postoje dvije toèke
			    		temp1= udaljenostDvijeTocke(rec.centerX(),rec.centerY(),pTemp1.x,pTemp1.y);
			    		temp2= udaljenostDvijeTocke(rec.centerX(),rec.centerY(),pTemp2.x,pTemp2.y);
			    		if(temp1<temp2) pMinTemp1=pTemp1;
			    		else pMinTemp1=pTemp2;
			    	     }
			    	 else pMinTemp1=pTemp1;//ako je zakaèio samo jednu stranicu što je nemoguæe ali ipak
			    	 }
			     else if(pTemp2!=null) pMinTemp1=pTemp2;// ako jezakaèio samo drugu toèku što je nemoguæe ali ipak
			 //druga horizontalna os tornja
                     pTemp1=null;
                     pTemp2=null;
					    pTemp1=getLineLineIntersection(rec.centerX(),rec.centerY(),rec.centerX()+50,rec.centerY(),// lijeva stranica kvadrata
								listaSudara[i].getRect().left,listaSudara[i].getRect().top,listaSudara[i].getRect().left,listaSudara[i].getRect().bottom	);
					    pTemp2=getLineLineIntersection(rec.centerX(),rec.centerY(),rec.centerX()+50,rec.centerY(),// desna stranica kvadrata
								listaSudara[i].getRect().right,listaSudara[i].getRect().top,listaSudara[i].getRect().right,listaSudara[i].getRect().bottom	); 	 
					    if(pTemp1!=null){
					    	 if(pTemp2!=null){// ako postoje dvije toèke
					    		temp1= udaljenostDvijeTocke(rec.centerX(),rec.centerY(),pTemp1.x,pTemp1.y);
					    		temp2= udaljenostDvijeTocke(rec.centerX(),rec.centerY(),pTemp2.x,pTemp2.y);
					    		if(temp1<temp2) pMinTemp2=pTemp1;
					    		else pMinTemp2=pTemp2;
					    	     }
					    	 else pMinTemp2=pTemp1;//ako je zakaèio samo jednu stranicu što je nemoguæe ali ipak
					    	 }
					     else if(pTemp2!=null) pMinTemp2=pTemp2;// ako jezakaèio samo drugu toèku što je nemoguæe ali ipak
					    ////sada usporeðuje te dvije (ako postoje) toèke koja je bliza
					    if(pMinTemp1!=null){
					    	 if(pMinTemp2!=null){// ako postoje dvije toèke
					    		temp1= udaljenostDvijeTocke(rec.centerX(),rec.centerY(),pMinTemp1.x,pMinTemp1.y);
					    		temp2= udaljenostDvijeTocke(rec.centerX(),rec.centerY(),pMinTemp2.x,pMinTemp2.y);
					    		if(temp1<temp2) pMin=pMinTemp1;
					    		else pMin=pMinTemp2;
					    	     }
					    	 else pMin=pMinTemp1;
					    	 }
					     else if(pMinTemp2!=null) pMin=pMinTemp2;
		          }
			 i++;
			 if(listaSudara.length<=i) break;
		 }
		 if(rec.centerX()<pMin.x)  this.xOkupljanja=pMin.x+rec.width()/4;// namješta xy okupljanja na novu vrijednos
		 else this.xOkupljanja=pMin.x-rec.width()/4;
		 if(rec.centerY()<pMin.y) this.yOkupljanja=pMin.y+rec.width()/4;// namješta xy okupljanja na novu vrijednost
		 else this.yOkupljanja=pMin.y-rec.width()/4;*/
		
 }
 private void ubijeSveSvojeVojnike(){
	 while(!this.listaGrupe.isEmpty()){
		 ObjectIgre ob=(ObjectIgre)(listaGrupe.getFirst());
		 ob.ubijMe(false);
		 //listaGrupe.removeFirst();
	 }
 }
 private void stvoriNovogVojnikaPripadajuæegTipa(){
	 if(indikator==150){
		 
		
	     GameLogicBranitelj  temp;
         temp=FI.br1VojnikRazina1(this.dvojnik.getGlavniRectPrikaza().centerX(),this.dvojnik.getGlavniRectPrikaza().bottom,this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==151){
	     GameLogicBranitelj  temp;
         temp=FI.br2Branitelj2Razina(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==152){
	     GameLogicBranitelj  temp;
         temp=FI.br3Branitelj3Razina(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==153){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br4BraniteljGorstak(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==154){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br5BraniteljVitez(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==155){//strijelci
	     GameLogicBranitelj  temp;
         temp=FI.br51BraniteljStrijelciIzKasarne(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==156){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br6BraniteljVitezRank1(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==157){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br7BraniteljVitezRank2(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==158){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br8BraniteljVitezRank3(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==159){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br9BraniteljGorstakRank1(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==160){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br10BraniteljGorstakRank2(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==161){//gorstak
	     GameLogicBranitelj  temp;
         temp=FI.br11BraniteljGorstakRank3(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==162){//strijelci
	     GameLogicBranitelj  temp;
         temp=FI.br52BraniteljStrijelciIzKasarneRank1(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==163){//strijelci
	     GameLogicBranitelj  temp;
         temp=FI.br53BraniteljStrijelciIzKasarneRank2(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 else if(indikator==164){//strijelci
	     GameLogicBranitelj  temp;
         temp=FI.br54BraniteljStrijelciIzKasarneRank3(rec.centerX(),rec.centerY(),this);// stvara novog vojnika
         ObjectIgre obj=(ObjectIgre)temp;
         obj.setGameLogicReferencu(GL);
	     listaGrupe.add( temp);// stavlja ga u grupu
         temp.setGrupniXY(xOkupljanja, yOkupljanja);// daje mu cilj za iæi
         temp.setGrupnuListu(listaGrupe);//salje mu referencu grupe
         temp.jaSamTeStvorio(this);
	 }
	 
 }
 private void stvoriNovogVojnikaAkojeVrijeme888(){
	 if(listaGrupe.size()<this.maxBrVojnika){
		  if(pocObukeNovogVojnika==false){
			  startTime= System.currentTimeMillis();
			  pocObukeNovogVojnika=true;
		  }
		  else {
			  if(startTime+secDoNovogVojnika+this.vrijemePauze<System.currentTimeMillis()){
				  stvoriNovogVojnikaPripadajuæegTipa();
				  pocObukeNovogVojnika=false;
			  }
		  

		 }
	 } 
 }
    private void posaljiPorukuPrikazu(){
    	ge.x2=this.xOkupljanja;
    	ge.y2=this.yOkupljanja;
    	dvojnik.GameLinkerIzvrsitelj(ge);
    	 ge.izbornikUpaljen=izborNaMeni||izabranVojnik;
    }
	private void izvuciMaxPoSmijerovima(){
		int i=0;
		maxDolje=maxGore=maxDesno=maxLijevo=null;// poništava sve iz prošlog pozivanja
		while(listaSudara[i]!=null){
			if(listaSudara[i].getIndikator()>=-100&&listaSudara[i].getIndikator()<=-1)// provjerava dali je igraè od protivnika
			{
				if(listaSudara[i].getDy()>0){
				    if(maxDolje==null) maxDolje=listaSudara[i];
				    else{// ako je od novoga y veæi nego li je od staroga sprema se na njegovo mjesto novi
					if(listaSudara[i].getY()>maxDolje.getY()) maxDolje=listaSudara[i];
				    }
			     }
			     else if(listaSudara[i].getDy()<0){// prema gore
				       if(maxGore==null) maxGore=listaSudara[i];
				       else{// ako je od novoga y manji nego li je od staroga sprema se na njegovo mjesto novi
				       if(listaSudara[i].getY()<maxGore.getY()) maxGore=listaSudara[i];
				       }
		       	}
			    else{// u sluèaju da je nula provjeravam dali se kreæe lijevo ili desno
				    if(listaSudara[i].getDx()<0){
					   if(maxLijevo==null) maxLijevo=listaSudara[i];
					   else{// ako je od novoga x veæi nego li je od staroga sprema se na njegovo mjesto novi
						   if(listaSudara[i].getX()>maxLijevo.getX()) maxLijevo=listaSudara[i];
					       }
				         }
				    else if(listaSudara[i].getDy()>0){// prema gore
					   if(maxDesno==null) maxDesno=listaSudara[i];
					   else{// ako je od novoga x veæi nego li je od staroga sprema se na njegovo mjesto novi
						   if(listaSudara[i].getX()<maxDesno.getX()) maxDesno=listaSudara[i];
					       }
					   }
			         }
			      }
			i++;
			if(listaSudara.length<=i) break;
			
		}
		objPoPravcima[0]=maxDolje;
		objPoPravcima[1]=maxGore;
		objPoPravcima[2]=maxDesno;
		objPoPravcima[3]=maxLijevo;
	}
	

//neke èesto korištene metode
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
		return 0;
	}
	public float getYVelUPrikazu(){
		return 0;
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
		
		 posaljiGrupiDaCrtajuDSuOznaceni(this.izborNaMeni);
		izborNaMeni=izbor.izbornikNaMeni(this);
		if(tekPoceo){// 
			stvariKOjeSeIzvrsavajuSamoJedanput();
			tekPoceo=false;
		}
		if(!pocetnoStvaranjeGotovo&&this.ge.bool1){
			int  i=0;
			 while(i<maxBrVojnika){// stvara toliko objekata koliko je navedeno u stvaranju objekkta tornja
				     stvoriNovogVojnikaPripadajuæegTipa();
			         i++;
			         }
			 pocetnoStvaranjeGotovo=true;
			}
		if(izborNaMeni)izbor.izmjenilistuNekoristenih(izborStanjeNekoristenihBotuna());
	
	    stvoriNovogVojnikaAkojeVrijeme888();
		
		posaljiPorukuPrikazu();
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
			posaljiGrupiDaCrtajuDSuOznaceni(true);
			izbor.pokreniMojIzbornik(this,izborStanjeNekoristenihBotuna());// ako detektira da je kliknuto na njega pokreæe izbornik
		    izborNaMeni=true;
		}
	}
////////////////////////////////////////////////////////
	@Override
	public void setDvojnikaUPrikazu(UIManagerObject obj) {
		// TODO Auto-generated method stub
		dvojnik=obj;
	}



}
