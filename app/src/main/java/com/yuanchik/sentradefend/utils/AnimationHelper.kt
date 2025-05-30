package com.yuanchik.sentradefend.utils

import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.math.hypot
import kotlin.math.roundToInt
import androidx.core.view.doOnPreDraw

object AnimationHelper {
    private const val menuItems = 5

    fun performFragmentCircularRevealAnimation(rootView: View, activity: Activity, position: Int) {
        rootView.doOnPreDraw {
            if (!rootView.isAttachedToWindow) return@doOnPreDraw

            val itemCenter = rootView.width / (menuItems * 2)
            val step = (itemCenter * 2) * (position - 1) + itemCenter

            val x = step
            val y = rootView.y.roundToInt() + rootView.height

            val startRadius = 0
            val endRadius = hypot(rootView.width.toDouble(), rootView.height.toDouble())

            val animator = ViewAnimationUtils.createCircularReveal(
                rootView,
                x,
                y,
                startRadius.toFloat(),
                endRadius.toFloat()
            ).apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }

            rootView.visibility = View.VISIBLE
        }
    }
}