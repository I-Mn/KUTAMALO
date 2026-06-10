package model;

import java.util.ArrayList;
import java.util.List;
import database.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;

public class Akun {
    private double saldoUtama;
    private double totalPemasukan;
    private double totalPengeluaran;
    private List<Transaksi> riwayatTransaksi;

    public Akun(double saldoAwal) {
        this.saldoUtama = saldoAwal;
        this.totalPemasukan = 0;
        this.totalPengeluaran = 0;
        this.riwayatTransaksi = new ArrayList<>();
    }

    public double getSaldoUtama() {
        return saldoUtama;
    }

    public double getTotalPemasukan() {
        return totalPemasukan;
    }

    public double getTotalPengeluaran() {
        return totalPengeluaran;
    }

    public List<Transaksi> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    public void tambahTransaksi(Transaksi transaksi) {
        riwayatTransaksi.add(transaksi);
        
        if (transaksi instanceof Pemasukan) {
            this.saldoUtama += transaksi.getNominal();
            this.totalPemasukan += transaksi.getNominal();
        } else if (transaksi instanceof Pengeluaran) {
            this.saldoUtama -= transaksi.getNominal();
            this.totalPengeluaran += transaksi.getNominal();
        }
    }

    public void muatDataDariDatabase() {
        riwayatTransaksi.clear();
        saldoUtama = 0;
        totalPemasukan = 0;
        totalPengeluaran = 0;

        if (model.Session.getInstance().getCurrentUser() == null) return;
        int userId = model.Session.getInstance().getCurrentUser().getId();

        String query = "SELECT * FROM transaksi WHERE user_id = ? ORDER BY tanggal ASC, id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                int id = rs.getInt("id");
                String jenis = rs.getString("jenis");
                double nominal = rs.getDouble("nominal");
                String kategori = rs.getString("kategori");
                LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
                String deskripsi = rs.getString("deskripsi");

                Transaksi t;
                if ("Pemasukan".equalsIgnoreCase(jenis)) {
                    t = new Pemasukan(id, nominal, kategori, tanggal, deskripsi);
                } else {
                    t = new Pengeluaran(id, nominal, kategori, tanggal, deskripsi);
                }
                // Pakai memori internal tambahTransaksi agar saldo terhitung
                tambahTransaksi(t);
            }
            } // Close inner try(ResultSet)
        } catch (SQLException e) {
            System.err.println("Gagal memuat data: " + e.getMessage());
        }
    }

    public void tambahTransaksiDB(Transaksi transaksi, String jenis) {
        if (model.Session.getInstance().getCurrentUser() == null) return;
        int userId = model.Session.getInstance().getCurrentUser().getId();

        String query = "INSERT INTO transaksi (user_id, jenis, nominal, kategori, tanggal, deskripsi) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             
            pstmt.setInt(1, userId);
            pstmt.setString(2, jenis);
            pstmt.setDouble(3, transaksi.getNominal());
            pstmt.setString(4, transaksi.getKategori());
            pstmt.setDate(5, java.sql.Date.valueOf(transaksi.getTanggal()));
            pstmt.setString(6, transaksi.getDeskripsi());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    transaksi.setId(generatedKeys.getInt(1));
                }
            }
            // Tambahkan ke sistem lokal (update memory & saldo)
            tambahTransaksi(transaksi);
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan data ke database: " + e.getMessage());
        }
    }

    public void updateTransaksiDB(Transaksi transaksi, String jenis) {
        if (model.Session.getInstance().getCurrentUser() == null) return;
        int userId = model.Session.getInstance().getCurrentUser().getId();

        String query = "UPDATE transaksi SET jenis = ?, nominal = ?, kategori = ?, tanggal = ?, deskripsi = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
             
            pstmt.setString(1, jenis);
            pstmt.setDouble(2, transaksi.getNominal());
            pstmt.setString(3, transaksi.getKategori());
            pstmt.setDate(4, java.sql.Date.valueOf(transaksi.getTanggal()));
            pstmt.setString(5, transaksi.getDeskripsi());
            pstmt.setInt(6, transaksi.getId());
            pstmt.setInt(7, userId);
            
            pstmt.executeUpdate();
            
            // Muat ulang data dari database agar saldo tersinkronisasi
            muatDataDariDatabase();
        } catch (SQLException e) {
            System.err.println("Gagal mengupdate data di database: " + e.getMessage());
        }
    }

    public void hapusSemuaTransaksiDB() {
        if (model.Session.getInstance().getCurrentUser() == null) return;
        int userId = model.Session.getInstance().getCurrentUser().getId();

        String query = "DELETE FROM transaksi WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
            // Bersihkan dari memori juga
            riwayatTransaksi.clear();
            saldoUtama = 0;
            totalPemasukan = 0;
            totalPengeluaran = 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus data dari database: " + e.getMessage());
        }
    }
}
