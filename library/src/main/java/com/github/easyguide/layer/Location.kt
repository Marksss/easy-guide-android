package com.github.easyguide.layer

/**
 * Created by shenxl on 2018/10/25.
 */
enum class Location {
    FILL, TO_LEFT, TO_RIGHT, TO_TOP, TO_BOTTOM;

    internal var index: Int = 0

    fun setIndex(index: Int) {
        this.index = index
    }
}
