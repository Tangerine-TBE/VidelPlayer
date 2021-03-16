package com.example.module_ad.utils;

import android.app.Activity;
import android.content.Intent;

public class StartActivityUtil  {

    public static void startActivity(Activity activity,Class aClass,boolean isFinish,int action) {
        Intent intent = new Intent(activity, aClass);
        intent.putExtra(Contents.KEY_ACTION,action);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

}
