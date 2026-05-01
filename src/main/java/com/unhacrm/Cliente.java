package com.unhacrm;

public class Cliente {
 public int id;
 public String nome;
 public String telefone;
 public String bairro;          // Novo
 public String dataNascimento;  // Novo
 public String tipoBase;
 public String ultimaCompra;
 public double valorUltimaVenda;

 // Construtor completo (usado no listar do DAO)
 public Cliente(int id, String nome, String telefone, String bairro, String dataNascimento, String tipoBase, String ultimaCompra) {
  this.id = id;
  this.nome = nome;
  this.telefone = telefone;
  this.bairro = bairro;
  this.dataNascimento = dataNascimento;
  this.tipoBase = tipoBase;
  this.ultimaCompra = ultimaCompra;
 }

 // Construtor simples (usado no adicionar da MainWindow)
 public Cliente(String nome, String telefone, String bairro, String dataNascimento, String tipoBase, String ultimaCompra) {
  this.nome = nome;
  this.telefone = telefone;
  this.bairro = bairro;
  this.dataNascimento = dataNascimento;
  this.tipoBase = tipoBase;
  this.ultimaCompra = ultimaCompra;
 }
}