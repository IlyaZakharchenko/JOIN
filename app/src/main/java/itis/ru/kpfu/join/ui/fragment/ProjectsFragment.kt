package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.ProjectsPresenter
import itis.ru.kpfu.join.mvp.view.ProjectsView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_projects.toolbar_projects
import java.util.concurrent.TimeUnit.SECONDS

class ProjectsFragment : BaseFragment(), ProjectsView {

    companion object {
        fun newInstance(): ProjectsFragment {
            val args = Bundle()
            val fragment = ProjectsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_projects

    override val toolbarTitle: Int?
        get() = R.string.projects_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_projects

    @InjectPresenter
    lateinit var presenter: ProjectsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* Single.just("asd")
                .delay(3, SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgressBar() }
                .subscribe({ showProgressError { Toast.makeText(baseActivity, "add", Toast.LENGTH_SHORT).show() } },
                        { showProgressError { Toast.makeText(baseActivity, "ad", Toast.LENGTH_SHORT).show() } })*/
    }
}