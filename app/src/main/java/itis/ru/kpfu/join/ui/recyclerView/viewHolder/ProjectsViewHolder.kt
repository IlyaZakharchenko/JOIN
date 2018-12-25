package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Project
import itis.ru.kpfu.join.db.entity.User
import kotlinx.android.synthetic.main.item_project.view.tv_project_description
import kotlinx.android.synthetic.main.item_project.view.tv_project_name

class ProjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Project) = with(itemView) {
        tv_project_name.text = item.name
        tv_project_description.text = item.description
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
            avatarCIV.setImageResource(R.drawable.ic_no_avatar)
            Picasso
                    .with(context)
                    .load(R.drawable.ic_no_avatar)
                    .resize(layoutParams.width, layoutParams.height)
                    .into(avatarCIV)
            image_container.addView(avatarCIV)
        }
    }*/
}