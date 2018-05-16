package com.yatai.suningfiredepartment.view.widget;/**
 * Author: CHC
 * Date: 2018/5/10 17:02
 * Description:
 **/

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

public class FixedImageView extends ImageView {
    private int mScreenHeight;
    public FixedImageView(Context context) {
        this(context,null);
    }

    public FixedImageView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FixedImageView(Context context, @Nullable AttributeSet attrs, int paramInt) {
        super(context,attrs,paramInt);
        init(context,attrs);
    }

    public static int[] getScreenWidthHeight(Context paramContext){
        int[] arrayOfInt = new int[2];
        if (paramContext == null){
            return arrayOfInt;
        }
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((Activity)paramContext).getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int i = localDisplayMetrics.widthPixels;
        int j = localDisplayMetrics.heightPixels;
        arrayOfInt[0]=i;
        arrayOfInt[1]=j;
        return arrayOfInt;
    }

    private void init(Context paramContext, AttributeSet paramAttrSet){
        this.mScreenHeight = getScreenWidthHeight(paramContext)[1];
    }

    protected void onMeasure(int paramInt1, int paramInt2){
        int i = View.MeasureSpec.getSize(paramInt1);
        View.MeasureSpec.getSize(paramInt1);
        setMeasuredDimension(i,this.mScreenHeight);
    }
}
