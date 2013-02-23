package task2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 16.02.13
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class Html {
    public static void main (String [] arg) throws IOException {
        if (arg.length > 0){
            String directory = arg[0];
            File [] st;

            File f = new File(directory);
            if ((f.exists()) && (f.isDirectory())){
                st = f.listFiles();

                Arrays.sort(st, new Comparator<File>() {
                    @Override
                    public int compare(File o1, File o2) {
                        if (o1.isDirectory()){
                            if(!o2.isDirectory()){
                                return -1;
                            }else{
                                return o1.getName().compareTo(o2.getName());
                            }
                        } else {
                            if (!o2.isDirectory()){
                                return o1.getName().compareTo(o2.getName());
                            }else {
                                return 1;
                            }
                        }
                    }
                });

                PrintHtml index = new PrintHtml(directory);
                index.printHtml(st);
            }else {
                System.out.println("1111");
            }


        }else {
            System.out.println("файл не указан");
        }
    }
}
