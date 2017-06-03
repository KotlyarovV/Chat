package accounts;
import chat.Message;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table (name = "users")
public class UserProfile {

    @Id
    @Column(name = "id")
    public int id;

    @Column(name = "login")
    public String login;

    @Column(name = "password")
    public String password;

    public List<Message> getSendedMessages () {
        return sendedMessages;
    }

    public List<Message> getGettedMessages () {
        return gettedMessages;
    }

    @Transient
    public List<Message> sendedMessages;

    @Transient
    public List<Message> gettedMessages;

    public UserProfile(String login, String pass) {
        this();
        this.login = login;
        this.password = pass;
    }

    public UserProfile(String login) {
        this();
        this.login = login;
        this.password = login;
    }

    @Transient
    public boolean online = false;

    public UserProfile() {
        sendedMessages = new ArrayList<>();
        gettedMessages = new ArrayList<>();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (o == null || !(o instanceof UserProfile)) return false;
        UserProfile userProfile = (UserProfile) o;
        return this.login.equals(userProfile.login);
    }
}