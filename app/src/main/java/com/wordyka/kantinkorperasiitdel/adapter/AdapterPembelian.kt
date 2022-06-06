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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wordyka.kantinkorperasiitdel.MainActivity
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.DetailRiwayatPemesananActivity
import com.wordyka.kantinkorperasiitdel.activity.RiwayatPemesananActivity
import com.wordyka.kantinkorperasiitdel.model.Pembelian
import com.wordyka.kantinkorperasiitdel.model.Produk
import java.text.NumberFormat
import java.util.*


class AdapterPembelian(var activity: Activity,  val listener: OnAdapterListener, var data: ArrayList<Pembelian>):RecyclerView.Adapter<AdapterPembelian.Holder>() {
    private var listPembelian:ArrayList<Pembelian> = ArrayList()

    class Holder(view: View):RecyclerView.ViewHolder(view) {
        val tvTanggal = view.findViewById<TextView>(R.id.tvTanggal)
        val tvJumlah = view.findViewById<TextView>(R.id.tvJumlah)
        val tvHarga = view.findViewById<TextView>(R.id.tvHarga)
        val tvStatus = view.findViewById<TextView>(R.id.tvStatus)
        val layout = view.findViewById<CardView>(R.id.layoutRiwayat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: AdapterPembelian.Holder, position: Int) {
        val beli = data[position]
        holder.tvTanggal.text = data[position].created_at.toString()
        holder.tvJumlah.text = data[position].jumlah.toString()
        holder.tvHarga.text = NumberFormat.getCurrencyInstance(Locale("in","ID")).format(data[position].harga)

        if(data[position].status.equals("belum-diantar")) {
            holder.tvStatus.text = "Proses Pengantaran"
        } else {
            holder.tvStatus.text = "Sedang Diproses"
        }

        holder.itemView.setOnClickListener {
            listener.onUpdate(beli)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(beli : List<Pembelian>) {
        data.clear()
        data.addAll(beli)
        listPembelian.addAll(beli)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onUpdate(beli : Pembelian)
        //fun onDelete(pro : Produk)
    }

}