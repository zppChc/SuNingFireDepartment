package com.yatai.suningfiredepartment.view.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.DepartmentEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chc
 * Date :2018/5/28
 * 相关部门 recycler view adapter
 */
public class HomeUnitAdapter extends RecyclerView.Adapter<HomeUnitAdapter.ViewHolder> {

    private List<DepartmentEntity> mDepartmentList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public HomeUnitAdapter(Context context){
        this.mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mDepartmentList.get(position).getImage()).into(holder.img);
        holder.nameTv.setText(mDepartmentList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mDepartmentList.size();
    }
    public void  setDepartmentList(List<DepartmentEntity> list){
        mDepartmentList = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener){
        mListener = listener;
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.item_show)
        ImageView img;
        @BindView(R.id.item_name)
        TextView nameTv;
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
