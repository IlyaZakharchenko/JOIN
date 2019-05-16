package itis.ru.kpfu.join.presentation.ui.main.projects.edit

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.ui.main.projects.add.AddProjectPresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class EditProjectPresenter @Inject constructor() : BasePresenter<EditProjectView>() {

    companion object {
        private const val ADD_SPEC_REQUEST_CODE = 1
    }

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    @JvmField
    var projectId: Long? = null
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getProject()
    }

    private fun getProject() {
        apiRequest.getProject(userRepository.getUser()?.token, projectId ?: -1L)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setProject(it, (it.leader?.id == userRepository.getUser()?.id), it
                            .participants?.map { it1 -> it1.id }?.contains(userRepository.getUser()?.id)
                            ?: false)

                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showRetry(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()
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

    fun onEditProject(project: ProjectModel) {
        project.userId = userRepository.getUser()?.id

        if (!hasErrors(project)) {
            apiRequest
                    .editProject(userRepository.getUser()?.token, project, projectId ?: -1L)
                    .doOnSubscribe { viewState.showWaitDialog() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        viewState.hideWaitDialog()
                        viewState.exit()
                    }, {
                        viewState.hideWaitDialog()
                        viewState.showErrorDialog("Ошибка при изменении проекта")
                    })
                    .disposeWhenDestroy()
        }
    }

    fun onRetry() {
        getProject()
    }
}