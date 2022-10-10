package com.rocket.android.core.navigation.extension

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle

// region DIALOG
/**
 * Shows a dialog with a tag
 * @param dialog the [DialogFragment] that will be shown
 * @param tag the tag for [dialog]
 */
internal fun FragmentActivity.show(dialog: DialogFragment, tag: String) {
    safeState { activity ->
        dialog.show(activity.supportFragmentManager, tag)
    }
}

/**
 * Dismisses a dialog with a tag. The dialog must be an instance of [DialogFragment]
 * @param tag the tag provided for the dialog
 */
internal fun FragmentActivity.dismiss(tag: String) {
    safeState { activity ->
        (activity.supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dismiss()
    }
}
// endregion

// region UTILS
/**
 * Performs the given action when the activity calls its parent's [Activity.onResume]. If the activity is already
 * resumed, the action will be executed instantly
 * @param action action to be performed
 */
private fun FragmentActivity.safeState(action: (activity: FragmentActivity) -> Unit) {
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        action(this)
    } else {
        application.registerActivityLifecycleCallbacks(
            object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

                override fun onActivityStarted(activity: Activity) {}

                override fun onActivityResumed(activity: Activity) {
                    activity.application.unregisterActivityLifecycleCallbacks(this)
                    (activity as? FragmentActivity)?.let { fragmentActivity ->
                        action(fragmentActivity)
                    }
                }

                override fun onActivityPaused(activity: Activity) {}

                override fun onActivityStopped(activity: Activity) {}

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

                override fun onActivityDestroyed(activity: Activity) {}
            }
        )
    }
}
// endregion
