package com.xhf.sms.api.service;


import com.xhf.sms.Response;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by xhf on 2018/4/9
 */
public interface ApiService {

    @POST("logadd")
    Observable<Response> sendSms(@Body String phone);

}
