
package com.unhacrm;
import javax.swing.SwingUtilities;

public class Main {
 public static void main(String[] args){
  Database.init();
  SwingUtilities.invokeLater(() -> new MainWindow());
 }
}
