package chat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import accounts.AccountService;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import templater.PageGenerator;
import java.io.IOException;
import java.util.HashMap;
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

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        boolean checking = accountService.checkingUser(login, password);

        System.out.print(checking);

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
        factory.setCreator((req, resp) -> new ChatWebSocket(chatService));
    }
}
