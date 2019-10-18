package com.xhf.sms.adapter;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xhf.sms.R;
import com.xhf.sms.bean.MainBean;

import java.util.List;

public class MainAdapter extends BaseQuickAdapter<MainBean,BaseViewHolder> {


    public MainAdapter(@Nullable List<MainBean> data) {
        super(R.layout.item_main, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MainBean item) {
        helper.setImageResource(R.id.imgView, item.getImgUrl());

    }
}
