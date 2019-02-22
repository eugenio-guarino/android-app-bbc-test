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
    private TextView fruitName;
    private TextView fruitWeight;
    private TextView fruitPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fruit);
        fruitName = findViewById(R.id.textView);

        // open the intent sent by the first screen and call the appropriate methods
        // to display the information
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("jsonObject"));
            double price = Double.parseDouble(jsonObject.optString("price"));
            double weight = Double.parseDouble(jsonObject.optString("weight"));
            String fruit = jsonObject.optString("type");
            showPrice(price);
            showWeight(weight);

            fruitName.setText(fruit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * this methods converts the price in pence and pounds
     * @param price
     */
    private void showPrice(double price){
        price /= 100;
        fruitPrice = findViewById(R.id.price_info);
        if (price > 1.00) {
            fruitPrice.setText(Double.toString(price) + " pounds");
        }
        else if (price == 1.00){
            fruitPrice.setText(Double.toString(price) + " pound");
        }
        else{
            fruitPrice.setText(Double.toString(price) + " pence");
        }
    }

    /**
     * this methods converts the weight into kgs
     * @param weight
     */
    private void showWeight(double weight){

        weight /= 1000;
        fruitWeight = findViewById(R.id.weight_info);
        fruitWeight.setText(Double.toString(weight)+ " kilograms");
    }
}
