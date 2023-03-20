package ui;

import bank.Payment;
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

public class PaymentController {
    @FXML
    private TextField input_date;
    @FXML
    private TextField input_amount;
    @FXML
    private TextField input_description;
    @FXML
    private TextField input_incoming;
    @FXML
    private TextField input_outgoing;
    @FXML
    private Button submit;
    @FXML
    private Button cancel;

    Singleton nameData = Singleton.getInstance();

    private void resetData(){
        input_date.setText(null);
        input_amount.setText(null);
        input_description.setText(null);
        input_incoming.setText(null);
        input_outgoing.setText(null);
    }
    public void UpdateAllTransaction (){
        if(input_date.getText().isEmpty() || input_amount.getText().isEmpty() || input_incoming.getText().isEmpty()
                || input_incoming.getText().isEmpty() || input_outgoing.getText().isEmpty()){
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
                double new_incoming = Double.parseDouble(input_incoming.getText());
                double new_outgoing = Double.parseDouble(input_outgoing.getText());

                PrivateBank myBank = new PrivateBank("myBank", 0.5, 0.5);
                Payment to_add = new Payment(new_date,new_amount,new_desc,new_incoming,new_outgoing);
                myBank.addTransaction(nameData.getName(), to_add);
                BackToAccountView();

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
