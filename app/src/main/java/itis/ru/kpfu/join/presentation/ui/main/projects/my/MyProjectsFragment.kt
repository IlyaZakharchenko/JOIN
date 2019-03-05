package itis.ru.kpfu.join.presentation.ui.main.projects.my

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.network.pojo.Project
import itis.ru.kpfu.join.presentation.adapter.ProjectsAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.add.AddProjectFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.details.ProjectDetailsFragment
import kotlinx.android.synthetic.main.fragment_my_projects.fab_add_project
import kotlinx.android.synthetic.main.fragment_my_projects.my_projects_container
import kotlinx.android.synthetic.main.fragment_my_projects.rv_my_projects
import kotlinx.android.synthetic.main.fragment_my_projects.toolbar_my_projects
import javax.inject.Inject
import javax.inject.Provider

class MyProjectsFragment : BaseFragment(), MyProjectsView {

    companion object {
        fun newInstance(): MyProjectsFragment {
            val args = Bundle()
            val fragment = MyProjectsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_my_projects

    override val toolbarTitle: Int?
        get() = R.string.my_projects_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_my_projects

    @InjectPresenter
    lateinit var presenter: MyProjectsPresenter

    @Inject
    lateinit var presenterProvider: Provider<MyProjectsPresenter>

    @Inject
    lateinit var adapter: ProjectsAdapter

    @ProvidePresenter
    fun providePresenter(): MyProjectsPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initClickListeners()

        fab_add_project.setContainer(my_projects_container)
        fab_add_project.setScale(0.8f)
        fab_add_project.setMargin(16)

        presenter.getProjects()
    }

    private fun initClickListeners() {
        fab_add_project.setOnClickListener {
            (baseActivity as? FragmentHostActivity)?.setFragment(AddProjectFragment.newInstance(), true)
        }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onConnectionError() {
        showProgressError { presenter.getProjects() }
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun setProjects(projects: List<Project>) {
        adapter.items = projects
    }

    private fun initRecyclerView() {
        adapter.onProjectClick = { onProjectClick(it) }
        rv_my_projects.adapter = adapter
        rv_my_projects.layoutManager = LinearLayoutManager(baseActivity)
        rv_my_projects.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab_add_project.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0 || dy < 0 && fab_add_project.isShown) {
                    fab_add_project.hide()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun onProjectClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProjectDetailsFragment.newInstance(id), true)
    }
}
