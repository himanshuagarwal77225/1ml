package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.medical.product.R;
import com.medical.product.Ui.Full_image;
import com.medical.product.Ui.UploadMyDocument;
import com.medical.product.helpingFile.ApiFileuri;
import com.medical.product.models.GatterGetFamilyDocumentListModel;

import java.util.ArrayList;


public class AdapterMyDocument extends RecyclerView.Adapter<AdapterMyDocument.MyViewHolder> {
    private boolean zoomOut = false;
    SharedPreferences prefrance, prefranceid;
    String sharid;
    private ArrayList<GatterGetFamilyDocumentListModel> recyclerModels; // this data structure carries our title and description
    Context context;

    public AdapterMyDocument(ArrayList<GatterGetFamilyDocumentListModel> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.family_document_item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // update your data here
        final GatterGetFamilyDocumentListModel getAddvertise = recyclerModels.get(position);


        //  holder.advertTitle.setText(getAddvertise.getTitle());

        holder.title.setText(getAddvertise.getDocument_title());
        holder.drname.setText(getAddvertise.getDr_name());

        holder.description.setText(getAddvertise.getDocument_description());


        if (!getAddvertise.getDocument_image().equals("")) {
            Glide.with(context).load(ApiFileuri.ROOT_URL_USER_IMAGE + getAddvertise.getDocument_image()).into(holder.imgDocument);
        }
        holder.imgDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          context.startActivity(new Intent(context, Full_image.class).putExtra("img", ApiFileuri.ROOT_URL_USER_IMAGE + getAddvertise.getDocument_image()));
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
        public TextView title, description,drname;
        public ImageView imgDocument;

        MyViewHolder(View view) {
            super(view);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            drname = (TextView) itemView.findViewById(R.id.drname);
            imgDocument = (ImageView) itemView.findViewById(R.id.imgDocument);

        }

    }

    public void filterList(ArrayList<GatterGetFamilyDocumentListModel> filterdNames) {
        this.recyclerModels = filterdNames;
        Log.i("ass", "notifyDataSetChanged===============");
        notifyDataSetChanged();
    }


}
