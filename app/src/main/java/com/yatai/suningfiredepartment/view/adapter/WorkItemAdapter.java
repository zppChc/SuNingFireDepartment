package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.WorkItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkItemAdapter extends RecyclerView.Adapter<WorkItemAdapter.ViewHolder> {

    private List<WorkItemEntity> mList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mListener;

    public WorkItemAdapter(Context context){
        mContext = context;
    }

    public void setList(List<WorkItemEntity> list){
        mList = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_work,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTv.setText(mList.get(position).getName());
        if (mList.get(position).getStatus() == 1){
            holder.flagTv.setText("已完成");
        }
        if(mList.get(position).getStatus() == 0){
            holder.flagTv.setText("未完成");
            holder.flagTv.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.item_flag_view)
        View flagView;
        @BindView(R.id.item_title)
        TextView titleTv;
        @BindView(R.id.item_content)
        TextView contentTv;
        @BindView(R.id.item_flag_text)
        TextView flagTv;

        private OnItemClickListener mListener;

        public ViewHolder(View itemView,OnItemClickListener listener) {
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
