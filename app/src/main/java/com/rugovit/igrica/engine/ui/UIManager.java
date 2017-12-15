package com.rugovit.igrica.engine.ui;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;



import android.graphics.Bitmap;
import android.graphics.Color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

import com.rugovit.igrica.engine.logic.GameEvent;
import com.rugovit.igrica.engine.logic.GameLogic;
import com.rugovit.igrica.engine.ui.elements.IzbornikUniverzalni;

public  class UIManager extends SurfaceView implements OnTouchListener,Runnable {
	////KONSTANTE
	private float marginaLijeva, marginaDesna,marginaGore,marginaDolje;
	private float kasnjGameThredToucha=80;
	private Bitmap temCanvasBitmap,tempKopiranCanvasBitmap;
   
	/////////////////
    private float xDodiraZadnjeg,yDodiraZadnjeg;
	private  Paint paintDinDjela,paintNadDinDjela,paintIznadTexture;
   private Path zajednickaSjena;
	private  boolean jesamLiBestredni=false;
   private boolean ukljuceniTouchEventi=true;
   ///////pozadina
   private Bitmap slikaPozadine;
   private RectF recCrtPoz;
   private boolean canLocked=false;
   ///////tekstura
   private Bitmap slikaTeksture;
   private RectF recCrtTeksture;
   private Paint paintTeksture;
   ///////////////
   private boolean neCrtajPauze=false;
   private boolean systemPauze=false;
   //varijable simulacije pauze
   private int brBlura;
   private float sirBmp,visBmp;
   private float koefSmanjPoz=4;
   private float vrijemeAnimPauze=0.3f;
   private boolean gotovZoomPauze=false;
   private GameEvent gePauze=new GameEvent(null);
   private float xPostoRazlPauze,yPostoRazlPauze;
   private float xPauzeZaZoom,yPauzeZaZoom;
   private RectF recPauzePomak=new RectF();
   //taskbar
   private Taskbar task;
   ///
   public GameLogic GL ;
   private  float sirEkrana,visEkrana, duzinaToucha;
   private boolean loadiraj=true;
   private boolean tekPoceo=true;
   private UIManagerObject tempSort;
   private UIManagerObject tempSortNext;
  // private LinkedList<UIManagerObject> lista;
   private LinkedList<UIManagerObject> listaTemeljna;
   private LinkedList<UIManagerObject> listaDinamicna;
   private LinkedList<UIManagerObject> listaKrovna;
   private LinkedList<UIManagerObject> listaObjekataIznadTeksture;
   private float PpCmX;
   private float PpCmY;
 
   private int gustEkr=0;
   private long timeOfTouch;// vrijeme kada je ekran dodirnut
   private float touchX,touchY,povlX=-10000,povlY=-10000,staroPovlX=-10000,staroPovlY=-10000,dxPovl,dyPovl,apsPovlX,apsPovlY ;
   private boolean povlaciSe=false;// kada se krene sa povlaèenjem , kada se prst digne trebalo bi ponovno biti false
   private int indexRun;
 
   private SurfaceHolder holder=null;
   public Canvas can; 
   private UIManagerObject tempOznacenObj; //trenutno oznaèen objekt
   private Thread th; //tread od klase
   private int  threadWait;
   private float fps;
   private float xPrviDodir,yPrviDodir;
   public boolean stvorenPauzeBitmap=false;
   private boolean isTouched=false,ignorirajOnUp=false, isPressed=false;
   private boolean runBit=true;
   private boolean runLista=false;
   private boolean zaustaviDokNeUbacimUListu=false;
   private LinkedList<UIManagerObject> listaDodaObj;//spremaju se objekti koji trebaju biti dodani
   private LinkedList<UIManagerObject> listaMakniObj;// spremaju se objekti koji bi trebali biti oduzeti
   private LinkedList<Integer> listaDodaInt;//sprema se index(1.2 ili3) za sortiranje 
   private int iZadStat=-1;// index zadnjeg statiènog objekta važno je da se ne provjerava preklapanje prije ovog
   private int iZadDina=-1;// zadnji inamièki objekrr tu listi, tj, zadnji kojem se provjerava preklapanje
   private boolean provjeraPovlacenja=true;
   public boolean onBackPressJeNormalan=false;// ako je normalan back botun ce raiti normalno ako ne slati ce poruku trenutaccnom univerrzalnom izborniku
   private IzbornikUniverzalni tempUniverzalniIzbornik;
   public Context context;
   
