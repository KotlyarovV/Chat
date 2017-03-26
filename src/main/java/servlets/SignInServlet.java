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
import java.net.URL;

public class SignInServlet extends HttpServlet {
    AccountService accountService;

    public SignInServlet (AccountService accountService) {
        this.accountService = accountService;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println("Page");
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        URL url = new URL(request.getHeader("referer"));

        boolean checking = accountService.checkingUser(login, password);

        if (checking) {
            response.addCookie(new Cookie("login", login));
            response.addCookie(new Cookie("password", password));
        }

        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect(url.getPath());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
