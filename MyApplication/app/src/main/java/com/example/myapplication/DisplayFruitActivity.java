package com.example.myapplication;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


public class DisplayFruitActivity extends AppCompatActivity {
    TextView fruitName;
    TextView fruitWeight;
    TextView fruitPrice;

    private String GET_URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/stats";
    private static final String EVENT_TYPE_DISPLAY = "display";
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fruit);
        queue = Volley.newRequestQueue(this);

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

    @Override
    protected void onStart() {
        super.onStart();
        //get request when the page is loaded
        submitEvent(EVENT_TYPE_DISPLAY, System.currentTimeMillis());
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

    /**
     * this issues "FIRE AND FORGET" GET requests
     * @param event
     * @param data
     */
    private void submitEvent(String event, long data ) {
        Log.d("test", "submitEvent() called with: event = [" + event + "], data = [" + data + "]");
        String url = GET_URL+"?event="+event+"&data="+data;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.add(stringRequest);
    }

}
