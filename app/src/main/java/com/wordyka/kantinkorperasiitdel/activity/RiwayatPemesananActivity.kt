package com.wordyka.kantinkorperasiitdel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.adapter.AdapterAdminProduk
import com.wordyka.kantinkorperasiitdel.adapter.AdapterPembelian
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Pembelian
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPemesananActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    lateinit var rv_riwayat: RecyclerView
    lateinit var sp: SharePref
    private lateinit var adapterPembelian: AdapterPembelian
    private var listBeli: ArrayList<Pembelian> = ArrayList()
    private var listBeliUser: ArrayList<Pembelian> = ArrayList()
    lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pemesanan)
        sp = SharePref(this)

        setupView()
        getPembelian()

        swipeLayout = findViewById(R.id.swipeRefreshLayoutCustomer)
        swipeLayout.setOnRefreshListener {
            getPembelian()
            swipeLayout.isRefreshing = false
        }

    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@RiwayatPemesananActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat = findViewById(R.id.rv_riwayat)
        rv_riwayat.layoutManager = layoutManager

        adapterPembelian = AdapterPembelian(this@RiwayatPemesananActivity,object : AdapterPembelian.OnAdapterListener {
            override fun onUpdate(beli: Pembelian) {
                sp.setPembelian(beli)

                val intent = Intent(this@RiwayatPemesananActivity,DetailRiwayatPemesananCustomerActivity::class.java)
                intent.putExtra("riwayat", beli.id)
                startActivity(intent)
            }

        }, arrayListOf())

        rv_riwayat.adapter = adapterPembelian
    }

    private fun getPembelian() {
        api.getPembelian(sp.getUser()!!.id).enqueue(object :Callback<List<Pembelian>> {
            override fun onResponse(
                call: Call<List<Pembelian>>,
                response: Response<List<Pembelian>>
            ) {
                if(response.isSuccessful) {
                    val result = response.body()
                    if(result!=null) {
                        adapterPembelian.setData(result)
                    }
                }
            }

            override fun onFailure(call: Call<List<Pembelian>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}