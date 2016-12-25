package com.github.ahmadnemati.wind;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.util.TypedValue;
import android.view.View;

import com.github.ahmadnemati.wind.enums.TrendType;

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
    private int textColor;
    private String WindDirectionText;
    private long startTime = 0;
    private PathEffect pathEffect;
    private Bitmap smallPoleBitmap;
    private Bitmap bigPoleBitmap;
    private Bitmap smallBladeBitmap;
    private Bitmap bigBladeBitmap;
    private Bitmap trendBitmap;
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
    private int labelFontSize;
    private int numericFontSize;
    private Typeface typeface;
    private String empty = "--";
    private TrendType trendType = TrendType.UP;
    private int lineColor = Color.WHITE;
    private float lineStrokeWidth = 1f;
    private int barometerColor = Color.WHITE;
    private float barometerStrokeWidth = 2f;


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

    public static String getTAG() {
        return TAG;
    }

    private void init(AttributeSet attrs) {
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
        textColor = resources.getColor(R.color.text_color);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(getTypeface());
        paint.setStyle(Paint.Style.FILL);
        smallPoleBitmap = BitmapFactory.decodeResource(resources, R.drawable.smallpole);
        bigPoleBitmap = BitmapFactory.decodeResource(resources, R.drawable.bigpole);
        smallBladeBitmap = BitmapFactory.decodeResource(resources, R.drawable.smallblade);
        bigBladeBitmap = BitmapFactory.decodeResource(resources, R.drawable.bigblade);
        matrix = new Matrix();
        WindDirectionText = "";
        lineColor = Color.parseColor("#8bece8e8");
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupPressureLine(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initCanvas(canvas);
    }

    private void initCanvas(Canvas canvas) {
        boolean enable = false;
        if (bigPoleBitmap != null && smallBladeBitmap != null && bigBladeBitmap != null) {
            paint.setColor(textColor);
            paint.setTypeface(typeface);
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
            matrix.setRotate(rotation, (bigBladeBitmap.getWidth()) / 2.0f, (bigBladeBitmap.getHeight()) / 2.0f);
            matrix.postTranslate((width - (bigBladeBitmap.getWidth() / 2)), (height - (bigBladeBitmap.getHeight() / 2)));
            canvas.drawBitmap(bigBladeBitmap, matrix, paint);
            width = smallPoleX + (smallPoleBitmap.getWidth() / 2);
            int height2 = (poleBottomY - smallPoleBitmap.getHeight()) - 4;
            rect.right = (smallBladeBitmap.getWidth() / 2) + width;
            rect.bottom = Math.max(rect.bottom, height + (smallBladeBitmap.getHeight() / 2));
            matrix.reset();
            matrix.setRotate(rotation, (smallBladeBitmap.getWidth()) / 2.0f, (smallBladeBitmap.getHeight()) / 2.0f);
            matrix.postTranslate((width - (smallBladeBitmap.getWidth() / 2)), (height2 - (smallBladeBitmap.getHeight() / 2)));
            canvas.drawBitmap(smallBladeBitmap, matrix, paint);
            drawWind(canvas);
            drawBarometer(canvas);
            if (!animationEnable) {
                curSize = lineSize;
            }
            drawLine(canvas);
            drawPressure(canvas);
            if (trendType == TrendType.DOWN && curSize < lineSize && animationBaroMeterEnable) {
                curSize += 1.0f;
                enable = true;
            } else if (trendType == TrendType.UP && curSize > lineSize && animationBaroMeterEnable) {
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

    private void drawLine(Canvas canvas) {
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineStrokeWidth);
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
        paint.setColor(textColor);
    }

    private void setupPressureLine(boolean z) {
        int height = getHeight();
        if (height > 0) {
            if ((senterOfPressureLine + (getPaddingTop())) + (getPaddingBottom()) >= (height)) {
                senterOfPressureLine = ((height - getPaddingTop()) - getPaddingBottom());
                pressureLineSize = senterOfPressureLine / 10.0d;
            }
            pressurePaddingTop = (float) ((((height - getPaddingTop()) / 2)) - (senterOfPressureLine / 2.0d));
            float f = (float) (((pressurePaddingTop) + (pressureLineSize * 5.0d)) + lineSpace);
            if (z) {
                lineSize = f;
                curSize = lineSize;
            } else if (trendType == TrendType.DOWN) {
                lineSize = (float) (((pressurePaddingTop) + (pressureLineSize * 5.0d)) + lineSpace);
                if (animationBaroMeterEnable) {
                    curSize = (float) (((pressurePaddingTop) + (pressureLineSize * 7.0d)) + lineSpace);
                } else {
                    curSize = lineSize;
                }
                trendBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trend_falling);
            } else if (trendType == TrendType.UP) {
                lineSize = (float) (((pressurePaddingTop) + (pressureLineSize * 5.0d)) + lineSpace);
                if (animationBaroMeterEnable) {
                    curSize = (float) (((pressurePaddingTop) + (pressureLineSize * 7.0d)) + lineSpace);
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
        paint.setTextSize(labelFontSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setPathEffect(null);
        float width = (((smallPoleX) + ((smallBladeBitmap.getWidth()) / 2.0f)) + windTextX);
        canvas.drawText(windText, width, windTextY, paint);
        if (windSpeed < 0.0f) {
            paint.setTextSize(labelFontSize);
            canvas.drawText(windName, width, (windTextY + labelFontSize), paint);
        } else if (!stringValid(windSpeedText)) {
            paint.setTextSize(numericFontSize);
            canvas.drawText(windSpeedText, width, (windTextY + numericFontSize), paint);
            if (!stringValid(windName)) {
                paint.setTextSize(labelFontSize);
                canvas.drawText(windName, width + (((windSpeedText.length() * numericFontSize)) / 2.0f), (windTextY + numericFontSize), paint);
            }
        }
    }

    private void drawPressure(Canvas canvas) {
        float f = curSize + (pressureTextY);
        int width = (getWidth() - 14) - scale;
        paint.setTextSize(labelFontSize);
        float measureText = (width - (paint.measureText(barometerText)));
        f += labelFontSize;
        canvas.drawText(barometerText, measureText, f, paint);
        if (trendBitmap != null) {
            canvas.drawBitmap(trendBitmap, (measureText - (trendBitmap.getWidth())) - 4.0f, f - ((labelFontSize / 2)), paint);
        }
        if (!TextUtils.isEmpty(pressureUnit)) {
            paint.setTextSize((float) labelFontSize);
            measureText = (float) (width - ((int) paint.measureText(pressureUnit)));
            f += (float) numericFontSize;
            canvas.drawText(pressureUnit, measureText, f, paint);
        }
        if (!TextUtils.isEmpty(pressureText)) {
            paint.setTextSize((float) numericFontSize);
            canvas.drawText(pressureText, measureText - ((((paint.measureText(pressureText)) + 4)) + ((paint.measureText(" ")))), f, paint);
        }
    }

    private void startTimes() {
        long nanoTime = System.nanoTime();
        float f = ((float) (nanoTime - startTime)) / 1000000.0f;
        startTime = nanoTime;
        rotation = ((float) ((Math.sqrt((double) windSpeed) * (f)) / 20.0d)) + rotation;
        rotation %= 360.0f;
    }

    private void drawBarometer(Canvas canvas) {
        paint.setStrokeWidth(barometerStrokeWidth);
        paint.setColor(barometerColor);
        paint.setStyle(Paint.Style.FILL);
        int width = getWidth();
        float f = pressurePaddingTop;
        for (int i = 0; i < 10; i++) {
            canvas.drawLine((float) ((width) - toPixel(5d)), f, width, f, paint);
            f = (float) ((f) + (lineSpace + (barometerTickSpacing)));
        }
        paint.setColor(textColor);
    }

    public void animateBaroMeter() {
        if (!animationBaroMeterEnable) {
            animationBaroMeterEnable = true;
            setupPressureLine(false);
            invalidate();
        }
    }

    private int getScaleInPixel(int i) {
        return (int) ((getContext().getResources().getDisplayMetrics().density * (i)) + 0.5f);
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private double toPixel(double d) {
        return (getContext().getResources().getDisplayMetrics().density) * d;
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

    public TrendType getTrendType() {
        return trendType;
    }

    public void setTrendType(TrendType trendType) {
        this.trendType = trendType;
        setupPressureLine(false);

    }

    public Typeface getTypeface() {
        if (typeface == null)
            return Typeface.DEFAULT;
        return typeface;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public float getLineStrokeWidth() {
        return lineStrokeWidth;
    }

    public void setLineStrokeWidth(float lineStrokeWidth) {
        this.lineStrokeWidth = lineStrokeWidth;
    }

    public int getBarometerColor() {
        return barometerColor;
    }

    public void setBarometerColor(int barometerColor) {
        this.barometerColor = barometerColor;
    }

    public float getBarometerStrokeWidth() {
        return barometerStrokeWidth;
    }

    public void setBarometerStrokeWidth(float barometerStrokeWidth) {
        this.barometerStrokeWidth = barometerStrokeWidth;
    }

    public String getWindDirectionText() {
        return WindDirectionText;
    }

    public void setWindDirectionText(String windDirectionText) {
        WindDirectionText = windDirectionText;
    }

    public int getBigPoleX() {
        return bigPoleX;
    }

    public void setBigPoleX(int bigPoleX) {
        this.bigPoleX = bigPoleX;
    }

    public int getSmallPoleX() {
        return smallPoleX;
    }

    public void setSmallPoleX(int smallPoleX) {
        this.smallPoleX = smallPoleX;
    }

    public String getWindText() {
        return windText;
    }

    public void setWindText(String windText) {
        this.windText = windText;
    }

    public String getWindName() {
        return windName;
    }

    public void setWindName(String windName) {
        this.windName = windName;
    }

    public String getWindSpeedText() {
        return windSpeedText;
    }

    public void setWindSpeedText(String windSpeedText) {
        this.windSpeedText = windSpeedText;
    }

    public String getBarometerText() {
        return barometerText;
    }

    public void setBarometerText(String barometerText) {
        this.barometerText = barometerText;
    }

    public int getPoleBottomY() {
        return poleBottomY;
    }

    public void setPoleBottomY(int poleBottomY) {
        this.poleBottomY = poleBottomY;
    }

    public int getWindTextX() {
        return windTextX;
    }

    public void setWindTextX(int windTextX) {
        this.windTextX = windTextX;
    }

    public int getWindTextY() {
        return windTextY;
    }

    public void setWindTextY(int windTextY) {
        this.windTextY = windTextY;
    }

    public String getWindSpeedUnit() {
        return windSpeedUnit;
    }

    public void setWindSpeedUnit(String str) {
        windSpeedUnit = str;
        initSpeedUnit();
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getLabelFontSize() {
        return labelFontSize;
    }

    public void setLabelFontSize(int labelFontSize) {
        this.labelFontSize = labelFontSize;
    }

    public int getNumericFontSize() {
        return numericFontSize;
    }

    public void setNumericFontSize(int numericFontSize) {
        this.numericFontSize = numericFontSize;
    }

    public String getPressureUnit() {
        return pressureUnit;
    }

    public void setPressureUnit(String str) {
        pressureUnit = str;
    }

    public int getPressureTextY() {
        return pressureTextY;
    }

    public void setPressureTextY(int pressureTextY) {
        this.pressureTextY = pressureTextY;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float f) {
        pressure = getscale(f, 2);
        if (pressure < 0.0f) {
            pressureText = empty;
        } else {
            pressureText = formatFloat(pressure);
        }
    }

    public float getLineSize() {
        return lineSize;
    }

    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    public double getLineSpace() {
        return lineSpace;
    }

    public void setLineSpace(double lineSpace) {
        this.lineSpace = lineSpace;
    }
}
