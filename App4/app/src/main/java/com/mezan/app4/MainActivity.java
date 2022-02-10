package com.mezan.app4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private ImageView courseIV;
    private ProgressBar loadingPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingPB = findViewById(R.id.idLoadingPB);
        courseIV = findViewById(R.id.idIVCourse);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = getString(R.string.url) + getString(R.string.access_key);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingPB.setVisibility(View.GONE);
                courseIV.setVisibility(View.VISIBLE);

                try {
                    JSONObject jsonObject = response.getJSONObject(0);
                    JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                    String imgUrl = imgUrlObject.getString("small");
                    Picasso.get().load(imgUrl).into(courseIV);
                    Log.d("IMAGEURL", imgUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d("Main Response", response.toString());
            }
//            @Override
//            public void onResponse(JSONObject response) {
//                // inside the on response method.
//                // we are hiding our progress bar.
//                loadingPB.setVisibility(View.GONE);
//
//                // in below line we are making our card
//                // view visible after we get all the data.
//                // courseCV.setVisibility(View.VISIBLE);
//                // now we get our response from API in json object format.
//                // in below line we are extracting a string with its key
//                // value from our json object.
//                // similarly we are extracting all the strings from our json object.
//                // String courseName = response.getString("courseName");
//                // String courseTracks = response.getString("courseTracks");
//                //  String courseMode = response.getString("courseMode");
//                try {
//                    String courseImageURL = response.getString("courseimg");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                // after extracting all the data we are
                // setting that data to all our views.
                // courseNameTV.setText(courseName);
                // courseTracksTV.setText(courseTracks);
                // courseBatchTV.setText(courseMode);

                // we are using picasso to load the image from url.
                //  Picasso.get().load(courseImageURL).into(courseIV);
//                System.out.println(response.toString());
//            }
        }, new Response.ErrorListener() {
            // this is the error listener method which
            // we will call if we get any error from API.
            @Override
            public void onErrorResponse(VolleyError error) {
                // below line is use to display a toast message along with our error.
                Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        queue.add(jsonArrayRequest);
    }
}