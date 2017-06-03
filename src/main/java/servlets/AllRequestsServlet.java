package servlets;

import accounts.AccountService;
import templater.PageGenerator;

import javax.print.attribute.URISyntax;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class AllRequestsServlet extends HttpServlet {

    private AccountService accountService;
    public AllRequestsServlet (AccountService accountService) {
        this.accountService = accountService;
    }

    private String form = "<p>Sign in</p><form action=\"/signin\" method=\"POST\">\n" +
            "<p> Login: <input type=\"text\" name=\"login\"/> </p>\n" +
            "<p>   Password: <input type=\"password\" name=\"password\"/>"+
            " <input type=\"submit\" value=\"Ok\"> </p>\n" +
            "</form><br><form action=\"/signup\"><button type = \"submit\">" +
            " button for registration</button></form>\n";

    private String greeting (String login) {
        return "<h1>Hello, " + login + "!</h1>" +
                "<button onclick=\"window.location.href='/chat'\">Go to chat</button><br><br>" +
                "<p>Key word chat</p><form action=\"/chat\" method=\"GET\">" +
                "<p> Keyword <input type=\"text\" name=\"link\"/> </p>" +
                "<input type=\"submit\" value=\"Ok\"> </p> </form><br>" +
                "<a href=\"people\" lang=\"ru\">Список пользователей</a><br><br>" +
                "<a href=\"file\" lang=\"ru\">Скачать Мирохину песню</a><br><br><br>" +
                "<a href=\"music\" lang=\"ru\">Музыка</a><br><br><br>" +
                "<button onclick=\"anuuthorization()\">Exit</button>";

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {


        if (!request.getRequestURI().equals("/")) {
            FileSender.sendFile(request, response, request.getPathInfo());
            return;
        }

        StringBuilder login = new StringBuilder();
        boolean checking = accountService.checkingUser(request.getCookies(), login);

        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = new HashMap<String, Object>();
        pageVariables.put("page", checking ? greeting(login.toString()) : form);

        response.getWriter().println(PageGenerator.instance().getPage("page.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost (HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {

    }
}