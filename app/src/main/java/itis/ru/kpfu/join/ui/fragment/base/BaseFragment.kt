package itis.ru.kpfu.join.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.api.TestApi
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import javax.inject.Inject

abstract class BaseFragment : MvpAppCompatFragment() {

    @Inject
    lateinit var api: TestApi

    @Inject
    lateinit var userRepository: UserRepository

    protected abstract val contentLayout: Int

    protected abstract val toolbarTitle: Int?

    protected abstract val menu: Int?

    protected abstract val enableBackPressed: Boolean

    protected val baseActivity get() = activity as BaseActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JoinApplication.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(contentLayout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as FragmentHostActivity).showToolbar()
        (activity as FragmentHostActivity).setToolbarTitle(toolbarTitle)
        (activity as FragmentHostActivity).enableBackPressed(enableBackPressed)
        this.menu?.let { setHasOptionsMenu(true) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu?.let { inflater?.inflate(it, menu) }
    }
}