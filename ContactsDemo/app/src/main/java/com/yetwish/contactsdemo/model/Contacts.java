package com.yetwish.contactsdemo.model;

import java.util.List;

/**
 * 联系人数据类
 * Created by yetwish on 2016/9/6.
 */
public class Contacts implements Comparable<Contacts> {

    private int id;
    private String name;
    private List<String> phoneNumber;
    private String sortKey;
    private boolean isFirst;

    public Contacts() {
    }

    public Contacts(int id, String name, List<String> phoneNumber, String sortedKey, boolean isFirst) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.sortKey = sortedKey;
        this.isFirst = isFirst;
    }

    public Contacts(int id, String name, List<String> phoneNumber) {
        this(id, name, phoneNumber, null, false);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    public int compareTo(Contacts another) {
        return this.sortKey.compareTo(another.sortKey);
    }
}
