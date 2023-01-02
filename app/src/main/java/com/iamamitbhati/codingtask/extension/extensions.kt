package com.iamamitbhati.codingtask.extension

import android.view.View

/**
 * Extension function for View's visibility
 */
fun View.setVisibility(visible: Boolean) {
    visibility = if (visible)
        View.VISIBLE
    else
        View.GONE
}
