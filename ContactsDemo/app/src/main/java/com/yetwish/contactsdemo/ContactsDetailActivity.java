package com.yetwish.contactsdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yetwish.contactsdemo.model.Contacts;

/**
 * 联系人详情页
 * Created by yetwish on 2016/9/3.
 */
public class ContactsDetailActivity extends BaseActivity{

    public static void startActivity(Context context){
        Intent intent = new Intent(context,ContactsDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }


}
