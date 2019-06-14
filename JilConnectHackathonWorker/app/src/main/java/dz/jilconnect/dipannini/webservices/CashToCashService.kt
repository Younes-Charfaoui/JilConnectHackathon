package dz.jilconnect.dipannini.webservices

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dz.jilconnect.dipannini.model.UserProfile
import dz.jilconnect.dipannini.model.UserRegistrationResponse

import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface CashToCashService {

    companion object {
        private const val BASE_URL = "http://5fac28ca.ngrok.io/"
        private const val REGISTER = "register"
        private const val PROFILE = "api/profile"
        private const val IMAGE_ADS = "pub_images.php"
        const val IMAGE_DIR = BASE_URL + "pubImages/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createClient())
            .build()

        private fun createClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
            return builder.build()
        }
    }

    @POST(REGISTER)
    @FormUrlEncoded
    fun signUpAsync(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<UserRegistrationResponse>

    @GET("api/profile/{id}")
    fun getUserDataAsync(@Path("id") id: String): Deferred<UserProfile>

    @PATCH("api/profile")
    @FormUrlEncoded
    fun updateAsync(
        @Field("id") id: String,
        @Field("phone") phone: String,
        @Field("location") location: String
    ): Deferred<UserProfile>
}
