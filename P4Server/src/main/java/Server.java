
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Consumer;


public class Server {

    private int portNumber;
    public char name ;

    private ConnectionThread connectionThread;

    // will handle the flow of data from server to client
    private Consumer<Serializable> callBack;
    private EvalGameInfo evalGameInfo;

    // Server class
    Server (int portNumber, Consumer<Serializable> callBack)
    {
        this.callBack = callBack;
        this.connectionThread = new ConnectionThread();
        this.connectionThread.setDaemon(true);
        this.portNumber = portNumber;
        this.evalGameInfo = new EvalGameInfo();
    }

    // starts the connection with the port
    public void startConnection ()
    {
        connectionThread.start();
    }

    // sends all information from the server to the client
    public void sendAllInformation(Serializable data) throws Exception
    {
        System.out.println("Server sending all information: " + data);
        for (ClientThread thread : connectionThread.sockets.values())
        {
            //clientCount++;
            thread.output.writeObject(data);
        }
    }

    // sends information from server to client
    public void sendInformation (Serializable data, ClientThread thread) throws Exception
    {
        // prints out what data is about to be sent
        //clientCount++;
        System.out.println("Server sent: " + data);
        // sends the actual data from server to client
        thread.output.writeObject(data);
    }

    // closes the connection
    public void closeConnection () throws Exception
    {
        for (ClientThread thread: connectionThread.sockets.values())
        {
            thread.socket.close();
        }
        connectionThread.server.close();
    }

    // returns the port number
    int getPortNumber()
    {
        return portNumber;
    }

    // updates the list of clients from the server side
    public void updateClientNumberFX ()
    {
        callBack.accept("CLIENT#" + connectionThread.sockets.size());
    }

    // updates the list of clients once a new connection with client is established
    public void updateClientList()
    {
        String list = "UPDATELIST#";
        for (String sock : connectionThread.sockets.keySet())
        {
            list+= sock + "\n";
        }
        callBack.accept(list);
    }

    // counter = threadCount
    // allPreviousClient = previousClients
    class ConnectionThread extends Thread {
        private ServerSocket server;
        private Integer threadCount;
        private HashMap<String, ClientThread> sockets;

        ConnectionThread()
        {
            this.threadCount = 0;
            this.sockets = new HashMap<>();
        }

        public void run()
        {
            try
            {
                this.server = new ServerSocket(getPortNumber());
                while (true)
                {
                    Socket sock = server.accept();
                    String name = "Client " + this.threadCount;

                    ClientThread thread = new ClientThread(sock, name);
                    String previousClients = "CONNECTED#";
                    for (String num : sockets.keySet())
                    {
                        previousClients+= num + "#";
                    }
                    sendInformation(previousClients, thread);

                    threadCount++;
                    this.sockets.put (name, thread);

                    // tells client what their name is
                    sendInformation("NAME#" + name, thread);

                    // updates the client number in the fx
                    updateClientNumberFX();

                    // updates client list in fx
                    updateClientList();

                    thread.start();
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
                callBack.accept("Connection closed");
            }
        }
    }

    public class ClientThread extends Thread
    {
        Socket socket;
        int count;
        int clientNumber;
        Boolean newGame;
        ObjectOutputStream output;
        ObjectInputStream input;
        String category;

        private ClientThread otherClient;
        // keep something here
        private GameInfo game;

