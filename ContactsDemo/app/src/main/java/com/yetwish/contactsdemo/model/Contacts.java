package com.yetwish.contactsdemo.model;

import com.yetwish.contactsdemo.utils.ContactsUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 联系人数据类
 * Created by yetwish on 2016/9/6.
 */
public class Contacts implements Comparable<Contacts>, Serializable {

    private int id;
    private String name;
    private List<String> phoneNumber;
    private String sortKey;
    private boolean isFirst;
    private String searchKey;

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
        this.name = ContactsUtils.getActualName(name);
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public int compareTo(Contacts another) {
        return this.sortKey.compareTo(another.sortKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Contacts))
            return false;
        Contacts contacts = (Contacts) obj;
        if (name == null || contacts.getName() == null)
            throw new IllegalArgumentException("the name of contacts cannot be null!");
        if (!name.equals(contacts.getName()))
            return false;
        if (phoneNumber == null && contacts.getPhoneNumber() != null || phoneNumber != null && contacts.getPhoneNumber() == null)
            return false;
        return phoneNumber != null && contacts.getPhoneNumber() != null && phoneNumber.equals(contacts.getPhoneNumber());
    }

}
