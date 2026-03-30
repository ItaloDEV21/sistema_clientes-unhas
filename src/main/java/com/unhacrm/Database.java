package com.unhacrm;
import java.sql.*;

public class Database {
 // COLE AQUI OS DADOS QUE VOCÊ PEGOU NO RENDER
 private static final String HOST = "dpg-d75bar6a2pns73dlja4g-a";
 private static final String DB_NAME = "unha_db";
 private static final String USER = "karol_admin";
 private static final String PASS = "wHIBY6gKANjwuX3E7aAo7Vv9cAKE5dVB";
 private static final String PORT = "5432";

 // URL agora aponta para a internet
 private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB_NAME + "?sslmode=require";

 public static Connection connect() throws SQLException {
  return DriverManager.getConnection(URL, USER, PASS);
 }

 public static void init() {
  try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
   // No PostgreSQL o comando de ID é 'SERIAL', não AUTOINCREMENT
   stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
           "id SERIAL PRIMARY KEY, " +
           "nome TEXT, " +
           "telefone TEXT UNIQUE, " +
           "tipo_base TEXT, " +
           "ultima_compra TEXT)");

   stmt.execute("CREATE TABLE IF NOT EXISTS estoque (id INTEGER PRIMARY KEY, quantidade INTEGER)");

   // Inicializa estoque se estiver vazio
   stmt.execute("INSERT INTO estoque(id, quantidade) " +
           "SELECT 1, 0 WHERE NOT EXISTS (SELECT 1 FROM estoque WHERE id = 1)");

   System.out.println("Ligado ao banco de dados na nuvem com sucesso!");
  } catch (Exception e) {
   System.err.println("Erro ao ligar ao banco na nuvem: " + e.getMessage());
  }
 }
}