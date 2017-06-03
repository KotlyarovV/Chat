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
import java.util.List;
import java.util.Map;

/**
 * Created by vitaly on 07.05.17.
 */
public class AllUsersServlet extends HttpServlet {

    private AccountService accountService;
    public AllUsersServlet (AccountService accountService) {
        this.accountService = accountService;
    }

    private String greeting (String login) {
        String page = "<h1>Hello, " + login + "!</h1>";
        UserProfile user = accountService.getUser(login);
        List<Integer> ids = accountService.idsWrittenUsers(user);
        for (String userProfile : accountService.getLoginToProfile().keySet()) {
            if (!userProfile.equals(login)) {
                String message = (ids.contains(accountService.getUser(userProfile).id)) ? " + " : "";
                page = page + "<a href=\"chat?name=" + userProfile +"\">"+ userProfile+ message +"</a><br>";
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

        Map<String, Object> pageVariables = new HashMap<String, Object>();

        StringBuilder login = new StringBuilder();
        boolean checking = accountService.checkingUser(request.getCookies(), login);

        if (checking) pageVariables.put("page",  greeting(login.toString()));


        String page = checking ?
                PageGenerator.instance().getPage("page.html", pageVariables) :
                PageGenerator.instance().getPage("pageNotRegistered.html", null);

        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(page);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost (HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {
    }
}
