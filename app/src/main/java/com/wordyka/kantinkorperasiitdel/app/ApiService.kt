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



    //  ADMIN KANTIN & KOPERASI
    @GET("data-produk")
    fun getAdminProduk(): Call<List<Produk>>

    @GET("data-produk/makananKoperasi")
    fun getAdminProdukMakananKoperasi(): Call<List<Produk>>

    @GET("data-produk/makananKantin")
    fun getAdminProdukMakananKantin(): Call<List<Produk>>

    @GET("data-produk/minumanKoperasi")
    fun getAdminProdukMinumanKoperasi(): Call<List<Produk>>

    @GET("data-produk/minumanKantin")
    fun getAdminProdukMinumanKantin(): Call<List<Produk>>

    @GET("data-produk/ruanganKoperasi")
    fun getAdminProdukRuanganKoperasi(): Call<List<Produk>>

    @GET("data-produk/ruanganKantin")
    fun getAdminProdukRuanganKantin(): Call<List<Produk>>

    @GET("data-produk/pulsaKoperasi")
    fun getAdminProdukPulsaKoperasi(): Call<List<Produk>>

    @GET("data-produk/pulsaKantin")
    fun getAdminProdukPulsaKantin(): Call<List<Produk>>

    @GET("data-produk/barangKoperasi")
    fun getAdminProdukBarangKoperasi(): Call<List<Produk>>

    @GET("data-produk/barangKantin")
    fun getAdminProdukBarangKantin(): Call<List<Produk>>

    @FormUrlEncoded
    @POST("data-produk/tambah")
    fun addAdminProduk(
        @Field("nama") nama: String,
        @Field("kategori") kategori: String,
        @Field("jumlah") jumlah: Int,
        @Field("status") status: String,
        @Field("gambar") gambar: String,
        @Field("deskripsi") deskripsi: String,
        @Field("hargaPcs") hargaPcs: BigInteger,
        @Field("role") role: String
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