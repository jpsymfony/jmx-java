package question1;

import javax.management.*;
import java.lang.management.*;

public class Tests_TCPServerMBean extends junit.framework.TestCase implements NotificationListener
{
    private boolean unePanneRecue;

    public void handleNotification(Notification notification, Object handback)
    {
        this.unePanneRecue = true;
    }

    public void test_TCPServerMBean()
    {
        MBeanServer mbs = null;
        try {
            mbs = ManagementFactory.getPlatformMBeanServer();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        TCPServer bean = new TCPServer();
        try {
            ObjectName name = new ObjectName("question1.TCPServer:name=TCPAgent");
            mbs.registerMBean(bean, name);
            bean.addNotificationListener(this,null,null);

            TCPServerMBean server = (TCPServerMBean)
                    MBeanServerInvocationHandler.newProxyInstance(mbs, name, question1.TCPServerMBean.class, false);
            assertFalse("le server est actif ?, curieux ...", server.isActive());
            this.unePanneRecue = false;
            server.create(5555);
            server.start();
            assertTrue("le server est inactif ?, curieux ...", server.isActive());

            Thread.sleep(18 * 1000);
            server.stop();
            assertTrue("aucune notification en cas de panne ... ?", this.unePanneRecue);
            mbs.unregisterMBean(name);
        } catch (Exception e) {
            fail("exception inattendue ! " + e.getClass().getName());
        }
    }
}


