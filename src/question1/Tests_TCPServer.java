package question1;

import java.net.*;

import java.io.*;

public class Tests_TCPServer extends junit.framework.TestCase
{
    public void test_ExperimentalTCPServer()
    {
        Socket socket = null;
        try {
            ExperimentalTCPServer server = new ExperimentalTCPServer(5000);
            server.start();
            assertTrue("curieux, ce n'est pas le bon port ?", server.port() == 5000);
            socket = new Socket("localhost", 5000);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("test");

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object obj = ois.readObject();

            server.stop();
            assertTrue("ce serveur ne fonctionne pas correctement ...", obj.toString().equals("test"));

        } catch (Exception e) {
            fail("exception inattendue ! " + e.getClass().getName());
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                //fail("exception inattendue ! " + e.getClass().getName());
            }
        }
    }
}
