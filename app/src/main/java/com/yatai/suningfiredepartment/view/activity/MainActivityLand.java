package com.yatai.suningfiredepartment.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.NetUtil;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.MainViewPagerAdapter;
import com.yatai.suningfiredepartment.view.fragment.HomePageFragment;
import com.yatai.suningfiredepartment.view.fragment.HomePageFragmentLand;
import com.yatai.suningfiredepartment.view.fragment.InfoFragment;
import com.yatai.suningfiredepartment.view.fragment.PersonalFragment;
import com.yatai.suningfiredepartment.view.fragment.WorkFragment;
import com.yatai.suningfiredepartment.view.widget.ViewPagerSlide;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import top.wuhaojie.installerlibrary.AutoInstaller;

public class MainActivityLand extends BaseActivity {

    @BindView(R.id.main_view_pager)
    ViewPagerSlide mainViewPager;
    @BindView(R.id.main_menu_tab)
    TabLayout mainMenuTab;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;



    //    private String[] mMenus = {
//            "首页",
//            "工作",
//            "资讯",
//            "统计",
//            "我的"
//    };
//    private int[] images= new int[]{
//            R.drawable.home_page,
//            R.drawable.work,
//            R.drawable.info,
//            R.drawable.calc,
//            R.drawable.personal
//    };
//    private int[] imagesSelected= new int[]{
//            R.drawable.home_page_blue,
//            R.drawable.work_blue,
//            R.drawable.info_blue,
//            R.drawable.calc_blue,
//            R.drawable.personal_blue
//    };
    private String[] mMenus = {
            "首页",
            "工作",
            "资讯",
            "我的"
    };
    private int[] images= new int[]{
            R.drawable.home_page,
            R.drawable.work,
            R.drawable.info,
            R.drawable.personal
    };
    private int[] imagesSelected= new int[]{
            R.drawable.home_page_blue,
            R.drawable.work_blue,
            R.drawable.info_blue,
            R.drawable.personal_blue
    };
    private List<Fragment> fragments;
    private MainViewPagerAdapter mPagerAdapter;
    private String gridId;
    private long exitTime;
    private int nowVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_land);

        ButterKnife.bind(this);

        Configuration configuration = getResources().getConfiguration();
        //0 是竖屏， 1 ,是横屏
        int ori = configuration.getLayoutDirection();
        Logger.i("On Create: ori : "+ ori);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            nowVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if(!NetUtil.isConnected(MainActivityLand.this)){
            ToastUtil.show(MainActivityLand.this,"请连接网络");
        }

