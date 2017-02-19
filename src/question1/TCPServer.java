package question1;

import javax.management.NotificationBroadcasterSupport;
import javax.management.Notification;

import java.io.Serializable;

public class TCPServer extends NotificationBroadcasterSupport implements TCPServerMBean, Serializable
{
    private transient ExperimentalTCPServer server;
    private boolean active;

    private long sequenceNumber = 0;

    public void create(int port) throws Exception
    {
        this.server = new ExperimentalTCPServer(port, new NotificationHandler());
    }

    private class NotificationHandler implements ExceptionListener, Serializable /*, a completer */
    {
        public void onException(Throwable cause)
        {
            TCPServer.this.sequenceNumber++;
            // envoi d'une notification, aux abonnes
            // a completer
            sendNotification(
                    new Notification("TCPServer.onException",
                            this,
                            sequenceNumber,
                            "" + cause
                    )
            );
        }
    }

    public void start()
    {
        server.start();
        this.active = true;
    }

    public void stop()
    {
        server.stop();
        this.active = false;
    }

    private boolean getActive()
    {
        return active;
    }


    // "getter" seulement
    public long getMaxActiveTime()
    {
        return this.server.maxActiveTime();
    }

    public long getPort()
    {
        return this.server.port();
    }

    public boolean isActive()
    {
        return active;
    }
}
