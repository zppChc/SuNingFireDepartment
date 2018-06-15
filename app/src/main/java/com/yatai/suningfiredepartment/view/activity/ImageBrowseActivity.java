package com.yatai.suningfiredepartment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.yatai.suningfiredepartment.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageBrowseActivity extends AppCompatActivity {

    @BindView(R.id.imageBrowseViewPager)
    ViewPager imageBrowseViewPager;
    private ArrayList<String> imageList;
    private int currentPos;
    ImageBrowseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_image_browse);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        imageList = intent.getStringArrayListExtra("images");
        currentPos = intent.getIntExtra("position",0);

        mAdapter = new ImageBrowseAdapter(this,imageList);
        imageBrowseViewPager.setAdapter(mAdapter);
        imageBrowseViewPager.setCurrentItem(currentPos);

    }

    class ImageBrowseAdapter extends PagerAdapter {
        private  ArrayList<String> mList;
        private Context mContext;

        public ImageBrowseAdapter(Context context,  ArrayList<String> list) {
            mContext = context;
            mList = new ArrayList<>();
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final PhotoView image = new PhotoView(ImageBrowseActivity.this);
            // 开启图片缩放功能
            image.enable();
            // 设置缩放类型
            image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            // 设置最大缩放倍数
            image.setMaxScale(2.5f);
            // 加载图片
            Glide.with(mContext).load(mList.get(position)).into(image);
            // 单击图片，返回
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    image.disenable();
                    finish();
                }
            });
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }
}
