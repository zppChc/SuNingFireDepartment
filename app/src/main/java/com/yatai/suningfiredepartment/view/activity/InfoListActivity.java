package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.fragment.InfoFragment;


public class InfoListActivity extends AppCompatActivity {
    String gridId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_list);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        gridId = getIntent().getStringExtra("gridId");
        FragmentManager fm = getSupportFragmentManager();
        Fragment mFragmentContainer = fm.findFragmentById(R.id.info_list_fragment_container);
        if (mFragmentContainer == null){
            mFragmentContainer = InfoFragment.newInstance(gridId);
            fm.beginTransaction().add(R.id.info_list_fragment_container,mFragmentContainer).commit();
        }
    }
}
