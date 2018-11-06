package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.MenuPresenter
import itis.ru.kpfu.join.mvp.view.MenuView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
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
        get() = null

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    @InjectPresenter
    lateinit var presenter: MenuPresenter
}