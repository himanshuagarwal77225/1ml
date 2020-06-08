package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;
import com.medical.product.Ui.ProductActivity;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.Child;
import com.medical.product.models.PROFILE;
import com.medical.product.usersession.UserSession;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapterMostBooked extends RecyclerView.Adapter<RecyclerViewAdapterMostBooked.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapterMost";
//    private ArrayList<String> mImageUrl = new ArrayList<>();
//    private ArrayList<String> mName = new ArrayList<>();
//    private ArrayList<String> mDisc = new ArrayList<>();
//    private ArrayList<String> mTests = new ArrayList<>();
//    private ArrayList<String> mPrice = new ArrayList<>();
//    private ArrayList<String> mStrikePrice = new ArrayList<>();
    private List<PROFILE> profile = new ArrayList<>();
    private ArrayList<Child> childArrayList = new ArrayList<Child>();
    private Context mContext;

    public RecyclerViewAdapterMostBooked(Context mContext){
        this.mContext=mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_carditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        if(!profile.get(position).getImageMaster().isEmpty()){
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(profile.get(position).getImageMaster().get(0).getImgLocations())
                    .into(holder.image);
        }else{
            Glide.with(mContext)
                    .asBitmap()
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .load(R.mipmap.ic_launcher)
                    .into(holder.image);
        }
        holder.name.setText((CharSequence) profile.get(position).getName());
        holder.discount.setText("20% \n OFF");
        holder.tests.setText(profile.get(position).getTestCount()+" Tests");
        holder.price.setText("₹ "+profile.get(position).getRate().getB2c());
        holder.strike_price.setText(ReuseMethod.discount_offer_strike(Double.parseDouble(profile.get(position).getRate().getB2c()),Double.parseDouble("20")));
        holder.strike_price.setPaintFlags(holder.strike_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + profile.get(position).getName());
                childArrayList = (ArrayList<Child>) profile.get(position).getChilds();
                Log.d(TAG, "onClick: clicked on: " + profile.get(position).getName());
                Intent intent = new Intent(mContext.getApplicationContext(), ProductActivity.class);
                Bundle bundle= new Bundle();
                if(!profile.get(position).getImageMaster().isEmpty()){
                    bundle.putString("image",profile.get(position).getImageMaster().get(1).getImgLocations());
                }
                bundle.putString("name",profile.get(position).getName());
                bundle.putString("fasting",profile.get(position).getFasting());
                bundle.putString("price",profile.get(position).getRate().getB2c());
                bundle.putString("test_count",profile.get(position).getTestCount());
                bundle.putParcelableArrayList("child",childArrayList);
                bundle.putString("productId",profile.get(position).getCode());
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

            if(profile.size()>5){
                return 5;
            }else{
                return profile.size();
            }
        }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView discount,name,tests,price,strike_price;
        CardView parent_layout;
        ViewHolder(View itemview){
            super(itemview);
             image = itemview.findViewById(R.id.img_most_booked);
             discount = itemview.findViewById(R.id.disc_most_booked);
            name = itemview.findViewById(R.id.txt_most_booked);
            tests = itemview.findViewById(R.id.no_test_most_booked);
            price = itemview.findViewById(R.id.price_most_booked);
            strike_price = itemview.findViewById(R.id.strike_price_most_booked);
            parent_layout=itemview.findViewById(R.id.parent_layout);
        }

    }
    public void setOffer(List<PROFILE> mProfile){
        Log.d(TAG, "setOffer: called");
        if(mProfile == null){
            return;
        }
        profile.clear();
        profile.addAll(mProfile);
        notifyDataSetChanged();
    }
}
