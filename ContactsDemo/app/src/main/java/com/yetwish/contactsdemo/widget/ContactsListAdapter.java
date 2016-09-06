package com.yetwish.contactsdemo.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yetwish.contactsdemo.vo.Contacts;

import java.util.List;

/**
 * 联系人列表adapter
 * Created by yetwish on 2016/9/6.
 */
public class ContactsListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Contacts> mData;

    public ContactsListAdapter(Context context, List<Contacts> data){
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return null;
    }
}
