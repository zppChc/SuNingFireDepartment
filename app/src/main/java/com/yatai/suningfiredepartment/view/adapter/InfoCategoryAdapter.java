package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.model.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoCategoryAdapter extends RecyclerView.Adapter<InfoCategoryAdapter.ViewHolder> {
    private List<CategoryEntity> mCategoryEntityList = new ArrayList<>();
    private Context mContext;

    public InfoCategoryAdapter(Context context){
        this.mContext = context;
    }

    public void setCategoryEntityList(List<CategoryEntity> list){
        mCategoryEntityList.clear();
        mCategoryEntityList = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryTv.setText(mCategoryEntityList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mCategoryEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView categoryTv;
        public ViewHolder(View itemView) {
            super(itemView);
            categoryTv=(TextView)itemView.findViewById(R.id.item_category_title);
        }
    }

}
