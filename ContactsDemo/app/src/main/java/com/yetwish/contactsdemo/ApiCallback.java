package com.yetwish.contactsdemo;

/**
 * 回调接口
 * Created by yetwish on 2016/9/11.
 */
public interface ApiCallback<T>{

    /**
     * 数据加载成功
     * @param data
     */
    void onSuccess(T data);


    /**
     * 数据加载失败
     * @param msg
     */
    void onFailed(String msg);
}
