package bank;
import bank.exceptions.*;
import com.google.gson.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.io.File;

/**
 * Eine Klasse, die eine private Bank darstellen
 */
public class PrivateBank implements Bank, Serializable {
    private String name; // Name der privaten Bank repräsentieren
    private double incomingInterest;
    private double outgoingInterest;
    private Map<String, List<Transaction>> accountsToTransaction = new HashMap<>();
    private String directoryName;

    /**
     * Konstruktor, die Attribute name, incomingInterest und outgoingInterest initialisieren
     * @param input_name initial Wert für name
     * @param input_In initial Wert für incomingInterest
     * @param input_Out initial Wert für outgoingInterest
     */
    public PrivateBank (String input_name, double input_In, double input_Out) throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        this.name = input_name;
        setIncomingInterest(input_In);
        setOutgoingInterest(input_Out);
        setDirectoryName("src/main/Data/Accounts");
    }
    /**
     * Konstruktor, die Attribute name, incomingInterest und outgoingInterest initialisieren
     * @param input_name initial Wert für name
     * @param input_In initial Wert für incomingInterest
     * @param input_Out initial Wert für outgoingInterest
     */
    public PrivateBank (String input_name, double input_In, double input_Out,String input_directory) throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        this.name = input_name;
        setIncomingInterest(input_In);
        setOutgoingInterest(input_Out);
        setDirectoryName(input_directory);
    }

    /**
     * Copy-Konstruktur, mit dem man das gleiche Objekt kopieren kann.
     * @param other zukopierendes Objekt
     */
    public PrivateBank (PrivateBank other) throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        setName(other.getName());
        setIncomingInterest(other.getIncomingInterest());
        setOutgoingInterest(other.getOutgoingInterest());
        setDirectoryName(other.getDirectoryName());
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
     *Getter für Atrribute directoryName
     * @return aktueller Wert für directoryName
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     *Setter für die Attribute directoryName
     * @param set_directoryName
     */
    public void setDirectoryName(String set_directoryName) throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        this.directoryName = set_directoryName;
        Path folder = Paths.get(set_directoryName);

        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        } else {
            readAccount();
            for (Map.Entry<String,List<Transaction>> existingAccount : accountsToTransaction.entrySet()) {
                writeAccount(existingAccount.getKey());
            }
        }
    }
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
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {

        if (accountsToTransaction.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exist");
        else {
            accountsToTransaction.put(account, new ArrayList<>());
            writeAccount(account);
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
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        if (accountsToTransaction.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exist");

        accountsToTransaction.put(account,new ArrayList<>());

        for (Transaction collect : transactions) {
            this.addTransaction(account, collect);
        }

        writeAccount(account);
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
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

        if (!accountsToTransaction.containsKey(account))
            throw new AccountDoesNotExistException("Account does not exist");

        if (accountsToTransaction.get(account).contains(transaction))
            throw new TransactionAlreadyExistException("Transaction already exist");

        if (transaction instanceof Payment) {
            Payment copy_check = new Payment((Payment) transaction);
            copy_check.setIncomingInterest(this.getIncomingInterest());
            copy_check.setOutgoingInterest(this.getOutgoingInterest());

            if(accountsToTransaction.get(account).contains(copy_check))
                throw new TransactionAlreadyExistException("Transaction already exist");
            else {
                ((Payment) transaction).setIncomingInterest(this.getIncomingInterest());
                ((Payment) transaction).setOutgoingInterest(this.getOutgoingInterest());
            }
        }

        accountsToTransaction.get(account).add(transaction);
        writeAccount(account);
    }
    /**
     * remove a transaction from a desired account
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException such account does not exist
     * @throws TransactionDoesNotExistException such transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {

        if(!accountsToTransaction.containsKey(account)){
            throw new AccountDoesNotExistException("Account does not exist");
        }

        if(!accountsToTransaction.get(account).contains(transaction)){
            throw new TransactionDoesNotExistException("Transaction does not exist");
        }

        accountsToTransaction.get(account).remove(transaction);
        writeAccount(account);
    }
    /**
     * varify the existence of a mentioned transaction in an existing account
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @return true if found and false if not found
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {

        if(transaction instanceof  Payment) {
            try {
                Payment copy_check = new Payment((Payment) transaction);
                copy_check.setIncomingInterest(this.getIncomingInterest());
                copy_check.setOutgoingInterest(this.getOutgoingInterest());

                return accountsToTransaction.get(account) != null && accountsToTransaction.get(account).contains(copy_check);

            } catch (TransactionAttributeException e) {
                System.out.println(e.getMessage());
            }
        }

        return accountsToTransaction.get(account) != null && accountsToTransaction.get(account).contains(transaction);
    }

    /**
     * Getter for the current account balance
     * @param account the selected account
     * @return total account balance
     */
    @Override
    public double getAccountBalance(String account) {

        double total_amount = 0;
        for (Transaction collect : accountsToTransaction.get(account)){
            total_amount += collect.calculate();
        }
        return total_amount;
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
        List<Transaction> result = new ArrayList<>(accountsToTransaction.get(account));
        result.sort((o1, o2) -> (int)(o1.calculate() - o2.calculate()));

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

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException{
        if(!accountsToTransaction.containsKey(account)){
            throw new AccountDoesNotExistException("Account does not exists");
        }

        accountsToTransaction.remove(account);
        new File(directoryName + "/" + account + ".json").delete();
    }

    public List<String> getAllAccounts(){
        return new ArrayList<>(accountsToTransaction.keySet());
    }
    /**
     * Diese Methode soll alle vorhandenen Konten vom Dateisystem lesen
     * und im PrivateBank-Objekt(genauer: im Klassenattribut accountsToTransactions)
     * zur Verfügung stellen.
     * @throws IOException wenn der Pfad nicht existiert
     * @throws TransactionAlreadyExistException wenn Transaction schon existiert
     * @throws AccountAlreadyExistsException wenn das Konto schon existiert
     * @throws TransactionAttributeException wenn die Attribute falsch sind
     * @throws AccountDoesNotExistException wenn das Konto nicht existiert
     */
    private void readAccount() throws IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, AccountDoesNotExistException {
        File folder = new File(directoryName);
        File[] all_file = folder.listFiles();
        Gson customDeserializer = new GsonBuilder().registerTypeAdapter(Transaction.class, new Converter()).setPrettyPrinting().create();

        for (File take_file_element: all_file) {
            String account_name = take_file_element.getName().substring(0, take_file_element.getName().lastIndexOf('.'));
            List<Transaction> linked_transactions = new ArrayList<>();

            Scanner scanAll = new Scanner(take_file_element);
            StringBuilder line = new StringBuilder();
            while(scanAll.hasNext()){
                line.append(scanAll.nextLine());
            }

            JsonArray jsonArray = (JsonArray) new JsonParser().parse(line.toString());

            for (JsonElement travel : jsonArray) {
                linked_transactions.add(customDeserializer.fromJson(travel, Transaction.class));
            }

            if(!accountsToTransaction.containsKey(account_name)) {
                createAccount(account_name, linked_transactions);
            }
            scanAll.close();
        }

    }

    /**
     * Diese Methode soll das angegebene Konto im
     * Dateisystem persistieren (serialisieren und anschließend speichern)
     * @param new_account zuschreibendes Konto
     * @throws IOException wenn der Pfad nicht existiert
     */
    private void writeAccount(String new_account) throws IOException{
        List <String> jsonObjects = new ArrayList<>();
        File accountFile = new File(directoryName + "/" + new_account + ".json");
        FileWriter insert = new FileWriter(accountFile);

        for (Transaction take_array_element: accountsToTransaction.get(new_account)) {
            Gson customSerializer = new GsonBuilder().registerTypeAdapter(take_array_element.getClass(), new Converter()).setPrettyPrinting().create();
            String pass = customSerializer.toJson(take_array_element);
            jsonObjects.add(pass);
        }
        insert.write(jsonObjects.toString());
        insert.close();
    }
}
