package dz.jilconnect.dipanniniuser


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path
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

    @GET("/api/checkin/{data}")
    @FormUrlEncoded
    fun workerDataAsync(
        @Path("data") location: String
    ): Deferred<List<WorkersResult>>


}