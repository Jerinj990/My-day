package com.example.myday;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMS_RECEIVER extends BroadcastReceiver {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
		Bundle b = arg1.getExtras();
		Object[] obj = (Object[]) b.get("pdus");
		SmsMessage[] sms_list = new SmsMessage[obj.length];
		
		for (int i = 0; i < obj.length; i++) {
			sms_list[i] = SmsMessage.createFromPdu((byte[]) obj[i]);			
		}
		String this_msg = sms_list[0].getMessageBody().trim();	

		String this_phno= sms_list[0].getOriginatingAddress();	
		
		Intent i=new Intent(arg0, SmsOpn.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("msg", this_msg);
		i.putExtra("phno", this_phno);
		arg0.startActivity(i);	
	}
}