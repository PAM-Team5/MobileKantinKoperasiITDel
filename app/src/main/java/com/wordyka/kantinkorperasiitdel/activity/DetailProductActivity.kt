package com.wordyka.kantinkorperasiitdel.activity

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.wordyka.kantinkorperasiitdel.MainActivity
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.databinding.FragmentKeranjangBinding
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.Produk
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import com.wordyka.kantinkorperasiitdel.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

class DetailProductActivity : AppCompatActivity() {

    lateinit var produk: Produk
    lateinit var sp: SharePref

    private val api by lazy { ApiConfig.instanceRetrofit }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        sp = SharePref(this)
        getInfo()

        showTextDialog()
    }

    private fun showTextDialog() {
        val btn_pesan = findViewById<TextView>(R.id.btn_pesan)


        btn_pesan.setOnClickListener {
            if (sp.getStatusLogin() == false) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                val dialog = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.modal_pemesanan, null)
                val tv_pesan = dialogView.findViewById<EditText>(R.id.tv_pesan)
                dialog.setView(dialogView)
                dialog.setPositiveButton("Kirim", { dialogInterface: DialogInterface, i: Int -> })
                dialog.setNegativeButton("Batal", { dialogInterface: DialogInterface, i: Int -> })
                val customDialog = dialog.create()
                customDialog.show()

                customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    customDialog.dismiss()
                    ApiConfig.instanceRetrofit.tambahPesan(
                        produk.nama,
                        produk.kategori,
                        tv_pesan.text.toString().toInt(),
                        produk.status,
                        produk.gambar,
                        produk.deskripsi,
                        produk.hargaPcs * tv_pesan.text.toString().toBigInteger(),
                        produk.id,
                        sp.getUser()!!.id
                    ).enqueue(object :
                        Callback<ResponModel> {
                        override fun onResponse(
                            call: Call<ResponModel>,
                            response: Response<ResponModel>
                        ) {
                            val intent =
                                Intent(this@DetailProductActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                            Toast.makeText(
                                this@DetailProductActivity,
                                "Pesanan Anda ditambah ke keranjang",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                            Toast.makeText(
                                this@DetailProductActivity,
                                "Pesanan Anda gagal ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })

                }

                customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                    customDialog.dismiss()
                }
            }

        }
    }


    private fun openDialog(context: Context) {
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.modal_pemesanan, null)
        val tv_pesan = dialogLayout.findViewById<EditText>(R.id.tv_pesan)
        val builder = AlertDialog.Builder(dialogLayout.context)

        // set title
        builder.setTitle("Tambah Pesanan")


        //set negative button
        builder.setPositiveButton(
            "Update Now"
        ) { dialog, id ->
            // User clicked Update Now button
            api.tambahPesan(
                produk.nama,
                produk.kategori,
                tv_pesan.text.toString().toInt(),
                produk.status,
                produk.gambar,
                produk.deskripsi,
                produk.hargaPcs * tv_pesan.text.toString().toBigInteger(),
                produk.id,
                sp.getUser()!!.id
            )
        }

        //set positive button
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, id ->
            Toast.makeText(applicationContext, "Dibatalkan", Toast.LENGTH_SHORT).show()
        }

        builder.show()

    }


    private fun getInfo() {
        val dataProduct = intent.getStringExtra("extra")

        produk = Gson().fromJson<Produk>(dataProduct, Produk::class.java)

        val tv_nama = findViewById<TextView>(R.id.tv_nama)
        val tv_harga = findViewById<TextView>(R.id.tv_harga)
        val tv_deskripsi = findViewById<TextView>(R.id.tv_deskripsi)
        val tv_kategori = findViewById<TextView>(R.id.tv_kategori)
        val tv_status = findViewById<TextView>(R.id.tv_status)
        val tv_stok = findViewById<TextView>(R.id.tv_stok)
        val img_produk = findViewById<ImageView>(R.id.image)


        // set value
        tv_nama.text = produk.nama
        tv_harga.text = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(produk.hargaPcs)
        tv_deskripsi.text = produk.deskripsi
        tv_kategori.text = produk.kategori
        tv_status.text = produk.status
        tv_stok.text = produk.jumlah.toString()


        val img = "http://10.0.2.2:8000/storage/product/" + produk.gambar



        Glide.with(this).load(img).placeholder(R.drawable.product).error(R.drawable.product)
            .into(img_produk)

        // set toolbarr
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = produk.nama
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}