   private boolean zahtjevZaPause=false, odobrenaPauza=false;
   public UIManager(Context context,int fps,float PpCmX,float PpCmY ,float sirEkrana,float visEkrana,String threadName){
	   super(context);
	   setLayerType(View.LAYER_TYPE_HARDWARE, null);
	   this.context= context;
	   this.PpCmX=PpCmX;
	   paintDinDjela=new Paint();
	   paintNadDinDjela=new Paint();
	   paintIznadTexture=new Paint();
	 
		 this.PpCmY=PpCmY;
		// lista=new LinkedList<UIManagerObject>(); 
	   listaTemeljna=new LinkedList<UIManagerObject>(); // nemogu direktno poslat this.lista=lista jer kad salje null lista uopæe nebi postojala u ovoj klasi
	   listaDinamicna=new LinkedList<UIManagerObject>(); // nemogu direktno poslat this.lista=lista jer kad salje null lista uopæe nebi postojala u ovoj klasi
	   listaKrovna=new LinkedList<UIManagerObject>(); // nemogu direktno poslat this.lista=lista jer kad salje null lista uopæe nebi postojala u ovoj klasi
	   listaObjekataIznadTeksture=new LinkedList<UIManagerObject>();
	   this.setOnTouchListener(this);
	   this.fps=fps;
	 
	   this.visEkrana=visEkrana;
		 this.sirEkrana=sirEkrana;
	   threadWait=1000/fps; //varijabla ide u run thread sleep metodu i regulira broj crtanja po sekundi 
	   holder=getHolder();
	   pokreniGlavnuPetlju(threadName);
	   listaDodaObj= new LinkedList<UIManagerObject>();
	   listaDodaInt= new LinkedList<Integer>();
	   listaMakniObj= new LinkedList<UIManagerObject>();
	   duzinaToucha=(visEkrana+sirEkrana)/2;
	   duzinaToucha=duzinaToucha/20;
	   
   }
   public UIManager(Context context,float PpCmX,float PpCmY,float sirEkrana,float visEkrana ){// bestredni konstruktor
	   super(context);
	   this.context= context;
	   paintIznadTexture=new Paint();
		 this.visEkrana=visEkrana;
		 this.sirEkrana=sirEkrana;
	   paintDinDjela=new Paint();
	   paintNadDinDjela=new Paint();
	   this.PpCmX=PpCmX;
	 this.PpCmY=PpCmY;
	   //lista=new LinkedList<UIManagerObject>(); // nemogu direktno poslat this.lista=lista jer kad salje null lista uopæe nebi postojala u ovoj klasi
	   this.setOnTouchListener(this);
	   listaTemeljna=new LinkedList<UIManagerObject>(); // nemogu direktno poslat this.lista=lista jer kad salje null lista uopæe nebi postojala u ovoj klasi
	   listaDinamicna=new LinkedList<UIManagerObject>(); // nemogu direktno poslat this.lista=lista jer kad salje null lista uopæe nebi postojala u ovoj klasi
	   listaKrovna=new LinkedList<UIManagerObject>(); // 
	   holder=getHolder();
	   listaDodaObj= new LinkedList<UIManagerObject>();
	   listaDodaInt= new LinkedList<Integer>();
	   listaMakniObj= new LinkedList<UIManagerObject>();
	   jesamLiBestredni=true;
	   listaObjekataIznadTeksture=new LinkedList<UIManagerObject>();
	   duzinaToucha=(visEkrana+sirEkrana)/2;
	   duzinaToucha=duzinaToucha/20;
   }

@Override
public boolean onTouch(View arg0, MotionEvent e) {
	switch(e.getAction()){
	case MotionEvent.ACTION_DOWN:  if(!isTouched){
		
		                           timeOfTouch=System.currentTimeMillis();// sprema trenutak toga dodira
		                           isTouched=true;// oznaèava da je dodirnut ekran
		                           xPrviDodir=e.getX();
		                           yPrviDodir=e.getY();
		                           isPressed=false;
		                       
	                                }
	                               
                                  
	                             
	break;
	case MotionEvent.ACTION_MOVE:// if(System.currentTimeMillis()-timeOfTouch>400){// pošto se ova i gornja istovremeno izvršavajau trebam èekat odreðeno vrijeme da vidim jeli bio dodir ili povlaèenje
		                                  
		                                  povlX=e.getX();
	                                      povlY=e.getY();
	                                      
	                                      if(this.udaljenostDvijeTocke(povlX, povlY, xPrviDodir, yPrviDodir)>this.duzinaToucha){
	                                    	   ignorirajOnUp=true; 
	                                    	   isTouched=false;
	                                           povlaciSe=true;// oznaèava da se povlaèi
	                                           timeOfTouch=0;
	                                           isPressed=true;
	                                      }
	                                       else if(!isPressed&&
	                                    		   System.currentTimeMillis()-this.timeOfTouch>kasnjGameThredToucha){
	                                    	 touchMe(e.getX()-apsPovlX,e.getY()-apsPovlY);//traženje na koji je objekt klikknutotouchMe(e.getX()-apsPovlX,e.getY()-apsPovlY);//traženje na koji je objekt klikknuto
	  		                                 touchKoordinate(e.getX()-apsPovlX,e.getY()-apsPovlY); //jednostavno spremanje koordinata i slanje ako postoji tempTouchedobj
	                                             /// važan redosljed za efekt klika pokraj prozora, naime trebao bi se ugasit prozor
	  		                                 isTouched=false; 
	  		                                 povlaciSe=false;
		                                     povlX=-10000;// poništava sve vrijednosti 
		                                     povlY=-10000;
		                                     staroPovlX=-10000;
		                                     staroPovlY=-10000;
		                                     ignorirajOnUp=true;
		                                     isPressed=true;
		                                     
		                                 
		                                    
	                                      }
	                              
	                              //  }
	break;
	case MotionEvent.ACTION_UP:  if(!povlaciSe){// povlaèi se znaèi ovo se ne izvršava
		                            if(ignorirajOnUp&&!isPressed){     touchMe(e.getX()-apsPovlX,e.getY()-apsPovlY);//traženje na koji je objekt klikknutotouchMe(e.getX()-apsPovlX,e.getY()-apsPovlY);//traženje na koji je objekt klikknuto
		                                 touchKoordinate(e.getX()-apsPovlX,e.getY()-apsPovlY); //jednostavno spremanje koordinata i slanje ako postoji tempTouchedobj
                                           /// važan redosljed za efekt klika pokraj prozora, naime trebao bi se ugasit prozor
		                                 isTouched=false; 
		                                 isPressed=true;
		                                 
		                   
		                                    }  
	                                     
	                                     }
		                                 povlaciSe=false;
	                                     povlX=-10000;// poništava sve vrijednosti 
	                                     povlY=-10000;
	                                     staroPovlX=-10000;
	                                     staroPovlY=-10000;
	                                    
	                                     ignorirajOnUp=false;
	break;
	
	                                
	}
   
	/*if(e.getAction()==MotionEvent.ACTION_DOWN){
	touchKoordinate(e.getX(),e.getY()); //jednostavno spremanje koordinata
    touchMe(e.getX(),e.getY());//traženje na koji je objekt klikknuto
    }
 else if(e.getAction()==MotionEvent.ACTION_MOVE){
	povlX=e.getX();
    povlY=e.getY();
    povlaciSe=true;
    
    }*/
	return true;
}
private void obradiTouchUPetlji(){

	if(System.currentTimeMillis()-this.timeOfTouch>
	kasnjGameThredToucha&&!isPressed){
    	 touchMe(xPrviDodir-apsPovlX,yPrviDodir-apsPovlY);//traženje na koji je objekt klikknutotouchMe(e.getX()-apsPovlX,e.getY()-apsPovlY);//traženje na koji je objekt klikknuto
         touchKoordinate(xPrviDodir-apsPovlX,yPrviDodir-apsPovlY); //jednostavno spremanje koordinata i slanje ako postoji tempTouchedobj
         /// važan redosljed za efekt klika pokraj prozora, naime trebao bi se ugasit prozor
            
          isTouched=false;
          povlaciSe=false;
          povlX=-10000;// poništava sve vrijednosti 
          povlY=-10000;
          staroPovlX=-10000;
          staroPovlY=-10000;
       
          isPressed=true;
          ignorirajOnUp=true;
          
         
    }
}
/////////////BETHREDNI RUN////////////////////////////////////////////////////////////////////////////////
public void runBestredni(float fps, GameLogic GL) {
	//try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}// obje threda mi se prestanu vrtjet bez ovoga, neznam zašto
	can=null;
	if(loadiraj){
		loadMetoda();
		loadiraj=false;
	}
	UIManagerObject temp;
    this.GL=GL;
    obradiTouchUPetlji();
    if(this.jesamLiBestredni){
    	
    	//task.iskljuciCrtanjeNekihUPauzi(this.zahtjevZaPause);
    	
    	if(!odobrenaPauza){
    		task.iskljuciCrtanjeNekihUPauzi(false);
    		boolean b=true;
        	while(b){
        		if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
    		if(!holder.getSurface().isValid()&&!zaustaviDokNeUbacimUListu) continue;
    		b=false;
    	    //can.drawColor(34);   ovo nerqadi neznam zašto????
        	}
        	
        
    	    can=holder.lockCanvas();
	    	if(can!=null){//ovo sam ubacio zbog toga što kada treba prekinut thread prilikom pritiska na strelicu nazad can se uništi prije nego završi thread, uvijek je bila greška pri prvom pozivu
			
			if(tekPoceo) {
				this.stvariKojeSeIzvrsavajuSamoNaPocetku();
			    tekPoceo=false;
			}
			
			gustEkr=can.getDensity();
		
			
			if(systemPauze) {
				can.drawColor(Color.rgb(89, 88, 10));
			}
			glavnoCrtanje(can);
             
		     holder.unlockCanvasAndPost(can);
		    
		      
            }
     
           
		  } 
    	else if(!neCrtajPauze) {//  ako je dobio zahtjev za pauzomm
    
    		
    		//if(provjeraPovlacenja)provjeriPovlacenjeIPomakniCanvas();	
    	  ///////////////////////////////////////////////////////
            if(!stvorenPauzeBitmap){
            	double vrijemeLodiranja=System.currentTimeMillis();
       	  
            	brBlura=1;
            	 stvoriBitmapCanvasa();
            	 vrijemeLodiranja=(System.currentTimeMillis()-vrijemeLodiranja);
            	 Log.d("Vrijeme stvaranja bmp", "vrijeme lod=" +vrijemeLodiranja);
            	
            	 vrijemeLodiranja=System.currentTimeMillis();
            	//temCanvasBitmap=applyGaussianBlur(temCanvasBitmap);
            	 fastblur(this.temCanvasBitmap, 1);
            	 this.recPauzePomak.set(0,0, this.sirEkrana/this.koefSmanjPoz,this.visEkrana/this.koefSmanjPoz);
             	this.xPauzeZaZoom= this.xPostoRazlPauze*sirBmp/100;
             	this.yPauzeZaZoom= this.yPostoRazlPauze*visBmp/100;
             	gotovZoomPauze=false;
             //	recPauzePomak.set(recPauzePomak.left+xPauzeZaZoom/2, recPauzePomak.top+yPauzeZaZoom/2,recPauzePomak. right-xPauzeZaZoom/2, recPauzePomak.bottom-yPauzeZaZoom/2);
       
             	 this.gePauze.jesamLiZiv=true;
             	 
             	 vrijemeLodiranja=(System.currentTimeMillis()-vrijemeLodiranja);
            	 Log.d("Vrijeme blura", "vrijeme lod=" +vrijemeLodiranja);
            		dodavanjeBluraProgresivno(100);
            }
    		boolean b=true;
        	while(b){
        		if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
    		if(!holder.getSurface().isValid()&&!zaustaviDokNeUbacimUListu) continue;
    		b=false;
        	}
         	can=holder.lockCanvas();
         	//if(provjeraPovlacenja)provjeriPovlacenjeIPomakniCanvas();
         	can.translate(apsPovlX,apsPovlY);// pomicem rucno canvas tako da mi ne ra�?unna nove svapove
        	 ////////////////////////////////////////////////////
         	//can.setBitmap(null);
         
	        
    	  
    	    Rect recCrt=new Rect();
             
    	
    	    recCrt.set(-(int)this.apsPovlX, -(int)this.apsPovlY,(int) (-this.apsPovlX+this.sirEkrana), (int)(-this.apsPovlY+this.visEkrana));
    	    Rect recBmp=new Rect();
    	    float postoPovecanje=0;
            if(!gotovZoomPauze){
            	gotovZoomPauze=SpriteHendler.animacijaSlaganjeUvecanjeVremenska(gePauze, xPauzeZaZoom, yPauzeZaZoom, vrijemeAnimPauze, vrijemeAnimPauze, recPauzePomak);
            	if(xPauzeZaZoom>yPauzeZaZoom){
            		postoPovecanje=(recPauzePomak.width()-xPauzeZaZoom)/(recPauzePomak.width()/100);
            	}
            	else{
            		postoPovecanje=(recPauzePomak.height()-yPauzeZaZoom)/(recPauzePomak.height()/100);
            	}
            	
            }
            recBmp.set((int)recPauzePomak.left-(int)(this.apsPovlX/koefSmanjPoz), (int)recPauzePomak.top-(int)(this.apsPovlY/koefSmanjPoz), (int)(recPauzePomak.right)-(int)(this.apsPovlX/koefSmanjPoz),(int)recPauzePomak.bottom-(int)(this.apsPovlY/koefSmanjPoz));
    	    
            if(recBmp.left<0){
    	    	 recBmp.set(0, recBmp.top, recBmp.right-recBmp.left,recBmp.bottom);
    	    }
            float razlikaDesno=this.recCrtPoz.width()/this.koefSmanjPoz-recBmp.right;
    	    	if(razlikaDesno<0){
    	    		recBmp.set((int)(recBmp.left+razlikaDesno),recBmp.top, (int)(recBmp.right+razlikaDesno),recBmp.bottom);
    	    	
    	    }
    	    if(recBmp.top<0){
   	    	 recBmp.set(recBmp.left,0, recBmp.right,(int)recBmp.bottom-recBmp.top);
   	          }
    	    float razlikaLijevo=this.recCrtPoz.height()/this.koefSmanjPoz-recBmp.bottom;
	    	if(razlikaLijevo<0){
	    		recBmp.set(recBmp.left, (int)(recBmp.top+razlikaLijevo), recBmp.right,(int)(recBmp.bottom+razlikaLijevo));
	    	
	          }
	    
	   
            can.drawBitmap(this.temCanvasBitmap, recBmp, recCrt, null);
    		//task.iskljuciCrtanje(odobrenaPauza);
    	    task.iskljuciCrtanjeNekihUPauzi(true);
	        task.nacrtaj(can, fps,this,PpCmX,PpCmY,apsPovlX,apsPovlY);// stavio sam ga skroz iza tako da bude sigurno prvi
	    	
		     indexRun=0;
	    	 if(!listaObjekataIznadTeksture.isEmpty())  while(true){ 
	    		 if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije//pokreæe sve svoje objekte
				   if(indexRun>listaObjekataIznadTeksture.size()-1) break; //zanimljiva greška bila, mora sam stavit "-1" zato što indexsiranje liste polazi od 0 a velièina se normalno broji od 1 
				   temp=listaObjekataIznadTeksture.get(indexRun);
				   temp.nacrtaj(can, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);
				   
				   /////	crtanje sjene/////////
			        if(indexRun==listaObjekataIznadTeksture.size()-1){
			        	
			        if(can!=null)	can.drawPath(this.zajednickaSjena, this.paintIznadTexture);
			        	
			        }
			  ////////////////////////////////  
				   indexRun++;
			       }
	    	 
	     holder.unlockCanvasAndPost(can);
	   
    	}
    	 
		runLista=false;// dopušta ubacivanje elemenata u glavnu listu
		ubaciUListuElementeNaCekanju();// ubacuje zahtjeve koji su se nakupili dok se petlje izvodila
		izbaciIzListeElementeNaCekanju();// ista stvar samo za ubijnje objekata
    }
    if(this.zahtjevZaPause){
    	this.odobrenaPauza=true;
    	}
	else {
	   
		stvorenPauzeBitmap=false;
		this.odobrenaPauza=false;
	}
}
private void dodavanjeBluraProgresivno(float postoPovecanje){
	if(postoPovecanje>20&&postoPovecanje<40){
		 if(brBlura==1){
			 fastblur(this.temCanvasBitmap, brBlura);
			 brBlura++;
		 }
		
	}
	else if(postoPovecanje>40&&postoPovecanje<60){
		 if(brBlura<=2){
			 int i= 2-brBlura;
			 fastblur(this.temCanvasBitmap,i);
			 brBlura+=i;
		 }
		
	}
	else if(postoPovecanje>60&&postoPovecanje<80){
		 if(brBlura<=3){
			 int i= 3-brBlura;
			 fastblur(this.temCanvasBitmap,i);
			 brBlura+=i;
		 }
		
	}
	else if(postoPovecanje>80){
		 if(brBlura<=4){
			 int i= 4-brBlura;
			 fastblur(this.temCanvasBitmap,i);
			 brBlura+=i;
		 }
		
	}
	
}

////////////GLAVNO CRTANJE
public void glavnoCrtanje(Canvas tempCan){
	UIManagerObject temp;
	can=tempCan;
	if(provjeraPovlacenja)provjeriPovlacenjeIPomakniCanvas();
	if(this.slikaPozadine!=null&&runBit) tempCan.drawBitmap(slikaPozadine, null, recCrtPoz, null);    
	else tempCan.drawARGB(250, 0, 0, 0);
	runLista=true;// oznaèava da je lista krenula sa izvršavanjem  novi objekti se mogu dodati tek na kraju 
	indexRun=0;
	
	for(int i=0; i<5; i++){
	    sortirajListu();	
	 }
	 this.zajednickaSjena=new Path();
	 /* if(!lista.isEmpty()) 
    	while(true){   //pokreæe sve svoje objekte
	   if(indexRun>lista.size()-1) break; //zanimljiva greška bila, mora sam stavit "-1" zato što indexsiranje liste polazi od 0 a velièina se normalno broji od 1 
	 
	   temp=lista.get(indexRun);
	   temp.nacrtaj(tempCan, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);
	  /////	crtanje sjene/////////
	        if(this.iZadStat==this.indexRun){//crtanje sjene za  po�?etni dio liste
	        	Paint tempPaint = new Paint();
	        	if(GL!=null)if(GL.faIgr!=null)
				      {
				     tempPaint.setARGB(this.GL.faIgr.sjenaAlpha,this.GL.faIgr.sjenaRead,this.GL.faIgr.sjenaGreen,this.GL.faIgr.sjenaBlue);
				         tempPaint.setStyle(Paint.Style.FILL);
				         }
	        	tempCan.drawPath(this.zajednickaSjena, tempPaint);
	        	zajednickaSjena=new Path();// ponovno je postavljam za dinami�?ki dio liste
	              }
	        else if(this.iZadDina==this.indexRun){//crtanje sjene za  po�?etni dio liste
	        	
	        	tempCan.drawPath(this.zajednickaSjena, this.paintDinDjela);
	        	zajednickaSjena=new Path();// ponovno je postavljam za nad-dinami�?ki dio liste
	              }
	        else if(indexRun==lista.size()-1){
	        	
	        	tempCan.drawPath(this.zajednickaSjena, this.paintNadDinDjela);
	        	zajednickaSjena=new Path();// ponovno je postavljam za sljedeću listu
	        }
	  ////////////////////////////////      
	        
	   indexRun++;
       }*/
    /////TEMELJNA LISTA
    indexRun=0;
    if(!this.listaTemeljna.isEmpty()) 
    	while(true){ 
    		if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije//pokreæe sve svoje objekte
	   if(indexRun>listaTemeljna.size()-1) break; //zanimljiva greška bila, mora sam stavit "-1" zato što indexsiranje liste polazi od 0 a velièina se normalno broji od 1 
	 
	  listaTemeljna.get(indexRun).nacrtaj(tempCan, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);

	       
	        
	   indexRun++;
       }
    ////////sjena ispod dinamicne
    Paint tempPaint = new Paint();
	if(GL!=null)if(GL.faIgr!=null)
	      {
	     tempPaint.setARGB(this.GL.faIgr.sjenaAlpha,this.GL.faIgr.sjenaRead,this.GL.faIgr.sjenaGreen,this.GL.faIgr.sjenaBlue);
	         tempPaint.setStyle(Paint.Style.FILL);
	         }
	tempCan.drawPath(this.zajednickaSjena, tempPaint);
	zajednickaSjena=new Path();
    ///////////////
	
	
	
    ////////////DINAMICNA LIST
	indexRun=0;
    if(!this.listaDinamicna.isEmpty()) 
    	while(true){   //pokreæe sve svoje objekte
    		if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
	   if(indexRun>listaDinamicna.size()-1) break; //zanimljiva greška bila, mora sam stavit "-1" zato što indexsiranje liste polazi od 0 a velièina se normalno broji od 1 	 
	   listaDinamicna.get(indexRun).nacrtaj(tempCan, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);      
	   indexRun++;
       }
      ////////sjena ispod krovne
  
        tempCan.drawPath(this.zajednickaSjena, tempPaint);
        zajednickaSjena=new Path();
        ///////////////
        ////////////krovna lista
    	indexRun=0;
        if(!this.listaKrovna.isEmpty()) 
        	while(true){   //pokreæe sve svoje objekte
        		if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
    	   if(indexRun>listaKrovna.size()-1) break; //zanimljiva greška bila, mora sam stavit "-1" zato što indexsiranje liste polazi od 0 a velièina se normalno broji od 1 	 
    	   listaKrovna.get(indexRun).nacrtaj(tempCan, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);      
    	   indexRun++;
           }
       
       
        
    
        nacrtajDodatnoIznadGlavnePetlje(tempCan, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);	
        

        if(task!=null){
       
       	 task.nacrtaj(tempCan, fps,this,PpCmX,PpCmY,apsPovlX,apsPovlY);// stavio sam ga skroz iza tako da bude sigurno prvi
        }
        if(this.slikaTeksture!=null) tempCan.drawBitmap(slikaTeksture, null, recCrtTeksture, paintTeksture);
   
     

     indexRun=0;
     if(!listaObjekataIznadTeksture.isEmpty())  while(true){   //pokreæe sve svoje objekte
    	 if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
    	   if(indexRun>listaObjekataIznadTeksture.size()-1) break; //zanimljiva greška bila, mora sam stavit "-1" zato što indexsiranje liste polazi od 0 a velièina se normalno broji od 1 
		   temp=listaObjekataIznadTeksture.get(indexRun);
		   temp.nacrtaj(tempCan, fps,this, PpCmX,PpCmY,apsPovlX,apsPovlY);
		   /////	crtanje sjene/////////
	        if(indexRun==listaObjekataIznadTeksture.size()-1){
	        	
	        	
	        	
	        }
	  ////////////////////////////////  
		   indexRun++;
	       }
     

    ///////////////////////////////////////////
     
     
     
     
     
     
}
/////////////RUN//////////////////////////////////////////////////////////////////////////////////////////
@Override
public void run() { 
	int sort=0;
	//try{Thread.sleep(1000);}catch(InterruptedException e){e.printStackTrace();}// obje threda mi se prestanu vrtjet bez ovoga, neznam zašto
	long dinThreadWait=threadWait;
	long timeMili=threadWait;
    UIManagerObject temp;
    Looper.prepare();
    loadMetoda();
	while(runBit){
		
		///////////dinamièka brzina	
		
	    timeMili=System.currentTimeMillis();
	    //////////////////////////
	    obradiTouchUPetlji();
		if(!holder.getSurface().isValid()&&!zaustaviDokNeUbacimUListu) continue;
	    //can.drawColor(34);   ovo nerqadi neznam zašto????
		can=holder.lockCanvas();
		canLocked=true;
		if(runBit&&can!=null){//ovo sam ubacio zbog toga što kada treba prekinut thread prilikom pritiska na strelicu nazad can se uništi prije nego završi thread, uvijek je bila greška pri prvom pozivu
			
			runLista=true;// oznaèava da je lista krenula sa izvršavanjem  novi objekti se mogu dodati tek na kraju 
		
			glavnoCrtanje(can);
		     holder.unlockCanvasAndPost(can);
		     canLocked=false;
		  } 
		runLista=false;// dopušta ubacivanje elemenata u glavnu listu
		ubaciUListuElementeNaCekanju();// ubacuje zahtjeve koji su se nakupili dok se petlje izvodila
		izbaciIzListeElementeNaCekanju();// ista stvar samo za ubijnje objekata
		  timeMili=1+System.currentTimeMillis()-timeMili;// uzima razliku od poèetka petlje tj. vrijeme izvoðenja petlje
		  fps=1000/(timeMili+1  );  
		  dinThreadWait=threadWait- timeMili;
		    if(dinThreadWait<20) dinThreadWait=20;// u sluèaju da izraèuna prekratko vrijeme
		//	try{Thread.sleep(dinThreadWait);}catch(InterruptedException e){e.printStackTrace();}
			
		}
	if(th!=null)this.th=null;
	
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//////LOAD
public void loadMetoda(){
	
}
//////IZVAN THREADNO CRTANJE
public void nacrtajIzvanThreadno(){
	boolean b=true;
	while(b){
	
	if(!holder.getSurface().isValid()&&!zaustaviDokNeUbacimUListu) continue;
	b=false;

	}
	can=holder.lockCanvas();
	///////////
	//bug 9.
	if(can!=null)izvanThrednoZaNacrtati(can, fps,this,PpCmX,PpCmY,apsPovlX,apsPovlY);
    //////////
    holder.unlockCanvasAndPost(can);	
       }
  
public void izvanThrednoZaNacrtati(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){
    
	}
//////
///////setersi///////////////////////////////////////////////////////////////////////////////////////////// 
public void setMargine(float marginaLijeva,float  marginaDesna,float marginaGore,float marginaDolje){
	this.marginaLijeva=marginaLijeva;
	this.marginaDesna=marginaDesna;
	this.marginaGore=marginaGore;
	this.marginaDolje=marginaDolje;
}
public void setNeCrtajZaPauze(boolean neCrtaj){
	neCrtajPauze=neCrtaj;
}
public void odobriPauzuDirektno(boolean odobri){
	this.odobrenaPauza=odobri;
}
public void setSystemPauze(boolean systemPauze){
	this.systemPauze=systemPauze;
}
public void obradiSystemPauzeNaSvimObjektima(){
	     this.task.onSystemResume();
         for(int i=0;i<this.listaDinamicna.size();i++){
        	 listaDinamicna.get(i).onSystemResume();
         }
         for(int i=0;i<this.listaTemeljna.size();i++){
        	 listaTemeljna.get(i).onSystemResume();
         }
         for(int i=0;i<this.listaKrovna.size();i++){
        	 listaKrovna.get(i).onSystemResume();
         }
         for(int i=0;i<this.listaObjekataIznadTeksture.size();i++){
        	 listaObjekataIznadTeksture.get(i).onSystemResume();
         }
}
public void dodajSjenu(Path sjena){
	this.zajednickaSjena.addPath(sjena);
} 
public void setFPSGlavnePetlje(float fps){
	threadWait=(int)(1000/fps); 
} 
public void postaviPomakCanvasaApsolutno(float apsX, float apsY){
	this.apsPovlX=-apsX;
	this.apsPovlY=-apsY;
} 
public void postaviPomakCanvasaRelativno(float povX, float povY){
	this.povlX=-povX;
	this.povlY=-povY;
	this.staroPovlX=0;
	this.staroPovlY=0;
	
	this.provjeriPovlacenjeIPomakniCanvas();
	//this.povlaciSe=true;
	//this.povlaciSe=false;
	
} 
private void stvariKojeSeIzvrsavajuSamoNaPocetku(){
	postaviPomakCanvasaApsolutno(GL.faIgr.xVala(), GL.faIgr.yVala()); 
	
}
public void postaviTempUniverzalniIzbornik(IzbornikUniverzalni temp){
	this.tempUniverzalniIzbornik=temp;
}
public void daliDaProvjeravamPovlacenje(boolean daNe){
	provjeraPovlacenja=daNe;
}
public void reciklirajPozadinu(){
	if(slikaPozadine!=null){slikaPozadine.recycle();
	                       slikaPozadine=null;
	}
}
public void reciklirajTeksturu(){
	if(slikaTeksture!=null){slikaTeksture.recycle();
	                       slikaTeksture=null;
	}
}
public RectF getRectGlavnePozadine(){
	return this.recCrtPoz;
}
public void stvoriPozadinuCm(Bitmap poz, float sirCrt, float visCrt){
	this.slikaPozadine=poz;
	float sir=sirCrt*this.PpCmX;
	float vis=visCrt*this.PpCmY;
	this.recCrtPoz= new RectF(0,0,sir,vis);
	//this.recCrtPoz= new RectF(0,0,sirCrt*this.PpCm,visCrt*this.PpCm);
}
public void stvoriPozadinuPix(Bitmap poz, float sirCrt, float visCrt){
	this.slikaPozadine=poz;
	float sir=sirCrt;
	float vis=visCrt;
	this.recCrtPoz= new RectF(0,0,sir,vis);
	//this.recCrtPoz= new RectF(0,0,sirCrt*this.PpCm,visCrt*this.PpCm);
}
public void stvoriTeksturu(Bitmap tekstura, float sirCrt, float visCrt,int prozirnost){
	this.slikaTeksture=tekstura;
	float sir=sirCrt*this.PpCmX;
	float vis=visCrt*this.PpCmY;
	paintTeksture= new Paint();
	if(prozirnost<0) prozirnost =0;
	else if(prozirnost>100) prozirnost=100;
	int alfa=(255/100)*prozirnost;
	paintTeksture.setAlpha(alfa);
	this.recCrtTeksture= new RectF(0,0,sir,vis);
}
public void postaviZahtjevZaPause(boolean b){
	zahtjevZaPause=b;
	if(!zahtjevZaPause){// �?im se promjni da više nije pauza vraćam nazad zastavicu za normalno izvođenje petlje ina�?e mi se izvodi glavna petlja takt kasnije a glog briše vrijeme pauze tako da ga objekti nemaju vremena o�?itat
	this.odobrenaPauza=false;
	 
	}
}
public void setTaskbar(Taskbar task){
	   this.task=task;
}
public void setOznacenSam(UIManagerObject temObj){//ozna�?en zna�?i da će taj objekt primati xy toucha,
	if(tempOznacenObj!=temObj){// da ako se slucajno pozove isti da ne pokreće mehanizme ostale u korisnicama setImTouched metode
		if(tempOznacenObj!=null){
		tempOznacenObj.setImTouched(false);// salje prijasnjem poruku da vise nije oznacen
	    }
	    tempOznacenObj=temObj;
	    tempOznacenObj.setImTouched(true);// salje trenutaènom da je on oznaèemn sada
	}
	
}
public UIManagerObject dajMiTempOznacenObject(){
	return this.tempOznacenObj;
}
public void setNisamViseOznacen(UIManagerObject temObj){// namješta direktno da nije vise oznacen
	if(tempOznacenObj==temObj) tempOznacenObj=null;
	 temObj.setImTouched(false);// vraæa mu da više nije oznaèen bez obzira dali je zaista bio oznaèen
}
public void makniObjekt(UIManagerObject temp){

	if(!runLista){
		try{
		if(!this.listaDinamicna.remove(temp)){
			if(!this.listaKrovna.remove(temp)){
				if(!this.listaTemeljna.remove(temp)){
					 listaObjekataIznadTeksture.remove(temp);
				}
			}
		  }
		
	    }
		catch(ConcurrentModificationException e){
			if(!listaMakniObj.contains(temp))listaMakniObj.add(temp);
		}
		    }
		  
	else {
		if(!listaMakniObj.contains(temp))listaMakniObj.add(temp);
	}
}

public void makniSveObjekteMultyThread(){
  int i=0;
  UIManagerObject temp;
  while(listaDinamicna.size()>i){
	  temp=listaDinamicna.get(i);
	    if(!runLista){
		   
	    	listaDinamicna.remove(temp);
	    	i--;
	     }
	
	    else {
	    	listaMakniObj.add(temp);
	    	
	    }
	    i++;
  }
  i=0;
  while(listaKrovna.size()>i){
	  temp=listaKrovna.get(i);
	    if(!runLista){
		   
	    	listaKrovna.remove(temp);
	    	i--;
	     }
	
	    else {
	    	listaMakniObj.add(temp);
	    	
	    }
	    i++;
  }
  i=0;
  while(listaTemeljna.size()>i){
	  temp=listaTemeljna.get(i);
	    if(!runLista){
		   
	    	listaTemeljna.remove(temp);
	    	i--;
	     }
	
	    else {
	    	listaMakniObj.add(temp);
	    	
	    }
	    i++;
  }
  i=0;
  while(this.listaObjekataIznadTeksture.size()>i){// mice sve i iz liste iznad teksture
	  temp=listaObjekataIznadTeksture.get(i);
	    if(!runLista){    
	          if(temp==tempOznacenObj) tempOznacenObj=null;
	          try{
	        	  i--;
	        	  listaObjekataIznadTeksture.remove(temp);
	          }
	          catch(UnsupportedOperationException e){
	        	  i++;
	          }     
	     }
	
	    else {
	    	listaMakniObj.add(temp);
	    	
	    }
	    i++;
  }
}
public void ugasiGlavnuPetlju(){
	 runBit=false;

	/*try{
			th.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		   }*/
	        // th=null;	
      }
public void  pokreniGlavnuPetlju(String name){
	if(!jesamLiBestredni){
		runBit=true; 
	    th=new Thread(this);
	    th.setName(name);
        th.start();
	}
}            
 public  void dodajElementUListu(UIManagerObject op, int index){  //dodaje naknadno jedan objekt u listu 	
	if(!runLista){
		try{
		zaustaviDokNeUbacimUListu=true;
		
	       if(index==1){//dodaje se statièki objekt
	    	   if(!listaTemeljna.contains(op))   listaTemeljna.addLast(op);
		   
	             }
	       else if(index==2){// dodaje se dinamièki objekt
	    	   if(!listaDinamicna.contains(op)) listaDinamicna.addLast(op);
		        
		        }
	       else if(index==3)//dodaje se izbornik ili neki drugi koji æe biti uvijek na vrhu
	            {
	    	   if(!listaKrovna.contains(op))  listaKrovna.addLast(op);
	            } 
		
		if(!this.listaObjekataIznadTeksture.contains(op)){
			if(index==4){
				listaObjekataIznadTeksture.addLast(op);
			}
			else if(index==5){//dodaje ispod svi ostalih u ovoj listi
				
					listaObjekataIznadTeksture.addFirst(op);
				
			}
		}
		zaustaviDokNeUbacimUListu=false;
		}
		catch(ConcurrentModificationException e){
			listaDodaObj.addLast(op);
			listaDodaInt.addLast(index);
		}
	}
	else{// ako se petlja izvršava onda se objekti za dodavanje spremaju u ovu liste za naknadno ubacivanje u glavnu
		if(!listaDodaObj.contains(op)){
		listaDodaObj.addLast(op);
		listaDodaInt.addLast(index); }
	  }
}
public void stop(){
	ugasiGlavnuPetlju();
}
/////////public metode 
public float getSirinuVirtualogProstora(){
	if(recCrtPoz!=null)return recCrtPoz.width();
	return 0;
}
public float getVisinuVirtualogProstora(){
	if(recCrtPoz!=null)return recCrtPoz.height();
	return 0;
}
public float getMarginuLijevu(){
	return this.marginaLijeva;
}
public float getMarginuDesnu(){
	return this.marginaDesna;
}
public float getMarginuGore(){
	return this.marginaGore;
}
public float getMarginuDolje(){
	return this.marginaDolje;
}
public float getPixPoCmX(){
	return this.PpCmX;
}
public float getPixPoCmY(){
	return this.PpCmY;
}
public float getXZadnjegDodira(){
	return this.xDodiraZadnjeg;
}
public float getYZadnjegDodira(){
	return this.yDodiraZadnjeg;
}
public float getVisEkrana(){
	return this.visEkrana;
}
public float getSirEkrana(){
	return this.sirEkrana;
}
public void nacrtajDodatnoIznadGlavnePetlje(Canvas can, float fps,UIManager uiMan,float PpCmX, float PpCmY,float pomCanX,float pomCanY){} 
public float pomakCanvasaX(){
	return apsPovlX;
}
public float pomakCanvasaY(){
	return apsPovlY;
}
public void iskljuciTouchEvente(){
	ukljuceniTouchEventi=false;
}
public void ukljuciTouchEvente(){
	ukljuceniTouchEventi=true;
}
public boolean daliPostojiObjekt(UIManagerObject op){
	if(listaDinamicna.contains(op)){
		return true;
	}
	else if(listaKrovna.contains(op)){
		return true;
	}
	else if(listaTemeljna.contains(op)){
		return true;
	}
	else if(this.listaObjekataIznadTeksture.contains(op)){
		return true;
	}
	else return false;
} 
//public abstract void izvrsiNakonPrvogTakta();
public IzbornikUniverzalni tempUniverzalniIzbornik( ){
	return this.tempUniverzalniIzbornik;
}
public void unlockCanvasAndPost(){
	holder.unlockCanvasAndPost(can);
	while(true){
		if(!holder.getSurface().isValid()) continue;
	    //can.drawColor(34);   ovo nerqadi neznam zašto????
		can=holder.lockCanvas();
		break;
	}
}
public void nacrtajTaksturu(){
	if(canLocked &&this.slikaTeksture!=null) can.drawBitmap(slikaTeksture, null, recCrtTeksture, paintTeksture);
}
/////////privatne metode
private void stvoriBitmapCanvasa(){
	
	
	float sirBmp=recCrtPoz.width();
	float visBmp=recCrtPoz.height();
	float odnosEkr=this.sirEkrana/this.visEkrana;
	float odnosSl=recCrtPoz.width()/recCrtPoz.height();
	if(odnosEkr>=odnosSl){
		visBmp=sirBmp/ odnosEkr;
	}
	else{
		sirBmp=visBmp* odnosEkr;
	}

	this.xPostoRazlPauze=0;
	this.yPostoRazlPauze=0;
	if(sirBmp>this.sirEkrana){// ako je ekran prevelik
		this.xPostoRazlPauze=(sirBmp-sirEkrana)/(sirBmp/100);
		
	}
	if(visBmp>this.visEkrana){
		this.yPostoRazlPauze=(visBmp-visEkrana)/(visBmp/100);
	 }
 Bitmap  tempBmp= Bitmap.createBitmap((int)recCrtPoz.width(),(int)recCrtPoz.height(), Bitmap.Config.RGB_565);
 //  tempBmp.setDensity(gustEkr/4);
  Canvas tempCan=new Canvas(tempBmp);  
  stvorenPauzeBitmap=true;
  task.iskljuciCrtanjePotpuno(true);
  if(provjeraPovlacenja==false){
	  glavnoCrtanje(tempCan);
  }
  else {
	  provjeraPovlacenja=false;
	 
	  glavnoCrtanje(tempCan);
	  provjeraPovlacenja=true; 
  }
 
//malo posvjetljujem
	tempCan.drawColor(Color.argb(30, 255,255,255));
  task.iskljuciCrtanjePotpuno(false);

	this.visBmp= recCrtPoz.height()/koefSmanjPoz;
	this.sirBmp= recCrtPoz.width()/koefSmanjPoz;
  temCanvasBitmap=Bitmap.createScaledBitmap(tempBmp, (int)(this.sirBmp),(int)(this.visBmp), true);

}

public static Bitmap applyGaussianBlur(Bitmap src) {
    double[][] GaussianBlurConfig = new double[][] {
        { 1, 2, 1 },
        { 2, 4, 2 },
        { 1, 2, 1 }
    };
    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    convMatrix.applyConfig(GaussianBlurConfig);
    convMatrix.Factor = 16;
    convMatrix.Offset = 0;
    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
}
public void fastblur(Bitmap bitmap, int radius) {

    // Stack Blur v1.0 from
    // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
    //
    // Java Author: Mario Klingemann <mario at quasimondo.com>
    // http://incubator.quasimondo.com
    // created Feburary 29, 2004
    // Android port : Yahel Bouaziz <yahel at kayenko.com>
    // http://www.kayenko.com
    // ported april 5th, 2012

    // This is a compromise between Gaussian Blur and Box blur
    // It creates much better looking blurs than Box Blur, but is
    // 7x faster than my Gaussian Blur implementation.
    //
    // I called it Stack Blur because this describes best how this
    // filter works internally: it creates a kind of moving stack
    // of colors whilst scanning through the image. Thereby it
    // just has to add one new block of color to the right side
    // of the stack and remove the leftmost color. The remaining
    // colors on the topmost layer of the stack are either added on
    // or reduced by one, depending on if they are on the right or
    // on the left side of the stack.
    //
    // If you are using this algorithm in your code please add
    // the following line:
    //
    // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

 //  Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

    if (radius < 1) {
    	radius=1;
    }

    int w = bitmap.getWidth();
    int h = bitmap.getHeight();

    int[] pix = new int[w * h];
    bitmap.getPixels(pix, 0, w, 0, 0, w, h);

    int wm = w - 1;
    int hm = h - 1;
    int wh = w * h;
    int div = radius + radius + 1;

    int r[] = new int[wh];
    int g[] = new int[wh];
    int b[] = new int[wh];
    int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
    int vmin[] = new int[Math.max(w, h)];

    int divsum = (div + 1) >> 1;
    divsum *= divsum;
    int dv[] = new int[256 * divsum];
    for (i = 0; i < 256 * divsum; i++) {
        dv[i] = (i / divsum);
    }

    yw = yi = 0;

    int[][] stack = new int[div][3];
    int stackpointer;
    int stackstart;
    int[] sir;
    int rbs;
    int r1 = radius + 1;
    int routsum, goutsum, boutsum;
    int rinsum, ginsum, binsum;

    for (y = 0; y < h; y++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        for (i = -radius; i <= radius; i++) {
            p = pix[yi + Math.min(wm, Math.max(i, 0))];
            sir = stack[i + radius];
            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);
            rbs = r1 - Math.abs(i);
            rsum += sir[0] * rbs;
            gsum += sir[1] * rbs;
            bsum += sir[2] * rbs;
            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            } else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }
        }
        stackpointer = radius;

