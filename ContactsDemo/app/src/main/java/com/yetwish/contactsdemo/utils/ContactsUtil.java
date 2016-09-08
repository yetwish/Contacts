package com.yetwish.contactsdemo.utils;

import com.yetwish.contactsdemo.model.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * 帮助contacts数据的构建
 * Created by yetwish on 2016/9/7.
 */
public class ContactsUtil {

    private ContactsUtil() {
        throw new UnsupportedOperationException("contactsUtil cannot be instantiated");
    }

    /**
     * 将数组转换为json文本
     * @param phoneList
     * @return
     */
    public static String convertList2Jsop(List<String> phoneList) {
        String jsonStr = "";

        return jsonStr;
    }

    /**
     * 将json文本转换为phoneList
     * @param jsonStr
     * @return
     */
    public static List<String> convertJson2List(String jsonStr) {
        List<String> phones = null;

        return phones;
    }

    /**
     * 更新联系人的sortedKey字段
     * @param contacts
     */
    public static void updateSortedKey(Contacts contacts){

    }

}
