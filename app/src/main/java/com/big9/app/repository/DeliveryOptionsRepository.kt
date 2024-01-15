package com.big9.app.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.big9.app.data.genericmodel.BaseResponse
import com.big9.app.data.model.sample.Test
import com.big9.app.network.ResponseState
import com.big9.app.network.RetroApi
import com.google.gson.JsonObject
import javax.inject.Inject

class DeliveryOptionsRepository  @Inject constructor(private val api : RetroApi) {

    private val _loginResponseLiveData =
        MutableLiveData<ResponseState<BaseResponse<Test>>>()
    val loginResponseLiveData: LiveData<ResponseState<BaseResponse<Test>>>
        get() = _loginResponseLiveData



    suspend fun login(requestBody: JsonObject) {
        _loginResponseLiveData.postValue(ResponseState.Loading())
        try {

            val response = api.login(requestBody)
            _loginResponseLiveData.postValue(ResponseState.create(response,"aa"))
            Log.e("TAG_error", "login:OK "+response)

        } catch (throwable: Throwable) {
            _loginResponseLiveData.postValue(ResponseState.create(throwable))
            Log.e("TAG_error", "login: "+throwable.message)
        }

    }

}


