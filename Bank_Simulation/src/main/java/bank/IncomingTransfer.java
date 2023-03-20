package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Eine Klasse, der Empfang der Geldmenge darstellen
 */
public class IncomingTransfer extends Transfer {
    /**
     * 1.Konstruktor, der die Attribute date, amount und description initialisieren.
     * @param input_date initial Wert von date
     * @param input_amount initial Wert von amount
     * @param input_description initial Wert von description
     */
    public IncomingTransfer(String input_date, double input_amount, String input_description) throws TransactionAttributeException {
        super(input_date,input_amount,input_description);
    }
    /**
     * 2.Konstruktor, der die Attribute date, amount, description, incomingInterest und outgoingInterest
     * durch Wiederverwendung von 1.Konstruktor initialisieren
     * @param input_date initial Wert von date
     * @param input_amount initial Wert von amount
     * @param input_description initial Wert von description
     * @param input_sender initial Wert von sender
     * @param input_recipient initial Wert von recipient
     */
    public IncomingTransfer(String input_date, double input_amount, String input_description, String input_sender, String input_recipient) throws TransactionAttributeException {
        super(input_date,input_amount,input_description,input_sender,input_recipient);
    }
    /**
     * Copy-Konstruktor, mit dem man die Objekte des Gleichen Typs kopieren kann
     * @param incoming_copy zukopierendes Objekt von der Klasse IncomingTransfer
     */
    public IncomingTransfer(IncomingTransfer incoming_copy) throws TransactionAttributeException  {
        super(incoming_copy);
    }
    /**
     * Bill berechnen
     * @return Wert des Bills
     */
    @Override
    public double calculate(){
        return super.calculate();
    }
}
