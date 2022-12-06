package rabbitmq;

import com.org.validation.Validation;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import java.io.*;
import java.util.*;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;

public class NotifyThrouhRabbitMQ {

    private final static String EMAIL_BACKUP = "emailbackup";
    private final static String EMAIL_OTP_QUEUE_NAME = "email";
    private final static String SMS_OTP_QUEUE_NAME = "sms";
    private final static String EMAIL_QUEUE_NAME = "emailotp";
    private final static String SMS_QUEUE_NAME = "smsotp";
    private final static String VPN_QUEUE_NAME = "vpn";
    private final static String EXCHANGE_FANOUT = "eforms.notify";
    private final static String EXCHANGE_DIRECT_BACKUP = "eforms.backup";
    private final static String EXCHANGE_DIRECT = "eforms.direct";
    
    // start, code added by pr on 16thmar2020
    ServletContext ctx = getServletContext();
    private String flag_notify = ctx.getInitParameter("flag_notify"); // to send sms email or not, in development it should be false and in production it should be true
    // end, code added by pr on 16thmar2020
    String ip = ServletActionContext.getRequest().getRemoteAddr();

    public boolean notify(HashMap<String, Object> message) {
        boolean flag = true;
           //make false for Testing on Local machine 
        if (flag_notify.trim().equalsIgnoreCase("true")) {

            try {
                Channel channel = RabbitMqConnectionFactory.getInstance().getConnection().createChannel();
                System.out.println("channel created in" + channel);
                channel.exchangeDeclare(EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT, true);
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(message);
                channel.basicPublish(EXCHANGE_FANOUT, "", null, byteOut.toByteArray());
                channel.close();
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " NotifyThrouhRabbitMQ exception " + ex.getMessage());
                ex.printStackTrace();
            }

        }

        return flag;
    }

    public boolean notifyEmailBackup(HashMap<String, Object> message) {
        boolean flag = true;
        if (flag_notify.trim().equalsIgnoreCase("true")) {
            try {
                Channel channel = RabbitMqConnectionFactory.getInstance().getConnection().createChannel();
                System.out.println("channel created in" + channel);
                AMQP.Exchange.DeclareOk aa = channel.exchangeDeclare(EXCHANGE_DIRECT_BACKUP, BuiltinExchangeType.DIRECT, true);
                AMQP.Queue.DeclareOk bb = channel.queueDeclare(EMAIL_BACKUP, true, false, false, null);
                AMQP.Queue.BindOk cc = channel.queueBind(EMAIL_BACKUP, EXCHANGE_DIRECT_BACKUP, "backup");
                System.out.println("AA :: "+aa+" BB ::"+bb+" CC::"+cc);
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(message);
                channel.basicPublish(EXCHANGE_DIRECT_BACKUP, "backup", null, byteOut.toByteArray());
                System.out.println(" Inside notifyEmailBackup After publishing The Data In Queues... !!");
                channel.close();
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " NotifyThrouhRabbitMQ exception " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return flag;
    }

    public boolean notifyEmailToZimbraForRetiringThisMonth(HashMap<String, Object> message) {
        boolean flag = true;
        if (flag_notify.trim().equalsIgnoreCase("true")) {
            try {
                Channel channel = RabbitMqConnectionFactory.getInstance().getConnection().createChannel();
                System.out.println("channel created in" + channel);
                AMQP.Exchange.DeclareOk aa = channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT, true);
                AMQP.Queue.DeclareOk bb = channel.queueDeclare(EMAIL_OTP_QUEUE_NAME, true, false, false, null);
                AMQP.Queue.BindOk cc = channel.queueBind(EMAIL_OTP_QUEUE_NAME, EXCHANGE_DIRECT, "email");
                System.out.println("AA :: "+aa+" BB ::"+bb+" CC::"+cc);
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(message);
                channel.basicPublish(EXCHANGE_DIRECT, "email", null, byteOut.toByteArray());
                System.out.println(" Inside notifyEmailToZimbraForRetiringThisMonth After publishing The Data In Queues... !!");
                channel.close();
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " NotifyThrouhRabbitMQ exception " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return flag;
    }
    public boolean sendSmsOtpRabbitMq(HashMap<String, Object> message) {
        boolean flag = true;

        if (flag_notify.trim().equalsIgnoreCase("true")) {
            String mobile = message.get("mobile").toString();
            Validation validation = new Validation();
            if(mobile == null || mobile.isEmpty())
                return false;
            if(!validation.checkFormat("mobile", mobile)){
                return false;
            }
            if(mobile.startsWith("+")){
                mobile = mobile.substring(1);
            }
            
            try {
                Channel channel = RabbitMqConnectionFactory.getInstance().getConnection().createChannel();
                System.out.println("channel created in");
                channel.queueDeclare(SMS_QUEUE_NAME, true, false, false, null);
                System.out.println("sms queue declared");
                ByteArrayOutputStream byteOut_sms = new ByteArrayOutputStream();
                ObjectOutputStream out_sms = new ObjectOutputStream(byteOut_sms);
                out_sms.writeObject(message);
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration("300000").build();
                channel.basicPublish("", SMS_QUEUE_NAME, properties, byteOut_sms.toByteArray());
                channel.close();
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendSmsOtpRabbitMq exception " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        return flag;
    }

    public boolean sendEmailOtpRabbitMq(HashMap<String, Object> message) {
        boolean flag = true;

        if (flag_notify.trim().equalsIgnoreCase("true")) {

            try {
                Channel channel = RabbitMqConnectionFactory.getInstance().getConnection().createChannel();
                System.out.println("channel created in");

                channel.queueDeclare(EMAIL_QUEUE_NAME, true, false, false, null);
                System.out.println("email queue declared");
                ByteArrayOutputStream byteOut_email = new ByteArrayOutputStream();
                ObjectOutputStream out_email = new ObjectOutputStream(byteOut_email);
                out_email.writeObject(message);
                AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().expiration("600000").build();
                channel.basicPublish("", EMAIL_QUEUE_NAME, properties, byteOut_email.toByteArray());
                channel.close();
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendEmailOtpRabbitMq exception " + ex.getMessage());
                ex.printStackTrace();
            }

        }

        return flag;
    }

    public boolean sendVpnData(String registration_no) {
        boolean flag = true;

        if (flag_notify.trim().equalsIgnoreCase("true")) {

            try {
                Channel channel = RabbitMqConnectionFactory.getInstance().getConnection().createChannel();
                System.out.println("channel created in");

                channel.queueDeclare(VPN_QUEUE_NAME, true, false, false, null);
                System.out.println("vpn queue declared");
                ByteArrayOutputStream byteOut_email = new ByteArrayOutputStream();
                ObjectOutputStream out_email = new ObjectOutputStream(byteOut_email);
                out_email.writeObject(registration_no);
                channel.basicPublish("", VPN_QUEUE_NAME, null, byteOut_email.toByteArray());
                channel.close();
            } catch (Exception ex) {
                System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                        + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " sendVPNOtpRabbitMq exception " + ex.getMessage());
                ex.printStackTrace();
            }

        }

        return flag;
    }
}
