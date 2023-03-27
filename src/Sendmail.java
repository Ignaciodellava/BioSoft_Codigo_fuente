

	import javax.mail.*;
	import javax.mail.internet.InternetAddress;
	import javax.mail.internet.MimeBodyPart;
	import javax.mail.internet.MimeMessage;
	import javax.mail.internet.MimeMultipart;
	import java.time.LocalDateTime;
	import java.time.format.DateTimeFormatter;
	import java.util.Properties;
	import java.util.logging.Level;
	import java.util.logging.Logger;

	public class Sendmail {

		//--------------------------------------------------------------------------------------------------------------------
	    private static String fecha_y_hora() {
	        LocalDateTime FechaYHoraSinFormato = LocalDateTime.now();
	        DateTimeFormatter FormatoDeFechaYHora = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	        String FechaYHora = FechaYHoraSinFormato.format(FormatoDeFechaYHora);

	        return FechaYHora;
	    }

	  //--------------------------------------------------------------------------------------------------------------------
	    public void sendMail_LOG(String recepient) throws MessagingException{
	        System.out.println("Preparando para enviar el mail...");
	        Properties properties = new Properties();

	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.port", "587");
	        properties.put("mail.smtp.ssl.trust", "*");// Añade todas las propiedades.
	        
	        String micorreo = "biosoft.live.uem@gmail.com";
	        String contrasena = "MiMailJavaEnviar";

	        Session session = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(micorreo,contrasena);
	            }
	        });

	        Message message = prepareMessageLOG(session, micorreo, recepient);

	        Transport.send(message);
	        System.out.println("Mail enviado correctamente.");
	    }

	  //--------------------------------------------------------------------------------------------------------------------
	    public static void sendMail_PacientesT(String recepient) throws MessagingException{
	        System.out.println("Preparando para enviar el mail...");
	        Properties properties = new Properties();

	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.port", "587");

	        String micorreo = "biosoft.live.uem@gmail.com";
	        String contrasena = "MiMailJavaEnviar";

	        Session session = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(micorreo,contrasena);
	            }
	        });

	        Message message = prepareMessagePacientesT(session, micorreo, recepient);

	        Transport.send(message);
	        System.out.println("Mail enviado correctamente.");
	    }

	  //--------------------------------------------------------------------------------------------------------------------
	    public static void sendMail_CLIENTE(String recepient, String u) throws MessagingException{
	        System.out.println("Preparando para enviar el mail...");
	        Properties properties = new Properties();

	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.port", "587");

	        String micorreo = "biosoft.live.uem@gmail.com";
	        String contrasena = "MiMailJavaEnviar";

	        Session session = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(micorreo,contrasena);
	            }
	        });

	        Message message = prepareMessagePacientes(session, micorreo, recepient, u);

	        Transport.send(message);
	        System.out.println("Mail enviado correctamente.");
	    }

	  //--------------------------------------------------------------------------------------------------------------------
	    public static Message prepareMessageLOG(Session session, String micorreo, String recepient) {
	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(micorreo));
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
	            message.setSubject("Log del hospital");

	            Multipart emailContent = new MimeMultipart();
	            //Cuerpo del mail

	            MimeBodyPart textbody = new MimeBodyPart();
	            textbody.setText("Aquí se adjunta el log a fecha: "+fecha_y_hora());

	            MimeBodyPart csvAttachment = new MimeBodyPart();
	            csvAttachment.attachFile("Log.csv");

	            emailContent.addBodyPart(textbody);
	            emailContent.addBodyPart(csvAttachment);

	            message.setContent(emailContent);
	            return message;
	        } catch (Exception ex){
	            Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return null;
	    }

	  //--------------------------------------------------------------------------------------------------------------------
	    public static Message prepareMessagePacientesT(Session session, String micorreo, String recepient) {
	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(micorreo));
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
	            message.setSubject("Todos los pacientes");

	            Multipart emailContent = new MimeMultipart();
	            //Cuerpo del mail

	            MimeBodyPart textbody = new MimeBodyPart();
	            textbody.setText("Aquí se adjunta la lista de todos los pacientes a fecha: "+fecha_y_hora());

	            MimeBodyPart csvAttachment = new MimeBodyPart();
	            csvAttachment.attachFile("Pacientes.csv");

	            emailContent.addBodyPart(textbody);
	            emailContent.addBodyPart(csvAttachment);

	            message.setContent(emailContent);
	            return message;
	        } catch (Exception ex){
	            Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return null;
	    }

	  //--------------------------------------------------------------------------------------------------------------------
	    public static Message prepareMessagePacientes(Session session, String micorreo, String recepient, String Usuario) {
	        try {
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(micorreo));
	            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
	            message.setSubject("Mis pacientes");

	            Multipart emailContent = new MimeMultipart();
	            //Cuerpo del mail

	            MimeBodyPart textbody = new MimeBodyPart();
	            textbody.setText("Aquí se adjunta la lista de mis pacientes a fecha: "+fecha_y_hora());

	            MimeBodyPart csvAttachment = new MimeBodyPart();
	            csvAttachment.attachFile(Usuario+".csv");

	            emailContent.addBodyPart(textbody);
	            emailContent.addBodyPart(csvAttachment);

	            message.setContent(emailContent);
	            return message;
	        } catch (Exception ex){
	            Logger.getLogger(Sendmail.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return null;
	    }
	}



