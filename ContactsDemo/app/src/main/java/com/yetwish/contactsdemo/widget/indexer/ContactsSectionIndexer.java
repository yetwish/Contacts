package com.yetwish.contactsdemo.widget.indexer;


import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 联系人索引表实现类，存储索引信息，提供索引方法
 * Created by yetwish on 2016/9/7.
 */
public class ContactsSectionIndexer implements ISectionIndexer<Contacts> {

    //默认索引表
    private static final String DEFAULT_SECTIONS[] =
            {"#", "A", "B", "C", "D", "E", "F", "G", "H",
                    "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                    "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private List<Contacts> mContactsList; //联系人列表

    private int mPositions[]; //存储每个section对应listView的起始位置

    /**
     * section列表，默认使用{@link ContactsSectionIndexer#DEFAULT_SECTIONS}为索引表
     */
    private String mSections[]; //sections[] 与sectionIndexer强相关，目前算法不通用。无法实现sections[]可定制

//    public ContactsSectionIndexer(List<Contacts> contacts,String[] sections) {}

    public ContactsSectionIndexer(List<Contacts> contacts) {
        if (contacts == null)
            throw new NullPointerException("contacts cannot be null!");
        mContactsList = contacts;
        mSections = DEFAULT_SECTIONS;
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

    /**
     * 数据源已经获取过sortKey
     * 根据联系人列表建立索引
     */
    private void updateFirstItemAndPosition() {
        if (mContactsList.size() <= 1) {
            if (mContactsList.size() == 1)
                mContactsList.get(0).setFirst(true);
            return;
        }
        //根据sortKey排序
        Collections.sort(mContactsList);
        //获取第一个preFirstChar,方便循环对比
        char preFirstChar = mContactsList.get(0).getSortKey().charAt(0);
        mContactsList.get(0).setFirst(true);
        int sectionCounts[] = new int[128]; //记录每个字母对应的contacts个数 128: 字母最大ascii 为122-'z';
        int count = 1;
        for (int i = 1; i < mContactsList.size(); i++) {
            char firstChar = mContactsList.get(i).getSortKey().charAt(0);
            if (firstChar != preFirstChar) {
                sectionCounts[preFirstChar] = count;
                preFirstChar = firstChar;
                mContactsList.get(i).setFirst(true);
                count = 1;
            } else
                count++;
        }
        sectionCounts[preFirstChar] = count;
        //获取positions
        int i = count = 0;
        do {
            count += sectionCounts[mSections[i++].charAt(0)];
            if (sectionCounts[mSections[i].charAt(0)] != 0)
                mPositions[i] = count;
            else
                mPositions[i] = mPositions[i - 1];
        } while (i < mSections.length - 1);
    }

    @Override
    public int getSectionForPosition(int position) {
        if (position < 0 || position >= mContactsList.size())
            return -1;
        int index = Arrays.binarySearch(mPositions, position);

        return index >= 0 ? index : -index - 2;
    }

    // TODO: 2016/9/8 可进行优化，数据改变时只针对改变的数据进行更新
    // 增删改都需要进行更新
    @Override
    public void notifyDataChanged() {
        updateFirstItemAndPosition();
    }

}
