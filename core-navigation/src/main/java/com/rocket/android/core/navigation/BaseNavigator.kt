package com.rocket.android.core.navigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
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
import com.rocket.android.core.navigation.extension.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Offers functionality to manage app navigation
 *
 * Provides navigators to be used in the app navigation, as well as methods to manage navigation in activities,
 * fragments and dialogs
 *
 * @property navigatorLifecycle instance of [NavigatorLifecycle]
 * @property activity [NavigatorLifecycle.activity] of [navigatorLifecycle]
 * @property context provides access to [activity]
 * @property fragment currently active primary navigation fragment for the [activity] *FragmentManager*
 */
sealed class BaseNavigator(private val navigatorLifecycle: NavigatorLifecycle) {

    /**
     * App navigator
     *
     * Navigator intended to be used as the one available in all sections of the app. It may be used to navigate to
     * screens available from multiple places
     *
     * @property navigatorLifecycle instance of [NavigatorLifecycle]
     */
    abstract class AppBaseNavigator(navigatorLifecycle: NavigatorLifecycle) :
        BaseNavigator(navigatorLifecycle = navigatorLifecycle)

    /**
     * Feature navigator
     *
     * Navigator intended to be used as the one for an specific app flow
     *
     * @property navigatorLifecycle instance of [NavigatorLifecycle]
     */
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

    /**
     * If [reload] is false, it will try to find a fragment which extends from [Type] inside fragment's child fragment
     * manager
     * @param Type type of the fragment to be found
     * @param reload determines whether to search or not the fragment
     * @return false if a fragment has been found, otherwise returns true. Also returns true if [reload] is true
     */
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

    /**
     * If [clear] is true, constructs a new [NavOptions] using [to] and a new [PopUpToBuilder] built with
     * [popUpToInclusive] as parameters for the [NavOptionsBuilder.popUpTo] method
     * @param clear determines whether to create or not the builder
     * @param to indicates the destination to pop up to
     * @param popUpToInclusive whether the *to* destination should be popped from the back stack
     * @return if clear is true, the [NavOptions] built, otherwise returns null
     */
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
    /**
     * Calls [popBackStack] method on [fragment] inside the Main thread
     */
    fun goBack() {
        checkMainThread {
            fragment?.popBackStack()
        }
    }

    /**
     * Calls [popBackStackFragment] (with [Type] as parameter) on [fragment] inside the Main thread
     * @param [Type]
     */
    inline fun <reified Type : Fragment> goBackFragment() {
        checkMainThread {
            fragment?.popBackStackFragment<Type>()
        }
    }

    /**
     * Calls [navigate] (with [id], [navOptions] and [extras] as its parameters) on [fragment] inside the Main thread
     * @param id *id* parameter of the [navigate] method
     * @param navOptions *navOptions* parameter of the [navigate] method
     * @param extras *extras* parameter of the [navigate] method
     */
    fun goTo(
        @IdRes id: Int,
        navOptions: NavOptions? = null,
        extras: Bundle
    ) {
        checkMainThread {
            fragment?.navigate(id = id, extras = extras, navOptions = navOptions)
        }
    }

    /**
     * Calls [navigateFragment] (with [Type], [id], [navOptions] and [extras] as its parameters) on [fragment] inside
     * the Main thread
     * @param Type the type of the FragmentManager.getPrimaryNavigationFragment to be found
     * @param id *id* parameter of the [navigateFragment] method
     * @param navOptions *navOptions* parameter of the [navigateFragment] method
     * @param extras *extras* parameter of the [navigateFragment] method
     */
    inline fun <reified Type : Fragment> goToFragment(
        @IdRes id: Int,
        navOptions: NavOptions? = null,
        extras: Bundle
    ) {
        checkMainThread {
            fragment?.navigateFragment<Type>(id = id, extras = extras, navOptions = navOptions)
        }
    }

    /**
     * Calls the two-argument overload (using [NavDirections]) of [navigate] (with [directions] and [navOptions] as its
     * arguments) on [fragment] inside the Main thread
     * @param directions *directions* parameter of the two-argument overload (using [NavDirections]) of the [navigate]
     * method
     * @param navOptions *navOptions* parameter of the two-argument overload (using [NavDirections]) of the [navigate]
     * method
     */
    fun goTo(
        directions: NavDirections,
        navOptions: NavOptions? = null
    ) {
        checkMainThread {
            fragment?.navigate(directions = directions, navOptions = navOptions)
        }
    }

    /**
     * Calls the three-argument overload (using [NavDirections]) of [navigateFragment] (with [Type], [directions] and
     * [navOptions] as its arguments) on [fragment] inside the Main thread
     * @param Type *Type* parameter of the three-argument overload (using [NavDirections]) of the [navigateFragment]
     * method
     * @param directions *directions* parameter of the three-argument overload (using [NavDirections]) of the
     * [navigateFragment] method
     * @param navOptions *navOptions* parameter of the three-argument overload (using [NavDirections]) of the
     * [navigateFragment] method
     */
    inline fun <reified Type : Fragment> goToFragment(
        directions: NavDirections,
        navOptions: NavOptions? = null
    ) {
        checkMainThread {
            fragment?.navigateFragment<Type>(directions = directions, navOptions = navOptions)
        }
    }

    /**
     * Calls the two-argument overload (using [Uri]) of [navigate] (with [uri] and [navOptions] as its arguments) on
     * [fragment] inside the Main thread
     * @param uri *uri* parameter of the two-argument overload (using [Uri]) of the [navigate] method
     * @param navOptions *navOptions* parameter of the two-argument overload (using [Uri]) of the [navigate] method
     */
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
    /**
     * Executes [block] on the Main thread
     * @param block Lambda to be executed
     */
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
