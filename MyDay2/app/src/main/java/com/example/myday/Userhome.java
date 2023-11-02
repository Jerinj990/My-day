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

    ImageView img1,img2,img3,img4,img5,img6;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Toast.makeText(getApplicationContext(),"Location"+LocationService.lati+LocationService.logi,Toast.LENGTH_LONG).show();

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
        img4=(ImageView) findViewById(R.id.imgnote);
        img4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                startActivity(new Intent(getApplicationContext(),Add_notes.class));

            }
        });
        img5=(ImageView) findViewById(R.id.imglogout);
        img5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                startActivity(new Intent(getApplicationContext(),login.class));

            }
        });
        img6=(ImageView) findViewById(R.id.imgfeed);
        img6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                startActivity(new Intent(getApplicationContext(),Feedback.class));

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