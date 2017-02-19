package question2;

public interface TCPServerStarterMBean
{
    public boolean getActive();

    public void setActive(boolean active);

    public long getNotifications();

    public void reset();

    public void stop() throws Exception;
}
