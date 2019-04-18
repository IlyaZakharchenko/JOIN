package itis.ru.kpfu.join.presentation.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<T : MvpView> : MvpPresenter<T>() {

    private var destroyDisposable = CompositeDisposable()

    private var detachViewDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
        if (!destroyDisposable.isDisposed) {
            destroyDisposable.dispose()
        }
    }

    override fun detachView(view: T) {
        super.detachView(view)
        if(!detachViewDisposable.isDisposed) {
            detachViewDisposable.dispose()
        }
    }

    protected fun Disposable.disposeWhenDestroy() {
        destroyDisposable.add(this)
    }

    protected fun Disposable.disposeWhenDetachView() {
        detachViewDisposable.add(this)
    }
}