package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.model.ProjectMember
import itis.ru.kpfu.join.mvp.presenter.MyProjectsPresenter
import itis.ru.kpfu.join.mvp.view.MyProjectsView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectsAdapter
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

    private lateinit var list: List<Project>

    private lateinit var adapter: ProjectsAdapter

    @InjectPresenter
    lateinit var presenter: MyProjectsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO test data
        initTestData()
        initRecyclerView()
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

    override fun initRecyclerView() {
       /*adapter = ProjectsAdapter(list)
        rv_projects.adapter = adapter
        rv_projects.layoutManager = LinearLayoutManager(baseActivity)*/
    }

    //TODO test data
    private fun initTestData() {
        var project: Project
        list = ArrayList()
        val userList = ArrayList<ProjectMember>()
        for (j in 0..6) {
            val user = ProjectMember(username = "User$j")
            userList.add(user)
        }
        for (i in 0..20) {
            project = Project(name = "Project Name",
                    description = "Description of this undoubtably incredible and unique project, " +
                            "requiring professional developers with all possible skills!", participants = userList)
            (list as ArrayList<Project>).add(project)
        }
    }
}
