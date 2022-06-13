package com.wordyka.kantinkorperasiitdel.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.adapter.AdapterPembelian
import com.wordyka.kantinkorperasiitdel.adapter.AdapterRiwayat
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.app.SubmitModel
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailRiwayatPemesananCustomerActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    lateinit var rv_detail_riwayat: RecyclerView
    lateinit var sp: SharePref
    private lateinit var adapterRiwayat: AdapterRiwayat
    private var listUser : ArrayList<User> = ArrayList()
    lateinit var tv_pemesan: TextView
    lateinit var tv_telp: TextView
    lateinit var tv_totalPesan: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_riwayat_pemesanan_customer)
        sp = SharePref(this)

        init()
        setupView()
        getRiwayat()
    }

    private fun init() {
        tv_pemesan = findViewById(R.id.tv_pemesan)
        tv_telp = findViewById(R.id.tv_telp)
        tv_totalPesan = findViewById(R.id.tv_totalPesan)

        tv_totalPesan.text =
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(sp.getPembelian()!!.harga)

        customer()

    }


    private fun customer() {
        api.getCustomer(sp.getPembelian()!!.ID_User).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val result = response.body()!!
                listUser.clear()
                listUser.addAll(result)

                listUser.forEach {
                    tv_pemesan.text = it.name
                    tv_telp.text = it.phone
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                println("Gagal: " + t.message)
            }

        })
    }


    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@DetailRiwayatPemesananCustomerActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rv_detail_riwayat = findViewById(R.id.rv_detail_riwayat)
        rv_detail_riwayat.layoutManager = layoutManager

        adapterRiwayat = AdapterRiwayat(this@DetailRiwayatPemesananCustomerActivity, arrayListOf())

        rv_detail_riwayat.adapter = adapterRiwayat
    }

    private fun getRiwayat() {
        val bundle = intent.extras

        api.getPemesananRiwayat(sp.getPembelian()!!.id).enqueue(object : Callback<List<Pemesanan>> {
            override fun onResponse(
                call: Call<List<Pemesanan>>,
                response: Response<List<Pemesanan>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        adapterRiwayat.setData(result)
                    }

                }
            }

            override fun onFailure(call: Call<List<Pemesanan>>, t: Throwable) {

            }

        })


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}