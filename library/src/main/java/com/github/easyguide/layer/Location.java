package com.github.easyguide.layer;

/**
 * Created by shenxl on 2018/10/25.
 */
public enum Location {
    FILL, TO_LEFT, TO_RIGHT, TO_TOP, TO_BOTTOM;

    int index;

    public void setIndex(int index) {
        this.index = index;
    }
}