        for (x = 0; x < w; x++) {

            r[yi] = dv[rsum];
            g[yi] = dv[gsum];
            b[yi] = dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (y == 0) {
                vmin[x] = Math.min(x + radius + 1, wm);
            }
            p = pix[yw + vmin[x]];

            sir[0] = (p & 0xff0000) >> 16;
            sir[1] = (p & 0x00ff00) >> 8;
            sir[2] = (p & 0x0000ff);

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[(stackpointer) % div];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi++;
        }
        yw += w;
    }
    for (x = 0; x < w; x++) {
        rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
        yp = -radius * w;
        for (i = -radius; i <= radius; i++) {
            yi = Math.max(0, yp) + x;

            sir = stack[i + radius];

            sir[0] = r[yi];
            sir[1] = g[yi];
            sir[2] = b[yi];

            rbs = r1 - Math.abs(i);

            rsum += r[yi] * rbs;
            gsum += g[yi] * rbs;
            bsum += b[yi] * rbs;

            if (i > 0) {
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
            } else {
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
            }

            if (i < hm) {
                yp += w;
            }
        }
        yi = x;
        stackpointer = radius;
        for (y = 0; y < h; y++) {
            // Preserve alpha channel: ( 0xff000000 & pix[yi] )
            pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

            rsum -= routsum;
            gsum -= goutsum;
            bsum -= boutsum;

            stackstart = stackpointer - radius + div;
            sir = stack[stackstart % div];

            routsum -= sir[0];
            goutsum -= sir[1];
            boutsum -= sir[2];

            if (x == 0) {
                vmin[y] = Math.min(y + r1, hm) * w;
            }
            p = x + vmin[y];

            sir[0] = r[p];
            sir[1] = g[p];
            sir[2] = b[p];

            rinsum += sir[0];
            ginsum += sir[1];
            binsum += sir[2];

            rsum += rinsum;
            gsum += ginsum;
            bsum += binsum;

            stackpointer = (stackpointer + 1) % div;
            sir = stack[stackpointer];

            routsum += sir[0];
            goutsum += sir[1];
            boutsum += sir[2];

            rinsum -= sir[0];
            ginsum -= sir[1];
            binsum -= sir[2];

            yi += w;
        }
    }
    bitmap.setPixels(pix, 0, w, 0, 0, w, h);

}
private void postaviBitmapNaCanvas(){
	Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
	
	temCanvasBitmap = Bitmap.createBitmap((int)(this.sirEkrana),(int)(this.visEkrana), conf); 
	temCanvasBitmap.setDensity(320);
	can.setBitmap(temCanvasBitmap);
	
	
}
private void provjeriPovlacenjeIPomakniCanvas(){
	//samo u sluèaju da je veæ drugi ciklus prst u pomaku pomièe se ekran, odabrao sam nemoguæi broj -10000 za detekciju toga
    if(povlX!=-10000&&staroPovlX!=-10000) apsPovlX+=povlX-staroPovlX;
	if(povlY!=-10000&&staroPovlY!=-10000) apsPovlY+=povlY-staroPovlY;
	if(apsPovlX>0) apsPovlX=0;
	else if(
		apsPovlX<-this.recCrtPoz.width()+can.getWidth()){
		apsPovlX=- recCrtPoz.width()+can.getWidth();
	}
	if(apsPovlY>0) apsPovlY=0;
	else if(apsPovlY<-this.recCrtPoz.height()+can.getHeight()) apsPovlY=- recCrtPoz.height()+can.getHeight();
	can.translate(apsPovlX,apsPovlY);
	if(povlaciSe){
	  staroPovlX=povlX;
	  staroPovlY=povlY;
	}
 
}
private void ubaciUListuElementeNaCekanju(){
	int i=0;
	int lisVel=listaDodaObj.size();
	while(i<lisVel&&!listaDodaObj.isEmpty()){// dodaje sve objekte koji  èekaju na dodavanje u glavnu listu
		try{
			UIManagerObject obj=listaDodaObj.get(i);
		
		   Integer in=listaDodaInt.get(i);
		   dodajElementUListu( obj,in);
		   i++;
		}
		catch(IndexOutOfBoundsException e){
			
		}
	}
	listaDodaObj.clear();
	listaDodaInt.clear();
}
private void izbaciIzListeElementeNaCekanju(){
	int i=0;
	while(i<listaMakniObj.size()&&!listaMakniObj.isEmpty()){// dodaje sve objekte koji  èekaju na dodavanje u glavnu listu
		makniObjekt(listaMakniObj.get(i));
		i++;
	}
	listaMakniObj.clear();
}
private void sortirajListu(){
	int iSort=0;
	int iNajveceg=listaDinamicna.size()-1;//index zadnjeg sortiranog do kojeg bi trebalo sortirajne iæi 
	{ 
		while(listaDinamicna.size()-1>=iSort+1){// provjerava dali je dinamièki dio veæi od 1, ilii bar da ima sljedeæi poslioje zadnjeg dinamièkog radi provjere
			if(systemPauze) break;//zaustavlja sve petlje prije nego ga sistem ubije zbog memorije
		    tempSort=listaDinamicna.get(iSort);
		    tempSortNext=listaDinamicna.get(iSort+1);

		    if(tempSort.getY()+tempSort.getVis()>tempSortNext.getY()+tempSortNext.getVis()){
		    	listaDinamicna.set(iSort,listaDinamicna.set(iSort+1, listaDinamicna.get(iSort))); // zamjenjuje mjesta ovim dvama èlanovima
		                    iSort++;
	                         }
	        //else if(tempSort.getY()+tempSort.getVis()<=tempSortNext.getY()+tempSortNext.getVis()) iSort++;// u sluèaju da je manji prelazi se na sljedeæi ponovno se izvršava petlja
		    else iSort++;
		    if(0==iNajveceg) break;// prekida se kada doðe do poèetka dinamièkog dijela liste
		    if(iSort>iNajveceg){
		                   iSort=0;//kreæe ispoèetka sa sortiranjem
	                       iNajveceg--;
	                       }
            
    
	    }
	}
}

