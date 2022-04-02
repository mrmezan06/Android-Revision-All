package com.example.pexelswallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FullImageView extends AppCompatActivity {
    String portrait, original;
    ImageView singleFullImageView;
    Button btnSet;
    RadioButton btnHD, btnUHD;
    boolean checkHd = true;
    boolean checkUHd = false;
    AsyncTask mMyTask;
    LinearLayout root;

    ProgressDialog mProgressDialog;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_view);


        activity = FullImageView.this;
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Downloading");
        mProgressDialog.setMessage("Please wait...");

        root = findViewById(R.id.root);
        Intent it= getIntent();
        Bundle bundle = it.getExtras();
        if(bundle != null){
            portrait = bundle.getString("portraitUrl");
            original = bundle.getString("originalUrl");
        }

        singleFullImageView = findViewById(R.id.full_view);
        btnSet = findViewById(R.id.setBtn);
        btnHD = findViewById(R.id.radio_hd);
        btnUHD = findViewById(R.id.radio_uhd);

        Picasso.get()
                .load(portrait)
                .placeholder(R.drawable.loading)
                .into(singleFullImageView);

        btnHD.setChecked(checkHd);
        btnUHD.setChecked(checkUHd);

        btnHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkHd){
                    checkHd = false;
                    checkUHd = true;
                    btnHD.setChecked(checkHd);
                    btnUHD.setChecked(checkUHd);
                }else {
                    checkHd = true;
                    checkUHd = false;
                    btnHD.setChecked(checkHd);
                    btnUHD.setChecked(checkUHd);
                }
            }
        });
        btnUHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkHd){
                    checkHd = false;
                    checkUHd = true;
                    btnHD.setChecked(checkHd);
                    btnUHD.setChecked(checkUHd);
                }else {
                    checkHd = true;
                    checkUHd = false;
                    btnHD.setChecked(checkHd);
                    btnUHD.setChecked(checkUHd);
                }
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                String url = portrait;
                if (checkHd){
                    url = portrait;
                }else {
                    url = original;
                }
                try {
                    mMyTask = new DownloadTask().execute(new URL(url));
                    Snackbar.make(root, "Wallpaper Set Successful", Snackbar.LENGTH_LONG).show();
                } catch (MalformedURLException e) {
                    Snackbar.make(root, "Wallpaper Set Failed!", Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        });


    }

    private class DownloadTask extends AsyncTask<URL,Void,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];

                HttpURLConnection connection = null;
                try{
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    return BitmapFactory.decodeStream(bufferedInputStream);
                }catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            mProgressDialog.dismiss();

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}