package com.wordyka.kantinkorperasiitdel.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.app.SubmitModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateAdminActivity : AppCompatActivity() {
    private lateinit var edtNama: EditText
    private lateinit var edtKategori: EditText
    private lateinit var edtJumlah: EditText
    private lateinit var edtHargaPcs: EditText
    private lateinit var edtStatus: EditText
    private lateinit var btnGambar: Button
    private lateinit var edtDeskripsi: EditText
    private lateinit var btnCreate: MaterialButton
    private lateinit var imgProduk: ImageView
    private lateinit var imgUri: Uri
    private lateinit var path: String

    private val api by lazy { ApiConfig.instanceRetrofit }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_admin)

        btnGambar = findViewById(R.id.btnGambar) as Button

        btnGambar.setOnClickListener {
            selectImage()
        }

        setupView()
        setupListener()
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 100 && resultCode == RESULT_OK) {
            imgUri = data?.data!!
            imgProduk.setImageURI(imgUri)

        }
    }

    private fun setupView() {
        edtNama = findViewById(R.id.edtNama)
        edtKategori = findViewById(R.id.edtKategori)
        edtJumlah = findViewById(R.id.edtJumlah)
        edtHargaPcs = findViewById(R.id.edtHargaPcs)
        edtStatus = findViewById(R.id.edtStatus)
        imgProduk = findViewById(R.id.imgProduk)
        edtDeskripsi = findViewById(R.id.edtDeskripsi)
        btnCreate = findViewById(R.id.btnCreate)
    }

    private fun setupListener() {
        btnCreate.setOnClickListener {
            if (edtNama.text.toString().isEmpty()) {
                edtNama.error = "Nama produk harus diisi !"
            }
            if (edtKategori.text.toString().isEmpty()) {
                edtKategori.error = "Kategori produk harus diisi !"
            }
            if (edtJumlah.text.toString().isEmpty()) {
                edtJumlah.error = "jumlah produk harus diisi !"
            }
            if (edtHargaPcs.text.toString().isEmpty()) {
                edtHargaPcs.error = "Harga produk harus diisi !"
            }
            if (edtDeskripsi.text.toString().isEmpty()) {
                edtDeskripsi.error = "Deskripsi produk harus diisi !"
            }
            if (edtNama.text.toString().isNotEmpty() && edtKategori.text.toString()
                    .isNotEmpty() && edtJumlah.text.toString()
                    .isNotEmpty() && edtHargaPcs.text.toString()
                    .isNotEmpty() && edtDeskripsi.text.toString().isNotEmpty()
            ) {
                api.addAdminProduk(
                    edtNama.text.toString(),
                    edtKategori.text.toString(),
                    edtJumlah.text.toString().toInt(),
                    "Tersedia",
                    "Kdk0eURsVMJpfaScmP5fCJRZkhwj1GSfAYe4YxRo.png",
                    edtDeskripsi.text.toString(),
                    edtHargaPcs.text.toString().toBigInteger(),
                )
                    .enqueue(object : Callback<SubmitModel> {
                        override fun onResponse(
                            call: Call<SubmitModel>,
                            response: Response<SubmitModel>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Produk berhasil ditambahkan !",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                                Log.d("message",response.message())
                            }
                        }

                        override fun onFailure(call: Call<SubmitModel>, t: Throwable) {

                            Log.d("message","Error")
                        }

                    })
            }
        }
    }
}