package com.xhf.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
    }

    private void initView() {
        mBackView=findViewById(R.id.backView);
        mBackView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backView:
//                finish();
                Intent intent = new MQIntentBuilder(this).build();
                startActivity(intent);
                break;
                default:
        }
    }
}
