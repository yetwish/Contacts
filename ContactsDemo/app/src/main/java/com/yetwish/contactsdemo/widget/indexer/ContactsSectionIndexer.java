package com.yetwish.contactsdemo.widget.indexer;


import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yetwish.contactsdemo.model.Contacts;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by yetwish on 2016/9/7.
 */
public class ContactsSectionIndexer extends BaseAdapter implements ISectionIndexer<Contacts> {

    //默认索引
    private static final String DEFAULT_SECTIONS[] =
            {"#", "A", "B", "C", "D", "E", "F", "G", "H",
                    "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                    "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private List<Contacts> mContactsList;
    private int mPositions[];
    private String mSections[];

    public ContactsSectionIndexer(List<Contacts> contacts) {
        this(contacts, DEFAULT_SECTIONS);
    }

    public ContactsSectionIndexer(List<Contacts> contacts, String sections[]) {
        if (contacts == null)
            throw new NullPointerException("contacts cannot be null!");
        mContactsList = contacts;
        mSections = sections;
        mPositions = new int[mSections.length];
        updateFirstItemAndPosition();
    }


    @Override
    public String[] getSections() {
        return mSections;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        //根据contacts sortKey
        return mPositions[sectionIndex];
    }


    private void updateFirstItemAndPosition() {
        if (mContactsList.size() <= 1) return;
        //根据sortedKey排序
        Collections.sort(mContactsList);
        //获取第一个preFirstChar,方便循环对比
        char preFirstChar = mContactsList.get(0).getSortedKey().charAt(0);
        mContactsList.get(0).setFirst(true);
        int sectionCounts[] = new int[128]; //记录每个字母对应的contacts个数 128: 字母最大ascii 为122-'z';
        int count = 1;
        for (int i = 1; i < mContactsList.size(); i++) {
            char firstChar = mContactsList.get(i).getSortedKey().charAt(0);
            if (firstChar != preFirstChar) {
                sectionCounts[preFirstChar] = count;
                preFirstChar = firstChar;
                mContactsList.get(i).setFirst(true);
                count = 1;
            } else
                count++;
        }
        sectionCounts[preFirstChar] = count;

        int i = count = 0;
        do {
            count += sectionCounts[mSections[i++].charAt(0)];
            mPositions[i] = count;
        } while (i < mSections.length - 1);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mContactsList.size())
            return -1;
        int index = Arrays.binarySearch(mPositions, position);

        return index >= 0 ? index : -index - 2;
    }


    @Override
    public void notifyDataChanged(List<Contacts> contacts) {
        mContactsList = contacts;
        updateFirstItemAndPosition();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
