package com.yetwish.contactsdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装与database交互的方法 CRUD  todo 更新和添加可以合并 放到dbThread中执行
 * Created by yetwish on 2016/9/8.
 */
public class DbContactsManager {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDb;

    public DbContactsManager(Context context) {
        mHelper = new DatabaseHelper(context);
        mDb = mHelper.getWritableDatabase();
    }

    /**
     * 查询/获取联系人列表
     *
     * @return
     */
    public List<Contacts> query() {
        List<Contacts> contactsList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = query(null, null, null, null); //检索整个表格
            if (cursor == null) return null; //todo 是否需要进行null判定
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                Contacts contacts = new Contacts();
                contacts.setId(cursor.getInt(cursor.getColumnIndex("id")));
                contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
                contacts.setPhoneNumber(ContactsUtil.convertJson2List(cursor.getString(cursor.getColumnIndex("phoneNumber"))));
                contacts.setSortedKey(cursor.getString(cursor.getColumnIndex("sortedKey")));
                contacts.setFirst(false);
                contactsList.add(contacts);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return contactsList;
    }

    /**
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return
     */
    public Cursor query(String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return mDb.query("contacts", projection, selection, selectionArgs, null, null, sortOrder);
    }


    /**
     * @param id
     * @param projection
     * @param sortOrder
     * @return
     */
    public Cursor query(int id, String[] projection, String sortOrder) {

        return query(projection, "id = ?", new String[]{String.valueOf(id)}, sortOrder);
    }

    /**
     * 添加一条记录
     *
     * @param contacts
     * @return newInsertId  todo long? int
     */
    public long insert(Contacts contacts) {
//        mDb.execSQL("INSERT INTO contacts VALUES(null, ?, ?, ?)",
//                new Object[]{contacts.getName(), ContactsUtil.convertList2Jsop(contacts.getPhoneNumber()), contacts.getSortedKey()});
        ContentValues values = new ContentValues();
        values.put("name", contacts.getName());
        values.put("phoneNumber", ContactsUtil.convertList2Jsop(contacts.getPhoneNumber()));
        values.put("sortedKey", contacts.getSortedKey());
        return insert(null, values);
    }

    /**
     * 添加一条记录
     *
     * @param nullColumnHack
     * @param values
     * @return
     */
    public long insert(String nullColumnHack, ContentValues values) {
        return mDb.insert("contacts", nullColumnHack, values);
    }

    /**
     * 添加多条记录
     *
     * @param contactsList
     */
    public void insert(List<Contacts> contactsList) {
        mDb.beginTransaction();
        try {
            for (Contacts contacts : contactsList) {
                insert(contacts);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }

    /**
     * 更新一条记录
     *
     * @param contacts
     * @return updateRow
     */
    public int update(Contacts contacts) {
        ContentValues values = new ContentValues();
        values.put("name", contacts.getName());
        values.put("phoneNumber", ContactsUtil.convertList2Jsop(contacts.getPhoneNumber()));
        values.put("sortedKey", contacts.getSortedKey());
        return update(contacts.getId(), values);
    }

    /**
     * 更新一条记录
     * @param id
     * @param values
     * @return updateRow
     */
    public int update(int id, ContentValues values) {
        return update(values, "id = ?", new String[]{String.valueOf(id)});
    }

    /**
     * 根据where 更新记录
     *
     * @param values
     * @param selection
     * @param selectionArgs
     * @return updateRoｗ　todo 多条？
     */
    public int update(ContentValues values, String selection, String[] selectionArgs) {
        return mDb.update("contacts", values, selection, selectionArgs);
    }

    /**
     * 根据删除一条记录
     *
     * @param id
     * @return deletedRow
     */
    public int delete(int id) {
        return delete("id = ?", new String[]{String.valueOf(id)});
    }

    /**
     * 根据where条件 删除记录  todo 多条？ deletedRow是啥
     *
     * @param selection
     * @param selectionArgs
     * @return deletedRow
     */
    public int delete(String selection, String[] selectionArgs) {
        return mDb.delete("contacts", selection, selectionArgs);
    }

    /**
     * 关闭database
     */
    public void closeDb() {
        mDb.close();
    }


}
