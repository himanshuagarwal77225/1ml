package com.medical.product.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.medical.product.R;
import com.medical.product.models.LSlotDataRe;
import com.medical.product.models.Slot;

import java.util.ArrayList;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotHolder> {
    private Context mContext;
    int sposition=-1;
    private OnItemValue listener;
    private List<LSlotDataRe> slots = new ArrayList<>();
    public SlotAdapter(Context mContext) {
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public SlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_slot_item,parent,false);
        return new SlotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotHolder holder, int position) {
        if(position==sposition){
            holder.time.setBackgroundResource(R.color.colorPrimary);
            holder.time.setTextColor(Color.WHITE);
        }else{
            holder.time.setBackgroundResource(R.color.white);
            holder.time.setTextColor(Color.BLACK);
        }
    holder.time.setText(slots.get(position).getSlot());
    holder.time.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setValue(slots.get(position).getSlot());
            sposition=position;
            holder.time.setBackgroundResource(R.color.colorPrimary);
            holder.time.setTextColor(Color.WHITE);
            notifyDataSetChanged();
        }
    });
    }

    @Override
    public int getItemCount() {
       return slots.size();
    }

    class SlotHolder extends RecyclerView.ViewHolder{
     TextView time;

        public SlotHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.tv_Appointmentslot);
        }
    }
    public void setOffer(List<LSlotDataRe> mSlot){
        if(mSlot == null){
            return;
        }
        slots.clear();
        slots.addAll(mSlot);
        notifyDataSetChanged();
    }
    public interface OnItemValue {
        void onItemValue(String time);
    }

    public void setValues(OnItemValue listener ){
        this.listener=listener;

    }
    private void setValue(String time){
        listener.onItemValue(time);

    }
}
