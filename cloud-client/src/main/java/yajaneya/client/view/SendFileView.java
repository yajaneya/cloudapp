package yajaneya.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import yajaneya.client.controller.ExplController;

import java.io.File;
import java.io.IOException;

public class SendFileView {

    public File openwin(String pathCloud) {

        File file = null;
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/explWindow.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showOpenDialog(stage);
            stage.setTitle("Выбор файла");
            ExplController controller = loader.getController();
            controller.sendFile = file;
            controller.fileNameLabel.setText(file.getPath());
            controller.pathCloud = pathCloud;


            stage.setScene(new Scene(root, 450, 100));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return file;

    }
}
