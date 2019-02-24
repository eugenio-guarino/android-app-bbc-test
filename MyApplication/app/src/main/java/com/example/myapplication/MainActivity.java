package com.example.myapplication;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    private JSONObject APIdata;

    // initialise some variables
    private String URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/data.json";
    private String GET_URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/stats";
    private static final String EVENT_TYPE_NETWORK = "load";
    private static final String EVENT_TYPE_DISPLAY = "display";
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialise global request queue
        queue = Volley.newRequestQueue(this);
        spinner= findViewById(R.id.fruit_Name);
        //load the fruit names in the spinner
        loadSpinnerData(URL);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //get request when the page is loaded
        submitEvent(EVENT_TYPE_DISPLAY, System.currentTimeMillis());
    }

    /**
     * this method loads the API and displays it in the spinner
     * @param url
     */
    public void loadSpinnerData(String url) {
        // initialise the list of fruits that will go in the spinner
        final ArrayList<String> FruitName = new ArrayList<>();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    setAPIdata(jsonObject);
                    JSONArray jsonArray=jsonObject.getJSONArray("fruit");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String type=jsonObject1.getString("type");
                        FruitName.add(type);
                    }
                    spinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, FruitName));

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
            @Override
            protected Response<String> parseNetworkResponse( NetworkResponse response ) {
                long responseMS = response.networkTimeMs;
                submitEvent(EVENT_TYPE_NETWORK, responseMS);
                return super.parseNetworkResponse(response);
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    /**
     * this issues "FIRE AND FORGET" GET requests
     * @param event
     * @param data
     */
    private void submitEvent( String event, long data ) {
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

    /**
     * this method is called when the submit button is pressed
     * and its purpose is to pass the chosen fruit to the second
     * screen
     * @param view
     */
    public void displayInformation(View view){
        Intent intent = new Intent(this, DisplayFruitActivity.class);
        spinner= findViewById(R.id.fruit_Name);
        int fruitIndex = spinner.getSelectedItemPosition();
        try {
            JSONObject jsonObject = getAPIdata();
            JSONArray jsonArray = jsonObject.getJSONArray("fruit");
            jsonObject = jsonArray.getJSONObject(fruitIndex);
            intent.putExtra("jsonObject", jsonObject.toString());
            startActivity(intent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * forces the reload of the data
     * @param view
     */
    public void reloadAPI( View view ) {
        queue = Volley.newRequestQueue(this);
        loadSpinnerData(URL);
    }

    /**
     * gets the global API data
     * @return
     */
    public JSONObject getAPIdata() {
        return APIdata;
    }

    /**
     * sets the global API data
     * @param APIdata
     */
    public void setAPIdata(JSONObject APIdata) {
        this.APIdata = APIdata;
    }


}
