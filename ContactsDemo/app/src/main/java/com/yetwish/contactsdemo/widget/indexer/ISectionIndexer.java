package com.yetwish.contactsdemo.widget.indexer;

import android.widget.SectionIndexer;

import com.yetwish.contactsdemo.model.Contacts;

import java.util.List;

/**
 * Created by yetwish on 2016/9/7.
 */
public interface ISectionIndexer<T extends Contacts>  extends SectionIndexer{

    /**
     * 通知数据更新，当添加/删除或修改时，需要更新索引信息
     * @param contacts 联系人数据列表
     */
    public void notifyDataChanged(List<T> contacts);
}
