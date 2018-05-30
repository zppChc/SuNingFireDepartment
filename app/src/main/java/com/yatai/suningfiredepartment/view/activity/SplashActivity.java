package com.yatai.suningfiredepartment.view.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.app.MyApplication;
import com.yatai.suningfiredepartment.di.components.DaggerSplashComponent;
import com.yatai.suningfiredepartment.di.modules.SplashModule;
import com.yatai.suningfiredepartment.presenter.SplashContract;
import com.yatai.suningfiredepartment.presenter.SplashPresenter;
import com.yatai.suningfiredepartment.util.AppUtil;
import com.yatai.suningfiredepartment.util.FileUtil;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.view.widget.FixedImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SplashActivity extends BaseActivity implements SplashContract.View, EasyPermissions.PermissionCallbacks {

    private static final int PERMISSON_REQUESTCODE = 1;

    @BindView(R.id.splash_img)
    FixedImageView splashImg;
    @Inject
    SplashPresenter mPresenter;

    /**
     * 需要进行检测的权限数组
     */
    protected  String[] needPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        DaggerSplashComponent.builder()
                .netComponent(MyApplication.get(this).getNetComponent())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
        initStatus();
    }

    private void initStatus(){
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodePermissions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //do noting
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void delaySplash(){
        List<String> picList = FileUtil.getAllAD();
        if (picList.size() > 0){
            Random random = new Random();
            int index = random.nextInt(picList.size());
            int imgIndex = PreferenceUtils.getPrefInt(this,"splash_img_index",0);
            Logger.i("当前的imageIndex= "+imgIndex);
            if (index == imgIndex){
                if (index >= picList.size()){
                    index --;
                }else if (imgIndex==0){
                    if (index +1 <picList.size()){
                        index++;
                    }
                }
            }
            PreferenceUtils.setPrefInt(this,"splash_img_index",index);
            Logger.i("当前的picList.size =" + picList.size() + " index = " +index);
            File file = new File(picList.get(index));
            try{
                InputStream fis = new FileInputStream(file);
                splashImg.setImageDrawable(InputStream2Drawable(fis));
                animWelcomeImage();
                fis.close();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){

            }

        }else{
            try{
                AssetManager assetManager = this.getAssets();
                InputStream in  = assetManager.open("welcome_default.jpg");
                splashImg.setImageDrawable(InputStream2Drawable(in));
                animWelcomeImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Drawable InputStream2Drawable(InputStream is){
        Drawable drawable = BitmapDrawable.createFromStream(is,"splashImg");
        return drawable;
    }

    private void animWelcomeImage(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(splashImg,"translationX", -100F);
        animator.setDuration(1500L).start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                //动画完成后启动 登陆页面 或者 根据需求进行更改
//                if (PreferenceUtils.getPerfString(MyApplication.getContext(),"token","").isEmpty()) {
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @AfterPermissionGranted(PERMISSON_REQUESTCODE)
    private void requestCodePermissions(){
        if (!EasyPermissions.hasPermissions(this, needPermissions)){
            EasyPermissions.requestPermissions(this,"应用需要这些权限",PERMISSON_REQUESTCODE,needPermissions);
        }else{
            setContentView(R.layout.activity_splash);
            ButterKnife.bind(SplashActivity.this);
            delaySplash();
            String deviceId = AppUtil.getDeviceId(this);
            mPresenter.getSplash(deviceId);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        recreate();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showMissingPermissionDialog();
    }

    private void showMissingPermissionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        //拒绝，推出应用
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用设置
     */

    private void startAppSettings(){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+getPackageName()));
        startActivity(intent);
    }
}
