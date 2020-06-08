package com.medical.product.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.product.Ui.Product_detailActivity;
import com.medical.product.R;
import com.medical.product.Ui.SearchProductActivity;
import com.medical.product.Ui.SearchProductListActivity;
import com.medical.product.models.GatterGetSearchProductCatBrand;

import java.util.ArrayList;


public class AdapterSearchBrandCateList extends RecyclerView.Adapter<AdapterSearchBrandCateList.MyViewHolder> {

    SharedPreferences prefrance,prefranceid;
    String sharid;
    private ArrayList<GatterGetSearchProductCatBrand> recyclerModels; // this data structure carries our title and description
    Context context;
    public AdapterSearchBrandCateList(ArrayList<GatterGetSearchProductCatBrand> recyclerModels, Context context) {
        this.recyclerModels = recyclerModels;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_search, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // update your data here
        final GatterGetSearchProductCatBrand gatterGetSearchProductCatBrand = recyclerModels.get(position);


        holder.txttitle.setText(gatterGetSearchProductCatBrand.getName());

        holder.txtType.setText(gatterGetSearchProductCatBrand.getType());

        holder.txttitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywordis="";
                if (context instanceof SearchProductActivity) {
                    keywordis= ((SearchProductActivity) context).sendSEarchkey();
                }
               if(!gatterGetSearchProductCatBrand.getType().equals("product"))
               {

                   String id="";
                   String stpe="";

                    id=gatterGetSearchProductCatBrand.getId();

                   stpe= gatterGetSearchProductCatBrand.getType();

                   Log.i("asdf","type=======idsdasd==keyword=="+keywordis);

                   if(!(id.equals("") ||stpe.equals("")))
                   {
                       Intent intent = new Intent(context, SearchProductListActivity.class);
                       intent.putExtra("ckeck","check");
                       intent.putExtra("id",id+"");
                       intent.putExtra("keyword_search",keywordis+"");
                       intent.putExtra("type",stpe+"");
                       context.startActivity(intent);
                   }



               }
               else
               {
                   Intent intent = new Intent(context, Product_detailActivity.class);
                   intent.putExtra("medname",gatterGetSearchProductCatBrand.getName());
                   intent.putExtra("med_id",gatterGetSearchProductCatBrand.getId());
                  context.startActivity(intent);
               }

                Toast.makeText(context,gatterGetSearchProductCatBrand.getName(),Toast.LENGTH_SHORT).show();
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
          public TextView txttitle,txtType;


        MyViewHolder(View view) {
            super(view);
            txttitle =(TextView) itemView.findViewById(R.id.txttitle);
            txtType=(TextView) itemView.findViewById(R.id.txtType);
        }

    }

    public void filterList(ArrayList<GatterGetSearchProductCatBrand> filterdNames) {
        this.recyclerModels = filterdNames;
        notifyDataSetChanged();
    }



}
