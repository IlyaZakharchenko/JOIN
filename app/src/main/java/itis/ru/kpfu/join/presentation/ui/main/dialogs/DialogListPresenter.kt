package itis.ru.kpfu.join.presentation.ui.main.dialogs

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import itis.ru.kpfu.join.data.network.firebase.repository.DialogListRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class DialogListPresenter @Inject constructor() : BasePresenter<DialogListView>() {

    @Inject
    lateinit var dialogsRepository: DialogListRepository

    override fun attachView(view: DialogListView?) {
        super.attachView(view)
        update()
    }

    private fun update() {
        dialogsRepository
                .getDialogs()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showProgress()
                    viewState.hideRetry()
                }.doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.setDialogs(it)
                }, {
                    viewState.showRetry("Ошибка при загрузке данных")
                })
                .disposeWhenDestroy()
    }

    fun onRetry() {
        update()
    }

    fun onChat() {

    }
}