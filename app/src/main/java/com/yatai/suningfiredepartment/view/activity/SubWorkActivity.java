package com.yatai.suningfiredepartment.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yatai.suningfiredepartment.R;

/**
 * @author chc
 * 2018/6/4
 * 展示子网格工作页面，布局类似于 WorkFragment
 */
public class SubWorkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_sub_work);
    }
}
