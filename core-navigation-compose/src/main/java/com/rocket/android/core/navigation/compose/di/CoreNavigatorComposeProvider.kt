package com.rocket.android.core.navigation.compose.di

import com.rocket.android.core.navigation.compose.NavigatorManager

@Suppress("unused")
class CoreNavigatorComposeProvider private constructor() {

    val provideNavigatorManager: NavigatorManager by lazy {
        NavigatorManager()
    }

    companion object {
        val instance: CoreNavigatorComposeProvider by lazy {
            CoreNavigatorComposeProvider()
        }
    }
}
