package com.rocket.android.core.navigation.compose

import androidx.navigation.NavHostController

class NavigatorManager {
    private var navController: NavHostController? = null

    fun setController(controller: NavHostController) {
        navController = controller
    }

    fun goBack() {
        navController?.popBackStack()
    }
}
