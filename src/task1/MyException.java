package task1;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 16.02.13
 * Time: 14:22
 * To change this template use File | Settings | File Templates.
 */
public class MyException extends Exception {
    MyException(String s) {
        super(s);
        System.out.println(s);
    }
}
