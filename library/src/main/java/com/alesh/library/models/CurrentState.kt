package com.alesh.library.models

enum class CurrentState(private val value: Int) {

    LEFT(0),
    RIGHT(1);

    fun isLeft(): Boolean {
        return value == LEFT.ordinal
    }

    fun isRight(): Boolean {
        return value == RIGHT.ordinal
    }

    fun switch(): CurrentState {
        return if (value == 0) RIGHT else LEFT
    }
}