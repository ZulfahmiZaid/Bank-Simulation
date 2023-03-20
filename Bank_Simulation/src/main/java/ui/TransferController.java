package ui;

import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.PrivateBank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TransferController {
    @FXML
    private TextField input_date;
    @FXML
    private TextField input_amount;
    @FXML
    private TextField input_description;
    @FXML
    private TextField input_sender;
    @FXML
    private TextField input_recipient;
    @FXML
    private Button submit;
    @FXML
    private Button cancel;

    Singleton nameData = Singleton.getInstance();

    private void resetData(){
        input_date.setText(null);
        input_amount.setText(null);
        input_description.setText(null);
        input_sender.setText(null);
        input_recipient.setText(null);
    }
    public void UpdateAllTransactions (){
        if(input_date.getText().isEmpty() || input_amount.getText().isEmpty() || input_sender.getText().isEmpty()
                || input_description.getText().isEmpty() || input_recipient.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("insufficient Input!");
            alert.showAndWait();
            resetData();
        }
        else{

            try{
                String new_date = input_date.getText();
                double new_amount = Double.parseDouble(input_amount.getText());
                String new_desc = input_description.getText();
                String new_sender = input_sender.getText();
                String new_recipient = input_recipient.getText();

                PrivateBank myBank = new PrivateBank("myBank",0.5,0.5);

                if(new_recipient.equals(nameData.getName())){
                    IncomingTransfer to_add_IT = new IncomingTransfer(new_date,new_amount,new_desc,new_sender,new_recipient);
                    myBank.addTransaction(nameData.getName(),to_add_IT);
                    BackToAccountView();
                } else if (new_sender.equals(nameData.getName())) {
                    OutgoingTransfer to_add_OT = new OutgoingTransfer(new_date,new_amount,new_desc,new_sender,new_recipient);
                    myBank.addTransaction(nameData.getName(),to_add_OT);
                    BackToAccountView();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Your must be either the Sender or Recipient!");
                    alert.showAndWait();
                    resetData();
                }

            }catch(Exception e){
                Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
                exceptionAlert.setTitle("Exception Dialog");
                exceptionAlert.setHeaderText(null);

                if(e.getClass().equals(NumberFormatException.class))
                    exceptionAlert.setContentText("invalid input");
                else
                    exceptionAlert.setContentText(e.getMessage());

                exceptionAlert.showAndWait();
                resetData();
            }
        }

    }

    public void BackToAccountView (){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("accountview.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = (Stage) cancel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
