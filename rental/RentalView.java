package rental;

import javax.swing.*;
import java.awt.*;

public class RentalView extends JFrame {
    public JLabel lblJudul = new JLabel("CIHUUUY RENTAL MOBIL", JLabel.CENTER);
    public JButton btnTambah = new JButton("1. Tambah Mobil Baru");
    public JButton btnLihatSemua = new JButton("2. Lihat Semua Mobil");
    public JButton btnLihatTersedia = new JButton("3. Lihat Mobil Tersedia");
    public JButton btnCari = new JButton("4. Cari Mobil (Plat)");
    public JButton btnSewa = new JButton("5. Sewa Mobil");
    public JButton btnKembalikan = new JButton("6. Kembalikan Mobil");
    public JButton btnHapus = new JButton("7. Hapus Mobil");
    public JButton btnKeluar = new JButton("0. Keluar");

    public RentalView() {
        setTitle("SISTEM RENTAL MOBIL");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        lblJudul.setFont(new Font("Consolas", Font.BOLD, 18));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblJudul, BorderLayout.NORTH);

        JPanel panelMenu = new JPanel(new GridLayout(4, 2, 12, 12));
        panelMenu.setBackground(Color.BLACK);
        panelMenu.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton[] semuaTombol = {btnTambah, btnLihatSemua, btnLihatTersedia, btnCari, btnSewa, btnKembalikan, btnHapus, btnKeluar};
        for (JButton btn : semuaTombol) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            panelMenu.add(btn);
        }

        add(panelMenu, BorderLayout.CENTER);
    }
}