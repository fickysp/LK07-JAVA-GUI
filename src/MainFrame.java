package PemlanLK7.src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextField txtNis, txtNama, txtAlamat;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Siswa> dataSiswa;

    public MainFrame() {
        setTitle("Aplikasi Data Siswa Perpustakaan");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inisialisasi Data
        dataSiswa = FileHandler.readData();

        // 1. Bagian Judul 
        JLabel lblJudul = new JLabel("Data Siswa Perpustakaan SMP", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblJudul, BorderLayout.NORTH);

        // Panel Tengah untuk Form dan Tombol
        JPanel panelTengah = new JPanel(new BorderLayout());

        // 2. Bagian Entri Data 
        JPanel panelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("NIS:"));
        txtNis = new JTextField();
        panelForm.add(txtNis);

        panelForm.add(new JLabel("Nama Siswa:"));
        txtNama = new JTextField();
        panelForm.add(txtNama);

        panelForm.add(new JLabel("Alamat:"));
        txtAlamat = new JTextField();
        panelForm.add(txtAlamat);

        panelTengah.add(panelForm, BorderLayout.CENTER);

        // 3. Bagian Tombol CRUD 
        JPanel panelTombol = new JPanel();
        JButton btnCreate = new JButton("Create");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear Form");

        panelTombol.add(btnCreate);
        panelTombol.add(btnUpdate);
        panelTombol.add(btnDelete);
        panelTombol.add(btnClear);

        panelTengah.add(panelTombol, BorderLayout.SOUTH);
        add(panelTengah, BorderLayout.CENTER);

        // 4. Bagian Table 
        String[] kolom = {"NIS", "Nama Siswa", "Alamat"};
        tableModel = new DefaultTableModel(kolom, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // Tampilkan data ke tabel di awal program dijalankan [cite: 11]
        refreshTable();

        // --- EVENT LISTENERS (Logika CRUD) ---

        // Logika CREATE [cite: 5]
        btnCreate.addActionListener(e -> {
            String nis = txtNis.getText().trim();
            String nama = txtNama.getText().trim();
            String alamat = txtAlamat.getText().trim();

            if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua kolom harus diisi!");
                return;
            }

            // Exception handling jika NIS sama 
            try {
                for (Siswa s : dataSiswa) {
                    if (s.getNis().equals(nis)) {
                        throw new IllegalArgumentException("Error: NIS sudah terdaftar!");
                    }
                }
                // Jika lolos, tambahkan data
                dataSiswa.add(new Siswa(nis, nama, alamat));
                FileHandler.saveData(dataSiswa); // Simpan ke CSV 
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Duplikasi Data", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logika UPDATE [cite: 5]
        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel yang ingin diupdate!");
                return;
            }

            String nisBaru = txtNis.getText().trim();
            String namaBaru = txtNama.getText().trim();
            String alamatBaru = txtAlamat.getText().trim();

            Siswa siswaTerpilih = dataSiswa.get(row);

            // Cek duplikasi NIS jika NIS diubah ke NIS lain yang sudah ada 
            if (!siswaTerpilih.getNis().equals(nisBaru)) {
                for (Siswa s : dataSiswa) {
                    if (s.getNis().equals(nisBaru)) {
                        JOptionPane.showMessageDialog(this, "Error: NIS sudah digunakan siswa lain!", "Duplikasi Data", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            siswaTerpilih.setNis(nisBaru);
            siswaTerpilih.setNama(namaBaru);
            siswaTerpilih.setAlamat(alamatBaru);

            FileHandler.saveData(dataSiswa); // Update file CSV 
            refreshTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
        });

        // Logika DELETE [cite: 5]
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data di tabel yang ingin dihapus!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dataSiswa.remove(row);
                FileHandler.saveData(dataSiswa); // Update file CSV 
                refreshTable();
                clearForm();
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
            }
        });

        // Listener agar saat baris tabel diklik, masuk ke Form (memudahkan Update/Delete)
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtNis.setText(tableModel.getValueAt(row, 0).toString());
                txtNama.setText(tableModel.getValueAt(row, 1).toString());
                txtAlamat.setText(tableModel.getValueAt(row, 2).toString());
            }
        });

        btnClear.addActionListener(e -> clearForm());
    }

    private void refreshTable() {
        tableModel.setRowCount(0); // Kosongkan tabel
        for (Siswa s : dataSiswa) {
            tableModel.addRow(new Object[]{s.getNis(), s.getNama(), s.getAlamat()});
        }
    }

    private void clearForm() {
        txtNis.setText("");
        txtNama.setText("");
        txtAlamat.setText("");
        table.clearSelection();
    }
}