package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;
import com.medical.product.Ui.Artical_view;
import com.medical.product.models.BlogList;

import java.util.ArrayList;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private List<BlogList> blogLists = new ArrayList<>();
    private Context mContext;

    public BlogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_blog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (blogLists.get(position).getImage() == null || blogLists.get(position).getImage().isEmpty()) {
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(R.mipmap.ic_launcher)
                    .into(holder.blog_image);
        } else {
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load("http://1ml.co.in/blog/wp-content/uploads/" + blogLists.get(position).getImage())
                    .into(holder.blog_image);
        }
        holder.blog_title.setText(blogLists.get(position).getPostTitle());

        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
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
        if (blogLists.size() > 5) {
            return 5;
        } else {
            return blogLists.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView blog_image;
        TextView blog_title;
        CardView parent_layout;

        ViewHolder(View itemview) {
            super(itemview);
            blog_image = itemview.findViewById(R.id.blog_image);
            blog_title = itemview.findViewById(R.id.blog_title);
            parent_layout = itemview.findViewById(R.id.blog_parent_layout);
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