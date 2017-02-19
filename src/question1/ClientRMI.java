package question1;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.MBeanServerInvocationHandler;
import javax.management.Notification;
import javax.management.NotificationListener;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class ClientRMI implements NotificationListener /* a completer, c'est un ecouteur distant */
{
    public static void main(String[] args) throws Exception
    {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/TCPserver");
        JMXConnector cs = JMXConnectorFactory.connect(url);
        MBeanServerConnection mbs = cs.getMBeanServerConnection();
        ObjectName name = new ObjectName("question1.TCPServer:name=TCPAgent");

        TCPServerMBean res = null;
        // recuperation distante du mandataire : Appel de MBeanServerInvocationHandler.newProxyInstance
        res = (TCPServerMBean)MBeanServerInvocationHandler.newProxyInstance(mbs, name, TCPServerMBean.class, false);

        res.create(5555);
        res.start();
        // inscription distante via le connecteur rmi de cet ecouteur, appel de addNotificationListener
        res.addNotificationListener(new ClientRMI(),null,null);

        System.out.println("port : " + res.getPort());
        System.out.println("is active : " + res.isActive());
        Thread.sleep(1000 * 60 * 2);
    }

    @Override
    public void handleNotification(Notification notification, Object handback)
    {
        System.out.print(notification.getMessage());
        System.out.println(" number : " + notification.getSequenceNumber());
        System.out.println(handback);
    }
}
