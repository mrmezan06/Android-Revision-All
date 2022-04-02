package com.example.pexelswallpaper;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    List<String> imageList;
    List<String> portraitUrl;
    Context context;
    GridAdapter(List<String> imageList, List<String> portraitUrl, Context context){
        this.imageList = imageList;
        this.portraitUrl = portraitUrl;
        this.context = context;
    }
    @Override
    public int getCount() {
        return imageList.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.card_item_layout, viewGroup, false);
        ImageView imageView;
        imageView = view.findViewById(R.id.my_image);
        TextView textView;
        textView = view.findViewById(R.id.my_text);
        // imageView.setImageResource(R.drawable.image);
       // imageView.setImageURI(Uri.parse("https://images.pexels.com/photos/8478915/pexels-photo-8478915.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"));

        Picasso.get()
                .load(portraitUrl.get(i))
                .placeholder(R.drawable.loading)
                .into(imageView);
        textView.setText(imageList.get(i));
        return view;
    }
}
