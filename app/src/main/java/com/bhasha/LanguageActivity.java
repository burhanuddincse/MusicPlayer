package com.bhasha;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Android on 11-12-2015.
 */
public class LanguageActivity extends AppCompatActivity
{
    Button gujarati, english;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_layout);

        gujarati = (Button)findViewById(R.id.btGujarati);
        english = (Button)findViewById(R.id.btEnglish);

        gujarati.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state))
                {
                    File mydir = new File(Environment.getExternalStorageDirectory() + "/bhasha-gujarati/");
                    if(!mydir.exists())
                        mydir.mkdirs();
                    Intent i=new Intent(getApplicationContext(), AndroidBuildingMusicPlayerActivity.class);
                    i.putExtra("language","gujarati");
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "SD card not present", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        english.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state))
                {
                    File mydir = new File(Environment.getExternalStorageDirectory() + "/bhasha-english/");
                    if(!mydir.exists())
                        mydir.mkdirs();

                    Intent i=new Intent(getApplicationContext(), AndroidBuildingMusicPlayerActivity.class);
                    i.putExtra("language","english");
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "SD card not present", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }
}
