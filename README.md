# PorterDuffSwitch

<img src="/art/preview.gif" alt="sample" title="sample" width="320" height="600" align="right" vspace="52" />

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-LightProgress-orange.svg?style=flat)](https://android-arsenal.com/details/1/7459)

Inspired by [this article](https://android.jlelse.eu/the-power-of-android-porter-duff-mode-28b99ade45ec). 

Dependency
-----

```gradle
dependencies {
  implementation 'com.alesh:porterduff-switch:1.0.0'
}
```

XML
-----

```xml
<com.alesh.library.PorterDuffSwitch
        android:id="@+id/porterDuffSwitch"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:pds_colorActive="@color/colorPrimary"
        app:pds_colorInactive="@color/colorPrimaryDark"
        app:pds_defaultState="left"
        app:pds_fontFamily="@font/pro_display_semibold"
        app:pds_indent="4dp"
        app:pds_textLeft="°Casadasd"
        app:pds_textRight="°Fasdasd"
        app:pds_textSize="24sp" />
```

You must use the following properties in your XML to change switch.

##### Properties in xml:

* `app:pds_colorActive`            (color)          -> default "#273DBA"
* `app:pds_colorInactive`          (color)          -> default "#9CA0BC"
* `app:pds_fontFamily`             (string)         -> default "roboto"
* `app:pds_indent`                 (dimension)      -> default "8dp"
* `app:pds_defaultState`           (enum)           -> default "left"
* `app:pds_textLeft`               (string)         -> **mandatory field**
* `app:pds_textRight`              (string)         -> **mandatory field**
* `app:pds_textSize`               (dimension)      -> **mandatory field**

##### Properties in code:

* `view.setDuration(duration: Long)`                              
* `view.setInterpolator(interpolator: TimeInterpolator)`          
* `view.setOnStateChangeListener(listener: OnStateChangeListener)`
* `view.getState(): CurrentState`                                 

Kotlin
-----

```kotlin
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

class MainActivity : AppCompatActivity(), OnStateChangeListener {
 
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
    
         porterDuffSwitch.setDuration(1000)
         porterDuffSwitch.setInterpolator(LinearOutSlowInInterpolator())
         porterDuffSwitch.setOnStateChangeListener(this)
    
         porterDuffSwitch.getState()

     }
 
     override fun onStateChanged(view: PorterDuffSwitch?, state: CurrentState) {
         when (state) {
             CurrentState.LEFT  -> Toast.makeText(baseContext, "LEFT", Toast.LENGTH_SHORT).show();
             CurrentState.RIGHT -> Toast.makeText(baseContext, "RIGHT", Toast.LENGTH_SHORT).show();
         }
     }
 }
```

Java
-----

```java
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class MainActivity extends AppCompatActivity implements OnStateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PorterDuffSwitch porterDuffSwitch = findViewById(R.id.porterDuffSwitch);

        porterDuffSwitch.setDuration(400);
        porterDuffSwitch.setInterpolator(new LinearOutSlowInInterpolator());
        porterDuffSwitch.setOnStateChangeListener(this);

        porterDuffSwitch.getState();

    }

    @Override
    public void onStateChanged(@Nullable PorterDuffSwitch view, @NotNull CurrentState state) {
        switch (state) {
            case LEFT: {
                Toast.makeText(getBaseContext(), "LEFT", Toast.LENGTH_SHORT).show();
                break;
            }
            case RIGHT: {
                Toast.makeText(getBaseContext(), "RIGHT", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
```

Interpolators
-----

Abouts interpolators you can read in this [article](https://thoughtbot.com/blog/android-interpolators-a-visual-guide).
This is a really good article with ready-made examples.