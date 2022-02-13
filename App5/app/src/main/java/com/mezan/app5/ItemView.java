package com.mezan.app5;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    TextView txtUploader, txtLikes;
    LinearLayout itemRoot;

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

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Utility(fileName));

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String jsonID = jsonObject.getString("id");
                if(jsonID.equals(id)){
                    JSONObject imgUrlObject = jsonObject.getJSONObject("urls");
                    String regular = imgUrlObject.getString("regular");
                    String original = imgUrlObject.getString("raw");
                    String full = imgUrlObject.getString("full");
                    String thumb = imgUrlObject.getString("thumb");
                    JSONObject userObject = jsonObject.getJSONObject("user");
                    String uploaderName = userObject.getString("username");
                    String likes = jsonObject.getString("likes");
                    String colors = jsonObject.getString("color");
                    /*
                    * Matched ID Image Loading with thumb view
                    * */
                    Picasso
                            .get()
                            .load(thumb)
                            .placeholder(R.drawable.ic_baseline_person_outline_128)
                            .into(itemImage);
                    txtUploader.setText(uploaderName);
                    txtLikes.setText(likes);

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