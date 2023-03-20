package bank;
import bank.exceptions.*;

/**
 * Payment ist eine Klasse, die die Ein- und Auszahlungen in einer Bank repräsentieren
 */
public class Payment extends Transaction {

    private double incomingInterest; // die Zinsen im Prozent (0-1%), die bei einer Einzahlung anfallen
    private double outgoingInterest; // die Zinsen im Prozent (0-1%), die bei einer Auszahlung anfallen

    /**
     * 1.Konstruktor, der die Attribute date, amount und description initialisieren.
     * @param input_date initial Wert von date
     * @param input_amount initial Wert von amount
     * @param input_description initial Wert von description
     */
    public Payment(String input_date, double input_amount, String input_description) throws  TransactionAttributeException{
       super(input_date,input_amount,input_description);
    }

    /**
     * 2.Konstruktor, der die Attribute date, amount, description, incomingInterest und outgoingInterest
     * durch Wiederverwendung von 1.Konstruktor initialisieren
     * @param input_date initial Wert von date
     * @param input_amount initial Wert von amount
     * @param input_description initial Wert von description
     * @param input_incomingInterest inital Wert von incomingInterest
     * @param input_outgoingInterest inital Wert von outgoingInterest
     */
    public Payment(String input_date, double input_amount, String input_description,double input_incomingInterest, double input_outgoingInterest) throws TransactionAttributeException {
        this(input_date, input_amount, input_description); // Wiederverwendung

        this.setIncomingInterest(input_incomingInterest);
        this.setOutgoingInterest(input_outgoingInterest);
    }

    /**
     * Copy-Konstruktor, mit dem man die Objekte des Gleichen Typs kopieren kann
     * @param payment_copy zukopierendes Objekt von Klasse Payment
     */
    public Payment(Payment payment_copy) throws TransactionAttributeException{
        super(payment_copy);

        this.incomingInterest= payment_copy.getIncomingInterest();
        this.outgoingInterest= payment_copy.getOutgoingInterest();
    }

    /**
     * Getter für die Attribute incomingInterest
     * @return aktueller Wert von incomingInterest
     */
    public double getIncomingInterest(){
        return incomingInterest;
    }

    /**
     * Setter für die Attribute incomingInterest
     * @param input_incomingInterest zugesetzenden Wert
     */
    public void setIncomingInterest (double input_incomingInterest) throws TransactionAttributeException {
            if (input_incomingInterest >= 0 && input_incomingInterest <= 1) { // Wertebereich überprüfen
                this.incomingInterest = input_incomingInterest;
            } else
                throw new TransactionAttributeException("Invalid Range for Incoming Interest");
    }

    /**
     *  Getter für die Attribute incomingInterest
     *  @return aktueller Wert von incomingInterest
     */
    public double getOutgoingInterest() { return outgoingInterest;}


    /**
     * Setter füt die Attribute outgoingInterest
     * @param input_outgoingInterest zugesetzender Wert
     */
    public void setOutgoingInterest (double input_outgoingInterest) throws TransactionAttributeException{
            if (input_outgoingInterest >= 0 && input_outgoingInterest <= 1) { // Wertebereich überprüfen
                this.outgoingInterest = input_outgoingInterest;
            } else
                throw new TransactionAttributeException("Invalid Range for Outgoing Interest");
    }

    /**
     * neue Implimentation von calculate()
     * @return gerechneter Wert von calculate()
     */
    @Override
    public double calculate(){

        double result = 0.0;

        if(this.getAmount() >= 0){
            result = this.getAmount() - (this.getAmount() * getIncomingInterest());
        }
        else{
            result = this.getAmount() + (this.getAmount() * getOutgoingInterest());
        }

        return result;
    }


    /**
     * Inhalte von Objekt als String darstellen
     * @return Attribute des Objekts als String
     */
    public String toString(){
       return (super.toString() + "\n" +
               "Incoming Interest: " + this.getIncomingInterest() + "%" + "\n" +
               "Outgoing Interest: " + this.getOutgoingInterest() + "%" + "\n");
    }

    /**
     * die Methode, die die Gleichheit des Objekts mit andere Objekten inhaltlich überprüft
     * @param other zuvergleichenes Objekt
     * @return gleich bzw. ungleich mit bool Werte
     */
    @Override
    public boolean equals(Object other){
        if(other != null && other.getClass() == this.getClass()){
            return (super.equals(other) &&
                    this.incomingInterest == ((Payment) other).getIncomingInterest() &&
                    this.outgoingInterest == ((Payment) other).getOutgoingInterest()
            );
        }
        return false;
    }
}
