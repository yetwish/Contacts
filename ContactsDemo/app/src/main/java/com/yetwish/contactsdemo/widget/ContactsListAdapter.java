package com.yetwish.contactsdemo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
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
public class ContactsListAdapter extends BaseAdapter implements SectionIndexer {

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
            holder.line = convertView.findViewById(R.id.lineItem);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(mData.get(position).getName());
        if (mData.get(position).isFirst()) {
            holder.tvSections.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.GONE);
            holder.tvSections.setText((mData.get(position).getSortedKey().substring(0,1)).toUpperCase());
        }
        else{
            holder.tvSections.setVisibility(View.GONE);
            holder.line.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    public void setSectionIndexer(ISectionIndexer indexer) {
        mIndexer = indexer;
    }

    @Override
    public String[] getSections() {
        checkSectionIndexerNotNull();
        return (String[]) mIndexer.getSections();
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


    private void checkSectionIndexerNotNull() {
        if (mIndexer == null) {
            mIndexer = new ContactsSectionIndexer(mData);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(mIndexer != null){
            mIndexer.notifyDataChanged(mData);
        }
    }

    private class ViewHolder {
        TextView tvSections;
        CheckBox cbSelect;
        TextView tvName;
        View line;
    }
}