//        checkUpdate();
//
//        Intent intent = getIntent();
//        String gridId = intent.getStringExtra("gridId");

        gridId = PreferenceUtils.getPerfString(MainActivityLand .this,"gridId","");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initFragments(gridId);
    }
    private void initFragments(String gridId){
        fragments = new ArrayList<>();
        HomePageFragmentLand homePageFragment = HomePageFragmentLand.newInstance(gridId);
        WorkFragment workFragment = WorkFragment.newInstance(gridId);
        InfoFragment infoFragment = InfoFragment.newInstance(gridId);
//        CensusFragment censusFragment = CensusFragment.newInstance(gridId);
        PersonalFragment personalFragment = PersonalFragment.newInstance(gridId);

        fragments.add(homePageFragment);
        fragments.add(workFragment);
        fragments.add(infoFragment);
//        fragments.add(censusFragment);
        fragments.add(personalFragment);

        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),mMenus,fragments);
        mainViewPager.setAdapter(mPagerAdapter);
        mainMenuTab.setupWithViewPager(mainViewPager);

        for (int i = 0; i <mPagerAdapter.getCount(); i++ ){
            TabLayout.Tab tab =  mainMenuTab.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.item_menu);
            TextView textView = (TextView)tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(mMenus[i]);
            textView.setTextSize(10);
            ImageView imageView=(ImageView)tab.getCustomView().findViewById(R.id.tab_img);
            imageView.setImageDrawable(getResources().getDrawable(images[i]));
            imageView.setPadding(0,5,0,0);
            if (i == 0){
                imageView.setSelected(true);
//               tab.getCustomView().findViewById(R.id.tab_layout).setSelected(true);
                imageView.setImageDrawable(getResources().getDrawable(imagesSelected[i]));
            }
        }

        mainMenuTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_layout).setSelected(true);
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.tab_img);
                imageView.setSelected(true);
                imageView.setImageDrawable(getResources().getDrawable(imagesSelected[tab.getPosition()]));
                mainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_layout).setSelected(false);
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.tab_img);
                imageView.setSelected(false);
                imageView.setImageDrawable(getResources().getDrawable(images[tab.getPosition()]));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            Logger.i("Main 竖屏");
            Intent intent = new Intent(MainActivityLand.this,MainActivity.class);
            startActivity(intent);
            MainActivityLand.this.finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            Logger.i("Main 竖屏");
            Intent intent = new Intent(MainActivityLand.this,MainActivity.class);
            startActivity(intent);
            MainActivityLand.this.finish();
        }
    }

    /**
     * 当按返回键时
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.show(MainActivityLand.this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
                return false;
            } else {
                this.finish();
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void checkUpdate(){
        FinalHttp http = new FinalHttp();
        String url = getString(R.string.base_url)+"androidVersion";
        http.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        JSONObject data = jb.getJSONObject("data");
                        String versionCode = data.getString("version_code");
                        String versionName = data.getString("version_name");
                        String versionDesc = data.getString("version_desc");
                        String minCode = data.getString("min_version");
                        String downloadUrl = data.getString("download_url");
                        boolean forceUpdate = data.getBoolean("forced_upgrade");

                        if (nowVersion < Integer.valueOf(minCode)){
                            setUpDialog(versionName,downloadUrl,versionDesc,true);
                        }else{
                            if (forceUpdate){
                                if (nowVersion < Integer.valueOf(versionCode)){
                                    setUpDialog(versionName,downloadUrl,versionDesc,true);
                                }
                            }else{
                                if (nowVersion < Integer.valueOf(versionCode)){
                                    setUpDialog(versionName,downloadUrl,versionDesc,false);
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


    /**
     *
     * @param versionname
     *            地址中版本的名字
     * @param downloadurl
     *            下载包的地址
     * @param desc
     *            版本的描述
     */

    protected void setUpDialog(String versionname, final String downloadurl,String desc,final boolean isFocusUpdate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本升级");
        builder.setMessage(desc);
        if (isFocusUpdate){
            builder.setCancelable(false);
        }else{
            builder.setCancelable(true);
            builder.setNegativeButton("暂不升级",null);
        }
        builder.setPositiveButton("立即更新",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                loadNewVersionProgress(downloadurl);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 下载新版本程序，需要子线程
     */
    private void loadNewVersionProgress(final String uri){
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.setMessage("正在下载更新");
        pd.show();
        //启动子线程下载任务
        new Thread(){
            @Override
            public void run() {
                try {
                    File file = getFileFromServer(uri, pd);
                    sleep(6000);
                    installApk(file);
                    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    //下载apk失败
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }}.start();
    }

    /**
     * 从服务器获取apk文件的代码
     * 传入网址uri，进度条对象即可获得一个File文件
     * （要在子线程中执行哦）
     */
    public static File getFileFromServer(String uri, ProgressDialog pd) throws Exception{
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(uri);
            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            long time= System.currentTimeMillis();//当前时间的毫秒数
            File file = new File(Environment.getExternalStorageDirectory(), time+"updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len ;
            int total=0;
            while((len =bis.read(buffer))!=-1){
                fos.write(buffer, 0, len);
                total+= len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }
        else{
            return null;
        }
    }

    /**
     * 安装apk
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        Uri data;
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(MainActivityLand.this, "com.yatai.suningfiredepartment.fileProvider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        //执行的数据类型
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
