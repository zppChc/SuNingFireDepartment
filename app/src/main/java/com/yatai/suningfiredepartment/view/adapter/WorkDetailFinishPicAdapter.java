package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yatai.suningfiredepartment.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkDetailFinishPicAdapter extends RecyclerView.Adapter<WorkDetailFinishPicAdapter.ViewHolder> {
    List<String> imgs=new ArrayList<>();
    Context mContext;
    OnItemClickListener mListener;
    public WorkDetailFinishPicAdapter(Context context){
        this.mContext = context;
    }
    public void setImgs(List<String> list){
        this.imgs = list;
        notifyDataSetChanged();
    }
    public void setListener(OnItemClickListener listener){
        mListener =listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_work_img,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(imgs.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return imgs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_work_pic_show)
        ImageView img;
        OnItemClickListener mListener;
        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mListener = listener;
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
