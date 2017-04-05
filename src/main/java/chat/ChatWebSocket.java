package chat;

import accounts.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@SuppressWarnings("UnusedDeclaration")
@WebSocket
public class ChatWebSocket {
    private ChatService chatService;
    private Session session;
    public UserProfile userProfile;
    public String link;

    public ChatWebSocket(ChatService chatService, UserProfile userProfile, String link) {
        this.chatService = chatService;
        this.userProfile = userProfile;
        this.link = link;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        userProfile.online = true;
        chatService.add(this);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        Message message = new Message(userProfile, data);
        chatService.sendMessage(message, this);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        userProfile.online = false;
        chatService.remove(this);
    }

    public void sendString(String data) {
        try {
            session.getRemote().sendString(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

