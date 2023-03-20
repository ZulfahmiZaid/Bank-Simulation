package bank;
import bank.exceptions.*;
/**
 * Transfer ist eine Klasse, die Üerweisungen in einer Bank darstellen
 */
public class Transfer extends Transaction { // die Überweisungen in einer Bank darstellen
   private String sender; // der Sender der Geldmenge
   private String recipient; // der Empfänger der Geldmenge

   //

   /**
    * 1.Konstruktor, der die Attribute date, amount und description initialisieren.
    * @param input_date initial Wert von date
    * @param input_amount initial Wert von amount
    * @param input_description initial Wert von description
    */
   public Transfer(String input_date, double input_amount, String input_description) throws  TransactionAttributeException{
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
   public Transfer(String input_date, double input_amount, String input_description, String input_sender, String input_recipient)
   throws  TransactionAttributeException{
      this(input_date,input_amount,input_description);

      this.sender = input_sender;
      this.recipient = input_recipient;
   }

   /**
    * Copy-Konstruktor, mit dem man die Objekte des Gleichen Typs kopieren kann
    * @param transfer_copy zukopierendes Objekt von der Klasse Transfer
    */
   public Transfer(Transfer transfer_copy) throws  TransactionAttributeException{
      super(transfer_copy);

      this.sender= transfer_copy.getSender();
      this.recipient= transfer_copy.getRecipient();
   }

   /**
    * Getter für Attribute sender
    * @return aktueller Wert von sender
    */
   public String getSender(){
      return sender;
   }

   /**
    * Setter für die Atrribute sender
    * @param input_sender zugesetzender Wert
    */
   public void setSender(String input_sender){
      sender = input_sender;
   }

   /**
    * Getter für die Attribute recipient
    * @return aktuelle Wert von recipient
    */
   public String getRecipient(){
      return recipient;
   }

   /**
    * Setter für die Attribute recipient
    * @param input_recipient zugesetzender Wert
    */
   public void setRecipient(String input_recipient){
      recipient = input_recipient;
   }

   /**
    * Setter für Attribute Amount
    * @param input_amount zugesetzender Wert
    */
   @Override
   public void setAmount(double input_amount) throws TransactionAttributeException{
         if (input_amount >= 0)
            super.setAmount(input_amount);
         else
            throw new TransactionAttributeException("Invalid Amount for Transfer");
   }

   /**
    * neue Implementation von calculate()
    * @return gerechnete Amount
    */
   @Override
   public double calculate(){
      return this.getAmount();
   }


   /**
    * neue toString Methode, gibt das Objekt in einem String zurück
    * @return Objekt als String
    */
   @Override
   public String toString(){

      //dieses Objekt wird im Detail ausgegeben
      return (super.toString()  + "\n" +
              "Sender: " + this.getSender() + "\n" + "Recipient: " + this.getRecipient() + "\n");
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
                 this.sender.equals(((Transfer) other).getSender()) &&
                 this.recipient.equals (((Transfer) other).recipient)
                 );
      }
      return false;
   }

}
