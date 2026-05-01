package com.unhacrm;

import java.sql.*;

public class Database {
 // O banco será um arquivo chamado "dados_crm.db" na pasta do seu projeto
 private static final String URL = "jdbc:sqlite:dados_crm.db";

 public static Connection connect() throws SQLException {
  try {
   // Carrega o driver do SQLite
   Class.forName("org.sqlite.JDBC");
  } catch (ClassNotFoundException e) {
   System.err.println("Driver SQLite não encontrado!");
  }
  return DriverManager.getConnection(URL);
 }

 public static void init() {
  try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

   // 1. Criação da tabela de clientes local (com Bairro e Data de Nascimento)
   stmt.execute("""
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT,
                    telefone TEXT UNIQUE,
                    bairro TEXT,
                    data_nascimento TEXT,
                    tipo_base TEXT,
                    ultima_compra TEXT,
                    valor_ultima_venda REAL DEFAULT 0.0
                )
            """);

   // 2. Tabela de estoque
   stmt.execute("""
                CREATE TABLE IF NOT EXISTS estoque (
                    id INTEGER PRIMARY KEY,
                    quantidade INTEGER
                )
            """);

   // 3. Tabela de vendas (Financeiro)
   stmt.execute("""
                CREATE TABLE IF NOT EXISTS vendas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    quantidade INTEGER NOT NULL,
                    valor REAL NOT NULL,
                    data TEXT DEFAULT (date('now')),
                    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

   // 4. Inicializa o registro de estoque se estiver vazio
   stmt.execute("""
                INSERT OR IGNORE INTO estoque(id, quantidade) VALUES(1, 0)
            """);

   System.out.println("Banco de Dados LOCAL (SQLite) sincronizado com sucesso!");

  } catch (Exception e) {
   System.err.println("Erro ao sincronizar banco local: " + e.getMessage());
   e.printStackTrace();
  }
 }
}