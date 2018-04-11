package networking.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * <h1>Server Module</h1>
 * Class for server and its related methods.
 */
public class Server {

    /** Socket for Server. */
    private ServerSocket server;

    /** Array list of clients. */
    private ArrayList<ChatClient> clients = new ArrayList<>();

    /** Numbers of clients connected. */
    private int numClients = 0;

    /**
     * Constructor for a server.
     */
    public Server() {
        try {
            System.out.println("Creating Server...");
            server = new ServerSocket(8765);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to a client.
     *
     * @return  client socket
     */
    public Socket clientSocket(BigInteger e, BigInteger d, BigInteger n) {
        try {
            System.out.println("Connecting to client...");
            Socket currClient = server.accept();

            if (currClient != null) {
                ChatClient currChatClient = new ChatClient();
                currChatClient.setClients(currClient);
                currChatClient.setIn(new DataInputStream(currClient.getInputStream()));
                currChatClient.setOut(new DataOutputStream(currClient.getOutputStream()));

                DataOutputStream currOut = currChatClient.getOut();
                DataInputStream currIn = currChatClient.getIn();
                currOut.writeUTF(e.toString());
                currOut.flush();
                currOut.writeUTF(d.toString());
                currOut.flush();
                currOut.writeUTF(n.toString());
                currOut.flush();

                String username = currIn.readUTF();
                System.out.println(username);
                currChatClient.setUsers(username);
                clients.add(currChatClient);
                numClients++;

                return currClient;
            } else {
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Respond to removing client message.
     *
     * @param msg   message received
     */
    private void removeClient(String msg) {
        String[] parts = msg.split(" ");
        int count = 0;
        System.out.println(parts[0]);

        for (ChatClient items : clients) {
            if (parts[0].contains(items.getUsers())) {
                clients.remove(count);
                numClients--;
                break;
            }
            count++;
        }
    }

    /**
     * Respond to client list message.
     *
     * @param index index of the client that sends the notice
     * @throws IOException
     */
    private void clientList(int index) throws IOException {
        String list = "";

        for (ChatClient items : clients) {
            list += items.getUsers() + "\n";
        }

        System.out.println(list);
        clients.get(index).getOut().writeUTF(list);
        clients.get(index).getOut().flush();
    }

    /**
     * Process if the data received is in UTF form.
     *
     * @param msg   message received
     * @param index index of the current client that send the notice
     *
     * @return  true if processed UTF message, false else wise
     */
    private boolean listenUTF(String msg, int index) {
        try {
            if (msg.contains("exit")) {
                removeClient(msg);
                return true;
            } else if (msg.contains("client list")) {
                clientList(index);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sending the message received from one client, process the information and
     * send the responds to all clients.
     */
    public void sendMessage() {
        try {
            while (true) {
                for (int i = 0; i < numClients; ++i) {
                    DataInputStream input = clients.get(i).getIn();

                    if (input.available() > 0) {
                        byte[] message = new byte[1024];

                        if (input.read(message) > 0) {
                            String msg = new String(message);

                            if (!listenUTF(msg, i)) {
                                for (int j = 0; j < numClients; ++j) {
                                    final int index = j;
                                    new Thread(() -> {
                                        try {
                                            clients.get(index).getOut().write(message, 0, 1024);
                                            clients.get(index).getOut().flush();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }).start();
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
