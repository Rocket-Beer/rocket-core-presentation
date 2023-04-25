package com.rocket.android.core.navigation.compose

import androidx.navigation.NavHostController

class NavigatorManager {
    private var navController: NavHostController? = null

    fun setController(controller: NavHostController) {
        navController = controller
    }

    @Throws(Exception::class)
    fun getController(): NavHostController {
        navController?.let {
            return it
        } ?: run {
            throw Exception("Not setted navController in NavigatorManager yet")
        }
    }

    fun goBack() {
        navController?.popBackStack()
    }
}
