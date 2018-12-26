package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.mvp.presenter.MyProjectsPresenter
import itis.ru.kpfu.join.mvp.view.MyProjectsView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectsAdapter
import kotlinx.android.synthetic.main.fragment_projects.rv_projects
import kotlinx.android.synthetic.main.fragment_projects.toolbar_projects

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
        get() = R.layout.fragment_projects

    override val toolbarTitle: Int?
        get() = R.string.my_projects_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_projects

    private lateinit var adapter: ProjectsAdapter

    @InjectPresenter
    lateinit var presenter: MyProjectsPresenter

    @ProvidePresenter
    fun providePresenter(): MyProjectsPresenter {
        return JoinApplication.appComponent.providePresenters().provideMyProjectPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        presenter.getProjects()
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun setProjects(projects: List<Project>) {
        adapter.setItems(projects)
    }

    private fun initRecyclerView() {
        adapter = ProjectsAdapter { onProjectClick(it) }
        rv_projects.adapter = adapter
        rv_projects.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onProjectClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProjectFragment.newInstance(id), true)
    }
}
