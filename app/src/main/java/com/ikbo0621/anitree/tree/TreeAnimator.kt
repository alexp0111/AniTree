package com.ikbo0621.anitree.tree

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PointF
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.tree.elements.TreeElement
import java.lang.ref.WeakReference

class TreeAnimator(
    private val contextRef: WeakReference<Context>,
    private val treeView: TreeView
) {
    var elements: ArrayList<TreeElement?> = ArrayList()
    private var initPositions: ArrayList<PointF> = ArrayList()
    private var dPos = PointF()
    private val animator = ValueAnimator().apply {
        setFloatValues(0f, 1f)
        duration = 400
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            val context = contextRef.get() as? MainActivity ?: return@addUpdateListener
            context.runOnUiThread {
                for (i in elements.indices) {
                    elements[i]?.setAbsPos(
                        initPositions[i].x + ((animatedValue as Float) * dPos.x),
                        initPositions[i].y + ((animatedValue as Float) * dPos.y)
                    )
                }
                treeView.postInvalidate()
            }
        }
    }

    fun startAnimation(dPos: PointF) {
        this.dPos = dPos

        initPositions.clear()
        for (i in elements)
            initPositions.add(i?.getAbsPos() ?: PointF())

        animator.start()
    }

    fun setOnEndFunction(action: (animator: Animator) -> Unit) {
        animator.doOnEnd {
            // Restoration of original positions
            for (i in elements.indices)
                elements[i]?.setAbsPos(initPositions[i].x, initPositions[i].y)

            action(it)
        }
    }
}