package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    String URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/data.json";
    ArrayList<String> FruitName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FruitName=new ArrayList<>();
        spinner= findViewById(R.id.fruit_Name);
        loadSpinnerData(URL);
    }

    public void displayInformation(View view){
        Bundle extras = new Bundle();
        Intent intent = new Intent(this, DisplayFruitActivity.class);

        spinner= findViewById(R.id.fruit_Name);
        String fruit = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        int fruitIndex = spinner.getSelectedItemPosition();

        extras.putString("fruit", fruit);
        extras.putInt("index", fruitIndex);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void loadSpinnerData(String url) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("fruit");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String type=jsonObject1.getString("type");
                        FruitName.add(type);
                    }

                    spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, FruitName));
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

}
