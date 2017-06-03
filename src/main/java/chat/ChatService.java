package chat;

import accounts.AccountService;
import accounts.UserProfile;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatService {

    private ConcurrentHashMap<String, ChatWebSocket> webSockets;
    private ConcurrentHashMap<String,ConcurrentHashMap<String, ChatWebSocket>> webSocketsKeyWords;
    private AccountService accountService;

    public ChatService(AccountService accountService) {
        this.accountService = accountService;
        this.webSockets = new ConcurrentHashMap<>();
        this.webSocketsKeyWords = new ConcurrentHashMap<>();
    }

    private boolean overAllChatCondition (ChatWebSocket chatWebSocket, ChatWebSocket user) {
        return (chatWebSocket.link == null && user.link == null &&
                chatWebSocket.toUserProfile == null && user.toUserProfile == null);
    }

    private boolean privateOnline (String login, String loginTo) {
        return (webSockets.containsKey(loginTo) && webSockets.get(loginTo).toUserProfile.login.equals(login)) ;
    }

    public void sendMessage(Message message, ChatWebSocket chatWebSocket) {

        String login = chatWebSocket.userProfile.getLogin();
        String messageString = message.from.login + " : " + message.value;
        if (chatWebSocket.toUserProfile != null) {

            String loginTo = chatWebSocket.toUserProfile.getLogin();
            try {
                chatWebSocket.sendString(messageString);
                if (privateOnline(login, loginTo)) webSockets.get(loginTo).sendString(messageString);
                else accountService.addMessage(message);

            } catch (Exception e) { System.out.println(e.getMessage());}
            return;
        }

        for (ChatWebSocket user : webSockets.values()) {
            try {
                if (overAllChatCondition(chatWebSocket, user) ||
                        (user.link != null && user.link.equals(chatWebSocket.link))) {
                    user.sendString(messageString);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void loadGettedMessages (UserProfile userProfileTo, UserProfile userProfileFrom) {
        accountService.loadGettedMessages(userProfileTo,  userProfileFrom);
    }

    public void deleteMessage (Message message) {
        accountService.deleteMessage(message);
    }

    public void add(ChatWebSocket webSocket) {
        webSockets.put(webSocket.userProfile.login, webSocket);
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket.userProfile.login);
    }

}