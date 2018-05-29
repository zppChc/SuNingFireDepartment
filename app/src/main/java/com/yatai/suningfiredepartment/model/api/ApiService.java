package com.yatai.suningfiredepartment.model.api;

import com.yatai.suningfiredepartment.model.entity.LoginEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    /**
     * 登陆请求token
     * @param mobile
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login")
    Observable<LoginEntity> postLogin(@Field("mobile") String mobile, @Field("password") String password);
}
