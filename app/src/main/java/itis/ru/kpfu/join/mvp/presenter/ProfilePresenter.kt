package itis.ru.kpfu.join.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.esafirm.imagepicker.model.Image
import com.zxy.tiny.Tiny
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.api.JoinApi
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.ProfileView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@InjectViewState
class ProfilePresenter(private val userRepository: UserRepository, private val api: JoinApi) :
        MvpPresenter<ProfileView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getUser(userId: Long): User? {
        if (userId == -1L) {
            return userRepository.getUser()
        } else {
            compositeDisposable.add(
                    api.getUserInfo(userRepository.getUser()?.token, userId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe { viewState.showProgress() }
                            .doAfterTerminate { viewState.hideProgress() }
                            .subscribe({ viewState.initFields(it) }, { viewState.onConnectionError() })
            )
        }

        return null
    }

    fun changeProfileImage(image: Image) {

        Tiny
                .getInstance()
                .source(image.path)
                .asFile()
                .compress{_, outFile, _ ->
                    val file = File(outFile)

                    val fBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                            file)
                    val body = MultipartBody.Part.createFormData("file", file.name, fBody)

                    compositeDisposable.add(
                            api.changeProfileImage(userRepository.getUser()?.token, userRepository.getUser()?.id, body)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .doOnSubscribe { viewState.showProgress() }
                                    .doAfterTerminate { viewState.hideProgress() }
                                    .subscribe({
                                        it.url?.let { url ->
                                            userRepository.changeImageProfile(url)
                                            viewState.onImageSetSuccess(url)
                                        }
                                    }, {
                                        viewState.onConnectionError()
                                    })

                    )

                }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}