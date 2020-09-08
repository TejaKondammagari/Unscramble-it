import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {

    private String IPAddress;
    private int portNumber;

    private ConnectionThread connectionThread = new ConnectionThread();
    private Consumer<Serializable> callBack;

    Client (String IPAddress, int portNumber, Consumer <Serializable> callBack)
    {
        this.callBack = callBack;

        connectionThread.setDaemon(true);
        this.IPAddress = IPAddress;
        this.portNumber = portNumber;
    }

    public void startConnection() throws Exception
    {
        connectionThread.start();
    }

    public void send (Serializable data) throws Exception
    {
        System.out.println("Client sent: " + data);
        connectionThread.output.writeObject(data);
    }

    public void closeConnection() throws Exception
    {
        connectionThread.socket.close();
    }

    // returns IP Address
    String getIPAddress()
    {
        return this.IPAddress;
    }

    // returns the port number
    int getPortNumber()
    {
        return this.portNumber;
    }

    class ConnectionThread extends Thread
    {
        private Socket socket;
        private ObjectOutputStream output;

        public void run()
        {
            try {
                Socket socket = new Socket(getIPAddress(), getPortNumber());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                {
                    this.socket = socket;
                    this.output = output;
                    socket.setTcpNoDelay(true);

                    while (true)
                    {
                        Serializable data = (Serializable) input.readObject();
                        System.out.println("Client received: "+ data);
                        callBack.accept(data);
                    }
                }
            }
            catch (Exception e)
            {
                callBack.accept("$ Connection closed\n");
                System.out.println(e);
            }
        }
    }
}
