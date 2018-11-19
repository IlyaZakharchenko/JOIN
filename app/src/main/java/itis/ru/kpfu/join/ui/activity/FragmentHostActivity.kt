package itis.ru.kpfu.join.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.view.FragmentHostView
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.activity_fragment_host.bottom_nav_bar
import javax.inject.Inject

class FragmentHostActivity : BaseActivity(), FragmentHostView {

    @InjectPresenter
    lateinit var presenter: FragmentHostPresenter

    @Inject
    lateinit var userRepository: UserRepository

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

    @ProvidePresenter
    fun providePresenter(): FragmentHostPresenter {
        return JoinApplication.appComponent.providePresenters().provideFragmentHostPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disableShiftMode()

        bottom_nav_bar.setOnNavigationItemSelectedListener {
            presenter.onBottomNavBarClick(it.itemId)
            true
        }
        presenter.checkLogin()
    }

    override fun setFragment(fragment: BaseFragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager?.beginTransaction()

        if (addToBackStack) {
            transaction?.addToBackStack(fragment.javaClass.name)
        }
        transaction?.replace(R.id.main_container, fragment, fragment.javaClass.name)?.commit()
    }

    override fun clearFragmentsStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun showToolbar() {
        supportActionBar?.show()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }

    @SuppressLint("RestrictedApi")
    fun disableShiftMode() {
        val menuView = bottom_nav_bar.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false

            for (i in 0..menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                item.setShiftingMode(false)
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun enableBottomNavBar(state: Boolean) {
        bottom_nav_bar.visibility = if (state) View.VISIBLE else View.GONE
    }
}
