package com.medical.product.helpingFile;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RectangularFrameLayout extends FrameLayout {

    public RectangularFrameLayout(Context context) {
        super(context);
    }


    public RectangularFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RectangularFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // Here note that the height is width/2

    @Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, (heightMeasureSpec));
        int width = getMeasuredWidth();
        int height=getMeasuredHeight();

        //int size = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, (height));
    }
}