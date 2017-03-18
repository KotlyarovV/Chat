package accounts;
import javax.persistence.*;

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

    public UserProfile(String login, String pass) {
        this.login = login;
        this.password = pass;
    }

    public UserProfile(String login) {
        this.login = login;
        this.password = login;
    }

    public UserProfile() {};

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}