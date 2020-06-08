package com.medical.product.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;
import com.medical.product.Ui.ProductActivity;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.Child;
import com.medical.product.models.OFFER;
import com.medical.product.models.PROFILE;

import java.util.ArrayList;
import java.util.List;


public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.ViewHolder>{
    private static final String TAG = "ViewAllAdapter";
    private List<PROFILE> profile = new ArrayList<>();
    private Context mContext;
    ProgressDialog progressDialog;
    public String value = "offer";
    private ArrayList<Child> childArrayList = new ArrayList<Child>();
    public ViewAllAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_viewcarditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        String image;
            if (!profile.get(position).getImageMaster().isEmpty()) {
                image = profile.get(position).getImageMaster().get(1).getImgLocations();
                Glide.with(mContext)
                        .asBitmap()
                        .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                        .load(profile.get(position).getImageMaster().get(0).getImgLocations())
                        .into(holder.image);
            } else {
                image = "No";
                Glide.with(mContext)
                        .asBitmap()
                        .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                        .load(R.mipmap.ic_launcher)
                        .into(holder.image);
            }
            holder.name.setText((CharSequence) profile.get(position).getName());
            holder.discount.setText("20% \n OFF");
            holder.tests.setText(profile.get(position).getTestCount() + " Tests");
            holder.price.setText(profile.get(position).getRate().getB2c());
            holder.strike_price.setText(ReuseMethod.discount_offer_strike(Double.parseDouble(profile.get(position).getRate().getB2c()), Double.parseDouble("20")));
            holder.strike_price.setPaintFlags(holder.strike_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            final int finalPosition = position;
            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: clicked on: " + profile.get(finalPosition).getName());
                    childArrayList = (ArrayList<Child>) profile.get(finalPosition).getChilds();
                    Log.d(TAG, "onClick: clicked on: " + profile.get(finalPosition).getName());
                    Intent intent = new Intent(mContext.getApplicationContext(), ProductActivity.class);
                    Bundle bundle = new Bundle();
                    if (!profile.get(finalPosition).getImageMaster().isEmpty()) {
                        bundle.putString("image", profile.get(finalPosition).getImageMaster().get(1).getImgLocations());
                    }
                    bundle.putString("name", profile.get(finalPosition).getName());
                    bundle.putString("fasting", profile.get(finalPosition).getFasting());
                    bundle.putString("price", profile.get(finalPosition).getRate().getB2c());
                    bundle.putString("test_count", profile.get(finalPosition).getTestCount());
                    bundle.putParcelableArrayList("child", childArrayList);
                    bundle.putString("productId", profile.get(finalPosition).getCode());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            String fasting = "No";
            if (profile.get(position).getFasting().equals("CF")) {
                fasting = "Yes";
            }
            String finalFasting = fasting;
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage("Please wait, Adding to cart");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    ReuseMethod.addToLabCartDatabase(mContext, profile.get(finalPosition).getName(),
                            profile.get(position).getRate().getB2c(),
                            profile.get(position).getCode(), "20",
                            finalFasting, progressDialog, Long.parseLong(profile.get(position).getTestCount()),image);
                }
            });
    }

    @Override
    public int getItemCount() {

            return profile.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView discount,name,tests,price,strike_price;
        CardView parent_layout;
        Button add;
        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            image = itemview.findViewById(R.id.img_most_booked1);
            discount = itemview.findViewById(R.id.disc_most_booked1);
            name = itemview.findViewById(R.id.txt_most_booked1);
            tests = itemview.findViewById(R.id.no_test_most_booked1);
            price = itemview.findViewById(R.id.price_most_booked1);
            strike_price = itemview.findViewById(R.id.strike_price_most_booked1);
            parent_layout=itemview.findViewById(R.id.parent_layout1);
            add=itemview.findViewById(R.id.add);
        }
    }
    public void setOffer(List<PROFILE> mProfile){
        value = "profile";
        Log.d(TAG, "setOffer: called");

        if(mProfile == null){
            return;
        }

        profile.clear();
        profile.addAll(mProfile);
        notifyDataSetChanged();
    }

}
