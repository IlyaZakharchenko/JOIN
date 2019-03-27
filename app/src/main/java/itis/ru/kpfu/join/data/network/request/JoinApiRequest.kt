package itis.ru.kpfu.join.data.network.request

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import itis.ru.kpfu.join.data.network.pojo.NotificationResponse
import itis.ru.kpfu.join.data.network.pojo.ProfileImage
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.data.network.pojo.UserInfoResponse
import itis.ru.kpfu.join.presentation.model.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface JoinApiRequest {

    @POST("/registration")
    fun signUp(@Body registrationFormModel: RegistrationFormModel): Completable

    @POST("/registration/email_confirmation")
    fun confirmEmail(@Body confirmEmailFormModel: ConfirmEmailFormModel): Completable

    @POST("/login")
    fun signIn(@Body signInFormModel: SignInFormModel): Observable<UserInfoResponse>

    @Multipart
    @POST("/user/{id}/upload")
    fun changeProfileImage(@Header("Authorization") token: String?, @Path(
            "id") id: Long?, @Part image: MultipartBody.Part): Single<ProfileImage>

    @POST("/projects")
    fun addProject(@Header("Authorization") token: String?, @Body projectModel: ProjectModel): Completable

    @POST("/user/invite")
    fun inviteToProject(@Header("Authorization") token: String?, @Body formModel: InviteFormModel?): Completable

    @POST("/projects/join")
    fun joinProject(@Header("Authorization") token: String?, @Body formModel: InviteFormModel?): Completable

    @GET("/user/{id}/notifications")
    fun getNotifications(@Header("Authorization") token: String?, @Path(
            "id") userId: Long?): Single<List<NotificationModel>>

    @GET("/user/{id}")
    fun getUserInfo(@Header("Authorization") token: String?, @Path("id") id: Long?): Observable<User>

    @GET("/projects/{id}")
    fun getProject(@Header("Authorization") token: String?, @Path("id") id: Long): Single<ProjectModel>

    @GET("/user/{id}/projects")
    fun getMyProjects(@Header("Authorization") token: String?, @Path("id") userId: Long?): Single<List<ProjectModel>>

    @GET("/projects")
    fun getProjects(
            @Header("Authorization") token: String?,
            @Query("name") projectName: String? = null,
            @Query("vacancy_name") vacancyName: String? = null,
            @Query("knowledge_level") level: String? = null,
            @Query("experience") exp: String? = null): Single<List<ProjectModel>>

    @GET("/user/search")
    fun getUsers(
            @Header("Authorization") token: String?,
            @Query("project_id") projectId: Long?,
            @Query("username") username: String? = null,
            @Query("specialization_name") specName: String? = null,
            @Query("knowledge_level") level: String? = null,
            @Query("experience") exp: String? = null): Single<MutableList<ProjectMemberModel>>

    @GET("/specialization/name")
    fun getSpecializations( @Header("Authorization") token: String?): Single<MutableList<String>>

    @PUT("/user/{id}")
    fun changeUser(@Header("Authorization") token: String?, @Body user: User?, @Path(
            "id") id: Long?): Completable

    @POST("/notifications/{id}")
    fun responseToNotification(@Header("Authorization") token: String?, @Body answer: NotificationResponse,
            @Path("id") id: Long?): Completable

    @DELETE("/notifications/{id}")
    fun deleteNotification(@Header("Authorization") token: String?, @Path("id") id: Long?): Completable

    @POST("/recovery/email")
    fun restorePassChange(@Body email: String): Completable

    @POST("/recovery")
    fun restorePassChange(@Body form: RestorePassFormModel): Completable
}