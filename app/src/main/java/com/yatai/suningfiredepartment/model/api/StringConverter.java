package com.yatai.suningfiredepartment.model.api;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author: CHC
 * Date: 2018/5/9  10:20
 * Description:
 **/
public class StringConverter implements Converter<ResponseBody,String>{

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.toString();
    }
}
