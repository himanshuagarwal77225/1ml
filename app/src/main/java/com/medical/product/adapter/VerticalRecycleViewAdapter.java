package com.medical.product.adapter;

import android.content.Context;
import androidx.recyclerview.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.Ui.Dashbord;
import com.medical.product.helpingFile.GlobalVariable;
import com.medical.product.models.VerticalModel;

import java.util.ArrayList;

public class VerticalRecycleViewAdapter extends RecyclerView.Adapter<VerticalRecycleViewAdapter.VerticalRVViewHolder> {
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    ArrayList<VerticalModel> arrayList;

    public VerticalRecycleViewAdapter(Context context, ArrayList<VerticalModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VerticalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical, parent, false);

        return new VerticalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalRVViewHolder holder, int position) {
        final VerticalModel verticalModel = arrayList.get(position);
        String title = verticalModel.getTitle();

        if (title.equals("brands")) {
            holder.txtTitle.setText("Brands");
            holder.recyclerView.setBackgroundColor(context.getResources().getColor(R.color.white));
            HorizontalRecycleViewAdapterBrands horizontalRecycleViewAdapter = new HorizontalRecycleViewAdapterBrands(context, verticalModel.getArrayList());
            GlobalVariable.brandarray = verticalModel.getArrayList();
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setRecycledViewPool(viewPool);
            holder.recyclerView.setItemViewCacheSize(6);
            holder.recyclerView.setAdapter(horizontalRecycleViewAdapter);

        }

        if (title.equals("categories")) {
            holder.txtTitle.setText("Category");
            holder.recyclerView.setBackgroundColor(context.getResources().getColor(R.color.white));
            HorizontalCategoryRecycleViewAdapter horizontalRecycleViewAdapter = new HorizontalCategoryRecycleViewAdapter(context, verticalModel.getArrayListCategory());
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setRecycledViewPool(viewPool);
            holder.recyclerView.setItemViewCacheSize(6);
            holder.recyclerView.setAdapter(horizontalRecycleViewAdapter);

        }

        if (title.equals("products")) {

            holder.txtTitle.setText("Populor Product");

            HorizontalProductsRecycleViewAdapter horizontalRecycleViewAdapter = new HorizontalProductsRecycleViewAdapter(context, verticalModel.getArrayListProducts());
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setRecycledViewPool(viewPool);
            holder.recyclerView.setItemViewCacheSize(6);
            holder.recyclerView.setAdapter(horizontalRecycleViewAdapter);
        }
        if(title.equals("sponsor"))
        {
            holder.txtTitle.setText("Sponsor");
            HorizontalAdadpater horizontalRecycleViewAdapter = new HorizontalAdadpater(context, verticalModel.getArrayListad());
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setRecycledViewPool(viewPool);
            holder.recyclerView.setItemViewCacheSize(6);
            holder.recyclerView.setAdapter(horizontalRecycleViewAdapter);
        }

//            if (verticalModel.getArrayListProducts().size() == 0) {
//                holder.lables.setVisibility(View.GONE);
//            } else {
//                holder.lables.setVisibility(View.VISIBLE);
//            }

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verticalModel.getTitle().equals("categories")) {
                    ((Dashbord) context).SendAllCategory();
                } else if (verticalModel.getTitle().equalsIgnoreCase("brands")) {
                    ((Dashbord) context).SendAllbrands();
                }else if(verticalModel.getTitle().equalsIgnoreCase("sponsor")) {
                    ((Dashbord) context).SendAllad();
                }
                else {
                    ((Dashbord) context).SendAllproduct();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VerticalRVViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        RelativeLayout lables;
        TextView txtTitle;
        TextView btnMore;

        public VerticalRVViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.recycleview1);
            lables = (RelativeLayout) itemView.findViewById(R.id.lables);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            btnMore = (TextView) itemView.findViewById(R.id.btnmode);
        }
    }
}
