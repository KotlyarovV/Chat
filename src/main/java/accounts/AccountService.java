package accounts;

import chat.Message;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.Cookie;
import java.util.*;
import java.util.logging.Logger;

public class AccountService {

    private final Map<String, UserProfile> loginToProfile;
    public Map<String, UserProfile> getLoginToProfile() {
        return loginToProfile;
    }

    private final Map<Integer, UserProfile> idToProfile;
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public AccountService() {
        loginToProfile = new HashMap<>();
        idToProfile = new HashMap<>();
        loadUsers();
    }

    private void loadUsers() {
        try {
            Session session = sessionFactory.openSession();
            List<UserProfile> users = (List<UserProfile>)session.createQuery("from accounts.UserProfile").list();

            users.forEach((userProfile) -> {
                loginToProfile.put(userProfile.getLogin(), userProfile);
                idToProfile.put(userProfile.id, userProfile);
            });

            session.close();
        } finally {}
    }

    public boolean checkingUser (String login, String password) {

        if (!loginToProfile.containsKey(login)) return false;
        UserProfile userProfile = loginToProfile.get(login);
        return userProfile.getPassword().equals(password);
    }


    public boolean checkingUser (Cookie[] cookies, StringBuilder stringBuilder) {
        String login = "";
        String password = "";

        if (cookies == null) return false;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("login")) {
                login = cookie.getValue();
                stringBuilder.append(login);
            }
            if (cookie.getName().equals("password")) password = cookie.getValue();
        }

        return  this.checkingUser(login, password);
    }

    public UserProfile getUser (String login) {
        return loginToProfile.get(login);
    }

    public boolean checkRegistration (String login) {
        return loginToProfile.containsKey(login);
    }

    public void addNewUser(UserProfile userProfile) {
        saveInBase(userProfile);
        loginToProfile.put(userProfile.getLogin(), userProfile);
    }

    public void addMessage (Message message) {
        saveInBase(message);
    }

    public <T> void saveInBase (T value) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.saveOrUpdate(value);
            session.flush();
            session.clear();
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public List<Integer> idsWrittenUsers (UserProfile userProfile) {
        Session session = sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Message.class)
                .setProjection(Projections.distinct(Projections.property("fromId")))
                .add(Restrictions.eq("toId", userProfile.id));
        List<Integer> idsFrom = (List<Integer>) criteria.list();
        session.close();
        return idsFrom;
    }


    public void loadGettedMessages (UserProfile userTo, UserProfile userFrom) {
        try {
            Session session = sessionFactory.openSession();
            List<Message> messages = (List<Message>)session
                    .createQuery(
                            "from chat.Message m where m.toId=" + userTo.id
                        + " and m.fromId=" + userFrom.id).list();
            userTo.gettedMessages.addAll(messages);
            session.close();
        } finally {}
    }


    public void deleteMessage (Message message) {
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(message);
            session.getTransaction().commit();
            session.close();
        } finally {}
    }

    public void deleteUser (UserProfile user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (Message message : user.gettedMessages) {
            session.delete(message);
        }
        for (Message message : user.sendedMessages) {
            session.delete(message);
        }
        loginToProfile.remove(user.getLogin());
        idToProfile.remove(user.id);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }

    public UserProfile getUserByLogin(String login) {
        return loginToProfile.get(login);
    }

    public UserProfile getUserById(int sessionId) {
        return idToProfile.get(sessionId);
    }
}