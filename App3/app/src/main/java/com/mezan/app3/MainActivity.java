package com.mezan.app3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;

import android.widget.LinearLayout;

import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout ll;
    ListView lv;
    ArrayList<String> itemList = new ArrayList<>();
    //MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NumbersToWords numbersToWords = new NumbersToWords();

        ll = findViewById(R.id.root);
        lv = findViewById(R.id.ls);


        for(int i=0; i<=1000; i++){
            itemList.add(numbersToWords.solution(i));
        }
        Log.d("Main List:", itemList.toString());
        MyAdapter adapter = new MyAdapter(this, itemList);
        //ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((adapterView, view, i, l) -> Snackbar.make(adapterView,itemList.get(i),Snackbar.LENGTH_LONG).show());

    }
}

class NumbersToWords {

    private static final String ZERO = "zero";
    private static final String[] oneToNine = {
            "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    };

    private static final String[] tenToNinteen = {
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
    };

    private static final String[] dozens = {
            "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };

    public String solution(int number) {
        if(number == 0)
            return ZERO;

        return generate(number).trim();
    }

    public String generate(int number) {
        if(number >= 1000000000) {
            return generate(number / 1000000000) + " billion " + generate(number % 1000000000);
        }
        else if(number >= 1000000) {
            return generate(number / 1000000) + " million " + generate(number % 1000000);
        }
        else if(number >= 1000) {
            return generate(number / 1000) + " thousand " + generate(number % 1000);
        }
        else if(number >= 100) {
            return generate(number / 100) + " hundred " + generate(number % 100);
        }

        return generate1To99(number);
    }

    private String generate1To99(int number) {
        if (number == 0)
            return "";

        if (number <= 9)
            return oneToNine[number - 1];
        else if (number <= 19)
            return tenToNinteen[number % 10];
        else {
            return dozens[number / 10 - 1] + " " + generate1To99(number % 10);
        }
    }
}

