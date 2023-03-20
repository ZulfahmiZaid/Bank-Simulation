package bank.exceptions;

/**
 * Exception für nicht existernde Transaction
 */
public class TransactionDoesNotExistException extends Exception{
    /**
     * Konstruktur, mit dem man die Message intialisieren kann
     * @param message initial Wert für Attribute message
     */
    public TransactionDoesNotExistException (String message){
        super(message);
    }
}
