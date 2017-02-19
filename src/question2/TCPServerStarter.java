package question2;

import question1.*;

import javax.management.*;
import java.lang.management.*;

public class TCPServerStarter implements TCPServerStarterMBean, NotificationListener/*, a completer */
{
    private boolean active;
    private int notifications;

    private MBeanServer mbs = null;

    private TCPServerMBean server;

    public TCPServerStarter() throws Exception
    {
        this(5555);
    }

    public TCPServerStarter(int port) throws Exception
    {
        TCPServer bean = new TCPServer();
        ObjectName name = new ObjectName("TCPServer:name=TCPAgent_tests");
        try {
            mbs = ManagementFactory.getPlatformMBeanServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mbs.registerMBean(bean, name);

        // a completer
        // Effectuer les operations de demarrage du server ici
        this.server = (TCPServerMBean)
                MBeanServerInvocationHandler.newProxyInstance(mbs, name, TCPServerMBean.class, false);

        // server est un TCPServerMBean
        server.create(port);
        server.start();
        bean.addNotificationListener(this,null,null);
    }

    public void stop() throws Exception
    {
        server.stop();
        mbs.unregisterMBean(new ObjectName("TCPServer:name=TCPAgent_tests"));
    }

    public boolean getActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
        if (active)
            server.start();
        else
            server.stop();
    }

    public long getNotifications()
    {
        return notifications;
    }

    public void reset()
    {
        server.stop();
        server.start();
    }

    @Override
    public void handleNotification(Notification notification, Object handback)
    {
        // a completer
        // incrementer le nombre de notifications, (nombre de panne)
        // "re"-demarrer le serveur
        notifications++;
        this.reset();
    }

    public static void main(String[] args) throws Exception
    {
        new TCPServerStarter(5555);
        Thread.sleep(1000 * 60);
    }
}
