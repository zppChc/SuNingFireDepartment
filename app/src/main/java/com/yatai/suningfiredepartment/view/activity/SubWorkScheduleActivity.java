package com.yatai.suningfiredepartment.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.fragment.WorkTableFragment;

public class SubWorkScheduleActivity extends AppCompatActivity {
    String gridId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_work_schedule);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        gridId = getIntent().getStringExtra("gridId");
        FragmentManager fm = getSupportFragmentManager();
        Fragment mFragmentContainer = fm.findFragmentById(R.id.fragment_container);
        if (mFragmentContainer == null){
            mFragmentContainer = WorkTableFragment.newInstance(gridId);
            fm.beginTransaction().add(R.id.fragment_container,mFragmentContainer).commit();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
