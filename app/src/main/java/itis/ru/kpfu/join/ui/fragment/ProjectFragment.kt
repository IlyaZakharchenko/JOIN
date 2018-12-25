package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.model.ProjectMember
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectJobAdapter
import itis.ru.kpfu.join.ui.recyclerView.adapter.ProjectMemberAdapter
import kotlinx.android.synthetic.main.fragment_project.rv_project_jobs
import kotlinx.android.synthetic.main.fragment_project.rv_project_members
import kotlinx.android.synthetic.main.fragment_project.toolbar_project
import kotlinx.android.synthetic.main.fragment_projects.rv_projects

class ProjectFragment: BaseFragment() {
    
    companion object {
        fun newInstance(): ProjectFragment {
            val args = Bundle()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViews()
    }

    private fun initRecyclerViews() {
        val memberItems = arrayListOf(
                ProjectMember(name = "Damir Gayazov"),
                ProjectMember(name = "Vasya Pupkin"),
                ProjectMember(name = "Ilya Petrov"),
                ProjectMember(name = "Vova Nosurname"),
                ProjectMember(name = "Noname Hz")
        )

        val specItems = arrayListOf(
                Specialization(name = "first", knowledgeLevel = 0, experience = 3, technologies = "A, B, C"),
                Specialization(name = "second", knowledgeLevel = 1, experience = 12, technologies = "D, E, F"),
                Specialization(name = "third", knowledgeLevel = 2, experience = 34, technologies = "G, H, L"),
                Specialization(name = "fourth", knowledgeLevel = 2, experience = 12, technologies = "R, T, Y")
        )

        rv_project_members.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_members.adapter = ProjectMemberAdapter(memberItems)

        rv_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
        rv_project_jobs.adapter = ProjectJobAdapter(specItems)
    }
}