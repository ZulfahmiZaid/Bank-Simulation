package ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Objects;

public class FxApplication extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("mainpage.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Bank");
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
