package itis.ru.kpfu.join.presentation.ui.main.profile

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.zxy.tiny.Tiny
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor() : MvpPresenter<ProfileView>() {

    companion object {
        private const val PROFILE_CHOOSE_IMAGE_REQUEST_CODE = 0
        private const val PROFILE_CHOOSE_IMAGE_LIMIT = 1
    }

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var api: JoinApi

    private val compositeDisposable = CompositeDisposable()

    fun getUser(userId: Long): User? {
        if (userId == userRepository.getUser()?.id || userId == -1L) {
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

    fun onPhotoChange(path: String, requestCode: Int) {
        if (requestCode == PROFILE_CHOOSE_IMAGE_REQUEST_CODE) {
            Tiny
                    .getInstance()
                    .source(path)
                    .asFile()
                    .compress { _, outFile, _ ->
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
                                                viewState.setChangedPhotoProfile(url)
                                            }
                                        }, {
                                            viewState.onConnectionError()
                                        })

                        )

                    }
        }
    }

    fun onChooseProfilePhoto() {
        viewState.showChooseImageDialog(PROFILE_CHOOSE_IMAGE_REQUEST_CODE, PROFILE_CHOOSE_IMAGE_LIMIT)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun exit() {
        userRepository.clearUser()
    }

    fun getUserFromDb(): User? {
        return userRepository.getUser()
    }
}