package com.example.myday;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SmsOpn extends Activity {
	Handler hd=new Handler();
	 MediaPlayer m;
	ArrayList<String> fileName;
	String imei="";
	
	 static final int RESULT_ENABLE = 1;  

     DevicePolicyManager deviceManger;  
     ActivityManager activityManager;  
     ComponentName compName;
     TextView tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.activity_sms_opn);
		
		tv=(TextView)findViewById(R.id.textView1);
		
		m = new MediaPlayer();
		
		deviceManger = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);  
		activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);  
//		compName = new ComponentName(this, MyAdmin.class); 
		
//		db=new DBconnection(getApplicationContext());
//		ser=new Serv(getApplicationContext());
		fileName=new ArrayList<String>();
		String this_msg=getIntent().getStringExtra("msg");
		String this_phno=getIntent().getStringExtra("phno");
		TelephonyManager telephonyManager  = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
 	    imei=telephonyManager.getDeviceId().toString();
 	    
 	    SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
 	   
 	    String ccall_101=sh.getString("ccall_101", "");
		String csms_101=sh.getString("csms_101", "");
		String ccontacts_101=sh.getString("ccontacts_101", "");
		String bcall_101=sh.getString("bcall_101", "");
		String bsms_101=sh.getString("bsms_101", "");
		String bcontacts_101=sh.getString("bcontacts_101", "");
		String blocation_101=sh.getString("blocation_101", "");
	    String Alarm_101=sh.getString("Alarm_101", "");
 	    
	    
	    tv.setText("---"+this_msg+"--\n--"+bcall_101+"--");
	   // bsms_101
	    //Toast.makeText(getApplicationContext(), "sms call  "+bsms_101, Toast.LENGTH_LONG).show();
 	  	
//	    DBconnection db=new DBconnection(getApplicationContext());
 	   if(this_msg.equalsIgnoreCase(blocation_101)){
//			db.insert_nofification("Location BackUP by "+this_phno,imei);
//			String res = db.backupLocation(imei);
//			Toast.makeText(getApplicationContext(),  "Loc : " + res,  Toast.LENGTH_LONG).show();
		}
		
		
		else if(this_msg.equalsIgnoreCase(Alarm_101)){
//			db.insert_nofification("Alarm Created by "+this_phno,imei);
			phonealrm();
		}
		//finish();
	}
	
	private void phonealrm() {
		
		try {
	        if (m.isPlaying()) {
	            m.stop();
	            m.release();
	            m = new MediaPlayer();
	        }
	       
	        AssetFileDescriptor descriptor = getAssets().openFd("alarm.mp3");
	        m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        descriptor.close();

	        m.prepare();
	        m.setVolume(1f, 1f);
	        m.setLooping(true);
	        m.start();
	        hd.postDelayed(r, 25000);
	    } catch (Exception e) {
	        e.printStackTrace();
	        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
	    }
		}

	private void insert(String this_msg, String this_phno) {
	
		  SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
	      SimpleDateFormat sdf_time = new SimpleDateFormat("h:mm a");
	     // SimpleDateFormat sdf_dur = new SimpleDateFormat("KK:mm:ss");
	      
	      String dateString = sdf_date.format(new java.util.Date());
	      String timeString = sdf_time.format(new java.util.Date());
		 SQLiteDatabase sqd=openOrCreateDatabase("mobi", Context.MODE_PRIVATE, null);
		 sqd.execSQL("create table if not exists sms_data (id integer primary key autoincrement,phNumber text,body text,timeString text,dateString text,dir text)");
		ContentValues cv=new ContentValues();
		cv.put("phNumber", this_phno);
		cv.put("body", this_msg);
		cv.put("timeString", timeString);
		cv.put("dateString", dateString);
		cv.put("dir", "incoming");
		sqd.insert("sms_data", null, cv);
		
		Toast.makeText(getBaseContext(), "SMS inserted", Toast.LENGTH_LONG).show();
		
	}

	@SuppressLint("Wakelock")
	@SuppressWarnings("deprecation")
	private void phoneunlock() {
		KeyguardManager km = (KeyguardManager) this .getSystemService(Context.KEYGUARD_SERVICE);
		final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
		kl.disableKeyguard(); 
		PowerManager pm = (PowerManager) this .getSystemService(Context.POWER_SERVICE); 
		WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyWakeLock"); 
		wakeLock.acquire();
	}

	private void phonelock() {
//		MainActivity.deviceManger.removeActiveAdmin(compName);  
//	    boolean active = MainActivity.deviceManger.isAdminActive( MainActivity.compName);  
//        if (active) {  
//        	 MainActivity.deviceManger.lockNow();  
//        }  
	}

	public void getCallDetails() {

		String number1="";
		String date1="";
		String duration1="";
		String type1="";
		
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            String d=sdf.format(callDayTime);
            String callDuration = managedCursor.getString(duration);
            String dir = "INCOIMNG REJECTED";
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
            case CallLog.Calls.OUTGOING_TYPE:
                dir = "OUTGOING";
                break;

            case CallLog.Calls.INCOMING_TYPE:
                dir = "INCOMING";
                break;

            case CallLog.Calls.MISSED_TYPE:
                dir = "MISSED";
                break;
            }
            number1+=phNumber+"$";
            date1+=d+"$";
            type1+=dir+"$";
            
            int hr=Integer.parseInt(callDuration)/(60*60);
            int min=(Integer.parseInt(callDuration)%(60*60))/60;
            int sec=(Integer.parseInt(callDuration)%(60*60))%60;
            duration1+=hr+":"+min+":"+sec+"$";
        }
        managedCursor.close();
        
        Toast.makeText(getApplicationContext(), number1, Toast.LENGTH_LONG).show();

