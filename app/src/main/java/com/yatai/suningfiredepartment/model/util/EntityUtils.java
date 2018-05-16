package com.yatai.suningfiredepartment.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

/**
 * Author: CHC
 * Date: 2018/5/9  9:34
 * Description:
 **/
public class EntityUtils {
    private EntityUtils(){}

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class,new DateTimeTypeAdapter())
            .create();
}
