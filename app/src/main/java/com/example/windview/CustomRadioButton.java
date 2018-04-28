package com.example.windview;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by Ahmad Nemati on 12/25/2016.
 */

public class CustomRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    public CustomRadioButton(Context context) {
        super(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isChecked() {
        return super.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked)
            setTextColor(Color.WHITE);
        else
            setTextColor(Color.BLACK);
        super.setChecked(checked);
    }
}
