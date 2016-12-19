package com.github.ahmadnemati.wind;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by َAhmad Nemati on 12/19/2016.
 */

public class WindView extends View {
    private int pressureTextY;
    private int labelFontSize;
    private int numericFontSize;
    private float barometerTickSpacing;
    private int bigPoleX;
    private int smallPoleX;
    private int poleBottomY;
    private int windTextX;
    private int windTextY;
    private float pressure;
    private int scale;
    private boolean animationEnable = true;   //start at first time
    public WindView(Context context) {
        super(context);
        setupView();
    }

    public WindView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WindView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WindView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }
    private void init(AttributeSet attrs)
    {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.WindView);
        pressure = obtainStyledAttributes.getFloat(R.styleable.WindView_pressure, -1.0f);
        windTextX = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_windTextX, 242);
        windTextY = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_windTextY, 44);
        pressureTextY = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_pressureTextY, 8);
        labelFontSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_labelFontSize, 18);
        numericFontSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_numericFontSize, 14);
        barometerTickSpacing = (float) obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_barometerTickSpacing, 9);
        bigPoleX = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_bigPoleX, 48);
        smallPoleX = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_smallPoleX, 98);
        poleBottomY = obtainStyledAttributes.getDimensionPixelSize(R.styleable.WindView_poleBottomY, 118);
        scale = getScale(20);
        obtainStyledAttributes.recycle();
        setupView();
    }

    private void setupView()
    {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void start() {
        this.animationEnable = true;
        invalidate();
    }
    private int getScale(int i) {
        return (int) ((getContext().getResources().getDisplayMetrics().density * ((float) i)) + 0.5f);
    }
}
