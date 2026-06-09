package model;

import java.time.LocalDate;

public class Pemasukan extends Transaksi {

    public Pemasukan(int id, double nominal, String kategori, LocalDate tanggal, String deskripsi) {
        super(id, nominal, kategori, tanggal, deskripsi);
    }

    public Pemasukan(double nominal, String kategori, LocalDate tanggal, String deskripsi) {
        super(nominal, kategori, tanggal, deskripsi);
    }

    @Override
    public String formatTampilan() {
        return "(+) Rp" + String.format("%,.2f", getNominal());
    }
}
