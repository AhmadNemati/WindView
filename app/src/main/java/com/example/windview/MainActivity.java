package com.example.windview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;
import com.mikepenz.materialize.MaterializeBuilder;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private WindView windView;
    private SeekBar windSpeed;
    private SeekBar pressureSpeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MaterializeBuilder()
                .withActivity(this)
                .withFullscreen(true)
                .withTranslucentStatusBarProgrammatically(false)
                .withTintedStatusBar(false).build();
        windView = (WindView) findViewById(R.id.windview);
        windSpeed = (SeekBar) findViewById(R.id.wind_speed);
        pressureSpeed = (SeekBar) findViewById(R.id.pressure);
        windView.setPressure(20);
        windView.setPressureUnit("in Hg");
        windView.setWindSpeed(1);
        windView.setWindSpeedUnit("Km/s");
        windView.setTrendType(TrendType.UP);
        windView.start();
        windSpeed.setOnSeekBarChangeListener(this);
        pressureSpeed.setOnSeekBarChangeListener(this);


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
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
