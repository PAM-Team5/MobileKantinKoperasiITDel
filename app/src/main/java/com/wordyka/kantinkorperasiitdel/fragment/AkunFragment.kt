package com.wordyka.kantinkorperasiitdel.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wordyka.kantinkorperasiitdel.MainActivity
import com.wordyka.kantinkorperasiitdel.R
import com.wordyka.kantinkorperasiitdel.activity.LoginActivity
import com.wordyka.kantinkorperasiitdel.activity.RiwayatPemesananActivity
import com.wordyka.kantinkorperasiitdel.app.ApiConfig
import com.wordyka.kantinkorperasiitdel.helper.SharePref

/**
 * A simple [Fragment] subclass.
 * Use the [AkunFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class AkunFragment : Fragment() {
    lateinit var sp:SharePref
    lateinit var btnLogout:TextView
    lateinit var tvNama:TextView
    lateinit var tvEmail:TextView
    lateinit var tvPhone:TextView
    lateinit var layoutRiwayat: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)

        init(view)

        sp = SharePref(requireActivity())

        btnLogout.setOnClickListener {
            sp.setStatusLogin(false)
            val intent =  Intent(this.activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        setData()
        getRiwayat(view)

        return view
    }

    fun getRiwayat(view: View) {
        layoutRiwayat.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    RiwayatPemesananActivity::class.java
                )
            )
        }
    }

    fun setData() {
        if(sp.getUser() == null) {
            val intent =  Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            return
        }

        val user = sp.getUser()!!

        tvNama.text = user.name
        tvEmail.text = user.email
        tvPhone.text = user.phone
    }

    private fun init(view: View) {
        btnLogout = view.findViewById(R.id.btn_logout)
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvPhone = view.findViewById(R.id.tv_phone)
        layoutRiwayat = view.findViewById(R.id.layoutRiwayat)

    }

}