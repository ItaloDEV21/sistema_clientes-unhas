package com.unhacrm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainWindow extends JFrame {

 JTextField txtNome = new JTextField(), txtTelefone = new JTextField(),
         txtBairro = new JTextField(), txtCompra = new JTextField(),
         txtQtdVendida = new JTextField("1"), txtBusca = new JTextField(),
         txtValor = new JTextField("0.00");

 JSpinner spinDataNasc = new JSpinner(new SpinnerDateModel());
 JSpinner.DateEditor editorNasc = new JSpinner.DateEditor(spinDataNasc, "dd/MM/yyyy");

 String[] opcoesBases = {"", "Base Fortalecedora"};
 JComboBox<String> comboBase = new JComboBox<>(opcoesBases);

 JTable tabela;
 DefaultTableModel modelo;
 JLabel lblEstoqueStatus = new JLabel("Estoque: 0");
 JLabel lblTotalVendido = new JLabel("Total vendido: R$ 0.00");
 JComboBox<String> comboMensagens;
 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

 public MainWindow() {
  setTitle("KS Store CRM PRO - Local DB");
  setSize(1200, 750);
  setDefaultCloseOperation(EXIT_ON_CLOSE);
  setLocationRelativeTo(null);
  setLayout(new BorderLayout());

  txtCompra.setText(LocalDate.now().format(dtf));
  spinDataNasc.setEditor(editorNasc);

  txtValor.addFocusListener(new FocusAdapter() {
   @Override
   public void focusGained(FocusEvent e) {
    txtValor.selectAll();
   }
   @Override
   public void focusLost(FocusEvent e) {
    formatarCampoValor();
   }
  });

  JPanel pnlSuperior = new JPanel(new GridLayout(1, 2, 10, 10));
  pnlSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

  JPanel pnlForm = new JPanel(new GridLayout(9, 2, 8, 8));
  pnlForm.setBorder(BorderFactory.createTitledBorder("Venda"));

  pnlForm.add(new JLabel("Nome:")); pnlForm.add(txtNome);
  pnlForm.add(new JLabel("Telefone:")); pnlForm.add(txtTelefone);
  pnlForm.add(new JLabel("Bairro:")); pnlForm.add(txtBairro);
  pnlForm.add(new JLabel("Nascimento (dd/mm/aaaa):")); pnlForm.add(spinDataNasc);
  pnlForm.add(new JLabel("Base:")); pnlForm.add(comboBase);
  pnlForm.add(new JLabel("Data Venda:")); pnlForm.add(txtCompra);
  pnlForm.add(new JLabel("Qtd:")); pnlForm.add(txtQtdVendida);
  pnlForm.add(new JLabel("Valor (R$):")); pnlForm.add(txtValor);

  JButton btnSalvar = new JButton("Registrar Venda");
  pnlForm.add(btnSalvar);

  JPanel pnlEstoque = new JPanel(new FlowLayout());
  pnlEstoque.setBorder(BorderFactory.createTitledBorder("Estoque"));
  JTextField txtNovaQtd = new JTextField(5);
  JButton btnRepor = new JButton("Atualizar");
  pnlEstoque.add(new JLabel("Qtd:"));
  pnlEstoque.add(txtNovaQtd);
  pnlEstoque.add(btnRepor);
  pnlEstoque.add(lblEstoqueStatus);

  pnlSuperior.add(pnlForm);
  pnlSuperior.add(pnlEstoque);

  modelo = new DefaultTableModel(new Object[]{"✔", "ID", "Nome", "Telefone", "Bairro", "Nasc.", "Base", "Data", "Valor"}, 0) {
   public Class<?> getColumnClass(int c) { return c == 0 ? Boolean.class : String.class; }
   public boolean isCellEditable(int r, int c) { return c == 0; }
  };
  tabela = new JTable(modelo);
  tabela.setRowHeight(25);

  tabela.addMouseListener(new MouseAdapter() {
   public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() == 2) {
     int l = tabela.getSelectedRow();
     if (l != -1) {
      txtNome.setText(modelo.getValueAt(l, 2).toString());
      txtTelefone.setText(modelo.getValueAt(l, 3).toString());
      txtBairro.setText(modelo.getValueAt(l, 4).toString());
      comboBase.setSelectedItem(modelo.getValueAt(l, 6).toString());
      txtCompra.setText(modelo.getValueAt(l, 7).toString());
      txtValor.setText(modelo.getValueAt(l, 8).toString());
     }
    }
   }
  });

  JPanel pnlCentro = new JPanel(new BorderLayout());
  pnlCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
  JPanel pnlBusca = new JPanel(new BorderLayout());
  pnlBusca.add(new JLabel("Buscar (Nome/Bairro): "), BorderLayout.WEST);
  pnlBusca.add(txtBusca, BorderLayout.CENTER);
  pnlCentro.add(pnlBusca, BorderLayout.NORTH);
  pnlCentro.add(new JScrollPane(tabela), BorderLayout.CENTER);

  JPanel pnlAcoes = new JPanel();
  JButton btnSelecionarTodos = new JButton("Selecionar Todos");
  JButton btnExcluir = new JButton("Excluir");
  JButton btnWpp = new JButton("WhatsApp");
  JButton btnFinanceiro = new JButton("Financeiro");
  JButton btnExportar = new JButton("Exportar CSV");

  comboMensagens = new JComboBox<>(new String[]{
          "💅 Olá! Aqui é a KS Store! Sua base está acabando?",
          "🎂 Parabéns! A KS Store tem um presente de aniversário!",
          "✨ Já cuidou das suas unhas hoje?"
  });

  pnlAcoes.add(btnSelecionarTodos);
  pnlAcoes.add(comboMensagens);
  pnlAcoes.add(btnWpp);
  pnlAcoes.add(btnExportar);
  pnlAcoes.add(btnExcluir);
  pnlAcoes.add(btnFinanceiro);
  pnlAcoes.add(lblTotalVendido);

  add(pnlSuperior, BorderLayout.NORTH);
  add(pnlCentro, BorderLayout.CENTER);
  add(pnlAcoes, BorderLayout.SOUTH);

  // --- LISTENERS ---

  btnSelecionarTodos.addActionListener(e -> {
   boolean existeDesmarcado = false;
   for (int i = 0; i < modelo.getRowCount(); i++) {
    if (!(Boolean) modelo.getValueAt(i, 0)) {
     existeDesmarcado = true;
     break;
    }
   }
   for (int i = 0; i < modelo.getRowCount(); i++) {
    modelo.setValueAt(existeDesmarcado, i, 0);
   }
  });

  // LÓGICA DE ENVIO EM MASSA
  btnWpp.addActionListener(e -> {
   int contagem = 0;
   String mensagemBase = comboMensagens.getSelectedItem().toString();

   for (int i = 0; i < modelo.getRowCount(); i++) {
    Boolean selecionado = (Boolean) modelo.getValueAt(i, 0);

    if (selecionado) {
     String telefone = modelo.getValueAt(i, 3).toString().replaceAll("\\D", "");
     try {
      String textoEncoded = URLEncoder.encode(mensagemBase, "UTF-8");
      String url = "https://api.whatsapp.com/send?phone=55" + telefone + "&text=" + textoEncoded;
      Desktop.getDesktop().browse(new URI(url));
      contagem++;

      // Pequena pausa para não travar o navegador se houver muitos envios
      Thread.sleep(300);
     } catch (Exception ex) {
      System.err.println("Erro ao enviar para linha " + i + ": " + ex.getMessage());
     }
    }
   }

   if (contagem == 0) {
    JOptionPane.showMessageDialog(this, "Nenhum cliente marcado na coluna ✔!");
   } else {
    JOptionPane.showMessageDialog(this, contagem + " janelas do WhatsApp abertas!");
   }
  });

  btnSalvar.addActionListener(e -> registrarVenda());
  btnExportar.addActionListener(e -> exportarParaCSV());
  btnRepor.addActionListener(e -> {
   try {
    EstoqueDAO.atualizarEstoque(Integer.parseInt(txtNovaQtd.getText()));
    carregarEstoque();
    txtNovaQtd.setText("");
   } catch (Exception ex) { JOptionPane.showMessageDialog(null, "Informe um número válido."); }
  });
  btnExcluir.addActionListener(e -> {
   int l = tabela.getSelectedRow();
   if (l != -1) {
    ClienteDAO.excluir((int) modelo.getValueAt(l, 1));
    atualizarLista();
   }
  });
  btnFinanceiro.addActionListener(e -> new FinanceiroWindow());
  txtBusca.addCaretListener(e -> atualizarLista());

  carregarEstoque();
  atualizarLista();
  setVisible(true);
 }

 private void formatarCampoValor() {
  try {
   String valorLimpo = txtValor.getText().replace(".", "").replace(",", ".");
   double v = Double.parseDouble(valorLimpo);
   DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
   txtValor.setText(df.format(v));
  } catch (Exception e) {
   txtValor.setText("0.00");
  }
 }

 private void registrarVenda() {
  String nome = txtNome.getText();
  String tel = txtTelefone.getText();
  String bairro = txtBairro.getText();
  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  String nasc = sdf.format(spinDataNasc.getValue());
  String base = comboBase.getSelectedItem().toString();
  String data = txtCompra.getText();
  String vStr = txtValor.getText().replace(".", "").replace(",", ".");
  String qStr = txtQtdVendida.getText();

  new SwingWorker<Void, Void>() {
   protected Void doInBackground() throws Exception {
    if (base.isEmpty()) throw new Exception("Selecione uma base.");
    double valor = Double.parseDouble(vStr);
    int qtd = Integer.parseInt(qStr);
    Cliente c = new Cliente(nome, tel, bairro, nasc, base, data);
    ClienteDAO.adicionar(c, valor);
    VendaDAO.registrarVenda(qtd, valor);
    return null;
   }
   protected void done() {
    try {
     get();
     limparCampos();
     carregarEstoque();
     atualizarLista();
     JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");
    } catch (Exception ex) {
     JOptionPane.showMessageDialog(null, "Erro ao registrar: " + ex.getMessage());
    }
   }
  }.execute();
 }

 private void exportarParaCSV() {
  JFileChooser chooser = new JFileChooser();
  chooser.setSelectedFile(new File("relatorio_clientes.csv"));
  if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
   File arquivo = chooser.getSelectedFile();
   try (PrintWriter pw = new PrintWriter(arquivo, "UTF-8")) {
    pw.println("sep=;");
    pw.println("ID;Nome;Telefone;Bairro;Nasc;Base;Data;Valor");
    for (Cliente c : ClienteDAO.listar(null)) {
     String valorBr = String.format("%.2f", c.valorUltimaVenda).replace(".", ",");
     pw.println(c.id + ";" + c.nome + ";" + c.telefone + ";" + c.bairro + ";" +
             c.dataNascimento + ";" + c.tipoBase + ";" + c.ultimaCompra + ";" + valorBr);
    }
    pw.flush();
    JOptionPane.showMessageDialog(this, "Relatório Gerado com Sucesso!");
   } catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
   }
  }
 }

 void carregarEstoque() { lblEstoqueStatus.setText("Estoque: " + EstoqueDAO.getEstoque()); }

 void atualizarLista() {
  modelo.setRowCount(0);
  for (Cliente c : ClienteDAO.listar(txtBusca.getText())) {
   modelo.addRow(new Object[]{false, c.id, c.nome, c.telefone, c.bairro, c.dataNascimento, c.tipoBase, c.ultimaCompra, c.valorUltimaVenda});
  }
  calcularTotal();
 }

 void calcularTotal() {
  double total = 0;
  for (int i = 0; i < tabela.getRowCount(); i++) {
   total += Double.parseDouble(tabela.getValueAt(i, 8).toString());
  }
  lblTotalVendido.setText("Total exibido: R$ " + String.format("%.2f", total));
 }

 void limparCampos() {
  txtNome.setText(""); txtTelefone.setText(""); txtBairro.setText("");
  spinDataNasc.setValue(new java.util.Date());
  comboBase.setSelectedIndex(0);
  txtQtdVendida.setText("1"); txtValor.setText("0.00");
  txtCompra.setText(LocalDate.now().format(dtf));
 }
}