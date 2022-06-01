package com.wordyka.kantinkorperasiitdel.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.helper.SharePref

class MasukActivity : AppCompatActivity() {

    lateinit var sp: SharePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        sp = SharePref(this)


        mainButton()
    }

    fun mainButton() {
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val btnDaftar = findViewById<Button>(R.id.btnDaftar)
        btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}