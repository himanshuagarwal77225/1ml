package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;
import com.medical.product.Ui.Artical_view;
import com.medical.product.models.BlogList;

import java.util.ArrayList;
import java.util.List;


public class Blog_adpater extends RecyclerView.Adapter<Blog_adpater.MyViewHolder> {

    private List<BlogList> blogLists = new ArrayList<>();
    private Context mContext;

    public Blog_adpater(Context context) {
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_single, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // update your data here
        if (blogLists.get(position).getImage() == null || blogLists.get(position).getImage().isEmpty()) {
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(R.mipmap.ic_launcher)
                    .into(holder.image);
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load("http://1ml.co.in/blog/wp-content/uploads/" + blogLists.get(position).getImage())
                    .into(holder.image);
        }
        holder.blog_title.setText(blogLists.get(position).getPostTitle());

        holder.blg_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blogIntent =  new Intent(mContext, Artical_view.class);
                blogIntent.putExtra("id", blogLists.get(position).getID());
                blogIntent.putExtra("postTitle", blogLists.get(position).getPostTitle());
                blogIntent.putExtra("postContent", blogLists.get(position).getPostContent());
                blogIntent.putExtra("image", blogLists.get(position).getImage());
                mContext.startActivity(blogIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        /*if(blogLists.size()>5){
            return 5;
        }else{
            return blogLists.size();
        }*/
        return blogLists.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView blog_title;
        ImageView image;
        CardView blg_layout;

        MyViewHolder(View view) {
            super(view);
            blog_title = view.findViewById(R.id.blog_title1);
            image = view.findViewById(R.id.blog_image1);
            blg_layout = view.findViewById(R.id.blog_parent1);

        }

    }

    public void setOffer(List<BlogList> mBloglist) {
        if (mBloglist == null) {
            return;
        }
        blogLists.clear();
        blogLists.addAll(mBloglist);
        notifyDataSetChanged();
    }

}
