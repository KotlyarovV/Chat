package main;

import accounts.AccountService;
import accounts.UserProfile;
import imageGetter.ImageGetter;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.*;
import org.hibernate.cfg.AnnotationConfiguration;
import servlets.*;

import javax.servlet.MultipartConfigElement;
import javax.servlet.annotation.MultipartConfig;
import java.io.File;
import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        AccountService accountService = new AccountService();

        AllRequestsServlet allRequestsServlet = new AllRequestsServlet(accountService);
        AllUsersServlet allUsersServlet = new AllUsersServlet(accountService);
        AudioServlet audioServlet = new AudioServlet( accountService);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new chat.ChatServlet(accountService)), "/chat");
        context.addServlet(new ServletHolder(allUsersServlet), "/people");
        context.addServlet(new ServletHolder(audioServlet), "/music");
        context.addServlet(new ServletHolder(new FileServlet()), "/file");
        context.addServlet(new ServletHolder(allRequestsServlet), "/*");


        ServletHolder servletHolder = new ServletHolder(new ImageGetter());
        servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(
                "." + File.separator + "fileSystem",
                1024*1024*50,
                1024*1024*50*5,
                1024*1024*10));
        context.addServlet(servletHolder, "/getimage");



        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        logger.info("Server started");
        server.join();
    }
}