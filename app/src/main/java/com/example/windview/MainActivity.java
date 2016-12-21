package com.example.windview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.SeekBar;


import com.github.ahmadnemati.wind.WindView;
import com.github.ahmadnemati.wind.enums.TrendType;

public class MainActivity extends AppCompatActivity {
    private WindView windView;
    private AppCompatSeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        windView= (WindView) findViewById(R.id.windView);
        seekBar= (AppCompatSeekBar) findViewById(R.id.seekbar);
        windView.setPressure(50);
        windView.setPressureUnit("in Hg");
        windView.setWindSpeed(15);
        windView.setWindSpeedUnit("Km/s");
        windView.setWindDirection("Salam");
       windView.setTrendType(TrendType.UP);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

windView.setWindSpeed(progress);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
