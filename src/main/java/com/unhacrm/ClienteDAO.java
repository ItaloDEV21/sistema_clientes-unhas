package com.unhacrm;

import java.sql.*;
import java.util.*;

public class ClienteDAO {

 public static void adicionar(Cliente c, double valor) {
  // SQL configurado para SQLite/Postgres com ON CONFLICT
  String sql = "INSERT INTO clientes(nome, telefone, bairro, data_nascimento, tipo_base, ultima_compra, valor_ultima_venda) " +
          "VALUES(?,?,?,?,?,?,?) " +
          "ON CONFLICT (telefone) DO UPDATE SET " +
          "nome = EXCLUDED.nome, bairro = EXCLUDED.bairro, data_nascimento = EXCLUDED.data_nascimento, " +
          "tipo_base = EXCLUDED.tipo_base, ultima_compra = EXCLUDED.ultima_compra, " +
          "valor_ultima_venda = EXCLUDED.valor_ultima_venda";

  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement(sql)) {
   ps.setString(1, c.nome);
   ps.setString(2, c.telefone);
   ps.setString(3, c.bairro);
   ps.setString(4, c.dataNascimento);
   ps.setString(5, c.tipoBase);
   ps.setString(6, c.ultimaCompra);
   ps.setDouble(7, valor);
   ps.executeUpdate();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 public static void excluir(int id) {
  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement("DELETE FROM clientes WHERE id=?")) {
   ps.setInt(1, id);
   ps.executeUpdate();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }

 public static List<Cliente> listar(String filtro) {
  List<Cliente> lista = new ArrayList<>();

  // Atualizado: Busca por nome OU bairro
  String sql = (filtro == null || filtro.isEmpty())
          ? "SELECT * FROM clientes ORDER BY id DESC"
          : "SELECT * FROM clientes WHERE nome LIKE ? OR bairro LIKE ? ORDER BY id DESC";

  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement(sql)) {

   if (filtro != null && !filtro.isEmpty()) {
    String busca = "%" + filtro + "%";
    ps.setString(1, busca); // Filtra o nome
    ps.setString(2, busca); // Filtra o bairro
   }

   ResultSet rs = ps.executeQuery();
   while (rs.next()) {
    Cliente c = new Cliente(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("telefone"),
            rs.getString("bairro"),
            rs.getString("data_nascimento"),
            rs.getString("tipo_base"),
            rs.getString("ultima_compra")
    );
    c.valorUltimaVenda = rs.getDouble("valor_ultima_venda");
    lista.add(c);
   }
  } catch (Exception e) {
   e.printStackTrace();
  }
  return lista;
 }
}