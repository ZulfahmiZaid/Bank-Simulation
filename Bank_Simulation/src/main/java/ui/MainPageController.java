package ui;

import bank.PrivateBank;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    @FXML
    private ListView<String> allAccountsList;
    @FXML
    private Button addAccount;
    @FXML
    private ContextMenu options;
    @FXML
    private MenuItem select;
    @FXML
    private MenuItem delete;

    Singleton nameData = Singleton.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            PrivateBank myBank = new PrivateBank("myBank", 0.5, 0.5);
            allAccountsList.getItems().addAll(myBank.getAllAccounts());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void addNewAccount(){
        TextInputDialog newAccount = new TextInputDialog();
        newAccount.setTitle("Account registration");
        newAccount.setHeaderText("Welcome to our Bank!");
        newAccount.setContentText("Account name:");

        Optional<String> result = newAccount.showAndWait();
        if (result.isPresent()){
            try{
                PrivateBank myBank = new PrivateBank("myBank", 0.5, 0.5);
                myBank.createAccount(result.get());
                allAccountsList.getItems().add(result.get());
                newAccount.close();
            }catch (Exception e){
                Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
                exceptionAlert.setTitle("Exception Dialog");
                exceptionAlert.setHeaderText(null);
                exceptionAlert.setContentText(e.getMessage());
                exceptionAlert.showAndWait();
            }
        }
    }

    public void InspectAccount(){
        if(allAccountsList.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("no Account selected");
            alert.showAndWait();
        }
        else{
            try {
                String selected = allAccountsList.getSelectionModel().getSelectedItem();
                nameData.setName(selected);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                        .getResource("accountview.fxml"));

                Parent root = (Parent) fxmlLoader.load();
                Stage stage = (Stage) addAccount.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
            catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public void PopUpConfirmation(){
        if(allAccountsList.getSelectionModel().getSelectedItems().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("no Account selected");

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
                    PrivateBank myBank = new PrivateBank("myBank", 0.5, 0.5);
                    String to_delete = allAccountsList.getSelectionModel().getSelectedItem();
                    myBank.deleteAccount(to_delete);
                    allAccountsList.getItems().remove(to_delete);
                }catch (Exception e){
                    Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
                    exceptionAlert.setTitle("Exception Dialog");
                    exceptionAlert.setHeaderText(null);
                    exceptionAlert.setContentText(e.getMessage());
                    exceptionAlert.showAndWait();
                }
            }
        }
    }
}
