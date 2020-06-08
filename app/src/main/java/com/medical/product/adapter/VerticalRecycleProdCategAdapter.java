package com.medical.product.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.medical.product.Ui.Dashbord;
import com.medical.product.R;
import com.medical.product.models.HorizontalModelProducts;
import com.medical.product.models.VerticalModelCategory;

import java.util.ArrayList;

public class VerticalRecycleProdCategAdapter extends RecyclerView.Adapter<VerticalRecycleProdCategAdapter.VerticalRVViewHolder> {
    RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    ArrayList<VerticalModelCategory> arrayList;
    static ArrayList<HorizontalModelProducts> arrayLists;

    static HorizontalProductsCatehoryRecycleViewAdapter horizontalRecycleViewAdapter;

    int i=1;

    public VerticalRecycleProdCategAdapter(Context context, ArrayList<VerticalModelCategory> arrayList){
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public VerticalRVViewHolder onCreateViewHolder(ViewGroup parent, int i) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vertical,parent,false);

       return new VerticalRVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VerticalRVViewHolder holder, int position) {
        final VerticalModelCategory verticalModel=arrayList.get(position);
        String title=verticalModel.getTitle();

        holder.btnMore.setVisibility(View.GONE);

        if(title.equals("hotdeals"))
        {
            holder.txtTitle.setText("Hotdeals");
            //  recyclerlayoutManager=new GridLayoutManager(getActivity(),4);

             horizontalRecycleViewAdapter=new HorizontalProductsCatehoryRecycleViewAdapter(context,verticalModel.getArrayListProducts());

            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager( holder.recyclerView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            holder.recyclerView.setItemViewCacheSize(6);
            holder.recyclerView.setRecycledViewPool(viewPool);
            holder.recyclerView.setAdapter(horizontalRecycleViewAdapter);
            horizontalRecycleViewAdapter.notifyDataSetChanged();

        }

                if(title.equals("product"))
                {
                    holder.txtTitle.setText("Products");
                    //  recyclerlayoutManager=new GridLayoutManager(getActivity(),4);

                    HorizontalCategoryProductAdapter horizontalRecycleViewAdapter=new HorizontalCategoryProductAdapter(context,verticalModel.getArrayListProducts());

                    holder.recyclerView.setHasFixedSize(true);
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));

                    holder.recyclerView.setItemViewCacheSize(6);
                    holder.recyclerView.setRecycledViewPool(viewPool);
                    holder.recyclerView.setAdapter(horizontalRecycleViewAdapter);
                }

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(verticalModel.getTitle().equals("categories"))
                {
                    ((Dashbord)context).SendAllCategory();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VerticalRVViewHolder extends RecyclerView.ViewHolder{

        RecyclerView recyclerView;
        TextView txtTitle;
        TextView btnMore;

        public VerticalRVViewHolder(View itemView) {
            super(itemView);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.recycleview1);
            txtTitle=(TextView) itemView.findViewById(R.id.txtTitle);
            btnMore=(TextView) itemView.findViewById(R.id.btnmode);
        }
    }


    public static void notufy()
    {
         horizontalRecycleViewAdapter.notifyDataSetChanged();
    }






}
