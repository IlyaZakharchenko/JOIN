package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Notification
import itis.ru.kpfu.join.api.model.NotificationResponse
import itis.ru.kpfu.join.mvp.presenter.NotificationsPresenter
import itis.ru.kpfu.join.mvp.view.NotificationsView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.NotificationsAdapter
import kotlinx.android.synthetic.main.fragment_notifications.recyclerView
import kotlinx.android.synthetic.main.fragment_notifications.toolbar_notifications

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

    var adapter: NotificationsAdapter? = null

    @ProvidePresenter
    fun providePresenter(): NotificationsPresenter {
        return JoinApplication.appComponent.providePresenters().provideNotificationsPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        presenter.getNotifications()
    }

    private fun initRecyclerView() {
        adapter = NotificationsAdapter(
                onAccept = this::onAccept,
                onDecline = this::onDecline,
                onProjectClick = this::onProjectClick,
                onUsernameClick = this::onUsernameClick
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onAccept(id: Long) {
        presenter.responseToNotification(id, NotificationResponse(true))
    }

    private fun onDecline(id: Long) {
        presenter.responseToNotification(id, NotificationResponse(false))
    }

    private fun onProjectClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProjectFragment.newInstance(id), true)
    }

    private fun onUsernameClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(id), true)
    }

    override fun setNotifications(notifications: List<Notification>) {
        adapter?.setItems(notifications)
    }

    override fun onConnectionError() {
        showProgressError { presenter.getNotifications() }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }
}