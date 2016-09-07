package com.yetwish.contactsdemo.widget.indexer;

import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.SectionIndexer;

/**
 * IndexScroller接口，定义IndexScroll需要实现的方法
 * Created by yetwish on 2016/9/6.
 */
public interface IIndexScroller {

    public void setSectionIndexer(SectionIndexer indexer);

    public boolean onTouchEvent(MotionEvent event);

    public void onDraw(Canvas canvas);

    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight);

}
