package com.rugovit.igrica;
import rugovit.igrica.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;

import org.acra.*;
import org.acra.annotation.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import android.app.Application;
import android.widget.Toast;

@ReportsCrashes(
    /*formKey = "", // This is required for backward compatibility but not used
    formUri = "https://rugovit.cloudant.com/report8crash/725f2557540be87a58351471feface79",
    formUriBasicAuthLogin = "beirstocialmormseembeend", // optional
    formUriBasicAuthPassword = "lmcyEaivrEMTRbDXtqxs6NRa", // optional
 //   mode = ReportingInteractionMode.TOAST,
    reportType=org.acra.sender.HttpSender.Type.JSON*/
	//	formUri = "https://rugovit.cloudant.com/acra-com.rugovit.igrica/",
    formUri = "https://star_star_star.cloudant.com/report",
    reportType = org.acra.sender.HttpSender.Type.JSON,
    httpMethod = org.acra.sender.HttpSender.Method.POST,
    formUriBasicAuthLogin = "*************************",
    formUriBasicAuthPassword = "************************",
    	

    customReportContent = {
            ReportField.APP_VERSION_CODE,
            ReportField.APP_VERSION_NAME,
            ReportField.ANDROID_VERSION,
            ReportField.PACKAGE_NAME,
            ReportField.REPORT_ID,
            ReportField.BUILD,
            ReportField.STACK_TRACE,
            
           ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
             ReportField.LOGCAT, ReportField.AVAILABLE_MEM_SIZE, ReportField.APPLICATION_LOG, ReportField.FILE_PATH,
           ReportField.BRAND, ReportField.PRODUCT, ReportField.TOTAL_MEM_SIZE, ReportField.INITIAL_CONFIGURATION, ReportField.CRASH_CONFIGURATION, ReportField.DISPLAY,
            ReportField.USER_COMMENT, ReportField.USER_APP_START_DATE, ReportField.USER_CRASH_DATE, ReportField.DUMPSYS_MEMINFO, ReportField.EVENTSLOG, ReportField.RADIOLOG, ReportField.IS_SILENT,
            ReportField.DEVICE_ID, ReportField.INSTALLATION_ID, ReportField.DEVICE_FEATURES, ReportField.ENVIRONMENT, ReportField.SETTINGS_SYSTEM, ReportField.SETTINGS_SECURE,
            ReportField.SHARED_PREFERENCES, ReportField.MEDIA_CODEC_LIST, ReportField.THREAD_DETAILS
    },
    mode = ReportingInteractionMode.TOAST,
    resToastText = R.string.toast_crash
)
public class MyApplication extends Application{
	 @Override
     public void onCreate() {
         super.onCreate();
        // ACRA.getConfig().setResToastText(7); 
         // The following line triggers the initialization of ACRA
         ACRA.init(this);
     }
	 public JSONArray posaljiUpit(String url,String poruka){
			DefaultHttpClient httpclient = new DefaultHttpClient();
			//String custURL=URLEncoder.encode("{\"EmployeeID\":\"Marko\"}");
			HttpPost httppost = new HttpPost(url+poruka);
			
			try {
			    } catch (Exception e) {
			        Toast.makeText(this, "zapeo na string entity", Toast.LENGTH_LONG).show();
			    }
			    HttpResponse httpresponse=null;
			try {
			       httpresponse = httpclient.execute(httppost);
			        }   
	            
			catch (UnknownHostException e) {
		       e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	            JSONArray recvdjson=null;
	        try {
	            HttpEntity resultentity = httpresponse.getEntity();
	            InputStream inputstream = resultentity.getContent();
	            Header contentencoding = httpresponse.getFirstHeader("Content-Encoding");
	            if(contentencoding != null && contentencoding.getValue().equalsIgnoreCase("gzip")) {
	                inputstream = new GZIPInputStream(inputstream);
	            }
	            String resultstring = convertStreamToString(inputstream);
	            inputstream.close();
	          //  resultstring = resultstring.substring(1,resultstring.length()-1);
	           // recvdref.setText(resultstring + "\n\n" + httppostreq.toString().getBytes());
	             recvdjson = new JSONArray(resultstring);
	             
	            }
	           catch (Exception e) {
	               Toast.makeText(this, "zapeo na ekstraktiranju odgovora", Toast.LENGTH_LONG).show();
	           }
	           // recvdref.setText(recvdjson.toString(2));
	           return  recvdjson;
		}
	 private String convertStreamToString(InputStream is) {
		    String line = "";
		    StringBuilder total = new StringBuilder();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    try {
		        while ((line = rd.readLine()) != null) {
		            total.append(line);
		        }
		    } catch (Exception e) {
		        Toast.makeText(this, "Stream Exception", Toast.LENGTH_SHORT).show();
		    }
		    return total.toString();
		}
}
