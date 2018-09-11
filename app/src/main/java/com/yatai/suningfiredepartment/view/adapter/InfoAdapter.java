package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.InfoEntity;

import java.util.ArrayList;
import java.util.List;


public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<InfoEntity> mInfoEntityList;
    private Context mContext;
    private OnItemClickListener mClickListener;

    public InfoAdapter(Context context){
        this.mContext = context;
        mInfoEntityList = new ArrayList<>();
    }
    public void setClickListener(OnItemClickListener listener){
        mClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_info,parent,false);
        return new ViewHolder(view,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.infoTitle.setText(mInfoEntityList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mInfoEntityList.size();
    }

    public void setInfoEntityList(List<InfoEntity> list){
        mInfoEntityList.clear();
        mInfoEntityList = list;
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView infoTitle;
         OnItemClickListener mListener;// 声明自定义的接口
        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            infoTitle=(TextView)itemView.findViewById(R.id.info_detail_title);
            mListener=listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(view,getPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
