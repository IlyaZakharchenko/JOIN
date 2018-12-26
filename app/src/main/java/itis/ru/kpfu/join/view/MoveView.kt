package itis.ru.kpfu.join.view

import android.app.Activity
import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

class MoveView : FloatingActionButton {

    companion object {
        const val ORIENTATION_LEFT_RIGHT = "left_right"
        const val ORIENTATION_TOP_BOTTOM = "top_bottom"
    }

    private var container: View? = null

    private var scale = 1f

    private var attachDuration = 300L
    private var scaleDuration = 300L

    private var attachToLeftRight = true
    private var attachToBottomTop = false

    private var dX = 0
    private var dY = 0
    private var oldX = 0
    private var oldY = 0

    private var margin = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        setOnTouchListener { v, event -> onTouch(v, event) }
    }

    private fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        container?.let { container ->

            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldX = view.x.toInt()
                    oldY = view.y.toInt()

                    dX = (view.x - motionEvent.rawX).toInt()
                    dY = (view.y - motionEvent.rawY).toInt()

                    view.animate().scaleX(scale).scaleY(scale).setDuration(scaleDuration).start()
                }
                MotionEvent.ACTION_MOVE -> {
                    view.animate()
                            .x(motionEvent.rawX + dX)
                            .y(motionEvent.rawY + dY)
                            .setDuration(0)
                            .start()
                }
                MotionEvent.ACTION_UP -> {
                    view.animate().scaleX(1f).scaleY(1f).setDuration(scaleDuration).start()

                    if (attachToLeftRight) attachToLeftRight(view, motionEvent) else attachToBottomTop(view,
                            motionEvent)

                    if (oldX == view.x.toInt() && oldY == view.y.toInt()) {
                        view.performClick()
                    }
                }
            }
            container.invalidate()
        }
        return true
    }

    fun setContainer(container: View) {
        this.container = container
    }

    fun setScale(value: Float) {
        this.scale = value
    }

    fun setScaleDuration(duration: Long) {
        this.scaleDuration = duration
    }

    fun setAttachDuration(duration: Long) {
        this.attachDuration = duration
    }

    private fun attachToLeftRight(view: View, motionEvent: MotionEvent) {

        var x = 0f
        var y = 0f

        container?.let { container ->
            if (view.y < 0) {
                y = 0f + margin
            } else if (view.y > container.height - view.height) {
                y = container.height.toFloat() - view.height - margin
            } else {
                y = motionEvent.rawY + dY
            }

            if (motionEvent.rawX < container.width / 2) {
                x = 0f + margin
            } else {
                x = container.width.toFloat() - view.width - margin
            }
            view.animate()
                    .x(x)
                    .y(y)
                    .setDuration(attachDuration)
                    .start()
        }
    }

    private fun attachToBottomTop(view: View, motionEvent: MotionEvent) {

        var x = 0f
        var y = 0f

        container?.let { container ->
            if (view.x < 0) {
                x = 0f
            } else if (view.x > container.width - view.width) {
                x = container.width.toFloat() - view.width
            } else {
                x = motionEvent.rawX + dX
            }

            if (view.y + view.height  < container.height / 2) {
                y = 0f
            } else {
                y = container.height.toFloat() - view.height
            }

            view.animate()
                    .x(x)
                    .y(y)
                    .setDuration(attachDuration)
                    .start()
        }
    }

    fun changeAttachOrientation(orientation: String) {
        if(orientation == ORIENTATION_LEFT_RIGHT) {
            attachToLeftRight = true
            attachToBottomTop = false
        } else {
            attachToLeftRight = false
            attachToBottomTop = true
        }
    }

    fun setMargin(dp : Int) {
        margin = toPx(dp)
    }

    private fun toPx(dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return  (dp * scale + 0.5f).toInt()
    }
}