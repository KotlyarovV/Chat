package servlets;

import accounts.AccountService;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vitaly on 08.05.17.
 */
public class AudioServlet extends HttpServlet {
    private AccountService accountService;
    public AudioServlet (AccountService accountService) {
        this.accountService = accountService;
    }

    private String unregistered = "<p>You are not registered!</p>";

    private String musicList (String login, ArrayList<String> files, String folder) {
        String page = "<h1>Hello, " + login + "!</h1>";
        for (String file : files) {
            page = page + "<p>" + file.replaceAll(".mp3", "") + "</p><br>"+
                    "<audio controls preload=\"none\">" +
                    "<source src=\"music"+File.separator + folder +
                    File.separator + file +  "\" type=\"audio/mpeg\">" +
                    "Your browser does not support the audio element." +
                    "</audio><br><br>";
        }
        return page;
    }

    private String foldersList (String login) {
        ArrayList<String> folders = listOfFolders();
        String page = "<h1>Hello, " + login + "!</h1>";
        for (String file : folders) {
            page = page + "<p><br>"+
                    "<a href=\"music?album=" + file + "\">" + file + "</a><br>";
        }
        return page;
    }

    private  ArrayList<String> listOfFolders() {
        String[] files = new File("." + File.separator + "templates" +
                File.separator + "music").list();
        String way = "." + File.separator + "templates" + File.separator + "music" + File.separator;
        ArrayList<String> folders = new ArrayList<>();
        Arrays.stream(files)
                .filter((folder) -> new File(way + folder).isDirectory())
                .forEach(folders::add);
        return folders;
    }

    private ArrayList<String> listOfFilesMp3(String folder) {
        String wayToMp3 = "." + File.separator + "templates" + File.separator +
                            "music" + File.separator+ folder;
        String[] files = new File(wayToMp3).list();

        ArrayList<String> mp3Files = new ArrayList<>();
        Arrays.stream(files).filter((i) -> i.endsWith(".mp3")).forEach(mp3Files::add);
        mp3Files.sort(String::compareTo);
        return mp3Files;
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        if (!request.getRequestURI().equals("/music")) {
            FileSender.sendFile(request, response,  request.getPathInfo());
            return;
        }

        StringBuilder login = new StringBuilder();
        boolean checking = accountService.checkingUser(request.getCookies(), login);

        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> pageVariables = new HashMap<String, Object>();
        String answerPage = (request.getParameter("album") == null) ? foldersList(login.toString()) :
                musicList(login.toString(),
                        listOfFilesMp3(request.getParameter("album")),
                        request.getParameter("album"));
        pageVariables.put("page", checking ? answerPage : unregistered);

        response.getWriter().println(PageGenerator.instance().getPage("page.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost (HttpServletRequest request,
                        HttpServletResponse response) throws ServletException, IOException {
    }

}
