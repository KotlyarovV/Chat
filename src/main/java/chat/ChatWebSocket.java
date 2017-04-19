package chat;

import accounts.UserProfile;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.Iterator;

@SuppressWarnings("UnusedDeclaration")
@WebSocket
public class ChatWebSocket {
    private ChatService chatService;
    private Session session;
    public UserProfile userProfile;
    public UserProfile toUserProfile;
    public String link;

    public ChatWebSocket(ChatService chatService, UserProfile userProfile,
                         String link, UserProfile toUserProfile) {
        this.chatService = chatService;
        this.userProfile = userProfile;
        this.link = link;
        this.toUserProfile = toUserProfile;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        userProfile.online = true;
        chatService.add(this);
        this.session = session;

        Iterator<Message> iterator = userProfile.gettedMessages.iterator();

        while (iterator.hasNext()) {
            Message message = iterator.next();
            sendString(message.from.login + " : " + message.value);
            chatService.deleteMessage(message);
            iterator.remove();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        Message message;
        if (toUserProfile == null) message = new Message(userProfile, data);
        else message = new Message(userProfile, toUserProfile, data);
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
