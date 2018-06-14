package com.yatai.suningfiredepartment.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yatai.suningfiredepartment.util.UmengUtil;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtil.onResumeToActivity(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtil.onPauseToActivity(this);
    }
}
