package com.yetwish.contactsdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * todo 如何获取动态的长宽，
 * Created by yetwish on 2016/9/3.
 */
public class IndexScrollerBar extends View{

    public static final String DEFAULT_INDEXS[] =
            {"#", "A", "B", "C", "D", "E", "F", "G", "H",
                    "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                    "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private IndexScrollListener mListener;

    private Paint mIndexTextPaint;
    private Paint mIndexBgPaint;

    private Paint mPreviewTextPaint;
    private Paint mPreviewBgPaint;


    public IndexScrollerBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexScrollerBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initPaint(){


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        return super.onTouchEvent(event);
    }


    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight){


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw indexBar


    }


}