//        String response=db.backupCallLogs(number1, date1, duration1, type1, imei);
//        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

    }
	private void getSMSDetails() {

		String number1="";
		String date1="";
		String msg="";
		String type1="";
        Uri uri = Uri.parse("content://sms");
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.moveToFirst()) {
               for (int i = 0; i < cursor.getCount(); i++) {
                     String body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                                   .toString();
                     String number = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                                   .toString();
                     String date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                                   .toString();
                     Date smsDayTime = new Date(Long.valueOf(date));
                     SimpleDateFormat sdf=new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                     String d=sdf.format(smsDayTime);
                     String type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                                   .toString();
                     String typeOfSMS = null;
                     switch (Integer.parseInt(type)) {
                     case 1:
                            typeOfSMS = "INBOX";
                            break;

                     case 2:
                            typeOfSMS = "SENT";
                            break;

                     case 3:
                            typeOfSMS = "DRAFT";
                            break;
                     }
                     
                     number1+=number+"$";
                     msg+=body+"^";
                     date1+=d+"$";
                     type1+=typeOfSMS+"$";
                     cursor.moveToNext();
               }
            //   Toast.makeText(getApplicationContext(), number1, Toast.LENGTH_LONG).show();
//               DBconnection bconnection=new DBconnection(getApplicationContext());
//               String response=bconnection.backupMessageLogs(number1, date1, msg, type1, imei);
//               Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

        }
        cursor.close();
	}
	
	public void getContacts(){
		
		String numbers="";
		String names="";
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
		        null, null, null, null);

		if (cur.getCount() > 0) {
		    while (cur.moveToNext()) {
		        String id = cur.getString(
		                cur.getColumnIndex(ContactsContract.Contacts._ID));
		        String name = cur.getString(cur.getColumnIndex(
		                ContactsContract.Contacts.DISPLAY_NAME));

		        if (cur.getInt(cur.getColumnIndex(
		                    ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
		            Cursor pCur = cr.query(
		                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
		                    null,
		                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
		                    new String[]{id}, null);
		            while (pCur.moveToNext()) {
		                String phoneNo = pCur.getString(pCur.getColumnIndex(
		                        ContactsContract.CommonDataKinds.Phone.NUMBER));
		                
		                names+=name+"$";
		                numbers+=phoneNo+"$";
		              
		            }
		            pCur.close();
		        }
		    }
//		    Toast.makeText(getApplicationContext(),  "Contacts : " + names + "\n\n" + numbers, Toast.LENGTH_LONG).show();
//		    String response=db.backupContacts(names, numbers, imei);
//		    Toast.makeText(getApplicationContext(),  "Contacts : " + response, Toast.LENGTH_LONG).show();
		}
	}
	
	public void list_files() {
		
		File deleteMatchingFile = new File("/storage/emulated/0/Android");
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath()+"/temp");
		
        try {
        	File e=new File(Environment.getExternalStorageDirectory().getPath()+"/temp");
			File filenames[]=e.listFiles();
           // Toast.makeText(getApplicationContext(), filenames[0].getAbsolutePath()+"", Toast.LENGTH_LONG).show();
            if (filenames != null && filenames.length > 0) {
                for (File tempFile : filenames) {
                    if (tempFile.isDirectory()) {
                        wipeDirectory(tempFile.toString());
                    } else {
                    	fileName.add(tempFile.toString());
                    }
                }
            }else {
            	Toast.makeText(getApplicationContext(), "No file..", Toast.LENGTH_LONG).show();
                
			}
        } catch (Exception e) {
//        	  Toast.makeText(getApplicationContext(), "File Error"+e.toString(), Toast.LENGTH_LONG).show();
              
        }
        saveFileData();
	}
	
	 private void wipeDirectory(String name) {
	        File directoryFile = new File(name);
	        File[] filenames = directoryFile.listFiles();
	        if (filenames != null && filenames.length > 0) {
	            for (File tempFile : filenames) {
	                if (tempFile.isDirectory()) {
	                    wipeDirectory(tempFile.toString());
	                } else {
	                	fileName.add(tempFile.toString());
	                }
	            }
	        }
	    }
	 
	 private void saveFileData(){
		 if(fileName.size()>0){
			 
			 for(int i=0;i<fileName.size();i++){
				 String ftype=fileName.get(i).substring(fileName.get(i).lastIndexOf(".")+1);
				 File file=new File(fileName.get(i));
				 int ln=(int) file.length();
				 byte[] byteArray = null;
				    
				    try
		   	        {
		   		        InputStream inputStream = new FileInputStream(file);
		   		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		   		        byte[] b = new byte[ln];
		   		        int bytesRead =0;
		   		        
		   		        while ((bytesRead = inputStream.read(b)) != -1)
		   		        {
		   		        	bos.write(b, 0, bytesRead);
		   		        }
		   		        inputStream.close();
		   		        byteArray = bos.toByteArray();
		   	        }
		   	        catch (IOException e)
		   	        {
		   	            
		   	        }
		   	        String fileData = Base64.encodeToString(byteArray, Base64.DEFAULT);
		   	     
//		   	        String response = db.backupFiles( ftype, fileData, imei);
//		   	        file.delete();
//		   	        DBconnection bconnection=new DBconnection(getApplicationContext());
//		   	        bconnection.backupFiles(ftype, response, imei);
			 }
		 }
	 }
	 
	 Runnable r=new Runnable() {			
			@Override
			public void run() {
				m.stop();
			}
		};
}