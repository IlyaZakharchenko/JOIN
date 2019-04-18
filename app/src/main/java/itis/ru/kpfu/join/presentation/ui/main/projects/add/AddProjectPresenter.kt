package itis.ru.kpfu.join.presentation.ui.main.projects.add

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class AddProjectPresenter @Inject constructor() : BasePresenter<AddProjectView>() {

    companion object {
        private const val ADD_SPEC_REQUEST_CODE = 1
    }

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun saveProject(project: ProjectModel) {
        project.userId = userRepository.getUser()?.id

        if (!hasErrors(project)) {
            apiRequest.addProject(userRepository.getUser()?.token, project)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        viewState.showWaitDialog()
                        viewState.hideKeyboard()
                    }
                    .doAfterTerminate { viewState.hideWaitDialog() }
                    .subscribe({
                        viewState.onSaveSuccess()
                    }, {
                        if (it is NotAuthorizedException) {
                            viewState.setSignInFragment()
                        } else {
                            viewState.showErrorDialog(exceptionProcessor.processException(it))
                        }
                    })
                    .disposeWhenDestroy()
        }
    }

    private fun hasErrors(project: ProjectModel): Boolean {
        var hasErrors = false

        if (project.name.isNullOrEmpty()) {
            viewState.onNameEmpty()
            hasErrors = true
        } else if (project.description.isNullOrEmpty()) {
            viewState.onDescriptionEmpty()
            hasErrors = true
        } else if (project.vacancies?.size == 0) {
            viewState.onJobsEmpty()
            hasErrors = true
        }

        return hasErrors
    }

    fun onAddSpec() {
        viewState.showAddSpecDialog(ADD_SPEC_REQUEST_CODE, -1, null)
    }

    fun onEditSpec(position: Int, spec: Specialization?) {
        viewState.showAddSpecDialog(ADD_SPEC_REQUEST_CODE, position, spec)
    }

    fun onAddSpecResult(spec: Specialization, position: Int, requestCode: Int) {
        if (requestCode == ADD_SPEC_REQUEST_CODE) {
            if (position == -1) {
                viewState.addSpec(spec)
            } else {
                viewState.updateSpec(position, spec)
            }
        }
    }

    fun onRemoveSpec(position: Int) {
        viewState.removeSpec(position)
    }

}