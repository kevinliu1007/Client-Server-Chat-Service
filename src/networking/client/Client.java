package networking.client;

import service.RSA;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;

/**
 * <h1>Client Module</h1>
 * Class for client and related methods.
 */
public class Client {

    /** Socket for Client. */
    private Socket client;

    /** Input Stream. */
    private DataInputStream in;

    /** Output Stream. */
    private DataOutputStream out;

    /**
     * Constructor for client.
     *
     * @param address   address of the host
     */
    public Client(String address, String username) {
        try {
            client = new Socket(address, 8765);
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

            out.writeUTF(username);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send message to server.
     *
     * @param message   message
     */
    public void sendMessage(byte[] message) {
        try {
            System.out.println(message + " " + message.length);
            out.write(message, 0, 1024);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send exit message to server.
     *
     * @param username  username
     */
    public void sendExit(String username) {
        try {
            out.writeUTF(username + " exit");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive initial server message and create an RSA encryption/decryption object.
     *
     * @return  RSA
     */
    public RSA getRSA() {
        String e, d, N;

        try {
            e = in.readUTF();
            d = in.readUTF();
            N = in.readUTF();

            System.out.println(e + "\n" + d + "\n" + N + "\n");

            return new RSA(new BigInteger(e), new BigInteger(d), new BigInteger(N));
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Receive message from server.
     *
     * @return  message
     */
    public byte[] receive() {
        try {
            byte[] message = new byte[1024];

            if (in.available() > 0) {
                if (in.read(message) > 0) {
                    return message;
                } else {
                    return message;
                }
            } else {
                return message;
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String receiveList() {
        try {
            out.writeUTF("client list");
            out.flush();
            String clientList = in.readUTF();
            return clientList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeClient() {
        try {
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
