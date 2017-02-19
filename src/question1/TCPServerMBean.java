package question1;

import javax.management.NotificationEmitter;
import java.io.Serializable;

public interface TCPServerMBean extends NotificationEmitter
{
    // operations
    public void create(int port) throws Exception;

    public void start();

    public void stop();

    //"getter" seulement
    public long getMaxActiveTime();

    public long getPort();

    public boolean isActive();
}
