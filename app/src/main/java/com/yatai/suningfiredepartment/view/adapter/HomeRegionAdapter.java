package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
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

/**
 * @author chc
 * Date :2018/5/28
 * 相关网格 recycler view adapter
 */
public class HomeRegionAdapter extends RecyclerView.Adapter<HomeRegionAdapter.ViewHolder>{
    private List<Drawable> mImgs  = new ArrayList<>();
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
        holder.img.setImageDrawable(mImgs.get(position));
    }

    @Override
    public int getItemCount() {
        return mImgs.size();
    }

    public void setImgs(List<Drawable> imgs){
        mImgs = imgs;
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
