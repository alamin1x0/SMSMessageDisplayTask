package com.sms.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSRECEIVER";

    String msg, phoneNo = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: " + intent.getAction());

        if (intent.getAction() == SMS_RECEIVED) {
            Bundle dataBunndle = intent.getExtras();
            if (dataBunndle != null) {
                Object[] mypdu = (Object[]) dataBunndle.get("pdus*");
                final SmsMessage[] messages = new SmsMessage[mypdu.length];

                for (int i = 0; i < mypdu.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        String format = dataBunndle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) mypdu[i]);
                    }

                    msg = messages[i].getMessageBody();
                    phoneNo = messages[i].getOriginatingAddress();
                }
                Toast.makeText(context, "Message:" + msg + "\nNumber: " + phoneNo, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
