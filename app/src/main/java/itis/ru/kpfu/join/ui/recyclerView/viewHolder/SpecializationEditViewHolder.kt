package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.item_specialisation_edit.view.btn_edit
import kotlinx.android.synthetic.main.item_specialisation_edit.view.btn_remove
import kotlinx.android.synthetic.main.item_specialisation_edit.view.item_grid
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_experience
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_experience_years
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_lvl
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_spec_name
import java.util.Random

class SpecializationEditViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Specialization, onItemRemove: (Int, Specialization) -> Unit,
            onItemEdit: (Int, Specialization) -> Unit)= with(itemView) {

        generateImages()

        btn_edit.setOnClickListener { onItemEdit(position, item) }
        btn_remove.setOnClickListener { onItemRemove(position, item) }

        tv_experience.text = item.experience.toString()
        tv_lvl.text = item.knowledgeLevel
        tv_spec_name.text = item.specializationName

        val rem = item.experience.rem(10)

        if (rem == 1 && item.experience > 20) {
            tv_experience_years.text = "год"
        } else if ((rem == 2 || rem == 3 || rem == 4) && item.experience < 20) {
            tv_experience_years.text = "года"
        } else {
            tv_experience_years.text = "лет"
        }
    }

    private fun generateImages() = with(itemView) {
        for (j in 1..(Random().nextInt(10 - 1) + 1)) {
            val image = ImageView(context)
            when ((Math.random() * 3).toInt()) {
                0 -> image.setImageResource(R.drawable.kotlin_logo)
                1 -> image.setImageResource(R.drawable.android_logo)
                2 -> image.setImageResource(R.drawable.junit_logo)
                3 -> image.setImageResource(R.drawable.rx_logo)
            }

            val layoutParams = LinearLayout.LayoutParams(toPx(20, context), toPx(20, context))
            layoutParams.setMargins(toPx(8, context), 0, 0, toPx(8, context))

            image.layoutParams = layoutParams
            item_grid.addView(image)
        }
    }
}