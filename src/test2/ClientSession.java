package test2;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 19.02.13
 * Time: 1:10
 * To change this template use File | Settings | File Templates.
 */

/**
 * Обрабатывает запрос клиента.
 */
public class ClientSession implements Runnable {

    private Socket socket;
    private InputStream in = null;
    private OutputStream out = null;

    private static final String DEFAULT_FILES_DIR = "C:\\Test";

    @Override
    public void run() {
        try {
            String header = readHeader();                        //Получаем заголовок сообщения от клиента.
            System.out.println(header + "\n");
            String url = getURIFromHeader(header);              //Получаем из заголовка указатель на интересующий ресурс
            System.out.println("Resource: " + url + "\n");
            int code = send(url);                               //Отправляем содержимое ресурса клиенту
            System.out.println("Result code: " + code + "\n");
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        initialize();
    }

    private void initialize() throws IOException {
        in = socket.getInputStream();                           //Получаем поток ввода, в который помещаются сообщения от клиента
        out = socket.getOutputStream();                         //Получаем поток вывода, для отправки сообщений клиенту */
    }

    /**
     * Считывает заголовок сообщения от клиента.
     *
     * @return строка с заголовком сообщения от клиента.
     * @throws IOException
     */
    private String readHeader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String ln = null;
        while (true) {
            ln = reader.readLine();
            if (ln == null || ln.isEmpty()) {
                break;
            }
            builder.append(ln + System.getProperty("line.separator"));
        }
        return builder.toString();
    }

    /**
     * Вытаскивает идентификатор запрашиваемого ресурса из заголовка сообщения от
     * клиента.
     *
     * @param header
     *           заголовок сообщения от клиента.
     * @return идентификатор ресурса.
     */
    private String getURIFromHeader(String header) {
        int from = header.indexOf(" ") + 1;
        int to = header.indexOf(" ", from);
        String uri = header.substring(from, to);
        int paramIndex = uri.indexOf("?");
        if (paramIndex != -1) {
            uri = uri.substring(0, paramIndex);
        }
        return DEFAULT_FILES_DIR + uri;
    }

    /**
     * Отправляет ответ клиенту. В качестве ответа отправляется http заголовок и
     * содержимое указанного ресурса. Если ресурс не указан, отправляется
     * перечень доступных ресурсов.
     *
     * @param url
     *           идентификатор запрашиваемого ресурса.
     * @return код ответа. 200 - если ресурс был найден, 404 - если нет.
     * @throws IOException
     */
    private int send(String url) throws IOException {
        InputStream strm = HttpServer.class.getResourceAsStream(url);
        int code = (strm != null) ? 200 : 404;
        String header = getHeader(code);
        PrintStream answer = new PrintStream(out, true, "UTF-8");
        answer.print(header);
        if (code == 200) {
            int count = 0;
            byte[] buffer = new byte[1024];
            while((count = strm.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            strm.close();
        }
        return code;
    }

    /**
     * Возвращает http заголовок ответа.
     *
     * @param code
     *           код результата отправки.
     * @return http заголовок ответа.
     */
    private String getHeader(int code) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("HTTP/1.1 " + code + " " + getAnswer(code) + "\n");
        buffer.append("Date: " + new Date().toGMTString() + "\n");
        buffer.append("Accept-Ranges: none\n");
        buffer.append("\n");
        return buffer.toString();
    }

    /**
     * Возвращает комментарий к коду результата отправки.
     *
     * @param code
     *           код результата отправки.
     * @return комментарий к коду результата отправки.
     */
    private String getAnswer(int code) {
        switch (code) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            default:
                return "Internal Server Error";
        }
    }
}
