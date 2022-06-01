package com.wordyka.kantinkorperasiitdel.model;

import java.io.Serializable;
import java.math.BigInteger;

public class Pembelian implements Serializable {
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

}
