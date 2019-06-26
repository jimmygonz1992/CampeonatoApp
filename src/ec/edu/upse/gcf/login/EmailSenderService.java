package ec.edu.upse.gcf.login;

import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSenderService{
	
	private String correoApp = "gcf.campeonatofutbol@gmail.com";
    //contrase�a para el envio de correo electronicos generado desde la cuenta de la app
    private String password = "gtlykiwykkyvtcou";
    private String asunto = "Recuperar contraseña Campeonato de Futbol";
    private Properties properties = new Properties();   
    private Session session;
    
    private void init() {
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.mail.sender",correoApp);
        properties.put("mail.smtp.auth", "true");
        session = Session.getDefaultInstance(properties);
    }
    
    
    public boolean enviarCorreo(String destino,String usuario){
        try {
            init();
            Integer numeroRetornado = generarNumeroAleatorio6Digito();          
            VariableGeneral.setNumeroAleatorio(numeroRetornado);
            String mensaje = "<br><br>Estimado(a): <b>"+usuario+"</b> "
                           + "<br>Se ha generado una nueva clave de 6 digitos "
                           + "<br>Clave: <b>"+numeroRetornado +"</b>"
                           + "<br><br>Saludos cordiales,"
                           + "<br><b>Campeonato de Fútbol"
                           + "</b>";
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoApp));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destino));
            message.setSubject(asunto);
            DataHandler dh = new DataHandler(mensaje,"text/html");
            message.setDataHandler(dh);
            Transport t = session.getTransport("smtp");
            t.connect(correoApp,password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
            // TODO: handle exception
        }
        
    }
    
    private Integer generarNumeroAleatorio6Digito(){
        try {
            Random r = new Random(System.currentTimeMillis());
            Integer resultado = r.nextInt(900000)+100000;
            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            // TODO: handle exception
        } 
        
    }
}
