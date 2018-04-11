import networking.server.Server;
import networking.server.ServerThread;
import service.RSA;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <h1>Server Orchestrator</h1>
 * Starting the server side for the chat application.
 */
public class StartingServer {

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Server server = new Server();
        RSA encryption = new RSA();
        System.out.println(InetAddress.getLocalHost().toString());

        while (true) {
            Socket client = server.clientSocket(encryption.getE(), encryption.getD(), encryption.getN());

            if (client != null) {
                System.out.println("Connected to a client.");
                ServerThread thread =  new ServerThread(server);
                thread.start();
            }
        }
    }
}
