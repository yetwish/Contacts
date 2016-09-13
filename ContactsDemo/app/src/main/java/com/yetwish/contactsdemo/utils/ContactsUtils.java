package com.yetwish.contactsdemo.utils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.yetwish.contactsdemo.model.Contacts;

import java.util.NavigableMap;

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
        StringBuilder sortKeyBuilder = new StringBuilder();
        StringBuilder searchKeyBuilder = new StringBuilder();
        String name = contacts.getName();
        if (!isChineseOrLetter(name.charAt(0)))
            sortKeyBuilder.append("#");
        char c;
        boolean toAdd = true;
        for (int i = 0; i < name.length(); i++) {
            c = name.charAt(i);
            if (c == '\0') {//跳过空格
                toAdd = true;
                continue;
            }
            if (Pinyin.isChinese(c)) {
                sortKeyBuilder.append(Pinyin.toPinyin(c));
                searchKeyBuilder.append(Pinyin.toPinyin(c).charAt(0));
            } else {
                sortKeyBuilder.append(Character.toUpperCase(c));
                if (toAdd && isLetter(c)) {
                    toAdd = false;
                    searchKeyBuilder.append(Character.toUpperCase(c));
                }
            }
        }
        contacts.setSortKey(sortKeyBuilder.toString());
        contacts.setSearchKey(searchKeyBuilder.toString());
    }

    /**
     * 剔除名字中的空格符
     *
     * @param name
     * @return
     */
    public static String getActualName(String name) {
        String str = name.trim();
        if (isLetter(str.charAt(0)))//如果以英文开头则不去除
            return str;
        else { //去除名字中间的空格
            StringBuilder sb = new StringBuilder();
            char c;
            for (int i = 0; i < str.length(); i++) {
                if ((c = str.charAt(i)) != '\0')
                    sb.append(c);
            }
            return sb.toString();
        }
    }

    /**
     * 剔除queryKey中的无用字符
     *
     * @param queryKey
     * @return
     */
    public static String getActualQueryKey(String queryKey) {
        StringBuilder sb = new StringBuilder();
        char c;
        for (int i = 0; i < queryKey.length(); i++) {
            c = queryKey.charAt(i);
            if (isChineseOrLetter(c) || isNumber(c)) {
                sb.append(c);
            }
        }
        return sb.toString();

    }

    /**
     * @param c
     * @return 字符c 是否为汉字或字母
     */
    public static boolean isChineseOrLetter(char c) {
        return Pinyin.isChinese(c) || isLetter(c);
    }

    /**
     * 判断字符c 是否是字母
     *
     * @param c
     * @return
     */
    public static boolean isLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean hasLetterOrChinese(String queryKey) {
        for (int i = 0; i < queryKey.length(); i++) {
            if (isChineseOrLetter(queryKey.charAt(i)))
                return true;
        }
        return false;
    }


}
