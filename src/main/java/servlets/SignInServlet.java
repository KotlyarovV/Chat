package servlets;

import accounts.AccountService;
import accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        boolean checking = accountService.checkingUser(login, password);
        response.setContentType("text/html;charset=utf-8");

        String answer = checking ? "Authorized: " + login : "Unauthorized";
        int status = checking ? 200 : 401;
        response.getWriter().println(answer);
        response.setStatus(status);
    }
}
