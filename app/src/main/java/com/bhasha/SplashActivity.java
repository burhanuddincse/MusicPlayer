package com.bhasha;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.WindowManager;

/**
 * Created by Android on 11-12-2015.
 */
public class SplashActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.splash_layout);

        Thread background = new Thread() {
            public void run() {

                try
                {
                    // Thread will sleep for 5 seconds
                    sleep(3*1000);

                    // After 3 seconds redirect to another intent
                    Intent i=new Intent(getApplicationContext(), LanguageActivity.class);
                    startActivity(i);
                    finish();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        // start thread
        background.start();
    }
}
