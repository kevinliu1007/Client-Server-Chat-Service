package networking.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * <h1>Chat Client Module</h1>
 */
public class ChatClient {

    /** Socket for Client. */
    private Socket clients;

    /** Input Stream. */
    private DataInputStream in;

    /** Output Stream. */
    private DataOutputStream out;

    /** User list. */
    private String users;

    /**
     * Setter for client.
     *
     * @param clients   client
     */
    public void setClients(Socket clients) {
        this.clients = clients;
    }

    /**
     * Setter for input stream.
     *
     * @param in    input stream
     */
    public void setIn(DataInputStream in) {
        this.in = in;
    }

    /**
     * Setter for output stream.
     *
     * @param out   output stream
     */
    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    /**
     * Setter for username
     *
     * @param users username
     */
    public void setUsers(String users) {
        this.users = users;
    }

    /**
     * Getter for input stream.
     *
     * @return  input stream
     */
    public DataInputStream getIn() {
        return in;
    }

    /**
     * Getter for output stream.
     *
     * @return  output stream
     */
    public DataOutputStream getOut() {
        return out;
    }

    /**
     * Getter for username.
     *
     * @return  username
     */
    public String getUsers() {
        return users;
    }
}
