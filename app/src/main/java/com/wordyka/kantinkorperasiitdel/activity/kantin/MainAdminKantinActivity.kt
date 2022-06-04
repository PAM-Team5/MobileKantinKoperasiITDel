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
import com.wordyka.kantinkorperasiitdel.activity.koperasi.EditAdminActivity
import com.wordyka.kantinkorperasiitdel.activity.MasukActivity
import com.wordyka.kantinkorperasiitdel.adapter.AdapterAdminProduk
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Produk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAdminKantinActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    private lateinit var adapterAdminProdukMakanan: AdapterAdminProduk
    private lateinit var adapterAdminProdukMinuman: AdapterAdminProduk
    private lateinit var adapterAdminProdukPulsa: AdapterAdminProduk
    private lateinit var adapterAdminProdukRuangan: AdapterAdminProduk

    private lateinit var rvProdukMakanan: RecyclerView
    private lateinit var rvProdukMinuman: RecyclerView
    private lateinit var rvProdukPulsa: RecyclerView
    private lateinit var rvProdukRuangan: RecyclerView

    private lateinit var btnAddProduk: FloatingActionButton
    private lateinit var btnAdminProfil: ImageView

    private var listMinuman:ArrayList<Produk> = ArrayList()
    private var listMakanan:ArrayList<Produk> = ArrayList()
    private var listPulsa:ArrayList<Produk> = ArrayList()
    private var listRuangan:ArrayList<Produk> = ArrayList()
    private var listProduk:ArrayList<Produk> = ArrayList()

    lateinit var sp: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_admin_kantin)

        sp = SharePref(this)

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
        getProdukRuangan()
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager4 = LinearLayoutManager(this@MainAdminKantinActivity)
        layoutManager4.orientation = LinearLayoutManager.HORIZONTAL

        rvProdukMakanan = findViewById(R.id.list_produk_makanan)
        rvProdukMakanan.layoutManager = layoutManager

        rvProdukMinuman = findViewById(R.id.list_produk_minuman)
        rvProdukMinuman.layoutManager = layoutManager2

        rvProdukPulsa = findViewById(R.id.list_produk_pulsa)
        rvProdukPulsa.layoutManager = layoutManager3

        rvProdukRuangan = findViewById(R.id.list_produk_ruangan)
        rvProdukRuangan.layoutManager = layoutManager4

        listMakanan = listProduk.filter {
            it.kategori.equals("makanan",ignoreCase = true) &&
            it.role.equals("admin-kantin", ignoreCase = true)
        } as ArrayList<Produk>

        listMinuman = listProduk.filter {
            it.kategori.equals("minuman",ignoreCase = true)
        } as ArrayList<Produk>

        listPulsa = listProduk.filter {
            it.kategori.equals("pulsa",ignoreCase = true)
        } as ArrayList<Produk>

        listRuangan = listProduk.filter {
            it.kategori.equals("ruangan",ignoreCase = true)
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
                                getProdukMakanan()
                                getProdukMinuman()
                                getProdukPulsa()
                                getProdukRuangan()
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
                                getProdukPulsa()
                                getProdukRuangan()
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
                                getProdukPulsa()
                                getProdukRuangan()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {}

                    })
            }

        },this@MainAdminKantinActivity)


        adapterAdminProdukRuangan = AdapterAdminProduk(arrayListOf(), object : AdapterAdminProduk.OnAdapterListener {

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
                                getProdukPulsa()
                                getProdukRuangan()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {}

                    })
            }

        },this@MainAdminKantinActivity)



        rvProdukMakanan.adapter = adapterAdminProdukMakanan
        rvProdukMinuman.adapter = adapterAdminProdukMinuman
        rvProdukPulsa.adapter = adapterAdminProdukPulsa
        rvProdukRuangan.adapter = adapterAdminProdukRuangan
    }

    private fun setupListener() {
        btnAddProduk.setOnClickListener {
            startActivity(Intent(this, CreateAdminKantinActivity::class.java))
        }
    }


    private fun getProdukMakanan() {
        api.getAdminProdukMakananKantin().enqueue(object : Callback<List<Produk>> {
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
        api.getAdminProdukMinumanKantin().enqueue(object : Callback<List<Produk>> {
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
        api.getAdminProdukPulsaKantin().enqueue(object : Callback<List<Produk>> {
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


    private fun getProdukRuangan() {
        api.getAdminProdukRuanganKantin().enqueue(object : Callback<List<Produk>> {
            override fun onResponse(
                call: Call<List<Produk>>,
                response: Response<List<Produk>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        adapterAdminProdukRuangan.setData(result)
                    }
                }
            }

            override fun onFailure(call: Call<List<Produk>>, t: Throwable) {
                Log.e("MainAdminActivity", t.toString())
            }

        })
    }
}