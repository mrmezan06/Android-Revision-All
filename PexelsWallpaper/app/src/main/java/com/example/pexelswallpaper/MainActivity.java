package com.example.pexelswallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    GridAdapter adapter;
    final String key = "563492ad6f917000010000018dc55c90216c48058d27881b10950d9f";
    List<String> imageName;
    List<String> urlPortrait;
    List<String> urlOriginal;
    String nextUrl = null;

    FloatingActionButton nxtBtn;
    ImageView searchBtn;
    EditText searchEdtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.my_grid);
        nxtBtn = findViewById(R.id.nxtBtn);

        searchBtn = findViewById(R.id.search_btn);
        searchEdtText = findViewById(R.id.search);



        imageName = new ArrayList<>();
        urlPortrait = new ArrayList<>();
        urlOriginal = new ArrayList<>();
        adapter = new GridAdapter(imageName, urlPortrait, this);
        gridView.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String search = searchEdtText.getText().toString();
                    if(search == null){
                        search = "trending";
                    }
                    String data = new MyTask("https://api.pexels.com/v1/search?query="+search+"&per_page=30&page=1").execute().get();
                    CovertJSON(data);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });


        /*
        * Uri.parse(
            "https://api.pexels.com/v1/curated?per_page=$noOfImageToLoad&page=1"),
                headers: {"Authorization": apiKey});
        * */
        try {
            String data = new MyTask("https://api.pexels.com/v1/curated?per_page=30&page=1").execute().get();
            CovertJSON(data);

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, FullImageView.class);
                intent.putExtra("portraitUrl", urlPortrait.get(i));
                intent.putExtra("originalUrl", urlOriginal.get(i));
                startActivity(intent);
            }
        });


        nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nextUrl != null){
                    try {
                        String data = new MyTask(nextUrl).execute().get();
                        CovertJSON(data);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public void CovertJSON(String json){


        imageName.clear();
        urlPortrait.clear();
        urlOriginal.clear();


        try {
            JSONObject mainPageObject = new JSONObject(json);
            JSONArray mainPhotosList = mainPageObject.getJSONArray("photos");
            for(int i=0; i<mainPhotosList.length(); i++){
                JSONObject photosObject = mainPhotosList.getJSONObject(i);
                String resolution = photosObject.getString("width") + "X" + photosObject.getString("height");
                String photographer = photosObject.getString("photographer");
                JSONObject srcList = photosObject.getJSONObject("src");
                String portrait = srcList.getString("portrait");
                String original = srcList.getString("original");

                imageName.add(photographer);
                urlPortrait.add(portrait);

                urlOriginal.add(original);

                nextUrl = mainPageObject.getString("next_page");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();

    }

    private class MyTask extends AsyncTask<String, String, String>{
        String urlStr;

        public MyTask(String urlStr) {
            this.urlStr = urlStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            StringBuilder result=new StringBuilder();
            URL url = null;
            try {
               // url = new URL("https://api.pexels.com/v1/curated?per_page=30&page=1");
                url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization",key);
                InputStream in=new BufferedInputStream(connection.getInputStream());
                BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                String line;
                while((line=reader.readLine())!=null){

                    result.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            return result.toString();
        }
    }
}