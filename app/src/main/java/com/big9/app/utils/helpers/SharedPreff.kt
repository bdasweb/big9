package com.big9.app.utils.helpers

import android.content.Context
import com.big9.app.data.model.login.LoginResponse
import com.big9.app.data.model.profile.Data
import com.big9.app.utils.helpers.Constants.EPAY_SHAREDFREFFRENCE
import com.big9.app.utils.helpers.Constants.FCMTOKEN
import com.big9.app.utils.helpers.Constants.ISLogin
import com.big9.app.utils.helpers.Constants.TEST
import com.big9.app.utils.helpers.Constants.USER_DATA
import com.big9.app.utils.helpers.Constants.loginData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreff @Inject constructor(@ApplicationContext private val context: Context?){

    private var settings = context?.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
    fun setTestData(phn: String?) {

        context?.let {
//            val settings =
//                context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            val editor = settings?.edit()
            phn?.let { phone ->
                editor?.putString(TEST, phone)
            }
            editor?.apply()
        }
    }

    fun getTestData(): String {
        var phn: String? = null
        context?.let {
//            val shrdprf =
//                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            phn = settings?.getString(TEST, "0")
        }
        return phn ?: "0"

    }


    fun setLoginData(loginResponse: LoginResponse, iSLogin: Boolean, status: String) {

        context?.let {
             settings =
                context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            val editor = settings?.edit()
            if (status=="ACTIVE"){
                editor?.putBoolean(ISLogin, true)
            }
            else{
                editor?.putBoolean(ISLogin, false)
            }

            val gson = Gson()
            val json = gson.toJson(loginResponse)
            editor?.putString(loginData, json)

            editor?.apply()
        }
    }

    fun setUserInfoData(userdata: Data?) {

        context?.let {
             settings =
                context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            val editor = settings?.edit()


            val gson = Gson()
            val json = gson.toJson(userdata)
            editor?.putString(USER_DATA, json)

            editor?.apply()
        }
    }

    fun getUserData(): Data? {
        context?.let {
            //var settings2 = context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)


            val json = settings?.getString(USER_DATA, null)

            val gson = Gson()
            val userData = if (json != null) gson.fromJson(json, Data::class.java) else null

            return userData
        }

        // Return default values if context is null
        return null
    }



    fun checkIsLogin(): Boolean {
        var isLogin: Boolean? = null
        context?.let {
            settings =
                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            isLogin = settings?.getBoolean(ISLogin, false)
        }
        return isLogin ?: false

    }

    fun getLoginData(): Pair<Boolean, LoginResponse?> {
        context?.let {
            //var settings2 = context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)

            val isLogin = settings?.getBoolean(ISLogin, false) ?: false
            val json = settings?.getString(loginData, null)

            val gson = Gson()
            val loginResponse = if (json != null) gson.fromJson(json, LoginResponse::class.java) else null

            return Pair(isLogin, loginResponse)
        }

        // Return default values if context is null
        return Pair(false, null)
    }

    /*fun getLoginData(): LoginResponse? {
        var loginData: String? = null
        context?.let {
            settings =
                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            loginData = settings?.getString(loginData, "")
        }
        Log.d("TAG_loginData", "getLoginData:json "+loginData)
        val gson = Gson()

        val loginResponse = gson.fromJson(loginData, LoginResponse::class.java)

        return loginResponse?:null

    }*/

    fun setfirebase_token(token : String?){
        context?.let {
            val settings = context.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            val editor = settings.edit()
            token?.let {
                editor.putString(FCMTOKEN, it)
            }
            editor.apply()
        }

    }

    fun getFcnToken():String?{
        var token: String? = null
        context?.let {
            settings =
                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
            token = settings?.getString(FCMTOKEN, "")
        }
        return token ?: ""
    }

    fun clearUserData() {
        context?.let {
            settings =
                it.getSharedPreferences(EPAY_SHAREDFREFFRENCE, Context.MODE_PRIVATE)
                settings?.edit()?.clear()?.apply()
        }

    }
}