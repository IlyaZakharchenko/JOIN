package itis.ru.kpfu.join.presentation.ui.main.notifications

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.NotificationModel
import itis.ru.kpfu.join.data.network.joinapi.pojo.NotificationRequest
import itis.ru.kpfu.join.presentation.adapter.NotificationsAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.details.ProjectDetailsFragment
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.layout_progress_error.*
import javax.inject.Inject
import javax.inject.Provider

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

    @Inject
    lateinit var presenterProvider: Provider<NotificationsPresenter>

    @Inject
    lateinit var adapter: NotificationsAdapter

    @ProvidePresenter
    fun providePresenter(): NotificationsPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        presenter.getNotifications()
    }

    private fun initRecyclerView() {
        adapter.apply {
            onAccept = { onAccept(it) }
            onDecline = { onDecline(it) }
            onProjectClick = { onProjectClick(it) }
            onUsernameClick = { onUsernameClick(it) }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onAccept(id: Long) {
        presenter.responseToNotification(id, NotificationRequest(true))
    }

    private fun onDecline(id: Long) {
        presenter.responseToNotification(id, NotificationRequest(false))
    }

    private fun onProjectClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProjectDetailsFragment.newInstance(id), true)
    }

    private fun onUsernameClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(id), true)
    }

    override fun setNotifications(notifications: List<NotificationModel>) {
        adapter.items = notifications
    }

    override fun showRetry(errorText: String) {
        retry.visibility = View.VISIBLE
        retry_title.text = errorText
        btn_retry.setOnClickListener { presenter.onRetry() }
    }

    override fun hideRetry() {
        retry.visibility = View.GONE
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, clearStack = true)
    }
}