package yajaneya.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yajaneya.client.controller.MainController;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/mainWindow.fxml"));
        Parent parent = loader.load();
        primaryStage.setScene(new Scene(parent));
        primaryStage.setTitle("Облачное хранилище");
        primaryStage.setResizable(true);

        MainController controller = loader.getController();
        primaryStage.setOnCloseRequest((event) -> controller.shutdown());

        primaryStage.show();
    }
}
