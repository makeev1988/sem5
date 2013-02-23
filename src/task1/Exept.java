package task1;

/**
 * Created with IntelliJ IDEA.
 * User: Alexey
 * Date: 16.02.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class Exept implements ExceptionGeneration{

    @Override
    public void generateNullPointerException() {
        Integer a = null;
        a.toString();
    }

    @Override
    public void generateClassCastException() {
        Object ch = new Character('*');
        byte b =(Byte)ch;
    }

    @Override
    public void generateNumberFormatException() {
        Integer intVal = new Integer("#$");
    }

    @Override
    public void generateStackOverflowError() {
        Exept e1 = new Exept();
        e1.generateStackOverflowError();
    }

    @Override
    public void generateOutOfMemoryError() {
        Integer[] i = new Integer[1000000000];
    }

    @Override
    public void generateMyException(String message) throws MyException {
        throw new MyException(message);

    }

    public static void main (String [] arg) throws MyException {
        Exept e = new Exept();

        try{
            e.generateNullPointerException();
        }catch (NullPointerException e1){
            e1.printStackTrace();
        }

        try{
        e.generateClassCastException();
        }catch (ClassCastException e2){
            e2.printStackTrace();
        }

        try{
        e.generateNumberFormatException();
        }catch (NumberFormatException e3){
            e3.printStackTrace();
        }

        try{
            e.generateStackOverflowError();
        }catch (StackOverflowError e4){
            e4.printStackTrace();
        }

        try{
            e.generateOutOfMemoryError();
        }catch (OutOfMemoryError e5){
            e5.printStackTrace();
        }

        try{
            e.generateMyException("111111");
        }catch (MyException e6){
            e6.printStackTrace();
        }

    }
}
