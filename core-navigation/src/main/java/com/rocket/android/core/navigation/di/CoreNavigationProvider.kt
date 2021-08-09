package com.rocket.android.core.navigation.di

import com.rocket.android.core.navigation.NavigatorLifecycle


@Suppress("unused")
class CoreNavigationProvider private constructor() {
    val provideNavigatorLifecycle: NavigatorLifecycle by lazy {
        NavigatorLifecycle()
    }

    companion object {
        val instance: CoreNavigationProvider by lazy {
            CoreNavigationProvider()
        }
    }
}