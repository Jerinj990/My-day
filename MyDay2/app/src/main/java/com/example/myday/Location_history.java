package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class Location_history extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    EditText e1;
    ListView l1;
    TextView t1;
    SharedPreferences sh;
    String log_id;
    String[] location_id,latitude,longitude,place,date,details;

    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView) findViewById(R.id.lvservices);
        l1.setOnItemClickListener(this);
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
                jr.json_response = (JsonResponse) Location_history.this;
                String q = "/Location_historys?log_id="+sh.getString("log_id","")+"&date="+e1.getText().toString();
                jr.execute(q);
            }
        });


        JsonReq jr = new JsonReq();
        jr.json_response = (JsonResponse) Location_history.this;
        String q = "/Location_history?log_id="+log_id;
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

            if (method.equalsIgnoreCase("Location_history")) {

                String status = jo.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja = (JSONArray) jo.getJSONArray("data");


                    location_id = new String[ja.length()];
                    latitude = new String[ja.length()];
                    longitude = new String[ja.length()];
                    place = new String[ja.length()];
                    date = new String[ja.length()];

                    details = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        location_id[i] = ja.getJSONObject(i).getString(
                                "location_id");
                        latitude[i] = ja.getJSONObject(i).getString(
                                "latitude");
                        longitude[i] = ja.getJSONObject(i).getString(
                                "longitude");
                        place[i] = ja.getJSONObject(i).getString(
                                "place");
                        date[i] = ja.getJSONObject(i).getString(
                                "date");

                        details[i]="Place : "+place[i]+"\nLatitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\nDate : "+date[i] ;
                    }

                    l1.setAdapter(new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1, details));

                }
            }
            else{
                Toast.makeText(getApplicationContext(),"No Data",Toast.LENGTH_LONG).show();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+latitude[position]+","+longitude[position]));
        startActivity(intent);
    }
}
