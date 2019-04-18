package itis.ru.kpfu.join.data.network.joinapi.request

import io.reactivex.*
import io.reactivex.subjects.Subject
import itis.ru.kpfu.join.data.EventType
import itis.ru.kpfu.join.data.ProjectAddedEvent
import itis.ru.kpfu.join.data.network.joinapi.error.RestorePasswordCompletableErrorFunction
import itis.ru.kpfu.join.data.network.joinapi.error.SignInObservableErrorFunction
import itis.ru.kpfu.join.data.network.joinapi.error.SignUpCompletableErrorFunction
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.data.network.exception.*
import itis.ru.kpfu.join.data.network.exception.domain.DomainException
import itis.ru.kpfu.join.data.network.joinapi.pojo.*
import itis.ru.kpfu.join.presentation.model.NotificationModel
import itis.ru.kpfu.join.presentation.model.*
import okhttp3.MultipartBody
import java.lang.IllegalArgumentException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class JoinApiRequestDecorator(
        val apiRequest: JoinApiRequest,
        val eventSubject: Subject<EventType>
) : JoinApiRequest {

    companion object {
        private fun processApiThrowable(t: Throwable): Throwable {
            return when (t) {
                is DomainException -> t
                is IllegalArgumentException -> NotAuthorizedException()
                is UnknownHostException -> NoInternetConnectionException()
                is SocketTimeoutException -> TimeOutException()
                else -> UnknownException()
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


    override fun signUp(registrationFormModel: RegistrationFormModel): Completable {
        return apiRequest
                .signUp(registrationFormModel)
                .onErrorResumeNext(SignUpCompletableErrorFunction())
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun confirmEmail(confirmEmailFormModel: ConfirmEmailFormModel): Completable {
        return apiRequest
                .confirmEmail(confirmEmailFormModel)
                .onErrorResumeNext(SignUpCompletableErrorFunction())
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun signIn(signInFormModel: SignInFormModel): Observable<UserInfoResponse> {
        return apiRequest
                .signIn(signInFormModel)
                .onErrorResumeNext(SignInObservableErrorFunction())
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
                .doOnComplete { eventSubject.onNext(ProjectAddedEvent()) }
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

    override fun restorePassChange(email: String): Completable {
        return apiRequest
                .restorePassChange(email)
                .onErrorResumeNext(RestorePasswordCompletableErrorFunction())
                .compose(ApiRequestErrorCompletableTransformer())
    }

    override fun restorePassChange(form: RestorePassFormModel): Completable {
        return apiRequest
                .restorePassChange(form)
                .compose(ApiRequestErrorCompletableTransformer())
    }
}