package servlets;

import templater.PageGenerator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AllRequestsServlet extends HttpServlet {

    public AllRequestsServlet () {}

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(PageGenerator.instance().getPage("page.html", null));
        response.setStatus(HttpServletResponse.SC_OK);
    }

}