package question1;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Random;

/**
 * un tres simple serveur en mode TCP, (et experimental)
 * un Thread, traitement des requetes une par une, ...
 * <p>
 * Ce serveur repond aux requetes et parfois peut etre en panne ...
 * <p>
 * Ce serveur s'arrete de temps en temps (au bout de 10 secondes environ)
 * L'ecouteur associe (ExceptionListener) est declenche, si celui-ci est en place
 */
public class ExperimentalTCPServer implements Runnable
{
    private static Random random = new Random();
    private ExceptionListener el;
    private Thread local;
    private long timeActive;
    private ServerSocket serveur;
    private long top;

    public ExperimentalTCPServer(int port, ExceptionListener el) throws Exception
    {
        this.serveur = new ServerSocket(port);
        // pour les tests
        int delai = 3000 + new Random().nextInt(5000);
        this.serveur.setSoTimeout(delai); // delai d'attente avant la panne fatale

        this.el = el;
    }

    public ExperimentalTCPServer(int port) throws Exception
    {
        this(port, null);
    }

    public void start()
    {
        this.local = new Thread(this);
        this.local.start();
    }

    public void stop()
    {
        this.local.interrupt();
    }

    public ServerSocket getServeurSocket()
    {
        return serveur;
    }

    public synchronized void addExceptionListener(ExceptionListener el)
    {
        this.el = el;
    }

    public synchronized ExceptionListener getExceptionListener()
    {
        return this.el;
    }

    public void run()
    {
        try {
            this.top = System.currentTimeMillis();
            while (!local.isInterrupted()) {
                Socket socket = serveur.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object obj = ois.readObject();
                // traitement

                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(obj.toString());
                // retour au client
                socket.close();
            }
        } catch (SocketTimeoutException se) {
            if (!local.isInterrupted()) {
                // simulation d'une panne ici...
                //se.printStackTrace();
                if (getExceptionListener() != null)
                    getExceptionListener().onException(se.getCause());
            }
        } catch (Exception e) {
        } finally {
            this.timeActive = Math.max(System.currentTimeMillis() - this.top, timeActive);
        }
    }

    public int port()
    {
        return this.serveur.getLocalPort();
    }

    public long maxActiveTime()
    {
        return timeActive;
    }
}
