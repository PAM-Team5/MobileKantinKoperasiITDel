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
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.DetailProductActivity
import com.wordyka.kantinkorperasiitdel.model.Produk
import java.text.NumberFormat
import java.util.*

class AdapterProduk(var activity:Activity, var data:ArrayList<Produk>):RecyclerView.Adapter<AdapterProduk.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: AdapterProduk.Holder, position: Int) {
        val pro = data[position]
        holder.tvNama.text = data[position].nama
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(data[position].hargaPcs)

        val image = "http://10.0.2.2:8000/storage/product/"+data[position].gambar

        Glide.with(this.activity).load(image).placeholder(R.drawable.product).into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val intentActivityProduct = Intent(activity, DetailProductActivity::class.java)

            val str = Gson().toJson(data[position], Produk::class.java)
            intentActivityProduct.putExtra("extra",str)

            activity.startActivity(intentActivityProduct)
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}