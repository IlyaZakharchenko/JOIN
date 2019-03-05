package itis.ru.kpfu.join.presentation.ui.auth.signin

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
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
import io.reactivex.disposables.CompositeDisposable
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import javax.inject.Inject

@InjectViewState
class SignInPresenter @Inject constructor() : MvpPresenter<SignInView>() {

    @Inject
    lateinit var api: JoinApi
    @Inject
    lateinit var userRepository: UserRepository


    private val compositeDisposable = CompositeDisposable()

    fun signIn(email: String, password: String) {
        compositeDisposable.add(api
                .signIn(User(email = email.trim(), password = password.trim()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe({
                    if (it.code() == 200) {
                        val id = it.body()?.get("user_id")?.asLong
                        val token = it.headers().get("Authorization")

                        compositeDisposable.add(api.getUserInfo(token, id)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe { viewState.showProgress() }
                                .subscribe({
                                    it.token = token

                                    userRepository.addUser(it)
                                    viewState.signIn()
                                }, { viewState.onConnectionError() }))
                    } else {
                        viewState.onSignInError()
                    }
                }, {
                    viewState.onConnectionError()
                }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
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

                    viewState.signIn()
                } catch (e: Exception) {
                    viewState.onConnectionError()
                }
            }
        })
    }

    fun getGoogleUserInfo(account: GoogleSignInAccount?) {
        val givenName = account?.givenName
        val lastName = account?.familyName
        val email = account?.email

        val user = User(name = givenName, lastname = lastName, email = email)
        userRepository.addUser(user)

        viewState.signIn()
    }

    fun getFacebookUserInfo(result: LoginResult?) {

        GraphRequest.newMeRequest(result?.accessToken
        ) { `object`, response ->
            val email = `object`?.get("email") as String
            val firstName = `object`.get("first_name") as String
            val lastName = `object`.get("last_name") as String

            val user = User(name = firstName, lastname = lastName, email = email)
            userRepository.addUser(user)

            viewState.signIn()
        }.executeAsync()
    }

    fun onCreateAccountClick() {
        viewState.openSignUpFragment()
    }
}