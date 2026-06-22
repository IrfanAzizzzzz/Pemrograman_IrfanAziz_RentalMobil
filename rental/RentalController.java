package rental;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RentalController {
    private RentalView view;

    public RentalController(RentalView view) {
        this.view = view;

        this.view.btnTambah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField txtNama = new JTextField();
                JTextField txtPlat = new JTextField();
                Object[] formIsian = {"Nama / Model Mobil:", txtNama, "Plat Nomor Mobil:", txtPlat};

                int pilihan = JOptionPane.showConfirmDialog(view, formIsian, "Tambah Mobil Baru", JOptionPane.OK_CANCEL_OPTION);
                if (pilihan == JOptionPane.OK_OPTION) {
                    String nama = txtNama.getText().trim();
                    String plat = txtPlat.getText().trim();

                    if (!nama.isEmpty() && !plat.isEmpty()) {
                        try (Connection conn = DatabaseConfig.getConnection();
                             PreparedStatement ps = conn.prepareStatement("INSERT INTO mobil (nama_mobil, plat_nomor, status) VALUES (?, ?, 'Tersedia')")) {
                            ps.setString(1, nama);
                            ps.setString(2, plat);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(view, "Mobil berhasil ditambahkan ke database!");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(view, "Semua kolom wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });

        this.view.btnLihatSemua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DatabaseConfig.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM mobil")) {

                    StringBuilder sb = new StringBuilder("--- DAFTAR SEMUA MOBIL ---\n");
                    int nomor = 1;
                    boolean adaData = false;
                    while (rs.next()) {
                        sb.append(nomor++).append(". ")
                          .append(rs.getString("nama_mobil")).append(" [")
                          .append(rs.getString("plat_nomor")).append("] - ")
                          .append(rs.getString("status")).append("\n");
                        adaData = true;
                    }
                    if (!adaData) {
                        JOptionPane.showMessageDialog(view, "Belum ada data mobil di database.");
                    } else {
                        JOptionPane.showMessageDialog(view, sb.toString());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                }
            }
        });

        this.view.btnLihatTersedia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conn = DatabaseConfig.getConnection();
                     PreparedStatement ps = conn.prepareStatement("SELECT * FROM mobil WHERE status = 'Tersedia'")) {
                    ResultSet rs = ps.executeQuery();
                    StringBuilder sb = new StringBuilder("--- DAFTAR MOBIL TERSEDIA ---\n");
                    int nomor = 1;
                    boolean adaData = false;
                    while (rs.next()) {
                        sb.append(nomor++).append(". ")
                          .append(rs.getString("nama_mobil")).append(" [")
                          .append(rs.getString("plat_nomor")).append("]\n");
                        adaData = true;
                    }
                    if (!adaData) {
                        JOptionPane.showMessageDialog(view, "Tidak ada mobil yang tersedia saat ini.");
                    } else {
                        JOptionPane.showMessageDialog(view, sb.toString());
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                }
            }
        });

        this.view.btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cariPlat = JOptionPane.showInputDialog(view, "Masukkan Plat Nomor Mobil yang Dicari:");
                if (cariPlat != null && !cariPlat.trim().isEmpty()) {
                    try (Connection conn = DatabaseConfig.getConnection();
                         PreparedStatement ps = conn.prepareStatement("SELECT * FROM mobil WHERE plat_nomor LIKE ?")) {
                        ps.setString(1, "%" + cariPlat.trim() + "%");
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            String info = "Nama: " + rs.getString("nama_mobil") + "\nPlat: " + rs.getString("plat_nomor") + "\nStatus: " + rs.getString("status");
                            JOptionPane.showMessageDialog(view, "--- MOBIL DITEMUKAN ---\n" + info);
                        } else {
                            JOptionPane.showMessageDialog(view, "Mobil tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        this.view.btnSewa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cariPlat = JOptionPane.showInputDialog(view, "Masukkan Plat Nomor Mobil yang Ingin Disewa:");
                if (cariPlat != null && !cariPlat.trim().isEmpty()) {
                    try (Connection conn = DatabaseConfig.getConnection();
                         PreparedStatement psCek = conn.prepareStatement("SELECT * FROM mobil WHERE plat_nomor = ?")) {
                        psCek.setString(1, cariPlat.trim());
                        ResultSet rs = psCek.executeQuery();
                        if (rs.next()) {
                            if (rs.getString("status").equals("Tersedia")) {
                                String namaPenyewa = JOptionPane.showInputDialog(view, "Masukkan Nama Penyewa:");
                                String durasiStr = JOptionPane.showInputDialog(view, "Masukkan Durasi Sewa (Hari):");
                                if (namaPenyewa != null && durasiStr != null && !namaPenyewa.trim().isEmpty() && !durasiStr.trim().isEmpty()) {
                                    try {
                                        int durasi = Integer.parseInt(durasiStr.trim());
                                        try (PreparedStatement psUpdate = conn.prepareStatement("UPDATE mobil SET status = ? WHERE plat_nomor = ?")) {
                                            psUpdate.setString(1, "Sedang Disewa oleh " + namaPenyewa.trim() + " (" + durasi + " Hari)");
                                            psUpdate.setString(2, cariPlat.trim());
                                            psUpdate.executeUpdate();
                                            JOptionPane.showMessageDialog(view, "Sewa berhasil dilakukan!");
                                        }
                                    } catch (NumberFormatException ex) {
                                        JOptionPane.showMessageDialog(view, "Durasi harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(view, "Mobil sedang disewa orang lain!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(view, "Plat nomor tidak terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        this.view.btnKembalikan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cariPlat = JOptionPane.showInputDialog(view, "Masukkan Plat Nomor Mobil yang Dikembalikan:");
                if (cariPlat != null && !cariPlat.trim().isEmpty()) {
                    try (Connection conn = DatabaseConfig.getConnection();
                         PreparedStatement psCek = conn.prepareStatement("SELECT * FROM mobil WHERE plat_nomor = ?")) {
                        psCek.setString(1, cariPlat.trim());
                        ResultSet rs = psCek.executeQuery();
                        if (rs.next()) {
                            if (rs.getString("status").contains("Sedang Disewa")) {
                                try (PreparedStatement psUpdate = conn.prepareStatement("UPDATE mobil SET status = 'Tersedia' WHERE plat_nomor = ?")) {
                                    psUpdate.setString(1, cariPlat.trim());
                                    psUpdate.executeUpdate();
                                    JOptionPane.showMessageDialog(view, "Mobil berhasil dikembalikan!");
                                }
                            } else {
                                JOptionPane.showMessageDialog(view, "Mobil ini memang tidak sedang disewa.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(view, "Plat nomor tidak terdaftar.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        this.view.btnHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cariPlat = JOptionPane.showInputDialog(view, "Masukkan Plat Nomor Mobil yang Ingin Dihapus:");
                if (cariPlat != null && !cariPlat.trim().isEmpty()) {
                    try (Connection conn = DatabaseConfig.getConnection();
                         PreparedStatement ps = conn.prepareStatement("DELETE FROM mobil WHERE plat_nomor = ?")) {
                        ps.setString(1, cariPlat.trim());
                        int rows = ps.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(view, "Mobil berhasil dihapus dari database!");
                        } else {
                            JOptionPane.showMessageDialog(view, "Plat nomor tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                    }
                }
            }
        });

        this.view.btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}