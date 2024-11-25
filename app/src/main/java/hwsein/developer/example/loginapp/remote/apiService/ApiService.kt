package hwsein.developer.example.loginapp.remote.apiService

import hwsein.developer.example.loginapp.remote.model.ApiModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("email/login")
    suspend fun sendEmail(
        @Field("email") email: String
    ): Response<ApiModel>


    @FormUrlEncoded
    @POST("email/login/verify")
    suspend fun verifyCode(
        @Header("app-device-uid")androidId : String ,
        @Field("code") code : String,
        @Field("email") email : String
    ) : Response<ApiModel>

}