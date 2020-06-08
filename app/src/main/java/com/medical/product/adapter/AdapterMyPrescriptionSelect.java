package com.medical.product.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetPrescriptionModel;

import java.util.ArrayList;


public class AdapterMyPrescriptionSelect extends RecyclerView.Adapter<AdapterMyPrescriptionSelect.MyViewHolder> {
    private boolean zoomOut = false;
    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetPrescriptionModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterMyPrescriptionSelect(ArrayList<GatterGetPrescriptionModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.myprescription_grid_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // update your data here
        final GatterGetPrescriptionModel getAddvertise = recyclerModels.get(position);


        //  holder.advertTitle.setText(getAddvertise.getTitle());

        //  holder.title.setText(getAddvertise.getDocument_title());

        // holder.description.setText(getAddvertise.getDocument_description());

        if (!getAddvertise.getDocument_image().equals("")) {
            Glide.with(context).load(ApiFileuri.ROOT_URL_USER_IMAGE + getAddvertise.getDocument_image()).into(holder.imgDocument);
        }

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
        public ImageView imgDocument;
        CheckBox checkbox;

        MyViewHolder(View view) {
            super(view);
            imgDocument = (ImageView) itemView.findViewById(R.id.imgDocument);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkBox);

        }


    }

    /*public void filterList(ArrayList<GatterGetFamilyDocumentListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass","notifyDataSetChanged===============");
        notifyDataSetChanged();
    }*/


}
