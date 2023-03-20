package bank;
import bank.exceptions.*;
import java.util.*;

/**
 * Eine Klasse, die eine private Bank darstellen
 */
public class PrivateBankAlt implements Bank{
    private String name; // Name der privaten Bank repräsentieren
    private double incomingInterest;
    private double outgoingInterest;
    private Map<String, List<Transaction>> accountsToTransaction = new HashMap<>();

    /**
     * Konstruktor, die Attribute name, incomingInterest und outgoingInterest initialisieren
     * @param input_name initial Wert für name
     * @param input_In initial Wert für incomingInterest
     * @param input_Out initial Wert für outgoingInterest
     */
    public PrivateBankAlt (String input_name, double input_In, double input_Out){
        this.name = input_name;
        setIncomingInterest(input_In);
        setOutgoingInterest(input_Out);
    }

    /**
     * Copy-Konstruktur, mit dem man das gleiche Objekt kopieren kann.
     * @param other zukopierendes Objekt
     */
    public PrivateBankAlt (PrivateBankAlt other){
        setName(other.getName());
        setIncomingInterest(other.getIncomingInterest());
        setOutgoingInterest(other.getOutgoingInterest());
    }

    /**
     * Setter für die Attribute name
     * @param input_name zugewiesener Wert für name
     */
    public void setName(String input_name){ this.name = input_name;}

    /**
     * Getter für die Attribute name
     * @return aktuelle Wert für name
     */
    public String getName(){return this.name;}

    /**
     * Setter für die Attribute incomingInterest
     * @param input_incomingInterest zugewiesener Wert für incomingInterest
     */
    public void setIncomingInterest(double input_incomingInterest){
        if (input_incomingInterest >= 0 && input_incomingInterest <= 1) { // Wertebereich überprüfen
            this.incomingInterest = input_incomingInterest;
        } else
            System.out.println("Invalid Range for IncomingInterest (PB)");
    }

    /**
     * Getter für die Attribute incomingInterest
     * @return aktueller Wert für incomingInterest
     */
    public double getIncomingInterest(){return this.incomingInterest;}

    /**
     * Setter für die Attribute outgoingInterest
     * @param input_outgoingInterest zugewiesener Wert für outgoingInterest
     */
    public void setOutgoingInterest(double input_outgoingInterest){
        if (input_outgoingInterest >= 0 && input_outgoingInterest <= 1) { // Wertebereich überprüfen
            this.outgoingInterest = input_outgoingInterest;
        } else
            System.out.println("Invalid Range for OutgoingInterest (PB)");
    }
    /**
     * Getter für die Attribute outgoingInterest
     * @return aktueller Wert für outgoingInterest
     */
    public double getOutgoingInterest(){return this.outgoingInterest;}
    /**
     * Inhalte von Objekt als String darstellen
     * @return Attribute des Objekts als String
     */
    @Override
    public String toString(){
        return ("Name :" + this.getName() + "\n" +
                "IncomingInterest: " + this.getIncomingInterest() + "\n" +
                "OutgoingInterest: " + this.getOutgoingInterest() + "\n"
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
            return (this.getName().equals(((PrivateBank) other).getName()) &&
                    this.getIncomingInterest() == ((PrivateBank) other).getIncomingInterest() &&
                    this.getOutgoingInterest() == ((PrivateBank) other).getOutgoingInterest() );

        }
        return false;
    }

    /**
     * creates an empty account in PrivateBank without any transactions
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if account already exist
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {

        if (accountsToTransaction.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exist");
        else {
            accountsToTransaction.put(account, new ArrayList<>());
        }
    }

    /**
     * create an account with a pre-existing lists of transactions
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException such account already exist
     * @throws TransactionAlreadyExistException duplicate transaction record found
     * @throws TransactionAttributeException if fail to validate the attribute values of a transaction
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {
        if (accountsToTransaction.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exist");

        accountsToTransaction.put(account,new ArrayList<>());

        try {
            for (Transaction collect : transactions) {
                this.addTransaction(account, collect);
            }
        }
        catch (AccountDoesNotExistException e){
            System.out.println(e.getMessage());
        }
    }
    /**
     * adds a Transaction to a desired account
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException duplicate transaction record found
     * @throws AccountDoesNotExistException such account does not exist
     * @throws TransactionAttributeException if fail to validate the attribute values of a transaction
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {

        if (!accountsToTransaction.containsKey(account))
            throw new AccountDoesNotExistException("Account does not exist");

        if (accountsToTransaction.get(account).contains(transaction))
            throw new TransactionAlreadyExistException("Transaction already exist");

        if (transaction instanceof Payment) {
            ((Payment) transaction).setIncomingInterest(this.getIncomingInterest());
            ((Payment) transaction).setOutgoingInterest(this.getOutgoingInterest());
        }
        accountsToTransaction.get(account).add(transaction);
    }
    /**
     * remove a transaction from a desired account
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException such account does not exist
     * @throws TransactionDoesNotExistException such transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {

        if(!accountsToTransaction.containsKey(account)){
            throw new AccountDoesNotExistException("Account does not exist");
        }

        if(!accountsToTransaction.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException("Transaction does not exist");
        }

        accountsToTransaction.get(account).remove(transaction);
    }
    /**
     * varify the existence of a mentioned transaction in an existing account
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @return true if found and false if not found
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        return accountsToTransaction.get(account) != null && accountsToTransaction.get(account).contains(transaction);
    }

    /**
     * Getter for the current account balance
     * @param account the selected account
     * @return total account balance
     */
    @Override
    public double getAccountBalance(String account) {
        double total = 0;
        if(accountsToTransaction.containsKey(account)) {
            for (Transaction check_amount : accountsToTransaction.get(account)) {
                if (check_amount instanceof Transfer) {
                    if ((((Transfer) check_amount).getSender()).equals(account)) {
                        total -= check_amount.calculate();
                    }
                    if ((((Transfer) check_amount).getRecipient()).equals(account)) {
                        total += check_amount.calculate();
                    }
                }
                if(check_amount instanceof Payment){
                    total += check_amount.calculate();
                }
            }
        }
        return total;
    }

    /**
     * Getter for a list of transactions from selected account
     * @param account the selected account
     * @return the list of binded transactions
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        if(accountsToTransaction.containsKey(account)) {
            return accountsToTransaction.get(account);
        }
        return null;
    }

    /**
     * arrange the compared value of calculate() method from each transaction of an account either in ascending/ descending order
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return a sorted list of related transactions
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {
        List<Transaction> result = accountsToTransaction.get(account);
        result.sort(new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return (int)(o1.calculate() - o2.calculate());
            }
        });
        if(!asc){
            Collections.reverse(result);
        }
        return result;
    }

    /**
     * Group all positive or negative value of calculate() method from transactions in an account
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return an ArrayList plus if positive is true or minus if positive is false
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        List<Transaction> plus = new ArrayList<>();
        List<Transaction> minus = new ArrayList<>();

        for (Transaction check: accountsToTransaction.get(account)){
            if(check.calculate()>=0){
                plus.add(check);
            }
            if(check.calculate()<0){
                minus.add(check);
            }
        }
        return (positive) ? plus:minus;
    }

    public void deleteAccount(String account){

    }

    public List<String> getAllAccounts(){
        return null;
    }
}





