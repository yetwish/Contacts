package com.yetwish.contactsdemo.model;

import java.util.List;

/**
 * 联系人数据类
 * Created by yetwish on 2016/9/6.
 */
public class Contacts implements Comparable<Contacts>{

    private long id;
    private String name;
    private List<String> phoneNumber;
    private String sortedKey;
    private boolean isFirst;

    public Contacts() {
    }

    public Contacts(long id, String name, List<String> phoneNumber, String sortedKey, boolean isFirst) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sortedKey = sortedKey;
        this.isFirst = isFirst;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSortedKey() {
        return sortedKey;
    }

    public void setSortedKey(String sortedKey) {
        this.sortedKey = sortedKey;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    public int compareTo(Contacts another) {
        return this.sortedKey.compareTo(another.sortedKey);
    }
}
