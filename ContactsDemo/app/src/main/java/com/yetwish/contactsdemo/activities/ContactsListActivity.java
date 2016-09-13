package com.yetwish.contactsdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yetwish.contactsdemo.ApiCallback;
import com.yetwish.contactsdemo.BaseActivity;
import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.database.DbContactsManager;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.FileUtils;
import com.yetwish.contactsdemo.widget.ContactsListAdapter;
import com.yetwish.contactsdemo.widget.ContactsListView;
import com.yetwish.contactsdemo.widget.search.CustomSearchView;
import com.yetwish.contactsdemo.widget.search.ISearchListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactsListActivity extends BaseActivity {

    private static final String TAG = ContactsListActivity.class.getSimpleName();

    private ContactsListView mListView;
    private List<Contacts> mDataList = new ArrayList<>();
    private ContactsListAdapter mAdapter;

    private CustomSearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initViews();
    }

    private void loadDataFromDb() {
        DbContactsManager.getInstance().query(new ApiCallback<List<Contacts>>() {
            @Override
            public void onSuccess(List<Contacts> contactsList) {
                mDataList.clear();
                mDataList.addAll(contactsList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String msg) {
                Log.e(TAG, msg);
            }
        });
    }

    private void initViews() {
        mListView = (ContactsListView) findViewById(R.id.lvContactsList);
        //set emptyView
        View emptyView = findViewById(R.id.contacts_empty_view);
        mListView.setEmptyView(emptyView);

        mSearchView = (CustomSearchView) findViewById(R.id.svContactsSearch);
        mSearchView.setSearchListener(new ISearchListener() {
            @Override
            public void onSearch(CharSequence input) {
                if ("".equals(input.toString()))
                    mListView.setVisibility(View.VISIBLE);
                else
                    mListView.setVisibility(View.GONE);
            }

            @Override
            public void onQuitSearch(int id) {
                mListView.setVisibility(View.VISIBLE);
                hideKeyBoard();
                if(id != NULL){//选中了
                    for(Contacts contacts : mDataList){
                        if(contacts.getId() == id){
                            Intent intent = new Intent(ContactsListActivity.this, ContactsDetailActivity.class);
                            intent.putExtra(ContactsDetailActivity.EXTRA_CONTACTS, contacts);
                            startActivity(intent);
                            break;
                        }
                    }
                }
            }

        });

        emptyView.findViewById(R.id.tvContactsEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsListActivity.this, ContactsLoadActivity.class);
                startActivityForResult(intent, ContactsLoadActivity.REQUEST_CODE);
            }
        });

        mAdapter = new ContactsListAdapter(this, mDataList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.isCbVisible()) {
                    mAdapter.onCbClick(position);
                } else {
                    Intent intent = new Intent(ContactsListActivity.this, ContactsDetailActivity.class);
                    intent.putExtra(ContactsDetailActivity.EXTRA_CONTACTS, mDataList.get(position));
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContactsLoadActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //load
                int a = 10 * 7 + 8;
                File file = (File) data.getSerializableExtra(ContactsLoadActivity.EXTRA_FILE);
                FileUtils.loadContacts(file, new ApiCallback<List<Contacts>>() {
                    @Override
                    public void onSuccess(List<Contacts> data) {
                        mDataList.addAll(data);
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.e(TAG, msg);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mAdapter != null && mAdapter.isCbVisible())
            this.getMenuInflater().inflate(R.menu.menu_contacts_edit, menu);
        else
            this.getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                //新建联系人
                ContactsEditActivity.startActivity(this);
                break;

            case R.id.menu_edit:
                //编辑
                mAdapter.setCbVisible(true);
                invalidateOptionsMenu();
                break;

            case R.id.menu_all:
                //全选、反选
                mAdapter.onAllCbClick();
                break;

            case R.id.menu_save:
                //导出
                if (mAdapter.getCheckedContacts() != null) {
                    showInputDialog("导出联系人", "请输入文件名称，直接确定为默认", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            FileUtils.saveContacts(mDataList, input.toString(), new ApiCallback<String>() {
                                @Override
                                public void onSuccess(String data) {
                                    Toast.makeText(ContactsListActivity.this, "成功导出到" + data, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailed(String msg) {
                                }
                            });
                        }
                    });
                } else
                    Toast.makeText(this, "请选中要导出的联系人", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_delete:
                //删除
                if (mAdapter.getCheckedContacts() != null) {
                    for (Contacts contacts : mAdapter.getCheckedContacts()) {
                        DbContactsManager.getInstance().delete(contacts.getId());
                    }
                    // TODO: 2016/9/12   删除完成需要视图变换
                    mAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(this, "请选中要删除的联系人", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDb();
    }

    //当dialog show时不响应back press
    @Override
    public void onBackPressed() {
        if (mAdapter != null && mAdapter.isCbVisible()) {
            mAdapter.setCbVisible(false);
            invalidateOptionsMenu();
        } else
            super.onBackPressed();
    }

}
