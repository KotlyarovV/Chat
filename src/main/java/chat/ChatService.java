package chat;

import accounts.AccountService;
import accounts.UserProfile;

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

        boolean send = false;
        int i = 0;
        for (ChatWebSocket user : webSockets) {
            i++;
            try {

                if ((chatWebSocket.link == null && user.link == null && chatWebSocket.toUserProfile == null) ||
                        (user.link != null && user.link.equals(chatWebSocket.link)) ||
                        (chatWebSocket.toUserProfile.getLogin().equals(user.userProfile.getLogin())) ||
                        (chatWebSocket.userProfile.getLogin().equals(user.userProfile.getLogin()) )) {
                    user.sendString(message.from.login + " : " + message.value);
                    if (!(chatWebSocket.userProfile.getLogin().equals(user.userProfile.getLogin()) ))
                        send = true;
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (i == webSockets.size() && send == false) {
                accountService.addMessage(message);
            }
        }

    }

    public void loadGettedMessages (UserProfile userProfile) {accountService.loadGettedMessages(userProfile);}

    public void deleteMessage (Message message) {
        accountService.deleteMessage(message);
    }

    public void add(ChatWebSocket webSocket) {
        webSockets.add(webSocket);
    }

    public void remove(ChatWebSocket webSocket) {
        webSockets.remove(webSocket);
    }

}