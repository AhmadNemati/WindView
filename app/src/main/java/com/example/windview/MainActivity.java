package com.example.windview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.ahmadnemati.wind.WindView;

public class MainActivity extends AppCompatActivity {
    private WindView windView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        windView= (WindView) findViewById(R.id.windView);
        windView.setPressure(50);
        windView.setPressureUnit("in Hg");
        windView.setWindSpeed(75);
        windView.setWindSpeedUnit("Km/s");
        windView.setWindDirection("Salam");

    }
}
