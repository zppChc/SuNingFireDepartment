package com.yatai.suningfiredepartment.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.WorkItemEntity;
import com.yatai.suningfiredepartment.entity.WorkTemplateEntity;
import com.yatai.suningfiredepartment.view.adapter.WorkDetailPicAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkDetailActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO = 1;

    @BindView(R.id.work_detail_title_text_view)
    TextView mTitleTv;
    @BindView(R.id.work_detail_description_text_view)
    TextView mDescriptionTv;
    @BindView(R.id.work_detail_template_layout)
    RelativeLayout mTemplateLayout;
    @BindView(R.id.work_detail_confirm_button)
    Button mConfirmBtn;
    @BindView(R.id.work_Detail_photo_display_recycler_view)
    RecyclerView mPicRecyclerView;


    private WorkItemEntity mWorkItemEntity;
    private List<WorkTemplateEntity> mTemplateList;
    private List<Drawable> mPicList;
    private WorkDetailPicAdapter mPicAdapter;

    //图片对象
    private Uri contentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_work_detail);
        ButterKnife.bind(this);
        try {
            initView();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() throws JSONException {
        mTemplateList = new ArrayList<>();
        mPicList = new ArrayList<>();

        Intent intent = getIntent();
        String workItem = intent.getStringExtra("workItem");

        Gson gson = new Gson();
        mWorkItemEntity = gson.fromJson(workItem, WorkItemEntity.class);

        mTitleTv.setText(mWorkItemEntity.getName());
        Logger.d("Description: " + mWorkItemEntity.getDescription());
        mDescriptionTv.setText(mWorkItemEntity.getDescription());

        JSONObject jb = new JSONObject(workItem);
        JSONArray templateArray = jb.getJSONArray("template");
        for (int i = 0; i < templateArray.length(); i++) {
            WorkTemplateEntity templateEntity = gson.fromJson(templateArray.getJSONObject(i).toString(), WorkTemplateEntity.class);
            mTemplateList.add(templateEntity);
        }
        dynamicAdd(mTemplateList);

        mPicRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mPicAdapter = new WorkDetailPicAdapter(this);
        mPicAdapter.setListener(new WorkDetailPicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(position == 0){
                    takePhoto();
                }
            }
        });
        mPicRecyclerView.setAdapter(mPicAdapter);
        mPicList.add(getResources().getDrawable(R.drawable.take_photo));
        mPicAdapter.setDrawables(mPicList);
    }

    private void dynamicAdd(List<WorkTemplateEntity> list) {

    }

    private void takePhoto() {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 获取拍完后的uri
         Uri mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
         //判断7.0 android 系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //临时添加一个拍照权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider 获取uri
            contentUri = FileProvider.getUriForFile(WorkDetailActivity.this,"com.yatai.suningfiredepartment.fileProvider",
                    new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        }
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            switch (requestCode) {

                case TAKE_PHOTO:
                    Uri picture;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果是7.0android系统
                        picture = contentUri;
                    } else {
                        picture = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/temp.jpg"));
                    }
                    try {
                        Drawable d=Drawable.createFromStream(getContentResolver().openInputStream(picture),"work.jpg");
                        mPicList.add(d);
                        mPicAdapter.notifyDataSetChanged();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    //缺少一个上传功能
//
//                    startPhotoZoom(picture);
                    break;
            }
        }
    }
}
