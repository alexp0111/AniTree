package com.ikbo0621.anitree.animators

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.ikbo0621.anitree.MainActivity
import com.ikbo0621.anitree.tree.TreeView
import com.ikbo0621.anitree.tree.elements.TreeElement
import java.lang.ref.WeakReference
import kotlin.math.pow

open class TreeAnimator(
    private val contextRef: WeakReference<Context>,
    private val treeView: TreeView
) {
    var elements: ArrayList<TreeElement?> = ArrayList()
    var duration
        get() = animator.duration
        set(value) {
            animator.duration = value
        }
    private var initPositions: ArrayList<PointF> = ArrayList()
    private var effects: ArrayList<AdditionalEffect> = ArrayList()
    private var movementTypes: ArrayList<MovementType> = ArrayList()
    private var dPos = PointF()
    private val animator = ValueAnimator().apply {
        setFloatValues(0f, 1f)
        duration = 600
        interpolator = DecelerateInterpolator()
        addUpdateListener {
            val context = contextRef.get() as? MainActivity ?: return@addUpdateListener
            val factor = animatedValue as Float
            context.runOnUiThread {
                for (i in elements.indices) {
                    val alphaFactor = factor.pow(3.0f)
                    when (effects[i]) {
                        AdditionalEffect.FADEIN -> elements[i]?.alpha = (255.0 * alphaFactor).toInt()
                        AdditionalEffect.FADEOUT -> elements[i]?.alpha = (255.0 * (1.0 - alphaFactor)).toInt()
                        else -> {}
                    }

                    if (movementTypes[i] == MovementType.NONE)
                        continue

                    elements[i]?.setAbsPos(
                        initPositions[i].x + (factor * dPos.x),
                        initPositions[i].y + (factor * dPos.y)
                    )
                }
                treeView.invalidate()
            }
        }
    }

    fun addElement(
        element: TreeElement?,
        effect: AdditionalEffect = AdditionalEffect.NONE,
        movementType: MovementType = MovementType.MOVE
    ) {
        elements.add(element)
        effects.add(effect)
        movementTypes.add(movementType)
    }

    fun addElement(
        elements: Collection<TreeElement?>,
        effect: AdditionalEffect = AdditionalEffect.NONE,
        movementType: MovementType = MovementType.MOVE
    ) {
        this.elements.addAll(elements)
        for (i in elements.indices) {
            effects.add(effect)
            movementTypes.add(movementType)
        }
    }

    open fun startAnimation(dPos: PointF) {
        this.dPos = dPos

        for (i in elements.indices) {
            when (effects[i]) {
                AdditionalEffect.FADEIN -> elements[i]?.alpha = 0
                AdditionalEffect.FADEOUT -> elements[i]?.alpha = 255
                else -> {}
            }
        }

        initPositions.clear()
        for (i in elements)
            initPositions.add(i?.getAbsPos() ?: PointF())

        animator.start()
    }

    fun setOnEndFunction(action: (animator: Animator) -> Unit) {
        animator.doOnEnd {
            val context = contextRef.get() as? Activity ?: return@doOnEnd
            context.runOnUiThread {
                // Restoration of original properties
                for (i in elements.indices) {
                    elements[i]?.setAbsPos(initPositions[i].x, initPositions[i].y)
                    elements[i]?.alpha = 255
                }
                effects.clear()
                movementTypes.clear()

                action(it)
            }
        }
    }

    enum class AdditionalEffect {
        NONE, FADEIN, FADEOUT
    }

    enum class MovementType {
        NONE, MOVE
    }
}