private void touchKoordinate(float X, float Y){
	//trebalo bi poslat EventManageru trenutni x i y možda spremiti u neku lokalnu varijablu
	if(ukljuceniTouchEventi){
		touchX=X;
	    touchY=Y;
	    if(tempOznacenObj!=null){
		 tempOznacenObj.setTouchedXY(X, Y); 
	
	        }
	}
 }
private void touchMe(float X, float Y){
	xDodiraZadnjeg=X;
	yDodiraZadnjeg=Y;
   if(ukljuceniTouchEventi){
	int i=listaObjekataIznadTeksture.size()-1;// sprema duzinu liste
    boolean pronasaoObjektUListiIznadTeksture=false;
    while(!listaObjekataIznadTeksture.isEmpty()){//  ako je taskTouched = true preskaèe se petlja
			UIManagerObject obj=listaObjekataIznadTeksture.get(i);
			/*if( X>=(lista.get(i).getX())&&
			  X<=(lista.get(i).getX()+lista.get(i).getSir()) &&  //VAZNO!!! predpostavio sam da je
			  Y>=(lista.get(i).getY())&&                        //iscrtaanje u pozitivnom smijeru
			  Y<=(lista.get(i).getY()+lista.get(i).getVis()) ) //od referentne tocke spritea
			        */
		if(!obj.getDaliDaIgnoriramTouch())if( X>=(obj.getX())&&
					  X<=(obj.getX()+obj.getSir()) &&  //VAZNO!!! predpostavio sam da je
					  Y>=(obj.getY())&&                        //iscrtaanje u pozitivnom smijeru
					  Y<=(obj.getY()+obj.getVis()) ){                 
	                if((obj.getIndikator()>=1&&obj.getIndikator()<=200)//ako je objekt vojnik ili graðevina (toranj)od igraèa
	            		   ||(obj.getIndikator()>=301&&obj.getIndikator()<=400)){ // ili ako je izbornik
					 /*  if( tempOznacenObj!=null){
						   if(tempOznacenObj.getIndikator()>300&&tempOznacenObj.getIndikator()<=400){
							        tempOznacenObj.setTouchedXY(X, Y);//ako se kliknulo na izbornik onda se šalje gdje se toèno kliknulo
							        tempOznacenObj.setImTouched(false);// i oznaèava se da više nije izabran
							        tempOznacenObj=null;// i još postavljam da je null taj objekt više nije oznaèena
						           }
						   else  tempOznacenObj.setTouchedObject(lista.get(i)); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
						   tempOznacenObj.setImTouched(false);// daje doznanja predhodnom objektu da više nije on taj koji je  izabran
					       }*/
	                	if(tempOznacenObj!=null){// u slucaju da postoji veæ oznacen objekt
	
	                		
	                	
	                		
	                		
	                	    
	                	    if(tempOznacenObj!=obj&&tempOznacenObj!=null){
	                	    	 obj.setImTouched(true);
	                	    	tempOznacenObj.setTouchedObject(obj); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
	                	    	tempOznacenObj.setImTouched(false);
	                	    	
	                	    }
	                	    else obj.setImTouched(true); 
	                	    obj.setTouchedObject(obj);
	                	    tempOznacenObj=obj;  //sprema sadašnji kliknuti  objekt
	                	}
	                	else{
	                		obj.setImTouched(true); 
	                	    obj.setTouchedObject(obj);
	                	    tempOznacenObj=obj; 
	                	}
	                	 pronasaoObjektUListiIznadTeksture=true;		   
		                 break;
	                }
	                
			         }      
			 i--;  //dekriminira osnovnu listu objekata
			 if(i<0){
				 break;
			 }
		 }	
	 boolean  taskTouched=false;//služit æe da se nepokrene petlja za provjeru objekata u sluèaju da je taskbar kliknut
	 
	 //trazi koji je objekt dotaknut ali od kraja liste, pošto je ona sorrtirana dotaknut objek æe biti onaj koji je u prednjem planu
     if(task!=null&&!pronasaoObjektUListiIznadTeksture){//samo ako veæ postoji taskbaar
		    if(task.getDimenzijeMenuPolja().contains(X, Y)){
		        taskTouched=true;// preskaèe izvoðenje petlje za provjeru na koji je objekt kliknuto
		        task.setTouchedMenuPolje(X, Y);// salje taskbaru gdje je toèno kliknuto
      	     }
	        else if(task.getDimenzijeDodatnihFunkcja().contains(X, Y)){
		       taskTouched=true;
		       task.setTouchedDodatneFunkcje(X, Y);
	        }
	        else if( task.getDimenzijePauseResume().contains(X, Y)){
	 	       taskTouched=true;
	 	       task.setTouchedPauseResume(X, Y);
	        }
	        else if( task.getDimenzijeDolaskaProtivnik().contains(X, Y)){
		 	       taskTouched=true;
		 	       task.setTouchedDolazakProt(X, Y);
		        }
    }
     boolean nasaUglavnoj=false;
     i=listaKrovna.size()-1;// sprema duzinu liste
   	 if(!pronasaoObjektUListiIznadTeksture&&!listaKrovna.isEmpty()&&!taskTouched&&!this.zahtjevZaPause&&!nasaUglavnoj)
   		 while(true){//  ako je taskTouched = true preskaèe se petlja
   			UIManagerObject obj=listaKrovna.get(i);
   			/*if( X>=(lista.get(i).getX())&&
   			  X<=(lista.get(i).getX()+lista.get(i).getSir()) &&  //VAZNO!!! predpostavio sam da je
   			  Y>=(lista.get(i).getY())&&                        //iscrtaanje u pozitivnom smijeru
   			  Y<=(lista.get(i).getY()+lista.get(i).getVis()) ) //od referentne tocke spritea
   			        */
   			if(!obj.getDaliDaIgnoriramTouch())if( X>=(obj.getX())&&
   					  X<=(obj.getX()+obj.getSir()) &&  //VAZNO!!! predpostavio sam da je
   					  Y>=(obj.getY())&&                        //iscrtaanje u pozitivnom smijeru
   					  Y<=(obj.getY()+obj.getVis()) ){                 
   	                if((obj.getIndikator()>=1&&obj.getIndikator()<=200)//ako je objekt vojnik ili graðevina (toranj)od igraèa
   	            		   ||(obj.getIndikator()>=301&&obj.getIndikator()<=400)){ // ili ako je izbornik
   					 /*  if( tempOznacenObj!=null){
   						   if(tempOznacenObj.getIndikator()>300&&tempOznacenObj.getIndikator()<=400){
   							        tempOznacenObj.setTouchedXY(X, Y);//ako se kliknulo na izbornik onda se šalje gdje se toèno kliknulo
   							        tempOznacenObj.setImTouched(false);// i oznaèava se da više nije izabran
   							        tempOznacenObj=null;// i još postavljam da je null taj objekt više nije oznaèena
   						           }
   						   else  tempOznacenObj.setTouchedObject(lista.get(i)); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
   						   tempOznacenObj.setImTouched(false);// daje doznanja predhodnom objektu da više nije on taj koji je  izabran
   					       }*/
   	                	if(tempOznacenObj!=null){// u slucaju da postoji veæ oznacen objekt
   	                	
   	                		tempOznacenObj.setTouchedObject(obj); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
   	              
   	              
   	                	    }
   	                	 if(tempOznacenObj!=obj&&tempOznacenObj!=null){
   	                		obj.setImTouched(true);
   	                		 tempOznacenObj.setImTouched(false);
   	                		 
   	                	 }
   	                	  else obj.setImTouched(true); 
   	                	  tempOznacenObj=obj;  //sprema sadašnji kliknuti  objekt
   	                	  nasaUglavnoj=true;
   	                	 break;
   	                }
   							   
   	               
   			         }      
   			 i--;  //dekriminira osnovnu listu objekata
   			 if(i<0){
   				 break;
   			 }
   		 }
     i=listaDinamicna.size()-1;// sprema duzinu liste
	 if(!pronasaoObjektUListiIznadTeksture&&!listaDinamicna.isEmpty()&&!taskTouched&&!this.zahtjevZaPause&&! nasaUglavnoj)
		 while(true){//  ako je taskTouched = true preskaèe se petlja
			UIManagerObject obj=listaDinamicna.get(i);
			/*if( X>=(lista.get(i).getX())&&
			  X<=(lista.get(i).getX()+lista.get(i).getSir()) &&  //VAZNO!!! predpostavio sam da je
			  Y>=(lista.get(i).getY())&&                        //iscrtaanje u pozitivnom smijeru
			  Y<=(lista.get(i).getY()+lista.get(i).getVis()) ) //od referentne tocke spritea
			        */
			if(!obj.getDaliDaIgnoriramTouch())if( X>=(obj.getX())&&
					  X<=(obj.getX()+obj.getSir()) &&  //VAZNO!!! predpostavio sam da je
					  Y>=(obj.getY())&&                        //iscrtaanje u pozitivnom smijeru
					  Y<=(obj.getY()+obj.getVis()) ){                 
	                if((obj.getIndikator()>=1&&obj.getIndikator()<=200)//ako je objekt vojnik ili graðevina (toranj)od igraèa
	            		   ||(obj.getIndikator()>=301&&obj.getIndikator()<=400)){ // ili ako je izbornik
					 /*  if( tempOznacenObj!=null){
						   if(tempOznacenObj.getIndikator()>300&&tempOznacenObj.getIndikator()<=400){
							        tempOznacenObj.setTouchedXY(X, Y);//ako se kliknulo na izbornik onda se šalje gdje se toèno kliknulo
							        tempOznacenObj.setImTouched(false);// i oznaèava se da više nije izabran
							        tempOznacenObj=null;// i još postavljam da je null taj objekt više nije oznaèena
						           }
						   else  tempOznacenObj.setTouchedObject(lista.get(i)); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
						   tempOznacenObj.setImTouched(false);// daje doznanja predhodnom objektu da više nije on taj koji je  izabran
					       }*/
	                	if(tempOznacenObj!=null){// u slucaju da postoji veæ oznacen objekt
	                	
	                		tempOznacenObj.setTouchedObject(obj); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
	              
	              
	                	    }
	                	 if(tempOznacenObj!=obj&&tempOznacenObj!=null){
	                		 obj.setImTouched(true);
	                		 tempOznacenObj.setImTouched(false);
	                		
	                	 }
	                	  else obj.setImTouched(true); 
	                	  tempOznacenObj=obj;  //sprema sadašnji kliknuti  objekt
	                	  nasaUglavnoj=true;
	                	  break;
	                }
							   
	                
			         }      
			 i--;  //dekriminira osnovnu listu objekata
			 if(i<0){
				 break;
			 }
		 }
   
	  i=listaTemeljna.size()-1;// sprema duzinu liste
		 if(!pronasaoObjektUListiIznadTeksture&&!listaTemeljna.isEmpty()&&!taskTouched&&!this.zahtjevZaPause&&!nasaUglavnoj)
			 while(true){//  ako je taskTouched = true preskaèe se petlja
				UIManagerObject obj=listaTemeljna.get(i);
				/*if( X>=(lista.get(i).getX())&&
				  X<=(lista.get(i).getX()+lista.get(i).getSir()) &&  //VAZNO!!! predpostavio sam da je
				  Y>=(lista.get(i).getY())&&                        //iscrtaanje u pozitivnom smijeru
				  Y<=(lista.get(i).getY()+lista.get(i).getVis()) ) //od referentne tocke spritea
				        */
				if(!obj.getDaliDaIgnoriramTouch())if( X>=(obj.getX())&&
						  X<=(obj.getX()+obj.getSir()) &&  //VAZNO!!! predpostavio sam da je
						  Y>=(obj.getY())&&                        //iscrtaanje u pozitivnom smijeru
						  Y<=(obj.getY()+obj.getVis()) ){                 
		                if((obj.getIndikator()>=1&&obj.getIndikator()<=200)//ako je objekt vojnik ili graðevina (toranj)od igraèa
		            		   ||(obj.getIndikator()>=301&&obj.getIndikator()<=400)){ // ili ako je izbornik
						 /*  if( tempOznacenObj!=null){
							   if(tempOznacenObj.getIndikator()>300&&tempOznacenObj.getIndikator()<=400){
								        tempOznacenObj.setTouchedXY(X, Y);//ako se kliknulo na izbornik onda se šalje gdje se toèno kliknulo
								        tempOznacenObj.setImTouched(false);// i oznaèava se da više nije izabran
								        tempOznacenObj=null;// i još postavljam da je null taj objekt više nije oznaèena
							           }
							   else  tempOznacenObj.setTouchedObject(lista.get(i)); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
							   tempOznacenObj.setImTouched(false);// daje doznanja predhodnom objektu da više nije on taj koji je  izabran
						       }*/
		                	if(tempOznacenObj!=null){// u slucaju da postoji veæ oznacen objekt
		                	
		                		tempOznacenObj.setTouchedObject(obj); //salje prijašnjem kliknutom objektu sadašnji, na njemu je da odluèi dali æe ga uptrjebiti
		              
		              
		                	    }
		                	 if(tempOznacenObj!=obj&&tempOznacenObj!=null){
		                		 obj.setImTouched(true);
		                		 tempOznacenObj.setImTouched(false);
		                		
		                	 }
		                	  else obj.setImTouched(true); 
		                	  tempOznacenObj=obj;  //sprema sadašnji kliknuti  objekt
		                	  nasaUglavnoj=true;
		                	  break;
		                }
								   
		             
				         }      
				 i--;  //dekriminira osnovnu listu objekata
				 if(i<0){
					 break;
				 }
			 }
	
      }
  }
