package com.rocket.android.core.navigation.extension

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

// region FRAGMENT
/**
 * Calls [safeState] using as an action fragment's [popBack] method
 */
internal fun Fragment.popBackStack() {
    safeState { fragment ->
        fragment.popBack()
    }
}

/**
 * Performs [popBackStack] on the fragment's [getNavHostFragment] of type [Type]
 * @param Type the type of the [FragmentManager.getPrimaryNavigationFragment] to be found
 */
@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.popBackStackFragment() {
    getNavHostFragment<Type>()?.safeState { fragment ->
        fragment.popBack()
    }
}

/**
 * Calls [safeState] using as an action the three-argument overload (using id) of [NavController.navigate] method on the
 * fragment's [NavController]
 * @param id *resId* parameter of the three-argument overload (using id) of [NavController.navigate] method
 * @param navOptions *navOptions* parameter of the three-argument overload (using id) of [NavController.navigate] method
 * @param extras *args* parameter of the three-argument overload (using id) of [NavController.navigate] method
 */
internal fun Fragment.navigate(
    @IdRes id: Int,
    navOptions: NavOptions? = null,
    extras: Bundle
) {
    safeState { fragment ->
        fragment.findNavController().navigate(id, extras, navOptions)
    }
}

/**
 * Performs [navigate] on the fragment's [getNavHostFragment] of type [Type]
 * @param Type the type of the [FragmentManager.getPrimaryNavigationFragment] to be found
 * @param id *resId* parameter of the three-argument overload (using id) of [NavController.navigate] method
 * @param navOptions *navOptions* parameter of the three-argument (using id) overload of [NavController.navigate] method
 * @param extras *args* parameter of the three-argument overload (using id) of [NavController.navigate] method
 */
@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.navigateFragment(
    @IdRes id: Int,
    navOptions: NavOptions? = null,
    extras: Bundle
) {
    getNavHostFragment<Type>()?.safeState { fragment ->
        fragment.findNavController().navigate(id, extras, navOptions)
    }
}

/**
 * Calls [safeState] using as an action the two-argument overload (using [NavDirections]) of [NavController.navigate]
 * method on the fragment's [NavController]
 * @param directions *directions* parameter of the two-argument overload (using [NavDirections]) of
 * [NavController.navigate] method
 * @param navOptions *navOptions* parameter of the two-argument overload (using [NavDirections]) of
 * [NavController.navigate] method
 */
internal fun Fragment.navigate(
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    safeState { fragment ->
        fragment.findNavController().navigate(directions, navOptions)
    }
}

/**
 * Performs the two-argument overload (using [NavDirections]) of [navigate] method on the fragment's
 * [getNavHostFragment] of type [Type]
 * @param Type the type of the [FragmentManager.getPrimaryNavigationFragment] to be found
 * @param directions *directions* parameter of the two-argument overload (using [NavDirections]) of
 * [NavController.navigate] method
 * @param navOptions *navOptions* parameter of the two-argument overload (using [NavDirections]) of
 * [NavController.navigate] method
 */
@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.navigateFragment(
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    getNavHostFragment<Type>()?.safeState { fragment ->
        fragment.findNavController().navigate(directions, navOptions)
    }
}

/**
 * Calls [safeState] using as an action the two-argument overload (using [Uri]) of [NavController.navigate] method on
 * the fragment's [NavController]
 * @param uri *deepLink* parameter of the two-argument overload (using [Uri]) of [NavController.navigate] method
 * @param navOptions *navOptions* parameter of the two-argument overload (using [Uri]) of [NavController.navigate]
 * method
 */
internal fun Fragment.navigate(
    uri: Uri,
    navOptions: NavOptions? = null
) {
    safeState { fragment ->
        fragment.findNavController().navigate(uri, navOptions)
    }
}

/**
 * Performs the two-argument overload (using [Uri]) of [navigate] method on the fragment's [getNavHostFragment] of type
 * [Type]
 * @param Type the type of the [FragmentManager.getPrimaryNavigationFragment] to be found
 * @param uri *deepLink* parameter of the two-argument overload (using [Uri]) of [NavController.navigate] method
 * @param navOptions *navOptions* parameter of the two-argument overload (using [Uri]) of [NavController.navigate]
 * method
 */
@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.navigateFragment(
    uri: Uri,
    navOptions: NavOptions? = null
) {
    getNavHostFragment<Type>()?.safeState { fragment ->
        fragment.findNavController().navigate(uri, navOptions)
    }
}
// endregion

// region UTILS
/**
 * Starting from the bottom, returns the first [FragmentManager.getPrimaryNavigationFragment] found of type [Type] in
 * the back stack, if it exists. Otherwise returns null
 * @param Type the type of the [Fragment] to be found
 */
@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.getNavHostFragment(): Fragment? {
    var fragment: Fragment? = this
    while (fragment != null && fragment !is Type) {
        fragment = fragment.childFragmentManager.primaryNavigationFragment
            ?: fragment.childFragmentManager.fragments.firstOrNull { it is Type }
    }
    return fragment?.childFragmentManager?.primaryNavigationFragment
}

/**
 * Performs the given action after the fragment has returned from the [Fragment.onResume] call. If the fragment is
 * already resumed, the action will be executed immediately
 * @param ignore If true, the action will be performed only if the fragment is resumed
 * @param action action to be performed
 */
@PublishedApi
internal fun Fragment.safeState(ignore: Boolean = false, action: (fragment: Fragment) -> Unit) {
    if (isResumed) {
        action(this)
    } else if (!ignore) {
        parentFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                    super.onFragmentResumed(fm, f)
                    fm.unregisterFragmentLifecycleCallbacks(this)
                    action(f)
                }
            },
            false
        )
    }
}

/**
 * If fragment exists, calls [NavController.popBackStack] on its [NavController], otherwise it will try to call
 * [NavController.popBackStack] on the first parent fragment that exists.
 *
 * If no fragment has been found, [Activity.finish] will be called
 */
@PublishedApi
internal fun Fragment.popBack() {
    var fragment: Fragment? = this
    while (fragment != null && !fragment.findNavController().popBackStack()) {
        fragment = fragment.parentFragment
    }
    if (fragment == null) {
        activity?.finish()
    }
}
// endregion
