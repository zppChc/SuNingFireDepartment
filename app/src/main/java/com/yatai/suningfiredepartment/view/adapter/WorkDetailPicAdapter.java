package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yatai.suningfiredepartment.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkDetailPicAdapter extends RecyclerView.Adapter<WorkDetailPicAdapter.ViewHolder> {

    private Context mContext;
    private List<Drawable> mDrawables;
    private OnItemClickListener mListener;

    public WorkDetailPicAdapter(Context context){
        mContext = context;
        mDrawables = new ArrayList<>();
    }
    public void setDrawables(List<Drawable> drawables){
        mDrawables=drawables;
        notifyDataSetChanged();
    }
    public void setListener(OnItemClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img.setImageDrawable(mDrawables.get(position));
    }

    @Override
    public int getItemCount() {
        return mDrawables.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_show)
        ImageView img;
        OnItemClickListener mListener;
        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            ButterKnife.bind(this,itemView);
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
