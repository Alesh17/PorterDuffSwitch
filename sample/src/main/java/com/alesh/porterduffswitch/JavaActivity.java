package com.alesh.porterduffswitch;

import android.os.Bundle;
import android.view.animation.AnticipateInterpolator;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alesh.library.OnStateChangeListener;
import com.alesh.library.PorterDuffSwitch;
import com.alesh.library.models.CurrentState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JavaActivity extends AppCompatActivity implements OnStateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_java);

        PorterDuffSwitch porterDuffSwitch = findViewById(R.id.porterDuffSwitch);

        porterDuffSwitch.setDuration(4000);
        porterDuffSwitch.setInterpolator(new AnticipateInterpolator());
        porterDuffSwitch.setOnStateChangeListener(this);
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
