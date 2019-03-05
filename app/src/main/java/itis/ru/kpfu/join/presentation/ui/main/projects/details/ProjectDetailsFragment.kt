package itis.ru.kpfu.join.presentation.ui.main.projects.details

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.View.GONE
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.network.pojo.Project
import itis.ru.kpfu.join.network.pojo.ProjectMember
import itis.ru.kpfu.join.presentation.adapter.ProjectJobAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.main.users.UsersFragment
import itis.ru.kpfu.join.presentation.recyclerView.adapter.ProjectMemberAdapter
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import kotlinx.android.synthetic.main.fragment_project_details.add_member_container
import kotlinx.android.synthetic.main.fragment_project_details.et_project_desc
import kotlinx.android.synthetic.main.fragment_project_details.et_project_name
import kotlinx.android.synthetic.main.fragment_project_details.rv_project_jobs
import kotlinx.android.synthetic.main.fragment_project_details.rv_project_members
import kotlinx.android.synthetic.main.fragment_project_details.toolbar_project
import kotlinx.android.synthetic.main.fragment_project_details.tv_project_empty_vacancies
import javax.inject.Inject
import javax.inject.Provider

class ProjectDetailsFragment : BaseFragment(), ProjectDetailsView {

    companion object {
        const val PROJECT = "project"

        fun newInstance(id: Long): ProjectDetailsFragment {
            val args = Bundle()
            args.putLong(PROJECT, id)

            val fragment = ProjectDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_project_details

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
    lateinit var presenter: ProjectDetailsPresenter

    @Inject
    lateinit var presenterProvider: Provider<ProjectDetailsPresenter>

    @ProvidePresenter
    fun providePresenter(): ProjectDetailsPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViews()
        initListeners()
        projectId = arguments?.getLong(PROJECT)

        projectId?.let { presenter.getProject(it) }
    }

    private fun initListeners() {
        add_member_container.setOnClickListener {
            (baseActivity as? FragmentHostActivity)?.setFragment(UsersFragment.newInstance(projectId
                    ?: -1), true)
        }
    }

    private fun initRecyclerViews() {
        membersAdapter = ProjectMemberAdapter { onUserClick(it) }
        jobsAdapter = ProjectJobAdapter {onApply()}

        rv_project_members.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_members.adapter = membersAdapter

        rv_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_jobs.adapter = jobsAdapter
    }

    private fun onUserClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(id), true)
    }

    private fun onApply() {
        presenter.sendApply(projectId)
    }

    override fun onApplySuccess() {
        Toast.makeText(context, "Заявка успешно подана", Toast.LENGTH_SHORT).show()
    }

    override fun setProject(item: Project, isMyProject: Boolean, isInProject: Boolean) {
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
        item.vacancies?.let { jobsAdapter?.setJobs(it, isMyProject, isInProject) }

        tv_project_empty_vacancies.visibility = if(item.vacancies?.size == 0) View.VISIBLE else View.GONE
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