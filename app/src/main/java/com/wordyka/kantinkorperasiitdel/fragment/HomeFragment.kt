package com.wordyka.kantinkorperasiitdel.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.adapter.AdapterProduk
import com.wordyka.kantinkorperasiitdel.adapter.AdapterSlider
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var vp_slider : ViewPager

    lateinit var rv_makanan:RecyclerView
    lateinit var rv_minuman:RecyclerView
    lateinit var rv_pulsa:RecyclerView
    lateinit var rv_barang:RecyclerView
    lateinit var rv_ruangan:RecyclerView

    private var listProduk:ArrayList<Produk> = ArrayList()
    private var listMakanan:ArrayList<Produk> = ArrayList()
    private var listMinuman:ArrayList<Produk> = ArrayList()
    private var listPulsa:ArrayList<Produk> = ArrayList()
    private var listRuangan:ArrayList<Produk> = ArrayList()
    private var listBarang:ArrayList<Produk> = ArrayList()
    lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        init(view)
        getProduk()

        swipeLayout = view.findViewById(R.id.swipeRefreshLayoutProduk)
        swipeLayout.setOnRefreshListener {
            getProduk()
            swipeLayout.isRefreshing = false
        }


        return view
    }

    fun displayProduk() {
        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.slider1)
        arrSlider.add(R.drawable.slider2)
        arrSlider.add(R.drawable.slider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vp_slider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager3 = LinearLayoutManager(activity)
        layoutManager3.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager4 = LinearLayoutManager(activity)
        layoutManager4.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager5 = LinearLayoutManager(activity)
        layoutManager5.orientation = LinearLayoutManager.HORIZONTAL

        listMakanan = listProduk.filter {
            it.kategori.equals("makanan",ignoreCase = true)
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

        listBarang = listProduk.filter {
            it.kategori.equals("barang",ignoreCase = true)
        } as ArrayList<Produk>

        rv_makanan.adapter = AdapterProduk(requireActivity(), listMakanan)
        rv_makanan.layoutManager = layoutManager

        rv_minuman.adapter = AdapterProduk(requireActivity(), listMinuman)
        rv_minuman.layoutManager = layoutManager2

        rv_pulsa.adapter = AdapterProduk(requireActivity(), listPulsa)
        rv_pulsa.layoutManager = layoutManager3

        rv_ruangan.adapter = AdapterProduk(requireActivity(), listRuangan)
        rv_ruangan.layoutManager = layoutManager4

        rv_barang.adapter = AdapterProduk(requireActivity(), listBarang)
        rv_barang.layoutManager = layoutManager5
    }


    fun getProduk() {
        ApiConfig.instanceRetrofit.getProduk().enqueue(object :
            Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if(res.success == 1) {
                    listProduk = res.products
                    displayProduk()
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

        })
    }

    fun init(view: View) {
        vp_slider = view.findViewById(R.id.vp_slider)
        rv_makanan = view.findViewById(R.id.rv_makanan)
        rv_minuman = view.findViewById(R.id.rv_minuman)
        rv_pulsa = view.findViewById(R.id.rv_pulsa)
        rv_ruangan = view.findViewById(R.id.rv_ruangan)
        rv_barang = view.findViewById(R.id.rv_barang)
    }



}