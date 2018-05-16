package com.yatai.suningfiredepartment.model.api;/**
 * Author: CHC
 * Date: 2018/5/9  10:17
 * Description:
 **/

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class StringConverterFactory extends Converter.Factory {
    public static StringConverterFactory create(){
        return new StringConverterFactory();
    }

    public Converter<ResponseBody,?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit){
        if (type == String.class){
            return new StringConverter();
        }
        //其他类型 我们不处理，返回null就行
        return  null;
    }
}
