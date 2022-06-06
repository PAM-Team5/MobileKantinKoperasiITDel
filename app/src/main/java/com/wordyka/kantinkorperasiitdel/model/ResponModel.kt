package com.wordyka.kantinkorperasiitdel.model

class ResponModel {
    var success = 0
    lateinit var message:String
    var user = User()
    var products:ArrayList<Produk> = ArrayList()
    var pemesanan:ArrayList<Pemesanan> = ArrayList()
    var pembelian:ArrayList<Pembelian> = ArrayList()
}