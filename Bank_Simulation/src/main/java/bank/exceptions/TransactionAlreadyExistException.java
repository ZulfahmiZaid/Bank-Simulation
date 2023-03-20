package bank.exceptions;

/**
 * Exception für schon existernde Transaction
 */
public class TransactionAlreadyExistException extends Exception{
    /**
     * Konstruktur, mit dem man die Message intialisieren kann
     * @param message initial Wert für Attribute message
     */
    public TransactionAlreadyExistException (String message){
        super(message);
    }
}
