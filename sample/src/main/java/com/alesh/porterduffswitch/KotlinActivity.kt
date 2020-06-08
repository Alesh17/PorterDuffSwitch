package com.alesh.porterduffswitch

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.alesh.library.OnStateChangeListener
import com.alesh.library.PorterDuffSwitch
import com.alesh.library.models.CurrentState
import kotlinx.android.synthetic.main.activity_for_kotlin.*

class KotlinActivity : AppCompatActivity(), OnStateChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_kotlin)

        switchPush.setDuration(4000)
        switchPush.setInterpolator(LinearOutSlowInInterpolator())
        switchPush.setOnStateChangeListener(this)
    }

    override fun onStateChanged(view: PorterDuffSwitch?, state: CurrentState) {
        when (state) {
            CurrentState.LEFT  -> Toast.makeText(baseContext, "LEFT", Toast.LENGTH_SHORT).show()
            CurrentState.RIGHT -> Toast.makeText(baseContext, "RIGHT", Toast.LENGTH_SHORT).show()
        }
    }
}