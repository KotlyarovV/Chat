package imageGetter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by vitaly on 01.06.17.
 */
public class FileWorker {
    public static void writeFile (String fileName, byte[] bytes) throws IOException {
        Files.write(Paths.get("./" + fileName), bytes);
    }


    public static void makeDirectory (String name) {
        if (name.startsWith("/")) name = name.substring(1);
        File dir = new File(name);
        dir.mkdir();
    }

    public static void makeFolder (String folderWay) {
        String[] folders = folderWay.split("/");
        String folderBegin = "" ;
        for (String folder : folders) {
            folderBegin = folderBegin + "/" + folder;
            if (!(new File(folderBegin).exists()))
                makeDirectory(folderBegin);
        }
    }

    public static void makeFile  (String string, byte[] bytes) throws IOException {
        String[] folders = string.split("/");
        if (folders.length == 1)
            writeFile(string, bytes);
        else {
            folders = Arrays.copyOfRange(folders, 0, folders.length - 1);
            StringBuilder builder = new StringBuilder();
            for(String s : folders) {
                builder.append(s + "/");
            }
            String folder = builder.toString();
            makeFolder(folder);
            writeFile(string, bytes);
        }
    }

}
