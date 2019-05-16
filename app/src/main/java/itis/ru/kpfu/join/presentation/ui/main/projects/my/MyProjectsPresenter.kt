package itis.ru.kpfu.join.presentation.ui.main.projects.my

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.Subject
import itis.ru.kpfu.join.data.EventType
import itis.ru.kpfu.join.data.LeaveFromProjectEvent
import itis.ru.kpfu.join.data.ProjectAddedEvent
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.ProjectModel
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class MyProjectsPresenter @Inject constructor() : BasePresenter<MyProjectsView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor
    @Inject
    lateinit var eventSubject: Subject<EventType>

    fun onRetry() {
        Observable.concat(
                getProjectsObservable()
                        .doOnSubscribe {
                            viewState.hideRetry()
                            viewState.showProgress()
                        }
                        .doAfterTerminate { viewState.hideProgress() },
                eventSubject.filter { it is ProjectAddedEvent || it is LeaveFromProjectEvent }
                        .flatMap { getProjectsObservable() })
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        update()
    }

    private fun update() {
        Observable.concat(
                getProjectsObservable()
                        .doOnSubscribe { viewState.showProgress() }
                        .doAfterTerminate { viewState.hideProgress() },
                eventSubject.filter { it is ProjectAddedEvent }
                        .flatMap { getProjectsObservable() })
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


    fun onAddProject() {
        viewState.setAddProjectFragment()
    }

    fun onProjectDetails(id: Long) {
        viewState.setProjectDetailsFragment(id)
    }

    fun onDeleteProject(id: Long) {
        apiRequest
                .deleteProject(userRepository.getUser()?.token, id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    update()
                }, {
                    viewState.showErrorDialog("Ошибка при удалении проекта")
                }).disposeWhenDestroy()
    }

    private fun getProjectsObservable(): Observable<List<ProjectModel>> {
        return apiRequest.getMyProjects(userRepository.getUser()?.token, userRepository.getUser()?.id)
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
    }
}