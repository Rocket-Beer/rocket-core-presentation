package com.rocket.android.core.navigation

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

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

    private fun setCurrentActivity(currentActivity: Activity) {
        if (currentActivity is FragmentActivity && currentActivity !is NavigatorIgnoreActivity) {
            this.activity = currentActivity
        }
    }
}