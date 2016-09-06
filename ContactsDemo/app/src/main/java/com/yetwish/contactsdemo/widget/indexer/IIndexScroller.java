package com.yetwish.contactsdemo.widget.indexer;

import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * IndexScroller接口，定义IndexScroll需要实现的方法
 * Created by yetwish on 2016/9/6.
 */
public interface IIndexScroller {

    boolean onTouchEvent(MotionEvent event);

    void onDraw(Canvas canvas);

    void onSizeChanged(int width, int height, int oldWidth, int oldHeight);

}
