package task1;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 16.02.13
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
public interface ExceptionGeneration {
    void generateNullPointerException();
    void generateClassCastException();
    void generateNumberFormatException ();
    void generateStackOverflowError();
    void generateOutOfMemoryError();
    void generateMyException (String message) throws MyException;

}
