package question2;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.*;
import java.lang.management.ManagementFactory;
import javax.management.ObjectName;

public class MLetAgent
{
    public MLetAgent()
    {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("Services:name=mlet");
            mbs.createMBean("javax.management.loading.MLet", name);
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/TCPserver");
            JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
            cs.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

