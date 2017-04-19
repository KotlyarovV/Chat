package servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by vitaly on 08.04.17.
 */
public class FileServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
       // String fileName = (String) request.getParameter("file");
        String fileName = "oxxxy.mp3";

        ServletOutputStream servletOutputStream = null;
        BufferedInputStream inputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            File file = new File(fileName);

            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength((int) file.length());

            FileInputStream input = new FileInputStream(file);
            inputStream = new BufferedInputStream(input);
            int readBytes = 0;
            while ((readBytes = inputStream.read()) != -1)
                servletOutputStream.write(readBytes);
        }   catch (IOException ioe) {
            throw new ServletException(ioe.getMessage());
        } finally {
            if (servletOutputStream != null)
                servletOutputStream.close();
            if (inputStream != null)
                inputStream.close();
        }
    }
}
