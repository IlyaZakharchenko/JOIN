package itis.ru.kpfu.join.mvp.presenter

import android.os.Handler
import android.util.Log
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
import io.reactivex.schedulers.Schedulers
import itis.ru.kpfu.join.api.TestApi
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.TestRepository
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.mvp.view.SignInView
import java.util.concurrent.TimeUnit.SECONDS

@InjectViewState
class SignInPresenter(private val api: TestApi, private val userRepository: UserRepository) :
        MvpPresenter<SignInView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getDataFromServer() {

        compositeDisposable.add(api
                .getData("bash", 50)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    run {
                        viewState.showResult(it[0].name.toString())
                        viewState.hideProgress()
                    }
                }, { viewState.onSignInError() }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun getVkUserInfo(res: VKAccessToken?, userRepository: UserRepository) {
        val parameters = VKParameters.from(VKApiConst.ACCESS_TOKEN, res)
        val request = VKRequest("account.getProfileInfo", parameters)

        request.executeWithListener(object : VKRequestListener() {
            override fun onComplete(response: VKResponse?) {
                super.onComplete(response)

                try {
                    val jsonObject = response?.json?.getJSONObject("response")

                    val firstName = jsonObject?.get("first_name") as String
                    val lastName = jsonObject.get("last_name") as String

                    val user = User(firstName = firstName, lastName = lastName)
                    userRepository.addUser(user)

                    viewState.signIn()
                } catch (e: Exception) {
                    viewState.onConnectionError()
                }
            }
        })
    }

    fun getGoogleUserInfo(account: GoogleSignInAccount, userRepository: UserRepository) {
        val givenName = account.givenName
        val lastName = account.familyName
        val email = account.email

        val user = User(firstName = givenName, lastName = lastName, email = email)
        userRepository.addUser(user)

        viewState.signIn()
    }

    fun getFacebookUserInfo(result: LoginResult?, userRepository: UserRepository) {

        Log.d("ASDASDASD", "asdasdasd")

        GraphRequest.newMeRequest(result?.accessToken
        ) { `object`, response ->
            val email = `object`?.get("email") as String
            val firstName = `object`.get("first_name") as String
            val lastName = `object`.get("last_name") as String

            val user = User(firstName = firstName, lastName = lastName, email = email)
            userRepository.addUser(user)

            viewState.signIn()
        }.executeAsync()
    }

    fun onCreateAccountClick() {
        viewState.openSignUpFragment()
    }
}