package com.wordyka.kantinkorperasiitdel.activity.koperasi

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.app.SubmitModel
import com.wordyka.kantinkorperasiitdel.model.Produk
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditAdminActivity : AppCompatActivity() {
    private lateinit var edtNama: EditText
    private lateinit var edtKategori: EditText
    private lateinit var edtJumlah: EditText
    private lateinit var edtHargaPcs: EditText
    private lateinit var edtStatus: EditText
    private lateinit var btnGambar: Button
    private lateinit var edtDeskripsi: EditText
    private lateinit var btnUpdate: MaterialButton
    private lateinit var imgProduk: ImageView
    private lateinit var imgUri: Uri
    private lateinit var path: String

    private val api by lazy { ApiConfig.instanceRetrofit }
    private val prodi by lazy { intent.getSerializableExtra("pro") as Produk }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_admin)

        btnGambar = findViewById(R.id.btnGambar) as Button

        btnGambar.setOnClickListener {
            selectImage()
        }

        setupView()
        setupListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            imgProduk.setImageURI(imgUri)

        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    private fun setupView() {
        edtNama = findViewById(R.id.edtNama)
        edtKategori = findViewById(R.id.edtKategori)
        edtJumlah = findViewById(R.id.edtJumlah)
        edtHargaPcs = findViewById(R.id.edtHargaPcs)
        edtStatus = findViewById(R.id.edtStatus)
        btnGambar = findViewById(R.id.btnGambar)
        edtDeskripsi = findViewById(R.id.edtDeskripsi)
        btnUpdate = findViewById(R.id.btnUpdate)
        imgProduk = findViewById(R.id.imgProduk)

        edtNama.setText(prodi.nama)
        edtKategori.setText(prodi.kategori)
        edtJumlah.setText((prodi.jumlah).toString())
        edtHargaPcs.setText(prodi.hargaPcs.toString())
        edtStatus.setText(prodi.status)

        val path = "http://10.0.2.2:8000/storage/product/"+prodi.gambar
        Glide.with(this).load(path).placeholder(R.drawable.product).into(imgProduk)

        edtDeskripsi.setText(prodi.deskripsi)
    }

    private fun setupListener() {

        btnUpdate.setOnClickListener {
            api.updateAdminProduk(
                prodi.id,
                edtNama.text.toString(),
                edtKategori.text.toString(),
                edtJumlah.text.toString().toInt(),
                edtStatus.text.toString(),
                prodi.gambar,
                edtDeskripsi.text.toString(),
                edtHargaPcs.text.toString().toBigInteger()

            )
                .enqueue(object : Callback<SubmitModel> {
                    override fun onResponse(
                        call: Call<SubmitModel>,
                        response: Response<SubmitModel>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(
                                applicationContext, "Produk berhasil diubah !", Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<SubmitModel>, t: Throwable) {}

                })
        }
    }
}