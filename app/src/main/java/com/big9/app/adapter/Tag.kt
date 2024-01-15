package com.big9.app.adapter

import android.util.Log

class Tag {
    companion object Test {
        var API_TAG=""
        fun tag( s:String, s2:String) = Log.e(s,s2)
        fun tag( s:String) = Log.e("showLog",s)
    }
}