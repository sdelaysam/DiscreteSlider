package com.etiennelawlor.discreteslider.library.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.etiennelawlor.discreteslider.library.utilities.DisplayUtility;

/**
 * Created by etiennelawlor on 7/3/16.
 */

public class DiscreteSliderBackdrop extends FrameLayout {
    
    // region Member Variables
    private Paint fillPaint = new Paint();
    private Paint strokePaint = new Paint();
    private int tickMarkCount = 0;
    private float tickMarkRadius = 0f;
    private float horizontalBarThickness = 0f;
    private int backdropFillColor = 0;
    private int backdropStrokeColor = 0;
    private float backdropStrokeWidth = 0f;
    private float backdropLeftMargin = 0f;
    private float backdropRightMargin = 0f;
    // The x-radius of the oval used to round the corners
    private int xRadius = DisplayUtility.dp2px(8);
    // The y-radius of the oval used to round the corners
    private int yRadius = DisplayUtility.dp2px(8);
    // endregion
    private RectF horizontalBarRect = new RectF();

    // region Constructors
    public DiscreteSliderBackdrop(Context context) {
        super(context);
        init(context, null);
    }

    public DiscreteSliderBackdrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiscreteSliderBackdrop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    // endregion

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int interval = (width - (int)(backdropLeftMargin + backdropRightMargin)) / (tickMarkCount-1);

        setUpFillPaint();
        setUpStrokePaint();

        if (horizontalBarThickness > 0f) {
            horizontalBarRect.set(backdropLeftMargin,
                    (height/2) - (horizontalBarThickness/2),
                    width - backdropRightMargin,
                    (height/2) + (horizontalBarThickness/2));
            canvas.drawRoundRect(horizontalBarRect, xRadius, yRadius, fillPaint);
            canvas.drawRoundRect(horizontalBarRect, xRadius, yRadius, strokePaint);
        } else {
            canvas.drawLine(backdropLeftMargin,
                    (height/2) - 0.5f,
                    width - backdropRightMargin,
                    (height/2) + 0.5f,
                    strokePaint);
        }

        for(int i=0; i<tickMarkCount; i++){
            canvas.drawCircle(backdropLeftMargin + (i * interval), height/2, tickMarkRadius, fillPaint);
            canvas.drawCircle(backdropLeftMargin + (i * interval), height/2, tickMarkRadius, strokePaint);
        }

        if (horizontalBarThickness > 0f) {
            horizontalBarRect.set(backdropLeftMargin,
                    (height/2) - ((horizontalBarThickness/2)-DisplayUtility.dp2px(1)),
                    width - backdropRightMargin,
                    (height/2) + ((horizontalBarThickness/2)-DisplayUtility.dp2px(1)));
            canvas.drawRoundRect(horizontalBarRect, xRadius, yRadius, fillPaint);
        }
    }

    // region Helper Methods
    private void init(Context context, AttributeSet attrs){
    }

    private void setUpFillPaint(){
        fillPaint.setColor(backdropFillColor);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
    }

    private void setUpStrokePaint(){
        strokePaint.setColor(backdropStrokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(backdropStrokeWidth);
    }

    public void setTickMarkCount(int tickMarkCount) {
        this.tickMarkCount = tickMarkCount < 2 ? 2 : tickMarkCount;
    }

    public void setTickMarkRadius(float tickMarkRadius) {
        this.tickMarkRadius = tickMarkRadius < 2.0f ? 2.0f : tickMarkRadius;
    }

    public void setHorizontalBarThickness(float horizontalBarThickness) {
        this.horizontalBarThickness = horizontalBarThickness < 0.0f ? 0.0f : horizontalBarThickness;
    }

    public void setBackdropFillColor(int backdropFillColor) {
        this.backdropFillColor = backdropFillColor;
    }

    public void setBackdropStrokeColor(int backdropStrokeColor) {
        this.backdropStrokeColor = backdropStrokeColor;
    }

    public void setBackdropStrokeWidth(float backdropStrokeWidth) {
        this.backdropStrokeWidth = backdropStrokeWidth < 1.0f ? 1.0f : backdropStrokeWidth;
    }

    public void setBackdropLeftMargin(float backdropLeftMargin) {
        this.backdropLeftMargin = backdropLeftMargin;
    }

    public void setBackdropRightMargin(float backdropRightMargin) {
        this.backdropRightMargin = backdropRightMargin;
    }

    // endregion
}
