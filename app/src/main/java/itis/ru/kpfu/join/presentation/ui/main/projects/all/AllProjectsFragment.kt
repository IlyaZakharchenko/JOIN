package itis.ru.kpfu.join.presentation.ui.main.projects.all

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.adapter.ProjectsAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.details.ProjectDetailsFragment
import itis.ru.kpfu.join.presentation.util.toPx
import kotlinx.android.synthetic.main.activity_fragment_host.*
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.btn_show_results_projects_filter
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.spinner_exp_projects_filter
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.spinner_lvl_projects_filter
import kotlinx.android.synthetic.main.bottom_sheet_search_filter.spinner_spec_projects_filter
import kotlinx.android.synthetic.main.fragment_all_projects.*
import kotlinx.android.synthetic.main.layout_progress_error.*
import javax.inject.Inject
import javax.inject.Provider

class AllProjectsFragment : BaseFragment(), AllProjectsView {

    companion object {

        fun newInstance(): AllProjectsFragment {
            val args = Bundle()
            val fragment = AllProjectsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_all_projects

    override val toolbarTitle: Int?
        get() = R.string.projects_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_projects

    private var isSet: Boolean = false
    private var bottomSheetDialog: BottomSheetDialog? = null

    private var searchText: String? = null

    private lateinit var itemsSpec: MutableList<String>
    private lateinit var itemsLvl: MutableList<String>
    private lateinit var itemsExp: MutableList<String>

    @InjectPresenter
    lateinit var presenter: AllProjectsPresenter

    @Inject
    lateinit var presenterProvider: Provider<AllProjectsPresenter>

    @Inject
    lateinit var adapter: ProjectsAdapter

    @ProvidePresenter
    fun providePresenter(): AllProjectsPresenter = presenterProvider.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBottomSheetDialog()
        initRecyclerView()
        initClickListeners()
    }

    private fun initBottomSheetDialog() {
        bottomSheetDialog = BottomSheetDialog(baseActivity, R.style.BottomSheetDialog)
        val sheetView = activity?.layoutInflater?.inflate(R.layout.bottom_sheet_search_filter, null)
        sheetView?.let { bottomSheetDialog?.setContentView(it) }

        itemsSpec = mutableListOf("Ничего не выбрано", "iOS Developer", "Android Developer", "Java Developer",
                "Designer", "ProjectModel Manager", "C# Developer", "RoR Developer", "Python Developer",
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

    private fun initClickListeners() {
        btn_search_filter_projects.setOnClickListener {
            presenter.onShowSearchFilters()
        }

        bottomSheetDialog?.btn_show_results_projects_filter?.setOnClickListener {
            bottomSheetDialog?.let {
                presenter.onSearch(
                        searchText,
                        itemsSpec[it.spinner_spec_projects_filter.selectedIndex],
                        itemsExp[it.spinner_exp_projects_filter.selectedIndex],
                        itemsLvl[it.spinner_lvl_projects_filter.selectedIndex])
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
        menu?.findItem(R.id.action_search)?.let { initSearchView(it) }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchView(item: MenuItem) {
        val layoutParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER or Gravity.END

        search_view_projects.setMenuItem(item)
        search_view_projects.setHint("Поиск")
        search_view_projects.closeSearch()
        search_view_projects.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {

                if (newText.isEmpty()) {
                    layoutParams.setMargins(0, 0, toPx(16, baseActivity), 0)
                    isSet = false
                    btn_search_filter_projects.layoutParams = layoutParams

                    bottomSheetDialog?.apply {
                        spinner_spec_projects_filter.selectedIndex = 0
                        spinner_lvl_projects_filter.selectedIndex = 0
                        spinner_exp_projects_filter.selectedIndex = 0
                    }
                } else if (!isSet && !newText.isEmpty()) {
                    layoutParams.setMargins(0, 0, toPx(48, baseActivity), 0)
                    isSet = true
                    btn_search_filter_projects.layoutParams = layoutParams
                }

                searchText = newText
                bottomSheetDialog?.let {
                    presenter.onSearch(
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
                    presenter.onSearch(
                            searchText,
                            itemsSpec[it.spinner_spec_projects_filter.selectedIndex],
                            itemsExp[it.spinner_exp_projects_filter.selectedIndex],
                            itemsLvl[it.spinner_lvl_projects_filter.selectedIndex])
                }
                return true
            }
        })
        search_view_projects.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                btn_search_filter_projects.visibility = View.GONE
            }

            override fun onSearchViewShown() {
                btn_search_filter_projects.visibility = View.VISIBLE
            }
        })
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

    override fun setProjects(projects: List<ProjectModel>) {
        adapter.items = projects
    }

    private fun initRecyclerView() {
        adapter.onProjectClick = { onProjectClick(it) }
        rv_projects.adapter = adapter
        rv_projects.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onProjectClick(id: Long) {
        (activity as? FragmentHostActivity)?.setFragment(ProjectDetailsFragment.newInstance(id), true)
    }

    override fun showBottomSheetDialog() {
        bottomSheetDialog?.show()
    }

    override fun hideBottomSheetDialog() {
        bottomSheetDialog?.dismiss()
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, clearStack = true)
    }
}