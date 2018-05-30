package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.model.entity.Info;

import java.util.ArrayList;
import java.util.List;


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<Info> mInfoList;
    private Context mContext;

    public InfoAdapter(Context context){
        this.mContext = context;
        mInfoList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_info,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.infoTitle.setText(mInfoList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    public void setInfoList(List<Info> list){
        mInfoList.clear();
        mInfoList= list;
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        TextView infoTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            infoTitle=(TextView)itemView.findViewById(R.id.info_detail_title);
        }
    }
}
