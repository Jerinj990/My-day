package com.example.myday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Complaints extends AppCompatActivity implements JsonResponse {
    EditText e1;
    Button b1;
    ListView l1;
    String complaint,log_id;
    String[] date,complaints,statuss,val,solution;
    SharedPreferences sh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        log_id = sh.getString("logid","");
        l1=(ListView)findViewById(R.id.cview);

        JsonReq jr= new JsonReq();
        jr.json_response=(JsonResponse)Complaints.this;
        String q="/viewcomplaint?log_id="+log_id;
        jr.execute(q);
        e1=(EditText)findViewById(R.id.complaint);
        b1=(Button)findViewById(R.id.scomplaint);




        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                log_id = sh.getString("logid","");
                complaint=e1.getText().toString();
                JsonReq jr= new JsonReq();
                jr.json_response=(JsonResponse) Complaints.this;
                String q="/sendcomplaint?complaint="+complaint+"&log_id="+log_id;

                q.replace("", "%20");
                jr.execute(q);



            }
        });


    }



    @Override
    public void response(JSONObject jo) {
        // TODO Auto-generated method stub

        try
        {

            String method=jo.getString("method");

            if(method.equalsIgnoreCase("viewcomplaint")){

                String status=jo.getString("status");
                if(status.equalsIgnoreCase("success"))
                {
                    JSONArray ja=(JSONArray)jo.getJSONArray("data");


                    date=new String[ja.length()];
                    complaints= new String[ja.length()];
                    statuss=new String[ja.length()];
                    solution=new String[ja.length()];

                    val= new String[ja.length()];


                    for(int i=0;i<ja.length();i++)
                    {

                        date[i]=ja.getJSONObject(i).getString("date");
                        complaints[i]=ja.getJSONObject(i).getString("complaint");
                        solution[i]=ja.getJSONObject(i).getString("reply");
                        val[i]="Complaint : "+complaints[i]+"\nSolution : "+solution[i]+"\nDate : "+date[i];
                    }

                    l1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,val));


                }
            }
            if(method.equalsIgnoreCase("send")){

                String status=jo.getString("status");
                if(status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getApplicationContext(), "Complaint Sended", Toast.LENGTH_LONG).show();
//
                    startActivity(new Intent(getApplicationContext(), Complaints.class));
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
        startActivity(new Intent(getApplicationContext(), Userhome.class));
    }

}
