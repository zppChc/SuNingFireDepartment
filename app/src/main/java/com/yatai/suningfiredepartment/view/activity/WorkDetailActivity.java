package com.yatai.suningfiredepartment.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkDetailActivity extends AppCompatActivity{

    private static final int TAKE_PHOTO = 1;

    @BindView(R.id.title_name)
    TextView mTitleTv;
    @BindView(R.id.title_image_back)
    ImageView mImageBack;
    @BindView(R.id.work_detail_description_text_view)
    TextView mDescriptionTv;
    @BindView(R.id.temp_work_detail_work_tv)
    TextView mTemplateWorkTitleTv;
    @BindView(R.id.work_detail_dynamic_layout)
    LinearLayout mDynamicLayout;
    @BindView(R.id.work_detail_confirm_button)
    Button mConfirmBtn;
    @BindView(R.id.work_Detail_photo_display_recycler_view)
    RecyclerView mPicRecyclerView;

    private ProgressDialog mProgressDialog;

    private WorkItemEntity mWorkItemEntity;
    private List<WorkTemplateEntity> mTemplateList;
    private List<Drawable> mPicList;
    private WorkDetailPicAdapter mPicAdapter;
    private List<String> upLoadPicPath;
    private List<Double> mLatLng;
    private FinalHttp mHttp;
    private Gson gson;

    //图片对象
    private Uri contentUri;
    private File currentImageFile;


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    private final Timer timer = new Timer();
    private TimerTask task;
    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 要做的事情
//            Logger.i(""+msg.what);
            if (msg.what == 1){
                timer.cancel();
                submitInfo();
            }
        }
    };

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
        initLocation();
    }


    private void initView() throws JSONException {
        mTemplateList = new ArrayList<>();
        mPicList = new ArrayList<>();
        upLoadPicPath = new ArrayList<>();
        mLatLng= new ArrayList<>();
        mHttp = new FinalHttp();

        mProgressDialog = new ProgressDialog(WorkDetailActivity.this,ProgressDialog.THEME_HOLO_DARK);
        mProgressDialog.setMessage("正在提交...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        Intent intent = getIntent();
        String workItem = intent.getStringExtra("workItem");

        gson = new Gson();
        mWorkItemEntity = gson.fromJson(workItem, WorkItemEntity.class);

        mTitleTv.setText(mWorkItemEntity.getName());
        mDescriptionTv.setText("任务描述："+mWorkItemEntity.getDescription());
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkDetailActivity.this.finish();
            }
        });

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
        //初始化计时器任务
        task = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                if ((mPicList.size()-1 == upLoadPicPath.size()) && mPicList.size()>1) {
                    message.what = 1;
                }else{
                    message.what = 0;
                }
                handler.sendMessage(message);
            }
        };

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPicList.size() == 1){
                    ToastUtil.show(WorkDetailActivity.this,"请拍摄至少一张照片");
                }else{
                    mProgressDialog.show();
                    timer.schedule(task,2000,3000);
                }

            }
        });
    }

    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        locationClient.startLocation();
    }

    private void dynamicAdd(List<WorkTemplateEntity> list) {
        if (list.size() > 0){
            mTemplateWorkTitleTv.setVisibility(View.VISIBLE);
            for (int i = 0; i< list.size(); i++){
                final int j = i;
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView textView = new TextView(this);
                textView.setText(mTemplateList.get(i).getName());
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER_VERTICAL);

                final EditText editText = new EditText(this);
                editText.setGravity(Gravity.CENTER_VERTICAL);
                editText.setTextSize(16);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        mTemplateList.get(j).setContent(editText.getText().toString());
                    }
                });

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
                LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,3.0f);

                layout.addView(textView,textParams);
                layout.addView(editText,editParams);
                mDynamicLayout.addView(layout,layoutParams);
            }
        }
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
        String url = getString(R.string.base_url) + "image/task";
        http.addHeader("Authorization", token);
        AjaxParams params = new AjaxParams();
        try {
            params.put("image", new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        http.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        JSONObject data = jb.getJSONObject("data");
                        upLoadPicPath.add(data.getString("imagePath"));
                        Logger.i("Upload Path : " + data.getString("imagePath"));
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

    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    sb.append("定位成功" + "\n");
                    sb.append("定位类型: " + location.getLocationType() + "\n");
                    sb.append("经    度    : " + location.getLongitude() + "\n");
                    sb.append("纬    度    : " + location.getLatitude() + "\n");
                    mLatLng.add(location.getLongitude());
                    mLatLng.add(location.getLatitude());
                     //**************
                    //定位失败也可以上传
                    //**************
//                    ToastUtil.show(WorkDetailActivity.this,sb.toString());
                } else {
                    //定位失败
                    sb.append("定位失败" + "\n");
                    sb.append("错误码:" + location.getErrorCode() + "\n");
                    sb.append("错误信息:" + location.getErrorInfo() + "\n");
                    sb.append("错误描述:" + location.getLocationDetail() + "\n");
//                    ToastUtil.show(WorkDetailActivity.this,sb.toString());
                }
            }
        }
    };

    private void submitInfo(){

//        for (int i = 0 ; i< upLoadPicPath.size(); i++) {
//            Logger.i("Content : " + upLoadPicPath.get(i));
//        }
        //目前没有增加 输入信息的校验。。。主要是不知道里怎么增加

        String url = getString(R.string.base_url)+"taskRecord";
        String token =PreferenceUtils.getPerfString(WorkDetailActivity.this, "token", "");
        mHttp.addHeader("Authorization", "Bearer " + token);
        AjaxParams params = new AjaxParams();
        params.put("task_id",String.valueOf(mWorkItemEntity.getId()));
        params.put("content",convertTempleList(mTemplateList));
        params.put("images",gson.toJson(upLoadPicPath));
        params.put("position",gson.toJson(mLatLng));
        mHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        ToastUtil.show(WorkDetailActivity.this,"上传成功");
                        finish();
                    }else{
                        ToastUtil.show(WorkDetailActivity.this,jb.getString("message"));
                    }
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(WorkDetailActivity.this,strMsg);
                mProgressDialog.dismiss();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer !=null){
            timer.cancel();
        }
    }

    private String convertTempleList(List<WorkTemplateEntity> list){
        List<TemplateDemo> demoList = new ArrayList<>();
        TemplateDemo templateDemo = new TemplateDemo();
        for (int i=0; i<list.size(); i++){
            templateDemo.setName(list.get(i).getName());
            templateDemo.setContent(list.get(i).getContent());
            demoList.add(templateDemo);
        }
        return new Gson().toJson(demoList);
    }

    class TemplateDemo{
        String name;
        String content;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
