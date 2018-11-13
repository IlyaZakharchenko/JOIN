package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.MenuPresenter
import itis.ru.kpfu.join.mvp.view.MenuView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_menu.toolbar_profile
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class MenuFragment: BaseFragment(), MenuView {

    companion object {
        fun newInstance(): MenuFragment {
            val args = Bundle()
            val fragment = MenuFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_menu

    override val toolbarTitle: Int?
        get() = R.string.profile

    override val menu: Int?
        get() = R.menu.menu_profile

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_profile

    @InjectPresenter
    lateinit var presenter: MenuPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}