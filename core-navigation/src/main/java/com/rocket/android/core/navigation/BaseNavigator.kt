package com.rocket.android.core.navigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.rocket.android.core.navigation.extension.dismiss
import com.rocket.android.core.navigation.extension.navigate
import com.rocket.android.core.navigation.extension.navigateFragment
import com.rocket.android.core.navigation.extension.popBackStack
import com.rocket.android.core.navigation.extension.popBackStackFragment
import com.rocket.android.core.navigation.extension.popBackStackTo
import com.rocket.android.core.navigation.extension.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class BaseNavigator(private val navigatorLifecycle: NavigatorLifecycle) {
    abstract class AppBaseNavigator(navigatorLifecycle: NavigatorLifecycle) :
        BaseNavigator(navigatorLifecycle = navigatorLifecycle)

    abstract class FeatureBaseNavigator(navigatorLifecycle: NavigatorLifecycle) :
        BaseNavigator(navigatorLifecycle = navigatorLifecycle)

    private var activity: FragmentActivity?
        get() = navigatorLifecycle.activity
        set(value) {
            navigatorLifecycle.activity = value
        }

    val context: Context? = activity

    @PublishedApi
    internal val fragment: Fragment?
        get() = activity?.supportFragmentManager?.primaryNavigationFragment

    protected inline fun <reified Type : Fragment> canReload(reload: Boolean): Boolean {
        var canReload = !reload
        if (canReload) {
            val sameFragment = fragment?.childFragmentManager?.fragments?.find { item ->
                item is Type
            }
            canReload = sameFragment?.let { false } ?: true
        }

        return canReload
    }

    protected fun clearBackStackTo(
        clear: Boolean,
        @IdRes to: Int,
        popUpToInclusive: Boolean = false
    ): NavOptions? {
        return if (clear) {
            navOptions {
                popUpTo(
                    id = to,
                    popUpToBuilder = {
                        inclusive = popUpToInclusive
                    }
                )
            }
        } else null
    }

    // region FRAGMENT
    fun goBack() {
        checkMainThread {
            fragment?.popBackStack()
        }
    }

    fun goBack(@IdRes id: Int, inclusive: Boolean = true) {
        checkMainThread {
            fragment?.popBackStackTo(id, inclusive)
        }
    }

    inline fun <reified Type : Fragment> goBackFragment() {
        checkMainThread {
            fragment?.popBackStackFragment<Type>()
        }
    }

    fun goTo(
        @IdRes id: Int,
        navOptions: NavOptions? = null,
        extras: Bundle
    ) {
        checkMainThread {
            fragment?.navigate(id = id, extras = extras, navOptions = navOptions)
        }
    }

    inline fun <reified Type : Fragment> goToFragment(
        @IdRes id: Int,
        navOptions: NavOptions? = null,
        extras: Bundle
    ) {
        checkMainThread {
            fragment?.navigateFragment<Type>(id = id, extras = extras, navOptions = navOptions)
        }
    }

    fun goTo(
        directions: NavDirections,
        navOptions: NavOptions? = null
    ) {
        checkMainThread {
            fragment?.navigate(directions = directions, navOptions = navOptions)
        }
    }

    inline fun <reified Type : Fragment> goToFragment(
        directions: NavDirections,
        navOptions: NavOptions? = null
    ) {
        checkMainThread {
            fragment?.navigateFragment<Type>(directions = directions, navOptions = navOptions)
        }
    }

    fun goTo(
        uri: Uri,
        navOptions: NavOptions? = null
    ) {
        checkMainThread {
            fragment?.navigate(uri = uri, navOptions = navOptions)
        }
    }

    inline fun <reified Type : Fragment> goToFragment(
        uri: Uri,
        navOptions: NavOptions? = null
    ) {
        checkMainThread {
            fragment?.navigateFragment<Type>(uri = uri, navOptions = navOptions)
        }
    }
    // endregion

    // region DIALOG-FRAGMENT
    fun show(dialog: DialogFragment, tag: String) {
        checkMainThread {
            activity?.show(dialog = dialog, tag = tag)
        }
    }

    fun dismiss(tag: String) {
        checkMainThread {
            activity?.dismiss(tag = tag)
        }
    }
    // endregion

    // region ACTIVITY
    fun findFragmentByTag(tag: String): Fragment? =
        activity?.supportFragmentManager?.findFragmentByTag(tag)

    fun finish() {
        checkMainThread {
            activity?.finish()
        }
    }
    // endregion

    // region UTILS
    @PublishedApi
    internal fun checkMainThread(block: () -> Unit) {
        if (Thread.currentThread().isDaemon) {
            CoroutineScope(Dispatchers.Main).launch {
                block()
            }
        } else {
            block()
        }
    }
    // endregion
}
