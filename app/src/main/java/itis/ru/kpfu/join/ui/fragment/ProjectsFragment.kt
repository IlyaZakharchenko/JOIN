package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.ProjectsPresenter
import itis.ru.kpfu.join.mvp.view.ProjectsView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

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
        get() = R.string.app_name

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    @InjectPresenter
    lateinit var presenter: ProjectsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (userRepository.getUser() != null) {
            Toast.makeText(activity, "youre signed in!", Toast.LENGTH_SHORT).show()
        } else {
            (activity as FragmentHostActivity).setFragment(SignInFragment.newInstance(), false)
        }
    }
}