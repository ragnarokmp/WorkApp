package it.mobileprogramming.ragnarok.workapp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import it.mobileprogramming.ragnarok.workapp.R;

/**
 * Copyright (C) 2013- Sage 42 App Sdn Bhd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Corey Scott (corey.scott@sage42.com)
 *
 */
public class CircularProgressBar extends View {
    private static final int DEFAULT_MAX_VALUE = 100;
    private static final float ADJUST_FOR_12_OCLOCK = 270f;

    // properties for the background circle
    private Paint backgroundPaint;

    // properties for the progress circle
    private Paint progressPaint;

    // text properties for the countdown text
    private boolean showText;
    private Paint textPaint;

    // maximum number of points in the circle default is 100
    private int max;

    // current progress between 0 and max
    private int progress;

    // diameter (in dp) of the circle
    private float diameter;

    // margin between circle and edges (default is 4dp)
    // NOTE: you will need to include some margin to account for the stroke width, so min padding is strokeWidth/2
    private int layoutMargin;

    // area to draw the progress arc
    private RectF arcBounds;

    // height taken to draw text with the current settings
    private Rect textBounds;

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        // extract params (if provided)
        final TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.circularProgressBar);

        final float defaultDiameter = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, this.getResources().getDisplayMetrics());
        final float defaultStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, this.getResources().getDisplayMetrics());
        final float defaultMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, this.getResources().getDisplayMetrics());
        final float defaultTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, this.getResources().getDisplayMetrics());

        try {
            final int bgColor = args.getColor(R.styleable.circularProgressBar_bgColor, getResources().getColor(R.color.black_button));
            final int bgStrokeWidth = args.getDimensionPixelSize(R.styleable.circularProgressBar_bgStrokeWidth, (int) defaultStrokeWidth);

            final int progressColor = args.getColor(R.styleable.circularProgressBar_progressColor, getResources().getColor(R.color.accent));
            final int progressStrokeWidth = args.getDimensionPixelSize(R.styleable.circularProgressBar_progressStrokeWidth, (int) defaultStrokeWidth);

            this.showText = true;//args.getBoolean(R.styleable.showText, false);
            final int textSize = args.getDimensionPixelSize(R.styleable.circularProgressBar_android_textSize, (int) defaultTextSize);
            final int textColor = args.getInt(R.styleable.circularProgressBar_android_textColor, getResources().getColor(R.color.icons));

            this.layoutMargin = args.getDimensionPixelSize(R.styleable.circularProgressBar_android_layout_margin, (int) defaultMargin);

            this.max = args.getInt(R.styleable.circularProgressBar_max, DEFAULT_MAX_VALUE);

            this.diameter = args.getDimension(R.styleable.circularProgressBar_diameter, defaultDiameter);

            // create paint settings based on supplied args
            this.backgroundPaint = new Paint();
            this.backgroundPaint.setColor(bgColor);
            this.backgroundPaint.setStyle(Style.STROKE);
            this.backgroundPaint.setAntiAlias(true);
            this.backgroundPaint.setStrokeWidth(bgStrokeWidth);

            this.progressPaint = new Paint();
            this.progressPaint.setColor(progressColor);
            this.progressPaint.setStyle(Style.STROKE);
            this.progressPaint.setAntiAlias(true);
            this.progressPaint.setStrokeWidth(progressStrokeWidth);

            this.textPaint = new Paint();
            this.textPaint.setColor(textColor);
            this.textPaint.setAntiAlias(true);
            this.textPaint.setStyle(Style.STROKE);
            this.textPaint.setTextAlign(Align.CENTER);
            this.textPaint.setTextSize(textSize);
        } finally {
            args.recycle();
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (this.arcBounds == null) {
            // set view bounds for arc drawing
            this.arcBounds = new RectF(this.layoutMargin, this.layoutMargin, this.layoutMargin + this.diameter, this.layoutMargin + this.diameter);
        }

        // draw bg circle in the center
        final float radius = this.diameter / 2;
        final float center = radius + this.layoutMargin;
        canvas.drawCircle(center, center, radius, this.backgroundPaint);

        // draw any progress over the top
        // why is this BigDecimal crap even needed? java why?
        final BigDecimal percentage = BigDecimal.valueOf(this.progress).divide(BigDecimal.valueOf(this.max), 4, RoundingMode.HALF_DOWN);
        final BigDecimal sweepAngle = percentage.multiply(BigDecimal.valueOf(360));

        // bounds are same as the bg circle, so diameter width and height moved in by margin
        canvas.drawArc(this.arcBounds, CircularProgressBar.ADJUST_FOR_12_OCLOCK, sweepAngle.floatValue(), false, this.progressPaint);

        if (this.showText) {
            if (this.textBounds == null) {
                // Reference: http://stackoverflow.com/questions/3654321/measuring-text-height-to-be-drawn-on-canvas-android
                // answer #2
                this.textBounds = new Rect();
                this.textPaint.getTextBounds("0", 0, 1, this.textBounds); //$NON-NLS-1$
            }

            // draw text in the center
            canvas.drawText(String.valueOf(this.progress), center, center + (this.textBounds.height() >> 1), this.textPaint);
        }
    }

    public void setProgress(final int progress) {
        this.progress = progress;

        // force redraw
        this.invalidate();
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        // size will always be diameter + margin on add sides
        final int size = (int) this.diameter + (this.layoutMargin * 2);
        this.setMeasuredDimension(size, size);
    }

    public void setMax(final int max) {
        this.max = max;
    }

    public void setBgColor(final int bgColor) {
        this.backgroundPaint.setColor(bgColor);
    }

    public void setBgStrokeWidth(final int bgStrokeWidth) {
        this.backgroundPaint.setStrokeWidth(bgStrokeWidth);
    }

    public void setProgressColor(final int progressColor) {
        this.progressPaint.setColor(progressColor);
    }

    public void setProgressStrokeWidth(final int progressStrokeWidth) {
        this.progressPaint.setStrokeWidth(progressStrokeWidth);
    }

    public void setShowText(final boolean showText) {
        this.showText = showText;
    }

    public void setTextSize(final int textSize) {
        this.textPaint.setTextSize(textSize);
    }

    public void setTextColor(final int textColor) {
        this.textPaint.setColor(textColor);
    }

    public void setDiameter(final float diameter) {
        this.diameter = diameter;
    }
}
