package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vitaly on 07.05.17.
 */
public class AllUsersServlet extends HttpServlet {

    private AccountService accountService;
    public AllUsersServlet (AccountService accountService) {
        this.accountService = accountService;
    }

    private String unregistered = "<p>You are not registered!</p>";

    private String greeting (String login) {
        String page = "<h1>Hello, " + login + "!</h1>";
        for (String userProfile : accountService.getLoginToProfile().keySet()) {
            if (!userProfile.equals(login)) {
                page = page + "<a href=\"chat?name=" + userProfile +"\">"+ userProfile +"</a><br>";
            }
        }
        return page;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        if (!request.getRequestURI().equals("/people")) {
            FileSender.sendFile(request, response, request.getPathInfo());
            return;
        }

        StringBuilder login = new StringBuilder();
        boolean checking = accountService.checkingUser(request.getCookies(), login);

        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = new HashMap<String, Object>();
        pageVariables.put("page", checking ? greeting(login.toString()) : unregistered);

        response.getWriter().println(PageGenerator.instance().getPage("page.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost (HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {

    }
}
