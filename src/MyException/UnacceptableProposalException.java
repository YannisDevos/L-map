package MyException;

public class UnacceptableProposalException extends Exception {
    public UnacceptableProposalException(String msg){
        super(msg);
    }
}