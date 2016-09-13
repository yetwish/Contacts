package com.yetwish.contactsdemo.widget.search;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.yetwish.contactsdemo.R;
import com.yetwish.contactsdemo.database.DbContactsManager;
import com.yetwish.contactsdemo.model.Contacts;
import com.yetwish.contactsdemo.utils.ContactsUtils;
import com.yetwish.contactsdemo.utils.JsonUtils;

import java.util.List;

/**
 * search adapter
 * Created by yetwish on 2016/9/13.
 */
public class SearchAdapter extends CursorAdapter {

    private static final int TYPE_NONE = 0;
    private static final int TYPE_NUMBER = 1;
    private static final int TYPE_LETTERS = 2;
    private static final int TYPE_ONE_LETTER = 3;

    private String mQueryKey;
    private String mActualQueryKey;
    private String mLikeQueryKey;
    private int mQueryType = TYPE_NONE;

    public SearchAdapter(Context context) {
        this(context, null);
    }

    public SearchAdapter(Context context, Cursor c) {
        this(context, c, true);
    }

    public SearchAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public void startQuery(String queryKey) {
        mQueryKey = queryKey;
        mActualQueryKey = ContactsUtils.getActualQueryKey(mQueryKey);
        mLikeQueryKey = "%" + mActualQueryKey + "%";
        Cursor cursor = null;
        if (mActualQueryKey.length() < 1) mQueryType = TYPE_NONE;
        else if (mActualQueryKey.length() == 1 && !ContactsUtils.isNumber(mActualQueryKey.charAt(0))) { //只有一个字符，且该字符不是数字，只匹配searchKey和name
            mQueryType = TYPE_ONE_LETTER;
            cursor = queryOneLetterKey();
        } else if (ContactsUtils.hasLetterOrChinese(mActualQueryKey)) { //多个（>1）字符且含有字母/汉字时,不匹配phoneNumber
            mQueryType = TYPE_LETTERS;
            cursor = queryLettersKey();
        } else { //queryKey为数字时,匹配phoneNumber
            mQueryType = TYPE_NUMBER;
            cursor = queryNumberKey();
        }
        changeCursor(cursor);
    }

    public Cursor queryNumberKey() {
        return DbContactsManager.getInstance().query(null, " name like ? or sortKey like ? or phoneNumber like ?",
                new String[]{mLikeQueryKey, mLikeQueryKey, mLikeQueryKey}, null);
    }

    public Cursor queryOneLetterKey() {
        return DbContactsManager.getInstance().query(null, "searchKey like ? or name like ?",
                new String[]{mLikeQueryKey, mLikeQueryKey}, null);
    }

    public Cursor queryLettersKey() {
        return DbContactsManager.getInstance().query(null, "searchKey like ? or sortKey like ? or name like ?",
                new String[]{mLikeQueryKey, mLikeQueryKey, mLikeQueryKey}, null);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        holder.tvName = (TextView) convertView.findViewById(R.id.tvSearchName);
        holder.tvPhone = (TextView) convertView.findViewById(R.id.tvSearchPhone);

        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        if (cursor == null) return;
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.tvName.setText(cursor.getString(cursor.getColumnIndex("name")));
        String phoneJson = cursor.getString(cursor.getColumnIndex("phoneNumber"));
        if (!phoneJson.contains(mActualQueryKey)) { //phoneNumber无匹配到则不显示phoneNumber
            holder.tvPhone.setVisibility(View.GONE);
            return;
        }
        holder.tvPhone.setVisibility(View.VISIBLE);
        List<String> phoneList = JsonUtils.listFromJson(phoneJson, String.class);
        StringBuilder sb = new StringBuilder();
        for (String phone : phoneList) {
            if (phone.contains(mActualQueryKey)) {
                sb.append(phone);
                sb.append("\t\t");
            }
        }
        holder.tvPhone.setText(sb.toString());
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvPhone;
    }
}
