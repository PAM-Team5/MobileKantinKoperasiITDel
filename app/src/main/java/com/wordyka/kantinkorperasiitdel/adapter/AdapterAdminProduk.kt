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
import com.wordyka.kantinkorperasiitdel.model.Produk
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterAdminProduk (val produk: ArrayList<Produk>, val listener: OnAdapterListener, var activity: Activity): RecyclerView.Adapter<AdapterAdminProduk.ViewHolder>() {
    private var listProduk:ArrayList<Produk> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_product, parent, false)
    )

    override fun onBindViewHolder(holder: AdapterAdminProduk.ViewHolder, position: Int) {
        val pro = produk[position]
        holder.tvProduk.text = produk[position].nama
        holder.tvHargaPcs.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(produk[position].hargaPcs)

        val image = "http://10.0.2.2:8000/storage/product/"+produk[position].gambar
        Glide.with(activity).load(image).placeholder(R.drawable.product).into(holder.imgProduk)

        holder.itemView.setOnClickListener {
            listener.onUpdate(pro)
        }
        holder.imgDelete.setOnClickListener {
            listener.onDelete(pro)
        }
    }

    override fun getItemCount() = produk.size

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvProduk = view.findViewById<TextView>(R.id.tvProduk)
        val tvHargaPcs = view.findViewById<TextView>(R.id.tv_hargaPcs)
        val imgDelete = view.findViewById<ImageView>(R.id.imgDelete)
        val imgProduk = view.findViewById<ImageView>(R.id.imgProduk)
    }

    fun setData(data : List<Produk>) {
        produk.clear()
        produk.addAll(data)
        listProduk.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onUpdate(pro : Produk)
        fun onDelete(pro : Produk)
    }
}