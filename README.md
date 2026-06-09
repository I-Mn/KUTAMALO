# KUTAMALO

**"Catat Uang Saat Kurs Tak Masuk Logika"**
Aplikasi pencatatan keuangan (*Expense Tracker*) modern berbasis JavaFX yang dilengkapi dengan fitur pantauan nilai tukar USD ke IDR secara _real-time_.

## Fitur Utama
1. **Multi-User Authentication**: Sistem registrasi dan login yang aman menggunakan *database* MySQL. Setiap pengguna memiliki data terisolasi.
2. **Dashboard Interaktif**: Dasbor elegan dengan laporan ringkasan Saldo, Pemasukan, dan Pengeluaran, beserta kartu laporan interaktif.
3. **Live Exchange Rate**: Memuat nilai tukar USD/IDR langsung dari API secara _real-time_ untuk membantu dalam pencatatan transaksi yang peka terhadap kurs.
4. **Analisis Grafik (Data Analytics)**: Menampilkan visualisasi data transaksi dalam bentuk *Bar Chart* (per bulan) dan *Pie Chart* (distribusi kategori pengeluaran).
5. **Manajemen Profil**: Pengguna dapat memperbarui data pribadi seperti *Email*, *Telepon*, mengganti kata sandi secara aman, serta mengunggah foto profil (*Avatar*) secara langsung.
6. **Dark UI Modern**: Antarmuka kelas premium yang menggunakan paduan palet warna gelap (*dark mode*) dan nuansa warna *orange/gold* lengkap dengan animasi dan lengkungan (*rounded corners*) yang estetik.

## Prasyarat (Requirements)
Pastikan sistem Anda sudah terinstal:
- **Java Development Kit (JDK)**: Versi 26.0.1 (atau JDK 17+ ke atas).
- **JavaFX SDK**: Versi 26.0.1 (disesuaikan dengan JDK).
- **XAMPP / MySQL Server**: Untuk *database*.
- **Koneksi Internet**: Untuk fitur *Live USD/IDR Exchange Rate*.

## Panduan Instalasi & Eksekusi

### 1. Setup Database
1. Buka XAMPP dan nyalakan layanan **MySQL**.
2. Masuk ke phpMyAdmin atau jalankan MySQL CLI.
3. Buat *database* dengan nama: `kutamalo_db`
4. Anda dapat menggunakan struktur tabel standar yang berisi `users` dan (opsional) `transactions` jika ada migrasi tabel baru. Secara otomatis `users` berisi: `id`, `username`, `password`, `email`, `phone`, `avatar`.

### 2. Konfigurasi JavaFX Path
Jika Anda mengompilasi secara manual, pastikan *path* JavaFX SDK Anda sudah benar (contoh di PC: `D:\javafx-sdk-26.0.1\lib`).

### 3. Kompilasi Kode (Manual via Terminal)
Jalankan perintah ini di dalam *folder* utama `KUTAMALO`:
```bat
javac -d bin --module-path "D:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics controller/*.java database/*.java model/*.java Main.java
```

### 4. Menjalankan Aplikasi
Setelah di-kompilasi, jalankan melalui perintah `java` dengan format berikut, pastikan juga memuat modul JavaFX:
```bat
java --module-path "D:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp bin Main
```

*(Catatan: Anda juga dapat menggunakan tombol "Run" atau "F5" langsung dari IDE yang sudah dikonfigurasi JavaFX).*

## Informasi Rilis
- Didesain oleh tim **Madd1024**
- Aplikasi ini dibangun menggunakan antarmuka JavaFX XML (`.fxml`) dan diberi gaya menggunakan Custom CSS.
