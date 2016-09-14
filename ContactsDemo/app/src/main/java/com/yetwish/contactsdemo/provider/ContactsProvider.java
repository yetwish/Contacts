package com.yetwish.contactsdemo.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.yetwish.contactsdemo.database.DbContactsManager;

/**
 * 联系人content provider todo 应该将数据与应用放在两个project中
 * Created by yetwish on 2016/9/8.
 */
public class ContactsProvider extends ContentProvider {

    private static UriMatcher sUriMatcher;

    public static final int CODE_CONTACTS_DIR = 0;
    public static final int CODE_CONTACTS_ITEM = 1;

    public static final String AUTHORITY = "com.yetwish.contactsdemo.provider";

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "contacts", CODE_CONTACTS_DIR);
        sUriMatcher.addURI(AUTHORITY, "contacts/#", CODE_CONTACTS_ITEM);
    }

    private DbContactsManager mDbManager;

    @Override
    public boolean onCreate() {
        if (mDbManager == null) { //todo? provider创建时间比application#oncreate()调用早
            mDbManager = DbContactsManager.getInstance();
            mDbManager.init(getContext().getApplicationContext());
        }
        return true;
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_CONTACTS_DIR:
                cursor = mDbManager.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_CONTACTS_ITEM:
                int id = Integer.parseInt(uri.getPathSegments().get(1));
                cursor = mDbManager.query(id, projection, sortOrder);
                break;
        }
        return cursor;
    }

    // TODO: 2016/9/8 Update Uri应该是dir 还是item？
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri uriReturn = null;
        if (sUriMatcher.match(uri) == CODE_CONTACTS_DIR) {
            long newRowId = mDbManager.insert(null, values);
            uriReturn = Uri.parse("content://" + AUTHORITY + "/contacts/" + newRowId);
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRow = 0;
        switch (sUriMatcher.match(uri)) {
            case CODE_CONTACTS_DIR:
                deletedRow = mDbManager.delete(selection, selectionArgs);
                break;
            case CODE_CONTACTS_ITEM:
                int id = Integer.parseInt(uri.getPathSegments().get(1));
                deletedRow = mDbManager.delete(id);
                break;
        }
        return deletedRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updateRows = 0;
        switch (sUriMatcher.match(uri)) {
            case CODE_CONTACTS_DIR:
                updateRows = mDbManager.update(values, selection, selectionArgs);
                break;
            case CODE_CONTACTS_ITEM:
                int id = Integer.parseInt(uri.getPathSegments().get(1));
                updateRows = mDbManager.update(id, values);
                break;
        }
        return updateRows;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CODE_CONTACTS_DIR:
                return "vnd.android.cursor.dir/vnd." + AUTHORITY + ".contacts";
            case CODE_CONTACTS_ITEM:
                return "vnd.android.cursor.item/vnd." + AUTHORITY + ".contacts";
        }
        return null;
    }

}
