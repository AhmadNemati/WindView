package com.github.ahmadnemati.wind;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by َAhmad Nemati on 12/19/2016.
 */

public class WindView extends View {
    private static final String TAG = WindView.class.getSimpleName();
    private Rect rect = new Rect();
    private Path path;
    private Matrix matrix;
    private Paint paint;
    private int primaryTextColor;
    private String WindDirectionText;
    private long startTime = 0;
    private PathEffect pathEffect;
    private Bitmap smallPoleBitmap;
    private Bitmap bigPoleBitmap;
    private Bitmap smallBladeBitmap;
    private Bitmap bigBladeBitmap;
    private Bitmap trendBitmap;
    private Bitmap barometerBitmap;
    private float rotation;
    private int bigPoleX;
    private int smallPoleX;
    private String windText = "Wind";
    private String windName;
    private String windSpeedText;
    private String barometerText = "Barometer";
    private int poleBottomY;
    private int windTextX;
    private int windTextY;
    private float windSpeed;
    private String windSpeedUnit;
    private boolean animationEnable = true;
    private boolean animationBaroMeterEnable = false;
    private float pressurePaddingTop;
    private double senterOfPressureLine;
    private double pressureLineSize;
    private int scale;
    private float barometerTickSpacing;
    private double lineSpace;
    private float pressure;
    private String pressureText;
    private float lineSize;
    private float curSize;
    private int pressureTextY;
    private String pressureUnit;
    private int trendType;
    private int labelFontSize;
    private int numericFontSize;
    private Typeface typeface;
    private String empty="--";

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
        scale = getScaleInPixel(20);
        obtainStyledAttributes.recycle();
        setupView();
    }
    private void setupView() {
        Resources resources = getContext().getResources();
        primaryTextColor = resources.getColor(R.color.text_color);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(getTypeFace());
        paint.setStyle(Paint.Style.FILL);
        smallPoleBitmap = BitmapFactory.decodeResource(resources, R.drawable.smallpole);
        bigPoleBitmap = BitmapFactory.decodeResource(resources, R.drawable.bigpole);
        smallBladeBitmap = BitmapFactory.decodeResource(resources, R.drawable.smallblade);
        bigBladeBitmap = BitmapFactory.decodeResource(resources, R.drawable.bigblade);
        barometerBitmap = BitmapFactory.decodeResource(resources, R.drawable.barometer);
        matrix = new Matrix();
        WindDirectionText = "";
        rotation = 0.0f;
        lineSpace = toPixel(1d);
        pressureLineSize = lineSpace + (barometerTickSpacing);
        senterOfPressureLine = (9d * pressureLineSize) + lineSpace;
        pressurePaddingTop = getPaddingTop();
        pathEffect = new DashPathEffect(new float[]{4f, 4f}, 0f);

    }

    public void start() {
        animationEnable = true;
        invalidate();
    }

    public void stop() {
        animationEnable = false;
    }

    public void setWindSpeed(float f) {
        windSpeed = f;
        if (windSpeed >= 36.0f && animationEnable) {
            if (smallBladeBitmap != null) {
                smallBladeBitmap.recycle();
            }
            if (bigBladeBitmap != null) {
                bigBladeBitmap.recycle();
            }
            smallBladeBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.blade_s_blur);
            bigBladeBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.blade_big_blur);
        }
        windSpeedText = formatFloat(windSpeed);
        initSpeedUnit();
        invalidate();
    }

    public void setWindSpeedUnit(String str) {
        windSpeedUnit = str;
        initSpeedUnit();
    }

    public void setWindDirection(String str) {
        WindDirectionText = str;
        initSpeedUnit();
    }

    private void initSpeedUnit() {
        StringBuilder stringBuilder = new StringBuilder();
        if (windSpeed < 0.0f || windSpeedUnit == null) {
            stringBuilder.append(empty);
        } else {
            stringBuilder.append(" ").append(windSpeedUnit).append(" ").append(WindDirectionText);
        }
        windName = stringBuilder.toString();
    }

    public void setPressure(float f) {
        pressure = getscale(f, 2);
        if (pressure < 0.0f) {
            pressureText = empty;
        } else {
            pressureText = formatFloat(pressure);
        }
    }

    public void setPressureUnit(String str) {
        pressureUnit = str;
    }

    public void trendConfig(int i, boolean z) {
        trendType = i;
        if (getHeight() > 0) {
            setupPressureLine(false);
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setupPressureLine(false);
    }


    protected void onDraw(Canvas canvas) {
        boolean enable = false;
        if (bigPoleBitmap != null && smallBladeBitmap != null && bigBladeBitmap != null) {
            paint.setColor(primaryTextColor);
            canvas.drawBitmap(bigPoleBitmap, bigPoleX, (poleBottomY - bigPoleBitmap.getHeight()), paint);
            canvas.drawBitmap(smallPoleBitmap, smallPoleX, (poleBottomY - smallPoleBitmap.getHeight()), paint);
            if (animationEnable) {
                startTimes();
            }
            int width = bigPoleX + (bigPoleBitmap.getWidth() / 2);
            int height = (poleBottomY - bigPoleBitmap.getHeight()) - 4;
            rect.left = width - (bigBladeBitmap.getWidth() / 2);
            rect.top = height - (bigBladeBitmap.getHeight() / 2);
            rect.bottom = (bigBladeBitmap.getHeight() / 2) + height;
            matrix.reset();
            matrix.setRotate(rotation, ((float) bigBladeBitmap.getWidth()) / 2.0f, (bigBladeBitmap.getHeight()) / 2.0f);
            matrix.postTranslate((float) (width - (bigBladeBitmap.getWidth() / 2)), (height - (bigBladeBitmap.getHeight() / 2)));
            canvas.drawBitmap(bigBladeBitmap, matrix, paint);
            width = smallPoleX + (smallPoleBitmap.getWidth() / 2);
            int height2 = (poleBottomY - smallPoleBitmap.getHeight()) - 4;
            rect.right = (smallBladeBitmap.getWidth() / 2) + width;
            rect.bottom = Math.max(rect.bottom, height + (smallBladeBitmap.getHeight() / 2));
            matrix.reset();
            matrix.setRotate(rotation, ((float) smallBladeBitmap.getWidth()) / 2.0f, ((float) smallBladeBitmap.getHeight()) / 2.0f);
            matrix.postTranslate((float) (width - (smallBladeBitmap.getWidth() / 2)), (float) (height2 - (smallBladeBitmap.getHeight() / 2)));
            canvas.drawBitmap(smallBladeBitmap, matrix, paint);
            drawWind(canvas);
            drawBarometer(canvas);
            if (!animationEnable) {
                curSize = lineSize;
            }
            initPath(canvas);
            drawPressure(canvas);
            if (isUpTrend() && curSize < lineSize && animationBaroMeterEnable) {
                curSize += 1.0f;
                enable = true;
            } else if (isDownTrend() && curSize > lineSize && animationBaroMeterEnable) {
                curSize -= 1.0f;
                enable = true;
            } else {
                animationBaroMeterEnable = false;
            }
            if ((animationEnable && windSpeed > 0.0f) || enable) {
                invalidate();
            }
        }
    }

    private boolean isDownTrend() {
        return trendType == 1 || trendType == 4;
    }

    private boolean isUpTrend() {
        return trendType == 0 || trendType == 3;
    }

    private void initPath(Canvas canvas) {
        paint.setColor(1073741823);
        paint.setStrokeWidth(1.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(pathEffect);
        float width = (float) getWidth();
        if (path == null) {
            path = new Path();
        } else {
            path.reset();
        }
        path.moveTo(10.0f, curSize);
        path.quadTo(width / 2.0f, curSize, width - 20.0f, curSize);
        canvas.drawPath(path, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);
        paint.setColor(primaryTextColor);
    }

    private void setupPressureLine(boolean z) {
        int height = getHeight();
        if (height > 0) {
            if ((senterOfPressureLine + ((double) getPaddingTop())) + ((double) getPaddingBottom()) >= ((double) height)) {
                senterOfPressureLine = (double) ((height - getPaddingTop()) - getPaddingBottom());
                pressureLineSize = senterOfPressureLine / 10.0d;
            }
            pressurePaddingTop = (float) (((double) ((height - getPaddingTop()) / 2)) - (senterOfPressureLine / 2.0d));
            float f = (float) ((((double) pressurePaddingTop) + (pressureLineSize * 5.0d)) + lineSpace);
            if (z) {
                lineSize = f;
                curSize = lineSize;
            } else if (isUpTrend()) {
                lineSize = (float) ((((double) pressurePaddingTop) + (pressureLineSize * 6.0d)) + lineSpace);
                if (animationBaroMeterEnable) {
                    curSize = (float) ((((double) pressurePaddingTop) + (pressureLineSize * 4.0d)) + lineSpace);
                } else {
                    curSize = lineSize;
                }
                trendBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trend_falling);
            } else if (isDownTrend()) {
                lineSize = (float) ((((double) pressurePaddingTop) + (pressureLineSize * 5.0d)) + lineSpace);
                if (animationBaroMeterEnable) {
                    curSize = (float) ((((double) pressurePaddingTop) + (pressureLineSize * 7.0d)) + lineSpace);
                } else {
                    curSize = lineSize;
                }
                trendBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trend_rising);
            } else {
                lineSize = f;
                curSize = lineSize;
                if (trendBitmap != null) {
                    trendBitmap.recycle();
                    trendBitmap = null;
                }
            }
        }
    }

    private void drawWind(Canvas canvas) {
        paint.setTextSize((float) labelFontSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);
        float width = (((float) smallPoleX) + (((float) smallBladeBitmap.getWidth()) / 2.0f)) + ((float) windTextX);
        canvas.drawText(windText, width, (float) windTextY, paint);
        if (windSpeed < 0.0f) {
            paint.setTextSize((float) labelFontSize);
            canvas.drawText(windName, width, (float) (windTextY + labelFontSize), paint);
        } else if (!stringValid(windSpeedText)) {
            paint.setTextSize((float) numericFontSize);
            canvas.drawText(windSpeedText, width, (float) (windTextY + numericFontSize), paint);
            if (!stringValid(windName)) {
                paint.setTextSize((float) labelFontSize);
                canvas.drawText(windName, width + (((float) (windSpeedText.length() * numericFontSize)) / 2.0f), (float) (windTextY + numericFontSize), paint);
            }
        }
    }

    private void drawPressure(Canvas canvas) {
        float f = curSize + ((float) pressureTextY);
        int width = (getWidth() - 14) - scale;
        paint.setTextSize((float) labelFontSize);
        float measureText = (float) (width - ((int) paint.measureText(barometerText)));
        f += (float) labelFontSize;
        canvas.drawText(barometerText, measureText, f, paint);
        if (trendBitmap != null) {
            canvas.drawBitmap(trendBitmap, (measureText - ((float) trendBitmap.getWidth())) - 4.0f, f - ((float) (labelFontSize / 2)), paint);
        }
        if (!TextUtils.isEmpty(pressureUnit)) {
            paint.setTextSize((float) labelFontSize);
            measureText = (float) (width - ((int) paint.measureText(pressureUnit)));
            f += (float) numericFontSize;
            canvas.drawText(pressureUnit, measureText, f, paint);
        }
        if (!TextUtils.isEmpty(pressureText)) {
            paint.setTextSize((float) numericFontSize);
            canvas.drawText(pressureText, measureText - (((float) (((int) paint.measureText(pressureText)) + 4)) + ((float) ((int) paint.measureText(" ")))), f, paint);
        }
    }

    private void startTimes() {
        long nanoTime = System.nanoTime();
        float f = ((float) (nanoTime - startTime)) / 1000000.0f;
        startTime = nanoTime;
        rotation = ((float) ((Math.sqrt((double) windSpeed) * ((double) f)) / 20.0d)) + rotation;
        rotation %= 360.0f;
    }

    private void drawBarometer(Canvas canvas) {
        paint.setStrokeWidth(2.0f);
        paint.setColor(1073741823);
        paint.setStyle(Paint.Style.FILL);
        int width = getWidth();
        float f = pressurePaddingTop;
        for (int i = 0; i < 10; i++) {
            canvas.drawLine((float) (((double) width) - toPixel(5d)), f, (float) width, f, paint);
            f = (float) (((double) f) + (lineSpace + ((double) barometerTickSpacing)));
        }
        paint.setColor(primaryTextColor);
    }


    public void animateBaroMeter() {
        if (!animationBaroMeterEnable) {
            animationBaroMeterEnable = true;
            setupPressureLine(false);
            invalidate();
        }
    }


    public void cleanView() {
        if (smallPoleBitmap != null) {
            smallPoleBitmap.recycle();
            smallPoleBitmap = null;
        }
        if (bigPoleBitmap != null) {
            bigPoleBitmap.recycle();
            bigPoleBitmap = null;
        }
        if (smallBladeBitmap != null) {
            smallBladeBitmap.recycle();
            smallBladeBitmap = null;
        }
        if (bigBladeBitmap != null) {
            bigBladeBitmap.recycle();
            bigBladeBitmap = null;
        }
        if (trendBitmap != null) {
            trendBitmap.recycle();
            trendBitmap = null;
        }
        if (barometerBitmap != null) {
            barometerBitmap.recycle();
            barometerBitmap = null;
        }
        clearView(this);
    }

    private int getScaleInPixel(int i) {
        return (int) ((getContext().getResources().getDisplayMetrics().density * ((float) i)) + 0.5f);
    }

    private Typeface getTypeFace() {
        Typeface create = Typeface.create("sans-serif-light", 0);
        if (create == null) {
            return Typeface.DEFAULT;
        }
        return create;
    }

    private double toPixel(double d) {
        return ((double) getContext().getResources().getDisplayMetrics().density) * d;
    }

    private String formatFloat(float f) {
        try {
            return NumberFormat.getInstance().format(f);
        } catch (Exception e) {
            return empty;
        }
    }

    private float getscale(float f, int i) {
        return new BigDecimal(Float.toString(f)).setScale(i, 4).floatValue();
    }

    private boolean stringValid(String str) {
        return str == null || str.trim().length() == 0 || str.trim().equalsIgnoreCase("null");
    }


    private void clearView(View view) {
        if (!(view == null || view.getBackground() == null)) {
            view.getBackground().setCallback(null);
        }
        if (!(view == null || !(view instanceof ImageView) || ((ImageView) view).getDrawable() == null)) {
            ((ImageView) view).getDrawable().setCallback(null);
            ((ImageView) view).setImageDrawable(null);
        }
        if (view != null && (view instanceof ViewGroup)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                clearView(((ViewGroup) view).getChildAt(i));
            }
            try {
                if (!(view instanceof AdapterView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }
    }
}
