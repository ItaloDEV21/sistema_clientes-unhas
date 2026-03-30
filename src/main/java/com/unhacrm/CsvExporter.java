package com.unhacrm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JOptionPane;

public class CsvExporter {
    public static void exportar(List<Cliente> lista) {
        String nomeArquivo = "relatorio_clientes.csv";

        try (FileWriter fw = new FileWriter(nomeArquivo);
             PrintWriter pw = new PrintWriter(fw)) {

            // Cabeçalho da Planilha
            pw.println("ID;Nome;Telefone;Tipo de Base;Ultima Compra");

            // Dados dos Clientes
            for (Cliente c : lista) {
                pw.println(c.id + ";" +
                        c.nome + ";" +
                        c.telefone + ";" +
                        c.tipoBase + ";" +
                        c.ultimaCompra);
            }

            JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso: " + nomeArquivo);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao gerar CSV: " + e.getMessage());
        }
    }
}