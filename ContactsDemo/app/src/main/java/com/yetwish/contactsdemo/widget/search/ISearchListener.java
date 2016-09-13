package com.yetwish.contactsdemo.widget.search;

/**
 * SearchView 回调接口
 * Created by yetwish on 2016/9/13.
 */
public interface ISearchListener {

    public static final int NULL = -1;

    public void onSearch(CharSequence input);

    public void onQuitSearch(int id);

}
