package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class InfoCategoryAdapter extends RecyclerView.Adapter<InfoCategoryAdapter.ViewHolder> {
    private List<CategoryEntity> mCategoryEntityList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mClickListener;
    private int defItem = -1;

    public InfoCategoryAdapter(Context context){
        this.mContext = context;
    }

    public void setCategoryEntityList(List<CategoryEntity> list){
        mCategoryEntityList = list;
        notifyDataSetChanged();
    }
    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mClickListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view,mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryTv.setText(mCategoryEntityList.get(position).getName());
        if (defItem != -1) {
            if (defItem == position) {
                holder.categoryTv.setTextColor(mContext.getResources().getColor(R.color.category_text_blue));

            } else {
                holder.categoryTv.setTextColor(mContext.getResources().getColor(R.color.category_text_black));
            }
        }
        if (defItem == -1 && position == 0){
            holder.categoryTv.setTextColor(mContext.getResources().getColor(R.color.category_text_blue));
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView categoryTv;
        private OnItemClickListener mListener;// 声明自定义的接口

        public ViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            categoryTv=(TextView)itemView.findViewById(R.id.item_category_title);
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