////
private double udaljenostDvijeTocke(float ax,float ay, float bx,float by){// predpostavlja da se sve odvija u prvom kvadrantu
	 return Math.hypot(Math.abs(ax-bx),Math.abs(ay-by));
}


public static class ConvolutionMatrix
{
    public static final int SIZE = 3;
 
    public double[][] Matrix;
    public double Factor = 1;
    public double Offset = 1;
 
    public ConvolutionMatrix(int size) {
        Matrix = new double[size][size];
    }
 
    public void setAll(double value) {
        for (int x = 0; x < SIZE; ++x) {
            for (int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = value;
            }
        }
    }
 
    public void applyConfig(double[][] config) {
        for(int x = 0; x < SIZE; ++x) {
            for(int y = 0; y < SIZE; ++y) {
                Matrix[x][y] = config[x][y];
            }
        }
    }
 
    public static  Bitmap computeConvolution3x3(Bitmap src, ConvolutionMatrix matrix) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
 
        int A, R, G, B;
        int sumR, sumG, sumB;
        int[][] pixels = new int[SIZE][SIZE];
 
        for(int y = 0; y < height - 2; ++y) {
            for(int x = 0; x < width - 2; ++x) {
 
                // get pixel matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        pixels[i][j] = src.getPixel(x + i, y + j);
                    }
                }
 
                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);
 
                // init color sum
                sumR = sumG = sumB = 0;
 
                // get sum of RGB on matrix
                for(int i = 0; i < SIZE; ++i) {
                    for(int j = 0; j < SIZE; ++j) {
                        sumR += (Color.red(pixels[i][j]) * matrix.Matrix[i][j]);
                        sumG += (Color.green(pixels[i][j]) * matrix.Matrix[i][j]);
                        sumB += (Color.blue(pixels[i][j]) * matrix.Matrix[i][j]);
                    }
                }
 
                // get final Red
                R = (int)(sumR / matrix.Factor + matrix.Offset);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }
 
                // get final Green
                G = (int)(sumG / matrix.Factor + matrix.Offset);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }
 
                // get final Blue
                B = (int)(sumB / matrix.Factor + matrix.Offset);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }
 
                // apply new pixel
                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }
 
        // final image
        return result;
    }
}

}

 
