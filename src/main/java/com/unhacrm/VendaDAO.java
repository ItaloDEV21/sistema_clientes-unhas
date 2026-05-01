package com.unhacrm;

import java.sql.*;

public class VendaDAO {

    // 🔥 REGISTRAR VENDA (SQLite usa date('now') para a data atual)
    public static void registrarVenda(int quantidade, double valor) {
        String sql = "INSERT INTO vendas (quantidade, valor, data) VALUES (?, ?, date('now'))";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quantidade);
            ps.setDouble(2, valor);
            ps.executeUpdate();

            reduzirEstoque(quantidade);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔻 ESTOQUE
    private static void reduzirEstoque(int qtd) {
        String sql = "UPDATE estoque SET quantidade = quantidade - ? WHERE id = 1";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qtd);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- MÉTODOS DE LUCRO (VALOR) ---

    public static double totalHoje() {
        String sql = "SELECT SUM(valor) FROM vendas WHERE data = date('now')";
        return executarSoma(sql);
    }

    public static double totalSemana() {
        // Pega os últimos 7 dias
        String sql = "SELECT SUM(valor) FROM vendas WHERE data >= date('now', '-7 days')";
        return executarSoma(sql);
    }

    public static double totalMes() {
        // Pega o mês atual (do primeiro dia até agora)
        String sql = "SELECT SUM(valor) FROM vendas WHERE data >= date('now', 'start of month')";
        return executarSoma(sql);
    }

    // --- MÉTODOS DE QUANTIDADE (BASES SAÍDAS) ---

    public static int qtdBasesSemana() {
        String sql = "SELECT SUM(quantidade) FROM vendas WHERE data >= date('now', '-7 days')";
        return (int) executarSoma(sql);
    }

    public static int qtdBasesMes() {
        String sql = "SELECT SUM(quantidade) FROM vendas WHERE data >= date('now', 'start of month')";
        return (int) executarSoma(sql);
    }

    // 💡 TOTAL GERAL
    public static double totalGeral() {
        String sql = "SELECT SUM(valor) FROM vendas";
        return executarSoma(sql);
    }

    // 🔥 MÉTODO PADRÃO (EVITA REPETIÇÃO)
    // No SQLite, COALESCE é bom, mas o retorno de SUM em branco é null,
    // o rs.getDouble já trata isso retornando 0.0.
    private static double executarSoma(String sql) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}