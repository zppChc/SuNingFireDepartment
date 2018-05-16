package com.yatai.suningfiredepartment.util;

import android.app.DownloadManager;
import android.renderscript.ScriptGroup;

import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.model.util.HttpUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: CHC
 * Date: 2018/5/9  9:24
 * Description:
 **/
public class OKHttpImageDownloader {
    public static void download(String url){
        final Request request = new Request.Builder().url(url).build();
        HttpUtils.client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                FileUtil.createSdDir();
                String url  = response.request().url().toString();
                int index = url.lastIndexOf("/");
                String pictureName = url.substring(index+1);
                if (FileUtil.isFileExist(pictureName)){
                    return;
                }
                Logger.i("pictureName = " + pictureName);
                FileOutputStream fos = new FileOutputStream(FileUtil.createFile(pictureName));
                InputStream in = response.body().byteStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len= in.read(buf))!=-1){
                    fos.write(buf,0,len);
                }
                fos.flush();
                in.close();
                fos.close();
            }
        });
    }
}
