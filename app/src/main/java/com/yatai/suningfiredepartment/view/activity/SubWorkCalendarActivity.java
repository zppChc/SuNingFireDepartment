package com.yatai.suningfiredepartment.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.fragment.InfoFragment;
import com.yatai.suningfiredepartment.view.fragment.WorkCalendarFragment;
import com.yatai.suningfiredepartment.view.fragment.WorkTableFragment;

public class SubWorkCalendarActivity extends AppCompatActivity {
    String gridId;
    int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_work_calendar);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        gridId = getIntent().getStringExtra("gridId");
        categoryId = getIntent().getIntExtra("categoryId",0);
        FragmentManager fm = getSupportFragmentManager();
        Fragment mFragmentContainer = fm.findFragmentById(R.id.fragment_container);
        if (mFragmentContainer == null){
            mFragmentContainer = WorkCalendarFragment.newInstance(gridId,categoryId);
            fm.beginTransaction().add(R.id.fragment_container,mFragmentContainer).commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
