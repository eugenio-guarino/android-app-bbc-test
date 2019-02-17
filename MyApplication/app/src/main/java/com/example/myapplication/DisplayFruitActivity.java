package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
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

public class DisplayFruitActivity extends AppCompatActivity {

    String URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/data.json";
    TextView fruitName;
    TextView fruitWeight;
    TextView fruitPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fruit);

        Intent intent = getIntent();

        Bundle extras = intent.getExtras();

        String fruit = extras.getString("fruit");
        int fruitIndex = extras.getInt("index");
        fruitName = findViewById(R.id.textView);
        fruitName.setText(fruit);

        loadInformation(URL, fruitIndex);
    }

    private void loadInformation(String url, final int index) {
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("fruit");
                    JSONObject jsonObject1=jsonArray.getJSONObject(index);
                    String price=jsonObject1.optString("price");
                    String weight=jsonObject1.optString("weight");

                    fruitPrice = findViewById(R.id.price_info);
                    fruitPrice.setText(price);

                    fruitWeight = findViewById(R.id.weight_info);
                    fruitWeight.setText(weight);

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
