package itis.ru.kpfu.join.network.request

import com.google.gson.JsonObject
import io.reactivex.Single
import itis.ru.kpfu.join.network.pojo.InviteForm
import itis.ru.kpfu.join.network.pojo.Notification
import itis.ru.kpfu.join.network.pojo.NotificationResponse
import itis.ru.kpfu.join.network.pojo.ProfileImage
import itis.ru.kpfu.join.network.pojo.Project
import itis.ru.kpfu.join.network.pojo.ProjectMember
import itis.ru.kpfu.join.network.pojo.UserRegistrationForm
import itis.ru.kpfu.join.db.entity.User
import okhttp3.MultipartBody
import retrofit2.Response
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

interface JoinApi {

    @POST("/registration")
    fun signUp(@Body user: UserRegistrationForm): Single<Response<Void>>

    @POST("/registration/email_confirmation")
    fun confirmEmail(@Body email: String): Single<Response<Void>>

    @POST("/login")
    fun signIn(@Body user: User): Single<Response<JsonObject>>

    @Multipart
    @POST("/user/{id}/upload")
    fun changeProfileImage(@Header("Authorization") token: String?, @Path(
            "id") id: Long?, @Part image: MultipartBody.Part): Single<ProfileImage>

    @POST("/projects")
    fun addProject(@Header("Authorization") token: String?, @Body project: Project): Single<Response<Void>>

    @POST("/user/invite")
    fun inviteToProject(@Header("Authorization") token: String?, @Body form: InviteForm?): Single<Response<Void>>

    @POST("/projects/join")
    fun joinProject(@Header("Authorization") token: String?, @Body form: InviteForm?): Single<Response<Void>>

    @GET("/user/{id}/notifications")
    fun getNotifications(@Header("Authorization") token: String?, @Path(
            "id") userId: Long?): Single<List<Notification>>

    @GET("/user/{id}")
    fun getUserInfo(@Header("Authorization") token: String?, @Path("id") id: Long?): Single<User>

    @GET("/projects/{id}")
    fun getProject(@Header("Authorization") token: String?, @Path("id") id: Long): Single<Project>

    @GET("/user/{id}/projects")
    fun getMyProjects(@Header("Authorization") token: String?, @Path("id") userId: Long?): Single<List<Project>>

    @GET("/projects")
    fun getProjects(
            @Header("Authorization") token: String?,
            @Query("name") projectName: String? = null,
            @Query("vacancy_name") vacancyName: String? = null,
            @Query("knowledge_level") level: String? = null,
            @Query("experience") exp: String? = null): Single<List<Project>>

    @GET("/user/search")
    fun getUsers(
            @Header("Authorization") token: String?,
            @Query("project_id") projectId: Long?,
            @Query("username") username: String? = null,
            @Query("specialization_name") specName: String? = null,
            @Query("knowledge_level") level: String? = null,
            @Query("experience") exp: String? = null): Single<MutableList<ProjectMember>>

    @GET("/specialization/name")
    fun getSpecializations( @Header("Authorization") token: String?): Single<MutableList<String>>

    @PUT("/user/{id}")
    fun changeUser(@Header("Authorization") token: String?, @Body user: User?, @Path(
            "id") id: Long?): Single<Response<Void>>

    @DELETE("/user/{id}/profileImage")
    fun deleteImage(@Header("Authorization") token: String?, @Path("id") id: Long?): Single<Response<Void>>

    @POST("/notifications/{id}")
    fun responseToNotification(@Header("Authorization") token: String?, @Body answer: NotificationResponse,
            @Path("id") id: Long?): Single<Response<Void>>

    @DELETE("/notifications/{id}")
    fun deleteNotification(@Header("Authorization") token: String?, @Path("id") id: Long?): Single<Response<Void>>
}