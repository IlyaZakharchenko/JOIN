package itis.ru.kpfu.join.network.request

import io.reactivex.*
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.network.exception.*
import itis.ru.kpfu.join.network.pojo.*
import itis.ru.kpfu.join.presentation.model.NotificationModel
import itis.ru.kpfu.join.presentation.model.*
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class JoinApiRequestDecorator(val apiRequest: JoinApiRequest) : JoinApiRequest {

    companion object {
        private fun processApiThrowable(t: Throwable): Throwable {
            return if (t is HttpException) {
                when (t.code()) {
                    400 -> IncorrectDataException()
                    401 -> NotAuthorizedException()
                    else -> UnexpectedException()
                }
            } else if (t is UnknownHostException) {
                NoInternetConnectionException()
            } else if (t is SocketTimeoutException) {
                TimeOutException()
            } else {
                UnexpectedException()
            }
        }
    }

    private class ApiRequestErrorSingleTransformer<T> : SingleTransformer<T, T> {
        override fun apply(upstream: Single<T>): SingleSource<T> {
            return upstream.onErrorResumeNext { t: Throwable -> Single.error(processApiThrowable(t)) }
        }
    }

    private class ApiRequestErrorObservableTransformer<T> : ObservableTransformer<T, T> {
        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream.onErrorResumeNext { t: Throwable -> Observable.error(processApiThrowable(t)) }
        }
    }

    private class ApiRequestErrorCompletableTransformer : CompletableTransformer {
        override fun apply(upstream: Completable): CompletableSource {
            return upstream.onErrorResumeNext { t: Throwable -> Completable.error(processApiThrowable(t)) }
        }
    }


    override fun signUp(registrationFormModel: RegistrationFormModel):Completable {
        return apiRequest
                .signUp(registrationFormModel)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun confirmEmail(confirmEmailFormModel: ConfirmEmailFormModel): Completable {
        return apiRequest
                .confirmEmail(confirmEmailFormModel)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun signIn(signInFormModel: SignInFormModel): Observable<UserInfoResponse> {
        return apiRequest
                .signIn(signInFormModel)
                .compose(ApiRequestErrorObservableTransformer())
    }

    override fun changeProfileImage(token: String?, id: Long?, image: MultipartBody.Part): Single<ProfileImage> {
        return apiRequest
                .changeProfileImage(token, id, image)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun addProject(token: String?, projectModel: ProjectModel): Completable {
        return apiRequest
                .addProject(token, projectModel)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun inviteToProject(token: String?, formModel: InviteFormModel?): Completable {
        return apiRequest
                .inviteToProject(token, formModel)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun joinProject(token: String?, formModel: InviteFormModel?): Completable {
        return apiRequest
                .joinProject(token, formModel)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun getNotifications(token: String?, userId: Long?): Single<List<NotificationModel>> {
        return apiRequest
                .getNotifications(token, userId)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun getUserInfo(token: String?, id: Long?): Observable<User> {
        return apiRequest
                .getUserInfo(token, id)
                .compose(ApiRequestErrorObservableTransformer())
    }

    override fun getProject(token: String?, id: Long): Single<ProjectModel> {
        return apiRequest
                .getProject(token, id)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun getMyProjects(token: String?, userId: Long?): Single<List<ProjectModel>> {
        return apiRequest
                .getMyProjects(token, userId)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun getProjects(token: String?, projectName: String?, vacancyName: String?, level: String?, exp: String?): Single<List<ProjectModel>> {
        return apiRequest
                .getProjects(token, projectName, vacancyName, level, exp)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun getUsers(token: String?, projectId: Long?, username: String?, specName: String?, level: String?, exp: String?): Single<MutableList<ProjectMemberModel>> {
        return apiRequest
                .getUsers(token, projectId, username)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun getSpecializations(token: String?): Single<MutableList<String>> {
        return apiRequest
                .getSpecializations(token)
                .compose(ApiRequestErrorSingleTransformer())
    }

    override fun changeUser(token: String?, user: User?, id: Long?): Completable {
        return apiRequest
                .changeUser(token, user, id)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun responseToNotification(token: String?, answer: NotificationResponse, id: Long?): Completable {
        return apiRequest
                .responseToNotification(token, answer, id)
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun deleteNotification(token: String?, id: Long?): Completable {
        return apiRequest
                .deleteNotification(token, id)
                .compose(ApiRequestErrorCompletableTransformer())
    }
}