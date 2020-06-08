package com.medical.product.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.models.GatterGetVendersModel;

import java.util.ArrayList;


public class AdapterVendersSelect extends RecyclerView.Adapter<AdapterVendersSelect.MyViewHolder> {
    private boolean zoomOut = false;
    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetVendersModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterVendersSelect(ArrayList<GatterGetVendersModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.myprescription_vender_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // update your data here
        final GatterGetVendersModel getAddvertise = recyclerModels.get(position);

        holder.txtpharm_name.setText(getAddvertise.getPharm_name());
        //  holder.advertTitle.setText(getAddvertise.getTitle());
        if (getAddvertise.getStore_status().equalsIgnoreCase("0")) {
            holder.stai.setText("(Offline)");
            holder.stai.setTextColor(context.getResources().getColor(R.color.red));

        } else {
            holder.stai.setText("(Online)");
            holder.stai.setTextColor(context.getResources().getColor(R.color.green));
        }

        if (!TextUtils.isEmpty(getAddvertise.getAverage_rating())) {
            holder.txtaverage_rating.setText(getAddvertise.getAverage_rating());
        } else {
            holder.txtaverage_rating.setText("0");

        }
        holder.offer.setVisibility(View.GONE);
        holder.txtstore_address.setText(getAddvertise.getStore_address());
        holder.txtname.setText(getAddvertise.getName());
        float rate = 0.0f;
        try {
            rate = Float.parseFloat(getAddvertise.getAverage_rating());
        } catch (Exception e) {
            // or some value to mark this field is wrong. or make a function validates field first ...
        }
        holder.ratingbar.setRating(rate);


      /*  holder.description.setText(getAddvertise.getDocument_description());

        if(!getAddvertise.getDocument_image().equals(""))
        {
            Glide.with(context).load(ApiFileuri.ROOT_URL_USER_IMAGE+getAddvertise.getDocument_image()).into(holder.imgDocument);
        }*/

        holder.checkbox.setTag(position);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getAddvertise.isSelected()) {
                    getAddvertise.setSelected(false);

                } else {
                    getAddvertise.setSelected(true);

                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkbox.performClick();
            }
        });

    }


    @Override
    public int getItemCount() {
        return recyclerModels.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
        // return super.getItemViewType(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtpharm_name, txtaverage_rating, txtstore_address, txtname, stai,offer;
        CheckBox checkbox;
        RatingBar ratingbar;

        MyViewHolder(View view) {
            super(view);
            txtpharm_name = (TextView) itemView.findViewById(R.id.txtpharm_name);
            stai = (TextView) itemView.findViewById(R.id.stai);
            offer = (TextView) itemView.findViewById(R.id.offer);
            txtaverage_rating = (TextView) itemView.findViewById(R.id.txtaverage_rating);

            txtstore_address = (TextView) itemView.findViewById(R.id.txtstore_address);
            txtname = (TextView) itemView.findViewById(R.id.txtname);

            checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);

            ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }


    }

    /*public void filterList(ArrayList<GatterGetFamilyDocumentListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }*/


}
