package com.wordyka.kantinkorperasiitdel.model;

import java.io.Serializable;
import java.math.BigInteger;

import retrofit2.http.Field;


public class Produk implements Serializable {
    public int id;
    public String nama;
    public String kategori;
    public int jumlah;
    public String status;
    public String gambar;
    public String deskripsi;
    public BigInteger hargaPcs;
    public String role;
}
