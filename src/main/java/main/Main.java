package main;

import accounts.AccountService;
import accounts.UserProfile;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        AllRequestsServlet allRequestsServlet = new AllRequestsServlet();
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new chat.ChatServlet(accountService)), "/chat");
        context.addServlet(new ServletHolder(allRequestsServlet), "/*");

        //ResourceHandler resource_handler = new ResourceHandler();
        //resource_handler.setDirectoriesListed(true);
        //resource_handler.setResourceBase("public_html");

        //HandlerList handlers = new HandlerList();
        //handlers.setHandlers(new Handler[]{resource_handler, context});

        Server server = new Server(8080);
        //server.setHandler(handlers);
        server.setHandler(context);

        server.start();
        logger.info("Server started");
        server.join();
    }
}