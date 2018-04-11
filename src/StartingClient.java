import service.graphics.ChatBox;

/**
 * <h1>Client Orchestrator</h1>
 * Starting the client side of the chat application.
 */
public class StartingClient {
    public static void main(String[] args) {
        ChatBox chat = new ChatBox();
        chat.chatInit();
    }
}
