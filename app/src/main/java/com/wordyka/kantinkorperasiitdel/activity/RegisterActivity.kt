package com.wordyka.kantinkorperasiitdel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.wordyka.kantinkorperasiitdel.MainActivity
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var sp: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        sp = SharePref(this)

        val btnRegister = findViewById<Button>(R.id.btn_register)
        btnRegister.setOnClickListener {
            register()
        }


    }

    fun register() {
        val edtNama = findViewById<EditText>(R.id.edt_nama)
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPhone = findViewById<EditText>(R.id.edt_phone)
        val edtPassword = findViewById<EditText>(R.id.edt_password)

        if(edtEmail.text.isEmpty()) {
            edtEmail.error = "Kolom email tidak boleh kosong"
            edtEmail.requestFocus()
            return
        } else if(edtNama.text.isEmpty()) {
            edtNama.error = "Kolom nama tidak boleh kosong"
            edtNama.requestFocus()
            return
        } else if(edtPhone.text.isEmpty()) {
            edtPhone.error = "Kolom nomor telepon tidak boleh kosong"
            edtPhone.requestFocus()
            return
        }else if(edtPassword.text.isEmpty()) {
            edtPassword.error = "Kolom password tidak boleh kosong"
            edtPassword.requestFocus()
            return
        }

        val pb = findViewById<ProgressBar>(R.id.pb)
        pb.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.register(edtNama.text.toString(),edtEmail.text.toString(),edtPhone.text.toString(),edtPassword.text.toString()).enqueue(object :
            Callback<ResponModel> {

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                pb.visibility = View.GONE
                if(respon.success == 1) {
                    sp.setStatusLogin(true)
                    val intent =  Intent(this@RegisterActivity, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@RegisterActivity, "Selamat datang "+respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RegisterActivity, "Error: "+respon.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@RegisterActivity, "Error: "+t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }
}