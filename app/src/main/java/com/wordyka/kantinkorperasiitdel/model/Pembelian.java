package com.wordyka.kantinkorperasiitdel.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

public class Pembelian implements Serializable {
    public int id;
    public int jumlah;
    public int harga;
    public String status;
    public String deskripsi;
    public int ID_User;
    public Timestamp created_at;
}
