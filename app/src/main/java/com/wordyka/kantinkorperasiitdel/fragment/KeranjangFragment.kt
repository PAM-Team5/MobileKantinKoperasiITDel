package com.wordyka.kantinkorperasiitdel.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.LoginActivity
import com.wordyka.kantinkorperasiitdel.adapter.AdapterAdminProduk
import com.wordyka.kantinkorperasiitdel.adapter.AdapterPesan
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.app.SubmitModel
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [KeranjangFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KeranjangFragment : Fragment() {
    private val api by lazy { ApiConfig.instanceRetrofit }
    lateinit var rv_pesan: RecyclerView

    private var listPesan: ArrayList<Pemesanan> = ArrayList()
    private var listPesanUser: ArrayList<Pemesanan> = ArrayList()
    private var listPesanKosong: ArrayList<Pemesanan> = ArrayList()
    private var listProdukPesan: ArrayList<Pemesanan> = ArrayList()
    lateinit var tv_total: TextView
    lateinit var swipeLayout: SwipeRefreshLayout
    lateinit var sp: SharePref
    var total: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_keranjang, container, false)
        init(view)
        sp = SharePref(requireActivity())
        getPesan()

        val btn_bayar = view.findViewById<TextView>(R.id.btn_bayar)
        btn_bayar?.setOnClickListener {
            if (!sp.getStatusLogin()) {
                startActivity(Intent(activity, LoginActivity::class.java))
            } else {
                Toast.makeText(context, "Anda berhasil melakukan pembayaran", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        swipeLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeLayout.setOnRefreshListener {
            getPesan()
            swipeLayout.isRefreshing = false
        }


        val btn_delete = view.findViewById<ImageView>(R.id.btn_delete)
        btn_delete?.setOnClickListener {
            ApiConfig.instanceRetrofit.deletePesan(sp.getPesan()!!.id)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        Toast.makeText(
                            context,
                            "Pesanan Anda dibatalkan",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Berhasil", "Berhasil")
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        TODO("Not yet implemented")
                        Log.d("gagal", "gagal")
                    }

                })
        }

        return view
    }


    private fun init(view: View) {
        rv_pesan = view.findViewById(R.id.rv_pesan)
        tv_total = view.findViewById(R.id.tv_total)
    }


    private fun getPesan() {


        ApiConfig.instanceRetrofit.getPemesanan().enqueue(object :
            Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {

                    listPesan = res.pemesanan


                    val layoutManager = LinearLayoutManager(activity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL

                    if (sp.getStatusLogin()) {
                        listPesanUser = listPesan.filter {
                            it.ID_User == sp.getUser()!!.id
                        } as ArrayList<Pemesanan>

                        total = 0

                        listPesanUser.forEach {
                            total += it.harga.toInt()
                        }
                        tv_total.text = total.toString()
                        tv_total.text = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                            .format(tv_total.text.toString().toInt())

                        rv_pesan.adapter = AdapterPesan(
                            requireActivity(),
                            object : AdapterPesan.OnAdapterListener {
                                override fun onUpdateTambahPesan(
                                    pesan: Pemesanan,
                                    textView: TextView
                                ) {

                                    var harga = 0

                                    listProdukPesan = listPesanUser.filter {
                                        it.ID_Product == pesan.ID_Product
                                    }as ArrayList<Pemesanan>

                                    for (item in listProdukPesan) {
                                        harga = item.hargaPcs.toInt()
                                    }

                                    var tambah = pesan.jumlah + 1
                                    api.updatePemesanan(
                                        pesan.id,
                                        tambah,
                                        tambah.toBigInteger() * harga.toBigInteger()
                                    ).enqueue(object : Callback<SubmitModel> {
                                        override fun onResponse(
                                            call: Call<SubmitModel>,
                                            response: Response<SubmitModel>
                                        ) {
                                            textView.text = tambah.toString()
                                            getPesan()
                                        }

                                        override fun onFailure(
                                            call: Call<SubmitModel>,
                                            t: Throwable
                                        ) {
                                            TODO("Not yet implemented")
                                        }

                                    })


                                }

                                override fun onUpdateKurangPesan(
                                    pesan: Pemesanan,
                                    textView: TextView
                                ) {
                                    var harga = 0

                                    listProdukPesan = listPesanUser.filter {
                                        it.ID_Product == pesan.ID_Product
                                    }as ArrayList<Pemesanan>

                                    for (item in listProdukPesan) {
                                        harga = item.hargaPcs.toInt()
                                    }


                                    var kurang = pesan.jumlah - 1
                                    api.updatePemesanan(
                                        pesan.id,
                                        kurang,
                                        kurang.toBigInteger() * harga.toBigInteger()
                                    ).enqueue(object : Callback<SubmitModel> {
                                        override fun onResponse(
                                            call: Call<SubmitModel>,
                                            response: Response<SubmitModel>
                                        ) {
                                            textView.text = kurang.toString()
                                            getPesan()
                                        }

                                        override fun onFailure(
                                            call: Call<SubmitModel>,
                                            t: Throwable
                                        ) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                }

                                override fun onDeletePesan(pesan: Pemesanan) {
                                    api.deletePesan(pesan.id).enqueue(object : Callback<Void> {
                                        override fun onResponse(
                                            call: Call<Void>,
                                            response: Response<Void>
                                        ) {
                                            if (response.isSuccessful) {
                                                Toast.makeText(
                                                    context,
                                                    "Pesanan dibatalkan !",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                getPesan()
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            TODO("Not yet implemented")
                                        }

                                    })
                                }

                            },
                            listPesanUser
                        )

                    } else {
                        rv_pesan.adapter = AdapterPesan(
                            requireActivity(),
                            object : AdapterPesan.OnAdapterListener {
                                override fun onUpdateTambahPesan(
                                    pesan: Pemesanan,
                                    textView: TextView
                                ) {
                                    TODO("Not yet implemented")
                                }

                                override fun onUpdateKurangPesan(
                                    pesan: Pemesanan,
                                    textView: TextView
                                ) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDeletePesan(pesan: Pemesanan) {
                                    TODO("Not yet implemented")
                                }

                            },
                            listPesanKosong
                        )
                    }
                    rv_pesan.layoutManager = layoutManager


                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

        })

    }


}