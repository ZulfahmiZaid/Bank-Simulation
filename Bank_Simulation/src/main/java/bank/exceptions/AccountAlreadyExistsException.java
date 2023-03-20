package bank.exceptions;

/**
 * Exception für schon existerndes Konto
 */
public class AccountAlreadyExistsException extends Exception {
    /**
     * Konstruktur, mit dem man die Message intialisieren kann
     * @param message initial Wert für Attribute message
     */
    public AccountAlreadyExistsException (String message){
        super(message);
    }
}
