package com.yatai.suningfiredepartment.util;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {
    public static int randomFillArgb(){
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.argb(0x80,r,g,b);
    }

    public static int randomStrokeRgb(){
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.rgb(r,g,b);
    }

    public static int transparentColor(){
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return Color.argb(0x00,r,g,b);
    }

}
