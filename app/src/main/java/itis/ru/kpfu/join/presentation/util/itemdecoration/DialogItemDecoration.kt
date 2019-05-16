package itis.ru.kpfu.join.presentation.util.itemdecoration

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import itis.ru.kpfu.join.R

class DialogItemDecoration : RecyclerView.ItemDecoration() {

    var data = mutableListOf<String>()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val adapterPosition = parent.getChildAdapterPosition(child)
            val previous = adapterPosition - 1

            if (data.size - 1 >= adapterPosition) {
                if (previous >= 0) {
                    if (data[adapterPosition] != data[previous])
                        drawTitle(c, data[adapterPosition], child, parent)
                } else {
                    drawTitle(c, data[adapterPosition], child, parent)
                }
            }
        }
    }

    private fun drawTitle(canvas: Canvas, titleText: String, view: View, parent: ViewGroup) {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.item_message_header, parent, false) as? TextView
        textView?.apply {
            text = titleText
            measure(
                    View.MeasureSpec.makeMeasureSpec(
                            parent.measuredWidth,
                            View.MeasureSpec.AT_MOST
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                            parent.measuredHeight,
                            View.MeasureSpec.AT_MOST
                    )
            )
            layout(parent.left, 0, parent.right, measuredHeight)
        }

        textView?.layout(parent.left, 0, parent.right, textView.measuredHeight)
        if (textView != null) {
            canvas.save()
            canvas.translate((parent.right / 2f) - (textView.measuredWidth / 2f), (view.top - textView.measuredHeight).toFloat())
            textView.draw(canvas)
            canvas.restore()
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val adapterPosition = parent.getChildAdapterPosition(view)
        val previous = adapterPosition - 1

        if (previous >= 0 && data.isNotEmpty()) {
            if (data[adapterPosition].isNotEmpty() && data[previous].isNotEmpty()&& data [adapterPosition] != data[previous]) {
                outRect.top = 70
            } else {
                outRect.setEmpty()
            }
        } else {
            outRect.top = 70
        }
    }
}