package com.ikbo0621.anitree.animators

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.TreeElement
import java.lang.ref.WeakReference

abstract class Animator(
    private val contextRef: WeakReference<Context>,
    private val treeView: TreeView
) {
    var elements: ArrayList<TreeElement?> = ArrayList()
    var duration
        get() = animator.duration
        set(value) {
            animator.duration = value
        }
    var doRestore = true
    var autoUpdate = true
    protected val animator = ValueAnimator().apply {
        setFloatValues(0f, 1f)
        duration = 600
        interpolator = DecelerateInterpolator()
    }
    var onEnd: (animator: android.animation.Animator) -> Unit = {}

    fun cancel() {
        animator.cancel()
    }

    open fun start() {
        animator.doOnEnd {
            val context = contextRef.get() as? Activity ?: return@doOnEnd
            context.runOnUiThread {
                if (doRestore)
                    restoreElements()
                onEnd(it)
                clear()
            }
        }
    }

    // Not runs if item restore is not required
    abstract fun restoreElements()

    // Runs anyway
    protected open fun clear() {}
}