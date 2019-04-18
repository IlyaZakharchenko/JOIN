package itis.ru.kpfu.join.presentation.ui.main.projects.add

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.dialog.AddSpecializationDialog
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import kotlinx.android.synthetic.main.fragment_add_project.btn_add_job
import kotlinx.android.synthetic.main.fragment_add_project.et_project_desc
import kotlinx.android.synthetic.main.fragment_add_project.et_project_name
import kotlinx.android.synthetic.main.fragment_add_project.rv_add_project_jobs
import kotlinx.android.synthetic.main.fragment_add_project.toolbar_add_project
import javax.inject.Inject
import javax.inject.Provider

class AddProjectFragment : BaseFragment(), AddProjectView {

    companion object {
        private const val TAG_ADD_JOBS_DIALOG = "tag add jobs dialog"

        fun newInstance(): AddProjectFragment {
            val args = Bundle()
            val fragment = AddProjectFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_add_project

    override val toolbarTitle: Int?
        get() = R.string.toolbar_add_project

    override val menu: Int?
        get() = R.menu.menu_edit_profile

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_add_project

    @InjectPresenter
    lateinit var presenter: AddProjectPresenter

    @Inject
    lateinit var presenterProvider: Provider<AddProjectPresenter>

    @Inject
    lateinit var adapter: SpecializationsEditAdapter

    @ProvidePresenter
    fun providePresenter(): AddProjectPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onItemEdit = { position, spec -> presenter.onEditSpec(position, spec) }
        adapter.onItemRemove = { position -> presenter.onRemoveSpec(position) }

        rv_add_project_jobs.adapter = adapter
        rv_add_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initClickListeners() {
        btn_add_job.setOnClickListener { presenter.onAddSpec() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile_edit_save -> {
                val project = ProjectModel(name = et_project_name.text?.trim().toString(),
                        description = et_project_desc.text?.trim().toString(), vacancies = adapter.items)

                presenter.saveProject(project)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttachFragment(childFragment: Fragment?) {
        super.onAttachFragment(childFragment)
        if (childFragment is AddSpecializationDialog) {
            childFragment.onSave = { spec, position, requestCode ->
                presenter.onAddSpecResult(spec, position, requestCode)
            }
        }
    }

    override fun onSaveSuccess() {
        Toast.makeText(baseActivity, "Проект успешно создан", Toast.LENGTH_SHORT).show()
        baseActivity.onBackPressed()
    }

    override fun onNameEmpty() {
        Toast.makeText(baseActivity, "Заполните поле названия проекта", Toast.LENGTH_SHORT).show()
    }

    override fun onDescriptionEmpty() {
        Toast.makeText(baseActivity, "Заполните поле описания проекта", Toast.LENGTH_SHORT).show()
    }

    override fun onJobsEmpty() {
        Toast.makeText(baseActivity, "Добавьте хотя бы одну вакансию", Toast.LENGTH_SHORT).show()
    }

    override fun updateSpec(position: Int, spec: Specialization) {
        adapter.updateItem(position, spec)
    }

    override fun addSpec(spec: Specialization) {
        adapter.addItem(spec)
    }

    override fun removeSpec(position: Int) {
        adapter.removeItem(position)
    }

    override fun showAddSpecDialog(requestCode: Int, position: Int, spec: Specialization?) {
        AddSpecializationDialog.getInstance(requestCode, position, spec).show(childFragmentManager, TAG_ADD_JOBS_DIALOG)
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, clearStack = true)
    }
}