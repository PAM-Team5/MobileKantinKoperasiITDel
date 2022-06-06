package com.wordyka.kantinkorperasiitdel.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.adapter.AdapterPembelian
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Pembelian
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiwayatPemesanAdminActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    lateinit var rv_riwayat: RecyclerView
    lateinit var sp: SharePref
    private lateinit var adapterPembelian: AdapterPembelian
    private var listBeli: ArrayList<Pembelian> = ArrayList()
    private var listBeliUser: ArrayList<Pembelian> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pemesan_admin)
        sp = SharePref(this)

        setupView()
        getPembelian()

    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@RiwayatPemesanAdminActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat = findViewById(R.id.rv_riwayat)
        rv_riwayat.layoutManager = layoutManager

        adapterPembelian = AdapterPembelian(this@RiwayatPemesanAdminActivity,object : AdapterPembelian.OnAdapterListener {
            override fun onUpdate(beli: Pembelian) {
                TODO("Not yet implemented")
            }

        }, arrayListOf())

        rv_riwayat.adapter = adapterPembelian
    }

    private fun getPembelian() {
        api.getPembelianAdmin().enqueue(object : Callback<List<Pembelian>> {
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