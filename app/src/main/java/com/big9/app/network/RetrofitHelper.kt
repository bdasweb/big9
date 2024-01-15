package com.big9.app.network

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.big9.app.R
import com.big9.app.ui.popup.ErrorPopUp
import com.big9.app.utils.common.MethodClass.appUpdate
import com.big9.app.utils.common.MethodClass.userLogout
import com.google.gson.GsonBuilder
import com.intuit.sdp.BuildConfig

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    fun getClient(): Retrofit.Builder{

        //custom interceptor
        val commonHeaderInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            chain.proceed(request)
        }

        //logger
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        //Okhttp Client
        val client = OkHttpClient.Builder().also { client ->
            client.readTimeout(300, TimeUnit.SECONDS)
            client.writeTimeout(300, TimeUnit.SECONDS)
            client.connectTimeout(300, TimeUnit.SECONDS)
            client.addInterceptor(commonHeaderInterceptor)
           // if (BuildConfig.DEBUG) {
                client.addInterceptor(interceptor)
           // }
        }.build()

        val gson = GsonBuilder()
            .setLenient()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

        return Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
    }



    fun Fragment.handleApiError(isNetworkError: Boolean, errorCode: Int?, errorMessage: String?, isShowPopup:Boolean=true) {
        context?.let {
            var errorMessageData=errorMessage
            if (errorMessage?.lowercase()?.contains("authentication")==true){
                errorMessageData=getString(R.string.something_went_wrong_please_try_again_later)
            }

            handleError(it, isNetworkError, errorCode, errorMessageData, isShowPopup)
        }
    }

    fun Activity.handleApiError(isNetworkError: Boolean, errorCode: Int?, errorMessage: String?, isShowPopup:Boolean=true) {
        handleError(this, isNetworkError, errorCode, errorMessage, isShowPopup)
    }

    private fun handleError(
        context: Context,
        isNetworkError: Boolean,
        errorCode: Int?,
        errorMessage: String?,
        isShowPopup:Boolean,
    ) {

        if(isNetworkError) {
            // network error
            // ErrorPopUp(context).showMessageDialog(context.getString(R.string.network_error))
        }else{
            when (errorCode) {
                400 -> {
                    if(isShowPopup)
                        ErrorPopUp(context).showMessageDialog(errorMessage)
                }
                401 -> {
                    //Required Field Missing
                    ErrorPopUp(context).showMessageDialog(errorMessage)
                    //session logout
                //    context.userLogout()
                }
                402 -> {
                    //Required Field Missing
                    ErrorPopUp(context).showMessageDialog(errorMessage)
                    //session logout
                //    context.userLogout()
                }
                403 -> {

                    //App Update Required
                  //  context.appUpdateRequired()
                }
                417 -> {
                    //session logout
                    context.userLogout()
                }
                103 -> {

                    context.appUpdate()
                }
                105 -> {

                    context.userLogout()
                }

                else -> {
                    ErrorPopUp(context).showMessageDialog(errorMessage)
                   // ErrorPopUp(context).showMessageDialog(context.getString(R.string.something_went_wrong))
                }
            }
        }
    }

}