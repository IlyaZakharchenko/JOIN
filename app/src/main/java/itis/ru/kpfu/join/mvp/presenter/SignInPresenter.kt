package itis.ru.kpfu.join.mvp.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
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
import itis.ru.kpfu.join.db.repository.impl.TestRepositoryImpl
import itis.ru.kpfu.join.mvp.view.SignInView

@InjectViewState
class SignInPresenter : MvpPresenter<SignInView>() {

    private val compositeDisposable = CompositeDisposable()

    fun getDataFromServer(api: TestApi, repo: TestRepositoryImpl) {
        compositeDisposable.add(api
                .getData("bash", 50)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress() }
                .doAfterTerminate { viewState.hideProgress() }
                .subscribe(
                        {
                            repo.addTests(it)
                            repo.getTests()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .subscribe { result -> viewState.showResult(result.toString()) }
                        },
                        {
                            viewState.onConnectionError()
                        }))
    }

    override fun destroyView(view: SignInView?) {
        super.destroyView(view)
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

                    val firstName = jsonObject?.get("first_name")
                    val lastName = jsonObject?.get("last_name")

                    viewState.signIn()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getGoogleUserInfo(account: GoogleSignInAccount) {
        val givenName = account.givenName
        val lastName = account.familyName
        val displayName = account.displayName
        val email = account.email

        viewState.signIn()
    }
}