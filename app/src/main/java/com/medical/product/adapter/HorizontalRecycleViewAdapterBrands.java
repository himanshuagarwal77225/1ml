package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.medical.product.Ui.ProductCategory;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.HorizontalModelBrands;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HorizontalRecycleViewAdapterBrands extends RecyclerView.Adapter<HorizontalRecycleViewAdapterBrands.HorizontalRVViewHolder> {

    Context context;
    ArrayList<HorizontalModelBrands> arrayList;
    public HorizontalRecycleViewAdapterBrands(Context context, ArrayList<HorizontalModelBrands> arrayList)
    {
        this.context=context;
        this.arrayList=arrayList;
    }

    @Override
    public HorizontalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_brands,parent,false);

        return new HorizontalRVViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final HorizontalRVViewHolder holder, final int position) {

        final HorizontalModelBrands horizontalModel=arrayList.get(position);

        holder.textViewTitle.setText(horizontalModel.getName());
        Glide.with(context).load(ApiFileuri.ROOT_URL_BRANDS_IMAGE+horizontalModel.getImage()).into(holder.ivThumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i=new Intent(context, ProductCategory.class);
                    i.putExtra("searching","brand");
                    i.putExtra("id",horizontalModel.getId());
                    i.putExtra("name",horizontalModel.getName());
                    context.startActivity(i);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });



     /*   holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Product_detailActivity.class);
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context,
                        new Pair<View, String>(view.findViewById(R.id.ivThumb),
                                Product_detailActivity.VIEW_NAME_HEADER_IMAGE),
                        new Pair<View, String>(view.findViewById(R.id.txtTitlehorizontal),
                                Product_detailActivity.VIEW_NAME_HEADER_TITLE));
             *//*   new Pair<View, String>(view.findViewById(R.id.txtAddtocart),
                        Product_detailActivity.VIEW_NAME_CART);
                new Pair<View, String>(view.findViewById(R.id.txtPrice),
                        Product_detailActivity.VIEW_NAME_PRICE);*//*

                // Now we can start the Activity, providing the activity options as a bundle
                ActivityCompat.startActivity(context, intent, activityOptions.toBundle());
            }
        });*/


    }

    @Override
    public int getItemCount() {
        Log.i("as","yyyyyyyyyy=======arrayListsize"+arrayList.size());
        return arrayList.size();
    }

    public class HorizontalRVViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle,txtAddtocart,txtPrice;
        CircleImageView ivThumb;
        RelativeLayout relAddToCart;

        public HorizontalRVViewHolder(View itemView) {
            super(itemView);
            textViewTitle=(TextView)itemView.findViewById(R.id.txtTitlehorizontal);
            ivThumb=(CircleImageView) itemView.findViewById(R.id.ivThumb);
           /* txtAddtocart=(TextView)itemView.findViewById(R.id.txtAddtocart);
            txtPrice=(TextView)itemView.findViewById(R.id.txtPrice);*/


        }


    }

}
