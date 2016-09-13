package com.yetwish.contactsdemo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.yetwish.contactsdemo.ApiCallback;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtils;
import com.yetwish.contactsdemo.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装与database交互的方法 CRUD  todo 更新和添加可以合并 放到dbThread中执行
 * Created by yetwish on 2016/9/8.
 */
public class DbContactsManager {

    private static DbContactsManager sInstance;

    public static DbContactsManager getInstance() {
        if (sInstance == null) {
            synchronized (DbContactsManager.class) {
                if (sInstance == null) {
                    sInstance = new DbContactsManager();
                }
            }
        }
        return sInstance;
    }

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDb;
    private Handler mDbHandler;
    private HandlerThread mDbThread;

    private Handler mMainHandler;

    private DbContactsManager() {
    }

    public void init(Context context) {
        mHelper = new DatabaseHelper(context);
        mDb = mHelper.getWritableDatabase();
        if (mDbThread == null) {
            mDbThread = new HandlerThread("Db-Thread");
            mDbThread.start();
            mDbHandler = new Handler(mDbThread.getLooper());
        }
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 查询/获取联系人列表
     *
     * @return
     */
    public void query(@NonNull final ApiCallback<List<Contacts>> callback) {
        final List<Contacts> contactsList = new ArrayList<>();
        mDbHandler.post(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                try {
                    cursor = query(null, null, null, null); //检索整个表格
                    if (cursor == null) {
                        callback.onFailed("Something wrong");
                        return;
                    }
                    cursor.moveToFirst();
                    while (cursor.moveToNext()) {
                        Contacts contacts = new Contacts();
                        contacts.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
                        contacts.setPhoneNumber(JsonUtils.listFromJson(cursor.getString(cursor.getColumnIndex("phoneNumber")), String.class));
                        contacts.setSortKey(cursor.getString(cursor.getColumnIndex("sortKey")));
                        contacts.setSearchKey(cursor.getString(cursor.getColumnIndex("searchKey")));
                        contacts.setFirst(false);
                        contactsList.add(contacts);
                    }
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(contactsList);
                        }
                    });
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        });
    }


    public void query(final int id, @NonNull final ApiCallback<Contacts> callback) {
        if (callback == null)
            throw new IllegalArgumentException("callback cannot be null");
        mDbHandler.post(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = null;
                try {
                    cursor = query(id, null, null); //检索单条数据
                    if (cursor == null) {
                        callback.onFailed("Something wrong");
                        return;
                    }
                    cursor.moveToFirst();
                    final Contacts contacts = new Contacts();
                    if (cursor.moveToNext()) {
                        contacts.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                        contacts.setName(cursor.getString(cursor.getColumnIndex("name")));
                        contacts.setPhoneNumber(JsonUtils.listFromJson(cursor.getString(cursor.getColumnIndex("phoneNumber")), String.class));
                        contacts.setSortKey(cursor.getString(cursor.getColumnIndex("sortKey")));
                        contacts.setSearchKey(cursor.getString(cursor.getColumnIndex("searchKey")));
                        contacts.setFirst(false);
                    }
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(contacts);
                        }
                    });
                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }
        });
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

        return query(projection, "_id = ?", new String[]{String.valueOf(id)}, sortOrder);
    }

    /**
     * 搜索匹配
     *
     * @param searchKey
     * @param projection
     * @param sortOrder
     * @return
     */
    public Cursor query(String searchKey, String[] projection, String sortOrder) {

        return query(projection, "searchKey like ? or sortKey like ? or phoneNumber like ? or name like ?",
                new String[]{"%" + searchKey + "%", "%" + searchKey + "%", "%" + searchKey + "%", "%" + searchKey + "%"}, sortOrder);
    }

    /**
     * 添加一条记录
     *
     * @param contacts
     * @return newInsertId  todo long? int
     */
    public long insert(Contacts contacts) {
//        mDb.execSQL("INSERT INTO contacts VALUES(null, ?, ?, ?)",
//                new Object[]{contacts.getName(), ContactsUtils.toJson(contacts.getPhoneNumber()), contacts.getSortKey()});
        //获取sortKey
        ContactsUtils.updateSortKey(contacts);
        ContentValues values = new ContentValues();
        values.put("name", contacts.getName());
        values.put("phoneNumber", JsonUtils.toJson(contacts.getPhoneNumber()));
        values.put("sortKey", contacts.getSortKey());
        values.put("searchKey", contacts.getSearchKey());
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
        //更新sortKey
        ContactsUtils.updateSortKey(contacts);
        ContentValues values = new ContentValues();
        values.put("name", contacts.getName());
        values.put("phoneNumber", JsonUtils.toJson(contacts.getPhoneNumber()));
        values.put("sortKey", contacts.getSortKey());
        values.put("searchKey", contacts.getSearchKey());
        return update(contacts.getId(), values);
    }

    /**
     * 更新一条记录
     *
     * @param id
     * @param values
     * @return updateRow
     */
    public int update(int id, ContentValues values) {
        return update(values, "_id = ?", new String[]{String.valueOf(id)});
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
        return delete("_id = ?", new String[]{String.valueOf(id)});
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
        if (mDb != null)
            mDb.close();
        if (mDbThread != null) {
            mDbThread.quit();
            mDbHandler = null;
            mDbThread = null;
        }
    }


}
