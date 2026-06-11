<p align="center">
  <img src="view/images/app_icon.png" width="180" alt="Kutamalo Logo">
</p>

<h1 align="center">KUTAMALO</h1>

<h3 align="center"><i>"Catat Uang Saat Kurs Tak Masuk Logika"</i></h3>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/JavaFX-4796CC?style=for-the-badge&logo=java&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white" />
  <img src="https://img.shields.io/badge/UI/UX-Dark_Mode-121212?style=for-the-badge" />
</p>

<p align="center">
  <b>Kutamalo</b> adalah aplikasi pencatatan keuangan (<i>Expense Tracker</i>) modern berskala *desktop* yang dirancang khusus dengan antarmuka premium, transisi animasi halus, dan pengalaman pengguna setara aplikasi *mobile* kelas atas.
</p>

---

## ✨ Fitur Unggulan

- 🔒 **Sistem Autentikasi Cerdas**
  Login aman dengan MySQL, lengkap dengan fitur pengingat sesi (*Remember Me*) cerdas menggunakan `Preferences` untuk Auto-Login tanpa cela.
  
- 🎨 **Antarmuka Dark Mode Premium**
  Desain revolusioner JavaFX menggunakan paduan *Flexbox-like layout*, warna *dark grey*, aksen *gold/orange*, transisi *fade*, dan *rounded corners* di setiap sudut.

- 📊 **Dasbor & Analitik Visual**
  Pantau *cash flow* Anda menggunakan *Area Chart* yang interaktif (lengkap dengan *hover tooltips*) dan *Pie Chart* untuk melacak sumber penghasilan maupun kebocoran dompet Anda.

- 🌍 **Live API USD/IDR Exchange Rate**
  Secara dinamis menarik data nilai tukar mata uang terbaru dari Internet untuk memastikan keputusan finansial Anda selalu *up-to-date*.

- ⚙️ **Pengaturan Komprehensif (Settings)**
  Dari membersihkan *cache session* hingga mengekspor seluruh transaksi ke dalam *spreadsheet* berformat **CSV** dengan sekali klik, semuanya diamankan oleh sistem konfirmasi *Danger Modal Custom*.

- 🖼️ **Manajemen Profil & Avatar**
  Lengkapi identitas pengguna Anda. Ganti foto profil (*Avatar*), *password*, hingga informasi kontak dasar di satu *hub* yang estetik.

---

## 🛠️ Teknologi & Arsitektur

Aplikasi ini mengadopsi pola arsitektur **MVC** (*Model-View-Controller*) untuk memastikan kerapian kode, perawatan yang mudah, dan logika yang terisolasi.

* **Bahasa**: Java 17+ (Diuji pada JDK 26)
* **GUI Framework**: JavaFX (FXML & CSS)
* **Database**: MySQL (via JDBC Driver)
* **Lain-lain**: Java `Preferences` API (Manajemen Sesi), `HttpURLConnection` (Live API)

---

## 🚀 Panduan Instalasi & Eksekusi

### 1. Persiapan Database
1. Buka kontrol panel **XAMPP** dan hidupkan **MySQL**.
2. Masuk ke phpMyAdmin dan buat basis data baru bernama `kutamalo_db`.
3. *(Opsional)* Impor *file* struktur *database* SQL Anda ke dalam `kutamalo_db`. Tabel utama yang harus tersedia adalah `users` dan `transactions`.

### 2. Kompilasi Kode
Pastikan jalur `module-path` merujuk tepat pada lokasi instalasi JavaFX SDK di komputer Anda (misalnya: `D:\javafx-sdk-26.0.1\lib`). Jalankan perintah ini di dalam *folder* _root_ proyek:

```bat
javac -d bin --module-path "D:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics controller/*.java database/*.java model/*.java Main.java
```

### 3. Sinkronisasi Resource UI
Salin seluruh aset FXML, CSS, *font*, dan gambar ke direktori kompilasi:
```bat
Copy-Item view/* bin/view/ -Force -Recurse
```

### 4. Jalankan Kutamalo
Luncurkan aplikasi dan nikmati estetika dari Kutamalo:
```bat
java --module-path "D:\javafx-sdk-26.0.1\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics -cp bin Main
```

---

<p align="center">
  Didesain dengan ❤️ oleh <b>Madd1024</b>
</p>
