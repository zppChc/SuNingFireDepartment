package com.yatai.suningfiredepartment.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.yatai.suningfiredepartment.util.ImageUtil;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.WorkDetailPicAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private List<String> upLoadPicPath;

    //图片对象
    private Uri contentUri;
    private File currentImageFile;

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
        upLoadPicPath = new ArrayList<>();

        Intent intent = getIntent();
        String workItem = intent.getStringExtra("workItem");

        Gson gson = new Gson();
        mWorkItemEntity = gson.fromJson(workItem, WorkItemEntity.class);

        mTitleTv.setText(mWorkItemEntity.getName());
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
                    try {
                        takePhoto();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mPicRecyclerView.setAdapter(mPicAdapter);
        mPicList.add(getResources().getDrawable(R.drawable.take_photo));
        mPicAdapter.setDrawables(mPicList);
    }

    private void dynamicAdd(List<WorkTemplateEntity> list) {

    }

    private void takePhoto() throws IOException {
        // 启动系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //在sd下创建文件夹myimage；Environment.getExternalStorageDirectory()得到SD卡路径文件
        File dir = new File(Environment.getExternalStorageDirectory(), "YaTai");
        if (!dir.exists()) {    //exists()判断文件是否存在，不存在则创建文件
            dir.mkdirs();
        }
        //设置日期格式在android中，创建文件时，文件名中不能包含“：”冒号
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = df.format(new Date());
        currentImageFile = new File(dir, filename + ".jpg");
        if (!currentImageFile.exists()) {
            currentImageFile.createNewFile();
        }
         //判断7.0 android 系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //临时添加一个拍照权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider 获取uri
            contentUri = FileProvider.getUriForFile(WorkDetailActivity.this,"com.yatai.suningfiredepartment.fileProvider",currentImageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        }
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    String filePath = currentImageFile.getAbsolutePath();
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    Drawable d=new BitmapDrawable(bitmap);
                    mPicList.add(d);
                    mPicAdapter.notifyDataSetChanged();
                    //缺少一个上传功能
                    uploadAndCompress(bitmap);
                    break;
            }
        }
    }


    private void uploadAndCompress(final Bitmap unCompressBitmap){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bitmap = ImageUtil.compressImage(unCompressBitmap);
                String uploadPicPath = ImageUtil.saveBitmap(bitmap);
                uploadPicture(uploadPicPath);
            }
        }.start();
    }

    private void uploadPicture(String path){
        FinalHttp  http = new FinalHttp();
        String token = "Bearer " + PreferenceUtils.getPerfString(WorkDetailActivity.this, "token", "");
        String url = getString(R.string.base_url) + "image";
        http.addHeader("Authorization", token);
        AjaxParams params = new AjaxParams();
        try {
            params.put("image", new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        params.put("category","task");
        http.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        ToastUtil.show(WorkDetailActivity.this, "Upload Picture Success");
                        Logger.i("Upload pic : "+ s);
                    }else{
                        ToastUtil.show(WorkDetailActivity.this,jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(WorkDetailActivity.this,strMsg);
            }
        });
    }
}
