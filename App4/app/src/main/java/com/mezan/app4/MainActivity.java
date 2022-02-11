package com.mezan.app4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private ProgressBar loadingPB;
    private List<Model> modelList = new ArrayList<Model>();
    MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingPB = findViewById(R.id.idLoadingPB);
        gridView = findViewById(R.id.idGridView);

        adapter = new MyAdapter(this, modelList);
        gridView.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String url = getString(R.string.url) + getString(R.string.access_key);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                loadingPB.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);

                try {


                    //Picasso.get().load(imgUrl).into(courseIV);
                    Log.d("IMAGEURL", String.valueOf(response.length()));
                    for(int i=0; i<response.length();i++){
                        Model model = new Model();
                        JSONObject jsonObject = response.getJSONObject(i);
                        JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                        String imgUrl = imgUrlObject.getString("regular");

//                        jsonObject.getString("description");
//                        String imgDesc = jsonObject.getString("description");

                        String imgSize = jsonObject.getString("width")+" X "+jsonObject.getString("height");

                        model.setImageUrl(imgUrl);
//                        model.setImageDesc(imgDesc);
                        model.setImageSize(imgSize);
                        modelList.add(model);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // adapter notifier

                adapter.notifyDataSetChanged();
                Log.d("Main Response", response.toString());
            }
        }, new Response.ErrorListener() {

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