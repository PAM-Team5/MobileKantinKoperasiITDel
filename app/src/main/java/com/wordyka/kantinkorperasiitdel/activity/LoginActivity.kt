package com.wordyka.kantinkorperasiitdel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.wordyka.kantinkorperasiitdel.MainActivity
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.kantin.MainAdminKantinActivity
import com.wordyka.kantinkorperasiitdel.activity.koperasi.MainAdminActivity
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref
import com.wordyka.kantinkorperasiitdel.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var sp:SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sp = SharePref(this)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            login()
        }

        val btnDaftarUser = findViewById<TextView>(R.id.btn_daftarUser)
        btnDaftarUser.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
    }

    fun login() {
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPassword = findViewById<EditText>(R.id.edt_password)

        if(edtEmail.text.isEmpty()) {
            edtEmail.error = "Kolom email tidak boleh kosong"
            edtEmail.requestFocus()
            return
        }else if(edtPassword.text.isEmpty()) {
            edtPassword.error = "Kolom password tidak boleh kosong"
            edtPassword.requestFocus()
            return
        }

        val pb = findViewById<ProgressBar>(R.id.pb)
        pb.visibility = View.VISIBLE

        ApiConfig.instanceRetrofit.login(edtEmail.text.toString(),edtPassword.text.toString()).enqueue(object :
            Callback<ResponModel> {
            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                pb.visibility = View.GONE
                if(respon.success == 1) {
                    sp.setStatusLogin(true)
                    sp.setUser(respon.user)

                    if(respon.user.role.equals("admin-koperasi", ignoreCase = true)) {
                        val intent =  Intent(this@LoginActivity, MainAdminActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    if(respon.user.role.equals("admin-kantin", ignoreCase = true)) {
                        val intent =  Intent(this@LoginActivity, MainAdminKantinActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    if(respon.user.role.isNullOrBlank()) {
                        val intent =  Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    Toast.makeText(this@LoginActivity, "Selamat datang "+respon.user.name, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Error: "+respon.message, Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
                pb.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Error: "+t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }
}