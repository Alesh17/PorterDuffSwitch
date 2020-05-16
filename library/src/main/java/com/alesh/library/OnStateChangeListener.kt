package com.alesh.library

import com.alesh.library.models.CurrentState

/**
 * Interface definition for a callback to be invoked when the checked state
 * of a PorterDuffSwitch changed.
 */
interface OnStateChangeListener {

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param view The PorterDuffSwitch view whose state has changed.
     * @param state  The new checked state.
     */
    fun onStateChanged(view: PorterDuffSwitch?, state: CurrentState)
}