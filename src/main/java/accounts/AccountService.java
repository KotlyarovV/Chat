package accounts;

import chat.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.*;
import java.util.logging.Logger;

public class AccountService {

    private final Map<String, UserProfile> loginToProfile;
    private final Map<Integer, UserProfile> idToProfile;
    private final Set<String> hashes;
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public AccountService() {
        loginToProfile = new HashMap<>();
        idToProfile = new HashMap<>();
        hashes = new HashSet<>();
        loadUsers();
    }

    private void loadUsers() {
        try {
            Session session = sessionFactory.openSession();
            List<UserProfile> users = (List<UserProfile>)session.createQuery("from accounts.UserProfile").list();

            for (UserProfile userProfile : users) {
                loginToProfile.put(userProfile.getLogin(), userProfile);
                idToProfile.put(userProfile.id, userProfile);
                System.out.println(userProfile.getLogin()+" " +userProfile.getPassword());
                for (Message message : userProfile.getGettedMessagesMessages())
                    System.out.println(message.value + " getted");

                for (Message message : userProfile.getSendedMessages())
                    System.out.println(message.value);
            }

            session.close();
        } finally {}
    }

    public boolean checkingUser (String login, String password) {

            if (!loginToProfile.containsKey(login)) return false;
                UserProfile userProfile = loginToProfile.get(login);
            return userProfile.getPassword().equals(password);
    }

    public UserProfile getUser (String login) {
        return loginToProfile.get(login);
    }

    public boolean checkRegistration (String login) {
        return loginToProfile.containsKey(login);
    }

    public void addNewUser(UserProfile userProfile)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(userProfile);
        session.getTransaction().commit();
        session.close();
        loginToProfile.put(userProfile.getLogin(), userProfile);
    }


    public void addMessage (Message message) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(message);
        session.getTransaction().commit();
        session.close();
        getUserById(message.fromId).getSendedMessages().add(message);
        if (message.toId != null &&  message.toId != 0)
            getUserById(message.toId).getGettedMessagesMessages().add(message);
    }

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public UserProfile getUserById(int sessionId) {
        return idToProfile.get(sessionId);
    }

}
