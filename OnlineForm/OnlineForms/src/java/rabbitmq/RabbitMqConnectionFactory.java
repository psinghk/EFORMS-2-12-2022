package rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;
import org.apache.struts2.ServletActionContext;

public class RabbitMqConnectionFactory {

    private static final RabbitMqConnectionFactory INSTANCE = new RabbitMqConnectionFactory();
    private static final String HOSTNAME = "localhost";
    //private static final String HOSTNAME = "10.1.162.252";
    private Connection myConnection = null;
    String ip = ServletActionContext.getRequest().getRemoteAddr();

    private RabbitMqConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername("eforms");
        factory.setPassword("eforms@123");
        try {
            myConnection = factory.newConnection();
        } catch (IOException ex) {

            System.out.println(ServletActionContext.getRequest().getSession().getId() + " == IP: " + ip + " timestamp: == "
                    + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " == " + " RabbitMqConnectionFactory exception " + ex.getMessage());

            ex.printStackTrace();
            //Logger.getLogger(RabbitMqConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            //Logger.getLogger(RabbitMqConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public static RabbitMqConnectionFactory getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() {
        if (myConnection != null) {
            return myConnection;
        } else {
            return INSTANCE.getConnection();
        }
    }
}
