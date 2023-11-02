package com.example.myday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

public class ServiceForSmsIncoming extends BroadcastReceiver implements JsonResponse{
	 
//	  String namespace="urn:demo";//website open for every website there were same namespace
//	  String method="msglog";   //databse name in web
//	  String soap=namespace+method; 
	  Context context;
	  Handler hd=new Handler();
	String msg,number,blocknumber,nums;
	SharedPreferences sh;
	MediaPlayer m;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		context=arg0;
		sh=PreferenceManager.getDefaultSharedPreferences(arg0);
		
		Bundle b = arg1.getExtras();
		Object[] obj = (Object[]) b.get("pdus");
		SmsMessage[] sms_list = new SmsMessage[obj.length];//bundle b is a object the msg s inside tht object

		for (int i = 0; i < obj.length; i++) 
		{
			sms_list[i] = SmsMessage.createFromPdu((byte[]) obj[i]);			
		}
		

		msg = sms_list[0].getMessageBody();
		number = sms_list[0].getOriginatingAddress();
		Log.d("=================="+number, "msg--"+msg);
		blocknumber=sh.getString("block","anyType{}");
		
		if(blocknumber.equalsIgnoreCase("anyType{}"))
		{
			Toast.makeText(arg0, "No block number", Toast.LENGTH_SHORT).show();
		}
		else
		{
		
			
			checkBlock(blocknumber, number,arg0);
			//Toast.makeText(arg0, "block number"+blocknumber, Toast.LENGTH_SHORT).show();
		}
		
		msglogs(msg, number, "incoming");
	}
	
	private void msglogs(String msg, String phone, String type) {
		// TODO Auto-generated method stub
//		Toast.makeText(context, "Emergency Number" +Login.emergencynumber, Toast.LENGTH_LONG).show();
//		Toast.makeText(context, "Number" +number, Toast.LENGTH_LONG).show();
//		nums="+91"+Login.emergencynumber;
//		Toast.makeText(context, "Emergency Numbers" +nums, Toast.LENGTH_LONG).show();
//		Toast.makeText(context, "Message1" +msg, Toast.LENGTH_LONG).show();
//		Log.d("Emergency Nuber",Login.emergencynumber);
		
//		if(number.equals(nums)|| number.equals(Login.emergencynumber))
//		{
//			Toast.makeText(context, "Message" +msg, Toast.LENGTH_LONG).show();
//			Log.d("Message",msg);
//			if(msg.equals("send location"))
//			{
//				Toast.makeText(context, "Location HAiii", Toast.LENGTH_LONG).show();
//				String numm = Login.emergencynumber;
//				String msgmsg = "location information http://www.google.com/maps?q="+LocationService.lati+","+LocationService.logi;
//				SmsManager sm = SmsManager.getDefault();
//				sm.sendTextMessage(numm, null, msgmsg, null, null);
//			}
//			if(msg.equalsIgnoreCase("capture image"))
//			{
//				Toast.makeText(context, "capture HAiii", Toast.LENGTH_LONG).show();
//				 context.startService(new Intent(context,CameraService.class));
//			}
//			if(msg.equalsIgnoreCase("stop capture"))
//			{
//				Toast.makeText(context, "stop capture", Toast.LENGTH_LONG).show();
//				 context.stopService(new Intent(context,CameraService.class));
//			}
//			if(msg.equals("Make Alarm"))
//			{
//				try {
//					Toast.makeText(context, "Alarm HAiii", Toast.LENGTH_LONG).show();
//			        if (m.isPlaying()) {
//			            m.stop();
//			            m.release();
//			            m = new MediaPlayer();
//			        }
//
//			        AssetFileDescriptor descriptor = context.getAssets().openFd("alarm.mp3");
//			        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
//			        descriptor.close();
//
//			        m.prepare();
//			        m.setVolume(1f, 1f);
//			        m.setLooping(true);
//			        m.start();
//			        hd.postDelayed(r, 25000);
//			    } catch (Exception e) {
//			        e.printStackTrace();
//			        Toast.makeText(context,e.toString(),Toast.LENGTH_LONG).show();
//			    }
//			}
//		}
		
		JsonReq JR= new JsonReq();
		JR.json_response=(JsonResponse)ServiceForSmsIncoming.this;
		String q="/msglog/?id="+sh.getString("log_id", "")+"&imei="+sh.getString("imei","")+"&phone="+phone+"&message="+msg+"&type=incoming";
		JR.execute(q);
//		try
//		{
//			
//			Log.d("-----inside msglogs----", "gggggggggggggggggg");
//			
//			SoapObject sop=new SoapObject(namespace,method);
//			//sop.addProperty("uid",sh.getString("lid", ""));
//			sop.addProperty("imei",Login.imei);
//			sop.addProperty("phone", phone);
//			sop.addProperty("message", msg);
//			
//			sop.addProperty("type", "incoming");
//			
//			 
//			SoapSerializationEnvelope sen=new SoapSerializationEnvelope(SoapEnvelope.VER11);
//		    sen.setOutputSoapObject(sop);
//		    
//		    
//		    HttpTransportSE http=new HttpTransportSE(Ip.url);
//		    http.call(soap, sen);
//		    String tyy=sen.getResponse().toString();
//		  Log.d("======", tyy);
//			Toast.makeText(context, tyy, Toast.LENGTH_LONG).show();
//			
//		}
//		catch(Exception ex)
//		{
////			Log.d("-----error----", ex.getMessage());
////			
//			Toast.makeText(context, "error msg"+ex, Toast.LENGTH_LONG).show();
//		}
	}
	
	

	public void checkBlock(String blockednumber,String currentnumber,Context cntxt)
	{
		String [] blockednumers=blockednumber.split("#");
		int flagforblock=0;
		
				  		 	
		for(int i=0;i<blockednumers.length;i++)
		{
				if(blockednumers[i].length()>=10 && currentnumber.length()>=10)
				{
					blockednumers[i]=blockednumers[i].substring(blockednumers[i].length()-10,blockednumers[i].length() );
					currentnumber=currentnumber.substring(currentnumber.length()-10,currentnumber.length() );
					Log.d("....outnum....",blockednumers[i]+"..b[i]..outnum.."+currentnumber);
				}				
				if(blockednumers[i].equals(currentnumber))
				{
					flagforblock=1;
				}
		}
		
		if(flagforblock==1)
 		{
 			////msg reject					
 			try 
 			{			      		   
 				abortBroadcast();		 
 			} 
 			catch (Exception e)
 			{
 				// TODO: handle exception
 				Log.d("error in call blocking",e.getMessage()+"");
 				Toast.makeText(cntxt, "error in message blocking:"+e.getMessage()+"", Toast.LENGTH_SHORT).show();
 			}
 		}
	
	}

	@Override
	public void response(JSONObject jo) {
		// TODO Auto-generated method stub
	
			try{
			
				String status=jo.getString("status");
				if(status.equalsIgnoreCase("success"))
				{
					
					 Log.d("======", status);
					Toast.makeText(context, status, Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(context, "Not Success", Toast.LENGTH_LONG).show();
				}
			}
			catch(Exception e){
				e.printStackTrace();
				Toast.makeText(context, "haii"+e, Toast.LENGTH_LONG).show();
			}
	}
		
	 Runnable r=new Runnable() {			
			@Override
			public void run() {
				m.stop();
			}
		};
}
