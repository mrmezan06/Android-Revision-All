package com.mezan.app5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class ItemView extends AppCompatActivity {

    ImageView itemImage;
    Button btnRegular, btnFull, btnThumb, btnOriginal;
    TextView txtUploader, txtLikes, txtUpdloadSize;
    LinearLayout itemRoot;
    ProgressBar progressBar;

    private static final String[] STORAGE_PERMISSIONS_Write = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final String[] STORAGE_PERMISSIONS_Read = { Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);

        String id = getIntent().getStringExtra("id");
        String fileName = getIntent().getStringExtra("file");

        itemImage = findViewById(R.id.itemImage);
        btnRegular = findViewById(R.id.btnRegular);
        btnFull = findViewById(R.id.btnFull);
        btnThumb = findViewById(R.id.btnThumb);
        btnOriginal = findViewById(R.id.btnOriginal);
        txtUploader = findViewById(R.id.txtUploader);
        txtLikes = findViewById(R.id.txtLikes);
        itemRoot = findViewById(R.id.item_root);
        progressBar = findViewById(R.id.idSettingWall);
        txtUpdloadSize = findViewById(R.id.txtUploadSize);

        if(isNetworkConnected()){
            ConnectionCheck();
            update_view(id, fileName);
            btnFull.setEnabled(true);
            btnRegular.setEnabled(true);
            btnOriginal.setEnabled(true);
            btnThumb.setEnabled(true);
        }else {
            btnFull.setEnabled(false);
            btnRegular.setEnabled(false);
            btnOriginal.setEnabled(false);
            btnThumb.setEnabled(false);
        }

        btnRegular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    progressBar.setVisibility(View.VISIBLE);
                    itemImage.setVisibility(View.INVISIBLE);
                    setWallpaper(url_generate(id, fileName, "regular"));

            }
        });

        btnOriginal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                itemImage.setVisibility(View.GONE);
                setWallpaper(url_generate(id, fileName, "original"));
            }
        });
        btnFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                itemImage.setVisibility(View.GONE);
               setWallpaper(url_generate(id, fileName, "full"));
            }
        });
        btnThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                itemImage.setVisibility(View.GONE);
                setWallpaper(url_generate(id, fileName, "thumb"));
            }
        });

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
        new AlertDialog.Builder(ItemView.this)
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

    void setWallpaper(String url){

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.e("TAG", "OnBitmapLoaded");
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(ItemView.this);
                try {
                    wallpaperManager.setBitmap(bitmap);
                    progressBar.setVisibility(View.GONE);
                    itemImage.setVisibility(View.VISIBLE);
                    Snackbar.make(itemRoot, "Wallpaper Set Successful!", Snackbar.LENGTH_LONG).show();
                } catch (IOException e) {
                    Snackbar.make(itemRoot, "Wallpaper Set Failed!", Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.e("TAG", "IOException->" + e.getMessage());
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("TAG", "" + e.getMessage());
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("TAG", "OnPrepareLoad");
            }
        };

        Picasso.get().load(url).into(target);

    }

    //save image


    String url_generate(String id, String fileName, String typeName){
        String url=null;
        try {
            JSONArray jsonArray = new JSONArray(Utility(fileName));
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String jsonID = jsonObject.getString("id");
                if(jsonID.equals(id)) {
                    JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                    String regular = imgUrlObject.getString("regular");
                    String original = imgUrlObject.getString("raw");
                    String full = imgUrlObject.getString("full");
                    String thumb = imgUrlObject.getString("thumb");
                    switch (typeName) {
                        case "regular":
                            return regular;
                        case "original":
                            return original;
                        case "full":
                            return full;
                        default:
                            return thumb;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return url;
    }

    void update_view(String id, String fileName){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Utility(fileName));

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String jsonID = jsonObject.getString("id");
                if(jsonID.equals(id)){
                    JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                    String regular = imgUrlObject.getString("regular");
//                    String original = imgUrlObject.getString("raw");
//                    String full = imgUrlObject.getString("full");
//                    String thumb = imgUrlObject.getString("thumb");
                    String imgSize = jsonObject.getString("width")+" X "+jsonObject.getString("height");
                    JSONObject userObject = jsonObject.getJSONObject("user");
                    String uploaderName = userObject.getString("username");
                    String likes = jsonObject.getString("likes");
                    String colors = jsonObject.getString("color");
                    /*
                     * Matched ID Image Loading with thumb view
                     * */
                    Picasso
                            .get()
                            .load(regular)
                            .placeholder(R.drawable.ic_baseline_person_outline_128)
                            .fit()
                            .into(itemImage);
                    txtUploader.setText(uploaderName);
                    txtLikes.setText(likes);
                    txtUpdloadSize.setText(imgSize);

                    Log.d("StringColor",colors);

                    if(colors.contains("f")){

                        colors = colors.replace("f","d");
                        Log.d("StringReplaced", colors);

                        itemRoot.setBackgroundColor(Color.parseColor(colors));
                    }else {
                        itemRoot.setBackgroundColor(Color.parseColor(colors));
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar.make(itemRoot, "Data Retrieving Failed!", Snackbar.LENGTH_LONG).show();
        }
    }
    String Utility(String filename){
        String jsonStr;
        Random random = new Random();
        int x = random.nextInt(300) + 1;
        // 0 to 299 + 1 equal to 1 to 300
        try {
            InputStream inputStream = ItemView.this.getAssets().open(filename);
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
