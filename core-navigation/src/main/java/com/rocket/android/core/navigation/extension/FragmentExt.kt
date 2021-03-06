package com.rocket.android.core.navigation.extension

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

// region FRAGMENT
internal fun Fragment.popBackStack() {
    safeState { fragment ->
        fragment.popBack()
    }
}

@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.popBackStackFragment() {
    getNavHostFragment<Type>()?.safeState { fragment ->
        fragment.popBack()
    }
}

internal fun Fragment.navigate(
    @IdRes id: Int,
    navOptions: NavOptions? = null,
    extras: Bundle
) {
    safeState { fragment ->
        fragment.findNavController().navigate(id, extras, navOptions)
    }
}

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

internal fun Fragment.navigate(
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    safeState { fragment ->
        fragment.findNavController().navigate(directions, navOptions)
    }
}

@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.navigateFragment(
    directions: NavDirections,
    navOptions: NavOptions? = null
) {
    getNavHostFragment<Type>()?.safeState { fragment ->
        fragment.findNavController().navigate(directions, navOptions)
    }
}

internal fun Fragment.navigate(
    uri: Uri,
    navOptions: NavOptions? = null
) {
    safeState { fragment ->
        fragment.findNavController().navigate(uri, navOptions)
    }
}

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
@PublishedApi
internal inline fun <reified Type : Fragment> Fragment.getNavHostFragment(): Fragment? {
    var fragment: Fragment? = this
    while (fragment != null && fragment !is Type) {
        fragment = fragment.childFragmentManager.primaryNavigationFragment
            ?: fragment.childFragmentManager.fragments.firstOrNull { it is Type }
    }
    return fragment?.childFragmentManager?.primaryNavigationFragment
}

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
