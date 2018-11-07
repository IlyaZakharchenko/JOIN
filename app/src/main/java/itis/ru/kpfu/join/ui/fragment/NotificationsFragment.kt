package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.NotificationsPresenter
import itis.ru.kpfu.join.mvp.view.NotificationsView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

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

    @InjectPresenter
    lateinit var presenter: NotificationsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}