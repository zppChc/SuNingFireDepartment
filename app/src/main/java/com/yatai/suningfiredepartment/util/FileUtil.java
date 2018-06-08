package com.yatai.suningfiredepartment.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: CHC
 * Date: 2018/5/9  8:58
 * Description:
 **/
public class FileUtil {
    public static final String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String ADPATH = FileUtil.SDPATH+"SuNingFireDepartment";
    //SD卡根路径
    private static String  mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    //手机的缓存根目录
    private static String mDataRootPath = "";
    //保存图片的目录
    private final static String IMG_PATH = "/LB/image";
    private final static String IMG_SUFFIX = ".png";
    public FileUtil(Context context) {
        mDataRootPath = context.getCacheDir().getPath();
    }

    //获取图片存储的根目录
    public static String getImgDIrectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + IMG_PATH : mDataRootPath + IMG_PATH;
    }
    public static String getImgpath(String imgName) {
        File file = new File(getImgDIrectory());
        if (!file.exists()) {
            file.mkdirs();
        }
        return getImgDIrectory() + imgName + IMG_SUFFIX;
    }

    //图片保存到本地，--返回图片的路径
    public  static String saveImg(ByteArrayOutputStream bos) {
        FileOutputStream fos = null;
        String path = getImgpath(String.valueOf(System.currentTimeMillis()));
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            fos.write(bos.toByteArray());
            fos.flush();
        } catch (IOException e) {
            path = "";
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    public static List<String> getAllAD(){
        File file = new File(FileUtil.ADPATH);
        File[] fileList = file.listFiles();
        List<String> list = new ArrayList<>();
        if (null != fileList){
            for (File f: fileList){
                list.add(f.getAbsolutePath());
            }
        }
        return list;
    }

    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
