
package entities;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
public class Email_Sender {
    ServletContext ctx = getServletContext();
    private final String host = ctx.getInitParameter("send-mail");
    private final String from = ctx.getInitParameter("send-mail-from");
    public void Email_sender(String email, String msg, String subject) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            //message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse("support@gov.in"));
            message.setSubject(subject);
            message.setContent(msg, "text/html");
           //Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void Email_sender_attach(String email, String msg, String subject, String filepath) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(msg, "text/html");
            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            // adds attachments
            MimeBodyPart attachPart = new MimeBodyPart();
            attachPart.attachFile(filepath);
            multipart.addBodyPart(attachPart);
            // sets the multi-part as e-mail's content
            message.setContent(multipart);
            // sends the e-mail
           //Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    //below function added by pr on 29thjun18
      public void Email_sender_otp(String email, String msg, String subject) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);     //mail.nic.in
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props);
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            //message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse("support@gov.in"));
            message.setSubject(subject);
            message.setContent(msg, "text/html");
            //Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
