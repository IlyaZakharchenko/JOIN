package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.model.ProjectMember
import itis.ru.kpfu.join.mvp.presenter.UsersPresenter
import itis.ru.kpfu.join.mvp.view.UsersView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.recyclerView.adapter.UsersAdapter
import kotlinx.android.synthetic.main.fragment_users.rv_users
import kotlinx.android.synthetic.main.fragment_users.search_view_users
import kotlinx.android.synthetic.main.fragment_users.toolbar_users

class UsersFragment : BaseFragment(), UsersView {

    companion object {
        fun newInstance(): UsersFragment {
            val args = Bundle()
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
        get() = toolbar_users

    private var adapter: UsersAdapter? = null
    private var users: List<ProjectMember>? = null

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

        initRecyclerView()
        presenter.getUsers()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_users, menu)
        menu?.findItem(R.id.action_search)?.let { initSearchView(it) }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initRecyclerView() {
        adapter = UsersAdapter { onClick(it) }

        rv_users.adapter = adapter
        rv_users.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initSearchView(item: MenuItem) {
        search_view_users.setMenuItem(item)
        search_view_users.showSearch(false)
        search_view_users.clearFocus()
        search_view_users.setHint("Поиск")
        search_view_users.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                searchUsers(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                searchUsers(query)
                return true
            }
        })
    }

    private fun onClick(user: ProjectMember) {
        Snackbar.make(rv_users, "Пользователь ${user.username} приглашен в проект", Toast.LENGTH_SHORT).show()
    }

    override fun setUsers(users: List<ProjectMember>) {
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

    fun searchUsers(text: String) {
        val result = ArrayList<ProjectMember>()

        users?.forEach {
            val username = it.username?.trim() ?: ""
            if (username.startsWith(text.trim(), true)) result.add(it)
        }

        adapter?.setUsers(result)
    }
}