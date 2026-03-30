package com.unhacrm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainWindow extends JFrame {
 JTextField txtNome = new JTextField(), txtTelefone = new JTextField(),
         txtCompra = new JTextField(),
         txtQtdVendida = new JTextField("1"), txtBusca = new JTextField();

 // MUDANÇA AQUI: Começa com "" (vazio) e tem apenas a "Base Fortalecedora"
 String[] opcoesBases = {"", "Base Fortalecedora"};
 JComboBox<String> comboBase = new JComboBox<>(opcoesBases);

 JTable tabela;
 DefaultTableModel modelo;
 JLabel lblEstoqueStatus = new JLabel("Estoque: 0");
 JComboBox<String> comboMensagens;

 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

 public MainWindow() {
  setTitle("Unhas CRM 3.5 - Gestão Total");
  setSize(1100, 750);
  setDefaultCloseOperation(EXIT_ON_CLOSE);
  setLocationRelativeTo(null);
  setLayout(new BorderLayout());

  txtCompra.setText(LocalDate.now().format(dtf));

  txtCompra.addFocusListener(new FocusAdapter() {
   @Override
   public void focusGained(FocusEvent e) {
    txtCompra.selectAll();
   }
  });

  // --- PAINEL SUPERIOR ---
  JPanel pnlSuperior = new JPanel(new GridLayout(1, 2));

  JPanel pnlForm = new JPanel(new GridLayout(6, 2, 5, 5));
  pnlForm.setBorder(BorderFactory.createTitledBorder("Dados do Cliente / Registro de Venda"));
  pnlForm.add(new JLabel("Nome:")); pnlForm.add(txtNome);
  pnlForm.add(new JLabel("Telefone:")); pnlForm.add(txtTelefone);
  pnlForm.add(new JLabel("Tipo de Base:")); pnlForm.add(comboBase);
  pnlForm.add(new JLabel("Data Compra:")); pnlForm.add(txtCompra);
  pnlForm.add(new JLabel("Qtd Vendida:")); pnlForm.add(txtQtdVendida);
  JButton btnSalvar = new JButton("Confirmar Venda e Salvar");
  pnlForm.add(btnSalvar);

  JPanel pnlEstoque = new JPanel(new FlowLayout());
  pnlEstoque.setBorder(BorderFactory.createTitledBorder("Controle de Estoque"));
  JTextField txtNovaQtd = new JTextField(5);
  JButton btnRepor = new JButton("Definir Saldo Atual");
  pnlEstoque.add(new JLabel("Qtd:")); pnlEstoque.add(txtNovaQtd);
  pnlEstoque.add(btnRepor); pnlEstoque.add(lblEstoqueStatus);

  pnlSuperior.add(pnlForm);
  pnlSuperior.add(pnlEstoque);

  // --- TABELA ---
  modelo = new DefaultTableModel(new Object[]{"Selecionar", "ID", "Nome", "Telefone", "Base", "Data"}, 0) {
   @Override
   public Class<?> getColumnClass(int columnIndex) {
    return columnIndex == 0 ? Boolean.class : super.getColumnClass(columnIndex);
   }
   @Override
   public boolean isCellEditable(int row, int column) {
    return column == 0;
   }
  };
  tabela = new JTable(modelo);

  // --- CLIQUE DUPLO ---
  tabela.addMouseListener(new MouseAdapter() {
   @Override
   public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
     int linha = tabela.getSelectedRow();
     if (linha != -1) {
      txtNome.setText(modelo.getValueAt(linha, 2).toString());
      txtTelefone.setText(modelo.getValueAt(linha, 3).toString());

      // Sincroniza o seletor com o valor da tabela
      String baseTabela = modelo.getValueAt(linha, 4).toString();
      comboBase.setSelectedItem(baseTabela);

      txtCompra.setText(LocalDate.now().format(dtf));
      txtNome.setBackground(new Color(230, 240, 255));
     }
    }
   }
  });

  // --- RESTANTE DO LAYOUT ---
  JPanel pnlCentro = new JPanel(new BorderLayout());
  JPanel pnlBusca = new JPanel(new BorderLayout());
  JButton btnMarcarTodos = new JButton("Selecionar Todos");
  JButton btnDesmarcarTodos = new JButton("Desmarcar Todos");
  JPanel pnlBotoesSelecao = new JPanel();
  pnlBotoesSelecao.add(btnMarcarTodos); pnlBotoesSelecao.add(btnDesmarcarTodos);
  pnlBusca.add(new JLabel(" Pesquisar Nome: "), BorderLayout.WEST);
  pnlBusca.add(txtBusca, BorderLayout.CENTER);
  pnlBusca.add(pnlBotoesSelecao, BorderLayout.EAST);
  pnlCentro.add(pnlBusca, BorderLayout.NORTH);
  pnlCentro.add(new JScrollPane(tabela), BorderLayout.CENTER);

  JPanel pnlAcoes = new JPanel(new FlowLayout());
  String[] frases = {"Lembrete...", "Olá!...", "Promoção...", "Olá!..."};
  comboMensagens = new JComboBox<>(frases);
  JButton btnEnviarWpp = new JButton("Enviar WhatsApp");
  JButton btnExcel = new JButton("Exportar CSV");
  JButton btnExcluir = new JButton("Excluir Selecionado");
  btnExcluir.setBackground(new Color(255, 180, 180));
  pnlAcoes.add(new JLabel("Mensagem:"));
  pnlAcoes.add(comboMensagens);
  pnlAcoes.add(btnEnviarWpp);
  pnlAcoes.add(btnExcel);
  pnlAcoes.add(btnExcluir);

  add(pnlSuperior, BorderLayout.NORTH);
  add(pnlCentro, BorderLayout.CENTER);
  add(pnlAcoes, BorderLayout.SOUTH);

  // --- LÓGICA DO BOTÃO SALVAR ---
  btnSalvar.addActionListener(e -> {
   try {
    String baseSelecionada = comboBase.getSelectedItem().toString();

    // Opcional: Validar se ela escolheu a base antes de salvar
    if(baseSelecionada.isEmpty()) {
     JOptionPane.showMessageDialog(this, "Por favor, selecione o tipo de base!");
     return;
    }

    Cliente c = new Cliente(txtNome.getText(), txtTelefone.getText(), baseSelecionada, txtCompra.getText());
    ClienteDAO.adicionar(c);
    baixarEstoque(Integer.parseInt(txtQtdVendida.getText()));
    atualizarLista();
    limparCampos();
    JOptionPane.showMessageDialog(this, "Venda registrada!");
   } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Erro ao salvar!"); }
  });

  btnExcluir.addActionListener(e -> {
   int linha = tabela.getSelectedRow();
   if (linha != -1) {
    int id = (int) modelo.getValueAt(linha, 1);
    int confirm = JOptionPane.showConfirmDialog(this, "Excluir cliente?", "Confirmar", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
     ClienteDAO.excluir(id);
     atualizarLista();
     limparCampos();
    }
   }
  });

  btnRepor.addActionListener(e -> {
   try {
    atualizarEstoqueNoBanco(Integer.parseInt(txtNovaQtd.getText()));
    txtNovaQtd.setText("");
   } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Número inválido!"); }
  });

  btnMarcarTodos.addActionListener(e -> marcarTudo(true));
  btnDesmarcarTodos.addActionListener(e -> marcarTudo(false));
  txtBusca.addCaretListener(e -> atualizarLista());
  btnEnviarWpp.addActionListener(e -> {
   for (int i = 0; i < tabela.getRowCount(); i++) {
    if ((Boolean) tabela.getValueAt(i, 0)) {
     WhatsAppSender.enviar(tabela.getValueAt(i, 3).toString(), comboMensagens.getSelectedItem().toString());
    }
   }
  });
  btnExcel.addActionListener(e -> CsvExporter.exportar(ClienteDAO.listar(txtBusca.getText())));

  carregarEstoque();
  atualizarLista();
  setVisible(true);
 }

 // --- MÉTODOS AUXILIARES ---
 void marcarTudo(boolean status) {
  for (int i = 0; i < modelo.getRowCount(); i++) modelo.setValueAt(status, i, 0);
 }

 void baixarEstoque(int qtd) {
  int saldoAtual = 0;
  try (Connection conn = Database.connect()) {
   ResultSet rs = conn.createStatement().executeQuery("SELECT quantidade FROM estoque WHERE id = 1");
   if (rs.next()) saldoAtual = rs.getInt("quantidade");
  } catch (Exception e) { e.printStackTrace(); }
  atualizarEstoqueNoBanco(Math.max(0, saldoAtual - qtd));
 }

 void atualizarEstoqueNoBanco(int qtd) {
  try (Connection conn = Database.connect();
       PreparedStatement ps = conn.prepareStatement("UPDATE estoque SET quantidade = ? WHERE id = 1")) {
   ps.setInt(1, qtd);
   ps.executeUpdate();
   lblEstoqueStatus.setText("Estoque: " + qtd);
   lblEstoqueStatus.setForeground(qtd <= 20 ? Color.RED : new Color(0, 120, 0));
  } catch (Exception e) { e.printStackTrace(); }
 }

 void carregarEstoque() {
  try (Connection conn = Database.connect()) {
   ResultSet rs = conn.createStatement().executeQuery("SELECT quantidade FROM estoque WHERE id = 1");
   if (rs.next()) {
    int q = rs.getInt("quantidade");
    lblEstoqueStatus.setText("Estoque: " + q);
    lblEstoqueStatus.setForeground(q <= 20 ? Color.RED : new Color(0, 120, 0));
   }
  } catch (Exception e) { e.printStackTrace(); }
 }

 void atualizarLista() {
  modelo.setRowCount(0);
  for (Cliente c : ClienteDAO.listar(txtBusca.getText())) {
   modelo.addRow(new Object[]{false, c.id, c.nome, c.telefone, c.tipoBase, c.ultimaCompra});
  }
 }

 void limparCampos() {
  txtNome.setText(""); txtTelefone.setText("");
  comboBase.setSelectedIndex(0); // Reseta para o campo em branco ("")
  txtCompra.setText(LocalDate.now().format(dtf));
  txtQtdVendida.setText("1");
  txtNome.setBackground(Color.WHITE);
 }
}