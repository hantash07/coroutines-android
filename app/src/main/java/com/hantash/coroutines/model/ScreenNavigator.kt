package com.hantash.coroutines.model

import android.app.Activity

class ScreenNavigator(
    private val activity: Activity
) {

    fun goBack() {
        activity.onBackPressed()
    }



}