package itis.ru.kpfu.join.presentation.ui

import android.app.ActivityOptions
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.base.BaseActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.activity_fragment_host.bottom_nav_bar
import javax.inject.Inject
import javax.inject.Provider

class FragmentHostActivity : BaseActivity(), FragmentHostView {

    @InjectPresenter
    lateinit var presenter: FragmentHostPresenter

    @Inject
    lateinit var presenterProvider: Provider<FragmentHostPresenter>

    @ProvidePresenter
    fun providePresenter(): FragmentHostPresenter = presenterProvider.get()

    override val contentLayout: Int
        get() = R.layout.activity_fragment_host

    override val menu: Int?
        get() = null

    override val toolbarTitle: Int?
        get() = R.string.app_name

    override val enableBackPressed: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottom_nav_bar.selectedItemId = R.id.bottom_projects
        bottom_nav_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_projects -> presenter.onAllProject()
                R.id.bottom_my_projects -> presenter.onMyProjects()
                R.id.bottom_dialogs -> presenter.onDialogs()
                R.id.bottom_notifications -> presenter.onNotifications()
                else -> presenter.onProfile()
            }
            true
        }
    }

    override fun setFragment(fragment: BaseFragment, addToBackStack: Boolean, clearStack: Boolean) {
        val transaction = supportFragmentManager?.beginTransaction()
        if (addToBackStack) {
            transaction?.addToBackStack(fragment.javaClass.name)
        }
        if (clearStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        transaction?.replace(R.id.main_container, fragment, fragment.javaClass.name)?.commit()
    }


    fun enableBottomNavBar(state: Boolean) {
        bottom_nav_bar.visibility = if (state) View.VISIBLE else View.GONE
    }
}
