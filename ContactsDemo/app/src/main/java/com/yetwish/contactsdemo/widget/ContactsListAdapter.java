package com.yetwish.contactsdemo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.widget.indexer.ContactsSectionIndexer;
import com.yetwish.contactsdemo.widget.indexer.ISectionIndexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 联系人列表adapter 代理 todo 编辑
 * Created by yetwish on 2016/9/6.
 */
public class ContactsListAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private List<Contacts> mData;
    private ISectionIndexer mIndexer;
    private Map<Integer, Boolean> mCbMap; //position -> isChecked
    private boolean mCbVisible = false;

    public ContactsListAdapter(Context context, List<Contacts> data) {
        this.mContext = context;
        this.mData = data;
        this.mIndexer = new ContactsSectionIndexer(mData);
        this.mCbMap = new HashMap<>();
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
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_list, null);
            holder.tvSections = (TextView) convertView.findViewById(R.id.tvContactsSections);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cbContactsItem);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvContactsItem);
            holder.line = convertView.findViewById(R.id.lineItem);
            convertView.setTag(holder);
            holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCbMap.put(position, isChecked);
                }
            });
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(mData.get(position).getName());
        if (mData.get(position).isFirst()) {
            holder.tvSections.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.GONE);
            holder.tvSections.setText((mData.get(position).getSortKey().substring(0, 1)).toUpperCase());
        } else {
            holder.tvSections.setVisibility(View.GONE);
            holder.line.setVisibility(View.VISIBLE);
        }

        if (mCbVisible) {
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.cbSelect.setChecked(mCbMap.get(position));
        } else
            holder.cbSelect.setVisibility(View.GONE);
        return convertView;
    }

    public void setCbVisible(boolean visible) {
        if (mCbVisible != visible) {
            mCbVisible = visible;
            this.notifyDataSetInvalidated(); //需要重新getView todo
        }
    }

    public List<Contacts> getCheckedContacts() {
        List<Contacts> checkedContacts = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : mCbMap.entrySet()) {
            if (entry.getValue()) {
                checkedContacts.add(mData.get(entry.getKey()));
            }
        }
        return checkedContacts;
    }

    @Override
    public String[] getSections() {
        return (String[]) mIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mIndexer.getPositionForSection(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return mIndexer.getSectionForPosition(position);
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mIndexer.notifyDataChanged();
    }

    private class ViewHolder {
        TextView tvSections;
        CheckBox cbSelect;
        TextView tvName;
        View line;
    }
}
