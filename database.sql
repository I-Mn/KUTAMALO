CREATE DATABASE IF NOT EXISTS kutamalo_db;
USE kutamalo_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS transaksi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    jenis VARCHAR(20) NOT NULL,
    nominal DOUBLE NOT NULL,
    kategori VARCHAR(100) NOT NULL,
    tanggal DATE NOT NULL,
    deskripsi TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
