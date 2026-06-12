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

-- Insert Sample Data for Users (IGNORE handles duplicates if run multiple times)
INSERT IGNORE INTO users (id, username, password, email, phone) VALUES 
(1, 'johndoe', 'password123', 'john@example.com', '081234567890'),
(2, 'janedoe', 'password456', 'jane@example.com', '089876543210'),
(3, 'admin', 'admin123', 'admin@kutamalo.com', '08111222333');

-- Clear existing sample transactions to avoid duplicates
DELETE FROM transaksi;
ALTER TABLE transaksi AUTO_INCREMENT = 1;

-- Insert Sample Data for Transaksi using exact categories from Java:
-- "Food & Drinks", "Shopping", "Transport", "Subscriptions", "Housing", "Salary", "Investment", "Gift", "Other"

INSERT INTO transaksi (user_id, jenis, nominal, kategori, tanggal, deskripsi) VALUES 
-- Data for admin (user_id = 3)
(3, 'Pemasukan', 10000000, 'Salary', '2026-01-01', 'Salary January'),
(3, 'Pengeluaran', 1500000, 'Shopping', '2026-01-05', 'Monthly groceries'),
(3, 'Pengeluaran', 500000, 'Housing', '2026-01-08', 'Electricity & Water'),
(3, 'Pengeluaran', 300000, 'Transport', '2026-01-12', 'Gasoline'),
(3, 'Pengeluaran', 200000, 'Food & Drinks', '2026-01-15', 'Lunch with client'),
(3, 'Pemasukan', 2500000, 'Gift', '2026-01-20', 'End of year bonus'),
(3, 'Pengeluaran', 1000000, 'Other', '2026-01-25', 'Entertainment'),
(3, 'Pengeluaran', 400000, 'Food & Drinks', '2026-01-28', 'Family dinner'),

(3, 'Pemasukan', 10000000, 'Salary', '2026-02-01', 'Salary February'),
(3, 'Pengeluaran', 1600000, 'Shopping', '2026-02-04', 'Groceries'),
(3, 'Pengeluaran', 550000, 'Housing', '2026-02-07', 'Internet and utilities'),
(3, 'Pengeluaran', 350000, 'Transport', '2026-02-10', 'Car service'),
(3, 'Pemasukan', 1500000, 'Investment', '2026-02-14', 'Stock dividend'),
(3, 'Pengeluaran', 750000, 'Other', '2026-02-18', 'Healthcare'),
(3, 'Pengeluaran', 450000, 'Food & Drinks', '2026-02-22', 'Weekly lunch'),
(3, 'Pengeluaran', 800000, 'Subscriptions', '2026-02-26', 'Netflix & Spotify'),

(3, 'Pemasukan', 10000000, 'Salary', '2026-03-01', 'Salary March'),
(3, 'Pengeluaran', 1400000, 'Shopping', '2026-03-05', 'Basic needs'),
(3, 'Pengeluaran', 500000, 'Housing', '2026-03-08', 'Utilities'),
(3, 'Pemasukan', 3000000, 'Gift', '2026-03-12', 'Q1 Bonus'),
(3, 'Pengeluaran', 400000, 'Transport', '2026-03-15', 'Transport to work'),
(3, 'Pengeluaran', 600000, 'Other', '2026-03-19', 'Books and courses'),
(3, 'Pengeluaran', 300000, 'Food & Drinks', '2026-03-24', 'Eating out'),
(3, 'Pengeluaran', 1200000, 'Other', '2026-03-29', 'Weekend trip'),

(3, 'Pemasukan', 10000000, 'Salary', '2026-04-01', 'Salary April'),
(3, 'Pemasukan', 5000000, 'Gift', '2026-04-05', 'Holiday Bonus'),
(3, 'Pengeluaran', 2000000, 'Shopping', '2026-04-08', 'New clothes'),
(3, 'Pengeluaran', 1500000, 'Other', '2026-04-12', 'Party'),
(3, 'Pengeluaran', 600000, 'Housing', '2026-04-15', 'Internet'),
(3, 'Pengeluaran', 1000000, 'Transport', '2026-04-20', 'Flight tickets'),
(3, 'Pengeluaran', 500000, 'Other', '2026-04-25', 'Routine checkup'),
(3, 'Pemasukan', 2000000, 'Investment', '2026-04-28', 'Freelance project'),

(3, 'Pemasukan', 10000000, 'Salary', '2026-05-01', 'Salary May'),
(3, 'Pengeluaran', 1500000, 'Shopping', '2026-05-04', 'Groceries'),
(3, 'Pengeluaran', 550000, 'Housing', '2026-05-08', 'Utilities'),
(3, 'Pengeluaran', 350000, 'Transport', '2026-05-12', 'Gasoline'),
(3, 'Pengeluaran', 450000, 'Food & Drinks', '2026-05-16', 'Lunch'),
(3, 'Pengeluaran', 900000, 'Other', '2026-05-20', 'Seminar fee'),
(3, 'Pengeluaran', 700000, 'Other', '2026-05-25', 'Cinema and dining'),
(3, 'Pemasukan', 1000000, 'Investment', '2026-05-29', 'Freelance project'),

(3, 'Pemasukan', 10000000, 'Salary', '2026-06-01', 'Salary June'),
(3, 'Pengeluaran', 1600000, 'Shopping', '2026-06-05', 'Home supplies'),
(3, 'Pengeluaran', 500000, 'Housing', '2026-06-08', 'Electricity'),
(3, 'Pemasukan', 1500000, 'Gift', '2026-06-10', 'Mid-year bonus'),
(3, 'Pengeluaran', 300000, 'Transport', '2026-06-12', 'Office transport'),
(3, 'Pengeluaran', 250000, 'Food & Drinks', '2026-06-15', 'Casual dining'),
(3, 'Pengeluaran', 1500000, 'Other', '2026-06-18', 'Staycation'),
(3, 'Pengeluaran', 400000, 'Other', '2026-06-20', 'Pharmacy');
