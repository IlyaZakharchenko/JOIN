package itis.ru.kpfu.join.presentation.base

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatFragment
import dagger.android.support.AndroidSupportInjection
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import kotlinx.android.synthetic.main.activity_fragment_host.main_container
import kotlinx.android.synthetic.main.layout_progress.fragment_progress
import kotlinx.android.synthetic.main.layout_progress_error.*

abstract class BaseFragment : MvpAppCompatFragment(), BaseView {

    protected abstract val contentLayout: Int

    protected abstract val toolbarTitle: Int?

    protected abstract val menu: Int?

    protected abstract val enableBackPressed: Boolean

    protected abstract val enableBottomNavBar: Boolean

    protected abstract val toolbar: Toolbar?

    protected val baseActivity get() = activity as BaseActivity


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? FragmentHostActivity)?.setToolbar(toolbar)
        (activity as? FragmentHostActivity)?.setToolbarTitle(toolbarTitle)
        (activity as? FragmentHostActivity)?.enableBackPressed(enableBackPressed)
        (activity as? FragmentHostActivity)?.enableBottomNavBar(enableBottomNavBar)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu?.let { inflater?.inflate(it, menu) }
    }

    override fun hideKeyboard() {
        (activity as? FragmentHostActivity)?.hideKeyboard()
    }

    override fun showErrorDialog(text: Int) {
        (activity as? FragmentHostActivity)?.showErrorDialog(text)
    }

    override fun showErrorDialog(text: String) {
        (activity as? FragmentHostActivity)?.showErrorDialog(text)
    }

    override fun hideWaitDialog() {
        (activity as? FragmentHostActivity)?.hideWaitDialog()
    }

    override fun showWaitDialog() {
        (activity as? FragmentHostActivity)?.showWaitDialog()
    }
}