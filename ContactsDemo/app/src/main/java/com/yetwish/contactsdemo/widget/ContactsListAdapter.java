package com.yetwish.contactsdemo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.widget.indexer.ContactsSectionIndexer;
import com.yetwish.contactsdemo.widget.indexer.ISectionIndexer;

import java.util.List;

/**
 * 联系人列表adapter  todo cb  adapter与sectionIndexer 装饰者模式
 * Created by yetwish on 2016/9/6.
 */
public class ContactsListAdapter extends BaseAdapter implements ISectionIndexer<Contacts>{

    private Context mContext;
    private List<Contacts> mData;
    private ISectionIndexer mIndexer;

    public ContactsListAdapter(Context context, List<Contacts> data) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_list, null);
            holder.tvSections = (TextView) convertView.findViewById(R.id.tvContactsSections);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cbContactsItem);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvContactsItem);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(mData.get(position).getName());
        if (mData.get(position).isFirst())
            holder.tvSections.setVisibility(View.VISIBLE);
        else
            holder.tvSections.setVisibility(View.GONE);
        return convertView;
    }

    public void setSectionIndexer(ISectionIndexer sectionIndexer){
        mIndexer = sectionIndexer;
    }

    @Override
    public String[] getSections() {
        checkSectionIndexerNotNull();
        return mIndexer.getSections();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        checkSectionIndexerNotNull();
        return mIndexer.getPositionForSection(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        checkSectionIndexerNotNull();
        return mIndexer.getSectionForPosition(position);
    }

    //todo
    @Override
    public void notifyDataChanged(List<Contacts> contacts) {
    }

    private void checkSectionIndexerNotNull(){
        if(mIndexer == null){
            mIndexer = new ContactsSectionIndexer(mData);
        }
    }

    //todo
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    private class ViewHolder {
        TextView tvSections;
        CheckBox cbSelect;
        TextView tvName;
    }
}
