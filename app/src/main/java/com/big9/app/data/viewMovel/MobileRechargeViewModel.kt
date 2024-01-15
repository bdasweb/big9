package com.big9.app.data.viewMovel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.big9.app.data.model.PrepaidMoboleTranspherModel
import com.big9.app.data.model.allReport.PostPaidMobileTranspherModel
import com.big9.app.network.ResponseState
import com.big9.app.repository.AuthRepositoryRepository
import com.big9.app.repository.TableRepository


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MobileRechargeViewModel @Inject constructor(private val repository: AuthRepositoryRepository, private val tableRepository: TableRepository) : ViewModel() {
    //prepaid mobile transpher
    val prePaidMobileTranspherResponseLiveData: MutableLiveData<ResponseState<PrepaidMoboleTranspherModel>>
        get() = repository.prePaidMobileTranspherResponseLiveData
    fun PrePaidMobileTranspher(token: String, data: String) {
        viewModelScope.launch {
            repository.PrePaidMobileTranspher(token,data)
        }
    }


    //postpaid mobile transpher
    val postPaidMobileTranspherResponseLiveData: MutableLiveData<ResponseState<PostPaidMobileTranspherModel>>
        get() = repository.postPaidMobileTranspherResponseLiveData
    fun PostPaidMobileTranspher(token: String, data: String) {
        viewModelScope.launch {
            repository.PostPaidMobileTranspher(token,data)
        }
    }

}