        ClientThread (Socket sock, String name)
        {
            this.socket = sock;
            this.clientNumber = Integer.parseInt(name.replace("Client ", ""));
            this.count = clientNumber;
            this.game = new GameInfo(this.count);

            try
            {
                this.socket.setTcpNoDelay(true);
                this.output = new ObjectOutputStream(this.socket.getOutputStream());
                this.input = new ObjectInputStream(this.socket.getInputStream());
                this.output.writeObject("You are client# "+ this.count);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }

        public void run() {
            // start with the run method
            try {
                while (true) {

                    // whatever info we receive, happens here
                    String data = input.readObject().toString(); // #CATEGORY, WHOLE WORD OR LETTER
                    // prints out to terminal
                    System.out.println("Server received: " + data);

                    if (data.isEmpty())
                        Server.this.sendInformation("#NODATA \n", this);
                    else {
                        // received information is about category, then based on category, assign a word
                        if (data.charAt(0) == '#') {
                            category = data;
                            if (data.contains("#CATEGORY1")) {
                                this.game.category1Counter++;
                                this.game.currentCategory = 1;
                                this.game.setNumGuesses(0);
                                this.game.setCorrectWord(game.category1.get(this.game.category1Counter));
                                this.game.currentWord = (game.category1.get(this.game.category1Counter));

                                // SEND INFORMATION TO SERVER UI
                                callBack.accept("Client " + this.clientNumber + " selected CATEGORY1 \n");
                                //callBack.accept("Client " + counter + " selected CATEGORY1 \n");
                                // SEND INFORMATION TO CLIENT
                                //Server.this.sendInformation(this.getName() + " selected CATEGORY1 \n", this);
                                int wordLength = this.game.currentWord.length();
                                Server.this.sendInformation("Word length " + wordLength, this);

                            }
                            if (data.contains("#CATEGORY2")) {
                                this.game.category2Counter++;
                                this.game.currentCategory = 2;
                                this.game.setNumGuesses(0);
                                this.game.setCorrectWord(game.category2.get(this.game.category2Counter));
                                this.game.currentWord=(game.category2.get(this.game.category2Counter));
                                System.out.println("From category 2: " + this.game.currentWord);
                                callBack.accept("Client " + this.clientNumber + " selected CATEGORY2 \n");
                                Server.this.sendInformation(this.getName() + " selected CATEGORY2 \n", this);
                                int wordLength = this.game.currentWord.length();
                                Server.this.sendInformation("Word length " + wordLength, this);

                            }
                            if (data.contains("#CATEGORY3")) {
                                this.game.category3Counter++;
                                this.game.currentCategory = 3;
                                this.game.setNumGuesses(0);
                                this.game.setCorrectWord(game.category3.get(this.game.category3Counter));
                                this.game.currentWord=(game.category3.get(this.game.category3Counter));
                                System.out.println("From category 3: " + this.game.currentWord);
                                callBack.accept("Client " + this.clientNumber + " selected CATEGORY3 \n");
                                Server.this.sendInformation(this.getName() + " selected CATEGORY3 \n", this);
                                int wordLength = this.game.currentWord.length();
                                Server.this.sendInformation("Word length " + wordLength, this);
                            }
                            continue; // so that it can now collect the letter from client
                        }

                        // if the guess is the entire word
                        if (data.length() > 1 && data.charAt(0) != '#' && data.charAt(0) != '!') {
                            this.game.setWordGuess(data);
                            if (evalGameInfo.evalWholeWord(this.game).contains("#CORRECTWORD")) // check if the guessed word is same as current word
                            {
                                game.lives = 3;
                                callBack.accept("#Guessed correct word \n");
                                Server.this.sendInformation("#CORRECTWORD", this);
                                Server.this.sendInformation("#CATEGORY FINISHED:" + this.game.currentCategory, this);
                            } else {
                                callBack.accept("#Guessed wrong word \n");
                                Server.this.sendInformation("#WRONGWORD\n", this);
                            }
                        }

                        // if the guess is a letter
                        if (data.length() == 1) {
                            this.game.setGuessLetter(data.charAt(0));
                            String message = evalGameInfo.evalLetterGuess(this.game);
                            if (message.contains("#CORRECTLETTER: ")) {
                                callBack.accept("#Guessed correct letter \n");
                                //send the index of letter in correct word
                                message += 'p';
                                Server.this.sendInformation(message, this);
                            } else if (message.contains("#CORRECTWORD")) {
                                callBack.accept("#Guessed correct word \n");
                                //send the index of letter in correct word
                                Server.this.sendInformation(message, this);
                                Server.this.sendInformation("#CATEGORY FINISHED:" + this.game.currentCategory, this);
                            } else {
                                callBack.accept("#Guessed wrong letter \n");
                                //send the wrong letter back to client
                                Server.this.sendInformation(message, this);
                            }
                        }

                        if (this.game.lives < 1)
                            Server.this.sendInformation("#LOSTGAME", this);

                        if(!this.game.validCategory.contains(true))
                            Server.this.sendInformation("#WINNER", this);

                        // server removes client from socket if client wants to leave game
                        if (data.contains("!QUIT")) {
                            String name = "Client " + this.clientNumber + " has left the game"; // will be used to display in table view
                            // removes the name from the server
                            Server.this.connectionThread.sockets.remove(this.getName());

                            // sends the information to the client
                            sendInformation(name, this); // sends info to fx
                            // updates the list of clients in the server
                            updateClientList();
                            // lets the client know that the client has been disconnected
                            sendAllInformation("DISCONNECTED#" + this.clientNumber );
                            this.socket.close();
                            break;
                        }

                        // Refresh the game
                        if (data.contains("#REFRESH")) {
                            this.game = new GameInfo(this.clientNumber);
                        }
                    }
                }

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                e.printStackTrace();
                callBack.accept("OOOOPPs...Something wrong with the socket from client: " + this.clientNumber + "....closing down!");
            }
        }
    }
}
