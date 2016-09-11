package com.yetwish.contactsdemo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yetwish.contactsdemo.database.DbContactsManager;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtils;
import com.yetwish.contactsdemo.utils.FileUtils;
import com.yetwish.contactsdemo.widget.ContactsListAdapter;
import com.yetwish.contactsdemo.widget.ContactsListView;
import com.yetwish.contactsdemo.widget.indexer.ContactsSectionIndexer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactsListActivity extends BaseActivity {

    private ContactsListView mListView;
    private List<Contacts> mDataList = new ArrayList<>();
    private ContactsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initViews();
        loadDataFromDb();
    }

    private void loadDataFromDb() {
        DbContactsManager.getInstance().query(new ApiCallback<List<Contacts>>() {
            @Override
            public void onSuccess(List<Contacts> contactsList) {
                mDataList.addAll(contactsList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    private void initViews() {
        mListView = (ContactsListView) findViewById(R.id.lvContactsList);
        //set emptyView
        View emptyView = findViewById(R.id.contacts_empty_view);
        mListView.setEmptyView(emptyView);

        emptyView.findViewById(R.id.tvContactsEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(getString(R.string.contacts_find));
                FileUtils.getInstance().listContactsFile(new ApiCallback<List<String>>() {
                    @Override
                    public void onSuccess(List<String> fileNames) {
                        hideProgressDialog();
                        showListDialog(fileNames,getString(R.string.contacts_choice),null);
                    }

                    @Override
                    public void onFailed(String msg) {
                        hideProgressDialog();
                    }
                });
            }
        });
        mAdapter = new ContactsListAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);
        mAdapter.setSectionIndexer(new ContactsSectionIndexer(mDataList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //新建联系人
                break;

            case R.id.menu_edit:
                //编辑
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void syncContacts() {
        //get data todo
        mDataList.add(new Contacts(1, "叶桂", null));
        mDataList.add(new Contacts(1, "11马浪凯", null));
        mDataList.add(new Contacts(1, "12陈丽霞", null));
        mDataList.add(new Contacts(1, "12邵剑锋", null));
        mDataList.add(new Contacts(1, "13严雷", null));
        mDataList.add(new Contacts(1, "%A叶浩", null));
        mDataList.add(new Contacts(1, "ABC", null));
        mDataList.add(new Contacts(1, "Beyond", null));
        mDataList.add(new Contacts(1, "尘世", null));
        mDataList.add(new Contacts(1, "段浪", null));
        mDataList.add(new Contacts(1, "芬姐", null));
        mDataList.add(new Contacts(1, "黄Sir", null));
        mDataList.add(new Contacts(1, "胡椒", null));
        mDataList.add(new Contacts(1, "凯越", null));
        mDataList.add(new Contacts(1, "川哥", null));
        mDataList.add(new Contacts(1, "陆公", null));
        mDataList.add(new Contacts(1, "淘客", null));
        mDataList.add(new Contacts(1, "隔壁老王", null));
        mDataList.add(new Contacts(1, "万大爷", null));
        mDataList.add(new Contacts(1, "喜羊羊", null));
        mDataList.add(new Contacts(1, "灰太狼", null));
        mDataList.add(new Contacts(1, "小哥", null));
        mDataList.add(new Contacts(1, "无邪", null));
        mDataList.add(new Contacts(1, "嘻嘻", null));
        mDataList.add(new Contacts(1, "杨过", null));
        mDataList.add(new Contacts(1, "尹少", null));
        mDataList.add(new Contacts(1, "猿分", null));
        mDataList.add(new Contacts(1, "袁飞", null));
        mDataList.add(new Contacts(1, "周飞", null));
        mDataList.add(new Contacts(1, "智障", null));
        mDataList.add(new Contacts(1, "猪", null));
        mDataList.add(new Contacts(1, "温凯文", null));
        mDataList.add(new Contacts(1, "周子翔", null));
        mDataList.add(new Contacts(1, "11程瑞", null));
        mDataList.add(new Contacts(1, "程瑞", null));
        mDataList.add(new Contacts(1, "12陈招子", null));
        mDataList.add(new Contacts(1, "陈招子", null));
        mDataList.add(new Contacts(1, "啊兄", null));
        mDataList.add(new Contacts(1, "包", null));
        mDataList.add(new Contacts(1, "包 子", null));
        mDataList.add(new Contacts(1, "蔡士林", null));
        mDataList.add(new Contacts(1, "呆呆", null));
        mDataList.add(new Contacts(1, "纷纷", null));
        mDataList.add(new Contacts(1, "郭嘉", null));
        mDataList.add(new Contacts(1, "呵呵", null));
        mDataList.add(new Contacts(1, "桂桂", null));
        mDataList.add(new Contacts(1, "黄一航", null));
        mDataList.add(new Contacts(1, "机场大巴", null));
        mDataList.add(new Contacts(1, "家", null));
        mDataList.add(new Contacts(1, "李小龙", null));
        mDataList.add(new Contacts(1, "妈妈", null));
        mDataList.add(new Contacts(1, "牛牛", null));
        mDataList.add(new Contacts(1, "奶奶", null));
        mDataList.add(new Contacts(1, "舒雅", null));
        mDataList.add(new Contacts(1, "宿舍老大", null));
        mDataList.add(new Contacts(1, "亲戚儿子", null));
        mDataList.add(new Contacts(1, "vivo", null));
        mDataList.add(new Contacts(1, "vivo大大", null));

        List<String> phoneList = Arrays.asList(new String[]{"18729900796", "18565656939"});
        String json;
//        mDataList.get(0).setPhoneNumber(phoneList);
        for (Contacts contacts : mDataList) {
            phoneList = Arrays.asList(new String[]{getPhoneNumber(), getPhoneNumber()});
            contacts.setPhoneNumber(phoneList);
            ContactsUtils.updateSortKey(contacts);
//            json = JsonUtils.toJson(contacts);
//            phoneList = JsonUtils.listFromJson(json,String.class);
        }

//        FileUtils.getInstance().saveContacts(mDataList);

    }

    private String getPhoneNumber() {

        return "185" + Math.round(Math.random() * 100000000);
    }


    //当dialog show时不响应back press
    @Override
    public void onBackPressed() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return;
        else
            super.onBackPressed();
    }
}
