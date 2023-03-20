package bank.exceptions;

/**
 * Exception für Überprüfung der Transactionsattribute
 */
public class TransactionAttributeException extends Exception{
    /**
     * Konstruktur, mit dem man die Message intialisieren kann
     * @param message initial Wert für Attribute message
     */
    public TransactionAttributeException (String message){
        super(message);
    }
}
