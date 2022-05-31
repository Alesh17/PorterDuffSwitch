# PorterDuffSwitch

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Alesh-blue)](https://www.linkedin.com/in/dmitry-aleshkov)

Inspired by [this article](https://android.jlelse.eu/the-power-of-android-porter-duff-mode-28b99ade45ec). Apk for test [here](https://github.com/Alesh17/PorterDuffSwitch/raw/master/apk/sample.apk).

<img src="/art/preview1.gif" alt="sample" title="sample" width="300" height="112"  />
<img src="/art/preview2.gif" alt="sample" title="sample" width="300" height="318"  />

Dependency
-----

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
	/* other repositories */
	maven { url 'https://jitpack.io' }
    }
}
```

Add dependency in your app module:

```gradle
dependencies {
    implementation 'com.github.Alesh17:PorterDuffSwitch:2.8.1'
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
        app:pds_textLeft="Text left"
        app:pds_textRight="Text right"
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

* `view.getState(): CurrentState`                                 
* `view.setState(state: CurrentState)`                                 
* `view.setDefaultState(state: CurrentState)`  
* `view.setDuration(duration: Long)`                              
* `view.setInterpolator(interpolator: TimeInterpolator)`          
* `view.setOnStateChangeListener(listener: OnStateChangeListener)`                               

Kotlin
-----

```kotlin
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator

class MainActivity : AppCompatActivity() {
 
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
    
         porterDuffSwitch.setDuration(1000)
         porterDuffSwitch.setInterpolator(LinearOutSlowInInterpolator())
         porterDuffSwitch.getState()
         porterDuffSwitch.setOnStateChangeListener { view, state ->
             when (state) {
                 CurrentState.LEFT  -> Toast.makeText(baseContext, "LEFT", Toast.LENGTH_SHORT).show()
                 CurrentState.RIGHT -> Toast.makeText(baseContext, "RIGHT", Toast.LENGTH_SHORT).show()
             }
         }

         /* Set state with animation */
         switchPush.setState(CurrentState.RIGHT)

         /* Set state without animation */
         switchPush.setDefaultState(CurrentState.RIGHT)
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

        /* Set state with animation */
        switchPush.setState(CurrentState.RIGHT);
        
        /* Set state without animation */
        switchPush.setDefaultState(CurrentState.RIGHT);

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

About interpolators you can read in this [article](https://thoughtbot.com/blog/android-interpolators-a-visual-guide).
This is a really good article with ready-made examples.
