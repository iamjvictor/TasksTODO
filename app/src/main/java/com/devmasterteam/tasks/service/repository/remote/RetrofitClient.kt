package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import android.util.Log

class RetrofitClient private constructor(){
    companion object{
        private lateinit var INSTANCE: Retrofit
        private var token: String = ""
        private var personKey: String = ""

        private fun getRetrofitInstance(): Retrofit {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader(TaskConstants.HEADER.TOKEN, token)
                    .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                    .build()

                Log.d("RetrofitClient", "Token: $token")
                Log.d("RetrofitClient", "PersonKey: $personKey")

                val response = chain.proceed(request)
                val rawJson = response.body?.string()
                Log.d("RawResponse", rawJson ?: "Resposta nula")

                // Substituir o body por um novo response body para reutilizá-lo
                response.newBuilder().body(ResponseBody.create(response.body?.contentType(), rawJson ?: "")).build()
            }

            synchronized(RetrofitClient::class) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI/")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <T> getService(serviceClass: Class<T>): T {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeaders(tokenValue: String, personKeyValue: String) {
            token = tokenValue
            personKey = personKeyValue
        }
    }
}
