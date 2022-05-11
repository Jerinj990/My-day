package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Location_history extends AppCompatActivity implements JsonResponse  {


    ListView l1;
    TextView t1;
    SharedPreferences sh;
    String log_id;
    String[] location_id,latitude,longitude,date,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_history);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        l1=(ListView) findViewById(R.id.lvservices);

        log_id = sh.getString("log_id","");


        JsonReq jr = new JsonReq();
        jr.json_response = (JsonResponse) Location_history.this;
        String q = "/Location_history?log_id="+log_id;
        jr.execute(q);
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
                    date = new String[ja.length()];

                    details = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        location_id[i] = ja.getJSONObject(i).getString(
                                "location_id");
                        latitude[i] = ja.getJSONObject(i).getString(
                                "latitude");
                        longitude[i] = ja.getJSONObject(i).getString(
                                "longitude");
                        date[i] = ja.getJSONObject(i).getString(
                                "date");

                        details[i]="Latitude : "+latitude[i]+"\nLongitude : "+longitude[i]+"\nDate : "+date[i] ;
                    }

                    l1.setAdapter(new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1, details));

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
