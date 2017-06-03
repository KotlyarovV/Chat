package servlets;

import org.eclipse.jetty.http.HttpHeader;
import org.w3c.dom.ranges.Range;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by vitaly on 24.04.17.
 */
public class FileSender {

    private static long rangeGetSkipped(String range, long length) {
        String numbers = range.replaceAll("[^0-9?!\\.\\-]","");
        if (numbers.endsWith("-"))  {
            String number = numbers.substring(0, numbers.length() - 1);
            return Long.parseLong(number);
        }
        return 0;
    }


    public static void sendFile (HttpServletRequest request, HttpServletResponse response , String fileName) throws
            ServletException, IOException {

        ServletOutputStream servletOutputStream = null;
        BufferedInputStream inputStream = null;

        try {
            fileName = "templates" +fileName;
            servletOutputStream = response.getOutputStream();
            File file = new File(fileName);

            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            long l = file.length();
            long skipped = 0;

            FileInputStream input = new FileInputStream(file);
            inputStream = new BufferedInputStream(input);

            if (request.getHeader("Range") != null) {
                skipped = rangeGetSkipped(request.getHeader("Range"), l);
                response.setHeader("Content-Range", "bytes " + skipped + "-" + (l - 1 ) + "/" + l);
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                inputStream.skip(skipped);
            }

            response.setContentLengthLong(l - skipped);
            int readBytes = 0;
            try {
                while ((readBytes = inputStream.read()) != -1) {
                    servletOutputStream.write(readBytes);
                }
            } catch (Throwable time) {}

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
