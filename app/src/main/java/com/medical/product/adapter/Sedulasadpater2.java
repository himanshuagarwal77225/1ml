package com.medical.product.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.Ui.ReminderAddActivity;
import com.medical.product.models.Sedulas;

import java.util.ArrayList;


public class Sedulasadpater2 extends RecyclerView.Adapter<Sedulasadpater2.MyViewHolder> {
    private ArrayList<Sedulas> duration; // this data structure carries our title and description
    Context context;
    ReminderAddActivity addActivity;

    public Sedulasadpater2(ArrayList<Sedulas> duration, Context context) {
        this.duration = duration;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate your custom row layout here
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_sedulas, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        // update your data here
        final Sedulas duationvalue = duration.get(position);
        holder.time.setText(String.format("%02d:%02d", duationvalue.getHour(), duationvalue.getMin()) + " " + duationvalue.getZone());
        holder.dose.setText(duationvalue.getDose());
        holder.routing.setText(duationvalue.getDay());

    }


    @Override
    public int getItemCount() {
        return duration.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView time, dose, routing;

        MyViewHolder(View view) {
            super(view);
            time = (TextView) itemView.findViewById(R.id.time);
            dose = (TextView) itemView.findViewById(R.id.dose);
            routing = (TextView) itemView.findViewById(R.id.routing);


        }

    }

}
