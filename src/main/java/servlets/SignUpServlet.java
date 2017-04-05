package servlets;

import accounts.AccountService;
import accounts.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpServlet extends HttpServlet {

    AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("page_registration.html", null));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (accountService.checkRegistration(login)) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println("You have been already registered!");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        accountService.addNewUser(new UserProfile(login,password));

        response.setContentType("text/html;charset=utf-8");
        response.addCookie(new Cookie("login", login));
        response.addCookie(new Cookie("password", password));

        response.getWriter().println(PageGenerator.instance().getPage("go_to_chat.html", null));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
