
package com.unhacrm;
import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WhatsAppSender{
 public static void enviar(String telefone,String mensagem){
  try{
   String url="https://wa.me/"+telefone+"?text="+URLEncoder.encode(mensagem,StandardCharsets.UTF_8);
   Desktop.getDesktop().browse(new URI(url));
  }catch(Exception e){e.printStackTrace();}
 }
}
