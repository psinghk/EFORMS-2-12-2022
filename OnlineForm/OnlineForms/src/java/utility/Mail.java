package utility;

/*import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;*/
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.struts2.ServletActionContext;

public class Mail {

    private static String smtp = "relay.nic.in";

    public static void sendMail(String to, String msg, String sub, String from) {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " to is " + to + " sub is " + sub + " msg is " + msg);

        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.socketFactory.port", "25");
        //props.put("mail.debug", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props);
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(sub);
            message.setContent(msg, "text/html");

            Transport.send(message); // to be uncommented in production
        } catch (MessagingException e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR" + e.toString());
            throw new RuntimeException(e);
        }
    }

    public static void sendMailWithAttach(String[] cc, String from, String toAddress, String subject, String message,
            String[] attachFiles)
            throws AddressException, MessagingException {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " to is " + toAddress + " sub is " + subject + " msg is " + message + " cc is " + cc[0]);

        // sets SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.socketFactory.port", "25");
        //props.put("mail.debug", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");

        // creates a new session with an authenticator
        Session session = Session.getInstance(props);
        try {
            // creates a new e-mail message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(from));
            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);

            if (cc != null && cc.length > 0) {
                for (String mail : cc) {
                    msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail));
                }
            }

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            System.out.println(" attachFiles length is " + attachFiles.length);

            // adds attachments
            if (attachFiles != null && attachFiles.length > 0) {
                System.out.println(" inside attachfile not null ");

                for (String filePath : attachFiles) {
                    System.out.println(" inside for loop file path is " + filePath);

                    MimeBodyPart attachPart = new MimeBodyPart();

                    try {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " filepath is " + filePath);

                        attachPart.attachFile(filePath);

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " after filepath is ");

                    } catch (IOException ex) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside catch path ");

                        ex.printStackTrace();
                    }

                    multipart.addBodyPart(attachPart);
                }
            }
            // sets the multi-part as e-mail's content
            msg.setContent(multipart);
            // sends the e-mail
          Transport.send(msg); // to be uncommented in production
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // below function added by pr on 23rdmar18
    public static void sendMailWithAttachOnly(String from, String toAddress, String subject, String message,
            String[] attachFiles)
            throws AddressException, MessagingException {

        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " to is " + toAddress + " sub is " + subject + " msg is " + message);

        // sets SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.socketFactory.port", "25");
        //props.put("mail.debug", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");

        // creates a new session with an authenticator
        Session session = Session.getInstance(props);
        try {
            // creates a new e-mail message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(from));
            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            System.out.println(" attachFiles length is " + attachFiles.length);

            // adds attachments
            if (attachFiles != null && attachFiles.length > 0) {
                System.out.println(" inside attachfile not null ");

                for (String filePath : attachFiles) {
                    System.out.println(" inside for loop file path is " + filePath);

                    MimeBodyPart attachPart = new MimeBodyPart();

                    try {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " filepath is " + filePath);

                        attachPart.attachFile(filePath);

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " after filepath is ");

                    } catch (IOException ex) {

                        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " inside catch path ");

                        ex.printStackTrace();
                    }

                    multipart.addBodyPart(attachPart);
                }
            }
            // sets the multi-part as e-mail's content
            msg.setContent(multipart);
            // sends the e-mail
          Transport.send(msg); // to be uncommented in production
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendMailWithCcBcc(String to, String[] cc, String[] bcc, String msg, String sub, String from) {
        System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + " to is " + to + " sub is " + sub + " msg is " + msg + " cc is " + cc[0] + " bcc is " + bcc[0]);

        Properties props = new Properties();

        props.put("mail.smtp.host", smtp);

        props.put("mail.smtp.socketFactory.port", "25");

        //props.put("mail.debug", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.port", "25");

        Session session = Session.getInstance(props);

        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            if (cc != null && cc.length > 0) {
                for (String mail : cc) {
                    message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail));
                }
            }

            if (bcc != null && bcc.length > 0) {
                for (String mail : bcc) {
                    message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail));
                }

            }

            message.setSubject(sub);

            message.setContent(msg, "text/html");

           Transport.send( message ); // to be uncommented in production
        } catch (MessagingException e) {
            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == " + "ERROR " + e.toString());

            throw new RuntimeException(e);
        }

    }

}
