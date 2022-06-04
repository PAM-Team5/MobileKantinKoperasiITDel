package com.wordyka.kantinkorperasiitdel.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.wordyka.kantinkorperasiitdel.MainActivity
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.DetailProductActivity
import com.wordyka.kantinkorperasiitdel.model.Pemesanan
import java.text.NumberFormat
import java.util.*


class AdapterPesan(var activity: Activity, var data: ArrayList<Pemesanan>):RecyclerView.Adapter<AdapterPesan.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].nama
        holder.tvJumlah.text = data[position].jumlah.toString()
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(data[position].hargaPcs)

        val image = "http://10.0.2.2:8000/storage/product/"+data[position].gambar

        Glide.with(this.activity).load(image).placeholder(R.drawable.product).into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val intentActivityPesan = Intent(activity, MainActivity::class.java)

            val str = Gson().toJson(data[position], Pemesanan::class.java)
            intentActivityPesan.putExtra("extraPesan",str)

            activity.startActivity(intentActivityPesan)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }




}