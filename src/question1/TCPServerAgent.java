package question1;

import javax.management.*;
import java.lang.management.*;
import javax.management.remote.*;
import javax.management.remote.rmi.*;

import com.sun.jdmk.comm.*;

public class TCPServerAgent implements NotificationListener
{
    private MBeanServer mbs = null;

    public TCPServerAgent()
    {
        // Get the platform MBeanServer
        mbs = ManagementFactory.getPlatformMBeanServer();

        TCPServerMBean bean = new TCPServer();
        ObjectName name = null;

        try {
            name = new ObjectName("question1.TCPServer:name=TCPAgent");
            mbs.registerMBean(bean, name);
            bean.addNotificationListener(this,null,null);
            // ici le 'MBean' TCPServer et le listener se trouvent sur la meme jvm, c'est un cas particulier
            // voir la classe ClientRMI dans laquelle l'acces au 'MBean' TCPServer se fait de l'exterieur
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRMIConnector(String urlString) throws Exception
    {
        // ces quelques lignes evitent d'avoir a demarrer rmiregistry 9999
        java.rmi.registry.Registry registry = null;
        try {
            registry = java.rmi.registry.LocateRegistry.createRegistry(9999);
        } catch (Exception e) {
        }

        try {
            java.rmi.server.UnicastRemoteObject.unexportObject(registry.lookup("TCPserver"), true);
        } catch (Exception e) {
        }

        try {
            registry.unbind("TCPserver");
        } catch (Exception e) {
        }

        // Create an RMI connector and start it
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/TCPserver");
        JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
        cs.start();
    }

    public void setHTTPAdapter(String port) throws Exception
    {
        // a completer en question 4
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        adapter.setPort(Integer.parseInt(port));
        ObjectName name = new ObjectName("HtmlAdaptorServer:name=html,port=" + port);
        mbs.registerMBean(adapter, name);
        adapter.start();
    }

    @Override
    public void handleNotification(Notification notification, Object handback)
    {
        //System.out.print(notification.getMessage());
        System.out.println(" number : " + notification.getSequenceNumber());
    }

    public static void main(String argv[]) throws Exception
    {
        TCPServerAgent agent = new TCPServerAgent();
        agent.setRMIConnector("service:jmx:rmi:///jndi/rmi://localhost:9999/TCPserver");
        agent.setHTTPAdapter("8088");

        System.out.println("TCPServerAgent is running...");
        Thread.sleep(Long.MAX_VALUE);
    }
}
