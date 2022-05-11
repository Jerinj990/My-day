package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Userhome extends AppCompatActivity {

    ImageView img1,img2,img3;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toast.makeText(getApplicationContext(),"Login Id"+sh.getString("log_id",""),Toast.LENGTH_LONG).show();

        img1=(ImageView) findViewById(R.id.imgcall);
        img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                    startActivity(new Intent(getApplicationContext(),Call_history.class));

            }
        });

        img2=(ImageView) findViewById(R.id.imgsms);
        img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                    startActivity(new Intent(getApplicationContext(),Sms_history.class));

            }
        });

        img3=(ImageView) findViewById(R.id.imglocation);
        img3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                    startActivity(new Intent(getApplicationContext(),Location_history.class));

            }
        });

    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Userhome.class));
    }

}