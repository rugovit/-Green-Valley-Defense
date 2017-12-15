package com.rugovit.igrica.engine.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.SoundPool;
import android.text.TextPaint;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.engine.ui.levels.FazeIgre;


public class SpriteHendler implements Cloneable {
	
/////////varijable samostojece
	private  HashMap<Integer,Float> sansaZaZvukSamostalni;// kolika je sansa da se odreðeni zvuk uopæe pusti
	private HashMap<Integer,Float>randomPojacanjeDoSamostalni;//random izmeðu vrijednosti spremljene u pojaèanju i  ove rijednost
	private  HashMap<Integer,Integer> zvukListaSamostalni;// sprema kodove od soundpoola
	private  HashMap<Integer,Integer>  listaPrioritetaSamostalni;
    private  HashMap<Integer,Float> pojacanjeSamostalni;
	
	
private LinkedList<Integer>	listaInternihAnimKodovi;
private LinkedList<int[]>	listaInternihAnimSlike;
private LinkedList<int[]>	listaInternihAnimRedci;
private LinkedList<int[]>	listaInternihAnimStupci;
private boolean tekPoceoSaPunjenjemInternihAnimacija=true;
private LinkedList<int[]>	listaInternihAnimVremena;


private int[]	internaAnimRedci;
///varijable za gotove animacije

static private  int maxBrOcekStuIRed=20;
private boolean zavrJedALfaKask[][];	
private boolean smanjenje=false, povecanje=false;	
private int tick=0;
private int tempBrTaktova=-100;
private int[][] tempBrStupca;	
private RectF tempRect;
private double vrijemePocetkaAlfaReda[][];
private boolean tekPoceoAlfaReda[][];
private int staoNaSliciciAlfaReda[][];
private Paint paintZaAlfuReda;

private double vrijemePocetkaAlfa[][][];
private boolean tekPoceoAlfa[][][];

private double vrijemePocetkaSvitak[][];
private boolean tekPoceoSvitak[][];

private double vrijemePocetkaTranslacije[][];
private boolean tekPoceoTranslacijom[][];

private double vrijemePocetkaRotacije[][];
private boolean tekPoceoRotacijom[][];

private double vrijemePocetkaUvecanja[][];
private boolean tekPoceoUvecanja[][];

private float tempUkupnaTranslacijaZaX;
private float tempUkupnaTranslacijaZaY;
private float tempUkupnoPovZaX;
private float tempUkupnoPovZaY;
private float tempUkupniDeltaKut;
private float tempKutZaPomaknut[][];
private float tempUkupniDXRazm;
private float tempUkupniDYRazm;
private float tempUkupniDXSlikeRazm;
private float tempUkupniDYSlikeRazm;
private boolean transXGotova=false,transYGotova=false;
private boolean povXGotova=false,povYGotova=false;
/////////////////////////
private Random generator;
private float tempPojacanje;
private int tempZvuk;
private int tempKodZvuka;

private int sansaZaZvuk[];// kolika je sansa da se odreðeni zvuk uopæe pusti
private float randomPojacanjeDo[];//random izmeðu vrijednosti spremljene u pojaèanju i  ove rijednost
private int  zvukLista[][][];// povezuje kodove iz listeKodova sa svakom slièicom od svake veæe slike u sprite manageru
private int  listaKodova[];// sprema kodove od soundpoola
private int listaPrioriteta[];
private boolean zvukListaPostavljena=false;
public SoundPool soundPool;
private int zadnjaSlicicaZbroj[][];// da se zvuk ne ponavlja više puta ako se ista slièica iscrtava, ovaj broj ce biti zbroj slicice retka i stupca
private int zadnjiZvuk=1; // pocinje od 1 zato što je 0 rezervirana kao oznaka da je prazna pozicija
private int zadnjaListaZvuka=-1;//služi za spremanje položaja u listilistakodova liste zvukova  isto kao i za pojedinaène zvukove samo što æe biti u minusu
private int[][] listaListaKodova;
private int tempKodListeListaKodova;// ovdje ce se spremati random izvuèeni kod liz listelistakodova, kažu da je brže kada funkcija nevraæa ngo se sprema u varijablu
private float pojacanje[];
private int redBrDodat[];// redni broj odakle poèinje dodatak 	
private int krajReda=0;// kraj liste bitmapova, sve liste imaju ovu varijablu jer tj. iste su velièine 	
private Bitmap slike[]; // spremat æe slike
private float sirinaSlike[];// sprema širinu slike
private float visinaSlike[];// sprema visinu slike
private int brojRedova[];
private Rect rec;
private Paint aliasedPaint;
private int brojBitmapova;
private int brojStupaca[];//broj redova i stupaca na veæoj slici gdje se nalaze manje 
private int brojStupacaDod[];//broj redova i stupaca na veæoj slici gdje se nalaze manje,dodatni  
private float sirManjeSlike[];
private float sirManjeSlikeDod[];
private float visManjeSlike[];// sirina i visina manjeih slièica unautar veæe slike, ne unosi se posebno nego se izraèunava
private float prikPoSec[];// odreðuje koliko æe se odreðena slika prikazat puta u sekundi
private boolean recyclePozvan=false;
private boolean dodatniStupci[];
public SpriteHendler(int maxBrojBitmapova){ // broj slika koji se oèekuje u klasi
	slike= new Bitmap[maxBrojBitmapova];
	generator=new Random();
	sirinaSlike= new float[maxBrojBitmapova];
	visinaSlike=new float[maxBrojBitmapova];
	brojRedova=new int[maxBrojBitmapova];
	brojStupaca=new int[maxBrojBitmapova];
	brojStupacaDod=new int[maxBrojBitmapova];
	sirManjeSlike=new float[maxBrojBitmapova];
	sirManjeSlikeDod=new float[maxBrojBitmapova];
	visManjeSlike=new float[maxBrojBitmapova];
	prikPoSec=new float[maxBrojBitmapova];
	redBrDodat=new int[maxBrojBitmapova];
	
	
	
	
	
	
	
	
	
	
	
	
	rec=new Rect();
	zadnjaSlicicaZbroj=new int[maxBrojBitmapova][maxBrOcekStuIRed];
	tempKutZaPomaknut=new float[maxBrojBitmapova][maxBrOcekStuIRed];
	tempBrStupca=new int[maxBrojBitmapova][maxBrOcekStuIRed];
	for(int i=0; i<tempBrStupca.length;i++){
		for(int j=0; j<tempBrStupca[i].length;j++){
			tempBrStupca[i][j]=-1;
		}
	}
	this.brojBitmapova=maxBrojBitmapova;
	 aliasedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	 dodatniStupci=new boolean[maxBrojBitmapova];
	 tempRect=new RectF();
}
public SpriteHendler(int maxBrojBitmapova,int maxBrojZvukova,SoundPool soundPool){ // broj slika koji se oèekuje u klasi
	this.soundPool=soundPool;
	generator=new Random();
	zvukLista= new int[maxBrojBitmapova][maxBrOcekStuIRed][maxBrOcekStuIRed];
	for(int i=0; i<	zvukLista.length;i++){
		for(int j=0; j<	zvukLista[i].length;j++){
			for(int k=0; k<	zvukLista[i][j].length;k++){
				zvukLista[i][j][k]=-1;
			}
		}
	}
	slike= new Bitmap[maxBrojBitmapova];
	sirinaSlike= new float[maxBrojBitmapova];
	visinaSlike=new float[maxBrojBitmapova];
	brojRedova=new int[maxBrojBitmapova];
	brojStupaca=new int[maxBrojBitmapova];
	brojStupacaDod=new int[maxBrojBitmapova];
	sirManjeSlike=new float[maxBrojBitmapova];
	sirManjeSlikeDod=new float[maxBrojBitmapova];
	visManjeSlike=new float[maxBrojBitmapova];
	prikPoSec=new float[maxBrojBitmapova];
	zadnjaSlicicaZbroj=new int[maxBrojBitmapova][maxBrOcekStuIRed];
	rec=new Rect();
	tekPoceoSvitak=new boolean[maxBrojBitmapova][maxBrOcekStuIRed];
	
	
	
	
	
	
	
	tempKutZaPomaknut=new float[maxBrojBitmapova][maxBrOcekStuIRed];
	tempBrStupca=new int[maxBrojBitmapova][maxBrOcekStuIRed];
	for(int i=0; i<tempBrStupca.length;i++){
		for(int j=0; j<tempBrStupca[i].length;j++){
			tempBrStupca[i][j]=-1;
		}
	}
	

	
	pojacanje= new float[maxBrojZvukova+1];
	sansaZaZvuk=new int[maxBrojZvukova+1];
	randomPojacanjeDo=new float[maxBrojZvukova+1];
	listaKodova= new int[maxBrojZvukova+1];
	redBrDodat=new int[maxBrojBitmapova];
	listaPrioriteta=new int[maxBrojZvukova+1];
	aliasedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	listaListaKodova= new int[maxBrojZvukova+1][];
	this.brojBitmapova=maxBrojBitmapova;
	dodatniStupci=new boolean[maxBrojBitmapova];
	tempRect=new RectF();
}
//////////////////////PRIVATNE METODE
private void vratiRandomKodZvukaIzListeListaKodova(int polozajUListi){
	int velListe=listaListaKodova[polozajUListi].length;
	tempKodListeListaKodova=listaListaKodova[polozajUListi][generator.nextInt(velListe)];
}
private void pustiZvuk(int brSl,int brRed, int brSlic){
if(zadnjaSlicicaZbroj[brSl][brRed]!=brSl+brSlic+brRed){	
      if(zvukLista!=null) if(zvukLista[brSl][brRed][brSlic]>=0){ 
		 
			 tempZvuk=zvukLista[brSl][brRed][brSlic];	  
	   if(tempZvuk>0) {		
		   tempKodZvuka=this.listaKodova[tempZvuk];
		   if(tempKodZvuka<0) {// ako je u pitanju cijela lista a ne pojedinaèni zvuk
			   vratiRandomKodZvukaIzListeListaKodova(-tempKodZvuka);
			   tempKodZvuka=tempKodListeListaKodova;
		   }
		   if(this.sansaZaZvuk[tempZvuk]!=0) {//ako uopæe postoji vrijednost za to
			  if(generator.nextInt(100)<this.sansaZaZvuk[tempZvuk]){
			    if(this.randomPojacanjeDo[tempZvuk]!=0){
			    	tempPojacanje=pojacanje[tempZvuk]-(pojacanje[tempZvuk]/100)*generator.nextInt((int)((100-randomPojacanjeDo[tempZvuk])));
			    	  int i=0;
					    i=this.tick;
			    
			    }
			    else tempPojacanje=pojacanje[tempZvuk];	
			    
			    if(FazeIgre.listaSvikPustenihZvukova!=null){
			    	FazeIgre.listaSvikPustenihZvukova.add(soundPool.play(tempKodZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, listaPrioriteta[tempZvuk], 0, 1f));
			      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
			    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
			      }
			    }
			    else soundPool.play(tempKodZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, listaPrioriteta[tempZvuk], 0, 1f);
			  }
		     }
			else {
				 if(this.randomPojacanjeDo[tempZvuk]!=0){
					 tempPojacanje=pojacanje[tempZvuk]-(pojacanje[tempZvuk]/100)*generator.nextInt((int)((100-randomPojacanjeDo[tempZvuk])));
					  int i=0;
					    i=this.tick;
				 
				 }
				 else tempPojacanje=pojacanje[tempZvuk];
				 if(FazeIgre.listaSvikPustenihZvukova!=null){
				 FazeIgre.listaSvikPustenihZvukova.add(soundPool.play(tempKodZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka,tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, listaPrioriteta[tempZvuk], 0, 1f));
			      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
			    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
			      }
				 }
				 else {
					 soundPool.play(tempKodZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka,tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, listaPrioriteta[tempZvuk], 0, 1f);
				 }
			}
	    }
	
   	}
  }
 zadnjaSlicicaZbroj[brSl][brRed]=brSl+brSlic+brRed;// zbraja i tako stvara broj koji je razlièit od slièice do slièice
}
///////////////
//////////////////////PUBLIC METODE
public boolean vrtiAnimacijuRedaUnatrag(Canvas can, GameEvent ge, int brSl, int  brReda, RectF recCrt, Paint p){
	boolean b=false;
	if(ge.jesamLiZiv){//
		ge.indikator=this.brojStupaca(brSl)-1;
		ge.vrijeme1=System.currentTimeMillis();
		ge.jesamLiZiv=false;
		//ge.indikator=0;
		
	}
	if(System.currentTimeMillis()>1000/brojPrikazaPoSekundi(brSl)*(this.brojStupaca(brSl)-ge.indikator)+ge.vrijeme1){
		ge.indikator--;
		if(ge.indikator<0){
			nacrtajSprite(can, brSl, 0,brReda,recCrt,  p);
			ge.jesamLiZiv=true;
			ge.indikator=this.brojStupaca(brSl)-1;
			b=true;
		}
		else nacrtajSprite(can, brSl, ge.indikator,brReda,recCrt,  p);
	}
	else{
		 nacrtajSprite(can, brSl, ge.indikator,brReda,recCrt,  p);
	}
	return b;
} 
public boolean vrtiAnimacijuReda(Canvas can,GameEvent ge, int brSl,int  brReda, RectF recCrt, Paint p){
	boolean b=false;
	if(ge.jesamLiZiv){//
		ge.indikator=0;
		ge.vrijeme1=System.currentTimeMillis();
		ge.jesamLiZiv=false;
		//ge.indikator=0;
		
	}
	if(System.currentTimeMillis()>1000/brojPrikazaPoSekundi(brSl)*(ge.indikator+1)+ge.vrijeme1){
		ge.indikator++;
		if(ge.indikator>=this.brojStupaca(brSl)){
			nacrtajSprite(can, brSl, --ge.indikator,brReda,recCrt,  p);
			ge.jesamLiZiv=true;
			ge.indikator=0;
			b=true;
		}
		else nacrtajSprite(can, brSl, ge.indikator,brReda,recCrt,  p);
	}
	else{
		 nacrtajSprite(can, brSl, ge.indikator,brReda,recCrt,  p);
	}
	return b;
} 
public float getSirRezanja(int brSlike){
	if(sirManjeSlike.length-1>=brSlike){
	return sirManjeSlike[brSlike];
	}
	else return -1;
}
public float getVisRezanja(int brSlike){
	if(visManjeSlike.length-1>=brSlike){
	return visManjeSlike[brSlike];
	}
	else return -1;
}
public int getRedniBrojKoda(int kod){
	return listaInternihAnimKodovi.indexOf(kod);
}
public void setInternuAnimaciju(int kodAnimacije,int[] listaSlike,int[] listaRedka,int[] listaStupca, int[] listaVremena ){
	if(tekPoceoSaPunjenjemInternihAnimacija){
		listaInternihAnimSlike=new LinkedList<int[]>();
		listaInternihAnimRedci=new LinkedList<int[]>();
		listaInternihAnimStupci=new LinkedList<int[]>();
		listaInternihAnimVremena=new LinkedList<int[]>();
		tekPoceoSaPunjenjemInternihAnimacija=false;
	}
	listaInternihAnimSlike.add(listaSlike);
	listaInternihAnimRedci.add(listaRedka);
	listaInternihAnimStupci.add(listaStupca);
	listaVremena =obradiListuVremena(listaVremena );
	listaInternihAnimVremena.add(listaVremena);
} 
public boolean nacrtajInternuAnimaciju(int redniBrojInterneAnimacije,GameEvent geInt,Canvas can,float kut,RectF rectCrt,Paint p){// samo iscrtava rect koji mu se posalje i pazi da se naimacija pravilno iscrtava
    boolean b=false;
	if(geInt.jesamLiZiv){
		geInt.indikator=0;
		geInt.vrijeme1=System.currentTimeMillis();
		geInt.jesamLiZiv=false;
	}
	///////////zbrajanje otklona pauze
	geInt.vrijeme1+=geInt.vrijemePauze;
	///////////uvitavanje listi
	int[] slikee=listaInternihAnimSlike.get(redniBrojInterneAnimacije);
	int[] redci=listaInternihAnimRedci.get(redniBrojInterneAnimacije);
	int[] stupci=listaInternihAnimStupci.get(redniBrojInterneAnimacije);
	int[] vrijeme=listaInternihAnimVremena.get(redniBrojInterneAnimacije);
    ///////////odredivanje slicice
	
	/////////////////////////////////
	if(!recyclePozvan){
		  can.save();
		  if(kut!=0)can.rotate(-kut,rectCrt.centerX(), rectCrt.centerY());
		  rec.set(Math.round(sirManjeSlike[slikee[geInt.indikator]]*stupci[geInt.indikator]),Math.round(visManjeSlike[slikee[geInt.indikator]]*(redci[geInt.indikator])),Math.round(sirManjeSlike[slikee[geInt.indikator]]*(stupci[geInt.indikator]+1)),Math.round(visManjeSlike[slikee[geInt.indikator]]*(redci[geInt.indikator]+1)));
		  //Rect r=new Rect(0,0,150,100);
		  if(!recyclePozvan&&slike[slikee[geInt.indikator]]!=null)can.drawBitmap(slike[slikee[geInt.indikator]],rec,rectCrt,p);
		  can.restore();
		  pustiZvuk(slikee[geInt.indikator], redci[geInt.indikator],  stupci[geInt.indikator]);
		}
	if(vrijeme[geInt.indikator]+geInt.vrijeme1<System.currentTimeMillis()){
		geInt.indikator++;
	}
	if(geInt.indikator>=vrijeme.length){
		geInt.indikator=0;
		b=true;
		geInt.jesamLiZiv=true;
	}
 
 return b;//
}
private int[] obradiListuVremena(int[] listaVremena){
	int[] listaInkriminirana=new int[listaVremena.length];
	int zbroj=0;
	for(int i=0 ;i<listaVremena.length;i++){
		zbroj+=listaVremena[i];
		listaInkriminirana[i]=zbroj;
	}
	
	
	return listaInkriminirana;
}
/////bilder za zvuk
public void nacrtajTextUnutarPravokutnika(float pozX,float pozY,float sir,float vis, TextPaint p, String text,Canvas can){
	int brClanova=text.length();
	////
	TextPaint tp=new TextPaint();
	if(p!=null)tp.set(p);
	Rect recText=new Rect();
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
public void dodajZvukISincSaSl(int kodZvuka,int brSlike,int brReda, int brSlURedu,float pojacanje,int prioritet){// lodira zvuk i odreduje kada ce se pustiti

	listaPrioriteta[zadnjiZvuk]=prioritet;
	zvukLista[brSlike][brReda][brSlURedu]=zadnjiZvuk;
	listaKodova[zadnjiZvuk]=kodZvuka;
	this.pojacanje[zadnjiZvuk]=pojacanje;
	zadnjiZvuk++;
}
public void dodajZvukISincSaSlRandom(int kodZvuka,int brSlike,int brReda, int brSlURedu,float pojacanje,int prioritet,int sansaZaZvuk, float randomPojacanjeOdGlavnogPosto){// lodira zvuk i odreduje kada ce se pustiti


	listaPrioriteta[zadnjiZvuk]=prioritet;
	zvukLista[brSlike][brReda][brSlURedu]=zadnjiZvuk;
	listaKodova[zadnjiZvuk]=kodZvuka;
	this.pojacanje[zadnjiZvuk]=pojacanje;
	/////sansa za zvuk
	if(sansaZaZvuk<100&&sansaZaZvuk>0)this.sansaZaZvuk[zadnjiZvuk]=sansaZaZvuk;
	else this.sansaZaZvuk[zadnjiZvuk]=0;//znaèi da je iskljuèen
	////random pojacnje do
	 if(randomPojacanjeOdGlavnogPosto>0&&randomPojacanjeOdGlavnogPosto<=100){
		 this.randomPojacanjeDo[zadnjiZvuk]=randomPojacanjeOdGlavnogPosto;
	 }
		 else randomPojacanjeDo[zadnjiZvuk]=0;// oznacava da je iskljuèeno
	 
	zadnjiZvuk++;
}
public void dodajZvukISincSaSlRandomSamostalni(int kodZvuka,int komanda,float pojacanje,int prioritet,float sansaZaZvuk, float randomPojacanjeOdGlavnogPosto){// lodira zvuk i odreduje kada ce se pustiti
	if( randomPojacanjeDoSamostalni==null){
	sansaZaZvukSamostalni  = new HashMap<Integer,Float>();// kolika je sansa da se odreðeni zvuk uopæe pusti
	 randomPojacanjeDoSamostalni =new  HashMap<Integer,Float>();//random izmeðu vrijednosti spremljene u pojaèanju i  ove rijednost
	 zvukListaSamostalni= new HashMap<Integer,Integer>();// sprema kodove od soundpoola
	 listaPrioritetaSamostalni = new HashMap<Integer,Integer>() ;
	 pojacanjeSamostalni=new HashMap<Integer,Float>();
   }

	listaPrioritetaSamostalni.put(komanda, prioritet);
	zvukListaSamostalni.put(komanda,kodZvuka);

	this.pojacanjeSamostalni.put(komanda,pojacanje);
	/////sansa za zvuk
	if(sansaZaZvuk<100&&sansaZaZvuk>0)this.sansaZaZvukSamostalni.put(komanda,sansaZaZvuk);
	else sansaZaZvukSamostalni.put(komanda,sansaZaZvuk);//znaèi da je iskljuèen
	////random pojacnje do
	 if(randomPojacanjeOdGlavnogPosto>0&&randomPojacanjeOdGlavnogPosto<=100){
		 this.randomPojacanjeDoSamostalni.put(komanda, randomPojacanjeOdGlavnogPosto);
	 }
		 else randomPojacanjeDoSamostalni.put(komanda, randomPojacanjeOdGlavnogPosto);// oznacava da je iskljuèeno

}
public void pustiZvukSamostalno(int komanda){

      if(zvukListaSamostalni!=null) if(zvukListaSamostalni.containsKey(komanda)){ 
		 
    	   tempKodZvuka=zvukListaSamostalni.get(komanda);	  
	 

		   
			  if(generator.nextInt(100)<sansaZaZvukSamostalni.get(komanda)){
			    if(this.randomPojacanjeDoSamostalni.get(komanda)>=0&&this.randomPojacanjeDoSamostalni.get(komanda)<100){
			    	tempPojacanje=pojacanjeSamostalni.get(komanda)-(pojacanjeSamostalni.get(komanda)/100)*generator.nextInt((int)((100-randomPojacanjeDoSamostalni.get(komanda))));
			    	  int i=0;
					    i=this.tick;
			    
			    }
	
			    else tempPojacanje=pojacanjeSamostalni.get(komanda);	
			    
			    if(FazeIgre.listaSvikPustenihZvukova!=null){
			    	FazeIgre.listaSvikPustenihZvukova.add(soundPool.play(tempKodZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, listaPrioritetaSamostalni.get(komanda), 0, 1f));
			      if(IgricaActivity.maxBrojZvukova*2<   FazeIgre.listaSvikPustenihZvukova.size()){
			    	  FazeIgre.listaSvikPustenihZvukova.removeFirst();
			      }
			    }
			    else soundPool.play(tempKodZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, tempPojacanje*FazeIgre.koeficijentPojacanjaZvuka, listaPrioritetaSamostalni.get(komanda), 0, 1f);
			  }
		     
              }
	    
	
   	}
  
 

public void dodajZvukISincSaSlRandom(int[] kodZvuka,int brSlike,int brReda, int brSlURedu,float pojacanje,int prioritet,int sansaZaZvuk, float randomPojacanjeOdGlavnogPosto){// lodira zvuk i odreduje kada ce se pustiti

	listaPrioriteta[zadnjiZvuk]=prioritet;
	zvukLista[brSlike][brReda][brSlURedu]=zadnjiZvuk;
	listaListaKodova[-zadnjaListaZvuka]=kodZvuka;// stavljam minus pošto je varijabla vec u minusu tako da dobijem pozitivnu vrijednost
	listaKodova[zadnjiZvuk]=zadnjaListaZvuka;// sperema poziciju listezvukova u listiu lista zvukova
	this.pojacanje[zadnjiZvuk]=pojacanje;
	/////sansa za zvuk
	if(sansaZaZvuk<100&&sansaZaZvuk>0)this.sansaZaZvuk[zadnjiZvuk]=sansaZaZvuk;
	else this.sansaZaZvuk[zadnjiZvuk]=0;//znaèi da je iskljuèen
	////random pojacnje do
	 if(randomPojacanjeOdGlavnogPosto>0&&randomPojacanjeOdGlavnogPosto<=100){
		 this.randomPojacanjeDo[zadnjiZvuk]=randomPojacanjeOdGlavnogPosto;
	 }
		 else randomPojacanjeDo[zadnjiZvuk]=0;// oznacava da je iskljuèeno
	 
	zadnjiZvuk++;
	zadnjaListaZvuka--;
}
////////////////////////////////

public void dodajNoviSprite(Bitmap b,int brS,int brS2,float indexDodStup, int brR,int redBrDod, float brPriPoSec){	
    slike[krajReda]=b; 
    dodatniStupci[krajReda]=true;
    if(b!=null){  
    	 sirinaSlike[krajReda]=b.getWidth();
         visinaSlike[krajReda]=b.getHeight();
      } 
    prikPoSec[krajReda]=brPriPoSec;
  // iz ovih vrijednosti dobiva se visina i sirina mannjih slika
    if(brS==0) brS=1;
    if(brR==0) brR=1;
    redBrDodat[krajReda]=redBrDod;
    brojRedova[krajReda]=brR;
    brojStupaca[krajReda]=brS;
    brojStupacaDod[krajReda]=brS2;
    if(b!=null){ 
      sirManjeSlike[krajReda]=(float)b.getWidth()/brS;
      sirManjeSlikeDod[krajReda]=((float)b.getWidth()/brS)*indexDodStup;// indexdodatstupca je odnos izmeðu dodatne slike i normalne slike
      visManjeSlike[krajReda]=(float)b.getHeight()/brR;
     }
    krajReda++;// inkriminira se radi sljedeæe slke koja se ubacuje
  
}
public void dodajNoviSprite(Bitmap b, int brS, int brR, float brPriPoSec){	
    slike[krajReda]=b;
   if(b!=null){ 
    sirinaSlike[krajReda]=b.getWidth();
    visinaSlike[krajReda]=b.getHeight();
    }
    prikPoSec[krajReda]=brPriPoSec;
  // iz ovih vrijednosti dobiva se visina i sirina mannjih slika
    if(brS==0) brS=1;
    if(brR==0) brR=1;
    brojRedova[krajReda]=brR;
    brojStupaca[krajReda]=brS;
    if(b!=null){ 
    	 sirManjeSlike[krajReda]=(float)b.getWidth()/brS;
         visManjeSlike[krajReda]=(float)b.getHeight()/brR;
      }
    krajReda++;// inkriminira se radi sljedeæe slke koja se ubacuje
  
}
public void nacrtajSprite(Canvas can,int brSl, int brS, int brR, RectF rectCrt){
	//rec.set(sirManjeSlike[brSl]*brS,visManjeSlike[brSl]*(brR),sirManjeSlike[brSl]*(brS+1),visManjeSlike[brSl]*(brR+1));
	rec.set(Math.round(sirManjeSlike[brSl]*brS),
			Math.round(visManjeSlike[brSl]*(brR)),
		    Math.round(sirManjeSlike[brSl]*(brS+1)),
			Math.round(visManjeSlike[brSl]*(brR+1)));
	//Rect r=new Rect(0,0,150,100);
	if(!recyclePozvan&&slike[brSl]!=null){can.drawBitmap(slike[brSl],rec,rectCrt,aliasedPaint);
	                     pustiZvuk( brSl, brR,  brS);
	                   }
}
public void nacrtajSprite(Canvas can,int brSl, int brS, int brR, RectF rectCrt,Paint p){
	rec.set(Math.round(sirManjeSlike[brSl]*brS),Math.round(visManjeSlike[brSl]*(brR)),Math.round(sirManjeSlike[brSl]*(brS+1)),Math.round(visManjeSlike[brSl]*(brR+1)));
	//Rect r=new Rect(0,0,150,100);
	if(!recyclePozvan&&slike[brSl]!=null){can.drawBitmap(slike[brSl],rec,rectCrt,p);
	pustiZvuk( brSl, brR,  brS);}
	
}
public void nacrtajSpriteRotiran(Canvas can,int brSl, int brS, int brR, RectF rectCrt,float kut,Paint p){
	if(!recyclePozvan){
	  can.save();
	  if(kut!=0)can.rotate(-kut,rectCrt.centerX(), rectCrt.centerY());
	  rec.set(Math.round(sirManjeSlike[brSl]*brS),Math.round(visManjeSlike[brSl]*(brR)),Math.round(sirManjeSlike[brSl]*(brS+1)),Math.round(visManjeSlike[brSl]*(brR+1)));
	  //Rect r=new Rect(0,0,150,100);
	  if(!recyclePozvan&&slike[brSl]!=null)can.drawBitmap(slike[brSl],rec,rectCrt,p);
	  can.restore();
	  pustiZvuk( brSl, brR,  brS);
	}
	
}
public void nacrtajSpriteDod(Canvas can,int brSl, int brS, int brR, RectF rectCrt){
	//rec.set(sirManjeSlike[brSl]*brS,visManjeSlike[brSl]*(brR),sirManjeSlike[brSl]*(brS+1),visManjeSlike[brSl]*(brR+1));
	rec.set(Math.round(sirManjeSlikeDod[brSl]*brS),
			Math.round(visManjeSlike[brSl]*(brR)),
			Math.round(sirManjeSlikeDod[brSl]*(brS+1)),
			Math.round(visManjeSlike[brSl]*(brR+1)));
	//Rect r=new Rect(0,0,150,100);
	if(!recyclePozvan&&slike[brSl]!=null){can.drawBitmap(slike[brSl],rec,rectCrt,aliasedPaint);
	pustiZvuk( brSl, brR,  brS);}
}
public void nacrtajSpriteDod(Canvas can,int brSl, int brS, int brR, RectF rectCrt,Paint p){
	rec.set(Math.round(sirManjeSlikeDod[brSl]*brS),Math.round(visManjeSlike[brSl]*(brR)),Math.round(sirManjeSlikeDod[brSl]*(brS+1)),Math.round(visManjeSlike[brSl]*(brR+1)));
	//Rect r=new Rect(0,0,150,100);
	if(!recyclePozvan&&slike[brSl]!=null){can.drawBitmap(slike[brSl],rec,rectCrt,p);
    pustiZvuk( brSl, brR,  brS);}

}

public void nacrtjDioBitmapa(Canvas can,int brSl,RectF recRezanjaUPostotcima, RectF rectCrtanja,float kut, float xCentarRot, float yCentarRot, Paint p){
	Rect recRezanja=new Rect();
	 recRezanja.set(Math.round(recRezanjaUPostotcima.left*slike[brSl].getWidth()/100),Math.round(recRezanjaUPostotcima.top*slike[brSl].getHeight()/100), 
			Math.round(recRezanjaUPostotcima.right*slike[brSl].getWidth()/100),Math.round(recRezanjaUPostotcima.bottom*slike[brSl].getHeight()/100));
	//Rect r=new Rect(0,0,150,100);
	 if(!recyclePozvan&&slike[brSl]!=null){ 
		can.save();
	    can.rotate(-kut,xCentarRot, yCentarRot);
	    can.drawBitmap(slike[brSl],recRezanja,rectCrtanja,p);
	    can.restore();
	    pustiZvuk( brSl, 0,  0);
	}
	
}
public void nacrtjDioBitmapa(Canvas can,int brSl,int brRed,int brStup,RectF recRezanjaUPostotcima, RectF rectCrtanja,float kut, float xCentarRot, float yCentarRot, Paint p){
	Rect recRezanja=new Rect();
	float visManjSl=slike[brSl].getHeight()/this.brojRedova[brSl];
	float sirManjSl=slike[brSl].getWidth()/this.brojStupaca[brSl];	
	if(recRezanjaUPostotcima.left<0){
		recRezanjaUPostotcima.set(0, recRezanjaUPostotcima.top, recRezanjaUPostotcima.right, recRezanjaUPostotcima.bottom);
	}
	if(recRezanjaUPostotcima.top<0){
		recRezanjaUPostotcima.set( recRezanjaUPostotcima.left, 0, recRezanjaUPostotcima.right, recRezanjaUPostotcima.bottom);
	}
	if(recRezanjaUPostotcima.bottom>100){
		recRezanjaUPostotcima.set( recRezanjaUPostotcima.left, recRezanjaUPostotcima.top, recRezanjaUPostotcima.right,100);
	}
	if(recRezanjaUPostotcima.right>100){
		recRezanjaUPostotcima.set( recRezanjaUPostotcima.left, recRezanjaUPostotcima.top,100, recRezanjaUPostotcima.bottom);
	}
	 recRezanja.set(Math.round(sirManjSl*brStup+recRezanjaUPostotcima.left*sirManjSl/100),Math.round(visManjSl*brRed+recRezanjaUPostotcima.top*visManjSl/100), 
			 Math.round(sirManjSl*brStup+recRezanjaUPostotcima.right*sirManjSl/100),Math.round(visManjSl*brRed+recRezanjaUPostotcima.bottom*visManjSl/100));
	//Rect r=new Rect(0,0,150,100);
	 if(!recyclePozvan&&slike[brSl]!=null){ 
		can.save();
	    can.rotate(-kut,xCentarRot, yCentarRot);
	    can.drawBitmap(slike[brSl],recRezanja,rectCrtanja,p);
	    can.restore();
	    pustiZvuk( brSl, 0,  0);
	}
	
}
///////gotove animacije
///slaganje////
/*public boolean animacijaGradientKuglaNacrtajP(Canvas can,RectF rectCrtanja ,float postoVisShader,float postoSirShader, Paint p){
         
        
        p.setStyle(Paint.Style.FILL);
        p.setShadowLayer(radius, dx, dy, color);
        p.setShader(new RadialGradient(postoVisShader*rectCrtanja.width()/100,postoSirShader*rectCrtanja.height()/100,
                getHeight() / 3, Color.TRANSPARENT, Color.BLACK, TileMode.MIRROR));
    
    width = getWidth();
    height = getHeight();
    can.drawCircle(rectCrtanja,p);
} */
public boolean animacijaAlfaVanjskoSpremanje(GameEvent ge, int pocetniAlfa,int zavrsniAlfa,Paint paint, float vrijemeSec,int vrijemePauze){

	if(ge.jesamLiZiv){		
		ge.vrijeme1=System.currentTimeMillis();
		ge.jesamLiZiv=false;
	}
	
	boolean b=false;
	float vriDoKraja= (float)((ge.vrijeme1+vrijemeSec*1000+vrijemePauze-System.currentTimeMillis())/1000);
	//float vriOdPocetka=(float)(System.currentTimeMillis()-(vrijemePocetkaAlfa[brSl][brR][brS]+vrijemePauze)/1000);
	int alfa=0;
	/*if(pocetniAlfa>zavrsniAlfa){// smanjivanje
		alfa=(int)((pocetniAlfa-zavrsniAlfa/100)*(vriDoKraja/(vrijemeSec/100)));
	    alfa=zavrsniAlfa+alfa;
	}
	else{//povecavanje
		
	}*/
	if(vriDoKraja>0){//ako nije stiga do kraja još
	    alfa=(int)(((pocetniAlfa-zavrsniAlfa)/100)*(vriDoKraja/(vrijemeSec/100)));
        alfa=zavrsniAlfa+alfa;
	
    	if(alfa<0){
		  alfa=0;}
	    else if(alfa>255){
		  alfa=255;
	   }
	}
	else{// ako je stiga onda se namješta alfa na traženi i vraća true
		b=true;
		alfa=zavrsniAlfa;
	}
  paint.setAlpha(alfa);
  return b;
	
} 
public void resetirajTekPoceoAlfaRedaKaskadno(int brSl,int brR){
	if(tekPoceoAlfaReda!=null){
		tekPoceoAlfaReda[brSl][brR]=true;
		for(int j=0; j<zavrJedALfaKask[brSl].length;j++){
			zavrJedALfaKask[brSl][j]=false;
		}
	}
	 resetirajTekPoceoAlfa( brSl,brR);
	
}
public void resetirajTekPoceoAlfa(int brSl,int brR){
	if(tekPoceoAlfa!=null){
		
		
		for(int k=0;k<tekPoceoAlfa[brSl][brR].length;k++){
			tekPoceoAlfa[brSl][brR][k]=true;
		}
	

}
}
public boolean animacijaAlfaCijelogRedaKaskadnoVanjskoSpremanje(GameEvent geZaKaska,GameEvent zaPojedinacnuAlfu,int brSl, int brR, float vrijemeSec,RectF rec,Canvas can,float vrijemePauzeSec){

	if(geZaKaska.jesamLiZiv){		
		geZaKaska.jesamLiZiv=false;// tek poceo alfa kask
		geZaKaska.vrijeme1=System.currentTimeMillis();// vrjijeme poctka alfa reda 
		geZaKaska.indikator=0;// stao na slicici alfa redda
		geZaKaska.izbornikUpaljen=false;//zavrsena jedna alfa kask
		paintZaAlfuReda=new Paint();
		
		zaPojedinacnuAlfu.jesamLiZiv=true;// tek poceo
	}
	boolean bu=true;
	float vrijeme=0;
	if(brojStupaca[brSl]!=0)vrijeme=1000*vrijemeSec/brojStupaca[brSl];
    else vrijeme=1000*vrijemeSec;
	int vrijemePauzeMil=(int)(vrijemePauzeSec*1000);
	for(int i=0; i<this.brojStupaca[brSl]; i++){
       if(geZaKaska.indikator==i){
    	
    	  if(i==0){// samo u prvoj iteraciji
    		  this.nacrtajSprite(can, brSl, i, brR, rec);
    		  
    		  if(geZaKaska.vrijeme1+vrijeme/2+vrijemePauzeMil<System.currentTimeMillis()){
    			 if(this.brojStupaca[brSl]!=1){ 
    				 geZaKaska.indikator=1;
    				 zaPojedinacnuAlfu.jesamLiZiv=true;// tek poceo
    				 geZaKaska.izbornikUpaljen=false;
    			  }
    			 else{
    				if(geZaKaska.vrijeme1+vrijeme+vrijemePauzeMil>System.currentTimeMillis()){
    					geZaKaska.izbornikUpaljen=this.animacijaAlfaVanjskoSpremanje(zaPojedinacnuAlfu, 255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);  
   			    	     this.nacrtajSprite(can, brSl, i, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje     
   			        }
    				else {
    					zaPojedinacnuAlfu.jesamLiZiv=true;// tek poceo
    					geZaKaska.indikator=1;
    					geZaKaska.izbornikUpaljen=false;
    				 }
    			 }
    		  }
    	  }
    	  else if(i>0&&i<this.brojStupaca[brSl]){// samo nakon što se prva nacrta normalno, do kada dođe do zadnjeg
    		  if(i==this.brojStupaca[brSl]-1){
        		 
        		      if(geZaKaska.vrijeme1+((i)*vrijeme+vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()&&!geZaKaska.izbornikUpaljen){// samo ce se jedan dio vremena povećavati providnost prijašnje slike 
        		    	  geZaKaska.izbornikUpaljen=animacijaAlfaVanjskoSpremanje(zaPojedinacnuAlfu,  255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
        			     
        		    	  this.nacrtajSprite(can, brSl, i, brR, rec);// crtanje trenuta�?ne sli�?ice
            		
        		    	  
        		    	  if(!geZaKaska.izbornikUpaljen){
        			    	  this.nacrtajSprite(can, brSl, i-1, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
        			      }
        			  
        		        }
        		      else  if(geZaKaska.vrijeme1+((i+1)*vrijeme)+vrijemePauzeMil>System.currentTimeMillis()&&!geZaKaska.izbornikUpaljen){
        		    	  this.nacrtajSprite(can, brSl, i, brR, rec);// crtanje trenuta�?ne sli�?ice
        		    	  geZaKaska.izbornikUpaljen=false;
    			    	  zaPojedinacnuAlfu.jesamLiZiv=true;// tek poceo
        		      }
        		      
        		  else if(geZaKaska.vrijeme1+((i+1)*vrijeme+vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()&&!geZaKaska.izbornikUpaljen){
        		   // ovdej ona bljedi
        			  geZaKaska.izbornikUpaljen=animacijaAlfaVanjskoSpremanje(zaPojedinacnuAlfu,  255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
        			 if(!geZaKaska.izbornikUpaljen){
        				 this.nacrtajSprite(can, brSl, i, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
        			 }
        		  }
        		  else {
        			  break;
        		  }
        		  if(geZaKaska.vrijeme1+((i)*vrijeme+vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()&&!geZaKaska.izbornikUpaljen){// samo ce se jedan dio vremena povećavati providnost prijašnje slike 
        			  geZaKaska.izbornikUpaljen=animacijaAlfaVanjskoSpremanje(zaPojedinacnuAlfu,  255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
        			 if(!geZaKaska.izbornikUpaljen){
        				 this.nacrtajSprite(can, brSl, i-1, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
        			 }
        		  }
        		 
        		  
        	         }
    		  else{  
    		      this.nacrtajSprite(can, brSl, i, brR, rec);// crtanje trenuta�?ne sli�?ice
    		      if(geZaKaska.vrijeme1+((i)*vrijeme+vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()&&!geZaKaska.izbornikUpaljen){// samo ce se jedan dio vremena povećavati providnost prijašnje slike 
    		    	  geZaKaska.izbornikUpaljen=animacijaAlfaVanjskoSpremanje(zaPojedinacnuAlfu,  255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
    			      if(!geZaKaska.izbornikUpaljen){
    			    	  this.nacrtajSprite(can, brSl, i-1, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
    			      }
    			      else zaPojedinacnuAlfu.jesamLiZiv=true;// tek poceo
    		        }
    		  
    		      if(geZaKaska.vrijeme1+(i+1)*vrijeme+vrijemePauzeMil<System.currentTimeMillis()){
    		    	 
    		    	  
    		    	  geZaKaska.indikator=i+1;
    		    	  zaPojedinacnuAlfu.jesamLiZiv=true;// tek poceo
    		    	  geZaKaska.izbornikUpaljen=false;
        	         }
    		  }
    	  }
    	  
    	 
    	  bu=false;
    	  break;// svaki put se prestane vrtiti nakon što naiđe na trenuta�?nu s�?ui�?icu
       }		
	}
	if(bu){
		geZaKaska.jesamLiZiv=true;
	}
	return bu;
} 
public boolean animacijaAlfaCijelogRedaKaskadno(int brSl, int brR, float vrijemeSec,RectF rec,Canvas can,float vrijemePauzeSec){
	if(this.tekPoceoAlfaReda==null){
		this.tekPoceoAlfaReda=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
		for(int i=0; i<tekPoceoAlfaReda.length;i++){
			for(int j=0; j<tekPoceoAlfaReda[i].length;j++){
				tekPoceoAlfaReda[i][j]=true;
			}
		}
		zavrJedALfaKask=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
		for(int i=0; i<zavrJedALfaKask.length;i++){
			for(int j=0; j<zavrJedALfaKask[i].length;j++){
				zavrJedALfaKask[i][j]=false;
			}
		}
		vrijemePocetkaAlfaReda=new double[brojBitmapova][maxBrOcekStuIRed];
		staoNaSliciciAlfaReda=new int[brojBitmapova][maxBrOcekStuIRed];
	}
	if(this.tekPoceoAlfaReda[brSl][brR]){		
		this.tekPoceoAlfaReda[brSl][brR]=false;
		this.vrijemePocetkaAlfaReda[brSl][brR]=System.currentTimeMillis();
		this.staoNaSliciciAlfaReda[brSl][brR]=0;
		paintZaAlfuReda=new Paint();
	}
	boolean bu=true;
	float vrijeme=0;
	if(brojStupaca[brSl]!=0)vrijeme=1000*vrijemeSec/brojStupaca[brSl];
    else vrijeme=1000*vrijemeSec;
	int vrijemePauzeMil=(int)(vrijemePauzeSec*1000);
	for(int i=0; i<this.brojStupaca[brSl]; i++){
       if(staoNaSliciciAlfaReda[brSl][brR]==i){
    	
    	  if(i==0){// samo u prvoj iteraciji
    		  this.nacrtajSprite(can, brSl, i, brR, rec);
    		  
    		  if(vrijemePocetkaAlfaReda[brSl][brR]+vrijeme/2+vrijemePauzeMil<System.currentTimeMillis()){
    			 if(this.brojStupaca[brSl]!=1){ 
    				 staoNaSliciciAlfaReda[brSl][brR]=1;
    			     zavrJedALfaKask[brSl][brR]=false;
    			  }
    			 else{
    				if(vrijemePocetkaAlfaReda[brSl][brR]+vrijeme+vrijemePauzeMil>System.currentTimeMillis()){
    				     zavrJedALfaKask[brSl][brR]=this.animacijaAlfa(brSl, i, brR, 255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);  
   			    	     this.nacrtajSprite(can, brSl, i, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje     
   			        }
    				else {
    					 staoNaSliciciAlfaReda[brSl][brR]=1;
        			     zavrJedALfaKask[brSl][brR]=false;
    				 }
    			 }
    		  }
    	  }
    	  else if(i>0&&i<this.brojStupaca[brSl]){// samo nakon što se prva nacrta normalno, do kada dođe do zadnjeg
    		  if(i==this.brojStupaca[brSl]-1){
        		  if(vrijemePocetkaAlfaReda[brSl][brR]+((i+1)*vrijeme-vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()){// trećinu vremena prije kraja i trenuta�?na sli�?ica po�?inje bledjeti
        			  this.nacrtajSprite(can, brSl, i, brR, rec);// crtanje trenuta�?ne sli�?ice
        		  }
        		  else if(vrijemePocetkaAlfaReda[brSl][brR]+((i+1)*vrijeme)+vrijemePauzeMil>System.currentTimeMillis()&&!zavrJedALfaKask[brSl][brR]){
        		   // ovdej ona bljedi
        			 zavrJedALfaKask[brSl][brR]=this.animacijaAlfa(brSl, i, brR, 255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
        			 if(!zavrJedALfaKask[brSl][brR]){
        				 this.nacrtajSprite(can, brSl, i, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
        			 }
        		  }
        		  else {
        			  break;
        		  }
        		  if(vrijemePocetkaAlfaReda[brSl][brR]+((i)*vrijeme+vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()&&!zavrJedALfaKask[brSl][brR]){// samo ce se jedan dio vremena povećavati providnost prijašnje slike 
        			  zavrJedALfaKask[brSl][brR]=this.animacijaAlfa(brSl, i-1, brR, 255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
        			 if(!zavrJedALfaKask[brSl][brR]){
        				 this.nacrtajSprite(can, brSl, i-1, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
        			 }
        		  }
        		 
        		  
        	         }
    		  else{  
    		      this.nacrtajSprite(can, brSl, i, brR, rec);// crtanje trenuta�?ne sli�?ice
    		      if(vrijemePocetkaAlfaReda[brSl][brR]+((i)*vrijeme+vrijeme/2)+vrijemePauzeMil>System.currentTimeMillis()&&!zavrJedALfaKask[brSl][brR]){// samo ce se jedan dio vremena povećavati providnost prijašnje slike 
    		    	  zavrJedALfaKask[brSl][brR]=this.animacijaAlfa(brSl, i-1, brR, 255, 0,paintZaAlfuReda, vrijeme/2000,vrijemePauzeMil);
    			      if(!zavrJedALfaKask[brSl][brR]){
    			    	  this.nacrtajSprite(can, brSl, i-1, brR, rec, paintZaAlfuReda);// crtanje šrijašnje slike sa alfom koja se smanjuje
    			      }
    		        }
    		  
    		      if(vrijemePocetkaAlfaReda[brSl][brR]+(i+1)*vrijeme+vrijemePauzeMil<System.currentTimeMillis()){
    		    	 
    		    	  if(this.tekPoceoAlfa!=null) tekPoceoAlfa[brSl][brR][i]=true;// vraćam zastavicu na poi�?etni  u podmetodi "animacijaAlfa" za stupac koji će u sljedećoj iteraciji biti bivši i-1
        		       staoNaSliciciAlfaReda[brSl][brR]=i+1;
        		       zavrJedALfaKask[brSl][brR]=false;
        	         }
    		  }
    	  }
    	  
    	 
    	  bu=false;
    	  break;// svaki put se prestane vrtiti nakon što naiđe na trenuta�?nu s�?ui�?icu
       }		
	}
	if(bu){
		this.tekPoceoAlfaReda[brSl][brR]=true;
	}
	return bu;
} 
public boolean animacijaAlfa(int brSl,int brS, int brR,int pocetniAlfa,int zavrsniAlfa,Paint paint, float vrijemeSec,int vrijemePauze){
	if(tekPoceoAlfa==null){
		tekPoceoAlfa=new boolean[this.brojBitmapova][maxBrOcekStuIRed][maxBrOcekStuIRed];
		
		for(int i=0; i<tekPoceoAlfa.length;i++){
			for(int j=0; j<tekPoceoAlfa[i].length;j++){
				for(int k=0;k<tekPoceoAlfa[i][j].length;k++){
					tekPoceoAlfa[i][j][k]=true;
				}
			}
		}
		vrijemePocetkaAlfa=new double[this.brojBitmapova][maxBrOcekStuIRed][maxBrOcekStuIRed];
	}
	if(this.tekPoceoAlfa[brSl][brR][brS]){		
		this.tekPoceoAlfa[brSl][brR][brS]=false;
		this.vrijemePocetkaAlfa[brSl][brR][brS]=System.currentTimeMillis();
	}
	
	boolean b=false;
	float vriDoKraja= (float)((vrijemePocetkaAlfa[brSl][brR][brS]+vrijemeSec*1000+vrijemePauze-System.currentTimeMillis())/1000);
	//float vriOdPocetka=(float)(System.currentTimeMillis()-(vrijemePocetkaAlfa[brSl][brR][brS]+vrijemePauze)/1000);
	int alfa=0;
	/*if(pocetniAlfa>zavrsniAlfa){// smanjivanje
		alfa=(int)((pocetniAlfa-zavrsniAlfa/100)*(vriDoKraja/(vrijemeSec/100)));
	    alfa=zavrsniAlfa+alfa;
	}
	else{//povecavanje
		
	}*/
	if(vriDoKraja>0){//ako nije stiga do kraja još
	    alfa=(int)(((pocetniAlfa-zavrsniAlfa)/100)*(vriDoKraja/(vrijemeSec/100)));
        alfa=zavrsniAlfa+alfa;
	
    	if(alfa<0){
		  alfa=0;}
	    else if(alfa>255){
		  alfa=255;
	   }
	}
	else{// ako je stiga onda se namješta alfa na traženi i vraća true
		b=true;
		alfa=zavrsniAlfa;
		this.tekPoceoAlfa[brSl][brR][brS]=true;
	}
  paint.setAlpha(alfa);
  return b;
	
} 
public boolean animacijaSlaganjeRotacija( int brSl, int brR,float startKut,float deltaKut, float sec,float fps){
	boolean b=false;
	
	float dK=0;
	if(sec>0)dK=deltaKut/(sec*fps);
	this.tempUkupniDeltaKut+=dK;
	tempKutZaPomaknut[brSl][brR]=startKut+tempUkupniDeltaKut;
	if(Math.abs(deltaKut)<=Math.abs(this.tempUkupniDeltaKut)){
		tempKutZaPomaknut[brSl][brR]=0;
		this.tempUkupniDeltaKut=0;
		b=true;
	}
	return b;
	
} 
public boolean animacijaSlaganjeRotacijaVremenska( int brSl, int brR,float startKut,float deltaKut, float sec){
	boolean b=false;
	
    float	razlikaVremena;
	if(tekPoceoRotacijom==null){
		this.tekPoceoRotacijom=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
		for(int i=0; i<tekPoceoRotacijom.length;i++){
			for(int j=0; j<tekPoceoRotacijom[i].length;j++){
				tekPoceoRotacijom[i][j]=true;
			}
		}
		vrijemePocetkaRotacije=new double[this.brojBitmapova][maxBrOcekStuIRed];
		
		
	}
	if(tekPoceoRotacijom[brSl][brR]){//
		tekPoceoRotacijom[brSl][brR]=false;
		razlikaVremena=0;
	}
	else razlikaVremena=(Math.abs((float)(this.vrijemePocetkaRotacije[brSl][brR]-System.currentTimeMillis())))/1000;
	
	
	float dK=0;
	if(sec>0)dK=(deltaKut/(sec))*razlikaVremena;
	this.tempUkupniDeltaKut+=dK;
	tempKutZaPomaknut[brSl][brR]=startKut+tempUkupniDeltaKut;
	if(Math.abs(deltaKut)<=Math.abs(this.tempUkupniDeltaKut)){
		tempKutZaPomaknut[brSl][brR]=0;
		this.tempUkupniDeltaKut=0;
		b=true;
	}
	vrijemePocetkaRotacije[brSl][brR]=System.currentTimeMillis();
	if(b)tekPoceoRotacijom[brSl][brR]=true;
	return b;
	
}
public boolean animacijaSlaganjeRotacijaVremenska(GameEvent ge,float startKut,float deltaKut, float sec){
	boolean b=false;
	
    float	razlikaVremena;
	
	if(ge.jesamLiZiv){//
		ge.jesamLiZiv=false;
		razlikaVremena=0;
		ge.x=0;
		ge.vrijeme1=0;
	}
	else razlikaVremena=(Math.abs((float)(ge.vrijeme1+ge.vrijemePauze-System.currentTimeMillis())))/1000;
	
	
	float dK=0;
	if(sec>0)dK=(deltaKut/(sec))*razlikaVremena;
	ge.x2+=dK;
	ge.x=startKut+ge.x2;
	if(Math.abs(deltaKut)<=Math.abs(ge.x2)){
		ge.x=startKut+deltaKut;
		ge.x2=0;
		b=true;
	}
	ge.vrijeme1=System.currentTimeMillis();
	if(b)ge.jesamLiZiv=true;
	return b;
	
} 
public boolean animacijaSlaganjeNacrtaj(Canvas can,RectF rectCrt, int brSl, int brR,float fps,Paint p,boolean vrtiSlicice){// samo iscrtava rect koji mu se posalje i pazi da se naimacija pravilno iscrtava
	// vraca true kada zavrsi cijeli red
	boolean b=false;
	 if(tempBrStupca[brSl][brR]<0) tempBrStupca[brSl][brR]=0;
	 if(this.tempBrTaktova<0) this.tempBrTaktova=(int)(fps/this.prikPoSec[brSl])+1;// po defoltu je manje od nule tako da ce se odmah na po�?etku vrtiti iz prve slicice
    /////////////
   float temp=fps/this.prikPoSec[brSl];
   if(vrtiSlicice){	if(temp<this.tempBrTaktova){//ako je doslo vrijeme za promjenu slicice
   		//if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	        this.nacrtajSpriteRotiran(can, brSl, tempBrStupca[brSl][brR], brR, rectCrt,tempKutZaPomaknut[brSl][brR],p);
	       this.tempBrStupca[brSl][brR]++;
	       this.tempBrTaktova=0;
	       if(this.tempBrStupca[brSl][brR]>this.brojStupaca[brSl]-1){
	    	   tempBrStupca[brSl][brR]=0;
	    	   b=true;
	       }
          }
	 else {//ako nije vrijme za promjenu slicice
		    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
		    this.nacrtajSpriteRotiran(can, brSl, tempBrStupca[brSl][brR], brR, rectCrt,tempKutZaPomaknut[brSl][brR],p);
	     }
        }
   else {//
	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	    this.nacrtajSpriteRotiran(can, brSl, tempBrStupca[brSl][brR], brR, rectCrt,tempKutZaPomaknut[brSl][brR],p);
    }
   tempBrTaktova++;
 
 return b;//
}
public boolean animacijaSlaganjeNacrtaj(Canvas can,GameEvent eventSaKutom,RectF rectCrt, int brSl, int brR,float fps,Paint p,boolean vrtiSlicice){// samo iscrtava rect koji mu se posalje i pazi da se naimacija pravilno iscrtava
    
	float kut=0 ;
	if(eventSaKutom!=null){
		kut=eventSaKutom.x;
	}
	
	
	// vraca true kada zavrsi cijeli red
	boolean b=false;
	 if(tempBrStupca[brSl][brR]<0) tempBrStupca[brSl][brR]=0;
	 if(this.tempBrTaktova<0) this.tempBrTaktova=(int)(fps/this.prikPoSec[brSl])+1;// po defoltu je manje od nule tako da ce se odmah na po�?etku vrtiti iz prve slicice
    /////////////
   float temp=fps/this.prikPoSec[brSl];
   if(vrtiSlicice){	if(temp<this.tempBrTaktova){//ako je doslo vrijeme za promjenu slicice
   		//if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	        this.nacrtajSpriteRotiran(can, brSl, tempBrStupca[brSl][brR], brR, rectCrt,kut,p);
	       this.tempBrStupca[brSl][brR]++;
	       this.tempBrTaktova=0;
	       if(this.tempBrStupca[brSl][brR]>this.brojStupaca[brSl]-1){
	    	   tempBrStupca[brSl][brR]=0;
	    	   b=true;
	       }
          }
	 else {//ako nije vrijme za promjenu slicice
		    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
		    this.nacrtajSpriteRotiran(can, brSl, tempBrStupca[brSl][brR], brR, rectCrt,kut,p);
	     }
        }
   else {//
	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	    this.nacrtajSpriteRotiran(can, brSl, tempBrStupca[brSl][brR], brR, rectCrt,kut,p);
    }
   tempBrTaktova++;
 
 return b;//
}
public boolean animacijaSlaganjeUvecanje(float povX,float povY, float secZaX,float secZaY,float fps, RectF rect){
	boolean b=false;
	float dx=0;
	if(fps<=0)fps=1;
	if(secZaX>0)dx=povX/(secZaX*fps);
	
	float dy=0;
	if(secZaY>0)dy=povY/(secZaY*fps);
	
	this.tempUkupnoPovZaX+=dx;
	this.tempUkupnoPovZaY+=dy;
	
	if(!povXGotova){if(Math.abs(this.tempUkupnoPovZaX)>=Math.abs(povX)){
		           this.povXGotova=true;
		           this.tempUkupnoPovZaX=0;
		           dx=0;
	       }
	     }
	else{
		tempUkupnoPovZaX=0;
		dx=0;//tako da se i za sljedeće iteracjie briuše
	}
	if(!povYGotova){if(Math.abs(this.tempUkupnoPovZaY)>=Math.abs(povY)){
		            this.povYGotova=true;
		           this.tempUkupnoPovZaY=0;
		           dy=0;
	            }
	      }
	else {
		tempUkupnoPovZaY=0;
		dy=0;
	}
	if(this.povXGotova&&this.povYGotova){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		povYGotova=false;
		povXGotova=false;
		this.tempUkupnoPovZaX=0;
		this.tempUkupnoPovZaY=0;
	}
	this.povecajRectF(dx, dy,rect);
	
	return b;
}
public boolean animacijaSlaganjeUvecanjeVremenska(int brSl,int brReda,float povX,float povY, float secZaX,float secZaY, RectF rect){
	boolean b=false;
	float dx=0;
    float	razlikaVremena;
    if(this.tekPoceoUvecanja==null){
		this.tekPoceoUvecanja=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
		for(int i=0; i<tekPoceoUvecanja.length;i++){
			for(int j=0; j<tekPoceoUvecanja[i].length;j++){
				tekPoceoUvecanja[i][j]=true;
			}
		}
		vrijemePocetkaUvecanja=new double[this.brojBitmapova][maxBrOcekStuIRed];
	}
	if(tekPoceoUvecanja[brSl][brReda]){//
		tekPoceoUvecanja[brSl][brReda]=false;
		razlikaVremena=0;
	}
	else razlikaVremena=(Math.abs((float)(this.vrijemePocetkaUvecanja[brSl][brReda]-System.currentTimeMillis())))/1000;
	
	
	if(secZaX>0)dx=(povX/(secZaX))*razlikaVremena;
	
	float dy=0;
	if(secZaY>0)dy=(povY/(secZaY))*razlikaVremena;;

	this.tempUkupnoPovZaX+=dx;
	this.tempUkupnoPovZaY+=dy;

	if(!povXGotova){if(Math.abs(this.tempUkupnoPovZaX)>=Math.abs(povX)){
        this.povXGotova=true;
        this.tempUkupnoPovZaX=0;
        dx=0;
         }
    }
    else{
       tempUkupnoPovZaX=0;
       dx=0;//tako da se i za sljedeće iteracjie briuše
         }
     if(!povYGotova){if(Math.abs(this.tempUkupnoPovZaY)>=Math.abs(povY)){
          this.povYGotova=true;
          this.tempUkupnoPovZaY=0;
          dy=0;
        }
       }
     else {
        tempUkupnoPovZaY=0;
        dy=0;
     }
	if(this.povXGotova&&this.povYGotova){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		povYGotova=false;
		povXGotova=false;
		this.tempUkupnoPovZaX=0;
		this.tempUkupnoPovZaY=0;
	}
	this.povecajRectF(dx, dy,rect);
	vrijemePocetkaUvecanja[brSl][brReda]=System.currentTimeMillis();
	if(b)tekPoceoUvecanja[brSl][brReda]=true;
	return b;
}
static public boolean animacijaSlaganjeUvecanjeVremenska(GameEvent ge,float povX,float povY, float secZaX,float secZaY, RectF rect){
	boolean b=false;
	float dx=0;
    float	razlikaVremena;
   
	if(ge.jesamLiZiv){//
		ge.x2=rect.width();// pocetna sirina
		ge.y2=rect.height();// pocetna sirina
		ge.vrijeme2=ge.vrijeme1=System.currentTimeMillis();
		ge.jesamLiZiv=false;
		razlikaVremena=0;
		ge.zaleden=false;
		ge.otrovan=false;
		ge.x=0;
		ge.y=0;
	}
	else razlikaVremena=(Math.abs((float)(System.currentTimeMillis()-ge.vrijeme2+ge.vrijemePauze)))/1000;
	
	
	if(secZaX>0)dx=(povX/(secZaX))*razlikaVremena;
	
	float dy=0;
	if(secZaY>0)dy=(povY/(secZaY))*razlikaVremena;;

	ge.x+=dx;
	ge.y+=dy;

	if(!ge.otrovan){if(Math.abs(ge.x)>=Math.abs(povX)){
        ge.otrovan=true;
        ge.x=0;
        dx=0;
        rect.set(rect.centerX()-povX/2-ge.x2/2, rect.top, rect.centerX()+povX/2+ge.x2/2, rect.bottom);
         }
    }
    else{
      ge.x=0;
       dx=0;//tako da se i za sljedeće iteracjie briuše
         }
     if(!ge.zaleden){if(Math.abs(ge.y)>=Math.abs(povY)){
          ge.zaleden=true;
          rect.set(rect.left, rect.centerY()-povY/2-ge.y2/2, rect.right, rect.centerY()+povY/2+ge.y2/2);
         ge.y=0;
          dy=0;
        }
       }
     else {
        ge.y=0;
        dy=0;
     }
	if(ge.otrovan&&ge.zaleden){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		ge.zaleden=false;
		ge.otrovan=false;
		ge.x=0;
		ge.y=0;
	}
	rect.set(rect.left-dx/2, rect.top-dy/2, rect.right+dx/2, rect.bottom+dy/2);
	//this.povecajRectF(dx, dy,rect);
	boolean d=false;
	if(ge.vrijeme1+ge.vrijemePauze+secZaX*1000<System.currentTimeMillis()){
		 d=true;
		rect.set(rect.centerX()-povX/2-ge.x2/2, rect.top, rect.centerX()+povX/2+ge.x2/2, rect.bottom);
	}

	if(ge.vrijeme1+ge.vrijemePauze+secZaY*1000<System.currentTimeMillis()){
		if(d)b=true;
		rect.set(rect.left, rect.centerY()-povY/2-ge.y2/2, rect.right, rect.centerY()+povY/2+ge.y2/2);
	}
	
	
	ge.vrijeme2=System.currentTimeMillis();
	
	if(b){
		
		ge.jesamLiZiv=true;
	}
	return b;
}
public boolean animacijaSlaganjeUvecanjeVremenskaPix(int brSl,int brReda,float povX,float povY, float secZaX,float secZaY, RectF rect){
	boolean b=false;
	float dx=0;
    float	razlikaVremena;
	if(this.tekPoceoUvecanja==null){
		this.tekPoceoUvecanja=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
		for(int i=0; i<tekPoceoUvecanja.length;i++){
			for(int j=0; j<tekPoceoUvecanja[i].length;j++){
				tekPoceoUvecanja[i][j]=true;
			}
		}
		vrijemePocetkaUvecanja=new double[this.brojBitmapova][maxBrOcekStuIRed];
	}
	if(tekPoceoUvecanja[brSl][brReda]){//
		tekPoceoUvecanja[brSl][brReda]=false;
		razlikaVremena=0;
	}
	else razlikaVremena=(Math.abs((float)(this.vrijemePocetkaUvecanja[brSl][brReda]-System.currentTimeMillis())))/1000;
	
	
	if(secZaX>0)dx=(povX/(secZaX))*razlikaVremena;
	
	float dy=0;
	if(secZaY>0)dy=(povY/(secZaY))*razlikaVremena;;

	this.tempUkupnoPovZaX+=dx;
	this.tempUkupnoPovZaY+=dy;

	if(!povXGotova){if(Math.abs(this.tempUkupnoPovZaX)>=Math.abs(povX)){
        this.povXGotova=true;
        this.tempUkupnoPovZaX=0;
        dx=0;
         }
    }
    else{
       tempUkupnoPovZaX=0;
       dx=0;//tako da se i za sljedeće iteracjie briuše
         }
     if(!povYGotova){if(Math.abs(this.tempUkupnoPovZaY)>=Math.abs(povY)){
          this.povYGotova=true;
          this.tempUkupnoPovZaY=0;
          dy=0;
        }
       }
     else {
        tempUkupnoPovZaY=0;
        dy=0;
     }
	if(this.povXGotova&&this.povYGotova){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		povYGotova=false;
		povXGotova=false;
		this.tempUkupnoPovZaX=0;
		this.tempUkupnoPovZaY=0;
	}
	this.povecajRectPix(dx, dy,rect);
	vrijemePocetkaUvecanja[brSl][brReda]=System.currentTimeMillis();
	if(b)tekPoceoUvecanja[brSl][brReda]=true;
	return b;
}
static public boolean animacijaSlaganjeTranslacijaVremenskaVanjskoSpremanje(GameEvent ge,float transX,float transY, float secZaX,float secZaY, RectF rect){
	boolean b=false;
	
	float dx=0;
	
	float	razlikaVremena;
	if(ge.jesamLiZiv){//
		ge.y2=rect.top;
		ge.x2=rect.left;
		ge.vrijeme1=System.currentTimeMillis();
		ge.jesamLiZiv=false;
		ge.x=0;
		ge.y=0;
		razlikaVremena=0;
	}
	else razlikaVremena=(Math.abs((float)(ge.vrijeme1+ge.vrijemePauze-System.currentTimeMillis())))/1000;
		
		if(secZaX>0) dx=(transX/secZaX)*razlikaVremena;
	     
	    float dy=0;
	    if(secZaY>0) dy=(transY/secZaY)*razlikaVremena;
	    rect.set(rect.left+dx,rect.top+dy, rect.right+dx, rect.bottom+dy);
	
	    ge.x+=dx;
	    ge.y+=dy;
	
	
	if(Math.abs(ge.x)>=Math.abs(transX)){
		rect.set(ge.x2+transX, rect.top, ge.x2+transX+rect.width(), rect.bottom);
		 ge.gorim=true;
		 ge.x=0;
	}
	if(Math.abs(ge.y)>=Math.abs(transY)){
		rect.set(rect.left, ge.y2+transY, rect.right,ge.y2+transY+rect.height());
		ge.zaleden=true;
		 ge.y=0;
	}
	if( ge.gorim&& ge.zaleden){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		ge.zaleden=false;
		ge.gorim=false;
		ge.y=0;
		ge.x=0;
	}
	
	ge.vrijeme1=System.currentTimeMillis();
	if(b)	ge.jesamLiZiv=true;
	return b;
}
public boolean animacijaSlaganjeTranslacijaVremenska(int brSl,int brReda,float transX,float transY, float secZaX,float secZaY, RectF rect){
	boolean b=false;
	
	float dx=0;
	
	float	razlikaVremena;
    if(this.tekPoceoTranslacijom==null){
    	tekPoceoTranslacijom=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
    	for(int i=0; i<tekPoceoTranslacijom.length;i++){
    		for(int j=0; j<tekPoceoTranslacijom[i].length;j++){
    			tekPoceoTranslacijom[i][j]=true;
    		}
    	}
    	vrijemePocetkaTranslacije=new double[this.brojBitmapova][maxBrOcekStuIRed];
    }
	if(tekPoceoTranslacijom[brSl][brReda]){//
		tekPoceoTranslacijom[brSl][brReda]=false;
		razlikaVremena=0;
	}
	else razlikaVremena=(Math.abs((float)(this.vrijemePocetkaTranslacije[brSl][brReda]-System.currentTimeMillis())))/1000;
		
		if(secZaX>0) dx=(transX/secZaX)*razlikaVremena;
	     
	    float dy=0;
	    if(secZaY>0) dy=(transY/secZaY)*razlikaVremena;
	    this.translatirajRectF(dx, dy,rect);
	    this.tempUkupnaTranslacijaZaX+=dx;
	    this.tempUkupnaTranslacijaZaY+=dy;
	
	
	if(Math.abs(this.tempUkupnaTranslacijaZaX)>=Math.abs(transX)){
		this.transXGotova=true;
		this.tempUkupnaTranslacijaZaX=0;
	}
	if(Math.abs(this.tempUkupnaTranslacijaZaY)>=Math.abs(transY)){
		this.transYGotova=true;
		this.tempUkupnaTranslacijaZaY=0;
	}
	if(this.transXGotova&&this.transYGotova){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		transYGotova=false;
		transXGotova=false;
		this.tempUkupnaTranslacijaZaX=0;
		this.tempUkupnaTranslacijaZaY=0;
	}
	
	vrijemePocetkaTranslacije[brSl][brReda]=System.currentTimeMillis();
	if(b)tekPoceoTranslacijom[brSl][brReda]=true;
	return b;
}
public boolean animacijaSlaganjeTranslacija(float transX,float transY, float secZaX,float secZaY,float fps, RectF rect){
	boolean b=false;
	
	float dx=0;
	if(secZaX>0) dx=transX/(secZaX*fps);
	float dy=0;
	if(secZaY>0) dy=transY/(secZaY*fps);
	this.translatirajRectF(dx, dy,rect);
	this.tempUkupnaTranslacijaZaX+=dx;
	this.tempUkupnaTranslacijaZaY+=dy;
	
	if(Math.abs(this.tempUkupnaTranslacijaZaX)>=Math.abs(transX)){
		this.transXGotova=true;
		this.tempUkupnaTranslacijaZaX=0;
	}
	if(Math.abs(this.tempUkupnaTranslacijaZaY)>=Math.abs(transY)){
		this.transYGotova=true;
		this.tempUkupnaTranslacijaZaY=0;
	}
	if(this.transXGotova&&this.transYGotova){// ako su obje translacije gotove salje se nazad true i to bi ostali mehanizmi trebali znati prepoznati ina�?e bi se konstantno nastavilo gibati
		b=true;
		transYGotova=false;
		transXGotova=false;
		this.tempUkupnaTranslacijaZaX=0;
		this.tempUkupnaTranslacijaZaY=0;
	}
	
	
	return b;
}
///////////////

public boolean animacijaSlaganjeRazmotajSvitakHorizontalnoNACRTAJ(Canvas can,GameEvent geOstalo,GameEvent geKut,int brSl, int brR,float lijeviDXposto, float desniDXposto ,float sec,RectF rec,Paint p,boolean razmotajIliPovecajSredisnji ){
	boolean b=false;
	
    float	razlikaVremena;
	/*if(this.tekPoceoSvitak==null){
		tekPoceoSvitak=new boolean[this.brojBitmapova][maxBrOcekStuIRed];
		for(int i=0; i<tekPoceoSvitak.length;i++){
			for(int j=0; j<tekPoceoSvitak[i].length;j++){
				tekPoceoSvitak[i][j]=true;
			}
		}
		vrijemePocetkaSvitak=new double[this.brojBitmapova][maxBrOcekStuIRed];
	}*/
	if(geOstalo.jesamLiZiv){//
		geOstalo.x=0;
		geOstalo.x2=0;
		geOstalo.jesamLiZiv=false;
		razlikaVremena=0;
		
	}
	else razlikaVremena=(Math.abs((float)(geOstalo.vrijeme1+geOstalo.vrijemePauze-System.currentTimeMillis())))/1000;
	float dP=0;
	float dPSl=0;
	RectF lijevi,desni,centar;
	float lijeviDX=lijeviDXposto*(rec.width()/100);
	float desniDX=desniDXposto*(rec.width()/100);
	if(sec>0){
		dP=((rec.width()-(lijeviDX+ desniDX))/(sec))*razlikaVremena;// pomak
		dPSl=((100-(lijeviDXposto+ desniDXposto))/(sec))*razlikaVremena;// pomak sredisnje slike
	}
	
	geOstalo.x+=dP;
	geOstalo.x2+=dPSl;
	float kutZaPomaknut=0;
	if(geKut!=null){
		kutZaPomaknut=geKut.x;
	}
	lijevi=new RectF(rec.centerX()-lijeviDX-geOstalo.x/2, rec.top,rec.centerX()-geOstalo.x/2, rec.bottom);
	desni=new RectF(rec.centerX()+geOstalo.x/2, rec.top,rec.centerX()+ desniDX+geOstalo.x/2, rec.bottom);
	centar=new RectF(rec.centerX()-geOstalo.x/2, rec.top,rec.centerX()+geOstalo.x/2, rec.bottom);
	
	if(centar.width()>=rec.width()-lijeviDX- desniDX){
		b=true;
		geOstalo.x=(rec.width()-(lijeviDX+ desniDX));
		geOstalo.x2=(100-(lijeviDXposto+ desniDXposto));
		lijevi=new RectF(rec.centerX()-lijeviDX-geOstalo.x/2, rec.top,rec.centerX()-geOstalo.x/2, rec.bottom);
		desni=new RectF(rec.centerX()+geOstalo.x/2, rec.top,rec.centerX()+ desniDX+geOstalo.x/2, rec.bottom);
		centar=new RectF(rec.centerX()-geOstalo.x/2, rec.top,rec.centerX()+geOstalo.x/2, rec.bottom);
		
	}
    this.nacrtjDioBitmapa(can, brSl,new RectF(0, 0,  desniDXposto, 100),lijevi,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	
	if(razmotajIliPovecajSredisnji)this.nacrtjDioBitmapa(can, brSl,new RectF( 50-geOstalo.x2/2,0, 50+geOstalo.x2/2, 100),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	else this.nacrtjDioBitmapa(can, brSl,new RectF( lijeviDXposto,0, 100-desniDXposto, 100),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	this.nacrtjDioBitmapa(can, brSl,new RectF(100-desniDXposto, 0,100, 100),desni,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	geOstalo.vrijeme1=System.currentTimeMillis();
	if(b)geOstalo.jesamLiZiv=true;
	return b;
}

public boolean animacijaSlaganjeSmotajSvitakHorizontalnoNACRTAJ(Canvas can,GameEvent geOstalo,GameEvent geKut,int brSl, int brR,float lijeviDXposto, float desniDXposto ,float sec,RectF rec,Paint p,boolean smotajIliUmanjiSredisnji  ){
	
	boolean b=false;
float	razlikaVremena;
if(geOstalo.jesamLiZiv){//
	geOstalo.x=0;
	geOstalo.x2=0;
	geOstalo.jesamLiZiv=false;
	razlikaVremena=0;

}
	else razlikaVremena=(Math.abs((float)(geOstalo.vrijeme1+geOstalo.vrijemePauze-System.currentTimeMillis())))/1000;
	float dP=0;
	float dPSl=0;
	RectF lijevi,desni,centar;
	float lijeviDX=lijeviDXposto*(rec.width()/100);
	float desniDX=desniDXposto*(rec.width()/100);
	if(sec>0){
		dP=((rec.width()-(lijeviDX+ desniDX))/(sec))*razlikaVremena;// pomak
		dPSl=((100-(lijeviDXposto+ desniDXposto))/(sec))*razlikaVremena;// pomak sredisnje slike
	}
	geOstalo.x+=dP;
	geOstalo.x2+=dPSl;
	float kutZaPomaknut=0;
	if(geKut!=null){
		kutZaPomaknut=geKut.x;
	}
	
	float DX= (rec.width()-(lijeviDX+ desniDX))-geOstalo.x;
	float DXSl=(100-(lijeviDXposto+ desniDXposto))-geOstalo.x2;
	lijevi=new RectF(rec.centerX()-lijeviDX-DX/2, rec.top,rec.centerX()-DX/2, rec.bottom);
	desni=new RectF(rec.centerX()+DX/2, rec.top,rec.centerX()+ desniDX+DX/2, rec.bottom);
	centar=new RectF(rec.centerX()-DX/2, rec.top,rec.centerX()+DX/2, rec.bottom);
	
	if(centar.width()<=dP){
		b=true;
		geOstalo.x=(rec.width()-(lijeviDX+ desniDX));
		geOstalo.x2=(100-(lijeviDXposto+ desniDXposto));
		
		DX= (rec.width()-(lijeviDX+ desniDX))-geOstalo.x;
		 DXSl=(100-(lijeviDXposto+ desniDXposto))-geOstalo.x2;
		lijevi=new RectF(rec.centerX()-lijeviDX-DX/2, rec.top,rec.centerX()-DX/2, rec.bottom);
		desni=new RectF(rec.centerX()+DX/2, rec.top,rec.centerX()+ desniDX+DX/2, rec.bottom);
		centar=new RectF(rec.centerX()-DX/2, rec.top,rec.centerX()+DX/2, rec.bottom);
	}
	this.nacrtjDioBitmapa(can, brSl,new RectF(0, 0,  desniDXposto, 100),lijevi,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	if(smotajIliUmanjiSredisnji ){this.nacrtjDioBitmapa(can, brSl,new RectF( lijeviDXposto+geOstalo.x2/2,0, 100-desniDXposto-geOstalo.x2/2, 100),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);}
	else this.nacrtjDioBitmapa(can, brSl,new RectF( desniDXposto,0, 100-lijeviDXposto, 100),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	this.nacrtjDioBitmapa(can, brSl,new RectF(100-desniDXposto, 0,100, 100),desni,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	geOstalo.vrijeme1=System.currentTimeMillis();
	if(b)geOstalo.jesamLiZiv=true;
	return b;
}
public boolean animacijaSlaganjeRazmotajSvitakVertikalnoNACRTAJ(Canvas can,GameEvent geOstalo,GameEvent geKut,int brSl, int brR,float gornjiDXposto, float donjiDXposto ,float sec,RectF rec,Paint p,boolean razmotajIliPovecajSredisnji ){
	boolean b=false;

	float	razlikaVremena;
        
		if(geOstalo.jesamLiZiv){//
			geOstalo.jesamLiZiv=false;
			razlikaVremena=0;
			geOstalo.x=0;
			geOstalo.x2=0;
		}
		else razlikaVremena=(Math.abs((float)(geOstalo.vrijeme1+geOstalo.vrijemePauze-System.currentTimeMillis())))/1000;
	float dP=0;
	float dPSl=0;
	RectF gornji,donji,centar;
	float gornjiDX=gornjiDXposto*(rec.height()/100);
	float donjiDX=donjiDXposto*(rec.height()/100);
	if(sec>0){
		dP=((rec.height()-(gornjiDX+ donjiDX))/(sec))*razlikaVremena;// pomak
		dPSl=((100-(gornjiDXposto+ donjiDXposto))/(sec))*razlikaVremena;// pomak sredisnje slike
	}
	geOstalo.x+=dP;
	geOstalo.x2+=dPSl;
	float kutZaPomaknut=0;
	if(geKut!=null){
		kutZaPomaknut=geKut.x;
	}
	
	
	gornji=new RectF(rec.left,rec.centerY()-gornjiDX-geOstalo.x/2, rec.right, rec.centerY()-geOstalo.x/2);
	donji=new RectF( rec.left,rec.centerY()+geOstalo.x/2, rec.right,rec.centerY()+ donjiDX+geOstalo.x/2);
	centar=new RectF(rec.left, rec.centerY()-geOstalo.x/2, rec.right,rec.centerY()+geOstalo.x/2);
	if(centar.height()>=rec.height()-gornjiDX- donjiDX){
		b=true;
		geOstalo.x=(rec.height()-(gornjiDX+ donjiDX));
		geOstalo.x2=(100-(gornjiDXposto+ donjiDXposto));
		

		gornji=new RectF(rec.left,rec.centerY()-gornjiDX-geOstalo.x/2, rec.right, rec.centerY()-geOstalo.x/2);
		donji=new RectF( rec.left,rec.centerY()+geOstalo.x/2, rec.right,rec.centerY()+ donjiDX+geOstalo.x/2);
		centar=new RectF(rec.left, rec.centerY()-geOstalo.x/2, rec.right,rec.centerY()+geOstalo.x/2);
	}
	this.nacrtjDioBitmapa(can, brSl,new RectF(0, 0, 100,  donjiDXposto),gornji,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	
	if(razmotajIliPovecajSredisnji)this.nacrtjDioBitmapa(can, brSl,new RectF( 50-geOstalo.x2/2,0, 50+geOstalo.x2/2, 100),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	else this.nacrtjDioBitmapa(can, brSl,new RectF( 0,gornjiDXposto, 100, 100-donjiDXposto),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	this.nacrtjDioBitmapa(can, brSl,new RectF(0,100-donjiDXposto, 100, 100),donji,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	
	
	geOstalo.vrijeme1=System.currentTimeMillis();
	if(b)geOstalo.jesamLiZiv=true;
	return b;
}

public boolean animacijaSlaganjeSmotajSvitakVertikalnoNACRTAJ(Canvas can,GameEvent geOstalo,GameEvent geKut,int brSl, int brR,float gornjiDXposto, float donjiDXposto ,float sec,RectF rec,Paint p,boolean smotajIliUmanjiSredisnji  ){
	
	boolean b=false;
float	razlikaVremena;
  if(geOstalo.jesamLiZiv){//
	  geOstalo.jesamLiZiv=false;
	razlikaVremena=0;
	geOstalo.x=0;
	geOstalo.x2=0;
   }
	else razlikaVremena=(Math.abs((float)(geOstalo.vrijeme1+geOstalo.vrijemePauze-System.currentTimeMillis())))/1000;
	float dP=0;
	float dPSl=0;
	RectF gornji,donji,centar;
	float gornjiDX=gornjiDXposto*(rec.height()/100);
	float donjiDX=donjiDXposto*(rec.height()/100);
	if(sec>0){
		dP=((rec.height()-(gornjiDX+ donjiDX))/(sec))*razlikaVremena;// pomak
		dPSl=((100-(gornjiDXposto+donjiDXposto))/(sec))*razlikaVremena;// pomak sredisnje slike
	}
	geOstalo.x+=dP;
	geOstalo.x2+=dPSl;
	float kutZaPomaknut=0;
	if(geKut!=null){
		kutZaPomaknut=geKut.x;
	}
	
	
	float DX= (rec.height()-(gornjiDX+ donjiDX))-geOstalo.x;
	float DXSl=(100-(gornjiDXposto+ donjiDXposto))-geOstalo.x2;
	gornji=new RectF(rec.left,rec.centerY()-gornjiDX-DX/2, rec.right, rec.centerY()-DX/2);
	donji=new RectF(rec.left,rec.centerY()+DX/2, rec.right, rec.centerY()+ donjiDX+DX/2);
	centar=new RectF( rec.left,rec.centerY()-DX/2, rec.right,rec.centerY()+DX/2);
	if(centar.height()<=dP){
		b=true;
		geOstalo.x=(rec.height()-(gornjiDX+ donjiDX));
		geOstalo.x2=(100-(gornjiDXposto+donjiDXposto));
		
		DX= (rec.height()-(gornjiDX+ donjiDX))-geOstalo.x;
		 DXSl=(100-(gornjiDXposto+ donjiDXposto))-geOstalo.x2;
		gornji=new RectF(rec.left,rec.centerY()-gornjiDX-DX/2, rec.right, rec.centerY()-DX/2);
		donji=new RectF(rec.left,rec.centerY()+DX/2, rec.right, rec.centerY()+ donjiDX+DX/2);
		centar=new RectF( rec.left,rec.centerY()-DX/2, rec.right,rec.centerY()+DX/2);
	}
	this.nacrtjDioBitmapa(can, brSl,new RectF(0, 0, 100,  gornjiDXposto),gornji,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	if(smotajIliUmanjiSredisnji ){this.nacrtjDioBitmapa(can, brSl,new RectF(0, gornjiDXposto+geOstalo.x2/2, 100, 100-donjiDXposto-geOstalo.x2/2),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);}
	else this.nacrtjDioBitmapa(can, brSl,new RectF(0, donjiDXposto, 100, 100-gornjiDXposto),centar,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	this.nacrtjDioBitmapa(can, brSl,new RectF( 0,100-donjiDXposto,100, 100),donji,kutZaPomaknut,centar.centerX(),centar.centerY(), p);
	
	geOstalo.vrijeme1=System.currentTimeMillis();
	if(b)geOstalo.jesamLiZiv=true;
	return b;
}


public boolean animacijaPovecajISmanjiUOdnosuNaSliciceKontinuirano(Canvas can,RectF rectCrt, int brSl, int brR,int brSlDoMaxPovecanja, int brSlDoMaxSmanjenja, float postoPovecanje, float postoSmanjenje,float fps){
	boolean b=false;
	tick++;
	float stopaPovecanja=postoPovecanje/brSlDoMaxPovecanja;
    float stopaSmanjenja=postoSmanjenje/brSlDoMaxSmanjenja;
	if(tick>=fps/this.brojPrikazaPoSekundi(brSl)){//ako je doslo vrijeme za promjenu slicice
		tick=0;
	    this.tempRect.set(rectCrt);
	    this.tempBrStupca[brSl][brR]++;
        if(this.tempBrStupca[brSl][brR]<brSlDoMaxPovecanja){
    	    this.povecajRectF(stopaPovecanja*this.tempBrStupca[brSl][brR], stopaPovecanja*this.tempBrStupca[brSl][brR], tempRect);
    	    int i=this.tempBrStupca[brSl][brR];
    	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
    	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
    	    povecanje=true;
    	    smanjenje=false;
          }
         else if(this.tempBrStupca[brSl][brR]<brSlDoMaxPovecanja+brSlDoMaxSmanjenja){
    	    this.povecajRectF(stopaPovecanja*(brSlDoMaxPovecanja-1)-stopaSmanjenja*(this.tempBrStupca[brSl][brR]-brSlDoMaxPovecanja+1),stopaPovecanja*(brSlDoMaxPovecanja-1) -stopaSmanjenja*(this.tempBrTaktova-brSlDoMaxPovecanja+1), tempRect);
    	    int i=this.tempBrStupca[brSl][brR];
    	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
    	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
    	    povecanje=false;
    	    smanjenje=true;
          }
         else {
        	 int i=this.tempBrStupca[brSl][brR];
     	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
     	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
     	   tempBrStupca[brSl][brR]=-1;//zbog toga sto bi morao postaviti iza ofvih ifova sto znaci da ce se medu prikazno razdoblje u jednom ciklusu  jedanput pogresno povecavati i smanjivati
    	    povecanje=false;
    	    smanjenje=false;
         	b=true;
          }
        
       }
	else {//ako nije vrijme za promjenu slicice
		float fpsNeisk=fps/this.brojPrikazaPoSekundi(brSl);
		if(this.povecanje){
			this.povecajRectF(stopaPovecanja/fpsNeisk, stopaPovecanja/fpsNeisk, tempRect);
    	    int i=this.tempBrStupca[brSl][brR];
    	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
    	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
		}
		else if(this.smanjenje){
			 this.povecajRectF(-stopaSmanjenja/fpsNeisk, -stopaSmanjenja/fpsNeisk, tempRect);
	    	    int i=this.tempBrStupca[brSl][brR];
	    	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	    	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
		}
		else{   int i=this.tempBrStupca[brSl][brR];
		   if(tempBrStupca[brSl][brR]<0) i=0;
		   else if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
		   this.nacrtajSprite(can, brSl, i, brR, tempRect);
		   }
	}
  return b;
} 
public boolean animacijaPovecajISmanjiUOdnosuNaTaktove(Canvas can,RectF rectCrt, int brSl, int brR,int brTaktDoMaxPovecanja, int brTaktDoMaxSmanjenja, float postoPovecanje, float postoSmanjenje,boolean sljedecaSlicica ){
	boolean b=false;
	 if(tempBrStupca[brSl][brR]<0) tempBrStupca[brSl][brR]=0;
	 float stopaPovecanja=postoPovecanje/brTaktDoMaxPovecanja;
	 float stopaSmanjenja=postoSmanjenje/brTaktDoMaxSmanjenja;
     /////////////
	 this.tempRect.set(rectCrt);
     if(this.tempBrTaktova<brTaktDoMaxPovecanja){
    	
    	   this.povecajRectF(stopaPovecanja*this.tempBrTaktova, stopaPovecanja*this.tempBrTaktova, tempRect);
          }
     else if(this.tempBrTaktova<brTaktDoMaxPovecanja+brTaktDoMaxSmanjenja){
    	 //  this.tempRect.set(rectCrt);
    	    this.povecajRectF(stopaPovecanja*(brTaktDoMaxPovecanja-1)-stopaSmanjenja*(this.tempBrTaktova-brTaktDoMaxPovecanja+1),stopaPovecanja*(brTaktDoMaxPovecanja-1) -stopaSmanjenja*(this.tempBrTaktova-brTaktDoMaxPovecanja+1), tempRect);
          }
     else {
    	     if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	         this.nacrtajSprite(can, brSl, tempBrStupca[brSl][brR], brR, tempRect);
        	 tempBrTaktova=0;
         	 b=true;
       }
     /////////////
    if(!b){ 
    	if(sljedecaSlicica){//ako je doslo vrijeme za promjenu slicice
    		if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	        this.nacrtajSprite(can, brSl,tempBrStupca[brSl][brR], brR, tempRect);
	       this.tempBrStupca[brSl][brR]++;
           }
	    else {//ako nije vrijme za promjenu slicice
		    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) tempBrStupca[brSl][brR]=0;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	        this.nacrtajSprite(can, brSl,tempBrStupca[brSl][brR], brR, tempRect);
	     }
    }
  this.tempBrTaktova++;
  return b;//
} 
public boolean animacijaPovecajISmanjiUOdnosuNaSlicice(Canvas can,RectF rectCrt, int brSl, int brR,int brSlDoMaxPovecanja, int brSlDoMaxSmanjenja, float postoPovecanje, float postoSmanjenje,boolean sljedecaSlicica ){
	boolean b=false;
	if(sljedecaSlicica){//ako je doslo vrijeme za promjenu slicice
		this.tempBrStupca[brSl][brR]++;
	    this.tempRect.set(rectCrt);
	    float stopaPovecanja=postoPovecanje/brSlDoMaxPovecanja;
	    float stopaSmanjenja=postoSmanjenje/brSlDoMaxSmanjenja;
        if(this.tempBrStupca[brSl][brR]<brSlDoMaxPovecanja){
    	    this.povecajRectF(stopaPovecanja*this.tempBrStupca[brSl][brR], stopaPovecanja*this.tempBrStupca[brSl][brR], tempRect);
    	    int i=this.tempBrStupca[brSl][brR];
    	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
    	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
          }
         else if(this.tempBrStupca[brSl][brR]<brSlDoMaxPovecanja+brSlDoMaxSmanjenja){
    	    this.povecajRectF(stopaPovecanja*(brSlDoMaxPovecanja-1)-stopaSmanjenja*(this.tempBrStupca[brSl][brR]-brSlDoMaxPovecanja+1),stopaPovecanja*(brSlDoMaxPovecanja-1) -stopaSmanjenja*(this.tempBrTaktova-brSlDoMaxPovecanja+1), tempRect);
    	    int i=this.tempBrStupca[brSl][brR];
    	    if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
    	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
          }
         else {
        	 tempBrStupca[brSl][brR]=-1;
         	b=true;
          }
        
       }
	else {//ako nije vrijme za promjenu slicice
		int i=this.tempBrStupca[brSl][brR];
		 if(tempBrStupca[brSl][brR]<0) i=0;
		   else if(this.brojStupaca[brSl]-1<this.tempBrStupca[brSl][brR]) i=brojStupaca[brSl]-1;// u slucaju da je doslo do pogreske u postavljanju dase ipak prikazuje nesto
	    this.nacrtajSprite(can, brSl, i, brR, tempRect);
	}
  return b;
} 

////radne metode
private void translatirajRectF(float transX,float transY, RectF rect){
	rect.set(rect.left+transX,rect.top+transY, rect.right+transX, rect.bottom+transY);
}
private void povecajRectF(float postoX,float postoY, RectF rect){
	float xOfset=(rect.width()/100)*postoX;
	float yOfset=(rect.height()/100)*postoY;
	rect.set(rect.left-xOfset/2,rect.top-yOfset/2,rect.right+xOfset/2,rect.bottom+yOfset/2);
}
private void povecajRectPix(float pixX,float pixY, RectF rect){

	rect.set(rect.left-pixX/2,rect.top-pixY/2,rect.right+pixX/2,rect.bottom+pixY/2);
}
////////////////////////////////
//setersi
public void setBrojPrikazaPoSekundi(int brSlike, float brPrikPoSec){ prikPoSec[brSlike]=brPrikPoSec;}
////getersi
public boolean postojiLiSlikNaMjestu(int mjesto){
	boolean b=false;
	if(this.brojBitmapova>mjesto)if(this.slike[mjesto]!=null) b=true;
	else b=false;
	return b;
}
public int brojSlika(){return brojBitmapova;}
public int brojRedova(int brSlike){return brojRedova[brSlike];}
public int brojStupaca(int brSlike){return brojStupaca[brSlike];}
public int brojStupacaDod(int brSlike){return this.brojStupacaDod[brSlike];}
public int redBrPocDodatDjela(int brSlike){return this.redBrDodat[brSlike];}
public float brojPrikazaPoSekundi(int brSlike){ return prikPoSec[brSlike];}
/////recycle
public void reciklirajSveBitmapove(){// reciklira sve bitmapove da bi se moga ugasit activiti
	int i=0;
	recyclePozvan=true;
 while(this.brojBitmapova>i){
	 if(slike[i]!=null){
		 slike[i].recycle();
		 slike[i]=null;
	 }
	 i++;
 }
}
//////
////////metode za kloniranje
public void inicijalizirajSveListeIzKonstrukltora(SpriteHendler original){
	this.soundPool=soundPool;
	
	/*zvukLista= (int[][][] )clone.zvukLista.clone();
	slike= new Bitmap[maxBrojBitmapova];
	sirinaSlike= new float[maxBrojBitmapova];
	visinaSlike=new float[maxBrojBitmapova];
	brojRedova=new int[maxBrojBitmapova];
	brojStupaca=new int[maxBrojBitmapova];
	brojStupacaDod=new int[maxBrojBitmapova];
	sirManjeSlike=new float[maxBrojBitmapova];
	sirManjeSlikeDod=new float[maxBrojBitmapova];
	visManjeSlike=new float[maxBrojBitmapova];
	prikPoSec=new float[maxBrojBitmapova];
	zadnjaSlicicaZbroj=new int[maxBrojBitmapova][100];
	rec=new Rect();
	tekPoceoSvitak=new boolean[maxBrojBitmapova][100];
	for(int i=0; i<tekPoceoSvitak.length;i++){
		for(int j=0; j<tekPoceoSvitak[i].length;j++){
			tekPoceoSvitak[i][j]=true;
		}
	}
	vrijemePocetkaSvitak=new double[maxBrojBitmapova][100];
	
	tekPoceoTranslacijom=new boolean[maxBrojBitmapova][100];
	for(int i=0; i<tekPoceoTranslacijom.length;i++){
		for(int j=0; j<tekPoceoTranslacijom[i].length;j++){
			tekPoceoTranslacijom[i][j]=true;
		}
	}
	vrijemePocetkaTranslacije=new double[maxBrojBitmapova][100];
	
	this.tekPoceoRotacijom=new boolean[maxBrojBitmapova][100];
	for(int i=0; i<tekPoceoRotacijom.length;i++){
		for(int j=0; j<tekPoceoRotacijom[i].length;j++){
			tekPoceoRotacijom[i][j]=true;
		}
	}
	vrijemePocetkaRotacije=new double[maxBrojBitmapova][100];
	
	this.tekPoceoUvecanja=new boolean[maxBrojBitmapova][100];
	for(int i=0; i<tekPoceoUvecanja.length;i++){
		for(int j=0; j<tekPoceoUvecanja[i].length;j++){
			tekPoceoUvecanja[i][j]=true;
		}
	}
	vrijemePocetkaUvecanja=new double[maxBrojBitmapova][100];
	
	
	tempKutZaPomaknut=new float[maxBrojBitmapova][100];
	tempBrStupca=new int[maxBrojBitmapova][100];
	for(int i=0; i<tempBrStupca.length;i++){
		for(int j=0; j<tempBrStupca[i].length;j++){
			tempBrStupca[i][j]=-1;
		}
	}
	*/
	this.tekPoceoSvitak=null;
	
	tekPoceoTranslacijom=null;
	
	this.tekPoceoTranslacijom=null;
	tempUkupnoPovZaX=0;
	tempUkupnoPovZaY=0;
	
	this.tekPoceoRotacijom=null;
	
	tekPoceoAlfa=null;
	vrijemePocetkaAlfa=null;
	tekPoceoAlfaReda=null;
	staoNaSliciciAlfaReda=null;
	/*
	tekPoceoAlfa=new boolean[original.tekPoceoAlfa.length][100][100];
	
	for(int i=0; i<tekPoceoAlfa.length;i++){
		for(int j=0; j<tekPoceoAlfa[i].length;j++){
			for(int k=0;k<tekPoceoAlfa[i][j].length;k++){
				tekPoceoAlfa[i][j][k]=false;
			}
		}
	}
	vrijemePocetkaAlfa=new double[original.tekPoceoAlfa.length][100][100];
	
	this.tekPoceoAlfaReda=new boolean[original.tekPoceoAlfaReda.length][100];
	for(int i=0; i<tekPoceoAlfaReda.length;i++){
		for(int j=0; j<tekPoceoAlfaReda[i].length;j++){
			tekPoceoAlfaReda[i][j]=true;
		}
	}
	vrijemePocetkaAlfaReda=new double[original.tekPoceoAlfaReda.length][100];
	staoNaSliciciAlfaReda=new int[original.tekPoceoAlfaReda.length][100];
	
	pojacanje= new float[maxBrojZvukova+1];
	sansaZaZvuk=new int[maxBrojZvukova+1];
	randomPojacanjeDo=new float[maxBrojZvukova+1];
	listaKodova= new int[maxBrojZvukova+1];
	redBrDodat=new int[maxBrojBitmapova];
	listaPrioriteta=new int[maxBrojZvukova+1];
	aliasedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	listaListaKodova= new int[maxBrojZvukova+1][];
	this.brojBitmapova=maxBrojBitmapova;
	dodatniStupci=new boolean[maxBrojBitmapova];
	tempRect=new RectF();*/
}
@Override
protected Object clone() throws CloneNotSupportedException {
	SpriteHendler cloned =(SpriteHendler)super.clone();
	 cloned.inicijalizirajSveListeIzKonstrukltora( this);
return cloned;
}

}
