package itis.ru.kpfu.join.presentation.ui.main.projects.edit

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
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.dialog.AddSpecializationDialog
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import kotlinx.android.synthetic.main.fragment_edit_project.*
import kotlinx.android.synthetic.main.fragment_edit_project.btn_add_job
import kotlinx.android.synthetic.main.fragment_edit_project.et_project_desc
import kotlinx.android.synthetic.main.fragment_edit_project.et_project_name
import kotlinx.android.synthetic.main.layout_progress_error.*
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class EditProjectFragment : BaseFragment(), EditProjectView {

    companion object {
        private const val TAG_ADD_JOBS_DIALOG = "TAG_ADD_JOBS_DIALOG"
        private const val KEY_PROJECT_ID = "KEY_PROJECT_ID"

        fun getInstance(id: Long) = EditProjectFragment().also {
            it.arguments = Bundle().apply {
                putLong(KEY_PROJECT_ID, id)
            }
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_edit_project

    override val toolbarTitle: Int?
        get() = R.string.edit_project

    override val menu: Int?
        get() = R.menu.menu_edit_profile

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_edit_project

    @InjectPresenter
    lateinit var presenter: EditProjectPresenter

    @Inject
    lateinit var presenterProvider: Provider<EditProjectPresenter>

    @Inject
    lateinit var adapter: SpecializationsEditAdapter

    @ProvidePresenter
    fun providePresenter(): EditProjectPresenter = presenterProvider.get()

    fun getProjectId() = arguments?.getLong(KEY_PROJECT_ID)
            ?: throw  IllegalArgumentException("project id is null")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onItemEdit = { position, spec -> presenter.onEditSpec(position, spec) }
        adapter.onItemRemove = { position -> presenter.onRemoveSpec(position) }

        rv_edit_project_jobs.adapter = adapter
        rv_edit_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initClickListeners() {
        btn_add_job.setOnClickListener { presenter.onAddSpec() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile_edit_save -> {
                val project = ProjectModel(name = et_project_name.text?.trim().toString(),
                        description = et_project_desc.text?.trim().toString(), vacancies = adapter.items)

                presenter.onEditProject(project)
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

    override fun addSpec(spec: Specialization) {
        adapter.addItem(spec)
    }

    override fun onDescriptionEmpty() {
        Toast.makeText(baseActivity, "Заполните поле описания проекта", Toast.LENGTH_SHORT).show()
    }

    override fun onJobsEmpty() {
        Toast.makeText(baseActivity, "Добавьте хотя бы одну вакансию", Toast.LENGTH_SHORT).show()
    }

    override fun onNameEmpty() {
        Toast.makeText(baseActivity, "Заполните поле названия проекта", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveSuccess() {
        Toast.makeText(baseActivity, "Проект успешно создан", Toast.LENGTH_SHORT).show()
        (activity as? FragmentHostActivity)?.onBackPressed()
    }

    override fun removeSpec(position: Int) {
        adapter.removeItem(position)
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, clearStack = true)
    }

    override fun showAddSpecDialog(requestCode: Int, position: Int, spec: Specialization?) {
        AddSpecializationDialog.getInstance(requestCode, position, spec).show(childFragmentManager, TAG_ADD_JOBS_DIALOG)
    }

    override fun updateSpec(position: Int, spec: Specialization) {
        adapter.updateItem(position, spec)
    }

    override fun setProject(item: ProjectModel, isMyProject: Boolean, isInProject: Boolean) {
        et_project_name.setText(item.name)
        et_project_desc.setText(item.description)

        item.vacancies?.forEach {
            adapter.addItem(it)
        }
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

    override fun exit() {
        (activity as? FragmentHostActivity)?.onBackPressed()
    }
}
