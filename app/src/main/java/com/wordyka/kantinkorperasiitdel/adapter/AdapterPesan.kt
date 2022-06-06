package com.wordyka.kantinkorperasiitdel.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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
import com.wordyka.kantinkorperasiitdel.model.Produk
import java.text.NumberFormat
import java.util.*


class AdapterPesan(var activity: Activity, val listener: AdapterPesan.OnAdapterListener, var data: ArrayList<Pemesanan>):RecyclerView.Adapter<AdapterPesan.Holder>() {

    class Holder(view: View):RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        val tvDeskripsi = view.findViewById<TextView>(R.id.tv_deskripsi)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
        val tv_jumlah = view.findViewById<TextView>(R.id.tv_jumlah)
        val btn_kurang = view.findViewById<ImageView>(R.id.btn_kurang)
        val btn_tambah = view.findViewById<ImageView>(R.id.btn_tambah)
        val btn_delete = view.findViewById<ImageView>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val pesan = data[position]
        holder.tvNama.text = data[position].nama
        holder.tv_jumlah.text = data[position].jumlah.toString()
        holder.tvDeskripsi.text = data[position].deskripsi
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(data[position].hargaPcs)

        val image = "http://10.0.2.2:8000/storage/product/"+data[position].gambar

        Glide.with(this.activity).load(image).placeholder(R.drawable.product).into(holder.imgProduk)


        holder.btn_tambah.setOnClickListener {
            listener.onUpdateTambahPesan(pesan,holder.tv_jumlah)
        }
        holder.btn_kurang.setOnClickListener {
            listener.onUpdateKurangPesan(pesan,holder.tv_jumlah)
        }
        holder.btn_delete.setOnClickListener {
            listener.onDeletePesan(pesan)
        }
        holder.btn_delete.setOnClickListener {
            listener.onDeletePesan(pesan)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnAdapterListener {
        fun onUpdateTambahPesan(pesan : Pemesanan, textView: TextView)
        fun onUpdateKurangPesan(pesan : Pemesanan, textView: TextView)
        fun onDeletePesan(pesan : Pemesanan)
    }


}