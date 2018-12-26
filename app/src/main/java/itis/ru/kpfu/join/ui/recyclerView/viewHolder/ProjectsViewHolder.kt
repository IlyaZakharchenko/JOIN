package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.db.repository.UserRepository
import kotlinx.android.synthetic.main.item_project.view.cg_vacancies
import kotlinx.android.synthetic.main.item_project.view.tv_project_description
import kotlinx.android.synthetic.main.item_project.view.tv_project_name

class ProjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Project, onClick: (Long) -> Unit) = with(itemView) {
        tv_project_name.text = item.name
        tv_project_description.text = item.description

        item.vacancies?.forEach {
            val chip = Chip(context)
            chip.text = it.name
            chip.chipEndPadding = 4.toFloat()
            chip.chipStartPadding = 4.toFloat()
            cg_vacancies.addView(chip)
        }

        itemView.setOnClickListener { item.id?.let { it1 -> onClick(it1) } }
    }

   /* private fun initUsers(userList: List<User>) = with(itemView) {
        for (i in 0..userList.lastIndex) {
            var it = userList[i]
            var layoutParams = LinearLayout.LayoutParams(48, 48)
            val avatarCIV = ImageView(context)
            if (i != 0) {
                layoutParams.marginStart = -24
            }
            avatarCIV.layoutParams = layoutParams
            avatarCIV.setProfileImage(R.drawable.ic_no_avatar)
            Picasso
                    .with(context)
                    .load(R.drawable.ic_no_avatar)
                    .resize(layoutParams.width, layoutParams.height)
                    .into(avatarCIV)
            image_container.addView(avatarCIV)
        }
    }*/
}