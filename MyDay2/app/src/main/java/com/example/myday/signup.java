package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class signup extends AppCompatActivity implements JsonResponse{
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    RadioButton r1,r2;
    Button b1;
    String firstname,lastname,place,email,mobilenumber,uname,password,gender,imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e1=(EditText)findViewById(R.id.firstname);
        e2=(EditText)findViewById(R.id.lastname);
        e3=(EditText)findViewById(R.id.place);
        e4=(EditText)findViewById(R.id.email);
        e5=(EditText)findViewById(R.id.mobilenumber);
        e6=(EditText)findViewById(R.id.uname);
        e7=(EditText)findViewById(R.id.password);
        e8=(EditText)findViewById(R.id.imei);

        r1=(RadioButton) findViewById(R.id.male);
        r2=(RadioButton)findViewById(R.id.female);

        b1=(Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname=e1.getText().toString();
                lastname=e2.getText().toString();
                place=e3.getText().toString();
                email=e4.getText().toString();
                mobilenumber=e5.getText().toString();
                uname=e6.getText().toString();
                password=e7.getText().toString();
                imei=e8.getText().toString();
                if(r1.isChecked())
                {
                    gender="male";
                }
                else{
                    gender="female";
                }
                if (firstname.equalsIgnoreCase(""))
                {
                    e1.setError("please enter values");
                    e1.setFocusable(true);
                }
                else if(lastname.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else if(place.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else if(email.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else if(mobilenumber.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else if(uname.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else if(password.equalsIgnoreCase(""))
                {
                    e2.setError("please enter values");
                    e2.setFocusable(true);
                }
                else if(imei.equalsIgnoreCase(""))
                {
                    e8.setError("please enter values");
                    e8.setFocusable(true);
                }
                else{
                    JsonReq jr= new JsonReq();
                    jr.json_response=(JsonResponse) signup.this;
                    String q="/register?fname="+firstname+"&lname="+lastname+"&place="+place+"&username="+uname+"&password="+password+"&phone="+mobilenumber+"&email="+email+"&gender="+gender+"&imei="+imei;
                    q.replace("", "%20");
                    jr.execute(q);

                }


            }
        });
    }

    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try
        {

            String method=jo.getString("method");
            if(method.equalsIgnoreCase("send")){

                String status=jo.getString("status");
                if(status.equalsIgnoreCase("Success"))
                {
                    Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
//
                    startActivity(new Intent(getApplicationContext(), login.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Failed....", Toast.LENGTH_LONG).show();
                }
            }

        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "haii"+e, Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), ipsettings.class));
    }

}
