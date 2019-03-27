package itis.ru.kpfu.join.presentation.ui.main.profile.edit

import com.arellomobile.mvp.InjectViewState
import com.zxy.tiny.Tiny
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.data.network.request.JoinApiRequest
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@InjectViewState
class ProfileEditPresenter @Inject constructor() : BasePresenter<ProfileEditView>() {

    companion object {
        private const val ADD_SPEC_REQUEST_CODE = 1
        private const val CHOOSE_PHOTO_REQUEST_CODE = 2

        private const val IMAGES_LIMIT = 1
    }

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initFields(userRepository.getUser())
    }

    fun onChoosePhoto() {
        viewState.showChooseImageDialog(CHOOSE_PHOTO_REQUEST_CODE, IMAGES_LIMIT)
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

    fun onUpdateUser(user: User?) {
        user?.id = userRepository.getUser()?.id
        user?.profileImage = userRepository.getUser()?.profileImage
        if (!hasErrors(user)) {
            apiRequest
                    .changeUser(userRepository.getUser()?.token, user, user?.id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { viewState.showWaitDialog() }
                    .subscribe({
                        viewState.hideWaitDialog()
                        user?.let { it1 -> userRepository.updateUser(it1) }
                        viewState.onEditSuccess()
                    }, {
                        viewState.hideWaitDialog()
                        viewState.showErrorDialog(exceptionProcessor.processException(it))
                    })
                    .disposeWhenDestroy()
        }
    }

    fun onChoosePhotoResult(path: String, requestCode: Int) {
        if (requestCode == CHOOSE_PHOTO_REQUEST_CODE) {
            Tiny
                    .getInstance()
                    .source(path)
                    .asFile()
                    .compress { _, outFile, _ ->

                        val file = File(outFile)
                        val fBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                                file)
                        val body = MultipartBody.Part.createFormData("file", file.name, fBody)

                        apiRequest.changeProfileImage(userRepository.getUser()?.token, userRepository.getUser()?.id,
                                body)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe { viewState.showWaitDialog() }
                                .doAfterTerminate { viewState.hideWaitDialog() }
                                .subscribe({
                                    it.url?.let { url ->
                                        userRepository.changeImageProfile(url)
                                        viewState.onImageSetSuccess(url)
                                    }
                                }, {
                                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                                })
                                .disposeWhenDestroy()
                    }
        }
    }

    private fun hasErrors(user: User?): Boolean {
        var hasError = false

        if (user?.name.isNullOrEmpty()) {
            viewState.onEmptyName()
            hasError = true
        }
        if (user?.lastname.isNullOrEmpty()) {
            viewState.onEmptySurname()
            hasError = true
        }
        if (user?.email.isNullOrEmpty()) {
            viewState.onEmptyEmail()
            hasError = true
        }
        if (user?.username.isNullOrEmpty()) {
            viewState.onEmptyUsername()
            hasError = true
        }
        if (user?.phoneNumber.isNullOrEmpty()) {
            viewState.onEmptyPhoneNumber()
            hasError = true
        }
        if (user?.phoneNumber?.trim()?.length != 10) {
            viewState.onInvalidPhoneNumber()
            hasError = true
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user?.email?.trim()).matches()) {
            viewState.onInvalidEmail()
            hasError = true
        }
        if (user?.username?.trim()?.length ?: 0 < 6) {
            viewState.onInvalidUsername()
            hasError = true
        }
        if (user?.specializations?.size == 0) {
            viewState.onEmptySpecializations()
            hasError = true
        }

        return hasError
    }
}