package model;

import java.time.LocalDate;

public class Pengeluaran extends Transaksi {

    public Pengeluaran(int id, double nominal, String kategori, LocalDate tanggal, String deskripsi) {
        super(id, nominal, kategori, tanggal, deskripsi);
    }

    public Pengeluaran(double nominal, String kategori, LocalDate tanggal, String deskripsi) {
        super(nominal, kategori, tanggal, deskripsi);
    }

    @Override
    public String formatTampilan() {
        return "(-) Rp" + String.format("%,.2f", getNominal());
    }
}
