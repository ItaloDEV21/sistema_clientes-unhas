package com.unhacrm;

public class Cliente {
 public int id;
 public String nome;
 public String telefone;
 public String tipoBase;
 public String ultimaCompra;

 public Cliente(int id, String nome, String telefone, String tipoBase, String ultimaCompra) {
  this.id = id;
  this.nome = nome;
  this.telefone = telefone;
  this.tipoBase = tipoBase;
  this.ultimaCompra = ultimaCompra;
 }

 // Construtor auxiliar para novos clientes (sem ID)
 public Cliente(String nome, String telefone, String tipoBase, String ultimaCompra) {
  this(0, nome, telefone, tipoBase, ultimaCompra);
 }
}