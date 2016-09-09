package com.yetwish.contactsdemo.utils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.yetwish.contactsdemo.model.Contacts;

/**
 * 帮助contacts数据的构建
 * Created by yetwish on 2016/9/7.
 */
public class ContactsUtils {

    private ContactsUtils() {
        throw new UnsupportedOperationException("contactsUtil cannot be instantiated");
    }


    /**
     * 更新联系人的sortKey字段
     *
     * @param contacts
     */
    public static void updateSortKey(Contacts contacts) {
        StringBuilder sb = new StringBuilder();
        String name = contacts.getName();
        if (!isChineseOrAlpha(name.charAt(0)))
            sb.append("#");
        for (int i = 0; i < name.length(); i++) {
            sb.append(Pinyin.toPinyin(name.charAt(i)));
        }
        contacts.setSortKey(sb.toString());
    }

    /**
     * @param c
     * @return 字符c 是否为汉字或字母
     */
    private static boolean isChineseOrAlpha(char c) {
        if (Pinyin.isChinese(c) || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')
            return true;
        return false;
    }


}
