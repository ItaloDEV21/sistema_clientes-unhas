package com.unhacrm;

import java.sql.*;

public class EstoqueDAO {

    public static int getEstoque() {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT quantidade FROM estoque WHERE id = 1");
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt("quantidade");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void atualizarEstoque(int qtd) {
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE estoque SET quantidade = ? WHERE id = 1")) {

            ps.setInt(1, qtd);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}