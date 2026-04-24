package PemlanLK7.src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String FILE_NAME = "siswa.csv";

    // Membaca semua data dari file CSV
    public static List<Siswa> readData() {
        List<Siswa> listSiswa = new ArrayList<>();
        File file = new File(FILE_NAME);

        // Penanganan exception jika file tidak ada 
        if (!file.exists()) {
            System.out.println("File siswa.csv belum ada, akan dibuat saat ada data baru.");
            return listSiswa;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    listSiswa.add(new Siswa(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat membaca file: " + e.getMessage());
        }
        return listSiswa;
    }

    // Menyimpan keseluruhan data (List) kembali ke CSV (Digunakan untuk Create, Update, Delete)
    public static void saveData(List<Siswa> listSiswa) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Siswa s : listSiswa) {
                bw.write(s.getNis() + "," + s.getNama() + "," + s.getAlamat());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menulis file: " + e.getMessage());
        }
    }
}