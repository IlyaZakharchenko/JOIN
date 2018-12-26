package itis.ru.kpfu.join.api

import com.google.gson.JsonObject
import io.reactivex.Single
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.model.ProjectMember
import itis.ru.kpfu.join.model.UserRegistrationForm
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JoinApi {

    @POST("/registration")
    fun signUp(@Body user: UserRegistrationForm): Single<Response<Void>>

    @POST("/registration/email_confirmation")
    fun confirmEmail(@Body email: String): Single<Response<Void>>

    @POST("/login")
    fun signIn(@Body user: User): Single<Response<JsonObject>>

    @GET("/user/{id}")
    fun getUserInfo(@Header("Authorization") token: String?, @Path("id") id: Long?): Single<User>

    @GET("/projects")
    fun getProjects(@Header("Authorization") token: String?): Single<List<Project>>

    @GET("/projects/{id}")
    fun getProject(@Header("Authorization") token: String?, @Path("id") id: Long): Single<Project>

    @GET("/user/{id}/projects")
    fun getMyProjects(@Header("Authorization") token: String?, @Path("id") userId: Long?): Single<List<Project>>

    @GET("/user/search")
    fun getUsers(@Header("Authorization") token: String?): Single<List<ProjectMember>>

    @PUT("/user/{id}")
    fun changeUser(@Header("Authorization") token: String?, @Body user: User?, @Path(
            "id") id: Long?): Single<Response<Void>>
}