package com.example.windview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.mikepenz.materialize.MaterializeBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    @BindView(R.id.windview)
    WindView windView;
    @BindView(R.id.wind_speed)
    SeekBar windSpeed;
    @BindView(R.id.pressure)
    SeekBar pressureSpeed;
    @BindView(R.id.barometr_size)
    SeekBar barometreSize;
    @BindView(R.id.wind_unit)
    AppCompatSpinner windUnit;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.pressure_unit)
    AppCompatSpinner pressureUnit;
    @BindView(R.id.animate)
    Button animateBaroMeter;
    @BindView(R.id.linebar)
    SeekBar line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        materialize();
        setupWindView();
        initSpinner();
        initSeekBars();
        initRadioButton();


    }

    private void initRadioButton() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void setupWindView() {
        animateBaroMeter.setOnClickListener(this);
        windView.setPressure(20);
        windView.setPressureUnit("in Hg");
        windView.setWindSpeed(1);
        windView.setWindSpeedUnit(" km/h");
        windView.setTrendType(TrendType.UP);
        windView.start();


    }

    private void initSeekBars() {
        windSpeed.setOnSeekBarChangeListener(this);
        pressureSpeed.setOnSeekBarChangeListener(this);
        barometreSize.setOnSeekBarChangeListener(this);
        line.setOnSeekBarChangeListener(this);
    }

    private void materialize() {
        new MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withTranslucentStatusBarProgrammatically(false)
                .withTintedStatusBar(false).build();
    }

    private void initSpinner() {
        List<String> windUnits = new ArrayList<>();
        windUnits.add("km/h");
        windUnits.add("ft/s");
        windUnits.add("m/s");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, windUnits);
        windUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                windView.setWindSpeedUnit("" + adapterView.getAdapter().getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        windUnit.setAdapter(arrayAdapter);

        List<String> pressureUnits = new ArrayList<>();
        pressureUnits.add("Hg");
        pressureUnits.add("Pa");
        pressureUnits.add("hPa");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, pressureUnits);
        pressureUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                windView.setPressureUnit("in " + adapterView.getAdapter().getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pressureUnit.setAdapter(arrayAdapter2);


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.wind_speed:
                windView.setWindSpeed(i);
                break;
            case R.id.pressure:
                windView.setPressure(i);
                break;
            case R.id.barometr_size:
                windView.setBarometerStrokeWidth(i);
                break;
            case R.id.linebar:
                windView.setLineStrokeWidth(i);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.rv_up:
                windView.setTrendType(TrendType.UP);
                windView.start();
                break;
            case R.id.rv_down:
                windView.setTrendType(TrendType.DOWN);
                windView.start();
                break;
            case R.id.rv_none:
                windView.setTrendType(TrendType.NONE);
                windView.start();
                break;
        }

    }

    @Override
    public void onClick(View view) {
        Log.e("Click", "Clicked");
        windView.animateBaroMeter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        windView.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        windView.start();
    }
}
