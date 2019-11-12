package com.ggg.common.ui.utils.alphabetsindexfastscrollrecycler;

/**
 * Created by MyInnos on 31-01-2017.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import com.ggg.common.R;

public class IndexFastScrollRecyclerView extends RecyclerView {

    private IndexFastScrollRecyclerSection mScroller = null;
    private GestureDetector mGestureDetector = null;

    private boolean mEnabled = true;

    public int setIndexTextSize = 12;
    public float mIndexbarWidth = 20;
    public float mIndexbarMargin = 5;
    public int mPreviewPadding = 5;
    public int mIndexBarCornerRadius = 5;
    public float mIndexBarTransparentValue = (float) 0.6;
    public String mIndexbarBackgroudColor = "#000000";
    public String mIndexbarTextColor = "#FFFFFF";
    public String mIndexbarHighLateTextColor = "#000000";

    public IndexFastScrollRecyclerView(Context context) {
        super(context);
    }

    public IndexFastScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexFastScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        mScroller = new IndexFastScrollRecyclerSection(context, this);

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexFastScrollRecyclerView, 0, 0);

            if (typedArray != null) {
                try {
                    setIndexTextSize = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setIndexTextSize, setIndexTextSize);
                    mIndexbarWidth = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setIndexbarWidth, mIndexbarWidth);
                    mIndexbarMargin = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setIndexbarMargin, mIndexbarMargin);
                    mPreviewPadding = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setPreviewPadding, mPreviewPadding);
                    mIndexBarCornerRadius = typedArray.getInt(R.styleable.IndexFastScrollRecyclerView_setIndexBarCornerRadius, mIndexBarCornerRadius);
                    mIndexBarTransparentValue = typedArray.getFloat(R.styleable.IndexFastScrollRecyclerView_setIndexBarTransparentValue, mIndexBarTransparentValue);

                    if (typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarColor) != null) {
                        mIndexbarBackgroudColor = typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarColor);
                    }

                    if (typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarTextColor) != null) {
                        mIndexbarTextColor = typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarTextColor);
                    }

                    if (typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarHighlightTextColor) != null) {
                        mIndexbarHighLateTextColor = typedArray.getString(R.styleable.IndexFastScrollRecyclerView_setIndexBarHighlightTextColor);
                    }

                } finally {
                    typedArray.recycle();
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Overlay index bar
        if (mScroller != null)
            mScroller.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mEnabled) {
            // Intercept ListView's touch event
            if (mScroller != null && mScroller.onTouchEvent(ev))
                return true;

            if (mGestureDetector == null) {
                mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                            float velocityX, float velocityY) {
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }

                });
            }
            mGestureDetector.onTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mEnabled && mScroller != null && mScroller.contains(ev.getX(), ev.getY()))
            return true;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (mScroller != null)
            mScroller.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * @param value int to set the text size of the index bar
     */
    public void setIndexTextSize(int value) {
        mScroller.setIndexTextSize(value);
    }

    /**
     * @param value float to set the width of the index bar
     */
    public void setIndexbarWidth(float value) {
        mScroller.setIndexbarWidth(value);
    }

    /**
     * @param value float to set the margin of the index bar
     */
    public void setIndexbarMargin(float value) {
        mScroller.setIndexbarMargin(value);
    }

    /**
     * @param value int to set the preview padding
     */
    public void setPreviewPadding(int value) {
        mScroller.setPreviewPadding(value);
    }

    /**
     * @param value int to set the corner radius of the index bar
     */
    public void setIndexBarCornerRadius(int value) {
        mScroller.setIndexBarCornerRadius(value);
    }

    /**
     * @param value float to set the transparency value of the index bar
     */
    public void setIndexBarTransparentValue(float value) {
        mScroller.setIndexBarTransparentValue(value);
    }

    /**
     * @param typeface Typeface to set the typeface of the preview & the index bar
     */
    public void setTypeface(Typeface typeface) {
        mScroller.setTypeface(typeface);
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    public void setIndexBarVisibility(boolean shown) {
        mScroller.setIndexBarVisibility(shown);
        mEnabled = shown;
    }

    /**
     * @param shown boolean to show or hide the preview
     */
    public void setPreviewVisibility(boolean shown) {
        mScroller.setPreviewVisibility(shown);
    }

    /**
     * @param color The color for the index bar
     */
    public void setIndexBarColor(String color) {
        mScroller.setIndexBarColor(color);
    }

    /**
     * @param color The text color for the index bar
     */
    public void setIndexBarTextColor(String color) {
        mScroller.setIndexBarTextColor(color);
    }

    /**
     * @param color The text color for the index bar
     */
    public void setIndexbarHighLateTextColor(String color) {
        mScroller.setIndexBarHighLateTextColor(color);
    }

    /**
     * @param shown boolean to show or hide the index bar
     */
    public void setIndexBarHighLateTextVisibility(boolean shown) {
        mScroller.setIndexBarHighLateTextVisibility(shown);
    }
}