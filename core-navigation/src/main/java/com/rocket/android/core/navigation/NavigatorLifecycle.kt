package com.rocket.android.core.navigation

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Provides access to the current activity
 *
 * Contains an instance of [Application.ActivityLifecycleCallbacks] to be used in
 * [Application.registerActivityLifecycleCallbacks], so that it can provide access to the current activity
 *
 * @property activity current activity
 * @property activityLifecycleCallbacks callbacks used to set [activity]
 */
class NavigatorLifecycle {
    var activity: FragmentActivity? = null

    var activityLifecycleCallbacks = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            setCurrentActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            setCurrentActivity(activity)
        }

        override fun onActivityResumed(activity: Activity) {
            setCurrentActivity(activity)
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }
    }

    /**
     * Assigns [activity] to the current activity provided that it does not implement [NavigatorIgnoreActivity]
     * @param currentActivity current activity
     */
    private fun setCurrentActivity(currentActivity: Activity) {
        if (currentActivity is FragmentActivity && currentActivity !is NavigatorIgnoreActivity) {
            this.activity = currentActivity
        }
    }
}
