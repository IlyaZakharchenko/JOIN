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
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.model.ProjectMemberModel
import itis.ru.kpfu.join.presentation.adapter.ProjectJobAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.main.users.UsersFragment
import itis.ru.kpfu.join.presentation.recyclerView.adapter.ProjectMemberAdapter
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import kotlinx.android.synthetic.main.fragment_project_details.*
import kotlinx.android.synthetic.main.layout_progress_error.*
import java.lang.IllegalArgumentException
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
        get() = R.string.project_details_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_project

    @InjectPresenter
    lateinit var presenter: ProjectDetailsPresenter

    @Inject
    lateinit var membersAdapter: ProjectMemberAdapter
    @Inject
    lateinit var jobsAdapter: ProjectJobAdapter
    @Inject
    lateinit var presenterProvider: Provider<ProjectDetailsPresenter>

    @ProvidePresenter
    fun providePresenter(): ProjectDetailsPresenter = presenterProvider.get()

    fun getProjectId(): Long = arguments?.getLong(PROJECT)
            ?: throw IllegalArgumentException("project id is null")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViews()
        initListeners()
    }

    private fun initListeners() {
        add_member_container.setOnClickListener { presenter.onAddUser() }
    }

    private fun initRecyclerViews() {
        rv_project_members.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_members.adapter = membersAdapter.also { it.onUserClick = { id -> onUserClick(id) } }

        rv_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_jobs.adapter = jobsAdapter.also { it.onApply = { onApply() } }
    }

    private fun onUserClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(id), true)
    }

    private fun onApply() {
        presenter.onSendApply()
    }

    override fun onApplySuccess() {
        Toast.makeText(context, "Заявка успешно подана", Toast.LENGTH_SHORT).show()
    }

    override fun setProject(item: ProjectModel, isMyProject: Boolean, isInProject: Boolean) {
        et_project_name.setText(item.name)
        et_project_desc.setText(item.description)

        if (!isMyProject) {
            add_member_container.visibility = GONE
        }

        val allMembers = ArrayList<ProjectMemberModel>()

        item.leader?.let {
            it.isLeader = true
            allMembers.add(it)
        }
        item.participants?.let { allMembers.addAll(it) }

        membersAdapter.items = allMembers

        item.vacancies?.let {
            jobsAdapter.apply {
                this.isInProject = isInProject
                this.isMyProject = isMyProject
                items = it
            }
        }
        tv_project_empty_vacancies.visibility = if (item.vacancies?.size == 0) View.VISIBLE else View.GONE
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showRetry(errorText: String) {
        retry.visibility = View.VISIBLE
        retry_title.text = errorText
        btn_retry.setOnClickListener { presenter.onRetry() }
    }

    override fun hideRetry() {
        retry.visibility = View.GONE
    }

    override fun setUsersFragment(projectId: Long) {
        (baseActivity as? FragmentHostActivity)?.setFragment(UsersFragment.newInstance(projectId), true)
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, clearStack = true)
    }

}