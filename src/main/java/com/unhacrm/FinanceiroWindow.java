package com.unhacrm;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class FinanceiroWindow extends JFrame {
    JComboBox<String> comboMes = new JComboBox<>(new String[]{
            "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
    });
    JTextField txtAno = new JTextField(String.valueOf(LocalDate.now().getYear()));
    JTextArea txtResultado = new JTextArea();

    public FinanceiroWindow() {
        setTitle("Relatório Financeiro Completo");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel pnlFiltro = new JPanel();
        pnlFiltro.add(new JLabel("Mês:"));
        pnlFiltro.add(comboMes);
        pnlFiltro.add(new JLabel("Ano:"));
        pnlFiltro.add(txtAno);
        JButton btnBuscar = new JButton("Consultar");
        pnlFiltro.add(btnBuscar);

        txtResultado.setFont(new Font("Monospaced", Font.BOLD, 14));
        txtResultado.setEditable(false);
        txtResultado.setBackground(new Color(245, 245, 245));

        btnBuscar.addActionListener(e -> consultarMes());

        add(pnlFiltro, BorderLayout.NORTH);
        add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        // Carrega o mês atual ao abrir
        comboMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        consultarMes();
        setVisible(true);
    }

    private void consultarMes() {
        String mes = comboMes.getSelectedItem().toString();
        String ano = txtAno.getText();
        String dataFiltro = ano + "-" + mes; // Formato YYYY-MM para o SQLite

        String sql = "SELECT SUM(quantidade), SUM(valor) FROM vendas WHERE data LIKE '" + dataFiltro + "%'";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int qtd = rs.getInt(1);
                double total = rs.getDouble(2);
                txtResultado.setText(String.format(
                        "--- RESULTADO DE %s/%s ---\n\n" +
                                "Bases Vendidas: %d\n" +
                                "Faturamento: R$ %,.2f",
                        mes, ano, qtd, total
                ));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }
}