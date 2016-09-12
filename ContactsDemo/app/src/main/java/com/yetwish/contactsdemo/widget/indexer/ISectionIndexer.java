package com.yetwish.contactsdemo.widget.indexer;

import android.widget.SectionIndexer;

import com.yetwish.contactsdemo.model.Contacts;

import java.util.List;

/**
 * 联系人索引表接口，继承{@link SectionIndexer}
 * 添加一个方法，当数据方法变化时更新索引信息。
 * Created by yetwish on 2016/9/7.
 */
public interface ISectionIndexer<T extends Contacts>  extends SectionIndexer{

    /**
     * 通知数据更新，当添加/删除或修改时，需要更新索引信息
     */
    public void notifyDataChanged();
}
