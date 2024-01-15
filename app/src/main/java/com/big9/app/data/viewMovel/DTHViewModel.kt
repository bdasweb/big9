package com.big9.app.data.viewMovel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.big9.app.data.model.DTHTranspherModel
import com.big9.app.data.model.DTHUserInfoModel
import com.big9.app.network.ResponseState

import com.big9.app.repository.AuthRepositoryRepository
import com.big9.app.repository.TableRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DTHViewModel @Inject constructor(private val repository: AuthRepositoryRepository, private val tableRepository: TableRepository) : ViewModel() {
    //DTH Operator List
    val dthTransferResponseLiveData: MutableLiveData<ResponseState<DTHTranspherModel>>
        get() = repository.dthTransferResponseLiveData
    fun dthTransfer(token: String, data: String) {
        viewModelScope.launch {
            repository.dthTransfer(token,data)
        }
    }

    //DTH Operator List
    val dthUserInfoResponseLiveData: LiveData<ResponseState<DTHUserInfoModel>>
        get() = repository.dthUserInfoResponseLiveData
    fun dthUserInfo(token: String, data: String) {
        viewModelScope.launch {
            repository.dthUserInfo(token,data)
        }
    }




}