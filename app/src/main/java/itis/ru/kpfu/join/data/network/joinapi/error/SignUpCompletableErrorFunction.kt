package itis.ru.kpfu.join.data.network.joinapi.error

import io.reactivex.Completable
import io.reactivex.CompletableSource
import itis.ru.kpfu.join.data.network.exception.domain.InvalidCodeException
import itis.ru.kpfu.join.data.network.exception.domain.SignUpEmailAlreadyExistsException
import itis.ru.kpfu.join.data.network.exception.domain.SignUpUsernameAlreadyExistsException
import retrofit2.HttpException

class SignUpCompletableErrorFunction : CompletableErrorFunction {
    override fun apply(t: Throwable): CompletableSource {
        if (t is HttpException) {
            return when (t.response().headers().get("Error")) {
                "invalid username" -> Completable.error(SignUpUsernameAlreadyExistsException())
                "invalid login" -> Completable.error(SignUpEmailAlreadyExistsException())
                "invalid confirmation" -> Completable.error(InvalidCodeException())
                else -> Completable.error(t)
            }
        }
        return Completable.error(t)
    }
}