package com.yetwish.contactsdemo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yetwish.contactsdemo.ApiCallback;
import com.yetwish.contactsdemo.BaseActivity;
import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入页面
 * Created by yetwish on 2016/9/3.
 */
public class ContactsLoadActivity extends BaseActivity {

    public static final int REQUEST_CODE = 1;

    public static final String EXTRA_FILE = "EXTRA_FILE";

    private List<File> mFiles = new ArrayList<>();
    private List<String> mFileNames = new ArrayList<>();

    private ArrayAdapter<String> mAdapter;

    private ListView mListView;

    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mIntent = getIntent();
        initViews();
        searchContactsFile();

    }

    private void initViews() {
        mListView = (ListView) findViewById(R.id.lvLoad);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFileNames);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIntent.putExtra(EXTRA_FILE, mFiles.get(position));
                setResult(RESULT_OK,mIntent);
                finish();
            }
        });
    }

    private void searchContactsFile() {
        showProgressDialog(getString(R.string.contacts_find));
        FileUtils.listContactsFile(new ApiCallback<List<File>>() {
            @Override
            public void onSuccess(List<File> files) {
                hideProgressDialog();
                mFiles.addAll(files);
                for (File file : files) {
                    mFileNames.add(file.getName());
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(String msg) {
                hideProgressDialog();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
