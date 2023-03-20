package bank.exceptions;

/**
 * Exception für nicht existerndes Konto
 */
public class AccountDoesNotExistException extends Exception{
    /**
     * Konstruktur, mit dem man die Message intialisieren kann
     * @param message initial Wert für Attribute message
     */
    public AccountDoesNotExistException(String message){
        super(message);
    }
}
