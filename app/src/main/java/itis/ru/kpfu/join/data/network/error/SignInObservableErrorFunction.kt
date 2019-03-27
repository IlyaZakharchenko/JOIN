package itis.ru.kpfu.join.data.network.error

import io.reactivex.Observable
import itis.ru.kpfu.join.data.network.exception.domain.SignInException
import itis.ru.kpfu.join.data.network.pojo.UserInfoResponse
import retrofit2.HttpException

class SignInObservableErrorFunction : ErrorFunction<Observable<UserInfoResponse>> {

    override fun apply(t: Throwable): Observable<UserInfoResponse> {
        if (t is HttpException) {
            if (t.response().headers().get("Error") == "Full authentication is required to access this resource") {
                return Observable.error(SignInException())
            }
        }
        return Observable.error(t)
    }
}