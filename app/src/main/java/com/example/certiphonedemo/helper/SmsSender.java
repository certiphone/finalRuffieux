package com.example.certiphonedemo.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.certiphonedemo.FirstFragment;

public class SmsSender {
    private FirstFragment firstFragment;
    public SmsSender(FirstFragment firstFragment) {
        this.firstFragment=firstFragment;
        if (!checkPermission(firstFragment.getContext(), Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(firstFragment.getActivity(), new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        }
    }

    public boolean onSend(String number, String sms){
        String phoneNumber= number;
        String smsMessage= sms;

        if(phoneNumber==null || phoneNumber.length()==0 || smsMessage==null || smsMessage.length()==0){
            return false;
        }
        if(checkPermission(firstFragment.getContext(),  Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null);
            return true;
        } else {
            return false;

        }
    }

    private boolean checkPermission(Context context, String permission) {
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
