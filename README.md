# Sistem Manajemen Data Siswa Perpustakaan (JAVA GUI)

Aplikasi Desktop berbasis **Java Swing (GUI)** untuk mengelola database siswa perpustakaan sekolah secara digital. Sistem ini memanfaatkan **File I/O (.csv)** sebagai media penyimpanan data permanen dengan antarmuka yang user-friendly.

---

## Struktur Proyek dan Alur Kerja

Proyek ini memisahkan tanggung jawab antara antarmuka visual, logika pemrosesan file, dan representasi data:
- **Folder `src/`**:
    - `Siswa.java`: **Class Entity / Master Data**. Berisi blueprint data siswa (NIS, Nama, Alamat) dan enkapsulasi data melalui getter-setter.
    - `FileHandler.java`: **Data Access Object (DAO)**. Menangani logika pembacaan CSV menggunakan `BufferedReader` dan penulisan ulang data menggunakan `BufferedWriter`.
    - `MainFrame.java`: **Graphical User Interface (GUI)**. Mengatur tampilan jendela, tabel, serta logika event handling untuk tombol CRUD.
    - `Main.java`: **Entry Point**. Menjalankan aplikasi pada thread yang aman menggunakan `SwingUtilities`.
- **File `siswa.csv`**: Media penyimpanan data utama yang menggunakan pemisah koma (delimiter).
---

## Fitur Utama

1. **Antarmuka Desktop Modern**: Mengatur tata letak komponen secara sistematis menggunakan `BorderLayout` dan `GridLayout`.
2. **Manajemen Master Data (CRUD)**:
    - **Create**: Menambah data siswa baru dengan validasi kolom wajib isi.
    - **Update**: Mengubah data yang sudah ada dengan memilih baris pada tabel.
    - **Delete**: Menghapus data terpilih disertai dialog konfirmasi keamanan.
3. **Penyimpanan Otomatis**: Setiap perubahan data pada memori (`ArrayList`) akan langsung memicu `FileHandler.saveData()` untuk memperbarui file CSV secara permanen.
4. **Validasi NIS Ganda**: Menerapkan *Custom Exception Handling* untuk mencegah adanya dua siswa dengan NIS yang sama.
---

## Penjelasan Fungsi Tiap Class

### 1. `Siswa.java` (Model)
Kelas ini berfungsi sebagai **Data Model** atau entitas dasar dalam aplikasi.

* **Fungsi Utama**: Merepresentasikan objek Siswa dalam program.
* **Komponen**:
    * **Atribut**: Memiliki tiga atribut privat yaitu `nis`, `nama`, dan `alamat`.
    * **Constructor**: Digunakan untuk menginisialisasi objek siswa baru.
    * **Encapsulation**: Menyediakan method *Getter* dan *Setter* untuk mengakses dan mengubah data siswa secara aman.

---

### 2. `FileHandler.java` (Data Access/IO)
Kelas ini berfungsi sebagai pengelola komunikasi antara program dengan media penyimpanan eksternal (`siswa.csv`).

* **Atribut `FILE_NAME`**: Konstanta yang menentukan nama file penyimpanan.
* **Method `readData()`**:
    * Membaca file baris demi baris menggunakan `BufferedReader`.
    * Memecah data String berdasarkan tanda koma (CSV) menggunakan `split(",")`.
    * **Exception Handling**: Mengecek apakah file ada (`file.exists()`) dan menangkap `IOException` jika terjadi kegagalan akses file.
* **Method `saveData()`**:
    * Menulis seluruh daftar siswa kembali ke file menggunakan `BufferedWriter`.
    * Digunakan setiap kali ada perubahan data (Tambah, Update, atau Hapus) untuk sinkronisasi penyimpanan.

---

### 3. `MainFrame.java` (View & Controller)
Ini adalah kelas utama yang mengatur antarmuka pengguna (GUI) dan logika bisnis aplikasi.

* **Komponen Antarmuka**:
    * **Form Input**: Menggunakan `JTextField` untuk NIS, Nama, dan Alamat.
    * **Table**: Menggunakan `JTable` dan `DefaultTableModel` untuk menampilkan data secara visual.
    * **Layout**: Mengatur posisi komponen menggunakan `BorderLayout` dan `GridLayout`.
* **Logika CRUD (Create, Read, Update, Delete)**:
    * **Create**: Menambah siswa baru dengan validasi kolom kosong dan **Custom Exception** untuk mencegah duplikasi NIS menggunakan `IllegalArgumentException`.
    * **Update**: Mengubah data siswa yang dipilih pada tabel.
    * **Delete**: Menghapus data dengan dialog konfirmasi (`JOptionPane`).
* **Event Handling**: Menggunakan *Lambda Expression* untuk menangani klik tombol dan *selection listener* agar data tabel otomatis muncul di form saat diklik.

---

### 4. `Main.java` (Entry Point)
Kelas ini adalah titik awal ketika aplikasi dijalankan.

* **Fungsi Utama**: Menjalankan thread antarmuka pengguna.
* **`SwingUtilities.invokeLater`**: Memastikan pembuatan GUI dilakukan pada *Event Dispatch Thread* (EDT) agar aplikasi berjalan stabil dan responsif.
* **Konfigurasi**: Membuat instance dari `MainFrame`, mengaturnya agar muncul di tengah layar (`null`), dan menampilkannya.

---

## Alur Operasional

1. **Inisialisasi**: Saat aplikasi dibuka, `Main` memicu `MainFrame` yang langsung memanggil `FileHandler.readData()`.
2. **Sinkronisasi**: Setiap aksi **Create**, **Update**, atau **Delete** akan memperbarui `ArrayList` di memori dan langsung memicu `FileHandler.saveData()` agar file CSV selalu mutakhir.
3. **Validasi**: Sistem keamanan pada `MainFrame` memastikan tidak ada NIS ganda melalui mekanisme *Try-Catch*.

---
## Cara Menjalankan Program

1. *Compile* seluruh file `.java` di dalam folder `src/`.
2. Jalankan file `Main.java`.
3. Aplikasi akan muncul di tengah layar. Jika file `siswa.csv` belum tersedia, sistem secara otomatis akan menyiapkan file baru saat data pertama kali disimpan.
---

## Catatan

- **Pemisah Kolom (Delimiter)**: Menggunakan karakter koma (`,`). Disarankan tidak menginputkan karakter koma pada field Nama atau Alamat agar proses *parsing* data tidak terganggu.
- **Exception Handling**:
    - [cite_start]**Checked Exception**: Digunakan untuk menangani potensi kegagalan akses file pada operasi I/O[cite: 835, 895, 907].
    - [cite_start]**Unchecked Exception**: Digunakan pada logika bisnis untuk memvalidasi input pengguna[cite: 836, 983, 984].
- **Thread Safety**: Peluncuran GUI dibungkus dalam `SwingUtilities.invokeLater` untuk memastikan stabilitas performa aplikasi.
