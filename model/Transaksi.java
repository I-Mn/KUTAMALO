package model;

import java.time.LocalDate;

public abstract class Transaksi {
    private int id;
    private double nominal;
    private String kategori;
    private LocalDate tanggal;
    private String deskripsi;

    // Konstruktor saat mengambil dari database (sudah ada ID)
    public Transaksi(int id, double nominal, String kategori, LocalDate tanggal, String deskripsi) {
        this.id = id;
        this.nominal = nominal;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
    }

    // Konstruktor saat membuat baru (belum ada ID)
    public Transaksi(double nominal, String kategori, LocalDate tanggal, String deskripsi) {
        this.nominal = nominal;
        this.kategori = kategori;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    // Abstract method to demonstrate polymorphism
    public abstract String formatTampilan();
}
