package com.alesh.library

enum class StartPosition(private val value: Int) {

    LEFT(0),
    RIGHT(1);

    fun switch() {
        if (value == 0) RIGHT else LEFT
    }
}