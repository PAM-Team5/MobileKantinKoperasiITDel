package com.wordyka.kantinkorperasiitdel.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import java.util.ArrayList

class AdapterRiwayatCustomer(var activity: Activity, var data: ArrayList<Pemesanan>): RecyclerView.Adapter<AdapterRiwayatCustomer.Holder>() {

    private var listPemesanan: ArrayList<Pemesanan> = ArrayList()

    class Holder(view: View): RecyclerView.ViewHolder(view) {
        val tvProduk = view.findViewById<TextView>(R.id.tvProduk)
        val tvJumlah = view.findViewById<TextView>(R.id.tvJumlah)
        val imgProduk = view.findViewById<ImageView>(R.id.imgProduk)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat_customer, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.tvProduk.text = data[position].nama
        holder.tvJumlah.text = data[position].jumlah.toString()
        val image = "http://10.0.2.2:8000/storage/product/"+data[position].gambar
        Glide.with(this.activity).load(image).placeholder(R.drawable.product).into(holder.imgProduk)

        holder.itemView.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(pesan : List<Pemesanan>) {
        data.clear()
        data.addAll(pesan)
        listPemesanan.addAll(pesan)
        notifyDataSetChanged()
    }

}