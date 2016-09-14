package com.yetwish.contactsdemo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yetwish.contactsdemo.BaseActivity;
import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.database.DbContactsManager;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 编辑 / 新建联系人页面
 * Created by yetwish on 2016/9/12.
 */
public class ContactsEditActivity extends BaseActivity {

    public static void startActivity(Context context, Contacts contacts) {
        Intent intent = new Intent(context, ContactsEditActivity.class);
        intent.putExtra(ContactsDetailActivity.EXTRA_CONTACTS, contacts);
        context.startActivity(intent);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ContactsEditActivity.class);
        context.startActivity(intent);
    }

    private LinearLayout llPhoneNumbers;
    private EditText etName;
    private ImageButton ibAdd;
    private Contacts mContacts;

    private List<EditText> mViews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        etName = (EditText) findViewById(R.id.etContactsName);
        llPhoneNumbers = (LinearLayout) findViewById(R.id.llContactsPhone);
        ibAdd = (ImageButton) findViewById(R.id.ibAddPhoneNumber);
        mViews = new ArrayList<>();
        mContacts = (Contacts) getIntent().getSerializableExtra(ContactsDetailActivity.EXTRA_CONTACTS);
        //mContacts may be null

        if (mContacts != null) {
            etName.setText(mContacts.getName());
            showPhoneNumbers();
            getSupportActionBar().setTitle(R.string.contacts_edit_title);
        } else {
            getSupportActionBar().setTitle(R.string.contacts_add_title);
            addPhoneView();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoneView();
            }
        });

    }

    private EditText addPhoneView() {
        final View numberView = LayoutInflater.from(this).inflate(R.layout.phone_edittext, null);
        llPhoneNumbers.addView(numberView);
        final EditText etView = (EditText) numberView.findViewById(R.id.etPhone);
        mViews.add(etView);
        numberView.findViewById(R.id.ivDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViews.remove(etView);
                llPhoneNumbers.removeView(numberView);
            }
        });
        return etView;
    }

    private void showPhoneNumbers() {
        for (String item : mContacts.getPhoneNumber()) {
            EditText etNumber = addPhoneView();
            etNumber.setText(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_finish:
                //完成
                String name = getTrimText(etName);
                if ("".equals(name)) {
                    showToastShort("联系人名称不能为空");
                    return true;
                }
                //保存联系人
                Contacts contacts = new Contacts();
                contacts.setName(name);
                contacts.setPhoneNumber(getPhoneNumbers());
                //获取sortKey 和 searchKey
                ContactsUtils.updateSortKey(contacts);
                if (mContacts == null) {
                    DbContactsManager.getInstance().insert(contacts);
                } else if (!contacts.equals(mContacts)) { //如果没有改变则不需要更新
                    contacts.setId(mContacts.getId());
                    DbContactsManager.getInstance().update(contacts);
                }
                hideKeyBoard();
                finish();
                break;
            case android.R.id.home:
                hideKeyBoard();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getPhoneNumbers() {
        List<String> phones = new ArrayList<>();
        for (EditText view : mViews) {
            String phone = getTrimText(view);
            if (!"".equals(phone)) {
                phones.add(phone);
            }
        }
        return phones;
    }


    private String getTrimText(TextView view) {
        return view.getText().toString().trim();
    }

}
