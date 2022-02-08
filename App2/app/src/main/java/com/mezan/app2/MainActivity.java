package com.mezan.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    LinearLayout root;
    TextView show_msg;
    EditText box_msg;
    Button btn_show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        root = findViewById(R.id.root);
        show_msg = findViewById(R.id.txt_show);
        box_msg = findViewById(R.id.box_msg);
        btn_show = findViewById(R.id.btn_show);

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String msg = box_msg.getText().toString();
               if(msg.isEmpty()){
                   Snackbar.make(root, "Message is empty!", Snackbar.LENGTH_LONG).show();
               }else {
                   show_msg.setText(msg);
               }
                hideKeyboard(view);
            }
        });


    }
    void hideKeyboard(View v){
        InputMethodManager iMM = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        iMM.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

    }
}