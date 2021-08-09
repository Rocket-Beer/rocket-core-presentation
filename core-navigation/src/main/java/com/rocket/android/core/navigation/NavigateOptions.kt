package com.rocket.android.core.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.rocket.android.core.navigation.entities.NavAnimation


class NavigateOptions(clear: Boolean? = null,
                      @IdRes to: Int = 0,
                      popUpToInclusive: Boolean = false,
                      animation: NavAnimation? = null){

    private constructor(builder: Builder) : this(builder.clear, builder.to, builder.popUpToInclusive,
        builder.animation)

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var clear: Boolean? = null
        var to: Int = 0
        var popUpToInclusive: Boolean = false
        var animation: NavAnimation? = null


        fun build(): NavOptions{
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
