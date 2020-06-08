package com.medical.product.helpingFile;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.Ui.ProductCategory;
import com.medical.product.models.Banner;

import java.util.ArrayList;


public class MyAdapter extends PagerAdapter {
    private ArrayList<Banner> images;
    private LayoutInflater inflater;
    private Context context;

    public MyAdapter(Context context, ArrayList<Banner> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup view, final int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        Glide.with(context).load(images.get(position).getBanner()).into(myImage);
        if (!TextUtils.isEmpty(images.get(position).getCategory_id())|| !TextUtils.isEmpty(images.get(position).getProduct_id())) {
            myImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(context, ProductCategory.class);
                        i.putExtra("searching", "slider");
                        i.putExtra("id", images.get(position).getId());
                        i.putExtra("name", "Offer Products");
                        context.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
