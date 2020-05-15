package com.alesh.library

import com.alesh.library.models.CurrentState

interface OnStateChangeListener {

    fun onStateChanged(view: PorterDuffSwitch?, state: CurrentState)
}