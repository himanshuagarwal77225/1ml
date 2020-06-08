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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.medical.product.R;
import com.medical.product.Ui.Accreditation;
import com.medical.product.Ui.ProductActivity;
import com.medical.product.helpingFile.ReuseMethod;
import com.medical.product.models.Child;
import com.medical.product.models.PROFILE;

import java.util.ArrayList;
import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private static final String TAG = "SearchAdapter";
    private List<PROFILE> profileList;
    private List<PROFILE> profileListFull;
    ProgressDialog progressDialog;
    private ArrayList<Child> childArrayList = new ArrayList<Child>();
    private Context mContext;
    public SearchAdapter(List<PROFILE> allList,Context mContext) {
        this.profileList = allList;
        this.mContext=mContext;
        profileListFull = new ArrayList<>(profileList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PROFILE currentItem = profileList.get(position);
        String price="0";
        String fasting;
        if(currentItem.getFasting().equals("CF")){
            fasting = "Yes";
            holder.fasting.setText("Minimum 12 hours fasting is manadatory");
        }else{
            fasting = "No";
            holder.fasting.setText("Fasting is not manadatory");
        }
        holder.name.setText(currentItem.getName());
        if(!currentItem.getRate().getB2c().isEmpty()) {
           price=currentItem.getRate().getB2c();
        }
        holder.price.setText("₹ "+price);
        final int finalPosition = position;
        final String finalPrice = price;
        final String finalFasting = fasting;
        holder.strikeprice.setText(ReuseMethod.discount_offer_strike(Double.parseDouble(price),Double.parseDouble("20")));
        holder.strikeprice.setPaintFlags(holder.strikeprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        String image;
        if(!currentItem.getImageMaster().isEmpty()){
            image = currentItem.getImageMaster().get(1).getImgLocations();
        }else{
            image ="No";
        }
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("Please wait, Adding to cart");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                ReuseMethod.addToLabCartDatabase(mContext,currentItem.getName(), finalPrice,currentItem.getCode(),
                        "20", finalFasting,progressDialog, Long.parseLong(currentItem.getTestCount()),image);
            }
        });
        holder.accreditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Accreditation.class);
                mContext.startActivity(intent);
            }
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childArrayList = (ArrayList<Child>) profileList.get(finalPosition).getChilds();
                Log.d(TAG, "onClick: clicked on: " + profileList.get(finalPosition).getName());
                Intent intent = new Intent(mContext.getApplicationContext(), ProductActivity.class);
                Bundle bundle= new Bundle();
                if(!profileList.get(finalPosition).getImageMaster().isEmpty()){
                    bundle.putString("image",profileList.get(finalPosition).getImageMaster().get(1).getImgLocations());
                }
                bundle.putString("name",profileList.get(finalPosition).getName());
                bundle.putString("fasting",profileList.get(finalPosition).getFasting());
                bundle.putString("price",profileList.get(finalPosition).getRate().getB2c());
                bundle.putString("test_count",profileList.get(finalPosition).getTestCount());
                bundle.putParcelableArrayList("child",childArrayList);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
       private TextView name;
        private TextView fasting;
        private TextView price;
        private TextView strikeprice;
        private TextView accreditation;
        private TextView discount;
        private ImageView cart;
        private CardView relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            fasting = itemView.findViewById(R.id.fasting);
            price = itemView.findViewById(R.id.price);
            strikeprice=itemView.findViewById(R.id.strike);
            discount=itemView.findViewById(R.id.discount);
            cart=itemView.findViewById(R.id.cart);
            relativeLayout=itemView.findViewById(R.id.parentRelative);
            accreditation=itemView.findViewById(R.id.accreditation);
        }
    }
    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PROFILE> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(profileListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PROFILE item : profileListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            profileList.clear();
            profileList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
