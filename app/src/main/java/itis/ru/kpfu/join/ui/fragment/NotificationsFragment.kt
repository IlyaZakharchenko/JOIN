package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.model.ProjectMember
import itis.ru.kpfu.join.mvp.presenter.NotificationsPresenter
import itis.ru.kpfu.join.mvp.view.NotificationsView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectMemberAdapter
import kotlinx.android.synthetic.main.fragment_notifications.toolbar_notifications
import kotlinx.android.synthetic.main.fragment_project.rv_project_members
import kotlinx.android.synthetic.main.fragment_project.toolbar_project

class NotificationsFragment : BaseFragment(), NotificationsView {

    companion object {
        fun newInstance(): NotificationsFragment {
            val args = Bundle()
            val fragment = NotificationsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_notifications

    override val toolbarTitle: Int?
        get() = R.string.notifications_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_notifications

    @InjectPresenter
    lateinit var presenter: NotificationsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}