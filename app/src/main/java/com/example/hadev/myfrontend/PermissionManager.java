package com.example.hadev.myfrontend;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Hadev on 7-2-2017.
 */
public interface PermissionManager {
    boolean checkPermission(AppCompatActivity activity, String permission);
    public boolean onRequestPermissionsResult(AppCompatActivity activity, int requestCode,
                                              String[] permissions, int[] grantResults);
}
