package com.sms.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.sms.task.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //Here in MainActivity we will write code for asking permission
    private static final int MY_PERMISSIONS_REQUEST_RECEIVER_SMS = 0;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";


    SMSReceiver smsReceiver = new SMSReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            binding.message.setText(msg);
            binding.phone.setText(phoneNo);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(smsReceiver, new IntentFilter(SMS_RECEIVED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Check if the permission is not gradnted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            //if the permission is not been grated then check if the user has denied the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECEIVE_SMS)) {
                //Do nothing as user has denied
            } else {
                //a pop up will appear asking for required permission i e Allow or Deny
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVER_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_RECEIVER_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thank you for permission!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Well I can't do anything until you permit me", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}