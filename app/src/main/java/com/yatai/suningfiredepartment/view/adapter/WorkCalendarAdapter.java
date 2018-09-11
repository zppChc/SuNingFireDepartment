package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.WorkCalendar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkCalendarAdapter extends RecyclerView.Adapter<WorkCalendarAdapter.ViewHolder>{

    private List<WorkCalendar> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public WorkCalendarAdapter(Context context){
        mContext = context;
        mList = new ArrayList<>();
    }
    public void setList(List<WorkCalendar> list){
        mList = list;
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_work_calendar,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mList.get(position).getDay() == 0){
            holder.day.setVisibility(View.GONE);
            holder.desc.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        }else {
            holder.day.setVisibility(View.VISIBLE);
            holder.day.setText(String.valueOf(mList.get(position).getDay()));
            if (mList.get(position).isHasTask()){
                if (mList.get(position).getTask().getStatus() == 1){
                    Glide.with(mContext).load(mList.get(position).getTask().getImage()).into(holder.image);
                    holder.desc.setVisibility(View.GONE);
                }else{
                    holder.image.setVisibility(View.GONE);
                    holder.desc.setText(mList.get(position).getTask().getName());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_image)
        ImageView image;
        @BindView(R.id.item_desc)
        TextView desc;
        @BindView(R.id.item_week_day)
        TextView day;

        OnItemClickListener mListener;
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
