package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.mvp.presenter.AddProjectPresenter
import itis.ru.kpfu.join.mvp.view.AddPojectView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.ui.fragment.dialog.AddSpecializationDialog
import itis.ru.kpfu.join.ui.recyclerView.adapter.SpecializationsEditAdapter
import kotlinx.android.synthetic.main.fragment_add_project.btn_add_job
import kotlinx.android.synthetic.main.fragment_add_project.et_project_desc
import kotlinx.android.synthetic.main.fragment_add_project.et_project_name
import kotlinx.android.synthetic.main.fragment_add_project.rv_add_project_jobs
import kotlinx.android.synthetic.main.fragment_add_project.toolbar_add_project

class AddProjectFragment : BaseFragment(), AddPojectView {

    companion object {
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

    private var adapter: SpecializationsEditAdapter? = null

    @InjectPresenter
    lateinit var presenter: AddProjectPresenter

    @ProvidePresenter
    fun providePresenter(): AddProjectPresenter {
        return JoinApplication.appComponent.providePresenters().provideAddProjectPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = SpecializationsEditAdapter(ArrayList(), { p, s -> onItemRemove(p, s) },
                { p, s -> onItemEdit(p, s) })

        rv_add_project_jobs.adapter = adapter
        rv_add_project_jobs.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initClickListeners() {
        btn_add_job.setOnClickListener {
            val addSpecializationDialog = AddSpecializationDialog.newInstance(null, -1)
            addSpecializationDialog.setTargetFragment(this, AddSpecializationDialog.REQUEST_CODE)
            addSpecializationDialog.show(baseActivity.supportFragmentManager, AddSpecializationDialog.SPECIALIZATION)
        }
    }

    private fun onItemEdit(position: Int, spec: Specialization) {
        val addSpecializationDialog = AddSpecializationDialog.newInstance(spec, position)
        addSpecializationDialog.setTargetFragment(this, AddSpecializationDialog.REQUEST_CODE)
        addSpecializationDialog.show(baseActivity.supportFragmentManager, AddSpecializationDialog.SPECIALIZATION);
    }

    private fun onItemRemove(position: Int, spec: Specialization) {
        adapter?.removeItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AddSpecializationDialog.REQUEST_CODE) {
            val result = data?.getSerializableExtra(AddSpecializationDialog.RESULT_SPEC) as Specialization
            val position = data.getIntExtra(AddSpecializationDialog.RESULT_POS, -1)

            if (position == -1) adapter?.addItem(result) else adapter?.updateItem(position, result)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile_edit_save -> {
                val project = Project(name = et_project_name.text?.trim().toString(),
                        description = et_project_desc.text?.trim().toString(), vacancies = adapter?.getItems())

                presenter.saveProject(project)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveSuccess() {
        Toast.makeText(baseActivity, "Проект успешно создан", Toast.LENGTH_SHORT).show()
        baseActivity.onBackPressed()
    }

    override fun onConnectionError() {
        Toast.makeText(baseActivity, "Internet connection error", Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
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
}