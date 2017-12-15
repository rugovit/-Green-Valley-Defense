package com.rugovit.igrica;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;


public class  AMAZONProdajaListener  implements PurchasingListener {
	   public static final String TAG = " AMAZONProdajaListener ";
	   public String currentUserId = null;
	   public String currentMarketplace = null;
	   public Context cont;
	   boolean reset = false;//ako je feels onda ce samo vratiti   nove kupnje ili povrate kupnje od zadnjeg poziva getPurchaseUpdates()
	 public  AMAZONProdajaListener(Context con){
		cont=con;
	 }
	 public void  kupljenoNesto(PurchaseResponse response){}
	@Override
	public void onProductDataResponse(ProductDataResponse arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPurchaseResponse(PurchaseResponse response) {
		// TODO Auto-generated method stub
		if(response.getRequestStatus()==PurchaseResponse.RequestStatus.SUCCESSFUL){
			   SQLiteDatabase bazaPodataka;
		        bazaPodataka= cont.openOrCreateDatabase(IgricaActivity.glavniDB,cont.MODE_PRIVATE, null);
			  bazaPodataka.execSQL("INSERT OR IGNORE INTO " + 
					  IgricaActivity.listaPlacenihElemenata +"('"+IgricaActivity.IDPlacenogObjekta+"')"+
					  " Values ('"+  response.getReceipt().getSku()+"');");
			
			  bazaPodataka.close();
			
				kupljenoNesto( response);
		}
	}
	@Override
	public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse response) {
		// TODO Auto-generated method stub
	    // Process receipts

	    switch (response.getRequestStatus()) {
	      case SUCCESSFUL:
	    	   SQLiteDatabase bazaPodataka;
	    	   bazaPodataka= cont.openOrCreateDatabase(IgricaActivity.glavniDB,cont.MODE_PRIVATE, null);
	        for (final Receipt receipt : response.getReceipts()) {
	        
				if( receipt.isCanceled()){
					bazaPodataka.delete( IgricaActivity.listaPlacenihElemenata ,
							IgricaActivity.IDPlacenogObjekta + "=?", new String[] { receipt.getSku() });
				}
				else{
				  bazaPodataka.execSQL("INSERT OR IGNORE INTO " + 
						  IgricaActivity.listaPlacenihElemenata +"('"+IgricaActivity.IDPlacenogObjekta+"')"+
						  " Values ('"+  receipt.getSku()+"');");
		
	 				
				}
		
				
				
				
	          // Process receipts
	        }
			  bazaPodataka.close();
	        if (response.hasMore()) {
	          PurchasingService.getPurchaseUpdates(reset);
	        }
	        break;
	      case FAILED:
	        break;
	    }
	}
	@Override
	public void onUserDataResponse(UserDataResponse response) {
	    final UserDataResponse.RequestStatus status = response.getRequestStatus();
	    
	    switch(status) {
	      case SUCCESSFUL:

	        currentUserId = response.getUserData().getUserId();
	        currentMarketplace = response.getUserData().getMarketplace();
	        break;
	 
	      case FAILED:
	      case NOT_SUPPORTED:
	        // Fail gracefully.
	        break;
	    }
	  }
		
	
	}