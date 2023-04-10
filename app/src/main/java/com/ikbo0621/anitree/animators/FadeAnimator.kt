package com.ikbo0621.anitree.animators

import android.content.Context
import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.tree.TreeView
import java.lang.ref.WeakReference

class FadeAnimator(
    contextRef: WeakReference<Context>,
    treeView: TreeView
) : Animator(contextRef, treeView) {
    var type = Type.IN

    init {
        animator.addUpdateListener {
            val context = contextRef.get() as? MainActivity ?: return@addUpdateListener
            val factor = it.animatedValue as Float
            val alphaFactor = if (type == Type.IN) factor else 1.0f - factor

            context.runOnUiThread {
                for (i in elements.indices) {
                    elements[i]?.alpha = (255.0 * alphaFactor).toInt()
                }
                if (autoUpdate)
                    treeView.postInvalidate()
            }
        }
    }

    override fun start() {
        super.start()
        for (i in elements.indices) {
            if (type == Type.IN)
                elements[i]?.alpha = 0
            else
                elements[i]?.alpha = 255
        }

        animator.start()
    }

    override fun restoreElements() {
        for (i in elements.indices) {
            elements[i]?.alpha = 255
        }
    }

    enum class Type {
        IN, OUT
    }

    override fun clear() {
        elements.clear()
    }
}