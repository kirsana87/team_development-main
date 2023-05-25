package pro.fateeva.pillsreminder.clean.data.pillsearching

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pro.fateeva.pillsreminder.BuildConfig
import java.util.concurrent.TimeUnit

class RetrofitPillsApiClient {
    fun createClient(): OkHttpClient {
        val client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return client
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}