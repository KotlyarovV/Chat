package accounts;
import chat.Message;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    public List<Message> getGettedMessagesMessages () {
        return gettedMessages;
    }

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="from_id")
    public List<Message> sendedMessages;

    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="to_id")
    public List<Message> gettedMessages;

    public UserProfile(String login, String pass) {
        this.login = login;
        this.password = pass;
    }

    public UserProfile(String login) {
        this.login = login;
        this.password = login;
    }

    @Transient
    public boolean online = false;

    public UserProfile() {};

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}