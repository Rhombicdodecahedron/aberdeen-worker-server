package exceptions;

public class MyDBException extends Exception {

 
    public MyDBException(String method, String msg) {
        super("ERROR JDBC/JPA in: " + method + " caused by : " + msg);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
