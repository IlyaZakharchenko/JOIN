package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.ProjectMember
import itis.ru.kpfu.join.mvp.presenter.UsersPresenter
import itis.ru.kpfu.join.mvp.view.UsersView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.UsersAdapter
import kotlinx.android.synthetic.main.fragment_users.rv_users
import kotlinx.android.synthetic.main.fragment_users.search_view_users
import kotlinx.android.synthetic.main.fragment_users.toolbar_all_users
import android.graphics.PorterDuff
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.fragment_users.btn_search_filter

class UsersFragment : BaseFragment(), UsersView {

    companion object {
        const val USERS_FRAGMENT = "USERS_FRAGMENT"

        fun newInstance(projectId: Long): UsersFragment {
            val args = Bundle()
            args.putLong(USERS_FRAGMENT, projectId)

            val fragment = UsersFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_users

    override val toolbarTitle: Int?
        get() = R.string.toolbar_users

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_all_users

    private var adapter: UsersAdapter? = null
    private var users: List<ProjectMember>? = null
    private var projectId: Long? = null
    private var isSetted: Boolean = false

    @InjectPresenter
    lateinit var presenter: UsersPresenter

    @ProvidePresenter
    fun providePresenter(): UsersPresenter {
        return JoinApplication.appComponent.providePresenters().provideUsersPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectId = arguments?.getLong(USERS_FRAGMENT)

        initRecyclerView()
        presenter.getUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_users, menu)
        menu?.findItem(R.id.action_search)?.let { initSearchView(it) }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView() {
        adapter = UsersAdapter(ArrayList(), { onInviteClick(it) }, { onUserClick(it) })

        rv_users.adapter = adapter
        rv_users.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initSearchView(item: MenuItem) {
        toolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(baseActivity, R.color.colorWhite),
                PorterDuff.Mode.SRC_ATOP);

        val layoutParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER or Gravity.END

        search_view_users.setMenuItem(item)
        search_view_users.showSearch(false)
        search_view_users.clearFocus()
        search_view_users.setHint("Поиск")
        search_view_users.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isEmpty()) {
                    layoutParams.setMargins(0, 0, toPx(16, baseActivity), 0)
                    isSetted = false
                    btn_search_filter.layoutParams = layoutParams
                } else if (!isSetted && !newText.isEmpty()) {
                    layoutParams.setMargins(0, 0, toPx(48, baseActivity), 0)
                    isSetted = true
                    btn_search_filter.layoutParams = layoutParams
                }

                searchUsers(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                searchUsers(query)
                return true
            }
        })
        search_view_users.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                btn_search_filter.visibility = View.GONE
            }

            override fun onSearchViewShown() {
                btn_search_filter.visibility = View.VISIBLE
            }
        })
    }

    private fun onInviteClick(user: ProjectMember) {
        presenter.inviteUser(user, projectId)
    }

    private fun onUserClick(userId: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(userId), true)
    }

    override fun setUsers(users: MutableList<ProjectMember>) {
        this.users = users
        adapter?.setUsers(users)
    }

    override fun onConnectionError() {
        showProgressError { presenter.getUsers() }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onInviteSuccess(user: ProjectMember) {
        Snackbar.make(rv_users, "Пользователь ${user.username} приглашен в проект", Snackbar.LENGTH_SHORT).show()
        adapter?.updateUser(user)
    }

    fun searchUsers(text: String) {
        val result = ArrayList<ProjectMember>()

        users?.forEach {
            val username = it.username?.trim() ?: ""
            if (username.startsWith(text.trim(), true)) result.add(it)
        }

        adapter?.setUsers(result)
    }
}