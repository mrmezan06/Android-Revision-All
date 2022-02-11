package com.mezan.app5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ProgressBar progressBar;
    private List<Model> modelList = new ArrayList<Model>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.idLoadingPB);
        gridView = findViewById(R.id.idGridView);

        adapter = new MyAdapter(this, modelList);
        gridView.setAdapter(adapter);
        try {
            JSONArray jsonArray = new JSONArray(Utility(this));
            for (int i = 0; i < jsonArray.length(); i++){
                Model model = new Model();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                String imgUrl = imgUrlObject.getString("regular");
                String imgSize = "Size : "+jsonObject.getString("width")+" X "+jsonObject.getString("height");
                model.setImageUrl(imgUrl);
                model.setImageSize(imgSize);
                modelList.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "JSON Read Failed!",Toast.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);

        adapter.notifyDataSetChanged();
    }
    String Utility(Context context){
        String jsonStr;
        try {
            InputStream inputStream = context.getAssets().open("Unsplash.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonStr = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonStr;
    }

}
