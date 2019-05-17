package itis.ru.kpfu.join.presentation.ui.main.projects.all

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class AllProjectsPresenter @Inject constructor() : BasePresenter<AllProjectsView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getProjects()
    }

    fun onRetry() {
        getProjects()
    }

    fun onShowSearchFilters() {
        viewState.showBottomSheetDialog()
    }

    fun onSearch(projectName: String?, specName: String? = null, exp: String? = null,
                 lvl: String? = null) {

        val spec = if (specName == "Ничего не выбрано") null else specName
        val experience = if (exp == "Ничего не выбрано") null else exp
        var level = if (lvl == "Ничего не выбрано") null else lvl

        when (level) {
            "Junior" -> level = "0"
            "Middle" -> level = "1"
            "Senior" -> level = "2"
        }

        apiRequest.getProjects(userRepository.getUser()?.token, projectName, spec, level, experience)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate {
                    viewState.hideBottomSheetDialog()
                    viewState.hideProgress()
                }
                .subscribe({
                    viewState.setProjects(it)
                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()

    }

    private fun getProjects() {
        apiRequest.getProjects(userRepository.getUser()?.token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.hideRetry()
                    viewState.showProgress()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setProjects(it)
                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showRetry(exceptionProcessor.processException(it))
                    }
                }).disposeWhenDestroy()
    }

    fun onDeleteProject(id: Long) {
        apiRequest
                .deleteProject(userRepository.getUser()?.token, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getProjects()
                }, {
                    viewState.showErrorDialog("Ошибка при удалении проекта")
                }).disposeWhenDestroy()
    }

    fun onEditProject(id: Long) {
        viewState.setEditProjectFragment(id)
    }
}
