package com.mezan.app5;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    List<Model> modelList;
    Context context;
    public MyAdapter(){}
    public MyAdapter(Context context, List<Model> modelList){
        this.context = context;
        this.modelList = modelList;
    }
    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.image_layout,viewGroup,false);
        ImageView imageView;
        TextView  sizeView;
        imageView = view.findViewById(R.id.idImageView);

        sizeView = view.findViewById(R.id.idImageSize);
        Picasso
                .get()
                .load(modelList.get(i).imageUrl)
                .placeholder(R.drawable.ic_baseline_person_outline_128)
                .resize(160, 150)
                .centerCrop()
                .into(imageView);
        sizeView.setText(modelList.get(i).imageSize);
        Log.d("IMAGE LIST",String.valueOf(modelList.size()));
        return view;
    }
}
