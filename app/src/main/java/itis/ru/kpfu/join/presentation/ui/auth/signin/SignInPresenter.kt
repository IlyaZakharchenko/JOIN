package itis.ru.kpfu.join.presentation.ui.auth.signin

import com.arellomobile.mvp.InjectViewState
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.vk.sdk.VKAccessToken
import com.vk.sdk.api.VKApiConst
import com.vk.sdk.api.VKParameters
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKRequest.VKRequestListener
import com.vk.sdk.api.VKResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import itis.ru.kpfu.join.data.network.request.JoinApiRequest
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.SignInFormModel
import itis.ru.kpfu.join.presentation.util.Validator
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import javax.inject.Inject

@InjectViewState
class SignInPresenter @Inject constructor() : BasePresenter<SignInView>() {

    @Inject
    lateinit var apiRequest: JoinApiRequest
    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var exceptionProcessor: ExceptionProcessor

    fun onSignIn(email: String, password: String) {
        if (!Validator.isEmailValid(email)) {
            viewState.setEmailErrorEnabled(true)
            return
        } else if (!Validator.isPasswordValid(password)) {
            viewState.setPasswordErrorEnabled(true)
            return
        } else {
            viewState.setPasswordErrorEnabled(false)
            viewState.setEmailErrorEnabled(false)
        }

        var token: String? = null
        apiRequest
                .signIn(SignInFormModel(email, password))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    viewState.showWaitDialog()
                    viewState.hideKeyboard()
                }
                .doAfterTerminate { viewState.hideWaitDialog() }
                .flatMap {
                    token = it.token
                    apiRequest.getUserInfo(token, it.userId)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    userRepository.addUser(user.apply { this.token = token })
                    viewState.setAllProjectsFragment()
                }, {
                    viewState.showErrorDialog(exceptionProcessor.processException(it))
                }).disposeWhenDestroy()
    }

    fun getVkUserInfo(res: VKAccessToken?) {
        val parameters = VKParameters.from(VKApiConst.ACCESS_TOKEN, res)
        val request = VKRequest("account.getProfileInfo", parameters)

        request.executeWithListener(object : VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                try {
                    val jsonObject = response?.json?.getJSONObject("response")

                    val firstName = jsonObject?.get("first_name") as String
                    val lastName = jsonObject.get("last_name") as String

                    val user = User(name = firstName, lastname = lastName)
                    userRepository.addUser(user)

                    viewState.setAllProjectsFragment()
                } catch (e: Exception) {
                    viewState.showErrorDialog(exceptionProcessor.processException(e))
                }
            }
        })
    }

    fun onRestorePassword() {
        viewState.setRestorePasswordFragment()
    }

    fun getGoogleUserInfo(account: GoogleSignInAccount?) {
        val givenName = account?.givenName
        val lastName = account?.familyName
        val email = account?.email

        val user = User(name = givenName, lastname = lastName, email = email)
        userRepository.addUser(user)

        viewState.setAllProjectsFragment()
    }

    fun getFacebookUserInfo(result: LoginResult?) {

        GraphRequest.newMeRequest(result?.accessToken
        ) { `object`, response ->
            val email = `object`?.get("email") as String
            val firstName = `object`.get("first_name") as String
            val lastName = `object`.get("last_name") as String

            val user = User(name = firstName, lastname = lastName, email = email)
            userRepository.addUser(user)

            viewState.setAllProjectsFragment()
        }.executeAsync()
    }

    fun onCreateAccount() {
        viewState.setSignUpFragment()
    }
}