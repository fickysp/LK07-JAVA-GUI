package PemlanLK7.src;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setLocationRelativeTo(null); // Agar window muncul di tengah layar
            frame.setVisible(true);
        });
    }
}