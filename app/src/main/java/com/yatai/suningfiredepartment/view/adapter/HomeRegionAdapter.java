package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.GridEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chc
 * Date :2018/5/28
 * 相关网格 recycler view adapter
 */
public class HomeRegionAdapter extends RecyclerView.Adapter<HomeRegionAdapter.ViewHolder>{
    private List<GridEntity> gridList  = new ArrayList<>();
    private Context mContext;

    public HomeRegionAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int resource = R.drawable.a;
        Glide.with(mContext).load(resource).into(holder.img);
//        Glide.with(holder.itemView).load(gridList.get(position).getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return gridList.size();
    }

    public void setGridList(List<GridEntity> list){
        gridList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_show)
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
