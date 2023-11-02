package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telecom.Call;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
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

public class Call_history extends AppCompatActivity implements JsonResponse  {

    EditText e1;
    ListView l1;
    TextView t1;
    SharedPreferences sh;
    String log_id;
    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    String[] call_id,num,duration,date,time,calltype,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_history);


        e1=(EditText)findViewById(R.id.etdate) ;
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView) findViewById(R.id.lvservices);

        log_id = sh.getString("log_id","");

        e1.setInputType(InputType.TYPE_NULL);
        e1.requestFocus();
        setDateTimeField();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

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
                jr.json_response = (JsonResponse) Call_history.this;
                String q = "/Call_historys?log_id="+sh.getString("log_id","")+"&date="+e1.getText().toString();
                jr.execute(q);
            }
        });

        JsonReq jr = new JsonReq();
        jr.json_response = (JsonResponse) Call_history.this;
        String q = "/Call_history?log_id="+sh.getString("log_id","");
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

            if (method.equalsIgnoreCase("Call_history")) {

                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja = (JSONArray) jo.getJSONArray("data");


                    call_id = new String[ja.length()];
                    num = new String[ja.length()];
                    date = new String[ja.length()];
                    duration = new String[ja.length()];
                    time = new String[ja.length()];
                    calltype = new String[ja.length()];


                    details = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        call_id[i] = ja.getJSONObject(i).getString(
                                "call_id");
                        num[i] = ja.getJSONObject(i).getString(
                                "num");
                        date[i] = ja.getJSONObject(i).getString(
                                "date");
                        duration[i] = ja.getJSONObject(i).getString(
                                "duration");
                        time[i] = ja.getJSONObject(i).getString(
                                "time");
                        calltype[i] = ja.getJSONObject(i).getString(
                                "calltype");

                        details[i]="Number : "+num[i]+"\nDuration : "+duration[i]+"\nDate : "+date[i]+"\nTime : "+time[i]+"\nCall Type : "+calltype[i] ;
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
