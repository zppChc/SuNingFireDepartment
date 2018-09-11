package com.yatai.suningfiredepartment.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kelin.scrollablepanel.library.PanelAdapter;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.RecordEntity;
import com.yatai.suningfiredepartment.view.activity.DepartmentWorkActivity;
import com.yatai.suningfiredepartment.view.activity.WorkDetailFinishActivity;

import java.util.ArrayList;
import java.util.List;

public class WorkTableAdapter extends PanelAdapter {
    private static final int TITLE_TYPE = 4;
    private static final int CATEGORY_TYPE = 0;
    private static final int DATE_TYPE = 1;
    private static final int ORDER_TYPE = 2;

    private List<String> dateList=new ArrayList<>();
    private List<String> categoryList = new ArrayList<>();
    private List<List<RecordEntity>> recordList =new ArrayList<>();
    private Context mContext;
    public WorkTableAdapter(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public int getRowCount() {
        return dateList.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return categoryList.size()+1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
        int viewType = getItemViewType(row, column);
        switch (viewType) {
            case DATE_TYPE:
                setDateView(row, (DateViewHolder) holder);
                break;
            case CATEGORY_TYPE:
                setCategoryView(column, (CategoryViewHolder) holder);
                break;
            case ORDER_TYPE:
                setImageView(row, column, (ContentViewHolder) holder);
                break;
            case TITLE_TYPE:
                break;
            default:
                setImageView(row, column, (ContentViewHolder) holder);
        }
    }

    public int getItemViewType(int row, int column) {
        if (column == 0 && row == 0) {
            return TITLE_TYPE;
        }
        if (column == 0) {
            return DATE_TYPE;
        }
        if (row == 0) {
            return CATEGORY_TYPE;
        }
        return ORDER_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TITLE_TYPE:
                return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_title, parent, false));
            case DATE_TYPE:
                return new DateViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_left_title, parent, false));
            case CATEGORY_TYPE:
                return new CategoryViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_right_title, parent, false));
            case ORDER_TYPE:
                return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_content, parent, false));
            default:
                break;
        }
        return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false));
    }


    private void setCategoryView(int pos, CategoryViewHolder viewHolder) {
        String category = categoryList.get(pos-1);
        if (category != null && pos > 0) {
            viewHolder.titleTextView.setText(category);
        }
    }

    private void setDateView(int pos, DateViewHolder viewHolder) {
        String date = dateList.get(pos-1);
        if (date != null && pos > 0) {
            viewHolder.titleTextView.setText(date);
        }
    }

    private void setImageView(final int row, final int column, ContentViewHolder viewHolder) {
        final RecordEntity recordInfo = recordList.get(row - 1).get(column - 1);
        if (recordInfo != null) {
            if (recordInfo.getRecordId()!=0) {
                Glide.with(mContext).load(recordInfo.getImage()).into(viewHolder.image);
                viewHolder.itemView.setClickable(true);
                viewHolder.image.setVisibility(View.VISIBLE);
            } else{
                viewHolder.itemView.setClickable(false);
                viewHolder.image.setVisibility(View.GONE);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recordInfo.getRecordId()!=0){
                        Intent intent = new Intent(mContext, WorkDetailFinishActivity.class);
                        intent.putExtra("workItem", recordInfo.getRecordId());
                        //从workItem 中获取ID,可以用来查询单个数据
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }



    private static class ContentViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public View view;

        public ContentViewHolder(View view) {
            super(view);
            this.view = view;
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }

    private class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public TitleViewHolder(View view) {
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.title);
        }
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView) itemView.findViewById(R.id.title);
        }
    }

    private class DateViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTextView;
        public DateViewHolder(View itemView) {
            super(itemView);
            this.titleTextView = (TextView)itemView.findViewById(R.id.title);
        }
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public void setRecordList(List<List<RecordEntity>> recordList) {
        this.recordList = recordList;
    }
}
