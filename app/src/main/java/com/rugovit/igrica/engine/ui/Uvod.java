package com.rugovit.igrica.engine.ui;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.rugovit.igrica.IgricaActivity;
import com.rugovit.igrica.MapActivity;

public class Uvod extends SurfaceView implements Runnable{
	 ///////pozadina
	   private Bitmap slikaPozadine;
	   private RectF recCrtPoz;
	   private Rect recRezPoz;
	   //taskbar
	   private Taskbar task;
	   ///
	   private boolean canvasPreStarting=true;// za stvaranja i vracanja canvasa kroz metodu getCanvas prije pokretanja igrice, tj za onceate metodu
	   private UIManagerObject tempSort;
	   private UIManagerObject tempSortNext;
	   private LinkedList<UIManagerObject> lista;
	   private float PpCmX;
	   private float PpCmY;
	   private long timeOfTouch;// vrijeme kada je ekran dodirnut
	   private float touchX,touchY,povlX=-10000,povlY=-10000,staroPovlX=-10000,staroPovlY=-10000,dxPovl,dyPovl,apsPovlX,apsPovlY ;
	   private boolean povlaciSe=false;// kada se krene sa povlaèenjem , kada se prst digne trebalo bi ponovno biti false
	   private int indexRun;
	   private SurfaceHolder holder=null;
	   public Canvas can; 
	   private UIManagerObject tempOznacenObj; //trenutno oznaèen objekt
	   public Thread th; //tread od klase
	   private int fps, threadWait;
	   private boolean isTouched=false;
	   private boolean runBit=true;
	   private boolean runLista=false;
	   private SpriteHendler slike;
	   private double  vrijemePrikaza[];
	   private int zadnjaPrikazana=0;
	   private double tempZadVrijeme=0;
	   private int brStaze;
	   private boolean tekPocSaCrt=true;
	   private RectF recCrt;
	
	   /////////
	     private IgricaActivity ac;
	     private MapActivity mAc;
		 private SpriteHendler spriteHend;
	public Uvod(Context context,int fps,SpriteHendler spriteHend,IgricaActivity ac){
		   super(context);
		   this.spriteHend=spriteHend;
		   this.fps=fps;
		   this.ac=ac;
		   threadWait=1000/fps; //varijabla ide u run thread sleep metodu i regulira broj crtanja po sekundi 
		   holder=getHolder();
		   pokreniGlavnuPetlju("Uvod");
		   
	   }
	public Uvod(Context context,int fps,IgricaActivity ac, int brStaze){
		   super(context);
		   this.fps=fps;
		   this.ac=ac;
		   this.brStaze=brStaze;
		   threadWait=1000/fps; //varijabla ide u run thread sleep metodu i regulira broj crtanja po sekundi 
		   holder=getHolder();
		   pokreniGlavnuPetlju("Uvod");
		   recCrt= new RectF();
	   }
	public Uvod(Context context,int fps,MapActivity ac,int brStaze){
		   super(context);
		   this.brStaze=brStaze;
		   this.fps=fps;
		   this.mAc=ac;
		   threadWait=1000/fps; //varijabla ide u run thread sleep metodu i regulira broj crtanja po sekundi 
		   holder=getHolder();
		   pokreniGlavnuPetlju("Uvod");
		   
	   }
/////////////BILDER///////////////////////////////////////////////

public void dodajSlikuUvoda(SpriteHendler slike, double vrijeme[]){
	this.slike=slike;
	vrijemePrikaza=vrijeme;
}
///////////////////////////////////////////////////////////////////	
/////////////RUN//////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		//try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}// obje threda mi se prestanu vrtjet bez ovoga, neznam zašto
		long dinThreadWait=threadWait;
		long timeMili=threadWait;
		while(runBit){
			
			///////////dinamièka brzina	
			 if(threadWait<timeMili){// u sluèaju da se petlja izvršava dulje od
			    	dinThreadWait=2*threadWait-timeMili;
			           }
		    else if(threadWait>timeMili){// u sluèaju da se petlja izvršava kraæe od
		    	dinThreadWait=2*threadWait-timeMili;
	        }
		    if(dinThreadWait<20) dinThreadWait=20;// u sluèaju da izraèuna prekratko vrijeme
		    if(dinThreadWait>500) dinThreadWait=400;
		    timeMili=System.currentTimeMillis();
		    //////////////////////////
			
		    if(!holder.getSurface().isValid()) continue;
			can=holder.lockCanvas();
			if(this.tekPocSaCrt){
				if( recCrt!=null) recCrt.set(0, 0, can.getWidth(), can.getHeight());// stavlja recCrt na cijeli ekran
			}
			 if(can!=null){//ovo sam ubacio zbog toga što kada treba prekinut thread prilikom pritiska na strelicu nazad can se uništi prije nego završi thread, uvijek je bila greška pri prvom pozivu
			    
				if(this.slikaPozadine!=null&&runBit) can.drawBitmap(slikaPozadine, null, recCrtPoz, null);    
				else can.drawARGB(250, 250, 250, 32);
				if(nacrtajUvod()) zavrsiSaUvodom();
			  } 
		    holder.unlockCanvasAndPost(can);
			try{Thread.sleep(dinThreadWait);}catch(InterruptedException e){e.printStackTrace();}
			timeMili=System.currentTimeMillis()-timeMili;// uzima razliku od poèetka petlje tj. vrijeme izvoðenja petlje
			}
		
	}	
	////privatne metode
	private void zavrsiSaUvodom(){
		ac.efekSir=can.getWidth();
		ac.efekVis=can.getHeight();
		if(ac!=null) ac.stvoriMapu();
		//else if(this.mAc!=null) mAc.stvoriNovuFazu(brStaze,IDKoristeneFaze);
		ac=null;
		mAc=null;
		runBit=false;
	}
	private boolean nacrtajUvod(){
		boolean b=false;
		if(tekPocSaCrt){
			this.tempZadVrijeme=System.currentTimeMillis();
			tekPocSaCrt=false;
		}
		 if(slike!=null){  
			 if(slike.brojSlika()<=this.zadnjaPrikazana) {
		       if(this.tempZadVrijeme+this.vrijemePrikaza[zadnjaPrikazana]<System.currentTimeMillis()) {
			       zadnjaPrikazana++;
			        this.slike.nacrtajSprite(can, zadnjaPrikazana, 0, 0, recCrt);
			        tempZadVrijeme=System.currentTimeMillis();
		          }
		       else this.slike.nacrtajSprite(can, zadnjaPrikazana, 0, 0, recCrt);
		      }
		     else b=true;
		 }
		 else b=true;// prekida se iscrtavanje
		 return b;
	}
	///public metode
	public void stvoriPozadinu(Bitmap poz, float sirCrt, float visCrt){
		this.slikaPozadine=poz;
		float sir=sirCrt*this.PpCmX;
		float vis=visCrt*this.PpCmY;
		this.recCrtPoz= new RectF(0,0,sir,vis);
		//this.recCrtPoz= new RectF(0,0,sirCrt*this.PpCm,visCrt*this.PpCm);
	}

	public void ugasiUvod(){
		 runBit=false;
		 
	}
	public void pokreniGlavnuPetlju(String name){
		runBit=true;
		th=new Thread(this);
		th.setName(name);
	    th.start();
	}
}
