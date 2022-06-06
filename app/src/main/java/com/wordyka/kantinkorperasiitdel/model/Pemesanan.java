package com.wordyka.kantinkorperasiitdel.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

public class Pemesanan implements Serializable {
    public int id;
    public String nama;
    public String kategori;
    public int jumlah;
    public String status;
    public String gambar;
    public String deskripsi;
    public BigInteger hargaPcs;
    public int ID_Product;
    public int ID_User;
    public int ID_Pembelian;
    public String role;
    public BigInteger harga;
    public Timestamp updated_at;
}
