package com.wordyka.kantinkorperasiitdel.activity.kantin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.CreateAdminActivity
import com.wordyka.kantinkorperasiitdel.activity.EditAdminActivity
import com.wordyka.kantinkorperasiitdel.activity.MasukActivity
import com.wordyka.kantinkorperasiitdel.adapter.AdapterAdminProduk
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.model.Produk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAdminKantinActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    private lateinit var adapterAdminProdukMakanan: AdapterAdminProduk
    private lateinit var adapterAdminProdukMinuman: AdapterAdminProduk
    private lateinit var adapterAdminProdukPulsa: AdapterAdminProduk
    private lateinit var rvProdukMakanan: RecyclerView
    private lateinit var rvProdukMinuman: RecyclerView
    private lateinit var rvProdukPulsa: RecyclerView
    private lateinit var btnAddProduk: FloatingActionButton
    private lateinit var btnAdminProfil: ImageView

    private var listMinuman:ArrayList<Produk> = ArrayList()
    private var listMakanan:ArrayList<Produk> = ArrayList()
    private var listPulsa:ArrayList<Produk> = ArrayList()
    private var listProduk:ArrayList<Produk> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin_kantin)

        btnAdminProfil = findViewById(R.id.btnAdminProfil)
        btnAdminProfil.setOnClickListener {
            val intent =  Intent(this@MainAdminKantinActivity, MasukActivity::class.java)
            startActivity(intent)
            finish()
        }

        setupView()
        setupList()
        setupListener()
    }

    override fun onStart() {
        super.onStart()
        getProdukMakanan()
        getProdukMinuman()
        getProdukPulsa()
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        rvProdukMakanan = findViewById(R.id.list_produk_makanan)
        rvProdukMakanan.layoutManager = layoutManager

        rvProdukMinuman = findViewById(R.id.list_produk_minuman)
        rvProdukMinuman.layoutManager = layoutManager2

        rvProdukPulsa = findViewById(R.id.list_produk_pulsa)
        rvProdukPulsa.layoutManager = layoutManager3

        listMakanan = listProduk.filter {
            it.kategori.contains("makanan",ignoreCase = true)
        } as ArrayList<Produk>

        listMinuman = listProduk.filter {
            it.kategori.contains("minuman",ignoreCase = true)
        } as ArrayList<Produk>

        listPulsa = listProduk.filter {
            it.kategori.contains("pulsa",ignoreCase = true)
        } as ArrayList<Produk>


        btnAddProduk = findViewById(R.id.btnAddProduk)
    }

    private fun setupList() {
        adapterAdminProdukMinuman = AdapterAdminProduk(arrayListOf(), object : AdapterAdminProduk.OnAdapterListener {

            override fun onUpdate(pro: Produk) {
                startActivity(
                    Intent(this@MainAdminKantinActivity, EditAdminActivity::class.java)
                        .putExtra("pro", pro)
                )
            }

            override fun onDelete(pro: Produk) {
                api.deleteAdminProduk(pro.id)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Produk berhasil dihapus !",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getProdukMinuman()
                                getProdukMakanan()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {}

                    })
            }

        },this@MainAdminKantinActivity)

        adapterAdminProdukMakanan = AdapterAdminProduk(arrayListOf(), object : AdapterAdminProduk.OnAdapterListener {

            override fun onUpdate(pro: Produk) {
                startActivity(
                    Intent(this@MainAdminKantinActivity, EditAdminActivity::class.java)
                        .putExtra("pro", pro)
                )
            }

            override fun onDelete(pro: Produk) {
                api.deleteAdminProduk(pro.id)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Produk berhasil dihapus !",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getProdukMakanan()
                                getProdukMinuman()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {}

                    })
            }

        },this@MainAdminKantinActivity)

        adapterAdminProdukPulsa = AdapterAdminProduk(arrayListOf(), object : AdapterAdminProduk.OnAdapterListener {

            override fun onUpdate(pro: Produk) {
                startActivity(
                    Intent(this@MainAdminKantinActivity, EditAdminActivity::class.java)
                        .putExtra("pro", pro)
                )
            }

            override fun onDelete(pro: Produk) {
                api.deleteAdminProduk(pro.id)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(
                            call: Call<Void>,
                            response: Response<Void>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Produk berhasil dihapus !",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getProdukMakanan()
                                getProdukMinuman()

                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {}

                    })
            }

        },this@MainAdminKantinActivity)



        rvProdukMakanan.adapter = adapterAdminProdukMakanan
        rvProdukMinuman.adapter = adapterAdminProdukMinuman
        rvProdukPulsa.adapter = adapterAdminProdukPulsa



    }

    private fun setupListener() {
        btnAddProduk.setOnClickListener {
            startActivity(Intent(this, CreateAdminActivity::class.java))
        }
    }


    private fun getProdukMakanan() {
        api.getAdminProdukMakanan().enqueue(object : Callback<List<Produk>> {
            override fun onResponse(
                call: Call<List<Produk>>,
                response: Response<List<Produk>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        adapterAdminProdukMakanan.setData(result)
                    }
                }
            }

            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {
                Log.e("MainAdminActivity", t.toString())
            }

        })
    }

    private fun getProdukMinuman() {
        api.getAdminProdukMinuman().enqueue(object : Callback<List<Produk>> {
            override fun onResponse(
                call: Call<List<Produk>>,
                response: Response<List<Produk>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        adapterAdminProdukMinuman.setData(result)
                    }
                }
            }

            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {
                Log.e("MainAdminActivity", t.toString())
            }

        })
    }

    private fun getProdukPulsa() {
        api.getAdminProdukPulsa().enqueue(object : Callback<List<Produk>> {
            override fun onResponse(
                call: Call<List<Produk>>,
                response: Response<List<Produk>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        adapterAdminProdukPulsa.setData(result)
                    }
                }
            }

            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {
                Log.e("MainAdminActivity", t.toString())
            }

        })
    }
}