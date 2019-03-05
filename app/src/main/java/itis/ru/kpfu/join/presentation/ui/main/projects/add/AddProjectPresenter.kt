package itis.ru.kpfu.join.presentation.ui.main.projects.add

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.network.pojo.Project
import itis.ru.kpfu.join.db.repository.UserRepository
import javax.inject.Inject

@InjectViewState
class AddProjectPresenter @Inject constructor() : MvpPresenter<AddProjectView>() {

    @Inject
    lateinit var api: JoinApi
    @Inject
    lateinit var userRepository: UserRepository

    private val compositeDisposable = CompositeDisposable()

    fun saveProject(project: Project) {
        project.userId = userRepository.getUser()?.id

        if (!hasErrors(project)) {
            compositeDisposable.add(
                    api.addProject(userRepository.getUser()?.token, project)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { viewState.showProgress() }
                            .doAfterTerminate { viewState.hideProgress() }
                            .subscribe({ viewState.onSaveSuccess() }, { viewState.onConnectionError() })
            )
        }
    }

    private fun hasErrors(project: Project): Boolean {
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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}