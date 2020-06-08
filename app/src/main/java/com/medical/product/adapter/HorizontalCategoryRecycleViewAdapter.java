package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.Ui.ProductCategory;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.HorizontalModelCategory;

import java.util.ArrayList;

public class HorizontalCategoryRecycleViewAdapter extends RecyclerView.Adapter<HorizontalCategoryRecycleViewAdapter.HorizontalRVViewHolder> {

    Context context;
    ArrayList<HorizontalModelCategory> arrayList;


    public HorizontalCategoryRecycleViewAdapter(Context context, ArrayList<HorizontalModelCategory> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_category, parent, false);

        return new HorizontalRVViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final HorizontalModelCategory HorizontalModelCategory = arrayList.get(position);

        holder.textViewTitle.setText(HorizontalModelCategory.getName());
        Glide.with(context).load(ApiFileuri.ROOT_URL_CATEGORY_IMAGE + HorizontalModelCategory.getImage()).into(holder.ivThumb);
      /*  holder.txtAddtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ReuseMethod.makeFlyAnimation(holder.ivThumbcopy,cartRelativeLayout,Product_detailActivity.this);
                if(context instanceof Dashbord){
                    ((Dashbord)context).makeFlyAnimation(holder.ivThumbcopy);
                }
            }
        });*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(context, ProductCategory.class);
                    i.putExtra("searching", "category");
                    i.putExtra("id", HorizontalModelCategory.getId());
                    i.putExtra("name", HorizontalModelCategory.getName());
                    context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, txtAddtocart, txtPrice;
        ImageView ivThumb, ivThumbcopy;
        RelativeLayout relAddToCart;
        LinearLayout cardcategory;


        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            textViewTitle = (TextView) itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
            cardcategory = (LinearLayout) itemView.findViewById(R.id.cardcategory);
          /*  ivThumbcopy=(ImageView) itemView.findViewById(R.id.ivThumbcopy);
            txtAddtocart=(TextView)itemView.findViewById(R.id.txtAddtocart);
            txtPrice=(TextView)itemView.findViewById(R.id.txtPrice);*/


        }


    }

}
