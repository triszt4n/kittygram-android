package hu.triszt4n.kittygram.api

import hu.triszt4n.kittygram.util.Constants
import hu.triszt4n.kittygram.util.MoshiInstance
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(OkHttpInstance.okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(MoshiInstance.moshi).asLenient()
            )
            .build()
    }

    val api: KittyApi by lazy {
        retrofit.create(KittyApi::class.java)
    }

}
