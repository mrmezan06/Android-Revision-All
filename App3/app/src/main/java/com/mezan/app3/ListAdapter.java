package com.mezan.app3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> numList = new ArrayList<>();

    public  MyAdapter(){}
    public MyAdapter(Context context, ArrayList<String> numList){
        this.context = context;
        this.numList = numList;
    }
    @Override
    public int getCount() {
        return numList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup root) {
        //Log.d("mylist", numList.toString());
        view = LayoutInflater.from(context).inflate(R.layout.list_layout,root,false);
        TextView num2textV, numV;
        numV = view.findViewById(R.id.numV);
        num2textV = view.findViewById(R.id.num2textV);
        numV.setText(String.valueOf(i));
        num2textV.setText(numList.get(i));

        return view;
    }
}
