package MyException;

public class DataInvalidException extends Exception {
    public DataInvalidException(String msg){
        super(msg);
    }
}