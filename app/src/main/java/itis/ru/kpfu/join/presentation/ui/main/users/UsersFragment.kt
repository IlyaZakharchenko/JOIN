package itis.ru.kpfu.join.presentation.ui.main.users

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.network.pojo.ProjectMember
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_users.rv_users
import kotlinx.android.synthetic.main.fragment_users.search_view_users
import kotlinx.android.synthetic.main.fragment_users.toolbar_all_users
import android.graphics.PorterDuff
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R.style
import itis.ru.kpfu.join.presentation.adapter.UsersAdapter
import itis.ru.kpfu.join.presentation.ui.main.profile.ProfileFragment
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.btn_show_results_projects_filter
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.spinner_exp_projects_filter
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.spinner_lvl_projects_filter
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.spinner_spec_projects_filter
import kotlinx.android.synthetic.main.fragment_users.btn_search_filter_users
import kotlinx.android.synthetic.main.fragment_users.progress_bar_users
import javax.inject.Inject
import javax.inject.Provider

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
    private var projectId: Long? = null
    private var isSet: Boolean = false
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var searchText: String? = null

    private lateinit var itemsSpec: MutableList<String>
    private lateinit var itemsLvl: MutableList<String>
    private lateinit var itemsExp: MutableList<String>

    @InjectPresenter
    lateinit var presenter: UsersPresenter

    @Inject
    lateinit var presenterProvider: Provider<UsersPresenter>

    @ProvidePresenter
    fun providePresenter(): UsersPresenter = presenterProvider.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectId = arguments?.getLong(USERS_FRAGMENT)

        initBottomSheetDialog()
        initClickListeners()
        initRecyclerView()
        presenter.getUsers(projectId)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
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
                PorterDuff.Mode.SRC_ATOP)

        val layoutParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER or Gravity.END

        search_view_users.setMenuItem(item)
        search_view_users.setHint("Поиск")
        search_view_users.closeSearch()
        search_view_users.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isEmpty()) {
                    layoutParams.setMargins(0, 0, toPx(16, baseActivity), 0)
                    isSet = false
                    btn_search_filter_users.layoutParams = layoutParams

                    bottomSheetDialog?.apply {
                        spinner_spec_projects_filter.selectedIndex = 0
                        spinner_lvl_projects_filter.selectedIndex = 0
                        spinner_exp_projects_filter.selectedIndex = 0
                    }
                } else if (!isSet && !newText.isEmpty()) {
                    layoutParams.setMargins(0, 0, toPx(48, baseActivity), 0)
                    isSet = true
                    btn_search_filter_users.layoutParams = layoutParams
                }

                searchText = newText
                bottomSheetDialog?.let {
                    presenter.searchUsers(
                            projectId,
                            searchText,
                            itemsSpec[it.spinner_spec_projects_filter.selectedIndex],
                            itemsExp[it.spinner_exp_projects_filter.selectedIndex],
                            itemsLvl[it.spinner_lvl_projects_filter.selectedIndex])
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()

                searchText = query
                bottomSheetDialog?.let {
                    presenter.searchUsers(
                            projectId,
                            searchText,
                            itemsSpec[it.spinner_spec_projects_filter.selectedIndex],
                            itemsExp[it.spinner_exp_projects_filter.selectedIndex],
                            itemsLvl[it.spinner_lvl_projects_filter.selectedIndex])
                }
                return true
            }
        })
        search_view_users.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                btn_search_filter_users.visibility = View.GONE
            }

            override fun onSearchViewShown() {
                btn_search_filter_users.visibility = View.VISIBLE
            }
        })
    }

    private fun initClickListeners() {
        btn_search_filter_users.setOnClickListener {
            bottomSheetDialog?.show()
        }

        bottomSheetDialog?.btn_show_results_projects_filter?.setOnClickListener {
            bottomSheetDialog?.let {
                presenter.searchUsers(
                        projectId,
                        searchText,
                        itemsSpec[it.spinner_spec_projects_filter.selectedIndex],
                        itemsExp[it.spinner_exp_projects_filter.selectedIndex],
                        itemsLvl[it.spinner_lvl_projects_filter.selectedIndex])
            }

            bottomSheetDialog?.dismiss()
        }
    }

    private fun onInviteClick(user: ProjectMember) {
        presenter.inviteUser(user, projectId)
        adapter?.setUserInvited(user)
    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(baseActivity, style.BottomSheetDialog)
        val sheetView = activity?.layoutInflater?.inflate(R.layout.bottom_sheet_search_filter, null)
        sheetView?.let { bottomSheetDialog?.setContentView(it) }

        itemsSpec = mutableListOf("Ничего не выбрано", "iOS Developer", "Android Developer", "Java Developer",
                "Designer", "Project Manager", "C# Developer", "RoR Developer", "Python Developer",
                "Frontend Developer", "Backend Developer", "SMM Manager", "System Administrator")
        itemsLvl = mutableListOf("Ничего не выбрано", "Junior", "Middle", "Senior")
        itemsExp = mutableListOf("Ничего не выбрано")

        for (i in 0..50) {
            itemsExp.add("$i")
        }

        bottomSheetDialog?.apply {
            bottomSheetDialog?.spinner_spec_projects_filter?.setItems(itemsSpec)
            bottomSheetDialog?.spinner_exp_projects_filter?.setItems(itemsExp)
            bottomSheetDialog?.spinner_lvl_projects_filter?.setItems(itemsLvl)
            spinner_spec_projects_filter.selectedIndex = 0
            spinner_lvl_projects_filter.selectedIndex = 0
            spinner_exp_projects_filter.selectedIndex = 0
        }
    }

    private fun onUserClick(userId: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProfileFragment.newInstance(userId), true)
    }

    override fun setUsers(users: MutableList<ProjectMember>) {
        adapter?.setUsers(users)
    }

    override fun onConnectionError() {
        showProgressError { presenter.getUsers(projectId) }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onInviteSuccess(user: ProjectMember) {
        Snackbar.make(rv_users, "Пользователь ${user.username} приглашен в проект", Snackbar.LENGTH_SHORT).show()
    }

    override fun hideInnerProgress() {
        progress_bar_users.visibility = View.GONE
        rv_users.visibility = View.VISIBLE
    }

    override fun showInnerProgress() {
        progress_bar_users.visibility = View.VISIBLE
        rv_users.visibility = View.GONE
    }
}