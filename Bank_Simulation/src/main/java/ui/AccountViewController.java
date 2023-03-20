package ui;

import bank.Payment;
import bank.PrivateBank;
import bank.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountViewController implements Initializable {

    @FXML
    private Label selectedAccount;
    @FXML
    private Label currentBalance;
    @FXML
    private Button back;
    @FXML
    private ContextMenu options;
    @FXML
    private MenuItem delete;
    @FXML
    private ListView transactionsList;
    @FXML
    private Button addTransaction;
    @FXML
    private RadioButton ascending, descending, positive, negative;

    Singleton nameData = Singleton.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            PrivateBank myBank = new PrivateBank("myBank", 0.5, 0.5);
            List<String> existingAccounts = new ArrayList(myBank.getAllAccounts());
            if(existingAccounts.contains(nameData.getName()))
                transactionsList.getItems().addAll(myBank.getTransactions(nameData.getName()));

            selectedAccount.setText(nameData.getName());
            currentBalance.setText(Double.toString(myBank.getAccountBalance(nameData.getName())));
        }catch (Exception e){
            System.out.println("error");
        }
    }
    public void BackToMain(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("mainpage.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = (Stage) back.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void sortTransactions(javafx.event.ActionEvent actionEvent) {
        try {
            PrivateBank myBank = new PrivateBank("myBank",0.5,0.5);
            transactionsList.getItems().clear();
            if (ascending.isSelected()) {
                transactionsList.getItems().addAll(myBank.getTransactionsSorted(nameData.getName(),true));
            } else if (descending.isSelected()) {
                transactionsList.getItems().addAll(myBank.getTransactionsSorted(nameData.getName(),false));
            } else if (positive.isSelected()) {
                transactionsList.getItems().addAll(myBank.getTransactionsByType(nameData.getName(),true));
            } else if (negative.isSelected()) {
                transactionsList.getItems().addAll(myBank.getTransactionsByType(nameData.getName(),false));
            }
        }catch (Exception e){
            System.out.println("error");
        }
    }

    public void deleteTransaction(){
        if(transactionsList.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("no Transaction selected");

            alert.showAndWait();
        }
        else {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation Dialog");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you ok with this?");

            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK){
                try{
                    PrivateBank myBank = new PrivateBank("myBank",0.5,0.5);
                    String to_delete = transactionsList.getSelectionModel().getSelectedItems().toString().replaceAll("[^a-zA-Z0-9%]","");

                    for (Transaction search:myBank.getTransactions(nameData.getName())) {
                        if(search.toString().replaceAll("[^a-zA-Z0-9%]","").equals(to_delete)) {
                            myBank.removeTransaction(nameData.getName(), search);
                            break;
                        }
                    }

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                            .getResource("accountview.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = (Stage) back.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void addNewTransaction (){

        List<String> transactionType = new ArrayList<>();
        transactionType.add("Payment");
        transactionType.add("Transfer");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("--Select--", transactionType);
        dialog.setTitle("New Transactions");
        dialog.setHeaderText("Choose your Transaction type");
        dialog.setContentText("Transaction type :");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            if(result.get().equals("Payment")){
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                            .getResource("paymentview.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = (Stage) back.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            } else if (result.get().equals("Transfer")) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                            .getResource("transferview.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = (Stage) back.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch(IOException e){
                    System.out.println(e.getMessage());
                }
            } else if (result.get().equals("--Select--")) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText(null);
                alert.setContentText("no type selected");
                alert.showAndWait();
            }
        }
    }
}
