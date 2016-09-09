package com.yetwish.contactsdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtils;
import com.yetwish.contactsdemo.utils.FileUtils;
import com.yetwish.contactsdemo.utils.JsonUtils;
import com.yetwish.contactsdemo.widget.ContactsListAdapter;
import com.yetwish.contactsdemo.widget.ContactsListView;
import com.yetwish.contactsdemo.widget.indexer.ContactsSectionIndexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContactsListActivity extends AppCompatActivity {

    private ContactsListView mContactsListView;
    private List<Contacts> mContactsDataList;
    private ContactsListAdapter mContactsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContactsListView = (ContactsListView) findViewById(R.id.lvContactsList);
        syncContacts();
        mContactsListView.setAdapter(mContactsAdapter);
        mContactsAdapter.setSectionIndexer(new ContactsSectionIndexer(mContactsDataList));
    }

    private void syncContacts() {
        //get data todo
        mContactsDataList = new ArrayList<Contacts>();
//        mContactsDataList.add(new Contacts(1, "叶桂", null, "yegui", true));
//        mContactsDataList.add(new Contacts(1, "温凯文", null, "wenkaiwen", true));
//        mContactsDataList.add(new Contacts(1, "周子翔", null, "zhouzixiang", true));
//        mContactsDataList.add(new Contacts(1, "11程瑞", null, "#11chengrui", true));
//        mContactsDataList.add(new Contacts(1, "程瑞", null, "chengrui", false));
//        mContactsDataList.add(new Contacts(1, "12陈招子", null, "#12chenzhaozi", false));
//        mContactsDataList.add(new Contacts(1, "陈招子", null, "chenzhaozi", false));
//        mContactsDataList.add(new Contacts(1, "啊兄", null, "axiong", true));
//        mContactsDataList.add(new Contacts(1, "包", null, "bao", true));
//        mContactsDataList.add(new Contacts(1, "包子", null, "baozi", false));
//        mContactsDataList.add(new Contacts(1, "蔡士林", null, "caishilin", true));
//        mContactsDataList.add(new Contacts(1, "呆呆", null, "daidai", true));
//        mContactsDataList.add(new Contacts(1, "纷纷", null, "fenfen", true));
//        mContactsDataList.add(new Contacts(1, "郭嘉", null, "guojia", false));
//        mContactsDataList.add(new Contacts(1, "呵呵", null, "hehe", true));
//        mContactsDataList.add(new Contacts(1, "桂桂", null, "guigui", true));
//        mContactsDataList.add(new Contacts(1, "黄一航", null, "huangyihang", false));
//        mContactsDataList.add(new Contacts(1, "机场大巴", null, "jichangdaba", false));
//        mContactsDataList.add(new Contacts(1, "家", null, "jia", true));
//        mContactsDataList.add(new Contacts(1, "李小龙", null, "lixiaolong", true));
//        mContactsDataList.add(new Contacts(1, "妈妈", null, "mama", true));
//        mContactsDataList.add(new Contacts(1, "牛牛", null, "niuniu", true));
//        mContactsDataList.add(new Contacts(1, "vivo", null, "vivo", true));
//        mContactsDataList.add(new Contacts(1, "vivo大大", null, "vivodada", false));

        mContactsDataList.add(new Contacts(1, "叶桂", null));
        mContactsDataList.add(new Contacts(1, "温凯文", null));
        mContactsDataList.add(new Contacts(1, "周子翔", null));
        mContactsDataList.add(new Contacts(1, "11程瑞", null));
        mContactsDataList.add(new Contacts(1, "程瑞", null));
        mContactsDataList.add(new Contacts(1, "12陈招子", null));
        mContactsDataList.add(new Contacts(1, "陈招子", null));
        mContactsDataList.add(new Contacts(1, "啊兄", null));
        mContactsDataList.add(new Contacts(1, "包", null));
        mContactsDataList.add(new Contacts(1, "包 子", null));
        mContactsDataList.add(new Contacts(1, "蔡士林", null));
        mContactsDataList.add(new Contacts(1, "呆呆", null));
        mContactsDataList.add(new Contacts(1, "纷纷", null));
        mContactsDataList.add(new Contacts(1, "郭嘉", null));
        mContactsDataList.add(new Contacts(1, "呵呵", null));
        mContactsDataList.add(new Contacts(1, "桂桂", null));
        mContactsDataList.add(new Contacts(1, "黄一航", null));
        mContactsDataList.add(new Contacts(1, "机场大巴", null));
        mContactsDataList.add(new Contacts(1, "家", null));
        mContactsDataList.add(new Contacts(1, "李小龙", null));
        mContactsDataList.add(new Contacts(1, "妈妈", null));
        mContactsDataList.add(new Contacts(1, "牛牛", null));
        mContactsDataList.add(new Contacts(1, "vivo", null));
        mContactsDataList.add(new Contacts(1, "vivo大大", null));
        mContactsAdapter = new ContactsListAdapter(this, mContactsDataList);

        List<String> phoneList = Arrays.asList(new String[]{"18729900796","18565656939"});
        String json;
//        mContactsDataList.get(0).setPhoneNumber(phoneList);
        for(Contacts contacts : mContactsDataList){
            contacts.setPhoneNumber(phoneList);
            ContactsUtils.updateSortKey(contacts);
            json = JsonUtils.toJson(contacts);
//            phoneList = JsonUtils.listFromJson(json,String.class);
        }

        FileUtils.getInstance().saveContacts(mContactsDataList);

    }


}
