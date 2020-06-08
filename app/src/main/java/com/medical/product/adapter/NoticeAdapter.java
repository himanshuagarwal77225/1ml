package com.medical.product.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.medical.product.R;
import com.medical.product.models.Notice;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private ArrayList<Notice> dataList;

    public NoticeAdapter(ArrayList<Notice> dataList) {
        this.dataList = dataList;
    }

    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_row_view, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeViewHolder holder, int position) {
      //  holder.txtNoticeTitle.setText(dataList.get(position).getTitle());
      //  holder.txtNoticeBrief.setText(dataList.get(position).getBrief());
      //  holder.txtNoticeFilePath.setText(dataList.get(position).getFileSource());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class NoticeViewHolder extends RecyclerView.ViewHolder {

        TextView txtNoticeTitle, txtNoticeBrief, txtNoticeFilePath;

        NoticeViewHolder(View itemView) {
            super(itemView);
           // txtNoticeTitle =  itemView.findViewById(R.id.txt_notice_title);
          //  txtNoticeBrief =  itemView.findViewById(R.id.txt_notice_brief);
         //   txtNoticeFilePath =  itemView.findViewById(R.id.txt_notice_file_path);
        }
    }
}