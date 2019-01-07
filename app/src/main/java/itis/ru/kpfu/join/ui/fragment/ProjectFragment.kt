package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.View.GONE
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.api.model.ProjectMember
import itis.ru.kpfu.join.mvp.presenter.ProjectPresenter
import itis.ru.kpfu.join.mvp.view.ProjectView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectJobAdapter
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectMemberAdapter
import kotlinx.android.synthetic.main.fragment_project.add_member_container
import kotlinx.android.synthetic.main.fragment_project.et_project_desc
import kotlinx.android.synthetic.main.fragment_project.et_project_name
import kotlinx.android.synthetic.main.fragment_project.rv_project_jobs
import kotlinx.android.synthetic.main.fragment_project.rv_project_members
import kotlinx.android.synthetic.main.fragment_project.toolbar_project

class ProjectFragment : BaseFragment(), ProjectView {

    companion object {
        const val PROJECT = "project"

        fun newInstance(id: Long): ProjectFragment {
            val args = Bundle()
            args.putLong(PROJECT, id)

            val fragment = ProjectFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_project

    override val toolbarTitle: Int?
        get() = R.string.project_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_project

    private var projectId: Long? = null

    private var membersAdapter: ProjectMemberAdapter? = null
    private var jobsAdapter: ProjectJobAdapter? = null

    @InjectPresenter
    lateinit var presenter: ProjectPresenter

    @ProvidePresenter
    fun providePresenter(): ProjectPresenter {
        return JoinApplication.appComponent.providePresenters().provideProjectPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViews()
        initListeners()
        projectId = arguments?.getLong(PROJECT)

        projectId?.let { presenter.getProject(it) }
    }

    private fun initListeners() {
        add_member_container.setOnClickListener {
            (baseActivity as? FragmentHostActivity)?.setFragment(UsersFragment.newInstance(projectId ?: -1), true)
        }
    }

    private fun initRecyclerViews() {
        membersAdapter = ProjectMemberAdapter { onUserClick(it) }
        jobsAdapter = ProjectJobAdapter()

        rv_project_members.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_members.adapter = membersAdapter

        rv_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_jobs.adapter = jobsAdapter
    }

    private fun onUserClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(id), true)
    }

    override fun setProject(item: Project, isMyProject: Boolean) {
        et_project_name.setText(item.name)
        et_project_desc.setText(item.description)

        if (!isMyProject) {
            add_member_container.visibility = GONE
        }

        val allMembers = ArrayList<ProjectMember>()

        item.leader?.let {
            it.isLeader = true
            allMembers.add(it)
        }
        item.participants?.let { allMembers.addAll(it) }

        membersAdapter?.setMembers(allMembers)
        item.vacancies?.let { jobsAdapter?.setJobs(it, isMyProject) }
    }

    override fun onConnectionError() {
        showProgressError { projectId?.let { presenter.getProject(it) } }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }
}