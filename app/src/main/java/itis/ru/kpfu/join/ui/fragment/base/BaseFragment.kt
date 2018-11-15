package itis.ru.kpfu.join.ui.fragment.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import kotlinx.android.synthetic.main.activity_fragment_host.main_container
import kotlinx.android.synthetic.main.fragment_progress.fragment_progress
import kotlinx.android.synthetic.main.fragment_progress_error.btn_progress_dialog_again
import kotlinx.android.synthetic.main.fragment_progress_error.fragment_progress_error
import javax.inject.Inject

abstract class BaseFragment : MvpAppCompatFragment() {

    protected abstract val contentLayout: Int

    protected abstract val toolbarTitle: Int?

    protected abstract val menu: Int?

    protected abstract val enableBackPressed: Boolean

    protected abstract val enableBottomNavBar: Boolean

    protected abstract val toolbar: Toolbar?

    protected val baseActivity get() = activity as BaseActivity

    @Inject
    lateinit var api: JoinApi

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JoinApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideProgressBar()
        hideProgressError()

        (activity as? FragmentHostActivity)?.setToolbar(toolbar)
        (activity as? FragmentHostActivity)?.setToolbarTitle(toolbarTitle)
        (activity as? FragmentHostActivity)?.enableBackPressed(enableBackPressed)
        (activity as? FragmentHostActivity)?.enableBottomNavBar(enableBottomNavBar)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu?.let { inflater?.inflate(it, menu) }
    }

    fun showProgressBar() {
        (activity as? FragmentHostActivity)?.fragment_progress?.visibility = View.VISIBLE
        (activity as? FragmentHostActivity)?.main_container?.visibility = View.GONE
    }

    fun hideProgressBar() {
        (activity as? FragmentHostActivity)?.fragment_progress?.visibility = View.GONE
        (activity as? FragmentHostActivity)?.main_container?.visibility = View.VISIBLE
    }

    fun showProgressError(action: () -> Unit) {
        (activity as? FragmentHostActivity)?.fragment_progress?.visibility = View.GONE
        (activity as? FragmentHostActivity)?.fragment_progress_error?.visibility = View.VISIBLE

        (activity as? FragmentHostActivity)?.btn_progress_dialog_again?.setOnClickListener {
            hideProgressError()
            action()
        }
    }

    fun hideProgressError() {
        (activity as? FragmentHostActivity)?.fragment_progress_error?.visibility = View.GONE
        (activity as? FragmentHostActivity)?.main_container?.visibility = View.VISIBLE
    }
}