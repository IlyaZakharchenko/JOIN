package itis.ru.kpfu.join.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import itis.ru.kpfu.join.ui.activity.MainActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity

abstract class BaseFragment : MvpAppCompatFragment() {

    protected abstract val contentLayout: Int

    protected abstract val toolbarTitle: Int?

    protected abstract val menu: Int?

    protected abstract val enableBackPressed: Boolean

    protected val baseActivity get() = activity as BaseActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbarTitle(toolbarTitle)
        (activity as MainActivity).enableBackPressed(enableBackPressed)
        this.menu?.let { setHasOptionsMenu(true) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu?.let { inflater?.inflate(it, menu) }
    }
}