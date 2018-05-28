package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkRecyclerViewAdapter  extends RecyclerView.Adapter<WorkRecyclerViewAdapter.ViewHolder>{
    private List<String> mList = new ArrayList<>();
    private Context mContext;
    public WorkRecyclerViewAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_work,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String item = mList.get(position);
        String temp = holder.titleTv.getText().toString();
        holder.titleTv.setText(item+temp);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setDataList(List<String> list){
        mList = list;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_flag_view)
        View flagView;
        @BindView(R.id.item_title)
        TextView titleTv;
        @BindView(R.id.item_content)
        TextView contentTv;
        @BindView(R.id.item_flag_text)
        TextView flagTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

