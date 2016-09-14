package com.yetwish.contactsdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yetwish.contactsdemo.ApiCallback;
import com.yetwish.contactsdemo.BaseActivity;
import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.database.DbContactsManager;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.FileUtils;
import com.yetwish.contactsdemo.utils.SPUtils;
import com.yetwish.contactsdemo.widget.ContactsListAdapter;
import com.yetwish.contactsdemo.widget.ContactsListView;
import com.yetwish.contactsdemo.widget.search.CustomSearchView;
import com.yetwish.contactsdemo.widget.search.ISearchListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 联系人列表页面
 */
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
        firstInit();
        initViews();
    }

    /**
     * app第一次启动时，将data数据写入到手机设备中
     */
    private void firstInit() {
        if ((Boolean) SPUtils.getInstance().get(SPUtils.SP_FIRST_START, true))
            try {
                FileUtils.pushData2SDCard(getResources().getAssets().open(FileUtils.DEFAULT_DATA_FILE_NAME), new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        SPUtils.getInstance().put(SPUtils.SP_FIRST_START, false);
                    }

                    @Override
                    public void onFailed(String msg) {
                        // TODO: 2016/9/14 写入失败
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                if (id != NULL) {//选中了
                    for (Contacts contacts : mDataList) {
                        if (contacts.getId() == id) {
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

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                if (mAdapter.isCbVisible()) return false;
                showBasicDialog("删除联系人", "确定要删除所选中的联系人吗？", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which.equals(DialogAction.POSITIVE)) {
                            DbContactsManager.getInstance().delete((int) id);
                            mDataList.remove(position);
                            mAdapter.notifyDataSetChanged();
                            hideBasicDialog();
                            showToastShort("删除成功");
                        } else if (which.equals(DialogAction.NEGATIVE)) {
                            hideBasicDialog();
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContactsLoadActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //load
                File file = (File) data.getSerializableExtra(ContactsLoadActivity.EXTRA_FILE);
                FileUtils.loadContacts(file, new ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        loadDataFromDb();
                    }

                    @Override
                    public void onFailed(String msg) {

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
                invalidateListView(true);
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
                                    showToastLong("成功导出到" + data);
                                    invalidateListView(false);
                                    hideKeyBoard();
                                }

                                @Override
                                public void onFailed(String msg) {
                                    hideKeyBoard();
                                }
                            });
                        }
                    });
                } else
                    showToastShort("请选中要导出的联系人");
                break;

            case R.id.menu_delete:
                //删除
                if (mAdapter.getCheckedContacts() != null) {
                    //showDialog
                    showBasicDialog("删除联系人", "确定要删除所选中的联系人吗？", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (which.equals(DialogAction.POSITIVE)) {
                                for (Contacts contacts : mAdapter.getCheckedContacts()) {
                                    mDataList.remove(contacts);
                                    DbContactsManager.getInstance().delete(contacts.getId());
                                }
                                invalidateListView(false);
                                hideBasicDialog();
                                showToastShort("删除成功");
                            } else if (which.equals(DialogAction.NEGATIVE)) {
                                hideBasicDialog();
                            }
                        }
                    });

                } else
                    showToastShort("请选中要删除的联系人");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void invalidateListView(boolean isCbVisible) {
        mAdapter.setCbVisible(isCbVisible);
        invalidateOptionsMenu();
        if (isCbVisible) //don't work todo
            mSearchView.setFocusable(false);
        else
            mSearchView.setFocusable(true);
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
            invalidateListView(false);
        } else
            super.onBackPressed();
    }

}
