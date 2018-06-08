package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.PeopleEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author  chc
 * 2018/5/28
 * 重点人群 adapter
 */
public class FocusGroupAdapter extends RecyclerView.Adapter<FocusGroupAdapter.ViewHolder>{
    private Context mContext;
    private List<PeopleEntity> mList;

    public FocusGroupAdapter(Context context){
        this.mContext = context;
        mList = new ArrayList<>();
    }

    public void  setList(List<PeopleEntity> list){
        mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_focus_group,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).getImage()).into(holder.portrait);
        holder.name.setText(mList.get(position).getName());
        holder.phone.setText(mList.get(position).getMobile());
        holder.address.setText(mList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_focus_group_portrait)
        CircleImageView portrait;
        @BindView(R.id.item_focus_group_name)
        TextView name;
        @BindView(R.id.item_focus_group_phone)
        TextView phone;
        @BindView(R.id.item_focus_group_address)
        TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
