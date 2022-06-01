package com.wordyka.kantinkorperasiitdel.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.User

class SharePref(activity: Activity) {
    val login = "login"
    val name = "name"
    val phone = "phone"
    val email = "email"
    val user = "user"
    val pesan = "pesan"
    val produk = "produk"

    val mypref = "MAIN_PREF"
    val sp:SharedPreferences

    init {
        sp = activity.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    fun setStatusLogin(status: Boolean) {
        sp.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin():Boolean {
        return sp.getBoolean(login, false)
    }

    fun setProduk(value: Produk) {
        val data:String = Gson().toJson(value, Produk::class.java)
        sp.edit().putString(produk, data).apply()
    }

    fun getProduk():Produk? {
        val data:String = sp.getString(produk, "") ?: return null
        val json = Gson().fromJson<Produk>(data, Produk::class.java)
        return json
    }

    fun setUser(value:User) {
        val data:String = Gson().toJson(value, User::class.java)
        sp.edit().putString(user, data).apply()
    }

    fun getUser():User? {
        val data:String = sp.getString(user, "") ?: return null
        val json = Gson().fromJson<User>(data, User::class.java)
        return json
    }

    fun setPesan(value:Pemesanan?) {
        val data:String = Gson().toJson(value, Pemesanan::class.java)
        sp.edit().putString(pesan, data).apply()
    }

    fun getPesan():Pemesanan? {
        val data:String = sp.getString(pesan, "") ?: return null
        val json = Gson().fromJson<Pemesanan>(data, Pemesanan::class.java)
        return json
    }


    fun setString(key:String, value:String) {
        sp.edit().putString(key, value).apply()
    }

    fun getString(key: String) : String {
        return sp.getString(key, "")!!
    }

}