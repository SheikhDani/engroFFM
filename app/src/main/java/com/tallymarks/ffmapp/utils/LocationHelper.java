package com.tallymarks.ffmapp.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.multidex.BuildConfig;


public class LocationHelper
{
        public static boolean isMockLocationEnabled(Context context) {
    boolean isMockLocation = false;
    try {
        //if marshmallow
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            isMockLocation = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), BuildConfig.APPLICATION_ID)== AppOpsManager.MODE_ALLOWED);
        } else {
            // in marshmallow this will always return true
            isMockLocation = !android.provider.Settings.Secure.getString(context.getContentResolver(), "mock_location").equals("0");
        }
    } catch (Exception e) {
        return isMockLocation;
    }
    return isMockLocation;
}

        public static void mockLocationIntent(Context context)
        {
            context.startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
        }

}
