package itis.ru.kpfu.join.data.network.joinapi.error

import io.reactivex.Completable
import io.reactivex.CompletableSource
import itis.ru.kpfu.join.data.network.exception.domain.IncorrectDataException
import itis.ru.kpfu.join.data.network.exception.domain.InvalidCodeException
import itis.ru.kpfu.join.data.network.exception.domain.UserWithThisEmailNotExistsException
import retrofit2.HttpException

class RestorePasswordCompletableErrorFunction : CompletableErrorFunction {
    override fun apply(t: Throwable): CompletableSource {
        if (t is HttpException) {
            return when (t.response().headers().get("Error")) {
                "invalid login" -> Completable.error(UserWithThisEmailNotExistsException())
                "invalid confirmation" -> Completable.error(InvalidCodeException())
                else -> Completable.error(IncorrectDataException())
            }
        }
        return Completable.error(t)
    }
}