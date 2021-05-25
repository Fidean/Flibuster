package ru.fidean.flibuster

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://flibusta.site/"

object ServerAPI {
    private var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private var client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(false)


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(client.build())
        .build()

    var api: APIService = retrofit.create(APIService::class.java)
}

interface APIService {
    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("node?destination=node/")
    fun login(
        @Field("name") name: String,
        @Field("pass") pass: String,
        @Field("op") op: String,
        @Field("form_build_id") formBuildId: String,
        @Field("form_id") formId: String,
        @Field("openid.return_to") openidReturnTo: String,
        @Field("openid_identifier") openidIdentifier: String
    ): Call<ResponseBody>
}
