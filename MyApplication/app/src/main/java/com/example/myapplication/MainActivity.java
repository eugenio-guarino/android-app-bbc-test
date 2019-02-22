package com.example.myapplication;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

    // initialise some variables
    private Spinner spinner;
    private String URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/data.json";
    private String GET_URL="https://raw.githubusercontent.com/fmtvp/recruit-test-data/master/stats";
    private JSONObject APIdata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner= findViewById(R.id.fruit_Name);
        loadSpinnerData(URL);
        getRoundTrip(GET_URL);
    }

    /**
     * this method loads the API and displays it as a list
     * @param url
     */
    public void loadSpinnerData(String url) {
        // initialise list
        final ArrayList<String> FruitName = new ArrayList<>();

        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
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

    /**
     * this method prints out the HTTP round trip time in milliseconds
     * @param url
     */
    public void getRoundTrip( final String url){
        //starting time
        final long start_time = System.nanoTime();

        // instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // print out the time in milliseconds
                        long end_time = System.nanoTime();
                        double difference = (end_time - start_time) / 1e6;
                        String finalURL = url + "?event=load&data=";
                        System.out.println("REQUEST ROUND TRIP "+finalURL + difference );
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("That didn't work!");
            }
        });
        // add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * this method is called when the submit button is pressed
     * and its purpose is to pass the chosen fruit to the second
     * screen
     *
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
