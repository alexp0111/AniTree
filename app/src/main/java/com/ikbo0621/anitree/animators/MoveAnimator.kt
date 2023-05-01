package com.ikbo0621.anitree.animators

import android.content.Context
import android.graphics.PointF
import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.tree.TreeView
import java.lang.ref.WeakReference

open class MoveAnimator(
    contextRef: WeakReference<Context>,
    treeView: TreeView
) : Animator(contextRef, treeView) {
    var delta = PointF()
    private var initPositions: ArrayList<PointF> = ArrayList()

    init {
        animator.addUpdateListener {
            val context = contextRef.get() as? MainActivity ?: return@addUpdateListener
            val factor = it.animatedValue as Float

            context.runOnUiThread {
                for (i in elements.indices) {
                    elements[i]?.setAbsPos(
                        initPositions[i].x + (factor * delta.x),
                        initPositions[i].y + (factor * delta.y)
                    )
                }
                if (autoUpdate)
                    treeView.postInvalidate()
            }
        }
    }

    override fun start() {
        super.start()
        for (it in elements)
            initPositions.add(it?.getAbsPos() ?: PointF())

        animator.start()
    }

    override fun restoreElements() {
        for (i in elements.indices) {
            elements[i]?.setAbsPos(initPositions[i].x, initPositions[i].y)
        }
        initPositions.clear()
    }

    override fun clear() {
        initPositions.clear()
        elements.clear()
    }
}