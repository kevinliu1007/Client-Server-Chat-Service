package networking.server;

/**
 * <h1>Server Thread Module</h1>
 * Class for the thread for each client.
 */
public class ServerThread extends Thread {

    /** Server for the thread. */
    private Server server;

    /**
     * Constructor for ServerThread.
     *
     * @param socket    server socket
     */
    public ServerThread(Server socket) {
        server = socket;
    }

    @Override
    public void run() {
        server.sendMessage();
    }
}
