package com.rocket.android.core.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.rocket.android.core.navigation.NavigateOptions.Builder
import com.rocket.android.core.navigation.entities.NavAnimation

/**
 * Options for a navigation inside the app
 *
 * Provides different options which will be applied when navigating between destinations
 *
 * @param clear determines whether to clear or not the back stack. By default is null, which in this case is equivalent
 * to false
 * @param to indicates the destination to pop up to. By default is 0
 * @param popUpToInclusive whether the *to* destination should be popped from the back stack. By default is false
 * @param animation animations to be applied when performing the navigation. Bu default is null, which results in no
 * animations
 * @constructor Creates options from a given [Builder]
 */
class NavigateOptions(
    clear: Boolean? = null,
    @IdRes to: Int = 0,
    popUpToInclusive: Boolean = false,
    animation: NavAnimation? = null
) {

    private constructor(builder: Builder) : this(
        builder.clear, builder.to, builder.popUpToInclusive,
        builder.animation
    )

    companion object {
        /**
         * Constructs a new [NavOptions] from a [Builder] on which [block] is executed
         * @param block lambda to be executed as an extension of [Builder]
         */
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    /**
     * Creates a new [NavOptions]
     *
     * Provides a method to build a new [NavOptions] with its options determined by [clear], [to], [popUpToInclusive]
     * and [animation]
     *
     * @property clear determines whether to clear or not the back stack. By default is null, which in this case is
     * equivalent to false
     * @property to indicates the destination to pop up to. By default is 0
     * @property popUpToInclusive whether the *to* destination should be popped from the back stack. By default is false
     * @property animation animations to be applied when performing the navigation. Bu default is null, which results in
     * no animations
     */
    class Builder {
        var clear: Boolean? = null
        var to: Int = 0
        var popUpToInclusive: Boolean = false
        var animation: NavAnimation? = null

        /**
         * Creates a new instance of [NavOptions] using [clear], [to], [popUpToInclusive] and [animation] to determine
         * its options
         * @return a new [NavOptions]
         */
        fun build(): NavOptions {
            return navOptions {
                if (clear == true) {
                    popUpTo(
                        id = to,
                        popUpToBuilder = {
                            inclusive = popUpToInclusive
                        }
                    )
                }

                animation?.let {
                    anim {
                        this.enter = it.enter
                        this.exit = it.exit
                        this.popEnter = it.popEnter
                        this.popExit = it.popExit
                    }
                }
            }
        }
    }
}
