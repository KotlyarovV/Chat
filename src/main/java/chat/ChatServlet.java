package chat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import accounts.AccountService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
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
        this.chatService = new ChatService();
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

        Cookie[] ck=request.getCookies();
        String login = ck[0].getValue();
        String password = ck[1].getValue();

        boolean checking = accountService.checkingUser(login, password);

        if (checking) {

            Map<String, Object> pageVariables = new HashMap<String, Object>();
            pageVariables.put("login", login);

            resp.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            resp.getWriter().println("You are not registered!!!");
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((request, response) ->
        {
            List<HttpCookie> ck = request.getCookies();
            String login = ck.get(0).getValue();
            String link = null;

            if (request.getParameterMap().containsKey("link"))
                link = request.getParameterMap().get("link").get(0);

            return new ChatWebSocket(chatService, login, link);
        });
    }
}
