package com.xhf.sms.api.service;


import com.xhf.sms.Response;
import com.xhf.sms.bean.ConfigBean;
import com.xhf.sms.bean.MainBean;
import com.xhf.sms.bean.SmsBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by xhf on 2018/4/9
 */
public interface ApiService {

    @POST("logAdd")
    Observable<Response> sendSms(@Body SmsBean phone);


    @POST("productList")
    Observable<List<MainBean>> getMainInfo(@Body Map<String, Integer> requestBodyMap);


    @POST("appconfig")
    Observable<ConfigBean> getconfigInfo();

}
