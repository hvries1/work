package com.example.hadev.myfrontend.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Hadev on 7-2-2017.
 */

public class MyPermissionManager implements PermissionManager {

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    
    private static final String TAG = "Permission";

    @Override
    public boolean checkPermission(AppCompatActivity activity, String permission) {
        if (permission.equals(Manifest.permission.READ_CONTACTS)) {
            // Check the SDK version and whether the permission is already granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    activity.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Request permissions to read contacts.");
                Log.d(TAG, "" + activity.checkSelfPermission(Manifest.permission.READ_CONTACTS));
                activity.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
                return false;
            }
            // Android version is lesser than 6.0 or the permission is already granted.
            Log.i(TAG, "Contact permissions have already been granted.");
            return true;
        }
        return false;
    }

    @Override
    public boolean onRequestPermissionsResult(AppCompatActivity activity, int requestCode,
                                              String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                Log.d(TAG, "permission granted");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "" + activity.checkSelfPermission(Manifest.permission.READ_CONTACTS));
                }
                return true;
            } else {
                Log.d(TAG, "permission denied");
                Toast.makeText(activity, "Until you grant the permission, we cannot display contacts", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

}
