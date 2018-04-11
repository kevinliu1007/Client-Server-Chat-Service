package service.graphics;

import networking.client.Client;
import service.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * <h1>Chat Box</h1>
 * Chat box class that contains basic GUI for the chat application and functionality of buttons.
 */
public class ChatBox {

    /** Client object that will do receiving and sending functionality. */
    private Client client;

    /** RSA encryption. */
    private RSA encryption;

    /** Username for current client user. */
    private String username;

    /** Main frame of the application. */
    private static final JFrame frame = new JFrame("Chat Box");

    /** Main text area of the chat display. */
    private static final JTextArea textArea = new JTextArea(38, 58);

    /** Text field for user input. */
    private static final JTextField textField = new JTextField(50);

    /** JButton for sending message. */
    private static final JButton send = new JButton("Send");

    /** Main menu bar of the application. */
    private static final JMenuBar menuBar = new JMenuBar();

    /** Main menu of the menu bar. */
    private static final JMenu menu = new JMenu("Menu");

    /** Menu item that connects to the server. */
    private static final JMenuItem connect = new JMenuItem("Connect");

    /** Menu item that shows the clients connected to the server. */
    private static final JMenuItem clientList = new JMenuItem("Client List");

    /** Menu item that display the about box. */
    private static final JMenuItem about = new JMenuItem("About");

    /** Menu item that exits the application. */
    private static final JMenuItem exit = new JMenuItem("Exit");

    /** Main scroll pane of the application. */
    private static final JScrollPane scrollPane = new JScrollPane(textArea);

    /** South panel of the application. */
    private static final JPanel southPanel = new JPanel();

    /**
     * Helper method for implementing action listener for connect button.
     *
     * @param userNickname  user nickname
     * @param userInput     user input
     */
    private void connectActionHelper(JTextField userNickname, JTextField userInput) {
        username = userNickname.getText();

        new Thread(() -> {
            client = new Client(userInput.getText(), username);
            encryption = client.getRSA();
            JOptionPane.showMessageDialog(null, "Connected");

            new Thread(() -> {
                while (true) {
                    displayMessage();
                }
            }).start();
        }).start();
    }

    /**
     * Action listener for connect menu item.
     */
    private void connectAction() {
        JFrame textBox = new JFrame("Connect to server");
        textBox.setSize(300, 130);
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JLabel nickname = new JLabel("Nickname:       ");
        JLabel hostAddress = new JLabel("Host Address: ");
        JTextField userNickname = new JTextField(15);
        JTextField userInput = new JTextField(15);
        nickname.setLabelFor(userNickname);
        hostAddress.setLabelFor(userInput);

        JButton connect = new JButton("Connect");
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectActionHelper(userNickname, userInput);
            }
        });

        controlPanel.add(nickname);
        controlPanel.add(userNickname);
        controlPanel.add(hostAddress);
        controlPanel.add(userInput);
        controlPanel.add(connect);
        textBox.add(controlPanel);
        textBox.setVisible(true);
        textBox.toFront();
    }

    /**
     * Action listener for about menu item.
     */
    private void aboutAction() {
        String msg = "Chat Box Service\n\n" +
                "Author: Kevin Liu, Pierce Zajac, Chris Janowski\n\n" +
                "Basic chat box service that allow user to connect to a common server and chat with each other.\n" +
                "User can click on menu and click connect button. Enter user preferred username and also IP add\n" +
                "ress of the server, then click connect. After receiving connected message. Users within the se\n" +
                "rver and chat freely. To exit the program, go to menu and click exit button.";
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * Initialize menu action listener for connect and exit.
     */
    private void menuAction() {
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectAction();
            }
        });

        clientList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    String clientList = client.receiveList();
                    JOptionPane.showMessageDialog(null, clientList);
                }).start();
            }
        });

        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutAction();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendExit(username);
                client.closeClient();
                System.exit(0);
            }
        });
    }

    /**
     * Initialize menu and menu items of the application.
     */
    private void menuInit() {
        menuAction();
        menu.add(connect);
        menu.add(clientList);
        menu.add(about);
        menu.add(exit);
        menuBar.add(menu);
    }

    /**
     * Initialize send button and added its action listener.
     */
    private void buttonInit() {
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String userInput = textField.getText();
                    String message = new String(username + ": " + userInput + "\n");
                    client.sendMessage(encryption.encrypt(message.getBytes()));

                textField.setText("");
            }
        });
    }

    /**
     * Initialize the main panel and south panel for the application.
     */
    private void panelInit() {
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        buttonInit();

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        southPanel.add(textField);
        southPanel.add(send);
    }

    /**
     * Initialize the main frame for the application.
     */
    private void frameInit() {
        frame.setJMenuBar(menuBar);
        frame.add(scrollPane);
        frame.add(southPanel, BorderLayout.SOUTH);
        frame.setSize(700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.sendExit(username);
                    client.closeClient();
                    System.exit(0);
                } catch (Exception ex) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Method for listening to the server and decrypt message sent from the server.
     * The decrypted message will then be displayed onto the text area.
     */
    private void displayMessage() {
        try {
            byte[] encryptedMessage = client.receive();

            if (encryptedMessage != null) {
                byte[] decryptedMessage = encryption.decrypt(encryptedMessage);
                String decryptedString = new String(decryptedMessage);
                textArea.append(decryptedString);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Initialize the chat service.
     */
    public void chatInit() {
        menuInit();
        panelInit();
        frameInit();
    }
}
