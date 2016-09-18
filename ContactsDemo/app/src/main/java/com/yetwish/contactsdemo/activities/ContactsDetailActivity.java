package com.yetwish.contactsdemo.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yetwish.contactsdemo.ApiCallback;
import com.yetwish.contactsdemo.BaseActivity;
import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.database.DbContactsManager;
import com.yetwish.contactsdemo.model.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * 联系人详情页
 * Created by yetwish on 2016/9/3.
 */
public class ContactsDetailActivity extends BaseActivity {

    public static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";

//    public static final String TAG = "ContactsListActivity";

    private LinearLayout llPhoneNumbers;
    private TextView tvName;

    private Contacts mContacts;

    private List<TextView> mViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViews = new ArrayList<>();
        tvName = (TextView) findViewById(R.id.tvContactsName);
        llPhoneNumbers = (LinearLayout) findViewById(R.id.llContactsPhone);
        mContacts = (Contacts) getIntent().getSerializableExtra(EXTRA_CONTACTS);

        tvName.setText(mContacts.getName());
        showPhoneNumbers();
    }

    private void showPhoneNumbers() {
        for (String number : mContacts.getPhoneNumber()) {
            TextView tvNumber = (TextView) LayoutInflater.from(this).inflate(R.layout.phone_textview, null);
            llPhoneNumbers.addView(tvNumber);
            tvNumber.setText(number);
            mViews.add(tvNumber);
        }
    }


    private void updateContactsInfo() {
        tvName.setText(mContacts.getName());
        for (View view : mViews) {
            llPhoneNumbers.removeView(view);
        }
        mViews.clear();
        showPhoneNumbers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DbContactsManager.getInstance().query(mContacts.getId(), new ApiCallback<Contacts>() {
            @Override
            public void onSuccess(Contacts data) {
                if (mContacts.equals(data)) return;
                mContacts = data;
                updateContactsInfo();
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_edit:
                //编辑
                ContactsEditActivity.startActivity(this, mContacts);
                break;
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


}
