package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Sms_history extends AppCompatActivity implements JsonResponse  {

    EditText e1;
    ListView l1;
    TextView t1;
    SharedPreferences sh;

    String log_id;
    String[] sms_id,num,message,date,time,msgtype,details;
    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_history);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView) findViewById(R.id.lvservices);
        e1=(EditText)findViewById(R.id.etdate) ;
        log_id = sh.getString("log_id","");

        e1.setInputType(InputType.TYPE_NULL);
        e1.requestFocus();
        setDateTimeField();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                JsonReq jr = new JsonReq();
                jr.json_response = (JsonResponse) Sms_history.this;
                String q = "/Sms_historys?log_id="+sh.getString("log_id","")+"&date="+e1.getText().toString();
                jr.execute(q);
            }
        });

        JsonReq jr = new JsonReq();
        jr.json_response = (JsonResponse) Sms_history.this;
        String q = "/Sms_history?log_id="+log_id;
        jr.execute(q);
    }

    private void setDateTimeField() {

        e1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fromDatePickerDialog.show();



            }
        });
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog =new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                e1.setText(dateFormatter.format(newDate.getTime()));


//                bdate=e2.getText().toString();

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {

            String method = jo.getString("method");

            if (method.equalsIgnoreCase("Sms_history")) {

                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja = (JSONArray) jo.getJSONArray("data");


                    sms_id = new String[ja.length()];
                    num = new String[ja.length()];
                    message = new String[ja.length()];
                    date= new String[ja.length()];
                    time= new String[ja.length()];
                    msgtype= new String[ja.length()];


                    details = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        sms_id[i] = ja.getJSONObject(i).getString(
                                "sms_id");
                        num[i] = ja.getJSONObject(i).getString(
                                "num");
                        message[i] = ja.getJSONObject(i).getString(
                                "message");
                        date[i] = ja.getJSONObject(i).getString(
                                "date");
                        time[i] = ja.getJSONObject(i).getString(
                                "time");
                        msgtype[i] = ja.getJSONObject(i).getString(
                                "msgtype");

                        details[i]="Number : "+num[i]+"\nMessage : "+message[i]+"\nDate : "+date[i]+"\nTime : "+time[i]+"\nMessage Type : "+msgtype[i] ;
                    }

                    l1.setAdapter(new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1, details));

                }
                else{
                    Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "haii" + e,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), Userhome.class));
    }



}
