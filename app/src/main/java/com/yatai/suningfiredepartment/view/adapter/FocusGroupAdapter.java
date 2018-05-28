package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yatai.suningfiredepartment.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author  chc
 * 2018/5/28
 * 重点人群 adapter
 */
public class FocusGroupAdapter extends RecyclerView.Adapter<FocusGroupAdapter.ViewHolder>{
    private Context mContext;

    public FocusGroupAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_focus_group,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
