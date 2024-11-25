package hwsein.developer.example.loginapp.remote.retrofitService

import hwsein.developer.example.loginapp.remote.apiService.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val url = "https://learn.alirezaahmadi.info/api/v1/auth/"

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService : ApiService = retrofit.create(ApiService::class.java)

}