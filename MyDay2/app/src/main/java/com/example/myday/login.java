package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myday.R;
import com.example.myday.signup;

import org.json.JSONArray;
import org.json.JSONObject;

public class login extends AppCompatActivity implements JsonResponse{
    EditText e1,e2;
    Button b1,b2;
    String uname,password,usertype,imei,logid;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        e1=(EditText)findViewById(R.id.uname);
        e2=(EditText)findViewById(R.id.password);
        b1=(Button) findViewById(R.id.button);
        b2=(Button)findViewById(R.id.signup);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signup.class));
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname=e1.getText().toString();
                password=e2.getText().toString();
                if (uname.equalsIgnoreCase(""))
                {
                    e1.setError("please enter values");
                    e1.setFocusable(true);
                }
                else if(password.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else
                {
                    JsonReq JR=new JsonReq();
                    JR.json_response=(JsonResponse)login.this;
                    String q="/login?username=" + uname + "&password=" + password;
                    q = q.replace(" ", "%20");
                    JR.execute(q);

                }

               // Toast.makeText(getApplicationContext(),"username"+uname+"password"+password,Toast.LENGTH_SHORT);

            }
        });

    }

    @Override
    public void response(JSONObject jo) {

        try {
            String status = jo.getString("status");
            Log.d("pearl", status);

            if (status.equalsIgnoreCase("success")) {
                JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                logid = ja1.getJSONObject(0).getString("login_id");
                usertype = ja1.getJSONObject(0).getString("usertype");
                imei = ja1.getJSONObject(0).getString("imei");

                SharedPreferences.Editor e = sh.edit();
                e.putString("log_id", logid);
                e.putString("imei", imei);
                e.commit();

                if(usertype.equals("user"))
                {
                    Toast.makeText(getApplicationContext(),"Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(getApplicationContext(),LocationService.class);
                    startService(in);
                    Intent ii=new Intent(getApplicationContext(),CallDetails.class);
                    startService(ii);
                    Intent ij=new Intent(getApplicationContext(),CallReceiver.class);
                    startService(ij);
                    Intent ie=new Intent(getApplicationContext(),ServiceForSmsIncoming.class);
                    startService(ie);
                    Intent im=new Intent(getApplicationContext(),ServiceForSmsOutgoing.class);
                    startService(im);

                    startActivity(new Intent(getApplicationContext(),Userhome.class));
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Login failed invalid username and password", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        } catch (Exception e) {
            // TODO: handle exception

            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    @Override

    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),ipsettings.class);
        startActivity(b);

    }
}