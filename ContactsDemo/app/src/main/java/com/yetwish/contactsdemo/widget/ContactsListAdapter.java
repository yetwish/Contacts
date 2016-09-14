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
        return mData.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contacts_list, null);
            holder.tvSections = (TextView) convertView.findViewById(R.id.tvContactsSections);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cbContactsItem);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvContactsItem);
            holder.lineTop = convertView.findViewById(R.id.lineTop);
            holder.lineBottom = convertView.findViewById(R.id.lineBottom);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.tvName.setText(mData.get(position).getName());
        if (mData.get(position).isFirst()) {
            holder.tvSections.setVisibility(View.VISIBLE);
            holder.lineTop.setVisibility(View.GONE);
            holder.tvSections.setText((mData.get(position).getSortKey().substring(0, 1)).toUpperCase());
        } else {
            holder.tvSections.setVisibility(View.GONE);
            holder.lineTop.setVisibility(View.VISIBLE);
        }

        if (position == getCount() - 1)
            holder.lineBottom.setVisibility(View.VISIBLE);
        else
            holder.lineBottom.setVisibility(View.GONE);


        if (mCbMap.containsKey(position))
            holder.cbSelect.setChecked(mCbMap.get(position));
        else
            holder.cbSelect.setChecked(false);

        if (mCbVisible)
            holder.cbSelect.setVisibility(View.VISIBLE);
        else
            holder.cbSelect.setVisibility(View.GONE);
        return convertView;
    }

    public boolean isCbVisible() {
        return mCbVisible;
    }

    public void setCbVisible(boolean visible) {
        if (mCbVisible != visible) {
            mCbVisible = visible;
            if (!visible) mCbMap.clear();
            this.notifyDataSetInvalidated(); //需要重新getView
        }
    }

    /**
     * todo 优化
     *
     * @param position
     */
    public void onCbClick(int position) {
        if (mCbMap.containsKey(position))
            mCbMap.remove(position);
        else
            mCbMap.put(position, true);
        notifyDataSetInvalidated();
    }

    /**
     * 全选和反选
     */
    public void onAllCbClick() {
        if (mCbMap.size() != mData.size()) {
            for (int i = 0; i < mData.size(); i++) {
                mCbMap.put(i, true);
            }
        } else {
            mCbMap.clear();
        }
        notifyDataSetChanged();
    }

    public List<Contacts> getCheckedContacts() {
        if (mCbMap.size() == 0) return null;
        List<Contacts> checkedContacts = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : mCbMap.entrySet()) {
            checkedContacts.add(mData.get(entry.getKey()));
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
        View lineTop;
        View lineBottom;
    }
}
