package task2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 16.02.13
 * Time: 16:07
 * To change this template use File | Settings | File Templates.
 */
public class PrintHtml {
    private FileWriter fileHtml;
    String folder;

    PrintHtml(String f) throws IOException {
        folder = f;
        fileHtml = new FileWriter(folder+"//index.html");
    }

    public void printHtml (File[] list) throws IOException {
        fileHtml.append("<html>");
        fileHtml.append('\n');
        fileHtml.append("<body>");
        fileHtml.append('\n');

        fileHtml.append("<a href =" + folder + ">..</a>");
        fileHtml.append("</br>");
        fileHtml.append('\n');

        for (int i = 0; i < list.length; i++){
            fileHtml.append("<a href = " + list[i] + ">"+ list[i].getName() +"</a>");
            fileHtml.append("</br>");
            fileHtml.append('\n');
        }

        fileHtml.append("</body>");
        fileHtml.append('\n');
        fileHtml.append("</html>");

        fileHtml.flush();
        fileHtml.close();

    }

//    public static void main (String [] arg) throws IOException {
//        PrintHtml ph = new PrintHtml("C:\\Test");
//        ph.printHtml();
//    }
}
