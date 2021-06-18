package com.example.certiphonedemo.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.certiphonedemo.MainActivity;

public class SmsReader {
MainActivity mainActivity;

    public SmsReader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        if (!checkPermission(mainActivity.getApplicationContext(), Manifest.permission.READ_SMS)) {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        }
    }

    public String Read_SMS(){
        if (checkPermission(mainActivity.getApplicationContext(), Manifest.permission.READ_SMS)) {
            Cursor cursor = mainActivity.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
            cursor.moveToFirst();

            return (cursor.getString(12));
        }
        return null;
    }
    private boolean checkPermission(Context context, String permission) {
        int check = ContextCompat.checkSelfPermission(context, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}
