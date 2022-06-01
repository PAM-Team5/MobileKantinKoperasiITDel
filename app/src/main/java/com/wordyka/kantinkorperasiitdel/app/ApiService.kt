package com.wordyka.kantinkorperasiitdel.app

import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.math.BigInteger

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<ResponModel>


    @GET("produk")
    fun getProduk(): Call<ResponModel>

    @GET("pemesanan")
    fun getPemesanan(): Call<ResponModel>

    @FormUrlEncoded
    @POST("pemesanan/tambah")
    fun tambahPesan(
        @Field("nama") nama: String,
        @Field("kategori") kategori: String,
        @Field("jumlah") jumlah: Int,
        @Field("status") status: String,
        @Field("gambar") gambar: String,
        @Field("deskripsi") deskripsi: String,
        @Field("hargaPcs") hargaPcs: BigInteger,
        @Field("ID_Product") ID_Product: Int,
        @Field("ID_User") ID_User: Int,
    ): Call<ResponModel>

    @DELETE("pemesanan/hapus/{id}")
    fun deletePesan(
        @Path("id") id: Int
    ): Call<Void>

    //    KANTIN & KOPERASI
    @GET("data-produk")
    fun getAdminProduk(): Call<List<Produk>>

    @GET("data-produk/makanan")
    fun getAdminProdukMakanan(): Call<List<Produk>>

    @GET("data-produk/minuman")
    fun getAdminProdukMinuman(): Call<List<Produk>>

    @GET("data-produk/ruangan")
    fun getAdminProdukRuangan(): Call<List<Produk>>

    @GET("data-produk/pulsa")
    fun getAdminProdukPulsa(): Call<List<Produk>>


    @FormUrlEncoded
    @POST("data-produk/tambah")
    fun addAdminProduk(
        @Field("nama") nama: String,
        @Field("kategori") kategori: String,
        @Field("jumlah") jumlah: Int,
        @Field("status") status: String,
        @Field("gambar") gambar: String,
        @Field("deskripsi") deskripsi: String,
        @Field("hargaPcs") hargaPcs: BigInteger
    ): Call<SubmitModel>

    @FormUrlEncoded
    @PUT("data-produk/ubah/{id}")
    fun updateAdminProduk(
        @Path("id") id: Int,
        @Field("nama") nama: String,
        @Field("kategori") kategori: String,
        @Field("jumlah") jumlah: Int,
        @Field("status") status: String,
        @Field("gambar") gambar: String,
        @Field("deskripsi") deskripsi: String,
        @Field("hargaPcs") hargaPcs: BigInteger
    ): Call<SubmitModel>

    @DELETE("data-produk/hapus/{id}")
    fun deleteAdminProduk(
        @Path("id") id: Int
    ): Call<Void>



}