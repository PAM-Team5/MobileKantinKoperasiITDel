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
import com.wordyka.kantinkorperasiitdel.model.Pembelian
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import com.wordyka.kantinkorperasiitdel.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailRiwayatPemesananActivity : AppCompatActivity() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    lateinit var rv_detail_riwayat: RecyclerView
    lateinit var sp: SharePref
    private lateinit var adapterRiwayat: AdapterRiwayat
    private var listUser = User()
    lateinit var tv_pemesan: TextView
    lateinit var tv_telp: TextView
    lateinit var tv_totalPesan: TextView
    lateinit var btn_updateStatus: TextView
    lateinit var newStatus:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_riwayat_pemesanan)
        sp = SharePref(this)

        init()
        setupView()
        getRiwayat()
    }

    private fun init() {
        tv_pemesan = findViewById(R.id.tv_pemesan)
        tv_telp = findViewById(R.id.tv_telp)
        tv_totalPesan = findViewById(R.id.tv_totalPesan)
        btn_updateStatus = findViewById(R.id.btn_updateStatus)

        tv_totalPesan.text =
            NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(sp.getPembelian()!!.harga)

        btn_updateStatus.setOnClickListener {
            val items = arrayOf("Proses Pengantaran", "Telah Diantar", "Selesai", "Dibatalkan")
            val builder = AlertDialog.Builder(this@DetailRiwayatPemesananActivity)


            builder.setTitle("Pilih Status")
            builder.setSingleChoiceItems(items, -1) { dialog, which ->
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
                api.updatePembelianAdmin(sp.getPembelian()!!.id, newStatus).enqueue(object :
                    Callback<SubmitModel> {
                    override fun onResponse(
                        call: Call<SubmitModel>,
                        response: Response<SubmitModel>
                    ) {
                        Toast.makeText(
                            this@DetailRiwayatPemesananActivity,
                            "Status berhasil diubah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                        Toast.makeText(
                            this@DetailRiwayatPemesananActivity,
                            "Status gagal diubah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
                mDialog.dismiss()
            }
        }
    }


    private fun setupView() {
        val layoutManager = LinearLayoutManager(this@DetailRiwayatPemesananActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        rv_detail_riwayat = findViewById(R.id.rv_detail_riwayat)
        rv_detail_riwayat.layoutManager = layoutManager

        adapterRiwayat = AdapterRiwayat(this@DetailRiwayatPemesananActivity, arrayListOf())

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
}