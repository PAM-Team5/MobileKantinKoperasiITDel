package com.wordyka.kantinkorperasiitdel.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import com.wordyka.kantinkorperasiitdel.model.Pembelian
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
    private var listPesanUserBelumBayar: ArrayList<Pemesanan> = ArrayList()
    private var listPesanKosong: ArrayList<Pemesanan> = ArrayList()
    private var listProdukPesan: ArrayList<Pemesanan> = ArrayList()
    private var listProduk: ArrayList<Produk> = ArrayList()
    lateinit var tv_total: TextView
    lateinit var tv_bayar: EditText
    var jumlah: Int = 0
    lateinit var swipeLayout: SwipeRefreshLayout
    lateinit var sp: SharePref
    var idPembelian: Int = 0
    var total: Int = 0
    var totalHarga: Int = 0
    var jumlahProduk: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_keranjang, container, false)
        init(view)
        sp = SharePref(requireActivity())
        getPesan()
        bayarKeranjang(view)


        swipeLayout = view.findViewById(R.id.swipeRefreshLayout)
        swipeLayout.setOnRefreshListener {
            getPesan()
            swipeLayout.isRefreshing = false
        }

        return view
    }


    private fun init(view: View) {
        rv_pesan = view.findViewById(R.id.rv_pesan)
        tv_total = view.findViewById(R.id.tv_total)
    }


    private fun bayarKeranjang(view: View) {
        val tv_total = view.findViewById<TextView>(R.id.tv_total)
        val btn_bayar = view.findViewById<TextView>(R.id.btn_bayar)

        btn_bayar.setOnClickListener {
            if (!sp.getStatusLogin()) {
                startActivity(
                    Intent(
                        context,
                        LoginActivity::class.java
                    )
                )
            } else {
                val dialog = AlertDialog.Builder(view.context)
                val dialogView = layoutInflater.inflate(
                    R.layout.modal_pembayaran,
                    null
                )
                val tv_bayar = dialogView.findViewById<EditText>(R.id.tv_bayar)

                dialog.setView(dialogView)
                dialog.setPositiveButton(
                    "Bayar",
                    { dialogInterface: DialogInterface, i: Int -> })
                dialog.setNegativeButton(
                    "Batal",
                    { dialogInterface: DialogInterface, i: Int -> })
                val customDialog = dialog.create()
                customDialog.show()

                customDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener {
                        if (tv_bayar.text.isEmpty()) {
                            tv_bayar.error =
                                "Jumlah pembayaran harus diisi!"
                        } else if (!totalHarga.equals(tv_bayar.text.toString().toInt())) {
                            tv_bayar.error =
                                "Jumlah pembayaran tidak sesuai!"
                        } else {
                            val status = "bayar"
                            idPembelian += 1
                            listPesanUser.forEach {
                                while (it.ID_Pembelian >= idPembelian) {
                                    idPembelian += 1
                                }
                            }
                            jumlah = 0

                            listPesanUserBelumBayar.forEach {
                                jumlah += it.jumlah
                            }
                            ApiConfig.instanceRetrofit.tambahPembelian(
                                idPembelian,
                                jumlah,
                                totalHarga,
                                "belum-diantar",
                                "",
                                sp.getUser()!!.id
                            ).enqueue(object :
                                Callback<ResponModel> {
                                override fun onResponse(
                                    call: Call<ResponModel>,
                                    response: Response<ResponModel>
                                ) {
                                    for (item in listPesanUserBelumBayar) {
                                        ApiConfig.instanceRetrofit.bayarPemesanan(
                                            item.id,
                                            status,
                                            idPembelian
                                        ).enqueue(object :
                                            Callback<SubmitModel> {
                                            override fun onResponse(
                                                call: Call<SubmitModel>,
                                                response: Response<SubmitModel>
                                            ) {


                                                ApiConfig.instanceRetrofit.getProduk().enqueue(object :
                                                    Callback<ResponModel> {
                                                    override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                                                        val res = response.body()!!
                                                        if (res.success == 1) {
                                                            listProduk = res.products

                                                            listProduk = listProduk.filter {
                                                                it.id.equals(item.ID_Product)
                                                            } as ArrayList<Produk>

                                                            listProduk.forEach {
                                                                jumlahProduk = it.jumlah
                                                            }

                                                            var jumlahAfter : Int = jumlahProduk - item.jumlah

                                                            ApiConfig.instanceRetrofit.updateProduk(
                                                                item.ID_Product,
                                                                jumlahAfter
                                                            ).enqueue(object :
                                                                Callback<SubmitModel> {
                                                                override fun onResponse(
                                                                    call: Call<SubmitModel>,
                                                                    response: Response<SubmitModel>
                                                                ) {
                                                                    getPesan()
                                                                }

                                                                override fun onFailure(
                                                                    call: Call<SubmitModel>,
                                                                    t: Throwable
                                                                ) {

                                                                }

                                                            })

                                                            // jika jumlah barang 0

                                                            if(jumlahAfter<=0) {
                                                                ApiConfig.instanceRetrofit.updateProdukStatus(item.ID_Product,"Habis").enqueue(object :
                                                                    Callback<SubmitModel> {
                                                                    override fun onResponse(
                                                                        call: Call<SubmitModel>,
                                                                        response: Response<SubmitModel>
                                                                    ) {
                                                                        getPesan()
                                                                    }

                                                                    override fun onFailure(
                                                                        call: Call<SubmitModel>,
                                                                        t: Throwable
                                                                    ) {
                                                                    }

                                                                })
                                                            }


                                                        }
                                                    }

                                                    override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                                                    }

                                                })



                                                Toast.makeText(
                                                    context,
                                                    "Pembayaran berhasil dilakukan",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            override fun onFailure(
                                                call: Call<SubmitModel>,
                                                t: Throwable
                                            ) {
                                                Toast.makeText(
                                                    context,
                                                    "Pembayaran gagal dilakukan",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                getPesan()
                                            }

                                        })
                                    }
                                }

                                override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                                    TODO("Not yet implemented")
                                }

                            })


                            customDialog.dismiss()
                        }


                    }

                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setOnClickListener {
                        customDialog.dismiss()
                    }
            }

        }

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

                        listPesanUserBelumBayar = listPesanUser.filter {
                            it.status.equals("belum-dibayar", ignoreCase = true)
                        } as ArrayList<Pemesanan>

                        total = 0

                        listPesanUserBelumBayar.forEach {
                            total += it.harga.toInt()
                        }
                        tv_total.text = total.toString()
                        totalHarga = tv_total.text.toString().toInt()
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
                                        it.ID_Product.equals(pesan.ID_Product)
                                    } as ArrayList<Pemesanan>

                                    for (item in listPesanUserBelumBayar) {
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
                                    } as ArrayList<Pemesanan>

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
                            listPesanUserBelumBayar
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