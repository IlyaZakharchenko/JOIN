package itis.ru.kpfu.join.presentation.ui.main.profile

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.zxy.tiny.Tiny
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import itis.ru.kpfu.join.data.network.exception.NotAuthorizedException
import itis.ru.kpfu.join.data.network.firebase.repository.DialogListRepository
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@InjectViewState
class ProfilePresenter @Inject constructor() : BasePresenter<ProfileView>() {

    companion object {
        private const val PROFILE_CHOOSE_PHOTO_REQUEST_CODE = 0
        private const val PROFILE_CHOOSE_PHOTO_LIMIT = 1
    }

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var dialogListRepository: DialogListRepository
    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    @Inject
    @JvmField
    var userId: Long? = null

    private fun isOwner(): Boolean = userId == userRepository.getUser()?.id

    fun onRetry() {
        getUser()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getUser()
    }

    private fun getUser() {
        Observable.fromCallable {
            viewState.setBackArrowEnabled(!isOwner())
            userRepository.getUser()
        }.flatMap {
            if (isOwner()) {
                Observable.just(it)
            } else {
                apiRequest.getUserInfo(userRepository.getUser()?.token, userId)
            }
        }.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.hideCollapsingToolbar()
                    viewState.hideRetry()
                    viewState.showProgress()
                }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    viewState.showCollapsingToolbar()
                    viewState.setUser(it, isOwner())
                }, {
                    if (it is NotAuthorizedException) {
                        viewState.setSignInFragment()
                    } else {
                        viewState.showRetry(exceptionProcessor.processException(it))
                    }
                })
                .disposeWhenDestroy()
    }

    fun onChoosePhotoResult(path: String, requestCode: Int) {
        if (requestCode == PROFILE_CHOOSE_PHOTO_REQUEST_CODE) {
            Tiny
                    .getInstance()
                    .source(path)
                    .asFile()
                    .compress { _, outFile, _ ->
                        val file = File(outFile)

                        val fBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                                file)
                        val body = MultipartBody.Part.createFormData("file", file.name, fBody)


                        apiRequest.changeProfileImage(userRepository.getUser()?.token, userRepository.getUser()?.id, body)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe { viewState.showWaitDialog() }
                                .doAfterTerminate { viewState.hideWaitDialog() }
                                .subscribe({
                                    it.url?.let { url ->
                                        userRepository.changeImageProfile(url)
                                        viewState.setChangedPhotoProfile(url)
                                    }
                                }, {
                                    if(it is NotAuthorizedException) {
                                        viewState.setSignInFragment()
                                    } else {
                                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                                    }
                                })
                                .disposeWhenDestroy()

                    }
        }
    }

    fun onChoosePhoto() {
        viewState.showChooseImageDialog(PROFILE_CHOOSE_PHOTO_REQUEST_CODE, PROFILE_CHOOSE_PHOTO_LIMIT)
    }

    fun onLogout() {
        userRepository.clearUser()
        viewState.setSignInFragment()
    }

    fun onEditProfile() {
        viewState.setChangeProfileFragment()
    }

    fun onOpenChat() {
        dialogListRepository
                .createDialog(userId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showWaitDialog() }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .subscribe({
                    viewState.setDialogFragment(it)
                }, {
                    viewState.showErrorDialog("Ошибка при создании диалога")
                })
                .disposeWhenDestroy()
    }
}