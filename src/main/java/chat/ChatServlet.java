package chat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import accounts.AccountService;
import accounts.UserProfile;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import servlets.FileSender;
import templater.PageGenerator;
import java.io.IOException;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ChatServlet", urlPatterns = {"/chat"})
public class ChatServlet extends WebSocketServlet {

    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final ChatService chatService;
    private AccountService accountService;

    public ChatServlet(AccountService accountService) {
        this.chatService = new ChatService(accountService);
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(request.getRequestURI());

        if (!request.getRequestURI().equals("/chat")) {
            FileSender.sendFile(request, resp, request.getPathInfo());
            return;
        }

        StringBuilder login = new StringBuilder();
        boolean checking = accountService.checkingUser(request.getCookies(), login);

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        if (checking) {

            Map<String, Object> pageVariables = new HashMap<String, Object>();
            pageVariables.put("login", login.toString());
            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
        }
        else resp.getWriter().println("You are not registered!!!");
    }

    @Override
    public void configure(WebSocketServletFactory factory) {


        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((request, response) ->
        {
            List<HttpCookie> ck = request.getCookies();
            String login = "";

            for (HttpCookie cookie : ck)
                if (cookie.getName().equals("login")) login = cookie.getValue();


            UserProfile userProfile = accountService.getUser(login);
            Map <String, List<String>> parameterMap = request.getParameterMap();

            String link = parameterMap.containsKey("link") ? parameterMap.get("link").get(0) : null;
            String name = parameterMap.containsKey("name") ? parameterMap.get("name").get(0) : null;

            UserProfile toUserProfile1 = (name == null) ? null : accountService.getUser(name);

            return new ChatWebSocket(chatService,  userProfile, link, toUserProfile1);
        });
    }
}
