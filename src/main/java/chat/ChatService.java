package chat;

import accounts.AccountService;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {

    private Set<ChatWebSocket> webSockets;
    private AccountService accountService;

    public ChatService(AccountService accountService) {
        this.accountService = accountService;
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(Message message, ChatWebSocket chatWebSocket) {

        int i = 0;

        for (ChatWebSocket user : webSockets) {
            i++;
            try {
                if (chatWebSocket.link == null && user.link == null) {
                    user.sendString(message.from.login + " : " + message.value);
                    accountService.addMessage(message);
                }
                else if (user.link != null && user.link.equals(chatWebSocket.link))
                    user.sendString(message.from.login +" : "+ message.value);
                    accountService.addMessage(message);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
    }

}