[![License Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=true)](http://www.apache.org/licenses/LICENSE-2.0)
![minSdkVersion 14](https://img.shields.io/badge/minSdkVersion-14-red.svg?style=true)
![compileSdkVersion 25](https://img.shields.io/badge/compileSdkVersion-25-yellow.svg?style=true)
[![Release](https://img.shields.io/github/release/jitpack/android-example.svg?label=Jitpack)](https://jitpack.io/#jitpack/android-example)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WindView-brightgreen.svg?style=flat)](https://android-arsenal.com)
# WindView
WindView is an Android Library to show Weather's Wind & pressure Status

# Screenshot
![alt tag](https://raw.githubusercontent.com/AhmadNemati/WindView/master/art/screen.gif)

# Demo

 - [**demo.apk**](https://raw.githubusercontent.com/AhmadNemati/WindView/master/app/demo.apk)



# Setup
# Step 1: Add it as a Dependency in Your Root's `build.gradle` File



```gradle
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```
  -  add this to your app `build.gradle`:

```gradle
dependencies {
	compile 'com.github.AhmadNemati:WindView:1.1.1'
}
```

# Step 2: Add it to your Layout
```xml
  <com.github.ahmadnemati.wind.WindView
            android:id="@+id/windview"
            android:layout_width="match_parent"
            android:layout_height="106dp"
            app:barometerTickSpacing="9dp"
            app:bigPoleX="38dp"
            app:labelFontSize="12sp"
            app:numericFontSize="25sp"
            app:poleBottomY="98dp"
            app:pressureLineY="73dp"
            app:pressureTextX="12dp"
            app:pressureTextY="4dp"
            app:smallPoleX="75dp"
            app:windTextX="10dp"
            app:windTextY="29dp" />
```

# Step 3: Initialize WindView and Start it
```java
            WindView windView= (WindView) findViewById(R.id.windview);
            windView.setPressure(20);
            windView.setPressureUnit("in Hg");
            windView.setWindSpeed(1);
            windView.setWindSpeedUnit(" km/h");
            windView.setTrendType(TrendType.UP);
            windView.start();
```

# Note
Graphical implementations are based on Yahoo Weather Service
# Developed By

* Ahmad Nemati
 * [ahmadnemati.com](http://ahmadnemati.com) - <nematiprog@gmail.com>


# License

    Copyright 2016 Ahmad Nemati

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

