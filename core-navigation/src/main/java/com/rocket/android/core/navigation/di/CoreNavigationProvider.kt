package com.rocket.android.core.navigation.di

import com.rocket.android.core.navigation.NavigatorLifecycle

/**
 * Contains an instance of *NavigatorLifecycle*
 *
 * Provides a singleton instance of [NavigatorLifecycle], which manages app's lifecycle
 *
 * @property provideNavigatorLifecycle instance of [NavigatorLifecycle]
 */
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
