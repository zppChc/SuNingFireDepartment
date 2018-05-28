package com.yatai.suningfiredepartment.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.adapter.FocusGroupAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FocusGroupActivity extends AppCompatActivity {

    @BindView(R.id.focus_group_back)
    ImageView mBackIv;
    @BindView(R.id.focus_group_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.focus_group_refresh)
    SmartRefreshLayout mRefresh;

    FocusGroupAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus_group);
        ButterKnife.bind(this);

        initView();

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView(){
        mAdapter = new FocusGroupAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
    }

}
