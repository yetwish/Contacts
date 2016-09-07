package com.yetwish.contactsdemo.widget.indexer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.widget.ListView;

import com.yetwish.contactsdemo.R;

/**
 * 右边索引表
 * Created by yetwish on 2016/9/6.
 */
public class IndexScroller implements IIndexScroller {

    private static final String TAG = IndexScroller.class.getSimpleName();

    private ListView mListView;
    private ISectionIndexer mIndexer;
    private String[] mSections;
    private Context mContext;

    private Paint mIndexTextPaint;
    private Paint mIndexBgPaint;
    private RectF mIndexRectF;
    private float mIndexBarHeight;
    private float mIndexBarWidth;
    private float mIndexMarginTop;
    private float mIndexTextHeight;


    private Paint mPreviewTextPaint;
    private Paint mPreviewBgPaint;
    private RectF mPreviewRectF;
    private float mPreviewPadding;
    private float mPreviewSize;
    private float mPreviewTextHeight;

    private float mListViewWidth;
    private float mListViewHeight;

    private boolean isScroll;
    private int mCurIndex = -1;


    public IndexScroller(Context context, ListView listView) {
        this.mContext = context;
        this.mListView = listView;
        initPaint();
        initSizeInfo();
    }

    private void initPaint() {

        mIndexTextPaint = new Paint();
        mIndexTextPaint.setColor(ContextCompat.getColor(mContext, R.color.colorText));
        mIndexTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen.indexer_text_size));

        mPreviewTextPaint = new Paint();
        mPreviewTextPaint.setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        mPreviewTextPaint.setTextSize(mContext.getResources().getDimension(R.dimen.indexer_preview_text_size));

        mIndexBgPaint = new Paint();
        mIndexBgPaint.setAntiAlias(true);
        mIndexBgPaint.setStyle(Paint.Style.FILL);

        mPreviewBgPaint = new Paint(mIndexBgPaint);
        mIndexBgPaint.setColor(ContextCompat.getColor(mContext, R.color.colorWhite));
        mPreviewBgPaint.setColor(ContextCompat.getColor(mContext, R.color.colorBlackAlpha));
    }


    private void initSizeInfo() {

        Paint.FontMetrics fontMetrics = mIndexTextPaint.getFontMetrics();
        mIndexTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mIndexBarWidth = mContext.getResources().getDimension(R.dimen.indexer_width);


        fontMetrics = mPreviewTextPaint.getFontMetrics();
        mPreviewTextHeight = fontMetrics.descent - fontMetrics.ascent;
        mPreviewPadding = mContext.getResources().getDimension(R.dimen.indexer_preview_padding);
        mPreviewSize = mPreviewPadding * 2 + mPreviewTextHeight;
    }

    //todo
    @Override
    public void setSectionIndexer(ISectionIndexer sectionIndexer) {
        mIndexer = sectionIndexer;
        mSections = mIndexer.getSections();
        if (mSections == null)
            throw new NullPointerException("sections cannot be null!");

        mIndexBarHeight = mIndexTextHeight * mSections.length;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScroll = contains(event.getX(), event.getY());
                if (isScroll) {
                    //根据y获取index
                    mCurIndex = obtainIndexByPosition(event.getY());
                    notifyCurIndexChanged();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isScroll) {
                    //move多次调用，当获取到index与mCurIndex不同时才需要通知listView.setSelection()
                    int tempIndex = obtainIndexByPosition(event.getY());
                    if (mCurIndex != tempIndex) {
                        mCurIndex = tempIndex;
                        notifyCurIndexChanged();
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                isScroll = false;
                //motion_up时不占有事件分发，将touch事件分发下去
                break;
        }
        return false;
    }

    private boolean contains(float x, float y) {
        return mIndexRectF != null && x > mIndexRectF.left && y > mIndexRectF.top && y < mIndexRectF.bottom;
    }

    private int obtainIndexByPosition(float y) {
        return Math.round((y - mIndexMarginTop) / mIndexTextHeight);
    }

    private void notifyCurIndexChanged() {
        if (mListView == null || !isScroll)
            return;
        if (mCurIndex < 0 || mCurIndex >= mSections.length)
            return;
        mListView.setSelection(mIndexer.getPositionForSection(mCurIndex));
    }


    @Override
    public void onDraw(Canvas canvas) {
        drawIndexBar(canvas);
        if (mCurIndex != -1 && isScroll) {
            drawPreview(canvas);
        }
    }

    //draw indexBar
    private void drawIndexBar(Canvas canvas) {
        //draw bg
        canvas.drawRect(mIndexRectF, mIndexBgPaint);
        //draw sections
        for (int i = 0; i < mSections.length; i++) {//draw indexes
            canvas.drawText(mSections[i], mIndexRectF.centerX() - mIndexTextPaint.measureText(mSections[i]) / 2,
                    mIndexRectF.top + i * mIndexTextHeight, mIndexTextPaint);
        }
    }

    //draw preview
    private void drawPreview(Canvas canvas) {
        //draw bg
        canvas.drawRect(mPreviewRectF, mPreviewBgPaint);
        //draw preview text
        canvas.drawText(mSections[mCurIndex], mPreviewRectF.centerX() - mPreviewTextPaint.measureText(mSections[mCurIndex]) / 2,
                mPreviewRectF.top + mPreviewPadding, mPreviewTextPaint);
    }

    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        //获取关联的listView的宽高
        mListViewWidth = width;
        mListViewHeight = height;
        //根据listView的宽高 获取indexBar和preview的位置
        mIndexMarginTop = (mListViewHeight - mIndexBarHeight) / 2;
        mIndexRectF = new RectF(mListViewWidth - mIndexBarWidth, mIndexMarginTop, mListViewWidth, mListViewHeight);
        mPreviewRectF = new RectF(mListViewWidth / 2 - mPreviewSize / 2, mListViewHeight / 2 - mPreviewSize / 2,
                mListViewWidth / 2 + mPreviewSize / 2, mListViewHeight / 2 + mPreviewSize / 2);
    }
}
