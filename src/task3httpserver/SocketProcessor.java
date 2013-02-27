package task3httpserver;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 18.02.13
 * Time: 12:58
 * To change this template use File | Settings | File Templates.
 */
public class SocketProcessor implements Runnable {
    private Socket s;
    private InputStream is;
    private OutputStream os;
    private String homeDirectory;

    public SocketProcessor(Socket s, String hd) throws IOException {
        this.homeDirectory = hd;
        this.s = s;
        this.is = s.getInputStream();
        this.os = s.getOutputStream();
    }

    @Override
    public void run() {
        try {
            String request = readInputHeaders();
            String met =  getMethod(request);
            if (met.equals("GET")){
                String url = getURIFromHeader(request);
                getResponse(url);
            }else if (met.equals("HEAD")){
                String hed = "";
                hed = request.substring(met.length(), request.length());
                os.write(hed.getBytes());
                os.flush();
            }else {
                System.out.println("Неизвестная команда.");
            }


        }catch (Throwable t) {
               t.printStackTrace();
        }finally {
            try {
                s.close();
            } catch (Throwable t) {
                System.out.println("Не удается закрыть.");
            }
        }
        System.err.println("Обработка клиента окончена.");
    }

    private String readInputHeaders() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String request = br.readLine();
        request = request.trim();
        return request;
    }

    private String getMethod (String header){
        String[] ss = header.split(" ");
        String method = ss[0];
        return method;
    }

    private String getURIFromHeader(String header) throws UnsupportedEncodingException {
        String [] ss = header.split(" ");
        String url = ss[1];
        url = homeDirectory + url;
        url = url.replace('\\','/');
        url = URLDecoder.decode(url, "UTF-8");
        url = url.replaceAll("%20", " ");
        return url;
    }

    private void getResponse(String urlFromHeaders) throws IOException {
        String directory = urlFromHeaders;
        String index = directory + "/index.html";
        File indexHtml = new File(index);
        File f = new File(directory);
        String response;
        //Если файл существует, то работает.
        if (f.exists()){
            //Если это папка то возвращаем html со списком файлов.
            if (f.isDirectory()){
                //Если есть index.html, отдаем его.
                if (indexHtml.exists()){
                    InputStream in = new FileInputStream(indexHtml);
                    response = "HTTP/1.1 200 OK\r\n" +
                            "Server: YarServer/2009-09-09\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Connection: close\r\n\r\n";
                    os.write(response.getBytes());
                    int count = 0;
                    byte[] buffer = new byte[1024];
                    while  ( (count = in.read(buffer)) != -1){
                        os.write(buffer, 0, count);
                    }

                }
                //Если нет index.html, то создаем ответ.
                else {
                    response = "HTTP/1.1 200 OK\r\n" +
                            "Server: YarServer/2009-09-09\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Connection: close\r\n\r\n" +
                            generateHtml(f);
                    os.write(response.getBytes());

                }

            //Если это файл, то нужно вернуть соответствующий ContentType и передать файл.
            }else {
                InputStream in = new FileInputStream(f);
                response = "HTTP/1.1 200 OK\r\n" +
                        "Server: YarServer/2009-09-09\r\n" +
                        "Content-Type:" + new MimetypesFileTypeMap().getContentType(f) + "\r\n" +
                        "Content-Length: " + f.length() + "\r\n" +
                        "Connection: close\r\n\r\n";
                os.write(response.getBytes());
                int count = 0;
                byte[] buffer = new byte[1024];
                while  ( (count = in.read(buffer)) != -1){
                    //b = (byte) count;
                    os.write(buffer, 0, count);
                }
            }
        //Если файла не существует, то вернуть 404 ошбику.
        }else {
            response = "HTTP/1.1 404\r\n\r\n";
            os.write(response.getBytes());
        }
        os.flush();
    }

    private String generateHtml(File file) throws IOException {
        File[] listFiles = file.listFiles();
        //Упорядочить по имени
        Arrays.sort(listFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory()) {
                    if (!o2.isDirectory()) {
                        return -1;
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                } else {
                    if (!o2.isDirectory()) {
                        return o1.getName().compareTo(o2.getName());
                    } else {
                        return 1;
                    }
                }
            }
        });

        //Сформировать HTML
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append('\n');
        sb.append("<body>");
        sb.append('\n');

        sb.append("<a href =" + file.getAbsolutePath().substring(homeDirectory.length()).replace('\\', '/').replaceAll(" ", "%20") + "/..>..</a>");
        sb.append("</br>");
        sb.append('\n');

        for (File f: listFiles){
            String href;
            href = f.getAbsolutePath().substring(homeDirectory.length()).replace('\\','/');
            //href = URLEncoder.encode(href, "UTF-8");
            href = href.replaceAll(" ", "%20");

            sb.append("<a href = " + href + ">" + f.getName() + "</a>");
            sb.append("</br>");
            sb.append('\n');
        }

        sb.append("</body>");
        sb.append('\n');
        sb.append("</html>");

        return sb.toString();
    }
}
