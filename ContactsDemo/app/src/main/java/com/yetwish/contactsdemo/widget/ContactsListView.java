package com.yetwish.contactsdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.yetwish.contactsdemo.widget.indexer.IIndexScroller;
import com.yetwish.contactsdemo.widget.indexer.IndexScroller;

/**
 * 联系人列表ListView
 * Created by yetwish on 2016/9/3.
 */
public class ContactsListView extends ListView {

    private IIndexScroller mIndexScroller;


    public ContactsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIndexScroller = new IndexScroller(context, this);
        this.setFastScrollEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIndexScroller != null && mIndexScroller.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mIndexScroller != null) {
            mIndexScroller.onDraw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        if (mIndexScroller != null) {
            mIndexScroller.onSizeChanged(width, height, oldWidth, oldHeight);
        }
    }
}
