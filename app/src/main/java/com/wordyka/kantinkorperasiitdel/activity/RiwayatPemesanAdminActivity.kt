package com.wordyka.kantinkorperasiitdel.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.adapter.AdapterPembelian
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.app.SubmitModel
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Pembelian
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class RiwayatPemesanAdminActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    lateinit var rv_riwayat: RecyclerView
    lateinit var sp: SharePref
    private lateinit var adapterPembelian: AdapterPembelian
    private var listBeli: ArrayList<Pembelian> = ArrayList()
    private var listBeliUser: ArrayList<Pembelian> = ArrayList()
    lateinit var swipeLayout: SwipeRefreshLayout
    lateinit var newStatus:String
    lateinit var tv_pendapatan:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat_pemesan_admin)
        sp = SharePref(this)

        tv_pendapatan = findViewById(R.id.tv_pendapatan)

        setupView()
        getPembelian()

        swipeLayout = findViewById(R.id.swipeRefreshLayout)
        swipeLayout.setOnRefreshListener {
            getPembelian()
            swipeLayout.isRefreshing = false
        }


    }



    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@RiwayatPemesanAdminActivity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        rv_riwayat = findViewById(R.id.rv_riwayat)
        rv_riwayat.layoutManager = layoutManager

        adapterPembelian = AdapterPembelian(this@RiwayatPemesanAdminActivity,object : AdapterPembelian.OnAdapterListener {
            override fun onUpdate(beli: Pembelian) {
                val items = arrayOf("Proses Pengantaran","Telah Diantar","Dibatalkan")
                val builder = AlertDialog.Builder(this@RiwayatPemesanAdminActivity)


                builder.setTitle("Pilih Status")
                builder.setSingleChoiceItems(items, -1) {
                        dialog, which ->
                    Toast.makeText(
                        applicationContext, items[which] + " dipilih", Toast.LENGTH_SHORT
                    ).show()
                    newStatus = items[which]
                }

                builder.setPositiveButton("Pilih") { dialog: DialogInterface, which: Int ->

                }

                builder.setNeutralButton("Batal") { dialog, which ->
                    dialog.cancel()
                }

                val mDialog = builder.create()
                mDialog.show()

                mDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    api.updatePembelianAdmin(beli.id,newStatus).enqueue(object :
                        Callback<SubmitModel> {
                        override fun onResponse(
                            call: Call<SubmitModel>,
                            response: Response<SubmitModel>
                        ) {
                            Toast.makeText(this@RiwayatPemesanAdminActivity,"Status berhasil diubah",Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                            Toast.makeText(this@RiwayatPemesanAdminActivity,"Status gagal diubah",Toast.LENGTH_SHORT).show()
                        }

                    })
                    mDialog.dismiss()
                }
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
                var total : Int = 0
                if(response.isSuccessful) {
                    val result = response.body()
                    if(result!=null) {
                        adapterPembelian.setData(result)
                    }
                    result!!.forEach {
                        total += it.harga
                    }
                    tv_pendapatan.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(total)
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