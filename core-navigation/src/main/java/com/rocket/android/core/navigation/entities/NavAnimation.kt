package com.rocket.android.core.navigation.entities

import androidx.navigation.AnimBuilder

/**
 * Custom *Animations* or Animator resources for navigation
 *
 * This class contains four properties for the different animations which can be applied in navigation
 *
 * @property enter "enter" property of the [AnimBuilder] class
 * @property exit "exit" property of the [AnimBuilder] class
 * @property popEnter "popEnter" property of the [AnimBuilder] class
 * @property popExit "popExit" property of the [AnimBuilder] class
 */
class NavAnimation(
    val enter: Int,
    val exit: Int,
    val popEnter: Int,
    val popExit: Int
)
