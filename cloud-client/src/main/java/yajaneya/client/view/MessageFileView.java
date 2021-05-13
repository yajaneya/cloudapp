package yajaneya.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yajaneya.client.controller.MessageController;

import java.io.IOException;

public class MessageFileView {

    public void openwin (String message) {

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/messWindow.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Message");
            MessageController controller = loader.getController();
            controller.messageLabel.setText(message);
            stage.setScene(new Scene(root, 600, 77));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
