package com.wordyka.kantinkorperasiitdel.fragment

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
import com.google.gson.Gson
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.adapter.AdapterPesan
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

/**
 * A simple [Fragment] subclass.
 * Use the [KeranjangFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class KeranjangFragment : Fragment() {

    lateinit var rv_pesan: RecyclerView

    private var listPesan:ArrayList<Pemesanan> = ArrayList()
    private var listPesanUser:ArrayList<Pemesanan> = ArrayList()
    private var listPesanKosong:ArrayList<Pemesanan> = ArrayList()
    lateinit var tv_total:TextView
    lateinit var sp: SharePref
    var total : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_keranjang, container, false)
        init(view)
        sp = SharePref(requireActivity())
        getPesan()


        val btn_delete = view.findViewById<ImageView>(R.id.btn_delete)
        btn_delete?.setOnClickListener {
            ApiConfig.instanceRetrofit.deletePesan(sp.getPesan()!!.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    Toast.makeText(
                        context,
                        "Pesanan Anda dibatalkan",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Berhasil","Berhasil")
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    TODO("Not yet implemented")
                    Log.d("gagal","gagal")
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

                        listPesan = res.pembayarans

                        val layoutManager = LinearLayoutManager(activity)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL

                        if(sp.getStatusLogin()) {
                            listPesanUser = listPesan.filter {
                                it.ID_User == sp.getUser()!!.id
                            } as ArrayList<Pemesanan>

                            listPesanUser.forEach {
                                total += it.hargaPcs.toInt()
                            }

                            tv_total.text = total.toString()

                            rv_pesan.adapter = AdapterPesan(requireActivity(), listPesanUser)

                        } else {
                            rv_pesan.adapter = AdapterPesan(requireActivity(), listPesanKosong)
                        }
                        rv_pesan.layoutManager = layoutManager


                    }

                }

                override fun onFailure(call: Call<ResponModel>, t: Throwable) {

                }

            })

    }



}