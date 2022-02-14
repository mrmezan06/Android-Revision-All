package com.mezan.app5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ProgressBar progressBar;
    private List<Model> modelList = new ArrayList<Model>();
    MyAdapter adapter;

    Button forwardBtn;
    LinearLayout btnRoot;
    String jsonFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.idLoadingPB);
        gridView = findViewById(R.id.idGridView);

        forwardBtn = findViewById(R.id.forwardBtn);
        btnRoot = findViewById(R.id.btnRoot);


        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Forward Array
                Updating_View();
            }
        });

        adapter = new MyAdapter(this, modelList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // ID
                //Log.d("Onclick", modelList.get(i).id + modelList.get(i).fileName);
                Intent intent = new Intent(MainActivity.this, ItemView.class);
                intent.putExtra("id", modelList.get(i).id);
                intent.putExtra("file", modelList.get(i).fileName);
                startActivity(intent);
            }
        });
        if (isNetworkConnected()){
            ConnectionCheck();
            Updating_View();
            forwardBtn.setEnabled(true);
        }else {
           // forwardBtn.setEnabled(false);
        }

        // Initial Load


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(!(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected())){
            showNetworkDialog();
            return false;
        }
        return true;
    }
    private void ConnectionCheck(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest networkRequest = new NetworkRequest.Builder().build();
            connectivityManager.registerNetworkCallback(networkRequest, new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    Log.i("Tag", "active connection");
                }

                @Override
                public void onLost(Network network) {
                    super.onLost(network);
                    Log.i("Tag", "losing active connection");
                    isNetworkConnected();
                }
            });
        }
    }

    private void showNetworkDialog(){
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Connection lost?")
                .setMessage("Please check your internet connection!")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isNetworkConnected();
                    }
                })
                .show();
    }


    void Updating_View(){
        try {
            if(!modelList.isEmpty()){
                modelList.clear();
            }
            JSONArray jsonArray = new JSONArray(Utility());
            for (int i = 0; i < jsonArray.length(); i++){
                Model model = new Model();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                String imgUrl = imgUrlObject.getString("thumb");
                String id = jsonObject.getString("id");
                String imgSize = "Size : "+jsonObject.getString("width")+" X "+jsonObject.getString("height");
                model.setImageUrl(imgUrl);
                model.setImageSize(imgSize);
                model.setId(id);
                model.setFileName(jsonFileName);
                modelList.add(model);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "JSON Read Failed!",Toast.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        btnRoot.setVisibility(View.VISIBLE);


        adapter.notifyDataSetChanged();
    }
    String Utility(){
        String jsonStr;
        Random random = new Random();
        int x = random.nextInt(300) + 1;
        // 0 to 299 + 1 equal to 1 to 300
        jsonFileName = "unsplash_json (" + x + ").json";
        try {
            InputStream inputStream = MainActivity.this.getAssets().open(jsonFileName);
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
