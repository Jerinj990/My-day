package com.example.myday;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CallDetails extends Service implements JsonResponse {
//	
//	String namespace="urn:demo";
//	String method="calllog";
	
//	String soapaction=namespace+method;
	
	SharedPreferences sh;
	Editor ed;
	Cursor callDetailCursor;
	String blocknumber="";

	String opn;
	String dt="",tm="";

	TelephonyManager telman;
	int flag2=0;

	 public static int flg=0;	
	 String phnop="",imei;
	 @TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onCreate() 
	 {		
	//	 Toast.makeText(getApplicationContext(), "service started", Toast.LENGTH_SHORT).show();
		 
		// TODO Auto-generated method stub
		super.onCreate();
		
		if (Build.VERSION.SDK_INT > 9)
		{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		
		sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ed=sh.edit();

		 SimpleDateFormat tet=new SimpleDateFormat("hh:mm:ss");
		 tm=tet.format(new Date());
		 
		 telman=(TelephonyManager)getApplicationContext().getSystemService(TELEPHONY_SERVICE);
//		 imei=telman.getDeviceId().toString();
		 imei=sh.getString("imei","");
		
		 telman.listen(phlist,PhoneStateListener.LISTEN_CALL_STATE);
		 Log.d("....old...", ".....00");		
	}
  
	 public PhoneStateListener phlist=new PhoneStateListener()
   {
	   public void onCallStateChanged(int state, String inNum) 
	   {
		  switch (state) 
		  {  
		     case TelephonyManager.CALL_STATE_IDLE:
						
		    	 String duration="";
		    	 
		    	 try {
		    		 callDetailCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,null,null, CallLog.Calls.DATE + " DESC limit 1");
					 int callDuration=callDetailCursor.getColumnIndex(CallLog.Calls.DURATION);
					if(callDetailCursor.getCount()>0)
				    {		
						//Toast.makeText(getApplicationContext(), "inside", Toast.LENGTH_SHORT).show();
						 while(callDetailCursor.moveToNext())
					     {
							duration=callDetailCursor.getString(callDuration);
						 }
				    
								SharedPreferences shp=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
								Editor edit=shp.edit();
								edit.putString("duration", duration);
								edit.commit();
				    }
				}
						 
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
						Toast.makeText(getApplicationContext(), "error1 in call:"+e.getMessage(), Toast.LENGTH_SHORT).show();
							Log.d("error1",e.getMessage());
						}
						
						
						SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
						String name = preferences.getString("callstatus", "hi");
				
						if(name.equalsIgnoreCase("incoming"))
						{
							Log.d("....1....", "..incall..");
							

							SharedPreferences shpr=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							Editor edit1=shpr.edit();

	
							edit1.commit();
							
							
							
							try 
							{
							
							} 
							catch (Exception e)
							{
								// TODO Auto-generated catch block
								Toast.makeText(getApplicationContext(), "error2 in call:"+e.getMessage(), Toast.LENGTH_SHORT).show();
								Log.d("error2",e.getMessage());
							} 
							
							
							SharedPreferences sh1=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					
						String ss=sh1.getString("incoming number", "");
							
							String dur=sh1.getString("duration", "");
							
							
								inscall(imei,ss,"incoming",dur);
							 flg=0;
						 }
						
						
						 else if(flg==1)
						 {
							 Log.d("....1....", "..outcall..");
							 try 
							 {
								SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
								opn=sh.getString("num", "");
								
								//call1(MainActivity.phoneid,opn,"outgoing",duration,d,t);
							 } 
							 catch (Exception e) 
							 {
								// TODO Auto-generated catch block
								 Toast.makeText(getApplicationContext(), "error3 in call:"+e.getMessage(), Toast.LENGTH_SHORT).show();
								 Log.d("error3",e.getMessage());
							 }
							 
							 
							 SharedPreferences sh2=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
								
								
								String dur=sh2.getString("duration", "");
								inscall(imei,opn,"outgoing",dur);
							 
							 
							 flg=0;
						 }
						 
						
				         Editor editor = preferences.edit();
				         editor.putString("callstatus","idle");
				         editor.commit();
				         
				         break;
				         
				         
				         
				         
				         
			
			
		     case TelephonyManager.CALL_STATE_OFFHOOK:
			
		    	 		SimpleDateFormat sm=new SimpleDateFormat("dd/MM/yyyy");
		    	 		SimpleDateFormat sn=new SimpleDateFormat("hh:mm:ss");
			
		    	 		flg=1;
		    	 		flag2=0;
		    	 		
		    	 		dt=sm.format(new Date());
		    	 		tm=sn.format(new Date());
		    	 		
		    	 		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    	 		String opn=pref.getString("num", "");
			
		    	 		if(opn.equalsIgnoreCase(""))
		    	 		{
		    	 				opn=phnop;
		    	 		}
			
		    	 	//	SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    	 		String blknum=sh.getString("block","");
		    	 		
		    	 		int xy=0;
		    	 		Log.d("...outn..",blknum+"..outn.."+opn);
		    	 		if(!blknum.equalsIgnoreCase("@"))
		    	 		{
			
		    	 			String b[]=blknum.split("@");
		    	 			for(int i=0;i<b.length;i++)
		    	 			{
		    	 				if(b[i].length()>=10 && opn.length()>=10)
		    	 				{
		    	 					b[i]=b[i].substring(b[i].length()-10,b[i].length() );
		    	 					opn=opn.substring(opn.length()-10,opn.length() );
		    	 					Log.d("....outnum....",b[i]+"..b[i]..outnum.."+opn);
		    	 				}				
		    	 				if(b[i].equals(opn))
		    	 				{
		    	 					
		    	 					xy=1;
		    	 					//Toast.makeText(getApplicationContext(), "Equal", Toast.LENGTH_LONG).show();
		    	 				}
		    	 			}		
		    	 		}
			
		    	 		if(xy==1)
		    	 		{
		    	 			Toast.makeText(getApplicationContext(), "BLOCKED", Toast.LENGTH_SHORT).show();
		    	 			////call reject					
		    	 			try 
		    	 			{		
		    	 				
		    	 				telman = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
								Class c = Class.forName(telman.getClass().getName());
								Method m = c.getDeclaredMethod("getITelephony");
								m.setAccessible(true);
//								ITelephony telephonyService = (ITelephony)m.invoke(telman);
//								telephonyService.endCall();
    	 			} 
		    	 			catch (Exception e)
		    	 			{
		    	 				// TODO: handle exception
		    	 				Toast.makeText(getApplicationContext(), "error4 in call:"+e.getMessage(), Toast.LENGTH_SHORT).show();
		    	 				Log.d("error4",e.getMessage());
		    	 			}
		    	 		}
		    	 		
		    	 		
		    	 		break;
		    	 		
		    	 		
		    	 		
		    	 		 
		    	 		
	
		     case TelephonyManager.CALL_STATE_RINGING:

		     			phnop=inNum;
		     			flag2=1;
		     			//Toast.makeText(getApplicationContext(), phnop, Toast.LENGTH_LONG).show();
						sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						Editor edd=sh.edit();
						edd.putString("incoming number", phnop);
						edd.commit();
						
						blknum=sh.getString("block","");
					
						// saving the incoming number	
						
						Editor ed= sh.edit();
						ed.putString("callstatus","incoming");
						ed.putString("num",inNum);
						ed.commit();
						//ends
									
						int xyz=0;	
						Log.d("...rnggg..",blknum+"..innum.."+inNum);
						if(!blknum.equalsIgnoreCase("@"))
						{        
							String bb[]=blknum.split("@");
							for(int i=0;i<bb.length;i++)
							{
								if(bb[i].length()>=10)
								{
									bb[i]=bb[i].substring(bb[i].length()-10,bb[i].length() );
									inNum=inNum.substring(inNum.length()-10,inNum.length() );
									Log.d("...rnggg..substring..",bb[i]+"..innum.."+inNum);
								}
				
								if(bb[i].equals(inNum))
								{		
										xyz=1;
								}
							}
						}		////call reject 
						if(xyz==1)
						{		
							try
							{
								Log.d("...rnggg..","cutng........");  
			      		
								telman = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
								Class c = Class.forName(telman.getClass().getName());
								Method m = c.getDeclaredMethod("getITelephony");
								m.setAccessible(true);
//								ITelephony telephonyService = (ITelephony)m.invoke(telman);
//								telephonyService.endCall();
								
								
							 				 
							}
							catch (Exception e) 
							{
								// TODO: handle exception
								Toast.makeText(getApplicationContext(), "error5 in call:"+e.getMessage(), Toast.LENGTH_SHORT).show();
								Log.d("error5",e.getMessage());
								
							}
						}	
						
					
						break;
		  }	
		 
	   }

	private void inscall(String imei, String ss, String type, String dur) {
		
		

		
		SimpleDateFormat sdate=new SimpleDateFormat("dd/MM/yyy hh:mm:ss");
		String dt=sdate.format(new Date());
		
		
		JsonReq jr= new JsonReq();
		jr.json_response=(JsonResponse) CallDetails.this;
		String q="/calllog/?uid="+sh.getString("log_id", "")+"&imei="+sh.getString("imei", "")+"&phone="+ss+"&date="+dt+"&time="+tm+"&duration="+dur+"&type="+type;
		q.replace("", "%20");
		jr.execute(q);
		
//		
//		try{
//			SimpleDateFormat sdatein = new SimpleDateFormat("dd/MM/yyyy");
//			String dt=sdatein.format(new Date());
//			
//			SoapObject sop=new SoapObject(namespace, method);
//			sop.addProperty("uid",sh.getString("lid", ""));
//        	sop.addProperty("imei", imei);
//        	sop.addProperty("phone", ss);
//        	
//        	sop.addProperty("date",dt);
//        	sop.addProperty("time",tm);
//        	sop.addProperty("duration", dur);
//        	sop.addProperty("type", type);
//        	
//        	
//        
//        	
//        	SoapSerializationEnvelope senv=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        	senv.setOutputSoapObject(sop);
//        	
//        	
//        	
//        	HttpTransportSE htp=new HttpTransportSE(Ip.url);
//        	htp.call(soapaction, senv);
//        	
//        	String result=senv.getResponse().toString();
//			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//		}catch(Exception e){
//			
//		}
	}

   };
  
	
	public IBinder onBind(Intent arg0) {
		
		return null;
	}


	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
		
		try
		{
			String status=jo.getString("status");
			Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
			if(status.equalsIgnoreCase("success"))
			{
				Toast.makeText(getApplicationContext(), "Call Details Inserted Successfully", Toast.LENGTH_LONG).show();
				startActivity(new Intent(getApplicationContext(),login.class));
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Call Details Inserted Successfull", Toast.LENGTH_LONG).show();
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Hai"+e, Toast.LENGTH_LONG).show();
		}
	}

}
