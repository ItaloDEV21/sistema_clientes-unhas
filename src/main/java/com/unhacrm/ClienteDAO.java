package com.unhacrm;
import java.sql.*;
import java.util.*;

public class ClienteDAO {

 public static void adicionar(Cliente c) {
  // Comando 'UPSERT' (Update ou Insert) do PostgreSQL
  String sql = "INSERT INTO clientes(nome, telefone, tipo_base, ultima_compra) VALUES(?,?,?,?) " +
          "ON CONFLICT (telefone) DO UPDATE SET " +
          "nome = EXCLUDED.nome, tipo_base = EXCLUDED.tipo_base, ultima_compra = EXCLUDED.ultima_compra";

  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement(sql)) {
   ps.setString(1, c.nome);
   ps.setString(2, c.telefone);
   ps.setString(3, c.tipoBase);
   ps.setString(4, c.ultimaCompra);
   ps.executeUpdate();
  } catch (Exception e) { e.printStackTrace(); }
 }

 public static void excluir(int id) {
  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement("DELETE FROM clientes WHERE id=?")) {
   ps.setInt(1, id);
   ps.executeUpdate();
  } catch (Exception e) { e.printStackTrace(); }
 }

 public static List<Cliente> listar(String filtroNome) {
  List<Cliente> lista = new ArrayList<>();
  // ORDER BY id DESC: A última venda sempre aparece no topo da tabela!
  String sql = (filtroNome == null || filtroNome.isEmpty())
          ? "SELECT * FROM clientes ORDER BY id DESC"
          : "SELECT * FROM clientes WHERE nome LIKE ? ORDER BY id DESC";

  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement(sql)) {
   if (filtroNome != null && !filtroNome.isEmpty()) ps.setString(1, "%" + filtroNome + "%");
   ResultSet rs = ps.executeQuery();
   while (rs.next()) {
    lista.add(new Cliente(rs.getInt("id"), rs.getString("nome"),
            rs.getString("telefone"), rs.getString("tipo_base"), rs.getString("ultima_compra")));
   }
  } catch (Exception e) { e.printStackTrace(); }
  return lista;
 }
}