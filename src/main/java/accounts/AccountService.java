package accounts;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AccountService {

    private final Map<String, UserProfile> loginToProfile;
    private final Map<String, UserProfile> sessionIdToProfile;
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public AccountService() {
        loginToProfile = new HashMap<>();
        sessionIdToProfile = new HashMap<>();
        loadUsers();
    }

    private void loadUsers() {
        try {
            Session session = sessionFactory.openSession();
            List<UserProfile> users = (List<UserProfile>)session.createQuery("from accounts.UserProfile").list();

            for (UserProfile userProfile : users)
                loginToProfile.put(userProfile.getLogin(), userProfile);

            session.close();
        } finally {}
    }

    public boolean checkingUser (String login, String password) {

            if (!loginToProfile.containsKey(login)) return false;
                UserProfile userProfile = loginToProfile.get(login);
            return userProfile.getPassword().equals(password);
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

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public UserProfile getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessionIdToProfile.put(sessionId, userProfile);
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
