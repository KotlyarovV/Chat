package servlets;

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

public class AllRequestsServlet extends HttpServlet {

    public AllRequestsServlet () {}

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {


        if (!request.getPathInfo().equals("/")) {
            FileSender.sendFile(request, response, request.getPathInfo());
        }

        response.setContentType("text/html;charset=utf-8");

        System.out.println(request.getPathInfo());
        System.out.println(request.getRequestURI());
        System.out.println(this.getServletName());

        response.getWriter().println(PageGenerator.instance().getPage("page.html", null));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost (HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {

    }
}