package bank;
import bank.exceptions.*;

import java.io.Serializable;
import java.security.SecureRandomParameters;
import java.util.Comparator;

/**
 * Transaction ist eine abstrakte Klasse, die die Oberklasse von Payment und Transfer ist
 */
public abstract class Transaction implements CalculateBill, Serializable {

    private  String date;
    private double amount;
    private String description;

    /**
     * ein Konstruktor, der alle Attribute mit entsprechenden eingegebenen Werte als Parameter initialisiert
     * @param input_date initial Wert von date
     * @param input_amount initial Wert von amount
     * @param input_description inital Wert von description
     */
    public Transaction(String input_date, double input_amount, String input_description) throws  TransactionAttributeException{
        this.date = input_date;
        this.setAmount(input_amount);
        this.description = input_description;
    }

    /**
     * Copy-Konstruktor, mit dem man die Objekte des Gleichen Typs kopieren kann
     * @param copy zukopierendes Objekt von der Klasse Transaction
     */
    public Transaction(Transaction copy) throws  TransactionAttributeException{
        this.date = copy.getDate();
        this.setAmount(copy.getAmount());
        this.description = copy.getDescription();
    }

    /**
     * Getter für die Attribute date
     * @return aktuelle Wert von date
     */
    public String getDate() {return date;}

    /**
     * Setter für Atrribute date
     * @param input_date zugesetzender Wert
     */
    public void setDate(String input_date){
        this.date = input_date;
    };

    /**
     * Getter für die Attribute amount
     * @return aktuelle Wert von amount
     */
    public double getAmount() {return amount;}

    /**
     * Setter für Attribute amount
     * @param input_amount zugesetzender Wert
     */
    public void setAmount(double input_amount) throws TransactionAttributeException{
        this.amount = input_amount;
    };

    /**
     * Getter für Atrribute description
     * @return aktuelle Wert von description
     */
    public String getDescription() {return description;}

    /**
     * Setter für Attribute description
     * @param input_description zugesetzender Wert
     */
    public void setDescription(String input_description){
        this.description = input_description;
    };

    /**
     * Dieses Objekt als String zurückgeben
     * @return Objekt Inhalte als String
     */
    @Override
    public String toString(){
        return ("Date :" + this.getDate() + "\n" +
                "Description: " + this.getDescription() + "\n" +
                "Amount: " + this.calculate()
        );
    }

    /**
     * die Methode, die die Gleichheit des Objekts mit andere Objekten inhaltlich überprüft
     * @param other zuvergleichenes Objekt
     * @return gleich bzw. ungleich mit bool Werte
     */
    @Override
    public boolean equals(Object other){

        if(other != null && other.getClass() == this.getClass()){
            return (this.getDate().equals(((Transaction) other).getDate()) &&
                    this.getAmount() == ((Transaction) other).getAmount() &&
                    this.getDescription().equals (((Transaction) other).getDescription())
            );
        }

        return false;
    }
    /**
     * das Objekt ausgeben
     */
    public void printObject(){
        System.out.print(this.toString());
    }

}
