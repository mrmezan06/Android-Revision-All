package com.mezan.app4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {
HTTP_Handler handler;
String JsonString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetUnsplashData data = new GetUnsplashData();
        data.execute();
//        handler = new HTTP_Handler();
//        String url = getString(R.string.url);
//        String key = getString(R.string.access_key);
//        Log.d("JSONFILE:", url+key);
//
//        try {
//            JsonString = handler.makeServiceCall(url + key);
//            Log.d("JSONFILE:", JsonString);
//        } catch (IOException e) {
//            Log.d("JSONFILE:", "Failed!");
//        }
        //Log.d("DATA",data.toString());


    }
    private class GetUnsplashData extends AsyncTask {

//        public GetUnsplashData(Context context) {
//            super(context);
//        }
//        @Override
//        protected Object onLoadInBackground() {
//            // Loading time
//            return super.onLoadInBackground();
//        }
//        @Override
//        public Object loadInBackground() {
//            HTTP_Handler handler = new HTTP_Handler();
//            String data = "";
//            String url = getString(R.string.url);
//            String key = getString(R.string.access_key);
//            Log.d("DATA",url+key);
//            try {
//                data = handler.makeServiceCall(url+key);
//                //System.out.println(data);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("TAG", "Response from url:Error");
//            }
//            Log.e("TAG", "Response from url: " + data);
//            return "null";
//        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HTTP_Handler handler = new HTTP_Handler();
            String data = "";
            String url = getString(R.string.url);
            String key = getString(R.string.access_key);
            Log.d("DATA",url+key);
            try {
                data = handler.makeServiceCall(url+key);
                //System.out.println(data);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("TAG", "Response from url:Error");
            }
            Log.e("TAG", "Response from url: " + data);
            return null;
        }
    }
}
